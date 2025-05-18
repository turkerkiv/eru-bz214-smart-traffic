package com.tsmteam.erubz214javasmarttraffic.model;

import javafx.animation.AnimationTimer;

public class AnimationLoop extends AnimationTimer {
    private double _lastUpdate;
    @Override
    public void start() {
        _lastUpdate = System.nanoTime();
        super.start();
    }

    @Override
    public void handle(long l) {
        double deltaTime = (l - _lastUpdate) / 1_000_000_000.0;
        _lastUpdate = System.nanoTime();
        CycleManager.runCycle(l, deltaTime);
    }
}
