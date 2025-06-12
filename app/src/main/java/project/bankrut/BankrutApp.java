package project.bankrut;

import javafx.application.Application;
import javafx.stage.Stage;
import project.bankrut.controller.LoginController;
import project.bankrut.service.JsonUserDataService;
import project.bankrut.service.UserDataService;

public class BankrutApp extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        UserDataService userDataService = new JsonUserDataService();
        LoginController loginController = new LoginController(userDataService);
        loginController.showView(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

