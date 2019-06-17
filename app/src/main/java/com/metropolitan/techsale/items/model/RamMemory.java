package com.metropolitan.techsale.items.model;

public class RamMemory extends Item {

    private int memory;

    private int frequency;

    // DDR3, DD4...
    private String type;

    public RamMemory() { }

    public RamMemory(int id, String name, String make, double price, int quantity, int memory, int frequency, String type, String imageUrl) {
        super(id, name, make, price, quantity, imageUrl);
        this.memory = memory;
        this.frequency = frequency;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
