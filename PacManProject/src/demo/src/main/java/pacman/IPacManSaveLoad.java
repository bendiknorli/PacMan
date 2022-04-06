package pacman;

import java.io.FileNotFoundException;

public interface IPacManSaveLoad {

    Game loadGame(String filename) throws FileNotFoundException;

    void saveGame(String filename, Game game) throws FileNotFoundException;

}
