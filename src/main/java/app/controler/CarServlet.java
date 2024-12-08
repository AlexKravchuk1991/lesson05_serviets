package app.controler;

import app.model.Car;
import app.repository.CarReporitoryMap;
import app.repository.CarRepository;
import app.repository.CarRepositoryDB;
import app.repository.CarRepositoryHibernate;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarServlet extends HttpServlet {

    private CarRepository repository = new CarRepositoryHibernate();
    ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String[]> params = req.getParameterMap();

        if (params.isEmpty()) {
            // получение всего списка автомобилей
            List<Car> cars = repository.getAll();

            resp.setContentType("application/json");

            String json = mapper.writeValueAsString(cars);

            resp.getWriter().write(json);
            //        cars.forEach(car->{
//            try {
//
////                resp.getWriter().write(car.toString()+"\n");
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
        } else {
            String idStr = params.get("id")[0];
            Long id = Long.parseLong(idStr);
            Car car = repository.findById(id);
            if (car == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("Car not found");
            } else {
                String jsonResp = mapper.writeValueAsString(car);
                resp.getWriter().write(jsonResp);
            }
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Car car = mapper.readValue(req.getReader(), Car.class);
        System.out.println(car.toString());
        car=repository.save(car);
        String json = mapper.writeValueAsString(car);
        resp.getWriter().write(json);

//        Map<String, String[]> parameter = req.getParameterMap();
//        if (parameter.isEmpty()) {
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
//            return;
//        }
//
//        Car newCar = mapper.readValue(req.getReader(), Car.class);
//        if (newCar == null || newCar.getBrand() == null || newCar.getPrice() == null) {
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
//            return;
//        }
//        repository.save(newCar);
//        resp.setContentType("application/json");
//        String jsonResp = mapper.writeValueAsString(newCar);
//        resp.getWriter().write(jsonResp);

    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String[]> params = req.getParameterMap();

        if (!params.containsKey("id")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Long id = Long.parseLong(params.get("id")[0]);

        Car car = repository.findById(id);
        if (car == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Car updCar = mapper.readValue(req.getReader(), Car.class);
        if (updCar == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } else {
            updCar.setBrand(updCar.getBrand());
            updCar.setPrice(updCar.getPrice());
            repository.updateById(id, updCar);
        }
        resp.setContentType("application/json");
        resp.getWriter().write("Car is updated");

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String[]> params = req.getParameterMap();
        if (!params.containsKey("id")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Long id = Long.parseLong(params.get("id")[0]);
        boolean deleted = repository.deleteById(id);
        if (!deleted) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else {
            resp.setContentType("application/json");
            resp.getWriter().write("Car is deleted");
        }
    }
}
