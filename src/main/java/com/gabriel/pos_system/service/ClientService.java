package com.gabriel.pos_system.service;

import java.util.List;

import com.gabriel.pos_system.dto.ClientDto;
import com.gabriel.pos_system.model.Client;

public interface ClientService {

    List<Client> findAllClients();

    void saveClient(ClientDto clientDto);

}
