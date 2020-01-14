package de.upb.codingpirates.battleships.desktop.placeship;

import de.upb.codingpirates.battleships.desktop.endgame.Endgame;
import de.upb.codingpirates.battleships.desktop.gamefield.GameField;
import de.upb.codingpirates.battleships.desktop.gamefield.GameFieldController;
import de.upb.codingpirates.battleships.desktop.ingame.InGameController;
import de.upb.codingpirates.battleships.desktop.ingame.InGameModel;
import de.upb.codingpirates.battleships.desktop.lobby.Lobby;
import de.upb.codingpirates.battleships.desktop.ranking.Ranking;
import de.upb.codingpirates.battleships.desktop.settings.Settings;
import de.upb.codingpirates.battleships.desktop.util.GameView;
import de.upb.codingpirates.battleships.desktop.util.Help;
import de.upb.codingpirates.battleships.logic.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Controller Class for the PlaceShips Window.
 */
public class PlaceShipsController extends InGameController implements Initializable {

    @FXML
    private Button btn_rotate;
    @FXML
    private BorderPane borderPane;
    @FXML
    private GridPane grid;

    private int height;
    private int width;
    private GameField gameField;
    private String[][] type;
    private HashMap<Integer, GameFieldController> controllerMap = new HashMap<Integer, GameFieldController>();
    private HashMap<Integer, Node> fieldMap = new HashMap<Integer, Node>();
    private Game currentGame;
    private Map<Integer,PlacementInfo> placedShips;
    private Map<Integer,ShipType> shipsToPlace;
    private int selectedShip;


    public PlaceShipsController() {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        placedShips = new HashMap<>();
        shipsToPlace = new HashMap<>();
        selectedShip = 0;
    }

    public void setCurrentGame(Game currentGame){
        this.currentGame = currentGame;
        this.shipsToPlace.putAll(currentGame.getConfig().getShips());
    }

    public void closeStage() {
        Stage stage = (Stage) btn_rotate.getScene().getWindow();
        stage.close();
    }

    /**
     * next_Button
     * @throws Exception
     */
    @FXML
    public void handlerButton() throws Exception {
        Endgame endgame = new Endgame();
        Stage endstage = new Stage();
        try {
            endgame.display(endstage);
            closeStage();
        } catch (IOException e) {
            e.printStackTrace();//TODO
        }
        endstage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
    }

    @FXML
    public void help() throws IOException {
        Help help = new Help();
        try{
            help.display("PlaceShip-Help", "PlaceShip-Help");
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
        }
        catch (IOException e) {
            e.printStackTrace();//TODO
        }
        settingsStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });

    }

    @FXML
    public void back(){
        Lobby lobby = new Lobby();
        Stage lobbyStage = new Stage();
        try {
            lobby.display(lobbyStage);
            closeStage();
        } catch (IOException e) {
            e.printStackTrace();//TODO
        }
        lobbyStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
    }

    @FXML
    public void rotate(){
        //TODO
    }

    @FXML
    public void gamestart(){
        //TODO
       // InGameModel inGameModel = new InGameModel(game);
        Stage inGameStage = new Stage();
        try {
          //  inGameStage.start();
            closeStage();
        } catch (Exception e) {
            e.printStackTrace();
            inGameStage.setOnCloseRequest((t -> {
                Platform.exit();
                System.exit(0);
            }));
        };
    }

    /**
     * Adds a new GameField
     *
     * @throws Exception
     */
    public void fieldInit() {
        buildBoard(currentGame.getConfig().getHeight(),currentGame.getConfig().getWidth());
    }

    /**
     * Builds the GameField. Sets all Fields to WaterFields.
     */
    public void buildBoard(int height, int width) {
        this.height = height;
        this.width = width;
        gameField = new GameField(height, width);
        borderPane.setPadding(new Insets(1, 1, 1, 1));
        borderPane.setCenter(gameField.getDisplay());

    }

    /**
     * Clickevent for GridPane (print grid-cell, which is clicked)
     * @param event
     */
    public void clickGrid(javafx.scene.input.MouseEvent event) {
        if(!shipsToPlace.isEmpty()) {
            Node clickedNode = event.getPickResult().getIntersectedNode();
            if (clickedNode != grid) {
                // click on descendant node
                Integer colIndex = GridPane.getColumnIndex(clickedNode);
                Integer rowIndex = GridPane.getRowIndex(clickedNode);
                int row = gameField.getRow();
                int col = gameField.getCol();
                System.out.println("Mouse clicked cell: " + colIndex + " And: " + rowIndex);
                Point2D clickedPoint = new Point2D(colIndex, row - rowIndex-1);
                ArrayList<Point2D> movedShipPoints = new ArrayList<>();
                for (Point2D point : shipsToPlace.get(selectedShip).getPositions()) {
                    movedShipPoints.add(new Point2D(clickedPoint.getX()+point.getX(), clickedPoint.getY() + point.getY()));
                }
                gameField.shipPlaced(movedShipPoints);
                shipsToPlace.remove(selectedShip);
                placedShips.put(selectedShip, new PlacementInfo(clickedPoint,Rotation.NONE));
                if(!shipsToPlace.isEmpty()){
                    selectedShip = shipsToPlace.keySet().iterator().next();
                }
            }
        }
    }

}