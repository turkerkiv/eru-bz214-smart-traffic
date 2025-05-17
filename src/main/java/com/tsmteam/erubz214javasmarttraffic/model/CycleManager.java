package com.tsmteam.erubz214javasmarttraffic.model;

import com.tsmteam.erubz214javasmarttraffic.enums.Direction;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CycleManager {
    public static final int CYCLE_DURATION = 120;
    public static final int YELLOW_DURATION = 1;
    private static List<TrafficLight> _trafficLights = new ArrayList<>();
    private static TrafficLight _currentLight;
    private static int _currentLightIndex = 0;

    // there will be one background images pane, one lights pane, one cars pane

    public CycleManager() {
    }

    public static void initNewCycle(Rectangle[] roads, int[] carCounts, Pane vehiclesPane) {
        // lets say right now carCounts in shape of [north, east, south, west]
        // absolutely change the signature of this method to make it more readable

        int totalCars = Arrays.stream(carCounts).sum();
        for (int i = 0; i < carCounts.length; i++) {
            Vehicle[] vehicles = VehicleCreator.createVehicles(carCounts[i], vehiclesPane);
            int greenLightDuration = vehicles.length / totalCars * CYCLE_DURATION;
            TrafficLight light = new TrafficLight(Direction.values()[i], roads[i], vehicles, greenLightDuration);
            _trafficLights.add(light);
        }
    }

    public static void runCycle() {
        if (_currentLightIndex == _trafficLights.size())
            return;

        if (_currentLight == null) {
            _currentLight = _trafficLights.get(_currentLightIndex);
        } else if (_currentLight.runUntilRed()) {
            _currentLight = null;
            _currentLightIndex++;
        }
    }

    public static void pauseCycle() {

    }

    public static void resetCycle() {

    }
}
