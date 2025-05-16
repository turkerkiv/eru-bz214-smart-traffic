package main.java.com.tsmteam.model;

import main.java.com.tsmteam.enums.Direction;
import main.java.com.tsmteam.enums.VehicleState;

public class Vehicle {
    private String _name;
    private int _speed;
    private Direction _initialPos;
    private String _imgPath;
    private VehicleState _vehicleState;

    public Vehicle(String name, int speed, Direction initalPos, String imgPath)
    {
        _name = name;
        _speed = speed;
        _initialPos = initalPos;
        _imgPath = imgPath;
        _vehicleState = VehicleState.WAITING;
    }

    public void move(Direction destination) {

    }

    public void changeState() {
        switch(_vehicleState){
            case MOVING -> _vehicleState = VehicleState.WAITING;
            case WAITING -> _vehicleState = VehicleState.MOVING;
        }
    }
}
