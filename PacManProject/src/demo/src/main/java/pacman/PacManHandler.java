package pacman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

public class PacManHandler implements IPacManSaveLoad {

    @Override
    public Game loadGame(String filename) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(getFile(filename))) {
            int numYTiles = Integer.parseInt(scanner.nextLine());
            int numXTiles = Integer.parseInt(scanner.nextLine());

            Game game = new Game(numXTiles, numYTiles);

            String coins = scanner.nextLine();
            // game.setCoins(10);
            game.setCoins(Integer.parseInt(coins));
            // if (!coins.equals("null")) {
            // }

            List<String> characterStrings = Arrays.asList(scanner.nextLine().split(";"));
            ArrayList<Character> characters = new ArrayList<>();

            for (String character : characterStrings) {
                String[] characterAttributes = character.split(",");
                Character new_character = new Character(new int[] { Integer.parseInt(characterAttributes[0]),
                        Integer.parseInt(characterAttributes[1]) });
                new_character.setDirection(characterAttributes[2]);
                characters.add(new_character);
            }

            Tile[][] board = new Tile[numYTiles][numXTiles];
            for (int y = 0; y < numYTiles; y++) {
                for (int x = 0; x < numXTiles; x++) {
                    board[y][x] = new Tile();
                }
            }

            List<List<String>> rows = new ArrayList<>();
            while (scanner.hasNextLine()) {
                List<String> tiles = Arrays.asList(scanner.nextLine().split(";"));
                rows.add(tiles);
            }

            for (int y = 1; y < numYTiles - 1; y++) {
                for (int x = 1; x < numXTiles - 1; x++) {
                    String tileString = rows.get(y).get(x);

                    if (tileString.charAt(0) == '1') {
                        board[y][x].setPacMan(true);
                        game.setPacManPos(x, y);
                    }
                    if (tileString.charAt(1) == '1')
                        board[y][x].setCorridor(true);
                    if (tileString.charAt(2) == '1')
                        board[y][x].setCorner(true);
                    if (tileString.charAt(3) == '1')
                        board[y][x].setCoin(true);
                    if (tileString.charAt(4) == '1')
                        board[y][x].setGhost(true);
                    if (tileString.charAt(5) == '1')
                        board[y][x].setCherry(true);
                }
            }

            game.setBoard(board);
            game.setCharacters(characters);
            return game;
        }
    }

    @Override
    public void saveGame(String filename, Game game) throws FileNotFoundException {
        try (PrintWriter writer = new PrintWriter(getFile(filename))) {
            writer.println(game.getBoard()[0].length);
            writer.println(game.getBoard().length);
            writer.println(game.getCoins());

            String characterString = "";
            for (Character character : game.getCharacters()) {
                characterString += character.getPosition()[0] + "," +
                        character.getPosition()[1] + ","
                        + character.getDirection() + ";";
            }
            writer.println(characterString);

            for (Tile[] row : game.getBoard()) {
                String rowString = "";
                for (Tile tile : row) {
                    String tileString = "";
                    if (tile.isPacMan())
                        tileString += "1";
                    else
                        tileString += "0";
                    if (tile.isCorridor())
                        tileString += "1";
                    else
                        tileString += "0";
                    if (tile.isCorner())
                        tileString += "1";
                    else
                        tileString += "0";
                    if (tile.isCoin())
                        tileString += "1";
                    else
                        tileString += "0";
                    if (tile.isGhost())
                        tileString += "1";
                    else
                        tileString += "0";
                    if (tile.isCherry())
                        tileString += "1";
                    else
                        tileString += "0";

                    tileString += ";";

                    rowString += tileString;
                }
                writer.println(rowString);
            }
        }
    }

    private static File getFile(String filename) {
        return new File("src\\demo\\src\\main\\java\\pacman\\" + filename + ".txt");
    }

    public static void main(String[] args) throws FileNotFoundException {
        Game game = new Game(20, 20);
        PacManHandler pacManHandler = new PacManHandler();

        pacManHandler.saveGame("Fil", game);

        pacManHandler.loadGame("Fil");
    }
}
