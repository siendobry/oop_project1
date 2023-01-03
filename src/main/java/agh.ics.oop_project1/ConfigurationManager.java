package agh.ics.oop_project1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class ConfigurationManager {

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public String getMapVariant() {
        return mapVariant;
    }

    public int getNumberOfAnimals() {
        return numberOfAnimals;
    }

    public int getLengthOfGenome() {
        return lengthOfGenome;
    }

    public int getInitialEnergy() {
        return initialEnergy;
    }

    public int getStartingFlora() {
        return startingFlora;
    }

    public int getFloraGrowth() {
        return floraGrowth;
    }

    public int getNutritionValue() {
        return nutritionValue;
    }

    public int getEnergyNeededToBreed() {
        return energyNeededToBreed;
    }

    public int getEnergyLossOnBreeding() {
        return energyLossOnBreeding;
    }

    public int getMinMutations() {
        return minMutations;
    }

    public int getMaxMutations() {
        return maxMutations;
    }

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

    public int getDrainEnergyAmount() {
        return drainEnergyAmount;
    }

    //optional parameter
    private int drainEnergyAmount = 0;

    public ConfigurationManager(String fileUrl) {
        File file = new File(fileUrl);
        Scanner reader;
        try {
            reader = new Scanner(file);
        }
        catch (FileNotFoundException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }

        Boolean[] proprietyChecker = new Boolean[13];



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
                    proprietyChecker[0] = true;
                }
                case "mapWidth" -> {
                    if (Integer.parseInt(value) < 0 || Integer.parseInt(value) > 96) {
                        throw new IllegalArgumentException("Invalid map width");
                    }
                    this.mapWidth = Integer.parseInt(value) - 1;
                    proprietyChecker[1] = true;
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
                    proprietyChecker[2] = true;
                }
                case "numberOfAnimals" -> {
                    if (Integer.parseInt(value) < 0 || Integer.parseInt(value) > 2 * this.mapHeight * this.mapWidth) {
                        throw new IllegalArgumentException("Invalid number of animals");
                    }
                    this.numberOfAnimals = Integer.parseInt(value);
                    proprietyChecker[3] = true;
                }
                case "lengthOfGenome" -> {
                    if (Integer.parseInt(value) < 0 || Integer.parseInt(value) > this.mapHeight * this.mapWidth) {
                        throw new IllegalArgumentException("Invalid length of genome");
                    }
                    this.lengthOfGenome = Integer.parseInt(value);
                    proprietyChecker[4] = true;
                }
                case "initialEnergy" -> {
                    if (Integer.parseInt(value) < 0 || Integer.parseInt(value) > this.mapHeight * this.mapWidth) {
                        throw new IllegalArgumentException("Invalid initial energy value");
                    }
                    this.initialEnergy = Integer.parseInt(value);
                    proprietyChecker[5] = true;
                }
                case "startingFlora" -> {
                    if (Integer.parseInt(value) < 0 || Integer.parseInt(value) > this.mapHeight * this.mapWidth) {
                        throw new IllegalArgumentException("Invalid length of genome");
                    }
                    this.startingFlora = Integer.parseInt(value);
                    proprietyChecker[6] = true;
                }
                case "floraGrowth" -> {
                    if (Integer.parseInt(value) < 0 || Integer.parseInt(value) > this.mapHeight * this.mapWidth) {
                        throw new IllegalArgumentException("Invalid length of genome");
                    }
                    this.floraGrowth = Integer.parseInt(value);
                    proprietyChecker[7] = true;
                }
                case "nutritionValue" -> {
                    if (Integer.parseInt(value) < 0 || Integer.parseInt(value) > this.mapHeight * this.mapWidth) {
                        throw new IllegalArgumentException("Invalid nutrition value");
                    }
                    this.nutritionValue = Integer.parseInt(value);
                    proprietyChecker[8] = true;
                }
                case "energyNeededToBreed" -> {
                    if(Integer.parseInt(value) < 0) {
                        throw new IllegalArgumentException("Invalid value of energy needed to breed");
                    }
                    this.energyNeededToBreed = Integer.parseInt(value);
                    proprietyChecker[9] = true;
                }

                case "energyLossOnBreeding" -> {
                    if(Integer.parseInt(value) < 0) {
                        throw new IllegalArgumentException("Invalid value of energy loss on breeding");
                    }
                    this.energyLossOnBreeding = Integer.parseInt(value);
                    proprietyChecker[10] = true;
                }

                case "minMutations" -> {
                    if(Integer.parseInt(value) < 0) {
                        throw new IllegalArgumentException("Invalid max value of mutations");
                    }
                    this.minMutations = Integer.parseInt(value);
                    proprietyChecker[11] = true;
                }

                case "maxMutations" -> {
                    if(Integer.parseInt(value) < 0) {
                        throw new IllegalArgumentException("Invalid max value of mutations");
                    }
                    this.maxMutations = Integer.parseInt(value);
                    proprietyChecker[12] = true;
                }

                case "drainEnergyAmount" -> {
                    if (Integer.parseInt(value) < 0 ) {
                        throw new IllegalArgumentException("Invalid drain energy amount");
                    }
                    this.drainEnergyAmount = Integer.parseInt(value);
                }
            }
        }
        Boolean check = true;
        for(Boolean processed : proprietyChecker)
            check = check && processed;
        if(!check) {
            throw new IllegalArgumentException("Configuration lacks some of the fields");
        }
    }

}
