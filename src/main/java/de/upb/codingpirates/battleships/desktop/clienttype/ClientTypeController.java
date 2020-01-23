package de.upb.codingpirates.battleships.desktop.clienttype;

import de.upb.codingpirates.battleships.client.ListenerHandler;
import de.upb.codingpirates.battleships.client.listener.GameJoinPlayerResponseListener;
import de.upb.codingpirates.battleships.client.listener.GameJoinSpectatorResponseListener;
import de.upb.codingpirates.battleships.desktop.placeship.Placeships;
import de.upb.codingpirates.battleships.desktop.waiting.Waiting;
import de.upb.codingpirates.battleships.logic.Game;
import de.upb.codingpirates.battleships.network.message.response.GameJoinPlayerResponse;
import de.upb.codingpirates.battleships.network.message.response.GameJoinSpectatorResponse;
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

public class ClientTypeController implements Initializable, GameJoinSpectatorResponseListener, GameJoinPlayerResponseListener {

    @FXML
    private RadioButton rb_spectator;
    @FXML
    private RadioButton rb_player;
    @FXML
    private Label hinweis;
    @FXML
    private Button goButton;

    private String chosenClient;
    private ClientTypeModel clientTypeModel;
    private Stage LobbyStage;

    public ClientTypeController() {
        ListenerHandler.registerListener(this);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clientTypeModel = new ClientTypeModel();
    }
    @Override
    public void onGameJoinSpectatorResponse(GameJoinSpectatorResponse message, int messageId){
        Platform.runLater(()->{
        this.waiting();
        });
    }

    @Override
    public void onGameJoinPlayerResponse(GameJoinPlayerResponse message, int messageId) {
        Platform.runLater(()->{
            this.placeShips();
        });
    }

    @FXML
    public void player(){
        System.out.println("Player");
        chosenClient="Player";
    }

    @FXML
    public void spectator(){
        System.out.println("Spectator");
        chosenClient="Spectator";
    }


    @FXML
    public void start(){
        if(chosenClient=="Player"){
            closeStage();
            clientTypeModel.sendGameJoinPlayerRequest();
        }
        if(chosenClient=="Spectator"){
           clientTypeModel.sendGameJoinSpectatorRequest();
        }
        else
        {
            hinweis.setText("Bitte ein ClientTyp auswählen!");
        }
    }

    public void closeStage(){
        Stage stage = (Stage) goButton.getScene().getWindow();
        stage.close();
    }

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

    public void waiting(){
        Waiting waitingView = new Waiting();
        Stage waitingStage = new Stage();

        waitingStage.setOnCloseRequest(t -> {
            LobbyStage.show();
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

    public void setSelectedGame(Game selectedGame){
        clientTypeModel.setSelectedGame(selectedGame);
    }

    public void setClientID(int clientID){
        clientTypeModel.setClientID(clientID);
    }

    public void setLobbyStage(Stage LobbyStage){this.LobbyStage=LobbyStage;}


}
