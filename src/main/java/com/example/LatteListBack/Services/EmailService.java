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
        enviarMail(destinatario, "¡Bienvenido a LatteList! ☕",
                "Hola " + nombre + ",\n\nTu cuenta ha sido creada exitosamente."+
                        "Ya puedes iniciar sesión y comenzar a puntuar tus cafés favoritos.\n\n" +
                        "Saludos,\nEl equipo de LatteList.");
    }


    @Async
    public void enviarCorreoRecuperacion(String destinatario, String token) {
        String link = "http://localhost:4200/auth/reset-password?token=" + token;
        enviarMail(destinatario, "Recuperación de Contraseña - LatteList",
                "Haz clic aquí para restablecer tu contraseña:\n" + link + "\n\nEl enlace expira en 15 minutos.");
    }


    //inactivo
    @Async
    public void enviarNotificacionSuspension(String destinatario, String nombre) {
        enviarMail(destinatario, "Aviso de Suspensión de Cuenta - LatteList",
                "Hola " + nombre + ",\n\n" +
                        "Tu cuenta ha sido suspendida por un administrador por incumplimiento de las normas.\n" +
                        "Si crees que esto es un error, por favor contacta con soporte.\n\n" +
                        "Saludos,\nEl equipo de LatteList.");
    }

    //desactivado
    @Async
    public void enviarNotificacionPausa(String destinatario, String nombre) {
        enviarMail(destinatario, "Tu cuenta ha sido pausada - LatteList",
                "Hola " + nombre + ",\n\n" +
                        "Confirmamos que has pausado tu cuenta temporalmente.\n" +
                        "Tus datos están guardados, pero tu perfil no será visible.\n" +
                        "Para reactivarla, simplemente inicia sesión nuevamente.\n\n" +
                        "¡Te esperamos de vuelta pronto!");
    }

    //eliminado
    @Async
    public void enviarCorreoEliminacion(String destinatario, String nombre) {
        enviarMail(destinatario, "Tu cuenta ha sido eliminada - LatteList",
                "Hola " + nombre + ",\n\n" +
                        "Como solicitaste, tu cuenta ha sido eliminada permanentemente del sistema.\n" +
                        "Tus datos personales ya no son accesibles.\n\n" +
                        "Esperamos verte de nuevo algún día.\n" +
                        "Saludos,\nEl equipo de LatteList.");
    }

    //re activacion
    @Async
    public void enviarNotificacionReactivacion(String destinatario, String nombre) {
        enviarMail(destinatario, "¡Tu cuenta ha sido reactivada! - LatteList",
                "Hola " + nombre + ",\n\n" +
                        "Buenas noticias. Un administrador ha revisado tu caso y ha reactivado tu cuenta.\n" +
                        "Ya puedes volver a iniciar sesión y utilizar la plataforma con normalidad.\n\n" +
                        "¡Bienvenido de nuevo!\n" +
                        "El equipo de LatteList.");
    }

    @Async
    public void enviarAvisoCambioContrasena(String destinatario, String nombre) {
        enviarMail(destinatario, "Seguridad - Cambio de Contraseña",
                "Hola " + nombre + ",\n\n" +
                        "Te informamos que tu contraseña ha sido modificada exitosamente.\n" +
                        "Si no fuiste tú, por favor contacta a soporte inmediatamente.");
    }

    private void enviarMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}