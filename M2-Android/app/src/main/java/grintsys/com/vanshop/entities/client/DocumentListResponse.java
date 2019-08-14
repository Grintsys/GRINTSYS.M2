package grintsys.com.vanshop.entities.client;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alienware on 2/3/2017.
 */

public class DocumentListResponse {


    private long id;
    private String name;
    private String cardCode;
    private String contactPerson;
    private String phoneNumber;
    private double creditLimit;
    private double balance;
    private double inOrders;
    private String payCondition;
    private String email;
    private String address;
    private String rtn;
    private double pastDue;

    private List<Document> invoices;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Document> getDocuments() {
        return invoices;
    }

    public String getClientCardCode() {
        return cardCode;
    }

    public String getClientName() {
        return name;
    }

    public void setClientName(String clientName) {
        this.name = clientName;
    }

    public double getCreaditLimit() {
        return creditLimit;
    }

    public void setCreaditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getInOrders() {
        return inOrders;
    }

    public void setInOrders(double inOrders) {
        this.inOrders = inOrders;
    }

    public String getPayCondition() {
        return payCondition;
    }

    public void setPayCondition(String payCondition) {
        this.payCondition = payCondition;
    }

    public void setClientCardCode(String clientCardCode) {
        this.cardCode = clientCardCode;
    }

    public void setDocuments(List<Document> documents) {
        this.invoices = documents;
    }
}
