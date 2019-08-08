package grintsys.com.vanshop.entities.payment;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import grintsys.com.vanshop.entities.Metadata;

public class PaymentResponse {

    private Metadata metadata;

    @SerializedName("payments")
    private List<Payment> payments;

    public PaymentResponse() {
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaymentResponse that = (PaymentResponse) o;

        if (metadata != null ? !metadata.equals(that.metadata) : that.metadata != null) return false;
        return !(payments != null ? !payments.equals(that.payments) : that.payments != null);

    }

    @Override
    public int hashCode() {
        int result = metadata != null ? metadata.hashCode() : 0;
        result = 31 * result + (payments != null ? payments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PaymentResponse{" +
                "metadata=" + metadata +
                ", payments=" + payments +
                '}';
    }
}
