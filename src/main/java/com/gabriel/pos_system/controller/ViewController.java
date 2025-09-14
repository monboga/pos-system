package com.gabriel.pos_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping({ "/login", "/" })
    public String showLogin() {
        return "login";
    }

    @GetMapping("/forgot-password")
    public String showForgotPassword() {
        return "forgot-password";
    }

    @GetMapping("/index")
    public String showHome() {
        return "index";
    }

    @GetMapping("/users")
    public String showUserPage() {
        return "users";
    }

    @GetMapping("/business")
    public String showBusinessPage() {
        return "business";
    }
}
