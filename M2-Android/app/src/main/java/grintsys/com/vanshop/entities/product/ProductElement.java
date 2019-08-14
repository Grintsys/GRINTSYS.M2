package grintsys.com.vanshop.entities.product;

/**
 * Created by alienware on 3/31/2017.
 */

public class ProductElement {

    private ProductSize size;
    private ProductVariant variant;

    public ProductElement() {}

    public ProductElement(ProductSize size, ProductVariant variant) {
        this.size = size;
        this.variant = variant;
    }

    public ProductSize getSize() {
        return size;
    }

    public void setSize(ProductSize size) {
        this.size = size;
    }

    public ProductVariant getVariant() {
        return variant;
    }

    public void setVariant(ProductVariant variant) {
        this.variant = variant;
    }
}
