package com.gabriel.pos_system.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.gabriel.pos_system.dto.BusinessDto;
import com.gabriel.pos_system.model.Business;

public interface BusinessService {
    Business getBusinessData();

    void saveBusinessData(BusinessDto dto, MultipartFile logoFile) throws IOException;
}
