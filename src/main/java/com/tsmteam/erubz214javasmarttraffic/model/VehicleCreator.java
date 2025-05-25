package com.tsmteam.erubz214javasmarttraffic.model;

import com.tsmteam.erubz214javasmarttraffic.enums.Direction;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class VehicleCreator {
    private static final String[] _carImagePaths = {
            "/com/tsmteam/erubz214javasmarttraffic/image/cars/1.png",
            "/com/tsmteam/erubz214javasmarttraffic/image/cars/2.png",
            "/com/tsmteam/erubz214javasmarttraffic/image/cars/3.png",
            "/com/tsmteam/erubz214javasmarttraffic/image/cars/4.png",
            "/com/tsmteam/erubz214javasmarttraffic/image/cars/5.png",
            "/com/tsmteam/erubz214javasmarttraffic/image/cars/6.png",
            "/com/tsmteam/erubz214javasmarttraffic/image/cars/7.png",
            "/com/tsmteam/erubz214javasmarttraffic/image/cars/8.png",
            "/com/tsmteam/erubz214javasmarttraffic/image/cars/9.png",
            "/com/tsmteam/erubz214javasmarttraffic/image/cars/10.png",
            "/com/tsmteam/erubz214javasmarttraffic/image/cars/11.png",
            "/com/tsmteam/erubz214javasmarttraffic/image/cars/12.png",
            "/com/tsmteam/erubz214javasmarttraffic/image/cars/13.png",
            "/com/tsmteam/erubz214javasmarttraffic/image/cars/14.png",
            "/com/tsmteam/erubz214javasmarttraffic/image/cars/15.png",
            "/com/tsmteam/erubz214javasmarttraffic/image/cars/16.png",
            "/com/tsmteam/erubz214javasmarttraffic/image/cars/17.png",
            "/com/tsmteam/erubz214javasmarttraffic/image/cars/18.png",
            "/com/tsmteam/erubz214javasmarttraffic/image/cars/19.png",
            "/com/tsmteam/erubz214javasmarttraffic/image/cars/20.png",
            "/com/tsmteam/erubz214javasmarttraffic/image/cars/21.png",
            "/com/tsmteam/erubz214javasmarttraffic/image/cars/22.png",
            "/com/tsmteam/erubz214javasmarttraffic/image/cars/23.png",
            "/com/tsmteam/erubz214javasmarttraffic/image/cars/24.png",
    };

    private static final Random _random = new Random();

    public static Vehicle[] createVehicles(int carCount, Pane vehiclesPane, TrafficLight initialLight) {
        Vehicle[] vehicles = new Vehicle[carCount];
        for (int i = 0; i < carCount; i++) {
            String imagePath = _carImagePaths[_random.nextInt(_carImagePaths.length)];
            Image image = new Image(VehicleCreator.class.getResourceAsStream(imagePath));
            ImageView carImage = new ImageView(image);

            carImage.setFitWidth(23);
            carImage.setFitHeight(42);
            carImage.setX(500);
            carImage.setY(500);

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

    private static void rotateVehiclesIfNecessary(Direction initialLocation, Vehicle[] vehicles) {
        for (Vehicle vehicle : vehicles) {
            switch (initialLocation)
            {
                case NORTH -> vehicle.rotateImage(180);
                case EAST -> vehicle.rotateImage(270);
                case WEST -> vehicle.rotateImage(90);
            }
        }
    }
}
