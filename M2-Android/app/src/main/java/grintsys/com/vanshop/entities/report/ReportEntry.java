package grintsys.com.vanshop.entities.report;

/**
 * Created by serpel on 5/15/2017.
 */

public class ReportEntry {
    private float x;
    private float y;
    private String label;
    private int index;

    public ReportEntry(float x, float y, String label, int index) {
        this.x = x;
        this.y = y;
        this.label = label;
        this.index = index;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
