package grintsys.com.vanshop.entities.payment;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import grintsys.com.vanshop.entities.client.Client;

/**
 * Created by alienware on 3/29/2017.
 */

public class Payment implements Serializable {

    private int id;
    private String vendor;
    private String docEntry;
    private Double payedAmount;
    private String lastError;
    public String cardCode;
    private String comment;
    private Date date;
    private String creationTime;
    private Client client;
    private Cash cash;
    private Transfer transfer;
    private ArrayList<CheckPayment> checks;
    private ArrayList<InvoiceItem> invoices;
    private String status;
    private String statusText;
    private String referenceNumber;

    public Payment() {}

    public Payment(int id, String vendor, String docEntry, Double totalPaid, String lastError, Date date, Client client, Cash cash, Transfer transfer, ArrayList<CheckPayment> checks)
    {
        this.id = id;
        this.vendor = vendor;
        this.docEntry = docEntry;
        this.payedAmount = totalPaid;
        this.lastError = lastError;
        this.date = date;
        this.client = client;
        this.cash = cash;
        this.transfer = transfer;
        this.checks = checks;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getCreatedDate() {
        return creationTime;
    }

    public void setCreatedDate(String createdDate) {
        this.creationTime = createdDate;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<InvoiceItem> getInvoices() {
        return invoices;
    }

    public void setInvoices(ArrayList<InvoiceItem> invoices) {
        this.invoices = invoices;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getDocEntry() {
        return docEntry;
    }

    public void setDocEntry(String docEntry) {
        this.docEntry = docEntry;
    }

    public Double getTotalPaid() {
        return payedAmount;
    }

    public void setTotalPaid(Double totalPaid) {
        this.payedAmount = totalPaid;
    }

    public String getLastError() {
        return lastError;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Cash getCash() {
        return cash;
    }

    public void setCash(Cash cash) {
        this.cash = cash;
    }

    public Transfer getTransfer() {
        return transfer;
    }

    public void setTransfer(Transfer transfer) {
        this.transfer = transfer;
    }

    public ArrayList<CheckPayment> getChecks() {
        return checks;
    }

    public void setChecks(ArrayList<CheckPayment> checks) {
        this.checks = checks;
    }
}
