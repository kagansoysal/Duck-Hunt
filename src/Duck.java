import javafx.scene.image.ImageView;
import java.util.ArrayList;

public class Duck {
    ImageView image;
    ArrayList<String> colour;
    boolean shot;
    boolean crossDuck;

    Duck(ImageView image, ArrayList<String> colour,boolean crossDuck, double x, double y){
        this.image = image;
        this.colour = colour;
        this.crossDuck = crossDuck;
        this.image.setTranslateX(x);
        this.image.setTranslateY(y);
    }
}