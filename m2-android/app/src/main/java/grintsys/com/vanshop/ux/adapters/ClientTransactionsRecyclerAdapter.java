package grintsys.com.vanshop.ux.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import grintsys.com.vanshop.R;
import grintsys.com.vanshop.entities.client.ClientTransactions;
import grintsys.com.vanshop.interfaces.ClientTransactionsRecyclerInterface;
import timber.log.Timber;

/**
 * Adapter handling list of orders from history.
 */
public class ClientTransactionsRecyclerAdapter extends RecyclerView.Adapter<ClientTransactionsRecyclerAdapter.ViewHolder> {

    private final ClientTransactionsRecyclerInterface transactionsRecyclerInterface;
    private LayoutInflater layoutInflater;
    private List<ClientTransactions> transactions = new ArrayList<>();

    /**
     * Creates an adapter that handles a list of payments from history
     *
     * @param transactionsRecyclerInterface listener indicating events that occurred.
     */
    public ClientTransactionsRecyclerAdapter(ClientTransactionsRecyclerInterface transactionsRecyclerInterface){
        this.transactionsRecyclerInterface = transactionsRecyclerInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.list_item_client_transactions, parent, false);
        return new ViewHolder(view, transactionsRecyclerInterface);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ClientTransactions transaction = getTransactionItem(position);
        holder.bindContent(transaction);

        holder.referenceNumberTv.setText(String.valueOf(transaction.getReference_number()));
        holder.descriptionTv.setText(transaction.getDescription());
        holder.amountTv.setText(String.valueOf(transaction.getAmount()));
        holder.dateTv.setText(String.valueOf(transaction.getDate()));
    }

    private ClientTransactions getTransactionItem(int position) {
        return transactions.get(position);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void addPayments(List<ClientTransactions> transactions) {
        if (transactions != null && !transactions.isEmpty()) {
            this.transactions.addAll(transactions);
            notifyDataSetChanged();
        } else {
            Timber.e("Adding empty transactions list.");
        }
    }

    /**
     * Clear all data.
     */
    public void clear() {
        transactions.clear();
    }

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView referenceNumberTv;
        private TextView descriptionTv;
        private TextView dateTv;
        private TextView amountTv;

        private ClientTransactions transaction;

        public ViewHolder(View itemView, final ClientTransactionsRecyclerInterface transactionsRecyclerInterface) {
            super(itemView);

            referenceNumberTv = (TextView) itemView.findViewById(R.id.client_transaction_reference_number);
            dateTv = (TextView) itemView.findViewById(R.id.client_transaction_date);
            descriptionTv = (TextView) itemView.findViewById(R.id.client_transaction_description);
            amountTv = (TextView) itemView.findViewById(R.id.client_transaction_amount);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //transactionsRecyclerInterface
                    transactionsRecyclerInterface.onClientTransactionsSelected(v, transaction);
                }
            });
        }

        public void bindContent(ClientTransactions transaction) {
            this.transaction = transaction;
        }
    }
}
