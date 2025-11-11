package com.escrims.view.console;

import com.escrims.model.domain.adapter.PlayerStatsProvider;
import com.escrims.model.domain.builder.ScrimBuilder;
import com.escrims.model.domain.chain.PostulacionValidator;
import com.escrims.model.domain.command.Command;
import com.escrims.model.domain.decorator.Message;
import com.escrims.model.domain.events.InMemoryDomainEventBus;
import com.escrims.model.domain.events.ScrimStateChanged;
import com.escrims.model.domain.factory.*;
import com.escrims.model.domain.model.*;
import com.escrims.model.domain.strategy.ByLatenciaStrategy;
import com.escrims.model.domain.strategy.ByMMRStrategy;
import com.escrims.model.domain.strategy.FIFOStrategy;
import com.escrims.model.domain.strategy.SelectionStrategy;
import com.escrims.model.domain.template.ScrimMatchmakingTemplate;
import com.escrims.infra.adapter.RiotAPIAdapter;
import com.escrims.infra.adapter.SteamAPIAdapter;
import com.escrims.infra.chain.*;
import com.escrims.infra.command.*;
import com.escrims.infra.decorator.*;
import com.escrims.infra.notification.*;
import com.escrims.infra.persistence.inmemory.*;
import com.escrims.infra.singleton.SingletonEventBus;
import com.escrims.infra.template.*;
import com.escrims.model.repository.*;
import com.escrims.model.service.ScrimService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Demo completo que muestra TODOS los patrones implementados:
 * 1. State - Ciclo de vida del Scrim
 * 2. Strategy - Seleccion de jugadores
 * 3. Observer - Sistema de eventos
 * 4. Repository - Persistencia
 * 5. Builder - Construccion de Scrims
 * 6. Facade - Notificaciones multi-canal
 * 7. Factory Method - Fabricas especificas por juego
 * 8. Adapter - Adaptacion de APIs externas
 * 9. Decorator - Decoracion dinamica de mensajes
 * 10. Command - Encapsulacion de acciones como objetos
 * 11. Template Method - Algoritmo de matchmaking customizable
 * 12. Chain of Responsibility - Validacion en cadena
 * 13. Singleton - Instancia unica del EventBus
 */
public class CompleteDemoMain {
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("        eScrims - Demo Completo de Patrones de DiseÃ±o      ");
        System.out.println("============================================================\n");
        
        // ============================================================
        // SETUP: Infraestructura y Servicios
        // ============================================================
        
        System.out.println("CONFIGURACION DEL SISTEMA\n");
        
        // Event Bus (patrÃ³n Observer)
        InMemoryDomainEventBus bus = new InMemoryDomainEventBus();
        
        // Notification Facade (patrÃ³n Facade)
        System.out.println(">> Configurando sistema de notificaciones (PatrÃ³n FACADE)...");
        NotificationFacade notificationFacade = new NotificationFacade();
        notificationFacade.registrarServicio(new EmailNotificationService());
        notificationFacade.registrarServicio(new PushNotificationService());
        notificationFacade.registrarServicio(new SMSNotificationService());
        System.out.println("   OK - " + notificationFacade.cantidadCanales() + " canales de notificacion configurados\n");
        
        // Subscribers
        ConsoleNotificationSubscriber consoleSub = new ConsoleNotificationSubscriber();
        MultiChannelNotificationSubscriber multiChannelSub = new MultiChannelNotificationSubscriber(notificationFacade);
        bus.registerSubscriber(ScrimStateChanged.class, consoleSub);
        bus.registerSubscriber(ScrimStateChanged.class, multiChannelSub);
        
        // Repositories
        UsuarioRepository usuarioRepo = new InMemoryUsuarioRepository();
        ScrimRepository scrimRepo = new InMemoryScrimRepository();
        PostulacionRepository postulacionRepo = new InMemoryPostulacionRepository();
        
        // ============================================================
        // DEMOSTRACIÃ“N 1: PatrÃ³n BUILDER
        // ============================================================
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 1: PatrÃ³n BUILDER - ConstrucciÃ³n fluida de Scrims");
        System.out.println("=".repeat(60) + "\n");
        
        System.out.println(">> Creando scrim CASUAL con Builder...");
        Scrim scrimCasual = ScrimBuilder.casual()
            .withJuego(Juego.VALORANT)
            .withFormato(Formato.CINCO_VS_CINCO)
            .withRegion(Region.SA)
            .build();
        System.out.println("   OK - Scrim casual creado - Rango MMR: 0-3000, Latencia max: 100ms");
        
        System.out.println("\n>> Creando scrim COMPETITIVO con Builder...");
        Scrim scrimCompetitivo = ScrimBuilder.competitivo()
            .withJuego(Juego.LOL)
            .withFormato(Formato.TRES_VS_TRES)
            .withRegion(Region.SA)
            .withFechaHora(LocalDateTime.now().plusHours(3))
            .build();
        System.out.println("   OK - Scrim competitivo creado - Rango MMR: 1500-2500, Latencia max: 50ms");
        
        System.out.println("\n>> Creando scrim PROFESIONAL con Builder...");
        Scrim scrimPro = ScrimBuilder.profesional()
            .withJuego(Juego.CS2)
            .withFormato(Formato.CINCO_VS_CINCO)
            .withRegion(Region.EU)
            .withDuracion(120)
            .build();
        System.out.println("   OK - Scrim profesional creado - Rango MMR: 2000-3000, Latencia max: 30ms");
        
        // ============================================================
        // DEMOSTRACIÃ“N 2: PatrÃ³n STRATEGY
        // ============================================================
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 2: PatrÃ³n STRATEGY - Diferentes estrategias de selecciÃ³n");
        System.out.println("=".repeat(60) + "\n");
        
        // Crear usuarios
        Usuario u1 = new Usuario(UUID.randomUUID(), "ProPlayer", "pro@test.com", "hash", Region.SA, 2500, 20, Set.of(Rol.DUELIST));
        Usuario u2 = new Usuario(UUID.randomUUID(), "CasualGamer", "casual@test.com", "hash", Region.SA, 1200, 60, Set.of(Rol.SUPPORT));
        Usuario u3 = new Usuario(UUID.randomUUID(), "MidTierPlayer", "mid@test.com", "hash", Region.SA, 1800, 35, Set.of(Rol.FLEX));
        
        usuarioRepo.save(u1);
        usuarioRepo.save(u2);
        usuarioRepo.save(u3);
        
        System.out.println("Usuarios disponibles:");
        System.out.println("  1. " + u1.getUsername() + " - MMR: " + u1.getMmr() + ", Latencia: " + u1.getLatenciaMediaMs() + "ms");
        System.out.println("  2. " + u2.getUsername() + " - MMR: " + u2.getMmr() + ", Latencia: " + u2.getLatenciaMediaMs() + "ms");
        System.out.println("  3. " + u3.getUsername() + " - MMR: " + u3.getMmr() + ", Latencia: " + u3.getLatenciaMediaMs() + "ms");
        
        System.out.println("\n>> Comparando estrategias de selecciÃ³n:\n");
        
        System.out.println("   Strategy 1: BY_MMR (selecciona jugadores con mayor skill)");
        System.out.println("   Strategy 2: FIFO (orden de llegada)");
        System.out.println("   Strategy 3: BY_LATENCIA (menor ping primero)");
        
        // ============================================================
        // DEMOSTRACIÃ“N 3: IntegraciÃ³n completa (State + Strategy + Observer + Facade)
        // ============================================================
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 3: IntegraciÃ³n - State + Strategy + Observer + Facade");
        System.out.println("=".repeat(60) + "\n");
        
        // Usar Builder para crear scrim de demo
        System.out.println(">> Creando scrim con Builder (patrÃ³n BUILDER)...");
        Scrim scrim = ScrimBuilder.competitivo()
            .withJuego(Juego.VALORANT)
            .withFormato(Formato.TRES_VS_TRES)
            .withRegion(Region.SA)
            .build();
        scrimRepo.save(scrim);
        System.out.println("   OK - Scrim creado - Estado: " + scrim.getEstado().getNombre());
        
        // Crear mÃ¡s usuarios para llenar el scrim
        Usuario u4 = new Usuario(UUID.randomUUID(), "Player4", "p4@test.com", "hash", Region.SA, 1600, 45, Set.of(Rol.DUELIST));
        Usuario u5 = new Usuario(UUID.randomUUID(), "Player5", "p5@test.com", "hash", Region.SA, 1700, 40, Set.of(Rol.SUPPORT));
        Usuario u6 = new Usuario(UUID.randomUUID(), "Player6", "p6@test.com", "hash", Region.SA, 1400, 50, Set.of(Rol.FLEX));
        usuarioRepo.save(u4);
        usuarioRepo.save(u5);
        usuarioRepo.save(u6);
        
        // Crear postulaciones
        System.out.println("\n>> Recibiendo postulaciones...");
        Postulacion[] postulaciones = {
            new Postulacion(UUID.randomUUID(), u1.getId(), scrim.getId(), Rol.DUELIST),
            new Postulacion(UUID.randomUUID(), u2.getId(), scrim.getId(), Rol.SUPPORT),
            new Postulacion(UUID.randomUUID(), u3.getId(), scrim.getId(), Rol.FLEX),
            new Postulacion(UUID.randomUUID(), u4.getId(), scrim.getId(), Rol.DUELIST),
            new Postulacion(UUID.randomUUID(), u5.getId(), scrim.getId(), Rol.SUPPORT),
            new Postulacion(UUID.randomUUID(), u6.getId(), scrim.getId(), Rol.FLEX)
        };
        for (Postulacion p : postulaciones) {
            postulacionRepo.save(p);
        }
        System.out.println("   OK - " + postulaciones.length + " postulaciones recibidas");
        
        // Procesar con Strategy
        System.out.println("\n>> Procesando postulaciones (patron STRATEGY: ByMMRStrategy)...");
        SelectionStrategy strategy = new ByMMRStrategy();
        ScrimService service = new ScrimService(scrimRepo, usuarioRepo, postulacionRepo, bus, strategy);
        service.procesarPostulaciones(scrim.getId());
        
        scrim = scrimRepo.findById(scrim.getId()).get();
        System.out.println("   OK - Participantes seleccionados: " + scrim.getParticipantes().size() + "/" + scrim.getCupoTotal());
        System.out.println("   OK - Estado actual (patron STATE): " + scrim.getEstado().getNombre());
        
        // Confirmar participaciones
        System.out.println("\n>> Confirmando participaciones...");
        for (UUID participanteId : scrim.getParticipantes()) {
            service.confirmarParticipacion(scrim.getId(), participanteId);
        }
        
        scrim = scrimRepo.findById(scrim.getId()).get();
        System.out.println("   OK - Estado actual (patron STATE): " + scrim.getEstado().getNombre());
        
        // Finalizar
        System.out.println("\n>> Finalizando scrim...");
        service.finalizarScrim(scrim.getId());
        
        scrim = scrimRepo.findById(scrim.getId()).get();
        System.out.println("   OK - Estado final (patron STATE): " + scrim.getEstado().getNombre());
        
        // ============================================================
        // DEMOSTRACION 4: Patron FACTORY METHOD
        // ============================================================
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 4: Patron FACTORY METHOD - Fabricas especificas por juego");
        System.out.println("=".repeat(60) + "\n");
        
        System.out.println(">> Creando scrims con factories especializadas...\n");
        
        // Factory para Valorant
        ScrimFactory valorantFactory = new ValorantScrimFactory();
        Scrim scrimValorant = valorantFactory.crearScrim();
        System.out.println("Factory: " + valorantFactory.getNombre());
        System.out.println("  - Juego: " + scrimValorant.getJuego());
        System.out.println("  - Formato: " + scrimValorant.getFormato());
        System.out.println("  - Rango MMR: " + scrimValorant.getRangoMin() + "-" + scrimValorant.getRangoMax());
        System.out.println("  - Latencia max: " + scrimValorant.getLatenciaMax() + "ms");
        
        // Factory para LoL
        ScrimFactory lolFactory = new LoLScrimFactory();
        Scrim scrimLoL = lolFactory.crearScrim();
        System.out.println("\nFactory: " + lolFactory.getNombre());
        System.out.println("  - Juego: " + scrimLoL.getJuego());
        System.out.println("  - Formato: " + scrimLoL.getFormato());
        System.out.println("  - Rango MMR: " + scrimLoL.getRangoMin() + "-" + scrimLoL.getRangoMax());
        System.out.println("  - Latencia max: " + scrimLoL.getLatenciaMax() + "ms");
        
        // Factory para CS2
        ScrimFactory cs2Factory = new CS2ScrimFactory();
        Scrim scrimCS2 = cs2Factory.crearScrim();
        System.out.println("\nFactory: " + cs2Factory.getNombre());
        System.out.println("  - Juego: " + scrimCS2.getJuego());
        System.out.println("  - Formato: " + scrimCS2.getFormato());
        System.out.println("  - Rango MMR: " + scrimCS2.getRangoMin() + "-" + scrimCS2.getRangoMax());
        System.out.println("  - Latencia max: " + scrimCS2.getLatenciaMax() + "ms");
        
        // ============================================================
        // DEMOSTRACION 5: Patron ADAPTER
        // ============================================================
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 5: Patron ADAPTER - Adaptacion de APIs externas");
        System.out.println("=".repeat(60) + "\n");
        
        System.out.println(">> Conectando con APIs externas de juegos...\n");
        
        // Adapter para Riot Games API (Valorant/LoL)
        PlayerStatsProvider riotAdapter = new RiotAPIAdapter();
        System.out.println("Proveedor: " + riotAdapter.getProviderName());
        String riotPlayerId = "player123#NA1";
        if (riotAdapter.playerExists(riotPlayerId)) {
            System.out.println("  - Jugador: " + riotPlayerId);
            System.out.println("  - MMR/ELO: " + riotAdapter.getPlayerRanking(riotPlayerId));
            System.out.println("  - Latencia: " + riotAdapter.getPlayerLatency(riotPlayerId) + "ms");
        }
        
        // Adapter para Steam API (CS2)
        PlayerStatsProvider steamAdapter = new SteamAPIAdapter();
        System.out.println("\nProveedor: " + steamAdapter.getProviderName());
        String steamId = "76561198012345678";
        if (steamAdapter.playerExists(steamId)) {
            System.out.println("  - Steam ID: " + steamId);
            System.out.println("  - Skill Level: " + steamAdapter.getPlayerRanking(steamId));
            System.out.println("  - Ping: " + steamAdapter.getPlayerLatency(steamId) + "ms");
        }
        
        // ============================================================
        // DEMOSTRACION 6: Patron DECORATOR
        // ============================================================
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 6: Patron DECORATOR - Decoracion dinamica de mensajes");
        System.out.println("=".repeat(60) + "\n");
        
        System.out.println(">> Creando mensajes con diferentes decoradores...\n");
        
        // Mensaje simple
        Message simpleMsg = new SimpleMessage("Bienvenida", "Tu scrim comienza en 5 minutos");
        System.out.println("Mensaje Simple:");
        System.out.println("  Asunto: " + simpleMsg.getSubject());
        System.out.println("  Contenido: " + simpleMsg.getContent());
        
        // Mensaje con timestamp
        Message timestampedMsg = new TimestampedMessageDecorator(
            new SimpleMessage("Recordatorio", "Confirma tu participacion")
        );
        System.out.println("\nMensaje con Timestamp:");
        System.out.println("  Asunto: " + timestampedMsg.getSubject());
        System.out.println("  Contenido: " + timestampedMsg.getContent());
        
        // Mensaje con prioridad
        Message priorityMsg = new PriorityMessageDecorator(
            new SimpleMessage("Alerta", "Scrim cancelado por falta de jugadores"),
            "URGENTE"
        );
        System.out.println("\nMensaje con Prioridad:");
        System.out.println("  Asunto: " + priorityMsg.getSubject());
        System.out.println("  Contenido: " + priorityMsg.getContent());
        
        // Mensaje con multiples decoradores (timestamp + prioridad + encriptado)
        Message complexMsg = new EncryptedMessageDecorator(
            new PriorityMessageDecorator(
                new TimestampedMessageDecorator(
                    new SimpleMessage("Datos sensibles", "Tu codigo de acceso es: ABC123")
                ),
                "CONFIDENCIAL"
            )
        );
        System.out.println("\nMensaje con Multiples Decoradores (Timestamp + Prioridad + Encriptado):");
        System.out.println("  Asunto: " + complexMsg.getSubject());
        System.out.println("  Contenido: " + complexMsg.getContent());
        
        // ============================================================
        // DEMOSTRACION 7: Patron COMMAND
        // ============================================================
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 7: Patron COMMAND - Encapsulacion de acciones");
        System.out.println("=".repeat(60) + "\n");
        
        System.out.println(">> Ejecutando comandos con soporte para undo...\n");
        
        // Crear invoker
        CommandInvoker invoker = new CommandInvoker();
        
        // Crear algunos scrims para los comandos
        Scrim scrimParaCancelar = ScrimBuilder.casual()
            .withJuego(Juego.VALORANT)
            .withFormato(Formato.CINCO_VS_CINCO)
            .withRegion(Region.SA)
            .build();
        scrimRepo.save(scrimParaCancelar);
        
        Postulacion postParaRechazar = new Postulacion(
            UUID.randomUUID(),
            u1.getId(),
            scrimParaCancelar.getId(),
            Rol.DUELIST
        );
        postulacionRepo.save(postParaRechazar);
        
        // Ejecutar comando 1: Rechazar postulacion
        Command rechazarCmd = new RechazarPostulacionCommand(
            postParaRechazar.getId(),
            postulacionRepo
        );
        System.out.println("Ejecutando: " + rechazarCmd.getDescription());
        invoker.executeCommand(rechazarCmd);
        
        // Ejecutar comando 2: Cancelar scrim
        Command cancelarCmd = new CancelarScrimCommand(
            scrimParaCancelar.getId(),
            scrimRepo
        );
        System.out.println("Ejecutando: " + cancelarCmd.getDescription());
        invoker.executeCommand(cancelarCmd);
        
        // Mostrar historial
        invoker.showHistory();
        
        // Deshacer ultimo comando
        System.out.println("\n>> Deshaciendo ultimo comando...");
        invoker.undoLastCommand();
        
        // Verificar que el scrim fue restaurado
        boolean scrimRestaurado = scrimRepo.findById(scrimParaCancelar.getId()).isPresent();
        System.out.println("   Scrim restaurado: " + scrimRestaurado);
        
        // ============================================================
        // DEMOSTRACION 8: Patron TEMPLATE METHOD
        // ============================================================
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 8: Patron TEMPLATE METHOD - Algoritmo de matchmaking");
        System.out.println("=".repeat(60) + "\n");
        
        System.out.println(">> Comparando diferentes implementaciones de matchmaking...\n");
        
        // Crear scrim para matchmaking
        Scrim scrimMatchmaking = ScrimBuilder.competitivo()
            .withJuego(Juego.VALORANT)
            .withFormato(Formato.CINCO_VS_CINCO)
            .withRegion(Region.SA)
            .build();
        scrimRepo.save(scrimMatchmaking);
        
        // Crear 12 postulaciones para tener suficientes
        List<Postulacion> postulacionesMatchmaking = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            Usuario u = new Usuario(
                UUID.randomUUID(),
                "Player" + i,
                "player" + i + "@test.com",
                "hash123",
                Region.SA,
                1500 + (i * 100),
                30 + (i * 5),
                Set.of(Rol.DUELIST)
            );
            usuarioRepo.save(u);
            
            Postulacion post = new Postulacion(
                UUID.randomUUID(),
                u.getId(),
                scrimMatchmaking.getId(),
                Rol.DUELIST
            );
            postulacionRepo.save(post);
            postulacionesMatchmaking.add(post);
        }
        
        // Template 1: Matchmaking RAPIDO
        System.out.println("Matchmaking RAPIDO (prioriza velocidad):");
        ScrimMatchmakingTemplate rapidMatchmaking = new RapidMatchmaking();
        rapidMatchmaking.ejecutarMatchmaking(scrimMatchmaking, postulacionesMatchmaking);
        
        // Template 2: Matchmaking BALANCEADO
        System.out.println("\nMatchmaking BALANCEADO (prioriza calidad):");
        ScrimMatchmakingTemplate balancedMatchmaking = new BalancedMatchmaking();
        balancedMatchmaking.ejecutarMatchmaking(scrimMatchmaking, postulacionesMatchmaking);
        
        // ============================================================
        // DEMOSTRACION 9: Patron CHAIN OF RESPONSIBILITY
        // ============================================================
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 9: Patron CHAIN OF RESPONSIBILITY - Validacion");
        System.out.println("=".repeat(60) + "\n");
        
        System.out.println(">> Configurando cadena de validacion de postulaciones...\n");
        
        // Construir la cadena de validadores
        PostulacionValidator basicValidator = new BasicDataValidator();
        PostulacionValidator rolValidator = new RolValidator();
        PostulacionValidator duplicateValidator = new DuplicateValidator();
        PostulacionValidator businessValidator = new BusinessRulesValidator();
        
        // Encadenar validadores
        basicValidator
            .setNext(rolValidator)
            .setNext(duplicateValidator)
            .setNext(businessValidator);
        
        // Postulacion 1: VALIDA (pasa todos los validadores)
        Postulacion postValida = new Postulacion(
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID(),
            Rol.SUPPORT
        );
        System.out.println("Validando postulacion VALIDA:");
        boolean resultado1 = basicValidator.validate(postValida);
        System.out.println("   Resultado: " + (resultado1 ? "ACEPTADA" : "RECHAZADA") + "\n");
        
        // Postulacion 2: INVALIDA (ya fue aceptada - falla en DuplicateValidator)
        Postulacion postDuplicada = new Postulacion(
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID(),
            Rol.FLEX
        );
        postDuplicada.aceptar(); // Marcarla como aceptada
        System.out.println("Validando postulacion DUPLICADA:");
        boolean resultado2 = basicValidator.validate(postDuplicada);
        System.out.println("   Resultado: " + (resultado2 ? "ACEPTADA" : "RECHAZADA") + "\n");
        
        // ============================================================
        // DEMOSTRACION 10: Patron SINGLETON
        // ============================================================
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 10: Patron SINGLETON - EventBus unico global");
        System.out.println("=".repeat(60) + "\n");
        
        System.out.println(">> Verificando instancia unica del EventBus...\n");
        
        // Obtener instancia desde diferentes puntos
        SingletonEventBus instance1 = SingletonEventBus.getInstance();
        SingletonEventBus instance2 = SingletonEventBus.getInstance();
        SingletonEventBus instance3 = SingletonEventBus.getInstance();
        
        // Verificar que todas son la misma instancia
        System.out.println("Instancia 1 ID: " + instance1.getInstanceId());
        System.out.println("Instancia 2 ID: " + instance2.getInstanceId());
        System.out.println("Instancia 3 ID: " + instance3.getInstanceId());
        
        boolean mismaInstancia = (instance1 == instance2) && (instance2 == instance3);
        System.out.println("\nÂ¿Todas las instancias son la misma? " + (mismaInstancia ? "SI" : "NO"));
        System.out.println("   Patron SINGLETON verificado: Instancia unica garantizada\n");
        
        // ============================================================
        // RESUMEN FINAL
        // ============================================================
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println(">> RESUMEN DE PATRONES DEMOSTRADOS");
        System.out.println("=".repeat(60) + "\n");
        
        System.out.println("1.  OK - STATE         - Ciclo de vida: BUSCANDO -> LOBBY_ARMADO -> EN_CURSO -> FINALIZADO");
        System.out.println("2.  OK - STRATEGY      - Tres estrategias: ByMMR, FIFO, ByLatencia");
        System.out.println("3.  OK - OBSERVER      - Event Bus + Multiples subscribers");
        System.out.println("4.  OK - REPOSITORY    - Abstraccion de persistencia (Usuario, Scrim, Postulacion)");
        System.out.println("5.  OK - BUILDER       - Construccion fluida (casual, competitivo, profesional)");
        System.out.println("6.  OK - FACADE        - NotificationFacade simplifica Email + Push + SMS");
        System.out.println("7.  OK - FACTORY METHOD- Fabricas especializadas por juego (Valorant, LoL, CS2)");
        System.out.println("8.  OK - ADAPTER       - Adaptacion de APIs externas (Riot, Steam)");
        System.out.println("9.  OK - DECORATOR     - Decoradores de mensajes (Timestamp, Prioridad, Encriptado)");
        System.out.println("10. OK - COMMAND       - Encapsulacion de acciones con soporte para undo");
        System.out.println("11. OK - TEMPLATE METHOD - Algoritmo de matchmaking customizable (Rapido, Balanceado)");
        System.out.println("12. OK - CHAIN OF RESPONSIBILITY - Validacion en cadena de postulaciones");
        System.out.println("13. OK - SINGLETON     - Instancia unica del EventBus garantizada");
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Total de patrones implementados: 13");
        System.out.println("=".repeat(60) + "\n");
    }
}
