package MiniProject;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class TimetableGrid {
    public static GridPane createTimetable() {
        GridPane grid = new GridPane();
        grid.setHgap(10); // Horizontal spacing between columns
        grid.setVgap(10); // Vertical spacing between rows

        // Add headers for days
        grid.add(new Label("Time Slot"), 0, 0);
        grid.add(new Label("Monday"), 1, 0);
        grid.add(new Label("Tuesday"), 2, 0);
        grid.add(new Label("Wednesday"), 3, 0);
        grid.add(new Label("Thursday"), 4, 0);
        grid.add(new Label("Friday"), 5, 0);

        // Add sample timetable entries
        grid.add(new Label("9:00 - 9:50"), 0, 1);
        grid.add(new Label("OOP"), 1, 1);
        grid.add(new Label("DSA"), 2, 1);
        grid.add(new Label("COA"), 3, 1);
        grid.add(new Label("Statistics"), 4, 1);
        grid.add(new Label("Lunch Break"), 0, 4);

        return grid;
    }
}