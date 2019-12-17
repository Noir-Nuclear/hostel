package ui.controllers;

import checks.CommonChecks;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddServiceController extends AddEntityController  {
    public TextField nameField;
    public TextField priceField;
    public Button addButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addButton.setOnAction(event -> addService());
    }

    public void addService() {
        String name = nameField.getText(), price = priceField.getText(), id = generateId("public.services");
        if(name.isEmpty()) {
            addErrorWindow("Не заполнено поле name");
            return;
        }
        if(!CommonChecks.isInteger(price)) {
            addErrorWindow("Не обнаружено числовое значение в поле price");
            return;
        }
        try {
            statement.executeUpdate("INSERT INTO public.services " +
                    "VALUES (\'" + id + "\', \'" + price + "\', \'" + name + "\')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.close();
        shouldRefresh.setValue(true);
    }
}
