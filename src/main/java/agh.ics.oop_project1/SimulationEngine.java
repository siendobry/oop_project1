package agh.ics.oop_project1;

import java.util.*;
import java.util.stream.Collectors;

public class SimulationEngine implements Runnable, IDayChangeObserver {

    private IWorldMap map;
    private int numberOfAnimals;
    private final ArrayList<Animal> animals = new ArrayList<>();
    private HashMap<String,Integer> mostPopularGenotypes = new HashMap<>();
    private int lengthOfGenome;
    private int animalCounter = 0;
    private final int floraGrowth;
    private int nutritionValue;
    private int currentDay = 0;
    private int energyNeededToBreed;
    private int energyLossOnBreeding;
    private int minMutations;
    private int maxMutations;
    private long daysLivedByDead = 0;
    private long deadCount = 0;
    private final ArrayList<IDayChangeObserver> dayChangeObservers = new ArrayList<>();

    public SimulationEngine(ConfigurationManager configmanager) {
        if(configmanager.getMapVariant().equals("globe")) {
            this.map = new GlobeMap(configmanager.getMapHeight(), configmanager.getMapWidth());
        }
        else if(configmanager.getMapVariant().equals("nether")) {
            this.map = new NetherMap(configmanager.getMapHeight(), configmanager.getMapWidth(), configmanager.getEnergyDrainAmount());
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
            map.placeAnimal(animal);
            animalCounter++;
            String genomeString = animal.getGenome().stream().map(Object::toString)
                    .collect(Collectors.joining());
            if(!mostPopularGenotypes.containsKey(genomeString)) {
                mostPopularGenotypes.put(genomeString,1);
            }
            else {
                mostPopularGenotypes.put(genomeString, mostPopularGenotypes.remove(genomeString)+1);
            }
        }

        this.map.putFlora(configmanager.getStartingFlora());
    }

    public SimulationEngine(IWorldMap map, int numberOfAnimals, int initialEnergy, int startingFlora, int floraGrowth, int nutritionValue, int energyNeededToBreed, int energyLossOnBreeding, int minMutations, int maxMutations, int lengthOfGenome) {
        this.map = map;
        this.numberOfAnimals = numberOfAnimals;
        this.lengthOfGenome = lengthOfGenome;
        for(int i = 0; i < numberOfAnimals; i++) {
            Animal animal = new Animal(map, lengthOfGenome, initialEnergy, animalCounter, currentDay);
            animals.add(animal);
            animal.addObserver(this.map);
            map.placeAnimal(animal);
            animalCounter++;
            String genomeString = animal.getGenome().stream().map(Object::toString)
                    .collect(Collectors.joining());
            if(!mostPopularGenotypes.containsKey(genomeString)) {
                mostPopularGenotypes.put(genomeString,1);
            }
            else {
                mostPopularGenotypes.put(genomeString, mostPopularGenotypes.remove(genomeString)+1);
            }
        }
        this.map.putFlora(startingFlora);
        this.floraGrowth = floraGrowth;
        this.nutritionValue = nutritionValue;
        this.energyNeededToBreed = energyNeededToBreed;
        this.energyLossOnBreeding = energyLossOnBreeding;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
    }

    public List<Animal> getAnimals() {
        return Collections.unmodifiableList(this.animals);
    }

    public StatisticsSet returnStatistics() {
        int animalsAlive = animals.size();
        int floraCount = map.countFlora();
        int numberOfFreeFields = map.countFreeFields();
        float averageEnergy = 0;
        for(Animal a : animals) {
            averageEnergy+=(float)a.getEnergy();
        }
        averageEnergy/=(float)animalsAlive;
        String mostPopularGenotype = "";
        int countMostPopularGenotype = 0;
        for(String genotype : mostPopularGenotypes.keySet()) {
            if(mostPopularGenotypes.get(genotype) > countMostPopularGenotype) {
                countMostPopularGenotype = mostPopularGenotypes.get(genotype);
                mostPopularGenotype = genotype;
            }
        }
        float averageDaysLived = (float) daysLivedByDead / (float) deadCount;

        return new StatisticsSet(animalsAlive, floraCount, numberOfFreeFields, mostPopularGenotype, averageEnergy, averageDaysLived);
    }

    public void run() {
        try {
            while (animals.size() > 0) {
                ArrayList<Animal> toBeRemoved = new ArrayList<>();
                for (Animal animal : animals) {
                    if (animal.getEnergy() <= 0) {
                        animal.die(this.currentDay);
                        toBeRemoved.add(animal);
                        deadCount++;
                        daysLivedByDead += (currentDay - animal.getDateOfBirth());
                    }
                }
                for (Animal animal : toBeRemoved) {
                    animals.remove(animal);
                    Thread.sleep(50);
                }
                for (Animal animal : animals) {
                    animal.move();
                    Thread.sleep(50);
                }
                for (Animal animal : animals) {
                    animal.consumeFlora(nutritionValue);
                }

                HashSet<Vector2d> processedFields = new HashSet<>();
                ArrayList<Animal> newbornAnimals = new ArrayList<>();

                for (Animal animal : animals) {
                    Vector2d position = animal.getPosition();
                    TreeSet<Animal> animalsSharingPosition = map.animalsAt(position);

                    if (!processedFields.contains(position) && animalsSharingPosition.size() > 1) {

                        Animal firstAnimal = animalsSharingPosition.pollFirst();
                        Animal secondAnimal = animalsSharingPosition.pollFirst();

                        if (firstAnimal.getEnergy() > energyNeededToBreed && secondAnimal.getEnergy() > energyNeededToBreed) {
                            ArrayList<Integer> firstGenome = firstAnimal.getGenome();
                            ArrayList<Integer> secondGenome = secondAnimal.getGenome();

                            ArrayList<Integer> newbornsGenome = new ArrayList<>();

                            if (RandomNumberGenerator.getRandomNumber(0, 2) == 1) {
                                int i = 0;
                                for (; i < Math.round(firstAnimal.getEnergy() / (firstAnimal.getEnergy() + secondAnimal.getEnergy()) * lengthOfGenome); i++) {
                                    newbornsGenome.add(firstGenome.get(i));
                                }
                                for (; i < lengthOfGenome; i++) {
                                    newbornsGenome.add(secondGenome.get(i));
                                }
                            } else {
                                int i = lengthOfGenome - 1;
                                for (; i > Math.round(firstAnimal.getEnergy() / (firstAnimal.getEnergy() + secondAnimal.getEnergy()) * lengthOfGenome); i--) {
                                    newbornsGenome.add(firstGenome.get(i));
                                }
                                for (; i >= 0; i--) {
                                    newbornsGenome.add(secondGenome.get(i));
                                }
                            }

                            //mutations - to discuss
                            ArrayList<Integer> genes = new ArrayList<>();
                            for (int i = 0; i < lengthOfGenome; i++) {
                                genes.add(i);
                            }
                            int numberOfMutations = RandomNumberGenerator.getRandomNumber(this.minMutations, this.maxMutations + 1);
                            for (int i = 0; i < numberOfMutations; i++) {
                                int geneToMutate = genes.remove(RandomNumberGenerator.getRandomNumber(0, genes.size()));
                                int randomNumber = RandomNumberGenerator.getRandomNumber(0, 2);
                                if (randomNumber == 0)
                                    newbornsGenome.set(geneToMutate, (newbornsGenome.get(geneToMutate) + 9) % 8);
                                else if (randomNumber == 1)
                                    newbornsGenome.set(geneToMutate, (newbornsGenome.get(geneToMutate) + 7) % 8);
                            }


                            newbornAnimals.add(new Animal(map, position, lengthOfGenome, 2 * energyLossOnBreeding, animalCounter, currentDay, newbornsGenome));
                            animalCounter++;
                            String genomeString = animal.getGenome().stream().map(Object::toString)
                                    .collect(Collectors.joining());
                            if(!mostPopularGenotypes.containsKey(genomeString)) {
                                mostPopularGenotypes.put(genomeString,1);
                            }
                            else {
                                mostPopularGenotypes.put(genomeString, mostPopularGenotypes.remove(genomeString)+1);
                            }
                            firstAnimal.breed(energyLossOnBreeding);
                            secondAnimal.breed(energyLossOnBreeding);
                            processedFields.add(position);
                        }
                        animalsSharingPosition.add(firstAnimal);
                        animalsSharingPosition.add(secondAnimal);
                    }
                }
                animals.addAll(newbornAnimals);

                for (Animal a : newbornAnimals) {
                    map.placeAnimal(a);
                    a.addObserver(map);
                }


                map.putFlora(this.floraGrowth);
                this.currentDay++;
                this.dayChanged();
            }
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void dayChanged() {
        for(IDayChangeObserver observer: this.dayChangeObservers) {
            observer.dayChanged();
        }
    }

    public void addDayChangeObserver(IDayChangeObserver observer) {
        this.dayChangeObservers.add(observer);
    }

}
