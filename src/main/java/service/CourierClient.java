package service;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Courier;
import static io.restassured.RestAssured.given;

public class CourierClient {

    private static final String COURIER_BASE_PATH = "/api/v1/courier";

    @Step("Создание курьера")
    public Response create(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .log().all()
                .body(courier)
                .post(COURIER_BASE_PATH);
    }

    @Step("Курьер логинится в системе")
    public Response login(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .log().all()
                .body(courier)
                .post(COURIER_BASE_PATH + "/login");
    }

    @Step("Удаление курьера без ID")
    public Response delete() {
        return given()
                .header("Content-type", "application/json")
                .log().all()
                .when()
                .delete(COURIER_BASE_PATH);
    }

    @Step("Удаление курьера")
    public Response delete(Object id) {
        return given()
                .header("Content-type", "application/json")
                .log().all()
                .when()
                .delete(COURIER_BASE_PATH + "/" + id);
    }
}