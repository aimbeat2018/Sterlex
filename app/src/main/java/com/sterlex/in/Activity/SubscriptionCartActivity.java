package com.sterlex.in.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.bumptech.glide.Glide;
import com.sterlex.in.Constant.APIURLs;
import com.sterlex.in.Constant.FunctionConstant;
import com.sterlex.in.Constant.SharedPref;
import com.sterlex.in.Model.ProductModel;
import com.sterlex.in.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.gmail.samehadar.iosdialog.IOSDialog;
import com.razorpay.Checkout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubscriptionCartActivity extends AppCompatActivity {

    RecyclerView rec_cartList;
    ImageView img_back;
    TextView txt_unitPrice, txt_discountCharges, txt_couponCharges, txt_subTotal, txt_grandTotal, txt_TotalSaving,
            txt_deliveryAddress, txt_mincart, txt_couponCode, txt_days;
    LinearLayout lnr_coupon, lnr_totalsaving, lnr_discount;
    CardView card_cash, card_online;
    ImageView img_cashSelect, img_onlineSelect, img_remove;
    Button btn_pay;
    String product_id = "", from_date = "", to_date = "", unit_price = "", qty = "", address = "", total_bag = "", bag_discount = "",
            order_total = "", total_day = "", subscribe_mode = "", day = "", unit = "", discount = "";
    IOSDialog dialog;
    String paymentFlag = "", miniCart = "";
    double minCart = 0.0;
    private final int COUPON_USED = 1;
    String coupon_name = "", coupon_id = "", coupon_value = "", capping_value = "", total_product_amount = "", coupon_amt = "";
    String razorpayPaymentIDMain, rid, order_id, payamt;
    String ridd = "NA";
    int capt = 1;
    static int min, max, create_otp;
    ShimmerFrameLayout shimmer_view_container;
    LinearLayout lnr_main;
    ArrayList<ProductModel> productModelArrayList = new ArrayList<>();
    String cost_id = "", price = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_cart);
        init();
        cost_id = SharedPref.getVal(SubscriptionCartActivity.this, SharedPref.customer_id);
        onClick();
    }

    private void getIntentData() {

        product_id = getIntent().getStringExtra("product_id");
        from_date = getIntent().getStringExtra("from_date");
        to_date = getIntent().getStringExtra("to_date");
        subscribe_mode = getIntent().getStringExtra("subscribe_mode");
        day = getIntent().getStringExtra("day");
        unit = getIntent().getStringExtra("unit");
        discount = getIntent().getStringExtra("discount");
        unit_price = getIntent().getStringExtra("unit_price");
        qty = getIntent().getStringExtra("qty");
        address = getIntent().getStringExtra("address");

        subscriptionCalculation();
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
                if (cost_id.equals("")) {
                    Intent intent = new Intent(SubscriptionCartActivity.this, LoginActivity.class);
                    intent.putExtra("from", "subs_cart");
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SubscriptionCartActivity.this, SubscriptionAddressActivity.class);
                    if (lnr_coupon.getVisibility() == View.VISIBLE) {
                        coupon_amt = txt_couponCharges.getText().toString();
                        coupon_amt = coupon_amt.substring(1, coupon_amt.length());
                    } else {
                        coupon_amt = "";
                    }

                    price = price.substring(0,price.length() - 1);

                    intent.putExtra("from_date", from_date);
                    intent.putExtra("to_date", to_date);
                    intent.putExtra("subscribe_mode", subscribe_mode);
                    intent.putExtra("product_id", product_id);
                    intent.putExtra("qty", qty);
                    intent.putExtra("day", day);
                    intent.putExtra("unit_price", txt_unitPrice.getText().toString());
                    intent.putExtra("unit", unit);
                    intent.putExtra("discount", discount);
                    intent.putExtra("coupon_id", coupon_id);
                    intent.putExtra("total_bag", txt_grandTotal.getText().toString());
                    intent.putExtra("subscription_days", txt_days.getText().toString());
                    intent.putExtra("bag_discount", bag_discount);
                    intent.putExtra("coupon_amt", coupon_amt);
                    intent.putExtra("price", price);
                    startActivity(intent);
                    finish();
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


    public void useCoupon(View view) {
        if (cost_id.equals("")) {
            Intent intent = new Intent(SubscriptionCartActivity.this, LoginActivity.class);
            intent.putExtra("from", "subs_cart");
            startActivity(intent);
        } else {
            Intent intent = new Intent(SubscriptionCartActivity.this, SubscriptionCouponListActivity.class);
            startActivityForResult(intent, COUPON_USED);
        }
    }

    private void init() {
        dialog = new IOSDialog.Builder(SubscriptionCartActivity.this)
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
        txt_days = findViewById(R.id.txt_days);
        lnr_coupon = findViewById(R.id.lnr_coupon);
        lnr_totalsaving = findViewById(R.id.lnr_totalsaving);
        lnr_discount = findViewById(R.id.lnr_discount);
        card_cash = findViewById(R.id.card_cash);
        card_online = findViewById(R.id.card_online);
        img_cashSelect = findViewById(R.id.img_cashSelect);
        img_onlineSelect = findViewById(R.id.img_onlineSelect);
        btn_pay = findViewById(R.id.btn_pay);
        img_remove = findViewById(R.id.img_remove);
        rec_cartList = findViewById(R.id.rec_cartList);
        rec_cartList.setLayoutManager(new LinearLayoutManager(SubscriptionCartActivity.this));
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
        if (APIURLs.isNetwork(SubscriptionCartActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.my_subscription, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        if (status.equals("1")) {
                            Toast.makeText(SubscriptionCartActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SubscriptionCartActivity.this, OrderPlacedActivity.class);
                            intent.putExtra("OrderId", "");
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SubscriptionCartActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
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
                    params.put("customer_id", SharedPref.getVal(SubscriptionCartActivity.this, SharedPref.customer_id));

                    params.put("from_date", from_date);
                    params.put("to_date", to_date);
                    params.put("subscribe_mode", subscribe_mode);
                    params.put("product_id", product_id);
                    params.put("qty", qty);
                    params.put("day", day);
                    params.put("unit", unit);
                    params.put("address", address);
                    params.put("unit_price", unit_price);
                    params.put("discount", discount);
                    params.put("p_mode", paymentFlag);
                    params.put("order_total", order_total);
                    params.put("coupon_id", coupon_id);
                    params.put("rid", ridd);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(SubscriptionCartActivity.this, "no internet connection");
        }

    }

    public void subscriptionCalculation() {
//        dialog.show();
        shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        lnr_main.setVisibility(View.GONE);
        if (APIURLs.isNetwork(SubscriptionCartActivity.this)) {
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
                            total_day = jsonObject1.getString("total_day");

                            txt_unitPrice.setText("Rs." + total_bag);
                            if (bag_discount.equals("0")) {
//                                lnr_discount.setVisibility(View.GONE);
                                lnr_totalsaving.setVisibility(View.GONE);
                            } else {
//                                lnr_discount.setVisibility(View.VISIBLE);
                                lnr_totalsaving.setVisibility(View.VISIBLE);

//                                txt_discountCharges.setText("Rs." + bag_discount);
                                txt_TotalSaving.setText("Rs." + bag_discount);
                            }
                            txt_subTotal.setText("Rs." + order_total);
                            txt_grandTotal.setText("Rs." + order_total);
                            txt_days.setText(total_day);

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
                                        Toast.makeText(SubscriptionCartActivity.this, "coupon code not applicable", Toast.LENGTH_SHORT).show();
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

                            JSONArray jsonArray1 = jsonObject1.getJSONArray("product_list");
                            productModelArrayList = new ArrayList<>();
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                                ProductModel model = new ProductModel();
                                model.setProduct_id(jsonObject2.getString("product_id"));
                                model.setProduct_name(jsonObject2.getString("product_name"));
                                model.setUnit(jsonObject2.getString("unit"));
                                price += jsonObject2.getString("price") + ",";
                                model.setPrice(jsonObject2.getString("price"));
                                model.setDiscount(jsonObject2.getString("discount"));
                                model.setQty(jsonObject2.getString("qty"));
                                model.setImage(jsonObject2.getString("image"));
                                model.setTotal_amt(jsonObject2.getString("total_amt"));
                                productModelArrayList.add(model);
                            }
                            ProductAdapter adapter = new ProductAdapter(SubscriptionCartActivity.this);
                            rec_cartList.setAdapter(adapter);

                        } else {
                            Toast.makeText(SubscriptionCartActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                    params.put("from_date", from_date);
                    params.put("to_date", to_date);
                    params.put("subscribe_mode", subscribe_mode);
                    params.put("day", day);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(SubscriptionCartActivity.this, "no internet connection");
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
        if (APIURLs.isNetwork(SubscriptionCartActivity.this)) {
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
                            Toast.makeText(SubscriptionCartActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                    params.put("customer_id", SharedPref.getVal(SubscriptionCartActivity.this, SharedPref.customer_id));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            final Dialog dialog = new Dialog(SubscriptionCartActivity.this);
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
            options.put("name", SharedPref.getVal(SubscriptionCartActivity.this, SharedPref.customer_name));
            options.put("description", "711 Basket");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", payamt);


            JSONObject preFill = new JSONObject();
            preFill.put("email", SharedPref.getVal(SubscriptionCartActivity.this, SharedPref.email_id));
            preFill.put("contact", SharedPref.getVal(SubscriptionCartActivity.this, SharedPref.mobile_no));
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


    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
        Context context;
        int qty = 0;
        double total_product = 0.0;

        public ProductAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(SubscriptionCartActivity.this).inflate(R.layout.cart_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            ProductModel model = productModelArrayList.get(position);
            ((ViewHolder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return productModelArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_product;
            TextView txt_productName, txt_weight, txt_season, txt_actualprice, txt_stock, txt_discountprice, txt_discount, txt_count;
            Button btn_cart, btn_subscribe;
            LinearLayout lnr_product, lnr_remove;
            RelativeLayout rel_discount, rel_cart, rel_minus, rel_plus, rel_saveLater, rel_removeCart;
            CardView card_list;
            ProgressBar progressBar;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                img_product = itemView.findViewById(R.id.img_product);
                txt_productName = itemView.findViewById(R.id.txt_productName);
                txt_weight = itemView.findViewById(R.id.txt_weight);
                txt_season = itemView.findViewById(R.id.txt_season);
                txt_actualprice = itemView.findViewById(R.id.txt_actualprice);
                txt_discountprice = itemView.findViewById(R.id.txt_discountprice);
                txt_stock = itemView.findViewById(R.id.txt_stock);
                btn_cart = itemView.findViewById(R.id.btn_cart);
                btn_subscribe = itemView.findViewById(R.id.btn_subscribe);
                btn_subscribe.setVisibility(View.GONE);
                lnr_product = itemView.findViewById(R.id.lnr_product);
                lnr_remove = itemView.findViewById(R.id.lnr_remove);
                rel_saveLater = itemView.findViewById(R.id.rel_saveLater);
                rel_removeCart = itemView.findViewById(R.id.rel_removeCart);
                rel_discount = itemView.findViewById(R.id.rel_discount);
                rel_minus = itemView.findViewById(R.id.rel_minus);
                rel_plus = itemView.findViewById(R.id.rel_plus);
                rel_cart = itemView.findViewById(R.id.rel_cart);
                card_list = itemView.findViewById(R.id.card_list);
                txt_discount = itemView.findViewById(R.id.txt_discount);
                progressBar = itemView.findViewById(R.id.progressBar);
                txt_count = itemView.findViewById(R.id.txt_count);
                rel_cart.setVisibility(View.VISIBLE);
                btn_cart.setVisibility(View.GONE);
                rel_plus.setVisibility(View.GONE);
                rel_minus.setVisibility(View.GONE);
                txt_actualprice.setPaintFlags(txt_actualprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            public void bind(final ProductModel model, final int position) {
                Glide.with(SubscriptionCartActivity.this).load(model.getImage()).into(img_product);
                txt_productName.setText(model.getProduct_name());
                txt_weight.setText(model.getUnit());
                try {
                    if (model.getDiscount().equals("0")) {
                        txt_discountprice.setText("Rs." + model.getPrice());
                        txt_actualprice.setVisibility(View.GONE);
                        rel_discount.setVisibility(View.GONE);
                    }
                    /*else {
                        txt_actualprice.setVisibility(View.VISIBLE);
                        rel_discount.setVisibility(View.VISIBLE);
                        txt_discountprice.setText("Rs." + model.getDiscountPrice());
                        txt_actualprice.setText("Rs." + model.getPrice());
                        txt_discount.setText(model.getDiscount() + "% off");
                    }*/

                    /*product total calculation without multiple by days*/
                    total_product += Double.parseDouble(model.getTotal_amt());
                    txt_unitPrice.setText("Rs. " + String.valueOf(total_product));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (model.getQty().equals("0")) {
                    rel_cart.setVisibility(View.GONE);
                    btn_cart.setVisibility(View.VISIBLE);
                } else {
                    rel_cart.setVisibility(View.VISIBLE);
                    btn_cart.setVisibility(View.GONE);
                    qty = Integer.valueOf(model.getQty());
                    txt_count.setText(String.valueOf(qty));
                }

                btn_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rel_cart.setVisibility(View.VISIBLE);
                        btn_cart.setVisibility(View.GONE);
                    }
                });

                rel_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        qty = Integer.parseInt(txt_count.getText().toString());
                        if (qty >= 1) {
                            qty--;
                            if (qty != 0) {
                                txt_count.setText(String.valueOf(qty));
                                txt_count.setVisibility(View.GONE);
                                card_list.setVisibility(View.VISIBLE);
                               /* addCart(model.getProduct_id(), model.getUnit(), model.getUnit_price(), model.getDiscount(), txt_count.getText().toString());
                                for (int i = 0; i < qtyArrayList.size(); i++) {
                                    if (position == i) {
                                        qtyArrayList.set(i, String.valueOf(qty));
                                    }
                                }

                                if(unitArrayList.size()>0) {
                                    for (int i = 0; i < unitArrayList.size(); i++) {
                                        if (position == i) {
                                            unitArrayList.set(i, String.valueOf(model.getUnit()));
                                        }
                                    }
                                }

                                if(unitpriceArrayList.size()>0) {
                                    for (int i = 0; i < unitpriceArrayList.size(); i++) {
                                        if (position == i) {
                                            unitpriceArrayList.set(i, String.valueOf(model.getUnit_price()));
                                        }
                                    }
                                }

                                if(discountArrayList.size()>0) {
                                    for (int i = 0; i < discountArrayList.size(); i++) {
                                        if (position == i) {
                                            discountArrayList.set(i, String.valueOf(model.getDiscount()));
                                        }
                                    }
                                }*/
                            } else {
                                /*removeCart(model.getCart_id(), position);
                                for (int i = 0; i < qtyArrayList.size(); i++) {
                                    if (position == i) {
                                        qtyArrayList.set(i, String.valueOf(qty));
                                    }
                                }

                                if(unitArrayList.size()>0) {
                                    for (int i = 0; i < unitArrayList.size(); i++) {
                                        if (position == i) {
                                            unitArrayList.set(i, String.valueOf(model.getUnit()));
                                        }
                                    }
                                }

                                if(unitpriceArrayList.size()>0) {
                                    for (int i = 0; i < unitpriceArrayList.size(); i++) {
                                        if (position == i) {
                                            unitpriceArrayList.set(i, String.valueOf(model.getUnit_price()));
                                        }
                                    }
                                }

                                if(discountArrayList.size()>0) {
                                    for (int i = 0; i < discountArrayList.size(); i++) {
                                        if (position == i) {
                                            discountArrayList.set(i, String.valueOf(model.getDiscount()));
                                        }
                                    }
                                }*/
                            }
                        }
                    }
                });

                rel_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        qty = Integer.parseInt(txt_count.getText().toString());
                        qty++;
                        txt_count.setText(String.valueOf(qty));
                        txt_count.setVisibility(View.GONE);
                        /*addCart(model.getProduct_id(), model.getUnit(), model.getUnit_price(), model.getDiscount(), txt_count.getText().toString());

                        for (int i = 0; i < qtyArrayList.size(); i++) {
                            if (position == i) {
                                qtyArrayList.set(i, String.valueOf(qty));
                            }
                        }


                        if(unitArrayList.size()>0) {
                            for (int i = 0; i < unitArrayList.size(); i++) {
                                if (position == i) {
                                    unitArrayList.set(i, String.valueOf(model.getUnit()));
                                }
                            }
                        }

                        if(unitpriceArrayList.size()>0) {
                            for (int i = 0; i < unitpriceArrayList.size(); i++) {
                                if (position == i) {
                                    unitpriceArrayList.set(i, String.valueOf(model.getUnit_price()));
                                }
                            }
                        }

                        if(discountArrayList.size()>0) {
                            for (int i = 0; i < discountArrayList.size(); i++) {
                                if (position == i) {
                                    discountArrayList.set(i, String.valueOf(model.getDiscount()));
                                }
                            }
                        }*/
                    }
                });

//                qtyArrayList.add(txt_count.getText().toString());

                lnr_product.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(new Intent(SubscriptionCartActivity.this, ProductDetailsActivity.class));
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);

                    }
                });

                /*rel_removeCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(CartActivity.this)
                                .setTitle("Alert!!!")
                                .setMessage("Are u sure u want to remove item from cart?")

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Continue with delete operation
                                        dialog.dismiss();
                                        removeCart(model.getCart_id(), position);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                });

                rel_saveLater.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(CartActivity.this)
                                .setTitle("Alert!!!")
                                .setMessage("Are u sure?")

                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Continue with delete operation
                                        dialog.dismiss();
                                        saveLater(model.getProduct_id(), model.getUnit(), model.getUnit_price(), model.getDiscount(), txt_count.getText().toString(), position);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                });*/
            }

        }
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(SubscriptionCartActivity.this)
                .setTitle("Alert!!!")
                .setMessage("Are u sure u want leave? ")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        dialog.dismiss();
                        SubscriptionCartActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
