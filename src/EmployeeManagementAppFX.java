import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.*;
import javafx.geometry.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeManagementAppFX extends Application {

    private EmployeeDatabase<Integer> database = new EmployeeDatabase<>();
    private TableView<Employee<Integer>> tableView = new TableView<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        HBox topControls = createTopControls();
        VBox tableSection = createTableSection();

        root.setTop(topControls);
        root.setCenter(tableSection);

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setTitle("Employee Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createTopControls() {
        TextField searchField = new TextField();
        searchField.setPromptText("Search...");

        ComboBox<String> searchTypeCombo = new ComboBox<>();
        searchTypeCombo.getItems().addAll("ID", "Name", "Department");
        searchTypeCombo.setValue("ID");

        Button searchBtn = createStyledButton("Search");
        searchBtn.setOnAction(e -> handleSearch(searchField.getText(), searchTypeCombo.getValue()));

        HBox leftBox = new HBox(10, searchField, searchTypeCombo, searchBtn);
        leftBox.setAlignment(Pos.CENTER_LEFT);

        ComboBox<String> actionCombo = new ComboBox<>();
        actionCombo.getItems().addAll("Add", "Update", "Delete");
        actionCombo.setPromptText("Select Action");

        Button actionBtn = createStyledButton("Execute");
        actionBtn.setOnAction(e -> handleAction(actionCombo.getValue()));

        HBox rightBox = new HBox(10, actionCombo, actionBtn);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        return new HBox(20, leftBox, spacer, rightBox);
    }

    private VBox createTableSection() {
        tableView.setItems(FXCollections.observableArrayList(database.getAllEmployees()));
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

        Button advancedFilterBtn = createStyledButton("Advanced Filter");
        advancedFilterBtn.setOnAction(e -> showAdvancedFilterDialog());

        Button resetBtn = createStyledButton("Reset Table");
        resetBtn.setOnAction(e -> showResetDialog());

        Button averageBtn = createStyledButton("Calculate Average");
        averageBtn.setOnAction(e -> showAverageDialog());

        Button raiseBtn = createStyledButton("Raise");
        raiseBtn.setOnAction(e -> showGiveRaiseDialog());

        Button getTop5 = createStyledButton("Highest Earners");
        getTop5.setOnAction(e -> showTopPaidDialog());

        Button sortPerformanceBtn = createStyledButton("Sort by Performance");
        sortPerformanceBtn.setOnAction(e -> showSortByPerfomance());

        Button sortSalaryBtn = createStyledButton("Sort by Salary");
        sortSalaryBtn.setOnAction(e -> showSortSalary());

        HBox buttonBox = new HBox(10, advancedFilterBtn, resetBtn,averageBtn,getTop5,raiseBtn,sortPerformanceBtn,sortSalaryBtn);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        tableView.getColumns().addAll(idCol, nameCol, deptCol, salaryCol, ratingCol, expCol, activeCol);

        VBox tableBox = new VBox(10, tableView,buttonBox);
        tableBox.setPadding(new Insets(20, 0, 0, 0));
        return tableBox;
    }

    private Button createStyledButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: #008080; -fx-text-fill: white;");
        return btn;
    }

    private void handleSearch(String query, String type) {
        ObservableList<Employee<Integer>> filteredList = FXCollections.observableArrayList();
        switch (type) {
            case "ID":
                try {
                    int id = Integer.parseInt(query);
                    Employee<Integer> emp = database.getEmployeeById(id);
                    if (emp != null) filteredList.add(emp);
                } catch (NumberFormatException ignored) {}
                break;
            case "Name":
                filteredList.addAll(database.searchByName(query));
                break;
            case "Department":
                filteredList.addAll(database.searchByDepartment(query));
                break;
        }
        tableView.setItems(filteredList);
    }

    private void handleAction(String actionType) {
        switch (actionType) {
            case "Add": showAddDialog(); break;
            case "Update": showUpdateDialog(); break;
            case "Delete": showDeleteDialog(); break;
        }
    }

    private void showAddDialog() {
        Dialog<Employee<Integer>> dialog = new Dialog<>();
        dialog.setTitle("Add Employee");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField deptField = new TextField();
        TextField salaryField = new TextField();
        TextField ratingField = new TextField();
        TextField experienceField = new TextField();
        CheckBox activeCheck = new CheckBox("Active");

        grid.add(new Label("ID:"), 0, 0); grid.add(idField, 1, 0);
        grid.add(new Label("Name:"), 0, 1); grid.add(nameField, 1, 1);
        grid.add(new Label("Department:"), 0, 2); grid.add(deptField, 1, 2);
        grid.add(new Label("Salary:"), 0, 3); grid.add(salaryField, 1, 3);
        grid.add(new Label("Rating:"), 0, 4); grid.add(ratingField, 1, 4);
        grid.add(new Label("Experience:"), 0, 5); grid.add(experienceField, 1, 5);
        grid.add(activeCheck, 1, 6);

        ButtonType addBtnType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addBtnType, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {
            if (button == addBtnType) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    String name = nameField.getText();
                    String dept = deptField.getText();
                    double salary = Double.parseDouble(salaryField.getText());
                    double rating = Double.parseDouble(ratingField.getText());
                    int exp = Integer.parseInt(experienceField.getText());
                    boolean active = activeCheck.isSelected();

                    return new Employee<>(id, name, dept, salary, rating, exp, active);
                } catch (Exception ex) {
                    showAlert("Invalid input. Please fill all fields correctly.");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(emp -> {
            database.addEmployee(emp);
            refreshTable();
        });
    }

    private void showUpdateDialog() {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Update Employee");
        idDialog.setHeaderText("Enter Employee ID to update:");

        idDialog.showAndWait().ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr);
                Employee<Integer> emp = database.getEmployeeById(id);
                if (emp == null) {
                    showAlert("Employee not found.");
                    return;
                }

                Dialog<Employee<Integer>> dialog = new Dialog<>();
                dialog.setTitle("Update Employee");

                GridPane grid = new GridPane();
                grid.setHgap(10); grid.setVgap(10);
                grid.setPadding(new Insets(20));

                TextField nameField = new TextField(emp.getName());
                TextField deptField = new TextField(emp.getDepartment());
                TextField salaryField = new TextField(String.valueOf(emp.getSalary()));
                TextField ratingField = new TextField(String.valueOf(emp.getPerformanceRating()));
                TextField expField = new TextField(String.valueOf(emp.getYearsOfExperience()));
                CheckBox activeBox = new CheckBox("Active");
                activeBox.setSelected(emp.isActive());

                grid.add(new Label("Name:"), 0, 0); grid.add(nameField, 1, 0);
                grid.add(new Label("Department:"), 0, 1); grid.add(deptField, 1, 1);
                grid.add(new Label("Salary:"), 0, 2); grid.add(salaryField, 1, 2);
                grid.add(new Label("Rating:"), 0, 3); grid.add(ratingField, 1, 3);
                grid.add(new Label("Experience:"), 0, 4); grid.add(expField, 1, 4);
                grid.add(activeBox, 1, 5);

                dialog.getDialogPane().setContent(grid);
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                dialog.setResultConverter(button -> {
                    if (button == ButtonType.OK) {
                        try {
                            emp.setName(nameField.getText());
                            emp.setDepartment(deptField.getText());
                            emp.setSalary(Double.parseDouble(salaryField.getText()));
                            emp.setPerformanceRating(Double.parseDouble(ratingField.getText()));
                            emp.setYearsOfExperience(Integer.parseInt(expField.getText()));
                            emp.setActive(activeBox.isSelected());
                            return emp;
                        } catch (Exception ex) {
                            showAlert("Invalid input.");
                        }
                    }
                    return null;
                });

                dialog.showAndWait().ifPresent(updated -> {
                    database.updateEmployeeDetails(updated);
                    refreshTable();
                    tableView.refresh();
                });

            } catch (NumberFormatException ex) {
                showAlert("Invalid ID.");
            }
        });
    }

    private void showDeleteDialog() {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Delete Employee");
        idDialog.setHeaderText("Enter Employee ID to delete:");

        idDialog.showAndWait().ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr);
                Employee<Integer> emp = database.getEmployeeById(id);
                if (emp == null) {
                    showAlert("Employee not found.");
                    return;
                }

                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirm Deletion");
                confirm.setHeaderText("Are you sure you want to delete this employee?");
                confirm.setContentText(emp.toString());

                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        database.removeEmployee(id);
                        refreshTable();
                    }
                });

            } catch (NumberFormatException ex) {
                showAlert("Invalid ID input.");
            }
        });
    }

    private void showAdvancedFilterDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Advanced Filter");

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // Performance rating filter
        Label ratingLabel = new Label("Minimum Performance Rating:");
        TextField ratingField = new TextField();
        grid.add(ratingLabel, 0, 0);
        grid.add(ratingField, 1, 0);

        // Salary range filter
        Label salaryRangeLabel = new Label("Salary Range:");
        TextField minSalaryField = new TextField();
        minSalaryField.setPromptText("Min");
        TextField maxSalaryField = new TextField();
        maxSalaryField.setPromptText("Max");

        HBox salaryBox = new HBox(5, minSalaryField, maxSalaryField);
        grid.add(salaryRangeLabel, 0, 1);
        grid.add(salaryBox, 1, 1);

        Button applyBtn = new Button("Apply");
        applyBtn.setOnAction(e -> {
            List<Employee<Integer>> filtered = new ArrayList<>(database.getAllEmployees());

            if (!ratingField.getText().isEmpty()) {
                try {
                    double rating = Double.parseDouble(ratingField.getText());
                    filtered = database.searchByPerformance(rating);
                } catch (NumberFormatException ex) {
                    showAlert("Invalid rating input.");
                }
            }

            if (!minSalaryField.getText().isEmpty() || !maxSalaryField.getText().isEmpty()) {
                try {
                    double min = Double.parseDouble(minSalaryField.getText());
                    double max = Double.parseDouble(maxSalaryField.getText());
                    filtered = database.searchBySalaryRange(min,max);
                } catch (NumberFormatException ex) {
                    showAlert("Invalid salary inputs.");
                }
            }

            tableView.setItems(FXCollections.observableArrayList(filtered));
            dialog.show();
        });

        grid.add(applyBtn, 1, 2);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    private void showResetDialog(){
        tableView.setItems(FXCollections.observableArrayList(database.getAllEmployees()));
    }

    private void showAverageDialog(){
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Calculate Average");

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // Performance rating filter
        Label departmentLabel = new Label("Enter Department");
        TextField departmentField = new TextField();
        grid.add(departmentLabel, 0, 0);
        grid.add(departmentField, 1, 0);


        Button averageBtn = new Button("Calculate Average");
        averageBtn.setOnAction(e -> {
            List<Employee<Integer>> filtered = new ArrayList<>(database.getAllEmployees());
            String department = departmentField.getText();

            if (!department.isEmpty()) {
                try {
                    double averageSalary = database.averageSalaryByDepartment(department);
                    System.out.println("Average Salary in " + department + ": " + averageSalary);
                    String message = "Average Salary in " + department + ": " + averageSalary;
                    showInfoAlert(message);
                } catch (NumberFormatException ex) {
                    showAlert("Invalid rating input.");
                }
            }

            tableView.setItems(FXCollections.observableArrayList(filtered));
            dialog.close();
        });

        grid.add(averageBtn, 2, 2);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    private void showGiveRaiseDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Give Raise");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField ratingField = new TextField();
        TextField incrementField = new TextField();

        grid.add(new Label("Minimum Rating Threshold:"), 0, 0);
        grid.add(ratingField, 1, 0);

        grid.add(new Label("Increment Amount:"), 0, 1);
        grid.add(incrementField, 1, 1);

        Button applyBtn = new Button("Apply");

        applyBtn.setOnAction(e -> {
            String incrementNum = incrementField.getText();
            String rate = ratingField.getText();

            if (!incrementNum.isEmpty() && !rate.isEmpty()) {
                try {
                    double threshold = Double.parseDouble(ratingField.getText());
                    double increment = Double.parseDouble(incrementField.getText());
                    database.giveRaise(threshold, increment);
                    System.out.println("hi");
                    refreshTable();
                    tableView.refresh();
                } catch (NumberFormatException ex) {
                    showAlert("Please enter valid numbers.");
                }
            }
//            return null;
        });

        grid.add(applyBtn, 1, 2);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    private void showTopPaidDialog() {
        List<Employee<Integer>> topEmployees = database.getTopPaid();
        tableView.getItems().setAll(topEmployees);
    }

    private void showSortByPerfomance() {
        List<Employee<Integer>> sorted = database.sortByPerformance();
        tableView.getItems().setAll(sorted);
    }

    private void showSortSalary() {
        List<Employee<Integer>> sorted = database.sortBySalary();
        tableView.getItems().setAll(sorted);
    }



    private void refreshTable() {
        tableView.setItems(FXCollections.observableArrayList(database.getAllEmployees()));
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
