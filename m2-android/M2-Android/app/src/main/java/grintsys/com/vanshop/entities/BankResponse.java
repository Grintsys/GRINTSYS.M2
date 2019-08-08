package grintsys.com.vanshop.entities;

import java.util.ArrayList;

/**
 * Created by turupawn on 3/29/17.
 */
public class BankResponse {
    private ArrayList<Bank> banks;

    public ArrayList<Bank> getBanks()
    {
        return banks;
    }

    public void setBanks(ArrayList<Bank> banks)
    {
        this.banks = banks;
    }
}
