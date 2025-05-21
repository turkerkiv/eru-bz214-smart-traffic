package com.tsmteam.erubz214javasmarttraffic.model;

import com.tsmteam.erubz214javasmarttraffic.enums.Direction;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CycleManager {
    public static final double CYCLE_DURATION = 45;
    public static final double YELLOW_DURATION = 1;
    private static List<TrafficLight> _trafficLights = new ArrayList<>();
    private static TrafficLight _currentLight;
    private static int _currentLightIndex = 0;

    private static List<Vehicle> _allVehicles = new ArrayList<>();

    // there will be one background images pane, one lights pane, one cars pane

    public CycleManager() {
    }

    public static void initNewCycle(Rectangle[] roads, int[] carCounts, Pane vehiclesPane) {
        // lets say right now carCounts in shape of [north, east, south, west]
        // absolutely change the signature of this method to make it more readable

        // creates 4 lights
        int totalCars = Arrays.stream(carCounts).sum();
        for (int i = 0; i < carCounts.length; i++) {
            TrafficLight light = new TrafficLight(Direction.values()[i], roads[i]);
            _trafficLights.add(light);
        }

        // creates vehicles
        for (int i = 0; i < carCounts.length; i++) {
            TrafficLight light = _trafficLights.get(i);
            Vehicle[] vehicles = VehicleCreator.createVehicles(carCounts[i], vehiclesPane, light);
            _allVehicles.addAll(Arrays.asList(vehicles));
            light.addVehiclesToRoad(vehicles);
        }

        for (TrafficLight light : _trafficLights) {
            light.setGLDuration(calculateGreenLightDuration(light));
            light.setRedLDuration(calculateRedLightDuration(light));
        }
    }

    public static void runCycle(double now, double deltaTime) {
        // double elapsedSecondsInCycle = (now - ) / 1_000_000_000.0;
        for (TrafficLight light : _trafficLights) {
            light.run(now);
        }

        for (Vehicle vehicle : _allVehicles) {
            vehicle.run(deltaTime);
        }
    }

    public static TrafficLight getRandomDestination(TrafficLight initialLocation) {
        List<TrafficLight> filteredLights = _trafficLights.stream().filter(x -> x.getLocation() != initialLocation.getLocation()).toList();
        Random rnd = new Random();
        int randomIndex = rnd.nextInt(0, filteredLights.size());
        return filteredLights.get(randomIndex);
    }

    public static double calculateRedLightDuration(TrafficLight lightToCalculate) {
        double sum = 0;
        List<TrafficLight> filteredTrafficLights = _trafficLights.stream().filter(x -> x != lightToCalculate).toList();
        for (int i = 0; i < filteredTrafficLights.size(); i++) {
            TrafficLight light = filteredTrafficLights.get(i);
            double glDuration = light.getGLDuration();
            sum += glDuration;
            if (glDuration > 0) {
                sum += YELLOW_DURATION;
            }
        }

        System.out.println(lightToCalculate.getLocation() + " has red light duration of: " + sum);
        return sum;
    }

    public static double calculateGreenLightDuration(TrafficLight lightToCalculate) {
        int totalCars = 0;
        for (TrafficLight light : _trafficLights) {
            totalCars += light.getVehicleCountInLine();
        }

        double glDuration = ((double) lightToCalculate.getVehicleCountInLine() / totalCars) * CYCLE_DURATION;

        System.out.println(lightToCalculate.getLocation() + " has green light duration of: " + glDuration);
        return glDuration;
    }

    public static void pauseCycle() {

    }

    public static void resetCycle() {

    }
}
