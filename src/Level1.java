import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class Level1 extends GameScreen{
    Level1(Stage stage, ImageView background, ImageView crosshair, int backgroundNum) {
        super(stage, background, crosshair, backgroundNum, 1);
    }

    @Override
    public void setDucks(){
       ducks.add(new Duck(getImage(duckBlackPaths.get(3)), duckBlackPaths, false, -(7 * width / 16), -(3 * height/ 10)));
    }

    @Override
    public void passLevel() {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) music.stop();
                new Level2(stage, background, crosshair, backgroundNum);
        });
    }
}