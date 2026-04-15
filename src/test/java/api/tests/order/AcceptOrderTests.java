package api.tests.order;

import api.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Courier;
import models.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.CourierClient;
import service.CourierGenerator;
import service.OrderClient;
import service.OrderGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class AcceptOrderTests  extends BaseTest {

    private final OrderClient orderClient = new OrderClient();
    private final CourierClient courierClient = new CourierClient();

    private Response response;
    private Integer track;
    private Integer orderId;
    private Integer courierId;

    @Before
    public void createOrderAndCourier(){

        Order order = OrderGenerator.order();
        Courier courier = CourierGenerator.courier();

        courierClient.create(courier);
        track = orderClient.create(order).path("track");
        orderId = orderClient.getOrderByTrack(track).path("order.id");
        courierId = courierClient.login(courier).path("id");

    }

    @Test
    @DisplayName("Принять заказ c валидными данными")
    @Description("Принимаем заказ с валидными данными, проверяем код 200")

    public void acceptOrderSuccessTest() {

        response = orderClient.acceptOrder(orderId, courierId);
        response
                .then()
                .log().all()
                .statusCode(SC_OK)
                .and()
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Ошибка принятия заказа без передачи ID заказа")
    @Description("Пытаемся принять заказ без ID заказа, проверяем код 400")

    public void acceptOrderWithoutIDTest() {

        response = orderClient.acceptOrderWithoutOrderID(courierId);
        response
                .then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Ошибка принятия заказа с несуществующим ID заказа")
    @Description("Пытаемся принять заказ с несуществующим ID заказа, проверяем код 404")

    public void acceptOrderWithNoExistingOrderIDTest() {

        response = orderClient.acceptOrder(1, courierId);
        response
                .then()
                .log().all()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Заказа с таким id не существует"));
    }

    @Test
    @DisplayName("Ошибка принятия заказа с несуществующим ID курьра")
    @Description("Пытаемся принять заказ с несуществующим ID курьера, проверяем код 404")

    public void acceptOrderWithNoExistingCourierIDTest() {

        response = orderClient.acceptOrder(orderId, 1);
        response
                .then()
                .log().all()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Курьера с таким id не существует"));
    }

    @Test
    @DisplayName("Ошибка принятия заказа без передачи ID курьра")
    @Description("Пытаемся принять заказ без ID курьера, проверяем код 400")

    public void acceptOrderWithoutCourierIDTest() {

        response = orderClient.acceptOrderWithoutCourierID(orderId);
        response
                .then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @After
    public void tearDown() {

        if (orderId != null) {
            orderClient.finish(orderId);
        }

        if (track != null) {
            orderClient.delete(track);
        }

        if (courierId != null) {
            courierClient.delete(courierId);
        }
    }
}
