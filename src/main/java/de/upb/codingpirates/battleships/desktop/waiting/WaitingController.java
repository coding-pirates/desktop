package de.upb.codingpirates.battleships.desktop.waiting;

import de.upb.codingpirates.battleships.client.ListenerHandler;
import de.upb.codingpirates.battleships.client.listener.GameInitNotificationListener;
import de.upb.codingpirates.battleships.client.listener.GameLeaveResponseListener;
import de.upb.codingpirates.battleships.client.listener.GameStartNotificationListener;
import de.upb.codingpirates.battleships.desktop.ingame.InGame;
import de.upb.codingpirates.battleships.desktop.lobby.Lobby;
import de.upb.codingpirates.battleships.desktop.settings.Settings;
import de.upb.codingpirates.battleships.desktop.util.Help;
import de.upb.codingpirates.battleships.logic.Client;
import de.upb.codingpirates.battleships.logic.ClientType;
import de.upb.codingpirates.battleships.logic.Game;
import de.upb.codingpirates.battleships.network.message.notification.GameInitNotification;
import de.upb.codingpirates.battleships.network.message.notification.GameStartNotification;
import de.upb.codingpirates.battleships.network.message.response.GameLeaveResponse;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

public class WaitingController implements Initializable, GameStartNotificationListener, GameInitNotificationListener, GameLeaveResponseListener {

    @FXML
    private Button backButton;
    private WaitingModel model;
    private Game currentGame;
    private Collection<Client> clientList;
    private int clientID;
    
    public WaitingController(){
        ListenerHandler.registerListener(this);
    }

    public void setModel(){
        this.model = new WaitingModel(currentGame,clientID);
    }


    /**
     * Initial Method.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }
    @Override
    public void onGameStartNotification(GameStartNotification message, int messageId){
        Platform.runLater(()->{
            InGame inGame = new InGame();
            Stage inGameStage = new Stage();

            inGameStage.setOnCloseRequest(t -> {
                Platform.exit();
                System.exit(0);
            });

        try {
            inGame.start(inGameStage,currentGame, ClientType.SPECTATOR);
            closeStage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        });
       /* LobbyController lobbyControl = new LobbyController();
        lobbyControl.parseToGameView(message.getGames());
        */
    }
    @Override
    public void onGameInitNotification(GameInitNotification message, int clientId) {
        this.clientList = message.getClientList();
    }
    @FXML
    public void backLobby(){
        model.sendLeaveRequest();
    }

    public void closeStage(){
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }


    @FXML
    public void help() throws IOException {
        Help help = new Help();
        try{
            help.display("Settings-Help");
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

        /*settingsStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });*/
    }

    public void setCurrentGame(Game currentGame){
        this.currentGame = currentGame;
    }

    public void setClientId(int clientId) {this.clientID = clientId;}

    @Override
    public void onGameLeaveResponse (GameLeaveResponse message,int clientId){
        Platform.runLater(()->{

        Lobby lobby = new Lobby();
        Stage lobbyStage = new Stage();

        lobbyStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });

        try{
            lobby.display(lobbyStage,clientId);
            closeStage();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        });
    }
}
