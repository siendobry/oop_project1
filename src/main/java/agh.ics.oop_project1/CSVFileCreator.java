package agh.ics.oop_project1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSVFileCreator {

    private String filename;
    private File file;
    public CSVFileCreator(String filename) {
        this.filename = filename;
        file = new File(filename);
        if(!file.exists()) {
            try {
                FileWriter fr = new FileWriter(filename, false);
                fr.write("numberOfAnimals,numberOfFlora,numberOfFreeFields,mostPopularGenotype,averageEnergy,averageLifespan\n");
                fr.close();
            } catch (IOException exception)
            {
                throw new RuntimeException("Unable to write to file");
            }

        }
    }
    public void exportStatistics(StatisticsSet statistics) {
        File file = new File(filename);
        if(file.exists()) {
            try {
                FileWriter fr = new FileWriter(filename, true);
                fr.write(statistics.toString()+"\n");
                fr.close();
            } catch (IOException exception)
            {
                throw new RuntimeException("Unable to write to file");
            }
        }
    }
}
