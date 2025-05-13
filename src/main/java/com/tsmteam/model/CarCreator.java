package main.java.com.tsmteam.model;

import java.util.ArrayList;
import java.util.List;

public class CarCreator {
    public List<Car> CreateCars(int carCount)
    {
        List<Car> cars = new ArrayList<>();
        for (int i = 0; i < carCount; i++)
        {
            // burada car fotolarının değişmesi bir inheritance gerektirmiyor ama truck motor vb gibi şeyler inheritance olmalı fotolar ise her bir türde bir havuzdan seçilebilir
            // Car car = new Car();
        }

        return cars;
    }
}
