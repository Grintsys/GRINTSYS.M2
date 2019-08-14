package grintsys.com.vanshop.entities.invoice;

import com.google.gson.annotations.SerializedName;

/**
 * Created by serpel on 6/2/2017.
 */

public class Invoice {
    private int id;
    @SerializedName("doc_num")
    private String docNum;
    @SerializedName("card_code")
    private String cardCode;
    @SerializedName("card_name")
    private String cardName;
    private Double total;
    @SerializedName("doc_date")
    private String docDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocNum() {
        return docNum;
    }

    public void setDocNum(String docNum) {
        this.docNum = docNum;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getDocDate() {
        return docDate;
    }

    public void setDocDate(String docDate) {
        this.docDate = docDate;
    }
}
