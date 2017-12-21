import algorithm.InformationCascade;
import graph.Graph;
import gui.MainView;
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
        primaryStage.setMinHeight(980);
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

        String styleSheet = Application.class.getResource("styles/graph.css").getFile();
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

        String graphFile = Application.class
                .getResource("data/facebook_1000.txt").getFile().substring(1);
        GraphLoader.loadGraph(graph, graphFile);

        return graphPanel;
    }
}
