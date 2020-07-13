package com.example.objectmapper;

import com.example.objectmapper.domain.Car;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ObjectmapperApplicationTests {

	@Test
	void test1() {
		ObjectMapper objectMapper = new ObjectMapper();
		Car car = new Car("yellow", "renault");
	}

}
