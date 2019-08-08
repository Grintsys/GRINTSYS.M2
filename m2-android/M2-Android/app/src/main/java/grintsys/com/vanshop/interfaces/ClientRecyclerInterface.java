package grintsys.com.vanshop.interfaces;

import android.view.View;

import grintsys.com.vanshop.entities.client.Client;

/**
 * Created by alienware on 2/1/2017.
 */

public interface ClientRecyclerInterface {
    void onClientSelected(View view, Client client);
}
