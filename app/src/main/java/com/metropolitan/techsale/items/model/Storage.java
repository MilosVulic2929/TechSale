package com.metropolitan.techsale.items.model;

public class Storage extends Item {

    private int capacity;
    private DiskType diskType;
    // 7200 RPM (HDD), 450 Read/Write (SSD)
    private String speed;

    public Storage() { }

    public Storage(int id, String name, String make, double price, int quantity, int capacity, DiskType diskType, String speed, String imageUrl) {
        super(id, name, make, price, quantity, imageUrl);
        this.capacity = capacity;
        this.diskType = diskType;
        this.speed = speed;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public DiskType getDiskType() {
        return diskType;
    }

    public void setDiskType(DiskType diskType) {
        this.diskType = diskType;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "Storage{" +
                "capacity=" + capacity +
                ", diskType=" + diskType +
                ", speed='" + speed + '\'' +
                "} " + super.toString();
    }

    public enum DiskType {
        SSD, HDD
    }
}
