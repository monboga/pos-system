package com.gabriel.pos_system.infrastructure.adapter.input;

import com.gabriel.pos_system.application.dto.auth.AuthResponseDto;
import com.gabriel.pos_system.application.service.UserService;
import com.gabriel.pos_system.application.dto.auth.ForgotPasswordRequestDto;
import com.gabriel.pos_system.application.dto.auth.LoginRequestDto;
import com.gabriel.pos_system.application.dto.auth.ResetPasswordRequestDto;
import com.gabriel.pos_system.application.dto.auth.VerifyOtpRequestDto;
import com.gabriel.pos_system.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto request) {
        // 1. Autenticar al usuario usando el AuthenticationManager de Spring
        // Esto verifica email y contraseña contra la BD automáticamente.
        // Si falla, lanza una excepción (BadCredentialsException).
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        // 2. Si la autenticación pasó, cargamos los detalles completos del usuario
        // para poder generar el token.
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        // 3. Generamos el Token JWT
        final String jwt = jwtService.generateToken(userDetails);

        // 4. Devolvemos la respuesta en formato JSON
        return ResponseEntity.ok(AuthResponseDto.builder()
                .token(jwt)
                .message("Login exitoso")
                .build());
    }

    // --- 2. SOLICITAR CÓDIGO (Forgot Password) ---
    @PostMapping("/forgot-password")
    public ResponseEntity<AuthResponseDto> forgotPassword(@RequestBody ForgotPasswordRequestDto request) {
        try {
            userService.generateAndSendOtp(request.getEmail());
            return ResponseEntity.ok(AuthResponseDto.builder()
                    .message("Se ha enviado un código de verificación a tu correo.")
                    .build());
        } catch (RuntimeException e) {
            // Por seguridad, a veces no se debe revelar si el correo existe o no,
            // pero para desarrollo es útil ver el mensaje.
            return ResponseEntity.badRequest().body(AuthResponseDto.builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    // --- 3. VERIFICAR CÓDIGO ---
    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponseDto> verifyOtp(@RequestBody VerifyOtpRequestDto request) {
        boolean isValid = userService.verifyOtp(request.getEmail(), request.getOtp());

        if (isValid) {
            return ResponseEntity.ok(AuthResponseDto.builder()
                    .message("Código verificado correctamente.")
                    .build());
        } else {
            return ResponseEntity.badRequest().body(AuthResponseDto.builder()
                    .message("El código ingresado es incorrecto o ha expirado.")
                    .build());
        }
    }

    // --- 4. RESTABLECER CONTRASEÑA ---
    @PostMapping("/reset-password")
    public ResponseEntity<AuthResponseDto> resetPassword(@RequestBody ResetPasswordRequestDto request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(AuthResponseDto.builder()
                    .message("Las contraseñas no coinciden.")
                    .build());
        }

        try {
            userService.resetPassword(request.getEmail(), request.getOtp(), request.getNewPassword());
            return ResponseEntity.ok(AuthResponseDto.builder()
                    .message("Tu contraseña ha sido actualizada exitosamente.")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(AuthResponseDto.builder()
                    .message(e.getMessage())
                    .build());
        }
    }
}