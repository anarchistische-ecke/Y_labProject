package org.example.model;

public class Goal {
    private final int id;
    private double targetAmount;
    private double currentProgress;
    private String name;

    public Goal(int id, double targetAmount, String name) {
        this.id = id;
        this.targetAmount = targetAmount;
        this.currentProgress = 0;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public double getCurrentProgress() {
        return currentProgress;
    }

    public void addProgress(double amount) {
        this.currentProgress += amount;
    }

    public void setCurrentProgress(double currentProgress) {
        this.currentProgress = currentProgress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "id=" + id +
                ", targetAmount=" + targetAmount +
                ", currentProgress=" + currentProgress +
                '}';
    }
}
