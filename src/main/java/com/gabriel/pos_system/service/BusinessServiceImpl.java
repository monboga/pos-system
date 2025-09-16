package com.gabriel.pos_system.service;

import java.io.IOException;
import java.util.Base64;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gabriel.pos_system.dto.BusinessDto;
import com.gabriel.pos_system.model.Business;
import com.gabriel.pos_system.repository.BusinessRepository;

@Service
public class BusinessServiceImpl implements BusinessService {
    private final BusinessRepository businessRepository;

    public BusinessServiceImpl(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    @Override
    public Business getBusinessData() {
        // Obtenemos la primera (y única) configuración del negocio.
        return businessRepository.findAll().stream().findFirst().orElse(null);
    }

    @Override
    public void saveBusinessData(BusinessDto dto, MultipartFile logoFile) throws IOException {
        Business business = businessRepository.findAll().stream().findFirst().orElse(new Business());

        business.setRfc(dto.getRfc());
        business.setRazonSocial(dto.getRazonSocial());
        business.setEmail(dto.getEmail());
        business.setAddress(dto.getAddress());
        business.setPhoneNumber(dto.getPhoneNumber());
        business.setPostalCode(dto.getPostalCode());
        business.setCurrencyType(dto.getCurrencyType());

        if (logoFile != null && !logoFile.isEmpty()) {
            String logoBase64 = Base64.getEncoder().encodeToString(logoFile.getBytes());
            business.setLogo("data:" + logoFile.getContentType() + ";base64," + logoBase64);
        }

        businessRepository.save(business);
    }
}
