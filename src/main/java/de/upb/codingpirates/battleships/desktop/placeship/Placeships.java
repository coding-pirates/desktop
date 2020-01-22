package de.upb.codingpirates.battleships.desktop.placeship;


import de.upb.codingpirates.battleships.logic.Game;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

/**
 * Class that implements a Window to place the ships
 */
public class Placeships  {


    private Stage placeshipsStage;
    private PlaceShipsController placeshipsController;

    /**
     * Start.java Method that creates a new Window and a related Controller.
     */
    public void display(Stage placeshipsStage, Game currentGame, int clientID) throws IOException {
        this.placeshipsStage = placeshipsStage;
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/PlaceshipsView.fxml"));
        AnchorPane pane = loader.load();
        this.placeshipsController = loader.getController();
        placeshipsController.setCurrentGame(currentGame);
        placeshipsController.setClientId(clientID);
        placeshipsController.fieldInit();
        placeshipsController.setShipForm();
        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        placeshipsStage.getIcons().add(icon);
        placeshipsStage.setResizable(false);
        placeshipsStage.setMaximized(true);
        placeshipsStage.setTitle("PlaceShips");
        placeshipsStage.setScene(new Scene(pane));
        placeshipsStage.show();
    }

    /**
     * Closes the Ranking Window
     */
    protected void close() {
        placeshipsStage.close();
    }


}
