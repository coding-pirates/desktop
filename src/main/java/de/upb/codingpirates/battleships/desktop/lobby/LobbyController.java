package de.upb.codingpirates.battleships.desktop.lobby;

import de.upb.codingpirates.battleships.client.ListenerHandler;
import de.upb.codingpirates.battleships.client.listener.LobbyResponseListener;
import de.upb.codingpirates.battleships.desktop.clienttype.ClientType;
import de.upb.codingpirates.battleships.desktop.settings.Settings;
import de.upb.codingpirates.battleships.desktop.util.GameView;
import de.upb.codingpirates.battleships.desktop.util.Help;
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

/**
 * Controller Class for the Lobby Window.
 */
public class LobbyController implements Initializable , LobbyResponseListener {


    public Lobby lobby;
    private LobbyModel model;
    @FXML
    private ListView<GameView> lstvwStart;
    @FXML
    private ListView<GameView> lstvwRunning;
    @FXML
    private ListView<GameView> lstvwEnd;
    @FXML
    private Button refreshButton;
    private ChangeListener <GameView> changeListener;

    private ObservableList<GameView> startList = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
    private ObservableList<GameView> runningList = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
    private ObservableList<GameView> endList = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());

    private int clientID;




    public LobbyController() {
        ListenerHandler.registerListener(this);

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
                    cType.display(window, arg2.getContent(), clientID,lobby.getStage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            /*InGameModel inGameModel = new InGameModel(arg2.getContent());
            Stage inGameStage = new Stage();
            try {
                inGameModel.start(inGameStage);
            } catch (Exception e) {
                e.printStackTrace();
            }*/
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
            model = new LobbyModel();
            model.sendRequest();
        } catch (Exception e) {
            System.out.println("Fehler: " + e);
        }

        this.startList.clear();
        this.runningList.clear();
        this.endList.clear();

        //Show GameList
        lstvwStart.setItems(startList);
        lstvwRunning.setItems(runningList);
        lstvwEnd.setItems(endList);

    }

    public void setClientID(int clientID){
        this.clientID = clientID;
    }

    /**
     * Adds the GameView for every Game in the right Category.
     *
     * @param games Collection of Games
     */
    public void parseToGameView(Collection<Game> games) {
        for (Game game : games) {
            final GameView view = new GameView(game);

            switch (game.getState()) {
                case LOBBY:
                    startList.add(view);
                    break;
                case IN_PROGRESS:
                case PAUSED:
                    runningList.add(view);
                    break;
                case FINISHED:
                    endList.add(view);
            }
        }
    }

    /**
     * Initial Method.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }

    @Override
    public void onLobbyResponse(LobbyResponse message, int clientId) {
        Platform.runLater(()->
         parseToGameView(message.getGames())
        );
    }

    public void closeStage() {
        lobby.close();
    }

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
     * nextButton
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


}