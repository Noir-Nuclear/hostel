package ui.controllers;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public abstract class Controller {
    public void addErrorWindow(String errorMessage) {
        StackPane root = new StackPane();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Error of input");
        Label errorLabel = new Label();
        errorLabel.setText(errorMessage);
        errorLabel.setStyle("-fx-font-size: 13px");
        root.getChildren().add(errorLabel);
        stage.setHeight(100);
        stage.setWidth(380);
        stage.show();
    }
}
