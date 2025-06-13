package project.bankrut.controller;

import javafx.stage.Stage;
import project.bankrut.service.UserDataService;

public abstract class BaseController {
    protected Stage primaryStage;
    protected UserDataService userDataService;

    public BaseController(UserDataService userDataService) {
        this.userDataService = userDataService;
    }
    
    public abstract void showView(Stage primaryStage);
    protected abstract void setupEventHandlers();

    protected void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    
    protected UserDataService getUserDataService() {
        return userDataService;
    }
    
    protected boolean isPrimaryStageSet() {
        return primaryStage != null;
    }
    
    protected void logControllerAction(String controllerName, String action) {
        System.out.println(controllerName + " " + action + " pada " + 
                         new java.util.Date());
    }

    protected void handleError(String operation, Exception e) {
        System.err.println("Error dalam " + operation + ": " + e.getMessage());
        e.printStackTrace();
    }
}