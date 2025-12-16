package com.gabriel.pos_system.application.dto.user;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class UserResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Integer status;
    private String roleName; // Devolvemos el nombre del rol simplificado
    private LocalDateTime creationDate;
    private String photoUrl; // Si decides devolver la URL o base64
}