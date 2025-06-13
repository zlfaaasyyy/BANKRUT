package project.bankrut.view;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public abstract class BaseView {
    protected VBox root;
    
    public BaseView() {
        root = new VBox();
        inisialisasiRoot();
    }
    
    private void inisialisasiRoot() {
        root = new VBox();
        BackgroundFill backgroundFill = new BackgroundFill(
            Color.web("rgb(255, 235, 238)"), 
            CornerRadii.EMPTY, 
            Insets.EMPTY
        );
        root.setBackground(new Background(backgroundFill));
    }
    
    protected abstract void inisialisasiKomponen();
    protected abstract void setupLayout();
    protected abstract void applyStyles();

    public VBox getRoot() {
        return root;
    }
}