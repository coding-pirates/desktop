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

public class PlaceshipsModel implements PlaceShipsResponseListener, GameStartNotificationListener, GameInitNotificationListener {
    private Map<Integer, PlacementInfo> placedShips;
    private Map<Integer,ShipType> shipTypes;
    private Collection<Client> clientList;
    private Game currentGame;
    private int selectedShip;
    private Rotation currentRotation = Rotation.NONE;
    private boolean gameInitReceived = false;

    public PlaceshipsModel(){
        placedShips = new HashMap<>();
        ListenerHandler.registerListener((MessageHandlerListener) this);
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
                        || placedShipPoints.contains(new Point2D(point.getX() + 1, point.getY() - 1))) {
                    return false;
                }
            }
        }
        return true;
    }

    public void addShipPlacement(int shipID, PlacementInfo shipPlacement){
        placedShips.put(shipID, shipPlacement);
    }

    public ArrayList<Point2D> getShipPoints(PlacementInfo placementInfo, ShipType shipType){
        ArrayList<Point2D> movedShipPoints = new ArrayList<>();
        for (Point2D point : shipType.getPositions()) {
            movedShipPoints.add(new Point2D(placementInfo.getPosition().getX()+point.getX(), placementInfo.getPosition().getY() + point.getY()));
        }
        return movedShipPoints;
    }

    public void rotateLeft(){

    }
    public void rotateRight(){

    }

    public void sendPlaceShipsRequest(PlaceShipsController sender){
        if(placedShips.size() == shipTypes.size()) {
            if(gameInitReceived) {
                try {
                    BattleshipsDesktopClientApplication
                            .getInstance()
                            .getTcpConnector()
                            .sendMessageToServer(RequestBuilder.placeShipsRequest(placedShips));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                sender.showWaitForGameInit();
            }
        }
        else{
            sender.showPlaceAllShips();
        }
    }
    @Override
    public void onPlaceShipsResponse(PlaceShipsResponse message, int clientId) {
       //TODO Message Ships placed successfully
        /*Platform.runLater(()->{
            // InGameModel inGameModel = new InGameModel(game);
            Stage inGameStage = new Stage();
            try {
                inGameStage.display();
              //  closeStage();
            } catch (Exception e) {
                e.printStackTrace();
                inGameStage.setOnCloseRequest((t -> {
                    Platform.exit();
                    System.exit(0);
                }));
            };
        });*/
    }

    @Override
    public void onGameStartNotification(GameStartNotification message, int clientId) {
        Platform.runLater(()->{
            InGame inGame = new InGame();
            Stage inGameStage = new Stage();
            try {
                inGame.start(inGameStage,currentGame, clientList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            gameInitReceived = false;
        });
    }

    @Override
    public void onGameInitNotification(GameInitNotification message, int clientId) {
        gameInitReceived = true;
        this.clientList = message.getClientList();
    }
}
