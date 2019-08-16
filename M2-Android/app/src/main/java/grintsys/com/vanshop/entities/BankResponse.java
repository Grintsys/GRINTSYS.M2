package grintsys.com.vanshop.entities;

import java.util.ArrayList;

/**
 * Created by turupawn on 3/29/17.
 */
public class BankResponse {
    private ArrayList<Bank> items;

    public ArrayList<Bank> getBanks()
    {
        return items;
    }

    public void setBanks(ArrayList<Bank> banks)
    {
        this.items = banks;
    }
}
