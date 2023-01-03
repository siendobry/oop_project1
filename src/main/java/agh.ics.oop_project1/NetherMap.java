package agh.ics.oop_project1;

public class NetherMap extends AbstractWorldMap {

    // it is a parameter only when user chooses NetherMap variant
    private final int drainEnergyAmount;

    public NetherMap(int height, int width, int drainEnergyAmount) {
        super(height, width);
        this.drainEnergyAmount = drainEnergyAmount;
    }

    public void moveAnimal(Animal animal) {
        animal.setPosition(animal.getPosition().add(animal.getOrientation().toUnitVector()));
        if (animal.getPosition().getY() < 0 || animal.getPosition().getY() > this.heightCoordinate
                || animal.getPosition().getX() < 0 || animal.getPosition().getX() > this.widthCoordinate) {
            animal.setEnergy(animal.getEnergy() - drainEnergyAmount);
            animal.setPosition(this.placeAtRandomPosition());
        }
    }

}
