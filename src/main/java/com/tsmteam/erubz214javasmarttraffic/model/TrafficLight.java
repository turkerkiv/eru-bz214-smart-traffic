package com.tsmteam.erubz214javasmarttraffic.model;

import com.tsmteam.erubz214javasmarttraffic.enums.Direction;
import com.tsmteam.erubz214javasmarttraffic.enums.LightState;
import javafx.scene.shape.Rectangle;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

public class TrafficLight {
    private List<Vehicle> _vehiclesInLine;
    private int _greenLightDuration;
    private LightState _currentLightState;
    private Dictionary<LightState, String> _lightImages;
    private Direction _location;


    private Rectangle _roadUIImage;
    private Map<Point2D, Vehicle> _roadPoints;

    public TrafficLight(Direction location, Rectangle roadUIImage, Vehicle[] vehicles, int greenLightDuration) {
        _vehiclesInLine = new ArrayList<>();
        // _lightImages = new Dictionary<LightState, String>();
        _roadUIImage = roadUIImage;
        _location = location;

        // instead of hashmap it should be sortedmap etc because of placed order to move
        _roadPoints = new HashMap<>();
        setRoadPoints();
        addVehiclesToLine(vehicles);

        _greenLightDuration = greenLightDuration;
    }

    private void changeLightImage() {

    }

    public boolean runUntilRed() {
        switch (_currentLightState) {
            case RED -> {
                _currentLightState = LightState.YELLOW;
                changeLightImage();
            }
            case YELLOW -> {
                // wait 3 etc. seconds
                _currentLightState = LightState.GREEN;
                changeLightImage();
            }
            case GREEN -> {
                // wait gl duration seconds
                _currentLightState = LightState.RED;
                changeLightImage();
                return true;
            }
        }

        return false;
    }

    //instead of making public every method, call them in constructor

    private void addVehiclesToLine(Vehicle[] vehicles) {
        _vehiclesInLine.addAll(Arrays.asList(vehicles));
        placeVehiclesInsideRoad();
    }

    private void placeVehiclesInsideRoad() {
        boolean isHorizontal = _location == Direction.EAST || _location == Direction.WEST;
        int i = 0;
        for (Point2D roadPoint : _roadPoints.keySet()) {
            if (i == _vehiclesInLine.size()) break;
            Vehicle vehicle = _vehiclesInLine.get(i);
            _roadPoints.put(roadPoint, vehicle);
            vehicle.move(roadPoint.getX(), roadPoint.getY());
            i++;

            if (isHorizontal) {
                vehicle.rotate(90);
            }
        }
    }

    private void setRoadPoints() {
        int offset = 50;
        boolean isHorizontal = _location == Direction.EAST || _location == Direction.WEST;
        for (int i = 1; i < 4; i++) {
            double x = _roadUIImage.getLayoutX() + _roadUIImage.getWidth() / 2;
            double y = _roadUIImage.getLayoutY() + _roadUIImage.getHeight() / 2;

            if (isHorizontal) {
                x = _roadUIImage.getLayoutX() + i * offset;
            } else {
                y = _roadUIImage.getLayoutY() + i * offset;
            }

            _roadPoints.put(new Point2D.Double(x, y), null);
        }
    }
}