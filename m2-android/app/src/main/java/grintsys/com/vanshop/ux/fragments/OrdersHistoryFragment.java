package grintsys.com.vanshop.ux.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import grintsys.com.vanshop.CONST;
import grintsys.com.vanshop.MyApplication;
import grintsys.com.vanshop.R;
import grintsys.com.vanshop.SettingsMy;
import grintsys.com.vanshop.api.EndPoints;
import grintsys.com.vanshop.api.GsonRequest;
import grintsys.com.vanshop.entities.Metadata;
import grintsys.com.vanshop.entities.User.User;
import grintsys.com.vanshop.entities.order.Order;
import grintsys.com.vanshop.entities.order.OrderResponse;
import grintsys.com.vanshop.interfaces.OrdersRecyclerInterface;
import grintsys.com.vanshop.listeners.OnSingleClickListener;
import grintsys.com.vanshop.utils.EndlessRecyclerScrollListener;
import grintsys.com.vanshop.utils.MsgUtils;
import grintsys.com.vanshop.utils.RecyclerMarginDecorator;
import grintsys.com.vanshop.utils.Utils;
import grintsys.com.vanshop.ux.MainActivity;
import grintsys.com.vanshop.ux.adapters.OrdersHistoryRecyclerAdapter;
import grintsys.com.vanshop.ux.dialogs.LoginExpiredDialogFragment;
import timber.log.Timber;

/**
 * Fragment shows the user's order history.
 */
public class OrdersHistoryFragment extends Fragment {

    private ProgressDialog progressDialog;

    // Fields referencing complex screen layouts.
    private View empty;
    private View content;
    private EditText beginEdit, endEdit;
    private Button searchButton;

    /**
     * Request metadata containing urls for endlessScroll.
     */
    private Metadata ordersMetadata;

    private OrdersHistoryRecyclerAdapter ordersHistoryRecyclerAdapter;
    private EndlessRecyclerScrollListener endlessRecyclerScrollListener;

    /**
     * Field for recovering scroll position.
     */
    private RecyclerView ordersRecycler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("%s - onCreateView", this.getClass().getSimpleName());
        MainActivity.setActionBarTitle(getString(R.string.Order_history));

        View view = inflater.inflate(R.layout.fragment_orders_history, container, false);

        progressDialog = Utils.generateProgressDialog(getActivity(), false);

        empty = view.findViewById(R.id.order_history_empty);
        content = view.findViewById(R.id.order_history_content);
        beginEdit = (EditText) view.findViewById(R.id.order_history_begin);
        endEdit = (EditText) view.findViewById(R.id.order_history_end);
        searchButton = (Button) view.findViewById(R.id.order_history_okButton);

        searchButton.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                loadOrders(null);
            }
        });

        final Calendar beginCalendar = Calendar.getInstance();
        final Calendar endCalendar = Calendar.getInstance();

        String myFormat = "yyyy/MM/dd";
        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        beginCalendar.add(Calendar.DAY_OF_MONTH, -5);
        beginEdit.setText(sdf.format(beginCalendar.getTime()));
        endEdit.setText(sdf.format(endCalendar.getTime()));

        beginEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(),
                        R.style.MyDatePicker,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year,int monthOfYear, int dayOfMonth) {
                                beginCalendar.set(Calendar.YEAR, year);
                                beginCalendar.set(Calendar.MONTH, monthOfYear);
                                beginCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                beginEdit.setText(sdf.format(beginCalendar.getTime()));
                            }
                        },
                        beginCalendar.get(Calendar.YEAR),
                        beginCalendar.get(Calendar.MONTH),
                        beginCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        endEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(),
                        R.style.MyDatePicker,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                endCalendar.set(Calendar.YEAR, year);
                                endCalendar.set(Calendar.MONTH, monthOfYear);
                                endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                endEdit.setText(sdf.format(endCalendar.getTime()));
                            }
                        },
                        endCalendar.get(Calendar.YEAR),
                        endCalendar.get(Calendar.MONTH),
                        endCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        prepareOrdersHistoryRecycler(view);
        loadOrders(null);
        return view;
    }

    /**
     * Prepare content recycler. Create custom adapter and endless scroll.
     *
     * @param view root fragment view.
     */
    private void prepareOrdersHistoryRecycler(View view) {
        ordersRecycler = (RecyclerView) view.findViewById(R.id.orders_history_recycler);
        ordersHistoryRecyclerAdapter = new OrdersHistoryRecyclerAdapter(new OrdersRecyclerInterface() {
            @Override
            public void onOrderSelected(View v, Order order) {
                Activity activity = getActivity();
                if (activity instanceof MainActivity) ((MainActivity) activity).onOrderSelected(order);
            }
        });
        ordersRecycler.setAdapter(ordersHistoryRecyclerAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ordersRecycler.getContext());
        ordersRecycler.setLayoutManager(layoutManager);
        ordersRecycler.setItemAnimator(new DefaultItemAnimator());
        ordersRecycler.setHasFixedSize(true);
        ordersRecycler.addItemDecoration(new RecyclerMarginDecorator(getResources().getDimensionPixelSize(R.dimen.base_margin)));

        endlessRecyclerScrollListener = new EndlessRecyclerScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                /*if (ordersMetadata != null && ordersMetadata.getLinks() != null && ordersMetadata.getLinks().getNext() != null) {
                    loadOrders(ordersMetadata.getLinks().getNext());
                } else {
                    Timber.d("CustomLoadMoreDataFromApi NO MORE DATA");
                }*/
            }
        };
        ordersRecycler.addOnScrollListener(endlessRecyclerScrollListener);
    }

    /**
     * Endless content loader. Should be used after views inflated.
     *
     * @param url null for fresh load. Otherwise use URLs from response metadata.
     */
    private void loadOrders(String url) {
        User user = SettingsMy.getActiveUser();
        if (user != null) {
            progressDialog.show();
            ordersHistoryRecyclerAdapter.clear();
            if (url == null) {
                //url = String.format(EndPoints.ORDERS, SettingsMy.getActualNonNullShop(getActivity()).getId());
                url = String.format(EndPoints.ORDERS_RANGE, user.getId(), beginEdit.getText().toString(), endEdit.getText().toString());
            }
            GsonRequest<OrderResponse> req = new GsonRequest<>(Request.Method.GET, url, null, OrderResponse.class, new Response.Listener<OrderResponse>() {
                @Override
                public void onResponse(OrderResponse response) {
                    ordersMetadata = response.getMetadata();
                    ordersHistoryRecyclerAdapter.addOrders(response.getOrders());

                    if (progressDialog != null) progressDialog.cancel();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (progressDialog != null) progressDialog.cancel();
                    MsgUtils.logAndShowErrorMessage(getActivity(), error);
                }
            }, getFragmentManager(), user.getAccessToken());

            req.setRetryPolicy(MyApplication.getSimpleRetryPolice());
            req.setShouldCache(false);
            MyApplication.getInstance().addToRequestQueue(req, CONST.ORDERS_HISTORY_REQUESTS_TAG);
        } else {
            LoginExpiredDialogFragment loginExpiredDialogFragment = new LoginExpiredDialogFragment();
            loginExpiredDialogFragment.show(getFragmentManager(), "loginExpiredDialogFragment");
        }
    }

    @Override
    public void onStop() {
        if (progressDialog != null) {
            // Hide progress dialog if exist.
            if (progressDialog.isShowing() && endlessRecyclerScrollListener != null) {
                // Fragment stopped during loading data. Allow new loading on return.
                endlessRecyclerScrollListener.resetLoading();
            }
            progressDialog.cancel();
        }
        MyApplication.getInstance().cancelPendingRequests(CONST.ORDERS_HISTORY_REQUESTS_TAG);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        if (ordersRecycler != null) ordersRecycler.clearOnScrollListeners();
        super.onDestroyView();
    }
}
