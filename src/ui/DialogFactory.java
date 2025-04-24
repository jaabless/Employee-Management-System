package ui;// DialogFactory.java
import exceptions.EmployeeNotFoundException;
import exceptions.InvalidDepartmentException;
import exceptions.InvalidSalaryException;
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
import models.Employee;
import state.AppState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DialogFactory {
    private static TableView<Employee<Integer>> tableView = new TableView<>();

    public static HBox createTopControls() {
        TextField searchField = new TextField();
        searchField.setPromptText("Search Here...");

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
        if (query == null || query.trim().isEmpty()) {
            // If search field is empty, reset to full list
//            AppState.refreshTable();
            AlertUtils.showError("Please enter an ID, Name or Department");
            return;
        }

        ObservableList<Employee<Integer>> filteredList = FXCollections.observableArrayList();

        switch (type) {
            case "ID":
                try {
                    int id = Integer.parseInt(query.trim());
                    Employee<Integer> emp = AppState.database.getEmployeeById(id);
                    if (emp != null) filteredList.add(emp);
                } catch (NumberFormatException e) {
                    AlertUtils.showError("ID must be a number.");
                    return;
                }catch (EmployeeNotFoundException e) {
                    AlertUtils.showError(e.getMessage());
                    return;
                }
                break;
            case "Name":
                List<Employee<Integer>> nameResults = AppState.database.searchByName(query.trim());
                if (nameResults.isEmpty()) {
                    AlertUtils.showError("No employee found with the given name.");
                    return;
                }
                filteredList.addAll(nameResults);
                break;
            case "Department":
                try {
                    List<Employee<Integer>> deptResults = AppState.database.searchByDepartment(query.trim());
                    filteredList.addAll(deptResults);
                } catch (InvalidDepartmentException e) {
                    AlertUtils.showError(e.getMessage());
                    return;
                }
                break;
            default:
                AlertUtils.showError("Unknown search type.");
                return;
        }
        AppState.observableList.setAll(filteredList); // This makes sure the TableView gets the update
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
                // Check for empty fields
                if (idField.getText().trim().isEmpty() ||
                        nameField.getText().trim().isEmpty() ||
                        deptField.getText().trim().isEmpty() ||
                        salaryField.getText().trim().isEmpty() ||
                        ratingField.getText().trim().isEmpty() ||
                        experienceField.getText().trim().isEmpty()) {

                    AlertUtils.showError("All fields are required. Please fill in all fields.");
                    return null;
                }

                try {
                    int id = Integer.parseInt(idField.getText().trim());
                    String name = nameField.getText().trim();
                    String dept = deptField.getText().trim();
                    double salary = Double.parseDouble(salaryField.getText().trim());
                    double rating = Double.parseDouble(ratingField.getText().trim());
                    int exp = Integer.parseInt(experienceField.getText().trim());
                    boolean active = activeCheck.isSelected();

                    if (salary < 0) {
                        throw new InvalidSalaryException("Salary cannot be negative.");
                    }
                    return new Employee<>(id, name, dept, salary, rating, exp, active);
                } catch (NumberFormatException ex) {
                    AlertUtils.showError("Please enter valid numeric values for ID, Salary, Rating, and Experience.");
                } catch (InvalidSalaryException ex) {
                    AlertUtils.showError(ex.getMessage());
                } catch (Exception ex) {
                    AlertUtils.showError("Something went wrong. Please check your input.");
                } finally {
                    dialog.close();
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
                Employee<Integer> employeeData = AppState.database.getEmployeeById(id);

                Dialog<Employee<Integer>> dialog = new Dialog<>();
                dialog.setTitle("Update Employee");
                GridPane grid = UIFactory.createGridPane(20);
                TextField nameField = new TextField(employeeData.getName());
                TextField deptField = new TextField(employeeData.getDepartment());
                TextField salaryField = new TextField(String.valueOf(employeeData.getSalary()));
                TextField ratingField = new TextField(String.valueOf(employeeData.getPerformanceRating()));
                TextField expField = new TextField(String.valueOf(employeeData.getYearsOfExperience()));
                CheckBox activeBox = new CheckBox("Active");
                activeBox.setSelected(employeeData.isActive());

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
                            employeeData.setName(nameField.getText());
                            employeeData.setDepartment(deptField.getText());
                            employeeData.setSalary(Double.parseDouble(salaryField.getText()));
                            employeeData.setPerformanceRating(Double.parseDouble(ratingField.getText()));
                            employeeData.setYearsOfExperience(Integer.parseInt(expField.getText()));
                            employeeData.setActive(activeBox.isSelected());

                            if (Double.parseDouble(salaryField.getText()) < 0) {
                                throw new InvalidSalaryException("Salary cannot be negative.");
                            }
                            return employeeData;
                        } catch (NumberFormatException ex) {
                            AlertUtils.showError("Please enter valid numeric values.");
                        } catch (InvalidSalaryException ex) {
                            AlertUtils.showError(ex.getMessage());
                        } finally {
                            dialog.close();
                        }
                    }
                    return null;
                });

                dialog.showAndWait().ifPresent(updated -> {
                    AppState.database.updateEmployeeDetails(updated);
                    AppState.refreshTable();
                    tableView.refresh();
                });

            } catch (NumberFormatException ex) {
                AlertUtils.showError("Invalid ID");
            } catch (EmployeeNotFoundException ex) {
                AlertUtils.showError(ex.getMessage());
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
                Employee<Integer> emp = AppState.database.getEmployeeById(id); // May throw EmployeeNotFoundException
                boolean confirmed = AlertUtils.showConfirmation(
                        "Confirm Deletion",
                        "Are you sure you want to delete this employee?\n"+emp.toString()
                );
                if (confirmed) {
                    AppState.database.removeEmployee(id);
                    AppState.refreshTable();
                }
            } catch (NumberFormatException ex) {
                AlertUtils.showError("Invalid ID.");
            } catch (EmployeeNotFoundException ex) {
                AlertUtils.showError(ex.getMessage());
            } finally {
                idDialog.getEditor().clear();  // Clear input for re-entry
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

                // Update the observable list with the filtered results
                AppState.observableList.setAll(filtered); // Update list
                AppState.tableView.refresh(); // Refresh the table

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
        // Reset the observable list with all employees from the database
        AppState.observableList.setAll(AppState.database.getAllEmployees());

        // Refresh the table to reflect the updates
        AppState.tableView.refresh();  // Force visual update
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
            String rate = ratingField.getText().trim();
            String incrementNum = incrementField.getText().trim();

            if (rate.isEmpty() || incrementNum.isEmpty()) {
                AlertUtils.showError("All fields are required.");
                return;
            }

            try {
                double threshold = Double.parseDouble(rate);
                double increment = Double.parseDouble(incrementNum);

                // Apply the raise
                AppState.database.giveRaise(threshold, increment);

                // Update the observable list with the latest data
                AppState.observableList.setAll(AppState.database.getAllEmployees());

                // Refresh the table
                AppState.tableView.refresh();
            } catch (NumberFormatException ex) {
                AlertUtils.showError("Please enter valid numeric values.");
            }finally {
                dialog.close();  // ensure dialog is closed even if an error occurred
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
        AppState.observableList.setAll(topEmployees);
        AppState.tableView.refresh();
    }

    public static void showSortByPerfomance() {
        List<Employee<Integer>> sorted = AppState.database.sortByPerformance();
        AppState.observableList.setAll(sorted);
        AppState.tableView.refresh();
    }

    public static void refreshTable() {
        tableView.setItems(FXCollections.observableArrayList(AppState.database.getAllEmployees()));
    }

}