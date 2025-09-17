package com.gabriel.pos_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gabriel.pos_system.repository.CategoryRepository;
import com.gabriel.pos_system.repository.ClientRepository;
import com.gabriel.pos_system.repository.ProductRepository;

@Controller
public class POSController {
    private final ClientRepository clientRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public POSController(ClientRepository clientRepository, CategoryRepository categoryRepository,
            ProductRepository productRepository) {
        this.clientRepository = clientRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/pos")
    public String showPOSPage(Model model) {
        // 1. Obtenemos todos los clientes y los añadimos al modelo
        model.addAttribute("clients", clientRepository.findAll());

        // 2. Obtenemos todas las categorías y las añadimos al modelo
        model.addAttribute("categories", categoryRepository.findAll());

        // 3. Obtenemos todos los productos y los añadimos al modelo
        model.addAttribute("products", productRepository.findAll());

        return "pos";
    }
}
