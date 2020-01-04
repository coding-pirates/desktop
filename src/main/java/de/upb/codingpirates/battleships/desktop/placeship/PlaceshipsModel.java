package de.upb.codingpirates.battleships.desktop.placeship;

import de.upb.codingpirates.battleships.logic.Point2D;

import java.util.Collection;

public class PlaceshipsModel {
    private Collection<Point2D> alreadySetShipsPoints;
    public boolean proofShip(Collection<Point2D> shipPoints){
        for(Point2D point : shipPoints){
            if(alreadySetShipsPoints.contains(point)
                    ||alreadySetShipsPoints.contains(new Point2D(point.getX()+1,point.getY()))
                    ||alreadySetShipsPoints.contains(new Point2D(point.getX(),point.getY()+1))
                    ||alreadySetShipsPoints.contains(new Point2D(point.getX()-1,point.getY()))
                    ||alreadySetShipsPoints.contains(new Point2D(point.getX(),point.getY()-1))
                    ||alreadySetShipsPoints.contains(new Point2D(point.getX()+1,point.getY()+1))
                    ||alreadySetShipsPoints.contains(new Point2D(point.getX()-1,point.getY()+1))
                    ||alreadySetShipsPoints.contains(new Point2D(point.getX()-1,point.getY()-1))
                    ||alreadySetShipsPoints.contains(new Point2D(point.getX()+1,point.getY()-1))){
                return false;
            }
        }
        alreadySetShipsPoints.addAll(shipPoints);
        return true;
    }
}
