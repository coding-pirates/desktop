package de.upb.codingpirates.battleships.desktop.lobby;

import de.upb.codingpirates.battleships.desktop.BattleshipsDesktopClientApplication;
import de.upb.codingpirates.battleships.desktop.util.GameView;
import de.upb.codingpirates.battleships.logic.Game;
import de.upb.codingpirates.battleships.network.message.request.RequestBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collection;

/**
 * Model Class for the Lobby Window.
 */
public class LobbyModel {

    private ObservableList<GameView> startList = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
    private ObservableList<GameView> runningList = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
    private ObservableList<GameView> endList = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());

    private int clientID;

    public static LobbyModel INSTANCE;

    /**
     * Constructor. Creates a new LobbyRequest.
     */
    public LobbyModel() {
        INSTANCE = this;
    }


    public ObservableList<GameView> getStartList() {
        return startList;
    }

    public void setStartList(ObservableList<GameView> startList) {
        this.startList = startList;
    }

    public ObservableList<GameView> getRunningList() {
        return runningList;
    }

    public void setRunningList(ObservableList<GameView> runningList) {
        this.runningList = runningList;
    }

    public ObservableList<GameView> getEndList() {
        return endList;
    }

    public void setEndList(ObservableList<GameView> endList) {
        this.endList = endList;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public void addTostartList(){

    }

    public void clearGameLists(){
        this.startList.clear();
        this.runningList.clear();
        this.endList.clear();
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
     * Sends a Request about the games on the Server to the Server.
     *
     * @return ArrayList List of Games on the Server
     */
    public void sendRequest() {
        try {
            BattleshipsDesktopClientApplication
                .getInstance()
                .getTcpConnector()
                .sendMessageToServer(RequestBuilder.lobbyRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void serverLogout(){
        try {
            BattleshipsDesktopClientApplication
                    .getInstance()
                    .getTcpConnector().disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
