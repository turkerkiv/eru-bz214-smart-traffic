package com.tsmteam.erubz214javasmarttraffic.model;

import com.tsmteam.erubz214javasmarttraffic.enums.Direction;
import com.tsmteam.erubz214javasmarttraffic.enums.LightState;
import javafx.scene.shape.Rectangle;

import java.awt.geom.Point2D;
import java.util.*;

public class TrafficLight {
    private List<Vehicle> _vehiclesInLine;
    private int _greenLightDuration;
    private LightState _currentLightState;
    private Dictionary<LightState, String> _lightImages;
    private Direction _location;


    private Rectangle _roadUIImage;
    private Point2D _roadPoints;

    public TrafficLight(Direction location, Rectangle roadUIImage) {
        _vehiclesInLine = new ArrayList<>();
        // _lightImages = new Dictionary<LightState, String>();
        _roadUIImage = roadUIImage;
        _location = location;
        _roadPoints = new Point2D.Double();
        setRoadPoints();
    }

    private void changeLightImage() {

    }

    public void changeLightState() {
        switch (_currentLightState) {
            case RED -> _currentLightState = LightState.YELLOW;
            case YELLOW -> _currentLightState = LightState.GREEN;
            case GREEN -> _currentLightState = LightState.RED;
        }
    }

    //instead of making public every method, call them in constructor

    public void calculateGLDuration(int totalCars, int cycleDuration) {
        _greenLightDuration = _vehiclesInLine.size() / totalCars * cycleDuration;
    }

    public void addVehiclesToLine(Vehicle[] vehicles) {
        _vehiclesInLine.addAll(Arrays.asList(vehicles));
        placeVehiclesInsideRoad();
    }

    private void placeVehiclesInsideRoad() {
        for (Vehicle vehicle : _vehiclesInLine) {
            // make it dictionary like roadPoints and vehicle. if a roadPoints not being used etc. then move it to that
            vehicle.move(_roadPoints.getX(),_roadPoints.getY());
        }
    }

    private void setRoadPoints()
    {
        _roadPoints.setLocation(_roadUIImage.getLayoutX() + _roadUIImage.getWidth() / 2, _roadUIImage.getLayoutY() + _roadUIImage.getHeight() / 2);
    }
}