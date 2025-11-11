package com.escrims.view.gui;

import com.escrims.controller.ScrimController;
import com.escrims.model.application.ApplicationModel;
import com.escrims.model.application.ModelChangeListener;
import com.escrims.model.service.ScrimService;
import com.escrims.infra.persistence.inmemory.InMemoryScrimRepository;
import com.escrims.infra.persistence.inmemory.InMemoryUsuarioRepository;
import com.escrims.infra.persistence.inmemory.InMemoryPostulacionRepository;
import com.escrims.infra.singleton.SingletonEventBus;
import com.escrims.infra.notification.NotificationFacade;
import com.escrims.infra.notification.EmailNotificationService;
import com.escrims.infra.notification.PushNotificationService;
import com.escrims.infra.notification.SMSNotificationService;

import javax.swing.*;

/**
 * Ventana principal de la aplicaciÃ³n (PatrÃ³n MVC - View)
 * 
 * Contiene las diferentes pantallas de la aplicaciÃ³n.
 */
public class MainFrame extends JFrame implements ModelChangeListener {
    
    private final ScrimController controller;
    private final ApplicationModel model;
    
    // Paneles
    private JPanel currentPanel;
    private LoginPanel loginPanel;
    private DashboardPanel dashboardPanel;
    
    public MainFrame(ScrimController controller, ApplicationModel model) {
        this.controller = controller;
        this.model = model;
        
        // Registrarse como listener del modelo
        model.addModelChangeListener(this);
        
        initUI();
    }
    
    private void initUI() {
        setTitle("eScrims - Sistema de GestiÃ³n de Scrims");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // Crear paneles
        loginPanel = new LoginPanel(controller, this);
        dashboardPanel = new DashboardPanel(controller, model);
        
        // Mostrar login inicialmente
        showLogin();
    }
    
    /**
     * Mostrar panel de login
     */
    public void showLogin() {
        SwingUtilities.invokeLater(() -> {
            if (currentPanel != null) {
                getContentPane().remove(currentPanel);
            }
            currentPanel = loginPanel;
            getContentPane().add(currentPanel);
            revalidate();
            repaint();
        });
    }
    
    /**
     * Mostrar panel principal (dashboard)
     */
    public void showDashboard() {
        SwingUtilities.invokeLater(() -> {
            if (currentPanel != null) {
                getContentPane().remove(currentPanel);
            }
            currentPanel = dashboardPanel;
            dashboardPanel.refresh(); // Actualizar datos
            getContentPane().add(currentPanel);
            revalidate();
            repaint();
        });
    }
    
    @Override
    public void onModelChanged() {
        // Actualizar UI cuando el modelo cambia
        SwingUtilities.invokeLater(() -> {
            if (model.isLoggedIn() && currentPanel == loginPanel) {
                showDashboard();
            } else if (!model.isLoggedIn() && currentPanel == dashboardPanel) {
                showLogin();
            } else if (currentPanel == dashboardPanel) {
                dashboardPanel.refresh();
            }
        });
    }
    
    /**
     * Punto de entrada de la aplicación
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Inicializar repositorios (Patrón REPOSITORY)
            var usuarioRepo = new InMemoryUsuarioRepository();
            var scrimRepo = new InMemoryScrimRepository();
            var postulacionRepo = new InMemoryPostulacionRepository();
            
            // Inicializar EventBus (Patrón SINGLETON + OBSERVER)
            var eventBus = SingletonEventBus.getInstance();
            
            // Inicializar NotificationFacade (Patrón FACADE)
            NotificationFacade notificationFacade = new NotificationFacade();
            notificationFacade.registrarServicio(new EmailNotificationService());
            notificationFacade.registrarServicio(new PushNotificationService());
            notificationFacade.registrarServicio(new SMSNotificationService());
            System.out.println("[FACADE] Notification Facade inicializado con " + 
                notificationFacade.getCantidadCanales() + " canales");
            
            // Inicializar servicios
            var scrimService = new ScrimService(
                scrimRepo,
                postulacionRepo,
                eventBus
            );
            
            // Crear modelo de aplicación (MVC - Model)
            ApplicationModel model = new ApplicationModel(scrimService, usuarioRepo, notificationFacade);
            
            // Crear controlador (MVC - Controller)
            ScrimController controller = new ScrimController(model);
            
            // Crear y mostrar ventana principal (MVC - View)
            MainFrame frame = new MainFrame(controller, model);
            frame.setVisible(true);
        });
    }
}
