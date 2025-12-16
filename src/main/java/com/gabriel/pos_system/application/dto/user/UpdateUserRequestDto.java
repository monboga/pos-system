package com.gabriel.pos_system.application.dto.user;

import lombok.Data;

@Data
public class UpdateUserRequestDto {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Integer status;
    private Long roleId;
    // Nota: No incluimos email (identidad) ni password aquí por seguridad
}