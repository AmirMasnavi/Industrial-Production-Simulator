package org.example.sprint3;

import java.util.List;

public class Activity {
    private final int id;
    private final String description;
    private final int duration;
    private final String durationUnit;
    private final double cost;
    private final String costUnit;
    private final List<Integer> dependencies;

    public Activity(int id, String description, int duration, String durationUnit, double cost, String costUnit, List<Integer> dependencies) {
        this.id = id;
        this.description = description;
        this.duration = duration;
        this.durationUnit = durationUnit;
        this.cost = cost;
        this.costUnit = costUnit;
        this.dependencies = dependencies;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }

    public String getDurationUnit() {
        return durationUnit;
    }

    public double getCost() {
        return cost;
    }

    public String getCostUnit() {
        return costUnit;
    }

    public List<Integer> getDependencies() {
        return dependencies;
    }

    @Override
    public String toString() {
        return String.format("Activity %d (%s, %d %s, %.2f %s)",
                id, description, duration, durationUnit, cost, costUnit);
    }
}
