package com.gabriel.pos_system.controller;

import java.time.LocalDateTime;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gabriel.pos_system.repository.UserRepository;
import com.gabriel.pos_system.service.EmailService;
import com.gabriel.pos_system.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(EmailService emailService, UserRepository userRepository, UserService userService) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // Muestra el formulario inicial para ingresar el correo
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    // Procesa la solicitud de envío de OTP
    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, HttpSession session,
            RedirectAttributes redirectAttributes) {
        logger.info("Solicitud de recuperación de contraseña recibida para el correo: {}", email);

        if (userRepository.findByEmail(email).isEmpty()) {
            logger.warn("Intento de recuperación para un correo no existente: {}", email);
            redirectAttributes.addFlashAttribute("error", "No se encontró ninguna cuenta con ese correo electrónico.");
            return "redirect:/forgot-password";
        }

        String otp = String.format("%06d", new Random().nextInt(999999));

        try {
            logger.info("Enviando OTP {} al correo {}", otp, email);
            emailService.sendOtpEmail(email, otp);

            session.setAttribute("otp", otp);
            session.setAttribute("email", email);
            session.setAttribute("otpTimestamp", LocalDateTime.now());

            redirectAttributes.addFlashAttribute("success", "Se ha enviado un código de verificación a tu correo.");
            logger.info("OTP enviado exitosamente a {}", email);
            return "redirect:/verify-otp";

        } catch (Exception e) {
            logger.error("Error al enviar el correo de recuperación a {}: {}", email, e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al enviar el correo. Inténtalo de nuevo más tarde.");
            return "redirect:/forgot-password";
        }
    }

    // Muestra el formulario para verificar el OTP
    @GetMapping("/verify-otp")
    public String showVerifyOtpForm(HttpSession session) {
        // Si el usuario llega aquí sin haber solicitado un OTP, lo redirigimos
        if (session.getAttribute("email") == null) {
            return "redirect:/forgot-password";
        }
        return "verify-otp";
    }

    // Procesa la verificación del OTP
    @PostMapping("/verify-otp")
    public String processVerifyOtp(@RequestParam("otp") String otp, HttpSession session,
            RedirectAttributes redirectAttributes) {
        String sessionOtp = (String) session.getAttribute("otp");
        LocalDateTime otpTimestamp = (LocalDateTime) session.getAttribute("otpTimestamp");

        // Validar que el OTP de la sesión exista y no haya expirado (ej. 10 minutos)
        if (sessionOtp == null || otpTimestamp.isBefore(LocalDateTime.now().minusMinutes(10))) {
            redirectAttributes.addFlashAttribute("error", "El código ha expirado. Por favor, solicita uno nuevo.");
            return "redirect:/forgot-password";
        }

        if (otp.equals(sessionOtp)) {
            // OTP correcto, marcamos la verificación como exitosa en la sesión
            session.setAttribute("otpVerified", true);
            return "redirect:/reset-password";
        } else {
            redirectAttributes.addFlashAttribute("error", "El código ingresado es incorrecto.");
            return "redirect:/verify-otp";
        }
    }

    // Muestra el formulario para restablecer la contraseña
    @GetMapping("/reset-password")
    public String showResetPasswordForm(HttpSession session) {
        // Solo permitir acceso si el OTP fue verificado
        if (session.getAttribute("otpVerified") == null || !(Boolean) session.getAttribute("otpVerified")) {
            return "redirect:/forgot-password";
        }

        return "reset-password";
    }

    // Procesa el guardado de la nueva contraseña
    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam String newPassword, @RequestParam String confirmPassword,
            HttpSession session, RedirectAttributes redirectAttributes) {
        String email = (String) session.getAttribute("email");
        if (email == null || session.getAttribute("otpVerified") == null
                || !(Boolean) session.getAttribute("otpVerified")) {
            return "redirect:/forgot-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden.");
            return "redirect:/reset-password";
        }

        // Actualizamos la contraseña en la base de datos
        userService.updatePassword(email, newPassword);

        // Limpiamos los atributos de la sesión
        session.removeAttribute("otp");
        session.removeAttribute("email");
        session.removeAttribute("otpTimestamp");
        session.removeAttribute("otpVerified");

        redirectAttributes.addFlashAttribute("logout",
                "Tu contraseña ha sido actualizada exitosamente. Por favor, inicia sesión.");
        return "redirect:/login";
    }

}
