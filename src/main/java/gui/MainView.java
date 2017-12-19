package gui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 * This class represents the main GUI
 * of the application
 * @author Solange U. Gasengayire
 */
public class MainView extends StackPane {

    private int aRewardValue = 1;
    private int bRewardValue = 2;
    private int seededNodeId = 0;
    private StackPane centerPane;

    /**
     * Create this view
     */
    public MainView() {
        super();

        this.getChildren().add(getRootLayout());
    }

    /**
     * Return the main pane of this view
     * It'll be used to hold the graph visualizations
     * @return centerPane
     */
    public StackPane getCenterPane() {
        return this.centerPane;
    }

    /**
     * Return the root layout for this view
     * @return root layout
     */
    private HBox getRootLayout() {
        HBox root = new HBox();
        root.getStyleClass().add(".root");
        root.setPadding(new Insets(15));
        root.setSpacing(10);

        //TODO : add left and center panes
        GridPane leftPane = getLeftPane();
        initCenterPane();
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

        //TODO : add aReward, bReward, seededNode and legend components
        Label aLabel = new Label("Reward A");
        TextField aTextField = new TextField();

        leftPane.requestLayout();
        return leftPane;
    }

    /**
     * Init the center panel for this view
     * This is the panel that will hold the graph visualizations
     */
    private void initCenterPane() {
        this.centerPane = new StackPane();
        centerPane.requestLayout();
    }





}
