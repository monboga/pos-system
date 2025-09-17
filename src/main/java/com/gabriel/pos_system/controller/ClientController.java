package com.gabriel.pos_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gabriel.pos_system.dto.ClientDto;
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
    public String showClientsPage(Model model) {
        model.addAttribute("clients", clientService.findAllClients());
        model.addAttribute("allRegimenes", regimenFiscalRepository.findAll());
        model.addAttribute("clientDto", new ClientDto());
        return "clients";
    }

    @PostMapping("/clients/save")
    public String saveClient(@ModelAttribute("clientDto") ClientDto clientDto, RedirectAttributes redirectAttributes) {
        clientService.saveClient(clientDto);
        redirectAttributes.addFlashAttribute("successMessage", "¡Cliente guardado exitosamente!");
        return "redirect:/clients";
    }
}
