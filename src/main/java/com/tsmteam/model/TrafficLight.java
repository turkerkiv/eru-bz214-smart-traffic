package main.java.com.tsmteam.model;

import java.util.List;

public class TrafficLight {
    private enum LightState {
        RED,
        YELLOW,
        GREEN,
    }

    private static final int YELLOW_DURATION = 3;
    private int _greenLightDuration;
    private Direction _position;
    private List<Car> _carsInLine;
    private LightState _currentLightState;
    // and 3 image for red green yellow states

    public TrafficLight(Direction position, List<Car> carsInLine) {
        _position = position;
        _greenLightDuration = 0;
        _carsInLine = carsInLine;
        _currentLightState = LightState.RED;
    }

    public void changeLightState() {
        switch (_currentLightState) {
            case RED -> _currentLightState = LightState.YELLOW;
            case YELLOW -> _currentLightState = LightState.GREEN;
            case GREEN -> _currentLightState = LightState.RED;
        }
    }

    public int getCarCountInLine(){
        return _carsInLine.size();
    }

    public List<Car> getCars(){
        return _carsInLine;
    }

    public void setGreenLightDuration(int greenLightDuration){
        _greenLightDuration = greenLightDuration;
    }

}