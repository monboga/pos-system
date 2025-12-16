package com.gabriel.pos_system.infrastructure.adapter.input;

import com.gabriel.pos_system.application.dto.profile.ChangePasswordRequestDto;
import com.gabriel.pos_system.application.dto.profile.UpdateProfileRequestDto;
import com.gabriel.pos_system.application.dto.user.UserResponseDto; // Reutilizamos el DTO de respuesta de usuario
import com.gabriel.pos_system.application.service.UserService;
import com.gabriel.pos_system.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Tag(name = "Perfil", description = "Operaciones del usuario sobre su propia cuenta")
@SecurityRequirement(name = "bearerAuth") // Indica en Swagger que requiere Auth
public class ProfileController {

    private final UserService userService;

    // Obtener mis datos (GET /api/profile)
    @GetMapping
    @Operation(summary = "Obtener mi información de perfil")
    public ResponseEntity<UserResponseDto> getMyProfile(Authentication authentication) {
        // authentication.getName() devuelve el email gracias a nuestra config de JWT
        String email = authentication.getName();

        // Usamos findUserByEmail que ya tenías en el servicio
        User user = userService.findUserByEmail(email);

        return ResponseEntity.ok(mapToDto(user));
    }

    // Actualizar mis datos (PUT /api/profile)
    @PutMapping
    @Operation(summary = "Actualizar mi información personal")
    public ResponseEntity<UserResponseDto> updateMyProfile(
            @RequestBody @Valid UpdateProfileRequestDto request,
            Authentication authentication) {
        String email = authentication.getName();
        User updatedUser = userService.updateProfile(email, request);
        return ResponseEntity.ok(mapToDto(updatedUser));
    }

    // Cambiar contraseña (PUT /api/profile/change-password)
    @PutMapping("/change-password")
    @Operation(summary = "Cambiar mi contraseña (requiere contraseña actual)")
    public ResponseEntity<String> changePassword(
            @RequestBody @Valid ChangePasswordRequestDto request,
            Authentication authentication) {
        String email = authentication.getName();
        try {
            userService.changePassword(email, request);
            return ResponseEntity.ok("Contraseña actualizada correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Mapper auxiliar (Idealmente mover a una clase Mapper separada)
    private UserResponseDto mapToDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .status(user.getStatus())
                .roleName(user.getRoles().isEmpty() ? "N/A" : user.getRoles().iterator().next().getFriendlyName())
                .creationDate(user.getCreationDate())
                .photoUrl(user.getPhoto())
                .build();
    }
}