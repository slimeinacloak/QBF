package org.peartcraft;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import net.sandrohc.schematic4j.schematic.LitematicaSchematic;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigGui extends Application {

    private static final String BLOCK_PREFIX = "minecraft:";
    public static final String[] DEFAULT_BLOCK_BLACKLIST = {
        "barrel",
        "bee_nest",
        "beehive",
        "blast_furnace",
        "brewing_stand",
        "chest",
        "crying_obsidian",
        "dispenser",
        "ender_chest",
        "furnace",
        "grindstone",
        "hopper",
        "lectern",
        "obsidian",
        "reinforced_deepslate",
        "sculk_catalyst",
        "sculk_sensor",
        "sculk_shrieker",
        "smoker",
        "sponge",
        "spawner",
        "trapped_chest",
        "trial_spawner",
        "vault"
    };

    private final CompletableFuture<ConfigData> future = new CompletableFuture<>();
    private final Set<String> blockSet = new LinkedHashSet<>(List.of(DEFAULT_BLOCK_BLACKLIST));

    @Override
    public void start(Stage primaryStage) {
        // Declare selected file property
        ObjectProperty<LitematicaSchematic> selectedFile = new SimpleObjectProperty<>();
        Label fileLabel = new Label("No file selected");

        // Coordinates
        Label section1 = new Label("Coordinates");
        TextField x1Field = new TextField(), y1Field = new TextField(), z1Field = new TextField();
        TextField x2Field = new TextField(), y2Field = new TextField(), z2Field = new TextField();

        x1Field.setPromptText("x1");
        y1Field.setPromptText("y1");
        z1Field.setPromptText("z1");
        x2Field.setPromptText("x2");
        y2Field.setPromptText("y2");
        z2Field.setPromptText("z2");

        // Restrict to integers
        Stream.of(x1Field, y1Field, z1Field, x2Field, y2Field, z2Field).forEach(this::restrictToIntegers);

        VBox corner1Box = new VBox(5, new Label("Corner 1"), new HBox(5, new Label("X:"), x1Field), new HBox(5, new Label("Y:"), y1Field), new HBox(5, new Label("Z:"), z1Field));

        VBox corner2Box = new VBox(5, new Label("Corner 2"), new HBox(5, new Label("X:"), x2Field), new HBox(5, new Label("Y:"), y2Field), new HBox(5, new Label("Z:"), z2Field));

        HBox coordsBox = new HBox(40, corner1Box, corner2Box);

        Label section2 = new Label("Waypoint Type");
        ComboBox<String> providerDropdown = new ComboBox<>();
        providerDropdown.getItems().addAll(MapMod.VOXEL.name(), MapMod.XAERO.name());
        providerDropdown.getSelectionModel().selectFirst();

        Label section3 = new Label("Dimension");
        ComboBox<String> dimensionDropdown = new ComboBox<>();
        dimensionDropdown.getItems().addAll(Dimension.OVERWORLD.getId(), Dimension.NETHER.getId(), Dimension.END.getId());
        dimensionDropdown.getSelectionModel().selectFirst();

        Label section4 = new Label("Blacklist");
        TextField blockInput = new TextField();
        blockInput.setPromptText("Add block name");

        ListView<String> blockListView = new ListView<>();
        ObservableList<String> blockList = FXCollections.observableArrayList();
        blockList.setAll(blockSet);
        blockListView.setItems(blockList);

        blockInput.setOnAction(e -> {
            String input = blockInput.getText().trim().toLowerCase();
            if (!input.isEmpty() && blockSet.add(input)) {
                blockList.setAll(blockSet);
                blockInput.clear();
            }
        });

        VBox blockBox = getBlacklistVBox(blockInput, blockList, blockListView);

        Button loadLitematic = new Button("Load Litematic");
        Button submitButton = new Button("Submit");
        submitButton.setDisable(true);

        loadLitematic.setOnAction(e -> {
            LitematicaSchematic litematic = LitematicSelector.pickFile(primaryStage);
            if (litematic != null) {
                selectedFile.set(litematic);
                fileLabel.setText(litematic.name());
                submitButton.setDisable(false);
            }
        });

        submitButton.setOnAction(e -> {
            try {
                int x1 = Integer.parseInt(x1Field.getText());
                int y1 = Integer.parseInt(y1Field.getText());
                int z1 = Integer.parseInt(z1Field.getText());

                int x2 = Integer.parseInt(x2Field.getText());
                int y2 = Integer.parseInt(y2Field.getText());
                int z2 = Integer.parseInt(z2Field.getText());

                String dimension = dimensionDropdown.getValue();
                String provider = providerDropdown.getValue();

                Set<String> newBlockSet = blockSet.stream().map(s -> BLOCK_PREFIX + s).collect(Collectors.toCollection(LinkedHashSet::new));

                ConfigData config = new ConfigData(x1, y1, z1, x2, y2, z2, dimension, provider, newBlockSet, selectedFile.get());
                future.complete(config);
                primaryStage.close();
            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Invalid coordinates").showAndWait();
            }
        });

        VBox fileSection = new VBox(5, new Label("Litematic File"), fileLabel, loadLitematic);

        VBox root = new VBox(15, section1, coordsBox, section2, providerDropdown, section3, dimensionDropdown, section4, blockBox, fileSection, submitButton);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("BlockFinder");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox getBlacklistVBox(TextField blockInput, ObservableList<String> blockList, ListView<String> blockListView) {
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            String newBlock = blockInput.getText().trim();
            if (!newBlock.isEmpty() && blockSet.add(newBlock)) {
                blockList.setAll(blockSet);
                blockInput.clear();
            }
        });

        Button removeButton = new Button("Remove Selected");
        removeButton.setOnAction(e -> {
            String selected = blockListView.getSelectionModel().getSelectedItem();
            if (selected != null && blockSet.remove(selected)) {
                blockList.setAll(blockSet);
            }
        });

        Button removeAllButton = new Button("Remove All");
        removeAllButton.setOnAction(e -> {
            blockSet.clear();
            blockList.setAll(blockSet);
        });

        blockListView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE) {
                String selected = blockListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    blockSet.remove(selected);
                    blockListView.getItems().setAll(blockSet);
                }
            }
        });

        HBox blockControls = new HBox(10, blockInput, addButton, removeButton, removeAllButton);
        return new VBox(5, blockControls, blockListView);
    }

    private void restrictToIntegers(TextField field) {
        field.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("-?\\d*")) {
                field.setText(oldVal);
            }
        });
    }

    public CompletableFuture<ConfigData> showAndWait() {
        Platform.runLater(() -> {
            try {
                start(new Stage());
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }
}
