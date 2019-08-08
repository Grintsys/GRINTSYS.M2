package grintsys.com.vanshop.ux.fragments.reports;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import grintsys.com.vanshop.CONST;
import grintsys.com.vanshop.MyApplication;
import grintsys.com.vanshop.R;
import grintsys.com.vanshop.SettingsMy;
import grintsys.com.vanshop.api.EndPoints;
import grintsys.com.vanshop.api.GsonRequest;
import grintsys.com.vanshop.entities.User.User;
import grintsys.com.vanshop.entities.report.ReportEntry;
import grintsys.com.vanshop.entities.report.ReportEntryPieResponse;
import grintsys.com.vanshop.utils.MsgUtils;
import grintsys.com.vanshop.ux.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReportQuotaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReportQuotaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportQuotaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private ProgressBar progressView;
    private TextView invoicedTextView;
    private TextView quotaTextView;
    private TextView quotaAcumTextView;
    private EditText quotaMonth;
    private ImageButton quotaMinusButton;
    private ImageButton quotaPlusButton;
    private PieChart chart;
    private int currentMonth;
    private Calendar c = Calendar.getInstance();

    private OnFragmentInteractionListener mListener;

    public ReportQuotaFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ReportQuotaFragment newInstance() {
        ReportQuotaFragment fragment = new ReportQuotaFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity.setActionBarTitle(getString(R.string.QuotaMonth));
        currentMonth = c.get(Calendar.MONTH) + 1;

        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        progressView = (ProgressBar) view.findViewById(R.id.stats_progress);

        quotaTextView = (TextView) view.findViewById(R.id.stats_quota);
        invoicedTextView = (TextView) view.findViewById(R.id.stats_invoiced);
        quotaMonth = (EditText) view.findViewById(R.id.report_quota_month);

        quotaMonth.setText(String.valueOf(currentMonth));
        quotaMinusButton = (ImageButton) view.findViewById(R.id.report_quota_minus);
        quotaPlusButton = (ImageButton) view.findViewById(R.id.report_quota_plus);

        quotaMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentMonth > 2) {
                    quotaMonth.setText(String.valueOf(--currentMonth));
                    getData();
                }
            }
        });

        quotaPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentMonth < 12) {
                    quotaMonth.setText(String.valueOf(++currentMonth));
                    getData();
                }
            }
        });

        chart = (PieChart) view.findViewById(R.id.stats_chart);
        //chart.getDescription().setText(getString(R.string.QuotaMonth));
        chart.getDescription().setEnabled(false);
        chart.setCenterText(getString(R.string.QuotaMonth));
        chart.setCenterTextSize(22f);

        chart.setDragDecelerationFrictionCoef(0.95f);
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);
        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);
        chart.setRotationAngle(0);
        chart.animateY(1500);

        getData();

        return view;
    }

    private void FillChart(ArrayList<ReportEntry> list) {

        List<PieEntry> entries = new ArrayList<>();

        for(ReportEntry reportEntry:list){
            entries.add(new PieEntry(reportEntry.getY(), reportEntry.getLabel()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setValueTextSize(12f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.notifyDataSetChanged();

        PieData pieData = new PieData(dataSet);
        chart.setData(pieData);
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    public void getData(){

        User user = SettingsMy.getActiveUser();

        if(user != null) {

            String url = String.format(EndPoints.REPORT_QUOTA_PIE, user.getId(), c.get(Calendar.YEAR), Integer.parseInt(quotaMonth.getText().toString()));
            progressView.setVisibility(View.VISIBLE);

            GsonRequest<ReportEntryPieResponse> getProductRequest = new GsonRequest<>(Request.Method.GET, url, null, ReportEntryPieResponse.class,
                    new Response.Listener<ReportEntryPieResponse>() {
                        @Override
                        public void onResponse(@NonNull ReportEntryPieResponse response) {

                            FillChart(response.getEntries());

                            quotaTextView.setText(String.format(Locale.US, "%.2f", response.getQuota()));
                            invoicedTextView.setText(String.format(Locale.US, "%.2f", response.getTotalInvoiced()));

                            progressView.setVisibility(View.GONE);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressView.setVisibility(View.GONE);
                    MsgUtils.logAndShowErrorMessage(getActivity(), error);
                }
            });

            getProductRequest.setRetryPolicy(MyApplication.getSimpleRetryPolice());
            getProductRequest.setShouldCache(false);
            MyApplication.getInstance().addToRequestQueue(getProductRequest, CONST.REPORT_QUOTA_REQUESTS_TAG);
        }
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
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
