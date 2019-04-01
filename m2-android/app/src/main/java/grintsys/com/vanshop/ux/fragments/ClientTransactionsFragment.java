package grintsys.com.vanshop.ux.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
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

import java.net.URLEncoder;
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
import grintsys.com.vanshop.entities.client.ClientTransactions;
import grintsys.com.vanshop.entities.client.ClientTransactionsResponse;
import grintsys.com.vanshop.interfaces.ClientTransactionsRecyclerInterface;
import grintsys.com.vanshop.listeners.OnSingleClickListener;
import grintsys.com.vanshop.utils.EndlessRecyclerScrollListener;
import grintsys.com.vanshop.utils.MsgUtils;
import grintsys.com.vanshop.utils.RecyclerMarginDecorator;
import grintsys.com.vanshop.utils.Utils;
import grintsys.com.vanshop.ux.MainActivity;
import grintsys.com.vanshop.ux.adapters.ClientTransactionsRecyclerAdapter;
import grintsys.com.vanshop.ux.dialogs.LoginExpiredDialogFragment;
import timber.log.Timber;

import static grintsys.com.vanshop.SettingsMy.PREF_CLIENT_CARD_CODE_SELECTED;
import static grintsys.com.vanshop.SettingsMy.getSettings;

/**
 * Fragment shows the user's order history.
 */
public class ClientTransactionsFragment extends Fragment {

    private ProgressDialog progressDialog;

    // Fields referencing complex screen layouts.
    private View empty;
    private View content;
    private EditText beginEdit, endEdit;
    private Button searchButton;
    private Calendar beginCalendar, endCalendar;
    private static final String CARD_CODE_ARG = "cardcode-arg";

    /**
     * Request metadata containing urls for endlessScroll.
     */
    private Metadata paymentsMetadata;

    private ClientTransactionsRecyclerAdapter transactionsRecyclerAdapter;
    private EndlessRecyclerScrollListener endlessRecyclerScrollListener;
    private String cardCode = "";

    /**
     * Field for recovering scroll position.
     */
    private RecyclerView transactionsRecycler;

    public static ClientTransactionsFragment newInstance() {
        Bundle args = new Bundle();
        ClientTransactionsFragment fragment = new ClientTransactionsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ClientTransactionsFragment newInstance(String cardCode) {
        Bundle args = new Bundle();
        args.putString(CARD_CODE_ARG, cardCode);
        ClientTransactionsFragment fragment = new ClientTransactionsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("%s - onCreateView", this.getClass().getSimpleName());
        MainActivity.setActionBarTitle(getString(R.string.ClientTransactions));

        View view = inflater.inflate(R.layout.fragment_client_transactions, container, false);

        progressDialog = Utils.generateProgressDialog(getActivity(), false);

        empty = view.findViewById(R.id.client_transaction_empty);
        content = view.findViewById(R.id.client_transaction_content);
        beginEdit = (EditText) view.findViewById(R.id.client_transaction_begin);
        endEdit = (EditText) view.findViewById(R.id.client_transaction_end);
        searchButton = (Button) view.findViewById(R.id.client_transaction_ok_button);


        Bundle args = getArguments();
        if(args != null){
            cardCode = args.getString(CARD_CODE_ARG);
        }

        String myFormat = "yyyy/MM/dd";
        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        endCalendar = Calendar.getInstance();
        beginCalendar = Calendar.getInstance();
        beginCalendar.add(Calendar.DAY_OF_MONTH, -2);
        beginEdit.setText(sdf.format(endCalendar.getTime()));

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

        endCalendar.add(Calendar.DAY_OF_MONTH, 15);
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
                loadTransactions(null, cardCode);
            }
        });

        prepareClientTransactionsRecycler(view);
        loadTransactions(null, cardCode);
        return view;
    }

    /**
     * Prepare content recycler. Create custom adapter and endless scroll.
     *
     * @param view root fragment view.
     */
    private void prepareClientTransactionsRecycler(View view) {
        transactionsRecycler = (RecyclerView) view.findViewById(R.id.client_transaction_history_recycler);
        transactionsRecyclerAdapter = new ClientTransactionsRecyclerAdapter(new ClientTransactionsRecyclerInterface() {
            @Override
            public void onClientTransactionsSelected(View v, ClientTransactions transaction) {
                Activity activity = getActivity();
            }
        });
        transactionsRecycler.setAdapter(transactionsRecyclerAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(transactionsRecycler.getContext());
        transactionsRecycler.setLayoutManager(layoutManager);
        transactionsRecycler.setItemAnimator(new DefaultItemAnimator());
        transactionsRecycler.setHasFixedSize(true);
        transactionsRecycler.addItemDecoration(new RecyclerMarginDecorator(getResources().getDimensionPixelSize(R.dimen.base_margin)));
    }

    /**
     * Endless content loader. Should be used after views inflated.
     *
     * @param url null for fresh load. Otherwise use URLs from response metadata.
     */
    private void loadTransactions(String url, String cardCode) {
        User user = SettingsMy.getActiveUser();
        if (user != null) {
            progressDialog.show();
            transactionsRecyclerAdapter.clear();
            String beginString = beginEdit.getText().toString(), endString = endEdit.getText().toString();

            if (url == null) {
                url = String.format(EndPoints.CLIENT_TRANSACTIONS, cardCode, beginString, endString);
            }
            GsonRequest<ClientTransactionsResponse> req = new GsonRequest<>(Request.Method.GET, url, null, ClientTransactionsResponse.class, new Response.Listener<ClientTransactionsResponse>() {
                @Override
                public void onResponse(ClientTransactionsResponse response) {
                    transactionsRecyclerAdapter.addPayments(response.getTransactions());

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
            MyApplication.getInstance().addToRequestQueue(req, CONST.CLIENT_TRANSACTIONS_REQUESTS_TAG);
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
        MyApplication.getInstance().cancelPendingRequests(CONST.CLIENT_TRANSACTIONS_REQUESTS_TAG);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        if (transactionsRecycler != null) transactionsRecycler.clearOnScrollListeners();
        super.onDestroyView();
    }
}
