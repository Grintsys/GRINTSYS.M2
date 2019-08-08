package grintsys.com.vanshop.entities.client;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import grintsys.com.vanshop.entities.product.ProductListResponse;

/**
 * Created by alienware on 2/2/2017.
 */

public class ClientListResponse {

    @SerializedName("records")
    private List<Client> clients;

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductListResponse)) return false;

        ClientListResponse that = (ClientListResponse) o;

        return !(getClients() != null ? !getClients().equals(that.getClients()) : that.getClients() != null);
    }

    @Override
    public int hashCode() {
        int result = 31 * (getClients() != null ? getClients().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductListResponse{" +
                ", clients=" + clients +
                '}';
    }
}

