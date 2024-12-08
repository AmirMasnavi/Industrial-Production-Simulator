package org.example.sprint3;

import java.io.FileWriter;
import java.io.IOException;

public class GraphVizExporter {
    public static void exportToDot(Graph<Activity, Integer> graph, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("digraph PERTCPM {\n");
            writer.write("  rankdir=LR;\n"); // Left-to-right layout for PERT/CPM

            // Write vertices
            for (Activity activity : graph.vertices()) {
                writer.write(String.format("  %d [label=\"%s (%d %s, %.2f %s)\"];\n",
                        activity.getId(),
                        activity.getDescription(),
                        activity.getDuration(),
                        activity.getDurationUnit(),
                        activity.getCost(),
                        activity.getCostUnit()));
            }

            // Write edges
            for (Edge<Activity, Integer> edge : graph.edges()) {
                writer.write(String.format("  %d -> %d [label=\"%d %s\"];\n",
                        edge.getVOrig().getId(),
                        edge.getVDest().getId(),
                        edge.getWeight(),
                        edge.getVOrig().getDurationUnit())); // Use origin's duration unit
            }

            writer.write("}\n");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error writing DOT file", e);
        }
    }
}

