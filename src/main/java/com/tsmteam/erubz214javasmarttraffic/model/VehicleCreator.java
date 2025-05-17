package com.tsmteam.erubz214javasmarttraffic.model;

import com.tsmteam.erubz214javasmarttraffic.enums.Direction;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

public class VehicleCreator {
    public static Vehicle[] createVehicles(int carCount, Pane vehiclesPane) {
        Vehicle[] vehicles = new Vehicle[carCount];
        for (int i = 0; i < carCount; i++) {
            // for now they are all static not dynamic
            Rectangle carImage = new Rectangle(500, 500, 40, 80);
            carImage.setFill(Color.BLACK);
            vehiclesPane.getChildren().add(carImage);
            vehicles[i] = new Vehicle("BMW", 0.75, carImage);
        }
        return vehicles;
    }
}
