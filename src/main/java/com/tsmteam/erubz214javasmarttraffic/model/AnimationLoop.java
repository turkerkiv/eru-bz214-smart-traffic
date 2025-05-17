package com.tsmteam.erubz214javasmarttraffic.model;

import javafx.animation.AnimationTimer;

public class AnimationLoop extends AnimationTimer {
    @Override
    public void handle(long l) {
        CycleManager.runCycle();
    }
}
