package com.tsmteam.erubz214javasmarttraffic.model;

import com.tsmteam.erubz214javasmarttraffic.enums.Direction;
import com.tsmteam.erubz214javasmarttraffic.enums.LightState;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TrafficLight {
    private List<Vehicle> _vehiclesInLine;
    private double _greenLightDuration;
    private LightState _currentLightState;
    private Dictionary<LightState, String> _lightImages;
    private Direction _location;
    private double _roadEndLine;
    private double _redLightDuration;
    private double _yellowLightDuration;

    private Rectangle _roadUIImage;
    private Map<Point2D, Vehicle> _roadPoints;

    private double _lightLastChangeTime;

    public TrafficLight(Direction location, Rectangle roadUIImage, Vehicle[] vehicles, double greenLightDuration, double redLightDuration, double yellowLightDuration) {
        _vehiclesInLine = new ArrayList<>();
        _roadPoints = new LinkedHashMap<>();
        _vehiclesInLine.addAll(Arrays.asList(vehicles));
        _roadUIImage = roadUIImage;
        _location = location;
        _currentLightState = LightState.RED;
        _lightLastChangeTime = System.nanoTime();

        _greenLightDuration = greenLightDuration;
        _redLightDuration = redLightDuration;
        _yellowLightDuration = yellowLightDuration;
        System.out.println("Green light of " + _location + " " + _greenLightDuration);
        System.out.println("Red light of " + _location + " " + _redLightDuration);

        setRoadPoints();
        placeVehiclesInsideRoad();
        calculateRoadEndLine();

        // _lightImages = new Dictionary<LightState, String>();
    }

    private void changeLightImage() {

    }

    private void calculateRoadEndLine() {
        switch (_location) {
            case NORTH -> _roadEndLine = _roadUIImage.getHeight();
            case EAST -> _roadEndLine = _roadUIImage.getLayoutX();
            case SOUTH -> _roadEndLine = _roadUIImage.getLayoutY();
            case WEST -> _roadEndLine = _roadUIImage.getWidth();
        }
    }

    public void run(double now) {
        if (_vehiclesInLine.isEmpty()) return;

        switch (_currentLightState) {
            case RED -> {
                double elapsedSecondsInRed = (now - _lightLastChangeTime) / 1_000_000_000.0;
                if (elapsedSecondsInRed > _redLightDuration) {
                    _currentLightState = LightState.YELLOW;
                    _lightLastChangeTime = System.nanoTime();
                    changeLightImage();
                    System.out.println(_location + " now yellow");
                }
            }
            case YELLOW -> {
                double elapsedSecondsInYellow = (now - _lightLastChangeTime) / 1_000_000_000.0;
                if (elapsedSecondsInYellow > _yellowLightDuration) {
                    _lightLastChangeTime = System.nanoTime();
                    _currentLightState = LightState.GREEN;
                    changeLightImage();

                    for (int i = 0; i < _vehiclesInLine.size(); i++) {
                        Vehicle vehicle = _vehiclesInLine.get(i);

                        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                        Runnable task = () -> vehicle.changeState();
                        long delay = 500 + 300L * i;
                        scheduler.schedule(task, delay, TimeUnit.MILLISECONDS);

                        scheduler.shutdown();
                    }
                    System.out.println(_location + " now green");
                }
            }
            case GREEN -> {
                double elapsedSecondsInGreen = (now - _lightLastChangeTime) / 1_000_000_000.0;
                for (int i = 0; i < _vehiclesInLine.size(); i++) {
                    Vehicle vehicle = _vehiclesInLine.get(i);
                    if (!vehicle.isStillInRoad(_roadEndLine)) {
                        // vehicle.rotate(90);
                        _vehiclesInLine.remove(vehicle);
                        System.out.println("One vehicle left");
                        if (_vehiclesInLine.isEmpty()) break;
                    }
                }
                if (elapsedSecondsInGreen > _greenLightDuration) {
                    _lightLastChangeTime = System.nanoTime();
                    _currentLightState = LightState.RED;
                    changeLightImage();

                    for (Vehicle vehicle : _vehiclesInLine) {
                        vehicle.changeState();
                    }
                }
            }
        }
    }

    public double getGLDuration() {
        return _greenLightDuration;
    }

    //instead of making public every method, call them in constructor

    private void placeVehiclesInsideRoad() {
        boolean isHorizontal = _location == Direction.EAST || _location == Direction.WEST;
        int i = 0;
        for (Point2D point : _roadPoints.keySet()) {
            // if (i == _roadPoints.size()) i = _roadPoints.size() - 1;
            Vehicle vehicle = _vehiclesInLine.get(i);
            vehicle.teleport(point.getX(), point.getY());
            _roadPoints.put(point, vehicle);
            i++;

            if (isHorizontal) {
                vehicle.rotate(90);
            }
        }
    }

    private void setRoadPoints() {
        int carsOffset = 50;
        int rightLineOffset = 40;
        for (int i = 1; i < _vehiclesInLine.size() + 1; i++) {
            double x = _roadUIImage.getLayoutX() + _roadUIImage.getWidth() / 2;
            double y = _roadUIImage.getLayoutY() + _roadUIImage.getHeight() / 2;

            switch (_location) {
                case NORTH -> {
                    y = _roadUIImage.getHeight() - i * carsOffset;
                    x -= rightLineOffset;
                }
                case EAST -> {
                    x = _roadUIImage.getLayoutX() + i * carsOffset;
                    y -= rightLineOffset;
                }
                case SOUTH -> {
                    y = _roadUIImage.getLayoutY() + i * carsOffset;
                    x += rightLineOffset - 25;
                }
                case WEST -> {
                    x = _roadUIImage.getWidth() - i * carsOffset;
                    y += rightLineOffset - 35;
                }
            }

            _roadPoints.put(new Point2D.Double(x, y), null);
        }
    }
}