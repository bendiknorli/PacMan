import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.*;

import pacman.Game;

public class GameTest {
    private Game game;

    @BeforeEach
    public void setup() {
        game = new Game(20, 20);
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
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
			game.setPacManPos(21, 21);
		});

        game.setPacManPos(15,12);
        Assertions.assertEquals(15, game.getPacManPos().get(0));
        Assertions.assertEquals(12, game.getPacManPos().get(1));

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
			game.setPacManPos(-5, 30);
		});
    }

    @Test
    @DisplayName("Tester om PacMan dør dersom den er på samme Tile som et spøkelse")
    public void testPacManDeath() {
        
        game.setPacManPos(17,18); //En tile unna spøkelsene 
        Assertions.assertTrue(game.getIsAlive());
        
        game.setPacManPos(18,18); //Tilen der spøkelsene spawner
        game.moveAll("down");
    
        Assertions.assertFalse(game.getIsAlive());

    }

    @Test
    @DisplayName("Om spøkelser beveger seg der de kan")
    public void testGhostMovemenet() {
        
    }

//om spøkelser beveger seg opp når den er i en korridor, og om den beveger seg andre veier dersom den er i et hjørne

}
