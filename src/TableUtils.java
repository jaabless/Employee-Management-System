// TableUtils.java
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.*;
import javafx.geometry.*;

public class TableUtils {
    public static VBox createTableSection() {
        TableView<Employee<Integer>> tableView = new TableView<>();
        AppState.tableView = tableView;

        tableView.setItems(FXCollections.observableArrayList(AppState.database.getAllEmployees()));
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Employee<Integer>, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getEmployeeId()));

        TableColumn<Employee<Integer>, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getName()));

        TableColumn<Employee<Integer>, String> deptCol = new TableColumn<>("Department");
        deptCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getDepartment()));

        TableColumn<Employee<Integer>, Double> salaryCol = new TableColumn<>("Salary");
        salaryCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getSalary()));

        TableColumn<Employee<Integer>, Double> ratingCol = new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getPerformanceRating()));

        TableColumn<Employee<Integer>, Integer> expCol = new TableColumn<>("Experience");
        expCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getYearsOfExperience()));

        TableColumn<Employee<Integer>, Boolean> activeCol = new TableColumn<>("Active");
        activeCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().isActive()));

        tableView.getColumns().addAll(idCol, nameCol, deptCol, salaryCol, ratingCol, expCol, activeCol);

        Button advancedFilterBtn = UIFactory.createStyledButton("Advanced Filter");
        advancedFilterBtn.setOnAction(e ->DialogFactory.showAdvancedFilterDialog() );

        Button resetBtn = UIFactory.createStyledButton("Reset Table");
        resetBtn.setOnAction(e -> DialogFactory.showResetDialog());

        Button averageBtn = UIFactory.createStyledButton("Calculate Average");
        averageBtn.setOnAction(e -> DialogFactory.showAverageDialog());

        Button raiseBtn = UIFactory.createStyledButton("Raise");
        raiseBtn.setOnAction(e -> DialogFactory.showGiveRaiseDialog());

        Button getTop5 = UIFactory.createStyledButton("Highest Earners");
        getTop5.setOnAction(e -> DialogFactory.showTopPaidDialog());

        Button sortPerformanceBtn = UIFactory.createStyledButton("Sort by Performance");
        sortPerformanceBtn.setOnAction(e -> DialogFactory.showSortByPerfomance());

//
        HBox btnBox = UIFactory.createHBox(advancedFilterBtn, resetBtn,averageBtn,getTop5,raiseBtn,sortPerformanceBtn);
        VBox tableBox = new VBox(10, tableView,btnBox);
        tableBox.setPadding(new Insets(20, 0, 0, 0));
        return tableBox;
    }
}

