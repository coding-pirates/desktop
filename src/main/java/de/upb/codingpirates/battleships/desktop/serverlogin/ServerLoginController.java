package de.upb.codingpirates.battleships.desktop.serverlogin;

import de.upb.codingpirates.battleships.client.ListenerHandler;
import de.upb.codingpirates.battleships.client.listener.ServerJoinResponseListener;
import de.upb.codingpirates.battleships.desktop.lobby.Lobby;
import de.upb.codingpirates.battleships.desktop.settings.settings;
import de.upb.codingpirates.battleships.logic.ClientType;
import de.upb.codingpirates.battleships.network.message.response.ServerJoinResponse;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller Class for the ServerLogin Window.
 */
public class ServerLoginController implements ServerJoinResponseListener {

    @FXML
    private TextField ipField;
    @FXML
    private TextField portField;
    @FXML
    private TextField nameField;
    @FXML
    private Label lblStatus;

    @FXML
    private Button settings;

    public ServerLoginController() {
        ListenerHandler.registerListener(this);
    }

    /**
     * Creates a new TCPClient and ServerLoginModel with the Values of the TextFields.
     * Creates a Lobby if the Request was successful.
     */
    @FXML
    public void login() {
        String serverIP = ipField.getText();
        String port = portField.getText();

            try {
                ServerLogin
                    .getInstance()
                    .getTcpConnector()
                    .connect(serverIP, Integer.parseInt(port));

                //Send request to server
                ServerLoginModel slm = new ServerLoginModel(nameField.getText(), ClientType.SPECTATOR);
                slm.sendRequest(serverIP);
            } catch (Exception e) {
                lblStatus.setText("Anmeldung fehlgeschlagen: Server nicht erreichbar!");
            }
    }

    public void setLblStatus(String lblStatus) {
        this.lblStatus.setText(lblStatus);
    }



    @Override
    public void onServerJoinResponse(ServerJoinResponse response, int clientId){
        setLblStatus("");

        ServerLogin
            .getInstance()
            .getLoginStage()
            .close();

        Lobby lobby = new Lobby();
        Stage lobbyStage = new Stage();
        try {
            lobby.start(lobbyStage);
        } catch (IOException e) {
            e.printStackTrace();//TODO
        }
        lobbyStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public void settings() throws Exception {
        ServerLogin
                .getInstance()
                .getLoginStage()
                .close();

        de.upb.codingpirates.battleships.desktop.settings.settings settings = new settings();
        Stage settingsStage = new Stage();
        try {
            settings.start(settingsStage);
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
