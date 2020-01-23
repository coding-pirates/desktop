package de.upb.codingpirates.battleships.desktop.start;


import de.upb.codingpirates.battleships.desktop.util.Fullscreen;
import javafx.scene.control.Control;
import javafx.stage.Stage;

/**
 * Class that implements a Window to start the game
 */
public class Start extends Fullscreen {


    private Stage startStage;
    private Control StartController;

    public void display(Stage startStage) {
        this.startStage = startStage;
        initDimensions(this.startStage);
    }

    /**
     * Start Method that creates a new Window and a related Controller.
     */
   /** public void display(Stage primaryStage) throws Exception {
        this.startStage = primaryStage;
        Image background = new Image("file:Startscreen_BG.jpg");
        ImageView bg = new ImageView(background);


        Image start_btn = new Image("file:button.jpg");
        ImageView start = new ImageView(start_btn);
        Button btn_start = new Button("Start", start);

        Group root = new Group();
        root.getChildren().addAll(bg, btn_start);
        Scene scene = new Scene(root, 1920, 1080);
        startStage.setScene(scene);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StartView.fxml"));
        AnchorPane pane = loader.load();

        this.StartController = loader.getController();
        startStage.setTitle("Start");
        startStage.setResizable(false);
        startStage.setScene(new Scene(pane));
        startStage.show();
    }**/

}
