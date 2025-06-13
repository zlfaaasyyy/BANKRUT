package project.bankrut.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import project.bankrut.model.BankUser;
import project.bankrut.service.UserDataService;
import project.bankrut.view.SetorTunaiView;
import project.bankrut.model.Transaction;

public class SetorTunaiController extends BaseController {
    private SetorTunaiView setorTunaiView;
    private BankUser currentUser;
    private DashboardController dashboardController;
    
    public SetorTunaiController(UserDataService userDataService, BankUser user, DashboardController dashboardController) {
        super(userDataService);
        this.currentUser = user;
        this.dashboardController = dashboardController;
        System.out.println("SetorTunaiController dibuat untuk user: " + user.getUsername());
    }

    @Override
    public void showView(Stage primaryStage) {
        try {
            setPrimaryStage(primaryStage);

            BankUser refreshedUser = userDataService.cariUser(currentUser.getUsername());
            if (refreshedUser != null) {
                this.currentUser = refreshedUser;
                System.out.println("Data user diperbarui untuk setor tunai - Saldo: " + currentUser.getSaldo());
            }
            
            setorTunaiView = new SetorTunaiView(currentUser);
            setorTunaiView.setTransactionCallback(this::processSuccessfulTransaction);
            
            setupEventHandlers();
            
            Scene scene = new Scene(setorTunaiView.getRoot(), 360, 640);
            primaryStage.setTitle("BANKRUT - Setor Tunai");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
            
            System.out.println("Setor Tunai view berhasil ditampilkan untuk user: " + currentUser.getNamaLengkap());
        } catch (Exception e) {
            System.err.println("Error in SetorTunaiController.showView: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    protected void setupEventHandlers() {
        try {
            setorTunaiView.getBackButton().setOnAction(e -> handleBack());
            setorTunaiView.getKonfirmasiButton().setOnAction(e -> handleKonfirmasi());
            System.out.println("Event handlers untuk setor tunai berhasil diatur");
        } catch (Exception e) {
            System.err.println("Error setting up setor tunai event handlers: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleBack() {
        try {
            if (setorTunaiView.isShowingPinEntry()) {
                setorTunaiView.showNominalEntry();
                System.out.println("Kembali ke halaman nominal entry");
            } else {
                System.out.println("Kembali ke dashboard dari setor tunai");
                dashboardController.showView(primaryStage);
            }
        } catch (Exception e) {
            System.err.println("Error handling back button: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleKonfirmasi() {
        try {
            if (!setorTunaiView.isShowingPinEntry()) {
                setorTunaiView.showPinEntry();
                System.out.println("Menampilkan halaman PIN entry");
            } else {
                setorTunaiView.showNominalEntry();
                System.out.println("Kembali ke halaman nominal dari PIN entry");
            }
        } catch (Exception e) {
            System.err.println("Error handling konfirmasi button: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void processSuccessfulTransaction(double nominal) {
    try {
        boolean updateSuccess = userDataService.updateUser(currentUser);
        if (updateSuccess) {
            System.out.println("Setor tunai berhasil - Nominal: " + nominal + 
                             ", Saldo baru: " + currentUser.getSaldo());
            
            Transaction transaction = new Transaction(currentUser.getNamaLengkap(), 
            currentUser.getNomorRekening(), 
            nominal, 
            currentUser.getSaldo());
            dashboardController.getTransactionService().saveTransaction(transaction, currentUser.getUsername());
            
            dashboardController.showView(primaryStage);
        } else {
            System.err.println("Gagal update saldo user di database");

        }
    } catch (Exception e) {
        System.err.println("Error processing successful transaction: " + e.getMessage());
        e.printStackTrace();
        }
    }
}
