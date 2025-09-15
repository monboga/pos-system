package com.gabriel.pos_system.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.gabriel.pos_system.dto.UserDto;
import com.gabriel.pos_system.model.Role;
import com.gabriel.pos_system.repository.RoleRepository;
import com.gabriel.pos_system.service.UserService;

@Controller
public class UserController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    // Muestra la página con la lista de usuarios
    @GetMapping("/users")
    public String showUsersPage(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        // Pasamos un objeto DTO vacío para el formulario de "Agregar"
        model.addAttribute("userDto", new UserDto());
        // Pasamos la lista de roles para poblar el dropdown
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("allRoles", roles);
        return "users";
    }

    // Procesa el formulario para guardar/actualizar un usuario
    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute("userDto") UserDto userDto,
            @RequestParam("photoFile") MultipartFile photoFile) throws IOException {
        userService.saveUser(userDto, photoFile);
        return "redirect:/users";
    }
}
