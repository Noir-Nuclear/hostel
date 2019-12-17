package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL s = getClass().getResource("../application.fxml");
        Parent root = FXMLLoader.load(s);
        Scene rootScene = new Scene(root);
        primaryStage.setTitle("Hostel");
        primaryStage.setScene(rootScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
