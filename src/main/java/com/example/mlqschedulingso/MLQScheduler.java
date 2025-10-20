package com.example.mlqschedulingso;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MLQScheduler {
    private final List<Process> allProcesses = new ArrayList<>();
    private final ProcessQueue queue1; // RR(3)
    private final ProcessQueue queue2; // RR(5)
    private final ProcessQueue queue3; // FCFS
    private int currentTime;

    public MLQScheduler() {
        this.queue1 = new ProcessQueue(1, "RR", 3);
        this.queue2 = new ProcessQueue(2, "RR", 5);
        this.queue3 = new ProcessQueue(3, "FCFS", 0);
        this.currentTime = 0;
    }

    public void readInputFile(String filename) throws Exception {
        InputStream input = MLQScheduler.class.getClassLoader().getResourceAsStream(filename);
        if (input == null) throw new FileNotFoundException("Archivo no encontrado en resources: " + filename);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split(";");
                if (parts.length < 5) continue;

                String label = parts[0].trim();
                int bt = Integer.parseInt(parts[1].trim());
                int at = Integer.parseInt(parts[2].trim());
                int q = Integer.parseInt(parts[3].trim());
                int pr = Integer.parseInt(parts[4].trim());

                allProcesses.add(new Process(label, bt, at, q, pr));
            }
        }
    }

    public void schedule() {
        // Encolar procesos que llegan en t=0
        for (Process p : allProcesses) {
            if (p.arrivalTime == 0) {
                addToQueue(p);
                p.hasArrived = true;
            }
        }

        while (!queue1.isEmpty() || !queue2.isEmpty() || !queue3.isEmpty()) {
            // Encolar procesos que acaban de llegar
            for (Process p : allProcesses) {
                if (!p.hasArrived && p.arrivalTime <= currentTime) {
                    addToQueue(p);
                    p.hasArrived = true;
                }
            }

            Process current = null;
            ProcessQueue activeQueue = null;

            if (!queue1.isEmpty()) {
                current = queue1.getNextProcess();
                activeQueue = queue1;
            } else if (!queue2.isEmpty()) {
                current = queue2.getNextProcess();
                activeQueue = queue2;
            } else if (!queue3.isEmpty()) {
                current = queue3.getNextProcess();
                activeQueue = queue3;
            }

            if (current == null) break;
            executeProcess(current, activeQueue);
        }
    }

    private void addToQueue(Process p) {
        if (p.queueNumber == 1) {
            queue1.addProcess(p);
        } else if (p.queueNumber == 2) {
            queue2.addProcess(p);
        } else {
            queue3.addProcess(p);
        }
    }

    private void executeProcess(Process p, ProcessQueue q) {
        if (!p.hasStarted) {
            p.responseTime = currentTime - p.arrivalTime;
            p.hasStarted = true;
        }

        int execTime = ("RR".equals(q.getAlgorithm()))
                ? Math.min(q.getTimeQuantum(), p.remainingTime)
                : p.remainingTime;

        currentTime += execTime;
        p.remainingTime -= execTime;

        if (p.remainingTime == 0) {
            p.completionTime = currentTime;
            p.calculateMetrics();
            q.removeProcess(p);
        } else if ("RR".equals(q.getAlgorithm())) {
            q.moveToBack(p);
        }
    }

    public void writeOutputFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("# archivo: " + filename);
            writer.newLine();
            writer.write("# etiqueta; BT; AT; Q; Pr; WT; CT; RT; TAT");
            writer.newLine();

            double totalWT = 0, totalCT = 0, totalRT = 0, totalTAT = 0;

            for (Process p : allProcesses) {
                writer.write(String.format("%s;%d; %d; %d; %d; %d; %d; %d; %d",
                        p.label, p.burstTime, p.arrivalTime, p.queueNumber, p.priority,
                        p.waitingTime, p.completionTime, p.responseTime, p.turnaroundTime));
                writer.newLine();

                totalWT += p.waitingTime;
                totalCT += p.completionTime;
                totalRT += p.responseTime;
                totalTAT += p.turnaroundTime;
            }

            int n = allProcesses.size();
            writer.write(String.format("WT=%.1f; CT=%.1f; RT=%.1f; TAT=%.1f;",
                    totalWT / n, totalCT / n, totalRT / n, totalTAT / n));
            writer.newLine();
        }
    }

    public void displayResults() {
        System.out.println("\n=== Execution Results ===");
        System.out.println("Label\tBT\tAT\tQ\tPr\tWT\tCT\tRT\tTAT");
        for (Process p : allProcesses) {
            System.out.println(p.label + "\t" +
                    p.burstTime + "\t" +
                    p.arrivalTime + "\t" +
                    p.queueNumber + "\t" +
                    p.priority + "\t" +
                    p.waitingTime + "\t" +
                    p.completionTime + "\t" +
                    p.responseTime + "\t" +
                    p.turnaroundTime);
        }
    }
}