package graph;

import org.graphstream.graph.implementations.SingleGraph;

import java.util.*;

/**
 * Graph implementation
 * @author Solange U. Gasengayire
 *
 */
public class Graph  extends SingleGraph {

    private HashMap<Integer, Vertex> vertices;
    private int seededNodeCount = 0;
    private int switchedNodeCount = 0;

    /**
     * Create new empty graph
     * @param id the (single) graph id
     */
    public Graph(String id) {
        super(id);
        this.vertices = new HashMap<>();
    }

    /**
     * Add a vertex with the given id number to the graph
     * @param num the vertex id
     */
    public void addVertex(int num) {
        super.addNode(String.valueOf(num));

        if (! vertices.containsKey(num)) {
            Vertex vertex = new Vertex(this, String.valueOf(num));
            vertices.put(num, vertex);
        }
    }

    /**
     * Add an edge from a vertex to another
     * @param from the origin vertex id
     * @param to the destination verted id
     */
    public void addEdge(int from, int to) {
        String edgeId = String.valueOf(from) + "-" + String.valueOf(to);
        super.addEdge(edgeId, String.valueOf(from), String.valueOf(to));

        Vertex start = vertices.get(from);
        if (start != null) {
            Edge edge = new Edge(from, to);
            start.addEdge(edge);
        }
    }

    /**
     * Return the number of nodes in the graph
     * @return total number of nodes
     */
    public int getTotalNodeCount() {
        return vertices.size();
    }

    /**
     * Return the number of seeded vertices
     * at each simulation
     * @return seeded nodes count
     */
    public int getSeededNodeCount() {
        return seededNodeCount;
    }

    /**
     * Return the number of switched vertices
     * after each simulation
     * @return switched nodes count
     */
    public int getSwitchedNodeCount() {
        return switchedNodeCount;
    }

    /**
     * Behavior cascade simulation algorithm
     * @param rewardA value of incentive a
     * @param rewardB value of incentive b
     */
    public void runSimulations(int rewardA, int rewardB) {
        /*
         * Example to help formalize our model
         * ***********************************
         * For two vertices v and w that are neighbors:
         *  → if v and w both use the Netbeans IDE, they get reward a
         *  → if v and w both use the Eclipse IDE, they get reward b
         *  → if v and w use different IDEs, they get no reward
         *
         * So, the question becomes: when should a vertex switch IDEs?
         *  →       p : the fraction of v's neighbors that use Netbeans
         *  → (1 - p) : the fraction of v's neighbors that use Eclipse
         *  → v should switch to Netbeans if p > [b / (a + b)]
         * */

        System.out.println("\n***************************");
        System.out.println("    START OF SIMULATION    ");

        resetState();
        sleep(5000);

        // Value that determines whether a vertex should switch
        double threshold = ((double) rewardB) / ((double) (rewardA + rewardB));

        // A list of vertices that have switched
        Queue<Vertex> switched = new LinkedList<>();

        // A list of vertices that will switch at each iteration
        List<Vertex> toSwitch = new LinkedList<>();

        // A list of all visited vertices
        HashSet<Vertex> visited = new HashSet<>();

        // Start with randomly choosen nodes
        switched.addAll(getRandomSeededVertices());
        sleep(5000);

        while (! switched.isEmpty()) {

            Vertex current = switched.poll();

            // determine which neighbors should switch
            for (Integer id : current.getNeighbors()) {
                Vertex node = vertices.get(id);

                if (! visited.contains(node) || ! node.hasSwitched()) {
                    double prob = computeSwitchingProbability(node);

                    if (prob > threshold) {
                        toSwitch.add(node);
                    }
                }

                // keep track of all the neighbors that were visited
                visited.add(node);
            }

            // make all the changes (vertices that have switched)
            for (Vertex vertex: toSwitch) {
                vertex.setSwitched(true);
                sleep(1000);
                switched.add(vertex);
                switchedNodeCount++;
            }

            // clear this list before running next iteration
            toSwitch.clear();
        }

        System.out.println("\n     END OF SIMULATION     ");
        System.out.println("***************************\n");
    }

    /**
     * Return a randomly chosen seeded vertices list
     * for simulations purposes
     * @return a list of randomly chosen nodes
     */
    private List<Vertex> getRandomSeededVertices() {
        List<Vertex> nodes = new LinkedList<>();
        int limit = (vertices.size() + 1) / 10;

        for (int i = 0; i < limit; i++) {
            Vertex vertex = vertices.get(i);
            if (vertex != null) {
                vertex.setSwitched(true);
                vertex.setAttribute("ui.class", "seeded");
                nodes.add(vertex);
                seededNodeCount++;
            }
        }

        return nodes;
    }

    /**
     * Compute the switching probability of a given node
     * @param vertex the node at hand
     * @return the switching probability
     */
    private double computeSwitchingProbability(Vertex vertex) {
        int switchedNeighbors = 0;
        double totalNeighbors = (double) vertex.getNeighbors().size();

        for (Integer id: vertex.getNeighbors()) {
            Vertex neighbor = vertices.get(id);
            if (neighbor.hasSwitched()) {
                switchedNeighbors++;
            }
        }

        double prob;
        prob = ((double) switchedNeighbors) / totalNeighbors;
        return prob;
    }

    /**
     * Reset vertices states
     */
    private void resetState() {
        for (Vertex node: vertices.values()) {
            node.setSwitched(false);
        }
    }

    /**
     * In order to gradually visualize the
     * cascades in the graph, let's introduce a
     * 100 milliseconds pause.
     */
    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
