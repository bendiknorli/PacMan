package pacman;

import javafx.scene.paint.Color;

public class Ghost extends Character {

    private static Color color;
    private static Color normalColor;
    private static Color edibleColor;

    public Ghost(int[] position) {
        super(position);
        color = normalColor = Color.GREEN;
        edibleColor = Color.DARKBLUE;
    }

    public static Color getColor() {
        return color;
    }

    public static Color getNormalColor() {
        return normalColor;
    }

    public static Color getEdibleColor() {
        return edibleColor;
    }

    public static void setColor(Color color) {
        Ghost.color = color;
    }

}
