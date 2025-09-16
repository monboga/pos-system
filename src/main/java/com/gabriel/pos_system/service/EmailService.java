package com.gabriel.pos_system.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendOtpEmail(String to, String otp) throws MessagingException {
        // Preparamos el contexto con las variables para la plantilla
        Context context = new Context();
        context.setVariable("otp", otp);

        // Procesamos la plantilla Thymeleaf para generar el HTML del correo
        String process = templateEngine.process("emails/otp-email", context);

        // Creamos y enviamos el mensaje
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        helper.setSubject("Tu Código de Verificación");
        helper.setText(process, true); // true indica que el texto es HTML
        helper.setTo(to);

        mailSender.send(mimeMessage);
    }
}
