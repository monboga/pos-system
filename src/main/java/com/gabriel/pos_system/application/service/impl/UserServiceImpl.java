package com.gabriel.pos_system.application.service.impl;

import com.gabriel.pos_system.application.dto.profile.ChangePasswordRequestDto;
import com.gabriel.pos_system.application.dto.profile.UpdateProfileRequestDto;
import com.gabriel.pos_system.application.dto.user.CreateUserRequestDto;
import com.gabriel.pos_system.application.dto.user.UpdateUserRequestDto;
import com.gabriel.pos_system.application.service.UserService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    // --- MÉTODOS CRUD ---

    @Override
    public Page<User> findPaginated(int page, int size, String searchName) {
        Pageable pageable = PageRequest.of(page, size);
        if (searchName != null && !searchName.trim().isEmpty()) {
            return userRepository.findBySearchTerm(searchName, pageable);
        }
        return userRepository.findAll(pageable);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Override
    @Transactional // Asegura que si falla algo, no se guarde nada
    public User createUser(CreateUserRequestDto request, MultipartFile photo) throws IOException {
        // 1. Validar que el email no exista
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El correo electrónico ya está registrado: " + request.getEmail());
        }

        // 2. Buscar el Rol en la BD
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + request.getRoleId()));

        // 3. Crear la entidad User
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .status(request.getStatus())
                .password(passwordEncoder.encode(request.getPassword())) // Encriptar
                .roles(Set.of(role))
                .build();

        // 4. Procesar la foto si existe
        if (photo != null && !photo.isEmpty()) {
            String photoBase64 = Base64.getEncoder().encodeToString(photo.getBytes());
            user.setPhoto("data:" + photo.getContentType() + ";base64," + photoBase64);
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(Long id, UpdateUserRequestDto request, MultipartFile photo) throws IOException {
        User user = getUserById(id);

        // Actualizamos solo los campos que vienen en el request (PATCH parcial)
        if (request.getFirstName() != null)
            user.setFirstName(request.getFirstName());
        if (request.getLastName() != null)
            user.setLastName(request.getLastName());
        if (request.getPhoneNumber() != null)
            user.setPhoneNumber(request.getPhoneNumber());
        if (request.getStatus() != null)
            user.setStatus(request.getStatus());

        // Actualizar Rol
        if (request.getRoleId() != null) {
            Role role = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + request.getRoleId()));
            // Reemplazamos los roles actuales (asumiendo un solo rol por usuario en este
            // sistema)
            user.setRoles(Set.of(role));
        }

        // Actualizar Foto
        if (photo != null && !photo.isEmpty()) {
            String photoBase64 = Base64.getEncoder().encodeToString(photo.getBytes());
            user.setPhoto("data:" + photo.getContentType() + ";base64," + photoBase64);
        }

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. Usuario no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }

    // --- MÉTODOS DE SEGURIDAD (OTP) ---

    @Override
    public void generateAndSendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con correo: " + email));

        // Generar OTP de 6 dígitos
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Guardar en BD con expiración (15 mins)
        user.setOtpCode(otp);
        user.setOtpExpiration(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);

        // Enviar correo
        try {
            emailService.sendOtpEmail(user.getEmail(), otp, user.getFirstName());
        } catch (Exception e) {
            // En un sistema real, podrías querer revertir el guardado del OTP si falla el
            // correo
            // o manejarlo con una cola de mensajería.
            throw new RuntimeException("Error al enviar el correo de verificación: " + e.getMessage());
        }
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        if (user.getOtpCode() == null || user.getOtpExpiration() == null) {
            return false;
        }

        // Verificar coincidencia y tiempo
        return user.getOtpCode().equals(otp) &&
                user.getOtpExpiration().isAfter(LocalDateTime.now());
    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
        // Validar OTP nuevamente antes de cambiar la contraseña
        if (!verifyOtp(email, otp)) {
            throw new RuntimeException("El código OTP es inválido o ha expirado.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        user.setPassword(passwordEncoder.encode(newPassword));

        // Limpiar OTP para que no se pueda reusar
        user.setOtpCode(null);
        user.setOtpExpiration(null);

        userRepository.save(user);
    }

    // --- MÉTODOS DE CONSULTA ---

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User updateProfile(String email, UpdateProfileRequestDto request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado (Perfil)"));

        // Actualizamos solo los campos permitidos
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());

        // Nota: No actualizamos roles, estado ni email aquí.

        return userRepository.save(user);
    }

    @Override
    public void changePassword(String email, ChangePasswordRequestDto request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado (Perfil)"));

        // 1. Validar que la contraseña actual ingresada coincida con la de la BD
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("La contraseña actual es incorrecta.");
        }

        // 2. Validar que la nueva contraseña y la confirmación coincidan
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new RuntimeException("La nueva contraseña y la confirmación no coinciden.");
        }

        // 3. Encriptar y guardar la nueva contraseña
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}