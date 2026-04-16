package api;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.junit.Before;

public class BaseTest {

    private static final String BASE_URL= "https://qa-scooter.praktikum-services.ru/";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }
}
