package com.gabriel.pos_system.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.gabriel.pos_system.model.Business;
import com.gabriel.pos_system.model.User;
import com.gabriel.pos_system.service.BusinessService;

@ControllerAdvice
public class GlobalControllerAdvice {
    /**
     * Este método se ejecuta antes que cualquier método del controlador.
     * Su propósito es obtener el usuario actualmente autenticado y
     * añadirlo al modelo para que esté disponible en TODAS las vistas Thymeleaf.
     */
    private final BusinessService businessService;

    public GlobalControllerAdvice(BusinessService businessService) {
        this.businessService = businessService;
    }

    @ModelAttribute("loggedInUser")
    public User loggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null; // O un objeto de usuario anónimo si lo prefieres
    }

    @ModelAttribute("businessLogoExists")
    public boolean addBusinessLogoExistsAttribute() {
        Business business = businessService.getBusinessData();
        // Devuelve true si el objeto de negocio no es nulo Y el string del logo no es
        // nulo ni está vacío.
        return business != null && business.getLogo() != null && !business.getLogo().isEmpty();
    }
}
