package modal;

public class Order {
    private String id;
    private String orderId;

    public Order(String id, String courierId) {
        this.id = id;
        this.orderId = courierId;
    }

    public String getId() {
        return id;
    }

    public String getCourierId() {
        return orderId;
    }
}
