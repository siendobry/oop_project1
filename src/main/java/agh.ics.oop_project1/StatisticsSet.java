package agh.ics.oop_project1;

import java.util.ArrayList;

public record StatisticsSet(
        int numberOfAnimals,
        int numberOfFlora,
        int numberOfFreeFields,
        String mostPopularGenotype,
        float averageEnergy,
        float averageLifespan
) {
    @Override
    public String toString() {
        return numberOfAnimals+","+numberOfFlora+","+numberOfFreeFields+","+mostPopularGenotype+","+averageEnergy+","+averageLifespan;
    }
}
