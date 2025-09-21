package com.gabriel.pos_system.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gabriel.pos_system.dto.ClientDto;
import com.gabriel.pos_system.model.Client;
import com.gabriel.pos_system.repository.RegimenFiscalRepository;
import com.gabriel.pos_system.service.ClientService;

@Controller
public class ClientController {
    private final ClientService clientService;
    private final RegimenFiscalRepository regimenFiscalRepository;

    public ClientController(ClientService clientService, RegimenFiscalRepository regimenFiscalRepository) {
        this.clientService = clientService;
        this.regimenFiscalRepository = regimenFiscalRepository;
    }

    @GetMapping("/clients")
    public String showClientsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchTerm,
            Model model) {

        // 1. Obtener la página de clientes desde el servicio
        Page<Client> clientsPage = clientService.findPaginated(page, size, searchTerm);
        model.addAttribute("clientsPage", clientsPage);

        // 2. Pasar parámetros de vuelta a la vista para mantener el estado
        model.addAttribute("selectedSize", size);
        model.addAttribute("searchTermValue", searchTerm);

        // 3. Mantener los objetos necesarios para el modal
        model.addAttribute("allRegimenes", regimenFiscalRepository.findAll());
        if (!model.containsAttribute("clientDto")) {
            model.addAttribute("clientDto", new ClientDto());
        }

        return "clients";
    }

    @PostMapping("/clients/save")
    public String saveClient(@ModelAttribute("clientDto") ClientDto clientDto, RedirectAttributes redirectAttributes) {
        clientService.saveClient(clientDto);
        redirectAttributes.addFlashAttribute("successMessage", "¡Cliente guardado exitosamente!");
        return "redirect:/clients";
    }
}
