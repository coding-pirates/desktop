package de.upb.codingpirates.battleships.desktop.util;

import de.upb.codingpirates.battleships.desktop.lobby.Lobby;
import de.upb.codingpirates.battleships.desktop.placeship.Placeships;
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

/**
 * class for the clientType in a game
 */
public class ClientType {

    //views
    /**
     * Button for choosing to be a spectator
     */
    @FXML
    private RadioButton rb_spectator;
    /**
     * Button for choosing to be a player
     */
    @FXML
    private RadioButton rb_player;
    /**
     * display the choice
     */
    @FXML
    private Label lb_choice;
    /**
     * Button to close this window
     */
    @FXML
    private Button closeButton;
    /**
     * string which shows the choice
     */
    private String chosenClient;

    /**
     * starts the view in a new window
     * @throws IOException
     */
    public void display() throws IOException {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("ClientType");

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/ClientTypeView.fxml"));
        AnchorPane pane = loader.load();

        Image icon = new Image(String.valueOf(ClientType.class.getResource("/images/app_icon.png")));
        window.getIcons().add(icon);

        Scene scene = new Scene(pane);
        window.setResizable(false);
        window.setScene(scene);
        window.showAndWait();
    }

    /**
     * displays the choice
     */
    @FXML
    public void player(){
        chosenClient="Player";
        //lb_choice.setText("Player is chosen");
        }

    /**
     * displays the choice
     */
    @FXML
    public void spectator(){
        chosenClient="Spectator";
        //lb_choice.setText("Spectator is chosen");
    }

    /**
     * starts the placehipsView if "player" is chosen, otherwise the WaitingView starts, closes this window
     * @throws Exception
     */
    @FXML
    public void next() throws Exception {
        if(chosenClient=="Player"){
            closeStage();
            placeShips();

        }
        else if(chosenClient=="Spectator"){
            closeStage();
            waiting();
        }
        else{
            back();
        }
    }

    /**
     * closes the ClienttypeView
     */
    public void closeStage(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    /**
     * loads the lobbyView new and closes the ClientTypeView
     */
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

    /**
     * starts the placeShipsView for a player
     * @throws Exception
     */
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

    /**
     * starts the WaitingView for a spectator
     * @throws Exception
     */
    public void waiting() throws Exception {
        Waiting waitingView = new Waiting();
        try {
            waitingView.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
