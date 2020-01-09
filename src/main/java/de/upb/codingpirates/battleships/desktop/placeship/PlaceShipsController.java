package de.upb.codingpirates.battleships.desktop.placeship;

import de.upb.codingpirates.battleships.desktop.endgame.Endgame;
import de.upb.codingpirates.battleships.desktop.gamefield.GameFieldController;
import de.upb.codingpirates.battleships.desktop.ingame.InGameController;
import de.upb.codingpirates.battleships.desktop.lobby.Lobby;
import de.upb.codingpirates.battleships.desktop.settings.Settings;
import de.upb.codingpirates.battleships.desktop.util.Help;
import de.upb.codingpirates.battleships.logic.Client;
import de.upb.codingpirates.battleships.logic.Game;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Controller Class for the PlaceShips Window.
 */
public class PlaceShipsController extends InGameController implements Initializable {

    @FXML
    private Button btn_rotate;
    @FXML
    private AnchorPane spielfelder;
    @FXML
    private BorderPane borderPane;
    private Game game;
    private HashMap<Integer, GameFieldController> controllerMap = new HashMap<Integer, GameFieldController>();
    private HashMap<Integer, Node> fieldMap = new HashMap<Integer, Node>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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

    @FXML
    public void help() throws IOException {
        Help help = new Help();
        try{
            help.display("PlaceShip-Help", "PlaceShip-Help");
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

    @FXML
    public void rotate(){
        //TODO
    }

    @FXML
    public void gamestart(){
        //TODO
        Stage inGameStage = new Stage();
        try {
            inGameStage.show();
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
     * Adds a new GameField and related Controller for every Player.
     *
     * @param clientList Collection of all Players.
     * @throws Exception
     */
    public void fieldInit(Collection<Client> clientList) throws Exception {
            Client client = new Client(1, "self");
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/GameFieldView.fxml"));
            Node inGame = loader.load();
            spielfelder.getChildren().add(inGame);
            GameFieldController gameFieldController = loader.getController();
            gameFieldController.setParent(this);
            gameFieldController.setConfig(client.getName(), game, inGame);
            gameFieldController.enlargeBoard();
            gameFieldController.buildBoard(5,5);
            //controllerMap.put(client.getId(), gameFieldController);                //Create a Map of PlayerId and Controller Object
            //fieldMap.put(client.getId(), inGame);

    }


}