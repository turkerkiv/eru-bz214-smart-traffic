package com.tsmteam.erubz214javasmarttraffic.model;

import com.tsmteam.erubz214javasmarttraffic.enums.Direction;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class VehicleCreator {
    public static Vehicle[] createVehicles(int carCount, Pane vehiclesPane, TrafficLight initialLight) {
        Vehicle[] vehicles = new Vehicle[carCount];
        for (int i = 0; i < carCount; i++) {
            Rectangle carImage = new Rectangle(500, 500, 20, 40);
            carImage.setFill(Color.BLUE);
            vehiclesPane.getChildren().add(carImage);

            double speed = 135;

            // selecting random destinationLight for vehicle
            TrafficLight destinationLight = CycleManager.getRandomDestinationExceptInitial(initialLight);
            Direction initialLocation = initialLight.getLocation();
            Road destinationRoad = destinationLight.getRoad();
            vehicles[i] = new Vehicle("BMW", speed, carImage, initialLocation, destinationRoad);
        }

        rotateVehiclesIfNecessary(initialLight.getLocation(), vehicles);
        return vehicles;
    }

    private static void rotateVehiclesIfNecessary(Direction initialLocation, Vehicle[] vehicles)
    {
        boolean isHorizontal = initialLocation == Direction.EAST || initialLocation == Direction.WEST;
        for (Vehicle vehicle : vehicles) {
            if (isHorizontal) {
                vehicle.rotateImage(90);
            }
        }
    }
}
