package de.upb.codingpirates.battleships.desktop;

import de.upb.codingpirates.battleships.logic.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.*;

/**
 * Class that implements a Window with the Ranking when i Game is finished.
 */
public class Ranking extends Application {

  private Stage rankingStage;
  private RankingController rankingController;
  private Collection<Client> players;

  /**
   * Start Method that creates a new Window and a related Controller.
   */
  public void start(Stage rankingStage) throws Exception {
    this.rankingStage = rankingStage;
    FXMLLoader loader = new FXMLLoader(getClass().getResource("RankingView.fxml"));
    AnchorPane pane = loader.load();
    this.rankingController = loader.getController();
    rankingController.setRanking(this);
    rankingStage.setTitle("Rangliste");
    rankingStage.setResizable(false);
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
   * @param players Collection of Players
   */
  public void setPlayer(Collection<Client> players) {
    this.players = players;
  }

  /**
   * Sorts the PlayerIds by Points Decreasing.
   * Creates a pointsList with the sorted PlayerIds
   * @param points Map of PlayerIds and Points
   */
  public void sortPoints(Map<Integer, Integer> points) {
    Queue <Integer> pointsList = new LinkedList<Integer>();
    Map<Integer,Integer> pointsMap = points;
    while(points.isEmpty()==false) {
      Integer akt = 0;
      Set<Integer> clientIds = points.keySet();
      for (Integer clientId : clientIds) {
        if(points.get(clientId)>=akt) {
          akt = clientId;
        }
      }
      pointsList.add(akt);
      points.remove(akt);
    }
    rankingController.setPointsList(pointsList, pointsMap, players);
  }


}
