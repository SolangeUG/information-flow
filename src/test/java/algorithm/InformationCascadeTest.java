package algorithm;

import graph.Graph;
import org.graphstream.graph.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.GraphLoader;

import java.io.InputStream;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class is a JUnit test class for the
 * @see InformationCascade class
 * @author Solange U. Gasengayire
 */
@Disabled
class InformationCascadeTest {

    private InformationCascade algorithm;

    private Graph exampleGraph;
    private Graph f1000Graph;
    private Graph ucsdGraph;

    @BeforeEach
    void setUp() {
        exampleGraph = new Graph("Example Graph");
        exampleGraph.setAutoCreate(true);
        exampleGraph.setStrict(false);

        f1000Graph = new Graph("Facebook 1000 Graph");
        f1000Graph.setAutoCreate(true);
        f1000Graph.setStrict(false);

        ucsdGraph = new Graph("UCSD Graph");
        ucsdGraph.setAutoCreate(true);
        ucsdGraph.setStrict(false);

        algorithm = new InformationCascade();
    }

    @Test
    @DisplayName("Run simulation on a small size graph data")
    void simulationOnSmallGraph() {

        InputStream exampleStream = Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream("data/example.txt"));
        GraphLoader.loadGraph(exampleGraph, exampleStream);

        runSimulations(exampleGraph);
    }

    @Test
    @DisplayName("Run simulation on a medium size graph data")
    void simulationOnMediumGraph() {

        InputStream f1000Stream = Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream("data/facebook_1000.txt"));
        GraphLoader.loadGraph(f1000Graph, f1000Stream);

        runSimulations(f1000Graph);
    }

    @Test
    @DisplayName("Run simulation on a large size graph data")
    void simulationOnLargeGraph() {

        InputStream ucsdStream = Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream("data/facebook_ucsd.txt"));
        GraphLoader.loadGraph(ucsdGraph, ucsdStream);

        runSimulations(ucsdGraph);
    }

    /**
     * Utility method
     * @param graph the graph to run simulations on
     */
    private void runSimulations(Graph graph) {

        algorithm.init(graph);
        algorithm.compute();

        int seeded = 0;
        int switched = 0;

        for (Node node : graph.getNodeSet()) {
            String attr = node.getAttribute("ui.class").toString();
            if ("switched".equals(attr)) {
                switched++;
            } else if ("seeded".equals(attr)) {
                seeded++;
            }
        }

        assertEquals(seeded, algorithm.getSeededVertices());
        assertEquals(switched, algorithm.getSwitchedVertices());
    }

}
