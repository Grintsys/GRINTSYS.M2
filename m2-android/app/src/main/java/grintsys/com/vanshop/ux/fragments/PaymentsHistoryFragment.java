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
import grintsys.com.vanshop.entities.payment.Payment;
import grintsys.com.vanshop.entities.payment.PaymentResponse;
import grintsys.com.vanshop.interfaces.PaymentsRecyclerInterface;
import grintsys.com.vanshop.listeners.OnSingleClickListener;
import grintsys.com.vanshop.utils.EndlessRecyclerScrollListener;
import grintsys.com.vanshop.utils.MsgUtils;
import grintsys.com.vanshop.utils.RecyclerMarginDecorator;
import grintsys.com.vanshop.utils.Utils;
import grintsys.com.vanshop.ux.MainActivity;
import grintsys.com.vanshop.ux.adapters.PaymentsHistoryRecyclerAdapter;
import grintsys.com.vanshop.ux.dialogs.LoginExpiredDialogFragment;
import timber.log.Timber;

/**
 * Fragment shows the user's order history.
 */
public class PaymentsHistoryFragment extends Fragment {

    private ProgressDialog progressDialog;

    // Fields referencing complex screen layouts.
    private View empty;
    private View content;
    private EditText beginEdit, endEdit;
    private Button searchButton;
    private Calendar beginCalendar, endCalendar;

    /**
     * Request metadata containing urls for endlessScroll.
     */
    private Metadata paymentsMetadata;

    private PaymentsHistoryRecyclerAdapter paymentsHistoryRecyclerAdapter;
    private EndlessRecyclerScrollListener endlessRecyclerScrollListener;

    /**
     * Field for recovering scroll position.
     */
    private RecyclerView paymentsRecycler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("%s - onCreateView", this.getClass().getSimpleName());
        MainActivity.setActionBarTitle(getString(R.string.PaymentHistory));

        View view = inflater.inflate(R.layout.fragment_payment_history, container, false);

        progressDialog = Utils.generateProgressDialog(getActivity(), false);

        empty = view.findViewById(R.id.payment_history_empty);
        content = view.findViewById(R.id.payment_history_content);
        beginEdit = (EditText) view.findViewById(R.id.payment_history_begin);
        endEdit = (EditText) view.findViewById(R.id.payment_history_end);
        searchButton = (Button) view.findViewById(R.id.payment_history_ok_button);

        String myFormat = "yyyy/MM/dd";
        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        endCalendar = Calendar.getInstance();
        beginCalendar = Calendar.getInstance();

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

        endCalendar.add(Calendar.DAY_OF_MONTH, 5);
        endEdit.setText(sdf.format(endCalendar.getTime()));

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

        searchButton.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                loadPayments(null);
            }
        });

        preparePaymentsHistoryRecycler(view);

        loadPayments(null);
        return view;
    }

    /**
     * Prepare content recycler. Create custom adapter and endless scroll.
     *
     * @param view root fragment view.
     */
    private void preparePaymentsHistoryRecycler(View view) {
        paymentsRecycler = (RecyclerView) view.findViewById(R.id.payments_history_recycler);
        paymentsHistoryRecyclerAdapter = new PaymentsHistoryRecyclerAdapter(new PaymentsRecyclerInterface() {
            @Override
            public void onPaymentSelected(View v, Payment payment) {
                Activity activity = getActivity();
                if (activity instanceof MainActivity) ((MainActivity) activity).onPaymentSelected(payment);
            }
        });
        paymentsRecycler.setAdapter(paymentsHistoryRecyclerAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(paymentsRecycler.getContext());
        paymentsRecycler.setLayoutManager(layoutManager);
        paymentsRecycler.setItemAnimator(new DefaultItemAnimator());
        paymentsRecycler.setHasFixedSize(true);
        paymentsRecycler.addItemDecoration(new RecyclerMarginDecorator(getResources().getDimensionPixelSize(R.dimen.base_margin)));

        endlessRecyclerScrollListener = new EndlessRecyclerScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                /*if (paymentsMetadata != null && paymentsMetadata.getLinks() != null && paymentsMetadata.getLinks().getNext() != null) {
                    loadPayments(paymentsMetadata.getLinks().getNext());
                } else {
                    Timber.d("CustomLoadMoreDataFromApi NO MORE DATA");
                }*/
            }
        };
        paymentsRecycler.addOnScrollListener(endlessRecyclerScrollListener);
    }

    /**
     * Endless content loader. Should be used after views inflated.
     *
     * @param url null for fresh load. Otherwise use URLs from response metadata.
     */
    private void loadPayments(String url) {
        User user = SettingsMy.getActiveUser();
        if (user != null) {
            progressDialog.show();
            if (url == null) {
                url = String.format(EndPoints.PAYMENTS, user.getId(),beginEdit.getText().toString(),endEdit.getText().toString());
            }
            paymentsHistoryRecyclerAdapter.clear();
            GsonRequest<PaymentResponse> req = new GsonRequest<>(Request.Method.GET, url, null, PaymentResponse.class, new Response.Listener<PaymentResponse>() {
                @Override
                public void onResponse(PaymentResponse response) {
                    //paymentsMetadata = response.getMetadata();
                    paymentsHistoryRecyclerAdapter.addPayments(response.getPayments());
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
            MyApplication.getInstance().addToRequestQueue(req, CONST.PAYMENTS_HISTORY_REQUESTS_TAG);
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
        MyApplication.getInstance().cancelPendingRequests(CONST.PAYMENTS_HISTORY_REQUESTS_TAG);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        if (paymentsRecycler != null) paymentsRecycler.clearOnScrollListeners();
        super.onDestroyView();
    }
}
