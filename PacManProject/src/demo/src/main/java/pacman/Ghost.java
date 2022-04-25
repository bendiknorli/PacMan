package pacman;

import javafx.scene.paint.Color;

public class Ghost extends Character {

    private static Color color;

    public Ghost(int[] position) {
        super(position);
        color = Color.GREEN;
    }

    public static Color getColor() {
        return color;
    }

    public static void setColor(Color color) {
        Ghost.color = color;
    }

}
