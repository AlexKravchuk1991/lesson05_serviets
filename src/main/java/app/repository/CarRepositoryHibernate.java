package app.repository;

import app.model.Car;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class CarRepositoryHibernate implements CarRepository {
    EntityManager entityManager;

    public CarRepositoryHibernate() {
        entityManager = new Configuration()
                .configure("hibernate/postgres.cfg.xml")
                .buildSessionFactory()
                .createEntityManager();
    }

    @Override
    public List<Car> getAll() {

        return entityManager.createQuery("from Car", Car.class).getResultList();

        }



    @Override
    public Car save(Car car) {
        return entityManager.merge(car);
    }

    @Override
    public Car findById(long id) {

    try {
        return entityManager.find(Car.class, id);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}



    @Override
    public boolean deleteById(long id) {
        EntityTransaction transaction = null;
        try {
            // Получаем транзакцию
            transaction = entityManager.getTransaction();
            transaction.begin(); // Начало транзакции

            // Ищем объект
            Car car = findById(id);
            if (car != null) {
                // Удаляем объект
                entityManager.remove(car);
                transaction.commit(); // Фиксируем изменения
                return true;
            } else {
                transaction.rollback(); // Откатываем транзакцию в случае ошибки
            }
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback(); // Откат в случае исключения
            }
            throw new RuntimeException("Ошибка при удалении автомобиля", e);
        }
        return false;
    }

    @Override
    public boolean updateById(long id, Car car) {
        Car carToUpdate = findById(id);
        if (carToUpdate != null) {
            car.setId(carToUpdate.getId());
            entityManager.merge(car);
            return true;
        }
        return false;
    }

    @Override
    public Car update(Car car) {
        return entityManager.merge(car);
    }
}
