package agh.ics.oop_project1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;

public class Animal implements IMapElement {

    private Vector2d position;
    private MoveDirection orientation;
    private ArrayList<Integer> genome;
    private int activeGene;
    private final IWorldMap map;
    private int energy;
    private int eatenCount;
    private int childrenCount;
    private int dateOfBirth;
    private int deceaseDate;
    private LinkedList<IObserver> observers;
    private int id;

    public int getDateOfBirth() {
        return this.dateOfBirth;
    }
    public int getChildrenCount() {
        return childrenCount;
    }

    public Animal(IWorldMap map, int genomeLength, int startingEnergy, int id, int currentDay) {
        this(map, map.placeAtRandomPosition(), genomeLength, startingEnergy, id, currentDay);
    }

    public Animal(IWorldMap map, Vector2d position, int genomeLength, int startingEnergy, int id, int currentDay) {
        this.map = map;
        this.position = position;
        this.orientation = MoveDirection.getRandomDirection();
        this.genome = this.generateGenome(genomeLength);
        this.activeGene = RandomNumberGenerator.getRandomNumber(0, genomeLength);
        this.energy = startingEnergy;
        this.eatenCount = 0;
        this.childrenCount = 0;
        this.dateOfBirth = currentDay;
        this.observers = new LinkedList<>();
        this.id = id;
    }
    public Animal(IWorldMap map, Vector2d position, int genomeLength, int startingEnergy, int id, int currentDay, ArrayList<Integer> genome) {
        this.map = map;
        this.position = position;
        this.orientation = orientation.getRandomDirection();
        this.genome = genome;
        this.activeGene = RandomNumberGenerator.getRandomNumber(0, genomeLength);
        this.energy = startingEnergy;
        this.eatenCount = 0;
        this.childrenCount = 0;
        this.dateOfBirth = currentDay;
        this.observers = new LinkedList<>();
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public MoveDirection getOrientation() {
        return this.orientation;
    }

    public void setOrientation(MoveDirection orientation) {
        this.orientation = orientation;
    }

    public int getEnergy() {
        return this.energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    private ArrayList<Integer> generateGenome(int length) {
        ArrayList<Integer> genome = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            genome.add(RandomNumberGenerator.getRandomNumber(0, 8));
        }
        return genome;
    }

    // move method passes current animal's position added to animal orientation's vector
    // then receives proper position after executed movement action and updates animal's position
    public void move() {
        Vector2d oldPosition = this.getPosition();
        this.map.moveAnimal(this);
        this.positionChange(oldPosition);
        int rngRes = RandomNumberGenerator.getRandomNumber(0, 4);
        if (rngRes == 0) {
            this.activeGene = RandomNumberGenerator.getRandomNumber(0, this.genome.size());
        }
        else {
            this.activeGene = (this.activeGene + 1) % this.genome.size();
        }
        this.orientation.rotate(this.genome.get(this.activeGene));
        this.energy--;
    }

    @Override
    public String getImageUrl() {
        return null;
    }

    public void addObserver(IObserver observer) {
        this.observers.add(observer);
    }

    public void removeObserver(IObserver observer) {
        this.observers.remove(observer);
    }

    private void positionChange(Vector2d oldPosition) {
        this.observers.forEach(observer -> observer.positionChanged(this, oldPosition));
    }

    private void stateChange() {
        this.observers.forEach(IObserver::stateChanged);
    }

    public void die(int currentDay) {
        this.deceaseDate = currentDay;
        this.stateChange();
    }

    public void consumeFlora(int nutritionValue) {
        if(this.map.floraAt(this.getPosition()) != null) {
            Animal animalToConsume = map.animalsAt(this.position).first();
            animalToConsume.setEnergy(animalToConsume.getEnergy() + nutritionValue);
            this.map.removeFlora(animalToConsume.getPosition());
        }
    }

    //this method does not create a new animal, nor is it applied to two of the animals - it modifies stats of a parent
    public void breed(int energyConsumed) {
        energy = energy - energyConsumed;
        childrenCount++;
    }

    public ArrayList<Integer> getGenome() {
        return genome;
    }

}
