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
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class DeleteCourierTests extends BaseTest {

    private final CourierClient courierClient = new CourierClient();

    private Response response;
    private Integer courierId;

    @Before
    public void createCourier() {

        Courier courier = CourierGenerator.courier();
        courierClient.create(courier);
        courierId = courierClient.login(courier).path("id");
    }

    @Test
    @DisplayName("Успешное удаление курьера")
    @Description("Удаляем созданного курьера, проверяем код 200")

    public void deleteCourierTest() {

        response = courierClient.delete(courierId);
        response
                .then()
                .log().all()
                .statusCode(SC_OK)
                .and()
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Ошибка удаления курьера без ID")
    @Description("Пытаемся удалить курьера без ID, проверяем получение ошибки 400")

    public void deleteCourierWithoutId() {
        response = courierClient.delete();
        response
                .then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для удаления курьера"));
    }

    @Test
    @DisplayName("Ошибка удаления курьера c несуществующим ID")
    @Description("Пытаемся удалить курьера c несуществующим ID, проверяем получение ошибки 404")

    public void deleteCourierWithNotExistingId() {
        response = courierClient.delete(1);
        response
                .then()
                .log().all()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Курьера с таким id нет"));
    }

    @After
    public void tearDown() {

        if (courierId != null) {
            courierClient.delete(courierId);
        }
    }
}
