package grintsys.com.vanshop.entities.product;

import java.io.Serializable;
import java.util.Arrays;

public class ProductVariant implements Serializable {

    private long id;
    private ProductColor color;
    private ProductSize size;
    private String[] images;
    private String code;
    //SAP Fields
    private int quantity;
    private int is_committed;
    private double price;
    private String currency;
    private String warehouse;
    private int new_quantity;
    //END SAP Fields

    public ProductVariant() {
        new_quantity = 0;
    }

    public ProductVariant(long id, ProductSize size) {
        this.id = id;
        this.size = size;
        this.new_quantity = 0;
    }

    public int getNew_quantity() {
        return new_quantity;
    }

    public void setNew_quantity(int new_quantity) {
        this.new_quantity = new_quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ProductColor getColor() {
        return color;
    }

    public void setColor(ProductColor color) {
        this.color = color;
    }

    public ProductSize getSize() {
        return size;
    }

    public void setSize(ProductSize size) {
        this.size = size;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

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

    public int getIs_committed() {
        return is_committed;
    }

    public void setIs_committed(int is_committed) {
        this.is_committed = is_committed;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductVariant)) return false;

        ProductVariant that = (ProductVariant) o;

        if (getId() != that.getId()) return false;
        if (getColor() != null ? !getColor().equals(that.getColor()) : that.getColor() != null) return false;
        if (getSize() != null ? !getSize().equals(that.getSize()) : that.getSize() != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(getImages(), that.getImages())) return false;
        return !(getCode() != null ? !getCode().equals(that.getCode()) : that.getCode() != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getColor() != null ? getColor().hashCode() : 0);
        result = 31 * result + (getSize() != null ? getSize().hashCode() : 0);
        result = 31 * result + (getImages() != null ? Arrays.hashCode(getImages()) : 0);
        result = 31 * result + (getCode() != null ? getCode().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductVariant{" +
                "id=" + id +
                ", color=" + color +
                ", size=" + size +
                ", images=" + Arrays.toString(images) +
                ", code='" + code + '\'' +
                '}';
    }
}
