package ui.controllers;

import checks.AddRoomStateChecks;
import checks.CommonChecks;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddRoomStateController extends AddEntityController  {
    public TextField clientIdField;
    public TextField roomIdField;
    public TextField stateField;
    public DatePicker beginDateField;
    public DatePicker endDateField;
    public Button addButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addButton.setOnAction(event -> addRoomState());
    }

    public void addRoomState() {
        String id = generateId("public.room_states"),
                clientId = clientIdField.getText(),
                roomId = roomIdField.getText(),
                state = stateField.getText();

        Date begin = Date.valueOf(beginDateField.getValue()),
                end = Date.valueOf(endDateField.getValue());
        if (!CommonChecks.isInteger(roomId)) {
            addErrorWindow("Значение room_id не является числовым");
            return;
        }
        if (!CommonChecks.isInteger(clientId)) {
            addErrorWindow("Значение client_id не является числовым");
            return;
        }
        if (!CommonChecks.isExistedId(clientId, "public.clients", statement)) {
            addErrorWindow("Записи с таким client_id не существует в родительской таблице");
            return;
        }
        if (!AddRoomStateChecks.isStateValid(state)) {
            addErrorWindow("Поле state может содержать либо 0(забронирован), либо 1(выкуплен)");
            return;
        }
        if (!AddRoomStateChecks.isDateIntervalCorrect(begin, end)) {
            addErrorWindow("Некорректно проставлены даты. Даты должны быть выставлены не ранее текущего дня," +
                    "а также дата начала должна быть раньше даты конца");
            return;
        }
        if (!AddRoomStateChecks.isDateIntervalAvailable(begin, end, roomId, statement)) {
            addErrorWindow("Требуемая комната уже занята в указанные даты");
            return;
        }
        try {
            statement.executeUpdate("INSERT INTO public.room_states " +
                    "VALUES (\'" + id + "\', \'" +
                    roomId + "\', \'" +
                    state + "\', \'" +
                    clientId + "\', \'" +
                    begin + "\', \'" +
                    end + "\')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.close();
        shouldRefresh.setValue(true);
    }
}
