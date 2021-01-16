package com.sterlex.in.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.sterlex.in.Constant.APIURLs;
import com.sterlex.in.Constant.SharedPref;
import com.sterlex.in.Model.CouponModel;
import com.sterlex.in.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CouponsActivity extends AppCompatActivity {

    ImageView img_back;
    RecyclerView rec_coupon;
    IOSDialog dialog;
    ArrayList<CouponModel> couponModelArrayList = new ArrayList<>();
    TextView txt_nodata;
    ShimmerFrameLayout shimmer_view_container;
    String cost_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons);
        init();
        cost_id = SharedPref.getVal(CouponsActivity.this, SharedPref.customer_id);
        onClick();
//        getCouponList();
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
        dialog = new IOSDialog.Builder(CouponsActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();
        img_back = findViewById(R.id.img_back);
        rec_coupon = findViewById(R.id.rec_coupon);
        shimmer_view_container = findViewById(R.id.shimmer_view_container);
        txt_nodata = findViewById(R.id.txt_nodata);
        rec_coupon.setLayoutManager(new LinearLayoutManager(CouponsActivity.this));
    }

    public void dismissDialog() {
      /*  new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               *//* if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }*//*

            }
        }, 2000);*/
        shimmer_view_container.stopShimmerAnimation();
        shimmer_view_container.setVisibility(View.GONE);
        rec_coupon.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      /*  if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }*/
        shimmer_view_container.stopShimmerAnimation();
        shimmer_view_container.setVisibility(View.GONE);
        rec_coupon.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        rec_coupon.setVisibility(View.GONE);
        getCouponList();

    }

    @Override
    protected void onPause() {
        super.onPause();
        shimmer_view_container.stopShimmerAnimation();
        shimmer_view_container.setVisibility(View.GONE);
        rec_coupon.setVisibility(View.VISIBLE);
    }

    public void getCouponList() {
//        dialog.show();
        shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        rec_coupon.setVisibility(View.GONE);
        if (APIURLs.isNetwork(CouponsActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.COUPONLIST_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            couponModelArrayList = new ArrayList<>();
                            if (jsonObject.has("result")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    CouponModel model = new CouponModel();
                                    model.setCoupon_id(jsonObject1.getString("coupon_id"));
                                    model.setCoupon_name(jsonObject1.getString("coupon_name"));
                                    model.setDescription(jsonObject1.getString("description"));
                                    model.setCoupon_value(jsonObject1.getString("coupon_value"));
                                    model.setCapping_value(jsonObject1.getString("capping_value"));
                                    couponModelArrayList.add(model);
                                }
                                CouponAdapter adapter = new CouponAdapter(CouponsActivity.this);
                                rec_coupon.setAdapter(adapter);
                                rec_coupon.setVisibility(View.VISIBLE);
                                txt_nodata.setVisibility(View.GONE);
                                dismissDialog();
                            }
                        } else {
                            rec_coupon.setVisibility(View.GONE);
                            txt_nodata.setVisibility(View.VISIBLE);
                            dismissDialog();
                        }

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
                    params.put("customer_id", SharedPref.getVal(CouponsActivity.this, SharedPref.customer_id));

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            final Dialog dialog = new Dialog(CouponsActivity.this);
            dialog.setContentView(R.layout.no_internet_dialog);
            Button button = dialog.findViewById(R.id.btn_process);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCouponList();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> {
        Context context;

        public CouponAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(CouponsActivity.this).inflate(R.layout.coupons_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            CouponModel model = couponModelArrayList.get(position);
            ((ViewHolder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return couponModelArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            /*LinearLayout lnr_coupon;*/
            TextView txt_couponCode, txt_apply, txt_description;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                /*lnr_coupon = itemView.findViewById(R.id.lnr_coupon);*/
                txt_couponCode = itemView.findViewById(R.id.txt_couponCode);
                txt_description = itemView.findViewById(R.id.txt_description);
            }

            public void bind(final CouponModel model, final int position) {

                txt_couponCode.setText(model.getCoupon_name());
                txt_description.setText(model.getDescription());

               /* lnr_coupon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("coupon_name",model.getCoupon_name());
                        intent.putExtra("coupon_id",model.getCoupon_id());
                        intent.putExtra("coupon_value",model.getCapping_value());
                        intent.putExtra("capping_value",model.getCapping_value());
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });*/
            }
        }
    }

}
