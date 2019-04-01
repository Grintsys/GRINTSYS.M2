package grintsys.com.vanshop.ux.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import grintsys.com.vanshop.CONST;
import grintsys.com.vanshop.MyApplication;
import grintsys.com.vanshop.R;
import grintsys.com.vanshop.SettingsMy;
import grintsys.com.vanshop.api.EndPoints;
import grintsys.com.vanshop.api.GsonRequest;
import grintsys.com.vanshop.entities.User.User;
import grintsys.com.vanshop.entities.User.UsersResponse;
import grintsys.com.vanshop.entities.cart.Cart;
import grintsys.com.vanshop.entities.cart.CartProductItem;
import grintsys.com.vanshop.entities.order.Order;
import grintsys.com.vanshop.listeners.OnSingleClickListener;
import grintsys.com.vanshop.utils.JsonUtils;
import grintsys.com.vanshop.utils.MsgUtils;
import grintsys.com.vanshop.utils.Utils;
import grintsys.com.vanshop.ux.MainActivity;
import grintsys.com.vanshop.ux.adapters.UserSpinnerAdapter;
import grintsys.com.vanshop.ux.dialogs.LoginExpiredDialogFragment;
import grintsys.com.vanshop.ux.dialogs.OrderCreateSuccessDialogFragment;
import timber.log.Timber;

import static grintsys.com.vanshop.SettingsMy.PREF_CLIENT_CARD_CODE_SELECTED;
import static grintsys.com.vanshop.SettingsMy.PREF_CLIENT_NAME_SELECTED;
import static grintsys.com.vanshop.SettingsMy.getSettings;

/**
 * Fragment allowing the user to create order.
 */
public class OrderCreateFragment extends Fragment {

    public static final String MSG_LOGIN_EXPIRED_DIALOG_FRAGMENT = "loginExpiredDialogFragment";
    private ProgressDialog progressDialog;

    private ScrollView scrollLayout;
    private LinearLayout cartItemsLayout;
    private TextView orderTotalPriceTv;

    private Cart cart;
    private EditText commentEditText, deliveryDateEdit;

    private TextView summaryText;
    private GsonRequest<Order> postOrderRequest;

    private TextView subtotalTextView, totalTextView, discountTextView, isvTexView;

    private UserSpinnerAdapter userSpinnerAdapter;
    private User selectedSeller = null;
    private Calendar myCalendar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("%s - onCreateView", this.getClass().getSimpleName());
        MainActivity.setActionBarTitle(getString(R.string.Order_summary));

        View view = inflater.inflate(R.layout.fragment_order_create, container, false);

        progressDialog = Utils.generateProgressDialog(getActivity(), false);

        scrollLayout = (ScrollView) view.findViewById(R.id.order_create_scroll_layout);
        cartItemsLayout = (LinearLayout) view.findViewById(R.id.order_create_cart_items_layout);
        commentEditText = (EditText) view.findViewById(R.id.order_comment);
        subtotalTextView = (TextView) view.findViewById(R.id.order_create_subtotal);
        totalTextView = (TextView) view.findViewById(R.id.order_create_total_price);
        discountTextView = (TextView) view.findViewById(R.id.order_create_discount);
        isvTexView = (TextView) view.findViewById(R.id.order_create_isv);
        summaryText = (TextView) view.findViewById(R.id.order_create_summary_text);
        deliveryDateEdit = (EditText) view.findViewById(R.id.order_create_delivery_date);

        myCalendar = Calendar.getInstance();
        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        deliveryDateEdit.setText(sdf.format(myCalendar.getTime()));

        deliveryDateEdit.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                new DatePickerDialog(getContext(),
                        R.style.MyDatePicker,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, monthOfYear);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                String myFormat = "yyyy/MM/dd";
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                                deliveryDateEdit.setText(sdf.format(myCalendar.getTime()));
                            }
                        },
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        SharedPreferences prefs = getSettings();
        String card_code = prefs.getString(PREF_CLIENT_CARD_CODE_SELECTED, "" );
        String name = prefs.getString(PREF_CLIENT_NAME_SELECTED, "");
        summaryText.setText(getString(R.string.Summary) + " - " + getString(R.string.Customer) + ": " + card_code + " - " + name);

        orderTotalPriceTv = (TextView) view.findViewById(R.id.order_create_summary_total_price);
        TextView termsAndConditionsTv = (TextView) view.findViewById(R.id.order_create_summary_terms_and_condition);
        termsAndConditionsTv.setText(this.fromHtml(getString(R.string.Click_on_Order_to_allow_our_Terms_and_Conditions)));
        termsAndConditionsTv.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (getActivity() instanceof MainActivity)
                    ((MainActivity) getActivity()).onTermsAndConditionsSelected();
            }
        });

        prepareSellerSpinner(view);

        Button finishOrder = (Button) view.findViewById(R.id.order_create_finish);
        finishOrder.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {

                //TODO: Fix Seller Selection
                Order order = new Order();
                order.setDeliveryDate(deliveryDateEdit.getText().toString());
                order.setComment(commentEditText.getText().toString());
                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                postOrder(order);
            }
        });

        getSellers();
        getUserCart();
        return view;
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    //TODO: ARREGLAR CHANCHADA
    private void prepareSellerSpinner(View view){

        Spinner userSpinner = (Spinner) view.findViewById(R.id.order_seller_spinner);
        userSpinnerAdapter = new UserSpinnerAdapter(getActivity());
        userSpinner.setAdapter(userSpinnerAdapter);

        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                User user = userSpinnerAdapter.getItem(i);

                Timber.d("selectedSalesPersonId: %d,",user.getSalesPersonId());

                if(user.getName() != null && user.getSalesPersonId() > 0){
                    selectedSeller = user;
                }else{
                    selectedSeller = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedSeller = null;
            }
        });

    }


    private void getSellers(){
        GsonRequest<UsersResponse> usersRequest = new GsonRequest<>(Request.Method.GET, EndPoints.USERS, null, UsersResponse.class, new Response.Listener<UsersResponse>() {
            @Override
            public void onResponse(@NonNull UsersResponse usersResponse) {
                //deliveryProgressBar.setVisibility(View.GONE);
                userSpinnerAdapter.setUserList(usersResponse.getUserList());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //deliveryProgressBar.setVisibility(View.GONE);
                MsgUtils.logAndShowErrorMessage(getActivity(), error);
            }
        });
        usersRequest.setRetryPolicy(MyApplication.getSimpleRetryPolice());
        usersRequest.setShouldCache(true);
        MyApplication.getInstance().addToRequestQueue(usersRequest, CONST.USERS_TAG);
    }

    private void getUserCart() {
        final User user = SettingsMy.getActiveUser();
        if (user != null) {
            //String url = String.format(EndPoints.CART, SettingsMy.getActualNonNullShop(getActivity()).getId());
            String url = String.format(EndPoints.CART, user.getId(), 0);

            progressDialog.show();
            GsonRequest<Cart> getCart = new GsonRequest<>(Request.Method.GET, url, null, Cart.class,
                    new Response.Listener<Cart>() {
                        @Override
                        public void onResponse(@NonNull Cart cart) {
                            if (progressDialog != null) progressDialog.cancel();
                            refreshScreenContent(cart, user);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (progressDialog != null) progressDialog.cancel();
                    Timber.e("Get request cart error: %s", error.getMessage());
                    MsgUtils.logAndShowErrorMessage(getActivity(), error);
                    if (getActivity() instanceof MainActivity) ((MainActivity) getActivity()).onDrawerBannersSelected();
                }
            }, getFragmentManager(), user.getAccessToken());
            getCart.setRetryPolicy(MyApplication.getSimpleRetryPolice());
            getCart.setShouldCache(false);
            MyApplication.getInstance().addToRequestQueue(getCart, CONST.ORDER_CREATE_REQUESTS_TAG);
        } else {
            LoginExpiredDialogFragment loginExpiredDialogFragment = new LoginExpiredDialogFragment();
            loginExpiredDialogFragment.show(getFragmentManager(), MSG_LOGIN_EXPIRED_DIALOG_FRAGMENT);
        }
    }

    private void refreshScreenContent(@NonNull Cart cart, User user) {
        this.cart = cart;
        List<CartProductItem> cartProductItems = cart.getItems();
        if (cartProductItems == null || cartProductItems.isEmpty()) {
            Timber.e(new RuntimeException(), "Received null cart during order creation.");
            if (getActivity() instanceof MainActivity) ((MainActivity) getActivity()).onDrawerBannersSelected();
        } else {

            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int i = 0; i < cartProductItems.size(); i++) {
                LinearLayout llRow = (LinearLayout) inflater.inflate(R.layout.order_create_cart_item, cartItemsLayout, false);
                TextView tvItemName = (TextView) llRow.findViewById(R.id.order_create_cart_item_name);
                tvItemName.setText(cartProductItems.get(i).getVariant().getName());
                TextView tvItemPrice = (TextView) llRow.findViewById(R.id.order_create_cart_item_price);
                tvItemPrice.setText(cartProductItems.get(i).getTotalItemPriceFormatted());
                TextView tvItemQuantity = (TextView) llRow.findViewById(R.id.order_create_cart_item_quantity);
                tvItemQuantity.setText(getString(R.string.format_quantity, cartProductItems.get(i).getQuantity()));
                cartItemsLayout.addView(llRow);
            }
            if (cart.getDiscounts() != null) {
                for (int i = 0; i < cart.getDiscounts().size(); i++) {
                    LinearLayout llRow = (LinearLayout) inflater.inflate(R.layout.order_create_cart_item, cartItemsLayout, false);
                    TextView tvItemName = (TextView) llRow.findViewById(R.id.order_create_cart_item_name);
                    TextView tvItemPrice = (TextView) llRow.findViewById(R.id.order_create_cart_item_price);
                    tvItemName.setText(cart.getDiscounts().get(i).getDiscount().getName());
                    tvItemPrice.setText(cart.getDiscounts().get(i).getDiscount().getValueFormatted());
                    tvItemPrice.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    cartItemsLayout.addView(llRow);
                }
            }

            subtotalTextView.setText(String.format("%s %s", getString(R.string.SubTotal), cart.getSubtotalPriceFormatted()));
            discountTextView.setText(String.format("%s %s", getString(R.string.Discount), String.valueOf(cart.getDiscountPriceFormatted())));
            isvTexView.setText(String.format("%s %s", getString(R.string.ISV), String.valueOf(cart.getIsvPriceFormatted())));
            totalTextView.setText(String.format("%s %s", getString(R.string.Total_colon), String.valueOf(cart.getTotalPriceFormatted())));
        }
    }

    private void postOrder(final Order order) {
        final User user = SettingsMy.getActiveUser();

        SharedPreferences prefs = getSettings();
        String card_code = prefs.getString(PREF_CLIENT_CARD_CODE_SELECTED, null);

        if(card_code == null)
        {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
            builder1.setMessage(this.getContext().getString(R.string.You_must_select_a_client_before_creating_and_order));
                    builder1.setCancelable(true);

            builder1.setPositiveButton(
                    getString(R.string.Ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
            return;
        }

        if (user != null)
        {
            if(selectedSeller != null){
                order.setSalesPersonCode(selectedSeller.getSalesPersonId());
            }else{
                order.setSalesPersonCode(1);
            }
            order.setSeries(71);
            order.setCardCode(card_code);

            JSONObject jo;
            try {
                jo = JsonUtils.createOrderJson(order);
            } catch (JSONException e) {
                Timber.e(e, "Post order Json exception.");
                MsgUtils.showToast(getActivity(), MsgUtils.TOAST_TYPE_INTERNAL_ERROR, null, MsgUtils.ToastLength.SHORT);
                return;
            }

            Timber.d("Post order jo: %s", jo.toString());
            //String url = String.format(EndPoints.ORDERS, SettingsMy.getActualNonNullShop(getActivity()).getId());
            String url = String.format(EndPoints.ORDERS_CREATE, user.getId(), cart.getId(), jo.toString());

            progressDialog.show();
            postOrderRequest = new GsonRequest<>(Request.Method.GET, url, null, Order.class, new Response.Listener<Order>() {
                @Override
                public void onResponse(Order order) {
                    Timber.d("response: %s", order.toString());
                    progressDialog.cancel();

                    //Analytics.logOrderCreatedEvent(cart, order.getRemoteId(), orderTotalPrice, selectedShipping);

                    //updateUserData(user, order);
                    MainActivity.updateCartCountNotification();

                    DialogFragment thankYouDF = OrderCreateSuccessDialogFragment.newInstance(false);
                    thankYouDF.show(getFragmentManager(), OrderCreateSuccessDialogFragment.class.getSimpleName());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.cancel();
                    // Return 501 for sample application.
                    MsgUtils.logAndShowErrorMessage(getActivity(), error);
                }
            }, getFragmentManager(), user.getAccessToken());
            postOrderRequest.setRetryPolicy(MyApplication.getSimpleRetryPolice());
            postOrderRequest.setShouldCache(false);
            MyApplication.getInstance().addToRequestQueue(postOrderRequest, CONST.ORDER_CREATE_REQUESTS_TAG);
        } else {
            LoginExpiredDialogFragment loginExpiredDialogFragment = new LoginExpiredDialogFragment();
            loginExpiredDialogFragment.show(getFragmentManager(), MSG_LOGIN_EXPIRED_DIALOG_FRAGMENT);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        MyApplication.getInstance().cancelPendingRequests(CONST.USERS_TAG);
        MyApplication.getInstance().cancelPendingRequests(CONST.ORDER_CREATE_REQUESTS_TAG);
        if (progressDialog != null) progressDialog.cancel();
    }
}
