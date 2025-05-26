package com.tsmteam.erubz214javasmarttraffic.controller;

import com.tsmteam.erubz214javasmarttraffic.model.AnimationLoop;
import com.tsmteam.erubz214javasmarttraffic.model.CycleManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

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
    private Button startButton;
    @FXML
    private Button pauseButton;
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
    private Text westTimer;
    @FXML
    private Text northTimer;
    @FXML
    private Text eastTimer;
    @FXML
    private Text southTimer;
    @FXML
    private Text cycleTimer;
    @FXML
    private Pane carsPane;
    @FXML
    private Pane lightsPane;
    @FXML
    private Group transitionRectangles;
    @FXML
    private Text transitionTimer;
    @FXML
    private Tab randomTab;


    AnimationLoop _animationLoop = new AnimationLoop();
    private boolean _isRandom = false;

    public void initialize() {
        randomTab.setOnSelectionChanged(e -> {
            _isRandom = randomTab.isSelected();
        });

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                _animationLoop.stop();
                String rInputText = randomUpperLimitInput.getText();
                int[] carCounts;
                if (_isRandom) {
                    int rInput = Integer.parseInt(rInputText);
                    Random rand = new Random();
                    int northCount = rand.nextInt(rInput + 1);
                    int eastCount = rand.nextInt(rInput - northCount + 1);
                    int southCount = rand.nextInt(rInput - northCount - eastCount + 1);
                    int westCount = rand.nextInt(rInput - northCount - eastCount - southCount + 1);
                    carCounts = new int[]{northCount, eastCount, southCount, westCount};
                } else {
                    int nInput = 0;
                    int eInput = 0;
                    int sInput = 0;
                    int wInput = 0;
                    if (!northInput.getText().isEmpty())
                        nInput = Integer.parseInt(northInput.getText());
                    if (!eastInput.getText().isEmpty())
                        eInput = Integer.parseInt(eastInput.getText());
                    if (!southInput.getText().isEmpty())
                        sInput = Integer.parseInt(southInput.getText());
                    if (!westInput.getText().isEmpty())
                        wInput = Integer.parseInt(westInput.getText());
                    carCounts = new int[]{nInput, eInput, sInput, wInput};
                }

                CycleManager.initNewCycle(new Rectangle[]{northRoad, eastRoad, southRoad, westRoad}, carCounts, new Text[]{northTimer, eastTimer, southTimer, westTimer}, cycleTimer, carsPane, lightsPane);

                _animationLoop.start();
                pauseButton.setDisable(false);
                resetButton.setDisable(false);
                startButton.setDisable(true);
                northInput.setDisable(true);
                eastInput.setDisable(true);
                westInput.setDisable(true);
                southInput.setDisable(true);
                randomUpperLimitInput.setDisable(true);

                startButton.toFront();
                resetButton.toFront();
                pauseButton.toFront();
            }
        });
        resetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                CycleManager.resetCycle();
                startButton.setDisable(false);
                northInput.setDisable(false);
                eastInput.setDisable(false);
                westInput.setDisable(false);
                southInput.setDisable(false);
                randomUpperLimitInput.setDisable(false);
                pauseButton.setDisable(true);
                resetButton.setDisable(true);
            }
        });
        pauseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                CycleManager.togglePauseCycle(_animationLoop, pauseButton);
            }
        });
    }
}