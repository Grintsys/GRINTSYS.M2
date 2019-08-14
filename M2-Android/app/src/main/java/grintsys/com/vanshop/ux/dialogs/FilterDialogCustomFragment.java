package grintsys.com.vanshop.ux.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import java.util.HashMap;
import java.util.List;

import grintsys.com.vanshop.R;
import grintsys.com.vanshop.entities.filtr.DeserializerFilters;
import grintsys.com.vanshop.entities.filtr.FilterType;
import grintsys.com.vanshop.entities.filtr.FilterTypeColor;
import grintsys.com.vanshop.entities.filtr.FilterTypeRange;
import grintsys.com.vanshop.entities.filtr.FilterTypeSelect;
import grintsys.com.vanshop.entities.filtr.Filters;
import grintsys.com.vanshop.interfaces.FilterDialogInterface;
import grintsys.com.vanshop.utils.RecyclerMarginDecorator;
import grintsys.com.vanshop.ux.adapters.FilterRecyclerAdapter;
import timber.log.Timber;

public class FilterDialogCustomFragment extends DialogFragment {

    //private Filters filterData;
    private FilterDialogInterface filterDialogInterface;
    private List<String> filterLabel;
    private List<String> filterValue;

    public static FilterDialogCustomFragment newInstance(List<String> filterLabel, List<String> filterValue, FilterDialogInterface filterDialogInterface) {
        FilterDialogCustomFragment filterDialogFragment = new FilterDialogCustomFragment();

        if (filterLabel == null || filterValue == null || filterDialogInterface == null) {
            Timber.e(new RuntimeException(), "Created filterDialog with null parameters.");
            return null;
        }
        filterDialogFragment.filterLabel = filterLabel;
        filterDialogFragment.filterValue = filterValue;
        filterDialogFragment.filterDialogInterface = filterDialogInterface;
        return filterDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialogFullscreen);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("%s - OnCreateView", this.getClass().getSimpleName());
        View view = inflater.inflate(R.layout.dialog_filters, container, false);

        prepareFilterRecycler(view);

        Button btnApply = (Button) view.findViewById(R.id.filter_btn_apply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filterUrl = buildFilterUrl();
                filterDialogInterface.onFilterSelected(filterUrl);
                dismiss();
            }
        });

        Button btnCancel = (Button) view.findViewById(R.id.filter_btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear all selected values
                filterDialogInterface.onFilterCancelled();
                dismiss();
            }
        });
        return view;
    }

    private void prepareFilterRecycler(View view) {
        RecyclerView filterRecycler = (RecyclerView) view.findViewById(R.id.filter_recycler);
        filterRecycler.addItemDecoration(new RecyclerMarginDecorator(getActivity(), RecyclerMarginDecorator.ORIENTATION.VERTICAL));
        filterRecycler.setItemAnimator(new DefaultItemAnimator());
        filterRecycler.setHasFixedSize(true);
        filterRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        //FilterRecyclerAdapter filterRecyclerAdapter = new FilterRecyclerAdapter(getActivity(), filterData);
        //filterRecycler.setAdapter(filterRecyclerAdapter);
    }

    private String buildFilterUrl() {
        String filterUrl = "";

        for(int i=0; i<filterLabel.size(); i++){
            String label = filterLabel.get(i);
            String value = filterValue.get(i);

            filterUrl += "&"+label+"="+value;
        }

        Timber.d("BuildFilterUrl - %s", filterUrl);
        return filterUrl;
    }
}
