package org.example;

/**
 * @param priorityLevel Higher priority means a higher value (closer to final product)
 */
public record QualityCheck(int checkId, String checkName, int priorityLevel) implements Comparable<QualityCheck> {

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
