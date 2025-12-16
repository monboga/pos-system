package com.gabriel.pos_system.application.dto.auth;

import lombok.Data;

@Data // Genera Getters, Setters, toString, etc.
public class LoginRequestDto {
    private String email;
    private String password;
}
