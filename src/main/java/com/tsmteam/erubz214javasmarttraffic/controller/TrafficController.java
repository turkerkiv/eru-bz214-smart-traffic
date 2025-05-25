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
import java.util.Random;

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
    private Rectangle northRoadLight;
    @FXML
    private Rectangle eastRoadLight;
    @FXML
    private Rectangle southRoadLight;
    @FXML
    private Rectangle westRoadLight;
    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button resetButton;
    @FXML
    private TextField northInput;
    @FXML
    private TextField eastInput;
    @FXML
    private TextField southInput;
    @FXML
    private TextField westInput;
    @FXML
    private TextField randomUpperLimitInput;
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
                String rInputText = randomUpperLimitInput.getText();
                boolean isRandom = !rInputText.isEmpty();
                int[] carCounts;
                if (isRandom) {
                    int rInput = Integer.parseInt(rInputText);
                    Random rand = new Random();
                    int northCount= rand.nextInt(rInput+1);
                    int eastCount= rand.nextInt(rInput-northCount+1);
                    int southCount= rand.nextInt(rInput-northCount-eastCount+1);
                    int westCount= rand.nextInt(rInput-northCount-eastCount-southCount+1);
                    carCounts = new int[]{northCount,eastCount,southCount,westCount};
                }
                else {
                    int nInput = Integer.parseInt(northInput.getText());
                    int eInput = Integer.parseInt(eastInput.getText());
                    int sInput = Integer.parseInt(southInput.getText());
                    int wInput = Integer.parseInt(westInput.getText());
                   carCounts= new int[]{nInput, eInput, sInput, wInput};
                }

                CycleManager.initNewCycle(new Rectangle[]{northRoad, eastRoad, southRoad, westRoad},carCounts, carsPane);

                _animationLoop.start();
                startButton.setDisable(true);
            }
        });


    }
}