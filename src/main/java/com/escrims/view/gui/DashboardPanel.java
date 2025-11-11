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
 * Panel principal con pestaÃ±as para gestiÃ³n de scrims
 */
public class DashboardPanel extends JPanel {
    
    private final ScrimController controller;
    private final ApplicationModel model;
    
    private JTabbedPane tabbedPane;
    
    // Tab 1: Crear Scrim
    private JComboBox<Juego> juegoCombo;
    private JComboBox<Formato> formatoCombo;
    private JComboBox<Region> regionCombo;
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
    
    // Tab 4: EstadÃ­sticas
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
        
        // PestaÃ±as
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Crear Scrim", createCrearScrimTab());
        tabbedPane.addTab("Buscar Scrims", createBuscarScrimsTab());
        tabbedPane.addTab("Mis Postulaciones", createPostulacionesTab());
        tabbedPane.addTab("EstadÃ­sticas", createEstadisticasTab());
        
        add(tabbedPane, BorderLayout.CENTER);
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
        
        JButton logoutButton = new JButton("Cerrar SesiÃ³n");
        logoutButton.setBackground(new Color(244, 67, 54));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> {
            controller.handleLogout();
            JOptionPane.showMessageDialog(this, "SesiÃ³n cerrada exitosamente");
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
        
        // RegiÃ³n
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("RegiÃ³n:"), gbc);
        gbc.gridx = 1;
        regionCombo = new JComboBox<>(Region.values());
        panel.add(regionCombo, gbc);
        
        // MMR MÃ­nimo
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("MMR MÃ­nimo:"), gbc);
        gbc.gridx = 1;
        mmrMinSpinner = new JSpinner(new SpinnerNumberModel(1000, 0, 3000, 100));
        panel.add(mmrMinSpinner, gbc);
        
        // MMR MÃ¡ximo
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("MMR MÃ¡ximo:"), gbc);
        gbc.gridx = 1;
        mmrMaxSpinner = new JSpinner(new SpinnerNumberModel(2000, 0, 3000, 100));
        panel.add(mmrMaxSpinner, gbc);
        
        // Latencia MÃ¡xima
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Latencia MÃ¡xima (ms):"), gbc);
        gbc.gridx = 1;
        latenciaMaxSpinner = new JSpinner(new SpinnerNumberModel(50, 0, 200, 5));
        panel.add(latenciaMaxSpinner, gbc);
        
        // DuraciÃ³n
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("DuraciÃ³n (min):"), gbc);
        gbc.gridx = 1;
        duracionSpinner = new JSpinner(new SpinnerNumberModel(60, 30, 180, 15));
        panel.add(duracionSpinner, gbc);
        
        // BotÃ³n Crear
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
        String[] columns = {"ID", "Juego", "Formato", "RegiÃ³n", "Estado", "Capacidad", "Fecha"};
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
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        JButton refreshButton = new JButton("Actualizar");
        refreshButton.addActionListener(e -> loadScrims());
        
        JButton postularseButton = new JButton("Postularse");
        postularseButton.setBackground(new Color(33, 150, 243));
        postularseButton.setForeground(Color.WHITE);
        postularseButton.setFocusPainted(false);
        postularseButton.addActionListener(e -> handlePostularse());
        
        JButton verDetallesButton = new JButton("Ver Detalles");
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
        
        // BotÃ³n actualizar
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
        
        // BotÃ³n actualizar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton refreshButton = new JButton("Actualizar EstadÃ­sticas");
        refreshButton.addActionListener(e -> loadEstadisticas());
        buttonPanel.add(refreshButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void handleCrearScrim() {
        Juego juego = (Juego) juegoCombo.getSelectedItem();
        Formato formato = (Formato) formatoCombo.getSelectedItem();
        Region region = (Region) regionCombo.getSelectedItem();
        int mmrMin = (Integer) mmrMinSpinner.getValue();
        int mmrMax = (Integer) mmrMaxSpinner.getValue();
        int latenciaMax = (Integer) latenciaMaxSpinner.getValue();
        int duracion = (Integer) duracionSpinner.getValue();
        
        if (mmrMin > mmrMax) {
            JOptionPane.showMessageDialog(this,
                "El MMR mÃ­nimo debe ser menor o igual al MMR mÃ¡ximo",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Fecha de inicio: en 1 hora
        LocalDateTime fechaInicio = LocalDateTime.now().plusHours(1);
        
        Scrim scrim = controller.handleCrearScrim(
            juego,
            formato,
            region,
            mmrMin,
            mmrMax,
            latenciaMax,
            fechaInicio,
            duracion
        );
        
        if (scrim != null) {
            JOptionPane.showMessageDialog(this,
                "Scrim creado exitosamente con ID: " + scrim.getId(),
                "Ã‰xito",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Error al crear el scrim",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handlePostularse() {
        int selectedRow = scrimsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un scrim de la tabla",
                "Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        UUID scrimId = UUID.fromString((String) scrimsTableModel.getValueAt(selectedRow, 0));
        
        // Mostrar diÃ¡logo para seleccionar rol
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
            return; // CancelÃ³
        }
        
        Postulacion postulacion = controller.handlePostularse(scrimId, selectedRol);
        
        if (postulacion != null) {
            JOptionPane.showMessageDialog(this,
                "PostulaciÃ³n enviada exitosamente",
                "Ã‰xito",
                JOptionPane.INFORMATION_MESSAGE);
            loadScrims(); // Refrescar tabla
        } else {
            JOptionPane.showMessageDialog(this,
                "Error al postularse. Verifique que no estÃ© ya postulado.",
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
                "No se encontrÃ³ el scrim",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        StringBuilder details = new StringBuilder();
        details.append("ID: ").append(scrim.getId()).append("\n");
        details.append("Juego: ").append(scrim.getJuego()).append("\n");
        details.append("Formato: ").append(scrim.getFormato()).append("\n");
        details.append("RegiÃ³n: ").append(scrim.getRegion()).append("\n");
        details.append("Estado: ").append(scrim.getEstado()).append("\n");
        details.append("Capacidad: ").append(scrim.getParticipantes().size())
            .append("/").append(scrim.getCupoTotal()).append("\n");
        details.append("DuraciÃ³n: ").append(scrim.getDuracionMin()).append(" min\n");
        details.append("Rango MMR: ").append(scrim.getRangoMin())
            .append(" - ").append(scrim.getRangoMax()).append("\n");
        details.append("Latencia mÃ¡x: ").append(scrim.getLatenciaMax()).append(" ms\n");
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
            scrimsTableModel.addRow(new Object[]{
                scrim.getId().toString(),
                scrim.getJuego().toString(),
                scrim.getFormato().toString(),
                scrim.getRegion(),
                scrim.getEstado(),
                scrim.getParticipantes().size() + "/" + scrim.getCupoTotal(),
                scrim.getFechaHora().format(formatter)
            });
        }
    }
    
    private void loadPostulaciones() {
        postulacionesTableModel.setRowCount(0);
        
        if (!model.isLoggedIn()) {
            return;
        }
        
        UUID usuarioId = model.getUsuarioActual().getId();
        List<Postulacion> postulaciones = controller.handleObtenerPostulaciones(usuarioId);
        
        for (Postulacion p : postulaciones) {
            postulacionesTableModel.addRow(new Object[]{
                p.getScrimId().toString(),
                p.getEstado(),
                p.getRolDeseado(),
                "N/A" // Fecha no disponible en modelo
            });
        }
    }
    
    private void loadEstadisticas() {
        Map<String, Integer> stats = controller.handleObtenerEstadisticas();
        
        StringBuilder sb = new StringBuilder();
        sb.append("â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•\n");
        sb.append("    ESTADÃSTICAS DEL SISTEMA eScrims\n");
        sb.append("â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•\n\n");
        
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
        
        sb.append("â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•\n");
        
        statsArea.setText(sb.toString());
    }
}
