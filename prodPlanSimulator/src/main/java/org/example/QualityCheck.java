package org.example;

/**
 * Represents a quality check step within a production process, with a defined priority level.
 * Quality checks with higher priority levels are considered closer to the final product.
 * Implements {@link Comparable} to allow sorting based on priority level, with higher-priority checks appearing first.
 *
 * @param checkId       Unique identifier for the quality check.
 * @param checkName     Descriptive name of the quality check.
 * @param priorityLevel Priority of the quality check; higher values indicate higher priority.
 */
public record QualityCheck(int checkId, String checkName, int priorityLevel) implements Comparable<QualityCheck> {

    /**
     * Compares this quality check with another based on their priority levels.
     * Ensures that higher-priority checks (higher priorityLevel values) appear first when sorted.
     *
     * @param other the other {@code QualityCheck} instance to compare with.
     * @return a negative integer, zero, or a positive integer as this object's priority is
     *         greater than, equal to, or less than the specified object's priority.
     */
    @Override
    public int compareTo(QualityCheck other) {
        // Higher priority level should come first (max-heap behavior for sorting)
        return Integer.compare(other.priorityLevel, this.priorityLevel);
    }

    /**
     * Returns a string representation of the {@code QualityCheck} instance.
     * Includes the priority level, check ID, and name of the quality check.
     *
     * @return a formatted string describing the quality check.
     */
    @Override
    public String toString() {
        return String.format(
                "Quality Check [Priority Level: %d, ID: %d, Name: '%s']",
                priorityLevel, checkId, checkName
        );
    }
}
