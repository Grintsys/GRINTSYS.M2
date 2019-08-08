package grintsys.com.vanshop.entities.payment;

import java.util.ArrayList;

/**
 * Created by alienware on 3/29/2017.
 */

public class PaymentListResponse {

    private ArrayList<Payment> payments;

    public ArrayList<Payment> getPayments() {
        return payments;
    }

    public void setPayments(ArrayList<Payment> payments) {
        this.payments = payments;
    }
}
