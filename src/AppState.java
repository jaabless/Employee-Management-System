import javafx.collections.FXCollections;
import javafx.scene.control.TableView;

public class AppState {
    public static final EmployeeDatabase<Integer> database = new EmployeeDatabase<>();
    public static TableView<Employee<Integer>> tableView;

    public static void refreshTable() {
        if (tableView != null) {
            tableView.setItems(FXCollections.observableArrayList(database.getAllEmployees()));
        }
    }
}
