package com.escrims.view.gui;

import com.escrims.model.domain.model.*;
import com.escrims.model.domain.state.*;
import com.escrims.controller.ScrimController;
import com.escrims.model.application.ApplicationModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Panel principal con pestañas para gestión de scrims
 */
public class DashboardPanel extends JPanel {
    
    private final ScrimController controller;
    private final ApplicationModel model;
    
    private JTabbedPane tabbedPane;
    
    // Tab 1: Crear Scrim
    private JComboBox<Juego> juegoCombo;
    private JComboBox<Formato> formatoCombo;
    private JComboBox<Region> regionCombo;
    private JComboBox<String> estrategiaCombo;
    private JSpinner mmrMinSpinner;
    private JSpinner mmrMaxSpinner;
    private JSpinner latenciaMaxSpinner;
    private JSpinner duracionSpinner;
    
    // Tab 2: Buscar Scrims
    private JTable scrimsTable;
    private DefaultTableModel scrimsTableModel;
    
    // Tab 3: Mis Postulaciones
    private JTable postulacionesTable;
    private DefaultTableModel postulacionesTableModel;
    
    // Tab 4: Gestionar Postulaciones (de mis scrims)
    private JTable gestionPostulacionesTable;
    private DefaultTableModel gestionPostulacionesTableModel;
    
    // Tab 5: Gestionar Estados (mis scrims creados)
    private JTable misScrimsTable;
    private DefaultTableModel misScrimsTableModel;
    
    // Tab 6: Estadísticas
    private JTextArea statsArea;
    
    /**
     * Helper para convertir un Long ID a UUID válido
     * Toma el String del Long y crea un UUID con padding de ceros
     */
    private UUID longIdToUUID(String longIdStr) {
        // Crear un UUID basado en el Long ID
        // Formato: 00000000-0000-0000-0000-000000000001 para ID=1
        String paddedId = String.format("%012d", Long.parseLong(longIdStr));
        String uuidString = String.format("00000000-0000-0000-%s-%s",
            paddedId.substring(0, 4),
            paddedId.substring(4, 12));
        return UUID.fromString(uuidString);
    }
    
    public DashboardPanel(ScrimController controller, ApplicationModel model) {
        this.controller = controller;
        this.model = model;
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        
        // Panel superior con info del usuario
        add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Pestañas
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Crear Scrim", createCrearScrimTab());
        tabbedPane.addTab("Buscar Scrims", createBuscarScrimsTab());
        tabbedPane.addTab("Mis Postulaciones", createPostulacionesTab());
        tabbedPane.addTab("Gestionar Postulaciones", createGestionPostulacionesTab());
        tabbedPane.addTab("Gestionar Estados", createGestionEstadosTab());
        tabbedPane.addTab("Estadísticas", createEstadisticasTab());
        
        // Listener para cargar datos cuando se cambia de pestaña
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            switch (selectedIndex) {
                case 1: // Buscar Scrims
                    loadScrims();
                    break;
                case 2: // Mis Postulaciones
                    loadPostulaciones();
                    break;
                case 3: // Gestionar Postulaciones
                    loadPostulacionesRecibidas();
                    break;
                case 4: // Gestionar Estados
                    loadMisScrims();
                    break;
                case 5: // Estadísticas
                    loadEstadisticas();
                    break;
            }
        });
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Cargar scrims al inicio
        loadScrims();
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(63, 81, 181));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        Usuario currentUser = model.getUsuarioActual();
        String username = currentUser != null ? currentUser.getUsername() : "Usuario";
        
        JLabel welcomeLabel = new JLabel("Bienvenido, " + username);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        
        JButton logoutButton = new JButton("Cerrar Sesión");
        logoutButton.setBackground(new Color(244, 67, 54));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> {
            controller.handleLogout();
            JOptionPane.showMessageDialog(this, "Sesión cerrada exitosamente");
        });
        
        panel.add(welcomeLabel, BorderLayout.WEST);
        panel.add(logoutButton, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createCrearScrimTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        // Juego
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Juego:"), gbc);
        gbc.gridx = 1;
        juegoCombo = new JComboBox<>(Juego.values());
        panel.add(juegoCombo, gbc);
        
        // Formato
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Formato:"), gbc);
        gbc.gridx = 1;
        formatoCombo = new JComboBox<>(Formato.values());
        panel.add(formatoCombo, gbc);
        
        // Región
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Región:"), gbc);
        gbc.gridx = 1;
        regionCombo = new JComboBox<>(Region.values());
        panel.add(regionCombo, gbc);
        
        // Estrategia de Matchmaking
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Estrategia Matchmaking:"), gbc);
        gbc.gridx = 1;
        String[] estrategias = {"Por MMR", "Por Latencia", "Por Historial (FIFO)"};
        estrategiaCombo = new JComboBox<>(estrategias);
        estrategiaCombo.setToolTipText("Selecciona cómo se elegirán los jugadores para el scrim");
        panel.add(estrategiaCombo, gbc);
        
        // MMR Mínimo
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("MMR Mínimo:"), gbc);
        gbc.gridx = 1;
        mmrMinSpinner = new JSpinner(new SpinnerNumberModel(1000, 0, 3000, 100));
        panel.add(mmrMinSpinner, gbc);
        
        // MMR Máximo
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("MMR Máximo:"), gbc);
        gbc.gridx = 1;
        mmrMaxSpinner = new JSpinner(new SpinnerNumberModel(2000, 0, 3000, 100));
        panel.add(mmrMaxSpinner, gbc);
        
        // Latencia Máxima
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Latencia Máxima (ms):"), gbc);
        gbc.gridx = 1;
        latenciaMaxSpinner = new JSpinner(new SpinnerNumberModel(50, 0, 200, 5));
        panel.add(latenciaMaxSpinner, gbc);
        
        // Duración
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Duración (min):"), gbc);
        gbc.gridx = 1;
        duracionSpinner = new JSpinner(new SpinnerNumberModel(60, 30, 180, 15));
        panel.add(duracionSpinner, gbc);
        
        // Botón Crear
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton crearButton = new JButton("Crear Scrim");
        crearButton.setFont(new Font("Arial", Font.BOLD, 14));
        crearButton.setBackground(new Color(76, 175, 80));
        crearButton.setForeground(Color.WHITE);
        crearButton.setFocusPainted(false);
        crearButton.addActionListener(e -> handleCrearScrim());
        panel.add(crearButton, gbc);
        
        return panel;
    }
    
    private JPanel createBuscarScrimsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Tabla de scrims
        String[] columns = {"ID", "Juego", "Formato", "Región", "Estado", "Capacidad", "Fecha"};
        scrimsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        scrimsTable = new JTable(scrimsTableModel);
        scrimsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(scrimsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones con mejor visibilidad
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton refreshButton = new JButton("🔄 Actualizar");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 13));
        refreshButton.setBackground(new Color(76, 175, 80));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setPreferredSize(new Dimension(150, 35));
        refreshButton.addActionListener(e -> loadScrims());
        
        JButton postularseButton = new JButton("✋ Postularse");
        postularseButton.setFont(new Font("Arial", Font.BOLD, 13));
        postularseButton.setBackground(new Color(33, 150, 243));
        postularseButton.setForeground(Color.WHITE);
        postularseButton.setFocusPainted(false);
        postularseButton.setPreferredSize(new Dimension(150, 35));
        postularseButton.addActionListener(e -> handlePostularse());
        
        JButton verDetallesButton = new JButton("ℹ️ Ver Detalles");
        verDetallesButton.setFont(new Font("Arial", Font.BOLD, 13));
        verDetallesButton.setBackground(new Color(255, 152, 0));
        verDetallesButton.setForeground(Color.WHITE);
        verDetallesButton.setFocusPainted(false);
        verDetallesButton.setPreferredSize(new Dimension(150, 35));
        verDetallesButton.addActionListener(e -> handleVerDetalles());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(postularseButton);
        buttonPanel.add(verDetallesButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createPostulacionesTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Tabla de postulaciones
        String[] columns = {"Scrim", "Estado", "Rol", "Fecha"};
        postulacionesTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        postulacionesTable = new JTable(postulacionesTableModel);
        
        JScrollPane scrollPane = new JScrollPane(postulacionesTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Botón actualizar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton refreshButton = new JButton("Actualizar");
        refreshButton.addActionListener(e -> loadPostulaciones());
        buttonPanel.add(refreshButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createGestionPostulacionesTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Tabla de postulaciones recibidas
        String[] columns = {"Scrim", "Usuario", "Rol", "Mensaje", "Fecha"};
        gestionPostulacionesTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        gestionPostulacionesTable = new JTable(gestionPostulacionesTableModel);
        
        JScrollPane scrollPane = new JScrollPane(gestionPostulacionesTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        // Botón Actualizar
        JButton refreshButton = new JButton("🔄 Actualizar");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setBackground(new Color(76, 175, 80));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setPreferredSize(new Dimension(150, 35));
        refreshButton.addActionListener(e -> loadPostulacionesRecibidas());
        
        // Botón Aceptar
        JButton aceptarButton = new JButton("✅ Aceptar");
        aceptarButton.setFont(new Font("Arial", Font.BOLD, 14));
        aceptarButton.setBackground(new Color(33, 150, 243));
        aceptarButton.setForeground(Color.WHITE);
        aceptarButton.setFocusPainted(false);
        aceptarButton.setPreferredSize(new Dimension(150, 35));
        aceptarButton.addActionListener(e -> handleAceptarPostulacion());
        
        // Botón Rechazar
        JButton rechazarButton = new JButton("❌ Rechazar");
        rechazarButton.setFont(new Font("Arial", Font.BOLD, 14));
        rechazarButton.setBackground(new Color(244, 67, 54));
        rechazarButton.setForeground(Color.WHITE);
        rechazarButton.setFocusPainted(false);
        rechazarButton.setPreferredSize(new Dimension(150, 35));
        rechazarButton.addActionListener(e -> handleRechazarPostulacion());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(aceptarButton);
        buttonPanel.add(rechazarButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createGestionEstadosTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Título
        JLabel titleLabel = new JLabel("🎮 Gestionar Estados de Mis Scrims (Patrón STATE)", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Tabla de mis scrims
        String[] columns = {"ID", "Juego", "Formato", "Estado Actual", "Participantes", "Fecha"};
        misScrimsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        misScrimsTable = new JTable(misScrimsTableModel);
        misScrimsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(misScrimsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones para cambiar estado
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        // Botón Actualizar
        JButton refreshButton = new JButton("🔄 Actualizar");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setBackground(new Color(76, 175, 80));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setPreferredSize(new Dimension(150, 35));
        refreshButton.addActionListener(e -> loadMisScrims());
        
        // Botón Armar Lobby
        JButton armarLobbyButton = new JButton("🏠 Armar Lobby");
        armarLobbyButton.setFont(new Font("Arial", Font.BOLD, 14));
        armarLobbyButton.setBackground(new Color(156, 39, 176));
        armarLobbyButton.setForeground(Color.WHITE);
        armarLobbyButton.setFocusPainted(false);
        armarLobbyButton.setPreferredSize(new Dimension(150, 35));
        armarLobbyButton.addActionListener(e -> handleArmarLobby());
        
        // Botón Iniciar Scrim
        JButton iniciarButton = new JButton("▶️ Iniciar");
        iniciarButton.setFont(new Font("Arial", Font.BOLD, 14));
        iniciarButton.setBackground(new Color(33, 150, 243));
        iniciarButton.setForeground(Color.WHITE);
        iniciarButton.setFocusPainted(false);
        iniciarButton.setPreferredSize(new Dimension(150, 35));
        iniciarButton.addActionListener(e -> handleIniciarScrim());
        
        // Botón Finalizar Scrim
        JButton finalizarButton = new JButton("🏁 Finalizar");
        finalizarButton.setFont(new Font("Arial", Font.BOLD, 14));
        finalizarButton.setBackground(new Color(244, 67, 54));
        finalizarButton.setForeground(Color.WHITE);
        finalizarButton.setFocusPainted(false);
        finalizarButton.setPreferredSize(new Dimension(150, 35));
        finalizarButton.addActionListener(e -> handleFinalizarScrim());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(armarLobbyButton);
        buttonPanel.add(iniciarButton);
        buttonPanel.add(finalizarButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createEstadisticasTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        statsArea = new JTextArea();
        statsArea.setEditable(false);
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(statsArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Botón actualizar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton refreshButton = new JButton("Actualizar Estadísticas");
        refreshButton.addActionListener(e -> loadEstadisticas());
        buttonPanel.add(refreshButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void handleCrearScrim() {
        System.out.println("[DEBUG] handleCrearScrim - Iniciando creación de scrim");
        
        // Verificar que el usuario esté logueado
        Usuario usuarioActual = model.getUsuarioActual();
        System.out.println("[DEBUG] Usuario actual: " + (usuarioActual != null ? usuarioActual.getUsername() : "NULL"));
        
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(this,
                "Debe iniciar sesión para crear un scrim",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Juego juego = (Juego) juegoCombo.getSelectedItem();
        Formato formato = (Formato) formatoCombo.getSelectedItem();
        Region region = (Region) regionCombo.getSelectedItem();
        String estrategiaSeleccionada = (String) estrategiaCombo.getSelectedItem();
        int mmrMin = (Integer) mmrMinSpinner.getValue();
        int mmrMax = (Integer) mmrMaxSpinner.getValue();
        int latenciaMax = (Integer) latenciaMaxSpinner.getValue();
        int duracion = (Integer) duracionSpinner.getValue();
        
        System.out.println("[DEBUG] Datos del scrim:");
        System.out.println("  Juego: " + juego);
        System.out.println("  Formato: " + formato);
        System.out.println("  Region: " + region);
        System.out.println("  Estrategia: " + estrategiaSeleccionada);
        System.out.println("  MMR: " + mmrMin + " - " + mmrMax);
        System.out.println("  Latencia max: " + latenciaMax);
        System.out.println("  Duración: " + duracion);
        
        if (mmrMin > mmrMax) {
            JOptionPane.showMessageDialog(this,
                "El MMR mínimo debe ser menor o igual al MMR máximo",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Fecha de inicio: en 1 hora
        LocalDateTime fechaInicio = LocalDateTime.now().plusHours(1);
        System.out.println("  Fecha inicio: " + fechaInicio);
        
        try {
            System.out.println("[DEBUG] Llamando a controller.handleCrearScrim...");
            Scrim scrim = controller.handleCrearScrim(
                juego,
                formato,
                region,
                estrategiaSeleccionada,
                mmrMin,
                mmrMax,
                latenciaMax,
                fechaInicio,
                duracion
            );
            
            System.out.println("[DEBUG] Scrim creado: " + (scrim != null ? scrim.getId() : "NULL"));
            
            if (scrim != null) {
                JOptionPane.showMessageDialog(this,
                    "Scrim creado exitosamente con ID: " + scrim.getId(),
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                loadScrims(); // Refrescar la tabla de scrims
            } else {
                System.err.println("[ERROR] El scrim retornado es NULL");
                JOptionPane.showMessageDialog(this,
                    "Error al crear el scrim. Verifique los datos ingresados.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Excepción al crear scrim: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error al crear el scrim: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handlePostularse() {
        System.out.println("[DEBUG] handlePostularse - Iniciando postulación a scrim");
        
        int selectedRow = scrimsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un scrim de la tabla",
                "Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Obtener ID como String (viene de Long.toString())
            String scrimIdStr = (String) scrimsTableModel.getValueAt(selectedRow, 0);
            System.out.println("[DEBUG] Scrim ID (String): " + scrimIdStr);
            
            // Convertir Long ID a UUID válido
            UUID scrimId = longIdToUUID(scrimIdStr);
            System.out.println("[DEBUG] Scrim ID (UUID): " + scrimId);
        
            // Mostrar diálogo para seleccionar rol
            Rol[] roles = Rol.values();
            Rol selectedRol = (Rol) JOptionPane.showInputDialog(
                this,
                "Seleccione el rol con el que desea postularse:",
                "Postularse a Scrim",
                JOptionPane.QUESTION_MESSAGE,
                null,
                roles,
                roles[0]
            );
            
            if (selectedRol == null) {
                System.out.println("[DEBUG] Postulación cancelada por el usuario");
                return; // Canceló
            }
            
            System.out.println("[DEBUG] Rol seleccionado: " + selectedRol);
            System.out.println("[DEBUG] Llamando a controller.handlePostularse...");
            
            Postulacion postulacion = controller.handlePostularse(scrimId, selectedRol);
            
            System.out.println("[DEBUG] Postulación resultado: " + (postulacion != null ? "EXITOSA" : "NULL"));
            
            if (postulacion != null) {
                System.out.println("[POSTULACION] Usuario '" + model.getUsuarioActual().getUsername() + 
                                 "' se postuló al scrim " + scrimIdStr + " como " + selectedRol);
                JOptionPane.showMessageDialog(this,
                    "¡Postulación enviada exitosamente!\n" +
                    "Scrim: " + scrimIdStr + "\n" +
                    "Rol: " + selectedRol,
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                loadScrims(); // Refrescar tabla
                loadPostulaciones(); // Refrescar mis postulaciones
            } else {
                System.err.println("[ERROR] No se pudo crear la postulación");
                JOptionPane.showMessageDialog(this,
                    "Error al postularse. Verifique que no esté ya postulado.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("[ERROR] Error de conversión UUID: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error: ID de scrim inválido",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println("[ERROR] Error al postularse: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error inesperado: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleVerDetalles() {
        int selectedRow = scrimsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un scrim de la tabla",
                "Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Convertir Long ID a UUID
        String scrimIdStr = (String) scrimsTableModel.getValueAt(selectedRow, 0);
        UUID scrimId = longIdToUUID(scrimIdStr);
        List<Scrim> scrims = controller.handleBuscarScrims();
        
        Scrim scrim = scrims.stream()
            .filter(s -> s.getId() != null && s.getId().toString().equals(scrimIdStr))
            .findFirst()
            .orElse(null);
        
        if (scrim == null) {
            JOptionPane.showMessageDialog(this,
                "No se encontró el scrim",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        StringBuilder details = new StringBuilder();
        details.append("ID: ").append(scrim.getId()).append("\n");
        details.append("Juego: ").append(scrim.getJuego()).append("\n");
        details.append("Formato: ").append(scrim.getFormato()).append("\n");
        details.append("Región: ").append(scrim.getRegion()).append("\n");
        details.append("Estado: ").append(scrim.getEstado().getNombre()).append("\n");
        int cupoTotal = scrim.getFormato().getJugadoresPorEquipo() * 2;
        details.append("Capacidad: ").append(scrim.getParticipantes().size())
            .append("/").append(cupoTotal).append("\n");
        details.append("Rango MMR: ").append(scrim.getMmrMinimo())
            .append(" - ").append(scrim.getMmrMaximo()).append("\n");
        details.append("Fecha: ").append(scrim.getFechaHora()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
        
        JOptionPane.showMessageDialog(this,
            details.toString(),
            "Detalles del Scrim",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void refresh() {
        loadScrims();
        loadPostulaciones();
        loadEstadisticas();
    }
    
    private void loadScrims() {
        scrimsTableModel.setRowCount(0);
        
        List<Scrim> scrims = controller.handleBuscarScrims();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM HH:mm");
        
        for (Scrim scrim : scrims) {
            int cupoTotal = scrim.getFormato().getJugadoresPorEquipo() * 2;
            scrimsTableModel.addRow(new Object[]{
                scrim.getId().toString(),
                scrim.getJuego().toString(),
                scrim.getFormato().toString(),
                scrim.getRegion(),
                scrim.getEstado().getNombre(),
                scrim.getParticipantes().size() + "/" + cupoTotal,
                scrim.getFechaHora().format(formatter)
            });
        }
    }
    
    private void loadPostulaciones() {
        postulacionesTableModel.setRowCount(0);
        
        if (!model.isLoggedIn()) {
            return;
        }
        
        List<Postulacion> postulaciones = controller.handleObtenerMisPostulaciones();
        
        for (Postulacion p : postulaciones) {
            String estado;
            if (p.isAceptada()) {
                estado = "Aceptada";
            } else if (p.isRechazada()) {
                estado = "Rechazada";
            } else {
                estado = "Pendiente";
            }
            postulacionesTableModel.addRow(new Object[]{
                p.getScrim().getId().toString(),
                estado,
                p.getRolSolicitado(),
                p.getFechaPostulacion().toString()
            });
        }
    }
    
    private void loadPostulacionesRecibidas() {
        gestionPostulacionesTableModel.setRowCount(0);
        
        if (!model.isLoggedIn()) {
            return;
        }
        
        System.out.println("[DEBUG] Cargando postulaciones recibidas...");
        
        // Obtener todos los scrims creados por el usuario actual
        List<Scrim> scrims = controller.handleBuscarScrims();
        Usuario usuarioActual = model.getUsuarioActual();
        
        for (Scrim scrim : scrims) {
            // Solo procesar scrims creados por el usuario actual
            if (scrim.getCreador() != null && scrim.getCreador().equals(usuarioActual)) {
                System.out.println("[DEBUG] Scrim creado por usuario actual: " + scrim.getId());
                
                // Obtener postulaciones de este scrim
                // Convertir Long a UUID válido
                UUID scrimUUID = longIdToUUID(scrim.getId().toString());
                List<Postulacion> postulaciones = controller.handleObtenerPostulaciones(scrimUUID);
                
                System.out.println("[DEBUG] Postulaciones encontradas: " + postulaciones.size());
                
                for (Postulacion p : postulaciones) {
                    // Solo mostrar postulaciones pendientes (no aceptadas y no rechazadas)
                    if (!p.isAceptada() && !p.isRechazada()) {
                        gestionPostulacionesTableModel.addRow(new Object[]{
                            p.getScrim().getId().toString(),
                            p.getUsuario().getUsername(),
                            p.getRolSolicitado(),
                            p.getMensaje() != null ? p.getMensaje() : "",
                            p.getFechaPostulacion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                        });
                        System.out.println("[DEBUG] Postulación agregada: " + p.getUsuario().getUsername() + " -> " + p.getRolSolicitado());
                    }
                }
            }
        }
        
        System.out.println("[DEBUG] Total postulaciones en tabla: " + gestionPostulacionesTableModel.getRowCount());
    }
    
    private void handleAceptarPostulacion() {
        System.out.println("[DEBUG] handleAceptarPostulacion - Iniciando...");
        
        int selectedRow = gestionPostulacionesTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Por favor, seleccione una postulación para aceptar",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            System.out.println("[DEBUG] No se seleccionó ninguna postulación");
            return;
        }
        
        // Obtener datos de la fila seleccionada
        String scrimIdStr = (String) gestionPostulacionesTableModel.getValueAt(selectedRow, 0);
        String username = (String) gestionPostulacionesTableModel.getValueAt(selectedRow, 1);
        
        System.out.println("[DEBUG] Scrim ID: " + scrimIdStr);
        System.out.println("[DEBUG] Usuario: " + username);
        
        // Buscar la postulación correspondiente
        UUID scrimId = longIdToUUID(scrimIdStr);
        List<Postulacion> postulaciones = controller.handleObtenerPostulaciones(scrimId);
        
        Postulacion postulacionSeleccionada = postulaciones.stream()
            .filter(p -> p.getUsuario().getUsername().equals(username) && !p.isAceptada() && !p.isRechazada())
            .findFirst()
            .orElse(null);
        
        if (postulacionSeleccionada == null) {
            System.err.println("[ERROR] No se encontró la postulación");
            JOptionPane.showMessageDialog(this,
                "Error: No se encontró la postulación",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        System.out.println("[DEBUG] Postulación encontrada, ID: " + postulacionSeleccionada.getId());
        
        // Aceptar la postulación
        // Convertir Long a UUID
        UUID postulacionUUID = longIdToUUID(postulacionSeleccionada.getId().toString());
        controller.handleAceptarPostulacion(postulacionUUID);
        
        System.out.println("[ACCEPT] Postulación aceptada - Usuario: " + username + ", Scrim: " + scrimIdStr);
        
        JOptionPane.showMessageDialog(this,
            "¡Postulación aceptada!\n" +
            "Usuario: " + username + "\n" +
            "Se ha agregado al scrim",
            "Éxito",
            JOptionPane.INFORMATION_MESSAGE);
        
        // Refrescar tablas
        loadPostulacionesRecibidas();
        loadScrims(); // Para actualizar la capacidad del scrim
    }
    
    private void handleRechazarPostulacion() {
        System.out.println("[DEBUG] handleRechazarPostulacion - Iniciando...");
        
        int selectedRow = gestionPostulacionesTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Por favor, seleccione una postulación para rechazar",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            System.out.println("[DEBUG] No se seleccionó ninguna postulación");
            return;
        }
        
        // Obtener datos de la fila seleccionada
        String scrimIdStr = (String) gestionPostulacionesTableModel.getValueAt(selectedRow, 0);
        String username = (String) gestionPostulacionesTableModel.getValueAt(selectedRow, 1);
        
        System.out.println("[DEBUG] Scrim ID: " + scrimIdStr);
        System.out.println("[DEBUG] Usuario: " + username);
        
        // Buscar la postulación correspondiente
        UUID scrimId = longIdToUUID(scrimIdStr);
        List<Postulacion> postulaciones = controller.handleObtenerPostulaciones(scrimId);
        
        Postulacion postulacionSeleccionada = postulaciones.stream()
            .filter(p -> p.getUsuario().getUsername().equals(username) && !p.isAceptada() && !p.isRechazada())
            .findFirst()
            .orElse(null);
        
        if (postulacionSeleccionada == null) {
            System.err.println("[ERROR] No se encontró la postulación");
            JOptionPane.showMessageDialog(this,
                "Error: No se encontró la postulación",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        System.out.println("[DEBUG] Postulación encontrada, ID: " + postulacionSeleccionada.getId());
        
        // Confirmar rechazo
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea rechazar esta postulación?\n" +
            "Usuario: " + username,
            "Confirmar Rechazo",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirmacion != JOptionPane.YES_OPTION) {
            System.out.println("[DEBUG] Rechazo cancelado por el usuario");
            return;
        }
        
        // Rechazar la postulación
        UUID postulacionUUID = longIdToUUID(postulacionSeleccionada.getId().toString());
        controller.handleRechazarPostulacion(postulacionUUID);
        
        System.out.println("[REJECT] Postulación rechazada - Usuario: " + username + ", Scrim: " + scrimIdStr);
        
        JOptionPane.showMessageDialog(this,
            "Postulación rechazada\n" +
            "Usuario: " + username,
            "Rechazada",
            JOptionPane.INFORMATION_MESSAGE);
        
        // Refrescar tabla
        loadPostulacionesRecibidas();
    }
    
    // ============================================================
    // GESTIÓN DE ESTADOS (PATRÓN STATE)
    // ============================================================
    
    private void loadMisScrims() {
        System.out.println("[DEBUG] Cargando mis scrims creados...");
        misScrimsTableModel.setRowCount(0);
        
        String username = controller.handleObtenerUsuarioActual().getUsername();
        List<Scrim> todosScrims = controller.handleObtenerTodosScrims();
        
        // Filtrar scrims creados por el usuario actual que NO estén finalizados
        for (Scrim scrim : todosScrims) {
            if (scrim.getCreador() != null && scrim.getCreador().getUsername().equals(username)) {
                // Mostrar todos los scrims EXCEPTO los finalizados
                if (!(scrim.getEstado() instanceof FinalizadoState)) {
                    int totalJugadores = scrim.getFormato().getJugadoresPorEquipo() * 2; // 2 equipos
                    misScrimsTableModel.addRow(new Object[]{
                        scrim.getId().toString(),
                        scrim.getJuego().getNombre(),
                        scrim.getFormato().getDescripcion(),
                        scrim.getEstado().getNombre(),
                        scrim.getParticipantes().size() + "/" + totalJugadores,
                        scrim.getFechaHora().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM HH:mm"))
                    });
                    System.out.println("[DEBUG] Scrim agregado: ID=" + scrim.getId() + ", Estado=" + scrim.getEstado().getNombre());
                }
            }
        }
        
        System.out.println("[DEBUG] Total mis scrims activos: " + misScrimsTableModel.getRowCount());
    }
    
    private void handleArmarLobby() {
        int selectedRow = misScrimsTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Por favor, seleccione un scrim",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String scrimIdStr = (String) misScrimsTableModel.getValueAt(selectedRow, 0);
        String estadoActual = (String) misScrimsTableModel.getValueAt(selectedRow, 3);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Armar lobby para el scrim?\n" +
            "Estado actual: " + estadoActual + "\n" +
            "Nuevo estado: Lobby Armado",
            "Confirmar Cambio de Estado",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                UUID scrimId = longIdToUUID(scrimIdStr);
                controller.handleArmarLobby(scrimId);
                
                JOptionPane.showMessageDialog(this,
                    "Lobby armado exitosamente",
                    "Estado Actualizado",
                    JOptionPane.INFORMATION_MESSAGE);
                
                loadMisScrims();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error al armar lobby: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void handleIniciarScrim() {
        int selectedRow = misScrimsTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Por favor, seleccione un scrim",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String scrimIdStr = (String) misScrimsTableModel.getValueAt(selectedRow, 0);
        String estadoActual = (String) misScrimsTableModel.getValueAt(selectedRow, 3);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Iniciar el scrim?\n" +
            "Estado actual: " + estadoActual + "\n" +
            "Nuevo estado: En Curso",
            "Confirmar Cambio de Estado",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                UUID scrimId = longIdToUUID(scrimIdStr);
                controller.handleIniciarScrim(scrimId);
                
                JOptionPane.showMessageDialog(this,
                    "Scrim iniciado exitosamente",
                    "Estado Actualizado",
                    JOptionPane.INFORMATION_MESSAGE);
                
                loadMisScrims();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error al iniciar scrim: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void handleFinalizarScrim() {
        int selectedRow = misScrimsTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Por favor, seleccione un scrim",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String scrimIdStr = (String) misScrimsTableModel.getValueAt(selectedRow, 0);
        String estadoActual = (String) misScrimsTableModel.getValueAt(selectedRow, 3);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Finalizar el scrim?\n" +
            "Estado actual: " + estadoActual + "\n" +
            "Nuevo estado: Finalizado",
            "Confirmar Cambio de Estado",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                UUID scrimId = longIdToUUID(scrimIdStr);
                controller.handleFinalizarScrim(scrimId);
                
                JOptionPane.showMessageDialog(this,
                    "Scrim finalizado exitosamente",
                    "Estado Actualizado",
                    JOptionPane.INFORMATION_MESSAGE);
                
                loadMisScrims();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error al finalizar scrim: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void loadEstadisticas() {
        Map<String, Integer> stats = controller.handleObtenerEstadisticas();
        
        StringBuilder sb = new StringBuilder();
        sb.append("•••••••••••••••••••••••••••••••••••••••••••\n");
        sb.append("    ESTADÃSTICAS DEL SISTEMA eScrims\n");
        sb.append("•••••••••••••••••••••••••••••••••••••••••••\n\n");
        
        sb.append("USUARIOS\n");
        sb.append("  Total de usuarios: ").append(stats.get("totalUsuarios")).append("\n\n");
        
        sb.append("SCRIMS\n");
        sb.append("  Total de scrims: ").append(stats.get("totalScrims")).append("\n");
        sb.append("  Scrims abiertos: ").append(stats.get("scrimsAbiertos")).append("\n");
        sb.append("  Scrims en curso: ").append(stats.get("scrimsEnCurso")).append("\n");
        sb.append("  Scrims finalizados: ").append(stats.get("scrimsFinalizados")).append("\n\n");
        
        sb.append("POSTULACIONES\n");
        sb.append("  Total de postulaciones: ").append(stats.get("totalPostulaciones")).append("\n");
        sb.append("  Pendientes: ").append(stats.get("postulacionesPendientes")).append("\n");
        sb.append("  Aprobadas: ").append(stats.get("postulacionesAprobadas")).append("\n");
        sb.append("  Rechazadas: ").append(stats.get("postulacionesRechazadas")).append("\n\n");
        
        sb.append("•••••••••••••••••••••••••••••••••••••••••••\n");
        
        statsArea.setText(sb.toString());
    }
}



