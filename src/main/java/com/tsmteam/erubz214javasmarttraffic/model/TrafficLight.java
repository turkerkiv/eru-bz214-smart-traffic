package com.tsmteam.erubz214javasmarttraffic.model;

import com.tsmteam.erubz214javasmarttraffic.enums.Direction;
import com.tsmteam.erubz214javasmarttraffic.enums.LightState;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

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
    private Text _timerUI;
    private ImageView _currentUiImage;

    public TrafficLight(Direction location, Rectangle road, Text timerUI, Pane trafficLightPane, double yellowLightDuration) {
        _location = location;
        _currentLightState = LightState.RED;
        _road = new Road(this, road);
        _trafficLightPane = trafficLightPane;
        _yellowLightDuration = yellowLightDuration;
        _timerUI = timerUI;

        _lightImages = getLightImagesFromFiles();
    }

    private HashMap<LightState, ImageView> getLightImagesFromFiles()
    {
        HashMap<LightState, ImageView> lightImages = new HashMap<>();
        Image redLightImage = new Image(VehicleCreator.class.getResourceAsStream("/com/tsmteam/erubz214javasmarttraffic/image/lights/kirmizisik.png"));
        ImageView redLightIV = new ImageView(redLightImage);
        Image greenLightImage = new Image(VehicleCreator.class.getResourceAsStream("/com/tsmteam/erubz214javasmarttraffic/image/lights/yesilisik.png"));
        ImageView greenLightIV = new ImageView(greenLightImage);
        Image yellowLightImage = new Image(VehicleCreator.class.getResourceAsStream("/com/tsmteam/erubz214javasmarttraffic/image/lights/sarisik.png"));
        ImageView yellowLightIV = new ImageView(yellowLightImage);

        lightImages.put(LightState.RED, redLightIV);
        lightImages.put(LightState.YELLOW, yellowLightIV);
        lightImages.put(LightState.GREEN, greenLightIV);

        _trafficLightPane.getChildren().add(redLightIV);
        _currentUiImage = redLightIV;

       setLightsUiRepresentation(redLightIV, greenLightIV, yellowLightIV);

       return lightImages;
    }

    private void setLightsUiRepresentation(ImageView redLightIV, ImageView greenLightIV, ImageView yellowLightIV)
    {
        redLightIV.setFitWidth(80);
        redLightIV.setFitHeight(80);
        greenLightIV.setFitWidth(80);
        greenLightIV.setFitHeight(80);
        yellowLightIV.setFitWidth(80);
        yellowLightIV.setFitHeight(80);

        switch (_location) {
            case NORTH -> {
                redLightIV.setLayoutX(309);
                redLightIV.setLayoutY(229);
                greenLightIV.setLayoutX(309);
                greenLightIV.setLayoutY(229);
                yellowLightIV.setLayoutX(309);
                yellowLightIV.setLayoutY(229);
            }
            case EAST -> {
                redLightIV.setLayoutX(446);
                redLightIV.setLayoutY(252);
                greenLightIV.setLayoutX(446);
                greenLightIV.setLayoutY(252);
                yellowLightIV.setLayoutX(446);
                yellowLightIV.setLayoutY(252);
            }
            case SOUTH -> {
                redLightIV.setLayoutX(420);
                redLightIV.setLayoutY(380);
                greenLightIV.setLayoutX(420);
                greenLightIV.setLayoutY(380);
                yellowLightIV.setLayoutX(420);
                yellowLightIV.setLayoutY(380);
            }
            case WEST -> {
                redLightIV.setLayoutX(290);
                redLightIV.setLayoutY(360);
                greenLightIV.setLayoutX(290);
                greenLightIV.setLayoutY(360);
                yellowLightIV.setLayoutX(290);
                yellowLightIV.setLayoutY(360);
            }
        }
    }

    private void changeLightImage(LightState oldLightState, LightState newLightState) {
        _trafficLightPane.getChildren().remove(_lightImages.get(oldLightState));
        _trafficLightPane.getChildren().add(_lightImages.get(newLightState));

        _currentUiImage = _lightImages.get(newLightState);
    }

    public ImageView getUiImage()
    {
        return _currentUiImage;
    }

    public void runFrame(double now) {
        switch (_currentLightState) {
            case RED -> {
                double elapsedSecondsInRed = (now - _lightLastChangeTime) / 1_000_000_000.0;

                _timerUI.setText(String.valueOf(Math.round(_redLightDuration - elapsedSecondsInRed)));

                if (elapsedSecondsInRed > _redLightDuration) {
                    _currentLightState = LightState.YELLOW;
                    _lightLastChangeTime = System.nanoTime();
                    changeLightImage(LightState.RED, LightState.YELLOW);
                    System.out.println(_location + " now yellow");
                }
            }
            case YELLOW -> {
                double elapsedSecondsInYellow = (now - _lightLastChangeTime) / 1_000_000_000.0;

                _timerUI.setText(String.valueOf(Math.round(_yellowLightDuration - elapsedSecondsInYellow)));

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

                _timerUI.setText(String.valueOf(Math.round(_greenLightDuration - elapsedSecondsInGreen)));

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

    public Direction getLocation() {
        return _location;
    }
}