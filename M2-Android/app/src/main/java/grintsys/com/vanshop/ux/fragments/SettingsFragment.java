package grintsys.com.vanshop.ux.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;

import grintsys.com.vanshop.CONST;
import grintsys.com.vanshop.MyApplication;
import grintsys.com.vanshop.R;
import grintsys.com.vanshop.SettingsMy;
import grintsys.com.vanshop.api.EndPoints;
import grintsys.com.vanshop.api.GsonRequest;
import grintsys.com.vanshop.entities.tenant.Tenant;
import grintsys.com.vanshop.entities.tenant.TenantResponse;
import grintsys.com.vanshop.entities.tenant.TenantResult;
import grintsys.com.vanshop.utils.MsgUtils;
import grintsys.com.vanshop.utils.Utils;
import grintsys.com.vanshop.ux.MainActivity;
import grintsys.com.vanshop.ux.adapters.ShopSpinnerAdapter;
import grintsys.com.vanshop.ux.dialogs.LicensesDialogFragment;
import grintsys.com.vanshop.ux.dialogs.RestartDialogFragment;
import timber.log.Timber;

/**
 * Fragment shows app settings and information about used open source libraries.
 * Important is possibility of changing selected shop (if more shops exist).
 */
public class SettingsFragment extends Fragment {

    private ProgressDialog progressDialog;
    private Button applyButton;
    private Tenant selectedTenant;
    private EditText endpointTextView;

    /**
     * Spinner offering all available shops.
     */
    private Spinner spinShopSelection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("%s - onCreateView", this.getClass().getSimpleName());
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        MainActivity.setActionBarTitle(getString(R.string.Settings));

        progressDialog = Utils.generateProgressDialog(getActivity(), false);

        spinShopSelection = (Spinner) view.findViewById(R.id.settings_shop_selection_spinner);

        LinearLayout licensesLayout = (LinearLayout) view.findViewById(R.id.settings_licenses_layout);
        licensesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LicensesDialogFragment df = new LicensesDialogFragment();
                df.show(getFragmentManager(), LicensesDialogFragment.class.getSimpleName());
            }
        });

        endpointTextView = view.findViewById(R.id.settings_endpoint);
        endpointTextView.setText(EndPoints.API_URL);

        applyButton = view.findViewById(R.id.settings_apply);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String endpoint = endpointTextView.getText().toString();

                if(selectedTenant != null
                        && selectedTenant.getId() != SettingsMy.getActualNonNullShop(getActivity()).getId()
                        && !endpoint.equals(EndPoints.API_URL)){
                    EndPoints.API_URL = endpoint;
                    RestartDialogFragment rdf = RestartDialogFragment.newInstance(selectedTenant);
                    rdf.show(getFragmentManager(), RestartDialogFragment.class.getSimpleName());
                } else if(!endpoint.equals(EndPoints.API_URL)) {
                    EndPoints.API_URL = endpoint;
                } else if(selectedTenant != null
                        && selectedTenant.getId() != SettingsMy.getActualNonNullShop(getActivity()).getId()){
                    RestartDialogFragment rdf = RestartDialogFragment.newInstance(selectedTenant);
                    rdf.show(getFragmentManager(), RestartDialogFragment.class.getSimpleName());
                }
            }
        });

        requestShops();
        return view;
    }

    /**
     * Load available shops from server.
     */
    private void requestShops() {
        if (progressDialog != null) progressDialog.show();
        GsonRequest<TenantResponse> getShopsRequest = new GsonRequest<>(Request.Method.GET, EndPoints.TENANTS, null, TenantResponse.class,
                new Response.Listener<TenantResponse>() {
                    @Override
                    public void onResponse(@NonNull TenantResponse response) {
                        Timber.d("Available shops response: %s", response.toString());
                        setSpinShops(response.result.getTenantList());
                        if (progressDialog != null) progressDialog.cancel();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog != null) progressDialog.cancel();
                MsgUtils.logAndShowErrorMessage(getActivity(), error);
            }
        });
        getShopsRequest.setRetryPolicy(MyApplication.getSimpleRetryPolice());
        getShopsRequest.setShouldCache(false);
        MyApplication.getInstance().addToRequestQueue(getShopsRequest, CONST.SETTINGS_REQUESTS_TAG);
    }

    /**
     * Prepare spinner with tenants and pre-select already selected one.
     *
     * @param tenants list of tenants received from server.
     */
    private void setSpinShops(List<Tenant> tenants) {
        ShopSpinnerAdapter adapterLanguage = new ShopSpinnerAdapter(getActivity(), tenants, false);
        spinShopSelection.setAdapter(adapterLanguage);

        int position = 0;
        for (int i = 0; i < tenants.size(); i++) {
            if (tenants.get(i).getId() == SettingsMy.getActualNonNullShop(getActivity()).getId()) {
                position = i;
                break;
            }
        }
        spinShopSelection.setSelection(position);
        spinShopSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTenant = (Tenant) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Timber.d("Nothing selected");
            }
        });
    }

    @Override
    public void onStop() {
        MyApplication.getInstance().cancelPendingRequests(CONST.SETTINGS_REQUESTS_TAG);
        if (progressDialog != null) progressDialog.cancel();
        super.onStop();
    }
}
