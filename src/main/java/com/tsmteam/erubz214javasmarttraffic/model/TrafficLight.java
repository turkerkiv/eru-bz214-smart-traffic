package com.tsmteam.erubz214javasmarttraffic.model;

import com.tsmteam.erubz214javasmarttraffic.enums.Direction;
import com.tsmteam.erubz214javasmarttraffic.enums.LightState;
import javafx.scene.shape.Rectangle;

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
    private double _roadRightLine;
    private double _roadLeftLine;

    // TODO can be refactored to seperate road logic and traffic light logic and also maybe road holds its traffic light, road holds vehicles and traffic light only calculates duration etc.

    private Rectangle _roadUIImage;
    private Map<Point2D, Vehicle> _roadPoints;

    private double _lightLastChangeTime;

    public TrafficLight(Direction location, Rectangle roadUIImage) {
        _vehiclesInLine = new ArrayList<>();
        _roadPoints = new LinkedHashMap<>();
        _roadUIImage = roadUIImage;
        _location = location;
        _currentLightState = LightState.RED;
        _lightLastChangeTime = System.nanoTime();

        calculateRightAndLeftLine();

        // _lightImages = new Dictionary<LightState, String>();
    }

    private void changeLightImage() {

    }

    private void calculateRoadEndLine() {
        switch (_location) {
            case NORTH, SOUTH -> _roadEndLine = _roadPoints.keySet().stream().toList().get(0).getY();
            case EAST, WEST -> _roadEndLine = _roadPoints.keySet().stream().toList().get(0).getX();
        }
    }

    public void addVehiclesToRoad(Vehicle[] vehicles) {
        _vehiclesInLine.addAll(Arrays.asList(vehicles));
        setVehicleSpawnPoints();
        placeVehiclesToPoints();
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

                    // makes vehicles change state to MOVING
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
                //checks if vehicles are still in road
                for (int i = 0; i < _vehiclesInLine.size(); i++) {
                    Vehicle vehicle = _vehiclesInLine.get(i);
                    if (!vehicle.isStillInRoad(_roadEndLine)) {
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

                    _greenLightDuration = CycleManager.calculateGreenLightDuration(this);
                    _redLightDuration = CycleManager.calculateRedLightDuration(this);
                    setVehicleSpawnPoints();
                    placeVehiclesToPoints();
                }
            }
        }
    }

    public void setGLDuration(double greenLightDuration) {
        _greenLightDuration = greenLightDuration;
    }

    public void setRedLDuration(double redLightDuration) {
        _redLightDuration = redLightDuration;
    }

    public double getGLDuration() {
        return _greenLightDuration;
    }

    public double getRedLDuration() {
        return _redLightDuration;
    }

    public int getVehicleCountInLine()
    {
        return _vehiclesInLine.size();
    }

    public double getRoadLeftLine() {
        return _roadLeftLine;
    }

    public Direction getLocation() {
        return _location;
    }

    private void placeVehiclesToPoints() {
        boolean isHorizontal = _location == Direction.EAST || _location == Direction.WEST;
        int i = 0;
        for (Point2D point : _roadPoints.keySet()) {
            // if (i == _roadPoints.size()) i = _roadPoints.size() - 1;
            Vehicle vehicle = _vehiclesInLine.get(i);
            vehicle.teleport(point.getX(), point.getY());
            _roadPoints.put(point, vehicle);
            i++;

            if (isHorizontal) {
                vehicle.rotateImage(90);
            }
        }
    }

    private void calculateRightAndLeftLine() {
        double x = _roadUIImage.getLayoutX() + _roadUIImage.getWidth() / 2;
        double y = _roadUIImage.getLayoutY() + _roadUIImage.getHeight() / 2;
        int middleOffset = 40;
        switch (_location) {
            case NORTH -> {
                _roadRightLine = x - middleOffset;
                _roadLeftLine = x + middleOffset;
            }
            case EAST -> {
                _roadRightLine = y - middleOffset;
                _roadLeftLine = y + middleOffset;
            }
            case SOUTH -> {
                _roadRightLine = x + middleOffset - 25;
                _roadLeftLine = x - middleOffset + 25;
            }
            case WEST -> {
                _roadRightLine = y + middleOffset - 35;
                _roadLeftLine = y - middleOffset + 35;
            }

        }
    }

    private void setVehicleSpawnPoints() {
        _roadPoints.clear();
        int carsOffset = 50;
        for (int i = 1; i < _vehiclesInLine.size() + 1; i++) {
            double x = _roadUIImage.getLayoutX() + _roadUIImage.getWidth() / 2;
            double y = _roadUIImage.getLayoutY() + _roadUIImage.getHeight() / 2;

            switch (_location) {
                case NORTH -> {
                    y = _roadUIImage.getHeight() - i * carsOffset;
                    x = _roadRightLine;
                }
                case EAST -> {
                    x = _roadUIImage.getLayoutX() + i * carsOffset;
                    y = _roadRightLine;
                }
                case SOUTH -> {
                    y = _roadUIImage.getLayoutY() + i * carsOffset;
                    x = _roadRightLine;
                }
                case WEST -> {
                    x = _roadUIImage.getWidth() - i * carsOffset;
                    y = _roadRightLine;
                }
            }

            _roadPoints.put(new Point2D.Double(x, y), null);
        }

        calculateRoadEndLine();
    }
}