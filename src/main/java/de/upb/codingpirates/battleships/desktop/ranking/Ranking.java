package de.upb.codingpirates.battleships.desktop.ranking;

import de.upb.codingpirates.battleships.logic.Client;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.*;

/**
 * Class that implements a Window with the Ranking when i Game is finished.
 */
public class Ranking {

    private Stage rankingStage;
    private RankingController rankingController;

    /**
     * Start.java Method that creates a new Window and a related Controller.
     */
    public void display(Stage rankingStage) throws Exception {
        this.rankingStage = rankingStage;
        rankingStage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RankingView.fxml"));
        AnchorPane pane = loader.load();
        this.rankingController = loader.getController();
        rankingController.setRanking(this);
        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        rankingStage.getIcons().add(icon);
        rankingStage.setResizable(false);
        rankingStage.setTitle("Rangliste");
        rankingStage.setScene(new Scene(pane));
        rankingStage.show();
    }

    /**
     * Closes the Ranking Window
     */
    protected void close() {
        rankingStage.close();
    }

    /**
     * Set Method for Players.
     *
     * @param players Collection of Players
     */
    public void setPlayer(Collection<Client> players) {
        rankingController.setPlayers(players);
    }

    /**
     * Sorts the PlayerIds by Points Decreasing.
     * Creates a pointsList with the sorted PlayerIds
     *
     * @param points Map of PlayerIds and Points
     */
    public void sortPoints(Map<Integer, Integer> points) {
       rankingController.sortPoints(points);
    }


}
