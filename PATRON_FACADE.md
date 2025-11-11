# 🔨 Patrón FACADE - Sistema de Notificaciones eScrims

## Ubicación
- **Clase principal**: `NotificationFacade.java`
- **Enum de tipos**: `TipoNotificacion.java`
- **Paquete**: `com.escrims.infra.notification`

## Descripción

El patrón FACADE unifica múltiples sistemas de notificación complejos bajo una **interfaz simple y centralizada**.

### Objetivo
Proporcionar un único punto de acceso para enviar notificaciones a través de diferentes canales (Email, Push, SMS, Webhooks) sin que el cliente necesite conocer los detalles de implementación de cada servicio.

---

## Implementación

### 1. Interfaz Simplificada

```java
/**
 * MÉTODO PÚBLICO PRINCIPAL (Patrón FACADE)
 * 
 * Envía notificación al destinatario a través de los canales especificados.
 * Este es el único punto de entrada que necesita conocer el cliente.
 */
public void enviarNotificacion(Usuario destinatario, String mensaje, TipoNotificacion tipo)
```

### 2. Tipos de Notificación Soportados

El enum `TipoNotificacion` define 5 tipos:

```java
public enum TipoNotificacion {
    EMAIL,           // Solo email
    PUSH,            // Solo notificación push
    SMS,             // Solo SMS
    EMAIL_PUSH,      // Email + Push (para eventos importantes)
    TODOS            // Todos los canales disponibles (crítico)
}
```

### 3. Canales Integrados

El FACADE coordina internamente los siguientes servicios:

- ✉️ **EmailNotificationService**: Envío de correos electrónicos
- 📱 **PushNotificationService**: Notificaciones push a dispositivos móviles
- 💬 **SMSNotificationService**: Mensajes de texto SMS
- 🔗 **WebhookService**: (Preparado para integración futura)

---

## Uso en la Aplicación

### Escenario 1: Nueva Postulación (Solo Email)

```java
String mensaje = "¡Hola! El usuario 'Juan' se ha postulado a tu scrim...";
notificationFacade.enviarNotificacion(
    creadorDelScrim,
    mensaje,
    TipoNotificacion.EMAIL
);
```

### Escenario 2: Postulación Aceptada (Email + Push)

```java
String mensaje = "¡Felicidades! Tu postulación fue ACEPTADA...";
notificationFacade.enviarNotificacion(
    usuarioPostulante,
    mensaje,
    TipoNotificacion.EMAIL_PUSH  // ← Dos canales simultáneos
);
```

### Escenario 3: Postulación Rechazada (Solo Email)

```java
String mensaje = "Lamentablemente tu postulación no fue aceptada...";
notificationFacade.enviarNotificacion(
    usuarioPostulante,
    mensaje,
    TipoNotificacion.EMAIL
);
```

### Escenario 4: Notificación Crítica (Todos los canales)

```java
String mensaje = "¡ALERTA! Mantenimiento programado del sistema...";
notificationFacade.enviarNotificacion(
    usuario,
    mensaje,
    TipoNotificacion.TODOS  // ← Email + Push + SMS
);
```

---

## Ventajas del Patrón FACADE

### ✅ Simplicidad
El cliente solo necesita llamar a **un único método** con 3 parámetros simples:
- Destinatario
- Mensaje
- Tipo de notificación

### ✅ Abstracción
El cliente **NO necesita conocer**:
- Cómo funciona cada servicio de notificación
- Qué métodos específicos tiene cada servicio
- Cómo coordinar múltiples canales

### ✅ Flexibilidad
Cambiar la implementación interna (agregar/quitar servicios) **no afecta** al código cliente.

### ✅ Mantenibilidad
Toda la lógica de notificaciones está **centralizada** en una sola clase.

---

## Arquitectura Interna

### Métodos Privados (Ocultos al Cliente)

El FACADE encapsula la complejidad en métodos privados:

```java
// Cliente NO conoce estos métodos
private void enviarPorEmail(Usuario destinatario, String mensaje)
private void enviarPorPush(Usuario destinatario, String mensaje)
private void enviarPorSMS(Usuario destinatario, String mensaje)
private void enviarPorTodosLosCanales(Usuario destinatario, String mensaje)
```

### Switch Interno

```java
switch (tipo) {
    case EMAIL:
        enviarPorEmail(destinatario, mensaje);
        break;
    case EMAIL_PUSH:
        enviarPorEmail(destinatario, mensaje);
        enviarPorPush(destinatario, mensaje);
        break;
    case TODOS:
        enviarPorTodosLosCanales(destinatario, mensaje);
        break;
    // ...
}
```

---

## Integración con el Sistema

### Inicialización (MainFrame.java)

```java
// Se registran todos los servicios disponibles
NotificationFacade notificationFacade = new NotificationFacade();
notificationFacade.registrarServicio(new EmailNotificationService());
notificationFacade.registrarServicio(new PushNotificationService());
notificationFacade.registrarServicio(new SMSNotificationService());

// Se inyecta en el ApplicationModel
ApplicationModel model = new ApplicationModel(
    scrimService,
    usuarioRepository,
    notificationFacade  // ← Inyección de dependencia
);
```

### Uso en ApplicationModel

```java
// El ApplicationModel usa la interfaz simple del FACADE
notificationFacade.enviarNotificacion(usuario, mensaje, tipo);
```

---

## Ejemplo de Salida en Consola

```
[FACADE] Procesando notificación tipo: EMAIL_PUSH
[FACADE] Destinatario: Juan123

[EMAIL] To: juan@example.com
[EMAIL] Subject: Notificación eScrims
[EMAIL] Body: ¡Felicidades! Tu postulación fue ACEPTADA...

[PUSH] Sending push notification to: Juan123
[PUSH] Title: eScrims
[PUSH] Message: ¡Felicidades! Tu postulación fue ACEPTADA...

[FACADE] Notificación procesada exitosamente
```

---

## Comparación: Antes vs Después

### ❌ ANTES (Sin FACADE)

```java
// Cliente necesita conocer múltiples servicios
emailService.enviarEmail(usuario, asunto, mensaje);
pushService.enviarPush(usuario, titulo, mensaje);
smsService.enviarSMS(usuario.getTelefono(), mensaje);
webhookService.enviar(url, payload);

// ¿Qué pasa si falla uno?
// ¿Cómo saber cuáles enviar según el contexto?
// ¿Cómo manejar errores de cada servicio?
```

### ✅ DESPUÉS (Con FACADE)

```java
// Cliente usa una interfaz simple y unificada
notificationFacade.enviarNotificacion(
    usuario,
    mensaje,
    TipoNotificacion.EMAIL_PUSH
);

// El FACADE maneja toda la complejidad internamente
```

---

## Conclusión

El patrón FACADE en `NotificationFacade` cumple perfectamente su propósito:

1. ✅ **Interfaz simple**: Un único método público `enviarNotificacion()`
2. ✅ **Abstracción completa**: El cliente no conoce los detalles internos
3. ✅ **Múltiples canales**: Email, Push, SMS integrados
4. ✅ **Escalable**: Fácil agregar nuevos servicios (Webhooks, Telegram, etc.)
5. ✅ **Mantenible**: Lógica centralizada en una sola clase

---

## Extensibilidad Futura

### Agregar un nuevo canal (ejemplo: Telegram)

1. Crear `TelegramNotificationService` implementando `NotificationService`
2. Registrarlo en el FACADE: `facade.registrarServicio(new TelegramService())`
3. Agregar `TELEGRAM` al enum `TipoNotificacion`
4. Agregar caso en el `switch` interno

**El código cliente NO cambia en absoluto** ✨
