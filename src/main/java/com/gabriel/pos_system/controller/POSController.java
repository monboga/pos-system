package com.gabriel.pos_system.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gabriel.pos_system.dto.SaleDto;
import com.gabriel.pos_system.model.Sale;
import com.gabriel.pos_system.model.User;
import com.gabriel.pos_system.repository.CategoryRepository;
import com.gabriel.pos_system.repository.ClientRepository;
import com.gabriel.pos_system.repository.ProductRepository;
import com.gabriel.pos_system.service.SaleService;

@Controller
public class POSController {
    private final ClientRepository clientRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final SaleService saleService;

    public POSController(ClientRepository clientRepository, CategoryRepository categoryRepository,
            ProductRepository productRepository, SaleService saleService) {
        this.clientRepository = clientRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.saleService = saleService;
    }

    @GetMapping("/pos")
    public String showPOSPage(Model model) {
        // 1. Obtenemos todos los clientes y los añadimos al modelo
        model.addAttribute("clients", clientRepository.findByActivo(1));

        // 2. Obtenemos todas las categorías y las añadimos al modelo
        model.addAttribute("categories", categoryRepository.findByEstado(1));

        // 3. Obtenemos todos los productos y los añadimos al modelo
        model.addAttribute("products", productRepository.findByEstadoAndCategoryEstado(1, 1));

        return "pos";
    }

    @PostMapping("/pos/create-sale")
    @ResponseBody
    public ResponseEntity<Map<String, String>> createSale(@RequestBody SaleDto saleDto,
            @AuthenticationPrincipal User user) {
        try {
            // Guardamos la venta y obtenemos el objeto guardado
            Sale newSale = saleService.createSale(saleDto, user);

            // Creamos una respuesta JSON con el mensaje y el número de venta
            Map<String, String> response = new HashMap<>();
            response.put("message", "Venta registrada exitosamente.");
            response.put("saleNumber", newSale.getNumeroVenta());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
