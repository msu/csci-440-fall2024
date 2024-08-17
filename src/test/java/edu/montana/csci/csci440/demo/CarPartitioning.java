package edu.montana.csci.csci440.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class CarPartitioning {

    static class Car {
        String name;
        String color;

        public Car(String name, String color) {
            this.name = name;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        @Override
        public String toString() {
            return "Car{" +
                    "name='" + name + '\'' +
                    ", color='" + color + '\'' +
                    '}';
        }
    }

    static List<Car> cars = Arrays.asList(new Car("Honda", "Red"),
                                  new Car("Toyota", "Blue"),
                                  new Car("BMW", "Black"),
                                    new Car("Ford", "Blue"));

    public static void main(String[] args) {
        HashMap<String, List<Car>> map = new HashMap<>();
        for (Car car : cars) {
            List<Car> cars = map.computeIfAbsent(car.getColor(), k -> new ArrayList<>());
            cars.add(car);
        }
        System.out.println(map);
    }

}
