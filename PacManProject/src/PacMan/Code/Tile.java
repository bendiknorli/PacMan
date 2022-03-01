package PacMan.Code;

public class Tile {

    private boolean isSnake, isApple;

    public boolean isSnake() {
        return isSnake;
    }

    public boolean isApple() {
        return isApple;
    }

    public void setApple(boolean isApple) {
        this.isApple = isApple;
    }

    public void setSnake(boolean isSnake) {
        this.isSnake = isSnake;
    }
}
