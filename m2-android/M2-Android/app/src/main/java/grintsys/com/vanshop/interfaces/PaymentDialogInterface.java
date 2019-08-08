package grintsys.com.vanshop.interfaces;


import grintsys.com.vanshop.entities.delivery.Payment;

public interface PaymentDialogInterface {
    void onPaymentSelected(Payment payment);
}
