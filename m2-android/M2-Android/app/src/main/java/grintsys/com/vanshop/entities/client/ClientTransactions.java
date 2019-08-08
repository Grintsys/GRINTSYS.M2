package grintsys.com.vanshop.entities.client;

/**
 * Created by serpel on 5/9/2017.
 */

public class ClientTransactions {

    private long id;
    private long reference_number;
    private String card_code;
    private String description;
    private String date;
    private Double amount;

    public ClientTransactions(long id, long reference_number, String card_code, String description, String date, Double amount) {
        this.id = id;
        this.reference_number = reference_number;
        this.card_code = card_code;
        this.description = description;
        this.date = date;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getReference_number() {
        return reference_number;
    }

    public void setReference_number(long reference_number) {
        this.reference_number = reference_number;
    }

    public String getCard_code() {
        return card_code;
    }

    public void setCard_code(String card_code) {
        this.card_code = card_code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
