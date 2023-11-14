package kz.nik.myeditor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Editor extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../game_editor.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Guardian: The Game Editor");
        primaryStage.setScene(new Scene(root, 1280, 900));
        primaryStage.show();
    }
}
