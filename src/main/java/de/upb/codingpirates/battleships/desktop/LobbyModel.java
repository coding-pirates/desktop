package de.upb.codingpirates.battleships.desktop;

import de.upb.codingpirates.battleships.logic.Game;
import de.upb.codingpirates.battleships.network.message.Parser;
import de.upb.codingpirates.battleships.network.message.request.LobbyRequest;
import de.upb.codingpirates.battleships.network.message.response.LobbyResponse;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Model Class for the Lobby Window.
 */
public class LobbyModel {

  private LobbyRequest lreq = null;
  private LobbyResponse lres = null;

  /**
   * Constructor. Creates a new LobbyRequest.
   */
  public LobbyModel() {
    lreq = new LobbyRequest();
  }

  /**
   * Sends a Request about the games on the Server to the Server.
   * @return ArrayList List of Games on the Server
   */
  public Collection<Game> sendRequest() {
    Parser mp = new Parser();

    try {
      String jsonResult = mp.serialize(lreq);

      String response = SpectatorApp.tcpConnector.sendeAnfrage(jsonResult);

      if (response != null) {
        lres = (LobbyResponse) mp.deserialize(response);
        return lres.getGames();
      } else {
        return new ArrayList<Game>();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return new ArrayList<>();
  }


}
