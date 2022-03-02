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

public class PacManController {
    @FXML
    Pane board;

    private Game game;

    private int numXTiles = 10, numYTiles = 10;

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
                    timePassed += 0.2;
                    game.moveSnake(direction);
                    updateBoard();
                }
            }
        }.start();
    }

    public void updateBoard() {
        for (int y = 0; y < numYTiles; y++) {
            for (int x = 0; x < numXTiles; x++) {
                Pane newPane = new Pane();
                Color color = Color.LIGHTGRAY;
                if (game.getTile(x, y).isApple())
                    color = Color.RED;
                else if (game.getTile(x, y).isSnake()) {
                    color = Color.YELLOW;
                } else if ((x + y) % 2 == 0) {
                    color = Color.GRAY;
                }

                newPane.setBackground(
                        new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
                newPane.setTranslateX(x * 20);
                newPane.setTranslateY(y * 20);
                newPane.setPrefSize(20, 20);
                board.getChildren().add(newPane);
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
            default:
                break;
        }
        keyEvent.consume();
    }
}