package pacman;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
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

    private int numXTiles = 20, numYTiles = 20;

    private String direction = "right";

    // initialize betyr at koden kjører på start
    @FXML
    public void initialize() {
        // lager et gameobjekt og tegner brettet
        this.game = new Game(numXTiles, numYTiles);
        updateBoard();

        final long startNanoTime = System.nanoTime();

        // denne kjører hver frame
        new AnimationTimer() {
            double timePassed = 0;

            public void handle(long currentNanoTime) {
                // denne teller antall nanosekunder fra start og minuser antall sekunder
                // siden forrige gang man bevegde karakterer
                double t = ((currentNanoTime - startNanoTime) / 1000000000.0) - timePassed;
                // første gang man starter må det ha gått 1 sekund
                if (t > 1) {
                    game.moveAll(direction);
                    // sier at det er 0.08 sekunder til neste gang noen skal bevege seg
                    timePassed += 0.08;
                    // etter at man har endret karakterposisjoner tegner man brettet på nytt
                    updateBoard();
                }
            }
        }.start();
    }

    public void updateBoard() {
        // sletter hele brettet
        board.getChildren().clear();

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

    //
    @FXML
    private void keyPressed(KeyEvent keyEvent) {
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
            case K:
                game.moveGhosts();
            default:
                break;
        }
        keyEvent.consume();
    }
}