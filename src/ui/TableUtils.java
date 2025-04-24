package ui;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Employee;
import state.AppState;

public class TableUtils {

    public static VBox createTableSection() {
        TableView<Employee<Integer>> tableView = new TableView<>();
        AppState.tableView = tableView;

        // Shared ObservableList for consistent, updatable data
        ObservableList<Employee<Integer>> employeeList = FXCollections.observableArrayList(AppState.database.getAllEmployees());
        AppState.observableList = employeeList;
        tableView.setItems(employeeList);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // ID Column
        TableColumn<Employee<Integer>, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getEmployeeId()));
        idCol.setSortable(true);

        // Name Column
        TableColumn<Employee<Integer>, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getName()));
        nameCol.setSortable(true);

        // Department Column
        TableColumn<Employee<Integer>, String> deptCol = new TableColumn<>("Department");
        deptCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getDepartment()));
        deptCol.setSortable(true);

        // Salary Column
        TableColumn<Employee<Integer>, Double> salaryCol = new TableColumn<>("Salary");
        salaryCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getSalary()));
        salaryCol.setSortable(true);

        // Rating Column
        TableColumn<Employee<Integer>, Double> ratingCol = new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getPerformanceRating()));
        ratingCol.setSortable(true);

        // Experience Column
        TableColumn<Employee<Integer>, Integer> expCol = new TableColumn<>("Experience");
        expCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getYearsOfExperience()));
        expCol.setSortable(true);

        // Active Status Column
        TableColumn<Employee<Integer>, Boolean> activeCol = new TableColumn<>("Active");
        activeCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().isActive()));
        activeCol.setSortable(true);

        // Add columns to table
        tableView.getColumns().addAll(idCol, nameCol, deptCol, salaryCol, ratingCol, expCol, activeCol);

        // Buttons
        Button advancedFilterBtn = UIFactory.createStyledButton("Advanced Filter");
        advancedFilterBtn.setOnAction(e -> DialogFactory.showAdvancedFilterDialog());

        Button resetBtn = UIFactory.createStyledButton("Reset Table");
        resetBtn.setOnAction(e -> DialogFactory.showResetDialog());

        Button averageBtn = UIFactory.createStyledButton("Calculate Average");
        averageBtn.setOnAction(e -> DialogFactory.showAverageDialog());

        Button raiseBtn = UIFactory.createStyledButton("Raise");
        raiseBtn.setOnAction(e -> DialogFactory.showGiveRaiseDialog());

        Button getTop5 = UIFactory.createStyledButton("Highest Earners");
        getTop5.setOnAction(e -> DialogFactory.showTopPaidDialog());

        Button sortPerformanceBtn = UIFactory.createStyledButton("Sort by Performance");
        sortPerformanceBtn.setOnAction(e -> DialogFactory.showSortByPerfomance()); // fixed typo

        // Layout
        HBox btnBox = UIFactory.createHBox(advancedFilterBtn, resetBtn, averageBtn, getTop5, raiseBtn, sortPerformanceBtn);
        VBox tableBox = new VBox(10, tableView, btnBox);
        tableBox.setPadding(new Insets(20, 0, 0, 0));

        return tableBox;
    }
}
