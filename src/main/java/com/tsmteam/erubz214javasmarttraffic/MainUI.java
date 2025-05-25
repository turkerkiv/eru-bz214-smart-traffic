package com.tsmteam.erubz214javasmarttraffic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MainUI extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        URL fxmlLocation = MainUI.class.getResource("odev.fxml");
        System.out.println(fxmlLocation);
        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(loader.load(), 690, 704);

        stage.setTitle("Smart Traffic UI with Scene Builder");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
