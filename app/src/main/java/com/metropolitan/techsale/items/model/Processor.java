package com.metropolitan.techsale.items.model;

public class Processor extends Item{

    private int cores;
    private double speed;

    // intel 1151, amd am4...
    private String socket;

    public Processor() { }

    public Processor(int id, String name, String make, double price, int quantity, int cores, double speed, String socket, String imageUrl) {
        super(id, name, make, price, quantity, imageUrl);
        setType("cpu");
        this.cores = cores;
        this.speed = speed;
        this.socket = socket;
    }

    public int getCores() {
        return cores;
    }

    public void setCores(int cores) {
        this.cores = cores;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    @Override
    public String getType() {
        return "cpu";
    }

    @Override
    public String toString() {
        return "Processor{" +
                "cores=" + cores +
                ", speed=" + speed +
                ", socket='" + socket + '\'' +
                "} " + super.toString();
    }
}
