package grintsys.com.vanshop.interfaces;

import android.view.View;

import grintsys.com.vanshop.entities.client.ClientTransactions;

public interface ClientTransactionsRecyclerInterface {

    void onClientTransactionsSelected(View v, ClientTransactions transaction);

}
