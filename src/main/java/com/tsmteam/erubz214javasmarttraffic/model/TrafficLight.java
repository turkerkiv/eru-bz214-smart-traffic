package com.tsmteam.erubz214javasmarttraffic.model;

import com.tsmteam.erubz214javasmarttraffic.enums.Direction;
import com.tsmteam.erubz214javasmarttraffic.enums.LightState;
import javafx.scene.shape.Rectangle;

import java.util.*;

public class TrafficLight {
    private LightState _currentLightState;
    private Dictionary<LightState, String> _lightImages;
    private Direction _location;
    private double _redLightDuration;
    private double _yellowLightDuration;
    private double _greenLightDuration;
    private Road _road;
    private double _lightLastChangeTime;

    public TrafficLight(Direction location, Rectangle road) {
        _location = location;
        _currentLightState = LightState.RED;
        _road = new Road(this, road);
        // _lightImages = new Dictionary<LightState, String>();
    }

    private void changeLightImage() {

    }

    public void runFrame(double now) {
        if (_road.getVehicleCountInLine() == 0) return;

        switch (_currentLightState) {
            case RED -> {
                double elapsedSecondsInRed = (now - _lightLastChangeTime) / 1_000_000_000.0;
                if (elapsedSecondsInRed > _redLightDuration) {
                    _currentLightState = LightState.YELLOW;
                    _lightLastChangeTime = System.nanoTime();
                    changeLightImage();
                    System.out.println(_location + " now yellow");
                }
            }
            case YELLOW -> {
                double elapsedSecondsInYellow = (now - _lightLastChangeTime) / 1_000_000_000.0;
                if (elapsedSecondsInYellow > _yellowLightDuration) {
                    _lightLastChangeTime = System.nanoTime();
                    _currentLightState = LightState.GREEN;
                    changeLightImage();

                    // makes vehicles change state to MOVING
                    _road.changeVehicleStatesDelayed();
                    System.out.println(_location + " now green");
                }
            }
            case GREEN -> {
                double elapsedSecondsInGreen = (now - _lightLastChangeTime) / 1_000_000_000.0;
                if (elapsedSecondsInGreen > _greenLightDuration) {
                    _lightLastChangeTime = System.nanoTime();
                    _currentLightState = LightState.RED;
                    changeLightImage();

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

    public Road getRoad(){
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