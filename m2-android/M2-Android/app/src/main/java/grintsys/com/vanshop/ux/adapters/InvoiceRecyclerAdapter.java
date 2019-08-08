package grintsys.com.vanshop.ux.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import grintsys.com.vanshop.R;
import grintsys.com.vanshop.entities.client.Document;
import grintsys.com.vanshop.interfaces.InvoiceRecyclerInterface;

/**
 * Created by alienware on 3/28/2017.
 */

public class InvoiceRecyclerAdapter extends RecyclerView.Adapter<InvoiceRecyclerAdapter.ViewHolder>{

    private final Context context;
    private final InvoiceRecyclerInterface documentRecyclerInterface;
    private List<Document> documents = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public InvoiceRecyclerAdapter(Context context, InvoiceRecyclerInterface documentRecyclerInterface) {
        this.context = context;
        this.documentRecyclerInterface = documentRecyclerInterface;
    }

    public Document getItem(int position) {
        return this.documents.get(position);
    }

    @Override
    public int getItemCount() {
        return this.documents.size();
    }

    public void addDocuments(List<Document> documents){
        this.documents = documents;
        notifyDataSetChanged();
    }

    public void clear() {
        documents.clear();
    }

    @Override
    public InvoiceRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.list_item_documents, parent, false);
        return new InvoiceRecyclerAdapter.ViewHolder(view, documentRecyclerInterface);
    }

    @Override
    public void onBindViewHolder(InvoiceRecyclerAdapter.ViewHolder holder, int position) {
        Document document = getItem(position);
        holder.bindContent(document);
        // - replace the contents of the view with that element
        holder.documentCode.setText(holder.document.getDocumentCode());
        holder.createdDate.setText(holder.document.getCreatedDate());
        holder.dueDate.setText(holder.document.getDueDate());
        holder.totalAmount.setText(NumberFormat.getNumberInstance(Locale.US).format(holder.document.getTotalAmount()));
        holder.payedAmount.setText(NumberFormat.getNumberInstance(Locale.US).format(holder.document.getPayedAmount()));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView documentCode;
        public TextView createdDate;
        public TextView dueDate;
        public TextView totalAmount;
        public TextView payedAmount;
        public TextView createdDateText, dueDateText, totalText, payedText;
        //public TextView clientCode;
        private Document document;

        public ViewHolder(View v, final InvoiceRecyclerInterface documentRecyclerInterface) {
            super(v);
            //clientCode = (TextView) v.findViewById(R.id.document_client_code);
            documentCode = (TextView) v.findViewById(R.id.document_code);
            createdDate = (TextView) v.findViewById(R.id.document_created_date);
            dueDate = (TextView) v.findViewById(R.id.document_due_date);
            totalAmount = (TextView) v.findViewById(R.id.document_total_amount);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    documentRecyclerInterface.onDocumentRecyclerInterface(v, document);
                }
            });
        }

        public void bindContent(Document document) {
            this.document = document;
        }
    }
}
