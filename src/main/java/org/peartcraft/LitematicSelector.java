package org.peartcraft;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sandrohc.schematic4j.SchematicLoader;
import net.sandrohc.schematic4j.schematic.LitematicaSchematic;

import java.io.File;

public class LitematicSelector {

    public static LitematicaSchematic pickFile(Stage owner) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a File");
        String path = System.getProperty("user.home");
        fileChooser.setInitialDirectory(new File(path));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Litematica Files", "*.litematic")
        );

        File selectedFile = fileChooser.showOpenDialog(owner);
        if (selectedFile != null && selectedFile.getName().toLowerCase().endsWith(".litematic")) {
            try {
                return (LitematicaSchematic) SchematicLoader.load(selectedFile);
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Failed to load schematic: " + e.getMessage()).showAndWait();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please select a valid .litematic file.").showAndWait();
        }
        return null;
    }
}
