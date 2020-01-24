package de.upb.codingpirates.battleships.desktop.endgame;


import de.upb.codingpirates.battleships.desktop.util.Fullscreen;
import de.upb.codingpirates.battleships.logic.Client;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Collection;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Class that implements a Window to place the ships
 */
public class Endgame extends Fullscreen {


    private Stage endStage;
    private EndgameController EndgameController;
    private Map<Integer, Integer> points;
    private Collection<Client> players = null;

    /**
     * Start.java Method that creates a new Window and a related Controller.
     */
    public void display(Stage endStage, Map<Integer, Integer> points, Collection <Client> players, int clientID) throws Exception {
        this.endStage = endStage;
        this.points = points;
        this.players = players;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EndgameView.fxml"), ResourceBundle.getBundle("lang/desktop"));
        AnchorPane pane = loader.load();
        this.EndgameController = loader.getController();
        EndgameController.setPoints(points);
        EndgameController.setPlayers(players);
        EndgameController.placement();
        EndgameController.setClientID(clientID);
        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        endStage.getIcons().add(icon);
        endStage.setTitle("Endgame");
        endStage.setScene(new Scene(pane));
        super.display(endStage);
    }

}
