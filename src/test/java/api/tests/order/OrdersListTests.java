package api.tests.order;

import api.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import service.OrderClient;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrdersListTests extends BaseTest {

   private final OrderClient orderClient = new OrderClient();

    @Test
    @DisplayName("Получить список заказов")
    @Description("Получаем список заказов, проверяем код 200")

    public void getOrderListTest() {
        Response response = orderClient.ordersList();
        response.then()
                .log().all()
                .statusCode(SC_OK)
                .and()
                .body("orders", notNullValue());
    }
}
