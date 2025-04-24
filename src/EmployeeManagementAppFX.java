// MainApp.java
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import ui.DialogFactory;
import ui.TableUtils;

public class EmployeeManagementAppFX extends Application {
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

    }


}