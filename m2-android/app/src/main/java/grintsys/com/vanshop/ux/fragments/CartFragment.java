package grintsys.com.vanshop.ux.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

import grintsys.com.vanshop.CONST;
import grintsys.com.vanshop.MyApplication;
import grintsys.com.vanshop.R;
import grintsys.com.vanshop.SettingsMy;
import grintsys.com.vanshop.api.EndPoints;
import grintsys.com.vanshop.api.GsonRequest;
import grintsys.com.vanshop.api.JsonRequest;
import grintsys.com.vanshop.entities.User.User;
import grintsys.com.vanshop.entities.cart.Cart;
import grintsys.com.vanshop.entities.cart.CartDiscountItem;
import grintsys.com.vanshop.entities.cart.CartProductItem;
import grintsys.com.vanshop.interfaces.CartRecyclerInterface;
import grintsys.com.vanshop.interfaces.RequestListener;
import grintsys.com.vanshop.listeners.OnSingleClickListener;
import grintsys.com.vanshop.utils.MsgUtils;
import grintsys.com.vanshop.utils.RecyclerDividerDecorator;
import grintsys.com.vanshop.utils.Utils;
import grintsys.com.vanshop.ux.MainActivity;
import grintsys.com.vanshop.ux.adapters.CartRecyclerAdapter;
import grintsys.com.vanshop.ux.dialogs.LoginExpiredDialogFragment;
import grintsys.com.vanshop.ux.dialogs.UpdateCartItemDialogFragment;
import timber.log.Timber;

/**
 * Fragment handles shopping cart.
 */
public class CartFragment extends Fragment {

    private ProgressDialog progressDialog;
    private final static String CART_TYPE = "cart-type";

    private View emptyCart;
    private View cartFooter;
    private int type = 0;

    private RecyclerView cartRecycler;
    private CartRecyclerAdapter cartRecyclerAdapter;

    // Footer views and variables
    private TextView cartItemCountTv;
    private TextView cartTotalPriceTv;

    public static CartFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(CART_TYPE, type);

        CartFragment fragment = new CartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("%s - onCreateView", this.getClass().getSimpleName());
        MainActivity.setActionBarTitle(getString(R.string.Shopping_cart));

        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        progressDialog = Utils.generateProgressDialog(getActivity(), false);
        prepareCartRecycler(view);

        emptyCart = view.findViewById(R.id.cart_empty);
        View emptyCartAction = view.findViewById(R.id.cart_empty_action);
        emptyCartAction.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                // Just open drawer menu.
                Activity activity = getActivity();
                if (activity instanceof MainActivity) {
                    MainActivity mainActivity = (MainActivity) activity;
                    if (mainActivity.drawerFragment != null)
                        mainActivity.drawerFragment.toggleDrawerMenu();
                }
            }
        });

        cartFooter = view.findViewById(R.id.cart_footer);
        cartItemCountTv = (TextView) view.findViewById(R.id.cart_footer_quantity);
        cartTotalPriceTv = (TextView) view.findViewById(R.id.cart_footer_price);

        Button order = (Button) view.findViewById(R.id.cart_order);
        order.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).onOrderCreateSelected();
                }
            }
        });

        Bundle args = getArguments();

        if(args != null){
            type = args.getInt(CART_TYPE);
            getCartContent(type);
        }else {
            getCartContent(0);
        }

        return view;
    }

    private void getCartContent(int type) {
        User user = SettingsMy.getActiveUser();
        if (user != null) {
            //String url = String.format(EndPoints.CART, SettingsMy.getActualNonNullShop(getActivity()).getId());
            String url = String.format(EndPoints.CART, user.getId(), type);

            progressDialog.show();
            GsonRequest<Cart> getCart = new GsonRequest<>(Request.Method.GET, url, null, Cart.class,
                    new Response.Listener<Cart>() {
                        @Override
                        public void onResponse(@NonNull Cart cart) {
                            if (progressDialog != null) progressDialog.cancel();

                            MainActivity.updateCartCountNotification();
                            if (cart.getItems() == null || cart.getItems().size() == 0) {
                                setCartVisibility(false);
                            } else {
                                setCartVisibility(true);
                                cartRecyclerAdapter.refreshItems(cart);

                                cartItemCountTv.setText(getString(R.string.format_quantity_lines, cart.getProductCount()));
                                cartTotalPriceTv.setText(NumberFormat.getNumberInstance(Locale.US).format(cart.getTotalPrice()));
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (progressDialog != null) progressDialog.cancel();
                    setCartVisibility(false);
                    Timber.e("Get request cart error: %s", error.getMessage());
                    MsgUtils.logAndShowErrorMessage(getActivity(), error);
                }
            }, getFragmentManager(), user.getAccessToken());
            getCart.setRetryPolicy(MyApplication.getSimpleRetryPolice());
            getCart.setShouldCache(false);
            MyApplication.getInstance().addToRequestQueue(getCart, CONST.CART_REQUESTS_TAG);
        } else {
            LoginExpiredDialogFragment loginExpiredDialogFragment = new LoginExpiredDialogFragment();
            loginExpiredDialogFragment.show(getFragmentManager(), "loginExpiredDialogFragment");
        }
    }


    private void setCartVisibility(boolean visible) {
        if (visible) {
            if (emptyCart != null) emptyCart.setVisibility(View.GONE);
            if (cartRecycler != null) cartRecycler.setVisibility(View.VISIBLE);
            if (cartFooter != null) cartFooter.setVisibility(View.VISIBLE);
        } else {
            if (cartRecyclerAdapter != null) cartRecyclerAdapter.cleatCart();
            if (emptyCart != null) emptyCart.setVisibility(View.VISIBLE);
            if (cartRecycler != null) cartRecycler.setVisibility(View.GONE);
            if (cartFooter != null) cartFooter.setVisibility(View.GONE);
        }
    }

    private void prepareCartRecycler(View view) {
        this.cartRecycler = (RecyclerView) view.findViewById(R.id.cart_recycler);
        cartRecycler.addItemDecoration(new RecyclerDividerDecorator(getActivity()));
        cartRecycler.setItemAnimator(new DefaultItemAnimator());
        cartRecycler.setHasFixedSize(true);
        cartRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        cartRecyclerAdapter = new CartRecyclerAdapter(getActivity(), new CartRecyclerInterface() {
            @Override
            public void onProductUpdate(CartProductItem cartProductItem) {
                UpdateCartItemDialogFragment updateDialog = UpdateCartItemDialogFragment.newInstance(cartProductItem, new RequestListener() {
                    @Override
                    public void requestSuccess(long newId) {
                        getCartContent(type);
                    }

                    @Override
                    public void requestFailed(VolleyError error) {
                        MsgUtils.logAndShowErrorMessage(getActivity(), error);
                    }
                });

                if (updateDialog != null) {
                    updateDialog.show(getFragmentManager(), UpdateCartItemDialogFragment.class.getSimpleName());
                }
            }

            @Override
            public void onProductDelete(CartProductItem cartProductItem) {
                if (cartProductItem != null){
                    deleteItemFromCart(cartProductItem.getId(), false);
                    //((MainActivity)getActivity()).deleteElement(cartProductItem.getVariant().getCode());
                }
                else
                    Timber.e("Trying delete null cart item.");
            }

            @Override
            public void onDiscountDelete(CartDiscountItem cartDiscountItem) {
                if (cartDiscountItem != null)
                    deleteItemFromCart(cartDiscountItem.getId(), true);
                else
                    Timber.e("Trying delete null cart discount.");
            }

            @Override
            public void onProductSelect(long productId) {
                if (getActivity() instanceof MainActivity)
                    ((MainActivity) getActivity()).onProductSelected(productId);
            }

            private void deleteItemFromCart(final long id, boolean isDiscount) {
                User user = SettingsMy.getActiveUser();
                if (user != null) {
                    String url = String.format(EndPoints.CART_ITEM_DELETE, user.getId(), id, type);
                    progressDialog.show();
                    JsonRequest req = new JsonRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Timber.d("Delete item from cart: %s", response.toString());
                            getCartContent(type);
                            MsgUtils.showToast(getActivity(), MsgUtils.TOAST_TYPE_MESSAGE,
                                    getString(R.string.The_item_has_been_successfully_removed), MsgUtils.ToastLength.LONG);
                            if (progressDialog != null) progressDialog.cancel();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (progressDialog != null) progressDialog.cancel();
                            MsgUtils.logAndShowErrorMessage(getActivity(), error);
                        }
                    }, getFragmentManager(), user.getAccessToken());
                    req.setRetryPolicy(MyApplication.getSimpleRetryPolice());
                    req.setShouldCache(false);
                    MyApplication.getInstance().addToRequestQueue(req, CONST.CART_REQUESTS_TAG);
                } else {
                    LoginExpiredDialogFragment loginExpiredDialogFragment = new LoginExpiredDialogFragment();
                    loginExpiredDialogFragment.show(getFragmentManager(), "loginExpiredDialogFragment");
                }
            }
        });
        cartRecycler.setAdapter(cartRecyclerAdapter);
    }

    @Override
    public void onStop() {
        MyApplication.getInstance().cancelPendingRequests(CONST.CART_REQUESTS_TAG);
        if (progressDialog != null) progressDialog.cancel();
        super.onStop();
    }
}
