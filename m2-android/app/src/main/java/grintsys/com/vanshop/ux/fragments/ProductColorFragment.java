package grintsys.com.vanshop.ux.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import grintsys.com.vanshop.R;
import grintsys.com.vanshop.entities.product.ProductSize;
import grintsys.com.vanshop.entities.product.ProductVariant;
import grintsys.com.vanshop.ux.adapters.MyProductRecyclerViewAdapter;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ProductColorFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_PRODUCT_SIZE = "product-size";
    private static final String ARG_PRODUCT_VARIANTS = "product-variants";

    private static final String ARG_SIZE = "fragment-size";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private ProductSize size;
    private ArrayList<ProductVariant> variants;
    private OnListFragmentInteractionListener mListener;
    private MyProductRecyclerViewAdapter myProductRecyclerViewAdapter;

    public String getName(){
        return size.getValue();
    }
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProductColorFragment() {
    }

    public ArrayList<ProductVariant> getVariants() {
        return variants;
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ProductColorFragment newInstance(int columnCount) {
        ProductColorFragment fragment = new ProductColorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    public static ProductColorFragment newInstance(int columnCount, String size) {
        ProductColorFragment fragment = new ProductColorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(ARG_SIZE, size);
        fragment.setArguments(args);
        return fragment;
    }

    public static ProductColorFragment newInstance(ProductSize size, ArrayList<ProductVariant> variants) {
        ProductColorFragment fragment = new ProductColorFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT_SIZE, size);
        args.putSerializable(ARG_PRODUCT_VARIANTS, variants);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            size = (ProductSize) getArguments().getSerializable(ARG_PRODUCT_SIZE);
            variants = (ArrayList<ProductVariant>) getArguments().getSerializable(ARG_PRODUCT_VARIANTS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_color_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            myProductRecyclerViewAdapter = new MyProductRecyclerViewAdapter(variants, mListener, getContext());
            recyclerView.setAdapter(myProductRecyclerViewAdapter);
        }
        return view;
    }

    public MyProductRecyclerViewAdapter GetProductAdapter(){
        return myProductRecyclerViewAdapter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } /*else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }*/
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ProductVariant item);
    }
}
