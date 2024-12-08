package app.repository;

import app.model.Car;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarReporitoryMap implements CarRepository {
    private Map<Long, Car> database = new HashMap<>();
    private long currentId;

    public CarReporitoryMap() {
        initData();
    }

    private void initData() {
        save(new Car("vw",new BigDecimal(15000),2015));
        save(new Car("mazda",new BigDecimal(30000),2023));
        save(new Car("ford",new BigDecimal(40000),2024));

    }

    @Override
    public List<Car> getAll() {

        return new ArrayList<>(database.values());
    }

    @Override
    public Car save(Car car) {
        car.setId(++currentId);
        database.put(car.getId(), car);
        return car;
    }

    @Override
    public Car findById(long id) {
        return  database.getOrDefault(id,null);
    }

    @Override
    public boolean deleteById(long id) {
        if(database.containsKey(id)) {
            database.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateById(long id, Car car) {
        if(database.containsKey(id)) {
            Car prevCar = database.get(id);
            prevCar.setBrand(car.getBrand());
            prevCar.setPrice(car.getPrice());
            prevCar.setYear(car.getYear());
            return true;
        }
        return false;
    }

    @Override
    public Car update(Car car) {
        return null;
    }
}
