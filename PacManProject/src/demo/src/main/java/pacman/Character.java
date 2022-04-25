package pacman;

public class Character {

    protected int[] position = { 1, 1 };
    private String direction = "right";

    public Character(int[] position) {
        this.position[0] = (position[0]);
        this.position[1] = (position[1]);
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
