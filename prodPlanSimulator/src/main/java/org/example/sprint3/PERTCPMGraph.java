package org.example.sprint3;

import java.util.List;

public class PERTCPMGraph {
    private final Graph<Activity, Integer> graph;

    public PERTCPMGraph() {
        this.graph = new MapGraph<>(true); // Directed graph
    }

    public void buildGraph(List<Activity> activities) {
        // Add vertices
        for (Activity activity : activities) {
            graph.addVertex(activity);
        }

        // Add edges based on dependencies
        for (Activity activity : activities) {
            for (int depId : activity.getDependencies()) {
                Activity depActivity = findActivityById(activities, depId);
                if (depActivity != null) {
                    graph.addEdge(depActivity, activity, activity.getDuration());
                }
            }
        }
    }

    private Activity findActivityById(List<Activity> activities, int id) {
        return activities.stream().filter(a -> a.getId() == id).findFirst().orElse(null);
    }

    public Graph<Activity, Integer> getGraph() {
        return graph;
    }
}
