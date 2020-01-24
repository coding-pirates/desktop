package de.upb.codingpirates.battleships.desktop.gamefield;

import de.upb.codingpirates.battleships.desktop.ingame.InGameController;
import de.upb.codingpirates.battleships.desktop.placeship.PlaceShipsController;
import de.upb.codingpirates.battleships.logic.*;
import de.upb.codingpirates.battleships.logic.Game;
import de.upb.codingpirates.battleships.logic.PlacementInfo;
import de.upb.codingpirates.battleships.logic.Point2D;
import de.upb.codingpirates.battleships.logic.ShipType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;

/**
 * Controller Class for the GameField of every Player.
 */
public class GameFieldController implements Initializable {

    /**
     * parent Controller from the whole GameView
     */
    private InGameController parent;
    /**
     * height of the gamefield
     */
    private int height;
    /**
     * width of the gamefield
     */
    private int width;
    /**
     * describe the type of the field (water, ship, hit)
     */
    private String[][] type;
    /**
     * gameField class
     */
    private GameField gameField;
    /**
     * node inGame
     */
    private Node inGame;
    /**
     * Game logic
     */
    private Game game;
    /**
     * Layout GridPane for the single gamefield
     */
    private GridPane grid;
    private int playerID;

    //view
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
    public void setParent(PlaceShipsController temp){parent = temp;}

    public void setPlayerID(int playerID){
        this.playerID = playerID;
    }

    /**
     * Returns the Node inGame
     * @return Node inGame
     */
    public Node getInGame()
    {
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
        //gameField = new GameField(height, width);
        gameField = new GameField(height, width);
        grid = gameField.getDisplay();
        borderPane.setPadding(new Insets(1, 1, 1, 1));
        borderPane.setCenter(grid);
        type = new String[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                type[i][j] = "water";
            }
        }
        System.out.println(borderPane.getCenter());
    }


    /**
     * Places a Shot. Decides if a Ship or Water was hit.
     *
     * @param shot Point2D Coordinates of the Shot
     */
    public void shot(Point2D shot) {
        if (Objects.equals(type[shot.getX()][shot.getY()], "ship")) {
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
            Point2D[] places = place.get(keysArray[i]).getPositions().toArray(new Point2D[place.size()]);
            for (int j = 0; j < places.length; j++) {
                type[places[j].getX()][places[j].getY()] = "ship";
                System.out.println("Schiff auf " + places[j].getX() + "," + places[j].getY());
            }
        }
    }

    public void placePlayerShips(Map<Integer, PlacementInfo> shipPlacements, Map<Integer, ShipType> shipTypes){
        ArrayList<Point2D> movedShipPoints = new ArrayList<>();
        for(int ship : shipPlacements.keySet()) {
            for (Point2D point : shipTypes.get(ship).getPositions()) {
                Point2D rotatedPoint = rotatePoint(point, shipPlacements.get(ship).getRotation().ordinal());
                movedShipPoints.add(new Point2D(shipPlacements.get(ship).getPosition().getX()+rotatedPoint.getX(), shipPlacements.get(ship).getPosition().getY() + rotatedPoint.getY()));
            }
            gameField.placeShip(movedShipPoints);
            movedShipPoints.clear();
        }
    }

    public Point2D rotatePoint(Point2D point, int rotationEnum) {
        //rotation Matix
        double rotation = (4 - rotationEnum) * Math.PI / 2;
        int x = (int) Math.round(point.getX() * Math.cos(rotation) - point.getY() * Math.sin(rotation));
        int y = (int) Math.round(point.getX() * Math.sin(rotation) + point.getY() * Math.cos(rotation));
        return new Point2D(x, y);
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

    /**
     * Initialization
     * @param arg0 URL
     * @param arg1 ResourceBundle
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }

    /**
     * Clickevent for GridPane (print grid-cell, which is clicked)
     *
     * @param event
     */
    public void clickGrid(javafx.scene.input.MouseEvent event) {
        if (event.getClickCount() == 2) {
        if(parent.getTime()>100) {
                    Node clickedNode = event.getPickResult().getIntersectedNode();
                    if (clickedNode != grid) {
                        // click on descendant node
                        Node parent = clickedNode.getParent();
                        while (parent != grid && parent != null) {
                            clickedNode = parent;
                            parent = clickedNode.getParent();
                        }
                        Integer colIndex = GridPane.getColumnIndex(clickedNode);
                        Integer rowIndex = GridPane.getRowIndex(clickedNode);
                        int row = gameField.getRow();
                        Point2D point = new Point2D(colIndex, row - rowIndex - 1);
                        if (this.parent.getPlacedShots().contains(new Shot(playerID, point))) {
                            gameField.removeShot(point);
                            this.parent.removeShot(playerID, point);
                        } else{
                            if (this.parent.getPlacedShots().size() < game.getConfig().getShotCount()) {
                            gameField.shotPlaced(point);
                            this.parent.addShot(playerID, point);
                        }
                            else{
                                Alert alertToMuchShots = new Alert(Alert.AlertType.INFORMATION,
                                        "Dieser Schuss kann nichtmehr gesetzt werden", ButtonType.OK);
                                alertToMuchShots.showAndWait();
                            }
                    }
                }
            }
        else{
            Alert alertToLate = new Alert(Alert.AlertType.INFORMATION,
                    "Der Schuss wurde leider zu spät gesetzt", ButtonType.OK);
            alertToLate.showAndWait();
        }
        }

    }
    @FXML
    public void scroll(javafx.scene.input.ScrollEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if (clickedNode != grid) {
            double zoomFactor = 1.05;
            double deltaY = event.getDeltaY();
            if (deltaY < 0){
                zoomFactor = 2.0 - zoomFactor;
            }
            gameField.getDisplay().setScaleX(borderPane.getScaleX() * zoomFactor);
            gameField.getDisplay().setScaleY(borderPane.getScaleY() * zoomFactor);
        }
    }
}
