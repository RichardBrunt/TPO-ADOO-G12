package com.escrims.infra.notification;

/**
 * Tipos de notificación soportados por el sistema.
 * Usado por el patrón FACADE para determinar los canales de envío.
 */
public enum TipoNotificacion {
    EMAIL,           // Solo email
    PUSH,            // Solo notificación push
    SMS,             // Solo SMS
    EMAIL_PUSH,      // Email + Push (para eventos importantes)
    TODOS            // Todos los canales disponibles (crítico)
}
