package grintsys.com.vanshop.entities.client;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alienware on 2/3/2017.
 */

public class DocumentListResponse {

    @SerializedName("client_card_code")
    private String clientCardCode;

    @SerializedName("client_name")
    private String clientName;

    private double balance;

    private String address;

    @SerializedName("credit_limit")
    private double creaditLimit;

    @SerializedName("in_orders")
    private double inOrders;

    @SerializedName("pay_condition")
    private String payCondition;

    @SerializedName("records")
    private List<Document> documents;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public String getClientCardCode() {
        return clientCardCode;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public double getCreaditLimit() {
        return creaditLimit;
    }

    public void setCreaditLimit(double creaditLimit) {
        this.creaditLimit = creaditLimit;
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
        this.clientCardCode = clientCardCode;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}
