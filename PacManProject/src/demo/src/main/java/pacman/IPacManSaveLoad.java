package pacman;

import java.io.FileNotFoundException;

public interface IPacManSaveLoad {

    Game readGame(String filename) throws FileNotFoundException;

    void loadGame(String filename, Game game) throws FileNotFoundException;

}
