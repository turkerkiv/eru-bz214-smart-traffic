package main.java.com.tsmteam.model;

import main.java.com.tsmteam.enums.Direction;
import main.java.com.tsmteam.enums.LightState;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

public class TrafficLight {
    private List<Vehicle> _vehiclesInLine;
    private int _greenLightDuration;
    private LightState _currentLightState;
    private Dictionary<LightState, String> _lightImages;

    public TrafficLight() {
        _vehiclesInLine = new ArrayList<>();
        // _lightImages = new Dictionary<LightState, String>();
    }

    private void changeLightImage() {

    }

    public void changeLightState() {
        switch (_currentLightState) {
            case RED -> _currentLightState = LightState.YELLOW;
            case YELLOW -> _currentLightState = LightState.GREEN;
            case GREEN -> _currentLightState = LightState.RED;
        }
    }

    public void calculateGLDuration(int totalCars, int cycleDuration) {
        _greenLightDuration = _vehiclesInLine.size() / totalCars * cycleDuration;
    }


}