package grintsys.com.vanshop.entities.client;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by alienware on 2/1/2017.
 */

public class Client implements Serializable {

    private long id;
    private String name;
    @SerializedName("card_code")
    private String cardCode;
    private String contact;
    private String phone;
    private String email;
    private String address;
    private String RTN;
    @SerializedName("invoices")
    private ArrayList<Document> invoiceList;

    public Client(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        if(RTN!=null)
            return RTN;
        return "";
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setRTN(String RTN) {
        this.RTN = RTN;
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
        if(phone!=null)
            return phone;
        return "";
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
