package de.upb.codingpirates.battleships.desktop.ranking;

import de.upb.codingpirates.battleships.logic.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import java.util.ResourceBundle;

/**
 * Controller Class for the Ranking Window.
 */
public class RankingController implements Initializable {

    @FXML
    TableView<Player> tableRangList;
    @FXML
    TableColumn<Player, String> name;
    @FXML
    TableColumn<Player, Integer> rang;
    @FXML
    TableColumn<Player, Integer> points;

    private Ranking ranking;

    /**
     * Set Method for the Ranking.
     *
     * @param ranking The related Ranking Class.
     */
    public void setRanking(Ranking ranking) {
        this.ranking = ranking;
    }

    /**
     * Initial Method.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }

    /**
     * Fills the Ranking Window with the Placement of every Player, it's Name and Points.
     *
     * @param pointsList Sorted PlayerIds List
     * @param pointsMap  Map of PlayerIds and Points
     * @param players    Unsorted PlayerList
     */
    public void setPointsList(Queue<Integer> pointsList, Map<Integer, Integer> pointsMap, Collection<Client> players) {  //ToDo Tabelle füllen
        ObservableList<Player> playerList = null;
        playerList = FXCollections.observableArrayList();
        int i = 1;
        while (!pointsList.isEmpty()) {
            int playerInt = pointsList.remove();
            String playerName = "";
            for (Client player:players) {
                if(player.getId() == playerInt) {
                    playerName = player.getName();
                    break;
                }
            }
            //TODO
            //String Player is just a number
            int points = pointsMap.get(playerInt);
            int rang = i;
            i++;
            playerList.add(new Player(rang, playerName, points));

        }
        rang.setCellValueFactory(new PropertyValueFactory<>("rang"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        points.setCellValueFactory(new PropertyValueFactory<>("points"));
        tableRangList.setItems(playerList);
    }

}
