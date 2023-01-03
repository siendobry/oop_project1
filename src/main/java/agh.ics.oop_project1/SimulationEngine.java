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
    private int currentDay = 0;
    private int energyNeededToBreed;
    private int energyLossOnBreeding;
    private int minMutations;
    private int maxMutations;

    public SimulationEngine(ConfigurationManager configmanager) {
        if(configmanager.getMapVariant().equals("globe")) {
            this.map = new GlobeMap(configmanager.getMapHeight(), configmanager.getMapWidth());
        }
        else if(configmanager.getMapVariant().equals("nether")) {
            this.map = new NetherMap(configmanager.getMapHeight(), configmanager.getMapWidth(), configmanager.getDrainEnergyAmount());
        }

        this.numberOfAnimals = configmanager.getNumberOfAnimals();
        this.floraGrowth = configmanager.getFloraGrowth();
        this.nutritionValue = configmanager.getNutritionValue();
        this.energyNeededToBreed = configmanager.getEnergyNeededToBreed();
        this.energyLossOnBreeding = configmanager.getEnergyLossOnBreeding();
        this.minMutations = configmanager.getMinMutations();
        this.maxMutations = configmanager.getMaxMutations();
        this.lengthOfGenome = configmanager.getLengthOfGenome();

        for(int i = 0; i < numberOfAnimals; i++) {
            Animal animal = new Animal(map, configmanager.getLengthOfGenome(), configmanager.getInitialEnergy(), animalCounter, currentDay);
            animals.add(animal);
            animal.addObserver(this.map);
            map.placeAt(animal);
            animalCounter++;
        }

        this.map.putFlora(configmanager.getStartingFlora());
    }

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
        while(animals.size() > 0) {
            ArrayList<Animal> toBeRemoved = new ArrayList<>();
            for(Animal animal : animals) {
                if(animal.getEnergy() <= 0) {
                    animal.die(this.currentDay);
                    toBeRemoved.add(animal);
                }
            }
            for (Animal animal: toBeRemoved) {
                animals.remove(animal);
            }
            for(Animal animal : animals) {
                animal.move();
            }
            for(Animal animal : animals) {
                animal.consumeFlora(nutritionValue);

            }

            HashSet<Vector2d> processedFields = new HashSet<>();
            ArrayList<Animal> newbornAnimals = new ArrayList<>();

            for(Animal animal : animals) {
                Vector2d position = animal.getPosition();
                TreeSet<Animal> animalsSharingPosition = map.animalsAt(position);

                if(!processedFields.contains(position) && animalsSharingPosition.size() > 1) {
//                    for (Animal a: animalsSharingPosition
//                    ) {
//                        System.out.println(a.getId()+" "+a.getEnergy()+" "+a.getPosition().toString());
//                    }


                    Animal firstAnimal = animalsSharingPosition.pollFirst();
                    Animal secondAnimal = animalsSharingPosition.pollFirst();


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
                            for(; i >= 0; i--) {
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



                        newbornAnimals.add(new Animal(map, position, lengthOfGenome, 2 * energyLossOnBreeding, animalCounter, currentDay, newbornsGenome));
                        animalCounter++;
                        firstAnimal.breed(energyLossOnBreeding);
                        secondAnimal.breed(energyLossOnBreeding);
                        processedFields.add(position);
                    }
                    animalsSharingPosition.add(firstAnimal);
                    animalsSharingPosition.add(secondAnimal);
                }
            }
            animals.addAll(newbornAnimals);

            for(Animal a : newbornAnimals) {
                map.placeAt(a);
                a.addObserver(map);
            }


            map.putFlora(this.floraGrowth);
            this.currentDay++;
            System.out.println(currentDay+" "+animals.size());
            System.out.flush();
        }
    }


}
