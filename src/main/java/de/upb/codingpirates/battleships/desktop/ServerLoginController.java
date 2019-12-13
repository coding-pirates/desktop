package de.upb.codingpirates.battleships.desktop;

import de.upb.codingpirates.battleships.desktop.network.TCPClient;
import de.upb.codingpirates.battleships.logic.ClientType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller Class for the ServerLigon Window.
 */
public class ServerLoginController implements Initializable {

    public SpectatorApp main;

    @FXML
    private TextField ipField;
    @FXML
    private TextField portField;
    @FXML
    private TextField nameField;
    @FXML
    private Label lblStatus;

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

        if (TCPClient.validIP(serverIP)) {

            try {
                SpectatorApp.tcpConnector = new TCPClient(serverIP, Integer.parseInt(port));

                //Send request to server
                ServerLoginModel slm = new ServerLoginModel(nameField.getText(), ClientType.SPECTATOR);
                int result = slm.sendRequest(serverIP);

                //Test of Request was successful.
                if (result < 0) {
                    lblStatus.setText("Die Anmeldung ist fehlgeschlagen!");
                } else {
                    lblStatus.setText("");
                    main.close();
                    Lobby lobby = new Lobby();
                    Stage lobbyStage = new Stage();
                    lobby.start(lobbyStage);
                    lobbyStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent t) {
                            Platform.exit();
                            System.exit(0);
                        }
                    });
                }
            } catch (Exception e) {
                lblStatus.setText("Anmeldung fehlgeschlagen: Server nicht erreichbar!");
            }
        } else {
            lblStatus.setText("Ihre IP ist nicht valide.");
        }
    }

}
