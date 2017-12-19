package util;

import graph.Graph;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Utility class to add vertices and edges to a graph
 * @author UCSD MOOC Development Team
 *
 */
public class GraphLoader {

    /**
     * Loads graph with data from a file.
     * The file should consist of lines with 2 integers each, corresponding
     * to a "from" vertex and a "to" vertex.
     */
    public static void loadGraph(Graph graph, String filename) {
        Set<Integer> seen = new HashSet<>();
        Scanner sc;
        try {
            sc = new Scanner(new File(filename));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        // Iterate over the lines in the file, adding new
        // vertices as they are found and connecting them with edges.
        while (sc.hasNextInt()) {
            int v1 = sc.nextInt();
            int v2 = sc.nextInt();
            if (!seen.contains(v1)) {
                graph.addVertex(v1);
                seen.add(v1);
            }
            if (!seen.contains(v2)) {
                graph.addVertex(v2);
                seen.add(v2);
            }
            graph.addEdge(v1, v2);
        }

        sc.close();
    }

}
