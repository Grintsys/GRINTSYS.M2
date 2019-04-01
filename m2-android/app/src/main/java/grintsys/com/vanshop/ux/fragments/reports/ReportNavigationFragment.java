package grintsys.com.vanshop.ux.fragments.reports;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import grintsys.com.vanshop.R;

import grintsys.com.vanshop.ux.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReportNavigationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReportNavigationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportNavigationFragment extends Fragment {

    private ViewPager mViewPager;
    private ReportNavigationFragment.SectionsPagerAdapter mSectionsPagerAdapter;
    private OnFragmentInteractionListener mListener;

    public ReportNavigationFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ReportNavigationFragment newInstance() {
        ReportNavigationFragment fragment = new ReportNavigationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_navigation, container, false);

        MainActivity.setActionBarTitle(getString(R.string.Reports));

        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.report_view_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.report_tabs);
        tabLayout.setupWithViewPager(mViewPager);

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

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        private int pageSize = 2;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment result;

            if(position == 0){
                result = ReportQuotaFragment.newInstance();
            }else{
                result = ReportQuotaAccumFragment.newInstance();
            }

            return result;
        }

        @Override
        public int getCount() {
            return pageSize;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            String pageTitle = "";

            if(position == 0){
                pageTitle = getString(R.string.QuotaMonth);
            }else{
                pageTitle = getString(R.string.QuotaMonthAccum);
            }

            return pageTitle;
        }
    }
}
