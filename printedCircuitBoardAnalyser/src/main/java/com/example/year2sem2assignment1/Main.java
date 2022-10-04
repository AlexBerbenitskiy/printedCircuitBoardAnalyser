package com.example.year2sem2assignment1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        /*
        scene.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent event) {
             double xCo = (event.getScreenX());
             double yCo = (event.getScreenY());
            }
        });

         */
    }




    public static void main(String[] args) {
        launch();
    }
}