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
    private Direction _destination;

    public Vehicle(String name, double speed, Rectangle uiImage, Direction initialLocation, Direction destination) {
        _name = name;
        _speed = speed;
        _uiImage = uiImage;
        _vehicleState = VehicleState.WAITING;
        _initialLocation = initialLocation;
        _destination = destination;
    }

    public void run(double deltaTime) {
        if (_vehicleState == VehicleState.MOVING) {
            double distance = _speed * deltaTime;
            switch (_initialLocation) {
                case NORTH -> _uiImage.setY(_uiImage.getY() + distance);
                case EAST -> _uiImage.setX(_uiImage.getX() - distance);
                case SOUTH -> _uiImage.setY(_uiImage.getY() - distance);
                case WEST -> _uiImage.setX(_uiImage.getX() + distance);
            }
        }
    }

    public void teleport(double x, double y) {
        _uiImage.setX(x);
        _uiImage.setY(y);
    }

    public void turn() {
        // deciding whether to rotate image or not
        boolean shouldRotateImage = true;
        if ((_initialLocation == Direction.NORTH || _initialLocation == Direction.SOUTH) && (_destination == Direction.NORTH || _destination == Direction.SOUTH))
            shouldRotateImage = false;
        else if ((_initialLocation == Direction.EAST || _initialLocation == Direction.WEST) && (_destination == Direction.EAST || _destination == Direction.WEST))
            shouldRotateImage = false;
        if (shouldRotateImage)
            rotateImage(90);

        // changing initialLocation because moving depends on it
        switch (_destination) {
            case NORTH -> _initialLocation = Direction.SOUTH;
            case EAST -> _initialLocation = Direction.WEST;
            case SOUTH -> _initialLocation = Direction.NORTH;
            case WEST -> _initialLocation = Direction.EAST;
        }
    }

    public void rotateImage(double angle) {
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
