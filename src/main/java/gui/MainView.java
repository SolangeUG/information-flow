package gui;

import graph.Graph;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.graphstream.ui.swingViewer.ViewPanel;

import javax.swing.*;

/**
 * This class represents the main GUI
 * of the application
 * @author Solange U. Gasengayire
 */
public class MainView extends StackPane {

    private int aRewardValue = 1;
    private int bRewardValue = 2;

    private Label randomNodeLabel;
    private ViewPanel graphPanel;
    private Graph graph;

    /**
     * Create this view
     */
    public MainView(Graph graph, ViewPanel graphPanel) {
        super();

        this.graph = graph;
        this.graphPanel = graphPanel;
        this.getStyleClass().add("default");
        this.getChildren().add(getRootLayout());
    }

    /**
     * Return the root layout for this view
     * @return root layout
     */
    private HBox getRootLayout() {
        HBox root = new HBox();
        root.getStyleClass().add(".default");
        root.setPadding(new Insets(15));
        root.setSpacing(10);

        GridPane leftPane = getLeftPane();
        StackPane centerPane = getCenterPane();
        root.getChildren().addAll(leftPane, centerPane);

        root.requestLayout();
        return root;
    }

    /**
     * Return the left panel for this view
     * @return left panel
     */
    private GridPane getLeftPane() {
        GridPane leftPane = new GridPane();
        leftPane.getStyleClass().add(".default");

        leftPane.setPadding(new Insets(25));
        leftPane.setAlignment(Pos.CENTER);
        leftPane.setHgap(10);
        leftPane.setVgap(10);

        Label aLabel = new Label("Reward A");
        leftPane.add(aLabel, 0, 0);

        TextField aTextField = getTextField("a");
        leftPane.add(aTextField, 1, 0);

        Label bLabel = new Label("Reward B");
        leftPane.add(bLabel, 0, 1);

        TextField bTextField = getTextField("b");
        leftPane.add(bTextField, 1, 1);

        randomNodeLabel = new Label("[Seeded node id]");
        leftPane.add(randomNodeLabel, 1, 2);

        Button launch = getLaunchButton();
        leftPane.add(launch, 0, 3);

        leftPane.requestLayout();
        return leftPane;
    }

    /**
     * Create and initialize a textfield
     * @param id an id for the textfield
     * @return a textfield
     */
    private TextField getTextField(String id) {
        TextField textField = new TextField();
        textField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (! newValue.matches("\\d*")) {
                        textField.setText(newValue.replaceAll("[^\\d]", ""));
                    } else {
                        if ("a".equals(id)) {
                            aRewardValue = Integer.valueOf(newValue);
                        } else {
                            bRewardValue = Integer.valueOf(newValue);
                        }
                    }
                }
        );
        return textField;
    }

    /**
     * Create and initialize a launch button
     * @return a button
     */
    private Button getLaunchButton() {
        String imageFile = getClass()
                .getResource("images/launch.png").getFile().substring(1);
        Image launchImage = new Image(imageFile);

        Button launch = new Button("Launch", new ImageView(launchImage));
        launch.onMouseClickedProperty().addListener(
                event -> {
                    String randomNode = graph.getSeededVertex();
                    randomNodeLabel.setText("Seeded node id is " + randomNode);
                    launchSimulations();
                }
        );
        return launch;
    }

    /**
     * Return the center panel for this view.
     * This is the panel that holds the graph visualizations.
     * @return the center pane
     */
    private StackPane getCenterPane() {
        StackPane centerPane = new StackPane();
        centerPane.setMinSize(1280, 600);

        SwingNode node = new SwingNode();
        SwingUtilities.invokeLater(
                () -> node.setContent(graphPanel)
        );
        centerPane.getChildren().add(node);

        centerPane.requestLayout();
        return centerPane;
    }

    /**
     * Launch graph simulations with newly chosen
     * values for rewardA and reward B in a separate thread.
     * This is necessary to avoid freezing the GUI.
     */
    private void launchSimulations() {

        Task<String> simulationTask = new Task<String>() {
            @Override
            protected String call() {
                graph.runSimulations(aRewardValue, bRewardValue);
                return "simulations";
            }
        };
        new Thread(simulationTask).start();
    }

}
