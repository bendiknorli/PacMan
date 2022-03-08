package pacman;

import java.util.ArrayList;

public class Game {
    private int numXTiles, numYTiles;

    private int pacManStartX = 1, pacManStartY = 1;

    private ArrayList<Integer> pacManPos = new ArrayList<>();

    private ArrayList<Integer> lastPos = new ArrayList<>();

    private Tile[][] board;

    public Game(int numXTiles, int numYTiles) {
        this.numXTiles = numXTiles;
        this.numYTiles = numYTiles;

        initialize(numXTiles, numYTiles);
    }

    private void initialize(int numXTiles, int numYTiles) {

        pacManPos.add(pacManStartX);
        pacManPos.add(pacManStartY);

        lastPos.add(pacManStartX);
        lastPos.add(pacManStartY);

        board = new Tile[numYTiles][numXTiles];
        for (int y = 0; y < numYTiles; y++) {
            for (int x = 0; x < numXTiles; x++) {
                board[y][x] = new Tile();
            }
        }
        placeApple();
        placePacMan();
    }

    public void placeApple() {
        int randomY;
        int randomX;
        do {
            randomY = ((int) (Math.random() * numYTiles));
            randomX = ((int) (Math.random() * numXTiles));
        } while (board[randomY][randomX].isPacMan());

        board[randomY][randomX].setApple(true);
    }

    public void placePacMan() {
        if (board[pacManPos.get(0)][pacManPos.get(1)].isApple()) {
            board[pacManPos.get(0)][pacManPos.get(1)].setApple(false);
            placeApple();
        }
        board[lastPos.get(0)][lastPos.get(1)].setPacMan(false);
        board[pacManPos.get(0)][pacManPos.get(1)].setPacMan(true);
    }

    public Tile getTile(int xPos, int yPos) {
        return board[yPos][xPos];
    }

    public void movePacMan(String direction) {
        lastPos.set(0, pacManPos.get(0));
        lastPos.set(1, pacManPos.get(1));

        try {
            switch (direction) {
                case "up":
                    pacManPos.set(0, pacManPos.get(0) - 1);
                    break;
                case "down":
                    pacManPos.set(0, pacManPos.get(0) + 1);
                    break;
                case "left":
                    pacManPos.set(1, pacManPos.get(1) - 1);
                    break;
                case "right":
                    pacManPos.set(1, pacManPos.get(1) + 1);
                    break;
            }
            placePacMan();
        } catch (Exception e) {
            System.out.println("U DED because out of map");
        }
    }
}
