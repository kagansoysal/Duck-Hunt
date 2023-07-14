import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TitleScreen extends Screen {
    /**
     * The screen that will open when the game is started for the first time is opened with this constructor.
     * @param stage the Stage object that contains the scene of the start screen.
     */
    TitleScreen(Stage stage){
        setScreen(stage, setMusic("assets/effects/Title.mp3", -1));
    }

    /**
     * This constructor is used to ensure that the music is not interrupted when this screen is displayed by pressing ESC from the choice screen.
     * @param stage the Stage object that contains the scene of the start screen.
     * @param music used with choice screen
     */
    TitleScreen(Stage stage, MediaPlayer music) {
        setScreen(stage, music);
    }

    private void setScreen(Stage stage, MediaPlayer music){
        this.stage = stage;
        this.music = music;
        background = getImage("assets/welcome/1.png");

        setSizes();

        Text text = getText("PRESS ENTER TO START\nPRESS ESC TO EXIT", 0, height/5, 17);
        flashing(text);

        scene = setScene(new Node[]{text});
        setStage();

        gamerCommands();
    }

    /**
     Sets up the gamer commands for the scene.
     The method listens for key presses and performs specific actions based on the key code.
     Pressing the ENTER key opens a new ChoiceScreen with the provided stage and music.
     Pressing the ESCAPE key closes the stage.
     */
    private void gamerCommands(){
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                new ChoiceScreen(stage, music);
            }
            else if (event.getCode() == KeyCode.ESCAPE) {
                stage.close();
            }
        });
    }
}