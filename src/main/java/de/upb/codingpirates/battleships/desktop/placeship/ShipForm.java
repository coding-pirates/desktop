package de.upb.codingpirates.battleships.desktop.placeship;

import de.upb.codingpirates.battleships.desktop.gamefield.GameField;
import de.upb.codingpirates.battleships.logic.Point2D;
import de.upb.codingpirates.battleships.logic.Ship;
import de.upb.codingpirates.battleships.logic.ShipType;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.Collection;


public class ShipForm {

    private GridPane shipForm;
    private int nbRow;
    private int nbColumn;

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

    public int height(Collection<Point2D> positions){
        int maxHeight=0;
        for (Point2D pos:positions){
            maxHeight = Math.max(pos.getY(), maxHeight);
            maxHeight = Math.max(pos.getX(), maxHeight);
        }
        return maxHeight;
    }

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
    public GridPane getDisplay() {
        return shipForm;
    }

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

