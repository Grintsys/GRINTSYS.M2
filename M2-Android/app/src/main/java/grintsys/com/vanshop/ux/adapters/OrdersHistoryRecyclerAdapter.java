package grintsys.com.vanshop.ux.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import grintsys.com.vanshop.R;
import grintsys.com.vanshop.entities.order.Order;
import grintsys.com.vanshop.interfaces.OrdersRecyclerInterface;
import grintsys.com.vanshop.utils.Utils;
import timber.log.Timber;

/**
 * Adapter handling list of orders from history.
 */
public class OrdersHistoryRecyclerAdapter extends RecyclerView.Adapter<OrdersHistoryRecyclerAdapter.ViewHolder> {

    private final OrdersRecyclerInterface ordersRecyclerInterface;
    private LayoutInflater layoutInflater;
    private List<Order> orders = new ArrayList<>();

    /**
     * Creates an adapter that handles a list of orders from history
     *
     * @param ordersRecyclerInterface listener indicating events that occurred.
     */
    public OrdersHistoryRecyclerAdapter(OrdersRecyclerInterface ordersRecyclerInterface) {
        this.ordersRecyclerInterface = ordersRecyclerInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.list_item_orders_history, parent, false);
        return new ViewHolder(view, ordersRecyclerInterface);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Order order = getOrderItem(position);
        holder.bindContent(order);

        holder.orderClientNameTv.setText(Utils.truncate(order.getCardCode(), 50));
        holder.orderSAPId.setText(order.getRemoteId());
        holder.orderDateCreatedTv.setText(order.getDateCreated());
        holder.orderTotalPriceTv.setText(order.getTotalFormatted());
    }

    private Order getOrderItem(int position) {
        return orders.get(position);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void addOrders(List<Order> orderList) {
        if (orderList != null && !orderList.isEmpty()) {
            orders.addAll(orderList);
            notifyDataSetChanged();
        } else {
            Timber.e("Adding empty orders list.");
        }
    }

    /**
     * Clear all data.
     */
    public void clear() {
        orders.clear();
    }

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView orderClientNameTv;
        private TextView orderSAPId;
        private TextView orderDateCreatedTv;
        private TextView orderTotalPriceTv;
        //private TextView orderCountTv;
        private Order order;

        public ViewHolder(View itemView, final OrdersRecyclerInterface ordersRecyclerInterface) {
            super(itemView);
            orderClientNameTv = (TextView) itemView.findViewById(R.id.order_history_item_client_name);
            orderSAPId = (TextView) itemView.findViewById(R.id.order_history_item_sap_id);
            orderDateCreatedTv = (TextView) itemView.findViewById(R.id.order_history_item_dateCreated);
            orderTotalPriceTv = (TextView) itemView.findViewById(R.id.order_history_item_totalPrice);
            //orderCountTv = (TextView) itemView.findViewById(R.id.order_history_item_count);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ordersRecyclerInterface.onOrderSelected(v, order);
                }
            });
        }

        public void bindContent(Order order) {
            this.order = order;
        }
    }
}
