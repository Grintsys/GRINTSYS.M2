package grintsys.com.vanshop.entities.invoice;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by serpel on 6/2/2017.
 */

public class InvoiceResponse {

    private List<Invoice> items;

    public List<Invoice> getInvoices() {
        return items;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.items = invoices;
    }
}
