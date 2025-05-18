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
    public static Vehicle[] createVehicles(int carCount, Pane vehiclesPane, Direction initialLocation) {
        Vehicle[] vehicles = new Vehicle[carCount];
        for (int i = 0; i < carCount; i++) {
            Rectangle carImage = new Rectangle(500, 500, 20, 40);
            carImage.setFill(Color.BLUE);
            vehiclesPane.getChildren().add(carImage);

            double speed = 0.5 + Math.random() * 0.10;
            // later add real pngs for cars
            vehicles[i] = new Vehicle("BMW", speed, carImage, initialLocation);
        }
        return vehicles;
    }
}
