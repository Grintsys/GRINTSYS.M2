package grintsys.com.vanshop.ux.fragments.payment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import grintsys.com.vanshop.R;
import grintsys.com.vanshop.entities.client.Client;
import grintsys.com.vanshop.entities.client.Document;
import grintsys.com.vanshop.entities.payment.InvoiceItem;
import grintsys.com.vanshop.entities.payment.Payment;
import grintsys.com.vanshop.interfaces.DocumentRecyclerInterface;
import grintsys.com.vanshop.utils.RecyclerMarginDecorator;
import grintsys.com.vanshop.ux.adapters.DocumentsRecyclerAdapter;
import grintsys.com.vanshop.ux.fragments.dummy.DummyContent.DummyItem;

public class PaymentInvoiceFragment extends Fragment {

    private static final String ARG_CLIENT = "client";
    private OnListFragmentInteractionListener mListener;
    private RecyclerView documentsRecycler;
    private GridLayoutManager documentsRecyclerLayoutManager;
    private DocumentsRecyclerAdapter documentsRecyclerAdapter;
    private Client client;

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
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_document_list, container, false);

        if (documentsRecyclerAdapter == null || documentsRecyclerAdapter.getItemCount() == 0) {
            prepareRecyclerAdapter();
        }

        prepareDocumentRecycler(view);

        if(client != null && documentsRecycler != null)
            documentsRecyclerAdapter.addDocuments(client.getInvoiceList());

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
        documentsRecycler = (RecyclerView) view.findViewById(R.id.payment_document_recycler);
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
