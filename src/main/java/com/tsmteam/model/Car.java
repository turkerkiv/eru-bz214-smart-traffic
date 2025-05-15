package main.java.com.tsmteam.model;

import java.util.Arrays;
import java.util.Random;

public abstract class Car {
    private enum MovementState {
        MOVING,
        WAITING,
    }

    private int _speed;
    private String _type;
    private MovementState _currentMovementState;
    private Direction _destination;
    private Direction _currentPos;

    // ve bir de fotoğrafı

    public Car(int speed, String type, Direction currentPos) {
        _speed = speed;
        _type = type;
        _currentMovementState = MovementState.WAITING;
        _currentPos = currentPos;

        setRandomDestination();
    }

    private void setRandomDestination() {
        Direction[] directions = Arrays.stream(Direction.values()).filter(dir -> dir != _currentPos).toArray(Direction[]::new);
        Random rnd = new Random();
        int randomIndex = rnd.nextInt(directions.length);
        _destination = directions[randomIndex];
    }

    public void changeMovementState() {
        switch (_currentMovementState) {
            case MOVING -> _currentMovementState = MovementState.WAITING;
            case WAITING -> _currentMovementState = MovementState.MOVING;
        }
    }

    public void move() {
        switch (_currentMovementState) {
            case MOVING -> _currentMovementState = MovementState.MOVING;
            case WAITING -> _currentMovementState = MovementState.WAITING;
        }
    }
}
