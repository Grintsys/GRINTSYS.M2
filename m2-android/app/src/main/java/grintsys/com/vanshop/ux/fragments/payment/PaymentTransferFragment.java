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

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import grintsys.com.vanshop.R;
import grintsys.com.vanshop.entities.Bank;
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

    private static final String ARG_BANK_LIST = "bank-list";
    private static final String ARG_TRANSFER = "transfer";

    // TODO: Rename and change types of parameters
    private EditText amountEdit;
    private EditText referenceNumberEdit;

    private ArrayList<Bank> banks;
    private Spinner bankSpinner;
    private Spinner paymentType;
    private Spinner paymentReason;
    private Bank selectedBank;
    private BankDialogInterface bankDialogInterface;
    private Transfer transfer;

    private OnFragmentInteractionListener mListener;

    public PaymentTransferFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PaymentTransferFragment newInstance(ArrayList<Bank> banks) {
        PaymentTransferFragment fragment = new PaymentTransferFragment();
        Bundle args = new Bundle();

        args.putSerializable(ARG_BANK_LIST, banks);
        fragment.bankDialogInterface = new BankDialogInterface() {
            @Override
            public void onBankSelected(Bank bank) {

            }
        };
        fragment.setArguments(args);

        return fragment;
    }

    public static PaymentTransferFragment newInstance(Transfer transfer, ArrayList<Bank> banks) {
        PaymentTransferFragment fragment = new PaymentTransferFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_TRANSFER, transfer);
        args.putSerializable(ARG_BANK_LIST, banks);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            banks = (ArrayList<Bank>) getArguments().getSerializable(ARG_BANK_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_transfer, container, false);

        String pay ="";
        String Reason="";
        //PaymentTypes, added DEM August 15th-2017.
        paymentType = (Spinner) view.findViewById(R.id.payment_transfer_paymentTypes);
        paymentReason = (Spinner) view.findViewById(R.id.payment_transfer_paymentreason);
        //paymentType.add
        //transfer.setPaymentType(paymentType.getSelectedItem().toString());
        //((MainActivity)getActivity()).setPaymentType(paymentType.getSelectedItem().toString());
        //((MainActivity)getActivity()).getPaymentType(paymentType.set().toString());
       // paymentType.setSele
        //La primera vez deberia de estar vacia.
        pay = ((MainActivity)getActivity()).getPaymentType();
        Reason = ((MainActivity)getActivity()).getPaymentReason();
        if ( pay == "")
        {
            ((MainActivity)getActivity()).setPaymentType(paymentType.getSelectedItem().toString());
        }
        else
        {
            String[] pays = getResources().getStringArray(R.array.PaymentTypes);
            paymentType.setSelection(Arrays.asList(pays).indexOf(pay));
        }

        if (Reason == "")
        {
            ((MainActivity)getActivity()).setPaymentReason(paymentReason.getSelectedItem().toString());
        }
        else
        {
            String[] reasons = getResources().getStringArray(R.array.PaymentReason);
            paymentReason.setSelection(Arrays.asList(reasons).indexOf(Reason));
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

        paymentReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((MainActivity)getActivity()).setPaymentReason(paymentReason.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            Timber.d("OnNothingSelected - no change");
            }
        });

        referenceNumberEdit = (EditText) view.findViewById(R.id.payment_transfer_reference_number);
        referenceNumberEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String number = referenceNumberEdit.getText().toString();
                ((MainActivity)getActivity()).UpdateTransferReferenceNumber(number);
            }
        });

        amountEdit = (EditText) view.findViewById(R.id.payment_transfer_amount);
        amountEdit.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                String amount_string = amountEdit.getText().toString();
                if(!amount_string.isEmpty()) {
                    double amount = Double.parseDouble(amount_string);
                    ((MainActivity)getActivity()).UpdateTransferAmount(amount);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        final EditText dateEdit = (EditText) view.findViewById(R.id.payment_transfer_date);
        final Calendar myCalendar = Calendar.getInstance();

        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateEdit.setText(sdf.format(myCalendar.getTime()));
        //((MainActivity)getActivity()).UpdateTransferDate(dateEdit.getText().toString());

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
                                ((MainActivity)getActivity()).UpdateTransferDate(sdf.format(myCalendar.getTime()));
                            }
                        },
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        prepareSpinner(view);

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
        if(this.banks.size() > 0 && selectedBank != null)
            ((MainActivity)getActivity()).UpdateTransferBank(this.banks.get(0));

        bankSpinner = (Spinner) view.findViewById(R.id.payment_transfer_banks);
        final BankSpinnerAdapter bankSpinnerAdapter = new BankSpinnerAdapter(getActivity(), this.banks);
        bankSpinner.setAdapter(bankSpinnerAdapter);
        bankSpinner.setOnItemSelectedListener(null);
        bankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBank = bankSpinnerAdapter.getItem(i);
                ((MainActivity)getActivity()).UpdateTransferBank(selectedBank);

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
