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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

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
import grintsys.com.vanshop.entities.report.ReportEntryLineResponse;
import grintsys.com.vanshop.utils.MsgUtils;
import grintsys.com.vanshop.ux.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReportQuotaAccumFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReportQuotaAccumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportQuotaAccumFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private ProgressBar progressView;
    private TextView invoicedTextView;
    private TextView quotaTextView;
    private TextView quotaAcumTextView;
    private EditText quotaWeek;
    private ImageButton quotaMinusButton;
    private ImageButton quotaPlusButton;
    private LineChart lineChart;
    private int currentWeek;
    private Calendar c = Calendar.getInstance();

    private OnFragmentInteractionListener mListener;

    public ReportQuotaAccumFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ReportQuotaAccumFragment newInstance() {
        ReportQuotaAccumFragment fragment = new ReportQuotaAccumFragment();
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

        MainActivity.setActionBarTitle(getString(R.string.QuotaMonthAccum));
        currentWeek = c.get(Calendar.WEEK_OF_YEAR);

        View view = inflater.inflate(R.layout.fragment_quota_accum, container, false);
        progressView = (ProgressBar) view.findViewById(R.id.quota_accum_progress);

        quotaTextView = (TextView) view.findViewById(R.id.quota_accum);
        invoicedTextView = (TextView) view.findViewById(R.id.quota_accum_invoiced);
        quotaWeek = (EditText) view.findViewById(R.id.report_quota_accum_week);

        quotaWeek.setText(String.valueOf(currentWeek));
        quotaMinusButton = (ImageButton) view.findViewById(R.id.report_quota_accum_minus);
        quotaPlusButton = (ImageButton) view.findViewById(R.id.report_quota_accum_plus);

        quotaMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentWeek > 1) {
                    quotaWeek.setText(String.valueOf(--currentWeek));
                    c.set(Calendar.WEEK_OF_YEAR, currentWeek);
                    getData();
                }
            }
        });

        quotaPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentWeek < 52) {
                    quotaWeek.setText(String.valueOf(++currentWeek));
                    c.set(Calendar.WEEK_OF_YEAR, currentWeek);
                    getData();
                }
            }
        });

        lineChart = (LineChart) view.findViewById(R.id.quota_accum_line_chart);
        lineChart.getDescription().setEnabled(false);

        lineChart.setDragDecelerationFrictionCoef(0.95f);
        lineChart.animateXY(3000, 3000);

        getData();
        return view;
    }

    private void FillChart(ArrayList<ReportEntry> firstList, ArrayList<ReportEntry> secondList) {

        LineData lineData = new LineData();
        List<Entry> line1 = new ArrayList<>();
        List<Entry> line2 = new ArrayList<>();

        if(firstList != null) {
            for (ReportEntry reportEntry : firstList) {
                line1.add(new Entry(reportEntry.getX(), reportEntry.getY(), reportEntry.getLabel()));
            }

            if(firstList.size() > 0) {
                LineDataSet dataSet = new LineDataSet(line1, getString(R.string.DailyQuota));
                dataSet.setValueTextSize(10f);
                dataSet.setColor(Color.GREEN);
                dataSet.notifyDataSetChanged();
                lineData.addDataSet(dataSet);
            }
        }

        if(secondList != null) {
            for (ReportEntry reportEntry : secondList) {
                line2.add(new Entry(reportEntry.getX(), reportEntry.getY(), reportEntry.getLabel()));
            }

            if(secondList.size() > 0){
                LineDataSet dataSet1 = new LineDataSet(line2, getString(R.string.Invoiced));
                dataSet1.setValueTextSize(10f);
                dataSet1.setColor(Color.BLUE);
                dataSet1.notifyDataSetChanged();

                lineData.addDataSet(dataSet1);
            }
        }

        lineChart.setData(lineData);
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }

    public void getData(){

        User user = SettingsMy.getActiveUser();

        if(user != null) {

            String url = String.format(EndPoints.REPORT_QUOTA_ACCUM_LINEAR, user.getId(), c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH));
            progressView.setVisibility(View.VISIBLE);

            GsonRequest<ReportEntryLineResponse> getProductRequest = new GsonRequest<>(Request.Method.GET, url, null, ReportEntryLineResponse.class,
                    new Response.Listener<ReportEntryLineResponse>() {
                        @Override
                        public void onResponse(@NonNull ReportEntryLineResponse response) {


                            FillChart(response.getFirstLine(), response.getSecondLine());

                            quotaTextView.setText(String.format(Locale.US, "%.2f", response.getQuotaAccum()));
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
            MyApplication.getInstance().addToRequestQueue(getProductRequest, CONST.REPORT_QUOTA_ACCUM_REQUESTS_TAG);
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
