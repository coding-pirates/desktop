package de.upb.codingpirates.battleships.desktop.lobby;

import de.upb.codingpirates.battleships.client.ListenerHandler;
import de.upb.codingpirates.battleships.client.listener.LobbyResponseListener;
import de.upb.codingpirates.battleships.desktop.settings.Settings;
import de.upb.codingpirates.battleships.desktop.util.ClientType;
import de.upb.codingpirates.battleships.desktop.util.GameView;
import de.upb.codingpirates.battleships.desktop.util.Help;
import de.upb.codingpirates.battleships.logic.Game;
import de.upb.codingpirates.battleships.network.message.response.LobbyResponse;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

/**
 * Controller Class for the Lobby Window.
 */
public class LobbyController implements Initializable , LobbyResponseListener {


    /**
     * Lobby class to start this View
     */
    public Lobby lobby;

    //view
    /**
     * list with games, which are starting
     */
    @FXML
    private ListView<GameView> lstvwStart;
    /**
     * list with games, which are running
     */
    @FXML
    private ListView<GameView> lstvwRunning;
    /**
     * list with games, which are finished
     */
    @FXML
    private ListView<GameView> lstvwEnd;
    /**
     * button to load all games
     */
    @FXML
    private Button refreshButton;

    /**
     * Array with all games, which are starting
     */
    private ArrayList<GameView> startList = null;
    /**
     * Array with all games, which are running
     */
    private ArrayList<GameView> runningList = null;
    /**
     * Array with all games, which are finished
     */
    private ArrayList<GameView> endList = null;

    /**
     * Constructor of this class to register the Listener
     */
    public LobbyController() {

        ListenerHandler.registerListener(this);
    }

    /**
     * Set Method for the related Lobby.
     *
     * @param lobby Related Lobby
     */
    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    /**
     * Requests the GameList from Server and Shows the Games in the Lobby Window.
     */
    public void showgames() {
        // Request GameList from Server
        try {
            LobbyModel lm = new LobbyModel();
            lm.sendRequest();
        } catch (Exception e) {
            System.out.println("Fehler: " + e);
        }

        this.startList = new ArrayList<>();
        this.runningList = new ArrayList<>();
        this.endList = new ArrayList<>();

        //Show GameList

        ObservableList<GameView> startGames = FXCollections.observableArrayList(startList);
        lstvwStart.setItems(startGames);

        ObservableList<GameView> runningGames = FXCollections.observableArrayList(runningList);
        lstvwRunning.setItems(runningGames);

        ObservableList<GameView> endGames = FXCollections.observableArrayList(endList);
        lstvwEnd.setItems(endGames);

        ChangeListener<GameView> changeListener = (arg0, arg1, arg2) -> {
            ClientType client = new ClientType();
            //InGameModel inGameModel = new InGameModel(arg2.getContent());
            //Stage inGameStage = new Stage();
            try {
                client.display();
                //inGameModel.start(inGameStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        SelectionModel<GameView> smStart = lstvwStart.getSelectionModel();
        smStart.selectedItemProperty().addListener(changeListener);

        SelectionModel<GameView> smRunning = lstvwRunning.getSelectionModel();
        smRunning.selectedItemProperty().addListener(changeListener);

        SelectionModel<GameView> smEnd = lstvwEnd.getSelectionModel();
        smEnd.selectedItemProperty().addListener(changeListener);
    }

    /**
     * Adds the GameView for every Game in the right Category.
     *
     * @param games Collection of Games
     */
    public void parseToGameView(Collection<Game> games) {
        for (Game game : games) {
            final GameView view = new GameView(game);

            switch (game.getState()) {
                case LOBBY:
                    startList.add(view);
                    break;
                case IN_PROGRESS:
                case PAUSED:
                    runningList.add(view);
                    break;
                case FINISHED:
                    endList.add(view);
            }
        }
    }

    /**
     * Initial Method.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }

    @Override
    public void onLobbyResponse(LobbyResponse message, int clientId) {
        parseToGameView(message.getGames());
    }

    /**
     * closes this stage
     */
    public void closeStage() {
        Stage stage = (Stage) refreshButton.getScene().getWindow();
        stage.close();
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
        } catch (IOException e) {
            e.printStackTrace();//TODO
        }
        settingsStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
    }

    /**
     * nextButton - only to create the GUI
     * @throws IOException
     */
    @FXML
    public void handlerButton() throws IOException {
        try {
            ClientType cType = new ClientType();
            cType.display();
            closeStage();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * starts the HelpView with accessibility tools in an extra window
     * @throws IOException
     */
    @FXML
    public void help() throws IOException {
        Help help = new Help();
        try{
            help.display("Lobby-Help");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


}