package project.bankrut.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import project.bankrut.model.BankUser;
import project.bankrut.service.UserDataService;
import project.bankrut.view.RegisterView;

public class RegisterController extends BaseController {
    private RegisterView registerView;
    private LoginController loginController;
    
    public RegisterController(UserDataService userDataService, LoginController loginController) {
        super(userDataService);
        this.loginController = loginController;
        System.out.println("RegisterController dibuat");
    }
    
    @Override
    public void showView(Stage primaryStage) {
        setPrimaryStage(primaryStage);
        registerView = new RegisterView();
        setupEventHandlers();
        
        Scene scene = new Scene(registerView.getRoot(), 360, 640);
        primaryStage.setTitle("BANKRUT - Registrasi");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        
        System.out.println("Register view berhasil ditampilkan");
    }
    
    @Override
    protected void setupEventHandlers() {
        registerView.getBackButton().setOnAction(e -> handleBack());
        registerView.getDaftarButton().setOnAction(e -> handleRegisterSubmit());
        System.out.println("Event handlers untuk register berhasil diatur");
    }
    
    private void handleBack() {
        System.out.println("Kembali ke login screen");
        loginController.showView(primaryStage);
    }
    
    private void handleRegisterSubmit() {
        String namaLengkap = registerView.getNamaLengkapField().getText().trim();
        String username = registerView.getUsernameField().getText().trim();
        String pin = registerView.getPinField().getText().trim();
        registerView.hideWarning();

        if (!validateInput(namaLengkap, username, pin)) {
            return;
        }
        
        BankUser newUser = new BankUser(namaLengkap, username, pin);
        boolean success = userDataService.simpanUser(newUser);
        
        if (success) {
            System.out.println("Registrasi berhasil untuk user: " + username);
            registerView.showSuccessPopup(() -> {
                loginController.showView(primaryStage);
            });
        } else {
            System.out.println("Registrasi gagal - username sudah digunakan: " + username);
            registerView.showWarning("Username sudah digunakan!");
        }
    }
    

    private boolean validateInput(String namaLengkap, String username, String pin) {
        if (namaLengkap.isEmpty() || username.isEmpty() || pin.isEmpty()) {
            registerView.showWarning("Data harus diisi dengan lengkap.");
            return false;
        }
        
        if (namaLengkap.length() < 3) {
            registerView.showWarning("Nama lengkap minimal 3 karakter!");
            return false;
        }
        
        if (username.length() < 4) {
            registerView.showWarning("Username minimal 4 karakter!");
            return false;
        }
        
        if (!username.matches("^[a-zA-Z0-9]+$")) {
            registerView.showWarning("Username hanya boleh huruf dan angka!");
            return false;
        }

        if (pin.length() != 6 || !pin.matches("\\d{6}")) {
            registerView.showWarning("PIN harus terdiri dari 6 digit angka!");
            return false;
        }
        return true;
    }
}