package project.bankrut.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import project.bankrut.model.BankUser;
import project.bankrut.service.UserDataService;
import project.bankrut.view.TransferView;
import project.bankrut.model.Transaction;

public class TransferController extends BaseController {
    private TransferView transferView;
    private BankUser currentUser;
    private DashboardController dashboardController;
    
    public TransferController(UserDataService userDataService, BankUser user, DashboardController dashboardController) {
        super(userDataService);
        this.currentUser = user;
        this.dashboardController = dashboardController;
        System.out.println("TransferController dibuat untuk user: " + user.getUsername());
    }

    @Override
    public void showView(Stage primaryStage) {
        try {
            setPrimaryStage(primaryStage);

            BankUser refreshedUser = userDataService.cariUser(currentUser.getUsername());
            if (refreshedUser != null) {
                this.currentUser = refreshedUser;
                System.out.println("Data user diperbarui untuk transfer - Saldo: " + currentUser.getSaldo());
            }
            
            transferView = new TransferView(currentUser, userDataService);
            transferView.setTransactionCallback(this::processSuccessfulTransaction);
            
            setupEventHandlers();
            
            Scene scene = new Scene(transferView.getRoot(), 360, 640);
            primaryStage.setTitle("BANKRUT - Transfer");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
            
            System.out.println("Transfer view berhasil ditampilkan untuk user: " + currentUser.getNamaLengkap());
        } catch (Exception e) {
            System.err.println("Error in TransferController.showView: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    protected void setupEventHandlers() {
        try {
            transferView.getBackButton().setOnAction(e -> handleBack());
            transferView.getLanjutButton().setOnAction(e -> handleLanjut());
            System.out.println("Event handlers untuk transfer berhasil diatur");
        } catch (Exception e) {
            System.err.println("Error setting up transfer event handlers: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleBack() {
        try {
            if (transferView.isShowingPinEntry()) {
                transferView.showNominalEntry();
                System.out.println("Kembali ke halaman nominal entry");
            } else if (transferView.isShowingNominalEntry()) {
                transferView.showAccountEntry();
                System.out.println("Kembali ke halaman account entry");
            } else {
                System.out.println("Kembali ke dashboard dari transfer");
                dashboardController.showView(primaryStage);
            }
        } catch (Exception e) {
            System.err.println("Error handling back button: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleLanjut() {
        try {
            if (!transferView.isShowingNominalEntry() && !transferView.isShowingPinEntry()) {
                // Currently showing account entry
                String accountNumber = transferView.getAccountNumberField().getText().trim();
                if (transferView.validateAccountNumber(accountNumber)) {
                    transferView.showNominalEntry();
                    System.out.println("Menampilkan halaman nominal entry");
                }
            } else if (transferView.isShowingNominalEntry()) {
                // Currently showing nominal entry
                transferView.showPinEntry();
                System.out.println("Menampilkan halaman PIN entry");
            } else {
                // Currently showing PIN entry, go back to nominal
                transferView.showNominalEntry();
                System.out.println("Kembali ke halaman nominal dari PIN entry");
            }
        } catch (Exception e) {
            System.err.println("Error handling lanjut button: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void processSuccessfulTransaction(double nominal, String targetAccountNumber, String targetName) {
    try {
        boolean updateSuccess = userDataService.updateUser(currentUser);
        if (updateSuccess) {
            System.out.println("Transfer berhasil - Nominal: " + nominal + 
                             " ke rekening: " + targetAccountNumber +
                             ", Saldo baru: " + currentUser.getSaldo());
            
            // Save transaction to history
            Transaction transaction = new Transaction(currentUser.getNamaLengkap(), 
                                                    currentUser.getNomorRekening(),
                                                    targetName,
                                                    targetAccountNumber,
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