package grintsys.com.vanshop.entities.payment;

public class AddPaymentResponse {
    private int tenantId;
    private int docEntry;
    private Double payedAmount;
    private String lastMessage;
    private int status;
    private String comment;
    private String referenceNumber;
    private String creationTime;
    private int userId;
    private int bankId;
    private int type;
    private String payedDate;
    private String cardCode;
    private int id;

    public int getPaymentId() {
        return id;
    }
}
