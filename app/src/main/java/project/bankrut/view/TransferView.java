package project.bankrut.view;

import javafx.animation.*;
import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.util.Duration;
import project.bankrut.model.BankUser;
import project.bankrut.service.UserDataService;
import java.text.DecimalFormat;

public class TransferView {
    private VBox root;
    private Button backButton;
    private Button lanjutButton;
    private Label warningLabel;
    private BankUser currentUser;
    private UserDataService userDataService;
    private DecimalFormat currencyFormat;
    private ScrollPane scrollPane;
    private VBox accountContainer;
    private TextField accountNumberField;
    private Label targetUserLabel;
    private VBox nominalContainer;
    private TextField nominalField;
    private VBox pinContainer;
    private Label pinLabel;
    private HBox pinDisplayContainer;
    private Label[] pinDots;
    private StringBuilder currentPin;
    private GridPane keypadGrid;
    private boolean showingAccountEntry = true;
    private boolean showingNominalEntry = false;
    private boolean showingPinEntry = false;
    
    private BankUser targetUser;

    public TransferView(BankUser user, UserDataService userDataService) {
        this.currentUser = user;
        this.userDataService = userDataService;
        this.currencyFormat = new DecimalFormat("#,###");
        this.currentPin = new StringBuilder();
        this.pinDots = new Label[6];
        initializeComponents();
        setupLayout();
        applyStyles();
    }

    private void initializeComponents() {
        root = new VBox();
        backButton = new Button("◀");
        lanjutButton = new Button("Lanjut");
        warningLabel = new Label();
        accountContainer = new VBox();
        accountNumberField = new TextField();
        targetUserLabel = new Label();
        nominalContainer = new VBox();
        nominalField = new TextField();
        pinContainer = new VBox();
        pinLabel = new Label("Masukkan PIN Anda");
        pinDisplayContainer = new HBox();
        
        for (int i = 0; i < 6; i++) {
            pinDots[i] = new Label("●");
            pinDots[i].setFont(Font.font("Arial", 20));
            pinDots[i].setTextFill(Color.web("#E0E0E0"));
            pinDisplayContainer.getChildren().add(pinDots[i]);
        }
        
        createKeypad();
    }

    private void setupLayout() {
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(0);

        HBox header = createHeader();
        
        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle(
            "-fx-background: rgb(255, 235, 238);" + 
            "-fx-background-color: rgb(255, 235, 238);" +
            "-fx-control-inner-background: rgb(255, 235, 238);"
        );
        
        scrollPane.setPannable(true);
        scrollPane.setVmax(1.0);
        scrollPane.setVmin(0.0);
        
        VBox contentContainer = new VBox();
        contentContainer.setAlignment(Pos.TOP_CENTER);
        contentContainer.setPadding(new Insets(20, 30, 30, 30));
        contentContainer.setSpacing(20);
        contentContainer.setStyle("-fx-background-color: rgb(255, 235, 238);");
        
        setupAccountSection();
        setupNominalSection();
        setupPinSection();
        
        warningLabel.setText("");
        warningLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
        warningLabel.setTextFill(Color.web("#F44336"));
        warningLabel.setAlignment(Pos.CENTER);
        warningLabel.setVisible(false);
        
        HBox buttonContainer = new HBox();
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(20, 0, 20, 0));
        lanjutButton.setPrefWidth(280);
        lanjutButton.setPrefHeight(45);
        buttonContainer.getChildren().add(lanjutButton);
        
        contentContainer.getChildren().addAll(
            accountContainer,
            nominalContainer,
            warningLabel,
            pinContainer,
            buttonContainer
        );
        
        scrollPane.setContent(contentContainer);
        scrollPane.setPrefViewportHeight(560);
        scrollPane.setMinHeight(560);
        scrollPane.setMaxHeight(560);
        
        root.getChildren().addAll(header, scrollPane);
        showAccountEntry();
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
    
    Label titleLabel = new Label("Transfer");
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

    private void setupAccountSection() {
        accountContainer.setAlignment(Pos.CENTER);
        accountContainer.setSpacing(15);
        
        Label instructionLabel = new Label("Masukkan nomor rekening tujuan:");
        instructionLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        instructionLabel.setTextFill(Color.web("#424242"));
        instructionLabel.setAlignment(Pos.CENTER);
        
        VBox accountInputContainer = new VBox();
        accountInputContainer.setAlignment(Pos.CENTER);
        accountInputContainer.setSpacing(10);
        accountInputContainer.setPadding(new Insets(20));
        accountInputContainer.setStyle(
            "-fx-background-color: #FFFFFF;" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 8, 0, 0, 2);"
        );
        
        accountNumberField.setPromptText("Masukkan 10 digit nomor rekening");
        accountNumberField.setPrefWidth(250);
        accountNumberField.setPrefHeight(40);
        accountNumberField.setAlignment(Pos.CENTER);
        accountNumberField.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: transparent;" +
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #424242;" +
            "-fx-alignment: center;"
        );

        accountNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                accountNumberField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (newValue.length() > 10) {
                accountNumberField.setText(oldValue);
            }
            
            targetUser = null;
            targetUserLabel.setText("");
            targetUserLabel.setVisible(false);
        });
        
        targetUserLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        targetUserLabel.setTextFill(Color.web("#4CAF50"));
        targetUserLabel.setAlignment(Pos.CENTER);
        targetUserLabel.setVisible(false);
        
        accountInputContainer.getChildren().addAll(accountNumberField, targetUserLabel);
        
        Label infoLabel = new Label("Pastikan nomor rekening tujuan benar");
        infoLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 11));
        infoLabel.setTextFill(Color.web("#666666"));
        infoLabel.setAlignment(Pos.CENTER);
        
        accountContainer.getChildren().addAll(instructionLabel, accountInputContainer, infoLabel);
    }

    private void setupNominalSection() {
        nominalContainer.setAlignment(Pos.CENTER);
        nominalContainer.setSpacing(15);
        nominalContainer.setVisible(false);
        nominalContainer.setManaged(false);
        
        Label instructionLabel = new Label("Masukkan nominal transfer:");
        instructionLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        instructionLabel.setTextFill(Color.web("#424242"));
        instructionLabel.setAlignment(Pos.CENTER);
        
        // Show current balance and target account
        Label saldoLabel = new Label("Saldo tersedia: Rp. " + currencyFormat.format(currentUser.getSaldo()));
        saldoLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        saldoLabel.setTextFill(Color.web("#C01515"));
        saldoLabel.setAlignment(Pos.CENTER);
        
        VBox nominalInputContainer = new VBox();
        nominalInputContainer.setAlignment(Pos.CENTER);
        nominalInputContainer.setSpacing(10);
        nominalInputContainer.setPadding(new Insets(20));
        nominalInputContainer.setStyle(
            "-fx-background-color: #FFFFFF;" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 8, 0, 0, 2);"
        );
        
        Label rpLabel = new Label("Rp.");
        rpLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 18));
        rpLabel.setTextFill(Color.web("#C01515"));
        
        nominalField.setPromptText("0");
        nominalField.setPrefWidth(200);
        nominalField.setPrefHeight(40);
        nominalField.setAlignment(Pos.CENTER);
        nominalField.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: transparent;" +
            "-fx-font-size: 24px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #424242;" +
            "-fx-alignment: center;"
        );

        nominalField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                nominalField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        
        nominalField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused && !nominalField.getText().isEmpty()) {
                try {
                    double value = Double.parseDouble(nominalField.getText());
                    nominalField.setText(currencyFormat.format(value));
                } catch (NumberFormatException e) {
                }
            } else if (isNowFocused && !nominalField.getText().isEmpty()) {
                String unformatted = nominalField.getText().replaceAll("[^\\d]", "");
                nominalField.setText(unformatted);
            }
        });
        
        HBox nominalInputBox = new HBox();
        nominalInputBox.setAlignment(Pos.CENTER);
        nominalInputBox.setSpacing(5);
        nominalInputBox.getChildren().addAll(rpLabel, nominalField);
        
        nominalInputContainer.getChildren().add(nominalInputBox);
        
        Label infoLabel = new Label("Minimal Rp. 10.000");
        infoLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 11));
        infoLabel.setTextFill(Color.web("#666666"));
        infoLabel.setAlignment(Pos.CENTER);
        
        nominalContainer.getChildren().addAll(instructionLabel, saldoLabel, nominalInputContainer, infoLabel);
    }

    private void setupPinSection() {
        pinContainer.setAlignment(Pos.CENTER);
        pinContainer.setSpacing(20);
        pinContainer.setPadding(new Insets(20));
        pinContainer.setVisible(false);
        pinContainer.setManaged(false);
        
        pinLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));
        pinLabel.setTextFill(Color.web("#424242"));
        pinLabel.setAlignment(Pos.CENTER);
        
        pinDisplayContainer.setAlignment(Pos.CENTER);
        pinDisplayContainer.setSpacing(15);
        
        pinContainer.getChildren().addAll(pinLabel, pinDisplayContainer, keypadGrid);
    }

    private void createKeypad() {
        keypadGrid = new GridPane();
        keypadGrid.setAlignment(Pos.CENTER);
        keypadGrid.setHgap(15);
        keypadGrid.setVgap(15);
        keypadGrid.setPadding(new Insets(20, 0, 0, 0));
        
        for (int i = 1; i <= 9; i++) {
            Button numButton = createKeypadButton(String.valueOf(i));
            int row = (i - 1) / 3;
            int col = (i - 1) % 3;
            keypadGrid.add(numButton, col, row);
        }
        
        Region emptySpace = new Region();
        keypadGrid.add(emptySpace, 0, 3);
        
        Button zeroButton = createKeypadButton("0");
        keypadGrid.add(zeroButton, 1, 3);
        
        Button deleteButton = createKeypadButton("⌫");
        deleteButton.setStyle(
            "-fx-background-color: #2C3E50;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 25;" +
            "-fx-border-radius: 25;" +
            "-fx-cursor: hand;"
        );
        deleteButton.setOnAction(e -> deletePinDigit());
        keypadGrid.add(deleteButton, 2, 3);
    }

    private Button createKeypadButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(60, 60);
        button.setStyle(
            "-fx-background-color:rgb(192, 21, 21);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 25;" +
            "-fx-border-radius: 25;" +
            "-fx-cursor: hand;"
        );
        
        button.setOnMouseEntered(e -> {
            if (!text.equals("⌫")) {
                button.setStyle(
                    "-fx-background-color:rgb(161, 13, 13);" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 18px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 25;" +
                    "-fx-border-radius: 25;" +
                    "-fx-cursor: hand;"
                );
            }
        });
        
        button.setOnMouseExited(e -> {
            if (!text.equals("⌫")) {
                button.setStyle(
                    "-fx-background-color:rgb(192, 21, 21);" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 18px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 25;" +
                    "-fx-border-radius: 25;" +
                    "-fx-cursor: hand;"
                );
            }
        });
        
        if (!text.equals("⌫")) {
            button.setOnAction(e -> addPinDigit(text));
        }
        
        return button;
    }

    private void addPinDigit(String digit) {
        if (currentPin.length() < 6) {
            currentPin.append(digit);
            updatePinDisplay();
            
            if (currentPin.length() == 6) {
                validatePin();
            }
        }
    }

    private void deletePinDigit() {
        if (currentPin.length() > 0) {
            currentPin.deleteCharAt(currentPin.length() - 1);
            updatePinDisplay();
        }
    }

    private void updatePinDisplay() {
        for (int i = 0; i < 6; i++) {
            if (i < currentPin.length()) {
                pinDots[i].setTextFill(Color.web("#C01515"));
            } else {
                pinDots[i].setTextFill(Color.web("#E0E0E0"));
            }
        }
    }

    private void validatePin() {
        String enteredPin = currentPin.toString();
        if (currentUser.validasi(currentUser.getUsername(), enteredPin)) {
            processTransaction();
        } else {
            showWarning("PIN yang Anda masukkan salah!");
            currentPin.setLength(0);
            updatePinDisplay();
        }
    }

    private void processTransaction() {
        try {
            String cleanNominal = nominalField.getText().replaceAll("[^\\d]", "");
            double nominal = Double.parseDouble(cleanNominal);
            double newBalance = currentUser.getSaldo() - nominal;
            currentUser.updateSaldo(newBalance);
            double targetNewBalance = targetUser.getSaldo() + nominal;
            targetUser.updateSaldo(targetNewBalance);
            userDataService.updateUser(targetUser);
            
            showSuccessPopup(nominal);
        } catch (Exception e) {
            showWarning("Terjadi kesalahan saat memproses transaksi!");
        }
    }

    public boolean validateAccountNumber(String accountNumber) {
        if (accountNumber.isEmpty()) {
            showWarning("Masukkan nomor rekening tujuan!");
            return false;
        }
        
        if (accountNumber.length() != 10) {
            showWarning("Nomor rekening harus 10 digit!");
            return false;
        }
        
        if (accountNumber.equals(currentUser.getNomorRekening())) {
            showWarning("Tidak dapat transfer ke rekening sendiri!");
            return false;
        }

        for (BankUser user : userDataService.getAllUsers()) {
            if (user.getNomorRekening().equals(accountNumber)) {
                targetUser = user;
                targetUserLabel.setText("✓ " + user.getNamaLengkap());
                targetUserLabel.setVisible(true);
                hideWarning();
                return true;
            }
        }
        
        showWarning("Nomor rekening tidak ditemukan!");
        return false;
    }

    public void showAccountEntry() {
        showingAccountEntry = true;
        showingNominalEntry = false;
        showingPinEntry = false;
        
        accountContainer.setVisible(true);
        accountContainer.setManaged(true);
        nominalContainer.setVisible(false);
        nominalContainer.setManaged(false);
        pinContainer.setVisible(false);
        pinContainer.setManaged(false);
        
        lanjutButton.setText("Lanjut");
        
        Platform.runLater(() -> {
            scrollPane.setVvalue(0.0);
        });
    }

    public void showNominalEntry() {
        String nominalText = nominalField.getText().trim();
        if (!nominalText.isEmpty()) {
            try {
                String cleanNominal = nominalText.replaceAll("[^\\d]", "");
                double nominal = Double.parseDouble(cleanNominal);
                
                if (nominal < 10000) {
                    showWarning("Nominal minimal transfer adalah Rp. 10.000!");
                    return;
                }
                
                if (nominal > currentUser.getSaldo()) {
                    showWarning("Saldo tidak mencukupi!");
                    return;
                }
            } catch (NumberFormatException e) {
                showWarning("Nominal tidak valid!");
                return;
            }
        }
        
        showingAccountEntry = false;
        showingNominalEntry = true;
        showingPinEntry = false;
        
        accountContainer.setVisible(false);
        accountContainer.setManaged(false);
        nominalContainer.setVisible(true);
        nominalContainer.setManaged(true);
        pinContainer.setVisible(false);
        pinContainer.setManaged(false);
        
        lanjutButton.setText("Lanjut");
        hideWarning();
        
        Platform.runLater(() -> {
            scrollPane.setVvalue(0.0);
        });
    }

    public void showPinEntry() {
        String nominalText = nominalField.getText().trim();
        if (nominalText.isEmpty() || nominalText.equals("0")) {
            showWarning("Masukkan nominal transfer terlebih dahulu!");
            return;
        }
        
        try {
            String cleanNominal = nominalText.replaceAll("[^\\d]", "");
            double nominal = Double.parseDouble(cleanNominal);
            
            if (nominal < 10000) {
                showWarning("Nominal minimal transfer adalah Rp. 10.000!");
                return;
            }
            
            if (nominal > currentUser.getSaldo()) {
                showWarning("Saldo tidak mencukupi!");
                return;
            }
            
        } catch (NumberFormatException e) {
            showWarning("Nominal tidak valid!");
            return;
        }
        
        showingAccountEntry = false;
        showingNominalEntry = false;
        showingPinEntry = true;
        
        accountContainer.setVisible(false);
        accountContainer.setManaged(false);
        nominalContainer.setVisible(false);
        nominalContainer.setManaged(false);
        pinContainer.setVisible(true);
        pinContainer.setManaged(true);
        
        lanjutButton.setText("Kembali");
        currentPin.setLength(0);
        updatePinDisplay();
        hideWarning();
        
        Platform.runLater(() -> {
            double targetValue = 0.6;
            Timeline timeline = new Timeline();
            KeyFrame keyFrame = new KeyFrame(Duration.millis(300), 
                new KeyValue(scrollPane.vvalueProperty(), targetValue, Interpolator.EASE_OUT));
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();
        });
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

        lanjutButton.setStyle(
            "-fx-background-color:rgb(192, 21, 21);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 25;" +
            "-fx-border-radius: 25;" +
            "-fx-cursor: hand;"
        );

        lanjutButton.setOnMouseEntered(e -> {
            lanjutButton.setStyle(
                "-fx-background-color:rgb(161, 13, 13);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 25;" +
                "-fx-border-radius: 25;" +
                "-fx-cursor: hand;"
            );
        });

        lanjutButton.setOnMouseExited(e -> {
            lanjutButton.setStyle(
                "-fx-background-color:rgb(192, 21, 21);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 25;" +
                "-fx-border-radius: 25;" +
                "-fx-cursor: hand;"
            );
        });
    }

    public void showWarning(String message) {
        warningLabel.setText(message);
        warningLabel.setVisible(true);
        warningLabel.setManaged(true);

        PauseTransition hideWarning = new PauseTransition(Duration.seconds(3));
        hideWarning.setOnFinished(e -> hideWarning());
        hideWarning.play();
    }

    public void hideWarning() {
        warningLabel.setVisible(false);
        warningLabel.setManaged(false);
    }

    public void showSuccessPopup(double nominal) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.UNDECORATED);
        popupStage.setTitle("Transfer Berhasil");
        
        VBox popupContent = new VBox();
        popupContent.setAlignment(Pos.CENTER);
        popupContent.setSpacing(20);
        popupContent.setPadding(new Insets(40));
        popupContent.setPrefSize(320, 300);
        
        popupContent.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 15, 0, 0, 0);" +
            "-fx-border-color: #E0E0E0;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 15;"
        );
        
        StackPane iconContainer = new StackPane();
        Circle successCircle = new Circle(35);
        successCircle.setFill(Color.web("#4CAF50"));
        
        Text checkMark = new Text("✓");
        checkMark.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        checkMark.setFill(Color.WHITE);
        
        iconContainer.getChildren().addAll(successCircle, checkMark);
        
        Label successLabel = new Label("Transfer Berhasil!");
        successLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        successLabel.setTextFill(Color.web("#424242"));
        
        Label nominalLabel = new Label("Rp. " + currencyFormat.format(nominal));
        nominalLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 18));
        nominalLabel.setTextFill(Color.web("#000000"));
        
        Label toLabel = new Label("kepada " + targetUser.getNamaLengkap());
        toLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        toLabel.setTextFill(Color.web("#666666"));
        
        Label accountLabel = new Label("Rekening: " + targetUser.getNomorRekening());
        accountLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
        accountLabel.setTextFill(Color.web("#999999"));
        
        Label saldoLabel = new Label("Saldo baru: Rp. " + currencyFormat.format(currentUser.getSaldo()));
        saldoLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        saldoLabel.setTextFill(Color.web("#000000"));
        
        popupContent.getChildren().addAll(iconContainer, successLabel, nominalLabel, toLabel, accountLabel, saldoLabel);
        
        Scene popupScene = new Scene(popupContent);
        popupStage.setScene(popupScene);
        
        popupStage.centerOnScreen();
        popupStage.show();
        
        PauseTransition delay = new PauseTransition(Duration.seconds(4));
        delay.setOnFinished(e -> {
        popupStage.close();
        if (transactionCallback != null) {
            transactionCallback.onTransactionComplete(nominal, targetUser.getNomorRekening(), targetUser.getNamaLengkap());
        }
    });
        delay.play();
    }

    public interface TransactionCallback {
    void onTransactionComplete(double nominal, String targetAccountNumber, String targetName);
    }
    
    private TransactionCallback transactionCallback;

    public void setTransactionCallback(TransactionCallback callback) {
        this.transactionCallback = callback;
    }

    public VBox getRoot() { 
        return root; 
    }

    public Button getBackButton() { 
        return backButton; 
    }
    public Button getLanjutButton() { 
        return lanjutButton; 
    }

    public TextField getAccountNumberField() { 
        return accountNumberField; 
    }

    public boolean isShowingAccountEntry() { 
        return showingAccountEntry; 
    }

    public boolean isShowingNominalEntry() { 
        return showingNominalEntry; 
    }

    public boolean isShowingPinEntry() { 
        return showingPinEntry; 
    }
}