package com.sterlex.in.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.sterlex.in.Adapter.HomeServicesAdapter;
import com.sterlex.in.Adapter.HomeSliderAdapter;
import com.sterlex.in.Adapter.SliderAdapter;
import com.sterlex.in.Constant.APIURLs;
import com.sterlex.in.Constant.FunctionConstant;
import com.sterlex.in.Constant.SharedPref;
import com.sterlex.in.Model.BannerModel;
import com.sterlex.in.Model.CategoryModel;
import com.sterlex.in.Model.ProductModel;
import com.sterlex.in.Model.ServicesOffersModel;
import com.sterlex.in.Model.UnitModel;
import com.sterlex.in.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;

public class SubCategoryListingActivity extends AppCompatActivity {
    ImageView img_back;
    TextView txt, catName, txt_cart_count;
    RecyclerView rec_list, rec_products_list;
    RelativeLayout rel_cart_count, rel_bg,rel_cart_main;
    String cat_id = "", cat_name = "",prod_Qty = "null";
    LinearLayout lnr_home, lin_nodata,lnr_home_selected, lnr_search, lnr_search_selected,
            lnr_cart, lnr_cart_selected, lnr_subscription, lnr_subscription_selected, lnr_account, lnr_account_selected;
    RelativeLayout rel_home, rel_search, rel_cart, rel_subscription, rel_account;
    IOSDialog dialog;
    ShimmerFrameLayout shimmer_view_container;
    String cost_id = "";
    boolean cart_click = false;
    int qty = 0;
    TextView txt_cart_no_of_items, cart_txt_amount;
    RelativeLayout rel_cart_no_items;
    SliderView sliderView1;
    String cart_unit_price = "", cart_unit = "", cart_discount = "";
    ArrayList<CategoryModel> categoryModelArrayList = new ArrayList<>();
    ArrayList<BannerModel> bannerModelArrayList = new ArrayList<>();
    ArrayList<ProductModel> productModels = new ArrayList<>();
    ArrayList<UnitModel> product_unitArraylist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        setContentView(R.layout.activity_sub_category_listing);
        cost_id = SharedPref.getVal(SubCategoryListingActivity.this, SharedPref.customer_id);
        init();
        onClick();
        cat_id = getIntent().getStringExtra("cat_id");
        cat_name = getIntent().getStringExtra("cat_name");
        txt.setText(cat_name);
        catName.setText(cat_name);
        getCategoryList();
//        cartCount();

    }


    private void onClick() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        rel_cart_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cost_id.equals("")) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SubCategoryListingActivity.this);
                    // Setting Dialog Title
                    alertDialog.setTitle("Please Login");
                    // Setting Dialog Message
                    alertDialog.setMessage("Please login to view cart");
                    // On pressing Settings button
                    alertDialog.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(SubCategoryListingActivity.this, LoginActivity.class);
                            intent.putExtra("from", "home");
                            startActivity(intent);
                        }
                    });
                    // on pressing cancel button
                    alertDialog.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    // Showing Alert Message
                    alertDialog.show();
                } else {
                    startActivity(new Intent(SubCategoryListingActivity.this, CartActivity2.class));
                }
            }
        });
        rel_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_home_selected.setVisibility(View.VISIBLE);
                lnr_home.setVisibility(View.GONE);

                lnr_search.setVisibility(View.VISIBLE);
                lnr_search_selected.setVisibility(View.GONE);

                lnr_cart.setVisibility(View.VISIBLE);
                lnr_cart_selected.setVisibility(View.GONE);

                lnr_subscription.setVisibility(View.VISIBLE);
                lnr_subscription_selected.setVisibility(View.GONE);

                lnr_account.setVisibility(View.VISIBLE);
                lnr_account_selected.setVisibility(View.GONE);
                startActivity(new Intent(SubCategoryListingActivity.this, HomeActivity.class));
                finish();

            }
        });

        rel_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_home_selected.setVisibility(View.GONE);
                lnr_home.setVisibility(View.VISIBLE);

                lnr_search.setVisibility(View.GONE);
                lnr_search_selected.setVisibility(View.VISIBLE);

                lnr_cart.setVisibility(View.VISIBLE);
                lnr_cart_selected.setVisibility(View.GONE);

                lnr_subscription.setVisibility(View.VISIBLE);
                lnr_subscription_selected.setVisibility(View.GONE);

                lnr_account.setVisibility(View.VISIBLE);
                lnr_account_selected.setVisibility(View.GONE);

                startActivity(new Intent(SubCategoryListingActivity.this, OffersActivity.class));
//                    finish();
            }
        });

        rel_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_home_selected.setVisibility(View.GONE);
                lnr_home.setVisibility(View.VISIBLE);

                lnr_search.setVisibility(View.VISIBLE);
                lnr_search_selected.setVisibility(View.GONE);

                lnr_cart.setVisibility(View.GONE);
                lnr_cart_selected.setVisibility(View.VISIBLE);

                lnr_subscription.setVisibility(View.VISIBLE);
                lnr_subscription_selected.setVisibility(View.GONE);

                lnr_account.setVisibility(View.VISIBLE);
                lnr_account_selected.setVisibility(View.GONE);

                if (cost_id.equals("")) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SubCategoryListingActivity.this);
                    // Setting Dialog Title
                    alertDialog.setTitle("Please Login");
                    // Setting Dialog Message
                    alertDialog.setMessage("Please login to view cart");
                    // On pressing Settings button
                    alertDialog.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(SubCategoryListingActivity.this, LoginActivity.class);
                            intent.putExtra("from", "sub_cat");
                            startActivity(intent);
                        }
                    });
                    // on pressing cancel button
                    alertDialog.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    // Showing Alert Message
                    alertDialog.show();
                } else {
                    startActivity(new Intent(SubCategoryListingActivity.this, CartActivity2.class));
                }
            }
        });

        rel_subscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_home_selected.setVisibility(View.GONE);
                lnr_home.setVisibility(View.VISIBLE);

                lnr_search.setVisibility(View.VISIBLE);
                lnr_search_selected.setVisibility(View.GONE);

                lnr_cart.setVisibility(View.VISIBLE);
                lnr_cart_selected.setVisibility(View.GONE);


                lnr_subscription.setVisibility(View.GONE);
                lnr_subscription_selected.setVisibility(View.VISIBLE);


                lnr_account.setVisibility(View.VISIBLE);
                lnr_account_selected.setVisibility(View.GONE);

                if (cost_id.equals("")) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SubCategoryListingActivity.this);
                    // Setting Dialog Title
                    alertDialog.setTitle("Please Login");
                    // Setting Dialog Message
                    alertDialog.setMessage("Please login to view subscription");
                    // On pressing Settings button
                    alertDialog.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(SubCategoryListingActivity.this, LoginActivity.class);
                            intent.putExtra("from", "sub_cat");
                            startActivity(intent);
                        }
                    });
                    // on pressing cancel button
                    alertDialog.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    // Showing Alert Message
                    alertDialog.show();
                } else {
                    startActivity(new Intent(SubCategoryListingActivity.this, SubscriptionActivity.class));
                }

            }
        });

        rel_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_home_selected.setVisibility(View.GONE);
                lnr_home.setVisibility(View.VISIBLE);

                lnr_search.setVisibility(View.VISIBLE);
                lnr_search_selected.setVisibility(View.GONE);

                lnr_cart.setVisibility(View.VISIBLE);
                lnr_cart_selected.setVisibility(View.GONE);

                lnr_subscription.setVisibility(View.VISIBLE);
                lnr_subscription_selected.setVisibility(View.GONE);

                lnr_account.setVisibility(View.GONE);
                lnr_account_selected.setVisibility(View.VISIBLE);

                startActivity(new Intent(SubCategoryListingActivity.this, CategoriesActivity.class));
            }
        });


    }

    private void init() {
        dialog = new IOSDialog.Builder(SubCategoryListingActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();

        rec_products_list = findViewById(R.id.rec_products_list);
        rel_cart_main = findViewById(R.id.rel_cart_main);
        rel_cart_no_items = findViewById(R.id.rel_cart_no_items);
        txt_cart_no_of_items = findViewById(R.id.txt_cart_no_of_items);
        cart_txt_amount = findViewById(R.id.cart_txt_amount);

        sliderView1 = findViewById(R.id.slider);
        img_back = findViewById(R.id.img_back);
        txt = findViewById(R.id.txt);
        lin_nodata = findViewById(R.id.lin_nodata);
        catName = findViewById(R.id.catName);
        txt_cart_count = findViewById(R.id.txt_cart_count);
        rec_list = findViewById(R.id.rec_list);
        rel_cart_count = findViewById(R.id.rel_cart_count);
        rel_bg = findViewById(R.id.rel_bg);
        shimmer_view_container = findViewById(R.id.shimmer_view_container);

        lnr_home = findViewById(R.id.lnr_home);
        lnr_home_selected = findViewById(R.id.lnr_home_selected);
        lnr_search = findViewById(R.id.lnr_search);
        lnr_search_selected = findViewById(R.id.lnr_search_selected);
        lnr_cart = findViewById(R.id.lnr_cart);
        lnr_cart_selected = findViewById(R.id.lnr_cart_selected);
        lnr_subscription = findViewById(R.id.lnr_subscription);
        lnr_subscription_selected = findViewById(R.id.lnr_subscription_selected);
        lnr_account = findViewById(R.id.lnr_account);
        lnr_account_selected = findViewById(R.id.lnr_account_selected);
        rel_home = findViewById(R.id.rel_home);
        rel_search = findViewById(R.id.rel_search);
        rel_cart = findViewById(R.id.rel_cart);
        rel_subscription = findViewById(R.id.rel_subscription);
        rel_account = findViewById(R.id.rel_account);

        rec_list.setLayoutManager(new GridLayoutManager(SubCategoryListingActivity.this, 3));
        rec_products_list.setLayoutManager(new LinearLayoutManager(SubCategoryListingActivity.this));

    }

    public void account(View view) {
//        startActivity(new Intent(HomeActivity.this, AccountActivity.class));
        if (cost_id.equals("")) {
            notLoginDialog();
        } else {
            loginDialog();
        }
    }

    public void notLoginDialog() {
        final Dialog dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.not_login_menu_dialog_layout);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog1.getWindow().setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog1.getWindow().setGravity(Gravity.RIGHT | Gravity.TOP);
        dialog1.getWindow().setDimAmount((float) 0.8);
        dialog1.setCancelable(false);
        dialog1.setCanceledOnTouchOutside(true);
        LinearLayout lnr_sign_in, lnr_register;
        lnr_sign_in = dialog1.findViewById(R.id.lnr_sign_in);
        lnr_register = dialog1.findViewById(R.id.lnr_register);
        lnr_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SubCategoryListingActivity.this, LoginActivity.class));
                dialog1.dismiss();
            }
        });
        lnr_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SubCategoryListingActivity.this, OtpActivity.class));
                dialog1.dismiss();
            }
        });
        dialog1.show();
    }

    public void loginDialog() {
        final Dialog dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.menu_login_dilaog_layout);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog1.getWindow().setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog1.getWindow().setGravity(Gravity.RIGHT | Gravity.TOP);
        dialog1.getWindow().setDimAmount((float) 0.8);
        dialog1.setCancelable(false);
        dialog1.setCanceledOnTouchOutside(true);
        LinearLayout lnr_account, lnr_my_orders, lnr_my_list, lnr_signout, lnr_reorder;
        lnr_account = dialog1.findViewById(R.id.lnr_account);
        lnr_my_orders = dialog1.findViewById(R.id.lnr_my_orders);
        lnr_my_list = dialog1.findViewById(R.id.lnr_my_list);
        lnr_signout = dialog1.findViewById(R.id.lnr_signout);
        lnr_reorder = dialog1.findViewById(R.id.lnr_reorder);
        lnr_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SubCategoryListingActivity.this, AccountActivity.class));
                dialog1.dismiss();
            }
        });
        lnr_my_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SubCategoryListingActivity.this, MyOrderActivity.class));
                dialog1.dismiss();
            }
        });
        lnr_my_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(SubCategoryListingActivity.this, SaveforLaterActivity.class));
                dialog1.dismiss();
            }
        });
        lnr_reorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(SubCategoryListingActivity.this, SubscriptionActivity.class));
                dialog1.dismiss();
            }
        });
        lnr_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new androidx.appcompat.app.AlertDialog.Builder(SubCategoryListingActivity.this)
                        .setTitle("Really Logout?")
                        .setMessage("Are you sure you want to logout?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                SharedPref.clearData(SubCategoryListingActivity.this);
                                startActivity(new Intent(SubCategoryListingActivity.this, LoginActivity.class));
                                finishAffinity();
                            }
                        }).create().show();
                dialog1.dismiss();
            }
        });
        dialog1.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        rec_list.setVisibility(View.GONE);

        lnr_home_selected.setVisibility(View.VISIBLE);
        lnr_home.setVisibility(View.GONE);

        lnr_search.setVisibility(View.VISIBLE);
        lnr_search_selected.setVisibility(View.GONE);

        lnr_cart.setVisibility(View.VISIBLE);
        lnr_cart_selected.setVisibility(View.GONE);

        lnr_subscription.setVisibility(View.VISIBLE);
        lnr_subscription_selected.setVisibility(View.GONE);

        lnr_account.setVisibility(View.VISIBLE);
        lnr_account_selected.setVisibility(View.GONE);
        cartCount();
    }


    public void SuccessToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.sucess_toast_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        TextView txt_message = (TextView) layout.findViewById(R.id.txt_message);
        txt_message.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void ErrorToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.error_toast_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        TextView txt_message = (TextView) layout.findViewById(R.id.txt_message);
        txt_message.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 50);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
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


    public void cartCount() {
        if (APIURLs.isNetwork(SubCategoryListingActivity.this)) {
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
                            Toast.makeText(SubCategoryListingActivity.this, "Error", Toast.LENGTH_SHORT).show();
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

                    params.put("customer_id", SharedPref.getVal(SubCategoryListingActivity.this, SharedPref.customer_id));
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

//            final Dialog dialog = new Dialog(SubCategoryListingActivity.this);
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


    public void getCategoryList() {
//        dialog.show();
        shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        rec_list.setVisibility(View.GONE);

        if (APIURLs.isNetwork(SubCategoryListingActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.SUB_CATEGORY_URL, new Response.Listener<String>() {
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
                                model.setCategory_id(jsonObject1.getString("sub_category_id"));
                                model.setCategory_name(jsonObject1.getString("sub_category"));
                                model.setImage(jsonObject1.getString("image"));
                                model.setSubcategorey(jsonObject1.getString("sub_subcategorey"));
                                model.setColor(jsonObject1.getString("color"));
                                categoryModelArrayList.add(model);
                            }
                            CategoryAdapter adapter = new CategoryAdapter(SubCategoryListingActivity.this);
                            rec_list.setAdapter(adapter);
                            rec_list.setVisibility(View.VISIBLE);
                            lin_nodata.setVisibility(View.GONE);

                            bannerModelArrayList = new ArrayList<>();

                            JSONArray jsonArray1 = jsonObject.getJSONArray("banner");
                            if (jsonArray1.length() > 0) {
                                for (int j = 0; j < jsonArray1.length(); j++) {

                                    JSONObject jsonObject4 = jsonArray1.getJSONObject(j);
                                    BannerModel model = new BannerModel();
//                                            model.setBanner_id(jsonObject4.getString("banner_id"));
                                    model.setBanner(jsonObject4.getString("banner"));
                                    model.setCategory_id(jsonObject4.getString("sub_category_id"));
                                    model.setSub_category(jsonObject4.getString("sub_category"));
                                    bannerModelArrayList.add(model);
                                }
                                Slider();
                            } else {
                                sliderView1.setVisibility(View.GONE);
                            }
                            productModels = new ArrayList<>();

                            JSONArray jsonArray11 = jsonObject.getJSONArray("product_list");
                            if(jsonArray11.length()>0) {
                                for (int j = 0; j < jsonArray11.length(); j++) {

                                    JSONObject jsonObject3 = jsonArray11.getJSONObject(j);
                                    ProductModel model = new ProductModel();
                                    model.setProduct_id(jsonObject3.getString("product_id"));
                                    model.setProduct_name(jsonObject3.getString("product_name"));
                                    model.setImage(jsonObject3.getString("image"));
                                    model.setQty(jsonObject3.getString("qty"));
                                    model.setFlag(jsonObject3.getString("flag"));
                                    model.setType(jsonObject3.getString("type"));
                                    model.setIs_veg(jsonObject3.getString("is_veg"));

                                    JSONArray jsonArray111 = jsonObject3.getJSONArray("price_details");
                                    JSONObject jsonObject33 = jsonArray111.getJSONObject(0);
                                    model.setUnit(jsonObject33.getString("unit"));
                                    model.setPrice(jsonObject33.getString("price"));
                                    model.setDiscount(jsonObject33.getString("discount"));

                                    product_unitArraylist = new ArrayList<>();

//                                            JSONObject jsonObject4 = jsonObject2.getJSONObject("product_unit");
                                    JSONArray productArray = jsonObject3.getJSONArray("price_details");
                                    if (productArray.length() > 0) {
                                        for (int k = 0; k < productArray.length(); k++) {
                                            JSONObject imagesObject = productArray.getJSONObject(k);
                                            UnitModel unitModel = new UnitModel();
                                            unitModel.setUnit(imagesObject.getString("unit"));
                                            unitModel.setUint_price(imagesObject.getString("price"));
                                            unitModel.setUnit_discount(imagesObject.getString("discount"));
                                            product_unitArraylist.add(unitModel);
                                        }
                                        model.setProduct_unitArraylist(product_unitArraylist);
                                    }

                                    productModels.add(model);
                                }
                                rel_bg.setVisibility(View.VISIBLE);
                                rec_products_list.setVisibility(View.VISIBLE);
                                ProductsListingAdapter adapter1 = new ProductsListingAdapter(SubCategoryListingActivity.this);
                                rec_products_list.setAdapter(adapter1);

                            }else{
                                rec_products_list.setVisibility(GONE);
                                rel_bg.setVisibility(GONE);
                            }
                        } else {
                            rec_list.setVisibility(View.GONE);
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
                    params.put("category_id", cat_id);
                    params.put("customer_id", cost_id);

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            final Dialog dialog = new Dialog(SubCategoryListingActivity.this);
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

    private void Slider() {

        HomeSliderAdapter adapter = new HomeSliderAdapter(SubCategoryListingActivity.this, bannerModelArrayList);

        sliderView1.setVisibility(View.VISIBLE);
        sliderView1.setSliderAdapter(adapter);

        sliderView1.setIndicatorAnimation(IndicatorAnimations.WORM);
        //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView1.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView1.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);

        sliderView1.setIndicatorSelectedColor(Color.WHITE);
        sliderView1.setIndicatorUnselectedColor(Color.GRAY);
        sliderView1.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView1.startAutoCycle();
    }

    public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ItemViewholder> {
        Context context;

        public CategoryAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_by_category_list_home_layout, parent, false);
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
            ImageView img_category;
            TextView txt_categoryName;
            CardView card_item;

            public ItemViewholder(@NonNull View itemView) {
                super(itemView);
                img_category = itemView.findViewById(R.id.img_category);
                txt_categoryName = itemView.findViewById(R.id.txt_name);
                card_item = itemView.findViewById(R.id.card_item);
            }

            public void bind(final CategoryModel model, int position) {
                txt_categoryName.setText(model.getCategory_name());
                Glide.with(SubCategoryListingActivity.this).load(model.getImage()).into(img_category);
                if (!model.getColor().equals("")) {
                    card_item.setCardBackgroundColor(Color.parseColor(model.getColor()));
                }
                card_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SubCategoryListingActivity.this, CategoryWiseProductListActivity.class);
                        intent.putExtra("sub_cat_id", model.getCategory_id());
                        intent.putExtra("sub_cat_name", model.getCategory_name());
                        intent.putExtra("from", "sub_cat");
                        startActivity(intent);
                    }
                });
            }
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_most_popular_prdcts_list_home_layout, parent, false);
            ProductsListingAdapter.ItemViewholder itemViewholder = new ProductsListingAdapter.ItemViewholder(view);
            return itemViewholder;
        }

        @Override
        public void onBindViewHolder(@NonNull ProductsListingAdapter.ItemViewholder holder, int position) {
            ProductModel model = productModels.get(position);
            ((ProductsListingAdapter.ItemViewholder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return productModels.size();
        }

        public class ItemViewholder extends RecyclerView.ViewHolder {
            ImageView iv, iv_veg, iv_nonveg;

            LinearLayout lin_item, lin_comm;
            CardView card_unit, card_add_toCart;
            RelativeLayout rel_veg_nonveg, rel_no;
            TextView txt_name, txt_mrp, txt_og_price, txt_discount, txt_count, txt_cart_items;
            TextView txt_unit;
            Button btn_cart;
            int flag_qty = 0, qty_open_close_flag = 0;
            RelativeLayout rel_qty, rel_stock, rel_bottomcart, rel_discount, rel_cart, rel_minus, rel_plus, rel_subscribe, rel_subscribeminus, rel_subscribeplus, rel_cartmain;
            RecyclerView recy_qty;
            double ogPrice = 0.0, price = 0.0, discount = 0.0, totalPrice = 0.0;
            ProgressBar progressBar;

            public ItemViewholder(@NonNull View itemView) {
                super(itemView);

                iv = itemView.findViewById(R.id.iv);
                iv_veg = itemView.findViewById(R.id.iv_veg);
                iv_nonveg = itemView.findViewById(R.id.iv_nonveg);
                rel_veg_nonveg = itemView.findViewById(R.id.rel_veg_nonveg);
                rel_cart = itemView.findViewById(R.id.rel_cart);
                rel_stock = itemView.findViewById(R.id.rel_stock);
                rel_cartmain = itemView.findViewById(R.id.rel_cartmain);
                rel_minus = itemView.findViewById(R.id.rel_minus);
                rel_plus = itemView.findViewById(R.id.rel_plus);
                rel_no = itemView.findViewById(R.id.rel_no);
                txt_cart_items = itemView.findViewById(R.id.txt_cart_items);
                rel_qty = itemView.findViewById(R.id.rel_qty);
                card_unit = itemView.findViewById(R.id.card_unit);
                btn_cart = itemView.findViewById(R.id.btn_cart);
                recy_qty = itemView.findViewById(R.id.recy_qty);
                txt_name = itemView.findViewById(R.id.txt_name);
                txt_mrp = itemView.findViewById(R.id.txt_mrp);
                txt_og_price = itemView.findViewById(R.id.txt_og_price);
                txt_discount = itemView.findViewById(R.id.txt_discount);
                txt_unit = itemView.findViewById(R.id.txt_unit);
                lin_item = itemView.findViewById(R.id.lin_item);
                txt_count = itemView.findViewById(R.id.txt_count);
                card_unit = itemView.findViewById(R.id.card_unit);
                progressBar = itemView.findViewById(R.id.progressBar);
                txt_mrp.setPaintFlags(txt_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


            }

            public void bind(final ProductModel model, final int position) {

                if (model.getIs_veg().equals("1")) {
                    iv_veg.setVisibility(View.VISIBLE);

                } else if (model.getIs_veg().equals("0")) {
                    iv_nonveg.setVisibility(View.VISIBLE);
                } else {
                    iv_nonveg.setVisibility(View.GONE);
                    iv_veg.setVisibility(View.GONE);

                }

                if (model.getFlag().equals("0")) {
                    rel_stock.setVisibility(View.VISIBLE);
                } else if (model.getFlag().equals("1")) {
                    rel_stock.setVisibility(GONE);
                }


//             if (model.getType().equals("0")) {
//                    rel_subs.setVisibility(GONE);
//                    rel_cartmain.setVisibility(View.VISIBLE);
//                } else {
//                    rel_subs.setVisibility(View.VISIBLE);
//                    rel_cartmain.setVisibility(GONE);
//                }

                if (model.getQty().equals("0")) {
                    rel_cart.setVisibility(GONE);
                    btn_cart.setVisibility(View.VISIBLE);
                } else {
                    rel_cart.setVisibility(View.VISIBLE);
                    btn_cart.setVisibility(GONE);
                    rel_no.setVisibility(View.VISIBLE);
                    txt_cart_items.setText(model.getQty());
                    txt_count.setText(model.getQty());

                }

                try {
                    if (model.getDiscount().equals("0")) {
                       /* double actualamt = Double.parseDouble(model.getPrice()) * Double.parseDouble(model.getQty());
                        DecimalFormat twoDForm = new DecimalFormat("#");
                        actualamt = Double.valueOf(twoDForm.format(actualamt));*/

                        txt_og_price.setText("MRP \u20B9" + model.getPrice());
                        txt_mrp.setVisibility(View.INVISIBLE);
//                        rel_discount.setVisibility(View.GONE);
                        txt_discount.setVisibility(View.INVISIBLE);
                    } else {
                        txt_mrp.setVisibility(View.VISIBLE);
//                        rel_discount.setVisibility(View.VISIBLE);
                        txt_discount.setVisibility(View.VISIBLE);
//                        actualAmout = Double.parseDouble(model.getPrice());
                       /* discount = (actualAmout * Double.parseDouble(model.getDiscount())) / 100;
                        payableAmt = actualAmout - discount;
                        DecimalFormat twoDForm = new DecimalFormat("#");
                        payableAmt = Double.valueOf(twoDForm.format(payableAmt));

                        txt_discountprice.setText("Rs." + (int) Math.round(payableAmt));
                        txt_mrp.setText("Rs." + model.getPrice());
                        txt_discount.setText(model.getDiscount() + "% off");*/

//                        payableAmt = actualAmout - Double.parseDouble(model.getDiscount());
                        if (!model.getQty().equals("0")) {
                            double actualamt = Double.parseDouble(model.getPrice()) * Double.parseDouble(model.getQty());
                            long rounded = Math.round(actualamt);
                            txt_mrp.setText("MRP \u20b9" + rounded);


                            double payableAmt = Double.parseDouble(model.getPrice()) - Double.parseDouble(model.getDiscount());
                            double finalpayamt = payableAmt * Double.parseDouble(model.getQty());
                            long finalpayamtrounded = Math.round(finalpayamt);
                            txt_og_price.setText("Sterlex Super Mart \u20b9" + finalpayamtrounded);

//                        txt_mrp.setText("MRP \u20B9" + model.getPrice());
//                        txt_discount.setText("Save\n\u20B9" + model.getDiscount());
                            double saveamt = Double.parseDouble(model.getDiscount()) * Double.parseDouble(model.getQty());
                            long finalsaveamt = Math.round(saveamt);
                            txt_discount.setText("Save \u20B9" + finalsaveamt);
                        } else {
                            actualAmout = Double.parseDouble(model.getPrice());
                            payableAmt = actualAmout - Double.parseDouble(model.getDiscount());
                            long finalpayableAmtrounded = Math.round(payableAmt);

                            txt_og_price.setText("Sterlex Super Mart \u20b9" + finalpayableAmtrounded);
                            txt_mrp.setText("MRP \u20B9" + model.getPrice());
//                            txt_discount.setText("Save\n\u20B9" + model.getDiscount());
                            txt_discount.setText("Save \u20B9" + model.getDiscount());
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                price = Double.valueOf(model.getPrice());
                discount = Double.valueOf(model.getDiscount());
                totalPrice = price + discount;
                Glide.with(SubCategoryListingActivity.this).load(model.getImage()).error(R.drawable.loader).into(iv);

                txt_name.setText(model.getProduct_name());
//                txt_mrp.setText("MRP \u20B9" + totalPrice);
//                txt_og_price.setText("Sterlex Super Mart \u20B9" + model.getPrice());
//                txt_discount.setText("Save \u20B9" + model.getDiscount());
                txt_unit.setText(model.getUnit());

                product_unitArraylist = new ArrayList<>();
                product_unitArraylist = model.getProduct_unitArraylist();
                try {
                    if (product_unitArraylist != null) {
                        if (product_unitArraylist.size() > 1) {
                            card_unit.setVisibility(View.VISIBLE);
                            recy_qty.setVisibility(GONE);

                            flag_qty = 1;
                            recy_qty.setLayoutManager(new LinearLayoutManager(SubCategoryListingActivity.this, RecyclerView.HORIZONTAL, false));
                            ProductsListingAdapter.QuantityListingAdapter adapter1 = new ProductsListingAdapter.QuantityListingAdapter(SubCategoryListingActivity.this, product_unitArraylist, txt_mrp, txt_og_price, txt_discount, txt_unit);
                            recy_qty.setAdapter(adapter1);

                        } else {
                            flag_qty = 0;
                            card_unit.setVisibility(GONE);
                            recy_qty.setVisibility(GONE);
                        }
                    } else {
                        flag_qty = 0;
                        card_unit.setVisibility(GONE);
                        recy_qty.setVisibility(GONE);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    flag_qty = 0;
                    card_unit.setVisibility(GONE);
                    recy_qty.setVisibility(GONE);

                }
                rel_qty.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        prod_Qty = model.getQty();
                        if (model.getProduct_unitArraylist().size() > 1) {
                            if (qty_open_close_flag == 0) {
                                recy_qty.setVisibility(View.VISIBLE);
                                qty_open_close_flag = 1;
                            } else {
                                qty_open_close_flag = 0;
                                recy_qty.setVisibility(GONE);

                            }

                        }
                    }
                });

                btn_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cost_id.equals("")) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(SubCategoryListingActivity.this);
                            // Setting Dialog Title
                            alertDialog.setTitle("Please Login");
                            // Setting Dialog Message
                            alertDialog.setMessage("Please login to add product in cart");
                            // On pressing Settings button
                            alertDialog.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(SubCategoryListingActivity.this, LoginActivity.class);
                                    intent.putExtra("from", "product_details");
                                    startActivity(intent);
                                }
                            });
                            // on pressing cancel button
                            alertDialog.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            // Showing Alert Message
                            alertDialog.show();
                        } else {
                            if (model.getFlag().equals("1")) {
                                rel_cart.setVisibility(View.VISIBLE);
                                btn_cart.setVisibility(View.GONE);

//                                String price = txt_mrp.getText().toString();
//                                String str_price = price.substring(5, price.length());
//                                Toast.makeText(SubCategoryListingActivity.this, str_price, Toast.LENGTH_SHORT).show();
                                String discount = txt_discount.getText().toString();
                                String str_discount = "0";
                                if (!discount.equals("0")) {
                                    str_discount = discount.substring(6, discount.length());
//                                    txt_mrp.setVisibility(View.VISIBLE);
//                                    txt_discount.setVisibility(View.VISIBLE);
//                                    Toast.makeText(SubCategoryListingActivity.this, str_discount, Toast.LENGTH_SHORT).show();
                                }
                                if (!cart_click) {
                                    cart_unit = model.getUnit();
                                    cart_unit_price = model.getPrice();
                                    cart_discount = model.getDiscount();
                                }
                                addCart(model.getProduct_id(), cart_unit, cart_unit_price,
                                        cart_discount, txt_count.getText().toString(), model.getGst(), position, txt_og_price, txt_mrp, txt_discount
                                );
                                txt_count.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(SubCategoryListingActivity.this, "out of stock", Toast.LENGTH_SHORT).show();
                            }
                        }
//                        addCart(model.getProduct_id(), model.getUnit(), model.getPrice(), model.getDiscount(), model.getQty(), model.getGst(), position);
                    }
                });


                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SubCategoryListingActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });
                rel_veg_nonveg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SubCategoryListingActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });
                txt_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SubCategoryListingActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });
                txt_mrp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SubCategoryListingActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });
                txt_og_price.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SubCategoryListingActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });
                txt_discount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SubCategoryListingActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });

                rel_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        qty = Integer.parseInt(txt_count.getText().toString());
                        if (model.getFlag().equals("1")) {
                            qty = Integer.parseInt(txt_count.getText().toString());
                            if (qty >= 1) {
                                qty--;
                                if (qty != 0) {
                                    if (qty >= 1) {
//                                        progressBar.setVisibility(View.VISIBLE);
                                        txt_count.setVisibility(GONE);
                                        txt_count.setText(String.valueOf(qty));
                                        txt_cart_items.setText(String.valueOf(qty));
//                                        String price = txt_mrp.getText().toString();
//                                        String str_price = price.substring(5, price.length());
//                                        Toast.makeText(SubCategoryListingActivity.this, str_price, Toast.LENGTH_SHORT).show();
                                        String discount = txt_discount.getText().toString();
                                        String str_discount = "0";
                                        if (!discount.equals("0")) {
                                            str_discount = discount.substring(6, discount.length());
//                                            txt_mrp.setVisibility(View.VISIBLE);
//                                            txt_discount.setVisibility(View.VISIBLE);
//                                            Toast.makeText(SubCategoryListingActivity.this, str_discount, Toast.LENGTH_SHORT).show();
                                        }

                                        if (!cart_click) {
                                            cart_unit = model.getUnit();
                                            cart_unit_price = model.getPrice();
                                            cart_discount = model.getDiscount();
                                        }
                                        addCart(model.getProduct_id(), cart_unit, cart_unit_price,
                                                cart_discount, txt_count.getText().toString(), model.getGst(), position, txt_og_price, txt_mrp, txt_discount);
                                    }
                                    /*else if(qty==1){
                                        rel_cart.setVisibility(View.GONE);
                                        btn_cart.setVisibility(View.VISIBLE);
                                        removeCart(model.getProduct_id(),position);
                                    }*/
                                } else {
                                    rel_cart.setVisibility(GONE);
                                    btn_cart.setVisibility(View.VISIBLE);
//                                    Toast.makeText(SubCategoryListingActivity.this, "removing", Toast.LENGTH_SHORT).show();
                                    removeCart(model.getProduct_id(), txt_unit.getText().toString(), position);
                                }
                            }
                        } else {
                            Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                rel_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        qty = Integer.parseInt(txt_count.getText().toString());
                        if (model.getFlag().equals("1")) {
                            qty = Integer.parseInt(txt_count.getText().toString());
                            qty++;
                            /* progressBar.setVisibility(View.VISIBLE);*/
                            txt_count.setVisibility(GONE);
                            txt_count.setText(String.valueOf(qty));
                            txt_cart_items.setText(String.valueOf(qty));


                            String discount = txt_discount.getText().toString();
                            if (!discount.equals("0")) {
//                                txt_mrp.setVisibility(View.VISIBLE);
//                                txt_discount.setVisibility(View.VISIBLE);
                            }
                            if (!cart_click) {
                                cart_unit = model.getUnit();
                                cart_unit_price = model.getPrice();
                                cart_discount = model.getDiscount();
                            }
                            addCart(model.getProduct_id(), cart_unit, cart_unit_price, cart_discount,
                                    txt_count.getText().toString(), model.getGst(), position, txt_og_price, txt_mrp, txt_discount);
                        } else {
                            Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }

            public void addCart(final String prod_id, final String unit, final String price, final String discount, final String qty1, final String gst, final int position, final TextView txt_discountprice,
                                final TextView txt_actualprice,
                                final TextView txt_save_amt
            ) {
//                dialog.show();
                progressBar.setVisibility(View.VISIBLE);
                txt_count.setVisibility(GONE);

                if (APIURLs.isNetwork(SubCategoryListingActivity.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.ADDCART_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String status = jsonObject1.getString("status");
                                if (status.equals("1")) {
                                    cart_click = false;
                                    rel_no.setVisibility(View.VISIBLE);
                                    txt_count.setVisibility(View.VISIBLE);
                                    txt_cart_items.setText(qty1);
                                    txt_count.setText(qty1);

                                    if (discount.equals("0")) {
                                        double actualamt = Double.parseDouble(price) * Double.parseDouble(qty1);

                                        long finalpayableAmtrounded = Math.round(actualamt);
                                        txt_discountprice.setText("MRP \u20B9" + finalpayableAmtrounded);

                                        txt_actualprice.setVisibility(View.GONE);
                                        txt_save_amt.setVisibility(View.GONE);
                                    } else {
                                        double actualamt = Double.parseDouble(price) * Double.parseDouble(qty1);
                                        long finalactualamtrounded = Math.round(actualamt);
                                        txt_actualprice.setText("MRP \u20B9" + finalactualamtrounded);

                                        double payableAmt = Double.parseDouble(price) - Double.parseDouble(discount);
                                        double finalpayamt = payableAmt * Double.parseDouble(qty1);

                                        long finalpayamtrounded = Math.round(finalpayamt);
                                        txt_discountprice.setText("\u20b9" + finalpayamtrounded);

                                        double saveamt = Double.parseDouble(discount) * Double.parseDouble(qty1);

                                        long saveamtrounded = Math.round(saveamt);
                                        txt_save_amt.setText("Save \u20B9" + saveamtrounded);

                                        txt_actualprice.setVisibility(View.VISIBLE);
                                        txt_save_amt.setVisibility(View.VISIBLE);
                                    }
                                    qty = 0;
                                    cartCount();

                           /* productId = new ArrayList<>();
                            unitArrayList = new ArrayList<>();
                            unitArrayList = new ArrayList<>();
                            qtyArrayList = new ArrayList<>();
                            discountArrayList = new ArrayList<>();*/
//                            getProductList();
//                            getProductList();
                                } else {
                                    Toast.makeText(SubCategoryListingActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.GONE);
                                txt_count.setVisibility(View.VISIBLE);

//                                dismissDialog();
                            } catch (JSONException e) {
//                                dismissDialog();
                                progressBar.setVisibility(View.GONE);
                                txt_count.setVisibility(View.VISIBLE);
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            dismissDialog();
                            progressBar.setVisibility(GONE);
                            txt_count.setVisibility(View.VISIBLE);
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("customer_id", SharedPref.getVal(SubCategoryListingActivity.this, SharedPref.customer_id));
                            params.put("product_id", prod_id);
                            params.put("unit", unit);
                            params.put("price", price);
                            params.put("gst", "");
                            params.put("discount", discount);
                            params.put("qty", qty1);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(SubCategoryListingActivity.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    dismissDialog();
                    FunctionConstant.noInternetDialog(SubCategoryListingActivity.this, "no internet connection");
                }
            }


            public void removeCart(final String product_id, final String unit, final int position) {
//                dialog.show();
                progressBar.setVisibility(View.VISIBLE);
                txt_count.setVisibility(GONE);
                if (APIURLs.isNetwork(SubCategoryListingActivity.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.remove_product_qty, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
//                                JSONArray jsonArray = jsonObject.getJSONArray("result");
//                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String status = jsonObject.getString("status");
                                if (status.equals("1")) {
                                    qty = 0;
//                                    getProductList();
                                    rel_no.setVisibility(GONE);
                                    Toast.makeText(SubCategoryListingActivity.this, "Item removed from cart successfully", Toast.LENGTH_SHORT).show();
                                    cartCount();
                                   /* productId = new ArrayList<>();
                                    unitArrayList = new ArrayList<>();
                                    qtyArrayList = new ArrayList<>();
                                    discountArrayList = new ArrayList<>();*/
                                   /* adapter.notifyDataSetChanged();
                                    adapter.notifyItemRemoved(position);*/

                                    /*rel_cart.setVisibility(View.GONE);
                                    btn_cart.setVisibility(View.VISIBLE);*/
                                } else {
                                    Toast.makeText(SubCategoryListingActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
//                                dismissDialog();
                                progressBar.setVisibility(GONE);
                                txt_count.setVisibility(View.VISIBLE);
                            } catch (JSONException e) {
//                                dismissDialog();
                                progressBar.setVisibility(GONE);
                                txt_count.setVisibility(View.VISIBLE);
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            dismissDialog();
                            progressBar.setVisibility(GONE);
                            txt_count.setVisibility(View.VISIBLE);
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("product_id", product_id);
                            params.put("unit", unit);
                            params.put("customer_id", SharedPref.getVal(SubCategoryListingActivity.this, SharedPref.customer_id));
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(SubCategoryListingActivity.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    dismissDialog();
                    FunctionConstant.noInternetDialog(SubCategoryListingActivity.this, "no internet connection");
                }
            }
        }


        public class QuantityListingAdapter extends RecyclerView.Adapter<ProductsListingAdapter.QuantityListingAdapter.ItemViewholder> {
            Context context;
            ArrayList<UnitModel> unitModelArrayList;

            TextView txt_mrp, txt_og_price, txt_discount, txt_unit;

            int selected_index = 0;

            public QuantityListingAdapter(Context context, ArrayList<UnitModel> unitModelArrayList, TextView txt_mrp, TextView txt_og_price, TextView txt_discount, TextView txt_unit) {
                this.context = context;
                this.unitModelArrayList = unitModelArrayList;
                this.txt_mrp = txt_mrp;
                this.txt_og_price = txt_og_price;
                this.txt_discount = txt_discount;
                this.txt_unit = txt_unit;

            }

            @NonNull
            @Override
            public ProductsListingAdapter.QuantityListingAdapter.ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_qty_layout, parent, false);
                ProductsListingAdapter.QuantityListingAdapter.ItemViewholder itemViewholder = new ProductsListingAdapter.QuantityListingAdapter.ItemViewholder(view);
                return itemViewholder;

            }

            @Override
            public void onBindViewHolder(@NonNull ProductsListingAdapter.QuantityListingAdapter.ItemViewholder holder, int position) {
                UnitModel model = unitModelArrayList.get(position);
                ((ProductsListingAdapter.QuantityListingAdapter.ItemViewholder) holder).bind(model, position);
            }

            @Override
            public int getItemCount() {
                return unitModelArrayList.size();

            }

            public class ItemViewholder extends RecyclerView.ViewHolder {

                TextView txt_unit1;
                RelativeLayout rel_item;

                public ItemViewholder(@NonNull View itemView) {
                    super(itemView);

                    txt_unit1 = itemView.findViewById(R.id.txt_unit);
                    rel_item = itemView.findViewById(R.id.rel_item);

                }

                public void bind(final UnitModel unitModel, final int position) {

                    txt_unit1.setText(unitModel.getUnit());
//                    for (int i = 0; i <= unitModelArrayList.size(); i++) {
//                        String str = unitModel.getUnit();
//                        if (prod_Qty.equals(str)) {
//                            rel_item.setBackground(getResources().getDrawable(R.drawable.box_edittext_background_colored_layout));
//                        } else {
//                            rel_item.setBackground(getResources().getDrawable(R.drawable.box_edittext_background_layout));
//                        }
//                    }

                    rel_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            itemQty(unitModel.getUnit(), unitModel.getUint_price(), unitModel.getUnit_discount());
                        }
                    });

                    if (selected_index == position) {
                        rel_item.setBackground(getResources().getDrawable(R.drawable.box_edittext_background_colored_layout));
                        txt_unit1.setTextColor(getResources().getColor(R.color.colorWhite));
                        itemQty(unitModel.getUnit(), unitModel.getUint_price(), unitModel.getUnit_discount());
                    } else {
                        rel_item.setBackground(getResources().getDrawable(R.drawable.box_edittext_background_layout));
                        txt_unit1.setTextColor(getResources().getColor(R.color.colorBlack));
                    }

                    rel_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selected_index = position;
                            notifyDataSetChanged();
                            itemQty(unitModel.getUnit(), unitModel.getUint_price(), unitModel.getUnit_discount());
                        }
                    });
                }

                public void itemQty(String qty, String price, String discount) {
                    cart_discount = discount;
                    cart_unit_price = price;
                    cart_unit = qty;
                    cart_click = true;
                    txt_unit.setText(qty);
                    Double sprice = Double.parseDouble(price) - Double.parseDouble(discount);
                    txt_og_price.setText("Sterlex Super Mart \u20B9" + sprice);
                    if (discount.equals("0")) {
                        txt_discount.setVisibility(GONE);
                        txt_mrp.setVisibility(GONE);
                    } else {
                        txt_discount.setVisibility(View.VISIBLE);
                        txt_mrp.setVisibility(View.VISIBLE);
                        txt_discount.setText("Save \u20B9" + discount);
                        txt_mrp.setText("MRP \u20B9" + price);
                    }


//                    String discount1 = txt_discount.getText().toString();
//                    if (!discount1.equals("0")) {
//                        txt_mrp.setVisibility(View.VISIBLE);
//                        txt_discount.setVisibility(View.VISIBLE);
//                    } else {
//                        txt_mrp.setVisibility(GONE);
//                        txt_discount.setVisibility(GONE);
//                    }


                }
            }

        }


    }

}
