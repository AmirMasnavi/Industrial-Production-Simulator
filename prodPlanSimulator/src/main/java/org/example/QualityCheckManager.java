package org.example;

import java.util.PriorityQueue;
import java.util.Stack;


public class QualityCheckManager {

    private final PriorityQueue<QualityCheck> qualityCheckQueue;

    public QualityCheckManager() {
        // Max-heap by default (higher priority = higher value)
        qualityCheckQueue = new PriorityQueue<>();
    }

    // Add a quality check to the queue with the priority calculated from the tree depth
    public void addQualityCheckBasedOnDepth(ProductionTreeNode node, int depth) {
        if (node == null) return;

        // Assign higher priority for closer operations (smaller depth = higher priority)
        if(node.getItemId() != -1) {
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

    // Method to process quality checks and print them in reverse priority order (from 0 upwards)
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

    // Perform the quality checks one at a time based on priority
    public void performQualityChecks() {
        while (!qualityCheckQueue.isEmpty()) {
            QualityCheck check = qualityCheckQueue.poll();
            System.out.println("Performing Quality Check: " + check);
        }
    }

    // View all quality checks in order of priority
    public void viewQualityChecks() {
        System.out.println("Quality Checks in Order of Priority:");
        for (QualityCheck check : qualityCheckQueue) {
            System.out.println(check);
        }
    }
}


