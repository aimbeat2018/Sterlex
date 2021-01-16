package com.sterlex.in.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.sterlex.in.Constant.APIURLs;
import com.sterlex.in.Constant.SharedPref;
import com.sterlex.in.Model.CategoryModel;
import com.sterlex.in.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CategoriesActivity extends AppCompatActivity {

    ArrayList<CategoryModel> categoryModelArrayList = new ArrayList<>();
    ArrayList<CategoryModel> subcategoryModelArrayList = new ArrayList<>();
    ShimmerFrameLayout shimmer_view_container;
    RecyclerView rec_list;
    String cost_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        init();
        cost_id = SharedPref.getVal(CategoriesActivity.this, SharedPref.customer_id);
    }

    private void init() {
        shimmer_view_container = findViewById(R.id.shimmer_view_container);
        rec_list = findViewById(R.id.rec_list);
        rec_list.setLayoutManager(new LinearLayoutManager(CategoriesActivity.this));
    }

    public void onBack(View view) {
        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        rec_list.setVisibility(View.GONE);
        getCategoryList();
    }

    public void dismissDialog() {
       /* if(dialog!=null && dialog.isShowing()){
            dialog.dismiss();
        }*/

        shimmer_view_container.stopShimmerAnimation();
        shimmer_view_container.setVisibility(View.GONE);
        rec_list.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      /*  if(dialog!=null && dialog.isShowing()){
            dialog.dismiss();
        }*/
        shimmer_view_container.stopShimmerAnimation();
        shimmer_view_container.setVisibility(View.GONE);
        rec_list.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        shimmer_view_container.stopShimmerAnimation();
        shimmer_view_container.setVisibility(View.GONE);
        rec_list.setVisibility(View.VISIBLE);
    }


    public void getCategoryList() {
//        dialog.show();
        shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        rec_list.setVisibility(View.GONE);
        if (APIURLs.isNetwork(CategoriesActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.get_cats, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            categoryModelArrayList = new ArrayList<>();
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                CategoryModel model = new CategoryModel();
                                model.setCategory_id(jsonObject1.getString("category_id"));
                                model.setCategory_name(jsonObject1.getString("category_name"));
                                model.setImage(jsonObject1.getString("image"));
                                model.setSubcategorey(jsonObject1.getString("subcategorey"));
                                model.setColor(jsonObject1.getString("color"));
                                categoryModelArrayList.add(model);
                            }
                            CategoryAdapter adapter = new CategoryAdapter(CategoriesActivity.this);
                            rec_list.setAdapter(adapter);
                        } else {
//                            ErrorToast("Error"); //Error Toast
                            Toast.makeText(CategoriesActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
            final Dialog dialog = new Dialog(CategoriesActivity.this);
            dialog.setContentView(R.layout.no_internet_dialog);
            Button button = dialog.findViewById(R.id.btn_process);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCategoryList();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ItemViewholder> {
        Context context;

        public CategoryAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_item_layout, parent, false);
            ItemViewholder itemViewholder = new ItemViewholder(view);
            return itemViewholder;
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewholder holder, int position) {
            CategoryModel model = categoryModelArrayList.get(position);
            ((ItemViewholder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return categoryModelArrayList.size();
        }

        public class ItemViewholder extends RecyclerView.ViewHolder {
            ImageView img_category, img_down, img_up;
            TextView txt_categoryName;
            RelativeLayout rel_arrow;
            LinearLayout lnr_expand, lnr_details;
            RecyclerView rec_list;
            View view1;
            CardView card_item;

            public ItemViewholder(@NonNull View itemView) {
                super(itemView);
                img_category = itemView.findViewById(R.id.img_category);
                txt_categoryName = itemView.findViewById(R.id.txt_categoryName);
                rel_arrow = itemView.findViewById(R.id.rel_arrow);
                img_down = itemView.findViewById(R.id.img_down);
                img_up = itemView.findViewById(R.id.img_up);
                lnr_expand = itemView.findViewById(R.id.lnr_expand);
                rec_list = itemView.findViewById(R.id.rec_list);
                view1 = itemView.findViewById(R.id.view1);
                rec_list.setLayoutManager(new LinearLayoutManager(CategoriesActivity.this));
                lnr_details = itemView.findViewById(R.id.lnr_details);
                card_item = itemView.findViewById(R.id.card_item);
            }

            public void bind(final CategoryModel model, int position) {
                txt_categoryName.setText(model.getCategory_name());

                if (model.getSubcategorey().equals("1")) {
                    rel_arrow.setVisibility(View.VISIBLE);
                }else{
                    rel_arrow.setVisibility(View.GONE);
                }
                Glide.with(CategoriesActivity.this)
                        .load(model.getImage())
                        .error(R.drawable.loader).
                        placeholder(R.drawable.loader)
                        .into(img_category);

                if (!model.getColor().equals("")) {
                    card_item.setCardBackgroundColor(Color.parseColor(model.getColor()));
                }

                lnr_details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (model.getSubcategorey().equals("1")) {
                            if (img_down.getVisibility() == View.VISIBLE) {
                                img_down.setVisibility(View.GONE);
                                img_up.setVisibility(View.VISIBLE);
                                lnr_expand.setVisibility(View.VISIBLE);
                                getSubCategory(model.getCategory_id());
                            } else {
                                img_down.setVisibility(View.VISIBLE);
                                img_up.setVisibility(View.GONE);
                                lnr_expand.setVisibility(View.GONE);
                            }
                        }else{
                            Intent intent = new Intent(CategoriesActivity.this, CategoryWiseProductListActivity.class);
                            intent.putExtra("sub_cat_id", model.getCategory_id());
                            intent.putExtra("sub_cat_name", model.getCategory_name());
                            intent.putExtra("from", "cat");
                            startActivity(intent);
                        }
                    }
                });
               /* card_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (model.getSubcategorey().equals("1")) {
                            Intent intent = new Intent(CategoriesActivity.this, CategoriesActivity.class);
                            intent.putExtra("cat_id", model.getCategory_id());
                            intent.putExtra("cat_name", model.getCategory_name());
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(CategoriesActivity.this, CategoryWiseProductListActivity.class);
                            intent.putExtra("sub_cat_id", model.getCategory_id());
                            intent.putExtra("sub_cat_name", model.getCategory_name());
                            intent.putExtra("from", "cat");
                            startActivity(intent);
                        }
                    }
                });*/
            }

            public void getSubCategory(final String cat_id) {
//        dialog.show();
               /* shimmer_view_container.startShimmerAnimation();
                shimmer_view_container.setVisibility(View.VISIBLE);
                rec_list.setVisibility(View.GONE);*/

                if (APIURLs.isNetwork(CategoriesActivity.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.SUBCATEGORY_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                if (status.equals("1")) {
                                    subcategoryModelArrayList = new ArrayList<>();
                                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        CategoryModel model = new CategoryModel();
                                        model.setCategory_id(jsonObject1.getString("sub_category_id"));
                                        model.setCategory_name(jsonObject1.getString("sub_category"));
                                        model.setImage(jsonObject1.getString("image"));
                                        model.setSubcategorey(jsonObject1.getString("sub_subcategorey"));
                                        subcategoryModelArrayList.add(model);
                                    }
                                    SubCategoryAdapter adapter = new SubCategoryAdapter(CategoriesActivity.this);
                                    rec_list.setAdapter(adapter);
                                    rec_list.setVisibility(View.VISIBLE);
                                    lnr_expand.setVisibility(View.VISIBLE);
                                } else {
                                    rec_list.setVisibility(View.GONE);
                                    lnr_expand.setVisibility(View.GONE);
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
                            params.put("category_id", cat_id);

                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(CategoriesActivity.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    dismissDialog();
                    final Dialog dialog = new Dialog(CategoriesActivity.this);
                    dialog.setContentView(R.layout.no_internet_dialog);
                    Button button = dialog.findViewById(R.id.btn_process);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getSubCategory(cat_id);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        }
    }


    public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ItemViewholder> {
        Context context;

        public SubCategoryAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_categories_item_layout, parent, false);
            ItemViewholder itemViewholder = new ItemViewholder(view);
            return itemViewholder;
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewholder holder, int position) {
            CategoryModel model = subcategoryModelArrayList.get(position);
            ((ItemViewholder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return subcategoryModelArrayList.size();
        }

        public class ItemViewholder extends RecyclerView.ViewHolder {
            TextView txt_categoryName;
            LinearLayout lnr_details;

            public ItemViewholder(@NonNull View itemView) {
                super(itemView);

                txt_categoryName = itemView.findViewById(R.id.txt_categoryName);
                lnr_details = itemView.findViewById(R.id.lnr_details);
            }

            public void bind(final CategoryModel model, int position) {
                txt_categoryName.setText(model.getCategory_name());

                lnr_details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CategoriesActivity.this, CategoryWiseProductListActivity.class);
                        intent.putExtra("sub_cat_id", model.getCategory_id());
                        intent.putExtra("sub_cat_name", model.getCategory_name());
                        intent.putExtra("from", "sub_cat");
                        startActivity(intent);
                    }
                });
            }
        }
    }
}
