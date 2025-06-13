package project.bankrut.view;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
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

public class RegisterView {
    private VBox root;
    private Button backButton;
    private Button daftarButton;
    private TextField namaLengkapField;
    private TextField usernameField;
    private PasswordField pinField;
    private Label warningLabel;

    public RegisterView() {
        initializeComponents();
        setupLayout();
        applyStyles();
    }

    private void initializeComponents() {
        root = new VBox();
        backButton = new Button("◀");
        daftarButton = new Button("Daftar");
        namaLengkapField = new TextField();
        usernameField = new TextField();
        pinField = new PasswordField();
        warningLabel = new Label();
    }

    private void setupLayout() {
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40, 30, 40, 30));
        root.setSpacing(25);

        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(0, 0, 20, 0));

        backButton.setPrefSize(40, 40);
        headerBox.getChildren().add(backButton);

        Label titleLabel = new Label("REGISTRASI");
        titleLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.web("#424242"));
        titleLabel.setAlignment(Pos.CENTER);

        HBox titleContainer = new HBox();
        titleContainer.setAlignment(Pos.CENTER);
        titleContainer.getChildren().add(titleLabel);

        VBox namaContainer = createFieldContainer("👤", "Nama Lengkap", namaLengkapField);
        VBox usernameContainer = createFieldContainer("@", "Username", usernameField);
        VBox pinContainer = createPasswordFieldContainer("🔒", "PIN (6 Digit)", pinField);

        pinField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("\\d*")) {
                pinField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (newValue.length() > 6) {
                pinField.setText(oldValue);
            }
        });

        warningLabel.setText("");
        warningLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
        warningLabel.setTextFill(Color.web("#F44336"));
        warningLabel.setAlignment(Pos.CENTER);
        warningLabel.setVisible(false);

        daftarButton.setPrefWidth(280);
        daftarButton.setPrefHeight(45);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        HBox buttonContainer = new HBox();
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().add(daftarButton);

        root.getChildren().addAll(
            headerBox,
            titleContainer,
            namaContainer,
            usernameContainer,
            pinContainer,
            warningLabel,
            spacer,
            buttonContainer
        );
    }

    private VBox createFieldContainer(String icon, String placeholder, TextField textField) {
        VBox container = new VBox();
        container.setSpacing(5);

        HBox fieldBox = new HBox();
        fieldBox.setAlignment(Pos.CENTER_LEFT);
        fieldBox.setSpacing(10);

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));
        iconLabel.setPrefWidth(30);

        textField.setPromptText(placeholder);
        textField.setPrefWidth(240);
        textField.setPrefHeight(35);

        Region underline = new Region();
        underline.setPrefHeight(2);
        underline.setMaxWidth(270);
        underline.setStyle("-fx-background-color: #BDBDBD;");

        fieldBox.getChildren().addAll(iconLabel, textField);
        container.getChildren().addAll(fieldBox, underline);

        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                underline.setStyle("-fx-background-color:rgb(192, 21, 21);");
            } else {
                underline.setStyle("-fx-background-color: #BDBDBD;");
            }
        });

        return container;
    }

    private VBox createPasswordFieldContainer(String icon, String placeholder, PasswordField passwordField) {
        VBox container = new VBox();
        container.setSpacing(5);

        HBox fieldBox = new HBox();
        fieldBox.setAlignment(Pos.CENTER_LEFT);
        fieldBox.setSpacing(10);

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));
        iconLabel.setPrefWidth(30);

        passwordField.setPromptText(placeholder);
        passwordField.setPrefWidth(240);
        passwordField.setPrefHeight(35);

        Region underline = new Region();
        underline.setPrefHeight(2);
        underline.setMaxWidth(270);
        underline.setStyle("-fx-background-color: #BDBDBD;");

        fieldBox.getChildren().addAll(iconLabel, passwordField);
        container.getChildren().addAll(fieldBox, underline);

        passwordField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                underline.setStyle("-fx-background-color:rgb(192, 21, 21);");
            } else {
                underline.setStyle("-fx-background-color: #BDBDBD;");
            }
        });

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
            "-fx-background-color: transparent; " +
            "-fx-text-fill:rgb(192, 21, 21); " +
            "-fx-font-size: 24px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 20; " +
            "-fx-border-radius: 20; " +
            "-fx-border-color:rgb(192, 21, 21); " +
            "-fx-border-width: 2; " +
            "-fx-cursor: hand;"
        );

        backButton.setOnMouseEntered(e -> {
            backButton.setStyle(
                "-fx-background-color:rgb(192, 21, 21);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 24px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 20;" +
                "-fx-border-radius: 20;" +
                "-fx-cursor: hand;"
            );
        });

        backButton.setOnMouseExited(e -> {
            backButton.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill:rgb(192, 21, 21);" +
                "-fx-font-size: 24px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 20;" +
                "-fx-border-radius: 20;" +
                "-fx-border-color:rgb(192, 21, 21);" +
                "-fx-border-width: 2;" +
                "-fx-cursor: hand;"
            );
        });

        String fieldStyle =
            "-fx-background-color: transparent;" +
            "-fx-border-color: transparent;" +
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #424242;";

        namaLengkapField.setStyle(fieldStyle);
        usernameField.setStyle(fieldStyle); 
        pinField.setStyle(fieldStyle); 

        daftarButton.setStyle(
            "-fx-background-color:rgb(192, 21, 21);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 25;" +
            "-fx-border-radius: 25;" +
            "-fx-cursor: hand;"
        );

        daftarButton.setOnMouseEntered(e -> {
            daftarButton.setStyle(
                "-fx-background-color:rgb(161, 13, 13); " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 25;" +
                "-fx-border-radius: 25;" +
                "-fx-cursor: hand;"
            );
        });

        daftarButton.setOnMouseExited(e -> {
            daftarButton.setStyle(
                "-fx-background-color:rgb(192, 21, 21); " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 16px; " +
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

    public void showSuccessPopup(Runnable onComplete) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.UNDECORATED);
        popupStage.setTitle("Pendaftaran Berhasil");
        
        VBox popupContent = new VBox();
        popupContent.setAlignment(Pos.CENTER);
        popupContent.setSpacing(20);
        popupContent.setPadding(new Insets(40));
        popupContent.setPrefSize(300, 200);
        
        popupContent.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 15, 0, 0, 0);" +
            "-fx-border-color: #E0E0E0;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 15;"
        );
        
        StackPane iconContainer = new StackPane();
        Circle successCircle = new Circle(30);
        successCircle.setFill(Color.web("#4CAF50"));
        
        Text checkMark = new Text("✓");
        checkMark.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        checkMark.setFill(Color.WHITE);
        
        iconContainer.getChildren().addAll(successCircle, checkMark);
        
        Label successLabel = new Label("Pendaftaran Berhasil!");
        successLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 18));
        successLabel.setTextFill(Color.web("#424242"));
        
        Label infoLabel = new Label("Akun Anda telah dibuat");
        infoLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        infoLabel.setTextFill(Color.web("#666666"));
        
        popupContent.getChildren().addAll(iconContainer, successLabel, infoLabel);
        
        Scene popupScene = new Scene(popupContent);
        popupStage.setScene(popupScene);
        
        popupStage.centerOnScreen();
        popupStage.show();
        
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> {
            popupStage.close();
            onComplete.run();
        });
        delay.play();
    }

    public VBox getRoot() {
        return root;
    }

    public Button getBackButton() {
        return backButton;
    }

    public Button getDaftarButton() {
        return daftarButton;
    }

    public TextField getNamaLengkapField() {
        return namaLengkapField;
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public PasswordField getPinField() {
        return pinField;
    }
}