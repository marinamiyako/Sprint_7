import client.ScooterClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import modal.CreateOrder;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class CreateOrderTests {
    private final ScooterClient scooterServiceClient = new ScooterClient(BASE_URI);
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";
    private CreateOrder createOrder;

    public CreateOrderTests(CreateOrder createOrder) {
        this.createOrder = createOrder;
    }
    // Параметризованные данные для тестов
    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {new CreateOrder("Роман", "Пупкин", "ул.Пушкина 4", "Спортивная", "87774477000", 3, "2025-04-12", "Ок", new String[]{"BLACK"})},
                {new CreateOrder("Марина", "Тян", "ул.Абая 25", "Комсомольская", "87071234567", 5, "2025-04-14", "Жду самокат!", new String[]{"GREY"})},
                {new CreateOrder("Лара", "Сергиенко", "Проспект Назарбаева 88", "Комсомольская", "89990000999", 7, "2025-04-11", "", new String[]{"BLACK", "GREY"})},
                {new CreateOrder("Сай", "Башкирова", "ул.Шевченко 7", "Комсомольская", "89995001025", 3, "2025-04-11", "", new String[]{})}
        };
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Проверка создания заказа с разными данными")
    public void createOrder_ok() {
        ValidatableResponse response = scooterServiceClient.createOrder(createOrder);
        response.assertThat()
                .statusCode(201)
                .body("track", Matchers.notNullValue());
    }
}
