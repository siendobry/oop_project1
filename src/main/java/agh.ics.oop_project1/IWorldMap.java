package agh.ics.oop_project1;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface IWorldMap {

    void putFlora();

    void moveAnimal(Animal animal);

    Vector2d placeAtRandomPosition();

    ArrayList<Animal> animalsAt(Vector2d position);

    ArrayList<Flora> floraAt(Vector2d position);

    void growFlora();

}
