package de.upb.codingpirates.battleships.desktop.endgame;

import de.upb.codingpirates.battleships.desktop.lobby.Lobby;
import de.upb.codingpirates.battleships.desktop.ranking.Ranking;
import de.upb.codingpirates.battleships.desktop.settings.Settings;
import de.upb.codingpirates.battleships.desktop.util.Help;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller Class for the Endgame Window.
 */
public class EndgameController implements Initializable {

    @FXML
    Button btn_lobby;

    /**
     * Initial Method.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }

    /**
     * starts the HelpView with accessibility tools in an extra window
     * @throws IOException
     */
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

    /**
     * starts the SettingsView in an extra window
     * @throws Exception
     */
    @FXML
    public void settings() throws Exception {

        Settings settings = new Settings();
        Stage settingsStage;
        settingsStage = new Stage();
        try {
            settings.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        settingsStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
    }

    /**
     * shows the ranking in an extra window
     * @throws Exception
     */
    @FXML
    public void ranking() throws Exception {

         Ranking ranking = new Ranking();
         Stage rankingStage = new Stage();
        try {
            ranking.start();
        } catch (Exception e) {
            System.out.println("Fehler: " + e);
        }rankingStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
    }

    /**
     * starts the lobbyView and closes this endgameView
     * @throws Exception
     */
    @FXML
    public void lobby() throws Exception{
        Lobby lobby = new Lobby();
        Stage lobbyStage = new Stage();
        try {
            lobby.start(lobbyStage);
            closeStage();
        } catch (IOException e) {
            e.printStackTrace();//TODO
        }
        lobbyStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
    }

    /**
     * closes the endgameView
     */
    public void closeStage(){
        Stage stage = (Stage) btn_lobby.getScene().getWindow();
        stage.close();
    }
}
