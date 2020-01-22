package de.upb.codingpirates.battleships.desktop.endgame;

import de.upb.codingpirates.battleships.desktop.lobby.Lobby;
import de.upb.codingpirates.battleships.desktop.ranking.Ranking;
import de.upb.codingpirates.battleships.desktop.settings.Settings;
import de.upb.codingpirates.battleships.desktop.util.Help;
import de.upb.codingpirates.battleships.logic.Client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Controller Class for the PlaceShips Window.
 */
public class EndgameController implements Initializable {

    @FXML
    Button btn_lobby;
    @FXML
    Label first;
    @FXML
    Label second;
    @FXML
    Label third;
    private Map<Integer, Integer> points = null;
    private Collection<Client> players = null;


    private int clientID;

    /**
     * Initial Method.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }

    public void setPoints(Map<Integer, Integer> points) {
        this.points = points;
    }

    public void setPlayers(Collection<Client> players) {
        this.players = players;
    }

    @FXML
    public void help() throws IOException {
        Help help = new Help();
        try{
            help.display("Endgame-Help");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    @FXML
    public void settings() throws Exception {

        Settings settings = new Settings();
        Stage settingsStage = new Stage();
        try {
            settings.display(settingsStage);
        } catch (IOException e) {
            e.printStackTrace();//TODO
        }
        settingsStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
    }

    @FXML
    public void ranking() throws Exception {

        Ranking ranking = new Ranking();
        Stage rankingStage = new Stage();
        try {
            ranking.display(rankingStage);

        } catch (IOException e) {
            e.printStackTrace();//TODO
        }
        ranking.setPlayer(players);
        ranking.sortPoints(points);

    }

    @FXML
    public void lobby() throws Exception {
        Lobby lobby = new Lobby();
        Stage lobbyStage = new Stage();

        lobbyStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
        try {
            lobby.display(lobbyStage, clientID);
            closeStage();
        } catch (IOException e) {
            e.printStackTrace();//TODO
        }

    }

    public void closeStage() {
        Stage stage = (Stage) btn_lobby.getScene().getWindow();
        stage.close();
    }

    public void setClientID(int clientID){
        this.clientID = clientID;
    }


    public void placement() {

        int firstPoints = -3000;
        int secondPoints = -3000;
        int thirdPoints = -3000;

        int firstID = -1;
        int secondId = -1;
        int thirdId = -1;

        if (points != null) {
            for (Integer player : points.keySet()) {
                if(points.get(player)> firstPoints){
                    thirdPoints = secondPoints;
                    secondPoints = firstPoints;
                    firstPoints = points.get(player);

                    thirdId = secondId;
                    secondId = firstID;
                    firstID = player;
                }
                else if (points.get(player) > secondPoints){
                    thirdPoints = secondPoints;
                    secondPoints = points.get(player);

                    thirdId = secondId;
                    secondId = player;
                }
                else if (players.size()> 2 && points.get(player) > thirdPoints){
                    thirdPoints = points.get(player);

                    thirdId = player;
                }

            }

            Client cfirst = null;
            Client csecond = null;
            Client cthird = null;



            for(Client client : players){
                if(client.getId()==firstID){
                    cfirst = client;
                    System.out.println(cfirst);
                }
                else if(client.getId()==secondId){
                    csecond = client;
                    System.out.println(csecond
                    );
                }
                else if(players.size() > 2){
                    if(client.getId()==thirdId){
                        cthird = client;
                    }
                }

            }

            try {
                first.setText(cfirst.getName());
                second.setText(csecond.getName());
                if(players.size()>2)
                third.setText(cthird.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}
