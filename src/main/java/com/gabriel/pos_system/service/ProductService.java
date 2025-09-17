package com.gabriel.pos_system.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.gabriel.pos_system.dto.ProductDto;
import com.gabriel.pos_system.model.Product;

public interface ProductService {
    List<Product> findAllProducts();

    void saveProduct(ProductDto dto, MultipartFile file) throws IOException;
}
