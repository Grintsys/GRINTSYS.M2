package grintsys.com.vanshop.interfaces;

import grintsys.com.vanshop.entities.cart.CartDiscountItem;
import grintsys.com.vanshop.entities.cart.CartProductItem;

public interface CartRecyclerInterface {

    void onProductUpdate(CartProductItem cartProductItem);

    void onProductDelete(CartProductItem cartProductItem);

    void onDiscountDelete(CartDiscountItem cartDiscountItem);

    void onProductSelect(long productId);

}
