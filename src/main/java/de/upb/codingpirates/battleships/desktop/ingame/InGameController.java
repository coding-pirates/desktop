package de.upb.codingpirates.battleships.desktop.ingame;

import de.upb.codingpirates.battleships.desktop.endgame.Endgame;
import de.upb.codingpirates.battleships.desktop.gamefield.GameFieldController;
import de.upb.codingpirates.battleships.desktop.lobby.Lobby;
import de.upb.codingpirates.battleships.desktop.ranking.Ranking;
import de.upb.codingpirates.battleships.desktop.settings.Settings;
import de.upb.codingpirates.battleships.desktop.util.FxmlLoader;
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
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
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
public class InGameController implements Initializable, FxmlLoader {

    /**
     * model class of this view
     */
    private InGameModel model;
    /**
     * ranking class for the players
     */
    private Ranking ranking;
    /**
     * time to place the shots
     */
    private Long millis;
    /**
     * height of the gamefield
     */
    private int height;
    /**
     * width of the gamefield
     */
    private int width;
    /**
     * Map with id of gamefield/player and gamefieldcontroller class
     */
    private HashMap<Integer, GameFieldController> controllerMap = new HashMap<Integer, GameFieldController>();
    /**
     * Map with id of gamefield and the node
     */
    private HashMap<Integer, Node> fieldMap = new HashMap<Integer, Node>();
    /**
     * Node inGame
     */
    private Node inGame;
    /**
     * configuration for this game
     */
    private Configuration config;
    /**
     * timelien for counting the time
     */
    private Timeline time = new Timeline();
    /**
     * collection of all players
     */
    private Collection<Client> players = null;
    /**
     * gamefield is enlarged
     */
    private boolean enlarged = false;
    /**
     * map with playerId and points
     */
    private Map<Integer, Integer> points;
    /**
     * Game logic Class
     */
    private Game game;

    //view
    /**
     * Layout for the gamefields
     */
    @FXML
    private GridPane grid;
    /**
     * field for amount of player
     */
    @FXML
    private Label maxPlayerCount;
    /**
     * field for amount of shots
     */
    @FXML
    private Label shotCount;
    /**
     * field for showing points for hitting a ship
     */
    @FXML
    private Label hitPoints;
    /**
     * field for showing points for a sunken ship
     */
    @FXML
    private Label sunkPoints;
    /**
     * field for showing the time for one round
     */
    @FXML
    private Label roundTime;
    /**
     * field for showing the status
     */
    @FXML
    private Label status;
    /**
     * layout to fill in the gamefields
     */
    @FXML
    private FlowPane spielfelder;
    /**
     * layout to seperate one large gamefield to the others
     */
    @FXML
    private SplitPane splitPane;
    /**
     * field for showing the restTime for this one round
     */
    @FXML
    private Label restTime;
    /**
     * Indicator for the waiting time
     */
    @FXML
    private ProgressIndicator progressindicator;
    /**
     * layout for progressIndicator
     */
    @FXML
    private BorderPane borderpane;

    /**
     * Initialization
     * @param arg0 URL
     * @param arg1 ResourceBundle
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        this.model = new InGameModel();
        model.setInGameController(this);
        points = new HashMap<Integer,Integer>();
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
        model.setGame(game);
        this.game = game;
    }

    public long getTime(){
        return millis;
    }

    public void setClientType(ClientType clientType){
        model.setClientType(clientType);
    }

    public void setOwnShipPlacements(Map<Integer, PlacementInfo> ownShipPlacements){
        model.setOwnShipPlacements(ownShipPlacements);
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
        borderpane.setPadding(new Insets(1, 1, 1, 1));
        splitPane.setDividerPositions(0.18);
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
                millis = millis-1000;
                Long seconds = millis/1000;
                restTime.setText((seconds.toString()));
                if (millis <= 0) {
                    time.stop();
                }

            }

        });
        time.getKeyFrames().add(frame);
        time.playFromStart();
    }

    public GameState getGameState(){
        return model.getGameState();
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

    @FXML
    public void showRanking() {
        if (points != null) {
            this.ranking = new Ranking();
            Stage rankingStage = new Stage();
            try {
                ranking.display(rankingStage);
                ranking.setPlayer(players);
                ranking.sortPoints(points);

            } catch (Exception e) {
                System.out.println("Fehler: " + e);
            }

        }
        else
        {
            Alert alertContinue = new Alert(Alert.AlertType.INFORMATION, "Noch keine Punkte verfuegbar!! ",
                    ButtonType.OK);
            alertContinue.showAndWait();
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
        config = game.getConfig();
        height = config.getHeight();
        width = config.getWidth();
        millis = config.getRoundTime();
            maxPlayerCount.setText(Integer.toString(game.getCurrentPlayerCount()));
            shotCount.setText(Integer.toString(game.getConfig().getShotCount()));
            hitPoints.setText(Integer.toString(game.getConfig().getHitPoints()));
            sunkPoints.setText(Integer.toString(game.getConfig().getSunkPoints()));
            roundTime.setText(Long.toString(game.getConfig().getRoundTime()/1000));
            controllerMap.forEach((client, controller) -> {
                controller.buildBoard(game.getConfig().getHeight(), game.getConfig().getWidth());
            });


    }

    /**
     * Takes Configuration of the Game and displays the Information in the TextFields.
     *
     */
    public void sendGameStateRequest() {
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
    public void gameStateResponse(ClientType clientType, Collection<Client> players, Collection<Shot> shots, Map<Integer, Map<Integer, PlacementInfo>> ships, GameState state) throws Exception {

        if (this.players != null) {
            players.removeIf(client -> this.players.contains(client));
        }

        this.players = players;

            try {
                fieldInit(players);
            } catch (Exception e) {
                e.printStackTrace();
            }

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
            if(clientType == ClientType.SPECTATOR) {
                placeShips(ships);
            }
            else{
                GameFieldController controller = controllerMap.get(model.getClientID());
                controller.placePlayerShips(model.getOwnShipPlacements(), game.getConfig().getShips());
            }
            redoShots(shots);
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
            FXMLLoader loader = this.getLoader("GameFieldView");
            inGame = loader.load();
            spielfelder.getChildren().add(inGame);
            GameFieldController gameFieldController = loader.getController();
            gameFieldController.setParent(this);
            gameFieldController.setConfig(client.getName(), game, inGame);
            gameFieldController.setPlayerID(client.getId());
            controllerMap.put(client.getId(), gameFieldController);                //Create a Map of PlayerId and Controller Object
            fieldMap.put(client.getId(), inGame);
        }
        progressindicator.setVisible(false);
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

            thisOb.missedShots(missed);
            thisOb.hitShots(hits);
            thisOb.setPoints(points);
            thisOb.points = points;
    }

    public void playerUpdateNotification(Collection<Shot> hits, Map<Integer, Integer> points) {
        InGameController thisOb = this;

            thisOb.hitShots(hits);
            thisOb.setPoints(points);
            thisOb.points = points;
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

    public ArrayList<Shot> getPlacedShots(){
        return model.getPlacedShots();
    }

    public void addShot(int playerId, Point2D shot){
        model.addShot(playerId, shot);
    }

    public void removeShot(int playerId, Point2D shot){
        model.removeShot(playerId, shot);
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

   /* public void gameStartNotification() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                status.setText("Läuft");
            }
        });
        this.millis = game.getConfig().getRoundTime();
        this.startTimer();
    } */

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
            try {
                startEndView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * starts the EndView with the Winners of the game
     *
     * @throws Exception
     */
    private void startEndView() throws Exception {
        Endgame endgame = new Endgame();
        Stage endStage = new Stage();

        endStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });

        try {
            endgame.display(endStage,points,players,model.getClientID());
            closeStage();
        } catch (IOException e) {
            e.printStackTrace();//TODO
        }

    }

    /**
     * starts the HelpView with accessibility tools in an extra window
     * @throws IOException
     */
    @FXML
    public void help() throws IOException {
        Help help = new Help();
        try{
            help.display("InGame-Help");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    public void settings() throws Exception {

        Settings settings = new Settings();
        Stage settingsStage = new Stage();
        try {
            settings.display(settingsStage);
        } catch (IOException e) {
            e.printStackTrace();//TODO
        }
        /*settingsStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });*/
    }
    public void closeStage(){
        Stage stage = (Stage) shotCount.getScene().getWindow();
        stage.close();
    }

    /**
     * starts the LobbyView and closes this window
     */
    @FXML
    public void leave(){
        model.sendGameLeaveRequest();
    }

    public void gameLeaveNotification(int clientId){
        Lobby lobby = new Lobby();
        Stage lobbyStage = new Stage();

        lobbyStage.setOnCloseRequest(t->{
            Platform.exit();
            System.exit(0);
        });

        try {
            lobby.display(lobbyStage,clientId);
            closeStage();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @FXML
    public void shot(){
        model.sendShotRequest();
    }

    /**
     * Clickevent for GridPane (print grid-cell, which is clicked)
     * @param event
     */
  public void clickGrid(javafx.scene.input.MouseEvent event) {
        if(event.getClickCount() == 2){
            Node clickedNode = event.getPickResult().getIntersectedNode();
            if (clickedNode != grid) {
                // click on descendant node
                Integer colIndex = GridPane.getColumnIndex(clickedNode);
                Integer rowIndex = GridPane.getRowIndex(clickedNode);
                System.out.println("Mouse clicked cell: " + colIndex + " And: " + rowIndex);

            }}
    }

    public void setClientID(int clientID){
      model.setClientID(clientID);
    }

    /** Scroll event with mouse for the gamefield
     * @param event
     */
    @FXML
    public void scroll(javafx.scene.input.ScrollEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if (clickedNode != grid) {
            double zoomFactor = 1.05;
            double deltaY = event.getDeltaY();
            if (deltaY < 0){
                zoomFactor = 2.0 - zoomFactor;
            }
            borderpane.setScaleX(borderpane.getScaleX() * zoomFactor);
            borderpane.setScaleY(borderpane.getScaleY() * zoomFactor);
        }
    }

}

