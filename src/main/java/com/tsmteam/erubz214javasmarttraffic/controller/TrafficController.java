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
    private TextField eastInput;
    @FXML
    private TextField southInput;
    @FXML
    private TextField westInput;
    @FXML
    private Pane carsPane;
    @FXML
    private Pane lightsPane;

    AnimationLoop _animationLoop = new AnimationLoop();

    public void initialize() {
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                _animationLoop.stop();

                int nInput = Integer.parseInt(northInput.getText());
                int eInput = Integer.parseInt(eastInput.getText());
                int sInput = Integer.parseInt(southInput.getText());
                int wInput = Integer.parseInt(westInput.getText());

                CycleManager.initNewCycle(new Rectangle[]{northRoad, eastRoad, southRoad, westRoad}, new int[]{nInput, eInput, sInput, wInput}, carsPane);

                _animationLoop.start();
                startButton.setDisable(true);
            }
        });
    }
}