package org.example.sprint3;

import java.util.List;

/**
 * Represents an activity in a project schedule.
 * Includes details such as ID, description, duration, cost, dependencies, and scheduling metrics.
 */
public class Activity {
    private final String id; // Unique identifier for the activity
    private final String description; // Brief description of the activity
    private int duration; // Duration of the activity
    private final String durationUnit; // Unit of the duration (e.g., days, weeks)
    private final double cost; // Cost associated with the activity
    private final List<String> dependencies; // List of activity IDs that this activity depends on

    // Scheduling metrics for the activity
    private int earliestStart; // Earliest start time of the activity
    private int earliestFinish; // Earliest finish time of the activity
    private int latestStart; // Latest start time of the activity
    private int latestFinish; // Latest finish time of the activity
    private int slack; // Slack time (difference between latest and earliest times)

    /**
     * Constructs an activity with the specified attributes.
     *
     * @param id            Unique identifier for the activity.
     * @param description   Description of the activity.
     * @param duration      Duration of the activity.
     * @param durationUnit  Unit of the duration (e.g., days, weeks).
     * @param cost          Cost associated with the activity.
     * @param dependencies  List of activity IDs that this activity depends on.
     */
    public Activity(String id, String description, int duration, String durationUnit, double cost, List<String> dependencies) {
        this.id = id;
        this.description = description;
        this.duration = duration;
        this.durationUnit = durationUnit;
        this.cost = cost;
        this.dependencies = dependencies;
    }

    /**
     * Returns the unique identifier of the activity.
     *
     * @return The activity ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the description of the activity.
     *
     * @return The activity description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the duration of the activity.
     *
     * @return The activity duration.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the activity.
     *
     * @param duration The new duration to set.
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Returns the unit of the activity duration (e.g., days, weeks).
     *
     * @return The duration unit.
     */
    public String getDurationUnit() {
        return durationUnit;
    }

    /**
     * Returns the cost associated with the activity.
     *
     * @return The activity cost.
     */
    public double getCost() {
        return cost;
    }

    /**
     * Returns the list of dependencies (activity IDs) for this activity.
     *
     * @return A list of dependencies.
     */
    public List<String> getDependencies() {
        return dependencies;
    }

    /**
     * Returns the earliest start time of the activity.
     *
     * @return The earliest start time.
     */
    public int getEarliestStart() {
        return earliestStart;
    }

    /**
     * Sets the earliest start time of the activity.
     *
     * @param earliestStart The earliest start time to set.
     */
    public void setEarliestStart(int earliestStart) {
        this.earliestStart = earliestStart;
    }

    /**
     * Returns the earliest finish time of the activity.
     *
     * @return The earliest finish time.
     */
    public int getEarliestFinish() {
        return earliestFinish;
    }

    /**
     * Sets the earliest finish time of the activity.
     *
     * @param earliestFinish The earliest finish time to set.
     */
    public void setEarliestFinish(int earliestFinish) {
        this.earliestFinish = earliestFinish;
    }

    /**
     * Returns the latest start time of the activity.
     *
     * @return The latest start time.
     */
    public int getLatestStart() {
        return latestStart;
    }

    /**
     * Sets the latest start time of the activity.
     *
     * @param latestStart The latest start time to set.
     */
    public void setLatestStart(int latestStart) {
        this.latestStart = latestStart;
    }

    /**
     * Returns the latest finish time of the activity.
     *
     * @return The latest finish time.
     */
    public int getLatestFinish() {
        return latestFinish;
    }

    /**
     * Sets the latest finish time of the activity.
     *
     * @param latestFinish The latest finish time to set.
     */
    public void setLatestFinish(int latestFinish) {
        this.latestFinish = latestFinish;
    }

    /**
     * Returns the slack time of the activity.
     * Slack represents the amount of time the activity can be delayed without affecting the project schedule.
     *
     * @return The slack time.
     */
    public int getSlack() {
        return slack;
    }

    /**
     * Sets the slack time of the activity.
     *
     * @param slack The slack time to set.
     */
    public void setSlack(int slack) {
        this.slack = slack;
    }

    /**
     * Returns a formatted string representation of the activity, including its ID, description,
     * duration, duration unit, and cost.
     *
     * @return A string representation of the activity.
     */
    @Override
    public String toString() {
        return String.format("Activity %s (%s, %d %s, %.2f)",
                id, description, duration, durationUnit, cost);
    }

    /**
     * Finds an activity by its ID in a list of activities.
     *
     * @param activities A list of activities to search.
     * @param id         The ID of the activity to find.
     * @return The activity with the specified ID, or null if not found.
     */
    public static Activity findActivityById(List<Activity> activities, String id) {
        return activities.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
    }
}
