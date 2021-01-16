package com.sterlex.in.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.android.material.snackbar.Snackbar;
import com.sterlex.in.Constant.APIURLs;
import com.sterlex.in.Constant.SharedPref;
import com.sterlex.in.Model.OrderModel;
import com.sterlex.in.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MyOrderActivity extends AppCompatActivity {

    RecyclerView rec_order;
    ImageView img_back;
    IOSDialog dialog;
    ArrayList<OrderModel> orderModelArrayList = new ArrayList<>();
    TextView txt_nodata;
    ShimmerFrameLayout shimmer_view_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        init();
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
        dialog = new IOSDialog.Builder(MyOrderActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();
        img_back = findViewById(R.id.img_back);
        shimmer_view_container = findViewById(R.id.shimmer_view_container);
        txt_nodata = findViewById(R.id.txt_nodata);
        rec_order = findViewById(R.id.rec_order);
        rec_order.setLayoutManager(new LinearLayoutManager(MyOrderActivity.this));
    }

    public void dismissDialog() {
        shimmer_view_container.stopShimmerAnimation();
        shimmer_view_container.setVisibility(View.GONE);
        rec_order.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      /*  if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }*/
        shimmer_view_container.stopShimmerAnimation();
        shimmer_view_container.setVisibility(View.GONE);
        rec_order.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        rec_order.setVisibility(View.GONE);
        getOrderList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        shimmer_view_container.stopShimmerAnimation();
        shimmer_view_container.setVisibility(View.GONE);
        rec_order.setVisibility(View.VISIBLE);
    }

    public void getOrderList() {
//        dialog.show();
        shimmer_view_container.startShimmerAnimation();
        rec_order.setVisibility(View.GONE);
        shimmer_view_container.setVisibility(View.VISIBLE);

        if (APIURLs.isNetwork(MyOrderActivity.this)) {
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
                                rec_order.setAdapter(adapter);
                                rec_order.setVisibility(View.VISIBLE);
                                txt_nodata.setVisibility(View.GONE);
                            }
                        } else {
                            rec_order.setVisibility(View.GONE);
                            txt_nodata.setVisibility(View.VISIBLE);
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
                    params.put("customer_id", SharedPref.getVal(MyOrderActivity.this, SharedPref.customer_id));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            final Dialog dialog = new Dialog(MyOrderActivity.this);
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
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MyOrderActivity.this).inflate(R.layout.my_order_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            OrderModel model = orderModelArrayList.get(position);
            ((ViewHolder) holder).bind(model, position);
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
                    txt_cancel_order.setVisibility(View.VISIBLE);
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
                        Intent intent = new Intent(MyOrderActivity.this, OrderTrackingActivity.class);
                        intent.putExtra("order_id", model.getOid());
                        startActivity(intent);
                    }
                });

                txt_cancel_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MyOrderActivity.this, CancelOrderActivity.class);
                        intent.putExtra("oid", model.getOid());
                        startActivity(intent);
                    }
                });
            }
        }

    }
}
