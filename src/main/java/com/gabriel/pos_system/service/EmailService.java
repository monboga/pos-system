package com.gabriel.pos_system.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.gabriel.pos_system.model.Business;
import com.gabriel.pos_system.repository.BusinessRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final BusinessRepository businessRepository;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine,
            BusinessRepository businessRepository) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.businessRepository = businessRepository;
    }

    public void sendOtpEmail(String to, String otp, String userName) throws MessagingException {
        try {
            // Obtenemos los datos del negocio para usar su nombre
            Business business = businessRepository.findAll().stream().findFirst().orElse(new Business());

            // Preparamos el contexto con TODAS las variables para la plantilla
            Context context = new Context();
            context.setVariable("otpCode", otp);
            context.setVariable("userName", userName);
            context.setVariable("businessName", business.getRazonSocial());

            // Procesamos la plantilla Thymeleaf para generar el HTML del correo
            String htmlContent = templateEngine.process("emails/otp-email", context);

            // Creamos y enviamos el mensaje
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            helper.setSubject("Tu Código de Verificación para " + business.getRazonSocial());
            helper.setText(htmlContent, true); // true indica que el texto es HTML
            helper.setTo(to);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo de OTP: " + e.getMessage());
        }
    }
}
