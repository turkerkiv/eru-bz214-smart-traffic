package main.java.com.tsmteam.model;

public class DurationCalculator {

    public int calculateGreenDuration(int carsInLine, int totalCars, int cycleDuration)
    {
        return carsInLine / totalCars * cycleDuration;
    }

    public int calculateRedDuration(int order, int totalLights, int greenDuration, int cycleDuration)
    {
        // buna gerek olmayabilir çünkü zaten green iken diğerleri hep kırmızı
        return 0;
    }
}
