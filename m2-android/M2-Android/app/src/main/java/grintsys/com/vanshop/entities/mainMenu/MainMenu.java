package grintsys.com.vanshop.entities.mainMenu;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alienware on 2/14/2017.
 */

public class MainMenu {

    @SerializedName("products_count")
    private int productsCount;
    @SerializedName("clients_count")
    private int clientsCount;
    @SerializedName("reports_count")
    private int reportsCount;

    public MainMenu(){}

    public int getProductsCount() {
        return productsCount;
    }

    public void setProductsCount(int productsCount) {
        this.productsCount = productsCount;
    }

    public int getClientsCount() {
        return clientsCount;
    }

    public void setClientsCount(int clientsCount) {
        this.clientsCount = clientsCount;
    }

    public int getReportsCount() {
        return reportsCount;
    }

    public void setReportsCount(int reportsCount) {
        this.reportsCount = reportsCount;
    }
}
