package ua.skillsup;

import com.google.gson.Gson;
import ua.skillsup.model.Human;
import ua.skillsup.service.JsonProcessorService;

import java.time.LocalDate;
import java.time.Month;

public class App {

    public static void main(String[] args) {

        Human human = new Human("Vladimir", "Bredihin",
                "Programming", LocalDate.of(1985, Month.FEBRUARY, 5));

        Gson gson = new Gson();
        String json1 = gson.toJson(human);

        System.out.println(json1);


        JsonProcessorService service = new JsonProcessorService();
        service.printJson(service.toJson(human));
    }
}
