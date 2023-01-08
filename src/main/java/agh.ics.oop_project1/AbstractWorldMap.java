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
    protected final int heightCoordinate;
    protected final int widthCoordinate;

    public AbstractWorldMap(int heightCoordinate, int widthCoordinate) {
        this.animals = new HashMap<>();
        this.flora = new HashMap<>();
        this.heightCoordinate = heightCoordinate - 1;
        this.widthCoordinate = widthCoordinate - 1;
        this.moreProbableArea = new ArrayList<>();
        this.lessProbableArea = new ArrayList<>();
        for (int i = 0; i < widthCoordinate; i++) {
            for (int j = 0; j < (int)Math.round((2 * (double) heightCoordinate) / 5); j++) {
                this.lessProbableArea.add(new Vector2d(i, j));
            }
            for (int j = (int) Math.round((2 * (double) heightCoordinate) / 5); j < (int)Math.round((3 * (double) heightCoordinate) / 5); j++) {
                this.moreProbableArea.add(new Vector2d(i, j));
            }
            for (int j = (int) Math.round((3 * (double) heightCoordinate) / 5); j < heightCoordinate; j++) {
                this.lessProbableArea.add(new Vector2d(i, j));
            }
        }
    }

    public int getHeight() {
        return heightCoordinate + 1;
    }

    public int getWidth() {
        return widthCoordinate + 1;
    }

    public HashMap<Vector2d, IMapElement> getElements() {
        HashMap<Vector2d, IMapElement> elements = new HashMap<>();
        for(Flora flora: this.flora.values()) {
            elements.put(flora.getPosition(), flora);
        }
        for(TreeSet<Animal> animalSet: this.animals.values()) {
            elements.put(animalSet.last().getPosition(), animalSet.last());
        }
        return elements;
    }

    public void restoreFloraArea(Vector2d position) {
        if (position.getY() >= (int) Math.round((2 * (double) heightCoordinate) / 5) && position.getY() < (int)Math.round((3 * (double) heightCoordinate) / 5)) {
            this.moreProbableArea.add(position);
        }
        else {
            this.lessProbableArea.add(position);
        }
    }

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
                this.flora.put(randomField, new Flora(randomField));
            }
        }
    }

    public void removeAnimal(Animal animal) {
        if (this.animals.get(animal.getPosition()).size() == 1) {
            this.animals.remove(animal.getPosition());
        }
        else {
            this.animals.get(animal.getPosition()).remove(animal);
        }
    }

    public void removeFlora(Vector2d position) {
        this.flora.remove(position);
    }

    public Vector2d placeAtRandomPosition() {
        return new Vector2d(RandomNumberGenerator.getRandomNumber(0, this.widthCoordinate),
                            RandomNumberGenerator.getRandomNumber(0, this.heightCoordinate));
    }

    public void placeAnimal(Animal animal) {
        if (this.animals.get(animal.getPosition()) != null) {
            this.animals.get(animal.getPosition()).add(animal);
        }
        else {
            TreeSet<Animal> fieldAnimalList = new TreeSet<>(new AnimalComparator());
            fieldAnimalList.add(animal);
            this.animals.put(animal.getPosition(), fieldAnimalList);
        }
    }

    public TreeSet<Animal> animalsAt(Vector2d position) {
        return this.animals.get(position);
    }

    public Flora floraAt(Vector2d position) {
        return this.flora.get(position);
    }

    public void positionChanged(Animal animal, Vector2d oldPosition) {
        if (this.animals.get(oldPosition).size() == 1) {
            this.animals.remove(oldPosition);
        }
        else {
            this.animals.get(oldPosition).remove(animal);
        }
        this.placeAnimal(animal);
    }

    public void stateChanged(Animal animal) {
        this.removeAnimal(animal);
    }

    public int countFlora() {
        return flora.size();
    }

    public int countFreeFields() {
        return (heightCoordinate+1) * (widthCoordinate+1) - animals.size();
    }

}
