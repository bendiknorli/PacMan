package pacman;

public class Tile {

    private boolean isPacMan, isApple, isCorridor, isCorner, isGhost, isCoin, isCherry;

    public boolean isPacMan() {
        return isPacMan;
    }

    public boolean isApple() {
        return isApple;
    }

    public boolean isCorridor() {
        return isCorridor;
    }

    public boolean isCorner() {
        return isCorner;
    }

    public boolean isGhost() {
        return isGhost;
    }

    public boolean isCoin() {
        return isCoin;
    }

    public boolean isCherry() {
        return isCherry;
    }

    public void setApple(boolean isApple) {
        this.isApple = isApple;
    }

    public void setPacMan(boolean isPacMan) {
        this.isPacMan = isPacMan;
    }

    public void setCorridor(boolean isCorridor) {
        this.isCorridor = isCorridor;
    }

    public void setCorner(boolean isCorner) {
        this.isCorner = isCorner;
        this.isCorridor = isCorner;
    }

    public void setGhost(boolean isGhost) {
        this.isGhost = isGhost;
    }

    public void setCoin(boolean isCoin) {
        this.isCoin = isCoin;
    }

    public void setCherry(boolean isCherry) {
        this.isCherry = isCherry;
    }
}
