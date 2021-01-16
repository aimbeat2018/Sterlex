package com.sterlex.in.Constant;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    public static String SharedPref = "SharedPref";
    public static String isLogin = "isLogin";
    public static String customer_id = "customer_id";
    public static String customer_unique_id = "customer_unique_id";
    public static String mobile_no = "mobile_no";
    public static String state_id = "state_id";
    public static String city_id = "city_id";
    public static String address = "address";
    public static String pincode = "pincode";
    public static String email_id = "email_id";
    public static String customer_img = "customer_img";
    public static String customer_name = "customer_name";
    public static String token = "token";
    public static String Address = "Address";
    public static String referral_code = "referral_code";
    public static String fran_code = "fran_code";
    public static String version_code = "version_code";
    public static String appExit = "appExit";
    public static String pincodeChecked = "0";
    public static String customer_order_record = "customer_order_record";

    public static void putBol(Context context, String key, boolean val) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, val);
        editor.commit();
    }

    public static boolean getBol(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPref, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public static String getVal(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPref, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static String getPinVal(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPref, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "0");
    }

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


}
