package com.gabriel.pos_system.application.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.gabriel.pos_system.application.dto.auth.UserDto;
import com.gabriel.pos_system.domain.model.User;

public interface UserService {
    void saveUser(UserDto userDto, MultipartFile photoFile) throws IOException;

    User findUserByEmail(String email);

    List<User> findAllUsers();

    Page<User> findPaginated(int page, int size, String searchName);

    void updatePassword(User user, String newPassword);

    // Genera un código, lo guarda en BD y envía el correo
    void generateAndSendOtp(String email);

    // Verifica si el código es válido y no ha expirado
    boolean verifyOtp(String email, String otp);

    // Restablece la contraseña (usando el OTP como doble verificación)
    void resetPassword(String email, String otp, String newPassword);
}
