package api.tests.order;

import api.BaseTest;
import io.qameta.allure.Description;
import models.Order;
import org.junit.Before;
import service.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import service.OrderGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrderByOrderNumberTests extends BaseTest {

    private final OrderClient orderClient = new OrderClient();

    private Response response;
    private Integer track;

    @Before
    public void createOrder(){

        Order order = OrderGenerator.order();
        response = orderClient.create(order);
        track = response.path("track");
    }

    @Test
    @DisplayName("Получить заказ по его номеру")
    @Description("Получаем заказ по номеру, проверяем код 200")

    public void getOrderByNumberTest(){

        response = orderClient.getOrderByTrack(track);
        response.then()
                .log().all()
                .statusCode(SC_OK)
                .and()
                .body("order", notNullValue());
    }

    @Test
    @DisplayName("Ошибка получения заказа без номера заказа")
    @Description("Пытаемся получить заказ без номера, проверяем код 400")

    public void getOrderWithOutOrderNumberTest() {

        response = orderClient.getOrderByTrack();
        response.then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Ошибка получения заказа с несуществующим номером заказа")
    @Description("Пытаемся получить заказ с несуществующим номера, проверяем код 404")

    public void getOrderWithNoExistingOrderNumberTest(){

        response = orderClient.getOrderByTrack(1);
        response.then()
                .log().all()
                .statusCode(SC_NOT_FOUND)
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
