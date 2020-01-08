package de.upb.codingpirates.battleships.desktop.ClientType;

import de.upb.codingpirates.battleships.client.ListenerHandler;
import de.upb.codingpirates.battleships.client.listener.GameJoinSpectatorResponseListener;
import de.upb.codingpirates.battleships.desktop.lobby.Lobby;
import de.upb.codingpirates.battleships.desktop.placeship.Placeships;
import de.upb.codingpirates.battleships.desktop.waiting.Waiting;
import de.upb.codingpirates.battleships.network.message.response.GameJoinSpectatorResponse;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientTypeController implements GameJoinSpectatorResponseListener {

    @FXML
    private RadioButton rb_spectator;
    @FXML
    private RadioButton rb_player;
    @FXML
    private Label lb_choice;
    @FXML
    private Button closeButton;

    private String chosenClient;


    public ClientTypeController() {
        ListenerHandler.registerListener(this);
    }
    @Override
    public void onGameJoinSpectatorResponse(GameJoinSpectatorResponse message, int messageId){

    }

    @FXML
    public void player(){
        System.out.println("Player");
        chosenClient="Player";
        //lb_choice.setText("Player is chosen");
    }

    @FXML
    public void spectator(){
        System.out.println("Spectator");
        chosenClient="Spectator";
        //lb_choice.setText("Spectator is chosen");
    }


    @FXML
    public void next() throws Exception {
        if(chosenClient=="Player"){
            placeShips();
        }
        if(chosenClient=="Spectator"){
            waiting();
        }
        else{
            back();
        }
        closeStage();
    }

    public void closeStage(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void back(){
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

    public void placeShips() throws Exception {
        Placeships placeships = new Placeships();
        Stage placeStage = new Stage();
        try {
            placeships.display(placeStage);
        } catch (IOException e) {
            e.printStackTrace();//TODO
            placeStage.setOnCloseRequest(t -> {
                Platform.exit();
                System.exit(0);
            });
        }
    }

    public void waiting() throws Exception {
        Waiting waitingView = new Waiting();
        Stage waitingStage = new Stage();
        try {
            waitingView.display(waitingStage);
            this.closeStage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
