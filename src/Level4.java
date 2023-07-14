import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class Level4 extends GameScreen{

    Level4(Stage stage, ImageView background, ImageView crosshair, int backgroundNum) {
        super(stage, background, crosshair, backgroundNum, 4);
    }

    @Override
    public void setDucks() {
        ducks.add(new Duck(getImage(duckBlackPaths.get(0)), duckBlackPaths, true, -(7 * width / 16), -(3 * height/ 10)));
        ducks.add(new Duck(getImage(duckBluePaths.get(0)), duckBluePaths, true, (7 * width / 16), -(height/10)));
    }

    @Override
    public void passLevel() {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                music.stop();
                new Level5(stage, background, crosshair, backgroundNum);
            }
        });
    }
}