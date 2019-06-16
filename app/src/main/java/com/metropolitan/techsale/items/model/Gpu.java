package com.metropolitan.techsale.items.model;

public class Gpu extends Item {

    private int memory;

    private int cores;

    private int coreClock;

    private int boostClock;

    public Gpu() {}


    public Gpu(int id, String name, String make, double price, int quantity, int memory, int cores, int coreClock, int boostClock) {
        super(id, name, make, price, quantity);
        this.memory = memory;
        this.cores = cores;
        this.coreClock = coreClock;
        this.boostClock = boostClock;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getCores() {
        return cores;
    }

    public void setCores(int cores) {
        this.cores = cores;
    }

    public int getCoreClock() {
        return coreClock;
    }

    public void setCoreClock(int coreClock) {
        this.coreClock = coreClock;
    }

    public int getBoostClock() {
        return boostClock;
    }

    public void setBoostClock(int boostClock) {
        this.boostClock = boostClock;
    }
}
