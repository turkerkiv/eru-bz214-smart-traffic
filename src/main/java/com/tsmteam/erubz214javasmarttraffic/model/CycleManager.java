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
    private static List<TrafficLight> _trafficLights;

    // there will be one background images pane, one lights pane, one cars pane

    public CycleManager() {
    }

    public static void createNewCycle(Rectangle northRoad, Rectangle eastRoad, Rectangle southRoad, Rectangle westRoad, int[] carCounts, Pane vehiclesPane) {
        // lets say right now carCounts in shape of [north, east, south, west]
        // absolutely change the signature of this method to make it more readable
        _trafficLights = new ArrayList<>();
        _trafficLights.add(new TrafficLight(Direction.NORTH, northRoad));
        _trafficLights.add(new TrafficLight(Direction.EAST, eastRoad));
        _trafficLights.add(new TrafficLight(Direction.SOUTH, southRoad));
        _trafficLights.add(new TrafficLight(Direction.WEST, westRoad));

        int totalCars = Arrays.stream(carCounts).sum();
        for (int i = 0; i < carCounts.length; i++) {
            TrafficLight light = _trafficLights.get(i);
            light.calculateGLDuration(totalCars, CYCLE_DURATION);
            light.addVehiclesToLine(VehicleCreator.createVehicles(carCounts[i], vehiclesPane));
        }
    }

    public void pauseCycle() {

    }

    public void resetCycle() {

    }
}
