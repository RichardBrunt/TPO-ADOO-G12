# Interfaz Gráfica eScrims (UI)

## Descripción

Interfaz gráfica de usuario (GUI) desarrollada con **Java Swing** siguiendo el patrón arquitectónico **MVC (Model-View-Controller)** para el sistema de gestión de scrims.

## Arquitectura MVC

La interfaz está organizada en tres componentes principales:

### 📦 **Model** (`ui/model`)
- **ApplicationModel**: Gestiona el estado de la aplicación y la lógica de negocio
  - Manejo de sesión de usuario (login/logout)
  - Operaciones CRUD de scrims y postulaciones
  - Integración con repositorios y servicios del dominio
  - Notificación de cambios mediante patrón Observer

- **ModelChangeListener**: Interfaz para observadores del modelo
  - Permite que las vistas reaccionen a cambios en el modelo
  - Desacopla la vista del modelo

### 🎮 **Controller** (`ui/controller`)
- **ScrimController**: Gestiona las acciones del usuario
  - Autenticación: login, logout, registro
  - Gestión de scrims: crear, buscar, listar
  - Gestión de postulaciones: postularse, obtener mis postulaciones
  - Estadísticas del sistema
  - Traduce eventos de UI a llamadas al modelo

### 🖼️ **View** (`ui/view`)
- **MainFrame**: Ventana principal de la aplicación
  - Contenedor de todas las vistas
  - Gestión de navegación entre pantallas
  - Implementa ModelChangeListener para reaccionar a cambios

- **LoginPanel**: Pantalla de autenticación
  - Formulario de login (usuario/contraseña)
  - Diálogo de registro de nuevos usuarios
  - Validación de datos de entrada

- **DashboardPanel**: Panel principal con pestañas
  - **Pestaña 1 - Crear Scrim**: Formulario para crear nuevos scrims
  - **Pestaña 2 - Buscar Scrims**: Tabla con scrims disponibles
  - **Pestaña 3 - Mis Postulaciones**: Lista de postulaciones del usuario
  - **Pestaña 4 - Estadísticas**: Métricas del sistema

## Estructura de Archivos

```
ui/
├── ScrimApplication.java        # Punto de entrada de la aplicación GUI
├── model/
│   ├── ApplicationModel.java    # Modelo de la aplicación (MVC)
│   └── ModelChangeListener.java # Interfaz Observer para cambios
├── controller/
│   └── ScrimController.java     # Controlador principal (MVC)
└── view/
    ├── MainFrame.java            # Ventana principal
    ├── LoginPanel.java           # Panel de login
    └── DashboardPanel.java       # Panel principal con tabs
```

## Tecnologías

- **Java 17** (Vanilla - sin frameworks externos)
- **Java Swing** (`javax.swing`)
  - JFrame, JPanel, JTabbedPane
  - JTable, JTextField, JButton
  - GridBagLayout, BorderLayout, FlowLayout
- **Patrón MVC**
- **Patrón Observer** (ModelChangeListener)

## Ejecución

### Compilar
```powershell
.\compilar.ps1
```

### Ejecutar la aplicación GUI
```powershell
java -cp out com.escrims.ui.ScrimApplication
```

O usando el script:
```powershell
.\ejecutar-ui.ps1
```

## Funcionalidades Implementadas

### ✅ Autenticación
- [x] Login con usuario y contraseña
- [x] Registro de nuevos usuarios con perfil completo:
  - Username, email, contraseña
  - Región, MMR, latencia
  - Roles preferidos (Duelist, Support, Flex)
- [x] Logout
- [x] Persistencia de sesión durante la ejecución

### ✅ Gestión de Scrims
- [x] Crear nuevo scrim con parámetros completos:
  - Juego (VALORANT, LOL, CS2)
  - Formato (5v5, 3v3, etc.)
  - Región
  - Rango de MMR (mínimo y máximo)
  - Latencia máxima
  - Duración en minutos
  - Fecha de inicio automática
- [x] Buscar scrims disponibles
- [x] Ver detalles de un scrim
- [x] Tabla con información resumida (ID, Juego, Formato, Región, Estado, Capacidad, Fecha)

### ✅ Gestión de Postulaciones
- [x] Postularse a un scrim
- [x] Seleccionar rol al postularse
- [x] Ver mis postulaciones
- [x] Ver estado de postulaciones (PENDIENTE, ACEPTADA, RECHAZADA)

### ✅ Estadísticas
- [x] Total de usuarios
- [x] Total de scrims por estado (abiertos, en curso, finalizados)
- [x] Total de postulaciones por estado (pendientes, aprobadas, rechazadas)
- [x] Actualización en tiempo real

## Datos de Demostración

La aplicación carga datos de prueba automáticamente:

| Usuario | Contraseña | Región | MMR  | Roles |
|---------|------------|--------|------|-------|
| admin   | admin      | SA     | 1800 | DUELIST, SUPPORT |
| player1 | pass1      | SA     | 1500 | DUELIST |
| player2 | pass2      | NA     | 1700 | SUPPORT |

**3 Scrims de ejemplo** creados con diferentes juegos y configuraciones.

## Flujo de Uso

1. **Inicio**: La aplicación muestra la pantalla de login
2. **Login**: Ingresar credenciales (admin/admin)
3. **Dashboard**: Acceso a las 4 pestañas principales
4. **Crear Scrim**: Completar formulario y crear
5. **Buscar Scrims**: Ver tabla, seleccionar y postularse
6. **Ver Postulaciones**: Revisar el estado de mis aplicaciones
7. **Estadísticas**: Consultar métricas del sistema
8. **Logout**: Cerrar sesión

## Capturas de Funcionalidad

### Login
- Campo de usuario
- Campo de contraseña
- Botón "Iniciar Sesión"
- Enlace "¿No tienes cuenta? Regístrate"

### Dashboard - Crear Scrim
- Selector de Juego
- Selector de Formato
- Selector de Región
- Spinners para MMR mín/máx
- Spinner para latencia máxima
- Spinner para duración
- Botón "Crear Scrim"

### Dashboard - Buscar Scrims
- Tabla con scrims disponibles (7 columnas)
- Botón "Actualizar"
- Botón "Postularse"
- Botón "Ver Detalles"

### Dashboard - Mis Postulaciones
- Tabla con postulaciones del usuario (4 columnas)
- Botón "Actualizar"

### Dashboard - Estadísticas
- Área de texto con métricas formateadas
- Botón "Actualizar Estadísticas"

## Integración con el Dominio

La UI integra todos los 13 patrones de diseño implementados en el proyecto:

1. **State**: Los scrims muestran su estado actual (BUSCANDO, EN_CURSO, etc.)
2. **Strategy**: Se utiliza ByMMRStrategy para selección de jugadores
3. **Observer**: ModelChangeListener notifica cambios a las vistas
4. **Repository**: ApplicationModel usa todos los repositorios
5. **Builder**: (Disponible para construcción compleja si se requiere)
6. **Facade**: NotificationFacade podría usarse para notificaciones
7. **Factory Method**: Se crean instancias específicas según juego
8. **Adapter**: APIs externas disponibles para integración futura
9. **Decorator**: Mensajes decorados disponibles
10. **Command**: Comandos de cancelación disponibles
11. **Template Method**: Algoritmos de matchmaking configurables
12. **Chain of Responsibility**: Validación de postulaciones
13. **Singleton**: EventBus único en toda la aplicación

## Mejoras Futuras

- [ ] Validación mejorada de formularios
- [ ] Mensajes de error más descriptivos
- [ ] Confirmación de acciones destructivas
- [ ] Indicadores de carga para operaciones async
- [ ] Filtros avanzados en tabla de scrims
- [ ] Ordenamiento de columnas en tablas
- [ ] Temas visuales (dark mode, light mode)
- [ ] Internacionalización (i18n)
- [ ] Notificaciones visuales (toasts)
- [ ] Gráficos y visualizaciones para estadísticas

## Notas Técnicas

- **Thread Safety**: Las operaciones de UI se ejecutan en el Event Dispatch Thread (EDT) mediante `SwingUtilities.invokeLater()`
- **Look & Feel**: Se configura automáticamente el L&F del sistema operativo
- **Separación de Responsabilidades**: MVC estricto, sin lógica de negocio en vistas
- **Reutilización**: El modelo y controlador pueden usarse con otras vistas (web, móvil)
- **Extensibilidad**: Fácil agregar nuevas vistas o funcionalidades mediante el patrón MVC

---

**Desarrollado para**: TP Proceso y Desarrollo de Software  
**Tecnología**: Java 17 Vanilla + Swing  
**Arquitectura**: MVC + 13 Design Patterns  
**Fecha**: 2024
