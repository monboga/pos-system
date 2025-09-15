package com.gabriel.pos_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gabriel.pos_system.repository.UserRepository;

@Controller
public class ViewController {

    // Inyectamos el repositorio para poder acceder a los datos de los usuarios.
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/forgot-password")
    public String showForgotPassword() {
        return "forgot-password";
    }

    @GetMapping({ "/", "/index" })
    public String showHome() {
        return "index";
    }

    @GetMapping("/dashboard")
    public String showDashboardPage() {
        return "dashboard";
    }

    @GetMapping("/users")
    public String showUserPage(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users";
    }

    @GetMapping("/business")
    public String showBusinessPage() {
        return "business";
    }

    @GetMapping("/clients")
    public String showClientsPage() {
        return "clients";
    }

    @GetMapping("/categories")
    public String showCategoriesPage() {
        return "categories";
    }

    @GetMapping("/products")
    public String showProductsPage() {
        return "products";
    }

    @GetMapping("/pos")
    public String showPosPage() {
        return "pos";
    }

    @GetMapping("/sales-history")
    public String showSalesHistoryPage() {
        return "sales-history";
    }

    @GetMapping("sales-report")
    public String showSalesReportPage() {
        return "sales-report";
    }

    @GetMapping("/profile")
    public String showProfilePage() {
        return "profile";
    }
}
