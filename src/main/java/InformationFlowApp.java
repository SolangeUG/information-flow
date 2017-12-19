import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main application entry
 * @author Solange U. Gasengayire
 *
 */
public class InformationFlowApp extends Application {

    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui.renderer",
                           "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {

    }
}
