package de.upb.codingpirates.battleships.desktop.serverlogin;

import de.upb.codingpirates.battleships.client.ListenerHandler;
import de.upb.codingpirates.battleships.client.listener.ServerJoinResponseListener;
import de.upb.codingpirates.battleships.desktop.SpectatorApp;
import de.upb.codingpirates.battleships.logic.ClientType;
import de.upb.codingpirates.battleships.network.message.Parser;
import de.upb.codingpirates.battleships.network.message.request.ServerJoinRequest;
import de.upb.codingpirates.battleships.network.message.response.ServerJoinResponse;

import java.io.IOException;

/**
 * Model class for the ServerLogin Window.
 */
public class ServerLoginModel implements ServerJoinResponseListener {


    private String spielerName;
    private ClientType clientKind;
    private int clientId;

    /**
     * Constructor for ServerLoginModel
     *
     * @param spielerName Name of the Client
     * @param clientKind  Kind of the Client
     */
    public ServerLoginModel(String spielerName, ClientType clientKind) {
        ListenerHandler.registerListener(this);
        this.spielerName = spielerName;
        this.clientKind = clientKind;
    }

    /**
     * Get Method for the spielerName.
     *
     * @return spielerName
     */
    public String getSpielerName() {
        return spielerName;
    }

    /**
     * Set Method for spielerName.
     *
     * @param spielerName
     */
    public void setSpielerName(String spielerName) {
        this.spielerName = spielerName;
    }

    /**
     * Get Method for ClientKind.
     *
     * @return ClientKind
     */
    public ClientType getClientKind() {
        return clientKind;
    }

    /**
     * Set Method for ClientKind.
     *
     * @param clientKind
     */
    public void setClientKind(ClientType clientKind) {
        this.clientKind = clientKind;
    }

    /**
     * get Method for the ClientId.
     *
     * @return ClientId
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * Set method for the ClientId.
     *
     * @param clientId
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * Creates a new ServerJoinRequest, sends it to the Server.
     *
     * @param ip Ip of the Server
     * @return ClientId Id of the Client
     */
    public void sendRequest(String ip) {
        try {
            SpectatorApp.tcpConnector.sendMessageToServer(new ServerJoinRequest(spielerName, clientKind));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServerJoinResponse(ServerJoinResponse message, int clientId) {
        this.setClientId(message.getClientId());
    }
}
