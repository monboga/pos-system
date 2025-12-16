package com.gabriel.pos_system.application.service;

import com.gabriel.pos_system.application.dto.profile.ChangePasswordRequestDto;
import com.gabriel.pos_system.application.dto.profile.UpdateProfileRequestDto;
import com.gabriel.pos_system.application.dto.user.CreateUserRequestDto;
import com.gabriel.pos_system.application.dto.user.UpdateUserRequestDto;
import com.gabriel.pos_system.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    // --- MÉTODOS CRUD (Gestión de Usuarios) ---

    // Obtener página de usuarios (con búsqueda opcional)
    Page<User> findPaginated(int page, int size, String searchName);

    // Obtener un usuario por su ID
    User getUserById(Long id);

    // Crear un nuevo usuario (DTO específico de creación + Foto opcional)
    User createUser(CreateUserRequestDto request, MultipartFile photo) throws IOException;

    // Actualizar un usuario existente (DTO específico de actualización + Foto
    // opcional)
    User updateUser(Long id, UpdateUserRequestDto request, MultipartFile photo) throws IOException;

    // Eliminar (o desactivar) un usuario
    void deleteUser(Long id);

    // --- MÉTODOS DE SEGURIDAD / AUTH (Recuperación de Contraseña) ---

    // Generar OTP, guardarlo en BD y enviar correo
    void generateAndSendOtp(String email);

    // Verificar si un OTP es válido para un correo
    boolean verifyOtp(String email, String otp);

    // Restablecer la contraseña validando el OTP
    void resetPassword(String email, String otp, String newPassword);

    // --- MÉTODOS DE UTILIDAD / CONSULTA ---

    // Buscar usuario por email (usado internamente por la seguridad)
    User findUserByEmail(String email);

    // Listar todos (útil para reportes o desplegables, si se requiere)
    List<User> findAllUsers();

    // Actualizar datos del propio usuario (buscado por email)
    User updateProfile(String email, UpdateProfileRequestDto request);

    // Cambiar contraseña estando logueado (validando la actual)
    void changePassword(String email, ChangePasswordRequestDto request);
}