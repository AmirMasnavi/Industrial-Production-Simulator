package org.example;

import java.util.PriorityQueue;
import java.util.Stack;

/**
 * Manages a collection of quality checks for a production process.
 * Quality checks are prioritized based on the depth of the node in the production tree,
 * where closer nodes (smaller depth) have higher priority.
 * Supports adding quality checks, performing them in priority order, or processing them in reverse priority order.
 */
public class QualityCheckManager {

    private final PriorityQueue<QualityCheck> qualityCheckQueue;

    /**
     * Constructs an instance of {@code QualityCheckManager}.
     * Uses a max-heap (via {@link PriorityQueue}) to organize quality checks by priority,
     * where higher priority levels are processed first.
     */
    public QualityCheckManager() {
        // Max-heap by default (higher priority = higher value)
        qualityCheckQueue = new PriorityQueue<>();
    }

    /**
     * Adds a quality check for a node in the production tree based on its depth.
     * Nodes closer to the root (smaller depth) are given higher priority.
     *
     * @param node  the production tree node for which the quality check is added.
     * @param depth the depth of the node in the production tree (0 for the root).
     */
    public void addQualityCheckBasedOnDepth(ProductionTreeNode node, int depth) {
        if (node == null) return;

        // Assign higher priority for closer operations (smaller depth = higher priority)
        if (node.getItemId() != -1) {
            String checkName = node.getItemName() + " (" + node.getItemId() + ")";

            // Create a QualityCheck instance for this operation
            QualityCheck qualityCheck = new QualityCheck(node.getItemId(), checkName, depth);

            // Add the quality check to the priority queue
            qualityCheckQueue.offer(qualityCheck);
        }

        // Recursively add the operations for the child nodes with incremented depth
        for (ProductionTreeNode childNode : node.getChildren()) {
            addQualityCheckBasedOnDepth(childNode, depth + 1); // Increase depth as we go down the tree
        }
    }

    /**
     * Processes and performs quality checks in reverse priority order.
     * Quality checks with the lowest priority are handled first.
     */
    public void processQualityChecksInReverse() {
        // Create a stack to reverse the order
        Stack<QualityCheck> reverseStack = new Stack<>();

        // Move all quality checks to the stack
        while (!qualityCheckQueue.isEmpty()) {
            reverseStack.push(qualityCheckQueue.poll());
        }

        // Now process and perform quality checks in reverse order (lowest priority first)
        while (!reverseStack.isEmpty()) {
            QualityCheck qc = reverseStack.pop();  // Pop the checks from the stack (which gives reverse order)
            System.out.println("Performing Quality Check: " + qc);
        }
    }

    /**
     * Processes and performs quality checks in priority order.
     * Quality checks with the highest priority are handled first.
     */
    public void performQualityChecks() {
        while (!qualityCheckQueue.isEmpty()) {
            QualityCheck check = qualityCheckQueue.poll();
            System.out.println("Performing Quality Check: " + check);
        }
    }

    /**
     * Displays all pending quality checks in the queue in order of their priority.
     * Higher-priority checks are displayed first.
     */
    public void viewQualityChecks() {
        System.out.println("Quality Checks in Order of Priority:");
        for (QualityCheck check : qualityCheckQueue) {
            System.out.println(check);
        }
    }
}
