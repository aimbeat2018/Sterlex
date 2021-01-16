package com.sterlex.in.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.sterlex.in.Model.ProductModel;
import com.sterlex.in.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderItemsActivity extends AppCompatActivity {

    ImageView img_back;
    TextView txt_orderId;
    RecyclerView rec_list;
    IOSDialog dialog;
    ArrayList<ProductModel> productModelArrayList = new ArrayList<>();
    String oid = "", order_id = "";
    ShimmerFrameLayout shimmer_view_container;
    LinearLayout lnr_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_items);
        init();
        oid = getIntent().getStringExtra("oid");
        order_id = getIntent().getStringExtra("order_id");
        txt_orderId.setText("ORDER #" + order_id);
        onClick();
    }

    private void onClick() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init() {
        dialog = new IOSDialog.Builder(OrderItemsActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();

        img_back = findViewById(R.id.img_back);
        txt_orderId = findViewById(R.id.txt_orderId);
        shimmer_view_container = findViewById(R.id.shimmer_view_container);
        lnr_main = findViewById(R.id.lnr_main);
        rec_list = findViewById(R.id.rec_list);
        rec_list.setLayoutManager(new LinearLayoutManager(OrderItemsActivity.this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        lnr_main.setVisibility(View.GONE);
        getProductList();
    }


    public void dismissDialog() {
        /*if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }*/
        shimmer_view_container.startShimmerAnimation();
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

    @Override
    protected void onPause() {
        super.onPause();
        shimmer_view_container.stopShimmerAnimation();
        shimmer_view_container.setVisibility(View.GONE);
        lnr_main.setVisibility(View.VISIBLE);
    }


    public void getProductList() {
//        dialog.show();
        shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        lnr_main.setVisibility(View.GONE);
        if (APIURLs.isNetwork(OrderItemsActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.order_wise_item_history, new Response.Listener<String>() {
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
                                model.setProduct_name(jsonObject1.getString("product_name"));
                                model.setImage(jsonObject1.getString("main_image"));
                                model.setQty(jsonObject1.getString("qty"));
                                model.setPrice(jsonObject1.getString("total_price"));
                                model.setUnit(jsonObject1.getString("unit"));
                                productModelArrayList.add(model);
                            }
                            ProductAdapter adapter = new ProductAdapter(OrderItemsActivity.this);
                            rec_list.setAdapter(adapter);
                            rec_list.setVisibility(View.VISIBLE);
                        } else {
                            rec_list.setVisibility(View.GONE);
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
                    params.put("oid", oid);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            final Dialog dialog = new Dialog(OrderItemsActivity.this);
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

    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
        Context context;
        int lastPosition = -1;

        public ProductAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(OrderItemsActivity.this).inflate(R.layout.order_product_item_layout, parent, false);
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
            TextView txt_productName, txt_weight, txt_discountprice, txt_qty;
            CardView card_list;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                img_product = itemView.findViewById(R.id.img_product);
                txt_productName = itemView.findViewById(R.id.txt_productName);
                txt_weight = itemView.findViewById(R.id.txt_weight);
                txt_qty = itemView.findViewById(R.id.txt_qty);
                txt_discountprice = itemView.findViewById(R.id.txt_discountprice);
                card_list = itemView.findViewById(R.id.card_list);
            }

            public void bind(final ProductModel model, final int position) {
                // If the bound view wasn't previously displayed on screen, it's animated
                if (position < lastPosition) {
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                    card_list.startAnimation(animation);
                    lastPosition = position;
                }
                Glide.with(OrderItemsActivity.this).load(model.getImage()).into(img_product);
                txt_productName.setText(model.getProduct_name());
                txt_weight.setText(model.getUnit());
                txt_discountprice.setText("Rs." + model.getPrice());
                txt_qty.setText("Quantity : " + model.getQty());

            }
        }
    }
}
