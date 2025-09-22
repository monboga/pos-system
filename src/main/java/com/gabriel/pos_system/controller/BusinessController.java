package com.gabriel.pos_system.controller;

import java.io.IOException;
import java.util.Base64;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import com.gabriel.pos_system.repository.RegimenFiscalRepository;
import com.gabriel.pos_system.service.BusinessService;

@Controller
public class BusinessController {
    private final BusinessService businessService;
    private final RegimenFiscalRepository regimenFiscalRepository;

    public BusinessController(BusinessService businessService, RegimenFiscalRepository regimenFiscalRepository) {
        this.businessService = businessService;
        this.regimenFiscalRepository = regimenFiscalRepository;
    }

    @GetMapping("/business")
    public String showBusinessPage(Model model) {
        // Obtenemos la entidad desde la base de datos
        Business businessData = businessService.getBusinessData();

        // Pasamos la entidad directamente (o una nueva si no existe)
        model.addAttribute("businessData", businessData != null ? businessData : new Business());
        model.addAttribute("allRegimenes", regimenFiscalRepository.findAll());
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
    public ResponseEntity<byte[]> getLogo() {
        // 1. Obtenemos los datos del negocio
        Business business = businessService.getBusinessData();

        // 2. Verificamos si existe un negocio y si tiene un logo
        if (business != null && business.getLogo() != null && !business.getLogo().isEmpty()) {

            // 3. Tu logo está guardado como un Data URI ("data:image/png;base64,...")
            // Necesitamos separar el prefijo de los datos Base64.
            String[] parts = business.getLogo().split(",");
            String imageTypePart = parts[0]; // "data:image/png;base64"
            String base64Data = parts[1];

            // Extraemos el tipo de contenido (ej. "image/png")
            String contentType = imageTypePart.split(":")[1].split(";")[0];

            // 4. Decodificamos el String Base64 a un array de bytes
            byte[] logoBytes = Base64.getDecoder().decode(base64Data);

            // 5. Preparamos las cabeceras de la respuesta HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentLength(logoBytes.length);

            // 6. Devolvemos la imagen con una respuesta 200 OK
            return new ResponseEntity<>(logoBytes, headers, HttpStatus.OK);
        } else {
            // 7. Si no hay logo, devolvemos una respuesta 404 Not Found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
