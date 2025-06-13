package project.bankrut.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import project.bankrut.model.BankUser;
import project.bankrut.service.UserDataService;
import project.bankrut.view.*;

public class LoginController extends BaseController {
    private LoginView loginView;
    private LoginFormView loginFormView;
    private RegisterController registerController;
    private DashboardController dashboardController;
    
    public LoginController(UserDataService userDataService) {
        super(userDataService);
    }

    @Override
    public void showView(Stage primaryStage) {
        setPrimaryStage(primaryStage);
        loginView = new LoginView();
        setupEventHandlers();
        
        Scene scene = new Scene(loginView.getRoot(), 360, 640);
        primaryStage.setTitle("BANKRUT - BANK RAKYAT UNHAS TERPADU");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    protected void setupEventHandlers() {
        loginView.getMasukButton().setOnAction(e -> handleLogin());
        loginView.getDaftarButton().setOnAction(e -> handleRegister());
    }

    private void handleLogin() {
        showLoginFormScene();
    }

    private void handleRegister() {
        registerController = new RegisterController(userDataService, this);
        registerController.showView(primaryStage);
    }

    private void showLoginFormScene() {
        loginFormView = new LoginFormView();
        setupLoginFormEventHandlers();
        Scene loginFormScene = new Scene(loginFormView.getRoot(), 360, 640);
        primaryStage.setScene(loginFormScene);
    }

    private void setupLoginFormEventHandlers() {
        loginFormView.getBackButton().setOnAction(e -> {
            showView(primaryStage); 
        });
        loginFormView.getMasukButton().setOnAction(e -> handleLoginSubmit());
    }

    private void handleLoginSubmit() {
        String username = loginFormView.getUsernameField().getText().trim();
        String pin = loginFormView.getPinField().getText().trim();
        loginFormView.hideWarning();

        if (username.isEmpty() || pin.isEmpty()) {
            loginFormView.showWarning("Username dan PIN harus diisi!");
            return;
        }

        if (!validatePin(pin)) {
            loginFormView.showWarning("PIN harus terdiri dari 6 digit angka!");
            return;
        }

        BankUser user = userDataService.cariUser(username);
        
        if (user != null && user.validasi(username, pin)) {
            System.out.println("Login berhasil oleh user: " + user.getNamaLengkap());
            loginFormView.showWelcomePopup(user.getNamaLengkap(), () -> {
                showDashboardScene(user);
            });
        } else {
            System.out.println("Login gagal untuk username: " + username);
            loginFormView.showWarning("Username atau PIN salah!");
        }
    }

    private boolean validatePin(String pin) {
        return pin.length() == 6 && pin.matches("\\d{6}");
    }

    private void showDashboardScene(BankUser user) {
        try {
            System.out.println("Membuka dashboard untuk: " + user.getNamaLengkap());
            dashboardController = new DashboardController(userDataService, user);
            dashboardController.showView(primaryStage);
        } catch (Exception e) {
            System.err.println("Error saat membuka dashboard: " + e.getMessage());
            e.printStackTrace();
            loginFormView.showWarning("Terjadi kesalahan saat membuka dashboard!");
        }
    }
}