package com.example.objectmapper.objectMapperUtils;

import com.example.objectmapper.domain.Car;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CarDeserializerTest {

    final static String COLOR = "YELLOW";
    final static String TYPE = "RENAULT";

    @Test
    @DisplayName("")
    void test1() throws JsonProcessingException {
        String json =  "{ \"color\" : \""+COLOR+"\", \"type\" : \""+TYPE+"\" }";
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("CustomCarDeserializer", new Version(1, 0, 0, null, null, null));
        module.addDeserializer(Car.class, new CarDeserializer());
        mapper.registerModule(module);
        Car car = mapper.readValue(json, Car.class);

        assertThat(car.getColor(), equalTo(COLOR));
        assertThat(Optional.ofNullable(car.getType()).isPresent(), equalTo(false));
    }
}
