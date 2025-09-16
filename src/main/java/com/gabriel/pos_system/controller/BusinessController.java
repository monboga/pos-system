package com.gabriel.pos_system.controller;

import java.io.IOException;
import java.util.Base64;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gabriel.pos_system.dto.BusinessDto;
import com.gabriel.pos_system.model.Business;
import com.gabriel.pos_system.service.BusinessService;

@Controller
public class BusinessController {
    private final BusinessService businessService;

    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @GetMapping("/business")
    public String showBusinessPage(Model model) {
        Business businessData = businessService.getBusinessData();
        // Pasamos los datos existentes o un objeto nuevo a la vista
        model.addAttribute("businessData", businessData != null ? businessData : new Business());
        return "business";
    }

    @PostMapping("/business/save")
    public String saveBusinessData(@ModelAttribute BusinessDto dto,
            @RequestParam("logoFile") MultipartFile logoFile,
            RedirectAttributes redirectAttributes) throws IOException {
        businessService.saveBusinessData(dto, logoFile);
        redirectAttributes.addFlashAttribute("successMessage", "¡Datos del negocio guardados exitosamente!");
        return "redirect:/business";
    }

    @GetMapping("/business/logo")
    public ResponseEntity<byte[]> getBusinessLogo() {
        // 1. Obtenemos los datos del negocio.
        Business business = businessService.getBusinessData();

        // 2. Verificamos si existe un logo y si es una cadena Base64 válida.
        if (business != null && business.getLogo() != null && business.getLogo().startsWith("data:image")) {
            // 3. Separamos el contenido Base64 del prefijo.
            String base64Image = business.getLogo().split(",")[1];
            // 4. Decodificamos el texto a su arreglo de bytes original.
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            // 5. Devolvemos los bytes de la imagen con el tipo de contenido correcto.
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes);
        }

        // 6. Si no hay logo, devolvemos un error 404.
        return ResponseEntity.notFound().build();
    }
}
