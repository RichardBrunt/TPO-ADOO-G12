package com.escrims.view.console;

import com.escrims.model.domain.events.InMemoryDomainEventBus;
import com.escrims.model.domain.events.ScrimStateChanged;
import com.escrims.model.domain.model.*;
import com.escrims.model.domain.strategy.ByMMRStrategy;
import com.escrims.model.domain.strategy.SelectionStrategy;
import com.escrims.infra.notification.ConsoleNotificationSubscriber;
import com.escrims.infra.persistence.inmemory.*;
import com.escrims.model.repository.*;
import com.escrims.model.service.ScrimService;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * Demo completo que muestra los patrones State, Strategy y Observer en acciÃ³n.
 */
public class DemoMain {
    public static void main(String[] args) {
        System.out.println("=== eScrims - Demo de Patrones ===\n");
        
        // 1. Configurar infraestructura
        InMemoryDomainEventBus bus = new InMemoryDomainEventBus();
        ConsoleNotificationSubscriber consoleSub = new ConsoleNotificationSubscriber();
        bus.registerSubscriber(ScrimStateChanged.class, consoleSub);
        
        UsuarioRepository usuarioRepo = new InMemoryUsuarioRepository();
        ScrimRepository scrimRepo = new InMemoryScrimRepository();
        PostulacionRepository postulacionRepo = new InMemoryPostulacionRepository();
        
        // 2. Crear estrategia de selecciÃ³n (patrÃ³n Strategy)
        SelectionStrategy strategy = new ByMMRStrategy();
        
        // 3. Crear servicio
        ScrimService service = new ScrimService(scrimRepo, usuarioRepo, postulacionRepo, bus, strategy);
        
        // 4. Crear usuarios de prueba
        System.out.println(">> Creando usuarios...");
        Usuario u1 = new Usuario(UUID.randomUUID(), "player1", "p1@test.com", "hash1",
            Region.SA, 1500, 30, Set.of(Rol.DUELIST));
        Usuario u2 = new Usuario(UUID.randomUUID(), "player2", "p2@test.com", "hash2",
            Region.SA, 1800, 25, Set.of(Rol.SUPPORT));
        Usuario u3 = new Usuario(UUID.randomUUID(), "player3", "p3@test.com", "hash3",
            Region.SA, 1200, 40, Set.of(Rol.FLEX));
        Usuario u4 = new Usuario(UUID.randomUUID(), "player4", "p4@test.com", "hash4",
            Region.SA, 1600, 35, Set.of(Rol.DUELIST));
        Usuario u5 = new Usuario(UUID.randomUUID(), "player5", "p5@test.com", "hash5",
            Region.SA, 1400, 28, Set.of(Rol.SUPPORT));
        Usuario u6 = new Usuario(UUID.randomUUID(), "player6", "p6@test.com", "hash6",
            Region.SA, 1700, 32, Set.of(Rol.FLEX));
        
        usuarioRepo.save(u1);
        usuarioRepo.save(u2);
        usuarioRepo.save(u3);
        usuarioRepo.save(u4);
        usuarioRepo.save(u5);
        usuarioRepo.save(u6);
        System.out.println("   6 usuarios creados (MMR range: 1200-1800)");
        
        // 5. Crear scrim (patrÃ³n State: inicia en BUSCANDO)
        System.out.println("\n>> Creando scrim...");
        Scrim scrim = new Scrim(
            UUID.randomUUID(),
            Juego.VALORANT,
            Formato.TRES_VS_TRES, // 6 jugadores total
            Region.SA,
            1000, 2000, // rango MMR
            50, // latencia max
            LocalDateTime.now().plusHours(2),
            60 // duraciÃ³n
        );
        scrimRepo.save(scrim);
        System.out.println("   Scrim creado con ID: " + scrim.getId());
        System.out.println("   Estado inicial: " + scrim.getEstado().getNombre());
        System.out.println("   Cupos totales: " + scrim.getCupoTotal());
        
        // 6. Crear postulaciones
        System.out.println("\n>> Creando postulaciones...");
        Postulacion post1 = new Postulacion(UUID.randomUUID(), u1.getId(), scrim.getId(), Rol.DUELIST);
        Postulacion post2 = new Postulacion(UUID.randomUUID(), u2.getId(), scrim.getId(), Rol.SUPPORT);
        Postulacion post3 = new Postulacion(UUID.randomUUID(), u3.getId(), scrim.getId(), Rol.FLEX);
        Postulacion post4 = new Postulacion(UUID.randomUUID(), u4.getId(), scrim.getId(), Rol.DUELIST);
        Postulacion post5 = new Postulacion(UUID.randomUUID(), u5.getId(), scrim.getId(), Rol.SUPPORT);
        Postulacion post6 = new Postulacion(UUID.randomUUID(), u6.getId(), scrim.getId(), Rol.FLEX);
        
        postulacionRepo.save(post1);
        postulacionRepo.save(post2);
        postulacionRepo.save(post3);
        postulacionRepo.save(post4);
        postulacionRepo.save(post5);
        postulacionRepo.save(post6);
        System.out.println("   6 postulaciones creadas");
        
        // 7. Procesar postulaciones (usa Strategy para seleccionar)
        System.out.println("\n>> Procesando postulaciones con estrategia: " + strategy.getNombre());
        service.procesarPostulaciones(scrim.getId());
        
        // Recargar scrim
        scrim = scrimRepo.findById(scrim.getId()).get();
        System.out.println("   Participantes aceptados: " + scrim.getParticipantes().size());
        System.out.println("   Estado despuÃ©s de procesar: " + scrim.getEstado().getNombre());
        
        // 8. Simular confirmaciones
        System.out.println("\n>> Simulando confirmaciones de jugadores...");
        for (UUID participanteId : scrim.getParticipantes()) {
            service.confirmarParticipacion(scrim.getId(), participanteId);
        }
        
        scrim = scrimRepo.findById(scrim.getId()).get();
        System.out.println("   Todos confirmados: " + scrim.todosConfirmados());
        System.out.println("   Estado despuÃ©s de confirmar: " + scrim.getEstado().getNombre());
        
        // 9. Finalizar scrim
        System.out.println("\n>> Finalizando scrim...");
        service.finalizarScrim(scrim.getId());
        
        scrim = scrimRepo.findById(scrim.getId()).get();
        System.out.println("   Estado final: " + scrim.getEstado().getNombre());
        
        System.out.println("\n=== Demo completado ===");
        System.out.println("\nPatrones demostrados:");
        System.out.println("  âœ“ State: Scrim transicionÃ³ por BUSCANDO -> LOBBY_ARMADO -> EN_CURSO -> FINALIZADO");
        System.out.println("  âœ“ Strategy: SelecciÃ³n de jugadores usando " + strategy.getNombre());
        System.out.println("  âœ“ Observer: Eventos ScrimStateChanged publicados y manejados por ConsoleNotificationSubscriber");
    }
}

