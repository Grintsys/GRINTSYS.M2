package grintsys.com.vanshop.entities.payment;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by alienware on 4/5/2017.
 */

public class InvoiceItem implements Serializable {

    @SerializedName("doc_entry")
    public int docEntry;
    @SerializedName("document_number")
    public String documentNumber;
    @SerializedName("total_amount")
    public double totalAmount;
    @SerializedName("total_payed")
    public double payedAmount;

    public InvoiceItem(){ }

    public int getDocEntry() {
        return docEntry;
    }

    public void setDocEntry(int docEntry) {
        this.docEntry = docEntry;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getPayedAmount() {
        return payedAmount;
    }

    public void setPayedAmount(double payedAmount) {
        this.payedAmount = payedAmount;
    }
}
