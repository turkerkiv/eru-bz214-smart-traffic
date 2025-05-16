package main.java.com.tsmteam.model;

import java.util.List;

public class CycleManager {
    public final int CYCLE_DURATION = 120;
    public final int YELLOW_DURATION = 1;
    List<TrafficLight> _trafficLights;

    public CycleManager() {
        createNewCycle(new int[] {4, 3, 5, 1, 2});
    }

    public void createNewCycle(int[] carCounts) {
        // i guess must be in shape of [n of lights, first light, second, third, forth]
    }

    public void pauseCycle() {

    }

    public void resetCycle() {

    }
}
