import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ChoiceScreen extends Screen {

    ChoiceScreen(Stage stage, MediaPlayer music) {
        setScreen(stage, music);
    }

    private void setScreen(Stage stage, MediaPlayer music){
        this.stage = stage;
        this.music = music;

        background = getImage(backgroundsPaths.get(backgroundNum));
        crosshair = getImage(crosshairPaths.get(crosshairNum));

        setSizes();

        Text text = getText("USE ARROW KEYS TO NAVIGATE\nPRESS ENTER TO START\nPRESS ESC TO EXIT", 0, -5 * height / 14, 10);

        scene = setScene(new Node[]{crosshair, text});
        setStage();

        gamerCommands();
    }

    /**
     * Defines the key commands for the gamer in the game.
     * The commands include changing backgrounds and crosshairs, going back to the title screen, and starting a new level.
     * These commands are triggered by specific key presses.
     */
    private void gamerCommands() {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.RIGHT) {
                backgroundNum = (backgroundNum + 1) % backgroundsPaths.size();
            } else if (event.getCode() == KeyCode.LEFT) {
                backgroundNum = (backgroundNum - 1 + backgroundsPaths.size()) % backgroundsPaths.size();
            } else if (event.getCode() == KeyCode.UP) {
                crosshairNum = (crosshairNum + 1) % crosshairPaths.size();
            } else if (event.getCode() == KeyCode.DOWN) {
                crosshairNum = (crosshairNum - 1 + crosshairPaths.size()) % crosshairPaths.size();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                new TitleScreen(stage, music);
            } else if (event.getCode() == KeyCode.ENTER) {
                music.stop();
                music = setMusic("assets/effects/Intro.mp3", 1);
                music.setOnEndOfMedia(() -> new Level1(stage, background, crosshair, backgroundNum));
            }
            background.setImage(new Image(backgroundsPaths.get(backgroundNum)));
            crosshair.setImage(new Image(crosshairPaths.get(crosshairNum)));
        });
    }
}