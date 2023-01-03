package agh.ics.oop_project1;

import java.util.Scanner;
public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String filename = input.nextLine();
        ConfigurationManager configmanager = new ConfigurationManager(filename);
        SimulationEngine engine = new SimulationEngine(configmanager);
        engine.run();
    }

}
