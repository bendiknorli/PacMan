package pacman;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameTest {
    private Game game;
    private int numXTiles, numYTiles;

    @BeforeEach
    public void setup() {
        numXTiles = 20;
        numYTiles = 20;
        game = new Game(numXTiles, numYTiles);
    }

    @Test
    @DisplayName("Test å se om startposisjon er riktig")
    public void testStartPos() {
        assertEquals(1, game.getPacManPos()[0]);
        assertEquals(1, game.getPacManPos()[1]);
    }

    @Test
    @DisplayName("Tester om man kan sette posisjonen innenfor brettet, utafor og med negative verdier")
    public void testValidPos() {
        assertThrows(IllegalArgumentException.class, () -> {
            game.setPacManPos(21, 21);
        });

        game.setPacManPos(15, 12); // (x,y)
        assertEquals(12, game.getPacManPos()[0]);
        assertEquals(15, game.getPacManPos()[1]);

        assertThrows(IllegalArgumentException.class, () -> {
            game.setPacManPos(-5, 30);
        });
    }

    @Test
    @DisplayName("Tester om PacMan dør dersom den er på samme Tile som et spøkelse")
    public void testPacManDeath() {

        game.setPacManPos(17, 18); // En tile unna spøkelsene
        assertTrue(game.isAlive());

        game.setPacManPos(18, 18); // Tilen der spøkelsene spawner
        game.moveAll("down");

        assertFalse(game.isAlive());

    }

    @Test
    @DisplayName("Tester om brettets Corner, Corridor, Cherry, Coin, Ghosts, er der de skal")
    public void testMap() {
        assertTrue(game.getTile(1, 1).isCorner()); // Hjørne
        assertFalse(game.getTile(1, 2).isCorner()); // Korridor
        assertFalse(game.getTile(5, 13).isCorner()); // Svart tile

        assertTrue(game.getTile(2, 1).isCorridor());
        assertFalse(game.getTile(2, 2).isCorridor());

        assertTrue(game.getTile(10, 18).isCherry());
        assertFalse(game.getTile(2, 1).isCherry());

        assertFalse(game.getTile(2, 1).isCoin());
        assertTrue(game.getTile(3, 1).isCoin());
        assertFalse(game.getTile(4, 1).isCoin());
        assertFalse(game.getTile(1, 2).isCoin());

        assertEquals(3, game.getGhosts().size()); // Om antall spøkelser er spawnet riktig
        assertTrue(game.getTile(18, 18).isGhost()); // Spøkelser i startposisjon
        assertFalse(game.getTile(12, 18).isGhost()); // Tom felt
    }

    @Test
    @DisplayName("Tester settere for Tile klassen")
    public void testTileSetters() {
        game.getTile(2, 1).setGhost(true); // Plasserer et spøkelse for å se om det faktisk er en spøkelse
        assertTrue(game.getTile(2, 1).isGhost());

        game.getTile(2, 1).setCherry(true);
        assertTrue(game.getTile(2, 1).isCherry());

        game.getTile(2, 1).setCorner(true);
        assertTrue(game.getTile(2, 1).isCorner());

        game.getTile(2, 2).setCorridor(true);
        assertTrue(game.getTile(2, 2).isCorridor());

        game.getTile(2, 1).setCoin(true);
        assertTrue(game.getTile(2, 1).isCoin());

        game.getTile(2, 1).setPacMan(true);
        assertTrue(game.getTile(2, 1).isPacMan());
    }

    @Test
    @DisplayName("Tester areCoinsLeft, der PacMan er to tiles unna mynten")
    public void testCoinsAreLeft() {
        Game smallGame = new Game(5, 3);
        assertTrue(smallGame.areCoinsLeft());

        smallGame.moveAll("right");
        smallGame.moveAll("right");
        assertTrue(smallGame.areCoinsLeft());

        smallGame.moveAll("right");
        smallGame.moveAll("right");
        assertFalse(smallGame.areCoinsLeft());
    }

    @Test
    @DisplayName("Tester Score")
    public void testScore() {
        game.setScore(20);
        assertEquals(20, game.getScore());

        game.setScore(0);
        assertEquals(0, game.getScore());

        assertThrows(IllegalArgumentException.class, () -> {
            game.setScore(-1);
        });
    }

    @Test
    @DisplayName("Tester moveAll")
    public void testMoveAll() {

        Game smallGame = new Game(3, 7);

        smallGame.getTile(1, 2).setCherry(true); // Tester om power-upen fungerer som den skal
        assertTrue(smallGame.getTile(1, 5).isGhost()); // Tester startposisjonen til Pacman og ghost
        assertTrue(smallGame.getTile(1, 1).isPacMan());

        smallGame.moveAll("down");
        assertTrue(smallGame.getTile(1, 4).isGhost()); // moveAll sørger for at spøkelsene og Pacman beveger seg
                                                       // vekselsvis
        assertFalse(smallGame.getTile(1, 2).isPacMan());

        smallGame.moveAll("down");
        assertFalse(smallGame.getTile(1, 3).isGhost());
        assertTrue(smallGame.getTile(1, 2).isPacMan());

        smallGame.moveAll("down");
        assertTrue(smallGame.getTile(1, 3).isGhost());
        assertFalse(smallGame.getTile(1, 3).isPacMan());

        assertEquals(49, smallGame.getFramesSinceEatenCherry()); // moveAll sjekker også om PacMan har spist power-upen,
                                                                 // og må derfor gjentas for at den blir aktivert
        smallGame.moveAll("down");
        assertFalse(smallGame.getTile(1, 2).isGhost());
        assertTrue(smallGame.getTile(1, 3).isPacMan());

        smallGame.moveAll("down");
        assertFalse(smallGame.getTile(1, 2).isGhost()); // spøkelsen har blitt spist
        assertFalse(smallGame.getTile(1, 4).isPacMan());

        assertTrue(smallGame.isAlive()); // Sjekker om PacMan fortsatt er i live etter å ha truffet spøkelsen
        assertEquals(12, smallGame.getScore()); // To mynter på brettet + spøkelse gir 10
    }

    @Test
    @DisplayName("Om spøkelser beveger seg der de kan")
    public void testGhostMovemenet() {
        Game smallGame = new Game(8, 8);

        assertTrue(smallGame
                .getTile(smallGame.getGhosts().get(0).getPosition()[0], smallGame.getGhosts().get(0).getPosition()[1])
                .isCorner());
        smallGame.moveAll("left");
        System.out.println(smallGame.getGhosts().get(0).getPosition()[0]);
        System.out.println(smallGame.getGhosts().get(0).getPosition()[1]);
        assertTrue(Arrays.equals(smallGame.getGhosts().get(0).getPosition(), new int[] { 5, 6 })
                || Arrays.equals(smallGame.getGhosts().get(0).getPosition(), new int[] { 6, 5 }));

        assertTrue(smallGame.getTile(6, 5).isCorridor());
        smallGame.getGhosts().get(0).setPosition(new int[] { 6, 5 });
        ;
        smallGame.getGhosts().get(0).setDirection("up");
        smallGame.moveAll("up"); // Spøkelsen er i et korridor som fører til at den skal kun bevege seg oppover
                                 // korridoren
        assertTrue(Arrays.equals(smallGame.getGhosts().get(0).getPosition(), new int[] { 6, 5 }));
    }

    // placemap

}