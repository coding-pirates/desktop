package de.upb.codingpirates.battleships.desktop.endgame;

import de.upb.codingpirates.battleships.desktop.lobby.Lobby;
import de.upb.codingpirates.battleships.desktop.ranking.Ranking;
import de.upb.codingpirates.battleships.desktop.settings.Settings;
import de.upb.codingpirates.battleships.desktop.util.Help;
import de.upb.codingpirates.battleships.logic.Client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Controller Class for the Endgame Window.
 */
public class EndgameController implements Initializable {

    @FXML
    Button btn_settings;
    @FXML
    Button btn_help;
    @FXML
    ImageView background;
    @FXML
    Button btn_lobby;
    @FXML
    Label first;
    @FXML
    Label second;
    @FXML
    Label third;
    private Map<Integer, Integer> points = null;
    private Collection<Client> players = null;


    private int clientID;

    /**
     * Initial Method.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        double displayWidth = Screen.getPrimary().getBounds().getWidth();
        double displayHeight = Screen.getPrimary().getBounds().getHeight();

        background.setFitWidth(displayWidth);
        background.setFitHeight(displayHeight);

        //image button stettings
        double btn_startXSize = displayWidth * 53/1920;
        double btn_startYSize = displayHeight * 53/1080;

        btn_settings.setBackground(new Background(new BackgroundImage(new Image(getClass().getResource("/images/icon_settings.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(btn_startXSize,btn_startYSize,true,true,true,true))));
        btn_settings.setPrefSize(btn_startXSize, btn_startYSize);
        btn_settings.setOnAction((event)-> settings());
        btn_settings.setOnMouseEntered(this::changeCursor);

        btn_settings.setLayoutX(displayWidth * 0.93 - btn_startXSize / 2);
        btn_settings.setLayoutY(displayHeight * 0.13 - btn_startYSize / 2);

        btn_help.setBackground(new Background(new BackgroundImage(new Image(getClass().getResource("/images/icon_help.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(btn_startXSize,btn_startYSize,true,true,true,true))));
        btn_help.setPrefSize(btn_startXSize, btn_startYSize);
        btn_help.setOnAction((event)-> help());
        btn_help.setOnMouseEntered(this::changeCursor);

        btn_help.setLayoutX(displayWidth * 0.89 - btn_startXSize / 2);
        btn_help.setLayoutY(displayHeight * 0.13 - btn_startYSize / 2);
    }

    public void setPoints(Map<Integer, Integer> points) {
        this.points = points;
    }

    public void setPlayers(Collection<Client> players) {
        this.players = players;
    }

    /**
     * starts the HelpView with accessibility tools in an extra window
     * @throws IOException
     */
    @FXML
    public void help() {
        Help help = new Help();
        try{
            help.display("Endgame-Help");
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
    public void settings(){

        Settings settings = new Settings();
        Stage settingsStage = new Stage();
        try {
            settings.display(settingsStage);
        } catch (IOException e) {
            e.printStackTrace();//TODO
        }
        /*settingsStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });*/
    }

    /**
     * shows the ranking in an extra window
     * @throws Exception
     */
    @FXML
    public void ranking() throws Exception {

        Ranking ranking = new Ranking();
        Stage rankingStage = new Stage();
        try {
            ranking.display(rankingStage);

        } catch (IOException e) {
            e.printStackTrace();//TODO
        }
        if(points!= null) {
            ranking.setPlayer(players);
            ranking.sortPoints(points);
        }

    }

    /**
     * starts the lobbyView and closes this endgameView
     * @throws Exception
     */
    @FXML
    public void lobby() {
        Lobby lobby = new Lobby();
        Stage lobbyStage = new Stage();

        lobbyStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
        try {
            lobby.display(lobbyStage, clientID);
            closeStage();
        } catch (IOException e) {
            e.printStackTrace();//TODO
        }

    }

    /**
     * closes the endgameView
     */
    public void closeStage(){
        Stage stage = (Stage) btn_lobby.getScene().getWindow();
        stage.close();
    }

    public void setClientID(int clientID){
        this.clientID = clientID;
    }


    public void placement() {

        int firstPoints = -3000;
        int secondPoints = -3000;
        int thirdPoints = -3000;

        int firstID = -1;
        int secondId = -1;
        int thirdId = -1;

        if (points != null) {
            for (Integer player : points.keySet()) {
                if(points.get(player)> firstPoints){
                    thirdPoints = secondPoints;
                    secondPoints = firstPoints;
                    firstPoints = points.get(player);

                    thirdId = secondId;
                    secondId = firstID;
                    firstID = player;
                }
                else if (points.get(player) > secondPoints){
                    thirdPoints = secondPoints;
                    secondPoints = points.get(player);

                    thirdId = secondId;
                    secondId = player;
                }
                else if (players.size()> 2 && points.get(player) > thirdPoints){
                    thirdPoints = points.get(player);

                    thirdId = player;
                }

            }

            Client cfirst = null;
            Client csecond = null;
            Client cthird = null;



            for(Client client : players){
                if(client.getId()==firstID){
                    cfirst = client;
                    System.out.println(cfirst);
                }
                else if(client.getId()==secondId){
                    csecond = client;
                    System.out.println(csecond
                    );
                }
                else if(players.size() > 2){
                    if(client.getId()==thirdId){
                        cthird = client;
                    }
                }

            }

            try {
                first.setText(cfirst.getName());
                second.setText(csecond.getName());
                if(players.size()>2)
                third.setText(cthird.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    /**
     * changes the cursor for a button hover to Cursor.HAND
     * @param event event which calls the method
     */
    public void changeCursor(javafx.scene.input.MouseEvent event) {
        Button currentButton = (Button) event.getSource();
        currentButton.setCursor(Cursor.HAND);
    }
}
