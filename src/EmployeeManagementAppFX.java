// MainApp.java
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class EmployeeManagementAppFX extends Application {
    private EmployeeDatabase database = new EmployeeDatabase();
    private TableView<Employee<Integer>> table = new TableView<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        HBox topControls = DialogFactory.createTopControls();
        VBox tableSection = TableUtils.createTableSection();

        root.setTop(topControls);
        root.setCenter(tableSection);

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setTitle("Employee Management System");
        primaryStage.setScene(scene);
        primaryStage.show();


//        Button advancedFilterBtn = UIFactory.createStyledButton("Advanced Filter");
//        advancedFilterBtn.setOnAction(e ->DialogFactory.showAdvancedFilterDialog() );
//
//        Button resetBtn = UIFactory.createStyledButton("Reset Table");
//        resetBtn.setOnAction(e -> DialogFactory.showResetDialog());
//
//        Button averageBtn = UIFactory.createStyledButton("Calculate Average");
//        averageBtn.setOnAction(e -> DialogFactory.showAverageDialog());
//
//        Button raiseBtn = UIFactory.createStyledButton("Raise");
//        raiseBtn.setOnAction(e -> DialogFactory.showGiveRaiseDialog());
//
//        Button getTop5 = UIFactory.createStyledButton("Highest Earners");
//        getTop5.setOnAction(e -> DialogFactory.showTopPaidDialog());
//
//        Button sortPerformanceBtn = UIFactory.createStyledButton("Sort by Performance");
//        sortPerformanceBtn.setOnAction(e -> DialogFactory.showSortByPerfomance());
//
////
//        HBox btnBox = UIFactory.createHBox(advancedFilterBtn, resetBtn,averageBtn,getTop5,raiseBtn,sortPerformanceBtn);

//        VBox root1 = UIFactory.createVBox(table, btnBox);
//        root1.setPadding(new Insets(20, 0, 0, 0));
//
//        primaryStage.setScene(new Scene(root1, 800, 600));
//        primaryStage.show();
    }


}