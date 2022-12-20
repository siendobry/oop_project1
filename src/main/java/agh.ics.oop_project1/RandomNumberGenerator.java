package main.java.agh.ics.oop_project1;

import java.util.Random;

public class RandomNumberGenerator {

    private static final Random rand = new Random();

    public static int getRandomNumber(int lowerBound, int upperBound) {
        return rand.nextInt(upperBound - lowerBound) + lowerBound;
    }

}
