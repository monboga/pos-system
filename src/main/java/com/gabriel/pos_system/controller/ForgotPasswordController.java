package com.gabriel.pos_system.controller;

import java.time.LocalDateTime;
import java.util.Random;

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
public class ForgotPasswordController {
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final UserService userService;

    public ForgotPasswordController(EmailService emailService, UserRepository userRepository, UserService userService) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.userService = userService;
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
        // 1. Verificar si el usuario existe
        if (userRepository.findByEmail(email).isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "No se encontró ninguna cuenta con ese correo electrónico.");
            return "redirect:/forgot-password";
        }

        // 2. Generar un OTP de 6 dígitos
        String otp = String.format("%06d", new Random().nextInt(999999));

        try {
            // 3. Enviar el correo con el OTP
            emailService.sendOtpEmail(email, otp);

            // 4. Guardar el OTP, el email y una marca de tiempo en la sesión
            session.setAttribute("otp", otp);
            session.setAttribute("email", email);
            session.setAttribute("otpTimestamp", LocalDateTime.now());

            redirectAttributes.addFlashAttribute("success", "Se ha enviado un código de verificación a tu correo.");
            return "redirect:/verify-otp";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al enviar el correo. Inténtalo de nuevo.");
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
