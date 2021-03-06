import algorithm.InformationCascade;
import graph.Graph;
import gui.MainView;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import util.GraphLoader;

import java.io.InputStream;
import java.util.Objects;
import java.util.Scanner;

/**
 * Main application entry
 * @author Solange U. Gasengayire
 *
 */
public class Application extends javafx.application.Application {

    private Graph graph;

    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui.renderer",
                           "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        ViewPanel graphPanel = initGraph();
        InformationCascade algorithm = new InformationCascade();
        algorithm.init(graph);
        MainView mainView = new MainView(algorithm, graphPanel);

        Scene scene = new Scene(mainView);
        String styleSheet = Application.class
                .getResource("styles/application.css").toExternalForm();
        scene.getStylesheets().add(styleSheet);

        String iconFile = Application.class
                .getResource("images/social_network.png").toExternalForm();
        primaryStage.getIcons().add(new Image(iconFile));
        primaryStage.setTitle("Information Cascades");

        primaryStage.setScene(scene);
        primaryStage.setMinHeight(720);
        primaryStage.setMinWidth(1080);
        primaryStage.show();

        primaryStage.setOnCloseRequest(
                event -> System.exit(0)
        );
    }

    /**
     * Initialize a graph from a file
     * @return a panel for graph visualizations
     */
    private ViewPanel initGraph() {
        graph = new Graph("Information Cascades");
        graph.addAttribute("ui.stylesheet", getStyleSheet());
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");

        graph.setAutoCreate(true);
        graph.setStrict(false);

        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        ViewPanel graphPanel = viewer.addDefaultView(false);
        graphPanel.getCamera().setViewPercent(0.65);
        viewer.enableAutoLayout();

        //TODO #1: use a progress indicator, and run the graph initialization in a different thread!
        //TODO #1: make use of the edX's software design and data structures' Using Threads in Java video!

        // a try-with-resources statement
        // → we do not have to explicitly handle closing the stream
        // → it's done for us!
        try (InputStream graphStream = Objects.requireNonNull(getClass().getClassLoader()
                                            .getResourceAsStream("data/facebook_1000.txt"))
            ) {
            GraphLoader.loadGraph(graph, graphStream);
        } catch (Exception exception) {
            // do nothing (for the time being) until we add Logging to our application
        }

        return graphPanel;
    }

    /**
     * Return graph styling rules
     * @return stylesheet
     */
    private String getStyleSheet() {
        String styleSheet = "";

        // a try-with-resources statement
        // → we do not have to explicitly handle closing the stream and scanner
        // → it's done for us!
        try (InputStream styleStream = Objects.requireNonNull(getClass().getClassLoader()
                                        .getResourceAsStream("styles/graph.css"));
             Scanner scanner = new Scanner(styleStream).useDelimiter("\\A")
            ){

            if (scanner.hasNext()) {
                styleSheet = scanner.next();
            }

        } catch (Exception exception) {
            // do nothing, we'll use the default stylesheet
        }
        return styleSheet;
    }
}
