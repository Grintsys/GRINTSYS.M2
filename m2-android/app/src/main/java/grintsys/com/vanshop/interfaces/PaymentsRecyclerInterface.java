package grintsys.com.vanshop.interfaces;

import android.view.View;

import grintsys.com.vanshop.entities.payment.Payment;

public interface PaymentsRecyclerInterface {

    void onPaymentSelected(View v, Payment payment);

}
