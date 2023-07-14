import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class Level5 extends GameScreen{

    Level5(Stage stage, ImageView background, ImageView crosshair, int backgroundNum) {
        super(stage, background, crosshair, backgroundNum, 5);
    }

    @Override
    public void setDucks() {
        ducks.add(new Duck(getImage(duckBlackPaths.get(3)), duckBlackPaths, false, -(7 * width / 16), -(3 * height/ 10)));
        ducks.add(new Duck(getImage(duckBluePaths.get(3)), duckBluePaths, false, (7 * width / 16), -(height/10)));
        ducks.add(new Duck(getImage(duckRedPaths.get(0)), duckRedPaths, true, -(7 * width / 16), -(height/10)));
    }

    @Override
    public void passLevel() {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                music.stop();
                new Level6(stage, background, crosshair, backgroundNum);
            }
        });
    }
}