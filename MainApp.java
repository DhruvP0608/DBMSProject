package MiniProject;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("University Timetable System");

        // Root layout container
        BorderPane root = new BorderPane();

        // Add the timetable grid to the center
        root.setCenter(TimetableGrid.createTimetable());

        // Add action buttons to the bottom
        root.setBottom(ActionButtons.createButtons());

        // Add teacher assignments to the right
        root.setRight(TeacherAssignments.createTeacherTable());

        // Create a scene and attach CSS for styling
        Scene scene = new Scene(root, 1000, 600);
        scene.getStylesheets().add("styles.css");

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}