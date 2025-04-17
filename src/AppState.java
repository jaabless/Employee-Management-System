import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class AppState {
    public static final EmployeeDatabase<Integer> database = new EmployeeDatabase<>();
    public static TableView<Employee<Integer>> tableView;

    // Final because we never reassign it — only update its contents
    public static  ObservableList<Employee<Integer>> observableList =
            FXCollections.observableArrayList(database.getAllEmployees());

    public static void refreshTable() {
        observableList.setAll(database.getAllEmployees()); // only modifies contents
        if (tableView != null) {
            tableView.refresh();
        }
    }
}
