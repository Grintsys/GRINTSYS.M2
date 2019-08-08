package grintsys.com.vanshop.ux.fragments.payment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
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
import grintsys.com.vanshop.entities.payment.Cash;
import grintsys.com.vanshop.entities.payment.CheckPayment;
import grintsys.com.vanshop.entities.payment.InvoiceItem;
import grintsys.com.vanshop.entities.payment.Payment;
import grintsys.com.vanshop.entities.payment.Transfer;
import grintsys.com.vanshop.listeners.OnSingleClickListener;
import grintsys.com.vanshop.utils.MsgUtils;
import grintsys.com.vanshop.ux.MainActivity;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PaymentGeneralFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PaymentGeneralFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentGeneralFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CLIENT = "client";
    private static final String ARG_PAYMENT = "payment";
    private TextView clientCardCode;
    private TextView clientName;
    private EditText comment;
    private EditText referenceNumber;
    private Payment payment;

    // TODO: Rename and change types of parameters
    private Client client;
    private OnFragmentInteractionListener mListener;

    public PaymentGeneralFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PaymentGeneralFragment newInstance(Client client) {
        PaymentGeneralFragment fragment = new PaymentGeneralFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLIENT, client);
        fragment.setArguments(args);
        return fragment;
    }

    public static PaymentGeneralFragment newInstance(Payment payment) {
        PaymentGeneralFragment fragment = new PaymentGeneralFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PAYMENT, payment);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_general, container, false);

        clientCardCode = (TextView) view.findViewById(R.id.payment_general_client_code);
        clientName = (TextView) view.findViewById(R.id.payment_general_client_name);
        comment = (EditText) view.findViewById(R.id.payment_general_comment);
        referenceNumber = (EditText) view.findViewById(R.id.payment_reference_number);

        comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ((MainActivity)getActivity()).setComment(comment.getText().toString());
            }
        });

        referenceNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ((MainActivity)getActivity()).setReferenceNumber(referenceNumber.getText().toString());
            }
        });

        Bundle args = getArguments();
        if(args != null){
            payment = (Payment) args.getSerializable(ARG_PAYMENT);
            if(payment != null){
                comment.setText(payment.getComment());
                clientName.setText(payment.getClient().getName());
                clientCardCode.setText(payment.getClient().getCardCode());
                referenceNumber.setText(payment.getDocEntry());
                //referenceNumber.setText(((MainActivity) getActivity()).getReferenceNumber() );
                //referenceNumber.setText(payment.getDocEntry());

                //DEM
                UpdatePaymentType(comment.getText().toString().toLowerCase());

                //Status 4 is Canceled
                if(payment.getStatus() == 4){
                    comment.setEnabled(false);
                    clientName.setEnabled(false);
                    clientCardCode.setEnabled(false);
                }

            }else{
                client = (Client) args.getSerializable(ARG_CLIENT);
                if(client != null){
                    clientName.setText(client.getName());
                    clientCardCode.setText(client.getCardCode());
                }
            }
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    public void UpdatePaymentType(String comment)
    {

        if (comment.contains("cheque") == true )
        {
            ((MainActivity)getActivity()).setPaymentType("Cheque");
        }
        else if(comment.contains("deposito") == true )
        {
            ((MainActivity)getActivity()).setPaymentType("Deposito");
        }
        else if(comment.contains("efectivo") == true )
        {
            ((MainActivity)getActivity()).setPaymentType("Efectivo");
        }
        else if(comment.contains("transferencia") == true )
        {
            ((MainActivity)getActivity()).setPaymentType("Transferencia");
        }

    }

}
