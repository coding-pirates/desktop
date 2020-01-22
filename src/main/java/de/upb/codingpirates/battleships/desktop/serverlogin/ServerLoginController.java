package de.upb.codingpirates.battleships.desktop.serverlogin;

import de.upb.codingpirates.battleships.client.ListenerHandler;
import de.upb.codingpirates.battleships.client.listener.MessageHandlerListener;
import de.upb.codingpirates.battleships.client.listener.ServerJoinResponseListener;
import de.upb.codingpirates.battleships.desktop.BattleshipsDesktopClientApplication;
import de.upb.codingpirates.battleships.desktop.lobby.Lobby;
import de.upb.codingpirates.battleships.desktop.settings.Settings;
import de.upb.codingpirates.battleships.desktop.util.Help;
import de.upb.codingpirates.battleships.logic.ClientType;
import de.upb.codingpirates.battleships.network.message.response.ServerJoinResponse;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
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

    private BattleshipsDesktopClientApplication main;

    @FXML
    private TextField ipField;
    @FXML
    private TextField portField;
    @FXML
    private TextField nameField;
    @FXML
    private Label lblStatus;
    @FXML
    private Button btn_login;

    private StringPropertyBase text = new SimpleStringProperty();

    public ServerLoginController() {
        ListenerHandler.registerListener((MessageHandlerListener) this);
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

        // forces the portField to be numeric only
        portField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    portField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    /**
     * Creates a new TCPClient and ServerLoginModel with the Values of the TextFields.
     * Creates a Lobby if the Request was successful.
     *
     * @param event Button Pressed Event
     * @throws Exception when the server is not available this exception will be thrown
     */
    @FXML
    public void login(ActionEvent event) throws Exception {
        String serverIP = ipField.getText();
        String port = portField.getText();

                BattleshipsDesktopClientApplication
                    .getInstance()
                    .getTcpConnector()
                    .connect(serverIP, Integer.parseInt(port),() -> Platform.runLater(()->lblStatus.setText("Anmeldung fehlgeschlagen: Server nicht erreichbar!")),()->{
                        //Send request to server
                        ServerLoginModel slm = new ServerLoginModel(nameField.getText(), ClientType.PLAYER);
                        slm.sendRequest(serverIP);
                    });
    }

    private void setLblStatus(String lblStatus) {
        text.set(lblStatus);
    }

    public void closeStage(){
        Stage stage = (Stage) lblStatus.getScene().getWindow();
        stage.close();
    }

    @Override
    public void onServerJoinResponse(ServerJoinResponse response, int clientId) {
        Platform.runLater(() -> {
            setLblStatus("");



            Lobby lobby = new Lobby();
            Stage lobbyStage = new Stage();
            try {
                lobby.display(lobbyStage,response.getClientId());
                closeStage();
            } catch (IOException e) {
                e.printStackTrace();//TODO
            }
            lobbyStage.setOnCloseRequest(t -> {
                Platform.exit();
                System.exit(0);
            });
        });
    }
        @FXML
        public void settings() throws Exception {

            Settings settings = new Settings();
            Stage settingsStage = new Stage();
            try {
                settings.display(settingsStage);
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
    public void help() throws IOException {
        Help help = new Help();
        try{
            help.display("Server-Login-Help");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * changes the cursor for a button hover to Cursor.HAND
     * @param event event which calls the method
     */
    public void changeCursor(javafx.scene.input.MouseEvent event) {
        Button currentButton = (Button) event.getSource();
        currentButton.setCursor(Cursor.HAND);
    }
}
