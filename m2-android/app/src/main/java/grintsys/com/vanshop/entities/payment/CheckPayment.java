package grintsys.com.vanshop.entities.payment;

import java.io.Serializable;

import grintsys.com.vanshop.entities.Bank;

/**
 * Created by turupawn on 3/28/17.
 */
public class CheckPayment implements Serializable {
    private String checkNumber;

    private Bank bank;

    private double amount;

    private String date;

    public CheckPayment() {}

    public CheckPayment(String checkNumber, Bank bank, double amount, String date)
    {
        this.checkNumber = checkNumber;
        this.bank = bank;
        this.amount = amount;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCheckNumber()
    {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber)
    {
        this.checkNumber = checkNumber;
    }

    public Bank getBank()
    {
        return bank;
    }

    public void setBank(Bank bank)
    {
        this.bank = bank;
    }

    public double getAmount()
    {
        return amount;
    }

    public void setAmount(double amount)
    {
        this.amount = amount;
    }
}
