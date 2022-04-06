package pacman;

import javafx.scene.paint.Color;

public class Character {

    private int[] position = { 0, 0 };
    private String direction = "right";
    private static Color color;

    public Character(int[] position) {
        this.position[0] = (position[0]);
        this.position[1] = (position[1]);
        color = Color.PURPLE;
    }

    public static Color getColor() {
        return color;
    }

    public static void setColor(Color color) {
        Character.color = color;
    }

    public int[] getPosition() {
        return position;
    }

    public String getDirection() {
        return direction;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
