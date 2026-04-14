package api.tests.courier;

import api.BaseTest;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Courier;
import org.junit.Test;
import service.CourierClient;
import service.CourierGenerator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class DeleteCourierTests extends BaseTest {

    private final CourierClient courierClient = new CourierClient();

    private Response response;

    @Test
    @DisplayName("Успешное удаление курьера")
    public void deleteCourierTest() {

        Courier courier = CourierGenerator.courier();
        courierClient.create(courier);

        Response loginResponse = courierClient.login(courier);
        Integer id = loginResponse.path("id");

        response = courierClient.delete(id);
        response
                .then()
                .log().all()
                .statusCode(200)
                .and()
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Ошибка удаления курьера без ID")
    public void deleteCourierWithoutId() {
        response = courierClient.delete();
        response
                .then()
                .log().all()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для удаления курьера"));
    }

    @Test
    @DisplayName("Ошибка удаления курьера c несуществующим ID")
    public void deleteCourierWithNotExistingId() {
        response = courierClient.delete(1);
        response
                .then().statusCode(404)
                .and()
                .body("message", equalTo("Курьера с таким id нет"));
    }
}
