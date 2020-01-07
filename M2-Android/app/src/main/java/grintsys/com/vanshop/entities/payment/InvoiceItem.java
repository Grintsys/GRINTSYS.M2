package grintsys.com.vanshop.entities.payment;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

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
    @SerializedName("balance_due")
    public double balanceDue;
    @SerializedName("payed_amount")
    public double payedAmount;
    @SerializedName("overdue_days")
    public Double overdueDays;

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

    public double getBalanceDue() { return balanceDue; }

    public void setBalanceDue(double balanceDue) { this.balanceDue = balanceDue; }

    public double getPayedAmount() { return payedAmount; }

    public void setPayedAmount(double payedAmount) { this.payedAmount = payedAmount; }

    public Double getOverdueDays() { return overdueDays; }

    public void setOverdueDays(Double documentDate) { this.overdueDays = documentDate; }
}