package agh.ics.oop_project1.gui;

import agh.ics.oop_project1.*;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class App extends Application{

/*    private static class GuiElementBox {

        private final VBox container;

        private SimulationEngine engine;

        public GuiElementBox(IMapElement element) throws FileNotFoundException {
            ImageView image = new ImageView(new Image(new FileInputStream(element.getImageUrl())));
            image.setFitWidth(20);
            image.setFitHeight(20);
            Label label = new Label();
            if (element instanceof Animal) {
                label.setText(element.getPosition().toString());
            }
            else {
                label.setText("Grass");
            }
            this.container = new VBox(2, image, label);
            this.container.setAlignment(Pos.CENTER);
        }

    }

    private static final double CELL_DIMS = ;
    private GridPane grid;

    private void renderGrid() throws FileNotFoundException {

        Label coordinates = new Label("y/x");
        GridPane.setHalignment(coordinates, HPos.CENTER);
        this.grid.add(coordinates, 0, 0);
        this.grid.getColumnConstraints().add(new ColumnConstraints(CELL_DIMS));
        this.grid.getRowConstraints().add(new RowConstraints(CELL_DIMS));

        Vector2d upperRightMapCorner = new Vector2d()
        Vector2d lowerLeftMapCorner = new Vector2d(0, 0);

        for (int i = 0; i < upperRightMapCorner.getY() - lowerLeftMapCorner.getY() + 1; i++) {
            this.grid.getRowConstraints().add(new RowConstraints(CELL_DIMS));
            Label t = new Label(String.valueOf(upperRightMapCorner.getY() - i));
            GridPane.setHalignment(t, HPos.CENTER);
            this.grid.add(t, 0, i + 1);
        }

        for (int i = 0; i < upperRightMapCorner.getX() - lowerLeftMapCorner.getX() + 1; i++) {
            this.grid.getColumnConstraints().add(new ColumnConstraints(CELL_DIMS));
            Label t = new Label(String.valueOf(lowerLeftMapCorner.getX() + i));
            GridPane.setHalignment(t, HPos.CENTER);
            this.grid.add(t, i + 1, 0);
        }

        for (IMapElement element: this.map.getElements().values()) {
            VBox box = new GuiElementBox(element).container;
            GridPane.setHalignment(box, HPos.CENTER);
            this.grid.add(box, element.getPosition().getX() - lowerLeftMapCorner.getX() + 1, upperRightMapCorner.getY() - element.getPosition().getY() + 1);
        }

    }*/

    public void start(Stage primaryStage) {

    }
}
