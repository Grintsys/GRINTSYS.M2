package grintsys.com.vanshop.ux.fragments.payment;

import android.content.Context;
import android.location.SettingInjectorService;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;

import grintsys.com.vanshop.CONST;
import grintsys.com.vanshop.MyApplication;
import grintsys.com.vanshop.R;
import grintsys.com.vanshop.SettingsMy;
import grintsys.com.vanshop.api.EndPoints;
import grintsys.com.vanshop.api.GsonRequest;
import grintsys.com.vanshop.entities.User.User;
import grintsys.com.vanshop.entities.client.Client;
import grintsys.com.vanshop.entities.client.Document;
import grintsys.com.vanshop.entities.client.DocumentListResult;
import grintsys.com.vanshop.entities.invoice.InvoiceListResult;
import grintsys.com.vanshop.entities.payment.InvoiceItem;
import grintsys.com.vanshop.entities.payment.Payment;
import grintsys.com.vanshop.entities.tenant.Tenant;
import grintsys.com.vanshop.interfaces.DocumentRecyclerInterface;
import grintsys.com.vanshop.utils.MsgUtils;
import grintsys.com.vanshop.utils.RecyclerMarginDecorator;
import grintsys.com.vanshop.ux.MainActivity;
import grintsys.com.vanshop.ux.adapters.DocumentsRecyclerAdapter;
import grintsys.com.vanshop.ux.fragments.dummy.DummyContent.DummyItem;
import timber.log.Timber;

public class PaymentInvoiceFragment extends Fragment {

    private static final String ARG_CLIENT = "client";
    private OnListFragmentInteractionListener mListener;
    private RecyclerView documentsRecycler;
    private GridLayoutManager documentsRecyclerLayoutManager;
    private DocumentsRecyclerAdapter documentsRecyclerAdapter;
    private Client client;

    private TextView paymentTextView;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PaymentInvoiceFragment() {
    }

    public static PaymentInvoiceFragment newInstance(Client client) {
        PaymentInvoiceFragment fragment = new PaymentInvoiceFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLIENT, client);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            client = (Client) getArguments().getSerializable(ARG_CLIENT);

            if(client != null)
                loadInvoices(client.getId());
        }
    }

    private void loadInvoices(long id){

        User user = SettingsMy.getActiveUser();
        Tenant tenant = SettingsMy.getActualTenant();
        if(tenant == null || user == null)
            return;

        String url = String.format(EndPoints.INVOICES, id);

        GsonRequest<DocumentListResult> req = new GsonRequest<>(Request.Method.GET, url, null, DocumentListResult.class,
                new Response.Listener<DocumentListResult>() {
                    @Override
                    public void onResponse(DocumentListResult response) {
                        Timber.d("Esto devolvio %s", response);

                        if(response.result.getDocuments().size() <= 0)
                            paymentTextView.setVisibility(View.GONE);

                        ((MainActivity)getActivity()).ClearPaymentData();
                        documentsRecyclerAdapter.addDocuments(response.result.getDocuments());
                        //documentsRecyclerAdapter.notifyDataSetChanged();
                        //MsgUtils.showToast(getActivity(), MsgUtils.TOAST_TYPE_MESSAGE, getString(R.string.Success), MsgUtils.ToastLength.SHORT);
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
        MyApplication.getInstance().addToRequestQueue(req, CONST.DOCUMENT_REQUESTS_TAG);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_document_list, container, false);
        paymentTextView = view.findViewById(R.id.payment_document_text);

        if (documentsRecyclerAdapter == null || documentsRecyclerAdapter.getItemCount() == 0) {
            prepareRecyclerAdapter();
        }

        prepareDocumentRecycler(view);

        return view;
    }

    private void prepareRecyclerAdapter() {
        //On click event
        documentsRecyclerAdapter = new DocumentsRecyclerAdapter(getActivity(), new DocumentRecyclerInterface() {
            @Override
            public void onDocumentRecyclerInterface(View caller, Document document) {
            }
        }, true);
    }

    private void prepareDocumentRecycler(View view) {
        documentsRecycler = view.findViewById(R.id.payment_document_recycler);
        documentsRecycler.addItemDecoration(new RecyclerMarginDecorator(getActivity(), RecyclerMarginDecorator.ORIENTATION.BOTH));
        documentsRecycler.setItemAnimator(new DefaultItemAnimator());
        documentsRecycler.setHasFixedSize(true);
        documentsRecyclerLayoutManager = new GridLayoutManager(getActivity(), 1);
        documentsRecycler.setLayoutManager(documentsRecyclerLayoutManager);
        documentsRecycler.setAdapter(documentsRecyclerAdapter);
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

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }
}
