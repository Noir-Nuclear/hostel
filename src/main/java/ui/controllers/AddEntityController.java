package ui.controllers;

import connection.PostgreSQLConnector;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

abstract public class AddEntityController extends Controller implements Initializable {
    protected Statement statement = (new PostgreSQLConnector()).getStatement();
    public BooleanProperty shouldRefresh = new SimpleBooleanProperty(false);

    public String generateId(String tableName) {
        String columnName = tableName.substring(7, tableName.length() - 1) + "_id";
        String query = "SELECT t1." + columnName + " + 1 " +
                "FROM " + tableName + " t1 " +
                "WHERE (" +
                "SELECT 1 FROM " + tableName + " t2 WHERE t2." + columnName + " = (t1." + columnName + " + 1) )" +
                "IS NULL " +
                "ORDER BY t1." + columnName + " LIMIT 1";
        try {
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next())
                return Integer.toString(resultSet.getInt(1));
            else return "1";
        } catch (SQLException e) {
            e.printStackTrace();
            return "-1";
        }
    }

}
