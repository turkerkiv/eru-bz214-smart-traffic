package com.tsmteam.erubz214javasmarttraffic.model;

import com.tsmteam.erubz214javasmarttraffic.enums.Direction;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.*;

public class CycleManager {
    public static final double CYCLE_DURATION = 60;
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

    public CycleManager() {
    }

    private static void clearCycle() {
        _isStarted = false;
        _startTime = System.nanoTime();
        _trafficLights.clear();
        for (Vehicle vhc : _allVehicles) {
            _inputVehiclesPane.getChildren().remove(vhc.getUiImage());
        }
    }

    public static void initNewCycle(Rectangle[] roads, int[] carCounts, Pane vehiclesPane) {
        // carCounts and roads in shape of [north, east, south, west]
        _inputCarCounts = carCounts;
        _inputRoadRectangles = roads;
        _inputVehiclesPane = vehiclesPane;

        clearCycle();

        for (int i = 0; i < carCounts.length; i++) {
            TrafficLight light = new TrafficLight(Direction.values()[i], roads[i]);
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
        }

        if (!_isStarted) return;

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

    // TODO - araba grafikleri

    // TODO - ışık grafikleri

    // TODO - pauselarda falan animasyonlar devam ediyor
    // TODO - arabalar kırmızı ışıkta durunca animasyon yerine aynı gecikmeli change state kullanılsa daha doğal gözükebilir ama işte ilk araba geçebiliyor hadi o geçsin dersem bu defa arkadakiler çok geride kalabiliyor çizgiden belki en son repositioning yapılabilir ama speedlerin de çok fark etmemesi lazım yoksa yine bozuluyor.
    // TODO - geri kırmızı olunca değil de 4ü de 1 tur bittikten sonra tekrar hesaplatmak lazım gl ve rl durationları

    public static double calculateGreenLightDuration(TrafficLight lightToCalculate) {
        int totalCars = 0;
        for (TrafficLight light : _trafficLights) {
            Road road = light.getRoad();
            totalCars += road.getVehicleCountInLine();
        }

        Road road = lightToCalculate.getRoad();
        double glDuration = ((double) road.getVehicleCountInLine() / totalCars) * (CYCLE_DURATION - _elapsedSecondsInCycle - 4);

        System.out.println(lightToCalculate.getLocation() + " has green light duration of: " + glDuration);
        return glDuration;
    }

    public static void togglePauseCycle(AnimationLoop animationLoop) {
        _isPaused = !_isPaused;
        if (_isPaused)
            animationLoop.stop();
        else
            animationLoop.start();
    }

    public static void resetCycle() {
        clearCycle();
    }
}
