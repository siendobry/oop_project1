package agh.ics.oop_project1;

import java.util.ArrayList;
import java.util.TreeSet;

public class SimulationEngine implements Runnable {

    private IWorldMap map;
    private int numberOfAnimals;
    private final ArrayList<Animal> animals = new ArrayList<>();
    private int lengthOfGenome;
    private int initialEnergy;
    private int animalCounter = 0;
    private final int floraGrowth;
    private int nutritionValue;
    private int currentDay;


    public SimulationEngine(IWorldMap map, int numberOfAnimals, int initialEnergy, int startingFlora, int floraGrowth, int nutritionValue) {
        this.map = map;
        this.numberOfAnimals = numberOfAnimals;
        this.currentDay = 0;
        for(int i = 0; i < numberOfAnimals; i++) {
            animals.add(new Animal(map, lengthOfGenome, initialEnergy, animalCounter, currentDay));
            animalCounter++;
        }
        this.map.putFlora(startingFlora);
        this.floraGrowth = floraGrowth;
        this.nutritionValue = nutritionValue;
    }


    public void run() {
        for(Animal animal : animals) {
            if(animal.getEnergy() <= 0) {
                animal.die(this.currentDay);
                animals.remove(animal);
            }
        }
        for(Animal animal : animals) {
            animal.move();
        }
        for(Animal animal : animals) {
            animal.consumeFlora(nutritionValue);

        }
        for(Animal animal : animals) {
            animal.breed();
        }
        map.putFlora(this.floraGrowth);
        this.currentDay++;
    }

}
