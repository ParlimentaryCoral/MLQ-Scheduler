package com.example.mlqschedulingso;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    public static void main(String[] args) {
        String[] inputFiles = {"mlq001.txt", "mlq002.txt", "mlq003.txt"};

        for (String inputFile : inputFiles) {
            try {
                MLQScheduler scheduler = new MLQScheduler();

                String outputFile = inputFile.replace(".txt", "_output.txt");

                scheduler.readInputFile(inputFile);
                scheduler.schedule();
                scheduler.displayResults();
                scheduler.writeOutputFile(outputFile);

                System.out.println("\nArchivo procesado: " + inputFile);
                System.out.println("Resultado guardado en: " + outputFile);
                System.out.println("--------------------------------------------------");
            } catch (Exception e) {
                System.err.println("Error al procesar " + inputFile);
                e.printStackTrace();
            }
        }
    }
}