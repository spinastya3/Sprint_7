package api.tests.courier;

import api.BaseTest;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Courier;
import org.junit.After;
import org.junit.Test;
import service.CourierClient;
import service.CourierGenerator;
import utils.RandomGenerator;

import static org.hamcrest.CoreMatchers.*;

public class AuthorisationCourierTests extends BaseTest {

    private final CourierClient courierClient = new CourierClient();
    private Response response;
    private Courier courier;

    @Test
    @DisplayName("Проверяем код ответа для успешной авторизации курьера")
    public void courierAuthorisationTest() {

        courier = CourierGenerator.courier();
        courierClient.create(courier);
        response = courierClient.login(courier);
        response
                .then()
                .log().all()
                .statusCode(200)
                .and()
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Ошибка при авторизации курьера без передачи логина в теле запроса")
    public void courierWithoutLoginAuthorisationTest() {

        courier = new Courier()
                .withPassword("555");

        response = courierClient.login(courier);
        response
                .then()
                .log().all()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Ошибка при авторизации курьера с логином null")
    public void courierWithNullLoginAuthorisationTest() {

        courier = new Courier()
                .withLogin(null)
                .withPassword("555");

        response = courierClient.login(courier);
        response
                .then()
                .log().all()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Ошибка при авторизации курьера с логином \"\"")
    public void courierWithEmptyLoginAuthorisationTest() {

        courier = new Courier()
                .withLogin("")
                .withPassword("555");

        response = courierClient.login(courier);
        response
                .then()
                .log().all()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }


    @Test
    @DisplayName("Ошибка при авторизации курьера без передачи пароля в теле запроса")
    public void courierWithoutPassAuthorisationTest() {

        courier = new Courier()
                .withLogin(RandomGenerator.randomString(5));

        response = courierClient.login(courier);
        response
                .then()
                .log().all()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Ошибка при авторизации курьера с паролем null")
    public void courierWithNullPassAuthorisationTest() {

        courier = new Courier()
                .withLogin(RandomGenerator.randomString(5))
                .withPassword(null);

        response = courierClient.login(courier);
        response
                .then()
                .log().all()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Ошибка при авторизации курьера с паролем \"\"")
    public void courierWithEmptyPassAuthorisationTest() {

        courier = new Courier()
                .withLogin(RandomGenerator.randomString(5))
                .withPassword("");

        response = courierClient.login(courier);
        response
                .then()
                .log().all()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Ошибка при авторизации курьера с неверным паролем")
    public void courierWrongPassAuthorisationTest() {

        courier = CourierGenerator.courier();
        courierClient.create(courier);

       Courier courierWrongPass = new Courier()
                .withLogin(courier.getLogin())
                .withPassword("wrongPass");

        response = courierClient.login(courierWrongPass);
        response
                .then()
                .log().all()
                .statusCode(404)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Ошибка при авторизации незарегестрированного курьера")
    public void courierNotInBaseAuthorisationTest() {

        courier = CourierGenerator.courier();

        response = courierClient.login(courier);
        response
                .then()
                .log().all()
                .statusCode(404)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    public void tearDown() {

        if (response != null && response.statusCode() == 200) {

            Integer id = response.path("id");

            if (id != null) {
                courierClient.delete(id);
            }
        }
    }
}