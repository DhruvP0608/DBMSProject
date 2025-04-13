package MiniProject;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ActionButtons {
    public static HBox createButtons() {
        HBox buttonBox = new HBox(15); // Add spacing between buttons
        buttonBox.setStyle("-fx-padding: 10; -fx-border-color: gray; -fx-border-width: 2;");

        Button generateButton = createStyledButton("Generate Timetable", "blue");
        Button downloadButton = createStyledButton("Download PDF", "green");
        Button exitButton = createStyledButton("Exit", "red");

        // Add functionality to buttons
        generateButton.setOnAction(e -> System.out.println("Timetable generated!"));
        downloadButton.setOnAction(e -> System.out.println("PDF downloaded!"));
        exitButton.setOnAction(e -> System.exit(0));

        buttonBox.getChildren().addAll(generateButton, downloadButton, exitButton);
        return buttonBox;
    }

    private static Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 14; -fx-padding: 5 10;");
        return button;
    }
}
