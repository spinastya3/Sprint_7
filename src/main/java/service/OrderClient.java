package service;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String ORDER_BASE_PATH = "/api/v1/orders";


    @Step("Создание заказа")
    public Response create(Order order){
       return given()
               .header("Content-type", "application/json")
               .log().all()
                .body(order)
                .post(ORDER_BASE_PATH);
    }

    @Step("Получение списка заказов")
    public Response ordersList(){
        return given()
                .header("Content-type", "application/json")
                .log().all()
                .get(ORDER_BASE_PATH);
    }

    @Step("Получить заказ по номеру")
    public Response getOrderByTrack(Object track){
        return given()
                .header("Content-type", "application/json")
                .log().all()
                .queryParam("t", track)
                .get(ORDER_BASE_PATH + "/track");
    }

    @Step("Получить заказ без номера")
    public Response getOrderByTrack() {
        return given()
                .header("Content-type", "application/json")
                .log().all()
                .get(ORDER_BASE_PATH + "/track");
    }

    @Step("Принять заказ")
    public Response acceptOrder(Object orderId, Object courierId){
        return given()
                .header("Content-type", "application/json")
                .log().all()
                .pathParam("id", orderId)
                .queryParam("courierId", courierId)
                .put(ORDER_BASE_PATH + "/accept/{id}");
    }

    @Step("Принять заказ без ID заказа ")
    public Response acceptOrderWithoutOrderID(Object courierId){
        return given()
                .header("Content-type", "application/json")
                .log().all()
                .queryParam("courierId", courierId)
                .put(ORDER_BASE_PATH + "/accept/");
    }

    @Step("Принять заказ без ID курьера")
    public Response acceptOrderWithoutCourierID(Object orderId){
        return given()
                .header("Content-type", "application/json")
                .log().all()
                .pathParam("id", orderId)
                .put(ORDER_BASE_PATH + "/accept/{id}");
    }

    @Step("Завершение заказа")
    public void finish(Object orderId){
        given()
                .header("Content-type", "application/json")
                .log().all()
                .put(ORDER_BASE_PATH + "/finish/" + orderId);
    }

    @Step("Удаление заказа")
    public Response delete(Object track){
        return given()
                .header("Content-type", "application/json")
                .log().all()
                .queryParam("track", track)
                .put(ORDER_BASE_PATH + "/cancel");
    }
}
