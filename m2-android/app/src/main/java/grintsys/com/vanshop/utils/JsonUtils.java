package grintsys.com.vanshop.utils;

import org.json.JSONException;
import org.json.JSONObject;

import grintsys.com.vanshop.entities.order.Order;
import timber.log.Timber;

public class JsonUtils {

    // Server specific JSON tags
    public static final String TAG_ID = "id";
    public static final String TAG_FB_ID = "fb_id";
    public static final String TAG_FB_ACCESS_TOKEN = "fb_access_token";
    public static final String TAG_PRODUCT_VARIANT_ID = "product_variant_id";
    public static final String TAG_IS_IN_WISHLIST = "is_in_wishlist";
    public static final String TAG_WISHLIST_PRODUCT_ID = "wishlist_product_id";
    public static final String TAG_QUANTITY = "quantity";
    public static final String TAG_CODE = "code";
    public static final String TAG_PRODUCT_COUNT = "product_count";

    // Server specific JSON tags - user oriented
    public static final String TAG_EMAIL = "email";
    public static final String TAG_PASSWORD = "password";
    public static final String TAG_OLD_PASSWORD = "old_password";
    public static final String TAG_NEW_PASSWORD = "new_password";
    public static final String TAG_NAME = "name";
    public static final String TAG_STREET = "street";
    public static final String TAG_HOUSE_NUMBER = "house_number";
    public static final String TAG_CITY = "city";
    public static final String TAG_REGION = "region";
    public static final String TAG_ZIP = "zip";
    public static final String TAG_PHONE = "phone";
    public static final String TAG_GENDER = "gender";
    public static final String TAG_BLUETOOTH = "bluetooth";

    // ORDERS
    public static final String TAG_DATE_CREATED = "date_created";
    public static final String TAG_STATUS = "status";
    public static final String TAG_TOTAL = "total";
    public static final String TAG_SHIPPING_TYPE = "shipping_type";
    public static final String TAG_PAYMENT_TYPE = "payment_type";
    public static final String TAG_SHIPPING_NAME = "shipping_name";
    public static final String TAG_TOTAL_FORMATTED = "total_formatted";
    public static final String TAG_SHIPPING_PRICE_FORMATTED = "shipping_price_formatted";
    public static final String TAG_NOTE = "note";
    public static final String TAG_COMMENT = "comment";
    public static final String TAG_CARD_CODE = "card_code";
    public static final String TAG_SALES_PERSON_CODE = "sales_person_code";
    public static final String TAG_SERIES = "series";
    public static final String TAG_DELIVERY_DATE = "delivery_date";

    private JsonUtils() {}


    /**
     * @param order
     * @return
     * @throws JSONException
     */
    public static JSONObject createOrderJson(Order order) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put(TAG_SALES_PERSON_CODE, order.getSalesPersonCode());
        jo.put(TAG_SERIES, order.getSeries());
        jo.put(TAG_CARD_CODE, order.getCardCode());
        jo.put(TAG_COMMENT, order.getComment());
        jo.put(TAG_DELIVERY_DATE, order.getDeliveryDate());
        Timber.d("JSONParser postOrder: %s", jo.toString());
        return jo;
    }

    public static JSONObject createUserAuthentication(String email, String password) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put(JsonUtils.TAG_EMAIL, email);
        jo.put(JsonUtils.TAG_PASSWORD, password);
        return jo;
    }
}