package main.java.com.tsmteam.model;

import java.util.List;

public class TrafficLight {
    public enum LightState {
        RED,
        YELLOW,
        GREEN,
    }

    public enum Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST
    }

    private Direction _direction;
    private int _greenLightDuration;
    private List<Car> _carsInLine;
    private LightState _currentLightState;
    // and 3 image for red green yellow states

    public TrafficLight(Direction direction, List<Car> carsInLine) {
        _direction = direction;
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

    public void setGreenLightDuration(int greenLightDuration){
        _greenLightDuration = greenLightDuration;
    }
}