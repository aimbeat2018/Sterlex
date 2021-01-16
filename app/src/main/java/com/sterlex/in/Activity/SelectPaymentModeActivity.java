
package com.sterlex.in.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.gmail.samehadar.iosdialog.IOSDialog;
import com.razorpay.Checkout;
import com.sterlex.in.Constant.APIURLs;
import com.sterlex.in.Constant.FunctionConstant;
import com.sterlex.in.Constant.OrderSharedPrefence;
import com.sterlex.in.Constant.SharedPref;
import com.sterlex.in.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class SelectPaymentModeActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    String status2;
    ImageView img_back, img_remove;
    TextView txt_unitPrice, txt_discountCharges, txt_couponCharges, txt_subTotal, txt_grandTotal, txt_TotalSaving,
            txt_deliveryAddress, txt_couponCode, txt_walletCharges, txt_walletAmount, txt_SuperCoin, txt_superCoinCharges, txt_delivery;
    LinearLayout lnr_coupon, lnr_totalsaving, lnr_discount, lnr_walletBalance, lnr_superCoinBalance, lnr_useWallet, lnr_useSuperCoin, lnr_paymode;
    RelativeLayout rel_cash;
    CheckBox chk_useWallet, chk_useSuperCoin;
    CardView card_cash, card_online;
    ImageView img_cashSelect, img_onlineSelect;
    private final int COUPON_USED = 1;
    Button btn_pay;
    Double gtotal = 0.0, subTotal = 0.0;
    String coupon_name = "", coupon_id = "", coupon_value = "", capping_value = "";

    String product_id = "", grand_total = "", sub_total = "", slot_id = "", unit_price = "", discount_amt = "", coupon_amt = "", qty = "", address = "", finalUnit = "", finalUnitPrice = "", finalDiscount = "", actualBalance = "", walletBalance = "", superCoinBalance = "", delivery_status = "";
    String totalSaving = "", area_id = "", delivery_charges = "", delivery_flag = "", delivery_percent = "";
    IOSDialog dialog;
    String paymentFlag = "", payBalance = "", fid;
    Double walletdouble, superCoinDouble;
    Double subTotaldouble;
    Double grandTotaldouble;
    static int min, max, create_otp;
    String order_id, razorpayPaymentIDMain, payamt, rid = "NA";
    int capt = 1;
    private Object AlertDialog;
    ShimmerFrameLayout shimmer_view_container;
    LinearLayout lnr_main;
    String customer_order_record = "", wallet_use_min_limit = "", cost_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        setContentView(R.layout.activity_select_payment_mode);

        cost_id = SharedPref.getVal(SelectPaymentModeActivity.this, SharedPref.customer_id);

        unit_price = OrderSharedPrefence.getVal(SelectPaymentModeActivity.this, OrderSharedPrefence.total_bag);
        discount_amt = OrderSharedPrefence.getVal(SelectPaymentModeActivity.this, OrderSharedPrefence.total_discount);
        address = OrderSharedPrefence.getVal(SelectPaymentModeActivity.this, OrderSharedPrefence.deliver_address);
        delivery_charges = OrderSharedPrefence.getVal(SelectPaymentModeActivity.this, OrderSharedPrefence.delivery_charges);
        delivery_flag = OrderSharedPrefence.getVal(SelectPaymentModeActivity.this, OrderSharedPrefence.delivery_flag);
        delivery_percent = OrderSharedPrefence.getVal(SelectPaymentModeActivity.this, OrderSharedPrefence.delivery_percent);
        slot_id = OrderSharedPrefence.getVal(SelectPaymentModeActivity.this, OrderSharedPrefence.slot_id);

        init();

//        getIntentData();
        customer_order_record = SharedPref.getVal(SelectPaymentModeActivity.this, SharedPref.customer_order_record);
        onClick();
        showAlert();
        setData();


    }

    private void onClick() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        img_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_couponCharges.setText("");
                txt_couponCode.setText("");
                lnr_coupon.setVisibility(View.GONE);
                coupon_name = "";
                coupon_id = "";
                coupon_value = "";
                capping_value = "";
                setData();
                onResume();
                chk_useSuperCoin.setChecked(false);
                img_remove.setVisibility(View.GONE);
            }
        });
        card_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_cashSelect.setVisibility(View.VISIBLE);
                img_onlineSelect.setVisibility(View.GONE);
                paymentFlag = "0";
            }
        });
        card_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_cashSelect.setVisibility(View.GONE);
                img_onlineSelect.setVisibility(View.VISIBLE);
                paymentFlag = "1";
            }
        });

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paymentFlag.equals("")) {
                    Toast.makeText(SelectPaymentModeActivity.this, "Please select payment method", Toast.LENGTH_SHORT).show();
                } else {

                    if (paymentFlag.equals("0") || paymentFlag.equals("3")) {

//                        fid = SharedPref.getVal(SelectPaymentModeActivity.this, SharedPref.fran_code);
                        btn_pay.setClickable(false);
                        btn_pay.setEnabled(false);
                        new LoginAsynck2().execute();

//                        placeOrder();
                    } else {

                        String grand = txt_grandTotal.getText().toString();
                        grand = grand.substring(1, grand.length());
                        String wallet = "";
                        if (grand.equals("0.0")) {
                            grand = sub_total;
                        }

                        try {

                            double a = Double.parseDouble(grand);
                            double b = a;
                            b = b * 100;
                            payamt = String.valueOf(b);

                            // payamt = String.valueOf(Integer.parseInt(grand)*100);

                            min = 100000;
                            max = 999999;
                            Random r = new Random();
                            create_otp = r.nextInt(max - min + 1) + min;
                            order_id = String.valueOf(create_otp);
                            startPayment();
//                            placeOrder();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }
            }
        });

        chk_useWallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (customer_order_record.equals("1")) {
                    if (isChecked) {
                        try {
                            lnr_walletBalance.setVisibility(View.VISIBLE);
                            walletdouble = Double.parseDouble(walletBalance);
                            grand_total = txt_subTotal.getText().toString();
                            grand_total = grand_total.substring(1);
                            subTotaldouble = Double.parseDouble(grand_total);
                            grandTotaldouble = 0.0;

                            if (subTotaldouble >= walletdouble) {
                                grandTotaldouble = subTotaldouble - walletdouble;
                                payBalance = String.valueOf(walletdouble);
                                txt_walletCharges.setText("- " + payBalance);
                                walletdouble = 0.0;
                                payBalance = "";
                                long price_amount = Math.round(Double.valueOf(grandTotaldouble));

                                txt_grandTotal.setText("\u20B9" + price_amount);
                                lnr_paymode.setVisibility(View.VISIBLE);
                            } else {
                                walletdouble = walletdouble - subTotaldouble;
                                payBalance = String.valueOf(subTotaldouble);
                                txt_walletCharges.setText("- " + payBalance);
                                grandTotaldouble = 0.0;
                                long price_amount = Math.round(Double.valueOf(grandTotaldouble));

                                txt_grandTotal.setText("\u20B9" + String.valueOf(price_amount));
                                lnr_paymode.setVisibility(View.GONE);
                                paymentFlag = "3";
                            }
//                        setData();l
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        if (coupon_value.equals("")) {
                            if (chk_useSuperCoin.isChecked()) {
                                gtotal = (Double.valueOf(unit_price) - Double.valueOf(discount_amt)) + Double.valueOf(delivery_charges) - Double.valueOf(superCoinBalance);
                                subTotal = (Double.valueOf(unit_price) - Double.valueOf(discount_amt)) + Double.valueOf(delivery_charges);
                            } else {
                                gtotal = (Double.valueOf(unit_price) - Double.valueOf(discount_amt)) + Double.valueOf(delivery_charges);
                                subTotal = (Double.valueOf(unit_price) - Double.valueOf(discount_amt)) + Double.valueOf(delivery_charges);
                            }
                        } else {
                            if (chk_useSuperCoin.isChecked()) {
                                gtotal = (Double.valueOf(unit_price) - Double.valueOf(discount_amt)) + Double.valueOf(delivery_charges) - Double.valueOf(coupon_value) - Double.valueOf(superCoinBalance);
                                subTotal = (Double.valueOf(unit_price) - Double.valueOf(discount_amt)) + Double.valueOf(delivery_charges) - Double.valueOf(coupon_value);
                            } else {
                                gtotal = (Double.valueOf(unit_price) - Double.valueOf(discount_amt)) + Double.valueOf(delivery_charges) - Double.valueOf(coupon_value);
                                subTotal = (Double.valueOf(unit_price) - Double.valueOf(discount_amt)) + Double.valueOf(delivery_charges) - Double.valueOf(coupon_value);

                            }
                        }


                        paymentFlag = "";
                        lnr_walletBalance.setVisibility(View.GONE);
                        lnr_paymode.setVisibility(View.VISIBLE);
                        long subtotal_amount = Math.round(Double.valueOf(subTotal));
                        long price_amount = Math.round(Double.valueOf(gtotal));

                        txt_subTotal.setText("\u20B9" + subtotal_amount);
                        txt_grandTotal.setText("\u20B9" + price_amount);

                    }
                } else {
                    minimuncart();
                }
            }
        });
        chk_useSuperCoin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (customer_order_record.equals("1")) {
                    if (isChecked) {
                        try {
                            lnr_superCoinBalance.setVisibility(View.VISIBLE);
                            superCoinDouble = Double.parseDouble(superCoinBalance);
                            grand_total = txt_subTotal.getText().toString();
                            grand_total = grand_total.substring(1);
                            subTotaldouble = Double.parseDouble(grand_total);
                            grandTotaldouble = 0.0;
                            walletdouble = Double.parseDouble(walletBalance);

                            if (chk_useWallet.isChecked()) {

                                Double gtotal_amount = subTotaldouble - walletdouble - superCoinDouble;
                                long superCoinDoubleAmount = Math.round(superCoinDouble);
                                long roundedAmount = Math.round(gtotal_amount);
                                txt_superCoinCharges.setText("- \u20B9" + superCoinDoubleAmount);
                                txt_grandTotal.setText("\u20B9" + roundedAmount);

                            } else {

                                Double gtotal_amount = subTotaldouble - superCoinDouble;
                                long superCoinDoubleAmount = Math.round(superCoinDouble);
                                long roundedAmount = Math.round(gtotal_amount);
                                txt_superCoinCharges.setText("- \u20B9" + superCoinDoubleAmount);
                                txt_grandTotal.setText("\u20B9" + roundedAmount);

//                                if (subTotaldouble >= walletdouble) {
//                                    grandTotaldouble = subTotaldouble - walletdouble;
//                                    payBalance = String.valueOf(walletdouble);
//                                    txt_walletCharges.setText("- " + payBalance);
//                                    walletdouble = 0.0;
//                                    payBalance = "";
//                                    long price_amount = Math.round(Double.valueOf(grandTotaldouble));
//
//                                    txt_grandTotal.setText("\u20B9" + price_amount);
//                                    lnr_paymode.setVisibility(View.VISIBLE);
//                                } else {
//                                    walletdouble = walletdouble - subTotaldouble;
//                                    payBalance = String.valueOf(subTotaldouble);
//                                    txt_walletCharges.setText("- " + payBalance);
//                                    grandTotaldouble = 0.0;
//                                    long price_amount = Math.round(Double.valueOf(grandTotaldouble));
//
//                                    txt_grandTotal.setText("\u20B9" + String.valueOf(price_amount));
//                                    lnr_paymode.setVisibility(View.GONE);
//                                    paymentFlag = "3";
//                                }
                            }

//                        setData();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {

                        if (coupon_value.equals("")) {
                            if (chk_useWallet.isChecked()) {
                                subTotal = (Double.valueOf(unit_price) - Double.valueOf(discount_amt)) + Double.valueOf(delivery_charges);
                                gtotal = (Double.valueOf(unit_price) - Double.valueOf(discount_amt)) + Double.valueOf(delivery_charges) - Double.valueOf(walletBalance);
                            } else {
                                subTotal = (Double.valueOf(unit_price) - Double.valueOf(discount_amt)) + Double.valueOf(delivery_charges);
                                gtotal = (Double.valueOf(unit_price) - Double.valueOf(discount_amt)) + Double.valueOf(delivery_charges);

                            }
                        } else {
                            if (chk_useWallet.isChecked()) {
                                subTotal = (Double.valueOf(unit_price) - Double.valueOf(discount_amt)) + Double.valueOf(delivery_charges) - Double.valueOf(coupon_value);
                                gtotal = (Double.valueOf(unit_price) - Double.valueOf(discount_amt)) + Double.valueOf(delivery_charges) - Double.valueOf(coupon_value) - Double.valueOf(walletBalance);
                            } else {
                                subTotal = (Double.valueOf(unit_price) - Double.valueOf(discount_amt)) + Double.valueOf(delivery_charges) - Double.valueOf(coupon_value);
                                gtotal = (Double.valueOf(unit_price) - Double.valueOf(discount_amt)) + Double.valueOf(delivery_charges) - Double.valueOf(coupon_value);

                            }
                        }


                        paymentFlag = "";
                        lnr_superCoinBalance.setVisibility(View.GONE);
                        lnr_paymode.setVisibility(View.VISIBLE);
                        long price_amount = Math.round(Double.valueOf(gtotal));
                        long subTotal_amount = Math.round(Double.valueOf(subTotal));

                        txt_subTotal.setText("\u20B9" + subTotal_amount);
                        txt_grandTotal.setText("\u20B9" + price_amount);

                    }
                } else {
                    minimuncart();
                }
            }
        });
    }

    public void useCoupon(View view) {
        if (cost_id.equals("")) {
            Intent intent = new Intent(SelectPaymentModeActivity.this, LoginActivity.class);
            intent.putExtra("from", "cart");
            startActivity(intent);
        } else {
            Intent intent = new Intent(SelectPaymentModeActivity.this, CouponListActivity.class);
            startActivityForResult(intent, COUPON_USED);
        }
    }

    public void showAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SelectPaymentModeActivity.this);
        // Setting Dialog Title
        alertDialog.setTitle("Alert!!!");
        // Setting Dialog Message
        alertDialog.setMessage("Products will be delivered as market availability But we are trying our best to delivery product.");
        // On pressing Settings button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                startActivity(new Intent(SelectPaymentModeActivity.this, CartActivity2.class));
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        walletBalance();
        shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        lnr_main.setVisibility(View.GONE);
    }


    private class LoginAsynck2 extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

           /* pDialog = new android.app.ProgressDialog(SelectPaymentModeActivity.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.setIndeterminate(false);
            pDialog.show();*/
            shimmer_view_container.stopShimmerAnimation();
            lnr_main.setVisibility(View.GONE);
            shimmer_view_container.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... arg0) {
            OkHttpClient client2 = new OkHttpClient();
            client2 = new OkHttpClient.Builder()
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .build();

            try {
                RequestBody formBody = new FormBody.Builder()
//                        .add("franchise_id", fid)

                        .build();
                okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
                //builder.url(URLS.login);
                builder.url(APIURLs.order_limit);
                builder.post(formBody);
                okhttp3.Request request = builder.build();
                okhttp3.Response response = client2.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
          /*  if (pDialog.isShowing())
                pDialog.dismiss();*/

            try {
                JSONObject jsn = new JSONObject(result);
                JSONArray test = jsn.optJSONArray("result");
                JSONObject test1 = test.getJSONObject(0);

                status2 = test1.optString("status").toString();

                if (status2.equals("1")) {
                    btn_pay.setClickable(true);
                    btn_pay.setEnabled(true);
                    placeOrder();
                } else {
                    Toast.makeText(SelectPaymentModeActivity.this, "we are unavailable deliver now due to heavy traffic", Toast.LENGTH_SHORT).show();
                }

                dismissDialog();

            } catch (JSONException e) {
                btn_pay.setClickable(true);
                btn_pay.setEnabled(true);
                dismissDialog();
                e.printStackTrace();
            }

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == COUPON_USED) {
            if (resultCode == RESULT_OK) { // Activity.RESULT_OK

                coupon_name = data.getStringExtra("coupon_name");
                coupon_id = data.getStringExtra("coupon_id");
                coupon_value = data.getStringExtra("coupon_value");//200
                capping_value = data.getStringExtra("capping_value");//1900

                Double cappingValue = Double.parseDouble(capping_value);
                String gtotal = txt_grandTotal.getText().toString();
                gtotal = gtotal.substring(1, gtotal.length());
                Double g_total = Double.valueOf(gtotal);
                if (!cappingValue.equals("")) {
                    if (g_total >= cappingValue) {
                        img_remove.setVisibility(View.VISIBLE);
                        txt_couponCode.setText(coupon_name);
                        lnr_coupon.setVisibility(View.VISIBLE);
                        long coupon_amount = Math.round(Double.valueOf(coupon_value));

                        txt_couponCharges.setText("- \u20B9" + coupon_amount);
                        setData();
                    } else {
                        lnr_coupon.setVisibility(View.GONE);
                        txt_couponCode.setText("");

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SelectPaymentModeActivity.this);
                        // Setting Dialog Title
                        alertDialog.setTitle("Alert!!!");
                        // Setting Dialog Message
                        alertDialog.setMessage("This Coupon will be validate, \nover \u20B9" + capping_value + " Order Value");
                        // On pressing Settings button
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                coupon_name = "";
                                coupon_id = "";
                                coupon_value = "";
                                capping_value = "";
                                dialog.cancel();
                            }
                        });
                        alertDialog.setCancelable(false);
                        // Showing Alert Message
                        alertDialog.show();
                    }
                } else {
                    img_remove.setVisibility(View.GONE);
                    lnr_coupon.setVisibility(View.GONE);
                    txt_couponCode.setText("");
                }

            }

        }
    }

/*
    private void getIntentData() {

        delivery_status = getIntent().getStringExtra("delivery_status");
        product_id = getIntent().getStringExtra("product_id");
        List<String> items = Arrays.asList(product_id.split("\\s*,\\s*"));
        String position = "";
        for (int i = 0; i < items.size(); i++) {
            String itemstr = items.get(i);
            if (Collections.frequency(items, itemstr) > 1) {
                items.remove(i);
            }
        }
        product_id = TextUtils.join(", ", items);

        grand_total = getIntent().getStringExtra("grand_total");
        grand_total = grand_total.substring(3, grand_total.length());

        sub_total = getIntent().getStringExtra("sub_total");
        sub_total = sub_total.substring(3, sub_total.length());

        coupon_id = getIntent().getStringExtra("coupon_id");
        slot_id = getIntent().getStringExtra("slot_id");
        area_id = getIntent().getStringExtra("area_id");
        delivery_charges = getIntent().getStringExtra("delivery_charges");

        unit_price = getIntent().getStringExtra("unit_price");
        unit_price = unit_price.substring(3, unit_price.length());

        discount_amt = getIntent().getStringExtra("discount_amt");
        if (discount_amt.equals("")) {
            discount_amt = "0";
        }
        coupon_amt = getIntent().getStringExtra("coupon_amt");
        if (coupon_amt.equals("")) {
            coupon_amt = "0";
        }
        qty = getIntent().getStringExtra("qty");
        address = getIntent().getStringExtra("address");
        finalUnit = getIntent().getStringExtra("finalUnit");
        finalUnitPrice = getIntent().getStringExtra("finalUnitPrice");
        finalDiscount = getIntent().getStringExtra("finalDiscount");
      *//*  actualBalance = getIntent().getStringExtra("actualBalance");
        walletBalance = getIntent().getStringExtra("walletBalance");*//*

     *//* if(actualBalance.equals("")){
            lnr_walletBalance.setVisibility(View.GONE);
        }else{
            lnr_walletBalance.setVisibility(View.VISIBLE);
        }
*//*

        try {
            *//*if (!coupon_amt.equals("") || !discount_amt.equals("")) {
                //int saving = Integer.parseInt(coupon_amt) + Integer.parseInt(discount_amt);
                double saving = Double.parseDouble(coupon_amt) + Double.parseDouble(discount_amt);
                totalSaving = String.valueOf(saving);
            }*//*
//            setData();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }*/

    public void setData() {
//        dialog.show();
        shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        lnr_main.setVisibility(View.GONE);

//        if (delivery_status.equals("1")) {
//            rel_cash.setVisibility(View.GONE);
//        } else {
//            rel_cash.setVisibility(View.VISIBLE);
//        }
        if (coupon_value.equals("")) {

            subTotal = (Double.valueOf(unit_price) - Double.valueOf(discount_amt)) + Double.valueOf(delivery_charges);
            long price_amount = Math.round(Double.valueOf(subTotal));
            txt_subTotal.setText("\u20B9" + price_amount);
            if (chk_useWallet.isChecked()) {
                try {
                    lnr_walletBalance.setVisibility(View.VISIBLE);
                    walletdouble = Double.parseDouble(walletBalance);
                    grand_total = txt_subTotal.getText().toString();
                    grand_total = grand_total.substring(1);
                    subTotaldouble = Double.parseDouble(grand_total);
                    grandTotaldouble = 0.0;

                    if (subTotaldouble >= walletdouble) {
                        grandTotaldouble = subTotaldouble - walletdouble;
//                        grandTotaldouble = grandTotaldouble - Double.valueOf(coupon_value);
                        payBalance = String.valueOf(walletdouble);
                        txt_walletCharges.setText("- " + payBalance);
                        walletdouble = 0.0;
                        payBalance = "";
//                        gtotal = grandTotaldouble;

//                        Double gtotal11 = (Double.valueOf(unit_price) - Double.valueOf(discount_amt)) + Double.valueOf(delivery_charges) - Double.valueOf(coupon_value);
//                        long subTotal_amount = Math.round(Double.valueOf(gtotal11));
//                        txt_subTotal.setText("\u20B9" + subTotal_amount);


                        long price_amount1 = Math.round(Double.valueOf(grandTotaldouble));
                        txt_grandTotal.setText("\u20B9" + price_amount1);

                        lnr_paymode.setVisibility(View.VISIBLE);
                    } else {
                        walletdouble = walletdouble - subTotaldouble;
                        payBalance = String.valueOf(subTotaldouble);
                        txt_walletCharges.setText("- " + payBalance);
                        grandTotaldouble = 0.0;
//                        gtotal = grandTotaldouble;

//                        Double gtotal11 = (Double.valueOf(unit_price) - Double.valueOf(discount_amt)) + Double.valueOf(delivery_charges) - Double.valueOf(coupon_value);
//                        long subTotal_amount = Math.round(Double.valueOf(gtotal11));
//                        txt_subTotal.setText("\u20B9" + subTotal_amount);


                        long price_amount11 = Math.round(Double.valueOf(grandTotaldouble));
                        txt_grandTotal.setText("\u20B9" + price_amount11);
                        lnr_paymode.setVisibility(View.GONE);
                        paymentFlag = "3";
                    }
//                        setData();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                txt_grandTotal.setText("\u20B9" + price_amount);

            }

        } else {
            lnr_superCoinBalance.setVisibility(View.GONE);
            lnr_useSuperCoin.setVisibility(View.GONE);
            superCoinDouble = 0.0;
            superCoinBalance.equals("0");
            if (chk_useWallet.isChecked()) {
//                if (chk_useSuperCoin.isChecked()) {
//
//                } else {
                try {
                    lnr_walletBalance.setVisibility(View.VISIBLE);
                    walletdouble = Double.parseDouble(walletBalance);
                    grand_total = txt_subTotal.getText().toString();
                    grand_total = grand_total.substring(1);
                    subTotaldouble = Double.parseDouble(grand_total);
                    grandTotaldouble = 0.0;

                    if (subTotaldouble >= walletdouble) {
                        grandTotaldouble = subTotaldouble - walletdouble;
                        grandTotaldouble = grandTotaldouble - Double.valueOf(coupon_value);
                        payBalance = String.valueOf(walletdouble);
                        txt_walletCharges.setText("- " + payBalance);
                        walletdouble = 0.0;
                        payBalance = "";
                        gtotal = grandTotaldouble;

                        Double gtotal11 = (Double.valueOf(unit_price) - Double.valueOf(discount_amt)) + Double.valueOf(delivery_charges) - Double.valueOf(coupon_value);
                        long subTotal_amount = Math.round(Double.valueOf(gtotal11));
                        txt_subTotal.setText("\u20B9" + subTotal_amount);


                        long price_amount = Math.round(Double.valueOf(grandTotaldouble));
                        txt_grandTotal.setText("\u20B9" + String.valueOf(price_amount));

                        lnr_paymode.setVisibility(View.VISIBLE);
                    } else {
                        walletdouble = walletdouble - subTotaldouble;
                        payBalance = String.valueOf(subTotaldouble);
                        txt_walletCharges.setText("- " + payBalance);
                        grandTotaldouble = 0.0;
                        gtotal = grandTotaldouble;

                        Double gtotal11 = (Double.valueOf(unit_price) - Double.valueOf(discount_amt)) + Double.valueOf(delivery_charges) - Double.valueOf(coupon_value);
                        long subTotal_amount = Math.round(Double.valueOf(gtotal11));
                        txt_subTotal.setText("\u20B9" + subTotal_amount);


                        long price_amount = Math.round(Double.valueOf(grandTotaldouble));
                        txt_grandTotal.setText("\u20B9" + String.valueOf(price_amount));
                        lnr_paymode.setVisibility(View.GONE);
                        paymentFlag = "3";
                    }
//                        setData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                }
            } else {
//                if (chk_useSuperCoin.isChecked()) {
//
//                } else {
                gtotal = (Double.valueOf(unit_price) - Double.valueOf(discount_amt)) + Double.valueOf(delivery_charges) - Double.valueOf(coupon_value);
                long price_amount = Math.round(Double.valueOf(gtotal));

                txt_subTotal.setText("\u20B9" + price_amount);
                txt_grandTotal.setText("\u20B9" + price_amount);
            }
//            }
        }
        OrderSharedPrefence.putVal(SelectPaymentModeActivity.this, OrderSharedPrefence.order_grand_total, gtotal.toString());
        long price_amount = Math.round(Double.valueOf(gtotal));


//        txt_subTotal.setText("\u20B9" + price_amount);

//        txt_grandTotal.setText("\u20B9" + price_amount);
        txt_deliveryAddress.setText(address);

//        txt_grandTotal.setText("Rs." + grand_total);
//        txt_subTotal.setText("Rs." + sub_total);
//        txt_unitPrice.setText("Rs." + unit_price);
//        txt_walletCharges.setText("- " + actualBalance);

        if (discount_amt.equals("") || discount_amt.equals("0")) {
            lnr_totalsaving.setVisibility(View.GONE);
            lnr_discount.setVisibility(View.GONE);
        } else {
            lnr_discount.setVisibility(View.VISIBLE);
            long discount_amount = Math.round(Double.valueOf(discount_amt));

            txt_TotalSaving.setText(" \u20B9" + discount_amount);
            long price1_amount = Math.round(Double.valueOf(discount_amt));

            txt_discountCharges.setText("-  \u20B9" + price1_amount);
        }
//        if (coupon_amt.equals("") || coupon_amt.equals("0")) {
//            lnr_coupon.setVisibility(View.GONE);
//        } else {
//            lnr_coupon.setVisibility(View.VISIBLE);
//            txt_couponCharges.setText("-" + coupon_amt);
//        }

        if (delivery_charges.equals("0")) {
            txt_delivery.setText("FREE");
        } else {
            long delivery_charges_amount = Math.round(Double.valueOf(delivery_charges));

            txt_delivery.setText("+  \u20B9" + delivery_charges_amount);
            txt_delivery.setTextColor(getResources().getColor(R.color.black));
        }


    /*if (totalSaving.equals("") || totalSaving.equals("0") || totalSaving.equals("0.0")) {
        lnr_totalsaving.setVisibility(View.GONE);
    } else {
        lnr_totalsaving.setVisibility(View.VISIBLE);
        txt_TotalSaving.setText("Rs." + totalSaving);
    }*/
        dismissDialog();

    }

    private void init() {
        dialog = new IOSDialog.Builder(SelectPaymentModeActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();
        img_back = findViewById(R.id.img_back);
        img_remove = findViewById(R.id.img_remove);
        txt_unitPrice = findViewById(R.id.txt_unitPrice);
        shimmer_view_container = findViewById(R.id.shimmer_view_container);
        lnr_main = findViewById(R.id.lnr_main);
        txt_discountCharges = findViewById(R.id.txt_discountCharges);
        txt_couponCharges = findViewById(R.id.txt_couponCharges);
        txt_subTotal = findViewById(R.id.txt_subTotal);
        txt_grandTotal = findViewById(R.id.txt_grandTotal);
        txt_TotalSaving = findViewById(R.id.txt_TotalSaving);
        txt_walletCharges = findViewById(R.id.txt_walletCharges);
        txt_deliveryAddress = findViewById(R.id.txt_deliveryAddress);
        txt_couponCode = findViewById(R.id.txt_couponCode);
        lnr_coupon = findViewById(R.id.lnr_coupon);
        lnr_totalsaving = findViewById(R.id.lnr_totalsaving);
        lnr_discount = findViewById(R.id.lnr_discount);
        card_cash = findViewById(R.id.card_cash);
        card_online = findViewById(R.id.card_online);
        img_cashSelect = findViewById(R.id.img_cashSelect);
        img_onlineSelect = findViewById(R.id.img_onlineSelect);
        lnr_walletBalance = findViewById(R.id.lnr_walletBalance);
        lnr_superCoinBalance = findViewById(R.id.lnr_superCoinBalance);
        txt_superCoinCharges = findViewById(R.id.txt_superCoinCharges);
        lnr_useWallet = findViewById(R.id.lnr_useWallet);
        lnr_useSuperCoin = findViewById(R.id.lnr_useSuperCoin);
        chk_useSuperCoin = findViewById(R.id.chk_useSuperCoin);
        lnr_paymode = findViewById(R.id.lnr_paymode);
        chk_useWallet = findViewById(R.id.chk_useWallet);
        txt_walletAmount = findViewById(R.id.txt_walletAmount);
        btn_pay = findViewById(R.id.btn_pay);
        rel_cash = findViewById(R.id.rel_cash);
        txt_delivery = findViewById(R.id.txt_delivery);
        txt_SuperCoin = findViewById(R.id.txt_SuperCoin);

        if (OrderSharedPrefence.getVal(SelectPaymentModeActivity.this, OrderSharedPrefence.delivery_type).equals("0")) {
            rel_cash.setVisibility(View.GONE);
        }
        long price_amount = Math.round(Double.valueOf(unit_price));

        txt_unitPrice.setText("\u20B9" + price_amount);

        txt_deliveryAddress.setText(address);
//        if (discount_amt.equals("") || discount_amt.equals("0")) {
//            lnr_totalsaving.setVisibility(View.GONE);
//        } else {
//            lnr_totalsaving.setVisibility(View.VISIBLE);
//            txt_TotalSaving.setText("Rs." + discount_amt);
//        }
//
//        Double gtotal = Double.valueOf(total) - Double.valueOf(discount_amt);
//        txt_subTotal.setText(gtotal.toString());
//        txt_grandTotal.setText(gtotal.toString());
//        txt_deliveryAddress.setText(address);
//
//        if (delivery_charges.equals("0")) {
//            txt_delivery.setText("FREE");
//        } else {
//            txt_delivery.setText("Rs. +" + delivery_charges);
//            txt_delivery.setTextColor(getResources().getColor(R.color.black));
//        }

    }

    public void dismissDialog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               /* if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }*/
                shimmer_view_container.stopShimmerAnimation();
                shimmer_view_container.setVisibility(View.GONE);
                lnr_main.setVisibility(View.VISIBLE);
            }
        }, 2000);
    }

    public void dismissDialog1() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      /*  if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }*/
        shimmer_view_container.stopShimmerAnimation();
        shimmer_view_container.setVisibility(View.GONE);
        lnr_main.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onPause() {
        super.onPause();
        shimmer_view_container.stopShimmerAnimation();
        shimmer_view_container.setVisibility(View.GONE);
        lnr_main.setVisibility(View.VISIBLE);
    }

    public void minimuncart() {
//        dialog.show();
        shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        lnr_main.setVisibility(View.GONE);

        if (APIURLs.isNetwork(SelectPaymentModeActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.minimum_amount_limit, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            wallet_use_min_limit = jsonObject1.getString("wallet_use_min_limit");

                            if (Double.valueOf(grand_total) >= Double.valueOf(wallet_use_min_limit)) {
                                try {
                                    lnr_walletBalance.setVisibility(View.VISIBLE);
                                    walletdouble = Double.parseDouble(walletBalance);
                                    subTotaldouble = Double.parseDouble(grand_total);
                                    grandTotaldouble = 0.0;

                                    if (subTotaldouble >= walletdouble) {
                                        grandTotaldouble = subTotaldouble - walletdouble;
                                        payBalance = String.valueOf(walletdouble);
                                        txt_walletCharges.setText("- " + payBalance);
                                        walletdouble = 0.0;
                                        payBalance = "";
                                        long price_amount = Math.round(Double.valueOf(grandTotaldouble));

                                        txt_grandTotal.setText("\u20B9" + String.valueOf(price_amount));
                                        lnr_paymode.setVisibility(View.VISIBLE);
                                    } else {
                                        walletdouble = walletdouble - subTotaldouble;
                                        payBalance = String.valueOf(subTotaldouble);
                                        txt_walletCharges.setText("- " + payBalance);
                                        grandTotaldouble = 0.0;
                                        long price_amount = Math.round(Double.valueOf(grandTotaldouble));

                                        txt_grandTotal.setText("\u20B9" + String.valueOf(price_amount));
                                        lnr_paymode.setVisibility(View.GONE);
                                        paymentFlag = "3";
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(SelectPaymentModeActivity.this, "To use your wallet cart value should be" + wallet_use_min_limit + " or more.", Toast.LENGTH_LONG).show();
                            }


                        } else {
                            Toast.makeText(SelectPaymentModeActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                        dismissDialog();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dismissDialog();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dismissDialog();
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("customer_id", SharedPref.getVal(SelectPaymentModeActivity.this, SharedPref.customer_id));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(SelectPaymentModeActivity.this, "no internet connection");
            /*Toast.makeText(this, "no internet connection", Toast.LENGTH_SHORT).show();*/
        }
//        return miniCart;
    }

    public void walletBalance() {
//        dialog.show();
        shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        lnr_main.setVisibility(View.GONE);

        if (APIURLs.isNetwork(SelectPaymentModeActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.customer_wallet, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            walletBalance = jsonObject1.getString("wallet");
                            superCoinBalance = jsonObject1.getString("super_coin");

                            if (walletBalance.equals("0")) {
                                lnr_useWallet.setVisibility(View.GONE);
                            } else {
                                lnr_useWallet.setVisibility(View.VISIBLE);
                                txt_walletAmount.setText("Available Balance Rs: " + walletBalance);
                            }
                            if (superCoinBalance.equals("0")) {
                                lnr_useSuperCoin.setVisibility(View.GONE);
                            } else {
                                if(coupon_value.equals("")&&coupon_id.equals("")) {
                                    lnr_useSuperCoin.setVisibility(View.VISIBLE);
                                    txt_SuperCoin.setText("Available Supercoins: " + superCoinBalance);
                                }
                            }
                            /*
                            txt_walletCharges.setText("- " + walletBalance);*/
//                            getProductList();
                        } else {
                            Toast.makeText(SelectPaymentModeActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                        dismissDialog();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dismissDialog();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dismissDialog();
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("customer_id", SharedPref.getVal(SelectPaymentModeActivity.this, SharedPref.customer_id));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            final Dialog dialog = new Dialog(SelectPaymentModeActivity.this);
            dialog.setContentView(R.layout.no_internet_dialog);
            Button button = dialog.findViewById(R.id.btn_process);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    walletBalance();
                    dialog.dismiss();
                }
            });
            dialog.show();
            /*Toast.makeText(this, "no internet connection", Toast.LENGTH_SHORT).show();*/
        }
    }

    public void placeOrder() {
        dialog.show();
        /*shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        lnr_main.setVisibility(View.GONE);*/
        if (APIURLs.isNetwork(SelectPaymentModeActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.placeOrder, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        if (status.equals("1")) {
                            Toast.makeText(SelectPaymentModeActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                            String OrderId = jsonObject1.getString("order_generate_id");
                            Intent intent = new Intent(SelectPaymentModeActivity.this, OrderPlacedActivity.class);
                            intent.putExtra("OrderId", OrderId);
                            startActivity(intent);
                            finish();
                        } else if (status.equals("0")) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(SelectPaymentModeActivity.this);
                            // Setting Dialog Title
                            alertDialog.setTitle("Alert!!!");
                            // Setting Dialog Message
                            alertDialog.setMessage("You are blocked, \nPlease contact our team for further enquiry.");
                            // On pressing Settings button
                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(SelectPaymentModeActivity.this, HelpAndSupportActivity.class);
                                    startActivity(intent);
                                }
                            });
                            // on pressing cancel button
                            alertDialog.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            // Showing Alert Message
                            alertDialog.show();
                        } else {
                            Toast.makeText(SelectPaymentModeActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                        dismissDialog1();
                        btn_pay.setClickable(true);
                        btn_pay.setEnabled(true);
                    } catch (JSONException e) {
                        dismissDialog1();
                        e.printStackTrace();
                        btn_pay.setClickable(true);
                        btn_pay.setEnabled(true);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dismissDialog1();
                    error.printStackTrace();
                    btn_pay.setClickable(true);
                    btn_pay.setEnabled(true);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {


                    String discount_amt = "", coupon_Amt = "", delivery_charges1 = delivery_charges;
                    String total_bag = txt_grandTotal.getText().toString();

                    total_bag = total_bag.substring(1, total_bag.length());

                    String order_total = txt_grandTotal.getText().toString();
                    order_total = order_total.substring(1, order_total.length());

                    if (lnr_discount.getVisibility() == View.VISIBLE) {
//                        discount_amt = txt_TotalSaving.getText().toString();
//                        discount_amt = discount_amt.substring(5, discount_amt.length());
                        discount_amt = OrderSharedPrefence.getVal(SelectPaymentModeActivity.this, OrderSharedPrefence.total_discount);
                    } else {
                        discount_amt = "0";
                    }
                    if (lnr_coupon.getVisibility() == View.VISIBLE) {
                        coupon_Amt = txt_couponCharges.getText().toString();
                        coupon_Amt = coupon_Amt.substring(1, coupon_Amt.length());
                    } else {
                        coupon_Amt = "0";
                    }

//                    if (txt_delivery_fees.getText().toString().equals("FREE")) {
//                        delivery_charges = "FREE";
//                    } else {
//                        delivery_charges = txt_delivery_fees.getText().toString();
//                        delivery_charges = delivery_charges.substring(5, delivery_charges.length());
//                    }

                    String grand = OrderSharedPrefence.getVal(SelectPaymentModeActivity.this, OrderSharedPrefence.order_grand_total);

                            /*txt_grandTotal.getText().toString();
                    grand = grand.substring(3, grand.length());*/
                    String wallet = "";
                    if (grand.equals("0.0")) {
                        grand = sub_total;
                    }
                    if (chk_useWallet.isChecked()) {
                        wallet = txt_walletCharges.getText().toString();
                        wallet = wallet.substring(1, wallet.length());
                    } else {
                        wallet = "0.0";
                    }
                    HashMap<String, String> params = new HashMap<>();
                    params.put("customer_id", SharedPref.getVal(SelectPaymentModeActivity.this, SharedPref.customer_id));
//                    params.put("franchise_id", SharedPref.getVal(SelectPaymentModeActivity.this, SharedPref.fran_code));
                    params.put("total_bag", unit_price);
                    params.put("bag_discount", discount_amt);
                    params.put("order_total", grand);
                    params.put("slot_id", slot_id);
                    params.put("coupon_id", coupon_id);
                    params.put("deliver_address", address);
                    params.put("flag", paymentFlag);
                    params.put("rid", rid);
                    params.put("delivery_charges", delivery_charges1);
                    params.put("minus_wallet", wallet);
                    params.put("delivery_status", OrderSharedPrefence.getVal(SelectPaymentModeActivity.this, OrderSharedPrefence.delivery_status));
                    params.put("area_id", OrderSharedPrefence.getVal(SelectPaymentModeActivity.this, OrderSharedPrefence.area_id));
                    params.put("pickup_id", OrderSharedPrefence.getVal(SelectPaymentModeActivity.this, OrderSharedPrefence.pickUp_id));
                    params.put("product_id", OrderSharedPrefence.getVal(SelectPaymentModeActivity.this, OrderSharedPrefence.product_id));
                    params.put("qty", OrderSharedPrefence.getVal(SelectPaymentModeActivity.this, OrderSharedPrefence.qty));
                    params.put("unit", OrderSharedPrefence.getVal(SelectPaymentModeActivity.this, OrderSharedPrefence.unit));
                    params.put("unit_price", OrderSharedPrefence.getVal(SelectPaymentModeActivity.this, OrderSharedPrefence.unit_price));
                    params.put("discount", OrderSharedPrefence.getVal(SelectPaymentModeActivity.this, OrderSharedPrefence.discount));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            btn_pay.setClickable(true);
            btn_pay.setEnabled(true);
            dismissDialog();
            FunctionConstant.noInternetDialog(SelectPaymentModeActivity.this, "no internet connection");
        }
    }


    public void startPayment() {

        final Activity activity = this;

        final Checkout co = new Checkout();


        try {
            JSONObject options = new JSONObject();
            options.put("name", SharedPref.getVal(SelectPaymentModeActivity.this, SharedPref.customer_name));
            options.put("description", "Sterlex Super Mart");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", payamt);


            JSONObject preFill = new JSONObject();
            preFill.put("email", SharedPref.getVal(SelectPaymentModeActivity.this, SharedPref.email_id));
            preFill.put("contact", SharedPref.getVal(SelectPaymentModeActivity.this, SharedPref.mobile_no));
            preFill.put("payment_capture", capt);
            preFill.put("order_id", order_id);

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error com payment: " + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
            e.printStackTrace();
        }

    }


    @SuppressWarnings("unused")

    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_LONG).show();
            razorpayPaymentIDMain = razorpayPaymentID;
            rid = razorpayPaymentID;
            placeOrder();
        } catch (Exception e) {

        }
    }


    @SuppressWarnings("unused")

    public void onPaymentError(int code, String response) {
        try {

            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_LONG).show();

        } catch (Exception e) {

        }
    }


}
