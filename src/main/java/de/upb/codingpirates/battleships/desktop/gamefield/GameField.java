package de.upb.codingpirates.battleships.desktop.gamefield;

import de.upb.codingpirates.battleships.logic.Point2D;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;

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

        for (int i = 0; i < nbColumn; i++) {
            for (int j = 0; j < nbRow; j++) {
                Rectangle rectangle = new Rectangle(1, 1);
                rectangle.widthProperty().bind(widthProperty.divide(nbColumn + (nbColumn - 1) * 0.4));
                rectangle.heightProperty().bind(heightProperty.divide(nbRow + (nbRow - 1) * 0.4));
                rectangle.setFill(Color.WHITE);
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
     * Paints the Field grey where a Ship was hit by a Shot.
     *
     * @param shot
     */
    public void shipHit(Point2D shot) {
        String point = (shot.getX() + "," + shot.getY());
        map.get(point).setFill(Color.GREY);
        Tooltip toolTip = new Tooltip("Beschossen von Spieler");
        Tooltip.install(map.get(point), toolTip);
    }

    /**
     * Paints the Field blue where a WaterField was hit by a Shot.
     *
     * @param shot
     */
    public void waterHit(Point2D shot) {
        String point = (shot.getX() + "," + shot.getY());
        map.get(point).setFill(Color.BLUE);
        Tooltip toolTip = new Tooltip("Beschossen von Spieler");
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
}
