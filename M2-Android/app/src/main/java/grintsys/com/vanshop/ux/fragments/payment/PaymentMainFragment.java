package grintsys.com.vanshop.ux.fragments.payment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import grintsys.com.vanshop.CONST;
import grintsys.com.vanshop.MyApplication;
import grintsys.com.vanshop.R;
import grintsys.com.vanshop.SettingsMy;
import grintsys.com.vanshop.api.EndPoints;
import grintsys.com.vanshop.api.GsonRequest;
import grintsys.com.vanshop.entities.Bank;
import grintsys.com.vanshop.entities.BankResponse;
import grintsys.com.vanshop.entities.User.User;
import grintsys.com.vanshop.entities.client.Client;
import grintsys.com.vanshop.entities.client.Document;
import grintsys.com.vanshop.entities.order.Order;
import grintsys.com.vanshop.entities.payment.Cash;
import grintsys.com.vanshop.entities.payment.CheckPayment;
import grintsys.com.vanshop.entities.payment.InvoiceItem;
import grintsys.com.vanshop.entities.payment.Payment;
import grintsys.com.vanshop.entities.payment.Transfer;
import grintsys.com.vanshop.entities.tenant.Tenant;
import grintsys.com.vanshop.listeners.OnSingleClickListener;
import grintsys.com.vanshop.utils.BluetoothPrinterPayment;
import grintsys.com.vanshop.utils.JsonUtils;
import grintsys.com.vanshop.utils.MsgUtils;
import grintsys.com.vanshop.ux.MainActivity;
import timber.log.Timber;

import static grintsys.com.vanshop.SettingsMy.getActiveUser;

public class PaymentMainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CARDCODE = "cardcode";
    private static final String ARG_CLIENT = "client-payment";
    private static final String ARG_PAYMENT = "payment";

    // TODO: Rename and change types of parameters
    private String mCardCode;

    private PaymentMainFragment.SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ProgressBar progressView;
    protected TextView cashText, transferText, checkText, totalText, totalInvoiceText;
    private Client client;
    private ArrayList<Bank> banks;
    private Payment payment;
    private Button paymentSaveButton, paymentSentToSapButton, paymentCancelButton, paymentPrintButton;

    //DEM July 30TH/ 2017 Sunday.
    //Validacion para revisar si hay monto por asignar a facturas.
    double TotalFacturas=0;
    boolean PagoInvalido = true;
    String docs;
    String CardCode;
    Double Balance;
    private ArrayList<Document> invoiceList;

    String totalString="";
    private Order order;
    public Context context;

    private OnFragmentInteractionListener mListener;

    public PaymentMainFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PaymentMainFragment newInstance(String cardCode) {
        PaymentMainFragment fragment = new PaymentMainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CARDCODE, cardCode);
        fragment.setArguments(args);
        return fragment;
    }

    public static PaymentMainFragment newInstance(Payment payment) {
        PaymentMainFragment fragment = new PaymentMainFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PAYMENT, payment);
        fragment.setArguments(args);
        return fragment;
    }

    public static PaymentMainFragment newInstance(Client client) {
        PaymentMainFragment fragment = new PaymentMainFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLIENT, client);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCardCode = getArguments().getString(ARG_CARDCODE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        setContentVisible(CONST.VISIBLE.CONTENT);
    }

    private void setContentVisible(CONST.VISIBLE visible) {
        if (progressView != null) {
            switch (visible) {
                case PROGRESS:
                    progressView.setVisibility(View.VISIBLE);
                    break;
                default: // Content
                    progressView.setVisibility(View.GONE);
            }
        } else {
            Timber.e(new RuntimeException(), "Setting content visibility with null views.");
        }
    }

    private void getClient(String card_code) {
        String url = String.format(SettingsMy.getActualTenant().getUrl() + EndPoints.CLIENT, card_code);
        setContentVisible(CONST.VISIBLE.PROGRESS);

        GsonRequest<Client> clientGsonRequest = new GsonRequest<>(Request.Method.GET, url, null, Client.class,
                new Response.Listener<Client>() {
                    @Override
                    public void onResponse(@NonNull Client response) {
                        client = response;
                        getBanks();
                        setContentVisible(CONST.VISIBLE.CONTENT);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setContentVisible(CONST.VISIBLE.EMPTY);
                MsgUtils.logAndShowErrorMessage(getActivity(), error);
            }
        });
        clientGsonRequest.setRetryPolicy(MyApplication.getSimpleRetryPolice());
        clientGsonRequest.setShouldCache(false);
        MyApplication.getInstance().addToRequestQueue(clientGsonRequest, CONST.CLIENT_REQUESTS_TAG);
    }

    private void getBanks() {
        setContentVisible(CONST.VISIBLE.PROGRESS);
        GsonRequest<BankResponse> banksGsonRequest = new GsonRequest<>(Request.Method.GET, SettingsMy.getActualTenant().getUrl() + EndPoints.BANKS, null, BankResponse.class,
                new Response.Listener<BankResponse>() {
                    @Override
                    public void onResponse(@NonNull BankResponse response) {
                        banks = response.getBanks();
                        setContentVisible(CONST.VISIBLE.CONTENT);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MsgUtils.logAndShowErrorMessage(getActivity(), error);
            }
        });
        banksGsonRequest.setRetryPolicy(MyApplication.getSimpleRetryPolice());
        banksGsonRequest.setShouldCache(true);
        MyApplication.getInstance().addToRequestQueue(banksGsonRequest, CONST.BANKS_TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_main, container, false);

        MainActivity.setActionBarTitle(getString(R.string.Payments));
        progressView = view.findViewById(R.id.payment_progress);

        //agregado Sept. 27.2017. Issues con invoices de pago anterior.    DEM.
        ((MainActivity) getActivity()).ClearPaymentData();

        paymentSentToSapButton = view.findViewById(R.id.product_payment_main_sent_to_sap);
        paymentSaveButton = view.findViewById(R.id.product_payment_main_save);
        paymentCancelButton = view.findViewById(R.id.product_payment_main_cancel);
        paymentPrintButton = view.findViewById(R.id.product_payment_main_print);

        paymentSentToSapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPaymentToSap();
                ((MainActivity) getActivity()).onAccountSelected();
                MsgUtils.showToast(getActivity(), MsgUtils.TOAST_TYPE_MESSAGE, getString(R.string.PaymentProcess), MsgUtils.ToastLength.SHORT);
            }
        });

        paymentSaveButton.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                sendPaymentToDraft();
                ((MainActivity) getActivity()).onAccountSelected();
                MsgUtils.showToast(getActivity(), MsgUtils.TOAST_TYPE_MESSAGE, getString(R.string.PaymentProcess), MsgUtils.ToastLength.SHORT);
            }
        });

        paymentCancelButton.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                cancelPayment(payment.getId());
                ((MainActivity)getActivity()).ClearPaymentData();
                ((MainActivity)getActivity()).onAccountSelected();
            }
        });


        //Print Payment receipt
        paymentPrintButton.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                User user = getActiveUser();

                //Variable respuesta para tomar mensaje de funcion de Imprimir
                String respuesta ="";

                ArrayList<InvoiceItem> invoices = ((MainActivity) getActivity()).getInvoiceItems();
                Client client = payment.getClient();

                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                String PaymentId = "";

                PaymentId =  String.valueOf(payment.getId());

                try
                {

                    if(user != null && client != null && invoices != null) {
                        respuesta = BluetoothPrinterPayment.print(context,
                                user.getPrintBluetoothAddress(),
                                date,
                                client,
                                user.getName(),
                                invoices,
                                0.0,
                                0.0,
                                payment.getComment(),
                                PaymentId,
                                payment.getTotalPaid());
                    }else
                    {
                        //MsgUtils.showToast((Activity)context, MsgUtils.TOAST_TYPE_MESSAGE, context.getString(R.string.Internal_error), MsgUtils.ToastLength.SHORT);
                        Toast.makeText(getActivity(), context.getString(R.string.Internal_error), Toast.LENGTH_LONG).show();
                    }

                    if ( respuesta != "")
                    {
                        //MsgUtils.showToast((Activity)context, MsgUtils.TOAST_TYPE_MESSAGE, respuesta, MsgUtils.ToastLength.LONG);
                        Toast.makeText(getActivity(), respuesta, Toast.LENGTH_LONG).show();
                    }


                }catch (Exception e){
                    //MsgUtils.showToast((Activity)context, MsgUtils.TOAST_TYPE_MESSAGE, e.getMessage().toString(), MsgUtils.ToastLength.LONG);
                    Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_LONG).show();

                };

            }
        });

        Bundle arguments = getArguments();
        if(arguments != null){
            payment = (Payment) arguments.getSerializable(ARG_PAYMENT);
            if(payment != null){
                ((MainActivity) getActivity()).ClearPaymentData();
                //((MainActivity) getActivity()).UpdateTransfer(payment.getTransfer());
                ((MainActivity) getActivity()).UpdateCash(payment.getCash());
                ((MainActivity) getActivity()).UpdateChecks(payment.getChecks());
                //((MainActivity) getActivity()).UpdateInvoiceItems(payment.getInvoices());
                getBanks();
            } else {
                String cardcode = arguments.getString(ARG_CARDCODE, "");
                client = (Client) arguments.getSerializable(ARG_CLIENT);
                if(client == null)
                    getClient(cardcode);
                else {
                    getBanks();
                }
            }
        }

        if(payment != null) {
            switch (payment.getStatus()){
                case 1:
                    paymentSaveButton.setVisibility(View.GONE);
                    paymentSentToSapButton.setVisibility(View.GONE);
                    paymentPrintButton.setVisibility(View.VISIBLE);
                case 3:
                    paymentSaveButton.setVisibility(View.GONE);
                    paymentSaveButton.setVisibility(View.VISIBLE);
                    paymentCancelButton.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    paymentSaveButton.setVisibility(View.GONE);
                    paymentSentToSapButton.setVisibility(View.GONE);
                    paymentCancelButton.setVisibility(View.GONE);
                    paymentPrintButton.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    paymentCancelButton.setVisibility(View.GONE);
                    paymentSaveButton.setVisibility(View.GONE);
                    paymentSentToSapButton.setVisibility(View.GONE);
                    paymentPrintButton.setVisibility(View.VISIBLE);
                    break;
                default:
                    paymentCancelButton.setVisibility(View.VISIBLE);
                    paymentSaveButton.setVisibility(View.VISIBLE);
                    paymentSentToSapButton.setVisibility(View.GONE);
            }
        }

        mSectionsPagerAdapter = new PaymentMainFragment.SectionsPagerAdapter(getChildFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.payment_view_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        return view;
    }

    public void sendPaymentToDraft() {

        final User user = SettingsMy.getActiveUser();
        final Tenant tenant = SettingsMy.getActualTenant();
        if (user == null && tenant == null)
            return;


        JSONObject joPayment = new JSONObject();
        JSONArray joInvoices = new JSONArray();

        Bank bank =  ((MainActivity) getActivity()).getBank();
        double payedAmount = ((MainActivity) getActivity()).GetAmount();
        String comment = ((MainActivity) getActivity()).getPaymentType() + "|" + ((MainActivity) getActivity()).getComment();
        String referenceNumber = ((MainActivity) getActivity()).getReferenceNumber();
        Date payedDate = ((MainActivity) getActivity()).getDate();
        ArrayList<InvoiceItem> invoices = ((MainActivity) getActivity()).getInvoiceItems();

        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        //TODO: FIX THIS WE MUST GET THE SPINNER PAYMENT
        int paymentType = 0;

        try {
            joPayment.put(JsonUtils.TAG_TENANT_ID, tenant.getId());
            joPayment.put(JsonUtils.TAG_BANK_ID, bank.getId());
            joPayment.put(JsonUtils.TAG_PAYMENT_TYPE, paymentType);
            joPayment.put(JsonUtils.TAG_PAYED_AMOUNT_ID, payedAmount);
            joPayment.put(JsonUtils.TAG_COMMENT, comment);
            joPayment.put(JsonUtils.TAG_REFERENCE_NUMBER, referenceNumber);
            joPayment.put(JsonUtils.TAG_PAYMENT_TYPE, sdf.format(payedDate));

            if(invoices != null) {
                for (InvoiceItem invoice : invoices) {
                    JSONObject invoiceJSON = new JSONObject();
                    invoiceJSON.put(JsonUtils.TAG_DOCUMENT_CODE, invoice.getDocumentNumber());
                    joInvoices.put(invoiceJSON);
                }
                joPayment.put(JsonUtils.TAG_PAYMENT_ITEM_LIST, joInvoices.toString());
            }
        } catch (JSONException e) {
            String message = "Parse new transfer exception.";
            Timber.e(e, message);
            MsgUtils.showToast(getActivity(), MsgUtils.TOAST_TYPE_MESSAGE, message, MsgUtils.ToastLength.SHORT);
        }

        GsonRequest<JSONObject> req = new GsonRequest<>(Request.Method.GET, EndPoints.ADD_PAYMENT,null, JSONObject.class,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject payment) {
                        //progressDialog.cancel();
                        Timber.d("Esto devolvio %s", payment);
                        MsgUtils.showToast(getActivity(), MsgUtils.TOAST_TYPE_MESSAGE, getString(R.string.Success), MsgUtils.ToastLength.SHORT);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //if (progressDialog != null) progressDialog.cancel();
                MsgUtils.logAndShowErrorMessage(getActivity(), error);
            }
        }, getFragmentManager(), user.getAccessToken());
        req.setRetryPolicy(MyApplication.getSimpleRetryPolice());
        req.setShouldCache(false);
        MyApplication.getInstance().addToRequestQueue(req, CONST.SENT_PAYMENT_TAG);
    }

    public void sendPaymentToSap()
    {

    }

    private void putPayment(Client client, Cash cash, Transfer transfer, ArrayList<CheckPayment> checks, ArrayList<InvoiceItem> invoices, String comment, String refence) {

    }

    public void setFragments(View view){

        mSectionsPagerAdapter = new PaymentMainFragment.SectionsPagerAdapter(getFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.payment_view_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }


    public void cancelPayment(int id)
    {
        User user = SettingsMy.getActiveUser();

        if(user == null)
            return;

        String url = String.format(EndPoints.PAYMENT_DELETE, id);

        GsonRequest<JSONObject> req = new GsonRequest<>(Request.Method.GET, url, null, JSONObject.class,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject payment) {
                        Timber.d("Esto devolvio %s", payment);
                        ((MainActivity) getActivity()).ClearPaymentData();
                        MsgUtils.showToast(getActivity(), MsgUtils.TOAST_TYPE_MESSAGE, getString(R.string.Success), MsgUtils.ToastLength.SHORT);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //if (progressDialog != null) progressDialog.cancel();
                MsgUtils.logAndShowErrorMessage(getActivity(), error);
            }
        }, getFragmentManager(), user.getAccessToken());
        req.setRetryPolicy(MyApplication.getSimpleRetryPolice());
        req.setShouldCache(false);
        MyApplication.getInstance().addToRequestQueue(req, CONST.CANCEL_PAYMENT_TAG);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private int count = 2;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            if(position == 0)
                fragment = PaymentInvoiceFragment.newInstance(client);

            if(position == 1)
                fragment = PaymentTransferFragment.newInstance(client, banks);

            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return count;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = "";

            switch (position){
                case 0: title = "Factura";
                    break;
                case 1: title = "Datos";
                    break;
            }

            return title;
        }
    }
}
