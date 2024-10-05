package a;

import java.util.*;

public class PathMinCost {
    private Map<String, List<Edge>> graph;
    public boolean isWeighted;
    public boolean isDirected;

    // Constructor
    public PathMinCost(boolean isWeighted, boolean isDirected) {
        this.graph = new HashMap<>();
        this.isWeighted = isWeighted;
        this.isDirected = isDirected;
    }

    // Edge class to represent weighted edges
    private class Edge {
        String destination;
        int weight;

        Edge(String destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }

    // Add an edge to the graph
    public void addEdge(String u, String v, int weight) {
        graph.computeIfAbsent(u, k -> new ArrayList<>()).add(new Edge(v, weight));
        if (!isDirected) {
            graph.computeIfAbsent(v, k -> new ArrayList<>()).add(new Edge(u, weight));
        }
    }

    // Dijkstra's algorithm to find the minimum cost path
    public void dijkstra(String startNode, String targetNode) {
        // Check if startNode and targetNode are present in the graph
        if (!graph.containsKey(startNode) || !graph.containsKey(targetNode)) {
            System.out.println("Either the start node or target node does not exist in the graph.");
            System.out.println("Nodes in the graph: " + graph.keySet());
            return;
        }

        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previousNodes = new HashMap<>();
        PriorityQueue<NodeWeight> pq = new PriorityQueue<>(Comparator.comparingInt(nw -> nw.weight));
        Set<String> visited = new HashSet<>();

        // Initialize distances with infinity, except the startNode
        for (String node : graph.keySet()) {
            distances.put(node, Integer.MAX_VALUE);
        }
        distances.put(startNode, 0);
        pq.add(new NodeWeight(startNode, 0));

        while (!pq.isEmpty()) {
            NodeWeight current = pq.poll();
            String currentNode = current.node;

            if (visited.contains(currentNode)) {
                continue;
            }
            visited.add(currentNode);

            // Stop when we reach the target node
            if (currentNode.equals(targetNode)) {
                printPath(previousNodes, startNode, targetNode, distances.get(targetNode));
                return;
            }

            // Check all the neighbors
            for (Edge edge : graph.getOrDefault(currentNode, new ArrayList<>())) {
                String neighbor = edge.destination;
                int newDist = distances.get(currentNode) + edge.weight;

                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    previousNodes.put(neighbor, currentNode);
                    pq.add(new NodeWeight(neighbor, newDist));
                }
            }
        }

        // If we exit the loop without finding the target node
        System.out.println("Target node " + targetNode + " not reachable from " + startNode + ".");
    }

    // Helper method to print the path
    private void printPath(Map<String, String> previousNodes, String startNode, String targetNode, int totalWeight) {
        List<String> path = new ArrayList<>();
        for (String at = targetNode; at != null; at = previousNodes.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);

        System.out.println("Shortest path: " + path);
        System.out.println("Total weight of the shortest path: " + totalWeight);
    }

    // Helper class to store node and weight for Dijkstra's algorithm
    private class NodeWeight {
        String node;
        int weight;

        NodeWeight(String node, int weight) {
            this.node = node;
            this.weight = weight;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Create a weighted directed graph
        PathMinCost graph = new PathMinCost(true, true); // Directed and Weighted

        // Add edges based on your input
        graph.addEdge("0,0", "1,0", 5);
        graph.addEdge("0,0", "0,1", 2);
        graph.addEdge("0,1", "0,2", 2);
        graph.addEdge("0,2", "1,2", 1);
        graph.addEdge("1,2", "1,1", 1);
        graph.addEdge("1,0", "1,1", 3);
        graph.addEdge("1,0", "2,0", 1);
        graph.addEdge("1,1", "2,1", 3);
        graph.addEdge("2,1", "2,2", 1);

        // Define starting and target nodes
        String startNode = "0,0";
        String targetNode = "2,2";

        // Perform Dijkstra's algorithm to find the shortest path
        System.out.println("Finding the shortest path from " + startNode + " to " + targetNode + "...");
        graph.dijkstra(startNode, targetNode);
    }
}
