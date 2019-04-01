package grintsys.com.vanshop.ux.fragments;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.text.NumberFormat;
import java.util.Locale;

import grintsys.com.vanshop.CONST;
import grintsys.com.vanshop.MyApplication;
import grintsys.com.vanshop.R;
import grintsys.com.vanshop.SettingsMy;
import grintsys.com.vanshop.api.EndPoints;
import grintsys.com.vanshop.api.GsonRequest;
import grintsys.com.vanshop.entities.client.Document;
import grintsys.com.vanshop.entities.client.DocumentListResponse;
import grintsys.com.vanshop.interfaces.DocumentRecyclerInterface;
import grintsys.com.vanshop.listeners.OnSingleClickListener;
import grintsys.com.vanshop.utils.EndlessRecyclerScrollListener;
import grintsys.com.vanshop.utils.MsgUtils;
import grintsys.com.vanshop.utils.RecyclerMarginDecorator;
import grintsys.com.vanshop.ux.MainActivity;
import grintsys.com.vanshop.ux.adapters.DocumentsRecyclerAdapter;
import timber.log.Timber;

import static grintsys.com.vanshop.SettingsMy.getSettings;

/**
 * Created by alienware on 2/2/2017.
 */

public class DocumentsFragment extends Fragment {
    private static final String SEARCH_QUERY = "search_query";
    private static final String CARD_CODE = "cardCode";
    private String searchQuery = null;

    private RecyclerView documentsRecycler;
    private GridLayoutManager documentsRecyclerLayoutManager;
    private DocumentsRecyclerAdapter documentsRecyclerAdapter;
    private EndlessRecyclerScrollListener endlessRecyclerScrollListener;
    private ProgressBar loadMoreProgress;

    private TextView clientCode, clientName, clientCreditLimit, clientBalance, clientInOrders, clientPayCondition;
    private Button documentBegin, documentTransactions;
    private TextView clientAddress;

    public static DocumentsFragment newInstance() {
        Bundle args = new Bundle();
        args.putString(SEARCH_QUERY, null); 

        DocumentsFragment fragment = new DocumentsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static DocumentsFragment newInstance(String cardCode) {
        Bundle args = new Bundle();
        args.putString(CARD_CODE, cardCode);
        args.putString(SEARCH_QUERY, null);

        DocumentsFragment fragment = new DocumentsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static DocumentsFragment newInstance(String searchQuery, String cardCode) {
        Bundle args = new Bundle();
        args.putString(SEARCH_QUERY, searchQuery);
        args.putString(CARD_CODE, cardCode);

        DocumentsFragment fragment = new DocumentsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("%s - onCreateView", this.getClass().getSimpleName());
        View view = inflater.inflate(R.layout.fragment_documents, container, false);

        this.loadMoreProgress = (ProgressBar) view.findViewById(R.id.documents_load_more_progress);

        Bundle startBundle = getArguments();
        if (startBundle != null) {
            searchQuery = startBundle.getString(SEARCH_QUERY, null);
            String card_code = startBundle.getString(CARD_CODE, "");
            boolean isSearch = false;
            if (searchQuery != null && !searchQuery.isEmpty()) {
                isSearch = true;
            }
            //Timber.d("Client type: %s. CategoryId: %d. FilterUrl: %s.", categoryType, categoryId, filterParameters);
            MainActivity.setActionBarTitle("Documents");

            // Opened first time (not form backstack)
            if (documentsRecyclerAdapter == null || documentsRecyclerAdapter.getItemCount() == 0) {
                prepareRecyclerAdapter();
                prepareClientRecycler(view);
                getDocuments(card_code);

                //Analytics.logCategoryView(categoryId, categoryName, isSearch);
            } else {
                prepareClientRecycler(view);
                //prepareSortSpinner();
                Timber.d("Restore previous client state. (Clients already loaded) ");
            }
        } else {
            MsgUtils.showToast(getActivity(), MsgUtils.TOAST_TYPE_INTERNAL_ERROR, getString(R.string.Internal_error), MsgUtils.ToastLength.LONG);
            Timber.e(new RuntimeException(), "Run category fragment without arguments.");
        }

        clientCode = (TextView) view.findViewById(R.id.document_client_code);
        clientName = (TextView) view.findViewById(R.id.document_client_name);
        clientAddress = (TextView) view.findViewById(R.id.document_address);
        clientCreditLimit =  (TextView) view.findViewById(R.id.document_document_credit_limit);
        clientBalance = (TextView) view.findViewById(R.id.document_balance);
        clientInOrders = (TextView) view.findViewById(R.id.document_orders);
        clientPayCondition = (TextView) view.findViewById(R.id.document_document_pay_condition);
        documentTransactions = (Button) view.findViewById(R.id.document_transactions);

        documentTransactions.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                ((MainActivity)getActivity()).onClientTransacionSelected(clientCode.getText().toString());
            }
        });

        documentBegin = (Button) view.findViewById(R.id.document_begin);
        documentBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String customer = clientCode.getText().toString();
                Timber.e("OnSelectedClienCardCode %s", customer);
                SaveClientOnCache();
                MsgUtils.showToast(getActivity(), MsgUtils.TOAST_TYPE_MESSAGE, getString(R.string.Customer) + ": " + customer , MsgUtils.ToastLength.SHORT);
                ((MainActivity)getActivity()).onDrawerBannersSelected();
            }
        });

        return view;
    }

    private void SaveClientOnCache(){
        SharedPreferences prefs = getSettings();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SettingsMy.PREF_CLIENT_CARD_CODE_SELECTED, clientCode.getText().toString());
        editor.putString(SettingsMy.PREF_CLIENT_NAME_SELECTED, clientName.getText().toString());
        editor.commit();
    }

    private void prepareClientRecycler(View view) {
        this.documentsRecycler = (RecyclerView) view.findViewById(R.id.documents_recycler);
        documentsRecycler.addItemDecoration(new RecyclerMarginDecorator(getActivity(), RecyclerMarginDecorator.ORIENTATION.BOTH));
        documentsRecycler.setItemAnimator(new DefaultItemAnimator());
        documentsRecycler.setHasFixedSize(true);
        /*switchLayoutManager.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new ImageView(getContext());
            }
        });*/

        documentsRecyclerLayoutManager = new GridLayoutManager(getActivity(), 1);

        documentsRecycler.setLayoutManager(documentsRecyclerLayoutManager);
        endlessRecyclerScrollListener = new EndlessRecyclerScrollListener(documentsRecyclerLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                Timber.e("Load more");
                /*if (productsMetadata != null && productsMetadata.getLinks() != null && productsMetadata.getLinks().getNext() != null) {
                    getProducts(productsMetadata.getLinks().getNext());
                } else {
                    Timber.d("CustomLoadMoreDataFromApi NO MORE DATA");
                }*/
            }
        };
        documentsRecycler.addOnScrollListener(endlessRecyclerScrollListener);
        documentsRecycler.setAdapter(documentsRecyclerAdapter);
    }

    private void prepareRecyclerAdapter() {
        //On click event
        documentsRecyclerAdapter = new DocumentsRecyclerAdapter(getActivity(), new DocumentRecyclerInterface() {
            @Override
            public void onDocumentRecyclerInterface(View caller, Document document) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    setReenterTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade));
                }
                ((MainActivity) getActivity()).onDocumentSelected(document.getDocumentCode());
            }
        }, false);
    }

    /**
     * Animate change of rows in products recycler LayoutManager.
     *
     * @param layoutSpanCount number of rows to display.
     */
    private void animateRecyclerLayoutChange(final int layoutSpanCount) {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new DecelerateInterpolator());
        fadeOut.setDuration(400);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                documentsRecyclerLayoutManager.setSpanCount(layoutSpanCount);
                documentsRecyclerLayoutManager.requestLayout();
                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setInterpolator(new AccelerateInterpolator());
                fadeIn.setDuration(400);
                documentsRecycler.startAnimation(fadeIn);
            }
        });
        documentsRecycler.startAnimation(fadeOut);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Animation in = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_slowed);
        Animation out = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out);
        //switchLayoutManager.setInAnimation(in);
        //switchLayoutManager.setOutAnimation(out);
    }

    private void getDocuments(String card_code) {
        loadMoreProgress.setVisibility(View.VISIBLE);

        String url = String.format(EndPoints.DOCUMENTS_SINGLE, card_code);

        GsonRequest<DocumentListResponse> getDocumentRequest = new GsonRequest<>(Request.Method.GET, url, null, DocumentListResponse.class,
                new Response.Listener<DocumentListResponse>() {
                    @Override
                    public void onResponse(@NonNull DocumentListResponse response) {
//                        Timber.d("response:" + response.toString());
                        documentsRecyclerAdapter.addDocuments(response.getDocuments());
                        checkEmptyContent();

                        clientCode.setText(response.getClientCardCode());
                        clientName.setText(response.getClientName());
                        clientAddress.setText(response.getAddress());
                        clientCreditLimit.setText(NumberFormat.getNumberInstance(Locale.US).format(response.getCreaditLimit()));
                        clientBalance.setText(NumberFormat.getNumberInstance(Locale.US).format(response.getBalance()));
                        clientInOrders.setText(NumberFormat.getNumberInstance(Locale.US).format(response.getInOrders()));
                        clientPayCondition.setText(response.getPayCondition());

                        loadMoreProgress.setVisibility(View.GONE);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (loadMoreProgress != null) loadMoreProgress.setVisibility(View.GONE);
                checkEmptyContent();
                MsgUtils.logAndShowErrorMessage(getActivity(), error);
            }
        });
        getDocumentRequest.setRetryPolicy(MyApplication.getSimpleRetryPolice());
        getDocumentRequest.setShouldCache(false);
        MyApplication.getInstance().addToRequestQueue(getDocumentRequest, CONST.DOCUMENT_REQUESTS_TAG);
    }

    private void checkEmptyContent() {
        if (documentsRecyclerAdapter != null && documentsRecyclerAdapter.getItemCount() > 0) {
            documentsRecycler.setVisibility(View.VISIBLE);
        } else {
            documentsRecycler.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onStop() {
        if (loadMoreProgress != null) {
            // Hide progress dialog if exist.
            if (loadMoreProgress.getVisibility() == View.VISIBLE && endlessRecyclerScrollListener != null) {
                // Fragment stopped during loading data. Allow new loading on return.
                endlessRecyclerScrollListener.resetLoading();
            }
            loadMoreProgress.setVisibility(View.GONE);
        }
        MyApplication.getInstance().cancelPendingRequests(CONST.DOCUMENT_REQUESTS_TAG);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        if (documentsRecycler != null) documentsRecycler.clearOnScrollListeners();
        super.onDestroyView();
    }
}
