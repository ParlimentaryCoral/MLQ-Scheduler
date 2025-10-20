package com.example.mlqschedulingso;

public class Process {
    // Identidad / entrada
    String label;      // A, B, C...
    int burstTime;     // BT
    int arrivalTime;   // AT
    int queueNumber;   // Q (1..3)
    int priority;      // Pr (5 alta .. 1 baja)

    // Métricas
    int remainingTime;   // restante
    int waitingTime;     // WT
    int completionTime;  // CT
    int responseTime;    // RT
    int turnaroundTime;  // TAT

    boolean hasStarted;
    boolean hasArrived;

    public Process(String lbl, int bt, int at, int q, int pr) {
        this.label = lbl;
        this.burstTime = bt;
        this.arrivalTime = at;
        this.queueNumber = q;
        this.priority = pr;

        this.remainingTime = bt;
        this.waitingTime = 0;
        this.completionTime = 0;
        this.responseTime = -1;
        this.turnaroundTime = 0;
        this.hasStarted = false;
        this.hasArrived = false;
    }

    public void calculateMetrics() {
        this.turnaroundTime = this.completionTime - this.arrivalTime;
        this.waitingTime = this.turnaroundTime - this.burstTime;
    }
}