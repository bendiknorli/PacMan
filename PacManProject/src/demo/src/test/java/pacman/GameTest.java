package pacman;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertEquals(1, game.getPacManPos().get(0));
        assertEquals(1, game.getPacManPos().get(1));
    }

    @Test
    @DisplayName("Tester om man kan sette posisjonen innenfor brettet, utafor og med negative verdier")
    public void testValidPos() {
        assertThrows(IllegalArgumentException.class, () -> {
            game.setPacManPos(21, 21);
        });

        game.setPacManPos(15, 12); // (y,x)
        assertEquals(12, game.getPacManPos().get(0));
        assertEquals(15, game.getPacManPos().get(1));

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
        assertTrue(game.getTile(1, 1).isCorner());
        assertTrue(game.getTile(18, 1).isCorner());
        assertFalse(game.getTile(1, 2).isCorner());
        assertFalse(game.getTile(5, 13).isCorner());

        assertTrue(game.getTile(2, 1).isCorridor());
        assertFalse(game.getTile(2, 2).isCorridor());

        assertTrue(game.getTile(10, 18).isCherry());
        assertFalse(game.getTile(2, 1).isCherry());

        assertFalse(game.getTile(2, 1).isCoin());
        assertTrue(game.getTile(3, 1).isCoin());
        assertFalse(game.getTile(4, 1).isCoin());
        assertFalse(game.getTile(1, 2).isCoin());

        assertEquals(3, game.getCharacters().size());
        assertTrue(game.getTile(18, 18).isGhost());
        assertFalse(game.getTile(12, 18).isGhost());

    }

    @Test
    @DisplayName("Tester areCoinsLeft, der PacMan er to tiles unna mynten")
    public void testCoinsAreLeft() {
        Game smallGame = new Game(5, 3);
        assertTrue(smallGame.areCoinsLeft());

        smallGame.movePacMan("right");
        assertTrue(smallGame.areCoinsLeft());

        smallGame.movePacMan("right");
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
        game.setFramesSinceEatenCherry(1); // trenger en fram med power-up
        game.setPacManPos(18, 18);
        game.moveAll("down");

        assertTrue(game.isAlive());
        assertEquals(game.getScore(), 11);
    }

    @Test
    @DisplayName("Om spøkelser beveger seg der de kan")
    public void testGhostMovemenet() {

    }

    // om spøkelser beveger seg opp når den er i en korridor, og om den beveger seg
    // andre veier dersom den er i et hjørne

    // initalize

    // placemap

    // arecoins
}