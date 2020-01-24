package de.upb.codingpirates.battleships.desktop.lobby;

import de.upb.codingpirates.battleships.client.ListenerHandler;
import de.upb.codingpirates.battleships.client.listener.LobbyResponseListener;
import de.upb.codingpirates.battleships.desktop.clienttype.ClientType;
import de.upb.codingpirates.battleships.desktop.serverlogin.ServerLogin;
import de.upb.codingpirates.battleships.client.ListenerHandler;
import de.upb.codingpirates.battleships.client.listener.LobbyResponseListener;
import de.upb.codingpirates.battleships.desktop.settings.Settings;
import de.upb.codingpirates.battleships.desktop.util.GameView;
import de.upb.codingpirates.battleships.desktop.util.GameView;
import de.upb.codingpirates.battleships.desktop.util.Help;
import de.upb.codingpirates.battleships.logic.Game;
import de.upb.codingpirates.battleships.logic.GameState;
import de.upb.codingpirates.battleships.network.message.response.LobbyResponse;
import de.upb.codingpirates.battleships.logic.Game;
import de.upb.codingpirates.battleships.network.message.response.LobbyResponse;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

/**
 * Controller Class for the Lobby Window.
 */
public class LobbyController implements Initializable , LobbyResponseListener {


    /**
     * Lobby class to start this View
     */
    public Lobby lobby;
    private LobbyModel model;

    //view
    /**
     * list with games, which are starting
     */
    @FXML
    private ListView<GameView> lstvwStart;
    /**
     * list with games, which are running
     */
    @FXML
    private ListView<GameView> lstvwRunning;
    /**
     * list with games, which are finished
     */
    @FXML
    private ListView<GameView> lstvwEnd;
    /**
     * button to load all games
     */
    @FXML
    private Button refreshButton;
    private ChangeListener <GameView> changeListener;

    private boolean listen;

    /**
     * Constructor of this class to register the Listener
     */
    public LobbyController() {
        ListenerHandler.registerListener(this);
        model = new LobbyModel();
        listen= true;
    }
    @Override
    public boolean invalidated() {
        return !listen;
    }
    /**
     * Initial Method.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }

    public void setChangeListener(){
        SelectionModel<GameView> smStart = lstvwStart.getSelectionModel();
        SelectionModel<GameView> smRunning = lstvwRunning.getSelectionModel();
        SelectionModel<GameView> smEnd = lstvwEnd.getSelectionModel();

        this.changeListener = (arg0, arg1, arg2) -> {
            if(arg2 !=null) {
                Stage window = new Stage();
                ClientType cType = new ClientType();

                window.setOnCloseRequest( t -> {
                    showgames();
                        }
                );

                try {
                    System.out.println("Hallo nochmal ctype Display von "+ arg2.getContent());
                    cType.display(window, arg2.getContent(), model.getClientID(),lobby.getStage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };


        smStart.selectedItemProperty().addListener(changeListener);
        smRunning.selectedItemProperty().addListener(changeListener);
        smEnd.selectedItemProperty().addListener(changeListener);

    }

    /**
     * Set Method for the related Lobby.
     *
     * @param lobby Related Lobby
     */
    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    /**
     * Requests the GameList from Server and Shows the Games in the Lobby Window.
     */
    public void showgames() {
        // Request GameList from Server
        try {
            model.sendRequest();
        } catch (Exception e) {
            System.out.println("Fehler: " + e);
        }

       /* this.startList.clear();
        this.runningList.clear();
        this.endList.clear();
        */

        //Show GameList
        lstvwStart.setItems(model.getStartList());
        lstvwRunning.setItems(model.getRunningList());
        lstvwEnd.setItems(model.getEndList());

    }

    public void setClientID(int clientID){
        model.setClientID(clientID);
    }


    @Override
    public void onLobbyResponse(LobbyResponse message, int clientId) {
        Platform.runLater(()-> {
            model.clearGameLists();
            model.parseToGameView(message.getGames());
        });
    }

    /**
     * closes this stage
     */
    public void closeStage() {
        lobby.close();
    }

    /**
     * starts the SettingsView in an extra window
     * @throws Exception
     */
    @FXML
    public void settings() throws Exception {

        Settings settings = new Settings();
        Stage settingsStage = new Stage();
        try {
            settings.display(settingsStage);
        } catch (IOException e) {
            e.printStackTrace();//TODO
        }
        /*settingsStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });*/
    }

    /**
     * nextButton - only to create the GUI
     * @throws IOException
     */


    /**
     * starts the HelpView with accessibility tools in an extra window
     * @throws IOException
     */
    @FXML
    public void help() throws IOException {
        Help help = new Help();
        try{
            help.display("Lobby-Help");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void btnLogoutClicked(){
        model.serverLogout();
        ServerLogin login = new ServerLogin();
        Stage loginStage = new Stage();

        loginStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });

        try {
            login.display(loginStage);
            closeStage();
        } catch (Exception e) {
            e.printStackTrace();//TODO
        }
    }


}