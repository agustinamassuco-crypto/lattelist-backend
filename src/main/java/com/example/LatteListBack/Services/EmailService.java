package com.example.LatteListBack.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void enviarCorreoBienvenida(String destinatario, String nombre) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject("¡Bienvenido a LatteList! ☕");
        message.setText("Hola " + nombre + ",\n\n" +
                "Tu cuenta ha sido creada exitosamente.\n" +
                "Ya puedes iniciar sesión y comenzar a puntuar tus cafés favoritos.\n\n" +
                "Saludos,\nEl equipo de LatteList.");

        mailSender.send(message);
    }

    @Async
    public void enviarCorreoRecuperacion(String destinatario, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject("Recuperación de Contraseña - LatteList");
        message.setText("Hola,\n\n" +
                "Hemos recibido una solicitud para restablecer tu contraseña.\n" +
                "Haz clic en el siguiente enlace para crear una nueva:\n\n" +
                "http://localhost:4200/auth/reset-password?token=" + token + "\n\n" + //puerto angular
                "Si no fuiste tú, ignora este mensaje.\n" +
                "El enlace expira en 15 minutos.");

        mailSender.send(message);
    }
}