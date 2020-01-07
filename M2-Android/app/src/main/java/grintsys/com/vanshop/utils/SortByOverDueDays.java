package grintsys.com.vanshop.utils;

import java.util.Comparator;

import grintsys.com.vanshop.entities.payment.InvoiceItem;

public class SortByOverDueDays implements Comparator<InvoiceItem>
{
    @Override
    public int compare(InvoiceItem t1, InvoiceItem t2) {
        return  (int)(t2.overdueDays - t1.overdueDays);
    }
}