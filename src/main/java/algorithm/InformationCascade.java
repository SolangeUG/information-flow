package algorithm;

import org.graphstream.algorithm.Algorithm;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleNode;

import java.util.*;

/**
 * Information cascade simulation algorithm
 * @author Solange U. Gasengayire
 */
public class InformationCascade implements Algorithm {

    private graph.Graph graph;

    private int rewardA = 1;
    private int rewardB = 1;
    private int seededVertices;
    private int switchedVertices;

    /**
     * Initialize the graph on which
     * this algorithm will work
     * @param graph the graph at hand
     */
    @Override
    public void init(Graph graph) {
        this.graph = (graph.Graph) graph;
    }

    /**
     * Computing step
     */
    @Override
    public void compute() {
        runSimulations();
    }

    /**
     * Return the total node count of this graph
     * @return total node count
     */
    public int getTotalVertices() {
        return graph.getNodeCount();
    }

    /**
     * Return the seeded node count
     * @return seeded node count
     */
    public int getSeededVertices() {
        return seededVertices;
    }

    /**
     * Return the switched node count
     * @return switched node count
     */
    public int getSwitchedVertices() {
        return switchedVertices;
    }

    /**
     * Update the rewardA value
     * @param rewardA the new value
     */
    public void setRewardA(int rewardA) {
        this.rewardA = rewardA;
    }

    /**
     * Update the rewardB value
     * @param rewardB the new value
     */
    public void setRewardB(int rewardB) {
        this.rewardB = rewardB;
    }

    /**
     * Behavior cascade simulation algorithm
     */
    private void runSimulations() {
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

        resetState();
        sleep(1000);

        // Value that determines whether a vertex should switch
        double threshold = ((double) rewardB) / ((double) (rewardA + rewardB));

        // A list of vertices that have switched
        Queue<SingleNode> switched = new LinkedList<>();

        // A list of vertices that will switch at each iteration
        List<SingleNode> toSwitch = new LinkedList<>();

        // A list of all visited vertices
        HashSet<SingleNode> visited = new HashSet<>();

        // Start with randomly choosen nodes
        switched.addAll(getRandomSeededVertices());
        sleep(1000);

        while (! switched.isEmpty()) {

            SingleNode current = switched.poll();

            // determine which neighbors should switch
            Iterator<SingleNode> neighbors = current.getNeighborNodeIterator();
            while (neighbors.hasNext()) {
                SingleNode node = neighbors.next();

                String attr = node.getAttribute("ui.class").toString();
                boolean hasSwitched =  "switched".equals(attr) || "seeded".equals(attr);
                if (! visited.contains(node) && ! hasSwitched) {
                    double prob = computeSwitchingProbability(node);

                    if (prob > threshold) {
                        toSwitch.add(node);
                    }
                }

                // keep track of all the neighbors that were visited
                visited.add(node);
            }

            // make all the changes (vertices that have switched)
            for (SingleNode vertex: toSwitch) {
                vertex.setAttribute("ui.class", "switched");
                sleep(100);
                switched.add(vertex);
                switchedVertices++;
            }

            // clear this list before running next iteration
            toSwitch.clear();
        }
    }

    /**
     * Return a randomly chosen seeded vertices list
     * for simulations purposes
     * @return a list of randomly chosen nodes
     */
    private List<SingleNode> getRandomSeededVertices() {
        List<SingleNode> nodes = new LinkedList<>();
        int limit = (graph.getNodeSet().size() + 1) / 10;
        int start = (int) (Math.random() * graph.getNodeCount());

        for (int i = start; i < start + limit; i++) {
            SingleNode vertex = graph.getNode(i);
            if (vertex != null) {
                vertex.setAttribute("ui.class", "seeded");
                sleep(10);
                nodes.add(vertex);
                seededVertices++;
            }
        }

        return nodes;
    }

    /**
     * Compute the switching probability of a given node
     * @param vertex the node at hand
     * @return the switching probability
     */
    private double computeSwitchingProbability(SingleNode vertex) {
        int switchedNeighbors = 0;
        double totalNeighbors = (double) vertex.getDegree();

        Iterator<SingleNode> neighbors = vertex.getNeighborNodeIterator();
        while(neighbors.hasNext()) {
            SingleNode neighbor = neighbors.next();
            String attr = neighbor.getAttribute("ui.class").toString();
            boolean toSwitch = "seeded".equals(attr) || "switched".equals(attr);
            if (toSwitch) {
                switchedNeighbors++;
            }
        }

        double prob;
        prob = ((double) switchedNeighbors) / totalNeighbors;
        return prob;
    }

    /**
     * Reset to initial state
     */
    private void resetState() {
        for (Node node: graph.getNodeSet()) {
            node.removeAttribute("ui.class");
            node.setAttribute("ui.class", "default");
        }
        seededVertices = 0;
        switchedVertices = 0;
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
