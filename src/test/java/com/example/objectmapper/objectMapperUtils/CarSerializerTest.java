package com.example.objectmapper.objectMapperUtils;

import com.example.objectmapper.domain.Car;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CarSerializerTest {

    final static String COLOR = "YELLOW";
    final static String TYPE = "RENAULT";

    @Test
    @DisplayName("custom serializer인 CarSerializer를 사용하여 특정 필드만 다른 이름의 key로 변환한다.")
    void test1() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("CarSerializer", new Version(1, 0, 0, null, null, null));
        module.addSerializer(Car.class, new CarSerializer());
        mapper.registerModule(module);
        Car car = new Car(COLOR, TYPE);

        String carJson = mapper.writeValueAsString(car);
        System.out.println(carJson);
    }

}
