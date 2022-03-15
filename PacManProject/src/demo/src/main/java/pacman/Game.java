package pacman;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.paint.Color;

public class Game {
    private int numXTiles, numYTiles;

    private int pacManStartX = 1, pacManStartY = 1;
    private boolean pacManToMove;

    private ArrayList<Integer> pacManPos = new ArrayList<>();
    private ArrayList<Integer> lastPos = new ArrayList<>();
    private String lastDirection = "right";

    private int coins = 0;

    private int secondsSinceEatenCherry = 0;

    // public ArrayList<ArrayList<Integer>> ghostsPos = new ArrayList<>();

    public ArrayList<Character> characters = new ArrayList<>();

    public Tile[][] board;

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

        // ArrayList<Integer> ghostPos = new ArrayList<>();
        // ghostPos.add(0);
        // ghostPos.add(0);
        // ghostsPos.add(ghostPos);

        board = new Tile[numYTiles][numXTiles];
        for (int y = 0; y < numYTiles; y++) {
            for (int x = 0; x < numXTiles; x++) {
                board[y][x] = new Tile();
            }
        }

        // Character character = new Character(new int[] { 0, 0 }, "purpleGhost", this);
        // characters.add(character);
        placeMap();
        placePacMan();
    }

    public void placeMap() {
        for (int y = 1; y < numYTiles - 1; y++) {
            if (y % 5 == 0) {
                Character character = new Character(new int[] { numYTiles - 2, numXTiles - 2 }, "purpleGhost");
                characters.add(character);
            }
            for (int x = 1; x < numXTiles - 1; x++) {
                if (x == 1 || x % 5 == 0 || x == numXTiles - 2) {
                    if (x == numXTiles / 2 && y == numYTiles - 2)
                        board[y][x].setCherry(true);
                    else if ((x + y) % 2 == 0)
                        board[y][x].setCoin(true);
                    if (y == 1 || y % 6 == 0 || y == numYTiles - 2)
                        board[y][x].setCorner(true);
                    else
                        board[y][x].setCorridor(true);
                }
                if (y == 1 || y % 6 == 0 || y == numYTiles - 2) {
                    board[y][x].setCorridor(true);
                    if ((x + y) % 2 == 0 && !board[y][x].isCherry())
                        board[y][x].setCoin(true);
                }
            }
        }
    }

    public boolean areCoinsLeft() {
        for (int y = 0; y < numYTiles; y++) {
            for (int x = 0; x < numXTiles; x++) {
                if (board[y][x].isCoin()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void moveAll(String direction) {
        if (board[pacManPos.get(0)][pacManPos.get(1)].isGhost() && secondsSinceEatenCherry == 0) {
            System.out.println("U DED");
        } else if (board[pacManPos.get(0)][pacManPos.get(1)].isGhost() && secondsSinceEatenCherry != 0) {
            coins += 1000;
            System.out.println("GHOSTBUSTER");
            for (Character character : characters) {
                if (character.getPosition()[0] == pacManPos.get(0) &&
                        character.getPosition()[1] == pacManPos.get(1)) {
                    characters.remove(character);
                    board[pacManPos.get(0)][pacManPos.get(1)].setGhost(false);
                    return;
                }
            }
        }
        if (board[pacManPos.get(0)][pacManPos.get(1)].isCherry()) {
            board[pacManPos.get(0)][pacManPos.get(1)].setCherry(false);
            secondsSinceEatenCherry = 50;
        }
        if (pacManToMove)
            movePacMan(direction);
        else
            moveGhosts();

        pacManToMove = !pacManToMove;
    }

    public void moveGhosts() {
        if (secondsSinceEatenCherry != 0) {
            secondsSinceEatenCherry--;
            Character.setColor(Color.DARKBLUE);
        } else
            Character.setColor(Color.PURPLE);
        for (Character character : characters) {
            if (board[character.getPosition()[0]][character.getPosition()[1]].isCorner()) {
                ArrayList<String> possibleDirections = new ArrayList<>();
                if (board[character.getPosition()[0] - 1][character.getPosition()[1]].isCorridor())
                    possibleDirections.add("up");
                if (board[character.getPosition()[0] + 1][character.getPosition()[1]].isCorridor())
                    possibleDirections.add("down");
                if (board[character.getPosition()[0]][character.getPosition()[1] - 1].isCorridor())
                    possibleDirections.add("left");
                if (board[character.getPosition()[0]][character.getPosition()[1] + 1].isCorridor())
                    possibleDirections.add("right");

                Random rand = new Random();
                String randomDirection = possibleDirections.get(rand.nextInt(possibleDirections.size()));
                character.setDirection(randomDirection);
            }
            board[character.getPosition()[0]][character.getPosition()[1]].setGhost(false);
            int[] newPosition = character.getPosition();
            switch (character.getDirection()) {
                case "up":
                    newPosition[0] = character.getPosition()[0] - 1;
                    break;
                case "down":
                    newPosition[0] = character.getPosition()[0] + 1;
                    break;
                case "left":
                    newPosition[1] = character.getPosition()[1] - 1;
                    break;
                case "right":
                    newPosition[1] = character.getPosition()[1] + 1;
                    break;
            }
            character.setPosition(newPosition);
            board[character.getPosition()[0]][character.getPosition()[1]].setGhost(true);
        }
    }

    public void placePacMan() {
        if (!board[pacManPos.get(0)][pacManPos.get(1)].isCorridor())
            throw new IllegalArgumentException("Prøvde å bevege seg utenfor brettet");
        else if (board[pacManPos.get(0)][pacManPos.get(1)].isCoin()) {
            board[pacManPos.get(0)][pacManPos.get(1)].setCoin(false);
            coins++;
            if (!areCoinsLeft())
                System.out.println("W");
        }
        board[lastPos.get(0)][lastPos.get(1)].setPacMan(false);
        board[pacManPos.get(0)][pacManPos.get(1)].setPacMan(true);
    }

    public Tile getTile(int xPos, int yPos) {
        return board[yPos][xPos];
    }

    public int getCoins() {
        return coins;
    }

    public void movePacMan(String direction) {
        lastPos.set(0, pacManPos.get(0));
        lastPos.set(1, pacManPos.get(1));
        lastDirection = direction;

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
            pacManPos.set(0, lastPos.get(0));
            pacManPos.set(1, lastPos.get(1));
            direction = lastDirection;
        }
    }
}
