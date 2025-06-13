package project.bankrut.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import project.bankrut.model.Transaction;
import java.text.DecimalFormat;
import java.util.List;

public class RiwayatView {
    private VBox root;
    private Button backButton;
    private ScrollPane scrollPane;
    private VBox transactionListContainer;
    private DecimalFormat currencyFormat;
    private Label emptyLabel;
    
    public RiwayatView() {
        this.currencyFormat = new DecimalFormat("#,###");
        initializeComponents();
        setupLayout();
        applyStyles();
    }
    
    private void initializeComponents() {
        root = new VBox();
        backButton = new Button("◀");
        scrollPane = new ScrollPane();
        transactionListContainer = new VBox();
        emptyLabel = new Label("Belum ada transaksi");
    }
    
    private void setupLayout() {
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(0);
        
        HBox header = createHeader();
        
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle(
            "-fx-background:rgb(255, 235, 238);" + 
            "-fx-background-color:rgb(255, 235, 238);" +
            "-fx-control-inner-background:rgb(255, 235, 238);"
        );
        
        transactionListContainer.setAlignment(Pos.TOP_CENTER);
        transactionListContainer.setPadding(new Insets(20));
        transactionListContainer.setSpacing(10);
        transactionListContainer.setStyle("-fx-background-color:rgb(255, 235, 238);");
        
        emptyLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        emptyLabel.setTextFill(Color.web("#666666"));
        emptyLabel.setAlignment(Pos.CENTER);
        
        scrollPane.setContent(transactionListContainer);
        scrollPane.setPrefViewportHeight(560);
        scrollPane.setMinHeight(560);
        scrollPane.setMaxHeight(560);
        
        root.getChildren().addAll(header, scrollPane);
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
    
    Label titleLabel = new Label("Riwayat Transaksi");
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
    
    public void displayTransactions(List<Transaction> transactions) {
        transactionListContainer.getChildren().clear();
        
        if (transactions.isEmpty()) {
            transactionListContainer.getChildren().add(emptyLabel);
        } else {
            for (Transaction transaction : transactions) {
                HBox transactionItem = createTransactionItem(transaction);
                transactionListContainer.getChildren().add(transactionItem);
            }
        }
    }
    
    private HBox createTransactionItem(Transaction transaction) {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(new Insets(15));
        container.setSpacing(10);
        container.setStyle(
            "-fx-background-color: #FFFFFF;" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 5, 0, 0, 2);" +
            "-fx-cursor: hand;"
        );
        
        VBox iconContainer = new VBox();
        iconContainer.setAlignment(Pos.CENTER);
        iconContainer.setPrefWidth(40);
        
        Label iconLabel = new Label();
        iconLabel.setFont(Font.font("Arial", 24));
        
        if (transaction.getTransactionType().equals("Setor Tunai")) {
            iconLabel.setText("⬇");
            iconLabel.setTextFill(Color.web("#4CAF50"));
        } else if (transaction.getTransactionType().equals("Tarik Tunai")) {
            iconLabel.setText("⬆");
            iconLabel.setTextFill(Color.web("#F44336"));
        } else {
            iconLabel.setText("↔");
            iconLabel.setTextFill(Color.web("#1565C0"));
        }
        
        iconContainer.getChildren().add(iconLabel);
        
        VBox infoContainer = new VBox();
        infoContainer.setSpacing(5);
        HBox.setHgrow(infoContainer, Priority.ALWAYS);
        
        Label typeLabel = new Label(transaction.getTransactionType());
        typeLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        typeLabel.setTextFill(Color.web("#424242"));
        
        Label dateLabel = new Label(transaction.getShortDate());
        dateLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 11));
        dateLabel.setTextFill(Color.web("#666666"));
        
        String detailText = "";
        if (transaction.getTransactionType().contains("Transfer")) {
            detailText = "ke " + transaction.getTargetName();
        }
        
        if (!detailText.isEmpty()) {
            Label detailLabel = new Label(detailText);
            detailLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
            detailLabel.setTextFill(Color.web("#666666"));
            infoContainer.getChildren().addAll(typeLabel, detailLabel, dateLabel);
        } else {
            infoContainer.getChildren().addAll(typeLabel, dateLabel);
        }
        
        VBox amountContainer = new VBox();
        amountContainer.setAlignment(Pos.CENTER_RIGHT);
        
        Label amountLabel = new Label();
        amountLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        
        if (transaction.getTransactionType().equals("Setor Tunai")) {
            amountLabel.setText("+ Rp. " + currencyFormat.format(transaction.getAmount()));
            amountLabel.setTextFill(Color.web("#4CAF50"));
        } else if (transaction.getTransactionType().equals("Tarik Tunai")) {
            amountLabel.setText("- Rp. " + currencyFormat.format(transaction.getAmount()));
            amountLabel.setTextFill(Color.web("#F44336"));
        } else {
            amountLabel.setText("- Rp. " + currencyFormat.format(transaction.getAmount()));
            amountLabel.setTextFill(Color.web("#424242"));
        }
        
        amountContainer.getChildren().add(amountLabel);
        
        container.getChildren().addAll(iconContainer, infoContainer, amountContainer);
        
        container.setOnMouseEntered(e -> {
            container.setStyle(
                "-fx-background-color: #F5F5F5;" +
                "-fx-background-radius: 10;" +
                "-fx-border-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.15), 8, 0, 0, 3);" +
                "-fx-cursor: hand;"
            );
        });
        
        container.setOnMouseExited(e -> {
            container.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 10;" +
                "-fx-border-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 5, 0, 0, 2);" +
                "-fx-cursor: hand;"
            );
        });
        
        container.setUserData(transaction);
        
        return container;
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

    public VBox getTransactionListContainer() { 
        return transactionListContainer; 
    }
}