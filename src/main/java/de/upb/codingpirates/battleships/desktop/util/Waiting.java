package de.upb.codingpirates.battleships.desktop.util;

import de.upb.codingpirates.battleships.client.listener.LobbyResponseListener;
import de.upb.codingpirates.battleships.desktop.ingame.InGameModel;
import de.upb.codingpirates.battleships.desktop.lobby.LobbyController;
import de.upb.codingpirates.battleships.desktop.settings.Settings;
import de.upb.codingpirates.battleships.logic.Game;
import de.upb.codingpirates.battleships.network.message.response.LobbyResponse;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Waiting implements LobbyResponseListener {
    @FXML
    private Button closeButton;
    Game game;

    public void start() throws IOException {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Waiting");

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/WaitingView.fxml"));
        AnchorPane pane = loader.load();

        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        window.getIcons().add(icon);
        Scene scene = new Scene(pane);
        window.setResizable(false);
        window.setScene(scene);
        window.showAndWait();
    }


    @FXML
    public void closeStage(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handlerButton(){
        InGameModel inGameModel = new InGameModel(game);
        Stage inGameStage = new Stage();
        try {
            inGameModel.start2(inGameStage);
            closeStage();
        } catch (Exception e) {
            e.printStackTrace();
            inGameStage.setOnCloseRequest((t -> {
                Platform.exit();
                System.exit(0);
            }));
        };
        //this.onLobbyResponse();

    }

    @FXML
    public void help() throws IOException {
        Help help = new Help();
        try{
            help.display("Waiting-Help");
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
        } catch (IOException e) {
            e.printStackTrace();//TODO
        }
        settingsStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
    }

    @Override
    public void onLobbyResponse(LobbyResponse message, int clientId) {
        LobbyController lobbyControl = new LobbyController();
        lobbyControl.parseToGameView(message.getGames());
    }
}
