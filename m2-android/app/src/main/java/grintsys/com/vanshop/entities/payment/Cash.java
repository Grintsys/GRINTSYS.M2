package grintsys.com.vanshop.entities.payment;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by alienware on 3/29/2017.
 */

public class Cash  implements Serializable {

    private long id;
    private double amount;
    @SerializedName("general_account")
    private String generalAccount;

    public Cash(){}
    public Cash(long id, double amount, String generalAccount) {
        this.id = id;
        this.amount = amount;
        this.generalAccount = generalAccount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getGeneralAccount() {
        return generalAccount;
    }

    public void setGeneralAccount(String generalAccount) {
        this.generalAccount = generalAccount;
    }
}
