package com.sterlex.in.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.google.android.material.navigation.NavigationView;
import com.sterlex.in.Constant.APIURLs;
import com.sterlex.in.Constant.SharedPref;
import com.sterlex.in.Model.CategoryModel;
import com.sterlex.in.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubCategoryHomeActivity extends AppCompatActivity {

    DrawerLayout drawerlayout;
    NavigationView nav;
    RelativeLayout rel_cart;
    RecyclerView rec_shop_by_category;
    IOSDialog dialog;
    ShimmerFrameLayout shimmer_view_container;
    LinearLayout lnr_main;
    ArrayList<CategoryModel> categoryModels = new ArrayList<>();
    String id = "", cost_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_home);

        cost_id = SharedPref.getVal(SubCategoryHomeActivity.this, SharedPref.customer_id);
        if (!cost_id.equals("")) {
            SharedPref.putBol(SubCategoryHomeActivity.this, SharedPref.isLogin, true);
        }

        init();
        onClick();
        navigation();
        id = getIntent().getStringExtra("id");
        getCategoryList(id);
    }

    private void onClick() {
        rel_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SubCategoryHomeActivity.this, CartActivity2.class));
            }
        });
    }

    private void init() {
        drawerlayout = findViewById(R.id.drawerlayout);

        lnr_main = findViewById(R.id.lnr_main);
        shimmer_view_container = findViewById(R.id.shimmer_view_container);
        nav = findViewById(R.id.nav);

        dialog = new IOSDialog.Builder(SubCategoryHomeActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();


        rel_cart = findViewById(R.id.rel_cart);
        rec_shop_by_category = findViewById(R.id.rec_shop_by_category);

        rec_shop_by_category.setLayoutManager(new GridLayoutManager(SubCategoryHomeActivity.this, 3));
//        rec_shop_by_category.setAdapter(new ShopByCategoryListingAdapter(SubCategoryHomeActivity.this));
    }

    public void navigation() {
        nav.setItemIconTintList(null);

        View hear = nav.getHeaderView(0);
//        txt_name = hear.findViewById(R.id.txt_name);
//        txt_mobile = hear.findViewById(R.id.txt_mobile);
//        txt_name.setText(SharedPref.getVal(SubCategoryHomeActivity.this, SharedPref.name));
//        txt_mobile.setText(SharedPref.getVal(SubCategoryHomeActivity.this, SharedPref.mobile_no));

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.my_order:
                        startActivity(new Intent(SubCategoryHomeActivity.this, MyOrderActivity.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.nothing);
                        drawerlayout.closeDrawers();
                        break;
                    case R.id.wallet:
                        startActivity(new Intent(SubCategoryHomeActivity.this, ShopyBalanceActivity.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.nothing);
                        drawerlayout.closeDrawers();
                        break;
                    case R.id.coupon:
                        startActivity(new Intent(SubCategoryHomeActivity.this, CouponsActivity.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.nothing);
                        drawerlayout.closeDrawers();
                        break;
                    case R.id.subscription:
                        startActivity(new Intent(SubCategoryHomeActivity.this, SubscriptionActivity.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.nothing);
                        drawerlayout.closeDrawers();
                        break;
                    case R.id.delivery_address:
                        startActivity(new Intent(SubCategoryHomeActivity.this, AccountManageAddressActivity.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.nothing);
                        drawerlayout.closeDrawers();
                        break;
                    case R.id.refer:
                        startActivity(new Intent(SubCategoryHomeActivity.this, ReferActivity.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.nothing);
                        drawerlayout.closeDrawers();
                        break;
                    case R.id.about_us:
                        startActivity(new Intent(SubCategoryHomeActivity.this, AboutUsActivity.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.nothing);
                        drawerlayout.closeDrawers();
                        break;
                    case R.id.privacy_policy:
                        startActivity(new Intent(SubCategoryHomeActivity.this, PrivacyPolicyActivity.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.nothing);
                        drawerlayout.closeDrawers();
                        break;

                    case R.id.help_and_support:
                        startActivity(new Intent(SubCategoryHomeActivity.this, HelpAndSupportActivity.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.nothing);
                        drawerlayout.closeDrawers();
                        break;
                    case R.id.home:
                        onResume();
//                        startActivity(new Intent(SubCategoryHomeActivity.this, SubCategoryHomeActivity.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.nothing);
                        drawerlayout.closeDrawers();
                        break;
                }
                return false;
            }
        });
    }

    public void getCategoryList(final String id) {
//        dialog.show();
        shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        lnr_main.setVisibility(View.GONE);
        if (APIURLs.isNetwork(SubCategoryHomeActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.SUB_CATEGORY_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            categoryModels = new ArrayList<>();
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                CategoryModel model = new CategoryModel();
                                model.setCategory_id(jsonObject1.getString("sub_category_id"));
                                model.setCategory_name(jsonObject1.getString("sub_category"));
                                model.setImage(jsonObject1.getString("image"));
                                model.setSubcategorey(jsonObject1.getString("sub_subcategorey"));
                                model.setColor(jsonObject1.getString("color"));
                                categoryModels.add(model);
                            }
                            ShopByCategoryListingAdapter adapter = new ShopByCategoryListingAdapter(SubCategoryHomeActivity.this);
                            rec_shop_by_category.setAdapter(adapter);
                        } else {
//                            ErrorToast("Error"); //Error Toast
                            Toast.makeText(SubCategoryHomeActivity.this, "Error", Toast.LENGTH_SHORT).show();
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

                    params.put("category_id", id);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
//            FunctionConstant.noInternetDialog(SubCategoryHomeActivity.this, "no internet connection");
//            final Dialog dialog = new Dialog(SubCategoryHomeActivity.this);
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


    public class ShopByCategoryListingAdapter extends RecyclerView.Adapter<ShopByCategoryListingAdapter.ItemViewholder> {
        Context context;

        public ShopByCategoryListingAdapter(Context context) {
            this.context = context;

        }

        @NonNull
        @Override
        public ShopByCategoryListingAdapter.ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_by_category_list_home_layout, parent, false);
            ShopByCategoryListingAdapter.ItemViewholder itemViewholder = new ShopByCategoryListingAdapter.ItemViewholder(view);
            return itemViewholder;

        }

        @Override
        public void onBindViewHolder(@NonNull ShopByCategoryListingAdapter.ItemViewholder holder, int position) {
            CategoryModel model = categoryModels.get(position);
            ((ItemViewholder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return categoryModels.size();
        }

        public class ItemViewholder extends RecyclerView.ViewHolder {
            ImageView iv;

            LinearLayout lin_viewall_category, lnr_category;
            CardView card_bg;
            RelativeLayout lin_owner;
            TextView txt_mrp;
            ImageView img_category;
            TextView txt_name;


            public ItemViewholder(@NonNull View itemView) {
                super(itemView);

                lin_viewall_category = findViewById(R.id.lin_viewall_category);
                lnr_category = itemView.findViewById(R.id.lnr_category);
                img_category = itemView.findViewById(R.id.img_category);
                txt_name = itemView.findViewById(R.id.txt_name);

            }

            public void bind(final CategoryModel model, final int position) {
                txt_name.setText(model.getCategory_name());
                Glide.with(SubCategoryHomeActivity.this).load(model.getImage()).error(R.drawable.loader).
                        placeholder(R.drawable.loader).into(img_category);


                lnr_category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (model.getSubcategorey().equals("1")) {
//                            startActivity(new Intent(SubCategoryHomeActivity.this, CategoryWiseProductListActivity.class));
                            Intent intent = new Intent(SubCategoryHomeActivity.this, CategoryWiseProductListActivity.class);
                            intent.putExtra("id", model.getCategory_id());
                            startActivity(intent);
                        } else {
                            startActivity(new Intent(SubCategoryHomeActivity.this, GroceriesListingActivity.class));
                        }
                    }
                });


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
                startActivity(new Intent(SubCategoryHomeActivity.this, LoginActivity.class));
            }
        });

        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SubCategoryHomeActivity.this, OtpActivity.class));
            }
        });
        dialog1.show();
    }


    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        shimmer_view_container.stopShimmerAnimation();
        lnr_main.setVisibility(View.VISIBLE);
        shimmer_view_container.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        shimmer_view_container.stopShimmerAnimation();
        lnr_main.setVisibility(View.VISIBLE);
        shimmer_view_container.setVisibility(View.GONE);
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
        getCategoryList(id);
    }
}