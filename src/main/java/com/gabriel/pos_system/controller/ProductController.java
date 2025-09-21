package com.gabriel.pos_system.controller;

import java.io.IOException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gabriel.pos_system.dto.ProductDto;
import com.gabriel.pos_system.model.Product;
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
    public String showProductsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchTerm,
            Model model) {

        // 1. Cargar la lista paginada de productos para la tabla
        Page<Product> productsPage = productService.findPaginated(page, size, searchTerm);
        model.addAttribute("productsPage", productsPage);

        // 2. Devolver los parámetros para mantener el estado de los filtros
        model.addAttribute("selectedSize", size);
        model.addAttribute("searchTermValue", searchTerm);

        // 3. Preparar DTO para el formulario de "Agregar" si no viene de una
        // redirección con error
        if (!model.containsAttribute("productDto")) {
            model.addAttribute("productDto", new ProductDto());
        }

        // 4. Cargar todas las listas de catálogos para los dropdowns del modal (esto no
        // cambia)
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
        try {
            productService.saveProduct(dto, file);
            redirectAttributes.addFlashAttribute("successMessage", "¡Producto guardado exitosamente!");
        } catch (DataIntegrityViolationException e) {
            // Si atrapamos la excepción de duplicado...
            // 1. Añadimos el mensaje de error para mostrarlo en la vista.
            redirectAttributes.addFlashAttribute("barcodeError", e.getMessage());
            // 2. Devolvemos los datos que el usuario ya había ingresado para que no los
            // pierda.
            redirectAttributes.addFlashAttribute("productDto", dto);
        } catch (IOException e) {
            // Manejo de otros posibles errores
            redirectAttributes.addFlashAttribute("errorMessage", "Error al procesar la imagen.");
        }

        // Siempre redirigimos a la página de productos.
        return "redirect:/products";
    }
}
