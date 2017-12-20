import graph.Graph;
import gui.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import util.GraphLoader;

/**
 * Main application entry
 * @author Solange U. Gasengayire
 *
 */
public class InformationFlowApp extends Application {

    private Graph graph;

    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui.renderer",
                           "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        ViewPanel graphPanel = initGraph();
        MainView mainView = new MainView(graph, graphPanel);

        Scene scene = new Scene(mainView);
        String styleSheet = InformationFlowApp.class
                .getResource("styles/application.css").toExternalForm();
        scene.getStylesheets().add(styleSheet);

        String iconFile = InformationFlowApp.class
                .getResource("images/social_network.png").toExternalForm();
        primaryStage.getIcons().add(new Image(iconFile));
        primaryStage.setTitle("Information Cascades");

        primaryStage.setScene(scene);
        primaryStage.setMinHeight(880);
        primaryStage.setMinWidth(980);
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

        String styleSheet = InformationFlowApp.class.getResource("styles/graph.css").getFile();
        styleSheet = "url('file://" + styleSheet + "')";
        graph.addAttribute("ui.stylesheet", styleSheet);
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");

        graph.setAutoCreate(true);
        graph.setStrict(false);

        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        ViewPanel graphPanel = viewer.addDefaultView(false);
        graphPanel.getCamera().setViewPercent(0.65);
        viewer.enableAutoLayout();

        String graphFile = InformationFlowApp.class
                .getResource("data/facebook_1000.txt").getFile().substring(1);
        GraphLoader.loadGraph(graph, graphFile);

        return graphPanel;
    }
}
