package grintsys.com.vanshop.ux.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import grintsys.com.vanshop.BuildConfig;
import grintsys.com.vanshop.CONST;
import grintsys.com.vanshop.R;
import grintsys.com.vanshop.entities.product.ProductSize;
import grintsys.com.vanshop.entities.product.ProductVariant;
import grintsys.com.vanshop.ux.MainActivity;
import grintsys.com.vanshop.ux.fragments.ProductColorFragment.OnListFragmentInteractionListener;
import grintsys.com.vanshop.views.RoundedImageView;
import timber.log.Timber;

public class MyProductRecyclerViewAdapter extends RecyclerView.Adapter<MyProductRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private final List<ProductVariant> mValues;
    private final OnListFragmentInteractionListener mListener;
    private ProductSize mSize;

    public MyProductRecyclerViewAdapter(List<ProductVariant> items, OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        mContext = context;
    }

    public MyProductRecyclerViewAdapter(List<ProductVariant> items, OnListFragmentInteractionListener listener, Context context, ProductSize size) {
        mValues = items;
        mListener = listener;
        mContext = context;
        mSize = size;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_product_color, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mItem = mValues.get(position);
        holder.warehouse.setText(mContext.getString(R.string.Warehouse) + ": " + holder.mItem.getWarehouse());
        holder.colorCode.setText(holder.mItem .getColor().getValue());
        holder.new_quantity.setText(String.valueOf(holder.mItem.getNew_quantity()));
        holder.price.setText(mContext.getString(R.string.Price) + ": "
                + holder.mItem.getCurrency() + " "
                + String.valueOf(holder.mItem.getPrice()));
        holder.stock.setText(mContext.getString(R.string.Stock) + ": "
                + String.valueOf(holder.mItem.getQuantity()));
        holder.is_committed.setText(mContext.getString(R.string.Available) + ": "
                + String.valueOf(holder.mItem.getQuantity() - holder.mItem.getIs_committed()));

        //custom color view circle
        if (holder.mItem.getColor().getCode() != null && (!holder.mItem.getColor().getCode().isEmpty())) {
            final String hexColor = holder.mItem.getColor().getCode();
            GradientDrawable gradDrawable = (GradientDrawable) holder.roundImage.getBackground();
            int resultColor = 0xffffffff;
            try {
                resultColor = Color.parseColor(hexColor);
            } catch (Exception e) {
                Timber.e(e, "CustomSpinnerColors parse color exception");
            }
            gradDrawable.setColor(resultColor);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mValues == null)
            return 0;
        return mValues.size();
    }

    public ProductVariant getItemAt(int position){
        return mValues.get(position);
    }

    public List<ProductVariant> getItems() {
        return mValues;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView colorCode;
        public final TextView price;
        public final TextView stock;
        public final TextView warehouse;
        public final TextView is_committed;
        public final EditText new_quantity;
        public final RoundedImageView roundImage;
        public final ImageView plusImage, minuImage;
        public ProductVariant mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            colorCode = (TextView) view.findViewById(R.id.product_color_code);
            price = (TextView) view.findViewById(R.id.product_color_price);
            is_committed = (TextView) view.findViewById(R.id.product_color_quantity_avalible);
            stock = (TextView) view.findViewById(R.id.product_color_stock);
            warehouse = (TextView) view.findViewById(R.id.product_warehouse);
            new_quantity = (EditText) view.findViewById(R.id.product_color_new_quantity);
            roundImage = (RoundedImageView) view.findViewById(R.id.product_color_picker_image_view);
            plusImage = (ImageView) view.findViewById(R.id.product_color_plus_image) ;
            minuImage = (ImageView) view.findViewById(R.id.product_color_minus_image);

            final Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.shake_rapid);

            minuImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try{
                        int quantity = mItem.getNew_quantity();
//                        if(quantity > 0) {
                            //((MainActivity) mContext).updateQuantity(mItem, quantity);
                            mItem.setNew_quantity(--quantity);
                            new_quantity.setText(String.valueOf(mItem.getNew_quantity()));
                        //  view.startAnimation(animation);
                        //                      }
                    }catch (Exception e){
                        Timber.e(e.getMessage());
                    }
                }
            });

            plusImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try{
                        int quantity = mItem.getNew_quantity();
                        if(quantity <= CONST.MaxQuantityOrder) {
                            //((MainActivity) mContext).updateQuantity(mItem, quantity);

                             //new_quantity.setText(BuildConfig.VERSION_NAME);

                            //NOV 29TH. 2017. Bloquear el ingreso mayor a Disponible.
                            //if ( Integer.valueOf(is_committed.getText().toString().replace("Available: ","")) - quantity + 1 >= 0 )
                            //{
                                mItem.setNew_quantity(++quantity);
                                new_quantity.setText(String.valueOf(mItem.getNew_quantity()));
                                //view.startAnimation(animation);
                            //}

                        }
                    }catch (Exception e){
                        Timber.e(e.getMessage());
                    }
                }
            });

            new_quantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String quantityString = new_quantity.getText().toString();

                    if(!quantityString.isEmpty()) {
                        int quantity = Integer.parseInt(quantityString);
                            if(quantity > 0)

                                //Si hay Disponible
                                //if ( Integer.valueOf(is_committed.getText().toString().replace("Available: ","")) - quantity >= 0 )
                                //{
                                    ((MainActivity) mContext).updateQuantity(mItem, quantity);
                                //}
                        //else
                                //No hay disponible
                        //      {
                        //          new_quantity.setText(is_committed.getText().toString().replace("Available: ",""));
                        //          ((MainActivity) mContext).updateQuantity(mItem, Integer.valueOf(is_committed.getText().toString().replace("Available: ","")));
                        //      }

                    }
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItem.toString() + "'";
        }
    }
}
