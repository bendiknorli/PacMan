package pacman;

public class Character {

    private String type;
    private int[] position = { 0, 0 };
    private String direction = "right";

    public Character(int[] position, String type) {
        this.type = type;
        this.position[0] = (position[0]);
        this.position[1] = (position[1]);
    }

    public String getType() {
        return type;
    }

    public int[] getPosition() {
        return position;
    }

    public String getDirection() {
        return direction;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String toString() {
        return this.getType();
    }
}
