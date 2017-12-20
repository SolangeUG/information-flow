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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
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
    private int bRewardValue = 1;

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
    private BorderPane getRootLayout() {
        BorderPane root = new BorderPane();

        root.setTop(getTopPane());
        root.setCenter(getCenterPane());

        root.requestLayout();
        return root;
    }

    /**
     * Return the top panel for this view
     * @return top panel
     */
    private HBox getTopPane() {
        HBox topPane = new HBox(40);
        topPane.setStyle("-fx-background-color : #2980B9;");
        topPane.setPadding(new Insets(15, 0, 18, 15));

        Label aLabel = new Label("Reward A");
        aLabel.getStyleClass().add(".label");
        TextField aTextField = getTextField("a");
        HBox aBox = new HBox(10);
        aBox.setAlignment(Pos.BASELINE_CENTER);
        aBox.getChildren().addAll(aLabel, aTextField);

        Label bLabel = new Label("Reward B");
        bLabel.getStyleClass().add(".label");
        TextField bTextField = getTextField("b");
        HBox bBox = new HBox(10);
        bBox.setAlignment(Pos.BASELINE_CENTER);
        bBox.getChildren().addAll(bLabel, bTextField);

        Button launch = getLaunchButton();
        HBox lBox = new HBox(10);
        lBox.setPadding(new Insets(0, 15, 0, 0));
        lBox.setAlignment(Pos.CENTER_RIGHT);
        lBox.getChildren().add(launch);

        topPane.getChildren().addAll(aBox, bBox, lBox);
        HBox.setHgrow(lBox, Priority.ALWAYS);

        topPane.requestLayout();
        return topPane;
    }

    /**
     * Create and initialize a textfield
     * @param id an id for the textfield
     * @return a textfield
     */
    private TextField getTextField(String id) {
        TextField textField = new TextField();
        textField.setPrefWidth(120.0);
        textField.setAlignment(Pos.CENTER_RIGHT);

        if ("a".equals(id)) {
            textField.setText(String.valueOf(aRewardValue));
        } else {
            textField.setText(String.valueOf(bRewardValue));
        }

        textField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (! newValue.matches("\\d*")) {
                        textField.setText(oldValue);
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
        centerPane.setMinSize(1080, 600);

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
