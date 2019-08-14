package grintsys.com.vanshop.entities.report;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by serpel on 5/15/2017.
 */

public class ReportEntryPieResponse {

    @SerializedName("totalinvoiced")
    private Double totalInvoiced;
    private Double quota;

    private ArrayList<ReportEntry> entries;

    public ReportEntryPieResponse(ArrayList<ReportEntry> entries) {
        this.entries = entries;
    }

    public ArrayList<ReportEntry> getEntries() {
        return entries;
    }

    public Double getTotalInvoiced() {
        return totalInvoiced;
    }

    public void setTotalInvoiced(Double totalInvoiced) {
        this.totalInvoiced = totalInvoiced;
    }

    public Double getQuota() {
        return quota;
    }

    public void setQuota(Double quota) {
        this.quota = quota;
    }

    public void setEntries(ArrayList<ReportEntry> entries) {
        this.entries = entries;
    }
}
