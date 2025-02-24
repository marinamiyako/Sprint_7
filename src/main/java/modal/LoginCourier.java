package modal;

public class LoginCourier {
    private String login;
    private String password;

    public LoginCourier(String login, String password) {
        this.login = login;
        this.password = password;

    }

    public static LoginCourier fromCourier(Courier courier) {
        return new LoginCourier(courier.getLogin(), courier.getPassword());
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
