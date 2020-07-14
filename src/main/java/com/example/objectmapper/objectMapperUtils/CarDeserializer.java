package com.example.objectmapper.objectMapperUtils;

import com.example.objectmapper.domain.Car;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class CarDeserializer extends StdDeserializer<Car> {

    public CarDeserializer() {
        this(null);
    }

    public CarDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Car deserialize(JsonParser parser, DeserializationContext deserializer) throws IOException, JsonProcessingException {
        Car car = new Car();
        ObjectCodec codec = parser.getCodec();
        JsonNode node = codec.readTree(parser);

        JsonNode colorNode = node.get("color");
        String color = colorNode.asText();
        car.setColor(color);

        return car;
    }
}
