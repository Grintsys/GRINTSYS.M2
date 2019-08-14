package grintsys.com.vanshop.entities.order;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alienware on 3/26/2017.
 */

public class OrderItem {

    private String code;
    private int quantity;
    private Double price;
    private Double discount;

    @SerializedName("warehouse_code")
    private String warehouseCode;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }
}
