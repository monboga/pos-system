package com.gabriel.pos_system.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.gabriel.pos_system.dto.CartItemDto;
import com.gabriel.pos_system.dto.SaleDto;
import com.gabriel.pos_system.model.Client;
import com.gabriel.pos_system.model.Product;
import com.gabriel.pos_system.model.Sale;
import com.gabriel.pos_system.model.SaleDetail;
import com.gabriel.pos_system.model.User;
import com.gabriel.pos_system.repository.ClientRepository;
import com.gabriel.pos_system.repository.ProductRepository;
import com.gabriel.pos_system.repository.SaleRepository;

import jakarta.transaction.Transactional;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final AtomicLong saleCounter = new AtomicLong(0);

    public SaleServiceImpl(SaleRepository saleRepository, ClientRepository clientRepository,
            ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;

        // Al arrancar, inicializamos el contador con el último ID de venta de la BD

        long lastSaleId = saleRepository.findTopByOrderByIdDesc().map(Sale::getId).orElse(0L);
        this.saleCounter.set(lastSaleId);
    }

    @Override
    @Transactional
    public Sale createSale(SaleDto saleDto, User loggedInUser) {
        Client client = clientRepository.findByRfc(saleDto.getClientRfc())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con RFC: " + saleDto.getClientRfc()));

        // --- INICIO DE LA REFACTORIZACIÓN ---

        // 1. Primero, validamos y actualizamos el stock de todos los productos.
        List<Product> productsToUpdate = new ArrayList<>();

        for (CartItemDto item : saleDto.getItems()) {
            // Usamos nuestro nuevo método para buscar Y bloquear el producto.
            Product product = productRepository.findByIdForUpdate(item.getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + item.getId()));

            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + product.getDescripcion());
            }

            product.setStock(product.getStock() - item.getQuantity());
            productsToUpdate.add(product);
        }

        // Guardamos todos los productos actualizados de una vez.
        productRepository.saveAll(productsToUpdate);

        // A partir de aquí, la creación de la venta es igual, pero ya no hay riesgo de
        // deadlock.
        Sale newSale = new Sale();
        newSale.setNumeroVenta("V-" + String.format("%06d", saleCounter.incrementAndGet()));
        newSale.setClient(client);
        newSale.setUser(loggedInUser);
        newSale.setDocumentType("Ticket");

        Set<SaleDetail> details = new HashSet<>();
        double subtotal = 0.0;

        for (CartItemDto item : saleDto.getItems()) {
            Product product = productsToUpdate.stream()
                    .filter(p -> p.getId().equals(item.getId())).findFirst().get();

            SaleDetail detail = new SaleDetail();
            detail.setProduct(product);
            detail.setCantidad(item.getQuantity());
            detail.setPrecioUnitario(product.getPrecioUnitario());
            double totalProducto = product.getPrecioUnitario() * item.getQuantity();
            detail.setTotalProducto(totalProducto);
            detail.setSale(newSale);

            details.add(detail);
            subtotal += totalProducto;
        }

        double iva = subtotal * 0.16;
        double total = subtotal + iva;
        newSale.setSubtotal(subtotal);
        newSale.setIva(iva);
        newSale.setTotal(total);
        newSale.setDetails(details);

        return saleRepository.save(newSale);
        // --- FIN DE LA REFACTORIZACIÓN ---
    }
}
