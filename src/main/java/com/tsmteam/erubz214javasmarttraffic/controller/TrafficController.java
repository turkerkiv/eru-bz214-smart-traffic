package com.tsmteam.erubz214javasmarttraffic.controller;

import com.tsmteam.erubz214javasmarttraffic.model.CycleManager;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.animation.AnimationTimer;

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
    private Pane carsPane;
    @FXML
    private Pane lightsPane;

    public void initialize() {
        CycleManager.createNewCycle(northRoad, eastRoad, southRoad, westRoad, new int[]{1, 3, 4, 2}, carsPane);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
            }
        };
        timer.start();
    }
}
