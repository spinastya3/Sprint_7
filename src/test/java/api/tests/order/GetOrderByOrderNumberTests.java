package api.tests.order;

import api.BaseTest;
import models.Order;
import service.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import service.OrderGenerator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrderByOrderNumberTests extends BaseTest {

    private final OrderClient orderClient = new OrderClient();

    private Response response;
    private Integer track;

    @Test
    @DisplayName("Получить заказ по его номеру")
    public void getOrderByNumberTest(){

        Order order = OrderGenerator.order();
        response = orderClient.create(order);

        track = response.path("track");

        response = orderClient.getOrderByTrack(track);
        response.then()
                .log().all()
                .statusCode(200)
                .and()
                .body("order", notNullValue());
    }

    @Test
    @DisplayName("Ошибка получения заказа без номера заказа")
    public void getOrderWithOutOrderNumberTest() {

        response = orderClient.getOrderByTrack();
        response.then()
                .log().all()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Ошибка получения заказа с несуществующим номером заказа")
    public void getOrderWithNoExistingOrderNumberTest(){

        response = orderClient.getOrderByTrack(1);
        response.then()
                .log().all()
                .statusCode(404)
                .and()
                .body("message", equalTo("Заказ не найден"));
    }

    @After
    public void tearDown() {

        if (track != null) {
            orderClient.delete(track);
        }
    }
}
