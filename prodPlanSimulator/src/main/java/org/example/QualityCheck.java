package org.example;

public class QualityCheck implements Comparable<QualityCheck> {
    private final String checkName;
    private final int checkId;
    private final int priorityLevel; // Higher priority means a higher value (closer to final product)

    public QualityCheck(int checkId, String checkName, int priorityLevel) {
        this.checkId = checkId;
        this.checkName = checkName;
        this.priorityLevel = priorityLevel;
    }

    public String getCheckName() {
        return checkName;
    }

    public int getCheckId() {
        return checkId;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    // Implement the compareTo method to define the priority
    @Override
    public int compareTo(QualityCheck other) {
        // Higher priority level should come first (max-heap)
        return Integer.compare(other.priorityLevel, this.priorityLevel);
    }

    @Override
    public String toString() {
        return String.format(
                "Quality Check [Priority Level: %d, ID: %d, Name: '%s']",
                priorityLevel, checkId, checkName
        );
    }
}
