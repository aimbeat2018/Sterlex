package com.sterlex.in.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.text.HtmlCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.gmail.samehadar.iosdialog.IOSDialog;
import com.google.android.material.snackbar.Snackbar;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.sterlex.in.Constant.APIURLs;
import com.sterlex.in.Constant.FunctionConstant;
import com.sterlex.in.Constant.SharedPref;
import com.sterlex.in.Model.ProductModel;
import com.sterlex.in.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductDetailsActivity extends AppCompatActivity {

    RecyclerView rec_product, rec_package;
    NestedScrollView scrollView;
    CardView card_toolbar;
    Button btn_cart, btn_saveLater;
    TextView txt_productName, txt_weight, txt_desc, txt_stock, txt_cart_count, txt_discountprice, txt_actualprice, txt_discount,
            txt_bottomcount, txt_fact, txt_benefits;
    RelativeLayout rel_addcart;
    ImageView img_back, img_product;
    LinearLayout lnr_package, lnr_nutrition, lnr_benefits, lnr_main;
    RelativeLayout rel_cart_count, rel_toolbarcart, rel_discount, rel_minus, rel_plus, rel_bottomcart;
    String prod_id = "";
    IOSDialog dialog;
    String productImage = "", product_id = "",save_id = "", product_name = "", flag = "", description = "", unit = "", price = "", qty = "", gst = "", discount = "",
            save_flag = "", nutrition_fact = "", health_benefit = "", type = "";
    ArrayList<ProductModel> pricemodelArrayList = new ArrayList<>();
    ArrayList<String> stringArrayList = new ArrayList<>();
    Double actualAmout;
    Double discountOff;
    Double payableAmt;
    int quantity = 1;
    ShimmerFrameLayout shimmer_view_container;
    String cost_id = "";
    String image_send = "";
    RelativeLayout rel_cart, rel_cart_no_items;
    TextView txt_cart_no_of_items, cart_txt_amount;
    LikeButton btn_like;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        init();
        cost_id = SharedPref.getVal(ProductDetailsActivity.this, SharedPref.customer_id);
        prod_id = getIntent().getStringExtra("prod_id");
        onClick();
        getProductDetails();
        cartCount();

    }

    private void onClick() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_like.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                addToWishList(product_id, btn_like);
            }
            @Override
            public void unLiked(LikeButton likeButton) {
                removeWishList(save_id, btn_like);
            }
        });

        rel_toolbarcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cost_id.equals("")) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductDetailsActivity.this);
                    // Setting Dialog Title
                    alertDialog.setTitle("Please Login");
                    // Setting Dialog Message
                    alertDialog.setMessage("Please login to add product in cart");
                    // On pressing Settings button
                    alertDialog.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(ProductDetailsActivity.this, LoginActivity.class);
                            intent.putExtra("from", "product_details");
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
                    startActivity(new Intent(ProductDetailsActivity.this, CartActivity.class));
                }
            }
        });

        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cost_id.equals("")) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductDetailsActivity.this);
                    // Setting Dialog Title
                    alertDialog.setTitle("Please Login");
                    // Setting Dialog Message
                    alertDialog.setMessage("Please login to add product in cart");
                    // On pressing Settings button
                    alertDialog.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(ProductDetailsActivity.this, LoginActivity.class);
                            intent.putExtra("from", "product_details");
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
                    if (flag.equals("1")) {
                        rel_bottomcart.setVisibility(View.VISIBLE);
                        btn_cart.setVisibility(View.GONE);
                        addCart(product_id, unit, price, gst, discount, String.valueOf(quantity));
                    } else {
                        Toast.makeText(ProductDetailsActivity.this, "out of stock", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        rel_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cost_id.equals("")) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductDetailsActivity.this);
                    // Setting Dialog Title
                    alertDialog.setTitle("Please Login");
                    // Setting Dialog Message
                    alertDialog.setMessage("Please login to view cart");
                    // On pressing Settings button
                    alertDialog.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(ProductDetailsActivity.this, LoginActivity.class);
                            intent.putExtra("from", "home");
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
                    startActivity(new Intent(ProductDetailsActivity.this, CartActivity2.class));
                }
            }
        });


        rel_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag.equals("1")) {
                    if (quantity >= 1) {
                        quantity--;
                        if (quantity != 0) {
                            txt_bottomcount.setText(String.valueOf(quantity));
                            addCart(product_id, unit, price, gst, discount, String.
                                    valueOf(quantity));
                        } else {
                            rel_bottomcart.setVisibility(View.GONE);
                            btn_cart.setVisibility(View.VISIBLE);
                            addCart(product_id, unit, price, gst, discount, String.valueOf(quantity));
                        }
                    }
                } else {
                    Toast.makeText(ProductDetailsActivity.this, "out of stock", Toast.LENGTH_SHORT).show();
                }

            }
        });

        rel_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag.equals("1")) {
                    quantity++;
                    txt_bottomcount.setText(String.valueOf(quantity));
                    addCart(product_id, unit, price, gst, discount, String.valueOf(quantity));
                } else {
                    Toast.makeText(ProductDetailsActivity.this, "out of stock", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void init() {
        dialog = new IOSDialog.Builder(ProductDetailsActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();
        scrollView = findViewById(R.id.scrollView);
        shimmer_view_container = findViewById(R.id.shimmer_view_container);

        btn_like = findViewById(R.id.btn_like);
        rel_cart = findViewById(R.id.rel_cart);
        rel_cart_no_items = findViewById(R.id.rel_cart_no_items);
        txt_cart_no_of_items = findViewById(R.id.txt_cart_no_of_items);
        cart_txt_amount = findViewById(R.id.cart_txt_amount);
        img_back = findViewById(R.id.img_back);
        txt_productName = findViewById(R.id.txt_productName);
        rel_cart_count = findViewById(R.id.rel_cart_count);
        rel_toolbarcart = findViewById(R.id.rel_toolbarcart);
        txt_weight = findViewById(R.id.txt_weight);
        /*txt_price = findViewById(R.id.txt_price);*/
        txt_desc = findViewById(R.id.txt_desc);
        txt_bottomcount = findViewById(R.id.txt_bottomcount);
        txt_fact = findViewById(R.id.txt_fact);
        txt_benefits = findViewById(R.id.txt_benefits);
        rel_bottomcart = findViewById(R.id.rel_bottomcart);
        txt_stock = findViewById(R.id.txt_stock);
        txt_cart_count = findViewById(R.id.txt_cart_count);
        img_product = findViewById(R.id.img_product);
//        card_toolbar = findViewById(R.id.card_toolbar);
        txt_discountprice = findViewById(R.id.txt_discountprice);
        txt_actualprice = findViewById(R.id.txt_actualprice);
        btn_cart = findViewById(R.id.btn_cart);
        btn_saveLater = findViewById(R.id.btn_saveLater);
        lnr_package = findViewById(R.id.lnr_package);
        lnr_nutrition = findViewById(R.id.lnr_nutrition);
        lnr_benefits = findViewById(R.id.lnr_benefits);
        lnr_main = findViewById(R.id.lnr_main);
        rel_minus = findViewById(R.id.rel_minus);
        rel_plus = findViewById(R.id.rel_plus);
        rec_product = findViewById(R.id.rec_product);
        txt_discount = findViewById(R.id.txt_discount);
        rel_discount = findViewById(R.id.rel_discount);
        rel_addcart = findViewById(R.id.rel_addcart);
        rec_product.setLayoutManager(new LinearLayoutManager(ProductDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rec_package = findViewById(R.id.rec_package);
        rec_package.setLayoutManager(new GridLayoutManager(ProductDetailsActivity.this, 3));

        txt_actualprice.setPaintFlags(txt_actualprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

    }

    @Override
    protected void onResume() {
        super.onResume();
        shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        lnr_main.setVisibility(View.GONE);
        cartCount();
        getProductDetails();
    }


    /*Add to wishlist*/
    public void addToWishList(final String prod_id, final LikeButton btn) {
//        rel_progress.setVisibility(View.VISIBLE);
        if (APIURLs.isNetwork(ProductDetailsActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.add_to_wishlist, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject1 = new JSONObject(response);
                        JSONArray jsonArray = jsonObject1.getJSONArray("result");
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String status = jsonObject.getString("status");
                        String msg = jsonObject.getString("msg");
                        if (status.equals("1")) {
                            Snackbar.make(findViewById(android.R.id.content), "Product added to wish list", Snackbar.LENGTH_LONG).show();
                            btn.setLiked(true);
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("customer_id", SharedPref.getVal(ProductDetailsActivity.this, SharedPref.customer_id));
                    params.put("product_id", prod_id);
                    params.put("unit", unit);
                    params.put("price", price);
                    params.put("discount", discount);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(ProductDetailsActivity.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(ProductDetailsActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    /*Remove from wishlist*/
    public void removeWishList(final String product_id, final LikeButton btn) {
        if (APIURLs.isNetwork(ProductDetailsActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.remove_from_wishlist, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject1 = new JSONObject(response);
                        JSONArray jsonArray = jsonObject1.getJSONArray("result");
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            Snackbar.make(findViewById(android.R.id.content), "Product removed from wish list", Snackbar.LENGTH_LONG).show();
                            btn.setLiked(false);
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), "Error while removing product", Snackbar.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("save_id", product_id);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(ProductDetailsActivity.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(ProductDetailsActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        shimmer_view_container.stopShimmerAnimation();
        shimmer_view_container.setVisibility(View.GONE);
        lnr_main.setVisibility(View.VISIBLE);
    }


    public void addCart(final String prod_id, final String unit, final String price, final String gst, final String discount, final String qty1) {
        if (APIURLs.isNetwork(ProductDetailsActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.ADDCART_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        String msg = jsonObject1.getString("msg");
                        if (status.equals("1")) {
                            txt_cart_no_of_items.setText(qty1);

//                            quantity = 1;
                            cartCount();
                        } else {
                            Toast.makeText(ProductDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                    params.put("customer_id", SharedPref.getVal(ProductDetailsActivity.this, SharedPref.customer_id));
                    params.put("product_id", prod_id);
                    params.put("unit", unit);
                    params.put("price", price);
                    params.put("discount", discount);
                    params.put("qty", qty1);
                    params.put("gst", "");
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(ProductDetailsActivity.this, "no internet connection");
        }
    }

    public void saveLater(final String prod_id, final String unit, final String price, final String gst, final String discount, final String qty1) {
        if (APIURLs.isNetwork(ProductDetailsActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.SAVEFORLATER_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        String msg = jsonObject1.getString("msg");
                        if (status.equals("1")) {
                            Toast.makeText(ProductDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                            quantity = 1;
                            cartCount();
                        } else {
                            Toast.makeText(ProductDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                    params.put("customer_id", SharedPref.getVal(ProductDetailsActivity.this, SharedPref.customer_id));
                    params.put("product_id", prod_id);
                    params.put("unit", unit);
                    params.put("price", price);
                    params.put("gst", "");
                    params.put("discount", discount);
                    params.put("qty", qty1);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(ProductDetailsActivity.this, "no internet connection");
        }
    }

    public void SuccessToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.sucess_toast_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        TextView txt_message = (TextView) layout.findViewById(R.id.txt_message);
        txt_message.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void ErrorToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.error_toast_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        TextView txt_message = (TextView) layout.findViewById(R.id.txt_message);
        txt_message.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 50);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void dismissDialog() {
        /*if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }*/
        shimmer_view_container.stopShimmerAnimation();
        shimmer_view_container.setVisibility(View.GONE);
        lnr_main.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }*/
        shimmer_view_container.stopShimmerAnimation();
        shimmer_view_container.setVisibility(View.GONE);
        lnr_main.setVisibility(View.VISIBLE);
    }

    public void getProductDetails() {
//        dialog.show();
        shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        lnr_main.setVisibility(View.GONE);

        if (APIURLs.isNetwork(ProductDetailsActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.PRODUCTList_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            productImage = jsonObject1.getString("image");
                            product_id = jsonObject1.getString("product_id");
                            product_name = jsonObject1.getString("product_name");
                            flag = jsonObject1.getString("flag");
                            description = jsonObject1.getString("description");
                            qty = jsonObject1.getString("qty");
                            save_flag = jsonObject1.getString("save_flag");
                            nutrition_fact = jsonObject1.getString("nutrition_fact");
                            health_benefit = jsonObject1.getString("health_benefit");
                            health_benefit = jsonObject1.getString("health_benefit");
                            type = jsonObject1.getString("type");

                            if (nutrition_fact.equals("")) {
                                lnr_nutrition.setVisibility(View.GONE);
                            } else {
                                lnr_nutrition.setVisibility(View.VISIBLE);
                                txt_fact.setText(HtmlCompat.fromHtml(nutrition_fact, HtmlCompat.FROM_HTML_MODE_COMPACT));
                            }
                            if (health_benefit.equals("")) {
                                lnr_benefits.setVisibility(View.GONE);
                            } else {
                                lnr_benefits.setVisibility(View.VISIBLE);
                                txt_benefits.setText(HtmlCompat.fromHtml(health_benefit, HtmlCompat.FROM_HTML_MODE_COMPACT));
                            }
                            txt_productName.setText(product_name);
                            txt_desc.setText(HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT));

//                            if(type.equals("1")){
//                                rel_addcart.setVisibility(View.GONE);
//                            }else{
//                                rel_addcart.setVisibility(View.VISIBLE);
//                            }

                            Glide.with(ProductDetailsActivity.this).load(productImage).error(R.drawable.loader)
                                    .placeholder(R.drawable.loader).into(img_product);

                            if (flag.equals("0")) {
                                txt_stock.setVisibility(View.VISIBLE);
                                txt_stock.setText("Out of stock");
                                txt_stock.setTextColor(getResources().getColor(R.color.red));
                            } else if (flag.equals("1")) {
                                txt_stock.setVisibility(View.VISIBLE);
                                txt_stock.setText("Available in stock");
                                txt_stock.setTextColor(getResources().getColor(R.color.colorGreen));
                            }
                            if (save_flag.equals("0")) {
                                btn_saveLater.setText("Save for later");
                            } else if (save_flag.equals("1")) {
                                btn_saveLater.setText("Saved");
                            }

                            btn_saveLater.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (save_flag.equals("0")) {
                                        saveLater(product_id, unit, price, gst, discount, String.valueOf(quantity));
                                    } else if (save_flag.equals("1")) {
                                        /*btn_saveLater.setText("Saved");*/
                                    }

                                }
                            });
                            if (qty.equals("0")) {
                                btn_cart.setVisibility(View.VISIBLE);
                                rel_bottomcart.setVisibility(View.GONE);
                            } else {
                                btn_cart.setVisibility(View.GONE);
                                rel_bottomcart.setVisibility(View.VISIBLE);
                                quantity = Integer.parseInt(qty);
                                txt_bottomcount.setText(String.valueOf(quantity));
                            }
                            JSONArray jsonArray1 = jsonObject1.getJSONArray("price_details");
                            if (jsonArray1.length() > 0) {
                                JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
                                unit = jsonObject2.getString("unit");
                                price = jsonObject2.getString("price");
                                discount = jsonObject2.getString("discount");
                                save_id = jsonObject2.getString("save_id");

                                if(!save_id.equals("")){
                                    btn_like.setLiked(true);
                                }
                                if (discount.equals("0")) {
                                    txt_discountprice.setText("Rs." + price);
                                    txt_actualprice.setVisibility(View.GONE);
                                    rel_discount.setVisibility(View.GONE);
                                } else {
                                    txt_actualprice.setVisibility(View.VISIBLE);
                                    rel_discount.setVisibility(View.VISIBLE);
                                    actualAmout = Double.parseDouble(price);
                                    /*discountOff = (actualAmout * Double.parseDouble(discount)) / 100;
                                    payableAmt = actualAmout - discountOff;
                                    DecimalFormat twoDForm = new DecimalFormat("#");*/
                                    payableAmt = actualAmout - Double.parseDouble(discount);
                                    long price_amount = Math.round(Double.valueOf(payableAmt));
                                    txt_discountprice.setText("Rs." + price_amount);
                                    txt_actualprice.setText("Rs." + price);
//                                    txt_discount.setText(discount + "% off");
                                    txt_discount.setText("Save\n\u20B9" + discount);

                                }

                                /*txt_price.setText(price + "/-");*/
                                txt_weight.setText(unit);
                                if (jsonArray1.length() > 1) {
                                    pricemodelArrayList = new ArrayList<>();
                                    for (int i = 0; i < jsonArray1.length(); i++) {
                                        JSONObject jsonObject3 = jsonArray1.getJSONObject(i);
                                        ProductModel model = new ProductModel();
                                        model.setPrice(jsonObject3.getString("price"));
                                        model.setUnit(jsonObject3.getString("unit"));
                                        model.setDiscount(jsonObject3.getString("discount"));
                                        pricemodelArrayList.add(model);
                                    }
                                    ProductpackageAdapter adapter = new ProductpackageAdapter();
                                    rec_package.setAdapter(adapter);
                                    lnr_package.setVisibility(View.VISIBLE);
                                } else {
                                    lnr_package.setVisibility(View.GONE);
                                }
                            }

                            JSONArray jsonArray2 = jsonObject1.getJSONArray("thumbnail");
                            if (jsonArray2.length() > 0) {
                                stringArrayList = new ArrayList<>();
//                                stringArrayList.add(productImage);
                                for (int i = 0; i < jsonArray2.length(); i++) {
                                    JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                                    String image = jsonObject2.getString("thumbnail_image");
                                    stringArrayList.add(image);
                                }
                                ProductSliderAdapter sliderAdapter = new ProductSliderAdapter();
                                rec_product.setAdapter(sliderAdapter);
                                rec_product.setVisibility(View.VISIBLE);
                            } else {
                                rec_product.setVisibility(View.GONE);
                                image_send = productImage;
                                img_product.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(ProductDetailsActivity.this, FullImageActivity.class);
                                        intent.putExtra("image", image_send);
                                        intent.putExtra("position", "0");
                                        intent.putExtra("from", "single");
                                        startActivity(intent);
                                    }
                                });
                            }

                        } else {
                            Toast.makeText(ProductDetailsActivity.this, "error", Toast.LENGTH_SHORT).show();
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
                    params.put("product_id", prod_id);
                    params.put("customer_id", SharedPref.getVal(ProductDetailsActivity.this, SharedPref.customer_id));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            final Dialog dialog = new Dialog(ProductDetailsActivity.this);
            dialog.setContentView(R.layout.no_internet_dialog);
            Button button = dialog.findViewById(R.id.btn_process);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getProductDetails();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }


    public void cartCount() {
        if (APIURLs.isNetwork(ProductDetailsActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.CARTCOUNT_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        if (status.equals("1")) {
                             String qty = jsonObject1.getString("total_qty");
                            String price = jsonObject1.getString("total_price");
                            if (price.equals("0")) {
                                cart_txt_amount.setVisibility(View.GONE);
                            } else {
                                cart_txt_amount.setVisibility(View.VISIBLE);
                                long price_amount = Math.round(Double.valueOf(price));
                                cart_txt_amount.setText("\u20B9" + price_amount);
                            }
                            if (qty.equals("0")) {
                                txt_cart_no_of_items.setVisibility(View.GONE);
                                rel_cart_no_items.setVisibility(View.GONE);
                            } else {
                                rel_cart_no_items.setVisibility(View.VISIBLE);
                                txt_cart_no_of_items.setVisibility(View.VISIBLE);
                                txt_cart_no_of_items.setText(qty);
                            }
                        } else {
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
                    if(cost_id.equals("")){
                        params.put("customer_id","");
                    }else {
                        params.put("customer_id", SharedPref.getVal(ProductDetailsActivity.this, SharedPref.customer_id));
                    }return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();

//            final Dialog dialog = new Dialog(ProductDetailsActivity.this);
//            dialog.setContentView(R.layout.no_internet_dialog);
//            Button button = dialog.findViewById(R.id.btn_process);
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    cartCount();
//                    dialog.dismiss();
//                }
//            });
//            dialog.show();
        }
    }


    public void showUserMenuDialog(View view) {
        final Dialog dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.usermenu_dialog_layout);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog1.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog1.getWindow().setGravity(Gravity.TOP);
        dialog1.getWindow().setDimAmount((float) 0.7);
        dialog1.setCancelable(true);
        dialog1.setCanceledOnTouchOutside(true);

        TextView txt_sign_in = dialog1.findViewById(R.id.txt_sign_in);
        TextView txt_register = dialog1.findViewById(R.id.txt_register);

        txt_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductDetailsActivity.this, LoginActivity.class));
            }
        });

        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductDetailsActivity.this, OtpActivity.class));
            }
        });
        dialog1.show();
    }


    public class ProductSliderAdapter extends RecyclerView.Adapter<ProductSliderAdapter.ViewHolder> {
        int pos = 0;

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ProductDetailsActivity.this).inflate(R.layout.product_slider_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

            Glide.with(ProductDetailsActivity.this).load(stringArrayList.get(position)).into(holder.img_product1);

            holder.img_product1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos = position;
                    Glide.with(ProductDetailsActivity.this).load(stringArrayList.get(position)).into(img_product);
                }
            });

            img_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    image_send = stringArrayList.get(pos);
                    Intent intent = new Intent(ProductDetailsActivity.this, FullImageActivity.class);
                    intent.putExtra("arraylist", stringArrayList);
                    intent.putExtra("position", "0");
                    intent.putExtra("from", "multiple");
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return stringArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_product1;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                img_product1 = itemView.findViewById(R.id.img_product);
            }
        }
    }

    public class ProductpackageAdapter extends RecyclerView.Adapter<ProductpackageAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ProductDetailsActivity.this).inflate(R.layout.product_package_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ProductModel model = pricemodelArrayList.get(position);
            ((ViewHolder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return pricemodelArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_product1;
            TextView txt_productName1, txt_price1;
            CardView card_list;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                img_product1 = itemView.findViewById(R.id.img_product);
                txt_productName1 = itemView.findViewById(R.id.txt_productName);
                txt_price1 = itemView.findViewById(R.id.txt_price);
                card_list = itemView.findViewById(R.id.card_list);
            }

            public void bind(final ProductModel model, int position) {
                if (model.getDiscount().equals("0")) {
                    txt_productName1.setText(model.getUnit());
                    txt_price1.setText("Rs." + model.getPrice());
                } else {
                    actualAmout = Double.parseDouble(model.getPrice());
                               /* discountOff = (actualAmout * Double.parseDouble(model.getDiscount())) / 100;
                                payableAmt = actualAmout - discountOff;*/
                    payableAmt = actualAmout - Double.parseDouble(discount);

//                    txt_discountprice.setText("Rs." + String.valueOf(payableAmt));
//                    txt_actualprice.setText("Rs." + model.getPrice());
////                                txt_discount.setText(model.getDiscount() + "% off");
//                    txt_discount.setText("Save\n\u20B9" + model.getDiscount());
                    txt_productName1.setText(model.getUnit());
                    long price_amount = Math.round(Double.valueOf(payableAmt));

                    txt_price1.setText("Rs." + price_amount);
                }

                Glide.with(ProductDetailsActivity.this).load(productImage).into(img_product1);

                card_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (model.getDiscount().equals("0")) {
                                txt_discountprice.setText("Rs." + model.getPrice());
                                unit = model.getUnit();
                                price = model.getPrice();
                                txt_weight.setText(model.getUnit());
                                txt_actualprice.setVisibility(View.GONE);
                                rel_discount.setVisibility(View.GONE);
                            } else {
                                txt_actualprice.setVisibility(View.VISIBLE);
                                rel_discount.setVisibility(View.VISIBLE);
                                txt_weight.setText(model.getUnit());
                                actualAmout = Double.parseDouble(model.getPrice());
                               /* discountOff = (actualAmout * Double.parseDouble(model.getDiscount())) / 100;
                                payableAmt = actualAmout - discountOff;*/
                                payableAmt = actualAmout - Double.parseDouble(discount);
                                unit = model.getUnit();
                                price = model.getPrice();
                                long price_amount = Math.round(Double.valueOf(payableAmt));
                                txt_discountprice.setText("Rs." + price_amount);
                                txt_actualprice.setText("Rs." + model.getPrice());
//                                txt_discount.setText(model.getDiscount() + "% off");
                                txt_discount.setText("Save\n\u20B9" + model.getDiscount());

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                       /* txt_price.setText(model.getPrice()+"/-");
                        txt_weight.setText(model.getUnit());*/
                    }
                });
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
