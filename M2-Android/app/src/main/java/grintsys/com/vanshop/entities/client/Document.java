package grintsys.com.vanshop.entities.client;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by alienware on 2/1/2017.
 */

public class Document implements Serializable {

    private String documentCode;
    private String creationTime;
    private String dueDate;
    private Double totalAmount;
    private Double balanceDue;
    private Double overdueDays;
    private int docEntry;
    private boolean paymentSelected;
    private List<DocumentItem> items;

    public Document(){}

    public boolean getPaymentSelected() {
        return paymentSelected;
    }

    public void setPaymentSelected(boolean paymentSelected) {
        this.paymentSelected = paymentSelected;
    }

    public List<DocumentItem> getItems() {
        return items;
    }

    public void setItems(List<DocumentItem> items) {
        this.items = items;
    }

    public int getDocEntry() {
        return docEntry;
    }

    public void setDocEntry(int docEntry) {
        this.docEntry = docEntry;
    }

    public Double getBalanceDue() {
        return balanceDue;
    }

    public void setBalanceDue(Double balanceDue) {
        this.balanceDue = balanceDue;
    }

    public Double getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(Double overdueDays) {
        this.overdueDays = overdueDays;
    }

    public String getDocumentCode() {
        return documentCode;
    }

    public void setDocumentCode(String documentCode) {
        this.documentCode = documentCode;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getCreatedDate() {
        return creationTime;
    }

    public void setCreatedDate(String createdDate) {
        this.creationTime = createdDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
