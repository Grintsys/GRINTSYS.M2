package grintsys.com.vanshop.ux.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import grintsys.com.vanshop.R;
import grintsys.com.vanshop.entities.payment.Payment;
import grintsys.com.vanshop.interfaces.PaymentsRecyclerInterface;
import grintsys.com.vanshop.utils.Utils;
import timber.log.Timber;

/**
 * Adapter handling list of orders from history.
 */
public class PaymentsHistoryRecyclerAdapter extends RecyclerView.Adapter<PaymentsHistoryRecyclerAdapter.ViewHolder> {

    private final PaymentsRecyclerInterface paymentsRecyclerInterface;
    private LayoutInflater layoutInflater;
    private List<Payment> payments = new ArrayList<>();

    /**
     * Creates an adapter that handles a list of payments from history
     *
     * @param paymentsRecyclerInterface listener indicating events that occurred.
     */
    public PaymentsHistoryRecyclerAdapter(PaymentsRecyclerInterface paymentsRecyclerInterface) {
        this.paymentsRecyclerInterface = paymentsRecyclerInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.list_item_payments_history, parent, false);
        return new ViewHolder(view, paymentsRecyclerInterface);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Payment payment = getPaymentItem(position);
        holder.bindContent(payment);

        holder.paymentStatusTv.setText(String.valueOf(payment.getStatusText()));
        holder.paymentClientNameTv.setText(Utils.truncate(payment.getClient().getName(), 40));
        holder.paymentTotalPayedTv.setText(String.valueOf(payment.getTransfer().getAmount()));
        holder.paymentDateTv.setText(String.valueOf(payment.getCreatedDate()));
    }

    private Payment getPaymentItem(int position) {
        return payments.get(position);
    }

    @Override
    public int getItemCount() {
        return payments.size();
    }

    public void addPayments(List<Payment> paymentList) {
        if (paymentList != null && !paymentList.isEmpty()) {
            payments.addAll(paymentList);
            notifyDataSetChanged();
        } else {
            Timber.e("Adding empty payments list.");
        }
    }

    /**
     * Clear all data.
     */
    public void clear() {
        payments.clear();
    }

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {

        //private TextView paymentIdTv;
        private TextView paymentStatusTv;
        private TextView paymentClientNameTv;
        private TextView paymentTotalPayedTv;
        private TextView paymentDateTv;

        private Payment payment;

        public ViewHolder(View itemView, final PaymentsRecyclerInterface paymentsRecyclerInterface) {
            super(itemView);
            //paymentIdTv = (TextView) itemView.findViewById(R.id.payment_history_id);
            paymentStatusTv = (TextView) itemView.findViewById(R.id.payment_history_status);
            paymentClientNameTv = (TextView) itemView.findViewById(R.id.payment_history_client);
            paymentTotalPayedTv = (TextView) itemView.findViewById(R.id.payment_history_total_payed);
            paymentDateTv = (TextView) itemView.findViewById(R.id.payment_history_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    paymentsRecyclerInterface.onPaymentSelected(v, payment);
                }
            });
        }

        public void bindContent(Payment payment) {
            this.payment = payment;
        }
    }
}
