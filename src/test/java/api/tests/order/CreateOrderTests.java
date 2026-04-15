package api.tests.order;

import api.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Order;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import service.OrderClient;
import service.OrderGenerator;

import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTests extends BaseTest {

    private final OrderClient orderClient = new OrderClient();

    private Response response;

    private final List<String> colour;
    private final int expectedStatusCode;

    public CreateOrderTests(List<String> colour, int expectedStatusCode) {
        this.colour = colour;
        this.expectedStatusCode = expectedStatusCode;
    }

    @Parameterized.Parameters(name = "Цвет самоката: {0}")
    public static Object[] changeColor() {
        return new Object[][]{
                {List.of("BLACK"), SC_CREATED},
                {List.of("GREY"), SC_CREATED},
                {List.of("BLACK", "GREY"), SC_CREATED},
                {List.of(), SC_CREATED}
        };
    }

    @Test
    @DisplayName("Создать заказ для разных самокатов")
    @Description("Параметризованный тест: Создаем заказ с различными вариантами цвета, проверяем код 201")
    public void makeNewOrderTest() {
        Order order = OrderGenerator.order().withColor(colour);
        response = orderClient.create(order);
        response
                .then()
                .log().all()
                .statusCode(expectedStatusCode)
                .and()
                .body("track", notNullValue());
    }

    @After
    public void tearDown() {
        if (response != null && response.statusCode() == SC_CREATED) {
            Integer track = response.path("track");
            if (track != null) {
                response = orderClient.delete(track);
            }
        }
    }
}

