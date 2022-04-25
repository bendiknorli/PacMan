package pacman;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.paint.Color;

public class Game {
    private int numXTiles, numYTiles;

    private boolean pacManToMove; // om det er pacman som beveger seg denne gangen eller spøkelsene

    private int coins = 0;

    private int framesSinceEatenCherry = 0;

    private boolean isAlive = true;

    private ArrayList<Ghost> ghosts = new ArrayList<>();

    private Tile[][] board;

    private PacMan pacMan;

    // brettet blir initialisert hver gang et Game lages
    public Game(int numXTiles, int numYTiles) {
        this.numXTiles = numXTiles;
        this.numYTiles = numYTiles;

        initialize(numXTiles, numYTiles);
    }

    private void initialize(int numXTiles, int numYTiles) {
        // setter posisjonen til pacman
        pacMan = new PacMan(new int[] { 1, 1 });
        pacMan.setPacManStartPosition();

        // setter den siste posisjonen pacman var på
        // (dette er for å fjerne pacman der han var sist så ikke det blir flere pacmans
        // på en gang)
        pacMan.setLastPos(pacMan.getStartPos());

        // lager brettet med tomme Tiles
        board = new Tile[numYTiles][numXTiles];
        for (int y = 0; y < numYTiles; y++) {
            for (int x = 0; x < numXTiles; x++) {
                board[y][x] = new Tile();
            }
        }

        // tegner hele mappen på nytt
        placeMap();
        // plasserer pacman som sjekker om pacman har rørt et spøkelse eller noe annet
        placePacMan();
    }

    private void placeMap() {
        // går igjennom hele brettet
        for (int y = 1; y < numYTiles - 1; y++) {
            // for hver femte rad på spillebrettet skal det lages et nytt spøkelse i den
            // siste Tilen på brettet
            if (y % 5 == 0) {
                Ghost ghost = new Ghost(new int[] { numYTiles - 2, numXTiles - 2 });
                ghosts.add(ghost);
                board[ghost.getPosition()[0]][ghost.getPosition()[1]].setGhost(true);
            }
            for (int x = 1; x < numXTiles - 1; x++) {
                // setter korridorer pacman kan bevege seg på

                // setter den nederste Tilen i midten (derfor delt på to) til en cherry
                if (x == numXTiles / 2 && y == numYTiles - 2)
                    board[y][x].setCherry(true);

                // den første og siste kolonnen skal være korridorer samt hver femte kolonne
                // alt inni her kjører derfor bare hvis det skal være en korridor
                if (x == 1 || (x % 5 == 0 && x < numXTiles - 3) || x == numXTiles - 2) {
                    if (!board[y][x].isCherry() && (x + y) % 2 == 0) // gjør annenhver Tile til coin
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
    public void setScore(int coins) {
        if (coins < 0)
            throw new IllegalArgumentException("Kan ikke ha negative score");
        this.coins = coins;
    }

    // en funksjon som beveger alt (bytter på annenhver pacman og spøkelse)
    public void moveAll(String direction) {
        // hvis pacman rører et spøkelse uten å ha cherry-powerup dør han
        if (board[pacMan.getPosition()[0]][pacMan.getPosition()[1]].isGhost() && framesSinceEatenCherry == 0) {
            isAlive = false;
        }
        // ellers, hvis pacman har cherry-powerup når han rører et spøkelse skal
        // alle det legges til 10 coins og spøkelse som er på samme posisjon som pacman
        // skal dø
        else if (board[pacMan.getPosition()[0]][pacMan.getPosition()[1]].isGhost() && framesSinceEatenCherry != 0) {
            coins += 10;
            // looper over alle spøkelser og finner hvem som er på posisjonen til pacman
            for (Ghost ghost : ghosts) {
                if (ghost.getPosition()[0] == pacMan.getPosition()[0] &&
                        ghost.getPosition()[1] == pacMan.getPosition()[1]) {
                    // fjerner spøkelse
                    ghosts.remove(ghost);
                    // slutter å tegne spøkelse
                    board[pacMan.getPosition()[0]][pacMan.getPosition()[1]].setGhost(false);
                    return;
                }
            }
        }
        // hvis pacman er på en cherry skal det være 50
        // FRAMES (ikke sekunder) før han mister cherry-powerup
        if (board[pacMan.getPosition()[0]][pacMan.getPosition()[1]].isCherry()) {
            board[pacMan.getPosition()[0]][pacMan.getPosition()[1]].setCherry(false);
            framesSinceEatenCherry = 50;
        }
        // bever pacman hvis det er hans tur og spøkelser ellers
        if (pacManToMove)
            movePacMan(direction);
        else
            moveGhosts();

        // sier at neste gang er det motsatt folk som beveger seg
        pacManToMove = !pacManToMove;
    }

    private void moveGhosts() {
        // hvis det er mer enn null sekunder siden pacman spiste cherry skal spøkelser
        // være darkblue. Ellers tegnes de vanlige grønne

        if (framesSinceEatenCherry != 0) {
            framesSinceEatenCherry--;
            Ghost.setColor(Color.DARKBLUE);
        } else
            Ghost.setColor(Color.GREEN);
        // MÅ GJØRE DETTE OM TIL AT DEN VELGER FARGEN TIL SPØKELSE
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        // looper over alle karakterene
        // hvis de rører et hjørne så sjekkes det hvilke retninger det er korridorer
        // hvis det er en korridor til høyre for hjørne f.eks. vil dette legges til i et
        // array av mulige steder spøkelsene kan bevege seg
        // det velges da en tilfeldig retning av disse
        for (Ghost ghost : ghosts) {
            if (board[ghost.getPosition()[0]][ghost.getPosition()[1]].isCorner()) {
                // lager liste med strings av mulige retninger
                // (retninger legges bare til hvis de er mulige)
                ArrayList<String> possibleDirections = new ArrayList<>();
                if (board[ghost.getPosition()[0] - 1][ghost.getPosition()[1]].isCorridor())
                    possibleDirections.add("up");
                if (board[ghost.getPosition()[0] + 1][ghost.getPosition()[1]].isCorridor())
                    possibleDirections.add("down");
                if (board[ghost.getPosition()[0]][ghost.getPosition()[1] - 1].isCorridor())
                    possibleDirections.add("left");
                if (board[ghost.getPosition()[0]][ghost.getPosition()[1] + 1].isCorridor())
                    possibleDirections.add("right");

                // setter spøkelse til å ha tilfeldig retning
                Random rand = new Random();
                String randomDirection = possibleDirections.get(rand.nextInt(possibleDirections.size()));
                ghost.setDirection(randomDirection);
            }

            // fjerner spøkelse fra den forrige Tilen den var på
            board[ghost.getPosition()[0]][ghost.getPosition()[1]].setGhost(false);
            // henter ut gamle posisjon
            int[] newPosition = ghost.getPosition();
            // legger til bevegelse til den gamle posisjonen
            switch (ghost.getDirection()) {
                case "up":
                    newPosition[0] = ghost.getPosition()[0] - 1;
                    break;
                case "down":
                    newPosition[0] = ghost.getPosition()[0] + 1;
                    break;
                case "left":
                    newPosition[1] = ghost.getPosition()[1] - 1;
                    break;
                case "right":
                    newPosition[1] = ghost.getPosition()[1] + 1;
                    break;
            }
            // setter den nye posisjonen etter bevegelsen
            ghost.setPosition(newPosition);
            board[ghost.getPosition()[0]][ghost.getPosition()[1]].setGhost(true);
        }
    }

    private void placePacMan() {
        // hvis ikke det er en korridor der pacman skal bli plassert vil det komme en
        // error
        // dette gjør at try/catchen som kjøres går til catch delen som gjør at pacman
        // blir satt tilbake til sin gamle posisjon (han beveger seg ikke)
        if (!board[pacMan.getPosition()[0]][pacMan.getPosition()[1]].isCorridor())
            throw new IllegalArgumentException("Prøvde å bevege seg utenfor brettet");
        else if (board[pacMan.getPosition()[0]][pacMan.getPosition()[1]].isCoin()) {
            board[pacMan.getPosition()[0]][pacMan.getPosition()[1]].setCoin(false);
            coins++;
            if (!areCoinsLeft())
                System.out.println("W");
        }
        // fjerner pacman fra der han var før
        board[pacMan.getLastPos()[0]][pacMan.getLastPos()[1]].setPacMan(false);
        // legger til pacman der han er nå
        board[pacMan.getPosition()[0]][pacMan.getPosition()[1]].setPacMan(true);
    }

    public Tile getTile(int xPos, int yPos) {
        return board[yPos][xPos];
    }

    public int getScore() {
        return coins;
    }

    public Tile[][] getBoard() {
        return board;
    }

    // brukes av funksjonen som skriver til fil
    // fjerner det midlertidig brettet og lager et nytt et
    public void setBoard(Tile[][] board) {
        ghosts.clear();
        this.board = board;
    }

    public ArrayList<Ghost> getGhosts() {
        return ghosts;
    }

    public void setGhosts(ArrayList<Ghost> ghosts) {
        this.ghosts = ghosts;
    }

    public PacMan getPacMan() {
        return pacMan;
    }

    // public String getLastDirection() {
    // return lastDirection;
    // }

    // public void setLastDirection(String lastDirection) {
    // this.lastDirection = lastDirection;
    // }

    public int getFramesSinceEatenCherry() {
        return framesSinceEatenCherry;
    }

    public void setFramesSinceEatenCherry(int framesSinceEatenCherry) {
        this.framesSinceEatenCherry = framesSinceEatenCherry;
    }

    public void movePacMan(String direction) {
        // lagrer den nåværende posisjonen til pacman
        pacMan.setLastPos(pacMan.position);
        pacMan.setLastDirection(direction);

        try {
            switch (direction) {
                case "up":
                    pacMan.setPosition(new int[] { pacMan.getPosition()[0] - 1, pacMan.getPosition()[1] });
                    break;
                case "down":
                    pacMan.setPosition(new int[] { pacMan.getPosition()[0] + 1, pacMan.getPosition()[1] });
                    break;
                case "left":
                    pacMan.setPosition(new int[] { pacMan.getPosition()[0], pacMan.getPosition()[1] - 1 });
                    break;
                case "right":
                    pacMan.setPosition(new int[] { pacMan.getPosition()[0], pacMan.getPosition()[1] + 1 });
                    break;
            }
            placePacMan();
        } catch (Exception e) {
            pacMan.setPosition(pacMan.getLastPos());
            pacMan.setDirection(pacMan.getLastDirection());
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setPacManPos(int currentRow, int currentColumn) {
        pacMan.setPosition(new int[] { currentColumn, currentRow });
    }

    public ArrayList<Ghost> getPacManPos() {
        return null;
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
