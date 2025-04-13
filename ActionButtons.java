package MiniProject;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ActionButtons {
    public static HBox createButtons() {
        HBox buttonBox = new HBox(15); // Add spacing between buttons

        Button generateButton = new Button("Generate Timetable");
        Button downloadButton = new Button("Download PDF");
        Button exitButton = new Button("Exit");

        // Add actions to buttons
        generateButton.setOnAction(e -> System.out.println("Timetable generated!"));
        downloadButton.setOnAction(e -> System.out.println("PDF downloaded!"));
        exitButton.setOnAction(e -> System.exit(0));

        buttonBox.getChildren().addAll(generateButton, downloadButton, exitButton);
        return buttonBox;
    }
}