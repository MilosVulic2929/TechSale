package com.metropolitan.techsale.items.model;

public class Gpu extends Item {

    private int memory;
    private int cores;
    private int memorySpeed;
    private int boostClock;

    public Gpu() {
    }

    public Gpu(int id, String name, String make, double price, int quantity, int memory, int cores, int memorySpeed, int boostClock, String imageUrl) {
        super(id, name, make, price, quantity, imageUrl);
        setType("gpu");
        this.memory = memory;
        this.cores = cores;
        this.memorySpeed = memorySpeed;
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

    public int getMemorySpeed() {
        return memorySpeed;
    }

    public void setMemorySpeed(int memorySpeed) {
        this.memorySpeed = memorySpeed;
    }

    public int getBoostClock() {
        return boostClock;
    }

    public void setBoostClock(int boostClock) {
        this.boostClock = boostClock;
    }

    @Override
    public String getType() {
        return "gpu";
    }

    @Override
    public String toString() {
        return "Gpu{" +
                "memory=" + memory +
                ", cores=" + cores +
                ", memorySpeed=" + memorySpeed +
                ", boostClock=" + boostClock +
                "} " + super.toString();
    }
}
