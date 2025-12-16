package com.gabriel.pos_system.application.dto.auth;

import lombok.Data;

@Data
public class VerifyOtpRequestDto {
    private String email; // Necesitamos el email porque no hay sesión
    private String otp;
}
