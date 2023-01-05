package agh.ics.oop_project1.gui;

import agh.ics.oop_project1.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;

public class App extends Application {

    private TextField mapHeight;
    private TextField mapWidth;
    private ToggleGroup variant;
    private TextField energyDrainAmount;
    private TextField numberOfAnimals;
    private TextField initialEnergy;
    private TextField startingFlora;
    private TextField floraGrowth;
    private TextField nutritionValue;
    private TextField energyNeededToBreed;
    private TextField energyLossOnBreeding;
    private TextField minMutations;
    private TextField maxMutations;
    private TextField lengthOfGenome;

    private class DayChangeObserver implements IDayChangeObserver {

        private final SimulationEngine engine;
        private StatisticsSet set;
        private Label numberOfAnimals;
        private Label numberOfFlora;
        private Label numberOfFreeFields;
        private Label mostPopularGenotype;
        private Label averageEnergy;
        private Label averageLifespan;




        public DayChangeObserver(
                SimulationEngine engine,
                Label numberOfAnimals,
                Label numberOfFlora,
                Label numberOfFreeFields,
                Label mostPopularGenotype,
                Label averageEnergy,
                Label averageLifespan
        ) {
            this.engine = engine;
            this.set = engine.returnStatistics();
            this.numberOfAnimals = numberOfAnimals;
            this.numberOfFlora = numberOfFlora;
            this.numberOfFreeFields = numberOfFreeFields;
            this.mostPopularGenotype = mostPopularGenotype;
            this.averageEnergy = averageEnergy;
            this.averageLifespan = averageLifespan;
        }

        public void dayChanged() {
            Platform.runLater(() -> {
                this.set = engine.returnStatistics();
                this.numberOfAnimals.setText("Number of animals: " + this.set.numberOfAnimals());
                this.numberOfFlora.setText("Number of flora: " + this.set.numberOfFlora());
                this.numberOfFreeFields.setText("Number of free fields: " + this.set.numberOfFreeFields());
                this.mostPopularGenotype.setText("Most popular genotype: " + this.set.mostPopularGenotype());
                this.averageEnergy.setText("Average energy: " + Math.round(this.set.averageEnergy() * 100) / 100);
                this.averageLifespan.setText("Average lifespan: " + Math.round(this.set.averageLifespan() * 100) / 100);
            });
        }

    }

    private class SimulationObserver implements IObserver {

        SimulationEngine engine;
        IWorldMap map;
        GridPane grid;
        CSVFileCreator creator;
        boolean shouldSave;

        public SimulationObserver(SimulationEngine engine, IWorldMap map, GridPane grid, boolean shouldSave) {
            this.engine = engine;
            this.map = map;
            this.grid = grid;
            this.shouldSave = shouldSave;
            if (shouldSave) {
                this.creator = new CSVFileCreator("dane.csv");
            }
        }

        public void positionChanged(Animal animal, Vector2d oldPosition) {
            this.actualizeGrid();
        }

        public void stateChanged(Animal animal) {
            this.actualizeGrid();
        }

        private void actualizeGrid() {
            if (this.shouldSave) {
                creator.exportStatistics(this.engine.returnStatistics());
            }
            Platform.runLater(() -> {
                this.grid.getChildren().clear();
                try {
                    renderGrid(this.map, this.grid);
                }
                catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                    throw new RuntimeException(ex);
                }
            });
        }

    }

    public void start(Stage primaryStage) {

        primaryStage.setTitle("The Game of Programowanie Obiektowe");

        Label mapHeightLabel = new Label("Map height:");
        mapHeight = new TextField();
        Label mapWidthLabel = new Label("Map width:");
        mapWidth = new TextField();

        Label mapVariantLabel = new Label("Map variant:");
        variant = new ToggleGroup();
        RadioButton globeVariant = new RadioButton("Globe");
        globeVariant.setToggleGroup(variant);
        globeVariant.setSelected(true);
        globeVariant.setUserData("globe");
        RadioButton netherVariant = new RadioButton("Nether");
        netherVariant.setToggleGroup(variant);
        netherVariant.setUserData("nether");
        HBox mapVariant = new HBox(8, globeVariant, netherVariant);
        Label energyDrainAmountLabel = new Label("Amount of energy drain: (for Nether map variant)");
        energyDrainAmount = new TextField();
        Label numberOfAnimalsLabel = new Label("Number of starting animals:");
        numberOfAnimals = new TextField();
        Label lengthOfGenomeLabel = new Label("Length of genome:");
        lengthOfGenome = new TextField();
        Label initialEnergyLabel = new Label("Initial energy:");
        initialEnergy = new TextField();
        Label startingFloraLabel = new Label("Starting flora:");
        startingFlora = new TextField();
        Label floraGrowthLabel = new Label("Everyday flora growth:");
        floraGrowth = new TextField();
        Label nutritionValueLabel = new Label("Single flora energetic value:");
        nutritionValue = new TextField();
        Label energyNeededToBreedLabel = new Label("Energy needed to breed:");
        energyNeededToBreed = new TextField();
        Label energyLossOnBreedingLabel = new Label("Energy loss on breeding:");
        energyLossOnBreeding = new TextField();
        Label minMutationsLabel = new Label("Minimum mutations number:");
        minMutations = new TextField();
        Label maxMutationsLabel = new Label("Maximum mutations number:");
        maxMutations = new TextField();
        CheckBox saveCSV = new CheckBox("Save statistics in CSV");
        Label saveConfigLabel = new Label("Configuration file name:");
        TextField saveConfig = new TextField();

        Button save = new Button("Save configuration");
        save.setMaxWidth(Double.MAX_VALUE);
        Button start = new Button("Start simulation");
        start.setMaxHeight(Double.MAX_VALUE);
        VBox configContainer = new VBox(4, saveConfigLabel, saveConfig, save);

        save.setOnAction(actionEvent -> {
            if (saveConfig.getText().equals("")) {
                new Alert(Alert.AlertType.ERROR, "Provide a valid name for configuration file!", ButtonType.CLOSE).show();
            }
            else {
                try {
                    ConfigurationManager.saveConfig(this.concatenateInputs(), saveConfig.getText());
                }
                catch (IOException ex) {
                    new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.CLOSE).show();
                }
            }
        });

        start.setOnAction(actionEvent -> {
            Stage newSimulation = new Stage();
            IWorldMap map;
            SimulationEngine engine;
            if (this.isConfigCorrect()) {
                newSimulation.setTitle("A new life emerges... from user's input");
                if (Objects.equals(variant.getSelectedToggle().getUserData().toString(), "globe")) {
                    map = new GlobeMap(
                            parseTextFieldToInt(mapHeight),
                            parseTextFieldToInt(mapWidth)
                    );
                } else {
                    map = new NetherMap(
                            parseTextFieldToInt(mapHeight),
                            parseTextFieldToInt(mapWidth),
                            parseTextFieldToInt(energyDrainAmount)

                    );
                }
                engine = new SimulationEngine(
                        map,
                        parseTextFieldToInt(numberOfAnimals),
                        parseTextFieldToInt(initialEnergy),
                        parseTextFieldToInt(startingFlora),
                        parseTextFieldToInt(floraGrowth),
                        parseTextFieldToInt(nutritionValue),
                        parseTextFieldToInt(energyNeededToBreed),
                        parseTextFieldToInt(energyLossOnBreeding),
                        parseTextFieldToInt(minMutations),
                        parseTextFieldToInt(maxMutations),
                        parseTextFieldToInt(lengthOfGenome)
                );
            }
            else if (this.findFile(saveConfig.getText())) {
                newSimulation.setTitle("A new life emerges... from a configuration file");
                ConfigurationManager config;
                try {
                    config = new ConfigurationManager("configs/" + saveConfig.getText() + ".cfg");
                }
                catch (IllegalArgumentException ex) {
                    new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.CLOSE).show();
                    return;
                }
                if (config.getMapVariant().equals("globe")) {
                    map = new GlobeMap(
                            config.getMapHeight(),
                            config.getMapWidth());
                }
                else {
                    map = new NetherMap(
                            config.getMapHeight(),
                            config.getMapWidth(),
                            config.getEnergyDrainAmount());
                }
                engine = new SimulationEngine(config);
            }
            else {
                new Alert(Alert.AlertType.ERROR, "Incorrect input and/or configuration file with provided name does not exist!", ButtonType.CLOSE).show();
                return;
            }
            Thread engineThread = new Thread(engine);
            Button startSimulation = new Button("Start");
            startSimulation.setOnAction(actionSimulationEvent -> {
                Thread.currentThread().notifyAll();
            });
            Button pauseSimulation = new Button("Pause");
            pauseSimulation.setOnAction(actionSimulationEvent -> {
                try {
                    engineThread.wait();
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                    throw new RuntimeException(ex);
                }
            });
            HBox simulationControls = new HBox(8, pauseSimulation, startSimulation);
            GridPane grid = new GridPane();
            VBox simulationContainer = new VBox(8, simulationControls, grid);

            Label simulationNumberOfAnimals = new Label("Number of animals: " + numberOfAnimals);
            Label simulationNumberOfFlora = new Label("Number of flora: " + startingFlora);
            Label simulationNumberOfFreeFields = new Label("Number of free fields: " + 0);
            Label simulationMostPopularGenotype = new Label("Most popular genotype:");
            Label simulationAverageEnergy = new Label("Average energy: " + initialEnergy);
            Label simulationAverageLifespan = new Label("Average lifespan:");
            DayChangeObserver statObserver = new DayChangeObserver(
                    engine,
                    simulationNumberOfAnimals,
                    simulationNumberOfFlora,
                    simulationNumberOfFreeFields,
                    simulationMostPopularGenotype,
                    simulationAverageEnergy,
                    simulationAverageLifespan
            );
            engine.addDayChangeObserver(statObserver);
            VBox stats = new VBox(8,
                    simulationNumberOfAnimals,
                    simulationNumberOfFlora,
                    simulationNumberOfFreeFields,
                    simulationMostPopularGenotype,
                    simulationAverageEnergy,
                    simulationAverageLifespan);

            try {
                this.renderGrid(map, grid);
            }
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
            SimulationObserver observer = new SimulationObserver(engine, map, grid, saveCSV.isSelected());
            for(Animal animal: engine.getAnimals()) {
                animal.addObserver(observer);
            }
            VBox contents = new VBox(12, stats, simulationContainer);
            BorderPane mainContainer = new BorderPane();
            mainContainer.setCenter(contents);
            mainContainer.setPadding(new Insets(10));
            newSimulation.setScene(new Scene(mainContainer));
            newSimulation.setOnHiding(event -> engineThread.interrupt());
            newSimulation.show();
            engineThread.start();
        });
        BorderPane controls = new BorderPane();
        controls.setLeft(configContainer);
        controls.setRight(start);

        BorderPane gui = new BorderPane();
        VBox container = new VBox(8,
                mapHeightLabel,
                mapHeight,
                mapWidthLabel,
                mapWidth,
                mapVariantLabel,
                mapVariant,
                energyDrainAmountLabel,
                energyDrainAmount,
                numberOfAnimalsLabel,
                numberOfAnimals,
                lengthOfGenomeLabel,
                lengthOfGenome,
                initialEnergyLabel,
                initialEnergy,
                startingFloraLabel,
                startingFlora,
                floraGrowthLabel,
                floraGrowth,
                nutritionValueLabel,
                nutritionValue,
                energyNeededToBreedLabel,
                energyNeededToBreed,
                energyLossOnBreedingLabel,
                energyLossOnBreeding,
                minMutationsLabel,
                minMutations,
                maxMutationsLabel,
                maxMutations,
                saveCSV,
                controls
        );

        gui.setCenter(container);
        gui.setPadding(new Insets(10));
        Scene scene = new Scene(gui);
        primaryStage.setScene(scene);
        primaryStage.show();
        new Alert(Alert.AlertType.INFORMATION,
                """
                        Configuration file name input is multi-purpose!
                        It can act as an input for new configuration file when paired with Save button,
                        but it can also act as an input for an existing configuration file name when paired with Start button
                        (provided file name should not be followed by any extension).""", ButtonType.OK).show();
    }

    private String concatenateInputs() {
        String configuration;
        configuration = "mapHeight:" + mapHeight.getText() + "\n";
        configuration += "mapWidth:" + mapWidth.getText() + "\n";
        configuration += "mapVariant:" + variant.getSelectedToggle().getUserData().toString() + "\n";
        configuration += "energyDrainAmount:" + energyDrainAmount.getText() + "\n";
        configuration += "numberOfAnimals:" + numberOfAnimals.getText() + "\n";
        configuration += "lengthOfGenome:" + lengthOfGenome.getText() + "\n";
        configuration += "initialEnergy:" + initialEnergy.getText() + "\n";
        configuration += "startingFlora:" + startingFlora.getText() + "\n";
        configuration += "floraGrowth:" + floraGrowth.getText() + "\n";
        configuration += "nutritionValue:" + nutritionValue.getText() + "\n";
        configuration += "energyNeededToBreed:" + energyNeededToBreed.getText() + "\n";
        configuration += "energyLossOnBreeding:" + energyLossOnBreeding.getText() + "\n";
        configuration += "minMutations:" + minMutations.getText() + "\n";
        configuration += "maxMutations:" + maxMutations.getText();
        return configuration;
    }

    private boolean isConfigCorrect() {
        return (!mapHeight.getText().equals("") && isNumber(mapHeight.getText()))
                && (!mapWidth.getText().equals("") && isNumber(mapWidth.getText()))
                && ((variant.getSelectedToggle().getUserData().equals("globe") || !energyDrainAmount.getText().equals("")) && isNumber(energyDrainAmount.getText()))
                && (!numberOfAnimals.getText().equals("") && isNumber(numberOfAnimals.getText()))
                && (!initialEnergy.getText().equals("") && isNumber(initialEnergy.getText()))
                && (!startingFlora.getText().equals("") && isNumber(startingFlora.getText()))
                && (!floraGrowth.getText().equals("") && isNumber(floraGrowth.getText()))
                && (!nutritionValue.getText().equals("") && isNumber(nutritionValue.getText()))
                && (!energyNeededToBreed.getText().equals("") && isNumber(energyNeededToBreed.getText()))
                && (!energyLossOnBreeding.getText().equals("") && isNumber(energyLossOnBreeding.getText()))
                && (!minMutations.getText().equals("") && isNumber(minMutations.getText()))
                && (!maxMutations.getText().equals("") && isNumber(maxMutations.getText()))
                && (!lengthOfGenome.getText().equals("") && isNumber(lengthOfGenome.getText()));
    }

    private boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        }
        catch (NumberFormatException ex) {
            System.out.println("AAA");
            new Alert(Alert.AlertType.ERROR, "Value '" + str + "' is not of correct format", ButtonType.CLOSE).show();
            return false;
        }
    }

    private boolean findFile(String fileToBeFound) {
        File dir = new File(System.getProperty("user.dir") + "/configs/");
        return Objects.requireNonNull(dir.listFiles((dir1, name) -> name.matches(fileToBeFound + ".cfg"))).length == 1;
    }

    private Integer parseTextFieldToInt(TextField node) {
        return Integer.parseInt(node.getText());
    }

    private static class GuiElementBox {

        private final VBox container;

        public GuiElementBox(IMapElement element) throws FileNotFoundException {
            ImageView image = new ImageView(new Image(new FileInputStream(element.getImageUrl())));
            image.setFitWidth(20);
            image.setFitHeight(20);
            Label label = new Label(element.toString());
            this.container = new VBox(2, image, label);
            this.container.setAlignment(Pos.CENTER);
        }

    }

    private static final double CELL_DIMS = 20;

    private void renderGrid(IWorldMap map, GridPane grid) throws FileNotFoundException {

        Label coordinates = new Label("y/x");
        GridPane.setHalignment(coordinates, HPos.CENTER);
        grid.add(coordinates, 0, 0);
        grid.getColumnConstraints().add(new ColumnConstraints(CELL_DIMS));
        grid.getRowConstraints().add(new RowConstraints(CELL_DIMS));

        Vector2d upperRightMapCorner = new Vector2d(map.getWidth() - 1, map.getHeight() - 1);
        Vector2d lowerLeftMapCorner = new Vector2d(0, 0);

        for (int i = 0; i < upperRightMapCorner.getY() - lowerLeftMapCorner.getY() + 1; i++) {
            grid.getRowConstraints().add(new RowConstraints(CELL_DIMS));
            Label t = new Label(String.valueOf(upperRightMapCorner.getY() - i));
            GridPane.setHalignment(t, HPos.CENTER);
            grid.add(t, 0, i + 1);
        }

        for (int i = 0; i < upperRightMapCorner.getX() - lowerLeftMapCorner.getX() + 1; i++) {
            grid.getColumnConstraints().add(new ColumnConstraints(CELL_DIMS));
            Label t = new Label(String.valueOf(lowerLeftMapCorner.getX() + i));
            GridPane.setHalignment(t, HPos.CENTER);
            grid.add(t, i + 1, 0);
        }

        for (IMapElement element: map.getElements().values()) {
//            VBox box = new GuiElementBox(element).container;
            Label box = new Label(element.toString());
            GridPane.setHalignment(box, HPos.CENTER);
            grid.add(box, element.getPosition().getX() - lowerLeftMapCorner.getX() + 1, upperRightMapCorner.getY() - element.getPosition().getY() + 1);
        }

    }

}
