package com.tsmteam.erubz214javasmarttraffic.model;

import com.tsmteam.erubz214javasmarttraffic.enums.Direction;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.*;

public class CycleManager {
    public static final double CYCLE_DURATION = 120;
    public static final double YELLOW_DURATION = 1;
    private static int DELAY_TO_START = 3;
    private static double _elapsedSecondsInCycle;
    private static List<TrafficLight> _trafficLights = new ArrayList<>();
    private static double _startTime = System.nanoTime();
    private static boolean _isStarted = false;
    private static boolean _isPaused = false;
    private static List<Vehicle> _allVehicles = new ArrayList<>();

    private static int[] _inputCarCounts;
    private static Rectangle[] _inputRoadRectangles;
    private static Pane _inputVehiclesPane;
    private static Group _transitionRectangles;
    private static Text _transitionTimer;
    private static Text _cycleTimerUI;

    public CycleManager() {
    }

    private static void clearCycle() {
        _isStarted = false;
        _startTime = System.nanoTime();
        _trafficLights.clear();
        for (Vehicle vhc : _allVehicles) {
            _inputVehiclesPane.getChildren().remove(vhc.getUiImage());
        }
        _allVehicles.clear();
    }

    public static void initNewCycle(Rectangle[] roads, int[] carCounts, Text[] timers, Text cycleTimer, Pane vehiclesPane, Pane lightsPane) {
        // carCounts and roads in shape of [north, east, south, west]
        _inputCarCounts = carCounts;
        _inputRoadRectangles = roads;
        _inputVehiclesPane = vehiclesPane;
        _cycleTimerUI = cycleTimer;

        clearCycle();

        for (int i = 0; i < carCounts.length; i++) {
            TrafficLight light = new TrafficLight(Direction.values()[i], roads[i], timers[i], lightsPane, YELLOW_DURATION);
            _trafficLights.add(light);
        }

        // creates vehicles
        for (int i = 0; i < carCounts.length; i++) {
            TrafficLight light = _trafficLights.get(i);
            Road road = light.getRoad();
            Vehicle[] vehicles = VehicleCreator.createVehicles(carCounts[i], vehiclesPane, light);
            road.addVehiclesToRoad(vehicles);
            road.setUpRoad();

            _allVehicles.addAll(Arrays.asList(vehicles));
        }
    }

    public static void runFrame(double now, double deltaTime) {
        if (_isPaused || _elapsedSecondsInCycle > CYCLE_DURATION) return;
        _elapsedSecondsInCycle = (now - _startTime) / 1_000_000_000.0;
//        _transitionTimer.setText(String.valueOf(Math.round(DELAY_TO_START - _elapsedSecondsInCycle)));
//        _transitionRectangles.setOpacity(DELAY_TO_START - _elapsedSecondsInCycle);
        if (!_isStarted && _elapsedSecondsInCycle > DELAY_TO_START) {
            // start timer too after delay
            _elapsedSecondsInCycle = 0;
            _startTime = System.nanoTime();
            Collections.shuffle(_trafficLights);
            for (TrafficLight light : _trafficLights) {
                light.setGLDuration(calculateGreenLightDuration(light));
                light.setRedLDuration(calculateRedLightDuration(light));
            }
            _isStarted = true;
//            _transitionRectangles.setVisible(false);
        }

        if (!_isStarted) return;

        _cycleTimerUI.setText(String.valueOf(Math.round(_elapsedSecondsInCycle)));

        //below are all independently exist & run
        for (TrafficLight light : _trafficLights) {
            light.runFrame(now);

            Road road = light.getRoad();
            road.runFrame();
        }

        for (Vehicle vehicle : _allVehicles) {
            vehicle.runFrame(deltaTime);
        }
    }

    public static TrafficLight getRandomDestinationExceptInitial(TrafficLight initialLocation) {
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

    // TODO - refactor

    public static double calculateGreenLightDuration(TrafficLight lightToCalculate) {
        int totalCars = 0;
        for (TrafficLight light : _trafficLights) {
            Road road = light.getRoad();
            totalCars += road.getVehicleCountInLine();
        }

        Road road = lightToCalculate.getRoad();
        double glDuration = ((double) road.getVehicleCountInLine() / totalCars) * (CYCLE_DURATION - _elapsedSecondsInCycle - 4 * YELLOW_DURATION);

        System.out.println(lightToCalculate.getLocation() + " has green light duration of: " + glDuration);
        return glDuration;
    }

    public static void togglePauseCycle(AnimationLoop animationLoop, Button pauseButton) {
        _isPaused = !_isPaused;
        if (_isPaused) {
            animationLoop.stop();
            pauseButton.setText("Continue");
        } else {
            animationLoop.start();
            pauseButton.setText("Pause");
        }

        for(Vehicle vehicle : _allVehicles)
        {
            vehicle.toggleAnimations(_isPaused);
        }
    }

    public static void resetCycle() {
        clearCycle();
    }
}
