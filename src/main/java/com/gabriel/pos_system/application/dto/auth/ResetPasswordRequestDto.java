package com.gabriel.pos_system.application.dto.auth;

import lombok.Data;

@Data
public class ResetPasswordRequestDto {
    private String email;
    private String otp; // Requerimos el OTP de nuevo para validar antes de cambiar (doble check de
                        // seguridad)
    private String newPassword;
    private String confirmPassword;
}
