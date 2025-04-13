package MiniProject;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TeacherAssignments {
    public static VBox createTeacherTable() {
        VBox teacherBox = new VBox(10); // Add spacing between rows

        teacherBox.getChildren().add(new Label("Teacher Assignments"));
        teacherBox.getChildren().add(new Label("OOP - Dr. Smith"));
        teacherBox.getChildren().add(new Label("DSA - Dr. Kapoor"));
        teacherBox.getChildren().add(new Label("COA - Dr. Das"));
        teacherBox.getChildren().add(new Label("Statistics - Prof. Sharma"));

        return teacherBox;
    }
}