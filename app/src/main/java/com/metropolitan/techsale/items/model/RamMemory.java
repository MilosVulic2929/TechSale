package com.metropolitan.techsale.items.model;

public class RamMemory extends Item {

    private int memory;
    private int frequency;

    // DDR3, DD4...
    private String ramType;

    public RamMemory() {
    }

    public RamMemory(int id, String name, String make, double price, int quantity, int memory, int frequency, String ramType, String imageUrl) {
        super(id, name, make, price, quantity, imageUrl);
        setType("ram");
        this.memory = memory;
        this.frequency = frequency;
        this.ramType = ramType;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getRamType() {
        return ramType;
    }

    public void setRamType(String ramType) {
        this.ramType = ramType;
    }

    @Override
    public String getType() {
        return "ram";
    }

    @Override
    public String toString() {
        return "RamMemory{" +
                "memory=" + memory +
                ", frequency=" + frequency +
                ", ramType='" + ramType + '\'' +
                "} " + super.toString();
    }
}
