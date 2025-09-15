package com.gabriel.pos_system.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.gabriel.pos_system.model.User;

@ControllerAdvice
public class GlobalControllerAdvice {
    /**
     * Este método se ejecuta antes que cualquier método del controlador.
     * Su propósito es obtener el usuario actualmente autenticado y
     * añadirlo al modelo para que esté disponible en TODAS las vistas Thymeleaf.
     */
    @ModelAttribute("loggedInUser")
    public User loggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null; // O un objeto de usuario anónimo si lo prefieres
    }
}
