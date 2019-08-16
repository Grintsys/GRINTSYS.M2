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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import grintsys.com.vanshop.CONST;
import grintsys.com.vanshop.MyApplication;
import grintsys.com.vanshop.R;
import grintsys.com.vanshop.SettingsMy;
import grintsys.com.vanshop.api.EndPoints;
import grintsys.com.vanshop.api.GsonRequest;
import grintsys.com.vanshop.entities.Bank;
import grintsys.com.vanshop.entities.BankListResult;
import grintsys.com.vanshop.entities.User.User;
import grintsys.com.vanshop.entities.client.Client;
import grintsys.com.vanshop.entities.client.DocumentListResult;
import grintsys.com.vanshop.entities.payment.Transfer;
import grintsys.com.vanshop.entities.tenant.Tenant;
import grintsys.com.vanshop.interfaces.BankDialogInterface;
import grintsys.com.vanshop.utils.MsgUtils;
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
    private EditText receiptEdit, commentEdit;
    private Spinner bankSpinner;
    private EditText dateEdit;
    private Spinner paymentType;
    private EditText referenceEdit;
    private EditText amountEdit;
    private ProgressBar progressView;

    private Bank selectedBank;
    private BankDialogInterface bankDialogInterface;

    //if we re render this page need this object
    private Transfer transfer;
    private Client client;

    //private ArrayList<Bank> banks;

    private OnFragmentInteractionListener mListener;
    private BankSpinnerAdapter bankSpinnerAdapter;

    public PaymentTransferFragment() {
        // Required empty public constructor
    }

    public static PaymentTransferFragment newInstance(Client client) {
        PaymentTransferFragment fragment = new PaymentTransferFragment();

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
            loadBanks();
        }
    }

    private void setContentVisible(CONST.VISIBLE visible) {
        if (progressView != null) {
            switch (visible) {
                case PROGRESS:
                    progressView.setVisibility(View.VISIBLE);
                    break;
                case CONTENT:
                    progressView.setVisibility(View.GONE);
                default: // Content
                    progressView.setVisibility(View.GONE);
            }
        } else {
            Timber.e(new RuntimeException(), "Setting content visibility with null views.");
        }
    }

    private void loadBanks(){
        User user = SettingsMy.getActiveUser();
        Tenant tenant = SettingsMy.getActualTenant();
        if(tenant == null || user == null)
            return;

        //setContentVisible(CONST.VISIBLE.PROGRESS);
        String url = String.format(EndPoints.BANKS, tenant.getId());

        GsonRequest<BankListResult> req = new GsonRequest<>(Request.Method.GET, url, null, BankListResult.class,
                new Response.Listener<BankListResult>() {
                    @Override
                    public void onResponse(BankListResult response) {
                        Timber.d("Esto devolvio %s", response);
                        bankSpinnerAdapter.addBanks(response.result.getBanks());
                        bankSpinnerAdapter.notifyDataSetChanged();

                        //setContentVisible(CONST.VISIBLE.CONTENT);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //if (progressDialog != null) progressDialog.cancel();
                MsgUtils.logAndShowErrorMessage(getActivity(), error);
            }
        }, getFragmentManager(), user.getAccessToken());
        req.setRetryPolicy(MyApplication.getSimpleRetryPolice());
        req.setShouldCache(true);
        MyApplication.getInstance().addToRequestQueue(req, CONST.DOCUMENT_REQUESTS_TAG);
    }

    @Override
    public void onStop() {
        super.onStop();
        //setContentVisible(CONST.VISIBLE.CONTENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_transfer, container, false);

        clientCodeTextView =  view.findViewById(R.id.payment_transfer_client_code);
        clientNameTextView =  view.findViewById(R.id.payment_transfer_client_name);
        commentEdit = view.findViewById(R.id.payment_transfer_comment);
        receiptEdit = view.findViewById(R.id.payment_transfer_receipt);
        dateEdit = view.findViewById(R.id.payment_transfer_date);
        paymentType = view.findViewById(R.id.payment_transfer_paymentTypes);
        referenceEdit = view.findViewById(R.id.payment_transfer_reference);
        amountEdit = view.findViewById(R.id.payment_transfer_amount);
        progressView = view.findViewById(R.id.payment_progress);

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

        commentEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String txt = commentEdit.getText().toString();
                ((MainActivity)getActivity()).UpdateComment(txt);
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

        prepareBankSpinner(view);

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

    public void prepareBankSpinner(View view){

        bankSpinner = view.findViewById(R.id.payment_transfer_banks);
        bankSpinnerAdapter = new BankSpinnerAdapter(getActivity(), new ArrayList<Bank>());
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
