import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class Level2 extends GameScreen{
    Level2(Stage stage, ImageView background, ImageView crosshair, int backgroundNum) {
        super(stage, background, crosshair,  backgroundNum, 2);
    }

    @Override
    public void setDucks() {
        ducks.add(new Duck(getImage(duckBluePaths.get(0)), duckBluePaths, true, (7 * width / 16), (3 * height/ 10)));
    }

    @Override
    public void passLevel() {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                music.stop();
                new Level3(stage, background, crosshair, backgroundNum);
            }
        });
    }
}