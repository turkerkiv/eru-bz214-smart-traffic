package com.tsmteam.erubz214javasmarttraffic.model;

import com.tsmteam.erubz214javasmarttraffic.enums.Direction;
import com.tsmteam.erubz214javasmarttraffic.enums.VehicleState;
import javafx.scene.shape.Rectangle;

public class Vehicle {
    private String _name;
    private double _speed;
    private Rectangle _uiImage;
    private VehicleState _vehicleState;

    public Vehicle(String name, double speed, Rectangle uiImage) {
        _name = name;
        _speed = speed;
        _uiImage = uiImage;
        _vehicleState = VehicleState.WAITING;
    }

    public void move(Direction destination) {
        _uiImage.setY(_uiImage.getY() - _speed);
    }

    public void move(double x, double y)
    {
        _uiImage.setX(x);
        _uiImage.setY(y);
    }

    public void rotate(double angle)
    {
        _uiImage.setRotate(angle);
    }

    public void changeState() {
        switch (_vehicleState) {
            case MOVING -> _vehicleState = VehicleState.WAITING;
            case WAITING -> _vehicleState = VehicleState.MOVING;
        }
    }
}
