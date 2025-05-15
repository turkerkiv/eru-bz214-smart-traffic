package main.java.com.tsmteam.model;

import java.util.List;

public class AnimationManager {
    List<TrafficLight> _lights;

    public AnimationManager(List<TrafficLight> lights) {
        _lights = lights;
    }

    public void runAnimationsForOneCycle() {
        for (TrafficLight light : _lights) {
            // change it to yellow at first
            light.changeLightState();
            // then green
            light.changeLightState();
            // maybe move this part to light class to move cars
            for (Car car : light.getCars()) {
                car.move();
            }
            // then to red
            light.changeLightState();
        }
    }
}
