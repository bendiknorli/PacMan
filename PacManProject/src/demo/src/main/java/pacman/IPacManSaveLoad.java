package pacman;

import java.io.FileNotFoundException;

public interface IPacManSaveLoad {

    Game readGame(String filename, Game game) throws FileNotFoundException;

    void loadGame(String filename, Game game) throws FileNotFoundException;

}
