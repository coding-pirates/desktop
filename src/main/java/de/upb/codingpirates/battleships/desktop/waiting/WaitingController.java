package de.upb.codingpirates.battleships.desktop.waiting;

import de.upb.codingpirates.battleships.client.ListenerHandler;
import de.upb.codingpirates.battleships.client.listener.GameStartNotificationListener;
import de.upb.codingpirates.battleships.desktop.settings.Settings;
import de.upb.codingpirates.battleships.desktop.util.Help;
import de.upb.codingpirates.battleships.network.message.notification.GameStartNotification;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WaitingController implements Initializable, GameStartNotificationListener {

    @FXML
    private Button closeButton;

    public WaitingController(){
        ListenerHandler.registerListener(this);
    }

    /**
     * Initial Method.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }
    @Override
    public void onGameStartNotification(GameStartNotification message, int messageId){
        Platform.runLater(()-> closeStage());
       /* Platform.runLater(()->{
                InGameModel inGameModel = new InGameModel();
        Stage inGameStage = new Stage();
        try {
            inGameModel.start(inGameStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        }); */
       /* LobbyController lobbyControl = new LobbyController();
        lobbyControl.parseToGameView(message.getGames());
        */
    }
    @FXML
    public void closeStage(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handlerButton(){
        closeStage();
        //this.onLobbyResponse();
    }

    @FXML
    public void help() throws IOException {
        Help help = new Help();
        try{
            help.display("Settings-Help", "Settings");
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
}
