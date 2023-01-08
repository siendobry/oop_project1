package agh.ics.oop_project1;

public class Flora implements IMapElement {

    private Vector2d position;

    public Flora(Vector2d position) {
        this.position = position;
    }

    @Override
    public String getImageUrl() {
        return null;
    }

    public String toString() {
        return "*";
    }

    public Vector2d getPosition() {
        return position;
    }
}
