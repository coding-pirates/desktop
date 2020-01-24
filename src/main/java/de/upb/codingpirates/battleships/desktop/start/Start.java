package de.upb.codingpirates.battleships.desktop.start;


import de.upb.codingpirates.battleships.desktop.util.Fullscreen;
import javafx.scene.control.Control;
import javafx.stage.Stage;

/**
 * Class that implements a Window to start the game
 */
public class Start extends Fullscreen {


    private Stage startStage;
    private Control StartController;

    /**
     * method for displaying the startStage
     */
    public void display(Stage startStage) {
        this.startStage = startStage;
        initDimensions(this.startStage);
    }

}
