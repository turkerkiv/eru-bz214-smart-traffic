package main.java.com.tsmteam.model;

public class GreenLightDurationCalculator {

    public int calculateDuration(int carsInLine, int totalCars, int cycleDuration)
    {
        return carsInLine / totalCars * cycleDuration;
    }
}
