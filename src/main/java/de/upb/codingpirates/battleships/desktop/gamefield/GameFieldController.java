package de.upb.codingpirates.battleships.desktop.gamefield;

import de.upb.codingpirates.battleships.desktop.ingame.InGameController;
import de.upb.codingpirates.battleships.logic.Game;
import de.upb.codingpirates.battleships.logic.PlacementInfo;
import de.upb.codingpirates.battleships.logic.Point2D;
import de.upb.codingpirates.battleships.logic.ShipType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Controller Class for the GameField of every Player.
 */
public class GameFieldController implements Initializable {

    private InGameController parent;
    private int height;
    private int width;
    private String[][] type;
    private GameField gameField;
    private Node inGame;
    private Game game;

    @FXML
    private BorderPane borderPane;
    @FXML
    private Text name;
    @FXML
    private Text points;

    /**
     * Set Method for Parent.
     *
     * @param temp related InGameController
     */
    public void setParent(InGameController temp) {
        parent = temp;
    }

    public Node getInGame() {
        return inGame;
    }

    /**
     * Set Method for Configuration.
     *
     * @param name   Name of the Player
     * @param game   Selected Game
     * @param inGame Node
     */
    public void setConfig(String name, Game game, Node inGame) {
        this.name.setText("Name: " + name);
        this.game = game;
        this.inGame = inGame;
    }

    /**
     * Shows GameField on a bigger Screen.
     */
    public void enlargeBoard() {
        parent.enlargeBoard(this);
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

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                type = new String[height][width];
                type[i][j] = "water";
            }
        }
    }

    /**
     * Places a Shot. Decides if a Ship or Water was hit.
     *
     * @param shot Point2D Coordinates of the Shot
     */
    public void shot(Point2D shot) {
        if (type[shot.getX()][shot.getY()] == "ship") {
            gameField.shipHit(shot);
        } else {
            gameField.waterHit(shot);
        }
    }

    /**
     * Places a Ship on every Field where a Ship was placed by the Player.
     *
     * @param ships Map of ShipIds and PlacementInfo
     * @param place Map of ShipIds and Ships
     */
    public void placeShips(Map<Integer, PlacementInfo> ships, Map<Integer, ShipType> place) {
        Set<Integer> keys = ships.keySet();
        Object[] keysArray = keys.toArray();
        for (int i = 0; i < keysArray.length; i++) {
            Point2D[] places = (Point2D[]) place.get(keysArray[i]).getPositions().toArray();
            for (int j = 0; j < places.length; j++) {
                type[places[j].getX()][places[j].getY()] = "ship";
                System.out.println("Schiff auf " + places[j].getX() + "," + places[j].getY());
            }
        }
    }

    /**
     * Starts gameFields shipHit().
     *
     * @param hit Point2D Shot that hit a Ship
     */
    public void shipHit(Point2D hit) {
        gameField.shipHit(hit);
    }

    /**
     * Starts gameFields waterHit().
     *
     * @param miss Point2D Shot that hit a WaterField
     */
    public void missHit(Point2D miss) {
        gameField.waterHit(miss);
    }

    /**
     * Set Method for the Player's points.
     *
     * @param points Points of the Player
     */
    public void setPoints(Integer points) {
        this.points.setText("Punkte: " + points);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }

}
