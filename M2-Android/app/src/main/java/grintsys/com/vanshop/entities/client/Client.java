package grintsys.com.vanshop.entities.client;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by alienware on 2/1/2017.
 */

public class Client implements Serializable {

    private int id;
    private String name;
    private String cardCode;
    private String contactPerson;
    private String phoneNumber;
    private double creditLimit;
    private double balance;
    private double inOrders;
    private int payCondition;
    private String email;
    private String address;
    private String rtn;
    private double pastDue;

    @SerializedName("invoices")
    private ArrayList<Document> invoiceList;

    public Client(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
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

    public int getPayCondition() {
        return payCondition;
    }

    public void setPayCondition(int payCondition) {
        this.payCondition = payCondition;
    }

    public String getRtn() {
        return rtn;
    }

    public void setRtn(String rtn) {
        this.rtn = rtn;
    }

    public double getPastDue() {
        return pastDue;
    }

    public void setPastDue(double pastDue) {
        this.pastDue = pastDue;
    }

    public String getAddress() {
        if(address!=null)
            return address;
        return "";
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRTN() {
        if(rtn != null)
            return rtn;
        return "";
    }

    public String getContact() {
        return contactPerson;
    }

    public void setContact(String contact) {
        this.contactPerson = contact;
    }

    public void setRTN(String RTN) {
        this.rtn = rtn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardCode() {
        if(cardCode!=null)
            return cardCode;
        return "";
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getPhone() {
        if(phoneNumber!=null)
            return phoneNumber;
        return "";
    }

    public void setPhone(String phone) {
        this.phoneNumber = phone;
    }

    public String getEmail() {
        if(email!=null)
            return email;
        return "";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Document> getInvoiceList() {
        return invoiceList;
    }

    public void setInvoiceList(ArrayList<Document> invoiceList) {
        this.invoiceList = invoiceList;
    }
}
