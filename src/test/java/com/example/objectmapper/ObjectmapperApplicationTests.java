package com.example.objectmapper;

import com.example.objectmapper.domain.Car;
import com.example.objectmapper.domain.Request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@SpringBootTest
class ObjectmapperApplicationTests {

	static final String COLOR = "Black";
	static final String TYPE = "BMW";
	static final String YEAR = "1970";

	static final String[] COLORS = {"Black", "Red"};
	static final String[] TYPES = {"BMW", "FIAT"};

	@Test
	@DisplayName("java object to json file")
	void test1() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		Car car = new Car("yellow", "renault");
		objectMapper.writeValue(new File("target/car.json"), car);
	}

	@Test
	@DisplayName("java object to json string")
	void test2() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		Car car = new Car("yellow", "renault");
		String result = objectMapper.writeValueAsString(car);
		System.out.println(" >>>>>> "+result);
	}

	@Test
	@DisplayName("java object to json byte array")
	void test3() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		Car car = new Car("yellow", "renault");
		byte[] result = objectMapper.writeValueAsBytes(car);
		System.out.println(" byte[] >>>>>> "+result);
		System.out.println(" toString >>>>>> "+ Arrays.toString(result));
	}

	@Test
	@DisplayName("json string to java object")
	void test4() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = "{ \"color\" : \""+COLOR+"\", \"type\" : \""+TYPE+"\" }";
		Car car = objectMapper.readValue(json, Car.class);

		assertThat(car.getColor(), equalTo(COLOR));
		assertThat(car.getType(), equalTo(TYPE));
	}

	@Test
	@DisplayName("json string file to java object")
	void test5() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		Car car = objectMapper.readValue(new File("src/test/resources/json_car.json"), Car.class);

		assertThat(car.getColor(), equalTo(COLOR));
		assertThat(car.getType(), equalTo(TYPE));
	}

	@Test
	@DisplayName("json string file-url to java object")
	void test6() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		Car car = objectMapper.readValue(new URL("file:src/test/resources/json_car.json"), Car.class);

		assertThat(car.getColor(), equalTo(COLOR));
		assertThat(car.getType(), equalTo(TYPE));
	}

	@Test
	@DisplayName("json to jackson JsonNode")
	void test7() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = "{ \"color\" : \""+COLOR+"\", \"type\" : \""+TYPE+"\" }";
		JsonNode jsonNode = objectMapper.readTree(json);
		String color = jsonNode.get("color").asText();
		String type = jsonNode.get("type").asText();

		assertThat(color, equalTo(COLOR));
		assertThat(type, equalTo(TYPE));
	}

	@Test
	@DisplayName("JSON array string to java list")
	void test8() throws JsonProcessingException {
		String jsonCarArray = getJsonCarArray();

		ObjectMapper objectMapper = new ObjectMapper();
		List<Car> cars = objectMapper.readValue(jsonCarArray, new TypeReference<List<Car>>() {});

		String converted = objectMapper.writeValueAsString(cars).replaceAll(" ","");
		assertThat(converted, equalTo(jsonCarArray.replaceAll(" ","")));
	}

	private String getJsonCarArray() {
		String jsonCarArray = "[";
		for (int i = 0; i < COLORS.length; ++i){
			if(i != 0) {
				jsonCarArray += ", ";
			}
			jsonCarArray += "{ \"color\" : \"";
			jsonCarArray += COLORS[i];
			jsonCarArray += "\", \"type\" : \"";
			jsonCarArray += TYPES[i];
			jsonCarArray += "\" }";
		}
		jsonCarArray += "]";
		return jsonCarArray;
	}

	@Test
	@DisplayName("JSON array string to java map")
	void test9() throws JsonProcessingException {
		String json = "{ \"color\" : \""+COLOR+"\", \"type\" : \""+TYPE+"\" }";

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> cars = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});

		String converted = objectMapper.writeValueAsString(cars).replaceAll(" ","");
		assertThat(converted, equalTo(json.replaceAll(" ","")));
	}

	@Test
	@DisplayName("ignore new field")
	void test10() throws JsonProcessingException {
		String jsonString
				= "{ \"color\" : \""+COLOR+"\", \"type\" : \""+TYPE+"\", \"year\" : \""+ YEAR +"\" }";

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Car car = objectMapper.readValue(jsonString, Car.class);

		JsonNode jsonNodeRoot = objectMapper.readTree(jsonString);
		JsonNode jsonNodeYear = jsonNodeRoot.get("year");
		String year = jsonNodeYear.asText();

		assertThat(year, equalTo(YEAR));
		assertThat(car.getColor(), equalTo(COLOR));
		assertThat(car.getType(), equalTo(TYPE));
	}

	@Test
	@DisplayName("Date 타입을 포함하는 Request 오브젝트를 JSON 문자열로 출력한다.")
	void test11() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");
		objectMapper.setDateFormat(df);

		Car car = new Car(COLOR, TYPE);
		Request request = new Request(car, Date.valueOf(LocalDate.now()));

		String carAsString = objectMapper.writeValueAsString(request);
		System.out.println(carAsString);
	}

	@Test
	@DisplayName("Json array를 Object array로 변경한다.")
	void test12() throws JsonProcessingException {
		String jsonCarArray = getJsonCarArray();

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
		Car[] cars = objectMapper.readValue(jsonCarArray, Car[].class);

		String converted = objectMapper.writeValueAsString(cars).replaceAll(" ","");
		assertThat(converted, equalTo(jsonCarArray.replaceAll(" ","")));
	}

}
