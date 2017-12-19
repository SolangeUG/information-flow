package graph;

import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.SingleNode;

import java.util.HashSet;

/**
 * This class represents a vertex (a node)
 * in a graph (social network data graph)
 * @author Solange U. Gasengayire
 *
 */
class Vertex extends SingleNode {

    private int vertexId;
    private HashSet<Edge> edges;
    private HashSet<Integer> neighbors;
    private boolean switched = false;

    /**
     * Create a new graph vertex
     * @param graph the graph to which this vertex is added
     * @param id the vertex id
     */
    Vertex(AbstractGraph graph, String id) {
        super(graph, id);
        this.addAttribute("ui.label", id);

        this.vertexId = Integer.valueOf(id);
        this.edges = new HashSet<>();
        this.neighbors = new HashSet<>();
    }

    /**
     * Add a connection to this vertex set of edges
     * @param edge the connection to be added
     * @return true if the edge was successfully added
     *         false otherwise
     */
    boolean addEdge(Edge edge) {
       boolean result = false;
       if (edge != null) {
           result = edges.add(edge);
       }
       if (result) {
           neighbors.add(edge.getEndPoint());
       }
       return result;
    }

    /**
     * Return the value of the switched property
     * @return true if switched is true
     *         false otherwise
     */
    boolean hasSwitched() {
        return this.switched;
    }

    /**
     * Update the value of the switched property
     * @param change the new value
     */
    void setSwitched(boolean change) {
        switched = change;
        if (switched) {
            this.setAttribute("ui.class", "switched");
        } else {
            this.setAttribute("ui.class", "default");
        }
    }

    /**
     * Return this vertex set of neighbors
     * @return neighbors
     */
    HashSet<Integer> getNeighbors() {
        return new HashSet<>(neighbors);
    }

    /**
     * Return a hash code value for this vertex.
     * This method is supported for the benefit of
     * hash tables such as those provided by
     * @see java.util.HashMap
     * @return the hash code value
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + vertexId;
        return result;
    }

    /**
     * Indicate whether some other object is equal to this vertex
     * @param obj some other object
     * @return true if this connection equals to obj
     *         false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Vertex other = (Vertex) obj;
        return this.vertexId == other.vertexId;
    }

}
