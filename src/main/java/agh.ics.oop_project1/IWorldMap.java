package agh.ics.oop_project1;

import java.util.HashMap;
import java.util.TreeSet;

public interface IWorldMap extends IObserver {

    int getHeight();

    int getWidth();

    HashMap<Vector2d, IMapElement> getElements();

    void restoreFloraArea(Vector2d position);

    void putFlora(int amount);

    void removeFlora(Vector2d position);

    void moveAnimal(Animal animal);

    void placeAnimal(Animal animal);

    Vector2d placeAtRandomPosition();

    TreeSet<Animal> animalsAt(Vector2d position);

    Flora floraAt(Vector2d position);

    int countFlora();

    int countFreeFields();

}
