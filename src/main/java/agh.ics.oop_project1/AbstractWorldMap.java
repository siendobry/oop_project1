package agh.ics.oop_project1;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractWorldMap implements IWorldMap {

    //it would be practical to store animals in every list sorted by energy, age, number of children
    //it probably forces changing type from ArrayList to something that stores elements in order
    private final HashMap<Vector2d, ArrayList<Animal>> animals;
    private final HashMap<Vector2d, ArrayList<Flora>> flora;
//    private final ArrayList<Vector2d> moreProbableArea;
//    private final ArrayList<Vector2d> lessProbableArea;
    protected final int height;
    protected final int width;

    public AbstractWorldMap(int height, int width) {
        this.animals = new HashMap<>();
        this.flora = new HashMap<>();
        this.height = height - 1;
        this.width = width - 1;
    }

    // TO DO
    public void putFlora() {
        System.out.println("O");
    }

    public Vector2d placeAtRandomPosition() {
        return new Vector2d(RandomNumberGenerator.getRandomNumber(0, this.width),
                            RandomNumberGenerator.getRandomNumber(0, this.height));
    }

    public ArrayList<Animal> animalsAt(Vector2d position) {
        return this.animals.get(position);
    }

    public ArrayList<Flora> floraAt(Vector2d position) {
        return this.flora.get(position);
    }

    //this method adds new flora to the map after end of every day
    public void growFlora() {

    }

}
