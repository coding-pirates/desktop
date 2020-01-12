package de.upb.codingpirates.battleships.desktop.gamefield;

import de.upb.codingpirates.battleships.logic.Point2D;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Random;

/**
 * Class that implements the GameField of one Player.
 */
public class GameField {

    private GridPane gameField;
    private int nbRow;
    private int nbColumn;
    private HashMap<String, Rectangle> map = new HashMap<>();

    /**
     * Constructor sets a new GridPane.
     * Starts build().
     *
     * @param nbRow    Rows in the GridPane
     * @param nbColumn Columns in the GridPane
     */
    public GameField(int nbRow, int nbColumn) {
        this.gameField = new GridPane();
        gameField.setMinSize(1, 1);
        gameField.setAlignment(Pos.CENTER);
        this.nbRow = nbRow;
        this.nbColumn = nbColumn;
        build();
    }

    /**
     * Builds the GameField with the number of Fields needed.
     */
    public void build() {
        ReadOnlyDoubleProperty heightProperty = gameField.heightProperty();
        ReadOnlyDoubleProperty widthProperty = gameField.widthProperty();
        Image img = new Image(String.valueOf(GameField.class.getResource("/images/field.png")));

        for (int i = 0; i < nbColumn; i++) {
            for (int j = 0; j < nbRow; j++) {
                Rectangle rectangle = new Rectangle(1, 1);
                rectangle.widthProperty().bind(widthProperty.divide(nbColumn + (nbColumn - 1) * 0.4));
                rectangle.heightProperty().bind(heightProperty.divide(nbRow + (nbRow - 1) * 0.4));
                rectangle.setFill(new ImagePattern(img));
                gameField.add(rectangle, i, j);
                map.put(i + "," + ((nbRow - 1) - j), rectangle);
            }
        }
        gameField.setVgap(gameField.getWidth() / nbColumn);
        gameField.setHgap(gameField.getHeight() / nbRow);
    }

    /**
     * Get Method for the gameField.
     *
     * @return GridPane gameField
     */
    public GridPane getDisplay() {
        return gameField;
    }

    /**
     * Changes the Image where a Ship was hit by a Shot.
     *
     * @param shot
     */
    public void shipHit(Point2D shot) {
        String point = (shot.getX() + "," + shot.getY());
        Image img = new Image(String.valueOf(GameField.class.getResource("/images/field_hit.png")));
        map.get(point).setFill(new ImagePattern(img));
        Tooltip toolTip = new Tooltip("Beschossen von Spieler");
        Tooltip.install(map.get(point), toolTip);
    }

    /**
     * Changes the Image where a WaterField was hit by a Shot.
     *
     * @param shot
     */
    public void waterHit(Point2D shot) {
        String point = (shot.getX() + "," + shot.getY());
        Image img = new Image(String.valueOf(GameField.class.getResource("/images/field_hit.png")));
        map.get(point).setFill(new ImagePattern(img));
        Tooltip toolTip = new Tooltip("Beschossen von Spieler");
        Tooltip.install(map.get(point), toolTip);
    }

    public void shipPlaced(Point2D ship){
        String point = (ship.getX() + "," + ship.getY());
        Image img0 = new Image(String.valueOf(GameField.class.getResource("/images/ship1.png")));
        Image img1 = new Image(String.valueOf(GameField.class.getResource("/images/ship2.png")));
        Image img2 = new Image(String.valueOf(GameField.class.getResource("/images/ship3.png")));
        switch(new Random().nextInt(3)){
            case 0:
                map.get(point).setFill(new ImagePattern(img0));
                break;
            case 1:
                map.get(point).setFill(new ImagePattern(img1));
                break;
            case 2:
                map.get(point).setFill(new ImagePattern(img2));
        }
        Tooltip toolTip = new Tooltip("Schiff gesetzt");
        Tooltip.install(map.get(point), toolTip);
    }

    /**
     * Get Method for HashMap.
     *
     * @return HashMap map
     */
    public HashMap<String, Rectangle> getHashmap() {
        return map;
    }

    public int getRow(){return nbRow;};
    public int getCol(){return nbColumn;}
}
