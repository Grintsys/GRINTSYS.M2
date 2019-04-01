package grintsys.com.vanshop.entities.order;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import grintsys.com.vanshop.entities.client.Client;

public class Order {

    private long id;

    @SerializedName("remote_id")
    private String remoteId;

    @SerializedName("date_created")
    private String dateCreated;
    private String status;

    @SerializedName("item_count")
    private int itemCount;
    private double subtotal;
    private double discount;
    private double IVA;
    private double total;

    @SerializedName("total_formatted")
    private String totalFormatted;

    @SerializedName("items")
    private List<OrderItem> products;
    private Client client;
    private String seller;
    private String comment;
    @SerializedName("delivery_date")
    private String deliveryDate;

    @SerializedName("sales_person_code")
    private int salesPersonCode;
    private int series;

    @SerializedName("card_code")
    private String cardCode;

    public Order() {
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRemoteId() {
        return remoteId;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }

    public int getSalesPersonCode() {
        return salesPersonCode;
    }

    public void setSalesPersonCode(int salesPersonCode) {
        this.salesPersonCode = salesPersonCode;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalFormatted() {
        return totalFormatted;
    }

    public void setTotalFormatted(String totalFormatted) {
        this.totalFormatted = totalFormatted;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getIVA() {
        return IVA;
    }

    public void setIVA(double IVA) {
        this.IVA = IVA;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<OrderItem> getProducts() {
        return products;
    }

    public void setProducts(List<OrderItem> products) {
        this.products = products;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (id != order.id) return false;
        if (total != order.total) return false;
        if (remoteId != null ? !remoteId.equals(order.remoteId) : order.remoteId != null) return false;
        if (dateCreated != null ? !dateCreated.equals(order.dateCreated) : order.dateCreated != null) return false;
        if (status != null ? !status.equals(order.status) : order.status != null) return false;
        if (totalFormatted != null ? !totalFormatted.equals(order.totalFormatted) : order.totalFormatted != null) return false;
        if (products != null ? !products.equals(order.products) : order.products != null) return false;
        return !(comment != null ? !comment.equals(order.comment) : order.comment != null);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (remoteId != null ? remoteId.hashCode() : 0);
        result = 31 * result + (dateCreated != null ? dateCreated.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (totalFormatted != null ? totalFormatted.hashCode() : 0);
        result = 31 * result + (products != null ? products.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", remoteId='" + remoteId + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", status='" + status + '\'' +
                ", total=" + total +
                ", totalFormatted='" + totalFormatted + '\'' +
                ", products=" + products +
                ", note='" + comment + '\'' +
                '}';
    }
}
