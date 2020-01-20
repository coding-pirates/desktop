package de.upb.codingpirates.battleships.desktop.placeship;

import de.upb.codingpirates.battleships.desktop.endgame.Endgame;
import de.upb.codingpirates.battleships.desktop.gamefield.GameField;
import de.upb.codingpirates.battleships.desktop.gamefield.GameFieldController;
import de.upb.codingpirates.battleships.desktop.ingame.InGameController;
import de.upb.codingpirates.battleships.desktop.ingame.InGameModel;
import de.upb.codingpirates.battleships.desktop.lobby.Lobby;
import de.upb.codingpirates.battleships.desktop.settings.Settings;
import de.upb.codingpirates.battleships.desktop.util.Help;
import de.upb.codingpirates.battleships.logic.*;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Controller Class for the PlaceShips Window.
 */
public class PlaceShipsController extends InGameController implements Initializable {
    //views
    /**
     * button to rotate the ships
     */
    @FXML
    private Button btn_rotate;
    /**
     * layout to place the gamefield
     */
    @FXML
    private BorderPane borderPane;
    /**
     * Layout for gamefield
     */
    @FXML
    private GridPane grid;
    /**
     * layout for the ship preview
     */
    @FXML
    private BorderPane smallBorderPane;

    /**
     * height of the gamefield
     */
    private int height;
    /**
     * width of the gamefield
     */
    private int width;
    /**
     * gameField class
     */
    private GameField gameField;
    /**
     * Array with x, y-coordinates and the type of this field (e.g. water)
     */
    private String[][] type;
    /**
     * Map with gamefield ID/Player ID and GamefieldController
     */
    private HashMap<Integer, GameFieldController> controllerMap = new HashMap<Integer, GameFieldController>();
    /**
     * Game logic
     */
    private Game game;
    /**
     * class for the form of the ship
     */
    private ShipForm shipForm;


    /**
     * Constructor of this class
     */
    public PlaceShipsController() {
    }


    /**
     * Initialization
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * closes the PlaceShipView
     */
    public void closeStage() {
        Stage stage = (Stage) btn_rotate.getScene().getWindow();
        stage.close();
    }

    /**
     * next_Button
     * @throws Exception
     */
    @FXML
    public void handlerButton() throws Exception {
        Endgame endgame = new Endgame();
        Stage endstage = new Stage();
        try {
            endgame.start(endstage);
            closeStage();
        } catch (IOException e) {
            e.printStackTrace();//TODO
        }
        endstage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
    }

    /**
     * starts the HelpView with accessibility tools in an extra window
     * @throws IOException
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

    /**
     * starts the SettingsView in an extra window
     * @throws Exception
     */
    @FXML
    public void settings() throws Exception {

        Settings settings = new Settings();
        Stage settingsStage = new Stage();
        try {
            settings.start();
        }
        catch (IOException e) {
            e.printStackTrace();//TODO
        }
        settingsStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });

    }

    /**
     * starts the LobbyView and closes the placeShipsView
     */
    @FXML
    public void back(){
        Lobby lobby = new Lobby();
        Stage lobbyStage = new Stage();
        try {
            lobby.start(lobbyStage);
            closeStage();
        } catch (IOException e) {
            e.printStackTrace();//TODO
        }
        lobbyStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
    }

    /**
     * rotates the ship in shipForm
     */
    @FXML
    public void rotate(){
        shipForm.rotate();
    }

    /**
     * starts the inGameView and closes the placeshipView
     */
    @FXML
    public void gamestart(){
        //TODO
        InGameModel inGameModel = new InGameModel(game);
        Stage inGameStage = new Stage();
        try {
            inGameModel.start2(inGameStage);
            closeStage();
        } catch (Exception e) {
            e.printStackTrace();
            inGameStage.setOnCloseRequest((t -> {
                Platform.exit();
                System.exit(0);
            }));
        };
    }

    /**
     * Adds a new GameField
     *
     * @param clientList Collection of all Players.
     * @throws Exception
     */
    public void fieldInit(Collection<Client> clientList) throws Exception {
        buildBoard(20,20);



    }

    /**
     * Builds the GameField. Sets all Fields to WaterFields.
     */
    public void buildBoard(int height, int width) {
        this.height = height;
        this.width = width;
        gameField = new GameField(height, width);
        borderPane.setPadding(new Insets(1, 1, 1, 1));
        borderPane.setCenter(gameField.getDisplay());

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                type = new String[height][width];
                type[i][j] = "water";
            }}


    }

    /**
     * Clickevent for GridPane (print grid-cell, which is clicked)
     * @param event
     */
    public void clickGrid(javafx.scene.input.MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if (clickedNode != grid) {
            // click on descendant node
            Integer colIndex = GridPane.getColumnIndex(clickedNode);
            Integer rowIndex = GridPane.getRowIndex(clickedNode);
            int row = gameField.getRow();
            int col = gameField.getCol();
            System.out.println("Mouse clicked cell: " + colIndex + " And: " + rowIndex);
            gameField.shipPlaced(new Point2D(colIndex, row - rowIndex-1));
            //placeShips aufrufen für Aktualisierung der Map

        }
    }

    /**
     * sets the shipForm and visualize it
     */
    public void setShipForm(){
        //get ShipForm from server
        this.height = height;
        this.width = width;
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
    }

