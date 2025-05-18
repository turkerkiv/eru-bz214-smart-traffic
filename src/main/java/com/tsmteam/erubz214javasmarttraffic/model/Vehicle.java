package com.tsmteam.erubz214javasmarttraffic.model;

import com.tsmteam.erubz214javasmarttraffic.enums.Direction;
import com.tsmteam.erubz214javasmarttraffic.enums.VehicleState;
import javafx.scene.shape.Rectangle;

public class Vehicle {
    private String _name;
    private double _speed;
    private Rectangle _uiImage;
    private VehicleState _vehicleState;
    private Direction _initialLocation;

    public Vehicle(String name, double speed, Rectangle uiImage, Direction initialLocation) {
        _name = name;
        _speed = speed;
        _uiImage = uiImage;
        _vehicleState = VehicleState.WAITING;
        _initialLocation = initialLocation;
    }

    public void run() {
        if (_vehicleState == VehicleState.MOVING) {
            switch (_initialLocation) {
                case NORTH -> _uiImage.setY(_uiImage.getY() + _speed);
                case EAST -> _uiImage.setX(_uiImage.getX() - _speed);
                case SOUTH -> _uiImage.setY(_uiImage.getY() - _speed);
                case WEST -> _uiImage.setX(_uiImage.getX() + _speed);
            }
        }
    }

    public void teleport(double x, double y) {
        _uiImage.setX(x);
        _uiImage.setY(y);
    }

    public void rotate(double angle) {
        _uiImage.setRotate(_uiImage.getRotate() + angle);
    }

    public boolean isStillInRoad(double roadEndLine) {
        switch (_initialLocation) {
            case NORTH -> {
                return _uiImage.getY() < roadEndLine;
            }
            case EAST -> {
                return _uiImage.getX() > roadEndLine;
            }
            case SOUTH -> {
                return _uiImage.getY() > roadEndLine;
            }
            case WEST -> {
                return _uiImage.getX() < roadEndLine;
            }
        }
        return false;
    }

    public void changeState() {
        switch (_vehicleState) {
            case MOVING -> _vehicleState = VehicleState.WAITING;
            case WAITING -> _vehicleState = VehicleState.MOVING;
        }
    }
}
