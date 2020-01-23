package de.upb.codingpirates.battleships.desktop.start;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Class that implements a Window to start the game
 */
public class Start extends Application {


    private Stage startStage;
    private Control StartController;

    @Override
    public void start(Stage primaryStage) throws Exception {
    }

    /**
     * method for displaying the {@param startStage}
     * @param startStage
     */
    public void display(Stage startStage) {
        this.startStage = startStage;
        initDimensions(this.startStage);
    }

    /**
     * this method sets the dimensions for {@param stage}. Currently sets width and height to display size. Also removes the windows task bar.
     * @param stage instance of the stage the dimensions shall be set
     */
    private void initDimensions(Stage stage) {

        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        stage.setMaxWidth(screenWidth);
        stage.setMaxHeight(screenHeight);
        stage.setHeight(screenHeight);
        stage.setWidth(screenWidth);
        //remove the windows taskbar
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.show();
    }
}
