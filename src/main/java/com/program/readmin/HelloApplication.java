package com.program.readmin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("remadmin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        //stage.setTitle("Программа удалённого администрирования");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
        //stage.getIcons().add(new Image("C:\\RemAdmin\\pc.png"));
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}