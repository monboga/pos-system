package com.gabriel.pos_system.service;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.gabriel.pos_system.dto.ProductDto;
import com.gabriel.pos_system.model.Product;

public interface ProductService {
    // List<Product> findAllProducts();

    Page<Product> findPaginated(int page, int size, String searchTerm);

    void saveProduct(ProductDto dto, MultipartFile file) throws IOException;
}
