package project.bankrut.view;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import project.bankrut.model.BankUser;
import java.text.DecimalFormat;

public class SetorTunaiView {
    private VBox root;
    private Button backButton;
    private TextField nominalField;
    private Button konfirmasiButton;
    private Label warningLabel;
    private BankUser currentUser;
    private DecimalFormat currencyFormat;
    private VBox pinContainer;
    private HBox pinDisplayContainer;
    private Label[] pinDots;
    private StringBuilder currentPin;
    private GridPane keypadGrid;
    private boolean showingPinEntry = false;
    private ScrollPane scrollPane;
    private TransactionCallback transactionCallback;
    private static final String PRIMARY_COLOR = "rgb(192, 21, 21)";
    private static final String PRIMARY_DARK = "rgb(161, 13, 13)";
    private static final String BACKGROUND_COLOR = "rgb(255, 235, 238)";
    private static final String[] QUICK_AMOUNTS = {"50.000", "100.000", "150.000", "200.000", "250.000", "500.000"};
    private static final int[] QUICK_VALUES = {50000, 100000, 150000, 200000, 250000, 500000};

    public interface TransactionCallback {
        void onTransactionComplete(double nominal);
    }

    public SetorTunaiView(BankUser user) {
        this.currentUser = user;
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
        nominalField = new TextField();
        konfirmasiButton = new Button("Konfirmasi");
        warningLabel = new Label();
        pinContainer = new VBox();
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
        scrollPane.setStyle("-fx-background:" + BACKGROUND_COLOR + ";" + "-fx-background-color:" + BACKGROUND_COLOR + ";" + "-fx-control-inner-background:" + BACKGROUND_COLOR + ";");
        scrollPane.setPannable(true);
        scrollPane.setVmax(1.0);
        scrollPane.setVmin(0.0);
        
        VBox contentContainer = new VBox();
        contentContainer.setAlignment(Pos.TOP_CENTER);
        contentContainer.setPadding(new Insets(20, 30, 30, 30));
        contentContainer.setSpacing(20);
        contentContainer.setStyle("-fx-background-color:" + BACKGROUND_COLOR + ";");
        
        VBox nominalSection = createNominalSection();
        
        warningLabel.setText("");
        warningLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
        warningLabel.setTextFill(Color.web("#F44336"));
        warningLabel.setAlignment(Pos.CENTER);
        warningLabel.setVisible(false);
        
        setupPinSection();
        
        HBox buttonContainer = new HBox();
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(20, 0, 20, 0));
        konfirmasiButton.setPrefWidth(280);
        konfirmasiButton.setPrefHeight(45);
        buttonContainer.getChildren().add(konfirmasiButton);
        
        contentContainer.getChildren().addAll(nominalSection, warningLabel, pinContainer, buttonContainer);
        scrollPane.setContent(contentContainer);
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
        header.setStyle("-fx-background-color:" + PRIMARY_COLOR + ";");

        backButton.setPrefSize(40, 40);

        HBox titleContainer = new HBox();
        titleContainer.setAlignment(Pos.CENTER);
        HBox.setHgrow(titleContainer, Priority.ALWAYS);
        
        Label titleLabel = new Label("Setor Tunai");
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

    private VBox createNominalSection() {
        VBox nominalSection = new VBox();
        nominalSection.setAlignment(Pos.CENTER);
        nominalSection.setSpacing(15);
        
        Label instructionLabel = new Label("Masukkan nominal yang ingin disetor:");
        instructionLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        instructionLabel.setTextFill(Color.web("#424242"));
        instructionLabel.setAlignment(Pos.CENTER);
        
        VBox quickAmountContainer = new VBox();
        quickAmountContainer.setAlignment(Pos.CENTER);
        quickAmountContainer.setSpacing(10);
        
        Label quickAmountLabel = new Label("Pilih nominal cepat:");
        quickAmountLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
        quickAmountLabel.setTextFill(Color.web("#666666"));
        
        GridPane quickButtonGrid = new GridPane();
        quickButtonGrid.setAlignment(Pos.CENTER);
        quickButtonGrid.setHgap(10);
        quickButtonGrid.setVgap(10);
        
        for (int i = 0; i < QUICK_AMOUNTS.length; i++) {
            Button quickButton = createQuickAmountButton("Rp. " + QUICK_AMOUNTS[i], QUICK_VALUES[i]);
            quickButtonGrid.add(quickButton, i % 2, i / 2);
        }
        
        quickAmountContainer.getChildren().addAll(quickAmountLabel, quickButtonGrid);
        
        VBox nominalContainer = new VBox();
        nominalContainer.setAlignment(Pos.CENTER);
        nominalContainer.setSpacing(10);
        nominalContainer.setPadding(new Insets(20));
        nominalContainer.setStyle("-fx-background-color: #FFFFFF;" + "-fx-background-radius: 15;" + "-fx-border-radius: 15;" + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 8, 0, 0, 2);");
        
        Label rpLabel = new Label("Rp.");
        rpLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 18));
        rpLabel.setTextFill(Color.web("#1565C0"));
        
        nominalField.setPromptText("0");
        nominalField.setPrefWidth(200);
        nominalField.setPrefHeight(40);
        nominalField.setAlignment(Pos.CENTER);
        nominalField.setStyle("-fx-background-color: transparent;" + "-fx-border-color: transparent;" + "-fx-font-size: 24px;" + "-fx-font-weight: bold;" + "-fx-text-fill: #424242;" + "-fx-alignment: center;");

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
                } catch (NumberFormatException e) {}
            } else if (isNowFocused && !nominalField.getText().isEmpty()) {
                String unformatted = nominalField.getText().replaceAll("[^\\d]", "");
                nominalField.setText(unformatted);
            }
        });
        
        HBox nominalInputBox = new HBox();
        nominalInputBox.setAlignment(Pos.CENTER);
        nominalInputBox.setSpacing(5);
        nominalInputBox.getChildren().addAll(rpLabel, nominalField);
        
        nominalContainer.getChildren().add(nominalInputBox);
        
        Label infoLabel = new Label("Minimal Rp. 50.000 (kelipatan Rp. 50.000)");
        infoLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 11));
        infoLabel.setTextFill(Color.web("#666666"));
        infoLabel.setAlignment(Pos.CENTER);
        
        nominalSection.getChildren().addAll(instructionLabel, quickAmountContainer, nominalContainer, infoLabel);
        return nominalSection;
    }
    
    private Button createQuickAmountButton(String text, int value) {
        Button button = new Button(text);
        button.setPrefWidth(120);
        button.setPrefHeight(35);
        
        String normalStyle = "-fx-background-color:" + BACKGROUND_COLOR + ";" + "-fx-text-fill:" + PRIMARY_COLOR + ";" + "-fx-font-size: 12px;" + "-fx-font-weight: bold;" + "-fx-background-radius: 8;" + "-fx-border-radius: 8;" + "-fx-border-color:" + PRIMARY_COLOR + ";" + "-fx-border-width: 1;" + "-fx-cursor: hand;";
        String hoverStyle = "-fx-background-color:" + PRIMARY_COLOR + ";" + "-fx-text-fill: white;" + "-fx-font-size: 12px;" + "-fx-font-weight: bold;" + "-fx-background-radius: 8;" + "-fx-border-radius: 8;" + "-fx-cursor: hand;";
        
        button.setStyle(normalStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(normalStyle));
        button.setOnAction(e -> {
            nominalField.setText(String.valueOf(value));
            hideWarning();
        });
        
        return button;
    }

    private void setupPinSection() {
        pinContainer.setAlignment(Pos.CENTER);
        pinContainer.setSpacing(20);
        pinContainer.setPadding(new Insets(20));
        pinContainer.setVisible(false);
        pinContainer.setManaged(false);
        
        Label pinLabel = new Label("Masukkan PIN Anda");
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
            keypadGrid.add(numButton, (i - 1) % 3, (i - 1) / 3);
        }
        
        keypadGrid.add(new Region(), 0, 3);
        keypadGrid.add(createKeypadButton("0"), 1, 3);
        
        Button deleteButton = createKeypadButton("⌫");
        deleteButton.setStyle("-fx-background-color: #2C3E50;" + "-fx-text-fill: white;" + "-fx-font-size: 18px;" + "-fx-font-weight: bold;" + "-fx-background-radius: 25;" + "-fx-border-radius: 25;" + "-fx-cursor: hand;");
        deleteButton.setOnAction(e -> deletePinDigit());
        keypadGrid.add(deleteButton, 2, 3);
    }

    private Button createKeypadButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(60, 60);
        
        String normalStyle = "-fx-background-color:" + PRIMARY_COLOR + ";" + "-fx-text-fill: white;" + "-fx-font-size: 18px;" + "-fx-font-weight: bold;" + "-fx-background-radius: 25;" + "-fx-border-radius: 25;" + "-fx-cursor: hand;";
        String hoverStyle = "-fx-background-color:" + PRIMARY_DARK + ";" + "-fx-text-fill: white;" + "-fx-font-size: 18px;" + "-fx-font-weight: bold;" + "-fx-background-radius: 25;" + "-fx-border-radius: 25;" + "-fx-cursor: hand;";
        
        button.setStyle(normalStyle);
        if (!text.equals("⌫")) {
            button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
            button.setOnMouseExited(e -> button.setStyle(normalStyle));
            button.setOnAction(e -> addPinDigit(text));
        }
        
        return button;
    }

    private void addPinDigit(String digit) {
        if (currentPin.length() < 6) {
            currentPin.append(digit);
            updatePinDisplay();
            if (currentPin.length() == 6) validatePin();
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
            pinDots[i].setTextFill(i < currentPin.length() ? Color.web("#C01515") : Color.web("#E0E0E0"));
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
            double newBalance = currentUser.getSaldo() + nominal;
            currentUser.updateSaldo(newBalance);
            showSuccessPopup(nominal);
        } catch (Exception e) {
            showWarning("Terjadi kesalahan saat memproses transaksi!");
        }
    }

    private void applyStyles() {
        root.setBackground(new Background(new BackgroundFill(Color.web(BACKGROUND_COLOR), CornerRadii.EMPTY, Insets.EMPTY)));

        String backButtonNormal = "-fx-background-color: rgba(255, 255, 255, 0.2);" + "-fx-text-fill: white;" + "-fx-font-size: 20px;" + "-fx-font-weight: bold;" + "-fx-background-radius: 20;" + "-fx-border-radius: 20;" + "-fx-cursor: hand;";
        String backButtonHover = "-fx-background-color: rgba(255, 255, 255, 0.3);" + "-fx-text-fill: white;" + "-fx-font-size: 20px;" + "-fx-font-weight: bold;" + "-fx-background-radius: 20;" + "-fx-border-radius: 20;" + "-fx-cursor: hand;";
        
        backButton.setStyle(backButtonNormal);
        backButton.setOnMouseEntered(e -> backButton.setStyle(backButtonHover));
        backButton.setOnMouseExited(e -> backButton.setStyle(backButtonNormal));

        String konfirmasiNormal = "-fx-background-color:" + PRIMARY_COLOR + ";" + "-fx-text-fill: white;" + "-fx-font-size: 16px;" + "-fx-font-weight: bold;" + "-fx-background-radius: 25;" + "-fx-border-radius: 25;" + "-fx-cursor: hand;";
        String konfirmasiHover = "-fx-background-color:" + PRIMARY_DARK + ";" + "-fx-text-fill: white;" + "-fx-font-size: 16px;" + "-fx-font-weight: bold;" + "-fx-background-radius: 25;" + "-fx-border-radius: 25;" + "-fx-cursor: hand;";
        
        konfirmasiButton.setStyle(konfirmasiNormal);
        konfirmasiButton.setOnMouseEntered(e -> konfirmasiButton.setStyle(konfirmasiHover));
        konfirmasiButton.setOnMouseExited(e -> konfirmasiButton.setStyle(konfirmasiNormal));
    }

    public void showNominalEntry() {
        showingPinEntry = false;
        pinContainer.setVisible(false);
        pinContainer.setManaged(false);
        konfirmasiButton.setText("Konfirmasi");
        Platform.runLater(() -> scrollPane.setVvalue(0.0));
    }

    public void showPinEntry() {
        String nominalText = nominalField.getText().trim();
        if (nominalText.isEmpty() || nominalText.equals("0")) {
            showWarning("Masukkan nominal terlebih dahulu!");
            return;
        }
        
        try {
            String cleanNominal = nominalText.replaceAll("[^\\d]", "");
            double nominal = Double.parseDouble(cleanNominal);
            
            if (nominal < 50000) {
                showWarning("Nominal minimal setor tunai adalah Rp. 50.000!");
                return;
            }
            
            if (nominal % 50000 != 0) {
                showWarning("Nominal harus kelipatan Rp. 50.000!");
                return;
            }
            
        } catch (NumberFormatException e) {
            showWarning("Nominal tidak valid!");
            return;
        }
        
        showingPinEntry = true;
        pinContainer.setVisible(true);
        pinContainer.setManaged(true);
        konfirmasiButton.setText("Kembali");
        currentPin.setLength(0);
        updatePinDisplay();
        hideWarning();
        
        Platform.runLater(() -> {
            Timeline timeline = new Timeline();
            KeyFrame keyFrame = new KeyFrame(Duration.millis(300), new KeyValue(scrollPane.vvalueProperty(), 0.6, Interpolator.EASE_OUT));
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();
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
        popupStage.setTitle("Transaksi Berhasil");
        
        VBox popupContent = new VBox();
        popupContent.setAlignment(Pos.CENTER);
        popupContent.setSpacing(20);
        popupContent.setPadding(new Insets(40));
        popupContent.setPrefSize(320, 280);
        popupContent.setStyle("-fx-background-color: white;" + "-fx-background-radius: 15;" + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 15, 0, 0, 0);" + "-fx-border-color: #E0E0E0;" + "-fx-border-width: 1;" + "-fx-border-radius: 15;");
        
        StackPane iconContainer = new StackPane();
        Circle successCircle = new Circle(35);
        successCircle.setFill(Color.web("#4CAF50"));
        
        Text checkMark = new Text("✓");
        checkMark.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        checkMark.setFill(Color.WHITE);
        iconContainer.getChildren().addAll(successCircle, checkMark);
        
        Label successLabel = new Label("Transaksi Berhasil!");
        successLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        successLabel.setTextFill(Color.web("#424242"));
        
        Label nominalLabel = new Label("Rp. " + currencyFormat.format(nominal));
        nominalLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 18));
        nominalLabel.setTextFill(Color.web("#000000"));
        
        Label infoLabel = new Label("telah disetor ke rekening Anda");
        infoLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        infoLabel.setTextFill(Color.web("#666666"));
        
        Label saldoLabel = new Label("Saldo baru: Rp. " + currencyFormat.format(currentUser.getSaldo()));
        saldoLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        saldoLabel.setTextFill(Color.web("#000000"));
        
        popupContent.getChildren().addAll(iconContainer, successLabel, nominalLabel, infoLabel, saldoLabel);
        
        Scene popupScene = new Scene(popupContent);
        popupStage.setScene(popupScene);
        popupStage.centerOnScreen();
        popupStage.show();
        
        PauseTransition delay = new PauseTransition(Duration.seconds(4));
        delay.setOnFinished(e -> {
            popupStage.close();
            if (transactionCallback != null) {
                transactionCallback.onTransactionComplete(nominal);
            }
        });
        delay.play();
    }

    public void setTransactionCallback(TransactionCallback callback) { this.transactionCallback = callback; }
    public VBox getRoot() { return root; }
    public Button getBackButton() { return backButton; }
    public Button getKonfirmasiButton() { return konfirmasiButton; }
    public boolean isShowingPinEntry() { return showingPinEntry; }
}
