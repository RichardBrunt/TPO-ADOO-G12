package com.escrims.view.gui;

import com.escrims.model.domain.model.Region;
import com.escrims.model.domain.model.Rol;
import com.escrims.controller.ScrimController;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Panel de Login y Registro
 */
public class LoginPanel extends JPanel {
    
    private final ScrimController controller;
    private final MainFrame mainFrame;
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton showRegisterButton;
    
    public LoginPanel(ScrimController controller, MainFrame mainFrame) {
        this.controller = controller;
        this.mainFrame = mainFrame;
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        
        // Panel central con login
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 240, 245));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // TÃ­tulo
        JLabel titleLabel = new JLabel("eScrims - Sistema de GestiÃ³n de Scrims");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        centerPanel.add(titleLabel, gbc);
        
        // SubtÃ­tulo
        JLabel subtitleLabel = new JLabel("Iniciar SesiÃ³n");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        centerPanel.add(subtitleLabel, gbc);
        
        // Usuario
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        centerPanel.add(new JLabel("Usuario:"), gbc);
        
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        centerPanel.add(usernameField, gbc);
        
        // ContraseÃ±a
        gbc.gridx = 0;
        gbc.gridy = 3;
        centerPanel.add(new JLabel("ContraseÃ±a:"), gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        centerPanel.add(passwordField, gbc);
        
        // BotÃ³n Login
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        loginButton = new JButton("Iniciar SesiÃ³n");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(76, 175, 80));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(e -> handleLogin());
        centerPanel.add(loginButton, gbc);
        
        // BotÃ³n Registrarse
        gbc.gridy = 5;
        showRegisterButton = new JButton("Â¿No tienes cuenta? RegÃ­strate");
        showRegisterButton.setFont(new Font("Arial", Font.PLAIN, 12));
        showRegisterButton.setBorderPainted(false);
        showRegisterButton.setFocusPainted(false);
        showRegisterButton.setContentAreaFilled(false);
        showRegisterButton.setForeground(new Color(33, 150, 243));
        showRegisterButton.addActionListener(e -> showRegisterDialog());
        centerPanel.add(showRegisterButton, gbc);
        
        // Mensaje de demostraciÃ³n
        gbc.gridy = 6;
        JLabel demoLabel = new JLabel("Demo: Usa 'admin' / 'admin' o regÃ­strate");
        demoLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        demoLabel.setForeground(Color.GRAY);
        centerPanel.add(demoLabel, gbc);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Enter para login
        passwordField.addActionListener(e -> handleLogin());
    }
    
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor ingrese usuario y contraseÃ±a",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        boolean success = controller.handleLogin(username, password);
        
        if (success) {
            JOptionPane.showMessageDialog(this,
                "Â¡Bienvenido " + username + "!",
                "Login Exitoso",
                JOptionPane.INFORMATION_MESSAGE);
            // El cambio de pantalla se maneja automÃ¡ticamente por el listener
        } else {
            JOptionPane.showMessageDialog(this,
                "Usuario o contraseÃ±a incorrectos",
                "Error de Login",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showRegisterDialog() {
        JDialog registerDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            "Registro de Usuario", true);
        registerDialog.setSize(500, 600);
        registerDialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Campos
        JTextField regUsername = new JTextField(20);
        JTextField regEmail = new JTextField(20);
        JPasswordField regPassword = new JPasswordField(20);
        JComboBox<Region> regionCombo = new JComboBox<>(Region.values());
        JSpinner mmrSpinner = new JSpinner(new SpinnerNumberModel(1500, 0, 3000, 50));
        JSpinner latenciaSpinner = new JSpinner(new SpinnerNumberModel(50, 0, 200, 5));
        
        // Checkboxes para roles
        JCheckBox duelistCheck = new JCheckBox("Duelist");
        JCheckBox supportCheck = new JCheckBox("Support");
        JCheckBox flexCheck = new JCheckBox("Flex");
        
        // Layout
        int row = 0;
        gbc.gridx = 0; gbc.gridy = row++;
        panel.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        panel.add(regUsername, gbc);
        
        gbc.gridx = 0; gbc.gridy = row++;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(regEmail, gbc);
        
        gbc.gridx = 0; gbc.gridy = row++;
        panel.add(new JLabel("ContraseÃ±a:"), gbc);
        gbc.gridx = 1;
        panel.add(regPassword, gbc);
        
        gbc.gridx = 0; gbc.gridy = row++;
        panel.add(new JLabel("RegiÃ³n:"), gbc);
        gbc.gridx = 1;
        panel.add(regionCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = row++;
        panel.add(new JLabel("MMR:"), gbc);
        gbc.gridx = 1;
        panel.add(mmrSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = row++;
        panel.add(new JLabel("Latencia (ms):"), gbc);
        gbc.gridx = 1;
        panel.add(latenciaSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = row++;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Roles preferidos:"), gbc);
        
        gbc.gridy = row++;
        panel.add(duelistCheck, gbc);
        gbc.gridy = row++;
        panel.add(supportCheck, gbc);
        gbc.gridy = row++;
        panel.add(flexCheck, gbc);
        
        // BotÃ³n Registrar
        gbc.gridy = row++;
        JButton registerButton = new JButton("Registrarse");
        registerButton.addActionListener(e -> {
            Set<Rol> roles = new HashSet<>();
            if (duelistCheck.isSelected()) roles.add(Rol.DUELIST);
            if (supportCheck.isSelected()) roles.add(Rol.SUPPORT);
            if (flexCheck.isSelected()) roles.add(Rol.FLEX);
            
            if (roles.isEmpty()) {
                JOptionPane.showMessageDialog(registerDialog,
                    "Seleccione al menos un rol",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean success = controller.handleRegistro(
                regUsername.getText(),
                regEmail.getText(),
                new String(regPassword.getPassword()),
                (Region) regionCombo.getSelectedItem(),
                (Integer) mmrSpinner.getValue(),
                (Integer) latenciaSpinner.getValue(),
                roles
            );
            
            if (success) {
                JOptionPane.showMessageDialog(registerDialog,
                    "Â¡Registro exitoso! Ahora puede iniciar sesiÃ³n",
                    "Ã‰xito",
                    JOptionPane.INFORMATION_MESSAGE);
                registerDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(registerDialog,
                    "Error en el registro",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(registerButton, gbc);
        
        registerDialog.add(new JScrollPane(panel));
        registerDialog.setVisible(true);
    }
}
