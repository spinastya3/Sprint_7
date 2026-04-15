package api.tests.courier;

import api.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.CourierClient;
import service.CourierGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;

public class AuthorisationCourierTests extends BaseTest {

    private final CourierClient courierClient = new CourierClient();

    private Response response;
    private Courier courier;
    private Integer courierId;

    @Before
    public void createCourier() {

        courier = CourierGenerator.courier();
        courierClient.create(courier);
        courierId = courierClient.login(courier).path("id");
    }

    @Test
    @DisplayName("Проверяем код ответа для успешной авторизации курьера")
    @Description("Отправляем валидные данные курьера и проверяем получение ID и кода 200")
    public void courierAuthorisationTest() {

        response = courierClient.login(courier);
        response
                .then()
                .log().all()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Ошибка при авторизации курьера с неверным паролем")
    @Description("Пытаемся авторизовать курьера с неверным паролем, проверяем получение ошибки 404")

    public void courierWrongPassAuthorisationTest() {

        courier.setPassword(courier.getPassword()+ "wrongPass");

        response = courierClient.login(courier);
        response
                .then()
                .log().all()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Ошибка при авторизации незарегестрированного курьера")
    @Description("Пытаемся авторизовать незарегестрированного курьера, проверяем получение ошибки 404")

    public void courierNotInBaseAuthorisationTest() {

        courier.setLogin(courier.getLogin()+ "wrongLogin");

        response = courierClient.login(courier);
        response
                .then()
                .log().all()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    public void tearDown() {

        if (courierId != null) {
            courierClient.delete(courierId);
        }
    }
}
