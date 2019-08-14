package grintsys.com.vanshop.interfaces;

import android.view.View;

import grintsys.com.vanshop.entities.payment.CheckPayment;

/**
 * Created by alienware on 3/31/2017.
 */

public interface ChecksRecyclerInterface {
    void onCheckSelected(View view, CheckPayment checkPayment);
}
