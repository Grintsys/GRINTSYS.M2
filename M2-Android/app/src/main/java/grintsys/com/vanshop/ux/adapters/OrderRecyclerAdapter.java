package grintsys.com.vanshop.ux.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import grintsys.com.vanshop.R;
import grintsys.com.vanshop.entities.User.User;
import grintsys.com.vanshop.entities.client.Client;
import grintsys.com.vanshop.entities.order.Order;
import grintsys.com.vanshop.entities.order.OrderItem;
import grintsys.com.vanshop.utils.BluetoothPrinter;
import grintsys.com.vanshop.utils.MsgUtils;
import grintsys.com.vanshop.ux.MainActivity;
import grintsys.com.vanshop.views.ResizableImageView;
import timber.log.Timber;

import static grintsys.com.vanshop.SettingsMy.getActiveUser;

/**
 * Adapter handling list of order items.
 */
public class OrderRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM_ORDER = 1;

    private LayoutInflater layoutInflater;
    private Context context;
    private Order order;
    private static long orderId;

    /**
     * Creates an adapter that handles a list of order items.
     *
     * @param context activity context.
     */
    public OrderRecyclerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_ITEM_ORDER) {
            View view = layoutInflater.inflate(R.layout.list_item_order_product_image, parent, false);
            return new ViewHolderOrderProduct(view);
        } else {
            View view = layoutInflater.inflate(R.layout.list_item_order_header, parent, false);
            return new ViewHolderHeader(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderOrderProduct) {
            ViewHolderOrderProduct viewHolderOrderProduct = (ViewHolderOrderProduct) holder;
            /*
            Picasso.with(context).load(order.getProducts().get(position - 1).getVariant().getMainImage())
                    .fit().centerInside()
                    .placeholder(R.drawable.placeholder_loading)
                    .error(R.drawable.placeholder_error)
                    .into(viewHolderOrderProduct.productImage);
             */

        } else if (holder instanceof ViewHolderHeader) {
            ViewHolderHeader viewHolderHeader = (ViewHolderHeader) holder;

            viewHolderHeader.order=order;
            /*
            viewHolderHeader.orderId.setText(String.valueOf(order.getId()));
            viewHolderHeader.orderDateCreated.setText(order.getDateCreated());
            viewHolderHeader.orderTotal.setText(order.getTotalFormatted());
            viewHolderHeader.orderName.setText(order.getComment());
            viewHolderHeader.orderStatus.setText(order.getStatus());
            */

            viewHolderHeader.context = context;

            if(order.getClient()!=null)
                viewHolderHeader.order_create_summary_text.setText(context.getString(R.string.Summary) +
                        " - " + context.getString(R.string.Customer) + ": "
                        + order.getClient().getCardCode());
            DecimalFormat df = new DecimalFormat("0.00");
            viewHolderHeader.order_create_subtotal.setText("Subtotal: " + df.format(order.getSubtotal()));
            viewHolderHeader.order_create_discount.setText("Discount: " + df.format(order.getDiscount()));
            viewHolderHeader.order_create_isv.setText("ISV: " + df.format(order.getIVA()));
            viewHolderHeader.order_create_total_price.setText("Total: " + df.format(order.getTotal()));
            viewHolderHeader.order_create_seller.setText(order.getSeller());
            viewHolderHeader.order_comment.setText(order.getComment());
            viewHolderHeader.order_status.setText(order.getStatus());

            if(order.getProducts()!=null) {
                for (int i = 0; i < order.getProducts().size(); i++) {
                    LinearLayout llRow = (LinearLayout) layoutInflater.inflate(R.layout.order_create_cart_item, viewHolderHeader.order_create_cart_items_layout, false);
                    TextView tvItemName = (TextView) llRow.findViewById(R.id.order_create_cart_item_name);
                    tvItemName.setText(order.getProducts().get(i).getCode());
                    TextView tvItemPrice = (TextView) llRow.findViewById(R.id.order_create_cart_item_price);
                    tvItemPrice.setText(df.format(order.getProducts().get(i).getPrice()));
                    TextView tvItemQuantity = (TextView) llRow.findViewById(R.id.order_create_cart_item_quantity);
                    tvItemQuantity.setText("" + order.getProducts().get(i).getQuantity());
                    viewHolderHeader.order_create_cart_items_layout.addView(llRow);
                }
            }

            orderId = order.getId();

            viewHolderHeader.deliveryProgressBar.setVisibility(View.GONE);

            if(order.getRemoteId() != null){
                viewHolderHeader.orderResend.setVisibility(View.GONE);
            }else{
                viewHolderHeader.orderResend.setVisibility(View.VISIBLE);
            }

        } else {
            Timber.e(new RuntimeException(), "Unknown holder type.");
        }
    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        if (order != null) {
            if (order.getProducts() != null && order.getProducts().size() > 0) {
                return order.getProducts().size() + 1; // the number of items in the list, +1 for header view.
            } else {
                return 1;
            }
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        else
            return TYPE_ITEM_ORDER;
    }


    /**
     * Add item to list, and notify dataSet changed.
     *
     * @param order item to add.
     */
    public void addOrder(Order order) {
        if (order != null) {
            this.order = order;
            notifyDataSetChanged();
        } else {
            Timber.e("Setting null order object.");
        }
    }

    // Provide a reference to the views for each data item
    public static class ViewHolderOrderProduct extends RecyclerView.ViewHolder {
        ResizableImageView productImage;

        public ViewHolderOrderProduct(View itemView) {
            super(itemView);
            productImage = (ResizableImageView) itemView.findViewById(R.id.list_item_product_images_view);
        }
    }

    public static class ViewHolderHeader extends RecyclerView.ViewHolder {

        public Context context;

        public TextView order_create_summary_text;
        public TextView order_create_subtotal;
        public TextView order_create_discount;
        public TextView order_create_isv;
        public TextView order_create_total_price;
        public TextView order_create_seller;
        public TextView order_comment;
        public TextView order_status;

        LinearLayout order_create_cart_items_layout;

        public Button orderResend;
        public Button orderPrint;

        public Order order;

        ProgressBar deliveryProgressBar;

        public ViewHolderHeader(View headerView) {
            super(headerView);

            order_create_subtotal = (TextView) headerView.findViewById(R.id.order_history_subtotal);
            order_create_discount = (TextView) headerView.findViewById(R.id.order_history_discount);
            order_create_isv = (TextView) headerView.findViewById(R.id.order_history_isv);
            order_create_total_price = (TextView) headerView.findViewById(R.id.order_history_total_price);
            order_create_seller = (TextView) headerView.findViewById(R.id.order_create_seller);
            order_comment = (TextView) headerView.findViewById(R.id.order_comment);
            order_status = (TextView) headerView.findViewById(R.id.order_history_status);
            order_create_summary_text = (TextView) headerView.findViewById(R.id.order_create_summary_text);
            order_create_cart_items_layout = (LinearLayout) headerView.findViewById(R.id.order_create_cart_items_layout);
            deliveryProgressBar = (ProgressBar) headerView.findViewById(R.id.delivery_progress);
            orderResend = (Button) headerView.findViewById(R.id.order_resend);
            orderPrint = (Button) headerView.findViewById(R.id.order_print);

            orderResend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MsgUtils.showToast((MainActivity)context, MsgUtils.TOAST_TYPE_MESSAGE, "Se ha reenviado pedido", MsgUtils.ToastLength.LONG);
                }
            });

            orderPrint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User user = getActiveUser();

                    Client client = order.getClient();
                    List<OrderItem> products = order.getProducts();

                    try
                    {

                        if(user != null && client != null && products != null) {
                            BluetoothPrinter.print(context,
                                    user.getPrintBluetoothAddress(),
                                    order.getDateCreated(),
                                    client,
                                    order.getSeller(),
                                    products,
                                    order.getSubtotal(),
                                    order.getDiscount(),
                                    order.getSubtotal() - order.getDiscount(),
                                    order.getIVA(),
                                    order.getTotal());
                        }else
                        {
                            MsgUtils.showToast((Activity)context, MsgUtils.TOAST_TYPE_MESSAGE, context.getString(R.string.Internal_error), MsgUtils.ToastLength.LONG);
                           // MsgUtils.showToast((Activity)context, MsgUtils.TOAST_TYPE_MESSAGE, e.Mes, MsgUtils.ToastLength.LONG);
                        }

                    }catch (Exception e){
                        MsgUtils.showToast((Activity)context, MsgUtils.TOAST_TYPE_MESSAGE, e.getMessage().toString(), MsgUtils.ToastLength.LONG);

                    };


                }
            });
        }
    }
}