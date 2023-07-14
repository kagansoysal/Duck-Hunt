import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;

/**
 * The abstract class representing a level screens.
 * This class provides common functionalities and properties for levels.
 */
public abstract class GameScreen extends Screen {
    /**
     * Defines the necessary properties.
     */
    int ammo;
    int level;
    int backgroundNum;
    boolean finishLevel;
    boolean rightShifting;
    boolean leftShifting;
    ImageView crosshair;
    ImageView foreground;
    ImageView leftBackground;
    ImageView rightBackground;
    Text levelText;
    Text ammoText;
    Text infoText;
    Text commandText;
    ArrayList<String> foregroundsPaths = getPathName("foreground");
    ArrayList<String> duckBlackPaths = getPathName("duck_black");
    ArrayList<String> duckRedPaths = getPathName("duck_red");
    ArrayList<String> duckBluePaths = getPathName("duck_blue");

    GameScreen(Stage stage, ImageView background, ImageView crosshair, int backgroundNum, int level) {
        this.stage = stage;
        this.background = background;
        this.crosshair = crosshair;
        this.level = level;
        this.backgroundNum = backgroundNum;
        this.background.setTranslateX(0);

        setScreen();
        playDucks();
        mouseMove();
        mouseClick();
    }

    /**
     * Sets the ducks for the game screen.
     * This method should be implemented by subclasses(levels) to set up the ducks.
     */
    public abstract void setDucks();

    /**
     * Handles passing the current level.
     * This method should be implemented by subclasses(levels) to define the behavior when a level is passed.
     */
    public abstract void passLevel();

    /**
     * Sets up the game screen with the specified features.
     */
    private void setScreen() {
        setSizes();
        setDucks();

        ammo = 3 * ducks.size();
        levelText = getText("Level " + level + "/6", 0, -9*height/20, 10);
        ammoText = getText("Ammo Left: " + ammo, 3*width/8, -9*height/20, 10);
        infoText = getText("", 0, -height / 14, 17);
        commandText = getText("", 0, height / 20, 17);
        flashing(commandText);

        foreground = getImage(foregroundsPaths.get(backgroundNum));
        leftBackground  = getImage(backgroundsPaths.get(backgroundNum));
        rightBackground  = getImage(backgroundsPaths.get(backgroundNum));
        leftBackground.setTranslateX(-width);
        rightBackground.setTranslateX(width);

        scene = setScene(new Node[]{leftBackground, foreground, rightBackground, levelText, ammoText, crosshair});
        setStage();
    }

    /**
     * Starts the animation for the ducks in the game screen.
     * This method is called to make the ducks move and animate.
     */
    private void playDucks() {
        for (Duck duck : ducks)
            if (duck.crossDuck) {
                moveDuck(duck, -20, duck.colour, 0);
            } else {
                moveDuck(duck, 0, duck.colour, 3);
            }
    }

    /**
     * Moves the specified duck in the game screen.
     *
     * @param duck        the duck to move
     * @param changeY     the change in Y position for the duck (to fly cross)
     * @param duckImage   the list of images for the duck
     * @param imageNum    the starting image number for the duck
     */
    private void moveDuck(Duck duck, double changeY, ArrayList<String> duckImage, int imageNum) {
        int[] imageNumber = {imageNum};

        double firstPositionX = duck.image.getTranslateX();

        double[] changePositionX = {20};
        double[] changePositionY = {changeY};

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> {
            if (duck.shot) return;

            //move
            duck.image.setTranslateX(duck.image.getTranslateX() + changePositionX[0]);
            duck.image.setTranslateY(duck.image.getTranslateY() + changePositionY[0]);

            //wingbeat
            double location = Math.round((duck.image.getTranslateX() - firstPositionX) * 10) / 10.0;

            if (location % (30) == 0) {
                imageNumber[0]++;
                if (imageNumber[0] == imageNum + 3) {
                    imageNumber[0] = imageNum;
                }
                duck.image.setImage(new Image(duckImage.get(imageNumber[0])));
            }

            //turn ducks over
            boolean inHorizontalBorder = Math.abs(duck.image.getTranslateY()) >= (2 * height / 5);
            boolean inRightBorder = duck.image.getTranslateX() >= background.getTranslateX() + (2 * width / 5);
            boolean inLeftBorder = duck.image.getTranslateX() <= background.getTranslateX() - (2 * width / 5);
            boolean rightward = changePositionX[0] > 0;
            boolean leftward = changePositionX[0] < 0;

            if ((inRightBorder && rightward) || (inLeftBorder && leftward)){
                duck.image.setScaleX(duck.image.getScaleX() * -1);
                changePositionX[0] *= -1;
            } else if (inHorizontalBorder){
                duck.image.setScaleY(duck.image.getScaleY() * -1);
                changePositionY[0] *= -1;
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Makes the specified duck fall in the game screen.
     *
     * @param duck       the duck to fall
     * @param duckImage  the list of images for the falling duck
     */
    private void fallDuck(ImageView duck, ArrayList<String> duckImage) {
        duck.setScaleY(scale);
        duck.setImage(new Image(duckImage.get(6)));
        double firstPosition = duck.getTranslateY();
        int[] changePosition = {20};

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> {
            duck.setTranslateY(duck.getTranslateY() + changePosition[0]);

            if ((duck.getTranslateY() - firstPosition) > 20) {
                duck.setImage(new Image(duckImage.get(7)));
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Handles the mouse movement on the game screen.
     * The crosshair and backgrounds are adjusted based on the mouse movement.
     */
    private void mouseMove() {
        scene.setCursor(Cursor.NONE);
        crosshair.setVisible(true);

        scene.setOnMouseMoved(event -> {
            double mouseX = event.getSceneX();
            double mouseY = event.getSceneY();

            crosshair.setTranslateX(mouseX - (width / 2));
            crosshair.setTranslateY(mouseY - (height / 2));

            scene.setOnMouseExited(e -> crosshair.setVisible(false));

            scene.setOnMouseEntered(e -> crosshair.setVisible(true));

            if (crosshair.getTranslateX() > width/2 - 40) {
                shifting(-1);
            } else if (crosshair.getTranslateX() < -width/2 + 40) {
                shifting(1);
            }
            mouseClick();
        });
    }

    /**
     * Handles the mouse click event on the game screen.
     * This method is called when the mouse is clicked to shoot at ducks.
     */
    private void mouseClick() {
        scene.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && ammo > 0) {
                this.music = setMusic("assets/effects/Gunshot.mp3", 1);
                ammo -= 1;

                if (ammo >= 0) ammoText.setText("Ammo Left: " + ammo);

                double mouseX = event.getSceneX();
                double mouseY = event.getSceneY();

                boolean nextLevel = true;

                for (Duck duck : ducks) {
                    double duckSize = duck.image.getBoundsInLocal().getWidth() * scale;

                    boolean inRight = duck.image.getTranslateX() + (width/2 - duckSize) < mouseX;
                    boolean inLeft = duck.image.getTranslateX() + (width/2 + duckSize) > mouseX;
                    boolean inTop = duck.image.getTranslateY() + (height/2 + duckSize) > mouseY;
                    boolean inBottom = duck.image.getTranslateY() + (height/2 - duckSize) < mouseY;

                    if (inRight && inLeft && inBottom && inTop){
                        duck.shot = true;
                        fallDuck(duck.image, duck.colour);
                        music = setMusic("assets/effects/DuckFalls.mp3", 1);
                    }
                    if (!duck.shot) {
                        nextLevel = false;
                    }
                }

                finishLevel = nextLevel;

                if (finishLevel || ammo == 0) {
                    if (finishLevel) finishLevel();
                    else gameOver();

                    mouseMove();
                    return;
                }
            }
            mouseMove();
        });
    }

    /**
     * Handles the completion of a level.
     * This method is called when all ducks have been shot.
     */
    private void finishLevel() {
        if (level == 6) {
            this.music = setMusic("assets/effects/LevelCompleted.mp3", 1);
            infoText.setText("You have completed game!");
            commandText.setText("Press ENTER to play again\nPress ESC to exit");
        }else {
            this.music = setMusic("assets/effects/GameCompleted.mp3", 1);
            infoText.setText("YOU WIN!");
            commandText.setText("Press ENTER to play next level");
        }

        scene = setScene(new Node[]{leftBackground, foreground, rightBackground, levelText, ammoText, infoText, commandText, crosshair});
        setStage();

        passLevel();
    }

    /**
     * Handles the game over event.
     * This method is called when the game is over, either by running out of ammo.
     */
    private void gameOver() {
        this.music = setMusic("assets/effects/GameOver.mp3", 1);
        infoText.setText("GAME OVER!");
        commandText.setText("Press ENTER to play again\nPress ESC to exit");

        scene = setScene(new Node[]{leftBackground, foreground, rightBackground, levelText, ammoText, infoText, commandText, crosshair});
        setStage();

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                music.stop();
                new Level1(stage, background, crosshair, backgroundNum);
            } else if (event.getCode() == KeyCode.ESCAPE) {
                music.stop();
                new TitleScreen(stage);
            }
        });
    }

    /**
     * Shifts the backgrounds in the specified direction by the given shift amount.
     *
     * @param shiftAmount the amount by which the elements should be shifted
     */
    private void shifting(double shiftAmount){
        boolean[] stopTimeline = {false};

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            boolean inLeft = crosshair.getTranslateX() < width/2 - 40;
            boolean inRight = crosshair.getTranslateX() > -width/2 + 40;
            boolean leftBorder = leftBackground.getTranslateX() >= 0 && shiftAmount == 1;
            boolean rightBorder = rightBackground.getTranslateX() <= 0 && shiftAmount == -1;

            if ((inRight && inLeft) || (leftBorder || rightBorder)) {
                stopTimeline[0] = true;
                return;
            }

            background.setTranslateX(background.getTranslateX() + shiftAmount);
            foreground.setTranslateX(foreground.getTranslateX() + shiftAmount);
            leftBackground.setTranslateX(leftBackground.getTranslateX() + shiftAmount);
            rightBackground.setTranslateX(rightBackground.getTranslateX() + shiftAmount);
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (stopTimeline[0]) {
                    timeline.stop();
                    stop();
                }
            }
        }.start();

        timeline.play();
    }
}