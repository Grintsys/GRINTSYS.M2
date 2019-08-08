package grintsys.com.vanshop.entities.product;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Product {

    private long id;

    @SerializedName("remote_id")
    private long remoteId;
    private String url;
    private String name;
    //private double price;

    //@SerializedName("price_formatted")
    //private String priceFormatted;

    //@SerializedName("discount_price")
    //private double discountPrice;

    ///@SerializedName("discount_price_formatted")
    ///private String discountPriceFormatted;
    private long category;
    private String brand;
    private String season;
    private String code;
    private String description;

    @SerializedName("main_image")
    private String mainImage;

    @SerializedName("main_image_high_res")
    private String mainImageHighRes;
    private List<ProductVariant> variants;
    private List<Product> related;

    public Product() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(long remoteId) {
        this.remoteId = remoteId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCategory() {
        return category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public void setCategory(long category) {
        this.category = category;
    }

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

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getMainImageHighRes() {
        return mainImageHighRes;
    }

    public void setMainImageHighRes(String mainImageHighRes) {
        this.mainImageHighRes = mainImageHighRes;
    }

    public List<ProductVariant> getVariants() {
        return variants;
    }

    public ArrayList<ProductVariant> getVariantsBySize(ProductSize size){

        ArrayList<ProductVariant> items = new ArrayList<>();

        for(ProductVariant variant : this.variants){
            if(variant.getSize().getValue().equals(size.getValue())){
                items.add(variant);
            }
        }

        return items;
    }

    public ArrayList<ProductVariant> getVariantsBySizeAndWarehouse(ProductSize size, String warehouse){

        ArrayList<ProductVariant> items = new ArrayList<>();

        for(ProductVariant variant : this.variants){
            if(variant.getSize().getValue().equals(size.getValue()) && variant.getWarehouse().equals(warehouse)){
                items.add(variant);
            }
        }

        return items;
    }

    public void setVariants(List<ProductVariant> variants) {
        this.variants = variants;
    }

    public List<Product> getRelated() {
        return related;
    }

    public void setRelated(List<Product> related) {
        this.related = related;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (id != product.id) return false;
        if (remoteId != product.remoteId) return false;
        if (category != product.category) return false;
        if (url != null ? !url.equals(product.url) : product.url != null) return false;
        if (name != null ? !name.equals(product.name) : product.name != null) return false;
        if (code != null ? !code.equals(product.code) : product.code != null) return false;
        if (description != null ? !description.equals(product.description) : product.description != null)
            return false;
        if (mainImage != null ? !mainImage.equals(product.mainImage) : product.mainImage != null)
            return false;
        if (mainImageHighRes != null ? !mainImageHighRes.equals(product.mainImageHighRes) : product.mainImageHighRes != null)
            return false;
        if (variants != null ? !variants.equals(product.variants) : product.variants != null)
            return false;
        return !(related != null ? !related.equals(product.related) : product.related != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (remoteId ^ (remoteId >>> 32));
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (int) (category ^ (category >>> 32));
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (mainImage != null ? mainImage.hashCode() : 0);
        result = 31 * result + (mainImageHighRes != null ? mainImageHighRes.hashCode() : 0);
        result = 31 * result + (variants != null ? variants.hashCode() : 0);
        result = 31 * result + (related != null ? related.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", remoteId=" + remoteId +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", mainImage='" + mainImage + '\'' +
                ", mainImageHighRes='" + mainImageHighRes + '\'' +
                ", variants=" + variants +
                ", related=" + related +
                '}';
    }
}

