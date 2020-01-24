package de.upb.codingpirates.battleships.desktop.placeship;

import de.upb.codingpirates.battleships.client.ListenerHandler;
import de.upb.codingpirates.battleships.client.listener.*;
import de.upb.codingpirates.battleships.desktop.gamefield.GameField;
import de.upb.codingpirates.battleships.desktop.ingame.InGame;
import de.upb.codingpirates.battleships.desktop.ingame.InGameController;
import de.upb.codingpirates.battleships.desktop.lobby.Lobby;
import de.upb.codingpirates.battleships.desktop.settings.Settings;
import de.upb.codingpirates.battleships.desktop.util.Help;
import de.upb.codingpirates.battleships.logic.*;
import de.upb.codingpirates.battleships.network.message.notification.GameInitNotification;
import de.upb.codingpirates.battleships.network.message.notification.GameStartNotification;
import de.upb.codingpirates.battleships.network.message.response.GameLeaveResponse;
import de.upb.codingpirates.battleships.network.message.response.PlaceShipsResponse;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller Class for the PlaceShips Window.
 */
public class PlaceShipsController extends InGameController implements Initializable, PlaceShipsResponseListener, GameStartNotificationListener, GameInitNotificationListener, GameLeaveResponseListener {

    @FXML
    private Button btn_rotate;

    @FXML
    private Button btnShipNext;

    @FXML
    private Button btnShipBack;

    @FXML
    private Button btn_gameStart;

    @FXML
    private BorderPane borderPane;
    @FXML
    private GridPane selectedShipGrid;
    @FXML
    private BorderPane smallBorderPane;

    @FXML
    private PlaceshipsModel model;
    private GameField gameField;

    private ShipForm shipForm;



    public PlaceShipsController() {
        ListenerHandler.registerListener((MessageHandlerListener) this);
        this.model = new PlaceshipsModel();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setCurrentGame(Game currentGame){
       model.setCurrentGame(currentGame);
        model.addShipTypes(currentGame.getConfig().getShips()); //TODO maybe direct in Model
        model.setSelectedShip(currentGame.getConfig().getShips().keySet().iterator().next());
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
        /*settingsStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });*/

    }

    @FXML
    public void back(){
        System.out.println("Leave Request gesendet!");
        model.sendLeaveRequest();
    }

    @Override
    public void onGameLeaveResponse(GameLeaveResponse message, int clientId){
        System.out.println("GameLeaveResponse bekommen!");
        Platform.runLater(() ->{
            Lobby lobby = new Lobby();
            Stage lobbyStage = new Stage();

            lobbyStage.setOnCloseRequest(y -> {
                Platform.exit();
                System.exit(0);
            });

            try {
                lobby.display(lobbyStage,clientId);
                closeStage();
            } catch (IOException e) {
                e.printStackTrace();//TODO
            }
        });

    }

    @FXML
    public void rotate(){
        switch (model.getCurrentRotation()){
            case NONE:
                model.setCurrentRotation(Rotation.CLOCKWISE_90);
                break;
            case CLOCKWISE_90:
                model.setCurrentRotation(Rotation.CLOCKWISE_180);
                break;
            case CLOCKWISE_180:
                model.setCurrentRotation(Rotation.COUNTERCLOCKWISE_90);
                break;
            case COUNTERCLOCKWISE_90:
                model.setCurrentRotation(Rotation.NONE);
                break;
        }
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
            Node clickedNode = event.getPickResult().getIntersectedNode();
        Integer colIndex = GridPane.getColumnIndex(clickedNode);
        Integer rowIndex = GridPane.getRowIndex(clickedNode);
            if (clickedNode != selectedShipGrid && colIndex != null && rowIndex != null) {
                int row = gameField.getRow();
                int col = gameField.getCol();
                System.out.println("Mouse clicked cell: " + colIndex + " And: " + rowIndex);
                Point2D clickedPoint = new Point2D(colIndex, row - rowIndex-1);
                ArrayList<Point2D> shipPoints = model.getShipPoints(new PlacementInfo(clickedPoint, model.getCurrentRotation()), model.getShipTypes().get(model.getSelectedShip()));
                if(model.proofShip(shipPoints)) {//TODO ships which doesnt contain (0,0) ???
                    if (model.getPlacedShips().containsKey(model.getSelectedShip())) {
                        gameField.removePlacedShip(model.getShipPoints(model.getPlacedShips().get(model.getSelectedShip()), model.getShipTypes().get(model.getSelectedShip())));
                    }
                    gameField.placeShip(shipPoints);
                    model.addShipPlacement(model.getSelectedShip(), new PlacementInfo(clickedPoint, model.getCurrentRotation()));
                    model.setSelectedShip((model.getSelectedShip()+1)%(model.getCurrentGame().getConfig().getShips().size()));
                    setShipForm();
                }
            }
    }

    public void setShipForm(){
        //get ShipForm from server
        //gameField = new GameField(height, width);
       /* ArrayList<Point2D> positions= new ArrayList<>();
        positions.add(new Point2D(0,0));
        positions.add(new Point2D(0,1));
        positions.add(new Point2D(0,2));
        positions.add(new Point2D(1,2));
        Ship s = new Ship(new ShipType(positions));*/
        shipForm= new ShipForm(model.getShipTypes().get(model.getSelectedShip()).getPositions());
        smallBorderPane.setPadding(new Insets(1, 1, 1, 1));
        selectedShipGrid = shipForm.getDisplay();
        smallBorderPane.setCenter(selectedShipGrid);
        System.out.println(smallBorderPane.getCenter());
    }

    public void setClientId(int clientId){
        model.setClientID(clientId);
    }

    public void showWaitForGameInit(){
        //TODO Show "Please wait for GameInit"
    }
    public void showPlaceAllShips(){
        Alert alertFinish = new Alert(Alert.AlertType.INFORMATION,
                "Platziere bitte zuerst alle Schiffe.", ButtonType.OK);
        alertFinish.showAndWait();
    }

    @Override
    public void onPlaceShipsResponse(PlaceShipsResponse message, int clientId) {
        Platform.runLater(()->{
        Alert alertFinish = new Alert(Alert.AlertType.INFORMATION,
                "Die Schiffe wurden erfolgreich platziert. Warte auf andere Spieler..", ButtonType.OK);
        alertFinish.showAndWait();
        });
    }

    @Override
    public void onGameStartNotification(GameStartNotification message, int clientId) {
        Platform.runLater(()->{
            InGame inGame = new InGame();
            Stage inGameStage = new Stage();

            inGameStage.setOnCloseRequest(t->{
                Platform.exit();
                System.exit(0);
            });

            try {
                inGame.start(inGameStage,model.getCurrentGame(), ClientType.PLAYER, model.getPlacedShips(),clientId);
                closeStage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onGameInitNotification(GameInitNotification message, int clientId) {
        btn_gameStart.setVisible(true);
        model.setClientList(message.getClientList());
    }

    @FXML
    public void onNextShipClicked(){
        model.setSelectedShip((model.getSelectedShip()+1)%(model.getCurrentGame().getConfig().getShips().size()));
        model.setCurrentRotation(Rotation.NONE);
        setShipForm();
    }

    @FXML
    public void onBackShipClicked(){
        model.setSelectedShip((model.getSelectedShip()-1)%(model.getCurrentGame().getConfig().getShips().size()));
        model.setCurrentRotation(Rotation.NONE);
        setShipForm();
    }

}