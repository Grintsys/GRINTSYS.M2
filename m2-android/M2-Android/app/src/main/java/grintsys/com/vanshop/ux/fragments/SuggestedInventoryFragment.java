package grintsys.com.vanshop.ux.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import grintsys.com.vanshop.CONST;
import grintsys.com.vanshop.MyApplication;
import grintsys.com.vanshop.R;
import grintsys.com.vanshop.utils.Utils;
import grintsys.com.vanshop.ux.MainActivity;
import grintsys.com.vanshop.ux.dialogs.LicensesDialogFragment;
import timber.log.Timber;

/**
 * Fragment shows app settings and information about used open source libraries.
 * Important is possibility of changing selected shop (if more shops exist).
 */
public class SuggestedInventoryFragment extends Fragment {

    private ProgressDialog progressDialog;

    /**
     * Spinner offering all available shops.
     */
    //private Spinner spinShopSelection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("%s - onCreateView", this.getClass().getSimpleName());
        View view = inflater.inflate(R.layout.fragment_suggested_inventory, container, false);

        //MainActivity.setActionBarTitle(getString(R.string.Settings));
        MainActivity.setActionBarTitle("Inventario Sugerido");

        progressDialog = Utils.generateProgressDialog(getActivity(), false);

      //  spinShopSelection = (Spinner) view.findViewById(R.id.settings_shop_selection_spinner);

        LinearLayout licensesLayout = (LinearLayout) view.findViewById(R.id.settings_licenses_layout);
        licensesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LicensesDialogFragment df = new LicensesDialogFragment();
                df.show(getFragmentManager(), LicensesDialogFragment.class.getSimpleName());
            }
        });

        //requestShops();
        return view;
    }


    @Override
    public void onStop() {
        MyApplication.getInstance().cancelPendingRequests(CONST.SETTINGS_REQUESTS_TAG);
        if (progressDialog != null) progressDialog.cancel();
        super.onStop();
    }
}
