package de.upb.codingpirates.battleships.desktop.ingame;

import de.upb.codingpirates.battleships.client.ListenerHandler;
import de.upb.codingpirates.battleships.desktop.BattleshipsDesktopClientApplication;
import de.upb.codingpirates.battleships.logic.*;
import de.upb.codingpirates.battleships.network.message.notification.*;
import de.upb.codingpirates.battleships.network.message.request.RequestBuilder;
import de.upb.codingpirates.battleships.network.message.response.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.*;

/**
 * Model class for the InGame Window.
 * Implements the Communication with the Server.
 */
public class InGameModel extends Application implements InGameModelMessageListener {

    public static InGameModel INSTANCE;

    private Stage inGameStage;
    private Game ausgewaehltesSpiel = null;
    private InGameController inGameController;

    private Collection<Client> player = new ArrayList<Client>();
    private Map<Integer, Map<Integer, PlacementInfo>> ships = null;
    private GameState gameState = null;
    private Collection<Shot> shots = new ArrayList<Shot>();
    private ClientType clientType;
    private Map<Integer, PlacementInfo> ownShipPlacements;
    private int clientID;
    private ArrayList<Shot> placedShots = new ArrayList<>();

    /**
     * Constructor. Sets a tcpConnector and the chosen Game.
     */
    public InGameModel() {
        ListenerHandler.registerListener(this);
    }

    public void setInGameController(InGameController controller){
        this.inGameController = controller;
    }

    public void setGame(Game game){
        this.ausgewaehltesSpiel = game;
    }

    /**
     * Get Method for Player.
     *
     * @return Collection of Players
     */
    public Collection<Client> getPlayer() {
        return player;
    }

    /**
     * Set Method for Player.
     *
     * @param player Collection of PLayers
     */
    public void setPlayer(Collection<Client> player) {
        this.player = player;
    }

    /**
     * Get Method for Ships.
     *
     * @return Map of PlayerId on a Map Placement Info
     */
    public Map<Integer, Map<Integer, PlacementInfo>> getShips() {
        return ships;
    }

    /**
     * Set Method for Ships.
     *
     * @param ships Map of PlayerId on a Map Placement Info
     */
    public void setShips(Map<Integer, Map<Integer, PlacementInfo>> ships) {
        this.ships = ships;
    }

    /**
     * Get Method for GameState
     *
     * @return GameState
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Set Method for GameState
     *
     * @param gameState Status of the Game.
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Get Method for Shots.
     *
     * @return Collection of Shots
     */
    public Collection<Shot> getShots() {
        return shots;
    }

    /**
     * Set Method for Shots.
     *
     * @param shots All placed Shots till now.
     */
    public void setShots(Collection<Shot> shots) {
        this.shots = shots;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }
    public ArrayList<Shot> getPlacedShots() {
        return placedShots;
    }

    public void setPlacedShots(ArrayList<Shot> placedShots) {
        this.placedShots = placedShots;
    }

    public void addShot(int playerId, Point2D shot){
        this.placedShots.add(new Shot(playerId,shot));
        if(this.placedShots.size() == ausgewaehltesSpiel.getConfig().getShips().size()){
            sendShotRequest();
        }
    }

    public void setClientType(ClientType clientType){
        this.clientType = clientType;
    }
    public void setOwnShipPlacements(Map<Integer, PlacementInfo> shipPlacements){
        this.ownShipPlacements = shipPlacements;
    }

    public Map<Integer, PlacementInfo> getOwnShipPlacements() {
        return ownShipPlacements;
    }

    /**
     * Sends a GameJoinRequest and a GameStateRequest.
     * Initializes an InGame Window and a related Controller.
     * Starts spectatorGameStateResponse().
     * Throws Exception if the Server don't Response.
     */
    public void start(Stage inGameStage) {
        // sendGameJoinSpectatorRequest
        sendGameJoinSpectatorRequest(); //TODO why?
    }

    public void start() {
     //   sendGameStateRequest();
    }

    public void start2() throws Exception {
        inGameController.setModel(this);
        inGameController.setGame(ausgewaehltesSpiel);
        inGameController.gameStateResponse(clientType, player, shots, ships, gameState);

        /*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/InGameView.fxml"));
        AnchorPane pane = loader.load();
        inGameController = loader.getController();
        inGameController.setModel(this);
        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        inGameStage.getIcons().add(icon);
        inGameStage.setTitle("InGame");
        inGameStage.setScene(new Scene(pane));
        inGameStage.setMaximized(true);
        inGameStage.setResizable(false);
        inGameStage.show();
         */
    }

    /**
     * First Request: Asks the Server to join a Game as Spectator.
     *
     * @return gameId: Id of the joined Game
     */
    public void sendGameJoinSpectatorRequest() {
            try {
                BattleshipsDesktopClientApplication
                        .getInstance()
                        .getTcpConnector()
                        .sendMessageToServer(RequestBuilder.gameJoinSpectatorRequest(ausgewaehltesSpiel.getId()));
            } catch (Exception e) {
                System.out.println("Konnte GameJoin Request nicht schicken: " + e);
            }

    }

    /**
     * Second Request: Ask the Server about the GameState.
     *
     * @return boolean
     */
    // Zweiter Request: SpectatorGameStateRequest
    public void sendGameStateRequest() {
        // send to server
        try {
            if(clientType == ClientType.SPECTATOR) {
                BattleshipsDesktopClientApplication
                        .getInstance()
                        .getTcpConnector()
                        .sendMessageToServer(RequestBuilder.spectatorGameStateRequest());
            }
            else{
                BattleshipsDesktopClientApplication
                        .getInstance()
                        .getTcpConnector()
                        .sendMessageToServer(RequestBuilder.playerGameStateRequest());
            }
        } catch (Exception e) {
            System.out.println("Konnte GameState Request nicht schicken: " + e);
        }
    }

    /**
     * Closes the InGame Window.
     */
    public void close() {
        inGameStage.close();
    }

    /**
     * Asks the Server about the remaining Time for the GameRound.
     * Starts the Controllers remainingTimeResponse().
     *
     * @return boolean
     */
    public void remainingTimeRequest() {
        try {
            BattleshipsDesktopClientApplication
                .getInstance()
                .getTcpConnector()
                .sendMessageToServer(RequestBuilder.remainingTimeRequest());
        } catch (Exception e) {
            System.out.println("Konnte Remaining Time Request nicht schicken: " + e);
        }
    }

    /**
     * Asks the Server about the Points of every Player.
     * Starts the Controllers setPoints().
     *
     * @return
     */
    public void pointsRequest() {
        try {
            BattleshipsDesktopClientApplication
                .getInstance()
                .getTcpConnector()
                .sendMessageToServer(RequestBuilder.pointsRequest());
        } catch (Exception e) {
            System.out.println("Konnte Points Request nicht schicken: " + e);
        }
    }

    public void sendShotRequest(){
        try {
            BattleshipsDesktopClientApplication
                    .getInstance()
                    .getTcpConnector()
                    .sendMessageToServer(RequestBuilder.shotsRequest(placedShots));
        } catch (Exception e) {
            System.out.println("Konnte Points Request nicht schicken: " + e);
        }
    }
    @Override
    public void onContinueNotification(ContinueNotification message, int clientId) {
        Platform.runLater(()-> {
            inGameController.continueNotification();
        });
    }

    @Override
    public void onFinishNotification(FinishNotification message, int clientId) {
        Platform.runLater(()-> {
            Map<Integer, Integer> points = message.getPoints();
            List<Integer> winner = new ArrayList(message.getWinner());
            inGameController.FinishNotification(points,winner.get(0) );
        });
    }

    /*@Override
    public void onGameInitNotification(GameInitNotification message, int clientId) {
        Collection<Client> clients = message.getClientList();
        Configuration config = message.getConfiguration();
        try {
            inGameController.gameInitNotification(config, clients);
            //secondGameStateRequest();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    } */

    /*@Override
    public void onGameJoinSpectatorResponse(GameJoinSpectatorResponse message, int clientId) {
        start();
    }*/

 /*   @Override
    public void onGameStartNotification(GameStartNotification message, int clientId) {
        inGameController.gameStartNotification();
    } */

    @Override
    public void onLeaveNotification(LeaveNotification message, int clientId) {
        Platform.runLater(()-> {
            int playerId = message.getPlayerId();
            inGameController.leaveNotification(playerId);
        });
    }

    @Override
    public void onPauseNotification(PauseNotification message, int clientId) {
        Platform.runLater(()-> {
            inGameController.pauseNotification();
        });
    }

    @Override
    public void onPointsResponse(PointsResponse message, int clientId) {
        Platform.runLater(()-> {
            Map<Integer, Integer> points = message.getPoints();
            inGameController.setPoints(points);
        });
    }

    @Override
    public void onRemainingTimeResponse(RemainingTimeResponse message, int clientId) {
        Platform.runLater(()-> {
            Long rtime = message.getTime();
            inGameController.remainingTimeResponse(rtime);
        });
    }

    @Override
    public void onRoundStartNotification(RoundStartNotification message, int clientId) {
        this.placedShots.clear();
        Platform.runLater(()-> {
            inGameController.roundStartNotification();
        });
    }

    @Override
    public void onSpectatorGameStateResponse(SpectatorGameStateResponse message, int clientId) {
        Platform.runLater(()-> {
            player = message.getPlayers();
            ships = message.getShips();
            gameState = message.getState();
            shots = message.getShots();
            try {
                start2();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onPlayerGameStateResponse(PlayerGameStateResponse message, int clientId) {
        Platform.runLater(()-> {
            player = message.getPlayer();
            gameState = message.getState();
            shots = message.getHits();
            try {
                start2();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    @Override
    public void onSpectatorUpdateNotification(SpectatorUpdateNotification message, int clientId) {
        Platform.runLater(()-> {
            Collection<Shot> hits = message.getHits();
            Map<Integer, Integer> points = message.getPoints();
            Collection<Shot> sunk = message.getSunk();
            Collection<Shot> missed = message.getMissed();
            inGameController.spectatorUpdateNotification(hits, points, sunk, missed);
        });
    }

    @Override
    public void onPlayerUpdateNotification(PlayerUpdateNotification message, int clientId) {
        Platform.runLater(()-> {
            Collection<Shot> hits = message.getHits();
            Map<Integer, Integer> points = message.getPoints();
            Collection<Shot> sunk = message.getSunk();
            inGameController.playerUpdateNotification(hits, points);
        });
    }
}
