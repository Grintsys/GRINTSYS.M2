package grintsys.com.vanshop.api;

import grintsys.com.vanshop.SettingsMy;

public class EndPoints {

    /**
     * Base server url.
     */

    //production server
    public static String API_URL                  = "http://192.168.1.28:62114/api/";
    public static final String TENANTS            = API_URL.concat("services/app/Tenant/GetAll");
    public static String CATEGORIES               = API_URL.concat("services/app/category/getAll");

    public static String USER_LOGIN_EMAIL         = API_URL.concat("tokenAuth/AuthenticateTenantUser");
    public static String USER_LOGIN_FACEBOOK      = "%d/login/facebook";
    public static String USER_RESET_PASSWORD      = "%d/users/reset-password";
    public static String USER_SINGLE              = API_URL.concat("services/app/user/get?Id=%d");
    public static String USERS                    = "auth/Users";
    public static String USER_CHANGE_PASSWORD     = "%d/users/%d/password";
    public static String USER_UPDATE              = "/UpdateUser?userId=%d&bluetooth=%s";

    public static String PAYMENTS                 = API_URL.concat("services/app/payment/GetPaymentsByUser?tenantId=%d&begin=%s&end=%s");
    public static String PAYMENT_DELETE           = API_URL.concat("services/app/payment/DeletePayment?Id=%d");

    public static String ADD_PAYMENT              = API_URL.concat("services/app/payment/CreatePayment");
    public static String SENT_PAYMENT             = API_URL.concat("services/app/payment/SentPayment");
    public static String CANCEL_PAYMENT           = "document/CancelPayment?id=%d";
    public static String BANKS                    = API_URL.concat("services/app/bank/getAll?tenantId=%d");
    public static String SHOPS_SINGLE             = "shop/GetShops/%d";

    public static String BANNERS                  = "banner/GetBanners";
    public static String CLIENT                   = API_URL.concat("services/app/client/GetClient?Id=%s");
    public static String CLIENTS                  = API_URL.concat("services/app/client/GetClientBySearchQuery?tenantId=%d");
    public static String CLIENT_TRANSACTIONS      = "document/GetClientTransactions?cardcode=%s&begin=%s&end=%s";
    //public static final String DOCUMENTS                = API_URL.concat("/GetDocuments");
    public static String INVOICES                 = API_URL.concat("services/app/client/GetClientDocuments?Id=%d");
    //public static final String DOCUMENTS_DETAILS        = API_URL.concat("/Document?card_code=%s&include=%d");
    //public static final String CLIENTS_SINGLE           = API_URL.concat("/GetClient?card_code=%s");
    public static String PRODUCT                  = API_URL.concat("services/app/Product/GetProduct?Id=%d");
    public static String PRODUCTS                 = API_URL.concat("services/app/product/GetProductByQuerySearch");
    public static String PRODUCTS_SINGLE_RELATED  = "product/GetProduct/%d?include=related";

    public static String CART                     = API_URL.concat("services/app/cart/GetCart?tenantId=%d");
    public static String CART_CREATE              = API_URL.concat("services/app/cart/CreateCart?tenantId=%d");
    public static String CART_ADD_ITEM            = API_URL.concat("services/app/cart/AddItemToCart");
    //public static final String CART_ITEM                = API_URL.concat("cart/Cart/%d");
    public static String CART_ITEM_UPDATE         = "cart/UpdateToCart?userId=%d&productCartItemId=%d&newQuantity=%d&newProductVariantId=%d";
    public static String CART_ITEM_DELETE         = API_URL.concat("services/app/cart/DeleteItemToCart?id=%d");
    public static String CART_DISCOUNTS           = "cart/Cart/discounts";
    public static String ORDERS_RANGE             = API_URL.concat("services/app/order/GetOrdersByUser?tenantId=%d&begin=%s&end=%s");
    public static String ORDERS_CREATE            = API_URL.concat("services/app/order/CreateOrder");
    public static String ORDERS_SINGLE            = API_URL.concat("services/app/order/GetOrder?Id=%d");
    public static String BRANCHES                 = "%d/branches";
    public static String WISHLIST                 = "cart/GetWishlist?userId=%d";
    public static String WISHLIST_CREATE          = "cart/AddToWishList?userId=%d&variantId=%d";
    public static String WISHLIST_SINGLE          = "cart/GetWishlist/%d?userId=%d";
    public static String WISHLIST_IS_IN_WISHLIST  = "%d/wishlist/is-in-wishlist/%d?userId=%d";
    public static String REGISTER_NOTIFICATION    = "auth/GetDevices";
    public static String MAIN_MENU_BADGE_COUNT    = "stat/GetMenuBadgeCount";
    public static String INVOICE_HISTORY          = "document/GetInvoiceHistory";

    public static String REPORT_QUOTA_PIE         = "stat/GetReportQuota?userId=%d&year=%d&month=%d";
    public static String REPORT_QUOTA_ACCUM_LINEAR= "stat/GetReportQuotaAccum?userId=%d&year=%d&month=%d&day=%d";

    // Notifications parameters
    public static final String NOTIFICATION_LINK        = "link";
    public static final String NOTIFICATION_MESSAGE     = "message";
    public static final String NOTIFICATION_TITLE       = "title";
    public static final String NOTIFICATION_IMAGE_URL   = "image_url";
    public static final String NOTIFICATION_SHOP_ID     = "shop_id";
    public static final String NOTIFICATION_UTM         = "utm";

    private EndPoints() {}
}
