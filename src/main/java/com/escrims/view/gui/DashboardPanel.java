package com.escrims.view.gui;

import com.escrims.model.domain.model.*;
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
    
    // Tab 4: Estadísticas
    private JTextArea statsArea;
    
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
                case 3: // Estadísticas
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
        
        UUID scrimId = UUID.fromString((String) scrimsTableModel.getValueAt(selectedRow, 0));
        System.out.println("[DEBUG] Scrim seleccionado: " + scrimId);
        
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
                             "' se postuló al scrim " + scrimId + " como " + selectedRol);
            JOptionPane.showMessageDialog(this,
                "¡Postulación enviada exitosamente!\n" +
                "Scrim: " + scrimId + "\n" +
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
        
        UUID scrimId = UUID.fromString((String) scrimsTableModel.getValueAt(selectedRow, 0));
        List<Scrim> scrims = controller.handleBuscarScrims();
        
        Scrim scrim = scrims.stream()
            .filter(s -> s.getId().equals(scrimId))
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
            String estado = p.isAceptada() ? "Aceptada" : "Pendiente";
            postulacionesTableModel.addRow(new Object[]{
                p.getScrim().getId().toString(),
                estado,
                p.getRolSolicitado(),
                p.getFechaPostulacion().toString()
            });
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



