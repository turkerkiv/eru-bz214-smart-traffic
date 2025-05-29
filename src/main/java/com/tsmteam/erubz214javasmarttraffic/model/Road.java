package com.tsmteam.erubz214javasmarttraffic.model;

import com.tsmteam.erubz214javasmarttraffic.enums.Direction;
import javafx.scene.shape.Rectangle;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Road {
    private List<Vehicle> _vehiclesInLine;
    private double _roadLeftLine;
    private double _roadRightLine;
    private double _roadEndLine;
    private Direction _location;
    private Rectangle _roadUIImage;
    private Map<Point2D, Vehicle> _roadPoints;

    public Road(TrafficLight light, Rectangle roadUIImage) {
        _location = light.getLocation();
        _roadPoints = new LinkedHashMap<>();
        _vehiclesInLine = new ArrayList<>();
        _roadUIImage = roadUIImage;

        calculateRightAndLeftLine();
    }


    private void calculateRoadEndLine() {
        switch (_location) {
            case NORTH, SOUTH -> _roadEndLine = _roadPoints.keySet().stream().toList().get(0).getY();
            case EAST, WEST -> _roadEndLine = _roadPoints.keySet().stream().toList().get(0).getX();
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

    private void placeVehiclesToPoints() {
        int i = 0;
        for (Point2D point : _roadPoints.keySet()) {
            Vehicle vehicle = _vehiclesInLine.get(i);
            vehicle.teleportToPoint(point);
            _roadPoints.put(point, vehicle);
            i++;
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
    }

    public void setUpRoad(){
        if(_vehiclesInLine.isEmpty()) return;
        setVehicleSpawnPoints();
        calculateRoadEndLine();
        placeVehiclesToPoints();
    }

    public void runFrame() {
        checkIfVehiclesStillInRoad();
    }

    public Direction getLocation() {
        return _location;
    }

    public void addVehiclesToRoad(Vehicle[] vehicles) {
        _vehiclesInLine.addAll(Arrays.asList(vehicles));
    }

    public void changeVehicleStates() {
        for (Vehicle vehicle : _vehiclesInLine) {
            vehicle.changeState();
        }
    }

    public void changeVehicleStatesDelayed() {
        for (int i = 0; i < _vehiclesInLine.size(); i++) {
            Vehicle vehicle = _vehiclesInLine.get(i);

            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            Runnable task = () -> {
                if (vehicle.isStillInRoad(_roadEndLine))
                    vehicle.changeState();
            };
            long delay = 500 + 300L * i;
            scheduler.schedule(task, delay, TimeUnit.MILLISECONDS);
            scheduler.shutdown();
        }
    }

    public int getVehicleCountInLine() {
        return _vehiclesInLine.size();
    }

    public void checkIfVehiclesStillInRoad() {
        _vehiclesInLine.removeIf(vehicle -> !vehicle.isStillInRoad(_roadEndLine));
    }

    public double getRoadLeftLine() {
        return _roadLeftLine;
    }
}
