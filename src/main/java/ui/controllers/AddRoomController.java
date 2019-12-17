package ui.controllers;

import checks.CommonChecks;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddRoomController extends AddEntityController  {
    public TextField roomIdField;
    public TextField sumField;
    public Button addButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addButton.setOnAction(event -> addRoom());
    }

    public void addRoom() {
        String id = roomIdField.getText(), sum = sumField.getText();
        if(!CommonChecks.isInteger(roomIdField.getText())) {
            addErrorWindow("Не обнаружено числовое значение в поле room_id");
            return;
        }
        if(!CommonChecks.isInteger(sumField.getText())) {
            addErrorWindow("Не обнаружено числовое значение в поле sum");
            return;
        }
        try {
            statement.executeUpdate("INSERT INTO public.rooms " +
                    "VALUES (\'" + id + "\', \'" + sum + "\')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.close();
        shouldRefresh.setValue(true);
    }
}
