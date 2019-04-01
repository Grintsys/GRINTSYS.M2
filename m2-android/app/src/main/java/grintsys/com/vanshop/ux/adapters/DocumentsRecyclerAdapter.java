package grintsys.com.vanshop.ux.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import grintsys.com.vanshop.R;
import grintsys.com.vanshop.entities.client.Document;
import grintsys.com.vanshop.interfaces.DocumentRecyclerInterface;
import grintsys.com.vanshop.ux.MainActivity;

/**
 * Created by alienware on 2/1/2017.
 */

public class DocumentsRecyclerAdapter extends RecyclerView.Adapter<DocumentsRecyclerAdapter.ViewHolder> {

    private final Context context;
    private final DocumentRecyclerInterface documentRecyclerInterface;
    private List<Document> documents = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private boolean showSelected = false;

    public DocumentsRecyclerAdapter(Context context, DocumentRecyclerInterface documentRecyclerInterface, boolean showSelected) {
        this.context = context;
        this.documentRecyclerInterface = documentRecyclerInterface;
        this.showSelected = showSelected;
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
    public DocumentsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.list_item_documents, parent, false);
        return new DocumentsRecyclerAdapter.ViewHolder(view, documentRecyclerInterface, showSelected, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Document document = getItem(position);
        holder.bindContent(document);
        // - replace the contents of the view with that element
        holder.documentCode.setText(holder.document.getDocumentCode());
        holder.createdDate.setText(holder.document.getCreatedDate());
        holder.dueDate.setText(holder.document.getDueDate());
        holder.pastDueAmount.setText(NumberFormat.getNumberInstance(Locale.US).format(0));
        holder.totalAmount.setText(NumberFormat.getNumberInstance(Locale.US).format(holder.document.getTotalAmount()));
        holder.balanceDue.setText(NumberFormat.getNumberInstance(Locale.US).format(holder.document.getBalanceDue()));
        holder.overdueDays.setText(NumberFormat.getNumberInstance(Locale.US).format(holder.document.getOverdueDays()));
        holder.selectedCheck.setChecked(holder.document.getPaymentSelected());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView documentCode;
        public TextView createdDate;
        public TextView dueDate;
        public TextView totalAmount;
        public TextView balanceDue, overdueDays, pastDueAmount;
        public TextView selectedText;
        public CheckBox selectedCheck;
        //public TextView clientCode;
        private Document document;

        public ViewHolder(View v, final DocumentRecyclerInterface documentRecyclerInterface, boolean showSelected, final Context context) {
            super(v);
            //clientCode = (TextView) v.findViewById(R.id.document_client_code);
            documentCode = (TextView) v.findViewById(R.id.document_code);
            createdDate = (TextView) v.findViewById(R.id.document_created_date);
            dueDate = (TextView) v.findViewById(R.id.document_due_date);
            totalAmount = (TextView) v.findViewById(R.id.document_total_amount);
            pastDueAmount = (TextView) v.findViewById(R.id.document_past_due_amount);
            balanceDue = (TextView) v.findViewById(R.id.document_balance_due);
            overdueDays = (TextView) v.findViewById(R.id.document_overdue_days);

            selectedCheck = (CheckBox) v.findViewById(R.id.document_selected_checkbox);
            selectedText = (TextView) v.findViewById(R.id.document_selected_checkbox_text);

            //Selected is used on payments
            if(showSelected){
                selectedCheck.setVisibility(View.VISIBLE);
                selectedText.setVisibility(View.VISIBLE);

                selectedCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        document.setPaymentSelected(b);
                        if(b){
                            ((MainActivity) context).AddInvoice(document);
                        }else{
                            ((MainActivity) context).RemoveInvoice(document);
                        }
                    }
                });
            }else{
                selectedCheck.setVisibility(View.GONE);
                selectedText.setVisibility(View.GONE);
            }

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
