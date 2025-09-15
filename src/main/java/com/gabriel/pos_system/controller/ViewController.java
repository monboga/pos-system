package com.gabriel.pos_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

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
}
