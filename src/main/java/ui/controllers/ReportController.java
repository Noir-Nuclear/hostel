package ui.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ReportController implements Initializable {
    public Label reportTitle;
    public GridPane grid;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void fillRows(List<String[]> rows) {
        if(rows.size() == 1) {
            grid.addRow(0, new Text("Услуг по данному клиенту не найдено"));
            return;
        }
        for (int i = 0; i < rows.size(); i++) {
            grid.addRow(i, new Text(rows.get(i)[0]), new Text(rows.get(i)[1]));
        }
    }

    public void addLabel(String label) {
        reportTitle.setText(label);
    }
}
