package grintsys.com.vanshop.ux.dialogs;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import grintsys.com.vanshop.R;
import grintsys.com.vanshop.entities.Bank;
import grintsys.com.vanshop.entities.payment.CheckPayment;
import grintsys.com.vanshop.interfaces.BankDialogInterface;
import grintsys.com.vanshop.listeners.OnSingleClickListener;
import grintsys.com.vanshop.ux.adapters.BankSpinnerAdapter;
import grintsys.com.vanshop.ux.fragments.payment.PaymentCheckFragment;
import timber.log.Timber;

/**
 * Created by alienware on 4/2/2017.
 */

public class PaymentCheckDialog extends DialogFragment {

    private ProgressBar progressBar;
    private Fragment thisFragment;
    private BankDialogInterface bankDialogInterface;
    private CheckPayment checkPayment;

    private Calendar myCalendar = Calendar.getInstance();
    private TextView checkNumberEdit;
    private TextView dateEdit;
    private TextView amountEdit;
    private Button okBotton;
    private Button cancelBotton;
    private Spinner bankSpinner;
    private ArrayList<Bank> bankList;
    private Bank selectedBank;
    private PaymentCheckFragment fragment;
    private String formatDate = "yyyy/MM/dd";


    public static PaymentCheckDialog newInstance(PaymentCheckFragment fragment, CheckPayment checkPayment, BankDialogInterface bankDialogInterface, ArrayList<Bank> banks) {
        PaymentCheckDialog frag = new PaymentCheckDialog();
        frag.checkPayment = checkPayment;
        frag.bankDialogInterface = bankDialogInterface;
        frag.bankList = banks;
        frag.selectedBank = null;
        frag.fragment = fragment;
        return frag;
    }

    public PaymentCheckDialog() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setWindowAnimations(R.style.dialogFragmentAnimation);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        Timber.d("%s - OnCreateView", this.getClass().getSimpleName());
        View view = inflater.inflate(R.layout.dialog_check, container, false);

        checkNumberEdit = (TextView) view.findViewById(R.id.dialog_check_no);
        dateEdit = (TextView) view.findViewById(R.id.dialog_check_date);
        amountEdit = (TextView) view.findViewById(R.id.dialog_check_amount);
        okBotton = (Button) view.findViewById(R.id.dialog_check_ok);
        cancelBotton = (Button) view.findViewById(R.id.dialog_check_cancel);

        dateEdit.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                new DatePickerDialog(getContext(),
                        R.style.MyDatePicker,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, monthOfYear);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.US);
                                dateEdit.setText(sdf.format(myCalendar.getTime()));
                            }
                        },
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH)-1,
                        myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });


        okBotton.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                Double value = 0.0;

                if(!amountEdit.getText().toString().isEmpty())
                    value = Double.parseDouble(amountEdit.getText().toString());

                CheckPayment check = new CheckPayment(checkNumberEdit.getText().toString(),
                        selectedBank, value, dateEdit.getText().toString());

                fragment.addCheckData(check);
                dismiss();
            }
        });

        cancelBotton.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                dismiss();
            }
        });

        prepareSpinner(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Window window = d.getWindow();
            window.setLayout(width, height);
            window.setWindowAnimations(R.style.alertDialogAnimation);
        }
    }


    public void prepareSpinner(View view){
        bankSpinner = (Spinner) view.findViewById(R.id.dialog_check_banks_spinner);
        final BankSpinnerAdapter bankSpinnerAdapter = new BankSpinnerAdapter(getActivity(), this.bankList);
        bankSpinner.setAdapter(bankSpinnerAdapter);
        bankSpinner.setOnItemSelectedListener(null);
        bankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBank = bankSpinnerAdapter.getItem(i);

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
