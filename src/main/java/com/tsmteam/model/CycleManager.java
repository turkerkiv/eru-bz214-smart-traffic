package main.java.com.tsmteam.model;

import java.util.List;

public class CycleManager {
    CarCreator _carCreator;
    GreenLightDurationCalculator _glDurationCalculator;
    private static final int CYCLE_SECONDS = 120;
    private int _currentCycle = 0;
    private List<TrafficLight> _lights;

    public CycleManager() {
        _carCreator = new CarCreator();
        _glDurationCalculator = new GreenLightDurationCalculator();
    }

    public void startNewCycle() {
        int southCars = 5;
        int northCars = 3;
        int eastCars = 5;
        int westCars = 1;

        int totalCars = southCars + northCars + eastCars + westCars;

        _lights.add(
                new TrafficLight(
                        TrafficLight.Direction.SOUTH,
                        _carCreator.CreateCars(southCars)
                )
        );
        _lights.add(
                new TrafficLight(
                        TrafficLight.Direction.NORTH,
                        _carCreator.CreateCars(northCars)
                )
        );
        _lights.add(
                new TrafficLight(
                        TrafficLight.Direction.EAST,
                        _carCreator.CreateCars(eastCars)
                )
        );
        _lights.add(
                new TrafficLight(
                        TrafficLight.Direction.WEST,
                        _carCreator.CreateCars(westCars)
                )
        );

        for (var light : _lights)
        {
            int glDuration = _glDurationCalculator.calculateDuration(light.getCarCountInLine(), totalCars, CYCLE_SECONDS);
            light.setGreenLightDuration(glDuration);
        }

        _currentCycle++;
    }

    public void pauseCycle() {

    }

    public void resetCycle() {

    }
}
