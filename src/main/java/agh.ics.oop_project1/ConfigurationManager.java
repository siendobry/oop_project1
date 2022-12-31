package agh.ics.oop_project1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class ConfigurationManager {

    private int mapHeight;
    private int mapWidth;
    private String mapVariant;
    private int numberOfAnimals;
    private int lengthOfGenome;
    private int initialEnergy;
    private int startingFlora;
    private int floraGrowth;
    private int nutritionValue;
    private int energyNeededToBreed;
    private int energyLossOnBreeding;
    private int minMutations;
    private int maxMutations;

    public ConfigurationManager(String fileUrl) {
        File file = new File(fileUrl);
        Scanner reader;
        try {
            reader = new Scanner(file);
        }
        catch (FileNotFoundException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
        while (reader.hasNextLine()) {
            String[] arg = reader.nextLine().split(":");
            String propertyName = arg[0];
            String value = arg[1];
            switch (propertyName) {
                case "mapHeight" -> {
                    if (Integer.parseInt(value) < 0 || Integer.parseInt(value) > 54) {
                        throw new IllegalArgumentException("Invalid map height");
                    }
                    this.mapHeight = Integer.parseInt(value) - 1;
                }
                case "mapWidth" -> {
                    if (Integer.parseInt(value) < 0 || Integer.parseInt(value) > 96) {
                        throw new IllegalArgumentException("Invalid map width");
                    }
                    this.mapWidth = Integer.parseInt(value) - 1;
                }
                case "mapVariant" -> {
                    if (Objects.equals(value, "globe")) {
                        this.mapVariant = "globe";
                    }
                    else if (Objects.equals(value, "nether")) {
                        this.mapVariant = "nether";
                    }
                    else {
                        throw new IllegalArgumentException("Invalid map variant");
                    }
                }
                case "numberOfAnimals" -> {
                    if (Integer.parseInt(value) < 0 || Integer.parseInt(value) > 2 * this.mapHeight * this.mapWidth) {
                        throw new IllegalArgumentException("Invalid number of animals");
                    }
                    this.numberOfAnimals = Integer.parseInt(value);
                }
                case "lengthOfGenome" -> {
                    if (Integer.parseInt(value) < 0 || Integer.parseInt(value) > this.mapHeight * this.mapWidth) {
                        throw new IllegalArgumentException("Invalid length of genome");
                    }
                    this.lengthOfGenome = Integer.parseInt(value);
                }
                case "initialEnergy" -> {
                    if (Integer.parseInt(value) < 0 || Integer.parseInt(value) > this.mapHeight * this.mapWidth) {
                        throw new IllegalArgumentException("Invalid initial energy value");
                    }
                    this.initialEnergy = Integer.parseInt(value);
                }
                case "startingFlora" -> {
                    if (Integer.parseInt(value) < 0 || Integer.parseInt(value) > this.mapHeight * this.mapWidth) {
                        throw new IllegalArgumentException("Invalid length of genome");
                    }
                    this.startingFlora = Integer.parseInt(value);
                }
                case "floraGrowth" -> {
                    if (Integer.parseInt(value) < 0 || Integer.parseInt(value) > this.mapHeight * this.mapWidth) {
                        throw new IllegalArgumentException("Invalid length of genome");
                    }
                    this.floraGrowth = Integer.parseInt(value);
                }
                case "nutritionValue" -> {
                    if (Integer.parseInt(value) < 0 || Integer.parseInt(value) > this.mapHeight * this.mapWidth) {
                        throw new IllegalArgumentException("Invalid nutrition value");
                    }
                    this.nutritionValue = Integer.parseInt(value);
                }
            }
        }
    }

}
