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
    private int tenantId;
    private String docEntry;
    private Double payedAmount;
    private String lastMessage;
    private int status;
    private String statusDesc;
    private String comment;
    private String referenceNumber;
    private String creationTime;
    private long userId;
    private int bankId;
    private int type;
    private String typeDesc;
    private String payedDate;
    private String cardCode;
    private String cardName;
    private String userName;
    private ArrayList<InvoiceItem> invoices;

    public Payment() {}

    public Payment(int id, String vendor, String docEntry, Double totalPaid, String lastError, Date date, Client client, Cash cash, Transfer transfer, ArrayList<CheckPayment> checks)
    {
        this.id = id;
        //this.vendor = vendor;
        this.docEntry = docEntry;
        this.payedAmount = totalPaid;
        /*this.lastError = lastError;
        this.date = date;
        this.client = client;
        this.cash = cash;
        this.transfer = transfer;
        this.checks = checks;*/
    }

    public int getId(){ return this.id; }
    public void setId(int val){this.id = val;}

    public int getTenantId(){return this.tenantId;}
    public void setTenantId(int val){this.tenantId = val;}

    public String getDocEntry(){return this.docEntry;}
    public void setDocEntry(String val){this.docEntry = val;}

    public Double getPayedAmount(){return this.payedAmount;}
    public void setPayedAmount(Double val){this.payedAmount = val;}

    public String getLastMessage(){return this.lastMessage;}
    public void setLastMessage(String val){this.lastMessage = val;}

    public int getStatus(){return this.status;}
    public void setStatus(int val){this.status = val;}

    public String getStatusDesc(){return this.statusDesc;}
    public void setStatusDesc(String val){this.statusDesc = val;}

    public String getComment(){return this.comment;}
    public void setComment(String val){this.comment = val;}

    public String getReferenceNumber(){return this.referenceNumber;}
    public void setReferenceNumber(String val){this.referenceNumber = val;}

    public String getCreationTime(){return this.creationTime;}
    public void setCreationTime(String val){this.creationTime = val;}

    public long getUserId(){return this.userId;}
    public void setUserId(long val){this.userId = val;}

    public int getBankId(){return this.bankId;}
    public void setBankId(int val){this.bankId = val;}

    public int getType(){return this.type;}
    public void setType(int val){this.type = val;}

    public String getTypeDesc(){return this.typeDesc;}
    public void setTypeDesc(String val){this.typeDesc = val;}

    public String getPayedDate(){return this.payedDate;}
    public void setPayedDate(String val){this.payedDate = val;}

    public String getCardCode(){return this.cardCode;}
    public void setCardCode(String val){this.cardCode = val;}

    public String getCardName(){return this.cardName;}
    public void setCardName(String val){this.cardName = val;}

    public String getUserName(){return this.userName;}
    public void setUserName(String val){this.userName = val;}
}