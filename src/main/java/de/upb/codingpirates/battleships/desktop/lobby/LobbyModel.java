package de.upb.codingpirates.battleships.desktop.lobby;

import de.upb.codingpirates.battleships.desktop.BattleshipsDesktopClientApplication;
import de.upb.codingpirates.battleships.network.message.request.RequestBuilder;

/**
 * Model Class for the Lobby Window.
 */
public class LobbyModel {

    public static LobbyModel INSTANCE;

    /**
     * Constructor. Creates a new LobbyRequest.
     */
    public LobbyModel() {
        INSTANCE = this;
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


}
