package main.java.com.tsmteam.model;

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

    // ve bir de fotoğrafı

    public Car(int speed, String type) {
        _speed = speed;
        _type = type;
        _currentMovementState = MovementState.WAITING;

        setRandomDestination();
    }

    private void setRandomDestination() {
            Direction[] directions = Direction.values();
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
}
