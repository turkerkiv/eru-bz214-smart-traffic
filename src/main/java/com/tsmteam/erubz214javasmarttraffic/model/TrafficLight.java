package com.tsmteam.erubz214javasmarttraffic.model;

import com.tsmteam.erubz214javasmarttraffic.enums.Direction;
import com.tsmteam.erubz214javasmarttraffic.enums.LightState;
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

    // TODO can be refactored to seperate road logic and traffic light logic and also maybe road holds its traffic light, road holds vehicles and traffic light only calculates duration etc.

    public TrafficLight(Direction location) {
        _location = location;
        _currentLightState = LightState.RED;
        // _lightImages = new Dictionary<LightState, String>();
    }

    private void changeLightImage() {

    }

    public void setRoad(Road road)
    {
        _road = road;
    }


    public void run(double now) {
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

                //checks if vehicles are still in road
                _road.checkIfVehiclesStillInLine();

                if (elapsedSecondsInGreen > _greenLightDuration) {
                    _lightLastChangeTime = System.nanoTime();
                    _currentLightState = LightState.RED;
                    changeLightImage();

                    _road.changeVehicleStates();

                    // second cycle
                    _greenLightDuration = CycleManager.calculateGreenLightDuration(this);
                    _redLightDuration = CycleManager.calculateRedLightDuration(this);
                    _road.setVehicleSpawnPoints();
                    _road.placeVehiclesToPoints();
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

    public int getVehicleCountInLine()
    {
        return _road.getVehicleCountInLine();
    }

    public double getGLDuration() {
        return _greenLightDuration;
    }

    public double getRedLDuration() {
        return _redLightDuration;
    }

    public Direction getLocation() {
        return _location;
    }
}