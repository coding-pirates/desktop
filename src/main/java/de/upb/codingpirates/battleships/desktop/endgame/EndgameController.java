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
    private Map<Integer, Integer> points;
    private Collection<Client> players;


    /**
     * Initial Method.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }

    public  void setPoints(Map<Integer, Integer> points){
        this.points = points;
    }

    public void setPlayers (Collection<Client>players){
        this.players = players;
    }

    @FXML
    public void help() throws IOException {
        Help help = new Help();
        try{
            help.display("Endgame-Help", "Endgame-Help");
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
        }
        catch (IOException e) {
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
        Stage rankingstage = new Stage();
        try {
            ranking.display(rankingstage);

        } catch (IOException e) {
            e.printStackTrace();//TODO
        }
        ranking.setPlayer(players);
        ranking.sortPoints(points);
        rankingstage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
    }

    @FXML
    public void lobby() throws Exception{
        Lobby lobby = new Lobby();
        Stage lobbyStage = new Stage();
        try {
            lobby.display(lobbyStage);
            closeStage();
        } catch (IOException e) {
            e.printStackTrace();//TODO
        }
        lobbyStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public void closeStage(){
        Stage stage = (Stage) btn_lobby.getScene().getWindow();
        stage.close();
    }
}
