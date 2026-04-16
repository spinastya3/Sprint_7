package api.tests.courier;

import api.BaseTest;
import static utils.TestData.faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Courier;
import org.junit.After;
import org.junit.Test;
import service.CourierGenerator;
import service.CourierClient;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class CreateCourierTests extends BaseTest {

    private final CourierClient courierClient = new CourierClient();

    private Response response;
    private Courier courierForTest;

    @Test
    @DisplayName("Проверяем код ответа для успешного создания курьера")
    @Description("Отправляем валидные данные курьера и проверяем успешное создание курьера и получение кода 201")
    public void createCourierTest() {

        courierForTest = CourierGenerator.courier();
        response = courierClient.create(courierForTest);
        response
                .then()
                .log().all()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Ошибка при создании дубликата курьера")
    @Description("Пытаемся создать второго курьера с тем же логином, проверяем получение ошибки 409")
    public void createDuplicateCourierTest() {

        courierForTest = CourierGenerator.courier();
        response = courierClient.create(courierForTest);

        Courier duplicateCourier = new Courier()
                .withLogin(courierForTest.getLogin())
                .withPassword("Tets")
                .withFirstName(faker.name().firstName());

        Response duplicateResponse = courierClient.create(duplicateCourier);
        duplicateResponse
                .then()
                .log().all()
                .statusCode(SC_CONFLICT)
                .and()
                .body("message", equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Ошибка при создании курьера без передачи логина в тело запроса")
    @Description("Пытаемся создать курьера без логина, проверяем получение ошибки 400")

    public void createCourierWithoutLoginTest() {

        courierForTest = new Courier()
                .withPassword("test")
                .withFirstName(faker.name().firstName());

        response = courierClient.create(courierForTest);
        response
                .then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Ошибка при создании курьера c логином \"\"")
    @Description("Пытаемся создать курьера без логина, проверяем получение ошибки 400")

    public void createCourierWithEmptyLoginTest() {

        courierForTest = new Courier()
                .withLogin("")
                .withPassword("test")
                .withFirstName(faker.name().firstName());

        response = courierClient.create(courierForTest);
        response
                .then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Ошибка при создании курьера c логином null")
    @Description("Пытаемся создать курьера без логина, проверяем получение ошибки 400")

    public void createCourierWithNullLoginTest() {

        courierForTest = new Courier()
                .withLogin(null)
                .withPassword("test")
                .withFirstName(faker.name().firstName());

        response = courierClient.create(courierForTest);
        response
                .then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Ошибка при создании курьера без передачи пароля в тело запроса")
    @Description("Пытаемся создать курьера без пароля, проверяем получение ошибки 400")

    public void createCourierWithoutPassTest() {

        courierForTest = new Courier()
                .withLogin(faker.name().username())
                .withFirstName(faker.name().firstName());

        response = courierClient.create(courierForTest);
        response
                .then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Ошибка при создании курьера c паролем \"\"")
    @Description("Пытаемся создать курьера без пароля, проверяем получение ошибки 400")

    public void createCourierWithEmptyPassTest() {

        courierForTest = new Courier()
                .withLogin(faker.name().username())
                .withPassword("")
                .withFirstName(faker.name().firstName());

        response = courierClient.create(courierForTest);
        response
                .then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Ошибка при создании курьера c паролем null")
    @Description("Пытаемся создать курьера без пароля, проверяем получение ошибки 400")

    public void createCourierWithNullPassTest() {

        courierForTest = new Courier()
                .withLogin(faker.name().username())
                .withPassword(null)
                .withFirstName(faker.name().firstName());

        response = courierClient.create(courierForTest);
        response
                .then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
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