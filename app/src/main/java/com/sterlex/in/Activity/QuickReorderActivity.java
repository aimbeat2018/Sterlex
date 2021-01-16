package com.sterlex.in.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.gmail.samehadar.iosdialog.IOSDialog;
import com.sterlex.in.Constant.APIURLs;
import com.sterlex.in.Constant.FunctionConstant;
import com.sterlex.in.Constant.SharedPref;
import com.sterlex.in.Model.OrderModel;
import com.sterlex.in.Model.ProductModel;
import com.sterlex.in.Model.UnitModel;
import com.sterlex.in.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;

public class QuickReorderActivity extends AppCompatActivity {

    RecyclerView recy_list;
    LinearLayout lnr_main;
    ShimmerFrameLayout shimmer_view_container;
    ArrayList<OrderModel> orderModelArrayList = new ArrayList<>();
    String cost_id = "", prod_Qty = "null", key = "", from = "", cart_unit_price = "", cart_unit = "", cart_discount = "";

    ArrayList<ProductModel> productModels = new ArrayList<>();
    ArrayList<UnitModel> product_unitArraylist = new ArrayList<>();
    boolean cart_click = false;
    int qty = 0;
    TextView txt_cart_no_of_items, cart_txt_amount;
    RelativeLayout rel_cart_no_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_reorder);

        cost_id = SharedPref.getVal(QuickReorderActivity.this, SharedPref.customer_id);
        init();
        onClick();
        getOrderList();
    }

    private void init() {
        recy_list = findViewById(R.id.recy_list);
        shimmer_view_container = findViewById(R.id.shimmer_view_container);
        lnr_main = findViewById(R.id.lnr_main);


        rel_cart_no_items = findViewById(R.id.rel_cart_no_items);
        txt_cart_no_of_items = findViewById(R.id.txt_cart_no_of_items);
        cart_txt_amount = findViewById(R.id.cart_txt_amount);
        recy_list.setLayoutManager(new LinearLayoutManager(QuickReorderActivity.this));
//        ProductsListingAdapter adapter = new ProductsListingAdapter(QuickReorderActivity.this);
//        recy_list.setAdapter(adapter);
    }

    private void onClick() {
    }

    public void onBack(View view) {
        onBackPressed();
    }

    public void getOrderList() {
//        dialog.show();
        shimmer_view_container.startShimmerAnimation();
        recy_list.setVisibility(View.GONE);
        shimmer_view_container.setVisibility(View.VISIBLE);

        if (APIURLs.isNetwork(QuickReorderActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.order_history, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            orderModelArrayList = new ArrayList<>();
                            if (jsonObject.has("result")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    OrderModel model = new OrderModel();
                                    model.setOid(jsonObject1.getString("oid"));
                                    model.setOrder_id(jsonObject1.getString("order_id"));
                                    model.setStatus(jsonObject1.getString("status"));
                                    model.setOrder_total(jsonObject1.getString("order_total"));
                                    model.setOrder_date(jsonObject1.getString("order_date"));
                                    model.setItem(jsonObject1.getString("item"));
                                    model.setDelivery_status(jsonObject1.getString("delivery_status"));
                                    model.setOtp(jsonObject1.getString("otp"));
                                    orderModelArrayList.add(model);
                                }
                                Collections.reverse(orderModelArrayList);
                                MyOrderAdapter adapter = new MyOrderAdapter();
                                recy_list.setAdapter(adapter);
                                recy_list.setVisibility(View.VISIBLE);
//                                txt_nodata.setVisibility(View.GONE);
                            }
                        } else {
                            recy_list.setVisibility(View.GONE);
//                            txt_nodata.setVisibility(View.VISIBLE);
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
                    params.put("customer_id", SharedPref.getVal(QuickReorderActivity.this, SharedPref.customer_id));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            final Dialog dialog = new Dialog(QuickReorderActivity.this);
            dialog.setContentView(R.layout.no_internet_dialog);
            Button button = dialog.findViewById(R.id.btn_process);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getOrderList();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }


    public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

        @NonNull
        @Override
        public MyOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(QuickReorderActivity.this).inflate(R.layout.quick_reorder_item_layout, parent, false);
            return new MyOrderAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyOrderAdapter.ViewHolder holder, int position) {
            OrderModel model = orderModelArrayList.get(position);
            ((MyOrderAdapter.ViewHolder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return orderModelArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txt_cancel_order, txt_orderDate, txt_orderNumber, txt_price, txt_item, txt_status, txt_delivery, txt_pickupPoint;
            CardView card_list;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                txt_cancel_order = itemView.findViewById(R.id.txt_cancel_order);
                txt_orderDate = itemView.findViewById(R.id.txt_orderDate);
                txt_orderNumber = itemView.findViewById(R.id.txt_orderNumber);
                txt_price = itemView.findViewById(R.id.txt_price);
                txt_item = itemView.findViewById(R.id.txt_item);
                txt_status = itemView.findViewById(R.id.txt_status);
                txt_delivery = itemView.findViewById(R.id.txt_delivery);
                card_list = itemView.findViewById(R.id.card_list);
                txt_pickupPoint = itemView.findViewById(R.id.txt_pickupPoint);
            }

            public void bind(final OrderModel model, final int position) {
                txt_orderDate.setText(model.getOrder_date());
                txt_orderNumber.setText("Order Id : #" + model.getOrder_id());
                txt_price.setText("Rs." + model.getOrder_total());
                if (Integer.parseInt(model.getItem()) > 1) {
                    txt_item.setText(model.getItem() + " items");
                } else {
                    txt_item.setText(model.getItem() + " item");
                }
                if (model.getStatus().equals("0")) {
                    txt_status.setText("Status : Ordered");
                } else if (model.getStatus().equals("1")) {
                    txt_status.setText("Status : Approved");
                } else if (model.getStatus().equals("2")) {
                    txt_status.setText("Status : Dispatch");
                } else if (model.getStatus().equals("3")) {
                    txt_status.setText("Status : Delivered");
                } else if (model.getStatus().equals("4")) {
                    txt_status.setText("Status : Cancelled");
                }

                if (model.getDelivery_status().equals("0")) {
                    txt_delivery.setText("Home Delivery");
                    txt_pickupPoint.setVisibility(View.GONE);
                } else {
                    txt_delivery.setText("Delivery at pick point");
                    txt_pickupPoint.setVisibility(View.VISIBLE);
                    txt_pickupPoint.setText("Pickup Point Otp : " + model.getOtp());
                }

                card_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(QuickReorderActivity.this, OrderTrackingActivity.class);
                        intent.putExtra("order_id", model.getOid());
                        startActivity(intent);
                    }
                });

                txt_cancel_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(QuickReorderActivity.this, CancelOrderActivity.class);
//                        intent.putExtra("oid", model.getOid());
//                        startActivity(intent);
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(QuickReorderActivity.this);
                        // Setting Dialog Title
                        alertDialog.setTitle("Quick Reorder");
                        // Setting Dialog Message
                        alertDialog.setMessage("Do you want to order again?");
                        // On pressing Settings button
                        alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                quickReorder(model.getOid());
                                dialog.dismiss();
                            }
                        });
                        // on pressing cancel button
                        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        // Showing Alert Message
                        alertDialog.show();


                    }
                });
            }
        }

        private void quickReorder(final String oid) {
            shimmer_view_container.startShimmerAnimation();
            shimmer_view_container.setVisibility(View.VISIBLE);
            lnr_main.setVisibility(View.GONE);

            if (APIURLs.isNetwork(QuickReorderActivity.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.quick_order_v1, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
//                            JSONArray jsonArray = jsonObject.getJSONArray("result");
//                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            String status = jsonObject.getString("status");
                            if (status.equals("200")) {
//                            Toast.makeText(CancelOrderActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(QuickReorderActivity.this, CartActivity2.class);
//                                intent.putExtra("order_id", order_id);
                                startActivity(intent);
                            } else {
                                Toast.makeText(QuickReorderActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                        params.put("order_id", oid);
                        params.put("customer_id", SharedPref.getVal(QuickReorderActivity.this, SharedPref.customer_id));
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(QuickReorderActivity.this);
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);
            } else {
                dismissDialog();
                FunctionConstant.noInternetDialog(QuickReorderActivity.this, "no internet connection");
            }

        }

    }
//    public class ProductsListingAdapter extends RecyclerView.Adapter<ProductsListingAdapter.ItemViewholder> {
//        Context context;
//
//
//        public ProductsListingAdapter(Context context) {
//            this.context = context;
//        }
//
//        @NonNull
//        @Override
//        public ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quick_reorder_item_layout, parent, false);
//            ItemViewholder itemViewholder = new ItemViewholder(view);
//            return itemViewholder;
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull ItemViewholder holder, int position) {
////            HouseHelpCategoryPojo model = houseHelpCategoryPojos.get(position);
//            ((ItemViewholder) holder).bind(position);
//        }
//
//        @Override
//        public int getItemCount() {
//            return 6;
//        }
//
//
//        public class ItemViewholder extends RecyclerView.ViewHolder {
//            ImageView iv;
//
//            LinearLayout lin_item, lin_comm;
//            CardView card_bg;
//            RelativeLayout lin_owner;
//            TextView txt_mrp;
//
//            public ItemViewholder(@NonNull View itemView) {
//                super(itemView);
//
////                card_bg = itemView.findViewById(R.id.card_bg);
//                lin_item = itemView.findViewById(R.id.lin_item);
//                txt_mrp = itemView.findViewById(R.id.txt_mrp);
//
//            }
//
//            public void bind(final int position) {
////                txt_name.setText("");
////
////                txt_name.setText(model.getCategoryname());
////
////
//                lin_item.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(QuickReorderActivity.this, ProductDetailsActivity.class);
////                        intent.putExtra("id", "id");
//                        startActivity(intent);
//                    }
//                });
//
//
//            }
//
//        }
//
//    }
//
//    public class ProductsListingAdapter extends RecyclerView.Adapter<ProductsListingAdapter.ItemViewholder> {
//        Context context;
//        ArrayList<UnitModel> product_unitArraylist = new ArrayList<>();
//        Double actualAmout;
//        Double discount;
//        Double payableAmt;
//
//        public ProductsListingAdapter(Context context) {
//            this.context = context;
//        }
//
//        @NonNull
//        @Override
//        public ProductsListingAdapter.ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quick_reorder_item_layout, parent, false);
//            ProductsListingAdapter.ItemViewholder itemViewholder = new ProductsListingAdapter.ItemViewholder(view);
//            return itemViewholder;
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull ProductsListingAdapter.ItemViewholder holder, int position) {
//            ProductModel model = productModels.get(position);
////            ((ProductsListingAdapter.ItemViewholder) holder).bind(model, position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public int getItemViewType(int position) {
//            return position;
//        }
//
//        @Override
//        public int getItemCount() {
//            return 7;
//        }
//
//        public class ItemViewholder extends RecyclerView.ViewHolder {
////            ImageView iv, iv_veg, iv_nonveg;
////
////            LinearLayout lin_item, lin_comm;
////            CardView card_unit, card_add_toCart;
////            RelativeLayout rel_veg_nonveg, rel_no;
////            TextView txt_name, txt_mrp, txt_og_price, txt_discount, txt_count, txt_cart_items;
////            TextView txt_unit;
////            Button btn_cart;
////            int flag_qty = 0, qty_open_close_flag = 0;
////            RelativeLayout rel_qty, rel_stock, rel_bottomcart, rel_discount, rel_cart, rel_minus, rel_plus, rel_subscribe, rel_subscribeminus, rel_subscribeplus;
////            RecyclerView recy_qty;
////            double ogPrice = 0.0, price = 0.0, discount = 0.0, totalPrice = 0.0;
////            ProgressBar progressBar;
//
//            public ItemViewholder(@NonNull View itemView) {
//                super(itemView);
//
////                iv = itemView.findViewById(R.id.iv);
////                iv_veg = itemView.findViewById(R.id.iv_veg);
////                iv_nonveg = itemView.findViewById(R.id.iv_nonveg);
////                rel_veg_nonveg = itemView.findViewById(R.id.rel_veg_nonveg);
////                rel_cart = itemView.findViewById(R.id.rel_cart);
////                rel_stock = itemView.findViewById(R.id.rel_stock);
////                rel_minus = itemView.findViewById(R.id.rel_minus);
////                rel_plus = itemView.findViewById(R.id.rel_plus);
////                rel_no = itemView.findViewById(R.id.rel_no);
////                txt_cart_items = itemView.findViewById(R.id.txt_cart_items);
////                rel_qty = itemView.findViewById(R.id.rel_qty);
////                card_unit = itemView.findViewById(R.id.card_unit);
////                btn_cart = itemView.findViewById(R.id.btn_cart);
////                recy_qty = itemView.findViewById(R.id.recy_qty);
////                txt_name = itemView.findViewById(R.id.txt_name);
////                txt_mrp = itemView.findViewById(R.id.txt_mrp);
////                txt_og_price = itemView.findViewById(R.id.txt_og_price);
////                txt_discount = itemView.findViewById(R.id.txt_discount);
////                txt_unit = itemView.findViewById(R.id.txt_unit);
////                lin_item = itemView.findViewById(R.id.lin_item);
////                txt_count = itemView.findViewById(R.id.txt_count);
////                card_unit = itemView.findViewById(R.id.card_unit);
////                progressBar = itemView.findViewById(R.id.progressBar);
////
////                rel_bottomcart = itemView.findViewById(R.id.rel_cartmain);
////                txt_mrp.setPaintFlags(txt_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//
//
//            }
//
//            public void bind(final ProductModel model, final int position) {
//
////                if (model.getIs_veg().equals("1")) {
////                    iv_veg.setVisibility(View.VISIBLE);
////
////                } else if (model.getIs_veg().equals("0")) {
////                    iv_nonveg.setVisibility(View.VISIBLE);
////                } else {
////                    iv_nonveg.setVisibility(View.GONE);
////                    iv_veg.setVisibility(View.GONE);
////
////                }
////
////                if (model.getFlag().equals("0")) {
////                    rel_stock.setVisibility(View.VISIBLE);
////                } else if (model.getFlag().equals("1")) {
////                    rel_stock.setVisibility(GONE);
////                }
////
////
//////             if (model.getType().equals("0")) {
//////                    rel_subs.setVisibility(GONE);
//////                    rel_cartmain.setVisibility(View.VISIBLE);
//////                } else {
//////                    rel_subs.setVisibility(View.VISIBLE);
//////                    rel_cartmain.setVisibility(GONE);
//////                }
////
////                if (model.getQty().equals("0")) {
////                    rel_cart.setVisibility(GONE);
////                    btn_cart.setVisibility(View.VISIBLE);
////                } else {
////                    rel_cart.setVisibility(View.VISIBLE);
////                    btn_cart.setVisibility(GONE);
////                    rel_no.setVisibility(View.VISIBLE);
////                    txt_cart_items.setText(model.getQty());
////                    txt_count.setText(model.getQty());
////
////                }
////
////                try {
////                    if (model.getDiscount().equals("0")) {
////                       /* double actualamt = Double.parseDouble(model.getPrice()) * Double.parseDouble(model.getQty());
////                        DecimalFormat twoDForm = new DecimalFormat("#");
////                        actualamt = Double.valueOf(twoDForm.format(actualamt));*/
////
////                        txt_og_price.setText("MRP \u20B9" + model.getPrice());
////                        txt_mrp.setVisibility(View.INVISIBLE);
//////                        rel_discount.setVisibility(View.GONE);
////                        txt_discount.setVisibility(View.INVISIBLE);
////                    } else {
////                        txt_mrp.setVisibility(View.VISIBLE);
//////                        rel_discount.setVisibility(View.VISIBLE);
////                        txt_discount.setVisibility(View.VISIBLE);
//////                        actualAmout = Double.parseDouble(model.getPrice());
////                       /* discount = (actualAmout * Double.parseDouble(model.getDiscount())) / 100;
////                        payableAmt = actualAmout - discount;
////                        DecimalFormat twoDForm = new DecimalFormat("#");
////                        payableAmt = Double.valueOf(twoDForm.format(payableAmt));
////
////                        txt_discountprice.setText("Rs." + (int) Math.round(payableAmt));
////                        txt_mrp.setText("Rs." + model.getPrice());
////                        txt_discount.setText(model.getDiscount() + "% off");*/
////
//////                        payableAmt = actualAmout - Double.parseDouble(model.getDiscount());
////                        if (!model.getQty().equals("0")) {
////                            double actualamt = Double.parseDouble(model.getPrice()) * Double.parseDouble(model.getQty());
////                            DecimalFormat twoDForm = new DecimalFormat("#");
////                            actualamt = Double.valueOf(twoDForm.format(actualamt));
////                            txt_mrp.setText("MRP \u20b9" + (int) Math.round(actualamt));
////
////
////                            double payableAmt = Double.parseDouble(model.getPrice()) - Double.parseDouble(model.getDiscount());
////                            double finalpayamt = payableAmt * Double.parseDouble(model.getQty());
////                            DecimalFormat twoDForm1 = new DecimalFormat("#");
////                            finalpayamt = Double.valueOf(twoDForm1.format(finalpayamt));
////                            txt_og_price.setText("Sterlex Super Mart \u20b9" + (int) Math.round(finalpayamt));
////
//////                        txt_mrp.setText("MRP \u20B9" + model.getPrice());
//////                        txt_discount.setText("Save\n\u20B9" + model.getDiscount());
////                            double saveamt = Double.parseDouble(model.getDiscount()) * Double.parseDouble(model.getQty());
////                            DecimalFormat twoDForm2 = new DecimalFormat("#");
////                            saveamt = Double.valueOf(twoDForm2.format(saveamt));
////                            txt_discount.setText("Save \u20B9" + (int) Math.round(saveamt));
////                        } else {
////                            actualAmout = Double.parseDouble(model.getPrice());
////                            payableAmt = actualAmout - Double.parseDouble(model.getDiscount());
////                            DecimalFormat twoDForm1 = new DecimalFormat("#");
////                            payableAmt = Double.valueOf(twoDForm1.format(payableAmt));
////                            txt_og_price.setText("Sterlex Super Mart \u20b9" + (int) Math.round(payableAmt));
////                            txt_mrp.setText("MRP \u20B9" + model.getPrice());
//////                            txt_discount.setText("Save\n\u20B9" + model.getDiscount());
////                            txt_discount.setText("Save \u20B9" + model.getDiscount());
////                        }
////                    }
////
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////
////                price = Double.valueOf(model.getPrice());
////                discount = Double.valueOf(model.getDiscount());
////                totalPrice = price + discount;
////                Glide.with(QuickReorderActivity.this).load(model.getImage()).into(iv);
////
////                txt_name.setText(model.getProduct_name());
//////                txt_mrp.setText("MRP \u20B9" + totalPrice);
//////                txt_og_price.setText("Sterlex Super Mart \u20B9" + model.getPrice());
//////                txt_discount.setText("Save \u20B9" + model.getDiscount());
////                txt_unit.setText(model.getUnit());
////
////                product_unitArraylist = new ArrayList<>();
////                product_unitArraylist = model.getProduct_unitArraylist();
////                try {
////                    if (product_unitArraylist != null) {
////                        if (product_unitArraylist.size() > 1) {
////                            card_unit.setVisibility(View.VISIBLE);
////                            recy_qty.setVisibility(GONE);
////
////                            flag_qty = 1;
////                            recy_qty.setLayoutManager(new LinearLayoutManager(QuickReorderActivity.this, RecyclerView.HORIZONTAL, false));
////                            ProductsListingAdapter.QuantityListingAdapter adapter1 = new ProductsListingAdapter.QuantityListingAdapter(QuickReorderActivity.this, product_unitArraylist, txt_mrp, txt_og_price, txt_discount, txt_unit);
////                            recy_qty.setAdapter(adapter1);
////
////                        } else {
////                            flag_qty = 0;
////                            card_unit.setVisibility(GONE);
////                            recy_qty.setVisibility(GONE);
////                        }
////                    } else {
////                        flag_qty = 0;
////                        card_unit.setVisibility(GONE);
////                        recy_qty.setVisibility(GONE);
////
////                    }
////
////                } catch (Exception e) {
////                    e.printStackTrace();
////                    flag_qty = 0;
////                    card_unit.setVisibility(GONE);
////                    recy_qty.setVisibility(GONE);
////
////                }
////                rel_qty.setOnClickListener(new View.OnClickListener() {
////
////                    @Override
////                    public void onClick(View v) {
////                        prod_Qty = model.getQty();
////                        if (model.getProduct_unitArraylist().size() > 1) {
////                            if (qty_open_close_flag == 0) {
////                                recy_qty.setVisibility(View.VISIBLE);
////                                qty_open_close_flag = 1;
////                            } else {
////                                qty_open_close_flag = 0;
////                                recy_qty.setVisibility(GONE);
////
////                            }
////
////                        }
////                    }
////                });
////
////                btn_cart.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        if (cost_id.equals("")) {
////                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(QuickReorderActivity.this);
////                            // Setting Dialog Title
////                            alertDialog.setTitle("Please Login");
////                            // Setting Dialog Message
////                            alertDialog.setMessage("Please login to add product in cart");
////                            // On pressing Settings button
////                            alertDialog.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
////                                public void onClick(DialogInterface dialog, int which) {
////                                    Intent intent = new Intent(QuickReorderActivity.this, LoginActivity.class);
////                                    intent.putExtra("from", "product_details");
////                                    startActivity(intent);
////                                }
////                            });
////                            // on pressing cancel button
////                            alertDialog.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
////                                public void onClick(DialogInterface dialog, int which) {
////                                    dialog.cancel();
////                                }
////                            });
////                            // Showing Alert Message
////                            alertDialog.show();
////                        } else {
////                            if (model.getFlag().equals("1")) {
////                                rel_cart.setVisibility(View.VISIBLE);
////                                btn_cart.setVisibility(View.GONE);
////
//////                                String price = txt_mrp.getText().toString();
//////                                String str_price = price.substring(5, price.length());
//////                                Toast.makeText(QuickReorderActivity.this, str_price, Toast.LENGTH_SHORT).show();
////                                String discount = txt_discount.getText().toString();
////                                String str_discount = "0";
////                                if (!discount.equals("0")) {
////                                    str_discount = discount.substring(6, discount.length());
//////                                    txt_mrp.setVisibility(View.VISIBLE);
//////                                    txt_discount.setVisibility(View.VISIBLE);
//////                                    Toast.makeText(QuickReorderActivity.this, str_discount, Toast.LENGTH_SHORT).show();
////                                }
////                                if (!cart_click) {
////                                    cart_unit = model.getUnit();
////                                    cart_unit_price = model.getPrice();
////                                    cart_discount = model.getDiscount();
////                                }
////                                addCart(model.getProduct_id(), cart_unit, cart_unit_price,
////                                        cart_discount, txt_count.getText().toString(), model.getGst(), position, txt_og_price, txt_mrp, txt_discount
////                                );
////                                txt_count.setVisibility(View.VISIBLE);
////                            } else {
////                                Toast.makeText(QuickReorderActivity.this, "out of stock", Toast.LENGTH_SHORT).show();
////                            }
////                        }
//////                        addCart(model.getProduct_id(), model.getUnit(), model.getPrice(), model.getDiscount(), model.getQty(), model.getGst(), position);
////                    }
////                });
////
////
////                iv.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        Intent intent = new Intent(QuickReorderActivity.this, ProductDetailsActivity.class);
////                        intent.putExtra("prod_id", model.getProduct_id());
////                        startActivity(intent);
////                    }
////                });
////                rel_veg_nonveg.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        Intent intent = new Intent(QuickReorderActivity.this, ProductDetailsActivity.class);
////                        intent.putExtra("prod_id", model.getProduct_id());
////                        startActivity(intent);
////                    }
////                });
////                txt_name.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        Intent intent = new Intent(QuickReorderActivity.this, ProductDetailsActivity.class);
////                        intent.putExtra("prod_id", model.getProduct_id());
////                        startActivity(intent);
////                    }
////                });
////                txt_mrp.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        Intent intent = new Intent(QuickReorderActivity.this, ProductDetailsActivity.class);
////                        intent.putExtra("prod_id", model.getProduct_id());
////                        startActivity(intent);
////                    }
////                });
////                txt_og_price.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        Intent intent = new Intent(QuickReorderActivity.this, ProductDetailsActivity.class);
////                        intent.putExtra("prod_id", model.getProduct_id());
////                        startActivity(intent);
////                    }
////                });
////                txt_discount.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        Intent intent = new Intent(QuickReorderActivity.this, ProductDetailsActivity.class);
////                        intent.putExtra("prod_id", model.getProduct_id());
////                        startActivity(intent);
////                    }
////                });
////
////                rel_minus.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        qty = Integer.parseInt(txt_count.getText().toString());
////                        if (model.getFlag().equals("1")) {
////                            qty = Integer.parseInt(txt_count.getText().toString());
////                            if (qty >= 1) {
////                                qty--;
////                                if (qty != 0) {
////                                    if (qty >= 1) {
//////                                        progressBar.setVisibility(View.VISIBLE);
////                                        txt_count.setVisibility(GONE);
////                                        txt_count.setText(String.valueOf(qty));
////                                        txt_cart_items.setText(String.valueOf(qty));
//////                                        String price = txt_mrp.getText().toString();
//////                                        String str_price = price.substring(5, price.length());
//////                                        Toast.makeText(QuickReorderActivity.this, str_price, Toast.LENGTH_SHORT).show();
////                                        String discount = txt_discount.getText().toString();
////                                        String str_discount = "0";
////                                        if (!discount.equals("0")) {
////                                            str_discount = discount.substring(6, discount.length());
//////                                            txt_mrp.setVisibility(View.VISIBLE);
//////                                            txt_discount.setVisibility(View.VISIBLE);
//////                                            Toast.makeText(QuickReorderActivity.this, str_discount, Toast.LENGTH_SHORT).show();
////                                        }
////
////                                        if (!cart_click) {
////                                            cart_unit = model.getUnit();
////                                            cart_unit_price = model.getPrice();
////                                            cart_discount = model.getDiscount();
////                                        }
////                                        addCart(model.getProduct_id(), cart_unit, cart_unit_price,
////                                                cart_discount, txt_count.getText().toString(), model.getGst(), position, txt_og_price, txt_mrp, txt_discount);
////                                    }
////                                    /*else if(qty==1){
////                                        rel_cart.setVisibility(View.GONE);
////                                        btn_cart.setVisibility(View.VISIBLE);
////                                        removeCart(model.getProduct_id(),position);
////                                    }*/
////                                } else {
////                                    rel_cart.setVisibility(GONE);
////                                    btn_cart.setVisibility(View.VISIBLE);
//////                                    Toast.makeText(QuickReorderActivity.this, "removing", Toast.LENGTH_SHORT).show();
////                                    removeCart(model.getProduct_id(), position);
////                                }
////                            }
////                        } else {
////                            Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
////                        }
////
////                    }
////                });
////
////                rel_plus.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        qty = Integer.parseInt(txt_count.getText().toString());
////                        if (model.getFlag().equals("1")) {
////                            qty = Integer.parseInt(txt_count.getText().toString());
////                            qty++;
////                            /* progressBar.setVisibility(View.VISIBLE);*/
////                            txt_count.setVisibility(GONE);
////                            txt_count.setText(String.valueOf(qty));
////                            txt_cart_items.setText(String.valueOf(qty));
////
////
////                            String discount = txt_discount.getText().toString();
////                            if (!discount.equals("0")) {
//////                                txt_mrp.setVisibility(View.VISIBLE);
//////                                txt_discount.setVisibility(View.VISIBLE);
////                            }
////                            if (!cart_click) {
////                                cart_unit = model.getUnit();
////                                cart_unit_price = model.getPrice();
////                                cart_discount = model.getDiscount();
////                            }
////                            addCart(model.getProduct_id(), cart_unit, cart_unit_price, cart_discount,
////                                    txt_count.getText().toString(), model.getGst(), position, txt_og_price, txt_mrp, txt_discount);
////                        } else {
////                            Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
////                        }
////
////                    }
////                });
//
//
//            }
//
////            public void addCart(final String prod_id, final String unit, final String price, final String discount, final String qty1, final String gst, final int position, final TextView txt_discountprice,
////                                final TextView txt_actualprice,
////                                final TextView txt_save_amt
////            ) {
//////                dialog.show();
////                progressBar.setVisibility(View.VISIBLE);
////                txt_count.setVisibility(GONE);
////
////                if (APIURLs.isNetwork(QuickReorderActivity.this)) {
////                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.ADDCART_URL, new Response.Listener<String>() {
////                        @Override
////                        public void onResponse(String response) {
////                            try {
////                                JSONObject jsonObject = new JSONObject(response);
////                                JSONArray jsonArray = jsonObject.getJSONArray("result");
////                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
////                                String status = jsonObject1.getString("status");
////                                if (status.equals("1")) {
////                                    cart_click = false;
////                                    rel_no.setVisibility(View.VISIBLE);
////                                    txt_count.setVisibility(View.VISIBLE);
////                                    txt_cart_items.setText(qty1);
////                                    txt_count.setText(qty1);
////
////                                    if (discount.equals("0")) {
////                                        double actualamt = Double.parseDouble(price) * Double.parseDouble(qty1);
////                                        DecimalFormat twoDForm = new DecimalFormat("#");
////                                        actualamt = Double.valueOf(twoDForm.format(actualamt));
////
////                                        txt_discountprice.setText("MRP \u20B9" + (int) Math.round(actualamt));
////
////                                        txt_actualprice.setVisibility(View.GONE);
////                                        txt_save_amt.setVisibility(View.GONE);
////                                    } else {
////                                        double actualamt = Double.parseDouble(price) * Double.parseDouble(qty1);
////                                        DecimalFormat twoDForm = new DecimalFormat("#");
////                                        actualamt = Double.valueOf(twoDForm.format(actualamt));
////
////                                        txt_actualprice.setText("MRP \u20B9" + (int) Math.round(actualamt));
////
////                                        double payableAmt = Double.parseDouble(price) - Double.parseDouble(discount);
////                                        double finalpayamt = payableAmt * Double.parseDouble(qty1);
////
////                                        DecimalFormat twoDForm1 = new DecimalFormat("#");
////                                        finalpayamt = Double.valueOf(twoDForm1.format(finalpayamt));
////                                        txt_discountprice.setText("Sterlex Super Mart \u20b9" + (int) Math.round(finalpayamt));
////
////                                        double saveamt = Double.parseDouble(discount) * Double.parseDouble(qty1);
////                                        DecimalFormat twoDForm2 = new DecimalFormat("#");
////                                        saveamt = Double.valueOf(twoDForm2.format(saveamt));
////                                        txt_save_amt.setText("Save \u20B9" + (int) Math.round(saveamt));
////
////                                        txt_actualprice.setVisibility(View.VISIBLE);
////                                        txt_save_amt.setVisibility(View.VISIBLE);
////                                    }
////                                    qty = 0;
////                                    cartCount();
////
////                           /* productId = new ArrayList<>();
////                            unitArrayList = new ArrayList<>();
////                            unitArrayList = new ArrayList<>();
////                            qtyArrayList = new ArrayList<>();
////                            discountArrayList = new ArrayList<>();*/
//////                            getProductList();
//////                            getProductList();
////                                } else {
////                                    Toast.makeText(QuickReorderActivity.this, "Error", Toast.LENGTH_SHORT).show();
////                                }
////                                progressBar.setVisibility(View.GONE);
////                                txt_count.setVisibility(View.VISIBLE);
////
//////                                dismissDialog();
////                            } catch (JSONException e) {
//////                                dismissDialog();
////                                progressBar.setVisibility(View.GONE);
////                                txt_count.setVisibility(View.VISIBLE);
////                                e.printStackTrace();
////                            }
////                        }
////                    }, new Response.ErrorListener() {
////                        @Override
////                        public void onErrorResponse(VolleyError error) {
//////                            dismissDialog();
////                            progressBar.setVisibility(GONE);
////                            txt_count.setVisibility(View.VISIBLE);
////                            error.printStackTrace();
////                        }
////                    }) {
////                        @Override
////                        protected Map<String, String> getParams() throws AuthFailureError {
////                            HashMap<String, String> params = new HashMap<>();
////                            params.put("customer_id", SharedPref.getVal(QuickReorderActivity.this, SharedPref.customer_id));
////                            params.put("product_id", prod_id);
////                            params.put("unit", unit);
////                            params.put("price", price);
////                            params.put("gst", gst);
////                            params.put("discount", discount);
////                            params.put("qty", qty1);
////                            return params;
////                        }
////                    };
////
////                    RequestQueue requestQueue = Volley.newRequestQueue(QuickReorderActivity.this);
////                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
////                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
////                    requestQueue.add(stringRequest);
////                } else {
////                    dismissDialog();
////                    FunctionConstant.noInternetDialog(QuickReorderActivity.this, "no internet connection");
////                }
////            }
////
////
////            public void removeCart(final String product_id, final int position) {
//////                dialog.show();
////                progressBar.setVisibility(View.VISIBLE);
////                txt_count.setVisibility(GONE);
////                if (APIURLs.isNetwork(QuickReorderActivity.this)) {
////                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.remove_product_qty, new Response.Listener<String>() {
////                        @Override
////                        public void onResponse(String response) {
////                            try {
////                                JSONObject jsonObject = new JSONObject(response);
//////                                JSONArray jsonArray = jsonObject.getJSONArray("result");
//////                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
////                                String status = jsonObject.getString("status");
////                                if (status.equals("1")) {
////                                    qty = 0;
//////                                    getProductList();
////                                    rel_no.setVisibility(GONE);
////                                    Toast.makeText(QuickReorderActivity.this, "Item removed from cart successfully", Toast.LENGTH_SHORT).show();
////                                    cartCount();
////                                   /* productId = new ArrayList<>();
////                                    unitArrayList = new ArrayList<>();
////                                    qtyArrayList = new ArrayList<>();
////                                    discountArrayList = new ArrayList<>();*/
////                                   /* adapter.notifyDataSetChanged();
////                                    adapter.notifyItemRemoved(position);*/
////
////                                    /*rel_cart.setVisibility(View.GONE);
////                                    btn_cart.setVisibility(View.VISIBLE);*/
////                                } else {
////                                    Toast.makeText(QuickReorderActivity.this, "Error", Toast.LENGTH_SHORT).show();
////                                }
//////                                dismissDialog();
////                                progressBar.setVisibility(GONE);
////                                txt_count.setVisibility(View.VISIBLE);
////                            } catch (JSONException e) {
//////                                dismissDialog();
////                                progressBar.setVisibility(GONE);
////                                txt_count.setVisibility(View.VISIBLE);
////                                e.printStackTrace();
////                            }
////                        }
////                    }, new Response.ErrorListener() {
////                        @Override
////                        public void onErrorResponse(VolleyError error) {
//////                            dismissDialog();
////                            progressBar.setVisibility(GONE);
////                            txt_count.setVisibility(View.VISIBLE);
////                            error.printStackTrace();
////                        }
////                    }) {
////                        @Override
////                        protected Map<String, String> getParams() throws AuthFailureError {
////                            HashMap<String, String> params = new HashMap<>();
////                            params.put("product_id", product_id);
////                            params.put("customer_id", SharedPref.getVal(QuickReorderActivity.this, SharedPref.customer_id));
////                            return params;
////                        }
////                    };
////
////                    RequestQueue requestQueue = Volley.newRequestQueue(QuickReorderActivity.this);
////                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
////                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
////                    requestQueue.add(stringRequest);
////                } else {
////                    dismissDialog();
////                    FunctionConstant.noInternetDialog(QuickReorderActivity.this, "no internet connection");
////                }
////            }
////        }
////
////
////        public class QuantityListingAdapter extends RecyclerView.Adapter<ProductsListingAdapter.QuantityListingAdapter.ItemViewholder> {
////            Context context;
////            ArrayList<UnitModel> unitModelArrayList;
////
////            TextView txt_mrp, txt_og_price, txt_discount, txt_unit;
////
////            int selected_index = 0;
////
////            public QuantityListingAdapter(Context context, ArrayList<UnitModel> unitModelArrayList, TextView txt_mrp, TextView txt_og_price, TextView txt_discount, TextView txt_unit) {
////                this.context = context;
////                this.unitModelArrayList = unitModelArrayList;
////                this.txt_mrp = txt_mrp;
////                this.txt_og_price = txt_og_price;
////                this.txt_discount = txt_discount;
////                this.txt_unit = txt_unit;
////
////            }
////
////            @NonNull
////            @Override
////            public ProductsListingAdapter.QuantityListingAdapter.ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
////                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_qty_layout, parent, false);
////                ProductsListingAdapter.QuantityListingAdapter.ItemViewholder itemViewholder = new ProductsListingAdapter.QuantityListingAdapter.ItemViewholder(view);
////                return itemViewholder;
////
////            }
////
////            @Override
////            public void onBindViewHolder(@NonNull ProductsListingAdapter.QuantityListingAdapter.ItemViewholder holder, int position) {
////                UnitModel model = unitModelArrayList.get(position);
////                ((ProductsListingAdapter.QuantityListingAdapter.ItemViewholder) holder).bind(model, position);
////            }
////
////            @Override
////            public long getItemId(int position) {
////                return position;
////            }
////
////            @Override
////            public int getItemViewType(int position) {
////                return position;
////            }
////
////            @Override
////            public int getItemCount() {
////                return unitModelArrayList.size();
////
////            }
////
////            public class ItemViewholder extends RecyclerView.ViewHolder {
////
////                TextView txt_unit1;
////                RelativeLayout rel_item;
////
////                public ItemViewholder(@NonNull View itemView) {
////                    super(itemView);
////
////                    txt_unit1 = itemView.findViewById(R.id.txt_unit);
////                    rel_item = itemView.findViewById(R.id.rel_item);
////
////                }
////
////                public void bind(final UnitModel unitModel, final int position) {
////
////                    txt_unit1.setText(unitModel.getUnit());
//////                    for (int i = 0; i <= unitModelArrayList.size(); i++) {
//////                        String str = unitModel.getUnit();
//////                        if (prod_Qty.equals(str)) {
//////                            rel_item.setBackground(getResources().getDrawable(R.drawable.box_edittext_background_colored_layout));
//////                        } else {
//////                            rel_item.setBackground(getResources().getDrawable(R.drawable.box_edittext_background_layout));
//////                        }
//////                    }
////
////                    rel_item.setOnClickListener(new View.OnClickListener() {
////                        @Override
////                        public void onClick(View v) {
////                            itemQty(unitModel.getUnit(), unitModel.getUint_price(), unitModel.getUnit_discount());
////                        }
////                    });
////
////                    if (selected_index == position) {
////                        rel_item.setBackground(getResources().getDrawable(R.drawable.box_edittext_background_colored_layout));
////                        txt_unit1.setTextColor(getResources().getColor(R.color.colorWhite));
////                        itemQty(unitModel.getUnit(), unitModel.getUint_price(), unitModel.getUnit_discount());
////                    } else {
////                        rel_item.setBackground(getResources().getDrawable(R.drawable.box_edittext_background_layout));
////                        txt_unit1.setTextColor(getResources().getColor(R.color.colorBlack));
////                    }
////
////                    rel_item.setOnClickListener(new View.OnClickListener() {
////                        @Override
////                        public void onClick(View v) {
////                            selected_index = position;
////                            notifyDataSetChanged();
////                            itemQty(unitModel.getUnit(), unitModel.getUint_price(), unitModel.getUnit_discount());
////                        }
////                    });
////                }
////
////                public void itemQty(String qty, String price, String discount) {
////                    cart_discount = discount;
////                    cart_unit_price = price;
////                    cart_unit = qty;
////                    cart_click = true;
////                    txt_unit.setText(qty);
////                    Double sprice = Double.parseDouble(price) - Double.parseDouble(discount);
////                    txt_og_price.setText("Sterlex Super Mart \u20B9" + sprice);
////                    if (discount.equals("0")) {
////                        txt_discount.setVisibility(GONE);
////                        txt_mrp.setVisibility(GONE);
////                    } else {
////                        txt_discount.setVisibility(View.VISIBLE);
////                        txt_mrp.setVisibility(View.VISIBLE);
////                        txt_discount.setText("Save \u20B9" + discount);
////                        txt_mrp.setText("MRP \u20B9" + price);
////                    }
////
////
//////                    String discount1 = txt_discount.getText().toString();
//////                    if (!discount1.equals("0")) {
//////                        txt_mrp.setVisibility(View.VISIBLE);
//////                        txt_discount.setVisibility(View.VISIBLE);
//////                    } else {
//////                        txt_mrp.setVisibility(GONE);
//////                        txt_discount.setVisibility(GONE);
//////                    }
////
////
////                }
////            }
//
//        }
//
//
//    }

    public void cartCount() {
        if (APIURLs.isNetwork(QuickReorderActivity.this)) {
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
                            Toast.makeText(QuickReorderActivity.this, "Error", Toast.LENGTH_SHORT).show();
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

                    params.put("customer_id", SharedPref.getVal(QuickReorderActivity.this, SharedPref.customer_id));
//                    params.put("customer_id", "3");

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(QuickReorderActivity.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();

//            final Dialog dialog = new Dialog(QuickReorderActivity.this);
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


    public void dismissDialog() {
       /* if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }*/
        shimmer_view_container.stopShimmerAnimation();
        shimmer_view_container.setVisibility(View.GONE);
        lnr_main.setVisibility(View.VISIBLE);
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
    protected void onResume() {
        super.onResume();
//        shimmer_view_container.startShimmerAnimation();
//        shimmer_view_container.setVisibility(View.VISIBLE);
//        lnr_main.setVisibility(View.GONE);
//        getCategoryList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        shimmer_view_container.stopShimmerAnimation();
        shimmer_view_container.setVisibility(View.GONE);
        lnr_main.setVisibility(View.VISIBLE);
    }

}