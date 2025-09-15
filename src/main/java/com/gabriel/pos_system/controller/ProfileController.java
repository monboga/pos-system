package com.gabriel.pos_system.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gabriel.pos_system.model.User;
import com.gabriel.pos_system.service.UserService;

@Controller
public class ProfileController {
    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    // Muestra la página de perfil
    @GetMapping("/profile")
    public String showProfilePage() {
        return "profile";
    }

    // Procesa el cambio de contraseña
    @PostMapping("/profile/change-password")
    public String changePassword(@AuthenticationPrincipal User user,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("passwordError", "Las contraseñas no coinciden.");
            return "redirect:/profile";
        }

        userService.updatePassword(user, newPassword);
        redirectAttributes.addFlashAttribute("passwordSuccess", "Contraseña actualizada correctamente.");

        return "redirect:/profile";
    }
}
