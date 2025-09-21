package com.gabriel.pos_system.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.gabriel.pos_system.dto.ClientDto;
import com.gabriel.pos_system.model.Client;

public interface ClientService {

    Page<Client> findPaginated(int page, int size, String searchTerm);

    void saveClient(ClientDto clientDto);

}
