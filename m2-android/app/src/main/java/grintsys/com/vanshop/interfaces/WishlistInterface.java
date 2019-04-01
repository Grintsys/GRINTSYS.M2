package grintsys.com.vanshop.interfaces;

import android.view.View;

import grintsys.com.vanshop.entities.wishlist.WishlistItem;

public interface WishlistInterface {

    void onWishlistItemSelected(View view, WishlistItem wishlistItem);

    void onRemoveItemFromWishList(View caller, WishlistItem wishlistItem, int adapterPosition);
}
