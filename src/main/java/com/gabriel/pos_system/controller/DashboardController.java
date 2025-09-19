package com.gabriel.pos_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gabriel.pos_system.repository.CategoryRepository;
import com.gabriel.pos_system.repository.ProductRepository;

@Controller
public class DashboardController {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    // Inyectamos los repositorios que necesitamos para contar los registros.
    public DashboardController(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/dashboard")
    public String showDashboardPage(Model model) {
        // 1. Contamos el total de productos en la base de datos.
        // El método .count() es muy eficiente y nos lo provee JpaRepository.
        long totalProducts = productRepository.count();

        // 2. Contamos el total de categorías en la base de datos.
        long totalCategories = categoryRepository.count();

        // 3. Añadimos estos valores al modelo para que Thymeleaf pueda usarlos.
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalCategories", totalCategories);

        // Por ahora, mantenemos estos valores estáticos.
        model.addAttribute("totalSales", "1,250");
        model.addAttribute("totalRevenue", "$84,320");

        // 4. Devolvemos el nombre de la plantilla HTML.
        return "dashboard";
    }
}
