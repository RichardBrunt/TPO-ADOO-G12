# eScrims - Sistema de Gestión de Scrims

Sistema backend para organizar y gestionar scrims (partidas de práctica) para juegos competitivos.

## Patrones de Diseño Implementados (6 Total)

### 1. ✅ Patrón State
- **Interface**: `ScrimState`
- **Estados concretos**:
  - `BuscandoState` - Scrim buscando jugadores
  - `LobbyArmadoState` - Lobby lleno, esperando confirmaciones
  - `EnCursoState` - Scrim en progreso
  - `FinalizadoState` - Scrim terminado
- **Contexto**: `Scrim` (entidad de dominio)
- **Propósito**: Gestiona el ciclo de vida del scrim con transiciones de estado claras

### 2. ✅ Patrón Strategy
- **Interface**: `SelectionStrategy`
- **Estrategias concretas**:
  - `ByMMRStrategy` - Selecciona jugadores por MMR (mayor a menor)
  - `FIFOStrategy` - Selecciona por orden de llegada
  - `ByLatenciaStrategy` - Selecciona por menor latencia
- **Contexto**: `ScrimService` (usa la estrategia para procesar postulaciones)
- **Propósito**: Permite cambiar dinámicamente el algoritmo de selección de jugadores

### 3. ✅ Patrón Observer
- **Subject**: `DomainEventBus` (publica eventos)
- **Observer**: `DomainEventSubscriber<E>` (interface genérica)
- **Concrete Observers**: 
  - `ConsoleNotificationSubscriber` (imprime eventos a consola)
  - `MultiChannelNotificationSubscriber` (usa Facade para notificaciones)
- **Evento**: `ScrimStateChanged` (publicado en cada transición de estado)
- **Propósito**: Desacopla la lógica de notificación del negocio principal

### 4. ✅ Patrón Repository
- **Interfaces**: `UsuarioRepository`, `ScrimRepository`, `PostulacionRepository`
- **Implementaciones**: `InMemory*Repository` (almacenamiento en memoria)
- **Propósito**: Abstrae la persistencia de datos, facilitando cambios futuros a BD real

### 5. ✅ Patrón Builder
- **Builder**: `ScrimBuilder`
- **Métodos factory preconfigurados**:
  - `casual()` - Scrim casual (MMR 0-3000, latencia max 100ms)
  - `competitivo()` - Scrim competitivo (MMR 1500-2500, latencia max 50ms)
  - `profesional()` - Scrim profesional (MMR 2000-3000, latencia max 30ms)
- **Métodos fluidos**: `withJuego()`, `withFormato()`, `withRegion()`, etc.
- **Propósito**: Simplifica la creación de objetos complejos (Scrim) con múltiples parámetros

### 6. ✅ Patrón Facade
- **Facade**: `NotificationFacade`
- **Subsistemas coordinados**:
  - `EmailNotificationService`
  - `PushNotificationService`
  - `SMSNotificationService`
- **Métodos simplificados**:
  - `notificarATodos()` - Envía a todos los canales
  - `notificarPorEmail()`, `notificarPorPush()`, `notificarPorSMS()` - Canal específico
  - `notificarCritico()` - Notificación urgente por todos los canales
- **Propósito**: Simplifica el uso del sistema de notificaciones multi-canal

## Estructura del Proyecto

```
src/main/java/com/escrims/
├── app/
│   ├── CompleteDemoMain.java           # Demo completo (6 patrones)
│   └── DemoMain.java                   # Demo básico (3 patrones)
├── domain/
│   ├── builder/                        # Patrón Builder
│   │   └── ScrimBuilder.java
│   ├── events/                         # Patrón Observer
│   │   ├── DomainEvent.java
│   │   ├── DomainEventBus.java
│   │   ├── DomainEventSubscriber.java
│   │   ├── InMemoryDomainEventBus.java
│   │   └── ScrimStateChanged.java
│   ├── model/                          # Entidades de dominio
│   │   ├── Confirmacion.java
│   │   ├── Estadistica.java
│   │   ├── Formato.java
│   │   ├── Juego.java
│   │   ├── Postulacion.java
│   │   ├── Region.java
│   │   ├── Rol.java
│   │   ├── Scrim.java
│   │   └── Usuario.java
│   ├── notification/                   # Interface para Facade
│   │   └── NotificationService.java
│   ├── state/                          # Patrón State
│   │   ├── BuscandoState.java
│   │   ├── EnCursoState.java
│   │   ├── FinalizadoState.java
│   │   ├── LobbyArmadoState.java
│   │   └── ScrimState.java
│   └── strategy/                       # Patrón Strategy
│       ├── ByLatenciaStrategy.java
│       ├── ByMMRStrategy.java
│       ├── FIFOStrategy.java
│       └── SelectionStrategy.java
├── infra/
│   ├── notification/                   # Implementaciones Facade
│   │   ├── ConsoleNotificationSubscriber.java
│   │   ├── EmailNotificationService.java
│   │   ├── MultiChannelNotificationSubscriber.java
│   │   ├── NotificationFacade.java
│   │   ├── PushNotificationService.java
│   │   └── SMSNotificationService.java
│   └── persistence/                    # Patrón Repository
│       └── inmemory/
│           ├── InMemoryPostulacionRepository.java
│           ├── InMemoryScrimRepository.java
│           └── InMemoryUsuarioRepository.java
├── repository/                         # Interfaces de repositorio
│   ├── PostulacionRepository.java
│   ├── ScrimRepository.java
│   └── UsuarioRepository.java
└── service/
    └── ScrimService.java               # Servicio de aplicación
```

## Compilación

Este proyecto usa Java vanilla sin Maven. Para compilar:

### Opción 1: Usar el script de PowerShell (Recomendado)

```powershell
.\compilar.ps1
```

### Opción 2: Compilar manualmente

```powershell
# Limpiar y crear directorio de salida
Remove-Item -Recurse -Force out -ErrorAction SilentlyContinue
mkdir out

# Obtener todos los archivos .java y compilar
$files = Get-ChildItem -Path src\main\java -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d out $files
```

**Nota importante**: Es necesario usar `-encoding UTF-8` para que compile correctamente los archivos con caracteres especiales.

## Ejecución

### Requisitos
- JDK 11 o superior (recomendado: JDK 17)

### Opción 1: Usar el script de PowerShell (Recomendado)

```powershell
# Demo completo (6 patrones)
.\ejecutar.ps1 -Demo completo

# Demo básico (3 patrones: State, Strategy, Observer)
.\ejecutar.ps1 -Demo basico
```

### Opción 2: Ejecutar manualmente

```powershell
# Demo completo que muestra todos los patrones
java -cp out com.escrims.app.CompleteDemoMain

# Demo básico (State + Strategy + Observer)
java -cp out com.escrims.app.DemoMain
```

## Salida Esperada

### Demo Completo (CompleteDemoMain)

```
============================================================
        eScrims - Demo Completo de Patrones de Diseño      
============================================================

CONFIGURACION DEL SISTEMA

>> Configurando sistema de notificaciones (Patrón FACADE)...
   OK - 3 canales de notificacion configurados

============================================================
DEMO 1: Patrón BUILDER - Construcción fluida de Scrims
============================================================

>> Creando scrim CASUAL con Builder...
   OK - Scrim casual creado - Rango MMR: 0-3000, Latencia max: 100ms

>> Creando scrim COMPETITIVO con Builder...
   OK - Scrim competitivo creado - Rango MMR: 1500-2500, Latencia max: 50ms

>> Creando scrim PROFESIONAL con Builder...
   OK - Scrim profesional creado - Rango MMR: 2000-3000, Latencia max: 30ms

============================================================
DEMO 2: Patrón STRATEGY - Diferentes estrategias de selección
============================================================

Usuarios disponibles:
  1. ProPlayer - MMR: 2500, Latencia: 20ms
  2. CasualGamer - MMR: 1200, Latencia: 60ms
  3. MidTierPlayer - MMR: 1800, Latencia: 35ms

>> Comparando estrategias de selección:
   Strategy 1: BY_MMR (selecciona jugadores con mayor skill)
   Strategy 2: FIFO (orden de llegada)
   Strategy 3: BY_LATENCIA (menor ping primero)

============================================================
DEMO 3: Integración - State + Strategy + Observer + Facade
============================================================

>> Creando scrim con Builder (patrón BUILDER)...
   OK - Scrim creado - Estado: BUSCANDO

>> Recibiendo postulaciones...
   OK - 6 postulaciones recibidas

>> Procesando postulaciones (patron STRATEGY: ByMMRStrategy)...
   OK - Participantes seleccionados: 4/6
   OK - Estado actual (patron STATE): BUSCANDO

[... eventos y notificaciones multi-canal ...]

============================================================
>> RESUMEN DE PATRONES DEMOSTRADOS
============================================================

1. OK - STATE       - Ciclo de vida: BUSCANDO -> LOBBY_ARMADO -> EN_CURSO -> FINALIZADO
2. OK - STRATEGY    - Tres estrategias: ByMMR, FIFO, ByLatencia
3. OK - OBSERVER    - Event Bus + Multiples subscribers
4. OK - REPOSITORY  - Abstraccion de persistencia (Usuario, Scrim, Postulacion)
5. OK - BUILDER     - Construccion fluida (casual, competitivo, profesional)
6. OK - FACADE      - NotificationFacade simplifica Email + Push + SMS

============================================================
Total de patrones implementados: 6
============================================================
```

## Detalles de Implementación

### Patrón State
El ciclo de vida de un `Scrim` está modelado con el patrón State:
- Cada estado conoce sus transiciones válidas
- Las transiciones se realizan mediante `scrim.transicionarEstado()`
- El servicio publica eventos cuando el estado cambia

### Patrón Strategy
La selección de jugadores es configurable mediante estrategias:
- `ByMMRStrategy`: Prioriza jugadores con mayor MMR (skill rating)
- `FIFOStrategy`: Acepta por orden de llegada
- `ByLatenciaStrategy`: Prioriza jugadores con menor latencia

### Patrón Observer
Los cambios de estado generan eventos de dominio:
- `ScrimStateChanged` se publica en cada transición
- Los subscribers (ej: `ConsoleNotificationSubscriber`) reaccionan a los eventos
- Implementación desacoplada mediante el bus de eventos

### Patrón Repository
Abstrae el acceso a datos:
- Interfaces definen contratos (`findById`, `save`, `findAll`)
- Implementaciones in-memory para desarrollo
- Fácil cambio a BD real en el futuro

### Patrón Builder
Construcción fluida de Scrims complejos:
- Métodos factory preconfigurados para diferentes niveles (casual, competitivo, profesional)
- API fluida con métodos encadenables (`withJuego().withFormato()...`)
- Validaciones en el método `build()`

### Patrón Facade
Simplificación del sistema de notificaciones:
- Interfaz única para coordinar múltiples canales (Email, Push, SMS)
- Métodos convenientes para casos de uso comunes
- Oculta la complejidad de gestionar múltiples servicios

## Convenciones de Código

- Paquetes: `com.escrims.*`
- Clases: PascalCase
- Métodos/variables: camelCase
- Constantes: UPPER_SNAKE_CASE
- Imports organizados por grupo (java.*, dominio, infra)

## Tecnologías

- **Lenguaje**: Java 17
- **Build**: Compilación manual con `javac` (sin Maven/Gradle)
- **Arquitectura**: Domain-Driven Design (DDD)
- **Persistencia**: In-memory (implementaciones stub)

## Licencia

Proyecto académico - UADE - Proceso y Desarrollo de Software
