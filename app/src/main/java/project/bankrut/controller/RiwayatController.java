package project.bankrut.controller;

import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import project.bankrut.model.BankUser;
import project.bankrut.model.Transaction;
import project.bankrut.service.TransactionService;
import project.bankrut.service.UserDataService;
import project.bankrut.view.DetailTransaksiView;
import project.bankrut.view.RiwayatView;
import java.util.List;

public class RiwayatController extends BaseController {
    private RiwayatView riwayatView;
    private BankUser currentUser;
    private DashboardController dashboardController;
    private TransactionService transactionService;
    
    public RiwayatController(UserDataService userDataService, BankUser user, 
                           DashboardController dashboardController, TransactionService transactionService) {
        super(userDataService);
        this.currentUser = user;
        this.dashboardController = dashboardController;
        this.transactionService = transactionService;
        System.out.println("RiwayatController dibuat untuk user: " + user.getUsername());
    }
    
    @Override
    public void showView(Stage primaryStage) {
        try {
            setPrimaryStage(primaryStage);
            
            riwayatView = new RiwayatView();
            setupEventHandlers();
            loadUserTransactions();
            
            Scene scene = new Scene(riwayatView.getRoot(), 360, 640);
            primaryStage.setTitle("BANKRUT - Riwayat Transaksi");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
            
            System.out.println("Riwayat view berhasil ditampilkan");
        } catch (Exception e) {
            System.err.println("Error menampilkan riwayat view: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    @Override
    protected void setupEventHandlers() {
        try {
            riwayatView.getBackButton().setOnAction(e -> handleBack());
            System.out.println("Event handlers untuk riwayat berhasil diatur");
        } catch (Exception e) {
            System.err.println("Error setting up riwayat event handlers: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void loadUserTransactions() {
        try {
            List<Transaction> userTransactions = transactionService.getUserTransactions(currentUser.getUsername());
            riwayatView.displayTransactions(userTransactions);
            setupTransactionClickHandlers();
            System.out.println("Berhasil load " + userTransactions.size() + " transaksi");
        } catch (Exception e) {
            System.err.println("Error loading transactions: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void setupTransactionClickHandlers() {
        try {
            riwayatView.getTransactionListContainer().getChildren().forEach(node -> {
                if (node instanceof HBox && node.getUserData() instanceof Transaction) {
                    setupSingleTransactionClick(node);
                }
            });
        } catch (Exception e) {
            System.err.println("Error setting up transaction click handlers: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void setupSingleTransactionClick(javafx.scene.Node node) {
        node.setOnMouseClicked(e -> {
            Transaction transaction = (Transaction) node.getUserData();
            showTransactionDetail(transaction);
        });
    }

    private void handleBack() {
        try {
            System.out.println("Kembali ke dashboard dari riwayat");
            dashboardController.showView(primaryStage);
        } catch (Exception e) {
            System.err.println("Error handling back navigation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showTransactionDetail(Transaction transaction) {
        try {
            System.out.println("Menampilkan detail transaksi: " + transaction.getReferenceNumber());

            DetailTransaksiView detailView = new DetailTransaksiView(transaction);
            setupDetailViewBackButton(detailView);

            Scene detailScene = new Scene(detailView.getRoot(), 360, 640);
            primaryStage.setScene(detailScene);
        } catch (Exception e) {
            System.err.println("Error showing transaction detail: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupDetailViewBackButton(DetailTransaksiView detailView) {
        detailView.getBackButton().setOnAction(e -> {
            showView(primaryStage);
        });
    }
}