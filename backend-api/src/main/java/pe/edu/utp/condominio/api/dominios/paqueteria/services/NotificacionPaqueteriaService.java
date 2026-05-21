package pe.edu.utp.condominio.api.dominios.paqueteria.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pe.edu.utp.condominio.api.dominios.paqueteria.models.Paquete;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;

@Service
public class NotificacionPaqueteriaService {

    private final JavaMailSender mailSender;
    private final String correoOrigen;

    public NotificacionPaqueteriaService(JavaMailSender mailSender,
            @Value("${spring.mail.username:}") String correoOrigen) {
        this.mailSender = mailSender;
        this.correoOrigen = correoOrigen;
    }

    public void enviarNotificacion(Paquete paquete, Unidad unidad) {
        String destino = obtenerCorreoDestino(unidad);
        if (destino == null) {
            throw new IllegalArgumentException("La unidad no tiene correo para notificacion.");
        }

        SimpleMailMessage mensaje = new SimpleMailMessage();
        if (correoOrigen != null && !correoOrigen.isBlank()) {
            mensaje.setFrom(correoOrigen);
        }
        mensaje.setTo(destino);
        mensaje.setSubject("Notificacion de paquete recibido");
        mensaje.setText(construirMensaje(paquete, unidad));

        mailSender.send(mensaje);
    }

    private String obtenerCorreoDestino(Unidad unidad) {
        if (unidad.getEmailResidente() != null && !unidad.getEmailResidente().isBlank()) {
            return unidad.getEmailResidente();
        }
        if (unidad.getEmailPropietario() != null && !unidad.getEmailPropietario().isBlank()) {
            return unidad.getEmailPropietario();
        }
        return null;
    }

    private String construirMensaje(Paquete paquete, Unidad unidad) {
        String unidadTexto = "Unidad " + unidad.getNumeroUnidad();
        if (unidad.getTorre() != null && !unidad.getTorre().isBlank()) {
            unidadTexto = "Torre " + unidad.getTorre() + " - Unidad " + unidad.getNumeroUnidad();
        }

        return "Se registro un paquete para " + unidadTexto + ".\n"
                + "Remitente: " + paquete.getRemitente() + "\n"
                + "Destinatario: " + paquete.getDestinatario() + "\n"
                + "Fecha de recepcion: " + paquete.getFechaRecepcion();
    }
}
