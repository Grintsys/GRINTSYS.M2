package grintsys.com.vanshop.interfaces;

public interface UpdateCartItemListener {

    void updateProductInCart(long productCartId, long newVariantId, int newQuantity);

}

