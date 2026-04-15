package api.tests.courier;

import api.BaseTest;
import static utils.TestData.faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Courier;
import org.junit.Test;
import service.CourierClient;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.CoreMatchers.equalTo;

public class AuthorisationCourierNegativeTests extends BaseTest {

    private final CourierClient courierClient = new CourierClient();

    private Response response;
    private Courier courier;

    @Test
    @DisplayName("Ошибка при авторизации курьера без передачи логина в теле запроса")
    @Description("Пытаемся авторизовать курьера без логина в запросе, проверяем получение ошибки 400")
    public void courierWithoutLoginAuthorisationTest() {

        courier = new Courier()
                .withPassword("test");

        response = courierClient.login(courier);
        response
                .then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Ошибка при авторизации курьера с логином null")
    @Description("Пытаемся авторизовать курьера без логина в запросе, проверяем получение ошибки 400")
    public void courierWithNullLoginAuthorisationTest() {

        courier = new Courier()
                .withLogin(null)
                .withPassword("test");

        response = courierClient.login(courier);
        response
                .then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Ошибка при авторизации курьера с логином \"\"")
    @Description("Пытаемся авторизовать курьера без логина в запросе, проверяем получение ошибки 400")
    public void courierWithEmptyLoginAuthorisationTest() {

        courier = new Courier()
                .withLogin("")
                .withPassword("test");

        response = courierClient.login(courier);
        response
                .then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }


    @Test
    @DisplayName("Ошибка при авторизации курьера без передачи пароля в теле запроса")
    @Description("Пытаемся авторизовать курьера без пароля в запросе, проверяем получение ошибки 400")
    public void courierWithoutPassAuthorisationTest() {

        courier = new Courier()
                .withLogin(faker.name().username());

        response = courierClient.login(courier);
        response
                .then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Ошибка при авторизации курьера с паролем null")
    @Description("Пытаемся авторизовать курьера без пароля в запросе, проверяем получение ошибки 400")
    public void courierWithNullPassAuthorisationTest() {

        courier = new Courier()
                .withLogin(faker.name().username())
                .withPassword(null);

        response = courierClient.login(courier);
        response
                .then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Ошибка при авторизации курьера с паролем \"\"")
    @Description("Пытаемся авторизовать курьера без пароля в запросе, проверяем получение ошибки 400")
    public void courierWithEmptyPassAuthorisationTest() {

        courier = new Courier()
                .withLogin(faker.name().username())
                .withPassword("");

        response = courierClient.login(courier);
        response
                .then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }
}
