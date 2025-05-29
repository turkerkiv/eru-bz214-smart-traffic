package com.tsmteam.erubz214javasmarttraffic.model;

import com.tsmteam.erubz214javasmarttraffic.enums.Direction;
import com.tsmteam.erubz214javasmarttraffic.enums.VehicleState;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.awt.geom.Point2D;

public class Vehicle {
    private double _speed;
    private ImageView _uiImage;
    private VehicleState _vehicleState;
    private Road _destinationRoad;
    private Direction _initialLocation;
    private boolean _didTurn = false;
    private TranslateTransition _translateTransition;
    private RotateTransition _rotateTransition;

    public Vehicle(double speed, ImageView uiImage, Direction initialLocation, Road destinationRoad) {
        _speed = speed;
        _uiImage = uiImage;
        _vehicleState = VehicleState.WAITING;
        _initialLocation = initialLocation;
        _destinationRoad = destinationRoad;
        _rotateTransition = new RotateTransition(Duration.millis(400), _uiImage);
        _translateTransition = new TranslateTransition(Duration.millis(1000), _uiImage);
    }

    public ImageView getUiImage() {
        return _uiImage;
    }

    public void runFrame(double deltaTime) {
        if (_vehicleState == VehicleState.MOVING) {
            double distance = _speed * deltaTime;
            switch (_initialLocation) {
                case NORTH -> {
                    double newY = _uiImage.getY() + distance;
                    _uiImage.setY(newY);
                    if (!_didTurn && newY > _destinationRoad.getRoadLeftLine() - 35)
                        turn();
                }
                case EAST -> {
                    double newX = _uiImage.getX() - distance;
                    _uiImage.setX(newX);
                    if (!_didTurn && newX < _destinationRoad.getRoadLeftLine() - 20)
                        turn();
                }
                case SOUTH -> {
                    double newY = _uiImage.getY() - distance;
                    _uiImage.setY(newY);
                    if (!_didTurn && newY < _destinationRoad.getRoadLeftLine() - 35)
                        turn();
                }
                case WEST -> {
                    double newX = _uiImage.getX() + distance;
                    _uiImage.setX(newX);
                    if (!_didTurn && newX > _destinationRoad.getRoadLeftLine() - 20)
                        turn();
                }
            }
        }
    }

    public void teleportToPoint(Point2D destination) {
        double dx = destination.getX() - _uiImage.getX();
        double dy = destination.getY() - _uiImage.getY();
        _translateTransition.stop();
        _translateTransition.setByX(dx);
        _translateTransition.setByY(dy);
        _translateTransition.setCycleCount(1);
        _translateTransition.setAutoReverse(false);
        _translateTransition.setOnFinished((e) -> {
            _uiImage.setX(destination.getX());
            _uiImage.setY(destination.getY());
            _uiImage.setTranslateX(0);
            _uiImage.setTranslateY(0);
        });
        _translateTransition.play();
    }

    public void turn() {
        _didTurn = true;

        double angleToRotate = 0;
        Direction destination = _destinationRoad.getLocation();
        switch (_initialLocation) {
            case NORTH -> {
                if (destination == Direction.EAST)
                    angleToRotate = -90;
                else if (destination == Direction.WEST)
                    angleToRotate = 90;
            }
            case EAST -> {
                if (destination == Direction.NORTH)
                    angleToRotate = 90;
                else if (destination == Direction.SOUTH)
                    angleToRotate = -90;
            }
            case SOUTH -> {
                if (destination == Direction.EAST)
                    angleToRotate = 90;
                else if (destination == Direction.WEST)
                    angleToRotate = -90;
            }
            case WEST -> {
                if (destination == Direction.SOUTH)
                    angleToRotate = 90;
                else if (destination == Direction.NORTH)
                    angleToRotate = -90;
            }
        }

        rotateImage(angleToRotate);

        // changing initialLocation because moving depends on it
        switch (_destinationRoad.getLocation()) {
            case NORTH -> _initialLocation = Direction.SOUTH;
            case EAST -> _initialLocation = Direction.WEST;
            case SOUTH -> _initialLocation = Direction.NORTH;
            case WEST -> _initialLocation = Direction.EAST;
        }
    }

    public void rotateImage(double angle) {
        if (angle == 0) return;
        _rotateTransition.stop();
        _rotateTransition.setByAngle(angle);
        _rotateTransition.setCycleCount(1);
        _rotateTransition.setAutoReverse(false);
        _rotateTransition.play();
    }

    public boolean isStillInRoad(double roadEndLine) {
        switch (_initialLocation) {
            case NORTH -> {
                return _uiImage.getY() <= roadEndLine;
            }
            case EAST -> {
                return _uiImage.getX() >= roadEndLine;
            }
            case SOUTH -> {
                return _uiImage.getY() >= roadEndLine;
            }
            case WEST -> {
                return _uiImage.getX() <= roadEndLine;
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

    public void toggleAnimations(boolean isPaused) {
        if (isPaused) {
            _rotateTransition.pause();
            _translateTransition.pause();
        } else {
            if (_rotateTransition.getStatus() == Animation.Status.PAUSED)
                _rotateTransition.play();
            if (_translateTransition.getStatus() == Animation.Status.PAUSED)
                _translateTransition.play();
        }
    }
}
