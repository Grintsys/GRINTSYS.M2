package grintsys.com.vanshop.entities.client;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alienware on 4/7/2017.
 */

public class DocumentItem {

    private String code;
    private String description;
    private int quantity;
    private Double price;
    @SerializedName("discount_percent")
    private Double discountPercent;
    private Double total;

    public DocumentItem(){}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
