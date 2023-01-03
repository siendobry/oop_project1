package agh.ics.oop_project1;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.TreeSet;

public interface IWorldMap extends IObserver {

    void putFlora(int amount);

    void removeFlora(Vector2d position);

    void moveAnimal(Animal animal);

    void placeAt(Animal animal);

    Vector2d placeAtRandomPosition();

    TreeSet<Animal> animalsAt(Vector2d position);

    Flora floraAt(Vector2d position);

}
