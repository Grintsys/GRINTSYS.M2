package grintsys.com.vanshop.entities.order;

import java.util.List;

public class OrderResponse {

    public int totalCount;
    private List<Order> items;

    public OrderResponse() {
    }

    /*public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }*/

    public List<Order> getOrders() {
        return items;
    }

    public void setOrders(List<Order> orders) {
        this.items = orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderResponse that = (OrderResponse) o;

        return !(items != null ? !items.equals(that.items) : that.items != null);
    }

    @Override
    public int hashCode() {
        int result = 31 + (items != null ? items.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                ", orders=" + items +
                '}';
    }
}
