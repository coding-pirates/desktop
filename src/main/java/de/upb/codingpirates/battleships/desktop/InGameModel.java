package de.upb.codingpirates.battleships.desktop;

import de.upb.codingpirates.battleships.logic.*;
import de.upb.codingpirates.battleships.network.message.Parser;
import de.upb.codingpirates.battleships.network.message.notification.*;
import de.upb.codingpirates.battleships.network.message.request.GameJoinSpectatorRequest;
import de.upb.codingpirates.battleships.network.message.request.PointsRequest;
import de.upb.codingpirates.battleships.network.message.request.RemainingTimeRequest;
import de.upb.codingpirates.battleships.network.message.request.SpectatorGameStateRequest;
import de.upb.codingpirates.battleships.network.message.response.GameJoinSpectatorResponse;
import de.upb.codingpirates.battleships.network.message.response.PointsResponse;
import de.upb.codingpirates.battleships.network.message.response.RemainingTimeResponse;
import de.upb.codingpirates.battleships.network.message.response.SpectatorGameStateResponse;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Model class for the InGame Window. 
 * Implements the Communication with the Server.
 */
public class InGameModel extends Application implements INotificationCallback {

  private Stage inGameStage;
  private Game ausgewaehltesSpiel = null;
  private InGameController inGameController;

  // Gamedata
  private Collection<Client> player = new ArrayList<Client>();
  private java.util.Map<Integer, java.util.Map<Integer, PlacementInfo>> ships = null;
  private GameState gameState = null;
  private Collection<Shot> shots = new ArrayList<Shot>();
  private Map<Integer, Integer> points = null;
  private Long rtime = null;

  /**
   * Constructor. Sets a tcpConnector and the chosen Game.
   * @param g	Chosen Game.
   */
  public InGameModel(Game g) {
    ausgewaehltesSpiel = g;
    SpectatorApp.tcpConnector.setNotifier(this);
  }

  /**
   * Get Method for Player.
   * @return Collection of Players
   */
  public Collection<Client> getPlayer() {
    return player;
  }

  /**
   * Set Method for Player.
   * @param player	Collection of PLayers
   */
  public void setPlayer(Collection<Client> player) {
    this.player = player;
  }

  /**
   * Get Method for Ships.
   * @return Map of PlayerId on a Map Placement Info
   */
  public java.util.Map<Integer, java.util.Map<Integer, PlacementInfo>> getShips() {
    return ships;
  }

  /**
   * Set Method for Ships.
   * @param ships Map of PlayerId on a Map Placement Info
   */
  public void setShips(java.util.Map<Integer, java.util.Map<Integer, PlacementInfo>> ships) {
    this.ships = ships;
  }

  /**
   * Get Method for GameState
   * @return GameState
   */
  public GameState getGameState() {
    return gameState;
  }

  /**
   * Set Method for GameState
   * @param gameState Status of the Game.
   */
  public void setGameState(GameState gameState) {
    this.gameState = gameState;
  }

  /**
   * Get Method for Shots.
   * @return Collection of Shots
   */
  public Collection<Shot> getShots() {
    return shots;
  }

  /**
   * Set Method for Shots.
   * @param shots All placed Shots till now.
   */
  public void setShots(Collection<Shot> shots) {
    this.shots = shots;
  }

  /**
   * Sends a GameJoinRequest and a GameStateRequest.
   * Initializes an InGame Window and a related Controller.
   * Starts spectatorGameStateResponse().
   * Throws Exception if the Server don't Response.
   */
  public void start(Stage inGameStage) throws Exception {
    // sendGameJoinSpectatorRequest
    int gameID = sendGameJoinSpectatorRequest();

    if (gameID >= 0) {

      boolean result = sendGameStateRequest();
      if (result) {
        this.inGameStage = inGameStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InGameView.fxml"));
        AnchorPane pane = loader.load();
        inGameController = loader.getController();
        inGameController.setModel(this);
        inGameStage.setTitle("");
        inGameStage.setScene(new Scene(pane));
        inGameStage.setMaximized(true);
        inGameStage.show();
        inGameController.spectatorGameStateResponse(player, shots, ships, gameState);
        inGameController.setGame(ausgewaehltesSpiel);
      } else {
        throw new Exception("Keine Antwort von Server für SpectatorGameStateRequest erhalten");
      }

    } else {
      throw new Exception("Keine Antwort von Server SpectatorGameJoinRequest erhalten");
    }

  }

  /**
   * First Request: Asks the Server to join a Game as Spectator. 
   * @return gameId: Id of the joined Game
   */
  public int sendGameJoinSpectatorRequest() {
    GameJoinSpectatorRequest gsjr = new GameJoinSpectatorRequest(ausgewaehltesSpiel.getId());
    Parser mp = new Parser();
    int gameID = -1;
    // send to server
    try {
      String jsonResult = mp.serialize(gsjr);

      String response = SpectatorApp.tcpConnector.sendeAnfrage(jsonResult);

      if (response != null) {
        GameJoinSpectatorResponse gjprlres = (GameJoinSpectatorResponse) mp.deserialize(response);
        gameID = gjprlres.getGameId();
      }
    } catch (Exception e) {
      System.out.println("Konnte GameJoin Request nicht schicken: " + e);
    }

    return gameID;
  }

  /**
   * Second Request: Ask the Server about the GameState.
   * @return boolean
   */
  // Zweiter Request: SpectatorGameStateRequest
  public boolean sendGameStateRequest() {
    SpectatorGameStateRequest sgsr = new SpectatorGameStateRequest();
    Parser mp = new Parser();

    // send to server
    try {
      String jsonResult = mp.serialize(sgsr);

      String response = SpectatorApp.tcpConnector.sendeAnfrage(jsonResult);

      if (response != null) {
        SpectatorGameStateResponse sgsresp = (SpectatorGameStateResponse) mp
            .deserialize(response);

        // Mapping of Gamedata
        player = sgsresp.getPlayers();
        ships = sgsresp.getShips();
        gameState = sgsresp.getState();
        shots = sgsresp.getShots();

        return true;
      }
    } catch (Exception e) {
      System.out.println("Konnte GameState Request nicht schicken: " + e);
    }
    return false;
  }

  /**
   * Closes the InGame Window.
   */
  public void close() {
    inGameStage.close();
  }

  /**
   * Reacts on GameInitNotification.
   * Starts Controllers gameInitNotification().
   */
  @Override
  public void onGameInitNotification(GameInitNotification initNotification) {
    Collection<Client> clients = initNotification.getClientList();
    Configuration config = initNotification.getConfiguration();
    try {
      inGameController.gameInitNotification(config, clients);      
      //secondGameStateRequest();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Reacts on GameStartNotification.
   * Starts Controllers roundStartNotification().
   */
  @Override
  public void onGameStartNotification(GameStartNotification initNotification){
    inGameController.gameStartNotification();
  }

  /**
   * Reacts on SpectatorUpdateNotification.
   * Starts Controllers spectatorUpdateNotification().
   */
  @Override
  public void onSpectatorUpdateNotification(SpectatorUpdateNotification updateNotification) {
    Collection<Shot> hits = updateNotification.getHits();
    Map<Integer, Integer> points = updateNotification.getPoints();
    Collection<Shot> sunk = updateNotification.getSunk();
    Collection<Shot> missed = updateNotification.getMissed();
    inGameController.spectatorUpdateNotification(hits, points, sunk, missed);
  }

  /**
   * Reacts on FinishNotification.
   * Starts Controllers FinishNotification().
   */
  @Override
  public void onFinishNotification(FinishNotification finishNotification) {
    Map<Integer, Integer> points = finishNotification.getPoints();
    int winner = finishNotification.getWinner();
    inGameController.FinishNotification(points, winner);
  }

  /**
   * Reacts on PauseNotification.
   * Starts Controllers pauseNotification().
   */
  @Override
  public void onPauseNotification(PauseNotification pauseNotification) {
    inGameController.pauseNotification();
  }

  /**
   * Reacts on ContinueNotification.
   * Starts Controllers continueNotification().
   */
  @Override
  public void onContinueNotification(ContinueNotification continueNotification) {
    inGameController.continueNotification();
  }

  /**
   * Reacts on LeaveNotification.
   * Starts Controllers leaveNotification().
   */
  @Override
  public void onLeaveNotification(LeaveNotification leaveNotification) {
    int playerId = leaveNotification.getPlayerId();
    inGameController.leaveNotification(playerId);
  }

  /**
   * Reacts on RoundStartNotification.
   * Starts Controllers roundStartNotification().
   */
  @Override
  public void onRoundStartNotification(RoundStartNotification roundstartNotification) {
    inGameController.roundStartNotification();
  }

  /**
   * Asks the Server about the remaining Time for the GameRound.
   * Starts the Controllers remainingTimeResponse().
   * @return boolean
   */
  public boolean remainingTimeRequest() {
    RemainingTimeRequest rtr = new RemainingTimeRequest();
    Parser mpr = new Parser();

    // send to server
    try {
      String jsonremainingtime = mpr.serialize(rtr);
      String remainingtimeresult = SpectatorApp.tcpConnector.sendeAnfrage(jsonremainingtime);

      if (remainingtimeresult != null) {
        RemainingTimeResponse rtresp = (RemainingTimeResponse) mpr
            .deserialize(remainingtimeresult);

        // Mapping of Gamedata
        rtime = rtresp.getTime();
        inGameController.remainingTimeResponse(rtime);
        return true;
      }
    } catch (Exception e) {
      System.out.println("Konnte Remaining Time Request nicht schicken: " + e);
    }
    System.out.println("Fehler");
    return false;
  }

  /**
   * Asks the Server about the Points of every Player.
   * Starts the Controllers setPoints().
   * @return
   */
  public boolean pointsRequest() {
    PointsRequest pr = new PointsRequest();
    Parser mprs = new Parser();

    // send to server
    try {
      String jsonpointsresult = mprs.serialize(pr);
      String pointsresult = SpectatorApp.tcpConnector.sendeAnfrage(jsonpointsresult);

      if (pointsresult != null) {
        PointsResponse presp = (PointsResponse) mprs.deserialize(pointsresult);

        // Mapping of Gamedata
        points = presp.getPoints();
        inGameController.setPoints(points);
        return true;
      }
    } catch (Exception e) {
      System.out.println("Konnte Points Request nicht schicken: " + e);
    }
    return false;
  }
}
