package com.gabriel.pos_system.service;

import java.io.IOException;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gabriel.pos_system.dto.UserDto;
import com.gabriel.pos_system.model.Role;
import com.gabriel.pos_system.model.User;
import com.gabriel.pos_system.repository.RoleRepository;
import com.gabriel.pos_system.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto, MultipartFile photoFile) throws IOException {
        // Aquí iría la lógica para diferenciar entre crear (new User()) y actualizar
        // (userRepository.findById(...))
        // Por ahora, nos centramos en la creación.
        User user = new User();

        if (userDto.getId() != null) {
            // Es una actualización
            user = userRepository.findById(userDto.getId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + userDto.getId()));
        } else {
            // Es una creación
            user = new User();
        }

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setStatus(userDto.getStatus());

        // Solo actualizamos la contraseña si se proporcionó una nueva
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        // --- LÓGICA PARA PROCESAR Y GUARDAR LA IMAGEN ---
        if (photoFile != null && !photoFile.isEmpty()) {
            // Convertimos los bytes del archivo a un String Base64
            String photoBase64 = Base64.getEncoder().encodeToString(photoFile.getBytes());
            // Creamos el Data URI completo para que el HTML pueda leerlo
            user.setPhoto("data:" + photoFile.getContentType() + ";base64," + photoBase64);
        }
        // --- FIN DE LA LÓGICA DE IMAGEN ---

        if (userDto.getRoleId() != null) {
            Role role = roleRepository.findById(userDto.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));

            // --- INICIO DE LA CORRECCIÓN ---
            // Creamos un HashSet que sí es modificable
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);
            // --- FIN DE LA CORRECCIÓN ---
        }

        User savedUser = userRepository.save(user);

        // --- INICIO DE LA CORRECCIÓN ---
        // Verificamos si el usuario actualizado es el mismo que está actualmente
        // logueado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName().equals(savedUser.getEmail())) {
            // Si es así, creamos una nueva autenticación con los datos actualizados
            // y la establecemos en el contexto de seguridad para refrescar la sesión.
            User updatedPrincipal = userRepository.findByEmail(savedUser.getEmail()).orElseThrow();
            Authentication newAuth = new UsernamePasswordAuthenticationToken(updatedPrincipal,
                    authentication.getCredentials(), authentication.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }
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
    public void updatePassword(User user, String newPassword) {
        // Encriptamos la nueva contraseña y la guardamos
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Refrescamos la sesión de seguridad con los nuevos datos
        Authentication newAuth = new UsernamePasswordAuthenticationToken(user, user.getPassword(),
                user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    @Override
    public void updatePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el correo: " + email));

        // Encriptamos la nueva contraseña antes de guardarla
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
