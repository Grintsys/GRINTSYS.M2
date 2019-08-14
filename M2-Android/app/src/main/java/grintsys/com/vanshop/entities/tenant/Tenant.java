package grintsys.com.vanshop.entities.tenant;

import com.google.gson.annotations.SerializedName;

public class Tenant {

    private long id;
    private String name;
    private String description;
    private String url;
    private String logo;

    @SerializedName("google_ua")
    private String googleUa;
    private String language;
    private String currency;
    private String taxcode;
    private double isv;

    @SerializedName("flag_icon")
    private String flagIcon;

    public Tenant() {
    }

    public Tenant(String name, String googleUa) {
        this.name = name;
        this.googleUa = googleUa;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getGoogleUa() {
        return googleUa;
    }

    public void setGoogleUa(String googleUa) {
        this.googleUa = googleUa;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getFlagIcon() {
        return flagIcon;
    }

    public void setFlagIcon(String flagIcon) {
        this.flagIcon = flagIcon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tenant tenant = (Tenant) o;

        if (id != tenant.id) return false;
        if (name != null ? !name.equals(tenant.name) : tenant.name != null) return false;
        if (description != null ? !description.equals(tenant.description) : tenant.description != null)
            return false;
        if (url != null ? !url.equals(tenant.url) : tenant.url != null) return false;
        if (logo != null ? !logo.equals(tenant.logo) : tenant.logo != null) return false;
        if (googleUa != null ? !googleUa.equals(tenant.googleUa) : tenant.googleUa != null)
            return false;
        if (language != null ? !language.equals(tenant.language) : tenant.language != null)
            return false;
        if (currency != null ? !currency.equals(tenant.currency) : tenant.currency != null)
            return false;
        return !(flagIcon != null ? !flagIcon.equals(tenant.flagIcon) : tenant.flagIcon != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (logo != null ? logo.hashCode() : 0);
        result = 31 * result + (googleUa != null ? googleUa.hashCode() : 0);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (flagIcon != null ? flagIcon.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tenant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", logo='" + logo + '\'' +
                ", googleUa='" + googleUa + '\'' +
                ", language='" + language + '\'' +
                ", currency='" + currency + '\'' +
                ", flagIcon='" + flagIcon + '\'' +
                '}';
    }
}
