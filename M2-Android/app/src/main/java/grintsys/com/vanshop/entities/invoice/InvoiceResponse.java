package grintsys.com.vanshop.entities.invoice;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by serpel on 6/2/2017.
 */

public class InvoiceResponse {

    private List<Invoice> invoices;

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }
}
