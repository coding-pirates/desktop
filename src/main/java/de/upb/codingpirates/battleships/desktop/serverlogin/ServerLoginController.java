package de.upb.codingpirates.battleships.desktop.serverlogin;

import de.upb.codingpirates.battleships.client.ListenerHandler;
import de.upb.codingpirates.battleships.client.listener.ServerJoinResponseListener;
import de.upb.codingpirates.battleships.desktop.BattleshipsDesktopClientApplication;
import de.upb.codingpirates.battleships.desktop.lobby.Lobby;
import de.upb.codingpirates.battleships.desktop.settings.Settings;
import de.upb.codingpirates.battleships.desktop.util.Help;
import de.upb.codingpirates.battleships.logic.ClientType;
import de.upb.codingpirates.battleships.network.message.response.LobbyResponse;
import de.upb.codingpirates.battleships.network.message.response.ServerJoinResponse;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller Class for the ServerLogin Window.
 */
public class ServerLoginController implements Initializable, ServerJoinResponseListener {

    private BattleshipsDesktopClientApplication main;

    private ResourceBundle resources;

    //views
    /**
     * field to write in the IP_Adress of the server
     */
    @FXML
    private TextField ipField;
    /**
     * field to write in the port
     */
    @FXML
    private TextField portField;
    /**
     * field to write in the players' username
     */
    @FXML
    private TextField nameField;
    /**
     * field to show warnings
     */
    @FXML
    private Label lblStatus;
    @FXML
    private ProgressIndicator login_progress;
    @FXML
    private ImageView login_background_imageView;
    @FXML
    private ImageView btn_login_imageview;
    @FXML
    private Button btn_login;
    @FXML
    private Button btn_settings;
    @FXML
    private Button btn_help;

    private boolean listen;

    /**
     * StringProperty
     */
    private StringPropertyBase text = new SimpleStringProperty();

    /**
     * Constructor of this class to register the Listener
     */
    public ServerLoginController() {
        ListenerHandler.registerListener(this);
        listen = true;
    }

    @Override
    public boolean invalidated() {
        return !listen;
    }
    /**
     * Set Method for Main.
     *
     * @param main Related SpectatorApp
     */
    public void setMain(BattleshipsDesktopClientApplication main) {
        this.main = main;
    }

    /**
     * Initial Method.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
        text.addListener(listener ->lblStatus.setText(text.get()));

        //hide the progressbar
        login_progress.setVisible(false);

        double displayWidth = Screen.getPrimary().getBounds().getWidth();
        double displayHeight = Screen.getPrimary().getBounds().getHeight();

        //used for scaling the background image
        login_background_imageView.setFitHeight(displayHeight);
        login_background_imageView.setFitWidth(displayWidth);

        // forces the portField to be numeric only
        portField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    portField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        //image button stettings
        double btnXSize = displayWidth * 53/1920;
        double btnYSize = displayHeight * 53/1080;

        btn_settings.setBackground(new Background(new BackgroundImage(new Image(getClass().getResource("/images/icon_settings.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(btnXSize,btnYSize,true,true,true,true))));
        btn_settings.setPrefSize(btnXSize, btnYSize);
        btn_settings.setOnAction((event)-> settings());
        btn_settings.setOnMouseEntered(this::changeCursor);


        btn_settings.setLayoutX(displayWidth * 0.93 - btnXSize / 2);
        btn_settings.setLayoutY(displayHeight * 0.13 - btnYSize / 2);

        //image button help
        btn_help.setBackground(new Background(new BackgroundImage(new Image(getClass().getResource("/images/icon_help.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(btnXSize,btnYSize,true,true,true,true))));
        btn_help.setPrefSize(btnXSize, btnYSize);
        btn_help.setOnAction((event)-> help());
        btn_help.setOnMouseEntered(this::changeCursor);


        btn_help.setLayoutX(displayWidth * 0.89 - btnXSize / 2);
        btn_help.setLayoutY(displayHeight * 0.13 - btnYSize / 2);
    }

    /**
     * Creates a new TCPClient and ServerLoginModel with the Values of the TextFields.
     * Creates a Lobby if the Request was successful.
     *
     * @param event Button Pressed Event
     * @throws Exception when the server is not available this exception will be thrown
     */
    @FXML
    public void login(ActionEvent event) throws Exception {
        String serverIP = ipField.getText();
        String port = portField.getText();
        login_progress.setVisible(true);
        lblStatus.setText("");

        int flag = 0;
        if(nameField.getText().isEmpty())
            flag +=1;
        if(ipField.getText().isEmpty())
            flag +=2;
        if(portField.getText().isEmpty())
            flag += 4;

        if(flag != 0) {
            String string = "";
            switch (flag){
                case 1:
                    string = resources.getString("serverLogin.lblStatus.noName");
                    break;
                case 2:
                    string = resources.getString("serverLogin.lblStatus.noIp");
                    break;
                case 3:
                    string = resources.getString("serverLogin.lblStatus.noNameIp");
                    break;
                case 4:
                    string = resources.getString("serverLogin.lblStatus.noPort");
                    break;
                case 5:
                    string = resources.getString("serverLogin.lblStatus.noNamePort");
                    break;
                case 6:
                    string = resources.getString("serverLogin.lblStatus.noIpPort");
                    break;
                case 7:
                    string = resources.getString("serverLogin.lblStatus.noNameIpPort");
                    break;
            }
            lblStatus.setAlignment(Pos.CENTER_LEFT);
            lblStatus.setText(string);
            login_progress.setVisible(false);
            return;
        }

        BattleshipsDesktopClientApplication
                .getInstance()
                .getTcpConnector()
                .connect(serverIP, Integer.parseInt(port),() -> Platform.runLater(()->{
                    lblStatus.setAlignment(Pos.CENTER);
                    lblStatus.setText(resources.getString("serverLogin.lblStatus.noBattle"));
                    login_progress.setVisible(false);
                }),() -> Platform.runLater(()->lblStatus.setText("Anmeldung fehlgeschlagen: Server nicht erreichbar!")));
    }

    private void setLblStatus(String lblStatus) {
        text.set(lblStatus);
    }

    /**
     * closes the ServerLoginView
     */
    public void closeStage(){
        Stage stage = (Stage) lblStatus.getScene().getWindow();
        stage.close();
    }

    @Override
    public void onServerJoinResponse(ServerJoinResponse response, int clientId) {
        listen = false;
        Platform.runLater(() -> {
            setLblStatus("");

            Lobby lobby = new Lobby();
            Stage lobbyStage = new Stage();

            lobbyStage.setOnCloseRequest(t -> {
                Platform.exit();
                System.exit(0);
            });

            try {
                lobby.display(lobbyStage,response.getClientId());
                closeStage();
            } catch (IOException e) {
                e.printStackTrace();//TODO
            }
        });
    }
        @FXML
        public void settings() {

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
    public void help() {
        Help help = new Help();
        try{
            help.display(resources.getString("serverLogin.help.title"));
        }
        catch (IOException e){
            e.printStackTrace();
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
