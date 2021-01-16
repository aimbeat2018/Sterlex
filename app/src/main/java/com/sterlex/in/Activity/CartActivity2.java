package com.sterlex.in.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.gmail.samehadar.iosdialog.IOSDialog;
import com.sterlex.in.Constant.APIURLs;
import com.sterlex.in.Constant.FunctionConstant;
import com.sterlex.in.Constant.OrderSharedPrefence;
import com.sterlex.in.Constant.SharedPref;
import com.sterlex.in.Model.CartModel;
import com.sterlex.in.Model.SlotModel;
import com.sterlex.in.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;

public class CartActivity2 extends AppCompatActivity {
    RecyclerView rec_cartList;
    RelativeLayout rel_checkout, rel_nodata, rel_list_data;
    ImageView img_arrow;
    ArrayList<CartModel> cartModels = new ArrayList<>();
    IOSDialog dialog;
    String cost_id = "";
    LinearLayout lin_parent, lin_min_limit;
    ShimmerFrameLayout shimmer_view_container;
    TextView txt_cart_no_of_items, txt_savings, txt_total_amt, txt_deliveryCharges, txt_mincart;
    String total_bag = "", bag_discount = "", order_total = "", total_number_of_items = "", delivery_charges = "", miniCart = "0", no_of_items = "";
    int flag_checkout = 0;
    Double grandTotaldouble;
    double minCart = 0.0;

    ArrayList<CartModel> productModelArrayList = new ArrayList<>();
    ArrayList<SlotModel> slotModelArrayList = new ArrayList<>();
    ArrayList<String> productIdArrayList = new ArrayList<>();
    ArrayList<String> qtyArrayList = new ArrayList<>();
    ArrayList<String> unitArrayList = new ArrayList<>();
    ArrayList<String> unitpriceArrayList = new ArrayList<>();
    ArrayList<String> discountArrayList = new ArrayList<>();

    String total_amount = "", total_discount_amount = "", sub_total = "", total_shipping_cost = "";
    private final int COUPON_USED = 1;
    String coupon_name = "", coupon_id = "", coupon_value = "", capping_value = "";
    String product_id = "", discoun = "";
    String finalQty = "", finalUnit = "", finalUnitPrice = "", finalDiscount = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart2);

        cost_id = SharedPref.getVal(CartActivity2.this, SharedPref.customer_id);

        init();
        onCLick();
//        minimuncart();
//        cartCount();

    }

    public void goToHome(View view) {

        startActivity(new Intent(CartActivity2.this, HomeActivity2.class));
        finish();

    }

    private void onCLick() {
        rel_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String total = txt_total_amt.getText().toString();
                String sub_str_total = total.substring(13, total.length());


                String total_savings = txt_savings.getText().toString();
                String sub_str_savings = total_savings.substring(15, total_savings.length());
                Double minimum_cart = Double.valueOf(miniCart);
                Double total_cart = Double.valueOf(sub_str_total);
                if (total_cart >= minimum_cart) {

                    /*Comma Separated Product Id*/
                    for (int i = 0; i < productIdArrayList.size(); i++) {
                        product_id += productIdArrayList.get(i) + ",";
                    }
                    product_id = product_id.substring(0, product_id.length() - 1);

                    /*Comma Separated Qty*/
                    for (int i = 0; i < qtyArrayList.size(); i++) {
                        finalQty += qtyArrayList.get(i) + ",";
                    }
                    finalQty = finalQty.substring(0, finalQty.length() - 1);

                    /*Comma Separated Unit*/
                    for (int i = 0; i < unitArrayList.size(); i++) {
                        finalUnit += unitArrayList.get(i) + ",";
                    }
                    finalUnit = finalUnit.substring(0, finalUnit.length() - 1);

                    /*Comma Separated Unit Price*/
                    for (int i = 0; i < unitpriceArrayList.size(); i++) {
                        finalUnitPrice += unitpriceArrayList.get(i) + ",";
                    }
                    finalUnitPrice = finalUnitPrice.substring(0, finalUnitPrice.length() - 1);

                    /*Comma Separated Discount*/
                    for (int i = 0; i < discountArrayList.size(); i++) {
                        finalDiscount += discountArrayList.get(i) + ",";
                    }
                    finalDiscount = finalDiscount.substring(0, finalDiscount.length() - 1);


                    OrderSharedPrefence.putVal(CartActivity2.this, OrderSharedPrefence.product_id, product_id);
//                OrderSharedPrefence.putVal(CartActivity2.this, OrderSharedPrefence.delivery_charges, txt_delivery_fees.getText().toString());
//                OrderSharedPrefence.putVal(CartActivity2.this, OrderSharedPrefence.coupon_id, coupon_id);
//                OrderSharedPrefence.putVal(CartActivity2.this, OrderSharedPrefence.delivery_status, "1");
                    OrderSharedPrefence.putVal(CartActivity2.this, OrderSharedPrefence.total_bag, sub_str_total);
//                OrderSharedPrefence.putVal(CartActivity2.this, OrderSharedPrefence.order_total, txt_total_amt.getText().toString());
                    OrderSharedPrefence.putVal(CartActivity2.this, OrderSharedPrefence.total_discount, sub_str_savings);
                    OrderSharedPrefence.putVal(CartActivity2.this, OrderSharedPrefence.no_of_items, txt_cart_no_of_items.getText().toString());

                    OrderSharedPrefence.putVal(CartActivity2.this, OrderSharedPrefence.unit, finalUnit);
                    OrderSharedPrefence.putVal(CartActivity2.this, OrderSharedPrefence.unit_price, finalUnitPrice);
                    OrderSharedPrefence.putVal(CartActivity2.this, OrderSharedPrefence.qty, finalQty);
                    OrderSharedPrefence.putVal(CartActivity2.this, OrderSharedPrefence.discount, finalDiscount);
                    /*OrderSharedPrefence.putVal(CartActivity2.this, OrderSharedPrefence.order_total, txt_total_amnt.getText().toString());*/
//                if (coupon_amt.equals("Apply Coupon")) {
//                    OrderSharedPrefence.putVal(CartActivity2.this, OrderSharedPrefence.coupon_amt, "0");
//                } else {
//                    OrderSharedPrefence.putVal(CartActivity2.this, OrderSharedPrefence.coupon_amt, coupon_amt);
//                }
                    OrderSharedPrefence.putVal(CartActivity2.this, OrderSharedPrefence.discount_amt, txt_savings.getText().toString());
                    Intent intent = new Intent(CartActivity2.this, DeliveryTypeActivity.class);
              /*  intent.putExtra("product_id", product_id);
                intent.putExtra("grand_total", txt_total_amnt.getText().toString());
//                intent.putExtra("sub_total", txt_subTotal.getText().toString());
                intent.putExtra("coupon_id", coupon_id);
//                intent.putExtra("slot_id", selectedSlotId);
                intent.putExtra("unit_price", txt_total_price.getText().toString());
                intent.putExtra("discount_amt", discount_amt);
                intent.putExtra("coupon_amt", coupon_amt);
                intent.putExtra("qty", finalQty);
                intent.putExtra("finalUnit", finalUnit);
                intent.putExtra("finalUnitPrice", finalUnitPrice);
                intent.putExtra("finalDiscount", finalDiscount);
                intent.putExtra("delivery_charges", txt_delivery_fees.getText().toString());
                        *//*intent.putExtra("walletBalance", String.valueOf(walletdouble));
                        intent.putExtra("actualBalance", payBalance);*/
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(CartActivity2.this, "Total amount should be greater than " + miniCart, Toast.LENGTH_SHORT).show();

                }

//                Intent intent = new Intent(CartActivity2.this, DeliveryTypeActivity.class);
//                intent.putExtra("total", total_bag);
//                intent.putExtra("del_charges", delivery_charges);
//                startActivity(intent);
            }
        });
        img_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void init() {
        rel_list_data = findViewById(R.id.rel_list_data);
        rel_nodata = findViewById(R.id.rel_nodata);
        lin_parent = findViewById(R.id.lnr_main);
        lin_min_limit = findViewById(R.id.lin_min_limit);
        txt_mincart = findViewById(R.id.txt_mincart);
        txt_cart_no_of_items = findViewById(R.id.txt_cart_no_of_items);
        txt_savings = findViewById(R.id.txt_savings);
        txt_total_amt = findViewById(R.id.txt_total_amt);
        txt_deliveryCharges = findViewById(R.id.txt_deliveryCharges);

        shimmer_view_container = findViewById(R.id.shimmer_view_container);

        dialog = new IOSDialog.Builder(CartActivity2.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();

        img_arrow = findViewById(R.id.img_arrow);
        rel_checkout = findViewById(R.id.rel_checkout);
        rec_cartList = findViewById(R.id.rec_cartList);
//        rec_cartList.setAdapter(new CartListingAdapter(CartActivity2.this));
        rec_cartList.setLayoutManager(new LinearLayoutManager(CartActivity2.this, RecyclerView.VERTICAL, false));

    }


    public void cartList() {
        if (APIURLs.isNetwork(CartActivity2.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.CARTPRODUCT_LIST_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            productModelArrayList = new ArrayList<>();
                            slotModelArrayList = new ArrayList<>();
                            productIdArrayList = new ArrayList<>();
                            qtyArrayList = new ArrayList<>();
                            unitArrayList = new ArrayList<>();
                            unitpriceArrayList = new ArrayList<>();
                            discountArrayList = new ArrayList<>();

                            cartModels = new ArrayList<>();
                            if (jsonObject.has("result")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    rel_list_data.setVisibility(View.VISIBLE);
                                    rel_nodata.setVisibility(GONE);
                                    JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                                    CartModel model = new CartModel();
                                    model.setProduct_id(jsonObject3.getString("product_id"));
                                    model.setProduct_name(jsonObject3.getString("product_name"));
                                    model.setImage(jsonObject3.getString("image"));
                                    model.setQty(jsonObject3.getString("qty"));
                                    model.setUnit(jsonObject3.getString("unit"));
                                    model.setUnit_price(jsonObject3.getString("unit_price"));
                                    model.setPrice(jsonObject3.getString("price"));
                                    model.setGst(jsonObject3.getString("gst"));
                                    model.setDiscountPrice(jsonObject3.getString("discount"));
                                    model.setDiscount(jsonObject3.getString("discount_amt"));
                                    model.setCart_id(jsonObject3.getString("cart_id"));
                                    cartModels.add(model);

                                }
                                CartListingAdapter adapter = new CartListingAdapter(CartActivity2.this);
                                rec_cartList.setAdapter(adapter);

                                JSONArray jsonArray1 = jsonObject.getJSONArray("bag_details");
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(0);


                                txt_cart_no_of_items.setText(cartModels.size() + " item(s) in cart");

                                String discount = jsonObject1.getString("bag_discount");

                                long discount_amount = Math.round(Double.valueOf(discount));
                                txt_savings.setText("TOTAL SAVINGS \u20B9" + discount_amount);
//                                double total_amt = 0.00, discount = 0.00, ogprice = 0.00;
//                                ogprice = Double.valueOf(jsonObject1.getString("total_bag"));
//                                discount = Double.valueOf(jsonObject1.getString("bag_discount"));
//                                total_amt = ogprice - discount;

//                                txt_total_amt.setText("TOTAL PRICE \u20B9" + total_amt);
                            }

                            if (jsonObject.has("bag_details")) {
                                JSONArray orderArray = jsonObject.getJSONArray("bag_details");
                                JSONObject orderObject1 = orderArray.getJSONObject(0);
                                total_bag = orderObject1.getString("total_bag");
                                bag_discount = orderObject1.getString("bag_discount");
                                order_total = orderObject1.getString("order_total");
                                total_number_of_items = orderObject1.getString("total_number_of_items");
                                delivery_charges = orderObject1.getString("delivery_charges");


                                if (bag_discount.equals("0")) {
                                    txt_savings.setVisibility(View.GONE);
                                } else {
//                                    lnr_totalsaving.setVisibility(View.VISIBLE);
                                    txt_savings.setText("TOTAL SAVINGS \u20B9" + bag_discount);

                                }

//                                if (delivery_charges.equals("0")) {
//                                    txt_deliveryCharges.setText("FREE");
//                                } else {
//                                    txt_deliveryCharges.setText("Delivery Charges  + \u20B9" + delivery_charges);
//                                }


//                                txt_total_amt.setText("Rs." + order_total);
                               /* try {
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
                                            Toast.makeText(CartActivity2.this, "coupon code not applicable", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        img_remove.setVisibility(View.GONE);
                                        lnr_coupon.setVisibility(View.GONE);
                                        txt_couponCode.setText("");
                                    }*/
//                                    walletdouble = Double.parseDouble(walletBalance);
                                   /* subTotaldouble = Double.parseDouble(order_total);
                                    grandTotaldouble = 0.0;*/

                                    /*if (subTotaldouble >= walletdouble) {
                                        grandTotaldouble = subTotaldouble - walletdouble;
                                        payBalance = String.valueOf(walletdouble);
                                        txt_walletCharges.setText("- " + payBalance);
                                        walletdouble = 0.0;
                                        payBalance = "";
                                    } else {
                                        walletdouble = walletdouble - subTotaldouble;
                                        payBalance = String.valueOf(subTotaldouble);
                                        txt_walletCharges.setText("- " + payBalance);
                                        grandTotaldouble = 0.0;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }*/

                                Double total = Double.valueOf(total_bag) - Double.valueOf(bag_discount);
                                long total_amount = Math.round(Double.valueOf(total));
                                txt_total_amt.setText("TOTAL PRICE \u20B9" + total_amount);

//                                grandTotaldouble = Double.parseDouble(order_total);
                                minCart = Double.parseDouble(miniCart);
                                if (total >= minCart) {
                                    lin_min_limit.setVisibility(View.GONE);
                                    flag_checkout = 0;
                                    rel_checkout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                } else {
                                    lin_min_limit.setVisibility(View.VISIBLE);
                                    txt_mincart.setText("Grand total should be greater then minimum cart value i.e. " + String.valueOf(minCart));
                                    flag_checkout = 1;
                                    rel_checkout.setBackgroundColor(getResources().getColor(R.color.gray));
                                }
                            }
                            dismissDialog();

                        } else {
                            rel_list_data.setVisibility(GONE);
                            rel_nodata.setVisibility(View.VISIBLE);
//                            Toast.makeText(CartActivity2.this, "Error", Toast.LENGTH_SHORT).show();
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
                    params.put("customer_id", SharedPref.getVal(CartActivity2.this, SharedPref.customer_id));
//                    params.put("customer_id", "3");
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();

//            final Dialog dialog = new Dialog(CartActivity2.this);
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


    public void minimuncart() {
//        dialog.show();
        shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        lin_parent.setVisibility(View.GONE);

        if (APIURLs.isNetwork(CartActivity2.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.minimum_amount_limit, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            miniCart = jsonObject1.getString("min_limit");
                            miniCart = jsonObject1.getString("min_limit");
                            /*if(walletBalance.equals("0")){
                                lnr_walletBalance.setVisibility(View.GONE);
                            }else{
                                lnr_walletBalance.setVisibility(View.VISIBLE);
                            }
                            txt_walletCharges.setText("- "+walletBalance);
                            getProductList();*/
//                            getProductList();
                        } else {
//                            Toast.makeText(CartActivity2.this, "Error", Toast.LENGTH_SHORT).show();
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
                    params.put("customer_id", SharedPref.getVal(CartActivity2.this, SharedPref.customer_id));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
//            FunctionConstant.noInternetDialog(CartActivity2.this, "no internet connection");
            final Dialog dialog = new Dialog(CartActivity2.this);
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

    public void search(View view) {
        Intent intent = new Intent(CartActivity2.this, SearchActivity.class);
        intent.putExtra("key", "");
        intent.putExtra("from", "cart");
        startActivity(intent);
    }

    public void cart(View view) {
        startActivity(new Intent(CartActivity2.this, CartActivity2.class));

    }

    public class CartListingAdapter extends RecyclerView.Adapter<CartListingAdapter.ItemViewholder> {
        Context context;


        public CartListingAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public CartListingAdapter.ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
            CartListingAdapter.ItemViewholder itemViewholder = new CartListingAdapter.ItemViewholder(view);
            return itemViewholder;
        }

        @Override
        public void onBindViewHolder(@NonNull CartListingAdapter.ItemViewholder holder, int position) {
            CartModel model = cartModels.get(position);
            ((CartListingAdapter.ItemViewholder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return cartModels.size();
        }

        public class ItemViewholder extends RecyclerView.ViewHolder {
            ImageView iv;
            int qty = 0;
            LinearLayout lin_top;
            CardView card_minus,
                    card_plus;
            RelativeLayout lin_owner;
            TextView txt_name, txt_remove, txt_unit, txt_mrp, txt_price, txt_discount, txt_qty;

            ProgressBar progressBar;

            public ItemViewholder(@NonNull View itemView) {
                super(itemView);

                progressBar = itemView.findViewById(R.id.progressBar);
                iv = itemView.findViewById(R.id.iv);
                txt_name = itemView.findViewById(R.id.txt_name);
                lin_top = itemView.findViewById(R.id.lin_top);
                txt_unit = itemView.findViewById(R.id.txt_unit);
                txt_price = itemView.findViewById(R.id.txt_price);
                txt_discount = itemView.findViewById(R.id.txt_discount);
                txt_qty = itemView.findViewById(R.id.txt_qty);
                card_minus = itemView.findViewById(R.id.card_minus);
                card_plus = itemView.findViewById(R.id.card_plus);
                txt_remove = itemView.findViewById(R.id.txt_remove);
                txt_mrp = itemView.findViewById(R.id.txt_mrp);
                txt_mrp.setPaintFlags(txt_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            }

            public void bind(final CartModel model, final int position) {

                productIdArrayList.add(model.getProduct_id());
                unitArrayList.add(model.getUnit());
                unitpriceArrayList.add(model.getUnit_price());
                discountArrayList.add(model.getDiscount());
                qtyArrayList.add(model.getQty());


//                txt_name.setText("");
//
                Glide.with(CartActivity2.this).load(model.getImage()).error(R.drawable.loader)
                        .placeholder(R.drawable.loader).into(iv);

//                Glide.with(CartActivity2.this).load(model.getImage()).into(iv);

                qty = Integer.parseInt(model.getQty());


                txt_unit.setText(model.getUnit());
                txt_name.setText(model.getProduct_name());
                Double total = (Double.valueOf(model.getPrice()) - Double.valueOf(model.getDiscount()));
                long price_amount = Math.round(Double.valueOf(total));

                txt_price.setText("Sterlex Super Mart \u20B9" + price_amount);

                if (model.getDiscountPrice().equals("0")) {
                    txt_discount.setVisibility(View.INVISIBLE);
                    txt_mrp.setVisibility(View.INVISIBLE);
                } else {
                    txt_discount.setVisibility(View.VISIBLE);
                    txt_mrp.setVisibility(View.VISIBLE);
                    txt_discount.setText("Save \u20B9" + model.getDiscount());
                    txt_mrp.setText("MRP \u20B9" + model.getPrice());
                }
                txt_qty.setText(model.getQty());
//
                txt_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteCart(model.getProduct_id(), txt_unit.getText().toString(), position);
                    }
                });
                card_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (qty != 1) {
                            qty--;
                            if (qty != 0) {
                                txt_qty.setText(String.valueOf(qty));
                                addCart(model.getProduct_id(), model.getUnit(), model.getUnit_price(), model.getDiscountPrice(), txt_qty.getText().toString(), model.getGst(), position);
//
                            }
                        } else {
                            removeCart(model.getProduct_id(), txt_unit.getText().toString(), position);

                        }
                    }
                });

                card_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if (qty <= 5) {
                        qty++;
                        txt_qty.setText(String.valueOf(qty));

                        txt_qty.setVisibility(View.GONE);
                        addCart(model.getProduct_id(), model.getUnit(), model.getPrice(), model.getDiscountPrice(), txt_qty.getText().toString(), model.getGst(), position);
//
//                        }
                    }
                });

                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CartActivity2.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });

                txt_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CartActivity2.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });

                txt_price.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CartActivity2.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });

                txt_discount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CartActivity2.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });

                txt_mrp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CartActivity2.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });

            }

            public void addCart(final String prod_id, final String unit, final String price, final String discount, final String qty1, final String gst, final int position) {
                progressBar.setVisibility(View.VISIBLE);
                txt_cart_no_of_items.setVisibility(GONE);
                if (APIURLs.isNetwork(CartActivity2.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.ADDCART_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String status = jsonObject1.getString("status");
                                if (status.equals("1")) {
//                                    txt_cart_no_of_items.setText(qty1);
//                            qty = 0;
                                    cartList();

                           /* productId = new ArrayList<>();
                            unitArrayList = new ArrayList<>();
                            unitArrayList = new ArrayList<>();
                            qtyArrayList = new ArrayList<>();
                            discountArrayList = new ArrayList<>();*/
//                            getProductList();
//                            getProductList();
                                } else {
//                                    Toast.makeText(CartActivity2.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                                txt_cart_no_of_items.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            } catch (JSONException e) {
                                progressBar.setVisibility(View.GONE);


                                txt_cart_no_of_items.setVisibility(View.VISIBLE);
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);

//                            progressBar.setVisibility(GONE);
                            txt_cart_no_of_items.setVisibility(View.VISIBLE);
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("customer_id", SharedPref.getVal(CartActivity2.this, SharedPref.customer_id));
                            params.put("product_id", prod_id);
                            params.put("unit", unit);
                            params.put("price", price);
                            params.put("gst", "");
                            params.put("discount", discount);
                            params.put("qty", qty1);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(CartActivity2.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    dismissDialog();
                    FunctionConstant.noInternetDialog(CartActivity2.this, "no internet connection");
                }
            }

            public void removeCart(final String product_id, final String unit, final int position) {
//                dialog.show();
                progressBar.setVisibility(View.VISIBLE);
                txt_cart_no_of_items.setVisibility(GONE);
                if (APIURLs.isNetwork(CartActivity2.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.remove_product_qty, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
//                                JSONArray jsonArray = jsonObject.getJSONArray("result");
//                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String status = jsonObject.getString("status");
                                if (status.equals("1")) {
//                            qty = 0;
//                                    getProductList();
//                            rel_no.setVisibility(GONE);
                                    Toast.makeText(CartActivity2.this, "Item removed from cart successfully", Toast.LENGTH_SHORT).show();
                                    cartList();
                                   /* productId = new ArrayList<>();
                                    unitArrayList = new ArrayList<>();
                                    qtyArrayList = new ArrayList<>();
                                    discountArrayList = new ArrayList<>();*/
                                   /* adapter.notifyDataSetChanged();
                                    adapter.notifyItemRemoved(position);*/

                                    /*rel_cart.setVisibility(View.GONE);
                                    btn_cart.setVisibility(View.VISIBLE);*/
                                } else {
//                                    Toast.makeText(CartActivity2.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                                dismissDialog();
                                progressBar.setVisibility(GONE);
                                txt_cart_no_of_items.setVisibility(View.VISIBLE);
                            } catch (JSONException e) {
                                dismissDialog();
                                progressBar.setVisibility(GONE);
                                txt_cart_no_of_items.setVisibility(View.VISIBLE);
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dismissDialog();
                            progressBar.setVisibility(GONE);
                            txt_cart_no_of_items.setVisibility(View.VISIBLE);
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("product_id", product_id);
                            params.put("unit", unit);
                            params.put("customer_id", SharedPref.getVal(CartActivity2.this, SharedPref.customer_id));
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(CartActivity2.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    dismissDialog();
                    FunctionConstant.noInternetDialog(CartActivity2.this, "no internet connection");
                }
            }

            public void deleteCart(final String product_id, final String unit, final int position) {
                dialog.show();
//                progressBar.setVisibility(View.VISIBLE);
                txt_cart_no_of_items.setVisibility(GONE);
                if (APIURLs.isNetwork(CartActivity2.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.remove_product_qty, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
//                                JSONArray jsonArray = jsonObject.getJSONArray("result");
//                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String status = jsonObject.getString("status");
                                if (status.equals("1")) {
//                            qty = 0;
//                                    getProductList();
//                            rel_no.setVisibility(GONE);
                                    Toast.makeText(CartActivity2.this, "Product removed from cart successfully", Toast.LENGTH_SHORT).show();
                                    cartList();
                                   /* productId = new ArrayList<>();
                                    unitArrayList = new ArrayList<>();
                                    qtyArrayList = new ArrayList<>();
                                    discountArrayList = new ArrayList<>();*/
                                   /* adapter.notifyDataSetChanged();
                                    adapter.notifyItemRemoved(position);*/

                                    /*rel_cart.setVisibility(View.GONE);
                                    btn_cart.setVisibility(View.VISIBLE);*/
                                } else {
//                                    Toast.makeText(CartActivity2.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                                dismissDialog();
//                                progressBar.setVisibility(GONE);
                                txt_cart_no_of_items.setVisibility(View.VISIBLE);
                            } catch (JSONException e) {
                                dismissDialog();
//                                progressBar.setVisibility(GONE);
                                txt_cart_no_of_items.setVisibility(View.VISIBLE);
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dismissDialog();
//                            progressBar.setVisibility(GONE);
                            txt_cart_no_of_items.setVisibility(View.VISIBLE);
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("product_id", product_id);
                            params.put("unit", unit);
                            params.put("customer_id", SharedPref.getVal(CartActivity2.this, SharedPref.customer_id));
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(CartActivity2.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    dismissDialog();
                    FunctionConstant.noInternetDialog(CartActivity2.this, "no internet connection");
                }
            }

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
                startActivity(new Intent(CartActivity2.this, LoginActivity.class));
            }
        });

        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity2.this, OtpActivity.class));
            }
        });
        dialog1.show();
    }


    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        shimmer_view_container.stopShimmerAnimation();
        lin_parent.setVisibility(View.VISIBLE);
        shimmer_view_container.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        shimmer_view_container.stopShimmerAnimation();
        lin_parent.setVisibility(View.VISIBLE);
        shimmer_view_container.setVisibility(View.GONE);
    }


    @Override
    protected void onPause() {
        shimmer_view_container.stopShimmerAnimation();
        lin_parent.setVisibility(View.VISIBLE);
        shimmer_view_container.setVisibility(View.GONE);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        shimmer_view_container.startShimmerAnimation();
//        lin_parent.setVisibility(View.GONE);
//        shimmer_view_container.setVisibility(View.VISIBLE);
//        if (FunctionConstant.GPSRuntime(CartActivity2.this)) {
//            getCurrentLocation();
//        } else {
//            showSettingsAlert();
//        }

        minimuncart();
        cartList();
    }

}