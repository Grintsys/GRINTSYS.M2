package grintsys.com.vanshop.entities;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

public class Bank implements Serializable {
    private int id;
    private String name;

    @SerializedName("general_account")
    private String GeneralAccount;

    public Bank(int id, String name, String GeneralAccount)
    {
        this.id = id;
        this.name = name;
        this.GeneralAccount = GeneralAccount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGeneralAccount() {
        return GeneralAccount;
    }

    public void setGeneralAccount(String generalAccount) {
        GeneralAccount = generalAccount;
    }
}