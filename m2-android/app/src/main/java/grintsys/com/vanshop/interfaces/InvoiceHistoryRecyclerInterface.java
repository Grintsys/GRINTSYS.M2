package grintsys.com.vanshop.interfaces;

import android.view.View;

import grintsys.com.vanshop.entities.client.Client;
import grintsys.com.vanshop.entities.invoice.Invoice;

/**
 * Created by alienware on 2/1/2017.
 */

public interface InvoiceHistoryRecyclerInterface {
    void onInvoiceHistorySelected(View view, Invoice invoice);
}
