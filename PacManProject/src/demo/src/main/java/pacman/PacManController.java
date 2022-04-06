package pacman;

import java.io.FileNotFoundException;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class PacManController {
    @FXML
    Pane board;

    @FXML
    Text score;

    private Game game;

    PacManHandler pacManHandler = new PacManHandler();

    private int numXTiles, numYTiles;

    private String direction = "right";

    private AnimationTimer animationTimer;

    // initialize betyr at koden kjører på start
    @FXML
    public void initialize() throws FileNotFoundException {
        // lager et gameobjekt og tegner brettet

        this.game = pacManHandler.loadGame("Fil");
        numXTiles = game.getBoard()[0].length;
        numYTiles = game.getBoard().length;

        // numXTiles = numYTiles = 20;
        // this.game = new Game(numXTiles, numYTiles);

        updateBoard(null);
        direction = "right";

        final long startNanoTime = System.nanoTime();

        // denne kjører hver frame
        animationTimer = new AnimationTimer() {
            double timePassed = 0;

            public void handle(long currentNanoTime) {
                // denne teller antall nanosekunder fra start og minuser antall sekunder
                // siden forrige gang man bevegde karakterer
                double t = ((currentNanoTime - startNanoTime) / 1000000000.0) - timePassed;
                // første gang man starter må det ha gått 1 sekund
                if (t > 1) {
                    try {
                        game.moveAll(direction);
                        // sier at det er 0.08 sekunder til neste gang noen skal bevege seg
                        timePassed += 0.1;
                        // etter at man har endret karakterposisjoner tegner man brettet på nytt
                        updateBoard(this);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        animationTimer.start();
    }

    public void updateBoard(AnimationTimer t) {
        // sletter hele brettet
        board.getChildren().clear();

        if (!game.areCoinsLeft() && t != null) {
            System.out.println("Det er ikke noen coins");
            Alert wonGame = new Alert(AlertType.INFORMATION);
            wonGame.setTitle("Du vant!");
            wonGame.setHeaderText("Du samlet alle myntene på spillerbrettet");
            wonGame.setContentText("Trykk OK for å restarte spillet");
            wonGame.setOnHidden(evt -> {
                try {
                    initialize();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
            t.stop();
            wonGame.show();
        }

        if (!game.isAlive() && t != null) {
            System.out.println("Pacman lever ikke");
            Alert lostGame = new Alert(AlertType.INFORMATION);
            lostGame.setTitle("Du suger!");
            lostGame.setHeaderText("Du ble drept av et spøkelse");
            lostGame.setContentText("Trykk OK for å restarte spillet");
            lostGame.setOnHidden(evt -> {
                try {
                    initialize();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
            t.stop();
            lostGame.show();
        }

        // skriver hvor mange coins man har samlet
        score.setText(Integer.toString(game.getCoins()));

        // looper gjennom alle tiles fra 0,0 til numXTiles,numYTiles
        for (int y = 0; y < numYTiles; y++) {
            for (int x = 0; x < numXTiles; x++) {
                // lager en ny pane og setter fargen sort
                Pane newPane = new Pane();
                Color color = Color.BLACK;

                // velger farge basert på hva som er på den tilen
                if (!(game.getTile(x, y).isPacMan() && game.getTile(x, y).isGhost())) {
                    if (game.getTile(x, y).isPacMan()) {
                        color = Color.YELLOW;
                    } else if (game.getTile(x, y).isGhost()) {
                        color = Character.getColor();
                    } else if (game.getTile(x, y).isCherry()) {
                        color = Color.RED;
                    } else if (game.getTile(x, y).isCorridor()) {
                        color = Color.BLUE;
                    } else {
                        // continue gjør at man skipper til neste steg i for loppen
                        // dermed legger man ikke til en rute her
                        continue;
                    }
                }

                // setter fargen vi har valgt til tilen
                newPane.setBackground(
                        new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
                // setter størrelse og posisjon på Pane
                newPane.setTranslateX(x * 20);
                newPane.setTranslateY(y * 20);
                newPane.setPrefSize(20, 20);
                // legger til Pane
                board.getChildren().add(newPane);

                // hvis denne tilen har en coin skal vi legge til denne over resten
                if (game.getTile(x, y).isCoin()) {
                    // legger til på samme møte som før MEN...
                    Pane coinPane = new Pane();
                    Color coinColor = Color.ORANGE;
                    coinPane.setBackground(
                            new Background(new BackgroundFill(coinColor, CornerRadii.EMPTY, Insets.EMPTY)));
                    // størrelsen og posisjonen er annerledes
                    coinPane.setTranslateX(x * 20 + 5);
                    coinPane.setTranslateY(y * 20 + 5);
                    coinPane.setPrefSize(10, 10);
                    board.getChildren().add(coinPane);
                }
            }
        }

    }

    // gjør at piltastene velger retningen som pacman kommer til å bevege seg
    // neste gang moveAll - altså movePacMan kalles
    @FXML
    private void keyPressed(KeyEvent keyEvent) throws FileNotFoundException {
        KeyCode code = keyEvent.getCode();

        switch (code) {
            case LEFT:
                direction = "left";
                break;
            case RIGHT:
                direction = "right";
                break;
            case UP:
                direction = "up";
                break;
            case DOWN:
                direction = "down";
                break;
            case H:
                makeNewGame();
                break;
            case J:
                saveGame();
                break;
            case K:
                loadGame();
            default:
                break;
        }
        keyEvent.consume();
    }

    @FXML
    public void makeNewGame() throws FileNotFoundException {
        // lager et nytt spill
        Game newGame = new Game(20, 20);
        // lagrer det nye spillet til Fil.txt
        pacManHandler.saveGame("Fil", newGame);
        // laster inn spillet på nytt og siden spillet som er lagret er et nytt spill
        // vil et nytt spill bli laget
        loadGame();
    }

    @FXML
    private void saveGame() throws FileNotFoundException {
        // lagrer spillet til Fil.txt
        pacManHandler.saveGame("Fil", game);
        System.out.println("Lagret spill");
    }

    @FXML
    private void loadGame() throws FileNotFoundException {
        // laster inn spillet på nytt
        initialize();
        // stopper animationTimer for å ikke ha to animationTimere som kjører på en gang
        animationTimer.stop();
    }
}