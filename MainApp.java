package MiniProject;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class MainApp extends Application {

    private BorderPane root;
    private TimetableGenerator generator;
    private Connection connection;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("University Timetable System");

        // Establish connection and initialize TimetableGenerator
        connection = DatabaseConnection.getConnection();
        if (connection != null) {
            generator = new TimetableGenerator();
            generator.generateAndDisplayTimetable(connection);
        } else {
            System.out.println("Failed to connect to the database.");
            return;
        }

        root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #f3f4f6, #d3d3d3);");

        // Add header buttons, including "Regenerate Timetable"
        root.setTop(createHeaderButtons(primaryStage));

        // Default view: Display timetable for the first class
        updateContent("CSE-A");

        Scene scene = new Scene(root, 1150, 650);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createHeaderButtons(Stage primaryStage) {
        HBox buttonBox = new HBox(15);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setStyle("-fx-background-color: #ffecb3; -fx-border-color: #ffa726; -fx-border-width: 2; -fx-border-radius: 10;");

        Button cseButton = new Button("CSE-A");
        Button itButton = new Button("IT-A");
        Button regenerateButton = new Button("Regenerate Timetable");
        Button exitButton = new Button("Exit");

        // Button styles
        cseButton.setStyle("-fx-background-color: #66bb6a; -fx-text-fill: white; -fx-font-weight: bold;");
        cseButton.setOnMouseEntered(e -> cseButton.setStyle("-fx-background-color: #43a047; -fx-text-fill: white;"));
        cseButton.setOnMouseExited(e -> cseButton.setStyle("-fx-background-color: #66bb6a; -fx-text-fill: white;"));

        itButton.setStyle("-fx-background-color: #42a5f5; -fx-text-fill: white; -fx-font-weight: bold;");
        itButton.setOnMouseEntered(e -> itButton.setStyle("-fx-background-color: #1e88e5; -fx-text-fill: white;"));
        itButton.setOnMouseExited(e -> itButton.setStyle("-fx-background-color: #42a5f5; -fx-text-fill: white;"));

        regenerateButton.setStyle("-fx-background-color: #ffa726; -fx-text-fill: white; -fx-font-weight: bold;");
        regenerateButton.setOnMouseEntered(e -> regenerateButton.setStyle("-fx-background-color: #ef6c00; -fx-text-fill: white;"));
        regenerateButton.setOnMouseExited(e -> regenerateButton.setStyle("-fx-background-color: #ffa726; -fx-text-fill: white;"));
        regenerateButton.setOnAction(e -> regenerateTimetable());

        exitButton.setStyle("-fx-background-color: #e57373; -fx-text-fill: white; -fx-font-weight: bold;");
        exitButton.setOnMouseEntered(e -> exitButton.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white;"));
        exitButton.setOnMouseExited(e -> exitButton.setStyle("-fx-background-color: #e57373; -fx-text-fill: white;"));
        exitButton.setOnAction(e -> Platform.exit());

        // Button actions to update UI based on selected class
        cseButton.setOnAction(e -> updateContent("CSE-A"));
        itButton.setOnAction(e -> updateContent("IT-A"));

        buttonBox.getChildren().addAll(cseButton, itButton, regenerateButton, exitButton);
        return buttonBox;
    }

    private void regenerateTimetable() {
        if (connection != null) {
            generator = new TimetableGenerator();
            generator.generateAndDisplayTimetable(connection);
            updateContent("CSE-A");
        } else {
            System.out.println("Failed to reconnect to the database.");
        }
    }

    private void updateContent(String className) {
        root.setCenter(createTimetableGrid(className));
        root.setRight(createTeacherAssignmentsBox(className));
    }

    private GridPane createTimetableGrid(String className) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(15));
        grid.setStyle("-fx-border-color: #66bb6a; -fx-border-width: 3; -fx-border-radius: 8; -fx-background-color: #e8f5e9;");

        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        Label dayHeader = new Label("Day");
        Label coursesHeader = new Label("Courses"); // Updated title from "Activities" to "Courses"
        dayHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #1b5e20;");
        coursesHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #1b5e20;");
        grid.add(dayHeader, 0, 0);
        grid.add(coursesHeader, 1, 0);

        Map<String, List<String>> timetable = generator.getTimetable(className);
        int row = 1;
        for (String day : days) {
            List<String> activities = timetable.get(day);
            if (activities != null) {
                activities.removeIf(activity -> activity.contains("Break"));
            }

            Label dayLabel = new Label(day);
            Label activityLabel = new Label(activities != null ? String.join("\n", activities) : "No Data");

            dayLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px; -fx-text-fill: #2e7d32;");
            activityLabel.setStyle("-fx-padding: 5; -fx-font-size: 12px; -fx-text-fill: #388e3c;");

            grid.add(dayLabel, 0, row);
            grid.add(activityLabel, 1, row);
            row++;
        }

        return grid;
    }

    private VBox createTeacherAssignmentsBox(String className) {
        VBox teacherBox = new VBox(12);
        teacherBox.setPadding(new Insets(12));
        teacherBox.setStyle("-fx-border-color: #ffa726; -fx-border-width: 3; -fx-border-radius: 8; -fx-background-color: #fff3e0;");

        Map<String, String> assignments = generator.getTeacherAssignments(className);

        Label header = new Label("Teacher Assignments for " + className);
        header.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #ef6c00;");
        teacherBox.getChildren().add(header);

        for (Map.Entry<String, String> entry : assignments.entrySet()) {
            Label assignmentLabel = new Label(entry.getKey() + " - " + entry.getValue());
            assignmentLabel.setStyle("-fx-padding: 5; -fx-font-size: 12px; -fx-text-fill: #f57c00;");
            teacherBox.getChildren().add(assignmentLabel);
        }

        return teacherBox;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
