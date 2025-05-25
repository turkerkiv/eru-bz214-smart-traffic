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
            "/image/cars/1.png",
            "/image/cars/2.png",
            "/image/cars/3.png",
            "/image/cars/4.png",
            "/image/cars/5.png",
            "/image/cars/6.png",
            "/image/cars/7.png",
            "/image/cars/8.png",
            "/image/cars/9.png",
            "/image/cars/10.png",
            "/image/cars/11.png",
            "/image/cars/12.png",
            "/image/cars/13.png",
            "/image/cars/14.png",
            "/image/cars/15.png",
            "/image/cars/16.png",
            "/image/cars/17.png",
            "/image/cars/18.png",
            "/image/cars/19.png",
            "/image/cars/20.png",
            "/image/cars/21.png",
            "/image/cars/22.png",
            "/image/cars/23.png",
            "/image/cars/24.png",
    };

    private static final Random _random = new Random();

    public static Vehicle[] createVehicles(int carCount, Pane vehiclesPane, TrafficLight initialLight) {
        Vehicle[] vehicles = new Vehicle[carCount];
        for (int i = 0; i < carCount; i++) {
            String imagePath = _carImagePaths[_random.nextInt(_carImagePaths.length)];
            Image image = new Image(VehicleCreator.class.getResourceAsStream(imagePath));
            ImageView carImage = new ImageView(image);

            carImage.setFitWidth(20);
            carImage.setFitHeight(40);
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
        boolean isHorizontal = initialLocation == Direction.EAST || initialLocation == Direction.WEST;
        for (Vehicle vehicle : vehicles) {
            if (isHorizontal) {
                vehicle.rotateImage(90);
            }
        }
    }
}
