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

    @FXML
    public void initialize() {
        this.game = new Game(numXTiles, numYTiles);
        updateBoard();

        final long startNanoTime = System.nanoTime();

        new AnimationTimer() {
            double timePassed = 0;

            public void handle(long currentNanoTime) {
                double t = ((currentNanoTime - startNanoTime) / 1000000000.0) - timePassed;
                if (t > 1) {
                    game.moveAll(direction);
                    timePassed += 0.08;
                    updateBoard();
                }
            }
        }.start();
    }

    public void updateBoard() {
        board.getChildren().clear();
        score.setText(Integer.toString(game.getCoins()));
        for (int y = 0; y < numYTiles; y++) {
            for (int x = 0; x < numXTiles; x++) {
                Pane newPane = new Pane();
                Color color = Color.BLACK;
                if (!(game.getTile(x, y).isPacMan() && game.getTile(x, y).isGhost())) {
                    if (game.getTile(x, y).isPacMan()) {
                        color = Color.YELLOW;
                    } else if (game.getTile(x, y).isGhost()) {
                        color = Character.getColor();
                    } else if (game.getTile(x, y).isCherry()) {
                        color = Color.RED;
                    } else if (game.getTile(x, y).isCorridor()) {
                        color = Color.BLUE;
                    } else
                        continue;
                }

                newPane.setBackground(
                        new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
                newPane.setTranslateX(x * 20);
                newPane.setTranslateY(y * 20);
                newPane.setPrefSize(20, 20);
                board.getChildren().add(newPane);

                if (game.getTile(x, y).isCoin()) {
                    Pane coinPane = new Pane();
                    Color coinColor = Color.ORANGE;
                    coinPane.setBackground(
                            new Background(new BackgroundFill(coinColor, CornerRadii.EMPTY, Insets.EMPTY)));
                    coinPane.setTranslateX(x * 20 + 5);
                    coinPane.setTranslateY(y * 20 + 5);
                    coinPane.setPrefSize(10, 10);
                    board.getChildren().add(coinPane);
                }
            }
        }

    }

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