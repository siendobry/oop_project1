package agh.ics.oop_project1;

public interface IObserver {

    void positionChanged(Animal animal, Vector2d oldPosition);

    void stateChanged(Animal animal);

}
