package MiniProject;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("University Timetable System");

        // Root container using BorderPane layout
        BorderPane root = new BorderPane();

        // Add timetable grid to the center
        root.setCenter(createTimetableGrid());

        // Add action buttons to the bottom
        root.setBottom(createActionButtons());

        // Add teacher assignments to the right
        root.setRight(createTeacherAssignments());

        // Style the root layout with padding
        root.setPadding(new Insets(20));

        // Create the scene
        Scene scene = new Scene(root, 1000, 600);

        // Set background color
        scene.setFill(Color.LIGHTGRAY);

        // Add the scene to the stage
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Create the timetable grid
    private GridPane createTimetableGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10); // Space between columns
        grid.setVgap(10); // Space between rows
        grid.setPadding(new Insets(10));
        grid.setStyle("-fx-border-color: blue; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-color: white;");

        // Header: Days of the week
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        grid.add(new Label("Time Slot"), 0, 0); // Add time slot header
        for (int i = 0; i < days.length; i++) {
            Label dayLabel = new Label(days[i]);
            dayLabel.setFont(new Font("Arial Bold", 14));
            dayLabel.setTextFill(Color.DARKBLUE);
            grid.add(dayLabel, i + 1, 0); // Add day headers in row 0
        }

        // Time slots
        String[] timeSlots = {
            "9:00 - 9:50", 
            "9:50 - 10:40", 
            "10:40 - 10:50 (Break)", 
            "10:50 - 11:40", 
            "11:40 - 12:30", 
            "12:30 - 1:30 (Lunch Break)", 
            "2:00 - 4:00"
        };
        for (int i = 0; i < timeSlots.length; i++) {
            Label timeLabel = new Label(timeSlots[i]);
            timeLabel.setFont(new Font("Arial", 14));
            grid.add(timeLabel, 0, i + 1); // Add time slot labels in column 0
        }

        // Sample timetable data for each day and time slot
        String[][] timetableData = {
            {"OOP", "DSA", "Break", "COA", "DSD", "Lunch Break", "OOP Lab"},
            {"DSA", "OOP", "Break", "Statistics", "COA", "Lunch Break", "DSA Lab"},
            {"COA", "DSA", "Break", "DSD", "OOP", "Lunch Break", "DSD Lab"},
            {"Statistics", "COA", "Break", "DSA", "DSD", "Lunch Break", "OOP Lab"},
            {"DSD", "OOP", "Break", "COA", "Statistics", "Lunch Break", "DSA Lab"}
        };

        // Populate grid with timetable data
        for (int row = 0; row < timeSlots.length; row++) {
            for (int col = 0; col < days.length; col++) {
                Label subjectLabel = new Label(timetableData[col][row]);
                subjectLabel.setFont(new Font("Arial", 14));
                subjectLabel.setTextFill(Color.BLACK);
                grid.add(subjectLabel, col + 1, row + 1); // Populate cells
            }
        }

        return grid;
    }

    // Create action buttons at the bottom
    private HBox createActionButtons() {
        HBox buttonBox = new HBox(20); // Space between buttons
        buttonBox.setPadding(new Insets(10));
        buttonBox.setStyle("-fx-background-color: lightblue; -fx-border-radius: 5;");

        Label generateButton = createStyledButton("Generate Timetable", Color.DARKGREEN);
        Label downloadButton = createStyledButton("Download PDF", Color.DARKRED);
        Label exitButton = createStyledButton("Exit", Color.DARKBLUE);

        buttonBox.getChildren().addAll(generateButton, downloadButton, exitButton);
        return buttonBox;
    }

    // Create teacher assignments
    private VBox createTeacherAssignments() {
        VBox teacherBox = new VBox(10); // Spacing between rows
        teacherBox.setPadding(new Insets(10));
        teacherBox.setStyle("-fx-background-color: lightyellow; -fx-border-color: black; -fx-border-width: 1;");

        Label header = new Label("Teacher Assignments");
        header.setFont(new Font("Arial Bold", 16));
        header.setTextFill(Color.PURPLE);

        // Example teacher assignments
        Label oopTeacher = new Label("OOP - Dr. Smith");
        oopTeacher.setFont(new Font("Arial", 14));
        oopTeacher.setTextFill(Color.DARKGREEN);

        Label dsaTeacher = new Label("DSA - Dr. Kapoor");
        dsaTeacher.setFont(new Font("Arial", 14));
        dsaTeacher.setTextFill(Color.DARKRED);

        Label coaTeacher = new Label("COA - Dr. Das");
        coaTeacher.setFont(new Font("Arial", 14));
        coaTeacher.setTextFill(Color.DARKBLUE);

        teacherBox.getChildren().addAll(header, oopTeacher, dsaTeacher, coaTeacher);
        return teacherBox;
    }

    // Helper to create styled buttons
    private Label createStyledButton(String text, Color color) {
        Label button = new Label(text);
        button.setFont(new Font("Arial Bold", 14));
        button.setTextFill(color);
        return button;
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}
