package de.upb.codingpirates.battleships.desktop.endgame;

import de.upb.codingpirates.battleships.desktop.settings.Settings;
import de.upb.codingpirates.battleships.desktop.util.Help;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller Class for the PlaceShips Window.
 */
public class EndgameController implements Initializable {


    /**
     * Initial Method.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

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
            settings.start();
        }
        catch (IOException e) {
            e.printStackTrace();//TODO
        }
        settingsStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
    }

}
