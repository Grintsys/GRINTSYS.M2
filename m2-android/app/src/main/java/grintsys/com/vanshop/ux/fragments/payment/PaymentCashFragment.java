package grintsys.com.vanshop.ux.fragments.payment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import grintsys.com.vanshop.R;
import grintsys.com.vanshop.entities.payment.Cash;
import grintsys.com.vanshop.ux.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PaymentCashFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PaymentCashFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentCashFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private final static String ARG_CASH = "cash";

    private EditText amountEdit;
    private TextView mainAmountEdit;
    private Cash cash = new Cash();

    private OnFragmentInteractionListener mListener;

    public PaymentCashFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PaymentCashFragment newInstance() {
        PaymentCashFragment fragment = new PaymentCashFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static PaymentCashFragment newInstance(Cash cash) {
        PaymentCashFragment fragment = new PaymentCashFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CASH, cash);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_cash, container, false);

        //mainAmountEdit = (TextView) view.findViewById(R.id.payment_main_cash);
        amountEdit = (EditText)view.findViewById(R.id.payment_cash_amount);
        amountEdit.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                String amountString = amountEdit.getText().toString();
                if(amountString.length() > 0) {
                    double amount = Double.parseDouble(amountString);
                    cash = new Cash();
                    cash.setAmount(amount);
                    ((MainActivity)getActivity()).UpdateCash(cash);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        Bundle args = getArguments();

        if(args != null){
            cash = (Cash) args.getSerializable(ARG_CASH);

            if(cash != null){
                amountEdit.setText(String.valueOf(cash.getAmount()));
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
}
