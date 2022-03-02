package pacman;

import java.util.ArrayList;

public class Game {
    private int numXTiles, numYTiles;

    private int snakeStartX = 1, snakeStartY = 1;

    private ArrayList<ArrayList<Integer>> snakePos = new ArrayList<>();

    private ArrayList<Integer> lastPos = new ArrayList<>();

    private Tile[][] board;

    public Game(int numXTiles, int numYTiles) {
        this.numXTiles = numXTiles;
        this.numYTiles = numYTiles;

        initialize(numXTiles, numYTiles);
    }

    private void initialize(int numXTiles, int numYTiles) {

        ArrayList<Integer> snakeHead = new ArrayList<>();
        snakeHead.add(snakeStartX);
        snakeHead.add(snakeStartY);
        snakePos.add(snakeHead);

        lastPos.add(1);
        lastPos.add(1);

        System.out.println(snakePos);

        board = new Tile[numYTiles][numXTiles];
        for (int y = 0; y < numYTiles; y++) {
            for (int x = 0; x < numXTiles; x++) {
                board[y][x] = new Tile();
            }
        }
        placeApple();
        placeSnake();
    }

    public void placeApple() {
        int randomY;
        int randomX;
        do {
            randomY = ((int) (Math.random() * numYTiles));
            randomX = ((int) (Math.random() * numXTiles));
        } while (board[randomY][randomX].isSnake());

        board[randomY][randomX].setApple(true);
    }

    public void placeSnake() {
        if (board[snakePos.get(0).get(0)][snakePos.get(0).get(1)].isApple()) {
            board[snakePos.get(0).get(0)][snakePos.get(0).get(1)].setApple(false);
            placeApple();
        }
        board[lastPos.get(0)][lastPos.get(1)].setSnake(false);
        board[snakePos.get(0).get(0)][snakePos.get(0).get(1)].setSnake(true);
    }

    public Tile getTile(int xPos, int yPos) {
        return board[yPos][xPos];
    }

    public void moveSnake(String direction) {
        lastPos.set(0, snakePos.get(0).get(0));
        lastPos.set(1, snakePos.get(0).get(1));

        try {
            switch (direction) {
                case "up":
                    snakePos.get(0).set(0, snakePos.get(0).get(0) - 1);
                    break;
                case "down":
                    snakePos.get(0).set(0, snakePos.get(0).get(0) + 1);
                    break;
                case "left":
                    snakePos.get(0).set(1, snakePos.get(0).get(1) - 1);
                    break;
                case "right":
                    snakePos.get(0).set(1, snakePos.get(0).get(1) + 1);
                    break;
            }
        } catch (Exception e) {
            System.out.println("U DED because out of map");
        }
        placeSnake();
    }
}
