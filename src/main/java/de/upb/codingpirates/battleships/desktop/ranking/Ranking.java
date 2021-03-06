package de.upb.codingpirates.battleships.desktop.ranking;

import de.upb.codingpirates.battleships.desktop.util.FxmlLoader;
import de.upb.codingpirates.battleships.logic.Client;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.*;

/**
 * Class that implements a Window with the Ranking when i Game is finished.
 */
public class Ranking implements FxmlLoader {

    private Stage rankingStage;
    private RankingController rankingController;

    /**
     * Start.java Method that creates a new Window and a related Controller.
     */
    public void display(Stage rankingStage) throws Exception {
        this.rankingStage = rankingStage;
        rankingStage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = this.getLoader("RankingView");
        AnchorPane pane = loader.load();
        this.rankingController = loader.getController();
        this.rankingController.setRanking(this);
        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        this.rankingStage.getIcons().add(icon);
        this.rankingStage.setResizable(false);
        this.rankingStage.setTitle("Rangliste");
        this.rankingStage.setScene(new Scene(pane));
        this.rankingStage.initStyle(StageStyle.UNDECORATED);
        this.rankingStage.show();
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
