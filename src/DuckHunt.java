import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * @author Gazi Kağan Soysal - 2210356050
 * @version 1.0
 */
public class DuckHunt extends Application{
    static final double scale = 3;
    static final double volume = 0.025;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.getIcons().add(new Image("assets/favicon/1.png"));
        new TitleScreen(stage);
    }
}