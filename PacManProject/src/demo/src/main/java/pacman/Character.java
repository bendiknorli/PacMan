package pacman;

public abstract class Character {

    protected int[] position = { 1, 1 };
    private String direction = "right";

    public Character(int[] position) {
        this.position = position;
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
