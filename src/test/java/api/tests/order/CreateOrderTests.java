package api.tests.order;

import api.BaseTest;
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
                {List.of("BLACK"), 201},
                {List.of("GREY"), 201},
                {List.of("BLACK", "GREY"), 201},
                {List.of(), 201}
        };
    }

    @Test
    @DisplayName("Создать заказ для разных самокатов")
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
        if (response != null && response.statusCode() == 201) {
            Integer track = response.path("track");
            if (track != null) {
                response = orderClient.delete(track);
            }
        }
    }
}

