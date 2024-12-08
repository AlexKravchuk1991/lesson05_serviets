package app.repository;

import app.constants.Constants;
import app.model.Car;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import static app.constants.Constants.*;

public class CarRepositoryDB implements CarRepository {
    private Connection getConnection() {
        try {

            // String DB_ADRESS="jdbc:postgresql://localhost:5432/";
            //    String DB_NAME = "cars";
            //    String DB_USER = "my_user";
            //    String DB_PASSWORD = "qwerty007";

            Class.forName(Constants.DB_DRIVER_PATH);
            String dbUrl = String.format("%s%s?user=%s&password=%s",
                    DB_ADRESS,DB_NAME,DB_USER,DB_PASSWORD);
            return DriverManager.getConnection(dbUrl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Car> getAll() {
        try ( Connection connection = getConnection()){

        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return List.of();
    }

    @Override
    public Car save(Car car) {
        try ( Connection connection = getConnection()){
            //INSERT INTO cars (brand,price,year) VALUES ('Toyota',30000,2022)
        String querry = String.format("INSERT INTO cars (brand,price,year) VALUES ('%s', %s,%d)", car.getBrand(), car.getPrice(), car.getYear());
            Statement statement = connection.createStatement();
            statement.execute(querry,statement.RETURN_GENERATED_KEYS);
//            ResultSet resultSet = statement.getResultSet();
//            resultSet.next();
//            long id = resultSet.getLong("id");
//            car.setId(id);

            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            Long id = resultSet.getLong("id");
            car.setId(id);

            return car;
        }catch (Exception e){
            throw new RuntimeException(e);
        }


    }

    @Override
    public Car findById(long id) {
        try ( Connection connection = getConnection()){
        String querry = String.format("SELECT * FROM cars WHERE id=%d", id);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(querry);
        if(resultSet.next()){
            Long idDb = resultSet.getLong("id");
            String brand = resultSet.getString("brand");
            BigDecimal price = resultSet.getBigDecimal("price");
            int year = resultSet.getInt("year");
            Car car = new Car(brand,price,year);
            car.setId(idDb);
            return car;
        }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean deleteById(long id) {
        try ( Connection connection = getConnection()){

        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public boolean updateById(long id, Car car) {
        try ( Connection connection = getConnection()){

        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public Car update(Car car) {
        try ( Connection connection = getConnection()){

        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return null;
    }
}
