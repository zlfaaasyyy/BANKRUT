package project.bankrut.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import project.bankrut.model.BankUser;
import project.bankrut.service.UserDataService;
import project.bankrut.service.TransactionService;
import project.bankrut.view.DashboardView;

public class DashboardController extends BaseController {
    private DashboardView dashboardView;
    private BankUser currentUser;
    private TransactionService transactionService;
    private SetorTunaiController setorTunaiController;
    private TarikTunaiController tarikTunaiController;
    private TransferController transferController;
    private RiwayatController riwayatController;
    
    public DashboardController(UserDataService userDataService, BankUser user) {
        super(userDataService);
        this.currentUser = user;
        this.transactionService = new TransactionService();
        System.out.println("DashboardController dibuat untuk user: " + user.getUsername());
    }

    @Override
    public void showView(Stage primaryStage) {
        try {
            setPrimaryStage(primaryStage);
            refreshUserData();
            dashboardView = new DashboardView(currentUser);
            setupEventHandlers();

            Scene scene = new Scene(dashboardView.getRoot(), 360, 640);
            primaryStage.setTitle("BANKRUT - Dashboard");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
            System.out.println("Dashboard view berhasil ditampilkan untuk user: " + currentUser.getNamaLengkap());
        } catch (Exception e) {
            System.err.println("Error menampilkan dashboard: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    protected void setupEventHandlers() {
        try {
            dashboardView.getSetorTunaiButton().setOnAction(e -> handleSetorTunai());
            dashboardView.getTarikTunaiButton().setOnAction(e -> handleTarikTunai());
            dashboardView.getTransferButton().setOnAction(e -> handleTransfer());
            dashboardView.getRiwayatButton().setOnAction(e -> handleRiwayat());
            System.out.println("Event handlers untuk dashboard berhasil diatur");
        } catch (Exception e) {
            System.err.println("Error setting up dashboard event handlers: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void refreshUserData() {
        BankUser refreshedUser = userDataService.cariUser(currentUser.getUsername());
        if (refreshedUser != null) {
            this.currentUser = refreshedUser;
            System.out.println("Data user diperbarui - Saldo: " + currentUser.getSaldo());
        } else {
            System.err.println("Warning: User tidak ditemukan saat refresh");
        }
    }

    private void handleSetorTunai() {
        try {
            System.out.println("Navigasi ke Setor Tunai");
            setorTunaiController = new SetorTunaiController(userDataService, currentUser, this);
            setorTunaiController.showView(primaryStage);
        } catch (Exception e) {
            System.err.println("Error navigasi ke Setor Tunai: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleTarikTunai() {
        try {
            System.out.println("Navigasi ke Tarik Tunai");
            tarikTunaiController = new TarikTunaiController(userDataService, currentUser, this);
            tarikTunaiController.showView(primaryStage);
        } catch (Exception e) {
            System.err.println("Error navigasi ke Tarik Tunai: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleTransfer() {
        try {
            System.out.println("Navigasi ke Transfer");
            transferController = new TransferController(userDataService, currentUser, this);
            transferController.showView(primaryStage);
        } catch (Exception e) {
            System.err.println("Error navigasi ke Transfer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleRiwayat() {
        try {
            System.out.println("Navigasi ke Riwayat Transaksi");
            riwayatController = new RiwayatController(userDataService, currentUser, this, transactionService);
            riwayatController.showView(primaryStage);
        } catch (Exception e) {
            System.err.println("Error navigasi ke Riwayat: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void updateCurrentUser() {
        refreshUserData();
        if (dashboardView != null) {
            dashboardView.updateUserInfo(currentUser);
        }
    }
    
    public BankUser getCurrentUser() {
        return currentUser;
    }

    public TransactionService getTransactionService() {
        return transactionService;
    }
}