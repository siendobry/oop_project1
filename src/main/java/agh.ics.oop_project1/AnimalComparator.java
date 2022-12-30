package agh.ics.oop_project1;

import java.util.Comparator;

public class AnimalComparator implements Comparator<Animal> {
    @Override public int compare(Animal a1, Animal a2) {
        if(a1.getEnergy() < a2.getEnergy())
            return -1;
        else if(a1.getEnergy() == a2.getEnergy()) {
            if(a2.getDateOfBirth() > a2.getDateOfBirth())
                return -1;
            else if(a1.getDateOfBirth() == a2.getDateOfBirth()) {
                if(a1.getChildrenCount() < a2.getChildrenCount())
                    return -1;
                else if (a1.getChildrenCount() == a2.getChildrenCount()) {
                    return a1.getId() - a2.getId();
                }
            }
        }
        return 1;
    }
}
