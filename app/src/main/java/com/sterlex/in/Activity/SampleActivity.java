package com.sterlex.in.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.sterlex.in.Constant.APIURLs;
import com.sterlex.in.Model.BannerModel;
import com.sterlex.in.Model.CategoryModel;
import com.sterlex.in.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SampleActivity extends AppCompatActivity {

    IOSDialog dialog;
    LinearLayout parentLayout;
    ShimmerFrameLayout shimmer_view_container;
    ArrayList<BannerModel> bannerModelArrayList = new ArrayList<>();
    ArrayList<CategoryModel> categoryModels = new ArrayList<>();
    RecyclerView recyc_topCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        init();


    }

    private void init() {

//        parentLayout = findViewById(R.id.lnr_main);
        shimmer_view_container = findViewById(R.id.shimmer_view_container);

        dialog = new IOSDialog.Builder(SampleActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();

        parentLayout = (LinearLayout) findViewById(R.id.parent);
        TextView textView = new TextView(SampleActivity.this);
        textView.setText("Child First Row");
        textView.setAllCaps(true);
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        parentLayout.addView(textView);
        TextView textView1 = new TextView(SampleActivity.this);
        textView1.setText("Child Row");
        textView1.setAllCaps(true);
        textView1.setGravity(View.TEXT_ALIGNMENT_CENTER);
        parentLayout.addView(textView1);


        recyc_topCategory = new RecyclerView(SampleActivity.this);
//        recyclerView.isNestedScrollingEnabled();
        recyc_topCategory.setLayoutManager(new LinearLayoutManager(SampleActivity.this, RecyclerView.HORIZONTAL, false));

        getCategoryList();
        parentLayout.addView(recyc_topCategory);
        parentLayout.removeView(textView);
        parentLayout.addView(textView);

    }


    public void getCategoryList() {
        dialog.show();
        shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        parentLayout.setVisibility(View.GONE);
        if (APIURLs.isNetwork(SampleActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.HOME_SECTION_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("staus");
                        String message = jsonObject.getString("message");
                        if (status.equals("1")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                if (jsonObject1.has("category")) {

                                    JSONObject jsonObject2 = jsonObject1.getJSONObject("category");
                                    if (jsonObject2.length() > 0) {
                                        categoryModels = new ArrayList<>();

                                        JSONArray jsonArray1 = jsonObject2.getJSONArray("result");
                                        for (int j = 0; j < jsonArray1.length(); j++) {

                                            JSONObject jsonObject3 = jsonArray1.getJSONObject(j);
                                            CategoryModel model = new CategoryModel();
                                            model.setCategory_id(jsonObject3.getString("category_id"));
                                            model.setCategory_name(jsonObject3.getString("category_name"));
                                            model.setImage(jsonObject3.getString("image"));
                                            model.setSubcategorey(jsonObject3.getString("subcategorey"));
                                            model.setColor(jsonObject3.getString("color"));
                                            categoryModels.add(model);
                                        }
                                        TopCategoryListingAdapter adapter = new TopCategoryListingAdapter(SampleActivity.this);
                                        recyc_topCategory.setAdapter(adapter);
//                                        setContentView(view1);

                                    }
                                } else if (jsonObject1.has("banner")) {
                                    JSONObject bannerObject = jsonObject1.getJSONObject("banner");
                                    if (bannerObject.length() > 0) {
                                        bannerModelArrayList = new ArrayList<>();

                                        JSONArray jsonArray1 = bannerObject.getJSONArray("result");
                                        for (int j = 0; j < jsonArray1.length(); j++) {

                                            JSONObject jsonObject4 = jsonArray1.getJSONObject(j);
                                            BannerModel model = new BannerModel();
                                            model.setBanner_id(jsonObject4.getString("banner_id"));
                                            model.setBanner(jsonObject4.getString("banner"));
                                            model.setCategory_id(jsonObject4.getString("category_id"));
                                            bannerModelArrayList.add(model);
                                        }
//                                    TopCategoryListingAdapter adapter = new TopCategoryListingAdapter(SampleActivity.this);
//                                    rec_shop_by_category.setAdapter(adapter);

                                    }
                                }
                            }

                        } else {
//                            ErrorToast("Error"); //Error Toast
                            Toast.makeText(SampleActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
//                    if (!cost_id.equals("")) {
//                        params.put("franchise_id", SharedPref.getVal(SampleActivity.this, SharedPref.fran_code));
//                    } else {
//                    params.put("franchise_id", "1");
//                    }
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
//            FunctionConstant.noInternetDialog(SampleActivity.this, "no internet connection");
//            final Dialog dialog = new Dialog(SampleActivity.this);
//            dialog.setContentView(R.layout.no_internet_dialog);
//            Button button = dialog.findViewById(R.id.btn_process);
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    getCategoryList();
//                    dialog.dismiss();
//                }
//            });
//            dialog.show();

        }
    }


    public class TopCategoryListingAdapter extends RecyclerView.Adapter<TopCategoryListingAdapter.ItemViewholder> {
        Context context;


        public TopCategoryListingAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public TopCategoryListingAdapter.ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_category_list_home_layout, parent, false);
            TopCategoryListingAdapter.ItemViewholder itemViewholder = new TopCategoryListingAdapter.ItemViewholder(view);
            return itemViewholder;
        }

        @Override
        public void onBindViewHolder(@NonNull TopCategoryListingAdapter.ItemViewholder holder, int position) {
            CategoryModel model = categoryModels.get(position);
            ((TopCategoryListingAdapter.ItemViewholder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return categoryModels.size();
        }

        public class ItemViewholder extends RecyclerView.ViewHolder {
            ImageView iv;

            LinearLayout lin_item, lin_comm;
            CardView card_bg;
            RelativeLayout lin_owner;
            TextView txt_name;

            public ItemViewholder(@NonNull View itemView) {
                super(itemView);

//                card_bg = itemView.findViewById(R.id.card_bg);
//                lin_item = itemView.findViewById(R.id.lin_item);
                iv = itemView.findViewById(R.id.iv);
                txt_name = itemView.findViewById(R.id.txt_name);
//                txt_name = itemView.findViewById(R.id.txt_name);
            }

            public void bind(final CategoryModel model, final int position) {
                txt_name.setText("");
//
                txt_name.setText(model.getCategory_name());
                Glide.with(SampleActivity.this).load(model.getImage()).into(iv);

//
//                lin_item.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(this, GroceriesListingActivity.class);
////                        intent.putExtra("id", "id");
//                        startActivity(intent);
//                    }
//                });


            }

        }

    }


    /*Slider Adapter*/
//    public class ViewPagerAdapter extends PagerAdapter {
//
//        @NonNull
//        @Override
//        public Object instantiateItem(@NonNull ViewGroup container, int position) {
//            LayoutInflater inflater = LayoutInflater.from(SampleActivity.this);
//            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.viewpager_item_layout, container, false);
//            ImageView img_banner = layout.findViewById(R.id.img_banner);
//            Glide.with(SampleActivity.this).load(bannerModelArrayList.get(position).getBanner())
//                    .error(R.drawable.loader).placeholder(R.drawable.loader).into(img_banner);
//            container.addView(layout);
//            return layout;
//        }
//
//        @Override
//        public int getCount() {
//            return bannerModelArrayList.size();
//        }
//
//        @Override
//        public void destroyItem(ViewGroup collection, int position, Object view) {
//            collection.removeView((View) view);
//        }
//
//
//        @Override
//        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
//            return view == object;
//        }
//    }


    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        shimmer_view_container.stopShimmerAnimation();
        parentLayout.setVisibility(View.VISIBLE);
        shimmer_view_container.setVisibility(View.GONE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        shimmer_view_container.stopShimmerAnimation();
        parentLayout.setVisibility(View.VISIBLE);
        shimmer_view_container.setVisibility(View.GONE);
    }


    @Override
    protected void onPause() {
        shimmer_view_container.stopShimmerAnimation();
        parentLayout.setVisibility(View.VISIBLE);
        shimmer_view_container.setVisibility(View.GONE);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        shimmer_view_container.startShimmerAnimation();
        parentLayout.setVisibility(View.GONE);
        shimmer_view_container.setVisibility(View.VISIBLE);
    }
}