package de.upb.codingpirates.battleships.desktop.lobby;

import java.net.URL;
import java.util.*;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.stage.Stage;

import de.upb.codingpirates.battleships.client.ListenerHandler;
import de.upb.codingpirates.battleships.client.listener.LobbyResponseListener;
import de.upb.codingpirates.battleships.desktop.ingame.InGameModel;
import de.upb.codingpirates.battleships.desktop.util.GameView;
import de.upb.codingpirates.battleships.logic.Game;
import de.upb.codingpirates.battleships.network.message.response.LobbyResponse;

/**
 * Controller Class for the Lobby Window.
 */
public class LobbyController implements Initializable , LobbyResponseListener {


    public Lobby lobby;
    @FXML
    private ListView<GameView> lstvwStart;
    @FXML
    private ListView<GameView> lstvwRunning;
    @FXML
    private ListView<GameView> lstvwEnd;
    @FXML
    private Button refreshButton;
    private ObservableList<GameView> startList = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
    private ObservableList<GameView> runningList = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
    private ObservableList<GameView> endList = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());

    public LobbyController() {
        ListenerHandler.registerListener(this);
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
            LobbyModel lm = new LobbyModel();
            lm.sendRequest();
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

        ChangeListener<GameView> changeListener = (arg0, arg1, arg2) -> {
            InGameModel inGameModel = new InGameModel(arg2.getContent());
            Stage inGameStage = new Stage();
            try {
                inGameModel.start(inGameStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        SelectionModel<GameView> smStart = lstvwStart.getSelectionModel();
        smStart.selectedItemProperty().addListener(changeListener);

        SelectionModel<GameView> smRunning = lstvwRunning.getSelectionModel();
        smRunning.selectedItemProperty().addListener(changeListener);

        SelectionModel<GameView> smEnd = lstvwEnd.getSelectionModel();
        smEnd.selectedItemProperty().addListener(changeListener);
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
        parseToGameView(message.getGames());
    }
}
