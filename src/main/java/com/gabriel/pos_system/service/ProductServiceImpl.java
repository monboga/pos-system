package com.gabriel.pos_system.service;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gabriel.pos_system.dto.ProductDto;
import com.gabriel.pos_system.model.Category;
import com.gabriel.pos_system.model.ClaveProdServSat;
import com.gabriel.pos_system.model.ImpuestoSat;
import com.gabriel.pos_system.model.MedidaLocal;
import com.gabriel.pos_system.model.MedidaSat;
import com.gabriel.pos_system.model.ObjetoImpSat;
import com.gabriel.pos_system.model.Product;
import com.gabriel.pos_system.repository.CategoryRepository;
import com.gabriel.pos_system.repository.ClaveProdServSatRepository;
import com.gabriel.pos_system.repository.ImpuestoSatRepository;
import com.gabriel.pos_system.repository.MedidaLocalRepository;
import com.gabriel.pos_system.repository.MedidaSatRepository;
import com.gabriel.pos_system.repository.ObjetoImpSatRepository;
import com.gabriel.pos_system.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

        private final ProductRepository productRepository;
        private final CategoryRepository categoryRepository;
        private final MedidaLocalRepository medidaLocalRepository;
        private final MedidaSatRepository medidaSatRepository;
        private final ClaveProdServSatRepository claveProdServSatRepository;
        private final ObjetoImpSatRepository objetoImpSatRepository;
        private final ImpuestoSatRepository impuestoSatRepository;

        public ProductServiceImpl(ProductRepository productRepository,
                        CategoryRepository categoryRepository,
                        MedidaLocalRepository medidaLocalRepository,
                        MedidaSatRepository medidaSatRepository,
                        ClaveProdServSatRepository claveProdServSatRepository,
                        ObjetoImpSatRepository objetoImpSatRepository,
                        ImpuestoSatRepository impuestoSatRepository) {
                this.productRepository = productRepository;
                this.categoryRepository = categoryRepository;
                this.medidaLocalRepository = medidaLocalRepository;
                this.medidaSatRepository = medidaSatRepository;
                this.claveProdServSatRepository = claveProdServSatRepository;
                this.objetoImpSatRepository = objetoImpSatRepository;
                this.impuestoSatRepository = impuestoSatRepository;
        }

        // @Override
        // public List<Product> findAllProducts() {
        // return productRepository.findAll();
        // }

        @Override
        public void saveProduct(ProductDto dto, MultipartFile file) throws IOException {

                // 1. Buscamos si ya existe un producto con ese código de barras.
                Optional<Product> existingProduct = productRepository.findByCodigoDeBarra(dto.getCodigoDeBarra());

                // 2. Si existe, y estamos intentando crear uno nuevo (ID es nulo) O
                // si el producto encontrado tiene un ID diferente al que estamos editando,
                // entonces es un duplicado inválido.
                if (existingProduct.isPresent()
                                && (dto.getId() == null || !existingProduct.get().getId().equals(dto.getId()))) {
                        // 3. Lanzamos una excepción con un mensaje específico.
                        throw new DataIntegrityViolationException(
                                        "El código de barra '" + dto.getCodigoDeBarra() + "' ya existe.");
                }

                Product product;
                if (dto.getId() != null) {
                        product = productRepository.findById(dto.getId())
                                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                } else {
                        product = new Product();
                }

                // Mapear campos simples desde el DTO a la Entidad
                product.setCodigoDeBarra(dto.getCodigoDeBarra());
                product.setMarca(dto.getMarca());
                product.setDescripcion(dto.getDescripcion());
                product.setStock(dto.getStock());
                product.setPrecioUnitario(dto.getPrecioUnitario());
                product.setEstado(dto.getEstado());
                product.setDescuento(dto.getDescuento());

                // Mapear todas las relaciones buscando las entidades por ID
                Category category = categoryRepository.findById(dto.getCategoryId())
                                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
                product.setCategory(category);

                MedidaLocal medidaLocal = medidaLocalRepository.findById(dto.getMedidaLocalId())
                                .orElseThrow(() -> new RuntimeException("Medida Local no encontrada"));
                product.setMedidaLocal(medidaLocal);

                MedidaSat medidaSat = medidaSatRepository.findById(dto.getMedidaSatId())
                                .orElseThrow(() -> new RuntimeException("Medida SAT no encontrada"));
                product.setMedidaSat(medidaSat);

                ClaveProdServSat claveProdServSat = claveProdServSatRepository.findById(dto.getClaveProdServSatId())
                                .orElseThrow(() -> new RuntimeException("Clave Producto/Servicio SAT no encontrada"));
                product.setClaveProdServSat(claveProdServSat);

                ObjetoImpSat objetoImpSat = objetoImpSatRepository.findById(dto.getObjetoImpSatId())
                                .orElseThrow(() -> new RuntimeException("Objeto Impuesto SAT no encontrado"));
                product.setObjetoImpSat(objetoImpSat);

                ImpuestoSat impuestoSat = impuestoSatRepository.findById(dto.getImpuestoSatId())
                                .orElseThrow(() -> new RuntimeException("Impuesto SAT no encontrado"));
                product.setImpuestoSat(impuestoSat);

                // Procesar y guardar la imagen si se subió una nueva
                if (file != null && !file.isEmpty()) {
                        String imageBase64 = Base64.getEncoder().encodeToString(file.getBytes());
                        product.setImagen("data:" + file.getContentType() + ";base64," + imageBase64);
                }

                productRepository.save(product);
        }

        @Override
        public Page<Product> findPaginated(int page, int size, String searchTerm) {
                Pageable pageable = PageRequest.of(page, size);
                if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                        return productRepository.findBySearchTerm(searchTerm.trim(), pageable);
                } else {
                        return productRepository.findAll(pageable);
                }
        }
}
