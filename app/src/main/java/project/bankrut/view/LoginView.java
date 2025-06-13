package project.bankrut.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LoginView extends BaseView {
    private Button masukButton;
    private Button daftarButton;

    public LoginView() {
        super();
        inisialisasiKomponen();
        setupLayout();
        applyStyles();
    }

    @Override
    protected void inisialisasiKomponen() {
        masukButton = new Button("Masuk");
        daftarButton = new Button("Daftar");
    }

    @Override
    protected void setupLayout() {
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40, 30, 40, 30));
        root.setSpacing(25);

        Region topSpacer = new Region();
        VBox.setVgrow(topSpacer, Priority.ALWAYS);

        Label titleLabel = new Label("BANKRUT");
        titleLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 42));
        titleLabel.setTextFill(Color.web("rgb(192, 21, 21)"));
        titleLabel.setAlignment(Pos.CENTER);

        Label subtitleLabel = new Label("BANK RAKYAT UNHAS TERPADU");
        subtitleLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        subtitleLabel.setTextFill(Color.web("#666666"));
        subtitleLabel.setAlignment(Pos.CENTER);

        Region middleSpacer = new Region();
        middleSpacer.setPrefHeight(20);

        Label loginInstruction = new Label("Silahkan masuk jika sudah memiliki akun.");
        loginInstruction.setFont(Font.font("Tahoma", FontWeight.NORMAL, 13));
        loginInstruction.setTextFill(Color.web("#424242"));
        loginInstruction.setAlignment(Pos.CENTER);
        loginInstruction.setWrapText(true);

        masukButton.setPrefWidth(280);
        masukButton.setPrefHeight(45);

        Label registerInstruction = new Label("Atau, daftar jika belum memiliki akun.");
        registerInstruction.setFont(Font.font("Tahoma", FontWeight.NORMAL, 13));
        registerInstruction.setTextFill(Color.web("#424242"));
        registerInstruction.setAlignment(Pos.CENTER);
        registerInstruction.setWrapText(true);

        daftarButton.setPrefWidth(280);
        daftarButton.setPrefHeight(45);

        Region bottomSpacer = new Region();
        VBox.setVgrow(bottomSpacer, Priority.ALWAYS);

        root.getChildren().addAll(
            topSpacer,
            titleLabel,
            subtitleLabel,
            middleSpacer,
            loginInstruction,
            masukButton,
            registerInstruction,
            daftarButton,
            bottomSpacer
        );
    }

    @Override
    protected void applyStyles() {
        String buttonStyle = 
            "-fx-background-color:rgb(192, 21, 21);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 25;" +
            "-fx-border-radius: 25;" +
            "-fx-cursor: hand;";
            
        String buttonHoverStyle = 
            "-fx-background-color:rgb(161, 13, 13);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 25;" +
            "-fx-border-radius: 25;" +
            "-fx-cursor: hand;";

        masukButton.setStyle(buttonStyle);
        daftarButton.setStyle(buttonStyle);

        masukButton.setOnMouseEntered(e -> masukButton.setStyle(buttonHoverStyle));
        masukButton.setOnMouseExited(e -> masukButton.setStyle(buttonStyle));
        
        daftarButton.setOnMouseEntered(e -> daftarButton.setStyle(buttonHoverStyle));
        daftarButton.setOnMouseExited(e -> daftarButton.setStyle(buttonStyle));
    }

    public Button getMasukButton() { return masukButton; }
    public Button getDaftarButton() { return daftarButton; }
}