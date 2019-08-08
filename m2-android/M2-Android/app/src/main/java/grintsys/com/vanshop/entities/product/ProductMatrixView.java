package grintsys.com.vanshop.entities.product;

import java.util.ArrayList;

/**
 * Created by alienware on 2/10/2017.
 */

public class ProductMatrixView {
    private ProductSize size;
    private ArrayList<ProductVariant> variants;


    public ProductMatrixView(){

    }

    public ProductMatrixView(ProductSize size, ArrayList<ProductVariant> variants) {
        this.size = size;
        this.variants = variants;
    }

    public ProductSize getSize() {
        return size;
    }

    public void setSize(ProductSize size) {
        this.size = size;
    }

    public ArrayList<ProductVariant> getVariants() {
        return variants;
    }

    public void setVariants(ArrayList<ProductVariant> variants) {
        this.variants = variants;
    }
}
