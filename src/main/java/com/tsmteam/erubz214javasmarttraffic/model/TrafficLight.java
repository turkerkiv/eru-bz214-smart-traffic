package com.tsmteam.erubz214javasmarttraffic.model;

import com.tsmteam.erubz214javasmarttraffic.enums.Direction;
import com.tsmteam.erubz214javasmarttraffic.enums.LightState;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.*;

public class TrafficLight {
    private LightState _currentLightState;
    private Map<LightState, ImageView> _lightImages;
    private Direction _location;
    private double _redLightDuration;
    private double _yellowLightDuration;
    private double _greenLightDuration;
    private Road _road;
    private double _lightLastChangeTime;
    private Pane _trafficLightPane;

    public TrafficLight(Direction location, Rectangle road, Pane trafficLightPane) {
        _location = location;
        _currentLightState = LightState.RED;
        _road = new Road(this, road);
        _trafficLightPane = trafficLightPane;

        _lightImages = new HashMap<LightState, ImageView>();
        Image redLightImage = new Image(VehicleCreator.class.getResourceAsStream("/com/tsmteam/erubz214javasmarttraffic/image/lights/kirmizisik.png"));

        ImageView redLightIV = new ImageView(redLightImage);
        Image greenLightImage = new Image(VehicleCreator.class.getResourceAsStream("/com/tsmteam/erubz214javasmarttraffic/image/lights/yesilisik.png"));
        ImageView greenLightIV = new ImageView(greenLightImage);
        Image yellowLightImage = new Image(VehicleCreator.class.getResourceAsStream("/com/tsmteam/erubz214javasmarttraffic/image/lights/sarisik.png"));
        ImageView yellowLightIV = new ImageView(yellowLightImage);
        _lightImages.put(LightState.RED, redLightIV);
        _lightImages.put(LightState.YELLOW, yellowLightIV);
        _lightImages.put(LightState.GREEN, greenLightIV);
        _trafficLightPane.getChildren().add(_lightImages.get(LightState.RED));
        redLightIV.setFitWidth(100);
        redLightIV.setFitHeight(100);
        greenLightIV.setFitWidth(100);
        greenLightIV.setFitHeight(100);
        yellowLightIV.setFitWidth(100);
        yellowLightIV.setFitHeight(100);
        switch (_location)
        {
            case NORTH -> {
                redLightIV.setLayoutX(295);
                redLightIV.setLayoutY(206);
                greenLightIV.setLayoutX(295);
                greenLightIV.setLayoutY(206);
                yellowLightIV.setLayoutX(295);
                yellowLightIV.setLayoutY(206);
            }
            case EAST -> {
                redLightIV.setLayoutX(448);
                redLightIV.setLayoutY(244);
                greenLightIV.setLayoutX(448);
                greenLightIV.setLayoutY(244);
                yellowLightIV.setLayoutX(448);
                yellowLightIV.setLayoutY(244);
            }
            case SOUTH -> {
                redLightIV.setLayoutX(398);
                redLightIV.setLayoutY(393);
                greenLightIV.setLayoutX(398);
                greenLightIV.setLayoutY(393);
                yellowLightIV.setLayoutX(398);
                yellowLightIV.setLayoutY(393);
            }
            case WEST -> {
                redLightIV.setLayoutX(245);
                redLightIV.setLayoutY(362);
                greenLightIV.setLayoutX(245);
                greenLightIV.setLayoutY(362);
                yellowLightIV.setLayoutX(245);
                yellowLightIV.setLayoutY(362);
            }
        }
    }

    private void changeLightImage(LightState oldLightState, LightState newLightState) {
        _trafficLightPane.getChildren().remove(_lightImages.get(oldLightState));
        _trafficLightPane.getChildren().add(_lightImages.get(newLightState));
    }

    public void runFrame(double now) {
        if (_road.getVehicleCountInLine() == 0) return;

        switch (_currentLightState) {
            case RED -> {
                double elapsedSecondsInRed = (now - _lightLastChangeTime) / 1_000_000_000.0;
                if (elapsedSecondsInRed > _redLightDuration) {
                    _currentLightState = LightState.YELLOW;
                    _lightLastChangeTime = System.nanoTime();
                    changeLightImage(LightState.RED, LightState.YELLOW);
                    System.out.println(_location + " now yellow");
                }
            }
            case YELLOW -> {
                double elapsedSecondsInYellow = (now - _lightLastChangeTime) / 1_000_000_000.0;
                if (elapsedSecondsInYellow > _yellowLightDuration) {
                    _lightLastChangeTime = System.nanoTime();
                    _currentLightState = LightState.GREEN;
                    changeLightImage(LightState.YELLOW, LightState.GREEN);

                    // makes vehicles change state to MOVING
                    _road.changeVehicleStatesDelayed();
                    System.out.println(_location + " now green");
                }
            }
            case GREEN -> {
                double elapsedSecondsInGreen = (now - _lightLastChangeTime) / 1_000_000_000.0;
//                System.out.println(elapsedSecondsInGreen);
                if (elapsedSecondsInGreen > _greenLightDuration) {
                    _lightLastChangeTime = System.nanoTime();
                    _currentLightState = LightState.RED;
                    changeLightImage(LightState.GREEN, LightState.RED);

                    _road.changeVehicleStates();

                    // second cycle
                    _greenLightDuration = CycleManager.calculateGreenLightDuration(this);
                    _redLightDuration = CycleManager.calculateRedLightDuration(this);
                    _road.setUpRoad();
                }
            }
        }
    }

    public void setGLDuration(double greenLightDuration) {
        _lightLastChangeTime = System.nanoTime();
        _greenLightDuration = greenLightDuration;
    }

    public void setRedLDuration(double redLightDuration) {
        _lightLastChangeTime = System.nanoTime();
        _redLightDuration = redLightDuration;
    }

    public Road getRoad() {
        return _road;
    }

    public double getGLDuration() {
        return _greenLightDuration;
    }

    public double getRLDuration() {
        return _redLightDuration;
    }

    public Direction getLocation() {
        return _location;
    }
}