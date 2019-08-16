package grintsys.com.vanshop.entities.payment;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import grintsys.com.vanshop.entities.Metadata;

public class PaymentResponse {

    private Metadata metadata;

    private List<Payment> items;

    public PaymentResponse() {
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public List<Payment> getPayments() {
        return items;
    }

    public void setPayments(List<Payment> payments) {
        this.items = payments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaymentResponse that = (PaymentResponse) o;

        if (metadata != null ? !metadata.equals(that.metadata) : that.metadata != null) return false;
        return !(items != null ? !items.equals(that.items) : that.items != null);

    }

    @Override
    public int hashCode() {
        int result = metadata != null ? metadata.hashCode() : 0;
        result = 31 * result + (items != null ? items.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PaymentResponse{" +
                "metadata=" + metadata +
                ", payments=" + items +
                '}';
    }
}
