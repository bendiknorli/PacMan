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
        // ingenting skjer dersom filen ikke eksisterer
        try (Scanner scanner = new Scanner(getFile(filename))) {
            // scanner hver linje, og deklarerer variabelen og formaterer fra String til int
            int numXTiles = Integer.parseInt(scanner.nextLine());
            int numYTiles = Integer.parseInt(scanner.nextLine());

            Game game = new Game(numXTiles, numYTiles);

            String score = scanner.nextLine();

            // setter inn verdiene fra tekstfilen for å laste opp det gamle spillet
            game.setScore(Integer.parseInt(score));
            game.getPacMan().setLastDirection(scanner.nextLine());
            game.setFramesSinceEatenCherry(Integer.parseInt(scanner.nextLine()));

            // splitter for å få hvert enkel ghost
            List<String> ghostStrings = Arrays.asList(scanner.nextLine().split(";"));
            ArrayList<Ghost> ghosts = new ArrayList<>();

            // får posisjon, og retning til ghost
            for (String ghost : ghostStrings) {
                String[] ghostAttributes = ghost.split(",");
                if (ghostAttributes[0] == "")
                    continue;
                Ghost newGhost = new Ghost(new int[] { Integer.parseInt(ghostAttributes[0]),
                        Integer.parseInt(ghostAttributes[1]) });
                newGhost.setDirection(ghostAttributes[2]);
                ghosts.add(newGhost);
            }

            // lager et nytt brett
            Tile[][] board = new Tile[numYTiles][numXTiles];
            for (int y = 0; y < numYTiles; y++) {
                for (int x = 0; x < numXTiles; x++) {
                    board[y][x] = new Tile();
                }
            }

            // tekstfilen består av en to dimensjonal liste, der hver linje representerer en
            // rad på brettet
            List<List<String>> rows = new ArrayList<>();
            while (scanner.hasNextLine()) {
                List<String> tiles = Arrays.asList(scanner.nextLine().split(";"));
                rows.add(tiles);
            }

            // iterer gjennom hver Tile på raden, og setter tilstanden
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
            game.setGhosts(ghosts);
            return game;
        }
    }

    @Override
    public void saveGame(String filename, Game game) throws FileNotFoundException {
        // dersom filen ikke eksisterer, så lager den en ny fil
        try (PrintWriter writer = new PrintWriter(getFile(filename))) {
            // skriver ut spillet til tekstfil på angitt format
            writer.println(game.getBoard()[0].length);
            writer.println(game.getBoard().length);
            writer.println(game.getScore());
            writer.println(game.getPacMan().getLastDirection());
            writer.println(game.getFramesSinceEatenCherry());

            String ghostString = "";
            for (Ghost ghost : game.getGhosts()) {
                ghostString += ghost.getPosition()[0] + "," +
                        ghost.getPosition()[1] + ","
                        + ghost.getDirection() + ";";
            }
            writer.println(ghostString);

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
        return new File(filename + ".txt");
    }

}
