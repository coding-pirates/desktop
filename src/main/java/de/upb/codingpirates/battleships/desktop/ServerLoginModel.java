package de.upb.codingpirates.battleships.desktop;

import de.upb.codingpirates.battleships.logic.ClientType;
import de.upb.codingpirates.battleships.network.message.Parser;
import de.upb.codingpirates.battleships.network.message.request.ServerJoinRequest;
import de.upb.codingpirates.battleships.network.message.response.ServerJoinResponse;

/**
 * Model class for the ServerLogin Window.
 * 
 */
public class ServerLoginModel {

  public static final Parser MESSAGE_PARSER = new Parser();
  private String spielerName;
  private ClientType clientKind;
  private int clientId;
  /**
   * Constructor for ServerLoginModel
   * @param spielerName Name of the Client
   * @param clientKind	Kind of the Client
   */
  public ServerLoginModel(String spielerName, ClientType clientKind) {
    this.spielerName = spielerName;
    this.clientKind = clientKind;
  }

  /**
   * Get Method for the spielerName.
   * @return spielerName
   */
  public String getSpielerName() {
    return spielerName;
  }

  /**
   * Set Method for spielerName.
   * @param spielerName
   */
  public void setSpielerName(String spielerName) {
    this.spielerName = spielerName;
  }
  
  /**
   * Get Method for ClientKind.
   * @return	ClientKind
   */
  public ClientType getClientKind() {
    return clientKind;
  }

  /**
   * Set Method for ClientKind.
   * @param clientKind
   */
  public void setClientKind(ClientType clientKind) {
    this.clientKind = clientKind;
  }

  /**
   * get Method for the ClientId.
   * @return ClientId
   */
  public int getClientId() {
    return clientId;
  }

  /**
   * Set method for the ClientId.
   * @param clientId
   */
  public void setClientId(int clientId) {
    this.clientId = clientId;
  }

  /**
   * Creates a new ServerJoinRequest, sends it to the Server.
   * @param ip Ip of the Server
   * @return ClientId Id of the Client
   */
  public int sendRequest(String ip) {

    try {
      // ServerJoinRequest zu JSON parsen
      ServerJoinRequest request = new ServerJoinRequest(spielerName, clientKind);
      String jsonRequest = MESSAGE_PARSER.serialize(request);

      // An Server senden
      String result = SpectatorApp.tcpConnector.sendeAnfrage(jsonRequest);

      // Json wieder zu ServerJoinResponse parsen
      if (result != null) {
        ServerJoinResponse response = (ServerJoinResponse) MESSAGE_PARSER
            .deserialize(result);
        clientId = response.getClientId();
      }
    } catch (Exception e) {
      System.out.println("ERROR: parsing failed:" + e);
    }

    return clientId;
  }

}
