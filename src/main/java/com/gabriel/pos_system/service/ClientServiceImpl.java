package com.gabriel.pos_system.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gabriel.pos_system.dto.ClientDto;
import com.gabriel.pos_system.model.Client;
import com.gabriel.pos_system.model.RegimenFiscal;
import com.gabriel.pos_system.repository.ClientRepository;
import com.gabriel.pos_system.repository.RegimenFiscalRepository;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final RegimenFiscalRepository regimenFiscalRepository;

    public ClientServiceImpl(ClientRepository clientRepository, RegimenFiscalRepository regimenFiscalRepository) {
        this.clientRepository = clientRepository;
        this.regimenFiscalRepository = regimenFiscalRepository;
    }

    @Override
    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public void saveClient(ClientDto dto) {
        Client client;
        if (dto.getId() != null) {
            client = clientRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        } else {
            client = new Client();
        }

        client.setRfc(dto.getRfc());
        client.setNombre(dto.getNombre());
        client.setCorreo(dto.getCorreo());
        client.setActivo(dto.getActivo());

        RegimenFiscal regimen = regimenFiscalRepository.findById(dto.getRegimenFiscalId())
                .orElseThrow(() -> new RuntimeException("Régimen Fiscal no encontrado"));
        client.setRegimenFiscal(regimen);

        clientRepository.save(client);
    }

}
