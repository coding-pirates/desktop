package de.upb.codingpirates.battleships.desktop.placeship;

import de.upb.codingpirates.battleships.client.ListenerHandler;
import de.upb.codingpirates.battleships.client.listener.GameInitNotificationListener;
import de.upb.codingpirates.battleships.client.listener.GameStartNotificationListener;
import de.upb.codingpirates.battleships.client.listener.MessageHandlerListener;
import de.upb.codingpirates.battleships.client.listener.PlaceShipsResponseListener;
import de.upb.codingpirates.battleships.desktop.gamefield.GameField;
import de.upb.codingpirates.battleships.desktop.ingame.InGame;
import de.upb.codingpirates.battleships.desktop.ingame.InGameController;
import de.upb.codingpirates.battleships.desktop.settings.Settings;
import de.upb.codingpirates.battleships.desktop.util.Help;
import de.upb.codingpirates.battleships.logic.*;
import de.upb.codingpirates.battleships.network.message.notification.GameInitNotification;
import de.upb.codingpirates.battleships.network.message.notification.GameStartNotification;
import de.upb.codingpirates.battleships.network.message.response.PlaceShipsResponse;
import javafx.application.Platform;
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
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Controller Class for the PlaceShips Window.
 */
public class PlaceShipsController extends InGameController implements Initializable, PlaceShipsResponseListener, GameStartNotificationListener, GameInitNotificationListener {

    @FXML
    private Button btn_rotate;
    @FXML
    private BorderPane borderPane;
    @FXML
    private GridPane grid;
    @FXML
    private BorderPane smallBorderPane;

    private PlaceshipsModel model;
    private GameField gameField;

    private ShipForm shipForm;



    public PlaceShipsController() {
        ListenerHandler.registerListener((MessageHandlerListener) this);
        this.model = new PlaceshipsModel();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model.setSelectedShip(0);
    }

    public void setCurrentGame(Game currentGame){
       model.setCurrentGame(currentGame);
        model.addShipTypes(currentGame.getConfig().getShips()); //TODO maybe direct in Model
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
    public void help() throws IOException {
        Help help = new Help();
        try{
            help.display("PlaceShip-Help");
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
        //TODO leave Message
       /* Lobby lobby = new Lobby();
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
        }); */
    }

    @FXML
    public void rotate(){
        //TODO
        shipForm.rotate();
    }

    @FXML
    public void gamestart(){
        model.sendPlaceShipsRequest(this);
    }

    /**
     * Adds a new GameField
     *
     * @throws Exception
     */
    public void fieldInit() {
        buildBoard(model.getCurrentGame().getConfig().getHeight(),model.getCurrentGame().getConfig().getWidth());
    }

    /**
     * Builds the GameField. Sets all Fields to WaterFields.
     */
    public void buildBoard(int height, int width) {
        gameField = new GameField(height, width);
        borderPane.setPadding(new Insets(1, 1, 1, 1));
        borderPane.setCenter(gameField.getDisplay());
    }

    /**
     * Clickevent for GridPane (print grid-cell, which is clicked)
     * @param event
     */
    public void clickGrid(javafx.scene.input.MouseEvent event) {
        model.setSelectedShip(new Random().nextInt(3)); //for testing only
            Node clickedNode = event.getPickResult().getIntersectedNode();
            if (clickedNode != grid) {
                // click on descendant node
                Integer colIndex = GridPane.getColumnIndex(clickedNode);
                Integer rowIndex = GridPane.getRowIndex(clickedNode);
                int row = gameField.getRow();
                int col = gameField.getCol();
                System.out.println("Mouse clicked cell: " + colIndex + " And: " + rowIndex);
                Point2D clickedPoint = new Point2D(colIndex, row - rowIndex-1);
                ArrayList<Point2D> shipPoints = model.getShipPoints(new PlacementInfo(clickedPoint, model.getCurrentRotation()), model.getShipTypes().get(model.getSelectedShip()));
                if(model.proofShip(shipPoints)) {//TODO ships which doesnt contain (0,0) ???
                    if (model.getPlacedShips().containsKey(model.getSelectedShip())) {
                        gameField.removePlacedShip(model.getShipPoints(new PlacementInfo(model.getPlacedShips().get(model.getSelectedShip()).getPosition(),model.getCurrentRotation()), model.getShipTypes().get(model.getSelectedShip())));
                    }
                    gameField.placeShip(shipPoints);
                    model.addShipPlacement(model.getSelectedShip(), new PlacementInfo(clickedPoint, model.getCurrentRotation()));
                }
            }
    }

    public void setShipForm(){
        //get ShipForm from server
        //gameField = new GameField(height, width);
        ArrayList<Point2D> positions= new ArrayList<>();
        positions.add(new Point2D(0,0));
        positions.add(new Point2D(0,1));
        positions.add(new Point2D(0,2));
        positions.add(new Point2D(1,2));
        Ship s = new Ship(new ShipType(positions));
        shipForm= new ShipForm(positions);
        smallBorderPane.setPadding(new Insets(1, 1, 1, 1));
        grid = shipForm.getDisplay();
        smallBorderPane.setCenter(grid);
        System.out.println(smallBorderPane.getCenter());
    }

    public void setClientId(int clientId){
        model.setClientID(clientId);
    }

    public void showWaitForGameInit(){
        //TODO Show "Please wait for GameInit"
    }
    public void showPlaceAllShips(){
        //TODO Show "Please place all Ships first"
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
                inGame.start(inGameStage,model.getCurrentGame(), ClientType.PLAYER, model.getPlacedShips(),clientId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onGameInitNotification(GameInitNotification message, int clientId) {
        model.setClientList(message.getClientList());
    }
}