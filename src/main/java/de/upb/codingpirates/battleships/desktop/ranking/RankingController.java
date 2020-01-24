package de.upb.codingpirates.battleships.desktop.ranking;

import de.upb.codingpirates.battleships.desktop.util.Player;
import de.upb.codingpirates.battleships.logic.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;

/**
 * Controller Class for the Ranking Window.
 */
public class RankingController implements Initializable {

    //view
    /**
     * list for ranking with all players
     */
    @FXML
    TableView<Player> tableRangList;
    /**
     * column player in table
     */
    @FXML
    TableColumn<Player, String> name;
    /**
     * column rang in table according to the each player
     */
    @FXML
    TableColumn<Player, Integer> rang;
    /**
     * column points in table according to each player
     */
    @FXML
    TableColumn<Player, Integer> points;

    private Collection<Client> players;

    private Ranking ranking;

    /**
     * Initial Method.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }

    /**
     * Set Method for the Ranking.
     *
     * @param ranking The related Ranking Class.
     */
    public void setRanking(Ranking ranking) {
        this.ranking = ranking;
    }


    public Collection<Client> getPlayers() {
        return players;
    }

    public void setPlayers(Collection<Client> players) {
        this.players = players;
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

    /**
     * Sorts the PlayerIds by Points Decreasing.
     * Creates a pointsList with the sorted PlayerIds
     *
     * @param points Map of PlayerIds and Points
     */
    public void sortPoints(Map<Integer, Integer> points) {
        Map<Integer, Integer> tempPoints = new HashMap<>(points);
        Queue<Integer> playerIDS = new LinkedList<>();
        while (!tempPoints.isEmpty()) {
            Integer aktMaxPunkt = -3000;
            Integer aktMaxClientId =0;
            Set<Integer> clientIds = tempPoints.keySet();
            for (Integer clientId : clientIds) {
                if (tempPoints.get(clientId) >= aktMaxPunkt) {
                    aktMaxPunkt = points.get(clientId);
                    aktMaxClientId = clientId;

                }
            }
            playerIDS.add(aktMaxClientId);
            tempPoints.remove(aktMaxClientId);
        }
        this.setPointsList(playerIDS, points, players);
    }

}
