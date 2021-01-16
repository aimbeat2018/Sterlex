package com.sterlex.in.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.google.android.material.snackbar.Snackbar;
import com.like.LikeButton;
import com.mlsdev.animatedrv.AnimatedRecyclerView;
import com.sterlex.in.Constant.APIURLs;
import com.sterlex.in.Constant.FunctionConstant;
import com.sterlex.in.Constant.SharedPref;
import com.sterlex.in.Model.ProductModel;
import com.sterlex.in.Model.UnitModel;
import com.sterlex.in.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;

public class WishListActivity extends AppCompatActivity {

    ShimmerFrameLayout shimmer_view_container;
    AnimatedRecyclerView rec_list;
    LinearLayout lin_nodata;

    ArrayList<ProductModel> productModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        init();

        getProductList();
    }

    private void init() {
        rec_list = findViewById(R.id.rec_list);
        lin_nodata = findViewById(R.id.lin_nodata);
        shimmer_view_container = findViewById(R.id.shimmer_view_container);
    }


    public void getProductList() {
//        dialog.show();
        shimmer_view_container.startShimmerAnimation();
        rec_list.setVisibility(GONE);
        shimmer_view_container.setVisibility(View.VISIBLE);
        lin_nodata.setVisibility(GONE);

        if (APIURLs.isNetwork(WishListActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.wishlist_listing, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            productModelArrayList = new ArrayList<>();

                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                ProductModel model = new ProductModel();
                                model.setProduct_id(jsonObject1.getString("product_id"));
                                model.setProduct_name(jsonObject1.getString("product_name"));
                                model.setImage(jsonObject1.getString("image"));
                                model.setUnit(jsonObject1.getString("unit"));
//                                model.setType(jsonObject1.getString("type"));
                                model.setSave_id(jsonObject1.getString("save_id"));
                                model.setPrice(jsonObject1.getString("price"));
                                model.setDiscount(jsonObject1.getString("discount"));

//                                JSONArray jsonArray1 = jsonObject1.getJSONArray("product_unit");
//                                JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
//                                model.setUnit(jsonObject2.getString("unit"));
//                                model.setPrice(jsonObject2.getString("uint_price"));
//                                model.setDiscount(jsonObject2.getString("unit_discount"));
//                                model.setGst(jsonObject2.getString("unit_gst"));

                                productModelArrayList.add(model);
                            }
                            Collections.reverse(productModelArrayList);
                            ProductsListingAdapter adapter = new ProductsListingAdapter(WishListActivity.this);
                            rec_list.setAdapter(adapter);

                            rec_list.setVisibility(View.VISIBLE);
                            lin_nodata.setVisibility(GONE);
                        } else {
                            rec_list.setVisibility(GONE);
                            lin_nodata.setVisibility(View.VISIBLE);
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
                    params.put("customer_id", SharedPref.getVal(WishListActivity.this, SharedPref.customer_id));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            final Dialog dialog = new Dialog(WishListActivity.this);
            dialog.setContentView(R.layout.no_internet_dialog);
            Button button = dialog.findViewById(R.id.btn_process);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getProductList();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }


    public class ProductsListingAdapter extends RecyclerView.Adapter<ProductsListingAdapter.ItemViewholder> {
        Context context;
        ArrayList<UnitModel> product_unitArraylist = new ArrayList<>();
        Double actualAmout;
        Double discount;
        Double payableAmt;

        public ProductsListingAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ProductsListingAdapter.ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wishlist, parent, false);
            ProductsListingAdapter.ItemViewholder itemViewholder = new ProductsListingAdapter.ItemViewholder(view);
            return itemViewholder;
        }

        @Override
        public void onBindViewHolder(@NonNull ProductsListingAdapter.ItemViewholder holder, int position) {
            ProductModel model = productModelArrayList.get(position);
            ((ProductsListingAdapter.ItemViewholder) holder).bind(model, position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return productModelArrayList.size();
        }

        public class ItemViewholder extends RecyclerView.ViewHolder {
            ImageView iv, iv_veg, iv_nonveg;

            LinearLayout lin_item, lin_comm;
            CardView card_unit, card_add_toCart, card_remove_wishlist;
            RelativeLayout rel_veg_nonveg, rel_cart1, rel_no;
            TextView txt_name, txt_mrp, txt_og_price, txt_discount, txt_count, txt_cart_items;
            TextView txt_unit;
            Button btn_cart;
            int flag_qty = 0, qty_open_close_flag = 0;
            RelativeLayout rel_qty, rel_stock, rel_bottomcart, rel_discount, rel_cart, rel_minus, rel_plus, rel_subscribe, rel_subscribeminus, rel_subscribeplus;
            RecyclerView recy_qty;
            double ogPrice = 0.0, price = 0.0, discount = 0.0, totalPrice = 0.0;
            ProgressBar progressBar;

            public ItemViewholder(@NonNull View itemView) {
                super(itemView);

                iv = itemView.findViewById(R.id.iv);
                iv_veg = itemView.findViewById(R.id.iv_veg);
                iv_nonveg = itemView.findViewById(R.id.iv_nonveg);
                rel_veg_nonveg = itemView.findViewById(R.id.rel_veg_nonveg);
                rel_cart1 = itemView.findViewById(R.id.rel_cart1);
                rel_cart = itemView.findViewById(R.id.rel_cart);
                rel_stock = itemView.findViewById(R.id.rel_stock);
                rel_minus = itemView.findViewById(R.id.rel_minus);
                rel_plus = itemView.findViewById(R.id.rel_plus);
                rel_no = itemView.findViewById(R.id.rel_no);
                txt_cart_items = itemView.findViewById(R.id.txt_cart_items);
                rel_qty = itemView.findViewById(R.id.rel_qty);
                card_unit = itemView.findViewById(R.id.card_unit);
                card_remove_wishlist = itemView.findViewById(R.id.card_remove_wishlist);
                btn_cart = itemView.findViewById(R.id.btn_cart);
                recy_qty = itemView.findViewById(R.id.recy_qty);
                txt_name = itemView.findViewById(R.id.txt_name);
                txt_mrp = itemView.findViewById(R.id.txt_mrp);
                txt_og_price = itemView.findViewById(R.id.txt_og_price);
                txt_discount = itemView.findViewById(R.id.txt_discount);
                txt_unit = itemView.findViewById(R.id.txt_unit);
                lin_item = itemView.findViewById(R.id.lin_item);
                txt_count = itemView.findViewById(R.id.txt_count);
                progressBar = itemView.findViewById(R.id.progressBar);

                card_unit.setVisibility(GONE);
                rel_cart1.setVisibility(GONE);
                txt_mrp.setVisibility(View.VISIBLE);
                txt_discount.setVisibility(View.VISIBLE);

                rel_bottomcart = itemView.findViewById(R.id.rel_cartmain);
                txt_mrp.setPaintFlags(txt_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


            }

            public void bind(final ProductModel model, final int position) {

//                if (model.getIs_veg().equals("1")) {
//                    iv_veg.setVisibility(View.VISIBLE);
//
//                } else if (model.getIs_veg().equals("0")) {
//                    iv_nonveg.setVisibility(View.VISIBLE);
//                } else {
//                    iv_nonveg.setVisibility(View.GONE);
//                    iv_veg.setVisibility(View.GONE);
//
//                }

//                if (model.getFlag().equals("0")) {
//                    rel_stock.setVisibility(View.VISIBLE);
//                } else if (model.getFlag().equals("1")) {
//                    rel_stock.setVisibility(GONE);
//                }


//             if (model.getType().equals("0")) {
//                    rel_subs.setVisibility(GONE);
//                    rel_cartmain.setVisibility(View.VISIBLE);
//                } else {
//                    rel_subs.setVisibility(View.VISIBLE);
//                    rel_cartmain.setVisibility(GONE);
//                }

//                if (model.getQty().equals("0")) {
//                    rel_cart.setVisibility(GONE);
//                    btn_cart.setVisibility(View.VISIBLE);
//                } else {
//                    rel_cart.setVisibility(View.VISIBLE);
//                    btn_cart.setVisibility(GONE);
//                    rel_no.setVisibility(View.VISIBLE);
//                    txt_cart_items.setText(model.getQty());
//                    txt_count.setText(model.getQty());
//
//                }

                try {
                    if (model.getDiscount().equals("0")) {
//                       /* double actualamt = Double.parseDouble(model.getPrice()) * Double.parseDouble(model.getQty());
//                        DecimalFormat twoDForm = new DecimalFormat("#");
//                        actualamt = Double.valueOf(twoDForm.format(actualamt));*/
//
                        txt_og_price.setText("MRP \u20B9" + model.getPrice());
                        txt_mrp.setVisibility(View.INVISIBLE);
//                        rel_discount.setVisibility(View.GONE);
                        txt_discount.setVisibility(View.INVISIBLE);
                    } else {
                        txt_mrp.setVisibility(View.VISIBLE);
//                        rel_discount.setVisibility(View.VISIBLE);
                        txt_discount.setVisibility(View.VISIBLE);

                        price = Double.valueOf(model.getPrice());
                        discount = Double.valueOf(model.getDiscount());
                        totalPrice = price - discount;
                        txt_mrp.setText("MRP \u20B9" + price);
                        txt_og_price.setText("Sterlex Super Mart \u20B9" + totalPrice);
                        txt_discount.setText("Save \u20B9" + discount);
////                        actualAmout = Double.parseDouble(model.getPrice());
//                       /* discount = (actualAmout * Double.parseDouble(model.getDiscount())) / 100;
//                        payableAmt = actualAmout - discount;
//                        DecimalFormat twoDForm = new DecimalFormat("#");
//                        payableAmt = Double.valueOf(twoDForm.format(payableAmt));
//
//                        txt_discountprice.setText("Rs." + (int) Math.round(payableAmt));
//                        txt_mrp.setText("Rs." + model.getPrice());
//                        txt_discount.setText(model.getDiscount() + "% off");*/
//
////                        payableAmt = actualAmout - Double.parseDouble(model.getDiscount());
//                        if (!model.getQty().equals("0")) {
//                            double actualamt = Double.parseDouble(model.getPrice()) * Double.parseDouble(model.getQty());
//                            DecimalFormat twoDForm = new DecimalFormat("#");
//                            actualamt = Double.valueOf(twoDForm.format(actualamt));
//                            txt_mrp.setText("MRP \u20b9" + (int) Math.round(actualamt));
//
//
//                            double payableAmt = Double.parseDouble(model.getPrice()) - Double.parseDouble(model.getDiscount());
//                            double finalpayamt = payableAmt * Double.parseDouble(model.getQty());
//                            DecimalFormat twoDForm1 = new DecimalFormat("#");
//                            finalpayamt = Double.valueOf(twoDForm1.format(finalpayamt));
//                            txt_og_price.setText("Sterlex Super Mart \u20b9" + (int) Math.round(finalpayamt));
//
////                        txt_mrp.setText("MRP \u20B9" + model.getPrice());
////                        txt_discount.setText("Save\n\u20B9" + model.getDiscount());
//                            double saveamt = Double.parseDouble(model.getDiscount()) * Double.parseDouble(model.getQty());
//                            DecimalFormat twoDForm2 = new DecimalFormat("#");
//                            saveamt = Double.valueOf(twoDForm2.format(saveamt));
//                            txt_discount.setText("Save \u20B9" + (int) Math.round(saveamt));
//                        } else {
//                            actualAmout = Double.parseDouble(model.getPrice());
//                            payableAmt = actualAmout - Double.parseDouble(model.getDiscount());
//                            DecimalFormat twoDForm1 = new DecimalFormat("#");
//                            payableAmt = Double.valueOf(twoDForm1.format(payableAmt));
//                            txt_og_price.setText("Sterlex Super Mart \u20b9" + (int) Math.round(payableAmt));
//                            txt_mrp.setText("MRP \u20B9" + model.getPrice());
////                            txt_discount.setText("Save\n\u20B9" + model.getDiscount());
//                            txt_discount.setText("Save \u20B9" + model.getDiscount());
//                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                price = Double.valueOf(model.getPrice());
                discount = Double.valueOf(model.getDiscount());
                totalPrice = price - discount;
                Glide.with(WishListActivity.this).load(model.getImage()).error(R.drawable.loader).into(iv);

                txt_name.setText(model.getProduct_name());
                txt_mrp.setText("MRP \u20B9" + price);
                txt_og_price.setText("Sterlex Super Mart \u20B9" + totalPrice);
                txt_discount.setText("Save \u20B9" + discount);
                txt_unit.setText(model.getUnit());
//
//                product_unitArraylist = new ArrayList<>();
//                product_unitArraylist = model.getProduct_unitArraylist();
//                try {
//                    if (product_unitArraylist != null) {
//                        if (product_unitArraylist.size() > 1) {
//                            card_unit.setVisibility(View.VISIBLE);
//                            recy_qty.setVisibility(GONE);
//
//                            flag_qty = 1;
//
//
//                        } else {
//                            flag_qty = 0;
//                            card_unit.setVisibility(GONE);
//                            recy_qty.setVisibility(GONE);
//                        }
//                    } else {
//                        flag_qty = 0;
//                        card_unit.setVisibility(GONE);
//                        recy_qty.setVisibility(GONE);
//
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    flag_qty = 0;
//                    card_unit.setVisibility(GONE);
//                    recy_qty.setVisibility(GONE);
//
//                }

                card_remove_wishlist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeWishList(model.getSave_id(), position);

//                        notifyDataSetChanged();
                    }
                });
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(WishListActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });
                rel_veg_nonveg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(WishListActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });
                txt_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(WishListActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });
                txt_mrp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(WishListActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });
                txt_og_price.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(WishListActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });
                txt_discount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(WishListActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });


            }
        }


        /*Remove from wishlist*/
        public void removeWishList(final String product_id, final int position) {
            if (APIURLs.isNetwork(WishListActivity.this)) {
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
                                notifyDataSetChanged();
                                notifyItemRemoved(position);
                                productModelArrayList.remove(position);
                                getProductList();
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

                RequestQueue requestQueue = Volley.newRequestQueue(WishListActivity.this);
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);
            } else {
                Toast.makeText(WishListActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void dismissDialog() {
      /*  if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }*/
        shimmer_view_container.stopShimmerAnimation();
        rec_list.setVisibility(View.VISIBLE);
        shimmer_view_container.setVisibility(GONE);
    }
}