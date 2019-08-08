package grintsys.com.vanshop.api;

import grintsys.com.vanshop.SettingsMy;

public class EndPoints {

    /**
     * Base server url.
     */

    //production server
    private static final String API_URL = "http://181.199.177.162:98/api/";

    public static String PAYMENTS                 = "document/GetPayments?userId=%d&begin=%s&end=%s";
    public static String SENT_PAYMENT             = "document/SentPayment?userId=%d&clientId=%d&totalPaid=%f&comment=%s&cash=%s&transfer=%s&checks=%s&invoices=%s&reference=%s&paymentId=%d";
    public static String ADD_PAYMENT              = "document/AddPayment?userId=%d&clientId=%d&totalPaid=%f&comment=%s&cash=%s&transfer=%s&checks=%s&invoices=%s&reference=%s&causanocobro=%s";
    public static String CANCEL_PAYMENT           = "document/CancelPayment?id=%d";
    public static final String SHOPS              = API_URL.concat("shop/GetShops");
    public static String BANKS                    = "bank/GetBanks";
    public static String SHOPS_SINGLE             = "shop/GetShops/%d";
    public static String NAVIGATION_DRAWER        = API_URL.concat("navigation/GetNavigations/%d");
    public static String BANNERS                  = "banner/GetBanners";
    public static String PAGES_SINGLE             = "%d/pages/%d";
    public static String PAGES_TERMS_AND_COND     = "%d/pages/terms";
    public static String CLIENT                   = "client/Client?cardcode=%s";
    public static String CLIENTS                  = "client/GetClients";
    public static String CLIENT_TRANSACTIONS      = "document/GetClientTransactions?cardcode=%s&begin=%s&end=%s";
    //public static final String DOCUMENTS                = API_URL.concat("/GetDocuments");
    public static String DOCUMENTS_SINGLE         = "document/GetDocuments?card_code=%s";
    //public static final String DOCUMENTS_DETAILS        = API_URL.concat("/Document?card_code=%s&include=%d");
    //public static final String CLIENTS_SINGLE           = API_URL.concat("/GetClient?card_code=%s");
    public static String PRODUCTS                 = "product/GetProducts";
    public static String PRODUCTS_SINGLE          = "product/GetProduct/%d";
    public static String PRODUCTS_SINGLE_RELATED  = "product/GetProduct/%d?include=related";
    public static String USER_REGISTER            = "%d/users/register";
    public static String USER_LOGIN_EMAIL         = "auth/LoginByEmail?username=%s&password=%s";
    public static String USER_LOGIN_FACEBOOK      = "%d/login/facebook";
    public static String USER_RESET_PASSWORD      = "%d/users/reset-password";
    public static String USER_SINGLE              = "auth/GetUser/%d";
    public static String USERS                    = "auth/Users";
    public static String USER_CHANGE_PASSWORD     = "%d/users/%d/password";
    public static String USER_UPDATE              = "/UpdateUser?userId=%d&bluetooth=%s";
    public static String CART                     = "cart/Cart?userId=%d&type=%d";
    public static String CART_ADD_ITEM            = "cart/AddToCart?userId=%d&product_variant_id=%d&quantity=%d&cardcode=%s&type=%d";
    public static String CART_INFO                = "cart/CartInfo?userId=%d";
    //public static final String CART_ITEM                = API_URL.concat("cart/Cart/%d");
    public static String CART_ITEM_UPDATE         = "cart/UpdateToCart?userId=%d&productCartItemId=%d&newQuantity=%d&newProductVariantId=%d";
    public static String CART_ITEM_DELETE         = "cart/DeleteToCart?userId=%d&id=%d&type=%d";
    //public static final String CART_DELIVERY_INFO       = API_URL.concat("/Cart/delivery-info");
    public static String CART_DISCOUNTS           = "cart/Cart/discounts";
    //public static final String CART_DISCOUNTS_SINGLE    = API_URL.concat("/Cart/discounts/%d");
    //public static final String ORDERS                   = API_URL.concat("/GetOrders?userId=%d");
    public static String ORDERS_RANGE             = "order/GetOrders?userId=%d&begin=%s&end=%s";
    public static String ORDERS_CREATE            = "order/CreateOrder?userId=%d&cartId=%d&jo=%s";
    public static String ORDERS_SINGLE            = "order/Order/%d";
    //public static final String ORDERS_RECREATE          = API_URL.concat("/ReCreateOrder?orderId=%");
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
