package grintsys.com.vanshop.ux.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import grintsys.com.vanshop.BuildConfig;
import grintsys.com.vanshop.CONST;
import grintsys.com.vanshop.MyApplication;
import grintsys.com.vanshop.R;
import grintsys.com.vanshop.SettingsMy;
import grintsys.com.vanshop.api.EndPoints;
import grintsys.com.vanshop.api.GsonRequest;
import grintsys.com.vanshop.entities.User.User;
import grintsys.com.vanshop.listeners.OnSingleClickListener;
import grintsys.com.vanshop.utils.MsgUtils;
import grintsys.com.vanshop.utils.Utils;
import grintsys.com.vanshop.ux.MainActivity;
import timber.log.Timber;

/**
 * Fragment provides the account screen with options such as logging, editing and more.
 */
public class AccountFragment extends Fragment {

    private ProgressDialog pDialog;

    /**
     * Indicates if user data should be loaded from server or from memory.
     */
    private boolean mAlreadyLoaded = false;

    // User information
    private LinearLayout userInfoLayout;
    private TextView tvUserName;
    private TextView tvEmail;
    private Button backButton;
    private Button myPayments;
    private TextView tvVersion;

    // Actions
    private Button myOrdersBtn;
    private Button myPaymentsBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("%s - OnCreateView", this.getClass().getSimpleName());
        MainActivity.setActionBarTitle(getString(R.string.Profile));

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        pDialog = Utils.generateProgressDialog(getActivity(), false);

        userInfoLayout = (LinearLayout) view.findViewById(R.id.account_user_info);
        tvUserName = (TextView) view.findViewById(R.id.account_name);
        tvEmail = (TextView) view.findViewById(R.id.account_email);
        tvVersion = (TextView) view.findViewById(R.id.account_app_version);
        backButton = (Button) view.findViewById(R.id.account_back);

        myOrdersBtn = (Button) view.findViewById(R.id.account_my_orders);
        myOrdersBtn.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (getActivity() instanceof MainActivity)
                    ((MainActivity) getActivity()).onOrdersHistory();
            }
        });

        myPayments = (Button) view.findViewById(R.id.account_payment);
        myPayments.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (getActivity() instanceof MainActivity)
                    ((MainActivity) getActivity()).onPaymentsHistory();
            }
        });

        tvVersion.setText(BuildConfig.VERSION_NAME);

        Button settingsBtn = (Button) view.findViewById(R.id.account_settings);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity != null && activity instanceof MainActivity)
                    ((MainActivity) getActivity()).startSettingsFragment();
            }
        });

        Button bluetoothBtn = (Button) view.findViewById(R.id.account_printer_setting);
        bluetoothBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity != null && activity instanceof MainActivity)
                    ((MainActivity) getActivity()).startBluetoothFragment();
            }
        });

        User user = SettingsMy.getActiveUser();
        if (user != null) {
            Timber.d("user: %s", user.toString());
            // Sync user data if fragment created (not reuse from backstack)
            if (savedInstanceState == null && !mAlreadyLoaded) {
                mAlreadyLoaded = true;
                syncUserData(user);
            } else {
                refreshScreen(user);
            }
        } else {
            refreshScreen(null);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timber.d("onBackButtonClick");
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }

    private void syncUserData(@NonNull User user) {
        String url = String.format(EndPoints.USER_SINGLE, user.getId());
        pDialog.show();

        GsonRequest<User> getUser = new GsonRequest<>(Request.Method.GET, url, null, User.class,
                new Response.Listener<User>() {
                    @Override
                    public void onResponse(@NonNull User response) {
                        Timber.d("response: %s", response.toString());
                        SettingsMy.setActiveUser(response);
                        refreshScreen(SettingsMy.getActiveUser());
                        if (pDialog != null) pDialog.cancel();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (pDialog != null) pDialog.cancel();
                MsgUtils.logAndShowErrorMessage(getActivity(), error);
            }
        }, getFragmentManager(), user.getAccessToken());
        getUser.setRetryPolicy(MyApplication.getDefaultRetryPolice());
        getUser.setShouldCache(false);
        MyApplication.getInstance().addToRequestQueue(getUser, CONST.ACCOUNT_REQUESTS_TAG);
    }

    private void refreshScreen(User user) {
        if (user == null) {
            userInfoLayout.setVisibility(View.GONE);
            myOrdersBtn.setVisibility(View.GONE);
        } else {
            userInfoLayout.setVisibility(View.VISIBLE);
            myOrdersBtn.setVisibility(View.VISIBLE);

            tvUserName.setText(user.getName());
            tvEmail.setText(user.getEmail());
        }
    }

    /**
     * The method combines two strings. As the string separator is used space or comma.
     *
     * @param result   first part of final string.
     * @param append   second part of final string.
     * @param addComma true if comma with space should be used as separator. Otherwise is used space.
     * @return concatenated string.
     */
    private String appendCommaText(String result, String append, boolean addComma) {
        if (result != null && !result.isEmpty()) {
            if (append != null && !append.isEmpty()) {
                if (addComma)
                    result += getString(R.string.format_comma_prefix, append);
                else
                    result += getString(R.string.format_space_prefix, append);
            }
            return result;
        } else {
            return append;
        }
    }

    @Override
    public void onStop() {
        MyApplication.getInstance().getRequestQueue().cancelAll(CONST.ACCOUNT_REQUESTS_TAG);
        super.onStop();
    }
}
