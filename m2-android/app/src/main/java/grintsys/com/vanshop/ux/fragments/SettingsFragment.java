package grintsys.com.vanshop.ux.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

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
import grintsys.com.vanshop.entities.Shop;
import grintsys.com.vanshop.entities.ShopResponse;
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

        requestShops();
        return view;
    }

    /**
     * Load available shops from server.
     */
    private void requestShops() {
        if (progressDialog != null) progressDialog.show();
        GsonRequest<ShopResponse> getShopsRequest = new GsonRequest<>(Request.Method.GET, EndPoints.SHOPS, null, ShopResponse.class,
                new Response.Listener<ShopResponse>() {
                    @Override
                    public void onResponse(@NonNull ShopResponse response) {
                        Timber.d("Available shops response: %s", response.toString());
                        setSpinShops(response.getShopList());
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
     * Prepare spinner with shops and pre-select already selected one.
     *
     * @param shops list of shops received from server.
     */
    private void setSpinShops(List<Shop> shops) {
        ShopSpinnerAdapter adapterLanguage = new ShopSpinnerAdapter(getActivity(), shops, false);
        spinShopSelection.setAdapter(adapterLanguage);

        int position = 0;
        for (int i = 0; i < shops.size(); i++) {
            if (shops.get(i).getId() == SettingsMy.getActualNonNullShop(getActivity()).getId()) {
                position = i;
                break;
            }
        }
        spinShopSelection.setSelection(position);
        spinShopSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Shop selectedShop = (Shop) parent.getItemAtPosition(position);
                if (selectedShop != null && selectedShop.getId() != SettingsMy.getActualNonNullShop(getActivity()).getId()) {
                    RestartDialogFragment rdf = RestartDialogFragment.newInstance(selectedShop);
                    rdf.show(getFragmentManager(), RestartDialogFragment.class.getSimpleName());
                } else {
                    Timber.e("Selected null or same shop.");
                }
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
