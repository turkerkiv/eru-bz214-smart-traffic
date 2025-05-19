package com.tsmteam.erubz214javasmarttraffic.model;

import com.tsmteam.erubz214javasmarttraffic.enums.Direction;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

public class VehicleCreator {
    public static Vehicle[] createVehicles(int carCount, Pane vehiclesPane, Direction initialLocation) {
        Vehicle[] vehicles = new Vehicle[carCount];
        for (int i = 0; i < carCount; i++) {
            Rectangle carImage = new Rectangle(500, 500, 20, 40);
            carImage.setFill(Color.BLUE);
            vehiclesPane.getChildren().add(carImage);

            double speed = 135 + Math.random() * 5;

            // selecting random destination for car
            Stream<Direction> filteredDirections = Arrays.stream(Direction.values()).filter(x -> x != initialLocation);
            Direction[] directions = filteredDirections.toArray(Direction[]::new);
            Random rnd = new Random();
            int randomIndex = rnd.nextInt(0, directions.length);
            Direction destination = directions[randomIndex];

            vehicles[i] = new Vehicle("BMW", speed, carImage, initialLocation, destination);
        }
        return vehicles;
    }
}
