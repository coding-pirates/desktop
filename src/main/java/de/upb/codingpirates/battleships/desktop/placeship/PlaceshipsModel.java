package de.upb.codingpirates.battleships.desktop.placeship;

import de.upb.codingpirates.battleships.client.ListenerHandler;
import de.upb.codingpirates.battleships.client.listener.GameInitNotificationListener;
import de.upb.codingpirates.battleships.client.listener.GameStartNotificationListener;
import de.upb.codingpirates.battleships.client.listener.MessageHandlerListener;
import de.upb.codingpirates.battleships.client.listener.PlaceShipsResponseListener;
import de.upb.codingpirates.battleships.desktop.BattleshipsDesktopClientApplication;
import de.upb.codingpirates.battleships.desktop.ingame.InGame;
import de.upb.codingpirates.battleships.logic.*;
import de.upb.codingpirates.battleships.network.message.notification.GameInitNotification;
import de.upb.codingpirates.battleships.network.message.notification.GameStartNotification;
import de.upb.codingpirates.battleships.network.message.request.RequestBuilder;
import de.upb.codingpirates.battleships.network.message.response.PlaceShipsResponse;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PlaceshipsModel{
    private Map<Integer, PlacementInfo> placedShips;
    private Map<Integer,ShipType> shipTypes;
    private Collection<Client> clientList;
    private Game currentGame;
    private int clientID;
    private int selectedShip;
    private Rotation currentRotation = Rotation.NONE;

    public PlaceshipsModel(){
        placedShips = new HashMap<>();
    }

    public void setPlacedShips(Map<Integer, PlacementInfo> placedShips) {
        this.placedShips = placedShips;
    }

    public void setShipTypes(Map<Integer, ShipType> shipTypes) {
        this.shipTypes = shipTypes;
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }


    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }


    public int getSelectedShip() {
        return selectedShip;
    }

    public void setSelectedShip(int selectedShip) {
        this.selectedShip = selectedShip;
    }

    public Rotation getCurrentRotation() {
        return currentRotation;
    }

    public void setCurrentRotation(Rotation currentRotation) {
        this.currentRotation = currentRotation;
    }

    public Collection<Client> getClientList() {
        return clientList;
    }

    public void setClientList(Collection<Client> clientList) {
        this.clientList = clientList;
    }

    public void addShipTypes(Map<Integer,ShipType> shipTypes){
        this.shipTypes = shipTypes;
    }

    public Map<Integer, PlacementInfo> getPlacedShips(){
        return placedShips;
    }
    public Map<Integer,ShipType> getShipTypes(){
        return shipTypes;
    }

    public boolean proofShip(Collection<Point2D> shipPoints){
            for(Integer placedShipId : placedShips.keySet()) {
                ArrayList<Point2D> placedShipPoints = getShipPoints(placedShips.get(placedShipId), shipTypes.get(placedShipId));
                for(Point2D point : shipPoints){
                if (placedShipPoints.contains(point)
                        || placedShipPoints.contains(new Point2D(point.getX() + 1, point.getY()))
                        || placedShipPoints.contains(new Point2D(point.getX(), point.getY() + 1))
                        || placedShipPoints.contains(new Point2D(point.getX() - 1, point.getY()))
                        || placedShipPoints.contains(new Point2D(point.getX(), point.getY() - 1))
                        || placedShipPoints.contains(new Point2D(point.getX() + 1, point.getY() + 1))
                        || placedShipPoints.contains(new Point2D(point.getX() - 1, point.getY() + 1))
                        || placedShipPoints.contains(new Point2D(point.getX() - 1, point.getY() - 1))
                        || placedShipPoints.contains(new Point2D(point.getX() + 1, point.getY() - 1))
                        || point.getY()>= currentGame.getConfig().getHeight()-1
                        || point.getX()>= currentGame.getConfig().getWidth()-1
                        || point.getY()<0
                        || point.getX()<0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void addShipPlacement(int shipID, PlacementInfo shipPlacement){
        placedShips.put(shipID, shipPlacement);
        currentRotation = Rotation.NONE;
    }

    public ArrayList<Point2D> getShipPoints(PlacementInfo placementInfo, ShipType shipType){
        ArrayList<Point2D> movedShipPoints = new ArrayList<>();
        for (Point2D point : shipType.getPositions()) {
            Point2D rotatedPoint = rotatePoint(point, placementInfo.getRotation().ordinal());
            movedShipPoints.add(new Point2D(placementInfo.getPosition().getX()+rotatedPoint.getX(), placementInfo.getPosition().getY() + rotatedPoint.getY()));
        }
        return movedShipPoints;
    }

    public Point2D rotatePoint(Point2D point, int rotationEnum) {
        //rotation Matix
        double rotation = (4 - rotationEnum) * Math.PI / 2;
        int x = (int) Math.round(point.getX() * Math.cos(rotation) - point.getY() * Math.sin(rotation));
        int y = (int) Math.round(point.getX() * Math.sin(rotation) + point.getY() * Math.cos(rotation));
        return new Point2D(x, y);
    }
    public void rotateLeft(){

    }
    public void rotateRight(){

    }

    public void sendPlaceShipsRequest(PlaceShipsController sender){
        if(placedShips.size() == shipTypes.size()) {
            if(clientList!= null) {
                try {
                    BattleshipsDesktopClientApplication
                            .getInstance()
                            .getTcpConnector()
                            .sendMessageToServer(RequestBuilder.placeShipsRequest(placedShips));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            sender.showPlaceAllShips();
        }
    }

}
