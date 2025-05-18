package com.tsmteam.erubz214javasmarttraffic.model;

import com.tsmteam.erubz214javasmarttraffic.enums.Direction;
import com.tsmteam.erubz214javasmarttraffic.enums.LightState;
import javafx.scene.shape.Rectangle;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

public class TrafficLight {
    private List<Vehicle> _vehiclesInLine;
    private double _greenLightDuration;
    private LightState _currentLightState;
    private Dictionary<LightState, String> _lightImages;
    private Direction _location;
    public static final double YELLOW_DURATION = 1;


    private Rectangle _roadUIImage;
    private Map<Point2D, Vehicle> _roadPoints;

    private double _lightLastChangeTime;

    public TrafficLight(Direction location, Rectangle roadUIImage, Vehicle[] vehicles, double greenLightDuration) {
        _vehiclesInLine = new ArrayList<>();
        // _lightImages = new Dictionary<LightState, String>();
        _roadUIImage = roadUIImage;
        _location = location;

        // instead of hashmap it should be sortedmap etc because of placed order to move
        _roadPoints = new HashMap<>();
        setRoadPoints();

        addVehiclesToLine(vehicles);

        _greenLightDuration = greenLightDuration;

        _currentLightState = LightState.RED;
    }

    private void changeLightImage() {

    }

    public boolean runUntilRed(double now) {
        switch (_currentLightState) {
            case RED -> {
                _currentLightState = LightState.YELLOW;
                _lightLastChangeTime = System.nanoTime();
                changeLightImage();
            }
            case YELLOW -> {
                double elapsedSecondsInYellow = (now - _lightLastChangeTime) / 1_000_000_000.0;
                System.out.println(elapsedSecondsInYellow);
                System.out.println(_location + " now yellow");
                if(elapsedSecondsInYellow > YELLOW_DURATION)
                {
                    _lightLastChangeTime = System.nanoTime();
                    _currentLightState = LightState.GREEN;
                    changeLightImage();
                }
            }
            case GREEN -> {
                double elapsedSecondsInGreen = (now - _lightLastChangeTime) / 1_000_000_000.0;
                System.out.println(elapsedSecondsInGreen);
                System.out.println(_location + " now Green");
                for(Vehicle vehicle : _vehiclesInLine)
                {
                    vehicle.move();
                }
                if(elapsedSecondsInGreen > 5)
                {
                    _lightLastChangeTime = System.nanoTime();
                    _currentLightState = LightState.RED;
                    changeLightImage();
                    return true;
                }
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
            vehicle.teleport(roadPoint.getX(), roadPoint.getY());
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