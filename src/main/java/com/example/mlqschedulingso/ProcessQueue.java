package com.example.mlqschedulingso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProcessQueue {
    private final List<Process> processes = new ArrayList<>();
    private final int queueLevel;
    private final String algorithm;    // "RR", "FCFS", "SJF", "STCF"
    private final int timeQuantum;     // para el RR

    public ProcessQueue(int level, String algo, int quantum) {
        this.queueLevel = level;
        this.algorithm = algo;
        this.timeQuantum = quantum;
    }

    public void addProcess(Process p) {
        processes.add(p);
        // Orden según política
        if ("SJF".equals(algorithm)) {
            processes.sort(Comparator.comparingInt(a -> a.burstTime));
        } else if ("STCF".equals(algorithm)) {
            processes.sort(Comparator.comparingInt(a -> a.remainingTime));
        }
        // FCFS y RR: FIFO (sin ordenar)
    }

    public boolean isEmpty() {
        return processes.isEmpty();
    }

    public Process getNextProcess() {
        if (processes.isEmpty()) return null;
        if ("STCF".equals(algorithm)) {
            processes.sort(Comparator.comparingInt(a -> a.remainingTime));
        }
        return processes.get(0);
    }

    public void removeProcess(Process p) {
        processes.remove(p);
    }

    public int getTimeQuantum() {
        return timeQuantum;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void moveToBack(Process p) {
        // RR: mover al final manteniendo FIFO
        removeProcess(p);
        processes.add(p);
    }
}