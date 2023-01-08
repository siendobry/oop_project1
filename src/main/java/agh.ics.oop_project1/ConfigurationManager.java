package agh.ics.oop_project1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
    private int energyDrainAmount = 0;
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

    public int getEnergyDrainAmount() {
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
                    if (Integer.parseInt(value) < 0 || Integer.parseInt(value) > 36) {
                        throw new IllegalArgumentException("Invalid map height (has to be greater than 0 and smaller than 37");
                    }
                    this.mapHeight = Integer.parseInt(value);
                    proprietyChecker[0] = true;
                }
                case "mapWidth" -> {
                    if (Integer.parseInt(value) < 0 || Integer.parseInt(value) > 90) {
                        throw new IllegalArgumentException("Invalid map width (has to be greater than 0 and smaller than 91");
                    }
                    this.mapWidth = Integer.parseInt(value);
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
                case "energyDrainAmount" -> {
                    if (Integer.parseInt(value) < 0 || Integer.parseInt(value) > this.mapHeight * this.mapWidth) {
                        throw new IllegalArgumentException("Invalid energy drain amount (has to be greater than 0 and smaller than mapHeight times mapWidth)");
                    }
                    this.numberOfAnimals = Integer.parseInt(value);
                }
                case "numberOfAnimals" -> {
                    if (Integer.parseInt(value) < 0 || Integer.parseInt(value) > 2 * this.mapHeight * this.mapWidth) {
                        throw new IllegalArgumentException("Invalid number of animals (has to be greater than 0 and smaller than mapHeight times mapWidth)");
                    }
                    this.numberOfAnimals = Integer.parseInt(value);
                    proprietyChecker[3] = true;
                }
                case "lengthOfGenome" -> {
                    if (Integer.parseInt(value) < 0 || Integer.parseInt(value) > this.mapHeight * this.mapWidth) {
                        throw new IllegalArgumentException("Invalid length of genom (has to be greater than 0 and smaller than mapHeight times mapWidth)e");
                    }
                    this.lengthOfGenome = Integer.parseInt(value);
                    proprietyChecker[4] = true;
                }
                case "initialEnergy" -> {
                    if (Integer.parseInt(value) < 0 || Integer.parseInt(value) > this.mapHeight * this.mapWidth) {
                        throw new IllegalArgumentException("Invalid initial energy value (has to be greater than 0 and smaller than mapHeight times mapWidth)");
                    }
                    this.initialEnergy = Integer.parseInt(value);
                    proprietyChecker[5] = true;
                }
                case "startingFlora" -> {
                    if (Integer.parseInt(value) < 0 || Integer.parseInt(value) > this.mapHeight * this.mapWidth) {
                        throw new IllegalArgumentException("Invalid length of genome (has to be greater than 0 and smaller than mapHeight times mapWidth)");
                    }
                    this.startingFlora = Integer.parseInt(value);
                    proprietyChecker[6] = true;
                }
                case "floraGrowth" -> {
                    if (Integer.parseInt(value) < 0 || Integer.parseInt(value) > this.mapHeight * this.mapWidth) {
                        throw new IllegalArgumentException("Invalid length of genome (has to be greater than 0 and smaller than mapHeight times mapWidth)");
                    }
                    this.floraGrowth = Integer.parseInt(value);
                    proprietyChecker[7] = true;
                }
                case "nutritionValue" -> {
                    if (Integer.parseInt(value) < 0 || Integer.parseInt(value) > this.mapHeight * this.mapWidth) {
                        throw new IllegalArgumentException("Invalid nutrition value (has to be greater than 0 and smaller than mapHeight times mapWidth)");
                    }
                    this.nutritionValue = Integer.parseInt(value);
                    proprietyChecker[8] = true;
                }
                case "energyNeededToBreed" -> {
                    if(Integer.parseInt(value) < 0) {
                        throw new IllegalArgumentException("Invalid value of energy needed to breed (has to be greater than 0)");
                    }
                    this.energyNeededToBreed = Integer.parseInt(value);
                    proprietyChecker[9] = true;
                }

                case "energyLossOnBreeding" -> {
                    if(Integer.parseInt(value) < 0) {
                        throw new IllegalArgumentException("Invalid value of energy loss on breeding (has to be greater than 0)");
                    }
                    this.energyLossOnBreeding = Integer.parseInt(value);
                    proprietyChecker[10] = true;
                }

                case "minMutations" -> {
                    if(Integer.parseInt(value) < 0) {
                        throw new IllegalArgumentException("Invalid max value of mutations (has to be greater than 0)");
                    }
                    this.minMutations = Integer.parseInt(value);
                    proprietyChecker[11] = true;
                }

                case "maxMutations" -> {
                    if(Integer.parseInt(value) < 0) {
                        throw new IllegalArgumentException("Invalid max value of mutations (has to be greater than 0)");
                    }
                    this.maxMutations = Integer.parseInt(value);
                    proprietyChecker[12] = true;
                }

                case "drainEnergyAmount" -> {
                    if (Integer.parseInt(value) < 0 ) {
                        throw new IllegalArgumentException("Invalid drain energy amount (has to be greater than 0)");
                    }
                    this.drainEnergyAmount = Integer.parseInt(value);
                }
            }
        }
        boolean check = true;
        for(Boolean processed : proprietyChecker)
            check = check && processed;
        if(!check) {
            throw new IllegalArgumentException("Configuration lacks some of the fields");
        }
    }

    public static void saveConfig(String config, String fileName) throws IOException {
        File file = new File("configs/" + fileName + ".cfg");
        if (!file.exists()) {
            FileWriter writer = new FileWriter("configs/" + fileName + ".cfg", false);
            writer.write(config);
            writer.close();
        }
        else {
            throw new IOException("File with this name already exists!");
        }
    }

}
