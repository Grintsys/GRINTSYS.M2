package grintsys.com.vanshop.ux.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import grintsys.com.vanshop.R;
import grintsys.com.vanshop.entities.product.Product;
import grintsys.com.vanshop.interfaces.CategoryRecyclerInterface;
import grintsys.com.vanshop.views.ResizableImageView;

/**
 * Adapter handling list of product items.
 */
public class ProductsRowRecyclerAdapter extends RecyclerView.Adapter<ProductsRowRecyclerAdapter.ViewHolder> {

    private final Context context;
    private final CategoryRecyclerInterface categoryRecyclerInterface;
    private List<Product> products = new ArrayList<>();
    private LayoutInflater layoutInflater;

    private boolean loadHighRes = false;

    /**
     * Creates an adapter that handles a list of product items.
     *
     * @param context                   activity context.
     * @param categoryRecyclerInterface listener indicating events that occurred.
     */
    public ProductsRowRecyclerAdapter(Context context, CategoryRecyclerInterface categoryRecyclerInterface) {
        this.context = context;
        this.categoryRecyclerInterface = categoryRecyclerInterface;
    }

    private Product getItem(int position) {
        return products.get(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return products.size();
    }

    public void addProducts(List<Product> productList) {
        products.addAll(productList);
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProductsRowRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.list_item_products, parent, false);
        return new ViewHolder(view, categoryRecyclerInterface);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = getItem(position);
        holder.bindContent(product);
        // - replace the contents of the view with that element
        holder.productNameTV.setText(holder.product.getName());
        holder.productSKU.setText(holder.product.getCode());
        holder.productBrand.setText(holder.product.getBrand());
        holder.productSeason.setText(holder.product.getSeason());

        if (loadHighRes && product.getMainImageHighRes() != null) {
            Picasso.with(context).load(product.getMainImageHighRes())
                    .fit().centerInside()
                    .placeholder(R.drawable.placeholder_loading)
                    .error(R.drawable.placeholder_error)
                    .into(holder.productImage);
        } else {
            Picasso.with(context).load(holder.product.getMainImage())
                    .fit().centerInside()
                    .placeholder(R.drawable.placeholder_loading)
                    .error(R.drawable.placeholder_error)
                    .into(holder.productImage);
        }
    }

    public void clear() {
        products.clear();
    }

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productNameTV;
        public TextView productSKU;
        public TextView productBrand;
        public TextView productSeason;
        public ResizableImageView productImage;
        private Product product;

        public ViewHolder(View v, final CategoryRecyclerInterface categoryRecyclerInterface) {
            super(v);
            productNameTV = (TextView) v.findViewById(R.id.product_item_name);
            productSKU = (TextView) v.findViewById(R.id.product_item_sku);
            productBrand = (TextView) v.findViewById(R.id.product_item_brand);
            productSeason = (TextView) v.findViewById(R.id.product_item_season);
            productImage = (ResizableImageView) v.findViewById(R.id.product_item_image);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    categoryRecyclerInterface.onProductSelected(v, product);
                }
            });
        }

        public void bindContent(Product product) {
            this.product = product;
        }
    }
}
