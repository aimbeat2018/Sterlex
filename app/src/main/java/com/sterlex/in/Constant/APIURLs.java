package com.sterlex.in.Constant;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class APIURLs {

    //public static String BASE_URL = "https://vegshopy.com/vegshop/App_json/";
    public static String BASE_URL = "https://exoticbasket.in/strlex/";
    public static String LOGIN_OTP_URL = BASE_URL + "login_otp";
    public static String LOGIN_URL = BASE_URL + "cust_login";
    public static String SEND_OTP = BASE_URL + "send_otp";
    public static String reset_otp = BASE_URL + "reset_otp";
    public static String RESET_PASSWORD_URL = BASE_URL + "reset_password";
    public static String BANNER_URL = BASE_URL + "banner_list";
    public static String get_cats = BASE_URL + "get_cats";
    public static String HOME_SECTION_URL = BASE_URL + "get_sections";
    public static String SUB_CATEGORY_URL = BASE_URL + "get_sub_cats";
    public static String SUBCATEGORY_URL = BASE_URL + "subcategory_list";
    public static String sub_subcategory_list = BASE_URL + "sub_subcategory_list";
    public static String sub_sub_category_list = BASE_URL + "get_sub_sub_cats";
    public static String CATEGORY_WISE_PRODUCT_LIST = BASE_URL + "category_wise_product_list";
    public static String Delivery_Percentage_URL = BASE_URL + "getDeliveryPercentage";
    public static String PRODUCTLIST_URL = BASE_URL + "product_list";
    public static String PRODUCTDETAILS_URL = BASE_URL + "product_details_id_wise";
    public static String PRODUCTList_URL = BASE_URL + "product_list";
    public static String REGISTRATION_URL = BASE_URL + "reg_cust";
    public static String ADDCART_URL = BASE_URL + "add_to_cart";
    public static String CARTCOUNT_URL = BASE_URL + "cart_items_det";
    public static String CARTPRODUCT_LIST_URL = BASE_URL + "view_cart";
    public static String CARTPRODUCTLIST_URL = BASE_URL + "cart_wise_product";
    public static String SAVELATERPRODUCTLIST_URL = BASE_URL + "save_wise_product";
    public static String SAVEFORLATER_URL = BASE_URL + "save_for_later";
    public static String REMOVECART_URL = BASE_URL + "remove_cart_product";
    public static String REMOVESAVELATER_URL = BASE_URL + "remove_save_product";
    public static String COUPONLIST_URL = BASE_URL + "coupon_list";
    public static String SUBSCRIPTION_COUPONLIST_URL = BASE_URL + "subscription_coupon_list";
    public static String remove_product_qty2 = BASE_URL + "remove_product_qty";
    public static String remove_product_qty = BASE_URL + "delete_cart_item";
    public static String view_product_entry = BASE_URL + "view_product_entry";
    public static String customer_address = BASE_URL + "customer_address";
    public static String customer_address_record = BASE_URL + "customer_address_record";
    public static String customer_address_update = BASE_URL + "customer_address_update";
    public static String address_delete = BASE_URL + "address_delete";
    public static String placeOrder = BASE_URL + "order_entry";
    public static String order_history = BASE_URL + "order_history";
    public static String order_wise_item_history = BASE_URL + "order_wise_item_history";
    public static String order_summary = BASE_URL + "order_summary";
    public static String cancel_order = BASE_URL + "cancel_order";
    public static String order_cancel = BASE_URL + "order_cancel";
    public static String customer_profile = BASE_URL + "customer_profile";
    public static String update_profile = BASE_URL + "update_profile";
    public static String my_subscription = BASE_URL + "my_subscription";
    public static String subscription_list = BASE_URL + "subscription_list";
    public static String single_product_pause = BASE_URL + "single_product_pause";
    public static String single_product_end = BASE_URL + "single_product_end";
    public static String all_product_end = BASE_URL + "all_product_end";
    public static String all_product_pause = BASE_URL + "all_product_pause";
    public static String all_product_resume = BASE_URL + "all_product_resume";
    public static String single_product_resume = BASE_URL + "single_product_resume";
    public static String my_subscription_cal = BASE_URL + "my_subscribtion_cal";
    public static String product_search_list = BASE_URL + "product_search_list";
    public static String add_more_product = BASE_URL + "add_more_product";
    public static String customer_wallet = BASE_URL + "customer_wallet";
    public static String wallet_history = BASE_URL + "wallet_history";
    public static String recommend_list = BASE_URL + "recommend_list";
    public static String pickup_point = BASE_URL + "pickup_point";
    public static String slot_time = BASE_URL + "slot_time";
    public static String view_product_list = BASE_URL + "view_product_list";
    public static String check_pincode = BASE_URL + "check_pincode";
    public static String re_book_subscribe = BASE_URL + "re_book_subscribe";
    public static String minimum_amount_limit = BASE_URL + "minimum_amount_limit";
    public static String order_limit = BASE_URL + "order_limit";
    public static String best_selling_product = BASE_URL + "best_selling_product";
    public static String sub_category_wise_product_list = BASE_URL + "sub_category_wise_product_list";
    public static String sub_subcategory_wise_product_list = BASE_URL + "sub_subcategory_wise_product_list";
    public static String offer_list = BASE_URL + "offer_list";
    public static String offer_wise_product = BASE_URL + "offer_wise_product";
    public static String hot_product_list = BASE_URL + "hot_product_list";
    public static String notification_list = BASE_URL + "notification_list";
    public static String today_offer = BASE_URL + "today_offer";
    public static String add_to_wishlist = BASE_URL + "add_to_wishlist";
    public static String remove_from_wishlist = BASE_URL + "remove_from_wishlist";
    public static String wishlist_listing = BASE_URL + "wishlist_listing";
    public static String getNewHomePage = BASE_URL + "getNewHomePage";
    public static String quick_order_v1 = BASE_URL + "quick_order_v1";

    public static boolean cancleDialog = false;
    public static boolean pincodeChecked = false;

    public static boolean isNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
