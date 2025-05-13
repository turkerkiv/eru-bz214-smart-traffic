package main.java.com.tsmteam.model;

public abstract class Car {
    public enum MovementState
    {
        MOVING,
        WAITING,
    }

    private int _speed;
    private String _type;
    private MovementState _currentMovementState;

    // ve bir de fotoğrafı

    public Car(int speed, String type)
    {
        _speed = speed;
        _type = type;
        _currentMovementState = MovementState.WAITING;
    }

    public void changeMovementState()
    {
        switch(_currentMovementState)
        {
            case MOVING -> _currentMovementState = MovementState.WAITING;
            case WAITING -> _currentMovementState = MovementState.MOVING;
        }
    }
}
