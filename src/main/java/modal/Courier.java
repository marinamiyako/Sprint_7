package modal;

public class Courier {
    private String login;
    private String password;
    private String firstName;
    private String id;

    // Конструктор с четырьмя параметрами
    public Courier(String login, String password, String firstName, String id){
        this.login = login;
        this.password = password;
    }

    // Новый конструктор с тремя параметрами
    public Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getId() {
        return id;
    }
}
