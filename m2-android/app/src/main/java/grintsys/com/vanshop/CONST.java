package grintsys.com.vanshop;

public class CONST {

    // TODO update this variable
    /**
     * Specific organization ID, received by successful integration.
     */
    public static final int ORGANIZATION_ID = 4;
    /**
     * ID used for simulate empty/null value
     */
    public static final int DEFAULT_EMPTY_ID = -131;

    // Volley requests tags
    public static final String ADD_PAYMENT_TAG = "payment_requests";
    public static final String SENT_PAYMENT_TAG = "sent_payment_requests";
    public static final String CANCEL_PAYMENT_TAG = "cancel_payment_requests";
    public static final String BANKS_TAG = "banks_requests";
    public static final String SPLASH_REQUESTS_TAG = "splash_requests";
    public static final String USERS_TAG = "users_request";
    public static final String SALES_PERSON_TAG = "sales_person_requests";
    public static final String DRAWER_REQUESTS_TAG = "drawer_requests";
    public static final String BANNER_REQUESTS_TAG = "banner_requests";
    public static final String CATEGORY_REQUESTS_TAG = "category_requests";
    public static final String CLIENT_REQUESTS_TAG = "client_requests";
    public static final String INVOICE_HISTORY_REQUESTS_TAG = "invoice_history_requests";
    public static final String DOCUMENT_REQUESTS_TAG = "document_requests";
    public static final String PRODUCT_REQUESTS_TAG = "product_requests";
    public static final String PRODUCT_ADD_TO_CART_TAG = "product_add_cart_requests";
    public static final String MAIN_MENU_REQUESTS_TAG = "main_menu_requests";
    public static final String LOGIN_DIALOG_REQUESTS_TAG = "login_dialog_requests";
    public static final String ACCOUNT_REQUESTS_TAG = "account_requests";
    public static final String CART_REQUESTS_TAG = "cart_requests";
    public static final String CART_DISCOUNTS_REQUESTS_TAG = "cart_discounts_requests";
    public static final String ORDER_CREATE_REQUESTS_TAG = "order_create_requests";
    public static final String DELIVERY_DIALOG_REQUESTS_TAG = "delivery_dialog_requests";
    public static final String WISHLIST_REQUESTS_TAG = "wishlist_requests";
    public static final String ACCOUNT_EDIT_REQUESTS_TAG = "account_edit_requests";
    public static final String SETTINGS_REQUESTS_TAG = "settings_requests";
    public static final String UPDATE_CART_ITEM_REQUESTS_TAG = "update_cart_item_requests";
    public static final String MAIN_ACTIVITY_REQUESTS_TAG = "main_activity_requests";
    public static final String PAGE_REQUESTS_TAG = "page_requests";
    public static final String ORDERS_HISTORY_REQUESTS_TAG = "orders_history_requests";
    public static final String ORDERS_DETAIL_REQUESTS_TAG = "orders_detail_requests";
    public static final String PAYMENTS_HISTORY_REQUESTS_TAG = "payments_history_requests";
    public static final String CLIENT_TRANSACTIONS_REQUESTS_TAG = "client_tranactions_requests";
    public static final String REPORT_QUOTA_REQUESTS_TAG = "report_quota_requests";
    public static final String REPORT_QUOTA_ACCUM_REQUESTS_TAG = "report_quota_accum_requests";

    // Bundle constants
    public static final String BUNDLE_PASS_TARGET = "target";
    public static final String BUNDLE_PASS_TITLE = "title";
    /**
     * Volley request unknown status code
     */
    public static final int MissingStatusCode = 9999;
    public static final int MaxQuantityOrder =  999;

    /**
     * Possible visibility states of layout parts.
     */
    public enum VISIBLE {
        EMPTY, CONTENT, PROGRESS
    }
}
