package com.sterlex.in.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.sterlex.in.Constant.SharedPref;
import com.sterlex.in.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SubscriptionPaymentModeActivity extends AppCompatActivity {

    ImageView img_back;
    TextView txt_unitPrice, txt_discountCharges, txt_couponCharges, txt_subTotal, txt_grandTotal, txt_TotalSaving,
            txt_deliveryAddress, txt_mincart, txt_couponCode, txt_days;
    LinearLayout lnr_coupon, lnr_saving, lnr_discount;
    CardView card_cash, card_online;
    ImageView img_cashSelect, img_onlineSelect, img_remove;
    Button btn_pay;
    String product_id = "", from_date = "", to_date = "", unit_price = "", qty = "", address = "", total_bag = "", bag_discount = "",
            order_total = "", subscribe_mode = "", day = "", unit = "", discount = "", subscription_days = "", coupon_amt = "", price = "";
    IOSDialog dialog;
    String paymentFlag = "", miniCart = "";
    double minCart = 0.0;
    private final int COUPON_USED = 1;
    String coupon_name = "", coupon_id = "", coupon_value = "", capping_value = "";
    String razorpayPaymentIDMain, rid, order_id, payamt;
    String ridd = "NA";
    int capt = 1;
    static int min, max, create_otp;
    ShimmerFrameLayout shimmer_view_container;
    LinearLayout lnr_main;
    String totalBag = "", unitBag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        setContentView(R.layout.activity_subscription_payment_mode);
        init();
//        getIntentData();
        onClick();
    }

    private void onClick() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
                minCart = Double.parseDouble(miniCart);
                totalBag = total_bag.substring(3, total_bag.length());
                unitBag = unit_price.substring(3, unit_price.length());
                if (Double.parseDouble(totalBag) >= minCart) {
//
                    if (paymentFlag.equals("")) {
                        Toast.makeText(SubscriptionPaymentModeActivity.this, "Please select payment method", Toast.LENGTH_SHORT).show();
                    } else {
                        if (paymentFlag.equals("0")) {
                            subscribed();
//                            new DownloadJSON2().execute();
                        } else {
                            min = 100000;
                            max = 999999;
                            Random r = new Random();
                            create_otp = r.nextInt(max - min + 1) + min;
                            order_id = String.valueOf(create_otp);
                            payamt = String.valueOf(Integer.parseInt(totalBag) * 100);
                            startPayment();
                        }
                    }
                } else {
//                    txt_mincart.setVisibility(View.VISIBLE);
                    Toast.makeText(SubscriptionPaymentModeActivity.this, "Grand total should be greater then minimum value i.e. " + String.valueOf(minCart), Toast.LENGTH_SHORT).show();
                }

            }
        });

        img_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coupon_name = "";
                coupon_id = "";
                coupon_value = "";
                capping_value = "";
                subscriptionCalculation();
            }
        });
    }

    private void getIntentData() {

        /*product_id = getIntent().getStringExtra("product_id");
        from_date = getIntent().getStringExtra("from_date");
        to_date = getIntent().getStringExtra("to_date");
        subscribe_mode = getIntent().getStringExtra("subscribe_mode");
        day = getIntent().getStringExtra("day");
        unit = getIntent().getStringExtra("unit");
        discount = getIntent().getStringExtra("discount");
        unit_price = getIntent().getStringExtra("unit_price");
        qty = getIntent().getStringExtra("qty");*/
        address = getIntent().getStringExtra("address");
        from_date = getIntent().getStringExtra("from_date");
        to_date = getIntent().getStringExtra("to_date");
        subscribe_mode = getIntent().getStringExtra("subscribe_mode");
        product_id = getIntent().getStringExtra("product_id");
        qty = getIntent().getStringExtra("qty");
        day = getIntent().getStringExtra("day");
        unit_price = getIntent().getStringExtra("unit_price");
        unit = getIntent().getStringExtra("unit");
        discount = getIntent().getStringExtra("discount");
        coupon_id = getIntent().getStringExtra("coupon_id");
        total_bag = getIntent().getStringExtra("total_bag");
        subscription_days = getIntent().getStringExtra("subscription_days");
        bag_discount = getIntent().getStringExtra("bag_discount");
        coupon_amt = getIntent().getStringExtra("coupon_amt");
        price = getIntent().getStringExtra("price");

//        subscriptionCalculation();
        setData();
    }

    public void useCoupon(View view) {
        Intent intent = new Intent(SubscriptionPaymentModeActivity.this, SubscriptionCouponListActivity.class);
        startActivityForResult(intent, COUPON_USED);
    }

    private void init() {
        dialog = new IOSDialog.Builder(SubscriptionPaymentModeActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();
        shimmer_view_container = findViewById(R.id.shimmer_view_container);
        lnr_main = findViewById(R.id.lnr_main);
        img_back = findViewById(R.id.img_back);
        txt_unitPrice = findViewById(R.id.txt_unitPrice);
        txt_discountCharges = findViewById(R.id.txt_discountCharges);
        txt_couponCharges = findViewById(R.id.txt_couponCharges);
        txt_subTotal = findViewById(R.id.txt_subTotal);
        txt_grandTotal = findViewById(R.id.txt_grandTotal);
        txt_TotalSaving = findViewById(R.id.txt_TotalSaving);
        txt_deliveryAddress = findViewById(R.id.txt_deliveryAddress);
        txt_mincart = findViewById(R.id.txt_mincart);
        txt_couponCode = findViewById(R.id.txt_couponCode);
        lnr_coupon = findViewById(R.id.lnr_coupon);
        lnr_saving = findViewById(R.id.lnr_saving);
        lnr_discount = findViewById(R.id.lnr_discount);
        card_cash = findViewById(R.id.card_cash);
        card_online = findViewById(R.id.card_online);
        img_cashSelect = findViewById(R.id.img_cashSelect);
        img_onlineSelect = findViewById(R.id.img_onlineSelect);
        btn_pay = findViewById(R.id.btn_pay);
        img_remove = findViewById(R.id.img_remove);
        txt_days = findViewById(R.id.txt_days);
    }

    @Override
    protected void onResume() {
        super.onResume();
        minimuncart();
        shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        lnr_main.setVisibility(View.GONE);
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


    public void subscribed() {
//        dialog.show();
        shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        lnr_main.setVisibility(View.GONE);

        if (APIURLs.isNetwork(SubscriptionPaymentModeActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.my_subscription, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        if (status.equals("1")) {
                            Toast.makeText(SubscriptionPaymentModeActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SubscriptionPaymentModeActivity.this, OrderPlacedActivity.class);
                            intent.putExtra("OrderId", "");
                            startActivity(intent);
                            finish();
                        } else if (status.equals("0")) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(SubscriptionPaymentModeActivity.this);
                            // Setting Dialog Title
                            alertDialog.setTitle("Alert!!!");
                            // Setting Dialog Message
                            alertDialog.setMessage("You are blocked, \nPlease contact our team for further enquiry.");
                            // On pressing Settings button
                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(SubscriptionPaymentModeActivity.this, HelpAndSupportActivity.class);
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
                            Toast.makeText(SubscriptionPaymentModeActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                        dismissDialog();
                    } catch (JSONException e) {
                        dismissDialog();
                        e.printStackTrace();
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
                    params.put("customer_id", SharedPref.getVal(SubscriptionPaymentModeActivity.this, SharedPref.customer_id));
//                    params.put("franchise_id", SharedPref.getVal(SubscriptionPaymentModeActivity.this, SharedPref.fran_code));
                    params.put("from_date", from_date);
                    params.put("to_date", to_date);
                    params.put("subscribe_mode", subscribe_mode);
                    params.put("product_id", product_id);
                    params.put("qty", qty);
                    params.put("day", day);
                    params.put("unit", unit);
                    params.put("address", address);
                    params.put("unit_price", price);
                    params.put("discount", discount);
                    params.put("p_mode", paymentFlag);
                    params.put("order_total", totalBag);
                    params.put("coupon_id", coupon_id);
                    params.put("rid", ridd);
                    params.put("total_day", subscription_days);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(SubscriptionPaymentModeActivity.this, "no internet connection");
        }

    }

    public void setData() {
        txt_deliveryAddress.setText(address);
        txt_unitPrice.setText(unit_price);
        txt_days.setText(subscription_days);
        if (bag_discount.equals("0")) {
            lnr_discount.setVisibility(View.GONE);
            lnr_saving.setVisibility(View.GONE);
        } else {
            lnr_discount.setVisibility(View.VISIBLE);
            lnr_saving.setVisibility(View.VISIBLE);

            txt_discountCharges.setText("- " + total_bag);
            txt_TotalSaving.setText(bag_discount);
        }
        txt_subTotal.setText(total_bag);
        txt_grandTotal.setText(total_bag);

        if (coupon_amt.equals("")) {
            lnr_coupon.setVisibility(View.GONE);
        } else {
            lnr_coupon.setVisibility(View.VISIBLE);
            txt_couponCharges.setText(" - " + coupon_amt);
        }
        dismissDialog();
    }

    public void subscriptionCalculation() {
//        dialog.show();
        shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        lnr_main.setVisibility(View.GONE);
        if (APIURLs.isNetwork(SubscriptionPaymentModeActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.my_subscription_cal, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("bag_details");
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            total_bag = jsonObject1.getString("total_bag");
                            bag_discount = jsonObject1.getString("bag_discount");
                            order_total = jsonObject1.getString("order_total");

                            txt_unitPrice.setText("Rs." + total_bag);
                            if (bag_discount.equals("0")) {
                                lnr_discount.setVisibility(View.GONE);
                                lnr_saving.setVisibility(View.GONE);
                            } else {
                                lnr_discount.setVisibility(View.VISIBLE);
                                lnr_saving.setVisibility(View.VISIBLE);

                                txt_discountCharges.setText("- " + "Rs." + bag_discount);
                                txt_TotalSaving.setText("Rs." + bag_discount);
                            }
                            txt_subTotal.setText("Rs." + order_total);
                            txt_grandTotal.setText("Rs." + order_total);

                            try {
                                if (!capping_value.equals("")) {
                                    img_remove.setVisibility(View.VISIBLE);
                                    if (Integer.parseInt(order_total) >= Integer.parseInt(capping_value)) {
                                        txt_couponCode.setText(coupon_name);
                                        lnr_coupon.setVisibility(View.VISIBLE);
                                        txt_couponCharges.setText("-" + coupon_value);
                                        int finalAmount = Integer.parseInt(order_total) - Integer.parseInt(coupon_value);
                                        order_total = String.valueOf(finalAmount);
                                        txt_grandTotal.setText("Rs." + order_total);
                                        txt_subTotal.setText("Rs." + order_total);
                                    } else {
                                        lnr_coupon.setVisibility(View.GONE);
                                        txt_couponCode.setText("");
                                        Toast.makeText(SubscriptionPaymentModeActivity.this, "coupon code not applicable", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    img_remove.setVisibility(View.GONE);
                                    lnr_coupon.setVisibility(View.GONE);
                                    txt_couponCode.setText("");
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            minCart = Double.parseDouble(miniCart);
                            if (Double.parseDouble(order_total) >= minCart) {
                                txt_mincart.setVisibility(View.GONE);
                            } else {
                                txt_mincart.setVisibility(View.VISIBLE);
                                txt_mincart.setText("Grand total should be greater then minimum value i.e. " + String.valueOf(minCart));
                            }
                            txt_deliveryAddress.setText(address);
                        } else {
                            Toast.makeText(SubscriptionPaymentModeActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                        dismissDialog();
                    } catch (JSONException e) {
                        dismissDialog();
                        e.printStackTrace();
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
                    params.put("product_id", product_id);
                    params.put("qty", qty);
                    params.put("unit", unit);
                    params.put("unit_price", unit_price);
                    params.put("discount", discount);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(SubscriptionPaymentModeActivity.this, "no internet connection");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == COUPON_USED) {
            if (resultCode == RESULT_OK) { // Activity.RESULT_OK

                coupon_name = data.getStringExtra("coupon_name");
                coupon_id = data.getStringExtra("coupon_id");
                coupon_value = data.getStringExtra("coupon_value");
                capping_value = data.getStringExtra("capping_value");

                subscriptionCalculation();

            }

        }
    }

    public void minimuncart() {
//        dialog.show();
        shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        lnr_main.setVisibility(View.GONE);
        if (APIURLs.isNetwork(SubscriptionPaymentModeActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.minimum_amount_limit, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            miniCart = jsonObject1.getString("subscription_min_limit");
                            /*if(walletBalance.equals("0")){
                                lnr_walletBalance.setVisibility(View.GONE);
                            }else{
                                lnr_walletBalance.setVisibility(View.VISIBLE);
                            }
                            txt_walletCharges.setText("- "+walletBalance);
                            getProductList();*/

                            getIntentData();
                        } else {
                            Toast.makeText(SubscriptionPaymentModeActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                    params.put("customer_id", SharedPref.getVal(SubscriptionPaymentModeActivity.this, SharedPref.customer_id));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            final Dialog dialog = new Dialog(SubscriptionPaymentModeActivity.this);
            dialog.setContentView(R.layout.no_internet_dialog);
            Button button = dialog.findViewById(R.id.btn_process);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    minimuncart();
                    dialog.dismiss();
                }
            });
            dialog.show();
            /*Toast.makeText(this, "no internet connection", Toast.LENGTH_SHORT).show();*/
        }
//        return miniCart;
    }


    public void startPayment() {

        final Activity activity = this;

        final Checkout co = new Checkout();


        try {
            JSONObject options = new JSONObject();
            options.put("name", SharedPref.getVal(SubscriptionPaymentModeActivity.this, SharedPref.customer_name));
            options.put("description", "Exotic Basket");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", payamt);


            JSONObject preFill = new JSONObject();
            preFill.put("email", SharedPref.getVal(SubscriptionPaymentModeActivity.this, SharedPref.email_id));
            preFill.put("contact", SharedPref.getVal(SubscriptionPaymentModeActivity.this, SharedPref.mobile_no));
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
            subscribed();
//            new DownloadJSON2().execute();
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
