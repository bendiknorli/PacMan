package pacman;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.paint.Color;

public class Game {
    private int numXTiles, numYTiles;

    private int pacManStartX = 1, pacManStartY = 1;
    private boolean pacManToMove; // om det er pacman som beveger seg denne gangen eller spøkelsene

    private ArrayList<Integer> pacManPos = new ArrayList<>();
    private ArrayList<Integer> lastPos = new ArrayList<>();
    private String lastDirection = "right";

    private int coins = 0;

    private int secondsSinceEatenCherry = 0;

    private boolean isAlive = true;

    // public ArrayList<ArrayList<Integer>> ghostsPos = new ArrayList<>();

    private ArrayList<Character> characters = new ArrayList<>();

    private Tile[][] board;

    // brettet blir initialisert hver gang et Game lages
    public Game(int numXTiles, int numYTiles) {
        this.numXTiles = numXTiles;
        this.numYTiles = numYTiles;

        initialize(numXTiles, numYTiles);
    }

    private void initialize(int numXTiles, int numYTiles) {
        // setter posisjonen til pacman
        pacManPos.add(pacManStartX);
        pacManPos.add(pacManStartY);

        // setter den siste posisjonen pacman var på
        // (dette er for å fjerne pacman der han var sist så ikke det blir flere pacmans
        // på en gang)
        lastPos.add(pacManStartX);
        lastPos.add(pacManStartY);

        // ArrayList<Integer> ghostPos = new ArrayList<>();
        // ghostPos.add(0);
        // ghostPos.add(0);
        // ghostsPos.add(ghostPos);

        // lager brettet med tomme Tiles
        board = new Tile[numYTiles][numXTiles];
        for (int y = 0; y < numYTiles; y++) {
            for (int x = 0; x < numXTiles; x++) {
                board[y][x] = new Tile();
            }
        }

        // Character character = new Character(new int[] { 0, 0 }, "purpleGhost", this);
        // characters.add(character);

        // tegner hele mappen på nytt
        placeMap();
        // plasserer pacman som sjekker om pacman har rørt et spøkelse eller noe annet
        placePacMan();
    }

    public void placeMap() {
        // går igjennom hele brettet
        for (int y = 1; y < numYTiles - 1; y++) {
            // for hver femte rad på spillebrettet skal det lages et nytt spøkelse i den
            // siste Tilen på brettet
            if (y % 5 == 0) {
                Character character = new Character(new int[] { numYTiles - 2, numXTiles - 2 });
                characters.add(character);
            }
            for (int x = 1; x < numXTiles - 1; x++) {
                // setter korridorer pacman kan bevege seg på

                // den første og siste kolonnen skal være korridorer samt hver femte kolonne
                // alt inni her kjører derfor bare hvis det skal være en korridor
                if (x == 1 || (x % 5 == 0 && x < numXTiles - 3) || x == numXTiles - 2) {
                    // setter den nederste Tilen i midten (derfor delt på to) til en cherry
                    // vil denne alltid kjøre?
                    if (x == numXTiles / 2 && y == numYTiles - 2)
                        board[y][x].setCherry(true);
                    else if ((x + y) % 2 == 0) // gjør annenhver Tile til coin
                        board[y][x].setCoin(true);
                    if (y == 1 || y % 6 == 0 || y == numYTiles - 2) // setter første og siste
                        board[y][x].setCorner(true);
                    else
                        board[y][x].setCorridor(true); // hvis ikke Tilen er et hjørne, men vi er forstatt i denne for
                                                       // loopen skal det være en vanlig korridor
                }

                // den første og siste raden skal være korridorer
                // hver sjette rad skal også være korridor
                if (y == 1 || y % 6 == 0 || y == numYTiles - 2) {
                    // setter det til en korridor (hjørner er allerede satt)
                    board[y][x].setCorridor(true);

                    // må sette coins på nytt, denne gangen for rader
                    if ((x + y) % 2 == 0 && !board[y][x].isCherry())
                        board[y][x].setCoin(true);
                }
            }
        }
    }

    // returnerer true hvis det fortsatt finnes coins
    // (brukes for å vite om spillet er over)
    public boolean areCoinsLeft() {
        for (int y = 0; y < numYTiles; y++) {
            for (int x = 0; x < numXTiles; x++) {
                if (board[y][x].isCoin())
                    return true;
            }
        }
        return false;
    }

    // setter antall coins i spillet
    // (brukes for å skrive antall coins ut fra fil fra PacManHandler
    public void setCoins(int coins) {
        this.coins = coins;
    }

    // en funksjon som beveger alt (bytter på annenhver pacman og spøkelse)
    public void moveAll(String direction) {
        // hvis pacman rører et spøkelse uten å ha cherry-powerup dør han
        if (board[pacManPos.get(0)][pacManPos.get(1)].isGhost() && secondsSinceEatenCherry == 0) {
            isAlive = false;
        }
        // ellers, hvis pacman har cherry-powerup når han rører et spøkelse skal
        // alle det legges til 10 coins og spøkelse som er på samme posisjon som pacman
        // skal dø
        else if (board[pacManPos.get(0)][pacManPos.get(1)].isGhost() && secondsSinceEatenCherry != 0) {
            coins += 10;
            // looper over alle spøkelser og finner hvem som er på posisjonen til pacman
            for (Character character : characters) {
                if (character.getPosition()[0] == pacManPos.get(0) &&
                        character.getPosition()[1] == pacManPos.get(1)) {
                    // fjerner spøkelse
                    characters.remove(character);
                    // slutter å tegne spøkelse
                    board[pacManPos.get(0)][pacManPos.get(1)].setGhost(false);
                    return;
                }
            }
        }
        // hvis pacman er på en cherry skal det være 50
        // FRAMES (ikke sekunder (kan byttes hvis du vil)) før han mister cherry-powerup
        if (board[pacManPos.get(0)][pacManPos.get(1)].isCherry()) {
            board[pacManPos.get(0)][pacManPos.get(1)].setCherry(false);
            secondsSinceEatenCherry = 50;
        }
        // bever pacman hvis det er hans tur og spøkelser ellers
        if (pacManToMove)
            movePacMan(direction);
        else
            moveGhosts();

        // sier at neste gang er det motsatt folk som beveger seg
        pacManToMove = !pacManToMove;
    }

    public void moveGhosts() {
        // hvis det er mer enn null sekunder siden pacman spiste cherry skal spøkelser
        // være darkblue. Ellers tegnes de vanlige grønne

        if (secondsSinceEatenCherry != 0) {
            secondsSinceEatenCherry--;
            Character.setColor(Color.DARKBLUE);
        } else
            Character.setColor(Color.GREEN);

        // looper over alle karakterene
        // hvis de rører et hjørne så sjekkes det hvilke retninger det er korridorer
        // hvis det er en korridor til høyre for hjørne f.eks. vil dette legges til i et
        // array av mulige steder spøkelsene kan bevege seg
        // det velges da en tilfeldig retning av disse
        for (Character character : characters) {
            if (board[character.getPosition()[0]][character.getPosition()[1]].isCorner()) {
                // lager liste med strings av mulige retninger
                // (retninger legges bare til hvis de er mulige)
                ArrayList<String> possibleDirections = new ArrayList<>();
                if (board[character.getPosition()[0] - 1][character.getPosition()[1]].isCorridor())
                    possibleDirections.add("up");
                if (board[character.getPosition()[0] + 1][character.getPosition()[1]].isCorridor())
                    possibleDirections.add("down");
                if (board[character.getPosition()[0]][character.getPosition()[1] - 1].isCorridor())
                    possibleDirections.add("left");
                if (board[character.getPosition()[0]][character.getPosition()[1] + 1].isCorridor())
                    possibleDirections.add("right");

                // setter spøkelse til å ha tilfeldig retning
                Random rand = new Random();
                String randomDirection = possibleDirections.get(rand.nextInt(possibleDirections.size()));
                character.setDirection(randomDirection);
            }

            // fjerner spøkelse fra den forrige Tilen den var på
            board[character.getPosition()[0]][character.getPosition()[1]].setGhost(false);
            // henter ut gamle posisjon
            int[] newPosition = character.getPosition();
            // legger til bevegelse til den gamle posisjonen
            switch (character.getDirection()) {
                case "up":
                    newPosition[0] = character.getPosition()[0] - 1;
                    break;
                case "down":
                    newPosition[0] = character.getPosition()[0] + 1;
                    break;
                case "left":
                    newPosition[1] = character.getPosition()[1] - 1;
                    break;
                case "right":
                    newPosition[1] = character.getPosition()[1] + 1;
                    break;
            }
            // setter den nye posisjonen etter bevegelsen
            character.setPosition(newPosition);
            board[character.getPosition()[0]][character.getPosition()[1]].setGhost(true);
        }
    }

    public void placePacMan() {
        // hvis ikke det er en korridor der pacman skal bli plassert vil det komme en
        // error
        // dette gjør at try/catchen som kjøres går til catch delen som gjør at pacman
        // blir satt tilbake til sin gamle posisjon (han beveger seg ikke)
        if (!board[pacManPos.get(0)][pacManPos.get(1)].isCorridor())
            throw new IllegalArgumentException("Prøvde å bevege seg utenfor brettet");
        else if (board[pacManPos.get(0)][pacManPos.get(1)].isCoin()) {
            board[pacManPos.get(0)][pacManPos.get(1)].setCoin(false);
            coins++;
            if (!areCoinsLeft())
                System.out.println("W");
        }
        // fjerner pacman fra der han var før
        board[lastPos.get(0)][lastPos.get(1)].setPacMan(false);
        // legger til pacman der han er nå
        board[pacManPos.get(0)][pacManPos.get(1)].setPacMan(true);
    }

    public Tile getTile(int xPos, int yPos) {
        return board[yPos][xPos];
    }

    public int getCoins() {
        return coins;
    }

    public ArrayList<Integer> getPacManPos() {
        return pacManPos;
    }

    public boolean getIsAlive() {
        return isAlive;
    }

    public Tile[][] getBoard() {
        return board;
    }

    // DENNE FUNKSJONEN ER DEN SOM JEG FREM TIL NÅ HAR PRØVD Å BRUKE TIL Å SKRIVE
    // TIL FIL
    // DET KAN HENDE AT Å BYTTE BRETT IKKE ER DEN BESTE MÅTEN GJØRE DET PÅ
    public void setBoard(Tile[][] board) {
        characters.clear();
        this.board = board;
    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(ArrayList<Character> characters) {
        this.characters = characters;
    }

    public String getLastDirection() {
        return lastDirection;
    }

    public void setLastDirection(String lastDirection) {
        this.lastDirection = lastDirection;
    }

    public int getSecondsSinceEatenCherry() {
        return secondsSinceEatenCherry;
    }

    public void setSecondsSinceEatenCherry(int secondsSinceEatenCherry) {
        this.secondsSinceEatenCherry = secondsSinceEatenCherry;
    }

    public void movePacMan(String direction) {
        // lagrer den nåværende posisjonen til pacman
        lastPos.set(0, pacManPos.get(0));
        lastPos.set(1, pacManPos.get(1));
        lastDirection = direction;

        try {
            switch (direction) {
                case "up":
                    pacManPos.set(0, pacManPos.get(0) - 1);
                    break;
                case "down":
                    pacManPos.set(0, pacManPos.get(0) + 1);
                    break;
                case "left":
                    pacManPos.set(1, pacManPos.get(1) - 1);
                    break;
                case "right":
                    pacManPos.set(1, pacManPos.get(1) + 1);
                    break;
            }
            placePacMan();
        } catch (Exception e) {
            pacManPos.set(0, lastPos.get(0));
            pacManPos.set(1, lastPos.get(1));
            direction = lastDirection;
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setPacManPos(int currentRow, int currentColumn) {
        if (currentRow > numXTiles || currentColumn > numYTiles || currentRow < 0 || currentColumn < 0) 
            throw new IllegalArgumentException();

        if (!getTile(currentRow, currentColumn).isCorridor()) {
            throw new IllegalArgumentException();
        }

        pacManPos.set(0, currentColumn);
        pacManPos.set(1, currentRow);
    }

    // public void save() throws IOException {
    // BufferedWriter writer = new BufferedWriter(new FileWriter("Fil.txt"));
    // writer.write("to", 5, 10);
    // writer.close();
    // }

    // public Game load() throws IOException {
    // BufferedReader reader = new BufferedReader(new FileReader("Fil.txt"));
    // String currentLine = reader.readLine();
    // reader.close();
    // return new Game(0, 0);
    // }
}
