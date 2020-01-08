package de.upb.codingpirates.battleships.desktop.ClientType;

import de.upb.codingpirates.battleships.desktop.lobby.Lobby;
import de.upb.codingpirates.battleships.desktop.placeship.Placeships;
import de.upb.codingpirates.battleships.desktop.waiting.Waiting;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientType {

    @FXML
    private RadioButton rb_spectator;
    @FXML
    private RadioButton rb_player;
    @FXML
    private Label lb_choice;
    @FXML
    private Button closeButton;

    private String chosenClient;

    public void display() throws IOException {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("ClientType");

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/ClientTypeView.fxml"));
        AnchorPane pane = loader.load();

        Image icon = new Image(String.valueOf(ClientType.class.getResource("/images/app_icon.png")));
        window.getIcons().add(icon);

        Scene scene = new Scene(pane);
        window.setScene(scene);
        window.showAndWait();
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

    public void placeShips() throws Exception {
        Placeships placeships = new Placeships();
        Stage placeStage = new Stage();
        try {
            placeships.start(placeStage);
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
        try {
            waitingView.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
