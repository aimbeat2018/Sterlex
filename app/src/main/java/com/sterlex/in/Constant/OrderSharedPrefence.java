package com.sterlex.in.Constant;

import android.content.Context;
import android.content.SharedPreferences;

public class OrderSharedPrefence {
    public static String SharedPref = "SharedPref";
    public static String total_bag = "total_bag";
    public static String order_grand_total = "order_grand_total";
    public static String total_discount = "total_discount";
    public static String no_of_items = "no_of_items";
    public static String slot_id = "slot_id";
    public static String coupon_id = "coupon_id";
    public static String deliver_address = "deliver_address";
    public static String delivery_type = "delivery_type";
    public static String flag = "flag";
    public static String percentage = "percentage";
    public static String delivery_charge_amount = "delivery_charge_amount";
    public static String order_amount = "order_amount";
    public static String rid = "rid";
    public static String delivery_charges = "delivery_charges";
    public static String delivery_percent = "delivery_percent";
    public static String delivery_flag = "delivery_flag";
    public static String minus_wallet = "minus_wallet";
    public static String delivery_status = "delivery_status";
    public static String product_id = "product_id";
    public static String coupon_amt = "coupon_amt";
    public static String discount_amt = "discount_amt";
    public static String qty = "qty";
    public static String unit = "unit";
    public static String unit_price = "unit_price";
    public static String discount = "discount";
    public static String pickUp_id = "pickUp_id";
    public static String area_id = "area_id";


    public static void putVal(Context context, String key, String val) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, val);
        editor.commit();
    }

    public static void clearData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public static String getVal(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPref, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

}
