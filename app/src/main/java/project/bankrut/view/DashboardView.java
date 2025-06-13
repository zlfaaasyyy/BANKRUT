package project.bankrut.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import project.bankrut.model.BankUser;
import java.text.DecimalFormat;

public class DashboardView extends BaseView {
    private Label bankrutLabel;
    private Label nomorRekeningLabel;
    private VBox saldoContainer;
    private Label saldoTitleLabel;
    private Label saldoAmountLabel;
    private Button setorTunaiButton;
    private Button tarikTunaiButton;
    private Button transferButton;
    private Button riwayatButton;
    private BankUser currentUser;
    private DecimalFormat currencyFormat;

    public DashboardView(BankUser user) {
        this.currentUser = user;
        this.currencyFormat = new DecimalFormat("#,###.00");
        inisialisasiKomponen();
        setupLayout();
        applyStyles();
    }

    @Override
    protected void inisialisasiKomponen() {
        bankrutLabel = new Label("BANKRUT");
        nomorRekeningLabel = new Label("Nomor rekening: " + currentUser.getNomorRekening());
        
        saldoContainer = new VBox();
        saldoTitleLabel = new Label("Saldo Rekening:");
        saldoAmountLabel = new Label("Rp. " + currencyFormat.format(currentUser.getSaldo()));
        
        setorTunaiButton = createIconButton("🏧", "Setor Tunai");
        tarikTunaiButton = createIconButton("💳", "Tarik Tunai");
        transferButton = createIconButton("💸", "Transfer");
        riwayatButton = createIconButton("📄", "Riwayat");
    }

    @Override
    protected void setupLayout() {
        root.setAlignment(Pos.TOP_LEFT);
        root.setPadding(new Insets(20, 20, 20, 20));
        root.setSpacing(15);

        HBox topSection = new HBox();
        topSection.setAlignment(Pos.CENTER_LEFT);
        bankrutLabel.setAlignment(Pos.CENTER_LEFT);
        topSection.getChildren().add(bankrutLabel);

        HBox accountSection = new HBox();
        accountSection.setAlignment(Pos.CENTER_LEFT);
        nomorRekeningLabel.setAlignment(Pos.CENTER_LEFT);
        accountSection.getChildren().add(nomorRekeningLabel);

        saldoContainer.setAlignment(Pos.CENTER_LEFT);
        saldoContainer.setPadding(new Insets(15, 20, 15, 20));
        saldoContainer.setSpacing(8);
        saldoContainer.getChildren().addAll(saldoTitleLabel, saldoAmountLabel);

        GridPane buttonGrid = new GridPane();
        buttonGrid.setAlignment(Pos.CENTER);
        buttonGrid.setHgap(15);
        buttonGrid.setVgap(15);
        buttonGrid.setPadding(new Insets(20, 0, 0, 0));

        buttonGrid.add(setorTunaiButton, 0, 0);
        buttonGrid.add(tarikTunaiButton, 1, 0);
        buttonGrid.add(transferButton, 0, 1);
        buttonGrid.add(riwayatButton, 1, 1);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        root.getChildren().addAll(
            topSection,
            accountSection,
            saldoContainer,
            spacer,
            buttonGrid
        );
    }

    @Override
    protected void applyStyles() {
        bankrutLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));
        bankrutLabel.setTextFill(Color.web("#C01515"));

        nomorRekeningLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        nomorRekeningLabel.setTextFill(Color.web("#424242"));

        saldoContainer.setStyle(
            "-fx-background-color: #FFFFFF;" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 8, 0, 0, 2);"
        );

        saldoTitleLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        saldoTitleLabel.setTextFill(Color.web("#666666"));

        saldoAmountLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        saldoAmountLabel.setTextFill(Color.web("#000000"));

        applyButtonStyles();
    }

    private Button createIconButton(String icon, String text) {
        Button button = new Button();
        
        VBox buttonContent = new VBox();
        buttonContent.setAlignment(Pos.CENTER);
        buttonContent.setSpacing(8);
        
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font("Arial", 24));
        
        Label textLabel = new Label(text);
        textLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 11));
        textLabel.setTextFill(Color.web("#424242"));
        textLabel.setWrapText(true);
        textLabel.setAlignment(Pos.CENTER);
        
        buttonContent.getChildren().addAll(iconLabel, textLabel);
        button.setGraphic(buttonContent);
        button.setPrefSize(140, 100);
        
        return button;
    }

    private void applyButtonStyles() {
        String buttonStyle = 
            "-fx-background-color:rgb(255, 235, 238);" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-border-color: #E0E0E0;" +
            "-fx-border-width: 1;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 5, 0, 0, 1);";

        String buttonHoverStyle = 
            "-fx-background-color:rgb(255, 235, 238);" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-border-color:rgb(192, 21, 21);" +
            "-fx-border-width: 2;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(192, 21, 21, 0.3), 8, 0, 0, 2);";

        Button[] buttons = {setorTunaiButton, tarikTunaiButton, transferButton, riwayatButton};
        
        for (Button button : buttons) {
            button.setStyle(buttonStyle);
            
            button.setOnMouseEntered(e -> button.setStyle(buttonHoverStyle));
            button.setOnMouseExited(e -> button.setStyle(buttonStyle));
        }
    }

    public void updateUserInfo(BankUser updatedUser) {
        this.currentUser = updatedUser;
        nomorRekeningLabel.setText("Nomor rekening: " + currentUser.getNomorRekening());
        saldoAmountLabel.setText("Rp. " + currencyFormat.format(currentUser.getSaldo()));
    }

    public Button getSetorTunaiButton() { 
        return setorTunaiButton; 
    }

    public Button getTarikTunaiButton() { 
        return tarikTunaiButton; 
    }

    public Button getTransferButton() { 
        return transferButton; 
    }

    public Button getRiwayatButton() { 
        return riwayatButton; 
    }
}