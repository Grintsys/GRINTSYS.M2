package grintsys.com.vanshop.entities.report;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by serpel on 5/15/2017.
 */

public class ReportEntryLineResponse {

    @SerializedName("totalinvoiced")
    private Double totalInvoiced;
    @SerializedName("quotaaccum")
    private Double quotaAccum;
    @SerializedName("firstlist")
    private ArrayList<ReportEntry> firstLine;
    @SerializedName("secondlist")
    private ArrayList<ReportEntry> secondLine;

    public ReportEntryLineResponse(ArrayList<ReportEntry> firstLine, ArrayList<ReportEntry> secondLine) {
        this.firstLine = firstLine;
        this.secondLine = secondLine;
    }

    public ArrayList<ReportEntry> getFirstLine() {
        return firstLine;
    }

    public Double getTotalInvoiced() {
        return totalInvoiced;
    }

    public void setTotalInvoiced(Double totalInvoiced) {
        this.totalInvoiced = totalInvoiced;
    }

    public Double getQuotaAccum() {
        return quotaAccum;
    }

    public void setQuotaAccum(Double quotaAccum) {
        this.quotaAccum = quotaAccum;
    }

    public void setFirstLine(ArrayList<ReportEntry> firstLine) {
        this.firstLine = firstLine;
    }

    public ArrayList<ReportEntry> getSecondLine() {
        return secondLine;
    }

    public void setSecondLine(ArrayList<ReportEntry> secondLine) {
        this.secondLine = secondLine;
    }
}
