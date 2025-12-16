package com.gabriel.pos_system.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.gabriel.pos_system.application.dto.auth.UserDto;
import com.gabriel.pos_system.application.service.UserService;
import com.gabriel.pos_system.domain.model.Role;
import com.gabriel.pos_system.domain.model.User;
import com.gabriel.pos_system.infrastructure.persistence.RoleRepository;

@Controller
public class UserController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    // Muestra la página con la lista de usuarios PAGINADA Y FILTRABLE
    @GetMapping("/users")
    public String showUsersPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchName,
            Model model) {

        // 1. Obtenemos la página de usuarios desde el servicio con una sola llamada
        Page<User> usersPage = userService.findPaginated(page, size, searchName);
        model.addAttribute("usersPage", usersPage);

        // 2. Pasamos los parámetros de vuelta a la vista para mantener el estado
        model.addAttribute("selectedSize", size);
        model.addAttribute("searchNameValue", searchName);

        // 3. Mantenemos los objetos necesarios para el modal de "Agregar Usuario"
        if (!model.containsAttribute("userDto")) {
            model.addAttribute("userDto", new UserDto());
        }
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
