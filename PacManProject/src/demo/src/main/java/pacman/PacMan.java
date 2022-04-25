package pacman;

import java.util.ArrayList;

public class PacMan extends Character {

    private int[] startPos = { 1, 1 };
    private int[] lastPos = { 0, 0 };
    private String lastDirection = "right";

    public PacMan(int[] position) {
        super(position);
    }

    public void setPacManStartPosition() {
        position[0] = startPos[0];
        position[1] = startPos[1];
    }

    public int[] getStartPos() {
        return startPos;
    }

    public int[] getLastPos() {
        return lastPos;
    }

    public void setLastPos(int[] lastPos) {
        this.lastPos = lastPos;
    }

    public String getLastDirection() {
        return lastDirection;
    }

    public void setLastDirection(String lastDirection) {
        this.lastDirection = lastDirection;
    }
}
