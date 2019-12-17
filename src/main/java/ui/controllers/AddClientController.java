package ui.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddClientController extends AddEntityController {
    public TextField nameField;
    public Button addButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addButton.setOnAction(event -> addClient());
    }

    public void addClient() {
        String clientName = nameField.getCharacters().toString(), id = generateId("public.clients");
        if(clientName.isEmpty()) {
            addErrorWindow("Не заполнено поле name");
            return;
        }
        try {
            statement.executeUpdate("INSERT INTO public.clients " +
                    "VALUES (\'" + id + "\', \'" + clientName + "\')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.close();
        shouldRefresh.setValue(true);
    }
}
