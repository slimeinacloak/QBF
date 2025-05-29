package org.peartcraft;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SaveGui extends Application {

    public static List<String> linesToSave;
    public static final CompletableFuture<Void> finished = new CompletableFuture<>();

    @Override
    public void start(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Waypoints");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setInitialFileName("waypoints.txt");

        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                for (String line : linesToSave) {
                    writer.write(line);
                    writer.write(System.lineSeparator());
                }
            } catch (IOException ignored) {}
        }

        finished.complete(null);
        primaryStage.close();
    }
}
