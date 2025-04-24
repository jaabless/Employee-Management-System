package state;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import models.Employee;
import services.EmployeeDatabase;

public class AppState {
    public static final EmployeeDatabase<Integer> database = new EmployeeDatabase<>();
    public static TableView<Employee<Integer>> tableView;

    public static  ObservableList<Employee<Integer>> observableList =
            FXCollections.observableArrayList(database.getAllEmployees());

    public static void refreshTable() {
        observableList.setAll(database.getAllEmployees()); // only modifies contents
        if (tableView != null) {
            tableView.refresh();
        }
    }
}
