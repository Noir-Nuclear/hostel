package ui.controllers;

import checks.CommonChecks;
import connection.PostgreSQLConnector;
import entities.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainController extends Controller implements Initializable {
    public ChoiceBox<String> tableSelector;
    public TableView<Entity> tableView;
    public Button deleteButton;
    public Button addButton;
    public CheckBox idMode;
    public TextField clientInvoiceField;
    public TextField hotelEmployeeSalaryField;
    public Button calculateInvoiceButton;
    public Button calculateSalaryButton;

    private Map<String, String[]> tableDictionary;
    private int currentTableIndex;
    private Statement statement;
    private Class[] entities = {
            RoomState.class, ProvidedService.class,
            Service.class, Room.class,
            Client.class, HotelEmployee.class,
            RoomStateTextual.class, ProvidedServiceTextual.class
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PostgreSQLConnector connector = new PostgreSQLConnector();
        statement = connector.getStatement();
        fillDictionaryAndSelector();
        currentTableIndex = -1;
        tableSelector.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> chooseTable((int) newValue));
        deleteButton.setOnAction(event -> deleteRow());
        addButton.setOnAction(event -> addRow());
        idMode.selectedProperty().addListener((observable, oldValue, newValue) -> chooseTable(currentTableIndex));
        calculateInvoiceButton.setOnAction(event -> calculateInvoice());
        calculateSalaryButton.setOnAction(event -> calculateSalary());
    }

    public void chooseTable(int newValue) {
        try {
            if (newValue == -1) {
                return;
            }
            tableView.getColumns().clear();
            tableView.getItems().clear();
            String itemName = tableSelector.getItems().get(newValue);
            String query = (!idMode.isSelected() && tableDictionary.get(itemName)[2] != null) ?
                    tableDictionary.get(itemName)[2] :
                    "SELECT * FROM " + tableDictionary.get(itemName)[0] + " ORDER BY " + tableDictionary.get(itemName)[1];
            ResultSet set = statement.executeQuery(query);
            fillColumns(set, newValue);
            fillRows(set, newValue);
            currentTableIndex = newValue;
        } catch (SQLException | IllegalAccessException | InstantiationException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void addRow() {
        Parent root;
        if (tableSelector.getSelectionModel().getSelectedItem().isEmpty()) {
            return;
        }
        String windowName = tableDictionary.get(tableSelector.getSelectionModel().getSelectedItem())[3];
        try {
            FXMLLoader loader = new FXMLLoader();
            root = loader.load(getClass().getResource("../../" + windowName).openStream());
            AddEntityController childrenController = loader.getController();
            BooleanProperty shouldRefresh = childrenController.shouldRefresh;
            shouldRefresh.addListener((observable, oldValue, newValue) -> chooseTable(currentTableIndex));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene rootScene = new Scene(root);
        Stage stage1 = new Stage();
        stage1.setScene(rootScene);
        stage1.show();
        chooseTable(currentTableIndex);
    }

    public void deleteRow() {
        int index = tableView.getSelectionModel().getSelectedIndex();
        String columnName = tableDictionary.get(tableSelector.getSelectionModel().getSelectedItem())[1];
        String tableName = tableDictionary.get(tableSelector.getSelectionModel().getSelectedItem())[0];
        boolean isAvailable = true;
        for (String key : tableDictionary.keySet()) {
            if (!CommonChecks.isAvailableDeleting(
                    columnName,
                    tableView.getItems().get(index).getId(),
                    tableDictionary.get(key)[0], statement)
                    ) {
                isAvailable = false;
                break;
            }
        }
        if (isAvailable) {
            try {
                statement.executeUpdate("DELETE FROM " + tableName + " WHERE " + columnName + " = " + tableView.getItems().remove(index).getId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            addErrorWindow("Нельзя удалить запись, она связана с другими записями");
        }
    }

    void calculateInvoice() {
        String clientName = clientInvoiceField.getCharacters().toString();
        clientInvoiceField.clear();
        if (clientName.isEmpty()) {
            return;
        }
        try {
            ResultSet set = statement.executeQuery("SELECT client_id FROM public.clients WHERE name = \'" + clientName + "\'");
            if (set.next()) {
                String clientId = set.getString(1);
                ArrayList<String[]> expenses = new ArrayList<>();
                expenses.add(new String[]{"Имя услуги", "Цена услуги"});
                set = statement.executeQuery("SELECT r.room_id, r.price, (rs.end - rs.begin) " +
                        "FROM room_states rs JOIN rooms r ON rs.room_id = r.room_id " +
                        "WHERE rs.client_id = \'" + clientId + "\'");
                fillReportRows(set, expenses, true);
                set = statement.executeQuery("SELECT s.name, s.price " +
                        "FROM provided_services ps JOIN services s ON ps.service_id = s.service_id " +
                        "WHERE ps.client_id = \'" + clientId + "\'");
                fillReportRows(set, expenses, false);
                Integer totalSum = 0;
                for (int i = 1; i < expenses.size(); i++) {
                    totalSum += Integer.parseInt(expenses.get(i)[1]);
                }
                if(totalSum > 0) {
                    expenses.add(new String[]{"Итого", totalSum.toString()});
                }
                openReportWindow(expenses, true, clientName);
            } else {
                addErrorWindow("Клиент с таким именем не найден");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void calculateSalary() {
        String hotelEmployeeName = hotelEmployeeSalaryField.getCharacters().toString();
        hotelEmployeeSalaryField.clear();
        if (hotelEmployeeName.isEmpty()) {
            return;
        }
        try {
            ResultSet set = statement.executeQuery("SELECT hotel_employee_id FROM public.hotel_employees WHERE name = \'" + hotelEmployeeName + "\'");
            if (set.next()) {
                String hotelEmployeeId = set.getString(1);
                ArrayList<String[]> payments = new ArrayList<>();
                payments.add(new String[]{"Имя услуги", "Цена услуги"});
                set = statement.executeQuery("SELECT s.name, s.price " +
                        "FROM provided_services ps JOIN services s ON ps.service_id = s.service_id " +
                        "WHERE ps.hotel_employee_id = \'" + hotelEmployeeId + "\'");
                fillReportRows(set, payments, false);
                Integer totalSum = 0;
                for (int i = 1; i < payments.size(); i++) {
                    totalSum += Integer.parseInt(payments.get(i)[1]);
                }
                if(totalSum > 0) {
                    payments.add(new String[]{"Итого", totalSum.toString()});
                }
                openReportWindow(payments, false, hotelEmployeeName);
            } else {
                addErrorWindow("Работник с таким именем не найден");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void openReportWindow(List<String[]> reportRows, boolean isClient, String name) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader();
            root = loader.load(getClass().getResource("../../reportWindow.fxml").openStream());
            ReportController childrenController = loader.getController();
            childrenController.fillRows(reportRows);
            childrenController.addLabel(name + ": " + (isClient ? "Счет за обслуживание" : "З/П за услуги"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene rootScene = new Scene(root);
        Stage stage1 = new Stage();
        stage1.setScene(rootScene);
        stage1.show();
    }

    void fillReportRows(ResultSet set, List<String[]> rows, boolean mode) throws SQLException {
        if (mode) {
            while (set.next()) {
                int roomNumber = set.getInt(1),
                        roomPrice = set.getInt(2),
                        roomPeriod = set.getInt(3) + 2;
                rows.add(new String[]{"Комната №" + roomNumber, "" + roomPrice * roomPeriod});
            }
        } else {
            while (set.next()) {
                String serviceName = set.getString(1);
                int servicePrice = set.getInt(2);
                rows.add(new String[]{serviceName, servicePrice + ""});
            }
        }
    }

    void fillColumns(ResultSet set, int index) throws SQLException {
        if (!idMode.isSelected() && (index + 6) < entities.length) {
            index += 6;
        }
        Field[] fields = entities[index].getDeclaredFields();
        for (int i = 0; i < set.getMetaData().getColumnCount(); i++) {
            TableColumn newColumn = new TableColumn<>(set.getMetaData().getColumnName(i + 1));
            newColumn.setCellValueFactory(new PropertyValueFactory<Object, String>(fields[i].getName()));
            tableView.getColumns().add(newColumn);
        }
    }

    void fillRows(ResultSet set, int index) throws SQLException, IllegalAccessException, InstantiationException, ParseException {
        if (!idMode.isSelected() && (index + 6) < entities.length) {
            index += 6;
        }
        Object entity = entities[index].newInstance();
        Class baseClass = entity.getClass();
        Field[] fields = entity.getClass().getDeclaredFields();
        while (set.next()) {
            entity = baseClass.newInstance();
            for (int i = 0; i < set.getMetaData().getColumnCount(); i++) {
                fields[i].setAccessible(true);
                if (fields[i].getType().equals(Integer.class)) {
                    fields[i].set(entity, Integer.parseInt(set.getString(i + 1)));
                } else if (fields[i].getType().equals(CustomDate.class)) {
                    fields[i].set(entity, new CustomDate(set.getString(i + 1)));
                } else {
                    fields[i].set(entity, set.getString(i + 1));
                }
            }
            tableView.getItems().add((Entity) entity);
        }
    }


    void fillDictionaryAndSelector() {
        ObservableList<String> menuItems = tableSelector.getItems();
        menuItems.add("Список занятых и забронированных комнат");
        menuItems.add("Список заказанных клиентами услуг");
        menuItems.add("Список услуг");
        menuItems.add("Справочник комнат");
        menuItems.add("Список клиентов");
        menuItems.add("Список сотрудников");
        tableDictionary = new HashMap<>();
        tableDictionary.put(menuItems.get(0), new String[]{"public.room_states", "room_state_id",
                "SELECT st.room_state_id AS room_state_id, st.room_id AS room_number, st.state AS state, cl.name AS client_name, st.begin AS begin, st.end AS end " +
                        "FROM public.room_states st JOIN public.clients cl ON st.client_id = cl.client_id ORDER BY st.room_state_id", "addRoomStateWindow.fxml"});
        tableDictionary.put(menuItems.get(1), new String[]{"public.provided_services", "provided_service_id",
                "SELECT ps.provided_service_id AS provided_service_id, cl.name AS client_name, s.name AS service_name, hp.name AS name, ps.date AS date " +
                        "FROM public.provided_services ps JOIN public.clients cl ON ps.client_id = cl.client_id " +
                        "JOIN public.hotel_employees hp ON ps.hotel_employee_id = hp.hotel_employee_id " +
                        "JOIN public.services s ON ps.service_id = s.service_id ORDER BY ps.provided_service_id", "addProvidedServiceWindow.fxml"});
        tableDictionary.put(menuItems.get(2), new String[]{"public.services", "service_id", null, "addServiceWindow.fxml"});
        tableDictionary.put(menuItems.get(3), new String[]{"public.rooms", "room_id", null, "addRoomWindow.fxml"});
        tableDictionary.put(menuItems.get(4), new String[]{"public.clients", "client_id", null, "addClientWindow.fxml"});
        tableDictionary.put(menuItems.get(5), new String[]{"public.hotel_employees", "hotel_employee_id", null, "addHotelEmployeeWindow.fxml"});
    }

}