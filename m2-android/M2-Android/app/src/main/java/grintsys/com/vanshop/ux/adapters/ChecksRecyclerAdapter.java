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
import grintsys.com.vanshop.entities.payment.CheckPayment;
import grintsys.com.vanshop.interfaces.ChecksRecyclerInterface;

/**
 * Created by alienware on 2/1/2017.
 */

public class ChecksRecyclerAdapter extends RecyclerView.Adapter<ChecksRecyclerAdapter.ViewHolder> {

    private Context context;
    private ChecksRecyclerInterface checksRecyclerInterface;
    private List<CheckPayment> checks = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public ChecksRecyclerAdapter(Context context, ChecksRecyclerInterface checksRecyclerInterface) {
        this.context = context;
        this.checksRecyclerInterface = checksRecyclerInterface;
    }

    public CheckPayment getItem(int position) {
        return this.checks.get(position);
    }

    @Override
    public int getItemCount() {
        return this.checks.size();
    }

    public void addCheck(CheckPayment check){
        this.checks.add(check);
        notifyItemInserted(this.checks.size()-1);
    }

    public void removeCheck(int position){
        this.checks.remove(position);
        notifyItemRemoved(this.checks.size()-1);
    }

    public void updateView(){
        notifyDataSetChanged();
    }

    public void clear() {
        checks.clear();
    }

    @Override
    public ChecksRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.list_item_check, parent, false);

        return new ChecksRecyclerAdapter.ViewHolder(context, view, checksRecyclerInterface);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CheckPayment checkPayment = getItem(position);
        holder.bindContent(checkPayment);

        holder.checkNumberText.setText(holder.check.getCheckNumber());
        holder.createdDateText.setText(String.valueOf(holder.check.getDate().toString()));

        if(checkPayment.getBank() != null)
            holder.bankText.setText(holder.check.getBank().getName());

        holder.amountText.setText(NumberFormat.getNumberInstance(Locale.US).format(holder.check.getAmount()));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView checkNumberText;
        public TextView createdDateText;
        public TextView bankText;
        public TextView amountText;
        private CheckPayment check;

        public ViewHolder(final Context context, View v, final ChecksRecyclerInterface checksRecyclerInterface) {
            super(v);
            checkNumberText = (TextView) v.findViewById(R.id.check_number);
            createdDateText = (TextView) v.findViewById(R.id.check_date);
            bankText = (TextView) v.findViewById(R.id.check_bank);
            amountText = (TextView) v.findViewById(R.id.check_amount);

          /*  v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int postition = getAdapterPosition();

                    return false;
                }
            });*/
        }

        public void bindContent(CheckPayment check) {
            this.check = check;
        }
    }
}
