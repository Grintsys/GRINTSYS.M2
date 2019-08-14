package grintsys.com.vanshop.interfaces;

import android.view.View;

import grintsys.com.vanshop.entities.product.Product;

public interface RelatedProductsRecyclerInterface {

    void onRelatedProductSelected(View v, int position, Product product);
}
