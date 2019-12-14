package de.upb.codingpirates.battleships.desktop.serverlogin;

import de.upb.codingpirates.battleships.desktop.SpectatorApp;
import de.upb.codingpirates.battleships.desktop.lobby.Lobby;
import de.upb.codingpirates.battleships.logic.ClientType;
import de.upb.codingpirates.battleships.network.message.response.ServerJoinResponse;
import javafx.application.Platform;
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
 * Controller Class for the ServerLigon Window.
 */
public class ServerLoginController implements Initializable {

    public static ServerLoginController INSTANCE;

    public SpectatorApp main;

    @FXML
    private TextField ipField;
    @FXML
    private TextField portField;
    @FXML
    private TextField nameField;
    @FXML
    private Label lblStatus;

    public ServerLoginController() {
        INSTANCE = this;
    }

    /**
     * Set Method for Main.
     *
     * @param main Related SpectatorApp
     */
    public void setMain(SpectatorApp main) {
        this.main = main;
    }

    /**
     * Initial Method.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }

    /**
     * Creates a new TCPClient and ServerLoginModel with the Values of the TextFields.
     * Creates a Lobby if the Request was successful.
     *
     * @param event Button Pressed Event
     * @throws Exception
     */
    public void login(ActionEvent event) throws Exception {
        String serverIP = ipField.getText();
        String port = portField.getText();

            try {
                SpectatorApp.tcpConnector.connect(serverIP, Integer.parseInt(port));

                //Send request to server
                ServerLoginModel slm = new ServerLoginModel(nameField.getText(), ClientType.SPECTATOR);
                slm.sendRequest(serverIP);
            } catch (Exception e) {
                lblStatus.setText("Anmeldung fehlgeschlagen: Server nicht erreichbar!");
            }
    }

    public void setLblStatus(String lblStatus) {
        synchronized (lblStatus) {
            this.lblStatus.setText(lblStatus);
        }
    }

    public void closeMain(){
        main.close();
    }

    public void onServerJoinResponse(ServerJoinResponse response){
        ServerLoginModel.INSTANCE.setClientId(response.getClientId());
        setLblStatus("");
        closeMain();
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
}
