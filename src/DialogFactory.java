// DialogFactory.java
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
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

public class DialogFactory {
    private static TableView<Employee<Integer>> tableView = new TableView<>();

    public static HBox createTopControls() {
        TextField searchField = new TextField();
        searchField.setPromptText("Search...");

        ComboBox<String> searchTypeCombo = new ComboBox<>();
        searchTypeCombo.getItems().addAll("ID", "Name", "Department");
        searchTypeCombo.setValue("ID");

        Button searchBtn = UIFactory.createStyledButton("Search");
        searchBtn.setOnAction(e -> handleSearch(searchField.getText(), searchTypeCombo.getValue()));

        HBox leftBox = new HBox(10, searchField, searchTypeCombo, searchBtn);
        leftBox.setAlignment(Pos.CENTER_LEFT);

        ComboBox<String> actionCombo = new ComboBox<>();
        actionCombo.getItems().addAll("Add", "Update", "Delete");
        actionCombo.setPromptText("Select Action");

        Button actionBtn = UIFactory.createStyledButton("Execute");
        actionBtn.setOnAction(e -> handleAction(actionCombo.getValue()));

        HBox rightBox = new HBox(10, actionCombo, actionBtn);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        return new HBox(20, leftBox, spacer, rightBox);
    }

    public static void handleSearch(String query, String type) {
        ObservableList<Employee<Integer>> filteredList = FXCollections.observableArrayList();
        switch (type) {
            case "ID":
                try {
                    int id = Integer.parseInt(query);
                    Employee<Integer> emp = AppState.database.getEmployeeById(id);
                    if (emp != null) filteredList.add(emp);
                } catch (NumberFormatException ignored) {}
                break;
            case "Name":
                filteredList.addAll(AppState.database.searchByName(query));
                break;
            case "Department":
                filteredList.addAll(AppState.database.searchByDepartment(query));
                break;
        }
        tableView.setItems(filteredList);
    }

    public static void handleAction(String actionType) {
        switch (actionType) {
            case "Add": showAddDialog(); break;
            case "Update": showUpdateDialog(); break;
            case "Delete": showDeleteDialog(); break;
        }
    }

    public static void showAddDialog() {
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
                    AlertUtils.showError("Invalid input. Please fill all fields correctly.");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(emp -> {
            AppState.database.addEmployee(emp);
            AppState.refreshTable();
        });
    }

    public static void showUpdateDialog() {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Update Employee");
        idDialog.setHeaderText("Enter Employee ID to update:");

        idDialog.showAndWait().ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr);
                Employee<Integer> emp = AppState.database.getEmployeeById(id);
                if (emp == null) {
                    AlertUtils.showError("Employee not found.");
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
                            AlertUtils.showError("Invalid input.");
                        }
                    }
                    return null;
                });

                dialog.showAndWait().ifPresent(updated -> {
                    AppState.database.updateEmployeeDetails(updated);
                    AppState.refreshTable();
                });

            } catch (NumberFormatException ex) {
                AlertUtils.showError("Invalid ID.");
            }
        });
    }

    public static void showDeleteDialog() {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Delete Employee");
        idDialog.setHeaderText("Enter Employee ID to delete:");

        idDialog.showAndWait().ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr);
                Employee<Integer> emp = AppState.database.getEmployeeById(id);
                if (emp == null) {
                    AlertUtils.showError("Employee not found.");
                    return;
                }

                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirm Deletion");
                confirm.setHeaderText("Are you sure you want to delete this employee?");
                confirm.setContentText(emp.toString());

                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        AppState.database.removeEmployee(id);
                        AppState.refreshTable();
                    }
                });

            } catch (NumberFormatException ex) {
                AlertUtils.showError("Invalid ID input.");
            }
        });
    }

    public static void showAdvancedFilterDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Advanced Filter");

        GridPane grid = UIFactory.createGridPane(20);

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

        HBox salaryBox = UIFactory.createHBox(minSalaryField, maxSalaryField);
        grid.add(salaryRangeLabel, 0, 1);
        grid.add(salaryBox, 1, 1);

        Button applyBtn = UIFactory.createStyledButton("Apply");
        grid.add(applyBtn, 1, 2);

        applyBtn.setOnAction(e -> {
            List<Employee<Integer>> filtered = AppState.database.getAllEmployees();

            try {
                // Filter by performance rating
                if (!ratingField.getText().trim().isEmpty()) {
                    double rating = Double.parseDouble(ratingField.getText().trim());
                    filtered = AppState.database.searchByPerformance(rating);
                }

                // Filter by salary range
                if (!minSalaryField.getText().trim().isEmpty() && !maxSalaryField.getText().trim().isEmpty()) {
                    double min = Double.parseDouble(minSalaryField.getText().trim());
                    double max = Double.parseDouble(maxSalaryField.getText().trim());
                    filtered = AppState.database.searchBySalaryRange(min, max);
                }

                // Update the table
                tableView.setItems(FXCollections.observableArrayList(filtered));
                tableView.refresh();
                dialog.close();

            } catch (NumberFormatException ex) {
                AlertUtils.showError("Please enter valid numeric values.");
            }
        });

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    public static void showResetDialog() {
        tableView.setItems(FXCollections.observableArrayList(AppState.database.getAllEmployees()));
        tableView.refresh(); // Force visual update
    }

    public static void showAverageDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Calculate Average");

        GridPane grid = UIFactory.createGridPane(20); // use your UIFactory
        Label departmentLabel = new Label("Enter Department");
        TextField departmentField = new TextField();

        grid.add(departmentLabel, 0, 0);
        grid.add(departmentField, 1, 0);

        Button averageBtn = UIFactory.createStyledButton("Calculate Average");
        grid.add(averageBtn, 1, 1);

        averageBtn.setOnAction(e -> {
            String department = departmentField.getText().trim();

            if (!department.isEmpty()) {
                try {
                    double averageSalary = AppState.database.averageSalaryByDepartment(department);
                    String message = "Average Salary in " + department + ": " + averageSalary;
                    AlertUtils.showInfo(message);
                } catch (NumberFormatException ex) {
                    AlertUtils.showError("Invalid input. Please enter valid department.");
                }
            } else {
                AlertUtils.showError("Please enter a department name.");
            }

            // Update the table
            List<Employee<Integer>> filtered = new ArrayList<>(AppState.database.getAllEmployees());
            tableView.setItems(FXCollections.observableArrayList(filtered));
            tableView.refresh();

            dialog.close(); // Close after action
        });

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
        dialog.showAndWait();
    }


    public static void showGiveRaiseDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Give Raise");

        GridPane grid = UIFactory.createGridPane(20);

        TextField ratingField = new TextField();
        TextField incrementField = new TextField();

        grid.add(new Label("Minimum Rating Threshold:"), 0, 0);
        grid.add(ratingField, 1, 0);

        grid.add(new Label("Increment Amount:"), 0, 1);
        grid.add(incrementField, 1, 1);

        Button applyBtn = UIFactory.createStyledButton("Apply");

        applyBtn.setOnAction(e -> {
            String rate = ratingField.getText();
            String incrementNum = incrementField.getText();

            if (rate.isEmpty() || incrementNum.isEmpty()) {
                AlertUtils.showError("All fields are required.");
                return;
            }

            try {
                double threshold = Double.parseDouble(rate);
                double increment = Double.parseDouble(incrementNum);

                AppState.database.giveRaise(threshold, increment);

                // Refresh the table
                tableView.setItems(FXCollections.observableArrayList(AppState.database.getAllEmployees()));
                refreshTable();
                tableView.refresh();

                dialog.close(); // manually close the dialog

            } catch (NumberFormatException ex) {
                AlertUtils.showError("Please enter valid numeric values.");
            }
        });

        // Add button to grid or layout
        grid.add(applyBtn, 1, 2);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    public static void showTopPaidDialog() {
        List<Employee<Integer>> topEmployees = AppState.database.getTopPaid();
        tableView.getItems().setAll(topEmployees);
        tableView.refresh(); // ensure visual update
    }

    public static void showSortByPerfomance() {
        List<Employee<Integer>> sorted = AppState.database.sortByPerformance();
        tableView.getItems().setAll(sorted);
        tableView.refresh(); // ensure visual update
    }




    public static void refreshTable() {
        tableView.setItems(FXCollections.observableArrayList(AppState.database.getAllEmployees()));
    }

}