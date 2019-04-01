package grintsys.com.vanshop.api;

public class EndPoints {

    /**
     * Base server url.
     */
    //private static final String API_URL                  = "http://181.199.190.20/VanHeusenShop/api/list";    // staging
    //Production
    //private static final String API_URL                  = "http://181.199.190.20/VanShopVhBackend/api/list";    // staging
    //private static final String API_URL = "http://181.199.190.20/VanShopVhBackendPruebas3/api/list";    // staging

    private static final String API_URL = "http://181.199.177.162/M2HN/api/list";    // staging
    //private static final String API_URL = "http://181.199.190.20/M2CR/api/list";    // staging

    public static final String PAYMENTS                 = API_URL.concat("/GetPayments?userId=%d&begin=%s&end=%s");
    public static final String SENT_PAYMENT             = API_URL.concat("/SentPayment?userId=%d&clientId=%d&totalPaid=%f&comment=%s&cash=%s&transfer=%s&checks=%s&invoices=%s&reference=%s&paymentId=%d");
    public static final String ADD_PAYMENT              = API_URL.concat("/AddPayment?userId=%d&clientId=%d&totalPaid=%f&comment=%s&cash=%s&transfer=%s&checks=%s&invoices=%s&reference=%s&causanocobro=%s");
    public static final String CANCEL_PAYMENT           = API_URL.concat("/CancelPayment?id=%d");
    public static final String SHOPS                    = API_URL.concat("/GetShops");
    public static final String BANKS                    = API_URL.concat("/GetBanks");
    public static final String SHOPS_SINGLE             = API_URL.concat("/GetShops/%d");
    public static final String NAVIGATION_DRAWER        = API_URL.concat("/GetNavigations/%d");
    public static final String BANNERS                  = API_URL.concat("/GetBanners");
    public static final String PAGES_SINGLE             = API_URL.concat("%d/pages/%d");
    public static final String PAGES_TERMS_AND_COND     = API_URL.concat("%d/pages/terms");
    public static final String CLIENT                   = API_URL.concat("/Client?cardcode=%s");
    public static final String CLIENTS                  = API_URL.concat("/GetClients");
    public static final String CLIENT_TRANSACTIONS      = API_URL.concat("/GetClientTransactions?cardcode=%s&begin=%s&end=%s");
    public static final String DOCUMENTS                = API_URL.concat("/GetDocuments");
    public static final String DOCUMENTS_SINGLE         = API_URL.concat("/GetDocuments?card_code=%s");
    public static final String DOCUMENTS_DETAILS        = API_URL.concat("/Document?card_code=%s&include=%d");
    public static final String CLIENTS_SINGLE           = API_URL.concat("/GetClient?card_code=%s");
    public static final String PRODUCTS                 = API_URL.concat("/GetProducts");
    public static final String PRODUCTS_SINGLE          = API_URL.concat("/GetProduct/%d");
    public static final String PRODUCTS_SINGLE_RELATED  = API_URL.concat("/GetProduct/%d?include=related");
    public static final String USER_REGISTER            = API_URL.concat("%d/users/register");
    public static final String USER_LOGIN_EMAIL         = API_URL.concat("/LoginByEmail?username=%s&password=%s");
    public static final String USER_LOGIN_FACEBOOK      = API_URL.concat("%d/login/facebook");
    public static final String USER_RESET_PASSWORD      = API_URL.concat("%d/users/reset-password");
    public static final String USER_SINGLE              = API_URL.concat("/GetUser/%d");
    public static final String USERS                    = API_URL.concat("/Users");
    public static final String USER_CHANGE_PASSWORD     = API_URL.concat("%d/users/%d/password");
    public static final String USER_UPDATE              = API_URL.concat("/UpdateUser?userId=%d&bluetooth=%s");
    public static final String CART                     = API_URL.concat("/Cart?userId=%d&type=%d");
    public static final String CART_ADD_ITEM            = API_URL.concat("/AddToCart?userId=%d&product_variant_id=%d&quantity=%d&cardcode=%s&type=%d");
    public static final String CART_INFO                = API_URL.concat("/CartInfo?userId=%d");
    public static final String CART_ITEM                = API_URL.concat("/Cart/%d");
    public static final String CART_ITEM_UPDATE         = API_URL.concat("/UpdateToCart?userId=%d&productCartItemId=%d&newQuantity=%d&newProductVariantId=%d");
    public static final String CART_ITEM_DELETE         = API_URL.concat("/DeleteToCart?userId=%d&id=%d&type=%d");
    public static final String CART_DELIVERY_INFO       = API_URL.concat("/Cart/delivery-info");
    public static final String CART_DISCOUNTS           = API_URL.concat("/Cart/discounts");
    public static final String CART_DISCOUNTS_SINGLE    = API_URL.concat("/Cart/discounts/%d");
    public static final String ORDERS                   = API_URL.concat("/GetOrders?userId=%d");
    public static final String ORDERS_RANGE             = API_URL.concat("/GetOrders?userId=%d&begin=%s&end=%s");
    public static final String ORDERS_CREATE            = API_URL.concat("/CreateOrder?userId=%d&cartId=%d&jo=%s");
    public static final String ORDERS_SINGLE            = API_URL.concat("/Order/%d");
    public static final String ORDERS_RECREATE          = API_URL.concat("/ReCreateOrder?orderId=%");
    public static final String BRANCHES                 = API_URL.concat("%d/branches");
    public static final String WISHLIST                 = API_URL.concat("/GetWishlist?userId=%d");
    public static final String WISHLIST_CREATE          = API_URL.concat("/AddToWishList?userId=%d&variantId=%d");
    public static final String WISHLIST_SINGLE          = API_URL.concat("/GetWishlist/%d?userId=%d");
    public static final String WISHLIST_IS_IN_WISHLIST  = API_URL.concat("%d/wishlist/is-in-wishlist/%d?userId=%d");
    public static final String REGISTER_NOTIFICATION    = API_URL.concat("/GetDevices");
    public static final String MAIN_MENU_BADGE_COUNT    = API_URL.concat("/GetMenuBadgeCount");
    public static final String INVOICE_HISTORY          = API_URL.concat("/GetInvoiceHistory");


    public static final String REPORT_QUOTA_PIE         = API_URL.concat("/GetReportQuota?userId=%d&year=%d&month=%d");
    public static final String REPORT_QUOTA_ACCUM_LINEAR= API_URL.concat("/GetReportQuotaAccum?userId=%d&year=%d&month=%d&day=%d");


    // Notifications parameters
    public static final String NOTIFICATION_LINK        = "link";
    public static final String NOTIFICATION_MESSAGE     = "message";
    public static final String NOTIFICATION_TITLE       = "title";
    public static final String NOTIFICATION_IMAGE_URL   = "image_url";
    public static final String NOTIFICATION_SHOP_ID     = "shop_id";
    public static final String NOTIFICATION_UTM         = "utm";

    private EndPoints() {}
}
