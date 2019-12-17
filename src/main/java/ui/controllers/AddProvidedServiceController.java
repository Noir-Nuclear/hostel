package ui.controllers;

import checks.AddProvidedServiceChecks;
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

public class AddProvidedServiceController extends AddEntityController {
    public TextField clientIdField;
    public TextField serviceIdField;
    public TextField hotelEmployeeIdField;
    public DatePicker dateField;
    public Button addButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addButton.setOnAction(event -> addProvidedService());
    }

    public void addProvidedService() {
        String id = generateId("public.provided_services"),
                clientId = clientIdField.getText(),
                serviceId = serviceIdField.getText(),
                hotelEmployeeId = hotelEmployeeIdField.getText();

        Date begin = Date.valueOf(dateField.getValue());
        if (!CommonChecks.isInteger(clientId)) {
            addErrorWindow("Значение client_id не является числовым");
            return;
        }
        if (!CommonChecks.isExistedId(clientId, "public.clients", statement)) {
            addErrorWindow("Записи с таким client_id не существует в родительской таблице");
            return;
        }
        if (!CommonChecks.isInteger(serviceId)) {
            addErrorWindow("Значение service_id не является числовым");
            return;
        }
        if (!CommonChecks.isExistedId(serviceId, "public.services", statement)) {
            addErrorWindow("Записи с таким service_id не существует в родительской таблице");
            return;
        }
        if (!CommonChecks.isInteger(hotelEmployeeId)) {
            addErrorWindow("Значение hotel_employee_id не является числовым");
            return;
        }
        if (!CommonChecks.isExistedId(hotelEmployeeId, "public.hotel_employees", statement)) {
            addErrorWindow("Записи с таким hotel_employee_id не существует в родительской таблице");
            return;
        }
        if (!AddProvidedServiceChecks.isDateCorrect(begin)) {
            addErrorWindow("date должно быть заполнено датой, не менее, чем текущей");
            return;
        }
        if (AddProvidedServiceChecks.isHotelEmployeeBusy(hotelEmployeeId, begin, statement)) {
            addErrorWindow("Сотрудник с выбранным hotel_employee_id уже занят в указанную дату");
            return;
        }
        try {
            statement.executeUpdate("INSERT INTO public.provided_services " +
                    "VALUES (\'" + id + "\', \'" +
                    clientId + "\', \'" +
                    serviceId + "\', \'" +
                    hotelEmployeeId + "\', \'" +
                    begin + "\')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.close();
        shouldRefresh.setValue(true);
    }
}
