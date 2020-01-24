package de.upb.codingpirates.battleships.desktop.clienttype;

import de.upb.codingpirates.battleships.client.ListenerHandler;
import de.upb.codingpirates.battleships.client.listener.GameJoinPlayerResponseListener;
import de.upb.codingpirates.battleships.client.listener.GameJoinSpectatorResponseListener;
import de.upb.codingpirates.battleships.client.listener.SpectatorGameStateResponseListener;
import de.upb.codingpirates.battleships.desktop.endgame.Endgame;
import de.upb.codingpirates.battleships.desktop.ingame.InGame;
import de.upb.codingpirates.battleships.desktop.lobby.Lobby;
import de.upb.codingpirates.battleships.desktop.placeship.Placeships;
import de.upb.codingpirates.battleships.desktop.waiting.Waiting;
import de.upb.codingpirates.battleships.logic.ClientType;
import de.upb.codingpirates.battleships.logic.Game;
import de.upb.codingpirates.battleships.logic.GameState;
import de.upb.codingpirates.battleships.logic.Spectator;
import de.upb.codingpirates.battleships.network.message.response.GameJoinPlayerResponse;
import de.upb.codingpirates.battleships.network.message.response.GameJoinSpectatorResponse;
import de.upb.codingpirates.battleships.network.message.response.SpectatorGameStateResponse;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientTypeController implements Initializable, GameJoinSpectatorResponseListener, GameJoinPlayerResponseListener, SpectatorGameStateResponseListener {

    @FXML
    private RadioButton rb_spectator;
    @FXML
    private RadioButton rb_player;
    @FXML
    private Label hinweis;
    @FXML
    private Button goButton;

    private ClientTypeModel clientTypeModel;
    private Stage LobbyStage;

    private boolean listen;

    public ClientTypeController() {
        ListenerHandler.registerListener(this);
        listen= true;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clientTypeModel = new ClientTypeModel();
    }
    @Override
    public void onGameJoinSpectatorResponse(GameJoinSpectatorResponse message, int messageId){
        clientTypeModel.sendSpectatorGameStateRequest();

    }

    /**
     * Sets the Player CheckBox visibility
     * @param visible
     */
    public void setPlayerVisibility(boolean visible){
        rb_player.setVisible(visible);
    }

    @Override
    public void onGameJoinPlayerResponse(GameJoinPlayerResponse message, int messageId) {
        Platform.runLater(()->{
            this.placeShips();
        });
    }

    /**
     * Sets the chosen client to player
     */
    @FXML
    public void player(){
        System.out.println("Player");
        clientTypeModel.setChosenClient("Player");
    }
    /**
     * Sets the chosen client to spectator
     */
    @FXML
    public void spectator(){
        System.out.println("Spectator");
        clientTypeModel.setChosenClient("Spectator");
    }

    /**
     * Sends GameJoin Request for the chosen Client
     */
    @FXML
    public void start(){
        if(clientTypeModel.getChosenClient()=="Player"){
            clientTypeModel.sendGameJoinPlayerRequest();
        }
        else if(clientTypeModel.getChosenClient()=="Spectator"){
           clientTypeModel.sendGameJoinSpectatorRequest();
        }
        else
        {
            hinweis.setText("Bitte ein ClientTyp auswählen!");
        }
    }
    /**
     * Closes the stage
     */
    public void closeStage(){
        Stage stage = (Stage) goButton.getScene().getWindow();
        stage.close();
    }
    /**
     * Goes back to Lobby View
     */
    public void back(){
        closeStage();
    }

    /**
     * opens the place ships view
     */
    public void placeShips(){
        Placeships placeships = new Placeships();
        Stage placeStage = new Stage();

        placeStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
        try {
            placeships.display(placeStage,clientTypeModel.getSelectedGame(), clientTypeModel.getClientID());
            this.closeStage();
            this.LobbyStage.close();
        } catch (IOException e) {
            e.printStackTrace();//TODO
        }
    }

    /**
     * opens the waiting view
     */
    public void waiting(){
        Waiting waitingView = new Waiting();
        Stage waitingStage = new Stage();

        waitingStage.setOnCloseRequest(t -> {
           System.exit(0);
           Platform.exit();
        });

        try {
            waitingView.display(waitingStage, clientTypeModel.getSelectedGame(),clientTypeModel.getClientID());
           // waitingView.setCurrentGame(clientTypeModel.getSelectedGame());
            this.closeStage();
            this.LobbyStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the selected game
     * @param selectedGame
     */
    public void setSelectedGame(Game selectedGame){
        clientTypeModel.setSelectedGame(selectedGame);
    }

    /**
     * sets the clientID
     * @param clientID
     */
    public void setClientID(int clientID){
        clientTypeModel.setClientID(clientID);
    }

    /**
     * Sets the LobbyStage
     * @param LobbyStage
     */
    public void setLobbyStage(Stage LobbyStage){this.LobbyStage=LobbyStage;}

    /**
     * Is used from ListenerHandler Class
     * @return
     */
    @Override
    public boolean invalidated() {
        return !listen;
    }

    @Override
    public void onSpectatorGameStateResponse(SpectatorGameStateResponse message, int clientId) {
        listen = false;
        Platform.runLater(() -> {
                if (message.getShips().isEmpty()) {
                    this.waiting();
                } else { //goTo GameView
                    InGame inGame = new InGame();
                    Stage inGameStage = new Stage();

                    inGameStage.setOnCloseRequest(t -> {
                        Platform.exit();
                        System.exit(0);
                    });

                    try {
                        inGame.start(inGameStage, clientTypeModel.getSelectedGame(), ClientType.SPECTATOR);
                        this.closeStage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

        });
    }
}
