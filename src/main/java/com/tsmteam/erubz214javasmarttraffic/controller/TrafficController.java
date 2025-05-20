package com.tsmteam.erubz214javasmarttraffic.controller;

import com.tsmteam.erubz214javasmarttraffic.model.AnimationLoop;
import com.tsmteam.erubz214javasmarttraffic.model.CycleManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.TextField;

import java.awt.*;

public class TrafficController {

    @FXML
    private Rectangle northRoad;
    @FXML
    private Rectangle eastRoad;
    @FXML
    private Rectangle southRoad;
    @FXML
    private Rectangle westRoad;
    @FXML
    private TextField northInput;
    @FXML
    private Button startButton;

    @FXML
    private Pane carsPane;
    @FXML
    private Pane lightsPane;

    public void initialize() {
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int nInput = Integer.parseInt(northInput.getText());

                CycleManager.initNewCycle(new Rectangle[]{northRoad, eastRoad, southRoad, westRoad}, new int[]{nInput, 25, 20, 15}, carsPane);
                AnimationLoop animationLoop = new AnimationLoop();
                animationLoop.start();
            }
        });
    }
}
