package com.gabriel.pos_system.application.dto.profile;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateProfileRequestDto {
    @NotBlank(message = "El nombre es obligatorio")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;

    private String phoneNumber;

    // Nota: No incluimos email, rol ni estado.
    // El usuario no debe poder cambiar esos datos por sí mismo.
}