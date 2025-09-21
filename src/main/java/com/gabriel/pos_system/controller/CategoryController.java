package com.gabriel.pos_system.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gabriel.pos_system.dto.CategoryDto;
import com.gabriel.pos_system.model.Category;
import com.gabriel.pos_system.service.CategoryService;

@Controller
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public String showCategoriesPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchTerm,
            Model model) {

        Page<Category> categoriesPage = categoryService.findPaginated(page, size, searchTerm);
        model.addAttribute("categoriesPage", categoriesPage);
        model.addAttribute("selectedSize", size);
        model.addAttribute("searchTermValue", searchTerm);

        if (!model.containsAttribute("categoryDto")) {
            model.addAttribute("categoryDto", new CategoryDto());
        }

        return "categories";
    }

    @PostMapping("/categories/save")
    public String saveCategory(@ModelAttribute("categoryDto") CategoryDto categoryDto,
            RedirectAttributes redirectAttributes) {
        categoryService.saveCategory(categoryDto);
        redirectAttributes.addFlashAttribute("successMessage", "¡Categoría guardada exitosamente!");
        return "redirect:/categories";
    }
}
