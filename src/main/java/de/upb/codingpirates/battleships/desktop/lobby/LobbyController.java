package de.upb.codingpirates.battleships.desktop.lobby;

import de.upb.codingpirates.battleships.client.ListenerHandler;
import de.upb.codingpirates.battleships.client.listener.LobbyResponseListener;
import de.upb.codingpirates.battleships.desktop.ingame.InGameModel;
import de.upb.codingpirates.battleships.desktop.util.GameView;
import de.upb.codingpirates.battleships.logic.Game;
import de.upb.codingpirates.battleships.logic.GameState;
import de.upb.codingpirates.battleships.network.message.response.LobbyResponse;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

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
    private ArrayList<GameView> startList = null;
    private ArrayList<GameView> runningList = null;
    private ArrayList<GameView> endList = null;

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

        this.startList = new ArrayList<GameView>();
        this.runningList = new ArrayList<GameView>();
        this.endList = new ArrayList<GameView>();

        //Show GameList

        ObservableList<GameView> startGames = FXCollections.observableArrayList(startList);
        lstvwStart.setItems(startGames);

        ObservableList<GameView> runningGames = FXCollections.observableArrayList(runningList);
        lstvwRunning.setItems(runningGames);

        ObservableList<GameView> endGames = FXCollections.observableArrayList(endList);
        lstvwEnd.setItems(endGames);

        ChangeListener<GameView> changeListener = new ChangeListener<GameView>() {

            @Override
            public void changed(ObservableValue<? extends GameView> arg0, GameView arg1, GameView arg2) {

                InGameModel inGameModel = new InGameModel(arg2.getContent());
                Stage inGameStage = new Stage();
                try {
                    inGameModel.start(inGameStage);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

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
        for (Game g : games) {
            if (g.getState().equals(GameState.LOBBY)) {
                startList.add(new GameView(g));
            }
            if (g.getState().equals(GameState.IN_PROGRESS)) {
                runningList.add(new GameView(g));
            }
            if (g.getState().equals(GameState.PAUSED)) {
                runningList.add(new GameView(g));
            }
            if (g.getState().equals(GameState.FINISHED)) {
                endList.add(new GameView(g));
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
