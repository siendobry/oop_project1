package agh.ics.oop_project1.gui;

import agh.ics.oop_project1.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;

public class App extends Application {

    private class DayChangeObserver implements IDayChangeObserver {

        SimulationEngine engine;
        StatisticsSet set;
        Label numberOfAnimals;
        Label numberOfFlora;
        Label numberOfFreeFields;
        Label mostPopularGenotype;
        Label averageEnergy;
        Label averageLifespan;

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

        Label mapHeightLabel = new Label("Map height:");
        TextField mapHeight = new TextField();
        Label mapWidthLabel = new Label("Map width:");
        TextField mapWidth = new TextField();

        Label mapVariantLabel = new Label("Map variant:");
        ToggleGroup variant = new ToggleGroup();
        RadioButton globeVariant = new RadioButton("Globe");
        globeVariant.setToggleGroup(variant);
        globeVariant.setSelected(true);
        globeVariant.setUserData("globe");
        RadioButton netherVariant = new RadioButton("Nether");
        netherVariant.setToggleGroup(variant);
        netherVariant.setUserData("nether");
        HBox mapVariant = new HBox(8, globeVariant, netherVariant);

        Label energyDrainAmountLabel = new Label("Amount of energy drain:");
        TextField energyDrainAmount = new TextField();

        Label numberOfAnimalsLabel = new Label("Number of starting animals:");
        TextField numberOfAnimals = new TextField();
        Label lengthOfGenomeLabel = new Label("Length of genome:");
        TextField lengthOfGenome = new TextField();
        Label initialEnergyLabel = new Label("Initial energy:");
        TextField initialEnergy = new TextField();
        Label startingFloraLabel = new Label("Starting flora:");
        TextField startingFlora = new TextField();
        Label floraGrowthLabel = new Label("Everyday flora growth:");
        TextField floraGrowth = new TextField();
        Label nutritionValueLabel = new Label("Single flora energetic value:");
        TextField nutritionValue = new TextField();
        Label energyNeededToBreedLabel = new Label("Energy needed to breed:");
        TextField energyNeededToBreed = new TextField();
        Label energyLossOnBreedingLabel = new Label("Energy loss on breeding:");
        TextField energyLossOnBreeding = new TextField();
        Label minMutationsLabel = new Label("Minimum mutations number:");
        TextField minMutations = new TextField();
        Label maxMutationsLabel = new Label("Maximum mutations number:");
        TextField maxMutations = new TextField();
        CheckBox saveCSV = new CheckBox("Save statistics in CSV");

        Button save = new Button("Save configuration");
        Button start = new Button("Start simulation");
        start.setOnAction(actionEvent -> {
            Stage newSimulation = new Stage();
            IWorldMap map;
            if (Objects.equals(variant.getSelectedToggle().getUserData().toString(), "globe")) {
                map = new GlobeMap(
                        parseTextFieldToInt(mapHeight),
                        parseTextFieldToInt(mapWidth)
                );
            }
            else {
                map = new NetherMap(
                        parseTextFieldToInt(mapHeight),
                        parseTextFieldToInt(mapWidth),
                        parseTextFieldToInt(energyDrainAmount)

                );
            }
            SimulationEngine engine = new SimulationEngine(
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
            VBox mainContainer = new VBox(12, stats, simulationContainer);
            newSimulation.setScene(new Scene(mainContainer));
            newSimulation.show();
            engineThread.start();
        });
        HBox controls = new HBox(8, save, start);

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

        Scene scene = new Scene(container);
        primaryStage.setScene(scene);
        primaryStage.show();

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

        Vector2d upperRightMapCorner = new Vector2d(map.getHeight() - 1, map.getWidth() - 1);
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
