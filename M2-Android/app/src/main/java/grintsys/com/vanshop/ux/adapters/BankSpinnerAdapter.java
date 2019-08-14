package grintsys.com.vanshop.ux.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import grintsys.com.vanshop.R;
import grintsys.com.vanshop.entities.Bank;

/**
 * Created by alienware on 4/2/2017.
 */

public class BankSpinnerAdapter extends ArrayAdapter<Bank> {

    private static final int layoutID = R.layout.spinner_item_bank_dropdown;
    private final LayoutInflater layoutInflater;
    private List<Bank> bankList = new ArrayList<>();

    public BankSpinnerAdapter(Activity activity, ArrayList<Bank> banks) {
        super(activity, R.layout.spinner_item_sort);
        this.setDropDownViewResource(layoutID);
        this.bankList = banks;
        this.layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return bankList.size();
    }

    public Bank getItem(int position) {
        return bankList.get(position);
    }


    private View getCustomView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        BankSpinnerAdapter.ListItemHolder holder;

        if (v == null) {
            v = layoutInflater.inflate(layoutID, parent, false);
            holder = new BankSpinnerAdapter.ListItemHolder();
            holder.BankName = (TextView) v.findViewById(R.id.bank_text);
            v.setTag(holder);
        } else {
            holder = (BankSpinnerAdapter.ListItemHolder) v.getTag();
        }

        Bank bank = getItem(position);

        if (bank != null) {
            holder.BankName.setText(bank.getName());
        }

        return v;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public static class ListItemHolder {
        TextView BankName;
    }
}
