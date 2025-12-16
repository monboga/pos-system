package com.gabriel.pos_system.application.service.impl;

import com.gabriel.pos_system.application.service.UserService;
import com.gabriel.pos_system.application.dto.auth.UserDto; // Asumiendo que moviste UserDto aquí o lo importas
import com.gabriel.pos_system.domain.model.Role;
import com.gabriel.pos_system.domain.model.User;
import com.gabriel.pos_system.infrastructure.notification.EmailService;
import com.gabriel.pos_system.infrastructure.persistence.RoleRepository;
import com.gabriel.pos_system.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor // Inyección de dependencias automática (Lombok)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public void saveUser(UserDto userDto, MultipartFile photoFile) throws IOException {
        User user;
        if (userDto.getId() != null) {
            user = userRepository.findById(userDto.getId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + userDto.getId()));
        } else {
            user = new User();
        }

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setStatus(userDto.getStatus());

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        if (photoFile != null && !photoFile.isEmpty()) {
            String photoBase64 = Base64.getEncoder().encodeToString(photoFile.getBytes());
            user.setPhoto("data:" + photoFile.getContentType() + ";base64," + photoBase64);
        }

        if (userDto.getRoleId() != null) {
            Role role = roleRepository.findById(userDto.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);
        }

        userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> findPaginated(int page, int size, String searchName) {
        Pageable pageable = PageRequest.of(page, size);
        if (searchName != null && !searchName.trim().isEmpty()) {
            return userRepository.findBySearchTerm(searchName, pageable);
        }
        return userRepository.findAll(pageable);
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    // --- IMPLEMENTACIÓN DE LA LÓGICA OTP Y RESET PASSWORD ---

    @Override
    public void generateAndSendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 1. Generar OTP de 6 dígitos
        String otp = String.format("%06d", new Random().nextInt(999999));

        // 2. Guardar en BD con expiración (15 minutos)
        user.setOtpCode(otp);
        user.setOtpExpiration(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);

        // 3. Enviar correo
        try {
            emailService.sendOtpEmail(user.getEmail(), otp, user.getFirstName());
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage());
        }
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (user.getOtpCode() == null || user.getOtpExpiration() == null) {
            return false;
        }

        // Validar que coincida y no haya expirado
        return user.getOtpCode().equals(otp) &&
                user.getOtpExpiration().isAfter(LocalDateTime.now());
    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
        // 1. Validar OTP nuevamente por seguridad
        if (!verifyOtp(email, otp)) {
            throw new RuntimeException("OTP inválido o expirado");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Actualizar contraseña
        user.setPassword(passwordEncoder.encode(newPassword));

        // 3. Limpiar el OTP usado
        user.setOtpCode(null);
        user.setOtpExpiration(null);

        userRepository.save(user);
    }
}