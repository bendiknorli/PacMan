package pacman;

import java.util.ArrayList;

public class Game {
    private int numXTiles, numYTiles;

    private int snakeStartX = 1, snakeStartY = 1;

    private ArrayList<ArrayList<Integer>> snakePos = new ArrayList<>();

    private int[] lastSnakePartPos = { 0, 0 };
    private String oppositeDirection = "left";
    private String lastDirection = "right";

    private boolean alive = true;

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

        lastSnakePartPos[0] = snakeStartX;
        lastSnakePartPos[1] = snakeStartY;
        addSnakePart();

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

    public void addSnakePart() {
        ArrayList<Integer> newSnakePart = new ArrayList<>();
        newSnakePart.add(lastSnakePartPos[0]);
        newSnakePart.add(lastSnakePartPos[1]);
        snakePos.add(newSnakePart);
    }

    public void placeSnake() {
        int i = 0;
        while (i < snakePos.size()) {
            if (board[snakePos.get(i).get(0)][snakePos.get(i).get(1)].isApple()) {
                board[snakePos.get(i).get(0)][snakePos.get(i).get(1)].setApple(false);
                placeApple();
                addSnakePart();
            }
            board[snakePos.get(i).get(0)][snakePos.get(i).get(1)].setSnake(true);
            i++;
        }
    }

    public Tile getTile(int xPos, int yPos) {
        return board[yPos][xPos];
    }

    public void moveSnake(String direction) {
        if (!alive)
            return;
        if (direction == oppositeDirection)
            direction = lastDirection;
        try {
            switch (direction) {
                case "up":
                    if (getTile(snakePos.get(0).get(1), snakePos.get(0).get(0) - 1).isSnake()) {
                        alive = false;
                        System.out.println(
                                "U DED because " + snakePos.get(0).get(1) + " " + (snakePos.get(0).get(0) - 1));
                    }
                    snakeBodyFollow();
                    snakePos.get(0).set(0, snakePos.get(0).get(0) - 1);
                    oppositeDirection = "down";
                    break;
                case "down":
                    if (getTile(snakePos.get(0).get(1), snakePos.get(0).get(0) + 1).isSnake()) {
                        alive = false;
                        System.out.println(
                                "U DED because " + snakePos.get(0).get(1) + " " + (snakePos.get(0).get(0) + 1));
                        ;
                    }
                    snakeBodyFollow();
                    snakePos.get(0).set(0, snakePos.get(0).get(0) + 1);
                    oppositeDirection = "up";
                    break;
                case "left":
                    if (getTile(snakePos.get(0).get(1) - 1, snakePos.get(0).get(0)).isSnake()) {
                        alive = false;
                        System.out.println(
                                "U DED because " + (snakePos.get(0).get(1) - 1) + " " + snakePos.get(0).get(0));
                    }
                    snakeBodyFollow();
                    snakePos.get(0).set(1, snakePos.get(0).get(1) - 1);
                    oppositeDirection = "right";
                    break;
                case "right":
                    if (getTile(snakePos.get(0).get(1) + 1, snakePos.get(0).get(0)).isSnake()) {
                        alive = false;
                        System.out.println(
                                "U DED because " + (snakePos.get(0).get(1) - 1) + " " + snakePos.get(0).get(0));
                    }
                    snakeBodyFollow();
                    snakePos.get(0).set(1, snakePos.get(0).get(1) + 1);
                    oppositeDirection = "left";
                    break;
            }
        } catch (Exception e) {
            alive = false;
            System.out.println("U DED because out of map");
        }
        placeSnake();
        lastDirection = direction;
    }

    public void snakeBodyFollow() {
        lastSnakePartPos = new int[] { snakePos.get(0).get(0), snakePos.get(0).get(1) };
        int[] thisSnakePartPos = new int[] { 0, 0 };
        for (int i = 1; i < snakePos.size(); i++) {
            thisSnakePartPos[0] = lastSnakePartPos[0];
            thisSnakePartPos[1] = lastSnakePartPos[1];

            lastSnakePartPos[0] = snakePos.get(i).get(0);
            lastSnakePartPos[1] = snakePos.get(i).get(1);

            snakePos.get(i).set(0, thisSnakePartPos[0]);
            snakePos.get(i).set(1, thisSnakePartPos[1]);

            board[lastSnakePartPos[0]][lastSnakePartPos[1]].setSnake(false);
        }
    }
}
