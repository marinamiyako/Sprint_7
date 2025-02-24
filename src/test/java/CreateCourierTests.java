import client.ScooterClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import modal.Courier;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateCourierTests {
    private Courier courier;
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";
    private final ScooterClient client = new ScooterClient(BASE_URI);
    private String login = RandomStringUtils.randomAlphabetic(10);


    @Test
    @DisplayName("Создание курьера")
    @Description("Проверка статус кода и значений полей")
    public void createCourier_ok() {
        courier = new Courier(login, "12345678", "Vika");
        ValidatableResponse response =
                given()
                        .log()
                        .all()
                        .baseUri(BASE_URI)
                        .header("Content-Type", "application/json")
                        .body(courier)
                        .post("/api/v1/courier")
                        .then()
                        .log()
                        .all();
        response.assertThat().statusCode(201).body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Проверка статус кода и значений курьера без логина")
    public void createCourierWithoutLogin() {
        courier = new Courier("", "12345678", "Vika");
        ValidatableResponse response =
                given()
                        .log()
                        .all()
                        .baseUri(BASE_URI)
                        .header("Content-Type", "application/json")
                        .body(courier)
                        .post("/api/v1/courier")
                        .then()
                        .log()
                        .all();
        response.assertThat().statusCode(400)
                .body("message", Matchers.is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Проверка статус кода и значений курьера без пароля")
    public void createCourierWithoutPassword() {
        courier = new Courier("NeHeHe123", "", "Vika");
        ValidatableResponse response =
                given()
                        .log()
                        .all()
                        .baseUri(BASE_URI)
                        .header("Content-Type", "application/json")
                        .body(courier)
                        .post("/api/v1/courier")
                        .then()
                        .log()
                        .all();
        response.assertThat().statusCode(400)
                .body("message", Matchers.is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание копии курьера")
    @Description("Проверка статус кода и значений копии курьера")
    public void createSecondIdenticalCourier() {
        // Пытаемся создать курьера с тем же логином
        Courier duplicateCourier = new Courier("NeHeHe123", "12345678", "Vika");
        ValidatableResponse response =
                given()
                        .log()
                        .all()
                        .baseUri(BASE_URI)
                        .header("Content-Type", "application/json")
                        .body(duplicateCourier)
                        .post("/api/v1/courier")
                        .then()
                        .log()
                        .all();
        response.assertThat().statusCode(409)
                .body("message", Matchers.is("Этот логин уже используется. Попробуйте другой."));
    }

    @After
    public void after() {
        if (courier != null && courier.getLogin() != null && !courier.getLogin().isEmpty()) {
            String id = client.getIdCourier(courier);
            if (id != null) {
                client.deleteCourier(id);
            }
        }
    }
}
