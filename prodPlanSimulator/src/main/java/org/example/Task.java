package org.example;

/**
 * The Task class represents an individual task in the simulation.
 * It decouples the operation being executed from the Item itself,
 * allowing for better management of the queue of operations,
 * potentially with different priorities.
 * Each operation on an Item becomes its own "task" in the simulation queue.
 */
public class Task {
    private final Item item;
    private final String operation;
    private final int priority;

    /**
     * Constructor to initialize a Task with the associated Item, operation, and priority level.
     * The priority is determined based on the priority of the Item:
     * - HIGH priority corresponds to 3
     * - NORMAL priority corresponds to 2
     * - LOW priority corresponds to 1
     *
     * @param item      the Item associated with the task
     * @param operation the name of the operation to be performed
     */
    public Task(Item item, String operation) {
        this.item = item;
        this.operation = operation;
        this.priority = item.getPriority() == Item.Priority.HIGH ? 3 :
                item.getPriority() == Item.Priority.NORMAL ? 2 : 1;
    }

    /**
     * Get the Item associated with this task.
     *
     * @return the Item object linked to this task
     */
    public Item getItem() {
        return item;
    }

    /**
     * Get the operation to be performed for this task.
     *
     * @return the name of the operation as a String
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Get the priority level of this task.
     *
     * @return an integer representing the priority level (3 for HIGH, 2 for NORMAL, 1 for LOW)
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Provide a string representation of the Task object.
     *
     * @return a String describing the task, including the Item, operation, and priority level
     */
    @Override
    public String toString() {
        return "Task{" +
                "item=" + item +
                ", operation='" + operation + '\'' +
                ", priority=" + priority +
                '}';
    }
}
