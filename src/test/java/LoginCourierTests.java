import client.ScooterClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import modal.Courier;
import modal.LoginCourier;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;

public class LoginCourierTests {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";
    private Courier courier;
    private ScooterClient client;
    private ScooterClient scooterClient;

    @Before
    public void before() {
        scooterClient = new ScooterClient(BASE_URI);
        client = new ScooterClient(BASE_URI);
        courier = new Courier("NeHeHe123", "1234", "Masha");
        ValidatableResponse response = client.createCourier(courier);
        Assume.assumeTrue(response.extract().statusCode() == 201);
    }

    @Test
    @DisplayName("Логин курьера")
    @Description("Проверка статус кода и сообщения авторизации")
    public void login_ok() {
        LoginCourier loginCourier = new LoginCourier(courier.getLogin(), courier.getPassword());
        ValidatableResponse response = scooterClient.login(loginCourier);
        response.assertThat()
                .statusCode(200)
                .body("id", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Логин курьера без пароля")
    @Description("Проверка статус кода и сообщения авторизации без пароля")
    public void loginWithEmptyPassword() {
        LoginCourier loginCourier = new LoginCourier("NeHeHe123", "");
        ValidatableResponse response = scooterClient.login(loginCourier);
        response.assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин курьера без логина")
    @Description("Проверка статус кода и сообщения авторизации без логина")
    public void loginWithEmptyUserName() {
        LoginCourier loginCourier = new LoginCourier("", "1234");
        ValidatableResponse response = scooterClient.login(loginCourier);
        response.assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин курьера с несуществующими данными")
    @Description("Проверка статус кода и сообщения при отсутствии курьера в БД")
    public void loginNonExistCourier() {
        LoginCourier loginCourier = new LoginCourier("hehe", "4321");
        ValidatableResponse response = scooterClient.login(loginCourier);
        response.assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    public void after() {
        String id = scooterClient.getIdCourier(courier);
        assertNotNull(id, "Id курьера не должен быть null");
        scooterClient.deleteCourier(id); // Удаляем курьера
    }
}
