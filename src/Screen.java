import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Abstract class that represents a screen arrangement in a Duck Hunt game.
 * It provides methods for creating and managing the game screen, including the background,
 * crosshair, ducks, text, and music.
 */
public abstract class Screen {
    /**
     * Defines the necessary properties.
     */
    Stage stage;
    Scene scene;
    MediaPlayer music;
    ImageView background;
    ImageView crosshair;
    ArrayList<Duck> ducks = new ArrayList<>();
    ArrayList<String> backgroundsPaths = getPathName("background");
    ArrayList<String> crosshairPaths = getPathName("crosshair");
    int backgroundNum;
    int crosshairNum;
    double width;
    double height;
    double scale = DuckHunt.scale;
    double volume = DuckHunt.volume;

    /**
     * Creates a Text object with the specified text, position, and size.
     *
     * @param writing the text to be displayed
     * @param x       the x-coordinate of the text position
     * @param y       the y-coordinate of the text position
     * @param size    the font size of the text
     * @return the created Text object
     */
    public Text getText(String writing, double x, double y, int size){
        Font font = Font.font("Arial", FontWeight.BOLD, size * scale);
        Text text = new Text();

        text.setText(writing);
        text.setFont(font);
        text.setTranslateX(x);
        text.setTranslateY(y);
        text.setFill(Color.ORANGE);
        text.setTextAlignment(TextAlignment.CENTER);
        return text;
    }

    /**
     * Retrieves the file paths of images in the specified folder and adds them to the provided list.
     *
     * @param folder the name of the folder containing the images
     */
    public ArrayList<String> getPathName(String folder) {
        ArrayList<String> list = new ArrayList<String>();
        File[] files = new File("assets/" + folder).listFiles();

        for (File image : Objects.requireNonNull(files)) {
            Path filePath = Paths.get(image.getAbsolutePath());
            Path workingDirectory = Paths.get("").toAbsolutePath();
            list.add(workingDirectory.relativize(filePath).toString());
        }
        return list;
    }

    /**
     * Performs a flashing animation on the given Text object.
     *
     * @param text the Text object to be animated
     */
    public void flashing(Text text){
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1), text);
        scaleTransition.setFromY(1);
        scaleTransition.setToY(0);

        PauseTransition showTransition = new PauseTransition(Duration.seconds(1));
        showTransition.setOnFinished(event -> {
            text.setVisible(false);

            PauseTransition hideTransition = new PauseTransition(Duration.seconds(1));
            hideTransition.setOnFinished(event1 -> {
                text.setVisible(true);
                showTransition.play();
            });

            hideTransition.play();
        });
        showTransition.play();
    }

    /**
     * Creates an ImageView object with the specified image path.
     *
     * @param imagePath the path of the image file
     * @return the created ImageView object
     */
    public ImageView getImage(String imagePath){
        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setScaleX(scale);
        imageView.setScaleY(scale);
        return imageView;
    }

    /**
     * Sets the stage with the configured scene and title.
     */
    public void setStage(){
        stage.setScene(scene);
        stage.setTitle("HUBBM Duck Hunt");
        stage.show();
    }

    /**
     * Creates a scene with the provided nodes and sets it as the current scene.
     *
     * @param nodes the nodes to be added to the scene
     * @return the created Scene object
     */
    public Scene setScene(Node[] nodes){
        StackPane stackPane = new StackPane();

        stackPane.getChildren().add(background);
        for (Duck duck : ducks) stackPane.getChildren().add(duck.image);
        for (Node node : nodes) stackPane.getChildren().addAll(node);

        return new Scene(stackPane, width, height);
    }

    /**
     * Sets the music for the screen.
     *
     * @param path the path of the music file
     * @param time the number of times the audio should be played in a loop
     * @return the created MediaPlayer object
     */
    public MediaPlayer setMusic(String path, int time){
            Media media = new Media(new File(path).toURI().toString());
            MediaPlayer music = new MediaPlayer(media);
            music.setVolume(volume);
            music.setCycleCount(time);
            music.play();
            return music;
    }

    /**
     * Sets the width and height of the screen based on the dimensions of the background image.
     */
    public void setSizes(){
        width = background.getBoundsInLocal().getWidth() * scale;
        height = background.getBoundsInLocal().getHeight() * scale;
    }
}