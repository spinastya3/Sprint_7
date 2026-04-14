package api.tests.order;

import api.BaseTest;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Courier;
import models.Order;
import org.junit.After;
import org.junit.Test;
import service.CourierClient;
import service.CourierGenerator;
import service.OrderClient;
import service.OrderGenerator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class AcceptOrderTests  extends BaseTest {

    private final OrderClient orderClient = new OrderClient();
    private final CourierClient courierClient = new CourierClient();
    private Response response;
    private Integer track;
    private Integer orderId;
    private Integer courierId;

    @Test
    @DisplayName("Принять заказ c валидными данными")
    public void acceptOrderSuccessTest() {

        Order order = OrderGenerator.order();
        Courier courier = CourierGenerator.courier();

        track = orderClient.create(order).path("track");
        orderId = orderClient.getOrderByTrack(track).path("order.id");

        courierClient.create(courier);
        courierId = courierClient.login(courier).path("id");

        response = orderClient.acceptOrder(orderId, courierId);
        response
                .then()
                .log().all()
                .statusCode(200)
                .and()
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Ошибка принятия заказа без передачи ID заказа")
    public void acceptOrderWithoutIDTest() {

        Courier courier = CourierGenerator.courier();

        courierClient.create(courier);
        courierId = courierClient.login(courier).path("id");

        response = orderClient.acceptOrderWithoutOrderID(courierId);
        response
                .then()
                .log().all()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Ошибка принятия заказа с несуществующим ID заказа")
    public void acceptOrderWithNoExistingOrderIDTest() {

        Courier courier = CourierGenerator.courier();

        courierClient.create(courier);
        courierId = courierClient.login(courier).path("id");

        response = orderClient.acceptOrder(1, courierId);
        response
                .then()
                .log().all()
                .statusCode(404)
                .and()
                .body("message", equalTo("Заказа с таким id не существует"));
    }

    @Test
    @DisplayName("Ошибка принятия заказа с несуществующим ID курьра")
    public void acceptOrderWithNoExistingCourierIDTest() {

        Order order = OrderGenerator.order();
        track = orderClient.create(order).path("track");
        orderId = orderClient.getOrderByTrack(track).path("order.id");

        response = orderClient.acceptOrder(orderId, 1);
        response
                .then()
                .log().all()
                .statusCode(404)
                .and()
                .body("message", equalTo("Курьера с таким id не существует"));
    }

    @Test
    @DisplayName("Ошибка принятия заказа без передачи ID курьра")
    public void acceptOrderWithoutCourierIDTest() {

        Order order = OrderGenerator.order();

        track = orderClient.create(order).path("track");
        orderId = orderClient.getOrderByTrack(track).path("order.id");

        response = orderClient.acceptOrderWithoutCourierID(orderId);
        response
                .then()
                .log().all()
                .statusCode(400)
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
