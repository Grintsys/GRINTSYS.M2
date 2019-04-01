package grintsys.com.vanshop.interfaces;

import android.view.View;

import grintsys.com.vanshop.entities.order.Order;

public interface OrdersRecyclerInterface {

    void onOrderSelected(View v, Order order);

}
