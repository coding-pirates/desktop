package de.upb.codingpirates.battleships.desktop.serverlogin;

import de.upb.codingpirates.battleships.client.ListenerHandler;
import de.upb.codingpirates.battleships.client.listener.MessageHandlerListener;
import de.upb.codingpirates.battleships.client.listener.ServerJoinResponseListener;
import de.upb.codingpirates.battleships.desktop.BattleshipsDesktopClientApplication;
import de.upb.codingpirates.battleships.desktop.lobby.Lobby;
import de.upb.codingpirates.battleships.desktop.settings.Settings;
import de.upb.codingpirates.battleships.desktop.util.Help;
import de.upb.codingpirates.battleships.logic.ClientType;
import de.upb.codingpirates.battleships.network.message.response.LobbyResponse;
import de.upb.codingpirates.battleships.network.message.response.ServerJoinResponse;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller Class for the ServerLogin Window.
 */
public class ServerLoginController implements Initializable, ServerJoinResponseListener {

    public BattleshipsDesktopClientApplication main;

    @FXML
    private TextField ipField;
    @FXML
    private TextField portField;
    @FXML
    private TextField nameField;
    @FXML
    private Label lblStatus;

    private StringPropertyBase text = new SimpleStringProperty();

    public ServerLoginController() {
        //ListenerHandler.registerListener((MessageHandlerListener) this);
    }

    /**
     * Set Method for Main.
     *
     * @param main Related SpectatorApp
     */
    public void setMain(BattleshipsDesktopClientApplication main) {
        this.main = main;
    }

    /**
     * Initial Method.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        text.addListener(listener ->lblStatus.setText(text.get()));
    }

    /**
     * Creates a new TCPClient and ServerLoginModel with the Values of the TextFields.
     * Creates a Lobby if the Request was successful.
     *
     * @param event Button Pressed Event
     * @throws Exception
     */
    @FXML
    public void login(ActionEvent event) throws Exception {
        String serverIP = ipField.getText();
        String port = portField.getText();

        try {
            BattleshipsDesktopClientApplication.tcpConnector.connect(serverIP, Integer.parseInt(port));

            //Send request to server
            ServerLoginModel slm = new ServerLoginModel(nameField.getText(), ClientType.SPECTATOR);
            slm.sendRequest(serverIP);
        } catch (Exception e) {
            lblStatus.setText("Anmeldung fehlgeschlagen: Server nicht erreichbar!");
        }
    }

    public void setLblStatus(String lblStatus) {
        text.set(lblStatus);
    }

    public void closeStage(){
        Stage stage = (Stage) lblStatus.getScene().getWindow();
        stage.close();
    }

    @Override
    public void onServerJoinResponse(ServerJoinResponse response, int clientId){
        setLblStatus("");
        //closeMain();
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

    @FXML
    public void handlerButton(){
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

    @FXML
    public void help() throws IOException {
        Help help = new Help();
        try{
            help.display("Server-Login-Help");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


   /* @Override
    public void onLobbyResponse(LobbyResponse message, int clientId) {

    }*/
}
