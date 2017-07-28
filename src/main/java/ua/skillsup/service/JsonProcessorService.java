package ua.skillsup.service;

import ua.skillsup.service.components.JsonDecoder;
import ua.skillsup.service.components.JsonEncoder;

public class JsonProcessorService {
    // No DI for this time
    private JsonEncoder encoder = new JsonEncoder();
    private JsonDecoder decoder = new JsonDecoder();

    public String toJson(Object o) {
        return encoder.toJson(o);
    }

    public <T> T fromJson(String json, Class<T> clazz) {
        return decoder.fromJson(json, clazz);
    }

}
