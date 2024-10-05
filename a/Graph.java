package a;

import java.util.*;

public class Graph {
    private Map<String, List<Edge>> graph;
    private boolean isWeighted;
    private boolean isDirected;

    // Constructor
    public Graph(boolean isWeighted, boolean isDirected) {
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

    // Construct graph from file
    public void constructGraphFromFile(String fileName) {
        try (Scanner scanner = new Scanner(new java.io.File(fileName))) {
            while (scanner.hasNextLine()) {
                String[] edge = scanner.nextLine().split(" ");
                String u = edge[0].toUpperCase(); // Convert to uppercase
                String v = edge[1].toUpperCase(); // Convert to uppercase
                int weight = (edge.length == 3) ? Integer.parseInt(edge[2]) : 1; // Assume unweighted if no third value
                addEdge(u, v, weight);
            }
        } catch (java.io.FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
        }
    }

    // Dijkstra's algorithm
    public void dijkstra(String startNode, String targetNode) {
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
        System.out.println("Target node " + targetNode + " not found.");
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
        String runAgain;

        while (true) {
            // Read graph from a file
            System.out.print("Enter the file name (without extension): ");
            String fileName = scanner.nextLine().toUpperCase();
            if (fileName.equals("0Q")) {
                System.out.println("Restarting program...");
                continue; // Restart if "0q" is entered
            }

            // Ask if the graph is weighted
            System.out.print("Is the graph weighted? (yes or no): ");
            String weightedInput = scanner.nextLine().toLowerCase();
            boolean isWeighted = weightedInput.equals("yes");

            // Ask if the graph is directed
            System.out.print("Is the graph directed? (yes or no): ");
            String directedInput = scanner.nextLine().toLowerCase();
            boolean isDirected = directedInput.equals("yes");

            // Construct the graph
            Graph graph = new Graph(isWeighted, isDirected);
            graph.constructGraphFromFile(fileName + ".txt");

            // Take the starting node input
            System.out.print("Enter the starting node: ");
            String startNode = scanner.nextLine().toUpperCase();
            if (startNode.equals("0Q")) {
                System.out.println("Restarting program...");
                continue; // Restart if "0q" is entered
            }

            // Take the target node input
            System.out.print("Enter the target node: ");
            String targetNode = scanner.nextLine().toUpperCase();
            if (targetNode.equals("0Q")) {
                System.out.println("Restarting program...");
                continue; // Restart if "0q" is entered
            }

            // Perform Dijkstra's algorithm
            graph.dijkstra(startNode, targetNode);

            // Ask if the user wants to run the program again
            System.out.print("Do you want to run the program again? (yes or no): ");
            runAgain = scanner.nextLine().toLowerCase();
            if (!runAgain.equals("yes")) {
                break; // Exit if user doesn't want to run again
            }
        }
    }
}
