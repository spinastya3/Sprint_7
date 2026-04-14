package api.tests.courier;

import api.BaseTest;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Courier;
import org.junit.After;
import org.junit.Test;
import service.CourierGenerator;
import service.CourierClient;
import utils.RandomGenerator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class CreateCourierTests extends BaseTest {

    private final CourierClient courierClient = new CourierClient();

    private Response response;
    private Courier courierForTest;

    @Test
    @DisplayName("Проверяем код ответа для успешного создания курьера")
    public void createCourierTest() {

        courierForTest = CourierGenerator.courier();
        response = courierClient.create(courierForTest);
        response
                .then()
                .log().all()
                .statusCode(201)
                .and()
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Ошибка при создании дубликата курьера")
    public void createDuplicateCourierTest() {

        courierForTest = CourierGenerator.courier();
        response = courierClient.create(courierForTest);

        Courier duplicateCourier = new Courier()
                .withLogin(courierForTest.getLogin())
                .withPassword("777")
                .withFirstName("Garry" + RandomGenerator.randomString(3));

        Response duplicateResponse = courierClient.create(duplicateCourier);
        duplicateResponse
                .then()
                .log().all()
                .statusCode(409)
                .and()
                .body("message", equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Ошибка при создании курьера без передачи логина в тело запроса")
    public void createCourierWithoutLoginTest() {

        courierForTest = new Courier()
                .withPassword("555")
                .withFirstName("Garry" + RandomGenerator.randomString(3));

        response = courierClient.create(courierForTest);
        response
                .then()
                .log().all()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Ошибка при создании курьера c логином \"\"")
    public void createCourierWithEmptyLoginTest() {

        courierForTest = new Courier()
                .withLogin("")
                .withPassword("555")
                .withFirstName("Garry" + RandomGenerator.randomString(3));

        response = courierClient.create(courierForTest);
        response
                .then()
                .log().all()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Ошибка при создании курьера c логином null")
    public void createCourierWithNullLoginTest() {

        courierForTest = new Courier()
                .withLogin(null)
                .withPassword("555")
                .withFirstName("Garry" + RandomGenerator.randomString(3));

        response = courierClient.create(courierForTest);
        response
                .then()
                .log().all()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Ошибка при создании курьера без передачи пароля в тело запроса")
    public void createCourierWithoutPassTest() {

        courierForTest = new Courier()
                .withLogin("Potter" + RandomGenerator.randomString(3))
                .withFirstName("Garry" + RandomGenerator.randomString(3));

        response = courierClient.create(courierForTest);
        response
                .then()
                .log().all()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Ошибка при создании курьера c паролем \"\"")
    public void createCourierWithEmptyPassTest() {

        courierForTest = new Courier()
                .withLogin("Potter" + RandomGenerator.randomString(3))
                .withPassword("")
                .withFirstName("Garry" + RandomGenerator.randomString(3));

        response = courierClient.create(courierForTest);
        response
                .then()
                .log().all()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Ошибка при создании курьера c паролем null")
    public void createCourierWithNullPassTest() {

        courierForTest = new Courier()
                .withLogin("Potter" + RandomGenerator.randomString(3))
                .withPassword(null)
                .withFirstName("Garry" + RandomGenerator.randomString(3));

        response = courierClient.create(courierForTest);
        response
                .then()
                .log().all()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void tearDown() {

        if (response != null && response.statusCode() == 201) {

            Response loginResponse = courierClient.login(courierForTest);
            Integer id = loginResponse.path("id");

            if (id != null) {
                courierClient.delete(id);
            }
        }
    }
}