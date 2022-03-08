package pacman;

public class Tile {

    private boolean isPacMan, isApple;

    public boolean isPacMan() {
        return isPacMan;
    }

    public boolean isApple() {
        return isApple;
    }

    public void setApple(boolean isApple) {
        this.isApple = isApple;
    }

    public void setPacMan(boolean isPacMan) {
        this.isPacMan = isPacMan;
    }
}
