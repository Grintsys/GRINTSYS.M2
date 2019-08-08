package grintsys.com.vanshop.ux.fragments.payment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import java.util.ArrayList;

import grintsys.com.vanshop.R;
import grintsys.com.vanshop.entities.Bank;
import grintsys.com.vanshop.entities.payment.CheckPayment;
import grintsys.com.vanshop.entities.payment.Payment;
import grintsys.com.vanshop.interfaces.BankDialogInterface;
import grintsys.com.vanshop.listeners.OnSingleClickListener;
import grintsys.com.vanshop.utils.RecyclerMarginDecorator;
import grintsys.com.vanshop.ux.MainActivity;
import grintsys.com.vanshop.ux.adapters.ChecksRecyclerAdapter;
import grintsys.com.vanshop.ux.dialogs.PaymentCheckDialog;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PaymentCheckFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PaymentCheckFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class PaymentCheckFragment extends Fragment {

    private static final String ARG_BANK_LIST = "bank-list";
    private static final String ARG_CHECKS_LIST = "check-list";
    private OnFragmentInteractionListener mListener;
    private ChecksRecyclerAdapter checksRecyclerAdapter;
    private GridLayoutManager checksGridLayoutManager;
    private RecyclerView checkRecyclerView;
    private Button addCheckButton;
    private ArrayList<CheckPayment> checks = new ArrayList<>();
    private ArrayList<Bank> banks;
    private PaymentCheckFragment fragment;

    public PaymentCheckFragment() {}

    // TODO: Rename and change types and number of parameters
    public static PaymentCheckFragment newInstance(ArrayList<Bank> banks) {
        PaymentCheckFragment fragment = new PaymentCheckFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_BANK_LIST, banks);
        fragment.setArguments(args);
        fragment.fragment = fragment;
        return fragment;
    }

    public static PaymentCheckFragment newInstance(Payment payment, ArrayList<CheckPayment> checks) {
        PaymentCheckFragment fragment = new PaymentCheckFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CHECKS_LIST, checks);
        fragment.setArguments(args);
        fragment.fragment = fragment;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            banks = (ArrayList<Bank>) getArguments().getSerializable(ARG_BANK_LIST);
            checks = (ArrayList<CheckPayment>) getArguments().getSerializable(ARG_CHECKS_LIST);
        }
    }

    private void openCheckDialog()
    {
        FragmentManager fm = fragment.getFragmentManager();
        PaymentCheckDialog checkDialog = PaymentCheckDialog.newInstance(this, new CheckPayment(), new BankDialogInterface() {
            @Override
            public void onBankSelected(Bank bank) {
                Timber.d("Hola mundo");
            }
        }, banks);
        checkDialog.setRetainInstance(true);
        checkDialog.show(fm, PaymentCheckDialog.class.getSimpleName());
    }

    public void addCheckData(CheckPayment check){
        if(checksRecyclerAdapter != null) {
            checks.add(check);
            checksRecyclerAdapter.addCheck(check);
            checksRecyclerAdapter.updateView();
            ((MainActivity)getActivity()).addCheck(check);
        }
    }

    public void removeCheckData(int position){
        if(checksRecyclerAdapter != null) {
            CheckPayment check = checks.get(position);
            checks.remove(position);
            checksRecyclerAdapter.removeCheck(position);
            checksRecyclerAdapter.updateView();
            ((MainActivity)getActivity()).restCheck(check);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_check, container, false);

        addCheckButton = (Button) view.findViewById(R.id.payment_check_add_button);
        addCheckButton.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                openCheckDialog();
            }
        });

        Bundle bundle = getArguments();

        if(bundle != null){
            banks = (ArrayList<Bank>) bundle.getSerializable(ARG_BANK_LIST);

            if(checksRecyclerAdapter != null && checksRecyclerAdapter.getItemCount() > 0){
                prepareCheckRecycler(view);
            }else{
                prepareRecyclerAdapter();
                prepareCheckRecycler(view);
            }
        }

        return view;
    }

    private void prepareCheckRecycler(View view) {
        checkRecyclerView = (RecyclerView) view.findViewById(R.id.payment_check_recycler);
        checkRecyclerView.addItemDecoration(new RecyclerMarginDecorator(getActivity(), RecyclerMarginDecorator.ORIENTATION.BOTH));
        checkRecyclerView.setItemAnimator(new DefaultItemAnimator());
        checkRecyclerView.setHasFixedSize(true);

        checksGridLayoutManager = new GridLayoutManager(getActivity(), 1);

        checkRecyclerView.setLayoutManager(checksGridLayoutManager);
        checkRecyclerView.setAdapter(checksRecyclerAdapter);
    }

    public void prepareRecyclerAdapter(){
        checksRecyclerAdapter = new ChecksRecyclerAdapter(getActivity(), null);
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
