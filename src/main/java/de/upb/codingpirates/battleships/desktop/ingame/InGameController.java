package de.upb.codingpirates.battleships.desktop.ingame;

import de.upb.codingpirates.battleships.desktop.gamefield.GameFieldController;
import de.upb.codingpirates.battleships.desktop.ranking.Ranking;
import de.upb.codingpirates.battleships.desktop.util.Help;
import de.upb.codingpirates.battleships.logic.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Controller Class for the Window that shows the Game Configuration and GameFields
 * of every Player involved in the Game.
 */
public class InGameController implements Initializable {

    private InGameModel model;
    private Ranking ranking;
    private Long millis;
    private int height;
    private int width;
    private HashMap<Integer, GameFieldController> controllerMap = new HashMap<Integer, GameFieldController>();
    private HashMap<Integer, Node> fieldMap = new HashMap<Integer, Node>();
    private Node inGame;
    private Configuration config;
    private Timeline time = new Timeline();
    private Collection<Client> players = null;
    private boolean enlarged = false;
    private Map<Integer, Integer> points = null;
    private Game game;

    @FXML
    private GridPane grid;
    @FXML
    private Label maxPlayerCount;
    @FXML
    private Label shotCount;
    @FXML
    private Label hitPoints;
    @FXML
    private Label sunkPoints;
    @FXML
    private Label roundTime;
    @FXML
    private Label status;
    @FXML
    private FlowPane spielfelder;
    @FXML
    private SplitPane splitPane;
    @FXML
    private Label restTime;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        this.model = new InGameModel();
    }

    /**
     * Safes the connected Model Class.
     *
     * @param model the connected Model Class
     */
    public void setModel(InGameModel model) {
        this.model = model;
    }

    /**
     * Safes the selected Game Class.
     *
     * @param game
     */
    public void setGame(Game game) {
        this.game = game;
        model.setGame(game);
    }

    /**
     * Shows the klicked GameField of one Player on a bigger Screen in detail.
     */
    public void enlargeBoard(GameFieldController controller) {
        if (enlarged) {
            spielfelder.getChildren().add(this.inGame);
        }
        grid.getChildren().clear();
        grid.add(controller.getInGame(), 0, 0);
        splitPane.setDividerPositions(0.27);
        this.inGame = controller.getInGame();
        enlarged = true;
    }

    /**
     * Initializes a Timer for the remaining Round Time
     */
    public void startTimer() {

        time.setCycleCount(Timeline.INDEFINITE);
        if (time != null) {
            time.stop();
        }
        KeyFrame frame = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                millis--;
                restTime.setText(millis.toString());
                if (millis <= 0) {
                    time.stop();
                }

            }

        });
        time.getKeyFrames().add(frame);
        time.playFromStart();
    }


    /**
     * Closes the InGame Window.
     */
    public void closeModus() {
        model.close();
    }

    /**
     * Initializes a Ranking with the Rank, Name and Points of every Player.
     */
    public void showRanking() {
        this.ranking = new Ranking();
        Stage rankingStage = new Stage();
        try {
            ranking.display(rankingStage);
            if (points != null) {
                ranking.sortPoints(points);
                ranking.setPlayer(players);
            }
        } catch (Exception e) {
            System.out.println("Fehler: " + e);
        }
    }

    /**
     * Closes the detailed view of one GameField.
     */
    public void backToView() {
        spielfelder.getChildren().add(inGame);
        splitPane.setDividerPositions(1.0);
        enlarged = false;
    }

    /**
     * Takes Configuration of the running or ended Game and displays the Information in the TextFields.
     */
    public void setConfig() {
        Platform.runLater(() -> {
            maxPlayerCount.setText(Integer.toString(game.getConfig().getMaxPlayerCount()));
            shotCount.setText(Integer.toString(game.getConfig().getShotCount()));
            hitPoints.setText(Integer.toString(game.getConfig().getHitPoints()));
            sunkPoints.setText(Integer.toString(game.getConfig().getSunkPoints()));
            roundTime.setText(Long.toString(game.getConfig().getRoundTime()));
            controllerMap.forEach((client, controller) -> {
                controller.buildBoard(game.getConfig().getHeight(), game.getConfig().getWidth());
            });

        });

    }

    /**
     * Takes Configuration of the Game and displays the Information in the TextFields.
     *
     * @param tempConfig Configuration of the Game.
     * @param clientList List of all Clients taking part in the Game.
     */
    public void gameInit(Configuration tempConfig, Collection<Client> clientList) {
        config = tempConfig;
        height = config.getHeight();
        width = config.getWidth();
        millis = config.getRoundTime();

        Platform.runLater(() -> {
            maxPlayerCount.setText(Integer.toString(config.getMaxPlayerCount()));
            shotCount.setText(Integer.toString(config.getShotCount()));
            hitPoints.setText(Integer.toString(config.getHitPoints()));
            sunkPoints.setText(Integer.toString(config.getSunkPoints()));
            roundTime.setText(Long.toString(config.getRoundTime()));
            controllerMap.forEach((client, controller) -> {
                controller.buildBoard(height, width);
            });

        });
        try {
            model.sendGameStateRequest();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Takes GameStateResponse and decides by the GameState if the Timer needs to be manipulated and the
     * Points of Players need to be refreshed. Duplicated players dont get a second GameField.
     * Duplicated Shots and Ships are computed again.
     *
     * @param players Collection of all PLayers in the Game
     * @param shots   Collection of all still placed Shots
     * @param ships   Map of all Ships placed
     * @param state   GameState
     * @throws Exception
     */
    public void spectatorGameStateResponse(Collection<Client> players, Collection<Shot> shots, Map<Integer, Map<Integer, PlacementInfo>> ships, GameState state) throws Exception {

        if (this.players != null) {
            players.removeIf(client -> this.players.contains(client));
        }

        this.players = players;

        Platform.runLater(() -> {
            try {
                fieldInit(players);
            } catch (Exception e) {
                e.printStackTrace();
            }
            placeShips(ships);
            redoShots(shots);

        });

        switch (state) {
            case LOBBY:
                status.setText("Lobby");
                break;
            case IN_PROGRESS:
                status.setText("Läuft");
                setConfig();
                model.remainingTimeRequest();
                model.pointsRequest();
                startTimer();
                break;
            case PAUSED:
                status.setText("Pausiert");
                setConfig();
                model.pointsRequest();
                model.remainingTimeRequest();
                startTimer();
                time.pause();
                break;
            case FINISHED:
                status.setText("Beendet");
                setConfig();
                model.pointsRequest();
                break;
        }

    }

    /**
     * Starts shot() in the Controller of every GameField.
     *
     * @param shots Collection of all still placed Shots.
     */
    public void redoShots(Collection<Shot> shots) {
        Object[] shotArray = shots.toArray();
        for (int i = 0; i < shotArray.length; i++) {
            Shot shot = (Shot) shotArray[i];
            int id = shot.getClientId();
            GameFieldController controller = controllerMap.get(id);
            controller.shot(shot.getTargetField());
        }
    }

    /**
     * Starts placeShip() in the Controller of every GameField.
     *
     * @param ships Map of Ships and PlacementInfo
     */
    public void placeShips(Map<Integer, Map<Integer, PlacementInfo>> ships) {
        Set<Integer> keys = ships.keySet();
        Object[] keysArray = keys.toArray();
        for (Object o : keysArray) {
            GameFieldController controller = controllerMap.get(o);
            controller.placeShips(ships.get(o), game.getConfig().getShips());
        }
    }

    /**
     * Adds a new GameField and related Controller for every Player.
     *
     * @param clientList Collection of all Players.
     * @throws Exception
     */
    public void fieldInit(Collection<Client> clientList) throws Exception {
        Object[] clientArray = clientList.toArray();
        for (Object o : clientArray) {
            Client client = (Client) o;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../../../resources/GameFieldView.fxml"));
            inGame = loader.load();
            spielfelder.getChildren().add(inGame);
            GameFieldController gameFieldController = loader.getController();
            gameFieldController.setParent(this);
            gameFieldController.setConfig(client.getName(), game, inGame);
            controllerMap.put(client.getId(), gameFieldController);                //Create a Map of PlayerId and Controller Object
            fieldMap.put(client.getId(), inGame);
        }
    }

    /**
     * Starts missedShots(),hitShots() and setPoints().
     *
     * @param hits   Shots that hit an Ship
     * @param points Total Points of every Player.
     * @param sunk   Shots that make a Ship sink.
     * @param missed Shots that hit a WaterField.
     */
    public void spectatorUpdateNotification(Collection<Shot> hits, Map<Integer, Integer> points,
                                            Collection<Shot> sunk, Collection<Shot> missed) {
        InGameController thisOb = this;

        Platform.runLater(() -> {
            thisOb.missedShots(missed);
            thisOb.hitShots(hits);
            thisOb.setPoints(points);
            thisOb.points = points;
        });
    }

    /**
     * Starts setPoints() for every Controller related to a GameField.
     *
     * @param points Map of Points and Players
     */
    public void setPoints(Map<Integer, Integer> points) {
        Set<Integer> clientIds = points.keySet();
        for (Integer clientId : clientIds) {
            GameFieldController controller = controllerMap.get(clientId);
            controller.setPoints(points.get(clientId));
        }
    }

    /**
     * Starts missHit for every missed Shot on the Controller its related to.
     *
     * @param missed Collection of Shots that hit a water field
     */
    public void missedShots(Collection<Shot> missed) {
        for (Shot s : missed) {
            int id = s.getClientId();
            GameFieldController controller = controllerMap.get(id);
            controller.missHit(s.getTargetField());
        }
    }

    /**
     * Starts shipHit for every hit Shot on the Controller its related to.
     *
     * @param hits Collection of Shots that hit a Ship
     */
    public void hitShots(Collection<Shot> hits) {
        Object[] hitsArray = hits.toArray();
        for (Object o : hitsArray) {
            Shot hit = (Shot) o;
            int id = hit.getClientId();
            GameFieldController controller = controllerMap.get(id);
            controller.shipHit(hit.getTargetField());
        }
    }

    /**
     * Sends a second GameStateRequest when the Game starts.
     * Starts startTimer().
     */
    public void roundStartNotification() {
        this.millis = game.getConfig().getRoundTime();
        time.play();
    }

    public void gameStartNotification() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                status.setText("Läuft");
            }
        });
        this.millis = game.getConfig().getRoundTime();
        this.startTimer();
    }

    /**
     * Starts setPoints().
     *
     * @param pointsMap Map of Players and Points
     */
    public void pointsResponse(
            Map<Integer, Integer> pointsMap) {
        this.setPoints(pointsMap);
    }

    /**
     * Sets the remaining Time after a remainingTimeRequest.
     *
     * @param time Remaining Time
     */
    public void remainingTimeResponse(
            long time) {
        this.millis = time;
    }

    /**
     * Shows an Alert when a Player leaves the Game.
     *
     * @param playerId Id of the Player that left the Game
     */
    public void leaveNotification(int playerId) {
        Alert alertLeave = new Alert(Alert.AlertType.INFORMATION, "Der Spieler " + playerId + " hat das Spiel verlassen.", ButtonType.OK);
        alertLeave.showAndWait();
    }

    /**
     * Shows an Alert when the Game is paused and
     * pauses the remaining Time Timer.
     */
    public void pauseNotification() {
        time.pause();
        Alert alertPaused = new Alert(Alert.AlertType.INFORMATION, "Das Spiel wurde pausiert",
                ButtonType.OK);
        alertPaused.showAndWait();
    }

    /**
     * Shows an Alert when the Game is continued and
     * continues the remaining Time Timer.
     */
    public void continueNotification() {
        time.play();
        Alert alertContinue = new Alert(Alert.AlertType.INFORMATION, "Das Spiel läuft weiter",
                ButtonType.OK);
        alertContinue.showAndWait();
    }

    /**
     * Shows an Alert with the Winner when the Game is finished and
     * stops the remaining Time Timer. Starts showRanking().
     *
     * @param points Map of Players and Points
     * @param winner Id of the Winner
     */
    public void FinishNotification(Map<Integer, Integer> points, int winner) {
        time.stop();
        Platform.runLater(() -> {
            Alert alertFinish = new Alert(Alert.AlertType.INFORMATION,
                    "Das Spiel ist vorbei. Der Gewinner ist " + winner, ButtonType.OK);
            alertFinish.showAndWait();
            showRanking();
        });
    }


    @FXML
    public void help() throws IOException {
        Help help = new Help();
        try{
            help.display("InGame-Help", "InGame-Help");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
