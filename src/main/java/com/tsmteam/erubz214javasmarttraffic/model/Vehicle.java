package com.tsmteam.erubz214javasmarttraffic.model;

import com.tsmteam.erubz214javasmarttraffic.enums.Direction;
import com.tsmteam.erubz214javasmarttraffic.enums.VehicleState;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.awt.geom.Point2D;

public class Vehicle {
    private String _name;
    private double _speed;
    private Rectangle _uiImage;
    private VehicleState _vehicleState;
    private TrafficLight _connectedTrafficLight;
    private TrafficLight _destination;
    private Direction _initialLocation;
    private boolean _didTurn = false;

    public Vehicle(String name, double speed, Rectangle uiImage, TrafficLight connectedTrafficLight, TrafficLight destination) {
        _name = name;
        _speed = speed;
        _uiImage = uiImage;
        _vehicleState = VehicleState.WAITING;
        _connectedTrafficLight = connectedTrafficLight;
        _initialLocation = connectedTrafficLight.getLocation();
        _destination = destination;
    }

    public void run(double deltaTime) {
        if (_vehicleState == VehicleState.MOVING) {
            double distance = _speed * deltaTime;
            switch (_initialLocation) {
                case NORTH -> {
                    double newY = _uiImage.getY() + distance;
                    _uiImage.setY(newY);
                    if (!_didTurn && newY > _destination.getRoadLeftLine() - 35)
                        turn();
                }
                case EAST -> {
                    double newX = _uiImage.getX() - distance;
                    _uiImage.setX(newX);
                    if (!_didTurn && newX < _destination.getRoadLeftLine() - 20)
                        turn();
                }
                case SOUTH -> {
                    double newY = _uiImage.getY() - distance;
                    _uiImage.setY(newY);
                    if (!_didTurn && newY < _destination.getRoadLeftLine() - 35)
                        turn();
                }
                case WEST -> {
                    double newX = _uiImage.getX() + distance;
                    _uiImage.setX(newX);
                    if (!_didTurn && newX > _destination.getRoadLeftLine() - 20)
                        turn();
                }
            }
        }
    }

    public void teleportToPoint(Point2D destination) {
        double dx = destination.getX() - _uiImage.getX();
        double dy = destination.getY() - _uiImage.getY();
        double durationInMs = 1000;
        TranslateTransition move = new TranslateTransition(Duration.millis(durationInMs), _uiImage);
        move.setByX(dx);
        move.setByY(dy);
        move.setCycleCount(1);
        move.setAutoReverse(false);
        move.setOnFinished((e) -> {
            _uiImage.setX(destination.getX());
            _uiImage.setY(destination.getY());
            _uiImage.setTranslateX(0);
            _uiImage.setTranslateY(0);
        });
        move.play();
    }

    public void turn() {
        _didTurn = true;

        // deciding whether to rotate image or not
        double angleToRotate = 0;
        Direction destination = _destination.getLocation();
        switch (_initialLocation)
        {
            case NORTH -> {
                if(destination == Direction.EAST)
                    angleToRotate = -90;
                else if(destination == Direction.WEST)
                    angleToRotate = 90;
            }
            case EAST -> {
                if(destination == Direction.NORTH)
                    angleToRotate = 90;
                else if(destination == Direction.SOUTH)
                    angleToRotate = -90;
            }
            case SOUTH -> {
                if(destination == Direction.EAST)
                    angleToRotate = 90;
                else if(destination == Direction.WEST)
                    angleToRotate = -90;
            }
            case WEST -> {
                if(destination == Direction.SOUTH)
                    angleToRotate = 90;
                else if(destination == Direction.NORTH)
                    angleToRotate = -90;
            }
        }

        rotateImage(angleToRotate);

        // changing initialLocation because moving depends on it
        switch (_destination.getLocation()) {
            case NORTH -> _initialLocation = Direction.SOUTH;
            case EAST -> _initialLocation = Direction.WEST;
            case SOUTH -> _initialLocation = Direction.NORTH;
            case WEST -> _initialLocation = Direction.EAST;
        }
    }

    public void rotateImage(double angle) {
        if(angle == 0) return;
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(600), _uiImage);
        rotateTransition.setByAngle(angle);
        rotateTransition.setCycleCount(1);
        rotateTransition.setAutoReverse(false);
        rotateTransition.play();

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
