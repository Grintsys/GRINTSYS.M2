package grintsys.com.vanshop.ux.fragments.payment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import grintsys.com.vanshop.R;
import grintsys.com.vanshop.entities.Bank;
import grintsys.com.vanshop.entities.client.Client;
import grintsys.com.vanshop.entities.payment.Transfer;
import grintsys.com.vanshop.interfaces.BankDialogInterface;
import grintsys.com.vanshop.ux.MainActivity;
import grintsys.com.vanshop.ux.adapters.BankSpinnerAdapter;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PaymentTransferFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PaymentTransferFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentTransferFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private static final String ARG_CLIENT = "client";
    private static final String ARG_BANKS = "banks";

    // TODO: Rename and change types of parameters
    private TextView clientCodeTextView, clientNameTextView;
    private EditText receiptEdit;
    private Spinner bankSpinner;
    private EditText dateEdit;
    private Spinner paymentType;
    private EditText referenceEdit;
    private EditText amountEdit;

    private Bank selectedBank;
    private BankDialogInterface bankDialogInterface;

    //if we re render this page need this object
    private Transfer transfer;
    private Client client;

    private ArrayList<Bank> banks;

    private OnFragmentInteractionListener mListener;

    public PaymentTransferFragment() {
        // Required empty public constructor
    }

    public static PaymentTransferFragment newInstance(Client client, ArrayList<Bank> banks) {
        PaymentTransferFragment fragment = new PaymentTransferFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_CLIENT, client);
        args.putSerializable(ARG_BANKS, banks);
        fragment.setArguments(args);

        return fragment;
    }


    /*
    public static PaymentTransferFragment newInstance(Transfer transfer, ArrayList<Bank> banks) {
        PaymentTransferFragment fragment = new PaymentTransferFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_TRANSFER, transfer);
        args.putSerializable(ARG_BANK_LIST, banks);
        fragment.setArguments(args);

        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            client = (Client) getArguments().getSerializable(ARG_CLIENT);
            banks = (ArrayList<Bank>) getArguments().getSerializable(ARG_BANKS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_transfer, container, false);

        clientCodeTextView =  view.findViewById(R.id.payment_transfer_client_code);
        clientNameTextView =  view.findViewById(R.id.payment_transfer_client_name);
        receiptEdit = view.findViewById(R.id.payment_transfer_receipt);
        //bankSpinner = view.findViewById(R.id.payment_transfer_banks);
        dateEdit = view.findViewById(R.id.payment_transfer_date);
        paymentType = view.findViewById(R.id.payment_transfer_paymentTypes);
        referenceEdit = view.findViewById(R.id.payment_transfer_reference);
        amountEdit = view.findViewById(R.id.payment_transfer_amount);

        if(client != null){
            clientCodeTextView.setText(client.getCardCode());
            clientNameTextView.setText(client.getName());
        }

        String pay = "";

        pay = ((MainActivity)getActivity()).getPaymentType();
        if ( pay.equals(""))
        {
            ((MainActivity)getActivity()).setPaymentType(paymentType.getSelectedItem().toString());
        }
        else
        {
            String[] pays = getResources().getStringArray(R.array.PaymentTypes);
            paymentType.setSelection(Arrays.asList(pays).indexOf(pay));
        }

        //Add Item change on paymentTypes Dropdown:
        paymentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((MainActivity)getActivity()).setPaymentType(paymentType.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Timber.d("OnNothingSelected - no change");
            }
        });

        referenceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String number = referenceEdit.getText().toString();
                ((MainActivity)getActivity()).setReferenceNumber(number);
            }
        });

        amountEdit.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                String amountString = amountEdit.getText().toString();
                if(amountString.length() > 0) {
                    ((MainActivity)getActivity()).UpdateAmount( Double.parseDouble(amountString));
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        receiptEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String number = receiptEdit.getText().toString();
                ((MainActivity)getActivity()).UpdateReceipt(number);
            }
        });

        amountEdit.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                String amount_string = amountEdit.getText().toString();
                if(!amount_string.isEmpty()) {
                    double amount = Double.parseDouble(amount_string);
                    ((MainActivity)getActivity()).UpdateAmount(amount);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        final Calendar myCalendar = Calendar.getInstance();

        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateEdit.setText(sdf.format(myCalendar.getTime()));
        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(),
                        R.style.MyDatePicker,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year,int monthOfYear, int dayOfMonth) {
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, monthOfYear);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                String myFormat = "yyyy/MM/dd";
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                                dateEdit.setText(sdf.format(myCalendar.getTime()));
                                ((MainActivity)getActivity()).UpdateDate(myCalendar.getTime());
                            }
                        },
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        prepareSpinner(view);

        /*
        Bundle args = getArguments();
        if (args != null) {
            transfer = (Transfer) args.getSerializable(ARG_TRANSFER);
            banks = (ArrayList<Bank>) getArguments().getSerializable(ARG_BANK_LIST);

            if(transfer != null){
                referenceNumberEdit.setText(transfer.getNumber());
                amountEdit.setText(String.valueOf(transfer.getAmount()));
                dateEdit.setText(transfer.getDueDate());
                ((MainActivity)getActivity()).UpdateTransferDate(transfer.getDueDate());


                if(transfer.getBank() != null && bankSpinner != null){
                    int index = 0;
                    for(int i=0; i<this.banks.size() ; i++){
                        if(this.banks.get(i).getGeneralAccount().equals(transfer.getBank().getGeneralAccount()))
                        {
                            index = i;
                            break;
                        }
                    }
                    bankSpinner.setSelection(index);
                }
            }
        }
        */

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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void prepareSpinner(View view){

        if(banks != null && banks.size() > 0  && selectedBank != null)
            ((MainActivity)getActivity()).UpdateBank(this.banks.get(0));

        bankSpinner = view.findViewById(R.id.payment_transfer_banks);
        final BankSpinnerAdapter bankSpinnerAdapter = new BankSpinnerAdapter(getActivity(), this.banks);
        bankSpinner.setAdapter(bankSpinnerAdapter);
        bankSpinner.setOnItemSelectedListener(null);
        bankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBank = bankSpinnerAdapter.getItem(i);
                ((MainActivity)getActivity()).UpdateBank(selectedBank);

                if (bankDialogInterface != null)
                    bankDialogInterface.onBankSelected(selectedBank);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Timber.d("OnNothingSelected - no change");
            }
        });
    }
}
