package de.upb.codingpirates.battleships.desktop.placeship;

import de.upb.codingpirates.battleships.desktop.endgame.Endgame;
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
 * Controller Class for the PlaceShips Window.
 */
public class PlaceShipsController implements Initializable {

    @FXML
    private Button btn_rotate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void closeStage() {
        Stage stage = (Stage) btn_rotate.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handlerButton() throws Exception {

        /**ChangeListener<GameView> changeListener = (arg0, arg1, arg2) -> {
            InGameModel inGameModel = new InGameModel(arg2.getContent());
            Stage inGameStage = new Stage();
            try {
                inGameModel.start2();
                closeStage();
            } catch (Exception e) {
                e.printStackTrace();
                inGameStage.setOnCloseRequest((t -> {
                    Platform.exit();
                    System.exit(0);
                }));
            }
        };
    }**/
        Endgame endgame = new Endgame();
        Stage endstage = new Stage();
        try {
            endgame.display(endstage);
            closeStage();
        } catch (IOException e) {
            e.printStackTrace();//TODO
        }
        endstage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
    }

    @FXML
    public void help() throws IOException {
        Help help = new Help();
        try{
            help.display("PlaceShip-Help", "PlaceShip-Help");
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
}