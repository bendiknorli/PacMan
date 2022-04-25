package pacman;

public class PacMan extends Character {

    private int[] startPos = { 1, 1 };
    private int[] lastPos = { 0, 0 };
    private String lastDirection = "right";

    public PacMan(int[] position) {
        super(position);
    }

    public int[] getStartPos() {
        return startPos;
    }

    public int[] getLastPos() {
        return lastPos;
    }

    public String getLastDirection() {
        return lastDirection;
    }

    public void setLastPos(int[] lastPos) {
        this.lastPos = lastPos;
    }

    public void setLastDirection(String lastDirection) {
        this.lastDirection = lastDirection;
    }
}
