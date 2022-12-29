package agh.ics.oop_project1;

import java.util.ArrayList;
import java.util.TreeSet;

public class SimulationEngine implements Runnable {

    private IWorldMap map;
    private int numberOfAnimals;
    private ArrayList<Animal> animals;
    private int lengthOfGenome;
    private int initialEnergy;


    public SimulationEngine(IWorldMap map, int numberOfAnimals, int initialEnergy) {
        this.map = map;
        this.numberOfAnimals = numberOfAnimals;
        map.putFlora();
        for(int i = 0; i < numberOfAnimals; i++) {
            animals.add(new Animal(map, lengthOfGenome, initialEnergy));
        }
    }


    public void run() {
        for(Animal animal : animals) {
            if(animal.getEnergy() <= 0) {
                animal.die();
                animals.remove(animal);
            }
        }
        for(Animal animal : animals) {
            animal.move();
        }
        TreeSet<Vector2d> fieldsAlreadyConsumed = new TreeSet<>();
        for(Animal animal : animals) {
            animal.consumeFlora(fieldsAlreadyConsumed);

        }
        for(Animal animal : animals) {
            animal.breed();
        }
        map.growFlora();
    }

}
