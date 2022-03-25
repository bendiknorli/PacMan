package pacman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class PacManHandler implements IPacManSaveLoad {

    @Override
    public Game readGame(String filename) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(getFile(filename))) {
            int numYTiles = Integer.parseInt(scanner.nextLine());
            int numXTiles = Integer.parseInt(scanner.nextLine());

            Game game = new Game(numXTiles, numYTiles);

            String coins = scanner.nextLine();
            // game.setCoins(10);
            game.setCoins(Integer.parseInt(coins));
            // if (!coins.equals("null")) {
            // }

            Tile[][] board = new Tile[numYTiles][numXTiles];
            for (int y = 0; y < numYTiles; y++) {
                for (int x = 0; x < numXTiles; x++) {
                    board[y][x] = new Tile();
                }
            }

            for (int y = 1; y < numYTiles - 1; y++) {
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

            int currentRow = 0;
            int currentColumn = 0;

            while (scanner.hasNextLine()) {
                String[] properties = scanner.nextLine().split(";");

                // Tile tile = new Tile();
                // tile.setPacMan(Boolean.parseBoolean(properties[0]));
                // tile.setCorridor(Boolean.parseBoolean(properties[1]));
                // tile.setCorner(Boolean.parseBoolean(properties[2]));
                // tile.setCoin(Boolean.parseBoolean(properties[3]));
                // tile.setGhost(Boolean.parseBoolean(properties[4]));
                // tile.setCherry(Boolean.parseBoolean(properties[5]));

                if (currentColumn < numYTiles) {
                    System.out.println("row: " + currentRow + " col: " + currentColumn);
                    // System.out.println("Byttet properties til tile");
                    if (Boolean.parseBoolean(properties[0])) {
                        System.out.println("Fant pacman " + properties[0]);
                        game.setPacManPos(currentRow, currentColumn);
                    }
                    board[currentRow][currentColumn].setPacMan(Boolean.parseBoolean(properties[0]));
                    board[currentRow][currentColumn].setCorridor(Boolean.parseBoolean(properties[1]));
                    board[currentRow][currentColumn].setCorner(Boolean.parseBoolean(properties[2]));
                    board[currentRow][currentColumn].setCoin(Boolean.parseBoolean(properties[3]));
                    board[currentRow][currentColumn].setGhost(Boolean.parseBoolean(properties[4]));
                    board[currentRow][currentColumn].setCherry(Boolean.parseBoolean(properties[5]));
                    currentColumn++;
                } else {
                    currentColumn = 0;
                    currentRow++;
                }
            }
            game.setBoard(board);
            Game testGame = new Game(20, 20);
            return game;
        }
    }

    @Override
    public void loadGame(String filename, Game game) throws FileNotFoundException {
        try (PrintWriter writer = new PrintWriter(getFile(filename))) {
            writer.println(game.getBoard()[0].length);
            writer.println(game.getBoard().length);
            writer.println(game.getCoins());

            for (Tile[] row : game.getBoard()) {
                for (Tile tile : row) {
                    writer.println(
                            tile.isPacMan() + ";" +
                                    tile.isCorridor() + ";" +
                                    tile.isCorner() + ";" +
                                    tile.isCoin() + ";" +
                                    tile.isGhost() + ";" +
                                    tile.isCherry());
                }
            }
        }
    }

    private static File getFile(String filename) {
        return new File("src\\demo\\src\\main\\java\\pacman\\" + filename + ".txt");
    }

    public static void main(String[] args) throws FileNotFoundException {
        Game game = new Game(20, 20);
        PacManHandler pacManHandler = new PacManHandler();

        pacManHandler.loadGame("Fil", game);

        pacManHandler.readGame("Fil");
    }
}
