package agh.ics.oop_project1;

public class GlobeMap extends AbstractWorldMap {

    public GlobeMap(int height, int width) {
        super(height, width);
    }

    public void moveAnimal(Animal animal) {
        animal.setPosition(animal.getPosition().add(animal.getOrientation().toUnitVector()));
        if (animal.getPosition().getY() < 0 || animal.getPosition().getY() > this.heightCoordinate) {
            animal.setOrientation(animal.getOrientation().rotate(4));
            animal.setPosition(animal.getPosition().add(animal.getOrientation().toUnitVector()));
        }
        if (animal.getPosition().getX() < 0) {
            animal.setPosition(new Vector2d(this.widthCoordinate, animal.getPosition().getY()));
        }
        if (animal.getPosition().getX() > this.widthCoordinate) {
            animal.setPosition(new Vector2d(0, animal.getPosition().getY()));
        }
    }



}
