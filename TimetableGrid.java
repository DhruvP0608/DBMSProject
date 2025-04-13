package MiniProject;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class TimetableGrid {
    public static GridPane createTimetable() {
        GridPane grid = new GridPane();
        grid.setHgap(15); // Horizontal spacing between columns
        grid.setVgap(15); // Vertical spacing between rows
        grid.setStyle("-fx-border-color: blue; -fx-border-width: 2; -fx-border-radius: 5; -fx-padding: 15;");

        // Add headers for days
        Label timeSlotHeader = new Label("Time Slot");
        timeSlotHeader.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: darkblue;");
        grid.add(timeSlotHeader, 0, 0);

        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        for (int i = 0; i < days.length; i++) {
            Label dayLabel = new Label(days[i]);
            dayLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: green;");
            grid.add(dayLabel, i + 1, 0);
        }

        // Add sample timetable entries with styling
        grid.add(createStyledLabel("9:00 - 9:50", "red"), 0, 1);
        grid.add(createStyledLabel("OOP", "purple"), 1, 1);
        grid.add(createStyledLabel("DSA", "orange"), 2, 1);
        grid.add(createStyledLabel("COA", "blue"), 3, 1);
        grid.add(createStyledLabel("Statistics", "darkgreen"), 4, 1);

        // Add lunch break
        grid.add(createStyledLabel("12:30 - 1:30", "black"), 0, 4);
        grid.add(createStyledLabel("Lunch Break", "darkred"), 1, 4, 5, 1);

        return grid;
    }

    private static Label createStyledLabel(String text, String color) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14; -fx-text-fill: " + color + ";");
        return label;
    }
}
