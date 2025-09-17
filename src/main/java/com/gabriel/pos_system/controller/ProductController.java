package com.gabriel.pos_system.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gabriel.pos_system.dto.ProductDto;
import com.gabriel.pos_system.repository.CategoryRepository;
import com.gabriel.pos_system.repository.ClaveProdServSatRepository;
import com.gabriel.pos_system.repository.ImpuestoSatRepository;
import com.gabriel.pos_system.repository.MedidaLocalRepository;
import com.gabriel.pos_system.repository.MedidaSatRepository;
import com.gabriel.pos_system.repository.ObjetoImpSatRepository;
import com.gabriel.pos_system.service.ProductService;

@Controller
public class ProductController {
    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private final MedidaLocalRepository medidaLocalRepository;
    private final MedidaSatRepository medidaSatRepository;
    private final ClaveProdServSatRepository claveProdServSatRepository;
    private final ObjetoImpSatRepository objetoImpSatRepository;
    private final ImpuestoSatRepository impuestoSatRepository;

    public ProductController(ProductService productService,
            CategoryRepository categoryRepository,
            MedidaLocalRepository medidaLocalRepository,
            MedidaSatRepository medidaSatRepository,
            ClaveProdServSatRepository claveProdServSatRepository,
            ObjetoImpSatRepository objetoImpSatRepository,
            ImpuestoSatRepository impuestoSatRepository) {
        this.productService = productService;
        this.categoryRepository = categoryRepository;
        this.medidaLocalRepository = medidaLocalRepository;
        this.medidaSatRepository = medidaSatRepository;
        this.claveProdServSatRepository = claveProdServSatRepository;
        this.objetoImpSatRepository = objetoImpSatRepository;
        this.impuestoSatRepository = impuestoSatRepository;
    }

    @GetMapping("/products")
    public String showProductsPage(Model model) {
        // Cargar la lista de productos para la tabla
        model.addAttribute("products", productService.findAllProducts());
        // Preparar un DTO vacío para el formulario de "Agregar"
        model.addAttribute("productDto", new ProductDto());

        // Cargar todas las listas de catálogos para los dropdowns del modal
        model.addAttribute("allCategories", categoryRepository.findAll());
        model.addAttribute("allMedidasLocal", medidaLocalRepository.findAll());
        model.addAttribute("allMedidasSat", medidaSatRepository.findAll());
        model.addAttribute("allClavesProdServSat", claveProdServSatRepository.findAll());
        model.addAttribute("allObjetosImpSat", objetoImpSatRepository.findAll());
        model.addAttribute("allImpuestosSat", impuestoSatRepository.findAll());

        return "products";
    }

    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute("productDto") ProductDto dto,
            @RequestParam("productImage") MultipartFile file,
            RedirectAttributes redirectAttributes) throws IOException {
        productService.saveProduct(dto, file);
        redirectAttributes.addFlashAttribute("successMessage", "¡Producto guardado exitosamente!");
        return "redirect:/products";
    }
}
