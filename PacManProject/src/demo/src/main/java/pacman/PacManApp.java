package pacman;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PacManApp extends Application {
    @Override
    public void start(final Stage primaryStage) throws Exception {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("PacMan.fxml")));
        primaryStage.setTitle("PacMan");
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.getRoot().requestFocus();

    }

    public static void main(final String[] args) {
        Application.launch(args);
    }
}
