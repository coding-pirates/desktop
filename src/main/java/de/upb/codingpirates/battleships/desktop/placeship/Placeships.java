package de.upb.codingpirates.battleships.desktop.placeship;


import de.upb.codingpirates.battleships.logic.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.*;

/**
 * Class that implements a Window to place the ships
 */
public class Placeships extends Application {


    private Stage placeshipsStage;
    private PlaceShipsController placeshipsController;

    /**
     * Start Method that creates a new Window and a related Controller.
     */
    public void start(Stage placeshipsStage) throws Exception {
        this.placeshipsStage = placeshipsStage;
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/PlaceshipsView.fxml"));
        AnchorPane pane = loader.load();
        this.placeshipsController = loader.getController();
        Client c1 = new Client(1, "player1");
        Client c2 = new Client(2,"player2");
        Collection<Client> liste= Collections.unmodifiableList(new ArrayList<Client>() {{
            add(c1);
            add(c2);
        }});
        placeshipsController.fieldInit(liste);
        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        placeshipsStage.getIcons().add(icon);
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
