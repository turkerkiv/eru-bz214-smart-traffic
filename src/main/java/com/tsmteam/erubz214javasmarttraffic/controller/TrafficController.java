package com.tsmteam.erubz214javasmarttraffic.controller;

import com.tsmteam.erubz214javasmarttraffic.model.AnimationLoop;
import com.tsmteam.erubz214javasmarttraffic.model.CycleManager;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

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
        CycleManager.initNewCycle(new Rectangle[]{northRoad, eastRoad, southRoad, westRoad}, new int[]{1, 3, 4, 2}, carsPane);
        AnimationLoop animationLoop = new AnimationLoop();
        animationLoop.start();
    }
}
