package agh.ics.oop_project1;

import java.util.Comparator;

public class AnimalComparator implements Comparator<Animal> {
    @Override public int compare(Animal a1, Animal a2) {
        if(a1.getEnergy() < a2.getEnergy())
            return -1;
        else if(a1.getEnergy() == a2.getEnergy()) {
            if(a2.getDaysLived() < a2.getDaysLived())
                return -1;
            else if(a1.getDaysLived() == a2.getDaysLived()) {
                if(a1.getChildrenCount() < a2.getChildrenCount())
                    return -1;
            }
        }
        return 1;
    }
}
