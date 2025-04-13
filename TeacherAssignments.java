package MiniProject;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TeacherAssignments {
    public static VBox createTeacherTable() {
        VBox teacherBox = new VBox(10); // Add spacing between rows
        teacherBox.setStyle("-fx-padding: 15; -fx-border-color: darkblue; -fx-border-width: 2; -fx-background-color: lightgray;");

        Label header = new Label("Teacher Assignments");
        header.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: navy;");
        teacherBox.getChildren().add(header);

        teacherBox.getChildren().add(createStyledLabel("OOP - Dr. Smith", "darkblue"));
        teacherBox.getChildren().add(createStyledLabel("DSA - Dr. Kapoor", "darkgreen"));
        teacherBox.getChildren().add(createStyledLabel("COA - Dr. Das", "darkred"));
        teacherBox.getChildren().add(createStyledLabel("Statistics - Prof. Sharma", "purple"));

        return teacherBox;
    }

    private static Label createStyledLabel(String text, String color) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14; -fx-text-fill: " + color + ";");
        return label;
    }
}
