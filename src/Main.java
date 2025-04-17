import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends Application {
    public void start(Stage primaryStage) {
        // UI Elements
        Label title = new Label("Bank Account Management");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Button viewAccountBtn = new Button("View Account"); //Display View Account button
        Button createAccountBtn = new Button("Create Account");//Display View Account button
        Button depositBtn = new Button("Deposit");//Display Deposit button
        Button withdrawBtn = new Button("Withdraw");//Display Withdraw button
        Button viewTransactionsBtn = new Button("View Transactions");//Display View Past Transactions button

        // Button Styles
        String buttonStyle = "-fx-background-color: #1C74E9; -fx-text-fill: white; -fx-font-size: 14px;";
        viewAccountBtn.setStyle(buttonStyle);
        createAccountBtn.setStyle(buttonStyle);
        depositBtn.setStyle(buttonStyle);
        withdrawBtn.setStyle(buttonStyle);
        viewTransactionsBtn.setStyle(buttonStyle);

        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(20);// Horizontal space between columns
        buttonGrid.setVgap(20); // Vertical space between rows
        buttonGrid.setAlignment(Pos.CENTER); // Center the grid

        // Add buttons to 2 rows and 2 columns
        buttonGrid.add(viewAccountBtn, 0, 0);     // Column 0, Row 0
        buttonGrid.add(createAccountBtn, 1, 0);   // Column 1, Row 0 (this is changed)
        buttonGrid.add(depositBtn, 0, 1);         // Column 0, Row 1
        buttonGrid.add(withdrawBtn, 1, 1);        // Column 1, Row 1
        buttonGrid.add(viewTransactionsBtn, 0, 2); // Column 1, Row 1

        // Button Actions
//        viewAccountBtn.setOnAction(e -> viewAccount());
//        createAccountBtn.setOnAction(e -> createAccount());
//        depositBtn.setOnAction(e -> initiateDeposit());
//        withdrawBtn.setOnAction(e -> withdrawPopup());
//        viewTransactionsBtn.setOnAction(e -> viewTransactions());

        VBox layout = new VBox(15);//set spaces between items to 10
        layout.setPadding(new Insets(30));//set padding to 20//set padding to 20
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getChildren().addAll(title, buttonGrid);//confirm what to display on UI in order they should appear
        layout.setStyle("-fx-background-color: #fff;");

        Scene scene = new Scene(layout, 450, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Bank Management System");
        primaryStage.show();
    }
}