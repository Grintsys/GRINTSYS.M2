package grintsys.com.vanshop.interfaces;

import android.view.View;

import grintsys.com.vanshop.entities.drawerMenu.DrawerItemCategory;
import grintsys.com.vanshop.entities.drawerMenu.DrawerItemPage;

public interface DrawerRecyclerInterface {

    void onCategorySelected(View v, DrawerItemCategory drawerItemCategory);

    void onPageSelected(View v, DrawerItemPage drawerItemPage);

    void onHeaderSelected();
}
