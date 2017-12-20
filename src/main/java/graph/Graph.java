package graph;

import org.graphstream.graph.implementations.SingleGraph;

import java.util.HashMap;

/**
 * Graph implementation
 * @author Solange U. Gasengayire
 *
 */
public class Graph  extends SingleGraph {

    private HashMap<Integer, Vertex> vertices;
    private Vertex seededVertex;

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
     * Return a randomly chosen seeded vertex id
     * for simulations purposes
     * @return a randomly choosen vertex id
     */
    public String getSeededVertex() {
        seededVertex = getRandomVertex();
        return seededVertex.getId();
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

        double threshold = ((double) rewardB) / ((double) (rewardA + rewardB));

        // TODO : implement simulation algorithm


    }

    /**
     * Return a randomly chosen seeded vertex
     * for simulations purposes
     * @return a randomly chosen vertex
     */
    private Vertex getRandomVertex() {
        int randomIndex = (int)(Math.random() * vertices.size());
        Vertex random = null;
        while (random == null) {
            random = vertices.get(randomIndex);
        }
        return random;
    }

}
