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

    public static final String TAG_IS_IN_WISHLIST = "is_in_wishlist";
    public static final String TAG_WISHLIST_PRODUCT_ID = "wishlist_product_id";
    public static final String TAG_CODE = "code";
    public static final String TAG_PRODUCT_COUNT = "product_count";

    // Server specific JSON tags - user oriented
    public static final String TAG_EMAIL = "usernameOrEmailAddress";
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
    public static final String TAG_TENANT_ID = "TenantId";

    public static final String TAG_BANK_ID = "BankId";
    public static final String TAG_USER_ID = "UserId";
    public static final String TAG_PAYED_AMOUNT_ID = "PayedAmount";
    public static final String TAG_PAYED_STATUS = "Status";
    public static final String TAG_REFERENCE_NUMBER = "ReferenceNumber";
    public static final String TAG_DOCUMENT_CODE = "DocumentCode";
    public static final String TAG_TOTAL_AMOUNT = "TotalAmount";
    public static final String TAG_BALANCE_DUE = "BalanceDue";
    public static final String TAG_DOC_ENTRY = "DocEntry";
    public static final String TAG_PAYMENT_ITEM_LIST = "PaymentItemList";
    public static final String TAG_CLIENT_ID = "ClientId";

    public static final String TAG_PRODUCT_VARIANT_ID = "ProductVariantId";
    public static final String TAG_QUANTITY = "Quantity";

    // ORDERS
    public static final String TAG_DATE_CREATED = "date_created";
    public static final String TAG_PAYED_DATE = "PayedDate";
    public static final String TAG_STATUS = "status";
    public static final String TAG_TOTAL = "total";
    public static final String TAG_SHIPPING_TYPE = "shipping_type";
    public static final String TAG_PAYMENT_TYPE = "Type";
    public static final String TAG_SHIPPING_NAME = "shipping_name";
    public static final String TAG_TOTAL_FORMATTED = "total_formatted";
    public static final String TAG_SHIPPING_PRICE_FORMATTED = "shipping_price_formatted";
    public static final String TAG_NOTE = "note";
    public static final String TAG_COMMENT = "Comment";
    public static final String TAG_CARD_CODE = "CardCode";
    public static final String TAG_SALES_PERSON_CODE = "sales_person_code";
    public static final String TAG_SERIES = "Series";
    public static final String TAG_DELIVERY_DATE = "DeliveryDate";

    private JsonUtils() {}


    /**
     * @param order
     * @return
     * @throws JSONException
     */
    public static JSONObject createOrderJson(Order order, long tenantId) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put(TAG_SERIES, order.getSeries());
        jo.put(TAG_CARD_CODE, order.getCardCode());
        jo.put(TAG_COMMENT, order.getComment());
        jo.put(TAG_DELIVERY_DATE, order.getDeliveryDate());
        jo.put(TAG_TENANT_ID, tenantId);
        Timber.d("JSONParser postOrder: %s", jo.toString());
        return jo;
    }

    public static JSONObject createUserAuthentication(String email, String password, long id) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put(JsonUtils.TAG_EMAIL, email);
        jo.put(JsonUtils.TAG_PASSWORD, password);
        jo.put(JsonUtils.TAG_TENANT_ID, id);
        return jo;
    }
}
