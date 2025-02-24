package client;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.ValidatableResponse;
import modal.Courier;
import modal.CreateOrder;
import modal.LoginCourier;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ScooterClient {
    private final String baseURI;

    public ScooterClient(String baseURI){
        this.baseURI = baseURI;
    }
    @Step("Создание курьера")
    public ValidatableResponse createCourier(Courier courier){
        return given()
                .filter(new AllureRestAssured())
                .log()
                .all()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .body(courier)
                .post("/api/v1/courier")
                .then()
                .log().all();
    }
    @Step("Логин курьера")
    public ValidatableResponse login (LoginCourier loginCourier){
        return given()
                .filter(new AllureRestAssured())
                .log()
                .all()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .body(loginCourier)
                .post("/api/v1/courier/login")
                .then()
                .log().all();
    }

    @Step("Получение списка заказов")
    public ValidatableResponse getOrders(){
        return given()
                .filter(new AllureRestAssured())
                .log()
                .all()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .queryParam("limit", "2")
                .get("/api/v1/orders")
                .then()
                .log().all();
    }

    @Step("Удаление курьера")
    public ValidatableResponse deleteCourier(String id) {
        return given()
                .filter(new AllureRestAssured())
                .log().all()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .delete("/api/v1/courier/" + id)
                .then()
                .log()
                .all()
                .statusCode(200);
    }

    @Step("Получение id курьера")
    public String getIdCourier(Courier courier) {
        ValidatableResponse response =
                given()
                        .log().all()
                        .baseUri(baseURI)
                        .header("Content-Type", "application/json")
                        .body(Map.of("login", courier.getLogin(), "password", courier.getPassword())) // предполагаем, что тело запроса должно быть отправлено
                        .post("/api/v1/courier/login") // исправлено "delete" на "post"
                        .then()
                        .log()
                        .all();

        String id = response
                .extract()
                .jsonPath()
                .getString("id");
        return id;
    }

    @Step("Создание заказа")
    public ValidatableResponse createOrder(CreateOrder createOrder) {
        return given()
                .filter(new AllureRestAssured())
                .log().all()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .body(createOrder)
                .post("/api/v1/orders")
                .then()
                .log().all();
    }
}
