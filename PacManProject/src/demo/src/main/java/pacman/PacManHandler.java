package pacman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class PacManHandler implements IPacManSaveLoad {

    @Override
    public Game readGame(String filename, Game game) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(getFile(filename))) {
            String coins = scanner.nextLine();
            if (!coins.equals("null")) {
                game.areCoinsLeft();
            }
            while (scanner.hasNextLine()) {
                String[] properties = scanner.nextLine().split(";");

                Tile tile = new Tile();
                tile.setPacMan(Boolean.parseBoolean(properties[0]));
                tile.setApple(Boolean.parseBoolean(properties[1]));
                tile.setCoin(Boolean.parseBoolean(properties[2]));
                tile.setCorner(Boolean.parseBoolean(properties[3]));
                tile.setGhost(Boolean.parseBoolean(properties[4]));
                tile.setCorridor(Boolean.parseBoolean(properties[5]));
                tile.setCherry(Boolean.parseBoolean(properties[6]));
            }
        }
        return null;
    }

    @Override
    public void loadGame(String filename, Game game) throws FileNotFoundException {
        try (PrintWriter writer = new PrintWriter(getFile(filename))) {
            writer.println(game.getCoins());

            for (Tile[] row : game.board) {
                for (Tile tile : row) {
                    writer.println(tile.isPacMan() + ";" + tile.isApple() + ";" + tile.isCoin()
                            + ";" + tile.isCorner() + ";" + tile.isGhost() + ";" + tile.isCorridor() + ";"
                            + tile.isCherry());
                }
            }
        }
    }

    private static File getFile(String filename) {
        return new File("src\\demo\\src\\main\\java\\pacman\\" + filename + ".txt");
    }

    public static void main(String[] args) throws FileNotFoundException {
        Game game = new Game(10, 10);
        PacManHandler pacManHandler = new PacManHandler();

        pacManHandler.loadGame("Fil", game);
    }
}
