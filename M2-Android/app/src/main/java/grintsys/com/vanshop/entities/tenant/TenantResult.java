package grintsys.com.vanshop.entities.tenant;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TenantResult {

    public int totalCount;

    @SerializedName("items")
    List<Tenant> tenantList;

    public TenantResult() {
    }

    public List<Tenant> getTenantList() {
        return tenantList;
    }

    public void setTenantList(List<Tenant> tenantList) {
        this.tenantList = tenantList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TenantResult)) return false;

        TenantResult that = (TenantResult) o;

        return !(getTenantList() != null ? !getTenantList().equals(that.getTenantList()) : that.getTenantList() != null);

    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (getTenantList() != null ? getTenantList().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TenantResult{" +
                ", tenantList=" + tenantList +
                '}';
    }
}
