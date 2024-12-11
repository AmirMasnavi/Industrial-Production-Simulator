package org.example.sprint3;

import java.util.*;
import java.util.function.BinaryOperator;

/**
 *
 * @author DEI-ISEP
 *
 */
public class Algorithms {

    /** Performs breadth-first search of a Graph starting in a vertex
     *
     * @param g Graph instance
     * @param vert vertex that will be the source of the search
     * @return a LinkedList with the vertices of breadth-first search
     */
    public static <V, E> LinkedList<V> BreadthFirstSearch(Graph<V, E> g, V vert) {
        // Check if vertex exists in the graph
        if (!g.vertices().contains(vert)) {
            return null;
        }

        // Initialize data structures
        LinkedList<V> path = new LinkedList<>();
        Set<V> visited = new HashSet<>();
        Queue<V> queue = new LinkedList<>();

        // Start BFS from the given vertex
        queue.add(vert);
        visited.add(vert);
        path.add(vert);

        while (!queue.isEmpty()) {
            V current = queue.poll();

            // Explore adjacent vertices
            for (V adjacentVertex : g.adjVertices(current)) {
                if (!visited.contains(adjacentVertex)) {
                    queue.add(adjacentVertex);
                    visited.add(adjacentVertex);
                    path.add(adjacentVertex);
                }
            }
        }

        return path;
    }

    /** Performs depth-first search starting in a vertex
     *
     * @param g Graph instance
     * @param vOrig vertex of graph g that will be the source of the search
     * @param visited set of previously visited vertices
     * @param qdfs return LinkedList with vertices of depth-first search
     */
    private static <V, E> void DepthFirstSearch(Graph<V, E> g, V vOrig, boolean[] visited, LinkedList<V> qdfs) {
        // Find the index of the original vertex
        List<V> vertices = new ArrayList<>(g.vertices());
        int origIndex = vertices.indexOf(vOrig);

        // Mark the vertex as visited
        visited[origIndex] = true;

        // Add the vertex to the depth-first search list
        qdfs.add(vOrig);

        // Explore all adjacent vertices
        for (V adjacentVertex : g.adjVertices(vOrig)) {
            // Find the index of the adjacent vertex
            int adjIndex = vertices.indexOf(adjacentVertex);

            // If the adjacent vertex hasn't been visited, recursively explore it
            if (!visited[adjIndex]) {
                DepthFirstSearch(g, adjacentVertex, visited, qdfs);
            }
        }
    }

    /** Performs depth-first search starting in a vertex
     *
     * @param g Graph instance
     * @param vert vertex of graph g that will be the source of the search

     * @return a LinkedList with the vertices of depth-first search
     */
    public static <V, E> LinkedList<V> DepthFirstSearch(Graph<V, E> g, V vert) {
        // Check if the vertex exists in the graph
        if (!g.vertices().contains(vert)) {
            return null;
        }

        // Create a list of vertices to determine the size for visited array
        List<V> vertices = new ArrayList<>(g.vertices());

        // Initialize the visited array
        boolean[] visited = new boolean[vertices.size()];

        // Create a linked list to store the depth-first search path
        LinkedList<V> path = new LinkedList<>();

        // Perform depth-first search
        DepthFirstSearch(g, vert, visited, path);

        return path;
    }

    /** Returns all paths from vOrig to vDest
     *
     * @param g       Graph instance
     * @param vOrig   Vertex that will be the source of the path
     * @param vDest   Vertex that will be the end of the path
     * @param visited set of discovered vertices
     * @param path    stack with vertices of the current path (the path is in reverse order)
     * @param paths   ArrayList with all the paths (in correct order)
     */
    private static <V, E> void allPaths(Graph<V, E> g, V vOrig, V vDest, boolean[] visited,
                                        LinkedList<V> path, ArrayList<LinkedList<V>> paths) {
        visited[g.key(vOrig)] = true;
        path.add(vOrig);

        if (vOrig.equals(vDest)) {
            paths.add(new LinkedList<>(path));
        } else {
            for (V adjacent : g.adjVertices(vOrig)) {
                if (!visited[g.key(adjacent)]) {
                    allPaths(g, adjacent, vDest, visited, path, paths);
                }
            }
        }

        visited[g.key(vOrig)] = false;
        path.removeLast();
    }

    /** Returns all paths from vOrig to vDest
     *
     * @param g     Graph instance
     * @param vOrig information of the Vertex origin
     * @param vDest information of the Vertex destination
     * @return paths ArrayList with all paths from vOrig to vDest
     */
    public static <V, E> ArrayList<LinkedList<V>> allPaths(Graph<V, E> g, V vOrig, V vDest) {
        ArrayList<LinkedList<V>> paths = new ArrayList<>();
        if (!g.vertices().contains(vOrig) || !g.vertices().contains(vDest)) return paths;

        boolean[] visited = new boolean[g.numVertices()];
        LinkedList<V> path = new LinkedList<>();
        allPaths(g, vOrig, vDest, visited, path, paths);
        return paths;
    }

    /**
     * Computes shortest-path distance from a source vertex to all reachable
     * vertices of a graph g with non-negative edge weights
     * This implementation uses Dijkstra's algorithm
     *
     * @param g        Graph instance
     * @param vOrig    Vertex that will be the source of the path
     * @param visited  set of previously visited vertices
     * @param pathKeys minimum path vertices keys
     * @param dist     minimum distances
     */
    private static <V, E> void shortestPathDijkstra(Graph<V, E> g, V vOrig,
                                                    Comparator<E> ce, BinaryOperator<E> sum, E zero,
                                                    boolean[] visited, V[] pathKeys, E[] dist) {
        int numVerts = g.numVertices();
        Arrays.fill(visited, false);
        Arrays.fill(dist, null);
        Arrays.fill(pathKeys, null);

        int origIdx = g.key(vOrig);
        dist[origIdx] = zero;

        for (int i = 0; i < numVerts; i++) {
            // Find the unvisited vertex with the smallest distance
            E minDist = null;
            int minIdx = -1;
            for (int j = 0; j < numVerts; j++) {
                if (!visited[j] && dist[j] != null && (minDist == null || ce.compare(dist[j], minDist) < 0)) {
                    minDist = dist[j];
                    minIdx = j;
                }
            }

            if (minIdx == -1) break; // All reachable vertices are processed
            visited[minIdx] = true;

            // Update distances to neighbors
            for (Edge<V, E> edge : g.outgoingEdges(g.vertex(minIdx))) {
                int neighborIdx = g.key(edge.getVDest());
                if (!visited[neighborIdx]) {
                    E newDist = sum.apply(dist[minIdx], edge.getWeight());
                    if (dist[neighborIdx] == null || ce.compare(newDist, dist[neighborIdx]) < 0) {
                        dist[neighborIdx] = newDist;
                        pathKeys[neighborIdx] = g.vertex(minIdx);
                    }
                }
            }
        }
    }



    /** Shortest-path between two vertices
     *
     * @param g graph
     * @param vOrig origin vertex
     * @param vDest destination vertex
     * @param ce comparator between elements of type E
     * @param sum sum two elements of type E
     * @param zero neutral element of the sum in elements of type E
     * @param shortPath returns the vertices which make the shortest path
     * @return if vertices exist in the graph and are connected, true, false otherwise
     */
    public static <V, E> E shortestPath(Graph<V, E> g, V vOrig, V vDest,
                                        Comparator<E> ce, BinaryOperator<E> sum, E zero,
                                        LinkedList<V> shortPath) {
        if (!g.validVertex(vOrig) || !g.validVertex(vDest)) return null;

        int numVerts = g.numVertices();
        boolean[] visited = new boolean[numVerts];
        @SuppressWarnings("unchecked")
        V[] pathKeys = (V[]) new Object[numVerts];
        @SuppressWarnings("unchecked")
        E[] dist = (E[]) new Object[numVerts];

        Arrays.fill(dist, null);
        dist[g.key(vOrig)] = zero;

        for (int i = 0; i < numVerts; i++) {
            E minDist = null;
            int minIdx = -1;

            for (int j = 0; j < numVerts; j++) {
                if (!visited[j] && dist[j] != null && (minDist == null || ce.compare(dist[j], minDist) < 0)) {
                    minDist = dist[j];
                    minIdx = j;
                }
            }

            if (minIdx == -1) break;
            visited[minIdx] = true;

            for (Edge<V, E> edge : g.outgoingEdges(g.vertex(minIdx))) {
                int neighborIdx = g.key(edge.getVDest());
                if (!visited[neighborIdx]) {
                    E newDist = sum.apply(dist[minIdx], edge.getWeight());
                    if (dist[neighborIdx] == null || ce.compare(newDist, dist[neighborIdx]) < 0) {
                        dist[neighborIdx] = newDist;
                        pathKeys[neighborIdx] = g.vertex(minIdx);
                    }
                }
            }
        }

        if (dist[g.key(vDest)] == null) return null;

        getPath(g, vOrig, vDest, pathKeys, shortPath);
        return dist[g.key(vDest)];
    }


    /** Shortest-path between a vertex and all other vertices
     *
     * @param g graph
     * @param vOrig start vertex
     * @param ce comparator between elements of type E
     * @param sum sum two elements of type E
     * @param zero neutral element of the sum in elements of type E
     * @param paths returns all the minimum paths
     * @param dists returns the corresponding minimum distances
     * @return if vOrig exists in the graph true, false otherwise
     */
    public static <V, E> boolean shortestPaths(Graph<V, E> g, V vOrig,
                                               Comparator<E> ce, BinaryOperator<E> sum, E zero,
                                               ArrayList<LinkedList<V>> paths, ArrayList<E> dists) {
        if (!g.validVertex(vOrig)) return false;

        int numVerts = g.numVertices();
        boolean[] visited = new boolean[numVerts];
        @SuppressWarnings("unchecked")
        V[] pathKeys = (V[]) new Object[numVerts];
        @SuppressWarnings("unchecked")
        E[] dist = (E[]) new Object[numVerts];

        Arrays.fill(dist, null);
        dist[g.key(vOrig)] = zero;

        for (int i = 0; i < numVerts; i++) {
            E minDist = null;
            int minIdx = -1;

            for (int j = 0; j < numVerts; j++) {
                if (!visited[j] && dist[j] != null && (minDist == null || ce.compare(dist[j], minDist) < 0)) {
                    minDist = dist[j];
                    minIdx = j;
                }
            }

            if (minIdx == -1) break;
            visited[minIdx] = true;

            for (Edge<V, E> edge : g.outgoingEdges(g.vertex(minIdx))) {
                int neighborIdx = g.key(edge.getVDest());
                if (!visited[neighborIdx]) {
                    E newDist = sum.apply(dist[minIdx], edge.getWeight());
                    if (dist[neighborIdx] == null || ce.compare(newDist, dist[neighborIdx]) < 0) {
                        dist[neighborIdx] = newDist;
                        pathKeys[neighborIdx] = g.vertex(minIdx);
                    }
                }
            }
        }

        paths.clear();
        dists.clear();

        for (int i = 0; i < numVerts; i++) {
            LinkedList<V> path = new LinkedList<>();
            if (dist[i] != null) getPath(g, vOrig, g.vertex(i), pathKeys, path);
            paths.add(path);
            dists.add(dist[i]);
        }
        return true;
    }


    /**
     * Extracts from pathKeys the minimum path between voInf and vdInf
     * The path is constructed from the end to the beginning
     *
     * @param g        Graph instance
     * @param vOrig    information of the Vertex origin
     * @param vDest    information of the Vertex destination
     * @param pathKeys minimum path vertices keys
     * @param path     stack with the minimum path (correct order)
     */
    private static <V, E> void getPath(Graph<V, E> g, V vOrig, V vDest,
                                       V[] pathKeys, LinkedList<V> path) {
        path.clear();
        V current = vDest;
        while (current != null && !current.equals(vOrig)) {
            path.addFirst(current);
            current = pathKeys[g.key(current)];
        }
        if (current == null) path.clear();
        else path.addFirst(vOrig);
    }


    /** Calculates the minimum distance graph using Floyd-Warshall
     *
     * @param g initial graph
     * @param ce comparator between elements of type E
     * @param sum sum two elements of type E
     * @return the minimum distance graph
     */
    public static <V, E> MatrixGraph<V, E> minDistGraph(Graph<V, E> g, Comparator<E> ce, BinaryOperator<E> sum, E zero) {
        int numVerts = g.numVertices();
        MatrixGraph<V, E> minDistGraph = new MatrixGraph<>(g.isDirected());

        for (V v : g.vertices()) {
            minDistGraph.addVertex(v);
        }

        @SuppressWarnings("unchecked")
        E[][] dist = (E[][]) new Object[numVerts][numVerts];

        for (Edge<V, E> edge : g.edges()) {
            dist[g.key(edge.getVOrig())][g.key(edge.getVDest())] = edge.getWeight();
        }

        for (int i = 0; i < numVerts; i++) {
            for (int j = 0; j < numVerts; j++) {
                if (i != j && dist[i][j] == null) {
                    dist[i][j] = null;
                }
            }
        }

        for (int k = 0; k < numVerts; k++) {
            for (int i = 0; i < numVerts; i++) {
                for (int j = 0; j < numVerts; j++) {
                    if (dist[i][k] != null && dist[k][j] != null) {
                        E newDist = sum.apply(dist[i][k], dist[k][j]);
                        if (dist[i][j] == null || ce.compare(newDist, dist[i][j]) < 0) {
                            dist[i][j] = newDist;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < numVerts; i++) {
            for (int j = 0; j < numVerts; j++) {
                if (dist[i][j] != null) {
                    minDistGraph.addEdge(minDistGraph.vertex(i), minDistGraph.vertex(j), dist[i][j]);
                }
            }
        }

        return minDistGraph;
    }

}