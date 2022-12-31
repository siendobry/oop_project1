package agh.ics.oop_project1;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Vector;

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
    private int energyNeededToBreed;
    private int energyLossOnBreeding;
    private int minMutations;
    private int maxMutations;


    public SimulationEngine(IWorldMap map, int numberOfAnimals, int initialEnergy, int startingFlora, int floraGrowth, int nutritionValue, int energyNeededToBreed, int energyLossOnBreeding, int minMutations, int maxMutations) {
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
        this.energyNeededToBreed = energyNeededToBreed;
        this.energyLossOnBreeding = energyLossOnBreeding;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
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

        HashSet<Vector2d> processedFields = new HashSet<>();

        for(Animal animal : animals) {
            Vector2d position = animal.getPosition();
            TreeSet<Animal> animalsSharingPosition = map.animalsAt(position);

            if(!processedFields.contains(position) && animalsSharingPosition.size() > 1) {
                Animal firstAnimal = animalsSharingPosition.first();
                Animal secondAnimal = animalsSharingPosition.lower(firstAnimal);

                if(firstAnimal.getEnergy() > energyNeededToBreed && secondAnimal.getEnergy() > energyNeededToBreed) {
                    ArrayList<Integer> firstGenome = firstAnimal.getGenome();
                    ArrayList<Integer> secondGenome = secondAnimal.getGenome();

                    ArrayList<Integer> newbornsGenome = new ArrayList<>();

                    if(RandomNumberGenerator.getRandomNumber(0,2) == 1) {
                        int i = 0;
                        for(; i < Math.round(firstAnimal.getEnergy() / (firstAnimal.getEnergy() + secondAnimal.getEnergy())*lengthOfGenome); i++) {
                            newbornsGenome.add(firstGenome.get(i));
                        }
                        for(; i < lengthOfGenome; i++) {
                            newbornsGenome.add(secondGenome.get(i));
                        }
                    }
                    else {
                        int i = lengthOfGenome - 1;
                        for(; i > Math.round(firstAnimal.getEnergy() / (firstAnimal.getEnergy() + secondAnimal.getEnergy())*lengthOfGenome); i--) {
                            newbornsGenome.add(firstGenome.get(i));
                        }
                        for(; i >= 0; i++) {
                            newbornsGenome.add(secondGenome.get(i));
                        }
                    }

                    //mutations - to discuss
                    ArrayList<Integer> genes = new ArrayList<>();
                    for (int i = 0; i < lengthOfGenome; i++) {
                        genes.add(i);
                    }
                    int numberOfMutations = RandomNumberGenerator.getRandomNumber(this.minMutations, this.maxMutations + 1);
                    for(int i = 0; i < numberOfMutations; i++) {
                        int geneToMutate = genes.remove(RandomNumberGenerator.getRandomNumber(0, genes.size()));
                        int randomNumber = RandomNumberGenerator.getRandomNumber(0,2);
                        if(randomNumber == 0)
                            newbornsGenome.set(geneToMutate, (newbornsGenome.get(geneToMutate) + 9) % 8);
                        else if(randomNumber == 1)
                            newbornsGenome.set(geneToMutate, (newbornsGenome.get(geneToMutate) + 7) % 8);
                    }

                    animals.add(new Animal(map, position, lengthOfGenome, 2 * energyLossOnBreeding, numberOfAnimals, currentDay, newbornsGenome));
                    animalCounter++;
                    firstAnimal.breed(energyLossOnBreeding);
                    secondAnimal.breed(energyLossOnBreeding);
                    processedFields.add(position);
                }
            }
        }

        map.putFlora(this.floraGrowth);
        this.currentDay++;
    }

}
