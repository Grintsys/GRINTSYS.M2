package grintsys.com.vanshop.ux.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

import grintsys.com.vanshop.CONST;
import grintsys.com.vanshop.MyApplication;
import grintsys.com.vanshop.entities.client.Client;
import grintsys.com.vanshop.ux.adapters.ClientsRecyclerAdapter;

/**
 * Fragment shows app settings and information about used open source libraries.
 * Important is possibility of changing selected shop (if more shops exist).
 */
//public class TakeInventoryFragment extends Fragment {
public class TakeInventoryFragment extends Activity {

    private ProgressDialog progressDialog;
    private List<Client> clients;

    private RecyclerView clientsRecycler;
    private GridLayoutManager clientsRecyclerLayoutManager;
    private ClientsRecyclerAdapter clientsRecyclerAdapter;

    /**
     * Spinner offering all available shops.
     */
    //private Spinner spinShopSelection;
    private Spinner spinClientSelection;
    private EditText edtSKU;
    private Button btnScan;

    /*
    //@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("%s - onCreateView", this.getClass().getSimpleName());
        View view = inflater.inflate(R.layout.fragment_take_inventory, container, false);

        //MainActivity.setActionBarTitle(getString(R.string.Settings));
        MainActivity.setActionBarTitle("Tomar Inventario");

        progressDialog = Utils.generateProgressDialog(this, false);

        //spinShopSelection = (Spinner) view.findViewById(R.id.settings_shop_selection_spinner);
        spinClientSelection = (Spinner) view.findViewById(R.id.spiClientes);
        btnScan = (Button) view.findViewById(R.id.btnScan);
        edtSKU = (EditText) view.findViewById(R.id.edtSKU);
        String SKU="";

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanNow(v);
                //this.hide(this);
                //isHidden(true);
                v.setVisibility(View.GONE);

            }
        });

            //spinClientSelection.
        //requestShops();
        //getClients();
        //getView().setVisibility(View.GONE);
        return view;
    }



    private void getClients() {
        //loadMoreProgress.setVisibility(View.VISIBLE);

        String url = EndPoints.CLIENTS;


        GsonRequest<ClientListResponse> getClientsRequest = new GsonRequest<>(Request.Method.GET, url, null, ClientListResponse.class,
                new Response.Listener<ClientListResponse>() {
                    @Override
                    public void onResponse(@NonNull ClientListResponse response) {
//                        Timber.d("response:" + response.toString());
                        clientsRecyclerAdapter.addClients(response.getClients());
                        checkEmptyContent();
                        //loadMoreProgress.setVisibility(View.GONE);
                        //clientsRecycler.setAdapter(clientsRecyclerAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //if (loadMoreProgress != null) loadMoreProgress.setVisibility(View.GONE);
                //checkEmptyContent();
                //MsgUtils.logAndShowErrorMessage(getActivity(), error);
            }
        });
        getClientsRequest.setRetryPolicy(MyApplication.getSimpleRetryPolice());
        getClientsRequest.setShouldCache(false);
        MyApplication.getInstance().addToRequestQueue(getClientsRequest, CONST.CLIENT_REQUESTS_TAG);
    }

    private void checkEmptyContent() {
        if (clientsRecyclerAdapter != null && clientsRecyclerAdapter.getItemCount() > 0) {
            clientsRecycler.setVisibility(View.VISIBLE);
        } else {
            clientsRecycler.setVisibility(View.INVISIBLE);
        }
    }


    */

    /*
     * event handler for scan button
     * @param view view of the activity
     */
    public void scanNow(View view){

        //IntentIntegrator integrator = new IntentIntegrator.forFragment(this).setPrompt("Some prompt").initiateScan();
        //IntentIntegrator integrator = new IntentIntegrator.startIntent(intent, this);

        //IntentIntegrator integrator = new IntentIntegrator(this);

        IntentIntegrator integrator = new IntentIntegrator(this);

        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Escanear SKU");
        integrator.setResultDisplayDuration(0);
        integrator.setWide();  // Wide scanning rectangle, may work better for 1D barcodes
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();
    }

    /**
     * function handle scan result
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

            // display it on screen
            //formatTxt.setText("FORMAT: " + scanFormat);
            edtSKU.setText(scanContent);

        }else{
            Toast toast = Toast.makeText(getApplicationContext(),"No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }




    @Override
    public void onStop() {
        MyApplication.getInstance().cancelPendingRequests(CONST.SETTINGS_REQUESTS_TAG);
        if (progressDialog != null) progressDialog.cancel();
        super.onStop();
    }
}
