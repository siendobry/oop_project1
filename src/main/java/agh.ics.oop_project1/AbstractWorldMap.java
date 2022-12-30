package agh.ics.oop_project1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public abstract class AbstractWorldMap implements IWorldMap, IObserver {

    //it would be practical to store animals in every list sorted by energy, age, number of children
    //it probably forces changing type from ArrayList to something that stores elements in order
    private final HashMap<Vector2d, TreeSet<Animal>> animals;
    private final HashMap<Vector2d, Flora> flora;
    private final ArrayList<Vector2d> moreProbableArea;
    private final ArrayList<Vector2d> lessProbableArea;
    protected final int height;
    protected final int width;

    public AbstractWorldMap(int height, int width) {
        this.animals = new HashMap<>();
        this.flora = new HashMap<>();
        this.height = height - 1;
        this.width = width - 1;
        this.moreProbableArea = new ArrayList<>();
        this.lessProbableArea = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < (int)Math.round((2 * (double)height) / 5); j++) {
                this.lessProbableArea.add(new Vector2d(i, j));
            }
            for (int j = (int) Math.round((2 * (double)height) / 5); j < (int)Math.round((3 * (double)height) / 5); j++) {
                this.moreProbableArea.add(new Vector2d(i, j));
            }
            for (int j = (int) Math.round((3 * (double)height) / 5); j < height; j++) {
                this.lessProbableArea.add(new Vector2d(i, j));
            }
        }
    }

    // TO DO
    public void putFlora(int amount) {
        for (int i = 0; i < amount; i++) {
            int rngResult = RandomNumberGenerator.getRandomNumber(0, 4);
            Vector2d randomField = null;
            if (rngResult == 0 && this.moreProbableArea.size() > 0) {
                int randomIdx = RandomNumberGenerator.getRandomNumber(0, this.moreProbableArea.size());
                randomField = this.moreProbableArea.remove(randomIdx);
            } else if (this.lessProbableArea.size() > 0) {
                int randomIdx = RandomNumberGenerator.getRandomNumber(0, this.lessProbableArea.size());
                randomField = this.lessProbableArea.remove(randomIdx);
            }
            if (randomField != null) {
                this.flora.put(randomField, new Flora());
            }
        }
    }

    public void removeAnimal(Animal animal) {

    }

    public void removeFlora(Vector2d position) {
        this.flora.remove(position);
    }

    public Vector2d placeAtRandomPosition() {
        return new Vector2d(RandomNumberGenerator.getRandomNumber(0, this.width),
                            RandomNumberGenerator.getRandomNumber(0, this.height));
    }

    public TreeSet<Animal> animalsAt(Vector2d position) {
        return this.animals.get(position);
    }

    public Flora floraAt(Vector2d position) {
        return this.flora.get(position);
    }

    public void positionChanged(Animal animal, Vector2d oldPosition) {
        this.animals.get(oldPosition).remove(animal);
        if (this.animals.get(animal.getPosition()) != null) {
            this.animals.get(animal.getPosition()).add(animal);
        }
        else {
            TreeSet<Animal> fieldAnimalList = new TreeSet<>(new AnimalComparator());
            fieldAnimalList.add(animal);
            this.animals.put(animal.getPosition(), fieldAnimalList);
        }
    }

    public void stateChanged() {

    }

}
