package com.escrims.model.domain.notification;

import com.escrims.model.domain.model.Usuario;

public interface NotificationService {
    void enviarNotificacion(Usuario usuario, String mensaje);
    void enviarEmail(Usuario usuario, String asunto, String mensaje);
    void enviarNotificacionPush(Usuario usuario, String titulo, String mensaje);
}
