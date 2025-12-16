package com.gabriel.pos_system.infrastructure.adapter.input;

import com.gabriel.pos_system.application.dto.user.CreateUserRequestDto;
import com.gabriel.pos_system.application.dto.user.UpdateUserRequestDto;
import com.gabriel.pos_system.application.dto.user.UserResponseDto;
import com.gabriel.pos_system.application.service.UserService;
import com.gabriel.pos_system.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Operaciones de gestión de usuarios")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Listar usuarios paginados")
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {
        Page<User> usersPage = userService.findPaginated(page, size, search);

        // Mapeo de Entidad a ResponseDTO (Patrón Mapper simple)
        Page<UserResponseDto> responsePage = usersPage.map(this::mapToDto);

        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(mapToDto(user));
    }

    // Nota: Usamos consume MULTIPART_FORM_DATA para permitir subir foto y datos
    // JSON a la vez
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Crear nuevo usuario")
    public ResponseEntity<UserResponseDto> createUser(
            @RequestPart("user") @Valid CreateUserRequestDto request,
            @RequestPart(value = "photo", required = false) MultipartFile photo) throws IOException {
        User createdUser = userService.createUser(request, photo);
        return ResponseEntity.ok(mapToDto(createdUser));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Actualizar usuario")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @RequestPart("user") UpdateUserRequestDto request,
            @RequestPart(value = "photo", required = false) MultipartFile photo) throws IOException {
        User updatedUser = userService.updateUser(id, request, photo);
        return ResponseEntity.ok(mapToDto(updatedUser));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // --- Mapper Auxiliar (Podría moverse a una clase UserMapper) ---
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
                .photoUrl(user.getPhoto()) // Cuidado si la foto es base64 muy grande
                .build();
    }
}