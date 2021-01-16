package com.sterlex.in.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sterlex.in.Constant.APIURLs;
import com.sterlex.in.Constant.SharedPref;
import com.sterlex.in.Model.OfferModel;
import com.sterlex.in.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OffersActivity extends AppCompatActivity {

    ImageView img_back;
    TextView txt_nodata;
    ShimmerFrameLayout shimmer_view_container;
    LinearLayout lnr_main;
    RecyclerView rec_list;
    ArrayList<OfferModel> offerModelArrayList = new ArrayList<>();
    String cost_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        init();
        cost_id = SharedPref.getVal(OffersActivity.this, SharedPref.customer_id);
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
        img_back = findViewById(R.id.img_back);
        txt_nodata = findViewById(R.id.txt_nodata);
        shimmer_view_container = findViewById(R.id.shimmer_view_container);
        lnr_main = findViewById(R.id.lnr_main);
        rec_list = findViewById(R.id.rec_list);
        rec_list.setLayoutManager(new LinearLayoutManager(OffersActivity.this));
    }

    @Override
    protected void onPause() {
        shimmer_view_container.stopShimmerAnimation();
        lnr_main.setVisibility(View.VISIBLE);
        shimmer_view_container.setVisibility(View.GONE);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        shimmer_view_container.startShimmerAnimation();
        lnr_main.setVisibility(View.GONE);
        shimmer_view_container.setVisibility(View.VISIBLE);
        getOffers();
    }

    public void dismissDialog() {
       /* if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }*/

        shimmer_view_container.stopShimmerAnimation();
        lnr_main.setVisibility(View.VISIBLE);
        shimmer_view_container.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shimmer_view_container.stopShimmerAnimation();
        lnr_main.setVisibility(View.VISIBLE);
        shimmer_view_container.setVisibility(View.GONE);
    }

    public void getOffers() {
//        dialog.show();
        if (APIURLs.isNetwork(OffersActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.offer_list, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            offerModelArrayList = new ArrayList<>();
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                OfferModel model = new OfferModel();
                                model.setOffer_id(jsonObject1.getString("offer_id"));
                                model.setImage(jsonObject1.getString("image"));
                                offerModelArrayList.add(model);
                            }
                            OffersAdapter adapter = new OffersAdapter();
                            rec_list.setAdapter(adapter);
                            rec_list.setVisibility(View.VISIBLE);
                            txt_nodata.setVisibility(View.GONE);
                        } else {
                            rec_list.setVisibility(View.GONE);
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

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            final Dialog dialog = new Dialog(OffersActivity.this);
            dialog.setContentView(R.layout.no_internet_dialog);
            Button button = dialog.findViewById(R.id.btn_process);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getOffers();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(OffersActivity.this).inflate(R.layout.offer_list_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final OfferModel model = offerModelArrayList.get(position);
            ((ViewHolder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return offerModelArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_offer;
            RelativeLayout rel_offers;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                img_offer = itemView.findViewById(R.id.img_offer);
                rel_offers = itemView.findViewById(R.id.rel_offers);
            }

            public void bind(final OfferModel model, final int position) {
                Picasso.with(OffersActivity.this).load(model.getImage())
                        .error(R.drawable.loader)
                        .placeholder(R.drawable.loader)
                        .into(img_offer);

                rel_offers.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(OffersActivity.this, OffersProductListActivity.class);
                        intent.putExtra("offer_id", model.getOffer_id());
                        startActivity(intent);
                    }
                });
            }
        }
    }
}
