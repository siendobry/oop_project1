package main.java.agh.ics.oop_project1;

import java.util.ArrayList;
import java.util.LinkedList;

public class Animal implements IMapElement {

    private Vector2d position;
    private MoveDirection orientation;
    private ArrayList genome;
    private int activeGene;
    private final IWorldMap map;
    private int energy;
    private int eatenCount;
    private int childrenCount;
    private int daysLived;
    private LinkedList<IObserver> observers;

    public Animal(IWorldMap map, int genomeLength, int startingEnergy) {
        this(map, map.placeAtRandomPosition(), genomeLength, startingEnergy);
    }

    public Animal(IWorldMap map, Vector2d position, int genomeLength, int startingEnergy) {
        this.map = map;
        this.position = position;
        this.orientation = orientation.getRandomDirection();
        this.genome = this.generateGenome(genomeLength);
        this.activeGene = RandomNumberGenerator.getRandomNumber(0, genomeLength);
        this.energy = startingEnergy;
        this.eatenCount = 0;
        this.childrenCount = 0;
        this.daysLived = 0;
        this.observers = new LinkedList<>();
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
        this.map.moveAnimal(this);
        this.positionChange();
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

    private void positionChange() {
        this.observers.forEach(observer -> observer.positionChanged());
    }

    private void stateChange() {
        this.observers.forEach(observer -> observer.stateChanged());
    }

}
