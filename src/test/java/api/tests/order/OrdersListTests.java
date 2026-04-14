package api.tests.order;

import api.BaseTest;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import service.OrderClient;

import static org.hamcrest.CoreMatchers.notNullValue;

public class OrdersListTests extends BaseTest {

   private final OrderClient orderClient = new OrderClient();

    @Test
    @DisplayName("Получить список заказов")
    public void getOrderListTest() {
        Response response = orderClient.ordersList();
        response.then()
                .log().all()
                .statusCode(200)
                .and()
                .body("orders", notNullValue());
    }
}
