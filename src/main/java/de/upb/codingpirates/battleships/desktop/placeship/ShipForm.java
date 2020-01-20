package de.upb.codingpirates.battleships.desktop.placeship;

import de.upb.codingpirates.battleships.desktop.gamefield.GameField;
import de.upb.codingpirates.battleships.logic.Point2D;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.Collection;

/**
 * class to visualize the shipForm
 */
public class ShipForm {

    /**
     * Layout for shipFomr
     */
    private GridPane shipForm;
    /**
     * amount of rows of the gridPane
     */
    private int nbRow;
    /**
     * amount of the columns of the gridPane
     */
    private int nbColumn;

    /**
     * Constructor of this class and visualize it
     *
     * @param positions of one ship
     */
    public ShipForm(Collection<Point2D> positions) {
        this.shipForm = new GridPane();
        shipForm.setMinSize(1, 1);
        shipForm.setAlignment(Pos.CENTER);
        shipForm.setRotate(0.0);
        this.nbRow = height(positions)+1;
        this.nbColumn = height(positions)+1;
        System.out.println(positions);
        setShip(positions);

    }

    /**
     * Gets height/width of the ship for the layout
     * @param positions
     * @return
     */
    public int height(Collection<Point2D> positions){
        int maxHeight=0;
        for (Point2D pos:positions){
            maxHeight = Math.max(pos.getY(), maxHeight);
            maxHeight = Math.max(pos.getX(), maxHeight);
        }
        return maxHeight;
    }

    /**
     * builds the layout around the ship and the ship
     * @param positions
     */
    public void setShip(Collection<Point2D> positions){
        ReadOnlyDoubleProperty heightProperty = shipForm.heightProperty();
        ReadOnlyDoubleProperty widthProperty = shipForm.widthProperty();
        Image img1 = new Image(String.valueOf(ShipForm.class.getResource("/images/ship1.png")));
        Image img = new Image(String.valueOf(GameField.class.getResource("/images/field.png")));
        for (int i = 0; i < nbColumn; i++) {
            for (int j = 0; j < nbRow; j++) {
                Rectangle rectangle = new Rectangle(1, 1);
                rectangle.widthProperty().bind(widthProperty.divide(nbColumn + (nbColumn - 1) * 0.1));
                rectangle.heightProperty().bind(heightProperty.divide(nbRow + (nbRow - 1) * 0.1));

                if(positions.contains(new Point2D(i,j))){
                    rectangle.setFill(new ImagePattern(img1));}
                else{
                rectangle.setFill(new ImagePattern(img));}
            shipForm.add(rectangle, i, j);
            System.out.println(i+""+j);
        }}
        shipForm.setVgap(shipForm.getWidth() / nbColumn);
        shipForm.setHgap(shipForm.getHeight() / nbRow);

    }

    /**
     * visualize the shipForm
     * @return Gridpane  shipForm
     */
    public GridPane getDisplay() {

        return shipForm;
    }

    /**
     * rotate the ship
     */
    public void rotate(){
        double rotation = shipForm.getRotate();
        switch((int) rotation){
            case 0:
                shipForm.setRotate(90);
                break;
            case 90:
                shipForm.setRotate(180);
                break;
            case 180:
                shipForm.setRotate(270);
                break;
            case 270:
                shipForm.setRotate(0);
        }

    }

}

