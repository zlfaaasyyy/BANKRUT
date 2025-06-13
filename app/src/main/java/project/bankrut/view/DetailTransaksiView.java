package project.bankrut.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import project.bankrut.model.Transaction;
import java.text.DecimalFormat;

public class DetailTransaksiView {
    private VBox root;
    private Button backButton;
    private VBox receiptContainer;
    private DecimalFormat currencyFormat;
    private Transaction transaction;
    
    public DetailTransaksiView(Transaction transaction) {
        this.transaction = transaction;
        this.currencyFormat = new DecimalFormat("#,###");
        initializeComponents();
        setupLayout();
        applyStyles();
    }
    
    private void initializeComponents() {
        root = new VBox();
        backButton = new Button("◀");
        receiptContainer = new VBox();
    }
    
    private void setupLayout() {
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(0);
        
        HBox header = createHeader();
        
        VBox contentContainer = new VBox();
        contentContainer.setAlignment(Pos.TOP_CENTER);
        contentContainer.setPadding(new Insets(20));
        contentContainer.setSpacing(20);
        contentContainer.setStyle("-fx-background-color: rgb(255, 235, 238);");
        
        createReceiptContent();
        
        contentContainer.getChildren().add(receiptContainer);
        
        root.getChildren().addAll(header, contentContainer);
    }
    
    private HBox createHeader() {
    HBox header = new HBox();
    header.setAlignment(Pos.CENTER_LEFT);
    header.setPadding(new Insets(20, 20, 0, 20));
    header.setPrefHeight(80);
    header.setStyle("-fx-background-color:rgb(192, 21, 21);");

    backButton.setPrefSize(40, 40);

    HBox titleContainer = new HBox();
    titleContainer.setAlignment(Pos.CENTER);
    HBox.setHgrow(titleContainer, Priority.ALWAYS);
    
    Label titleLabel = new Label("Detail Transaksi");
    titleLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
    titleLabel.setTextFill(Color.WHITE);
    
    titleContainer.getChildren().add(titleLabel);

    Region rightSpacer = new Region();
    rightSpacer.setPrefWidth(40);
    rightSpacer.setMinWidth(40);
    rightSpacer.setMaxWidth(40);
    
    header.getChildren().addAll(backButton, titleContainer, rightSpacer);
    return header;
}
    
    private void createReceiptContent() {
        receiptContainer.setAlignment(Pos.TOP_CENTER);
        receiptContainer.setPadding(new Insets(30));
        receiptContainer.setSpacing(20);
        receiptContainer.setMaxWidth(320);
        receiptContainer.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.15), 10, 0, 0, 3);"
        );

        VBox headerSection = new VBox();
        headerSection.setAlignment(Pos.CENTER);
        headerSection.setSpacing(10);
        
        StackPane iconContainer = new StackPane();
        Circle successCircle = new Circle(30);
        successCircle.setFill(Color.web("#4CAF50"));
        
        Label checkMark = new Label("✓");
        checkMark.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        checkMark.setTextFill(Color.WHITE);
        
        iconContainer.getChildren().addAll(successCircle, checkMark);
        
        Label successLabel = new Label("Transaksi Berhasil");
        successLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 18));
        successLabel.setTextFill(Color.web("#424242"));
        
        Label dateLabel = new Label(transaction.getFormattedDate());
        dateLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
        dateLabel.setTextFill(Color.web("#666666"));
        
        headerSection.getChildren().addAll(iconContainer, successLabel, dateLabel);

        Region separator1 = createSeparator();

        VBox totalSection = new VBox();
        totalSection.setAlignment(Pos.CENTER);
        totalSection.setSpacing(5);
        
        Label totalLabel = new Label("Total Transaksi");
        totalLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
        totalLabel.setTextFill(Color.web("#666666"));
        
        Label amountLabel = new Label("Rp" + currencyFormat.format(transaction.getAmount()));
        amountLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 24));
        amountLabel.setTextFill(Color.web("#000000"));
        
        totalSection.getChildren().addAll(totalLabel, amountLabel);

        Region separator2 = createSeparator();

        VBox detailsSection = new VBox();
        detailsSection.setSpacing(15);

        HBox refRow = createDetailRow("No. Ref", transaction.getReferenceNumber());
        detailsSection.getChildren().add(refRow);

        if (transaction.getTransactionType().contains("Transfer")) {
            VBox sourceSection = createAccountSection("Sumber Dana", 
                transaction.getSourceName(), 
                "BANKRUT", 
                formatAccountNumber(transaction.getSourceAccount()));
            
            VBox targetSection = createAccountSection("Tujuan", 
                transaction.getTargetName(), 
                "BANKRUT", 
                formatAccountNumber(transaction.getTargetAccount()));
            
            detailsSection.getChildren().addAll(sourceSection, targetSection);
        } else {
            VBox accountSection = createAccountSection("Rekening", 
                transaction.getSourceName(), 
                "BANKRUT", 
                formatAccountNumber(transaction.getSourceAccount()));
            detailsSection.getChildren().add(accountSection);
        }
        
        Region separator3 = createSeparator();
        
        VBox footerSection = new VBox();
        footerSection.setSpacing(10);
        
        HBox typeRow = createDetailRow("Jenis Transaksi", transaction.getTransactionType());
        HBox nominalRow = createDetailRow("Nominal", "Rp" + currencyFormat.format(transaction.getAmount()));
        
        footerSection.getChildren().addAll(typeRow, nominalRow);
        
        receiptContainer.getChildren().addAll(
            headerSection,
            separator1,
            totalSection,
            separator2,
            detailsSection,
            separator3,
            footerSection
        );
    }
    
    private Region createSeparator() {
        Region separator = new Region();
        separator.setPrefHeight(1);
        separator.setStyle("-fx-background-color:rgb(255, 235, 238);");
        return separator;
    }
    
    private HBox createDetailRow(String label, String value) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        
        Label labelText = new Label(label);
        labelText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
        labelText.setTextFill(Color.web("#666666"));
        labelText.setPrefWidth(100);
        
        Label valueText = new Label(value);
        valueText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
        valueText.setTextFill(Color.web("#424242"));
        valueText.setWrapText(true);
        HBox.setHgrow(valueText, Priority.ALWAYS);
        
        row.getChildren().addAll(labelText, valueText);
        return row;
    }
    
    private VBox createAccountSection(String title, String name, String bank, String accountNumber) {
        VBox section = new VBox();
        section.setSpacing(5);
        section.setPadding(new Insets(5, 0, 5, 0));
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
        titleLabel.setTextFill(Color.web("#666666"));
        
        HBox accountInfo = new HBox();
        accountInfo.setAlignment(Pos.CENTER_LEFT);
        accountInfo.setSpacing(10);

        VBox logoContainer = new VBox();
        logoContainer.setAlignment(Pos.CENTER);
        logoContainer.setPrefSize(40, 40);
        logoContainer.setStyle(
            "-fx-background-color:rgb(192, 21, 21);" +
            "-fx-background-radius: 20;"
        );
        
        Label logoText = new Label("BR");
        logoText.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        logoText.setTextFill(Color.WHITE);
        logoContainer.getChildren().add(logoText);
        
        VBox nameAndBank = new VBox();
        nameAndBank.setSpacing(2);
        
        Label nameLabel = new Label(name.toUpperCase());
        nameLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 13));
        nameLabel.setTextFill(Color.web("#424242"));
        
        HBox bankInfo = new HBox();
        bankInfo.setSpacing(5);
        
        Label bankLabel = new Label(bank);
        bankLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 11));
        bankLabel.setTextFill(Color.web("#666666"));
        
        Label accountLabel = new Label(accountNumber);
        accountLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 11));
        accountLabel.setTextFill(Color.web("#666666"));
        
        bankInfo.getChildren().addAll(bankLabel, accountLabel);
        nameAndBank.getChildren().addAll(nameLabel, bankInfo);
        
        accountInfo.getChildren().addAll(logoContainer, nameAndBank);
        section.getChildren().addAll(titleLabel, accountInfo);
        
        return section;
    }
    
    private String formatAccountNumber(String accountNumber) {
        if (accountNumber.length() >= 4) {
            String lastFour = accountNumber.substring(accountNumber.length() - 4);
            return "**** **** " + lastFour;
        }
        return accountNumber;
    }
    
    private void applyStyles() {
        BackgroundFill backgroundFill = new BackgroundFill(
            Color.web("rgb(255, 235, 238)"), 
            CornerRadii.EMPTY, 
            Insets.EMPTY
        );
        root.setBackground(new Background(backgroundFill));
        
        backButton.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.2);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 20px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 20;" +
            "-fx-border-radius: 20;" +
            "-fx-cursor: hand;"
        );
        
        backButton.setOnMouseEntered(e -> {
            backButton.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.3);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 20;" +
                "-fx-border-radius: 20;" +
                "-fx-cursor: hand;"
            );
        });
        
        backButton.setOnMouseExited(e -> {
            backButton.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.2);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 20;" +
                "-fx-border-radius: 20;" +
                "-fx-cursor: hand;"
            );
        });
    }
    
    public VBox getRoot() { 
        return root; 
    }

    public Button getBackButton() { 
        return backButton; 
    }
}