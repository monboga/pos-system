package com.gabriel.pos_system.application.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {
    private String token;
    private String message;
    // Aquí podrías agregar más info útil para el frontend, como el nombre del
    // usuario o rol
    // private String username;
    // private String role;
}
