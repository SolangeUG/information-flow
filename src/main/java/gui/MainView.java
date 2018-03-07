package gui;

import algorithm.InformationCascade;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.graphstream.ui.swingViewer.ViewPanel;

import javax.swing.*;
import java.io.InputStream;
import java.util.Objects;

/**
 * This class represents the main GUI
 * of the application
 * @author Solange U. Gasengayire
 */
public class MainView extends StackPane {

    private int aRewardValue = 1;
    private int bRewardValue = 1;

    private ProgressBar progressBar;
    private TextField aTextField;
    private TextField bTextField;
    private Button launchButton;

    private GridPane legend;
    private Label totalLabel;
    private Label seededLabel;
    private Label switchedLabel;

    private InformationCascade algorithm;
    private ViewPanel graphPanel;

    /**
     * Create this view
     */
    public MainView(InformationCascade algorithm, ViewPanel graphPanel) {
        super();

        this.algorithm = algorithm;
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
        aTextField = getTextField("a");
        HBox aBox = new HBox(10);
        aBox.setAlignment(Pos.BASELINE_CENTER);
        aBox.getChildren().addAll(aLabel, aTextField);

        Label bLabel = new Label("Reward B");
        bLabel.getStyleClass().add(".label");
        bTextField = getTextField("b");
        HBox bBox = new HBox(10);
        bBox.setAlignment(Pos.BASELINE_CENTER);
        bBox.getChildren().addAll(bLabel, bTextField);

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(400);
        progressBar.setVisible(false);

        launchButton = getLaunchButton();
        HBox lBox = new HBox(40);
        lBox.setPadding(new Insets(0, 15, 0, 0));
        lBox.setAlignment(Pos.CENTER_RIGHT);
        lBox.getChildren().addAll(progressBar, launchButton);

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
                    if (newValue == null || newValue.isEmpty() || !newValue.matches("\\d*")) {
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

        Button launch = new Button("Launch");

        // a try-with-resources statement
        // → we do not have to explicitly handle closing the stream
        // → it's done for us!
        try (InputStream imageStream = Objects.requireNonNull(getClass().getClassLoader()
                                            .getResourceAsStream("images/launch.png"))
            ) {
            if (imageStream != null) {
                Image launchImage = new Image(imageStream);
                launch.setGraphic(new ImageView(launchImage));
            }

        } catch (Exception exception) {
            // do nothing (for the time being) until we add Logging to our application
        }

        launch.setOnAction(
                event -> {
                    aTextField.setDisable(true);
                    bTextField.setDisable(true);
                    launchButton.setDisable(true);
                    legend.setVisible(false);

                    progressBar.setVisible(true);
                    progressBar.progressProperty().unbind();

                    Task<String> task = launchSimulations();
                    progressBar.progressProperty().bind(task.progressProperty());

                    task.addEventHandler(
                            WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                            event1 -> {
                                aTextField.setDisable(false);
                                bTextField.setDisable(false);
                                launchButton.setDisable(false);

                                totalLabel.setText("initial vertices: "
                                        + String.valueOf(algorithm.getTotalVertices()));
                                seededLabel.setText("seeded vertices: "
                                        + String.valueOf(algorithm.getSeededVertices()));
                                switchedLabel.setText("switched vertices: "
                                        + String.valueOf(algorithm.getSwitchedVertices()));
                                legend.setVisible(true);

                                progressBar.setVisible(false);
                                progressBar.progressProperty().unbind();
                            });
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
        centerPane.setMinSize(1080, 600);

        //TODO #2: handle mouse click events!

        SwingNode node = new SwingNode();
        SwingUtilities.invokeLater(
                () -> node.setContent(graphPanel)
        );
        centerPane.getChildren().add(node);

        legend = getLegendPane();
        legend.setVisible(false);
        centerPane.getChildren().add(legend);
        StackPane.setAlignment(legend, Pos.BOTTOM_LEFT);

        centerPane.requestLayout();
        centerPane.requestFocus();
        return centerPane;
    }

    /**
     * Launch graph simulations with newly chosen
     * values for rewardA and reward B in a separate thread.
     * This is necessary to avoid freezing the GUI.
     */
    private Task<String> launchSimulations() {

        Task<String> simulationTask = new Task<String>() {
            @Override
            protected String call() {
                algorithm.setRewardA(aRewardValue);
                algorithm.setRewardB(bRewardValue);
                algorithm.compute();
                //graph.runSimulations(aRewardValue, bRewardValue);
                return "simulations";
            }
        };
        new Thread(simulationTask).start();

        return simulationTask;
    }

    /**
     * Create a panel for displaying the graph legend
     * @return a gridpane to hold the legend
     */
    private GridPane getLegendPane() {
        GridPane legend = new GridPane();
        legend.setPadding(new Insets(10, 10, 10, 0));
        legend.setStyle("-fx-background-color: #F2F4F4;");
        legend.setMaxSize(210, 40);
        legend.setAlignment(Pos.CENTER);
        legend.setVgap(5);
        legend.setHgap(5);

        Circle blueCircle = new Circle(5);
        blueCircle.setFill(Color.BLUE);
        legend.add(blueCircle, 0, 0);

        totalLabel = new Label("initial vertices: ");
        totalLabel.setStyle("-fx-text-fill: blue;");
        legend.add(totalLabel, 1, 0);

        Circle greenCircle = new Circle(5);
        greenCircle.setFill(Color.rgb(40, 180, 99));
        legend.add(greenCircle, 0, 1);

        seededLabel = new Label("seeded vertices: ");
        seededLabel.setStyle("-fx-text-fill: #28B463;");
        legend.add(seededLabel, 1, 1);

        Circle redCircle = new Circle(5);
        redCircle.setFill(Color.rgb(233, 30, 99));
        legend.add(redCircle, 0, 2);

        switchedLabel = new Label("switched vertices: ");
        switchedLabel.setStyle("-fx-text-fill: #E91E63;");
        legend.add(switchedLabel, 1, 2);

        return legend;
    }

}
