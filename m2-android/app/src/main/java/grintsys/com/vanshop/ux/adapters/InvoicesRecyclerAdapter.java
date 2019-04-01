package grintsys.com.vanshop.ux.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import grintsys.com.vanshop.R;
import grintsys.com.vanshop.entities.client.Client;
import grintsys.com.vanshop.entities.invoice.Invoice;
import grintsys.com.vanshop.interfaces.ClientRecyclerInterface;
import grintsys.com.vanshop.interfaces.InvoiceHistoryRecyclerInterface;
import grintsys.com.vanshop.interfaces.InvoiceRecyclerInterface;
import grintsys.com.vanshop.ux.MainActivity;

/**
 * Created by alienware on 2/1/2017.
 */

public class InvoicesRecyclerAdapter extends RecyclerView.Adapter<InvoicesRecyclerAdapter.ViewHolder> {

    private final Context context;
    private final InvoiceHistoryRecyclerInterface invoiceRecyclerInterface;
    private List<Invoice> invoices = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public InvoicesRecyclerAdapter(Context context, InvoiceHistoryRecyclerInterface invoiceRecyclerInterface) {
        this.context = context;
        this.invoiceRecyclerInterface = invoiceRecyclerInterface;
    }

    public Invoice getItem(int position) {
        return this.invoices.get(position);
    }

    @Override
    public int getItemCount() {
        return this.invoices.size();
    }

    public void addItem(List<Invoice> items){
        this.invoices = items;
        notifyDataSetChanged();
    }

    public void clear() {
        invoices.clear();
    }

    @Override
    public InvoicesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.list_item_invoices, parent, false);
        return new InvoicesRecyclerAdapter.ViewHolder(context, view, invoiceRecyclerInterface);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Invoice invoice = getItem(position);
        holder.bindContent(invoice);
        // - replace the contents of the view with that element
        holder.clientName.setText(holder.invoice.getCardName());
        holder.invoiceDocNum.setText(holder.invoice.getDocNum());
        holder.clientCardCode.setText(holder.invoice.getCardCode());
        //holder.clientPhone.setText(holder.client.getPhone());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView clientCardCode;
        public TextView clientName;
        public TextView invoiceDocNum;
        private Invoice invoice;

        public ViewHolder(final Context context, View v, final InvoiceHistoryRecyclerInterface invoiceRecyclerInterface) {
            super(v);
            clientCardCode = (TextView) v.findViewById(R.id.client_item_card_code);
            clientName = (TextView) v.findViewById(R.id.client_item_name);
            invoiceDocNum = (TextView) v.findViewById(R.id.invoice_doc_num);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    invoiceRecyclerInterface.onInvoiceHistorySelected(v, invoice);
                }
            });
        }

        public void bindContent(Invoice invoice) {
            this.invoice = invoice;
        }
    }
}
