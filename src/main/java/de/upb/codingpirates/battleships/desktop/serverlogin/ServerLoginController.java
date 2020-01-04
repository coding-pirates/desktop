package de.upb.codingpirates.battleships.desktop.serverlogin;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import de.upb.codingpirates.battleships.client.ListenerHandler;
import de.upb.codingpirates.battleships.client.listener.ServerJoinResponseListener;
import de.upb.codingpirates.battleships.desktop.BattleshipsDesktopClientApplication;
import de.upb.codingpirates.battleships.desktop.lobby.Lobby;
import de.upb.codingpirates.battleships.logic.ClientType;
import de.upb.codingpirates.battleships.network.message.response.ServerJoinResponse;
import org.apache.logging.log4j.LogManager;

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
                BattleshipsDesktopClientApplication
                    .getInstance()
                    .getTcpConnector()
                    .connect(serverIP, Integer.parseInt(port));

                //Send request to server
                ServerLoginModel slm = new ServerLoginModel(nameField.getText(), ClientType.SPECTATOR);
                slm.sendRequest(serverIP);
            } catch (Exception e) {
                lblStatus.setText("Anmeldung fehlgeschlagen: Server nicht erreichbar!");
                LogManager.getLogger().error(e);
            }
    }

    public void setLblStatus(String lblStatus) {
        this.lblStatus.setText(lblStatus);
    }

    @Override
    public void onServerJoinResponse(ServerJoinResponse response, int clientId){
        Platform.runLater(()-> {
            setLblStatus("");

            BattleshipsDesktopClientApplication
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
        });
    }
}
