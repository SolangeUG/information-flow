package gui;

import graph.Graph;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.controlsfx.tools.Borders;
import org.graphstream.ui.swingViewer.ViewPanel;

import javax.swing.*;
import java.util.Objects;

/**
 * This class represents the main GUI
 * of the application
 * @author Solange U. Gasengayire
 */
public class MainView extends StackPane {

    private int aRewardValue = 1;
    private int bRewardValue = 2;

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
        this.requestLayout();
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
        Node wrappedGrid = Borders.wrap(leftPane)
                .lineBorder().color(Color.WHITE).buildAll();

        StackPane centerPane = getCenterPane();
        root.getChildren().addAll(wrappedGrid, centerPane);

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
        leftPane.setHgap(15);
        leftPane.setVgap(20);

        Label aLabel = new Label("Reward A");
        aLabel.getStyleClass().add(".label");
        leftPane.add(aLabel, 0, 0);

        TextField aTextField = getTextField("a");
        leftPane.add(aTextField, 1, 0);

        Label bLabel = new Label("Reward B");
        bLabel.getStyleClass().add(".label");
        leftPane.add(bLabel, 0, 1);

        TextField bTextField = getTextField("b");
        leftPane.add(bTextField, 1, 1);

        Button launch = getLaunchButton();
        HBox container = new HBox(10);
        container.setAlignment(Pos.BOTTOM_RIGHT);
        container.getChildren().add(launch);
        leftPane.add(container, 1, 4);

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
                        textField.setText(newValue.replaceAll("[^\\d]", "0"));
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

        String imageFile = Objects.requireNonNull(getClass().getClassLoader()
                .getResource("images/launch.png")).getPath();
        Button launch;
        if (imageFile != null) {
            imageFile = "file://" + imageFile;
            Image launchImage = new Image(imageFile);
            launch = new Button("Launch", new ImageView(launchImage));
        } else {
            launch = new Button("Launch");
        }

        launch.setOnAction(
                event -> launchSimulations()
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
        centerPane.setMinSize(980, 600);

        SwingNode node = new SwingNode();
        SwingUtilities.invokeLater(
                () -> node.setContent(graphPanel)
        );
        centerPane.getChildren().add(node);

        centerPane.requestLayout();
        centerPane.requestFocus();
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
