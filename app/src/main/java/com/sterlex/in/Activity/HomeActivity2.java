package com.sterlex.in.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

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
import com.github.demono.AutoScrollViewPager;
import com.gmail.samehadar.iosdialog.IOSDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mlsdev.animatedrv.AnimatedRecyclerView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Orientation;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.sterlex.in.Adapter.HomeServicesAdapter;
import com.sterlex.in.Constant.APIURLs;
import com.sterlex.in.Constant.FunctionConstant;
import com.sterlex.in.Constant.SharedPref;
import com.sterlex.in.Model.BannerModel;
import com.sterlex.in.Model.CategoryModel;
import com.sterlex.in.Model.ProductModel;
import com.sterlex.in.Model.ServicesOffersModel;
import com.sterlex.in.Model.SubCategoryProductModel;
import com.sterlex.in.Model.UnitModel;
import com.sterlex.in.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.view.Gravity.BOTTOM;
import static android.view.Gravity.CENTER;
import static android.view.Gravity.RIGHT;
import static android.view.View.GONE;

public class HomeActivity2 extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerlayout;
    NavigationView nav;
    Dialog dialog1;
    LinearLayout lin_self_pickup, lin_home_del;
    ImageView img_nav, img_userMenu, img_back_to_menu, img_bg, img_icon1, img_icon2;
    RecyclerView recy_top_category, rec_category, rec_sub_list, rec_most_popular_prdcts, rec_shop_by_category;
    AnimatedRecyclerView rec_side_category;
    RelativeLayout rel_cart, rel_bg, rel_cart_no_items, rel_bg_container;
    String customer_name = "", cost_id = "", prod_Qty = "null", category_sec = "", category_sec_icon = "", product_sec = "", product_sec_icon = "", pick_up_option = "null", deliveryStatys = "null", flag = "";
    int qty = 0;
    EditText edt_search;
    ArrayList<BannerModel> bannerModelArrayList = new ArrayList<>();
    ArrayList<CategoryModel> sidecategoryModelArrayList = new ArrayList<>();
    ArrayList<CategoryModel> subcategoryModelArrayList = new ArrayList<>();
    ArrayList<CategoryModel> categoryModels = new ArrayList<>();
    ArrayList<ProductModel> productModels = new ArrayList<>();
    ArrayList<ServicesOffersModel> servicesOffersModels = new ArrayList<>();
    ArrayList<String> imagesArrayList = new ArrayList<>();
    ArrayList<String> imagesArrayList1 = new ArrayList<>();
    ArrayList<UnitModel> product_unitArraylist = new ArrayList<>();
    ArrayList<UnitModel> subproduct_unitArraylist = new ArrayList<>();
    ArrayList<CategoryModel> subcategoryArrayList = new ArrayList<>();
    ArrayList<ProductModel> subproductArrayList = new ArrayList<>();
    ArrayList<SubCategoryProductModel> subCategoryProductModelArrayList = new ArrayList<>();
    String cart_unit_price = "", cart_unit = "", cart_discount = "";
    ArrayList<BannerModel> bannerModels = new ArrayList<>();
    IOSDialog dialog;
    LinearLayout lin_content, lin_most_popular_products, lin_sub_categoryAndProduct_listings, lin_shopbyCat_Parent, lnr_main_nav, lnr_main_side, lnr_main, lin_parent, lin_parent2, lnr_login, lnr_details, lin_search;
    ShimmerFrameLayout shimmer_view_container;
    private View view1;
    SliderView sliderView;
    TextView txt_cart_no_of_items, txt_offer_name, cart_txt_amount, txt_bg, txt_name, txt_email, txt_mobile, txt_edit;
    //    ViewPager viewPager;
    AutoScrollViewPager viewPager;
    boolean cart_click = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);


        init();
        onClick();

//        boolean firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun", true);
//        if (firstrun) {
            showPincodeDialog();
//            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
//                    .edit()
//                    .putBoolean("firstrun", false)
//                    .commit();
//        }


//        setContentView(view1);
//
//        LinearLayout LL_Outer = (LinearLayout) findViewById(R.id.new_linearLayoutOuter);
//        LL_Outer.setOrientation(LinearLayout.VERTICAL); // set orientation
//        LL_Outer.setBackgroundColor(android.R.color.white); // set background
//// set Layout_Width and Layout_Height
//        LinearLayout.LayoutParams layoutForOuter = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        LL_Outer.setLayoutParams(layoutForOuter);
//
//        RecyclerView recyclerView = findViewById(R.id.);


    }

    public void shopByCategory(View view) {
        shopByCategoryLayout();
    }

    public void shopByCategoryLayout() {
        lnr_main_nav.setVisibility(View.GONE);
        lnr_main_side.setVisibility(View.VISIBLE);
    }

    public void getCategory() {
    /*    shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        lnr_main.setVisibility(View.GONE);*/
        if (APIURLs.isNetwork(HomeActivity2.this)) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.get_cats, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("200")) {

                            /*Category Data*/
                            sidecategoryModelArrayList = new ArrayList<>();
                            JSONArray jsonArray = jsonObject.getJSONArray("body");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                CategoryModel model = new CategoryModel();
                                model.setCategory_id(jsonObject1.getString("category_id"));
                                model.setCategory_name(jsonObject1.getString("category_name"));
                                model.setImage(jsonObject1.getString("image"));
                                model.setSubcategorey(jsonObject1.getString("subcategorey"));
                                model.setColor(jsonObject1.getString("color"));
                                sidecategoryModelArrayList.add(model);
                            }
                            rec_side_category.setAdapter(new SideCategoryAdapter());
                            rec_side_category.setVisibility(View.VISIBLE);

                        } else {
                            Toast.makeText(HomeActivity2.this, "no data found", Toast.LENGTH_SHORT).show();
                        }
                        dismissDialog();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dismissDialog();
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
//                    params.put("franchise_id", SharedPref.getVal(HomeActivity2.this, SharedPref.fran_code));
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            Toast.makeText(this, "no internet connection ", Toast.LENGTH_SHORT).show();
        }
    }


    public void account(View view) {
//        startActivity(new Intent(HomeActivity.this, AccountActivity.class));
        if (cost_id.equals("")) {
            notLoginDialog();
        } else {
            loginDialog();
        }
    }

    private void onClick() {
        img_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerlayout.isDrawerOpen(nav)) {
                    drawerlayout.closeDrawer(nav);
                } else {
                    drawerlayout.openDrawer(nav);
                }
            }
        });
        img_back_to_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lnr_main_side.setVisibility(View.GONE);
                lnr_main_nav.setVisibility(View.VISIBLE);
                Animation rightSwipe = AnimationUtils.loadAnimation(HomeActivity2.this, R.anim.slide_from_left);
                lnr_main_nav.startAnimation(rightSwipe);
            }
        });
        lin_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity2.this, SearchActivity.class);
                intent.putExtra("key", "");
                intent.putExtra("from", "");
                startActivity(intent);

            }
        });
        edt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity2.this, SearchActivity.class);
                intent.putExtra("key", "");
                intent.putExtra("from", "");
                startActivity(intent);

            }
        });
        rel_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cost_id.equals("")) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity2.this);
                    // Setting Dialog Title
                    alertDialog.setTitle("Please Login");
                    // Setting Dialog Message
                    alertDialog.setMessage("Please login to view cart");
                    // On pressing Settings button
                    alertDialog.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(HomeActivity2.this, LoginActivity.class);
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
                    startActivity(new Intent(HomeActivity2.this, CartActivity2.class));
                }
            }
        });
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        drawerlayout = findViewById(R.id.drawerlayout);
        lin_content = findViewById(R.id.lin_content);
        lin_most_popular_products = findViewById(R.id.lin_most_popular_products);
        lin_shopbyCat_Parent = findViewById(R.id.lin_shopbyCat_Parent);
        lin_sub_categoryAndProduct_listings = findViewById(R.id.lin_sub_categoryAndProduct_listings);
        txt_offer_name = findViewById(R.id.txt_offer_name);
        nav = findViewById(R.id.nav);
        img_nav = findViewById(R.id.img_nav);
        sliderView = findViewById(R.id.sliderView);
        img_back_to_menu = findViewById(R.id.img_back_to_menu);
        lnr_main = findViewById(R.id.lnr_main);
        lnr_main_nav = findViewById(R.id.lnr_main_nav);
        lnr_main_side = findViewById(R.id.lnr_main_side);
        rel_bg = findViewById(R.id.rel_bg);
        rec_side_category = findViewById(R.id.rec_side_category);
        edt_search = findViewById(R.id.edt_search);
        edt_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        lin_search = findViewById(R.id.lin_search);
        img_userMenu = findViewById(R.id.img_userMenu);
        shimmer_view_container = findViewById(R.id.shimmer_view_container);
        recy_top_category = findViewById(R.id.recy_top_category);
        rec_most_popular_prdcts = findViewById(R.id.rec_most_popular_prdcts);
        rec_sub_list = findViewById(R.id.rec_sub_list);
        rec_category = findViewById(R.id.rec_category);
        rec_shop_by_category = findViewById(R.id.rec_shop_by_category);
        rec_side_category.setLayoutManager(new LinearLayoutManager(HomeActivity2.this));

        dialog = new IOSDialog.Builder(HomeActivity2.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();

        view1 = getLayoutInflater().inflate(R.layout.home_top_category_content, null, false);

        /*new*/
        lin_parent = (LinearLayout) findViewById(R.id.lin_parent);
        lin_parent2 = (LinearLayout) findViewById(R.id.lin_parent2);
        viewPager = (AutoScrollViewPager) findViewById(R.id.viewPager2);
        viewPager.setCycle(true);

        rel_bg_container = new RelativeLayout(HomeActivity2.this);
        img_bg = new ImageView(HomeActivity2.this);
        img_icon1 = new ImageView(HomeActivity2.this);
        img_icon2 = new ImageView(HomeActivity2.this);
        txt_bg = new TextView(HomeActivity2.this);

//        recy_top_category = new RecyclerView(HomeActivity2.this);
//        rec_shop_by_category = new RecyclerView(HomeActivity2.this);
//        rec_most_popular_prdcts = new RecyclerView(HomeActivity2.this);
//        rec_category = new RecyclerView(HomeActivity2.this);
//        rec_most_popular_prdcts.setNestedScrollingEnabled(false);
        rec_shop_by_category.setNestedScrollingEnabled(false);
        rec_category.setNestedScrollingEnabled(false);


//        viewPager = new AutoScrollViewPager(HomeActivity2.this);
//        viewPager.setMinimumHeight(200);
//        viewPager.setDirection(RIGHT);
//        viewPager.setSlideDuration(1000);
//        viewPager.setSlideInterval(1000);
//        viewPager.setStopWhenTouch(true);

//        viewPager.setMinimumWidth(250);
//        mViewPagerAdapter = new ViewPagerAdapter(HomeActivity2.this, images);
        // Adding the Adapter to the ViewPager
//        viewPager.setAdapter(mViewPagerAdapter);

        recy_top_category.setLayoutManager(new LinearLayoutManager(HomeActivity2.this, RecyclerView.HORIZONTAL, false));
        rec_most_popular_prdcts.setLayoutManager(new LinearLayoutManager(HomeActivity2.this, RecyclerView.VERTICAL, false));
        rec_category.setLayoutManager(new LinearLayoutManager(HomeActivity2.this, RecyclerView.VERTICAL, false));
        rec_shop_by_category.setLayoutManager(new GridLayoutManager(HomeActivity2.this, 3));

        rel_cart = findViewById(R.id.rel_cart);
        rel_cart_no_items = findViewById(R.id.rel_cart_no_items);
        txt_cart_no_of_items = findViewById(R.id.txt_cart_no_of_items);
        cart_txt_amount = findViewById(R.id.cart_txt_amount);


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
                startActivity(new Intent(HomeActivity2.this, LoginActivity.class));
                dialog1.dismiss();
            }
        });

        lnr_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity2.this, OtpActivity.class));
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
                startActivity(new Intent(HomeActivity2.this, AccountActivity.class));
                dialog1.dismiss();
            }
        });
        lnr_my_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity2.this, MyOrderActivity.class));
                dialog1.dismiss();
            }
        });
        lnr_my_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity2.this, WishListActivity.class));
                dialog1.dismiss();
            }
        });
        lnr_reorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity2.this, QuickReorderActivity.class));
                dialog1.dismiss();
            }
        });
        lnr_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new androidx.appcompat.app.AlertDialog.Builder(HomeActivity2.this)
                        .setTitle("Really Logout?")
                        .setMessage("Are you sure you want to logout?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                SharedPref.clearData(HomeActivity2.this);
                                startActivity(new Intent(HomeActivity2.this, LoginActivity.class));
                                finishAffinity();
                            }
                        }).create().show();
                dialog1.dismiss();
            }
        });


        dialog1.show();
    }

    public void listingHeader() {

        img_bg.setImageResource(R.drawable.rectangle_bg);
        img_icon1.setImageResource(R.drawable.small_logo);
        img_icon2.setImageResource(R.drawable.small_logo);

//        img_bg.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 60));

//        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 80);
////        layoutParams11.gravity=Gravity.LEFT|Gravity.CENTER_VERTICAL;
//        rel_bg_container.setLayoutParams(layoutParams4);

        RelativeLayout.LayoutParams bgParams;
        bgParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 80);
        bgParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        RelativeLayout.LayoutParams imageParams;
        imageParams = new RelativeLayout.LayoutParams(45, 80);
        imageParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
        imageParams.addRule(RelativeLayout.CENTER_VERTICAL, 0);

        RelativeLayout.MarginLayoutParams marginParams = new RelativeLayout.MarginLayoutParams(imageParams);
        marginParams.setMargins(10, marginParams.topMargin, marginParams.rightMargin, marginParams.bottomMargin);
        RelativeLayout.LayoutParams layoutParamss = new RelativeLayout.LayoutParams(marginParams);

        RelativeLayout.LayoutParams imageParams2;
        imageParams2 = new RelativeLayout.LayoutParams(45, 80);
        imageParams2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        imageParams2.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

//        RelativeLayout.MarginLayoutParams marginParams2 = new RelativeLayout.MarginLayoutParams(imageParams2);
//        marginParams2.setMargins(marginParams2.leftMargin, marginParams2.topMargin, 10, marginParams2.bottomMargin);
//        RelativeLayout.LayoutParams layoutParamss2 = new RelativeLayout.LayoutParams(marginParams2);

        LinearLayout.LayoutParams layoutParams112 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 80);
//        layoutParams11.gravity=Gravity.LEFT|Gravity.CENTER_VERTICAL;
        img_bg.setLayoutParams(layoutParams112);

//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(40, 40);
//        layoutParams.gravity = Gravity.LEFT | CENTER_VERTICAL;
//        img_icon1.Mar
        img_icon1.setLayoutParams(layoutParamss);
//        img_icon1.setId(1);

//        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(40, 40);
//        layoutParams1.gravity = RIGHT | CENTER_VERTICAL;
        img_icon2.setLayoutParams(imageParams2);
//        img_icon2.getResources().getDimension(R.dimen.right);

        rel_bg_container.setLayoutParams(bgParams);

        RelativeLayout.LayoutParams imageParams3;
        imageParams3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        imageParams3.addRule(RelativeLayout.CENTER_IN_PARENT);
        imageParams3.addRule(RelativeLayout.RIGHT_OF, img_icon1.getId());
        imageParams3.addRule(RelativeLayout.LEFT_OF, img_icon2.getId());
//        RelativeLayout.LayoutParams layoutParamss3 = new RelativeLayout.LayoutParams(imageParams3);


//        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams3.gravity = CENTER;
        txt_bg.setText("HELLO I M HERE");
        txt_bg.setTypeface(Typeface.DEFAULT_BOLD);
        txt_bg.setTextColor(getResources().getColor(R.color.black));
        txt_bg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        txt_bg.setLayoutParams(imageParams3);

        rel_bg_container.removeAllViews();
        rel_bg_container.addView(img_bg);
        rel_bg_container.addView(img_icon1);
        rel_bg_container.addView(img_icon2);
        rel_bg_container.addView(txt_bg);
//        rel_bg_container.ad;

//        txt_bg.setGravity(View.TEXT_ALIGNMENT_CENTER);
//        rel_bg_container.addView(txt_bg);
//        lin_parent.addView(rel_bg_container);
    }


    public void Slider(ServicesOffersModel model) {
//        SliderView sliderView = findViewById(R.id.slider);
        sliderView.setVisibility(View.VISIBLE);
        sliderView.setMinimumHeight(220);
//        sliderView.setMinimumWidth(250);
        sliderView.setIndicatorAnimationDuration(600);
        sliderView.setAutoCycleDirection(RIGHT);
        sliderView.setAutoCycle(true);
        sliderView.setSliderAnimationDuration(600);
        sliderView.setIndicatorGravity(CENTER | BOTTOM);
        sliderView.setIndicatorMargin(15);
        sliderView.setIndicatorOrientation(Orientation.HORIZONTAL);
        sliderView.setIndicatorPadding(2);
        sliderView.setIndicatorRadius(2);

        HomeServicesAdapter adapter = new HomeServicesAdapter(this, imagesArrayList1, model);

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);

        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();

//        lin_parent.addView(sliderView);
    }

    public void navigation() {
        TextView txt_user_name = drawerlayout.findViewById(R.id.txt_user_name);
        if (!cost_id.equals("")) {
            txt_user_name.setText(customer_name);
        }
        drawerlayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                                           @Override
                                           public void onDrawerSlide(View drawer, float slideOffset) {

                                               lin_content.setX(nav.getWidth() * slideOffset);
                                               DrawerLayout.LayoutParams lp =
                                                       (DrawerLayout.LayoutParams) lin_content.getLayoutParams();
                                               lp.height = drawer.getHeight() -
                                                       (int) (drawer.getHeight() * slideOffset * 0.3f);
                                               lp.topMargin = (drawer.getHeight() - lp.height) / 2;
//                                               imgLogo.setVisibility(View.GONE);
                                               img_nav.setImageResource(R.drawable.arrow);
                                               lin_content.setLayoutParams(lp);
                                           }

                                           @Override
                                           public void onDrawerClosed(View drawerView) {
//                                               imgLogo.setVisibility(View.VISIBLE);
                                               img_nav.setImageResource(R.drawable.ic_baseline_menu_24);
                                           }
                                       }
        );

    }

    public void home(View view) {
//                        startActivity(new Intent(HomeActivity2.this, HomeActivity2.class));
        overridePendingTransition(R.anim.fade_in, R.anim.nothing);
        drawerlayout.closeDrawers();
    }

    public void becomeAnAgent(View view) {

        overridePendingTransition(R.anim.fade_in, R.anim.nothing);
        drawerlayout.closeDrawers();

        final Dialog dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.become_agent_dialog_layout);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog1.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog1.getWindow().setGravity(Gravity.CENTER);
        dialog1.getWindow().setDimAmount((float) 0.8);
        dialog1.setCancelable(true);
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.show();


//                        startActivity(new Intent(HomeActivity2.this, HomeActivity2.class));

    }

    public void wallet(View view) {
        startActivity(new Intent(HomeActivity2.this, ShopyBalanceActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.nothing);
        drawerlayout.closeDrawers();
    }

    public void coupons(View view) {
        startActivity(new Intent(HomeActivity2.this, CouponsActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.nothing);
        drawerlayout.closeDrawers();
    }

    public void deliveryAddress(View view) {
        startActivity(new Intent(HomeActivity2.this, AccountManageAddressActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.nothing);
        drawerlayout.closeDrawers();
    }

    public void refer(View view) {
        startActivity(new Intent(HomeActivity2.this, ReferActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.nothing);
        drawerlayout.closeDrawers();
    }

    public void aboutUs(View view) {
        startActivity(new Intent(HomeActivity2.this, AboutUsActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.nothing);
        drawerlayout.closeDrawers();
    }

    public void privacy(View view) {
        startActivity(new Intent(HomeActivity2.this, PrivacyPolicyActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.nothing);
        drawerlayout.closeDrawers();
    }

    public void helpnSupport(View view) {
        startActivity(new Intent(HomeActivity2.this, HelpAndSupportActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.nothing);
        drawerlayout.closeDrawers();
    }

    public void wishlist(View view) {
        startActivity(new Intent(HomeActivity2.this, WishListActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.nothing);
        drawerlayout.closeDrawers();
    }

    public void cartCount() {
        if (APIURLs.isNetwork(HomeActivity2.this)) {
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
                            Toast.makeText(HomeActivity2.this, "Error", Toast.LENGTH_SHORT).show();
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
                    if (!cost_id.equals("")) {
                        params.put("customer_id", SharedPref.getVal(HomeActivity2.this, SharedPref.customer_id));
                    } else {
                        params.put("customer_id", "");
                    }
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

//            final Dialog dialog = new Dialog(HomeActivity2.this);
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
        shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        lnr_main.setVisibility(View.GONE);
        if (APIURLs.isNetwork(HomeActivity2.this)) {
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
                                    category_sec = jsonObject1.getString("section_icon");
                                    category_sec_icon = jsonObject1.getString("section_background");

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

                                        lin_shopbyCat_Parent.setVisibility(View.VISIBLE);
                                        TopCategoryListingAdapter adapter = new TopCategoryListingAdapter(HomeActivity2.this);
//                                        lin_parent.removeAllViews();
                                        recy_top_category.setAdapter(adapter);
//                                        lin_parent.addView(recy_top_category);
                                        ShopByCategoryListingAdapter adapter1 = new ShopByCategoryListingAdapter(HomeActivity2.this);
                                        rec_shop_by_category.setAdapter(adapter1);
//                                        ShopByCategoryListingAdapter adapter1 = new ShopByCategoryListingAdapter(HomeActivity2.this);
//                                        rec_shop_by_category.setAdapter(adapter1);
////                                        lin_shopbyCat_Parent.setVisibility(View.VISIBLE);
//                                        lin_parent.addView(rec_shop_by_category);


                                    }
//                                    String android_app_version = jsonObject2.getString("version_code");
//
//                                    if (!android_app_version.equals("")) {
//                                        try {
//                                            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
//                                            String version_code1 = pInfo.versionName;
////                                    double version_code = Double.valueOf(version_code1);
//                                            boolean data_code = (Double.valueOf(android_app_version) > Double.valueOf(version_code1));
//                                            // boolean data_code = (1.14 > 1.13);
//                                            if (data_code) {
//                                                versionDialog();
//                                            }
//                                        } catch (PackageManager.NameNotFoundException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }


                                } else if (jsonObject1.has("offer_object")) {

                                    JSONObject jsonObject22 = jsonObject1.getJSONObject("offer_object");
                                    JSONArray jsonArray1 = jsonObject22.getJSONArray("result");
                                    servicesOffersModels = new ArrayList<>();

                                    if (jsonArray1.length() >= 0) {
                                        JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
                                        ServicesOffersModel model = new ServicesOffersModel();

                                        model.setSection_text(jsonObject2.getString("section_text"));
                                        model.setPosition(jsonObject2.getString("position"));
//                                        model.setSection_icon(jsonObject2.getString("section_icon"));

                                        imagesArrayList = new ArrayList<>();

                                        JSONObject jsonObject3 = jsonObject2.getJSONObject("images");
                                        JSONArray imagesArray = jsonObject3.getJSONArray("images_data");
                                        if (imagesArray.length() > 0) {
                                            for (int k = 0; k < imagesArray.length(); k++) {
                                                JSONObject imagesObject = imagesArray.getJSONObject(k);
                                                imagesArrayList.add(imagesObject.getString("images"));

                                                model.setOffer_id(imagesObject.getString("offer_id"));
                                            }
                                            model.setImagesArrayList(imagesArrayList);
                                        }
                                        servicesOffersModels.add(model);

//                                    listingHeader();
                                        CategoryListingAdapter adapter = new CategoryListingAdapter(HomeActivity2.this);
                                        rec_category.setAdapter(adapter);
//                                    lin_parent.addView(rec_category);
                                    } else {
                                        rec_category.setVisibility(GONE);
                                    }
                                    if (jsonArray1.length() >= 2) {

                                        JSONObject jsonObject11 = jsonArray1.getJSONObject(1);
                                        ServicesOffersModel model1 = new ServicesOffersModel();

                                        txt_offer_name.setText(jsonObject11.getString("section_text"));
                                        model1.setSection_text(jsonObject11.getString("section_text"));
                                        model1.setPosition(jsonObject11.getString("position"));
//                                        model1.setSection_background(jsonObject11.getString("section_background"));
//                                        model1.setSection_icon(jsonObject11.getString("section_icon"));

                                        imagesArrayList1 = new ArrayList<>();

                                        JSONObject jsonObject32 = jsonObject11.getJSONObject("images");
                                        JSONArray imagesArray2 = jsonObject32.getJSONArray("images_data");
                                        if (imagesArray2.length() > 0) {
                                            for (int k = 0; k < imagesArray2.length(); k++) {
                                                JSONObject imagesObject2 = imagesArray2.getJSONObject(k);
                                                imagesArrayList1.add(imagesObject2.getString("images"));

                                                model1.setOffer_id(imagesObject2.getString("offer_id"));
                                            }
                                            model1.setImagesArrayList(imagesArrayList1);
                                        }
                                        rel_bg.setVisibility(View.VISIBLE);
                                        Slider(model1);
                                    } else {
                                        sliderView.setVisibility(GONE);
                                        rel_bg.setVisibility(GONE);
                                    }

                                } else if (jsonObject1.has("product")) {
                                    product_sec = jsonObject1.getString("section_icon");
                                    product_sec_icon = jsonObject1.getString("section_background");

                                    JSONObject jsonObject2 = jsonObject1.getJSONObject("product");
                                    if (jsonObject2.length() > 0) {
                                        String status_product = jsonObject2.getString("status");
                                        if (!status_product.equals("0")) {
                                            lin_most_popular_products.setVisibility(View.VISIBLE);
                                            productModels = new ArrayList<>();

                                            JSONArray jsonArray1 = jsonObject2.getJSONArray("result");
                                            for (int j = 0; j < jsonArray1.length(); j++) {

                                                JSONObject jsonObject3 = jsonArray1.getJSONObject(j);
                                                ProductModel model = new ProductModel();
                                                model.setProduct_id(jsonObject3.getString("product_id"));
                                                model.setProduct_name(jsonObject3.getString("product_name"));
                                                model.setImage(jsonObject3.getString("image"));
//                                                model.setQty(jsonObject3.getString("product_qty"));
                                                model.setFlag(jsonObject3.getString("flag"));
                                                model.setType(jsonObject3.getString("type"));
//                                            model.setPrice_id(jsonObject3.getString("price_id"));
//                                            model.setUnit(jsonObject3.getString("unit"));
//                                            model.setPrice(jsonObject3.getString("price"));
//                                            model.setDiscount(jsonObject3.getString("discount"));
//                                            model.setGst(jsonObject3.getString("gst"));
                                                model.setIs_veg(jsonObject3.getString("is_veg"));

                                                JSONArray jsonArray11 = jsonObject3.getJSONArray("product_unit");
                                                JSONObject jsonObject33 = jsonArray11.getJSONObject(0);
                                                model.setUnit(jsonObject33.getString("unit"));
                                                model.setPrice(jsonObject33.getString("uint_price"));
                                                model.setDiscount(jsonObject33.getString("unit_discount"));
                                                model.setQty(jsonObject33.getString("cust_qty"));

                                                product_unitArraylist = new ArrayList<>();

//                                            JSONObject jsonObject4 = jsonObject2.getJSONObject("product_unit");
                                                JSONArray productArray = jsonObject3.getJSONArray("product_unit");
                                                if (productArray.length() > 0) {
                                                    for (int k = 0; k < productArray.length(); k++) {
                                                        JSONObject imagesObject = productArray.getJSONObject(k);
                                                        UnitModel unitModel = new UnitModel();
                                                        unitModel.setUnit(imagesObject.getString("unit"));
                                                        unitModel.setUint_price(imagesObject.getString("uint_price"));
                                                        unitModel.setUnit_discount(imagesObject.getString("unit_discount"));
                                                        unitModel.setUnit_qty(imagesObject.getString("cust_qty"));
//                                                        unitModel.setUnit_gst(imagesObject.getString("unit_gst"));
                                                        product_unitArraylist.add(unitModel);
                                                    }
                                                    model.setProduct_unitArraylist(product_unitArraylist);
                                                }

                                                productModels.add(model);
                                            }
//                                        listingHeader();
                                            MostPopularProductsListingAdapter adapter = new MostPopularProductsListingAdapter(HomeActivity2.this);
                                            rec_most_popular_prdcts.setAdapter(adapter);
//                                            lin_parent.addView(rec_most_popular_prdcts);

//                                        listingHeader();

                                        } else {
                                            lin_most_popular_products.setVisibility(GONE);
                                        }

                                    }

                                } else if (jsonObject1.has("banner")) {
                                    JSONObject bannerObject = jsonObject1.getJSONObject("banner");
                                    if (bannerObject.length() > 0) {
                                        String status_product = bannerObject.getString("status");
                                        if (!status_product.equals("0")) {

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
//                                        lin_parent.addView(viewPager);
                                            viewPager.setAdapter(new ViewPagerAdapter());
                                            viewPager.startAutoScroll();
//                                        lin_topCategory.addView(viewPager);

//                                        Slider();
//                                    TopCategoryListingAdapter adapter = new TopCategoryListingAdapter(HomeActivity2.this);
//                                    rec_shop_by_category.setAdapter(adapter);

                                        }
                                    }
                                }

//                                lin_parent2.removeAllViews();
//                                lin_parent2.addView(rec_shop_by_category);
                            }

                        } else {
//                            ErrorToast("Error"); //Error Toast
                            Toast.makeText(HomeActivity2.this, "Error", Toast.LENGTH_SHORT).show();
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
                    if (!cost_id.equals("")) {
                        params.put("customer_id", SharedPref.getVal(HomeActivity2.this, SharedPref.customer_id));
                    } else {
                        params.put("customer_id", "");
                    }
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
//            FunctionConstant.noInternetDialog(HomeActivity2.this, "no internet connection");
//            final Dialog dialog = new Dialog(HomeActivity2.this);
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


    public void getMostPopularProductList() {
//        shimmer_view_container.startShimmerAnimation();
//        shimmer_view_container.setVisibility(View.VISIBLE);
//        lnr_main.setVisibility(View.GONE);
        if (APIURLs.isNetwork(HomeActivity2.this)) {
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
                                if (jsonObject1.has("product")) {
                                    product_sec = jsonObject1.getString("section_icon");
                                    product_sec_icon = jsonObject1.getString("section_background");

                                    JSONObject jsonObject2 = jsonObject1.getJSONObject("product");
                                    if (jsonObject2.length() > 0) {
                                        String status_product = jsonObject2.getString("status");
                                        if (!status_product.equals("0")) {
                                            lin_most_popular_products.setVisibility(View.VISIBLE);
                                            productModels = new ArrayList<>();

                                            JSONArray jsonArray1 = jsonObject2.getJSONArray("result");
                                            for (int j = 0; j < jsonArray1.length(); j++) {

                                                JSONObject jsonObject3 = jsonArray1.getJSONObject(j);
                                                ProductModel model = new ProductModel();
                                                model.setProduct_id(jsonObject3.getString("product_id"));
                                                model.setProduct_name(jsonObject3.getString("product_name"));
                                                model.setImage(jsonObject3.getString("image"));
//                                                model.setQty(jsonObject3.getString("product_qty"));
                                                model.setFlag(jsonObject3.getString("flag"));
                                                model.setType(jsonObject3.getString("type"));
//                                            model.setPrice_id(jsonObject3.getString("price_id"));
//                                            model.setUnit(jsonObject3.getString("unit"));
//                                            model.setPrice(jsonObject3.getString("price"));
//                                            model.setDiscount(jsonObject3.getString("discount"));
//                                            model.setGst(jsonObject3.getString("gst"));
                                                model.setIs_veg(jsonObject3.getString("is_veg"));

                                                JSONArray jsonArray11 = jsonObject3.getJSONArray("product_unit");
                                                JSONObject jsonObject33 = jsonArray11.getJSONObject(0);
                                                model.setUnit(jsonObject33.getString("unit"));
                                                model.setPrice(jsonObject33.getString("uint_price"));
                                                model.setDiscount(jsonObject33.getString("unit_discount"));
                                                model.setQty(jsonObject33.getString("cust_qty"));

                                                product_unitArraylist = new ArrayList<>();

//                                            JSONObject jsonObject4 = jsonObject2.getJSONObject("product_unit");
                                                JSONArray productArray = jsonObject3.getJSONArray("product_unit");
                                                if (productArray.length() > 0) {
                                                    for (int k = 0; k < productArray.length(); k++) {
                                                        JSONObject imagesObject = productArray.getJSONObject(k);
                                                        UnitModel unitModel = new UnitModel();
                                                        unitModel.setUnit(imagesObject.getString("unit"));
                                                        unitModel.setUint_price(imagesObject.getString("uint_price"));
                                                        unitModel.setUnit_discount(imagesObject.getString("unit_discount"));
                                                        unitModel.setUnit_qty(imagesObject.getString("cust_qty"));
//                                                        unitModel.setUnit_gst(imagesObject.getString("unit_gst"));
                                                        product_unitArraylist.add(unitModel);
                                                    }
                                                    model.setProduct_unitArraylist(product_unitArraylist);
                                                }

                                                productModels.add(model);
                                            }
//                                        listingHeader();
                                            MostPopularProductsListingAdapter adapter = new MostPopularProductsListingAdapter(HomeActivity2.this);
                                            rec_most_popular_prdcts.setAdapter(adapter);
//                                            lin_parent.addView(rec_most_popular_prdcts);

//                                        listingHeader();

                                        } else {
                                            lin_most_popular_products.setVisibility(GONE);
                                        }

                                    }

                                }
                            }

                        } else {
//                            ErrorToast("Error"); //Error Toast
                            Toast.makeText(HomeActivity2.this, "Error", Toast.LENGTH_SHORT).show();
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
                    if (!cost_id.equals("")) {
                        params.put("customer_id", SharedPref.getVal(HomeActivity2.this, SharedPref.customer_id));
                    } else {
                        params.put("customer_id", "");
                    }
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
//            FunctionConstant.noInternetDialog(HomeActivity2.this, "no internet connection");
//            final Dialog dialog = new Dialog(HomeActivity2.this);
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


    public void showPincodeDialog() {
        dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.pincode_dialog_layout);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog1.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog1.getWindow().setGravity(CENTER);
        dialog1.getWindow().setDimAmount((float) 0.8);
        dialog1.setCancelable(true);
        dialog1.setCanceledOnTouchOutside(false);
        final EditText edt_pincode = dialog1.findViewById(R.id.edt_pincode);
        Button btn_process = dialog1.findViewById(R.id.btn_process);
        ImageView img_cancel_dialog = dialog1.findViewById(R.id.img_cancel_dialog);
        lin_self_pickup = dialog1.findViewById(R.id.lin_self_pickup);
        lin_home_del = dialog1.findViewById(R.id.lin_home_del);

        btn_process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog1.dismiss();
                if (edt_pincode.getText().toString().equals("")) {
                    Toast.makeText(HomeActivity2.this, "Please enter pincode", Toast.LENGTH_SHORT).show();
                } else {
                    checkPincode(edt_pincode.getText().toString());


                }
            }
        });
        img_cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();

            }
        });
        dialog1.show();
    }

    public void checkPincode(final String pincode) {
        dialog.show();
        if (APIURLs.isNetwork(HomeActivity2.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.check_pincode, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        deliveryStatys = jsonObject1.getString("status");
                        pick_up_option = jsonObject1.getString("pick_up_option");
                        if (!deliveryStatys.equals("null")) {
                            if (deliveryStatys.equals("0")) {
                                dialog1.dismiss();
                                new AlertDialog.Builder(HomeActivity2.this)
                                        .setTitle("Service unavailable")
                                        .setMessage("Currently we are not available in your area.")

                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Continue with delete operation
                                                dialog.dismiss();
                                            }
                                        })
                                        .show();
                            } else {
                                dialog.show();
                                Toast.makeText(HomeActivity2.this, "Service available", Toast.LENGTH_SHORT).show();
                                if (!pick_up_option.equals("null")) {
                                    if (pick_up_option.equals("1")) {
                                        lin_self_pickup.setVisibility(View.VISIBLE);
                                        lin_home_del.setVisibility(GONE);
                                    } else if (pick_up_option.equals("2")) {

                                        lin_self_pickup.setVisibility(GONE);
                                        lin_home_del.setVisibility(View.VISIBLE);
                                    } else if (pick_up_option.equals("3")) {

                                        lin_self_pickup.setVisibility(View.VISIBLE);
                                        lin_home_del.setVisibility(View.VISIBLE);
                                    }
                                }

//                        new AlertDialog.Builder(HomeActivity2.this)
//                                .setTitle("Service available")
//                                .setMessage("We are available in your area.")
//
//                                // Specifying a listener allows you to take an action before dismissing the dialog.
//                                // The dialog is automatically dismissed when a dialog button is clicked.
//                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        // Continue with delete operation
//                                        dialog.dismiss();
//                                    }
//                                })
//                                    .show();
                                dialog.dismiss();
                            }
                        }

                        APIURLs.pincodeChecked = true;
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
                    params.put("pincode", pincode);

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            final Dialog dialog = new Dialog(HomeActivity2.this);
            dialog.setContentView(R.layout.no_internet_dialog);
            Button button = dialog.findViewById(R.id.btn_process);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkPincode(pincode);
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public void details(View view) {
        startActivity(new Intent(HomeActivity2.this, GroceriesListingActivity.class));
    }

    public void getProfile() {
        dialog.show();
        if (APIURLs.isNetwork(HomeActivity2.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.customer_profile, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
//                            customer_id = jsonObject1.getString("customer_id");
//                            customer_unique_id = jsonObject1.getString("customer_unique_id");
                            customer_name = jsonObject1.getString("first_name") + " " + jsonObject1.getString("last_name");
//                            last_name = jsonObject1.getString("last_name");
//                            mobile_no = jsonObject1.getString("mobile_no");
//                            email_id = jsonObject1.getString("email_id");
//                            photo = jsonObject1.getString("photo");

                            SharedPref.putVal(HomeActivity2.this, SharedPref.referral_code, jsonObject1.getString("my_refral_code"));
//                            setData();

                            navigation();
                        } else {
                            Toast.makeText(HomeActivity2.this, "Error", Toast.LENGTH_SHORT).show();
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
                    if (!cost_id.equals("")) {
                        params.put("customer_id", SharedPref.getVal(HomeActivity2.this, SharedPref.customer_id));
                    } else {
                        params.put("customer_id", "");
                    }
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            final Dialog dialog = new Dialog(HomeActivity2.this);
            dialog.setContentView(R.layout.no_internet_dialog);
            Button button = dialog.findViewById(R.id.btn_process);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getProfile();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public void getSubCategoryAndProductsListing() {
    /*    shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        lnr_main.setVisibility(View.GONE);*/
        if (APIURLs.isNetwork(HomeActivity2.this)) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.getNewHomePage, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("200")) {
                            JSONArray bodyArray = jsonObject.getJSONArray("body");
                            if (bodyArray.length() > 0) {
                                subCategoryProductModelArrayList = new ArrayList<>();
                                for (int i = 0; i < bodyArray.length(); i++) {
                                    JSONObject bodyObject = bodyArray.getJSONObject(i);
                                    SubCategoryProductModel subCategoryProductModel = new SubCategoryProductModel();
                                    subCategoryProductModel.setCategory_id(bodyObject.getString("category_id"));
                                    subCategoryProductModel.setCategory_name(bodyObject.getString("category_name"));
                                    subCategoryProductModel.setSub_cat_banner(bodyObject.getString("sub_cat_banner"));

                                    subcategoryArrayList = new ArrayList<>();
                                    JSONArray subCatsArray = bodyObject.getJSONArray("sub_cats");
                                    if (subCatsArray.length() > 0) {
                                        for (int k = 0; k < subCatsArray.length(); k++) {
                                            JSONObject subCatsObject = subCatsArray.getJSONObject(k);
                                            CategoryModel categoryModel = new CategoryModel();
                                            categoryModel.setCategory_id(subCatsObject.getString("sub_category_id"));
                                            categoryModel.setCategory_name(subCatsObject.getString("sub_category"));
                                            categoryModel.setColor(subCatsObject.getString("color"));
                                            categoryModel.setMainCategory_id(subCatsObject.getString("category_id"));
                                            categoryModel.setSub_category(subCatsObject.getString("sub_subcategorey"));
                                            categoryModel.setImage(subCatsObject.getString("icon"));
                                            subcategoryArrayList.add(categoryModel);
                                        }
                                        subCategoryProductModel.setCategoryModelArrayList(subcategoryArrayList);
                                    }
                                    subproductArrayList = new ArrayList<>();
                                    JSONArray subProductsArray = bodyObject.getJSONArray("product_list");
//                                    if (subProductsArray.length() > 0) {
                                    for (int k = 0; k < subProductsArray.length(); k++) {
                                        JSONObject subProductsObject = subProductsArray.getJSONObject(k);
                                        ProductModel productModel = new ProductModel();
                                        productModel.setProduct_id(subProductsObject.getString("product_id"));
                                        productModel.setProduct_name(subProductsObject.getString("product_name"));
                                        productModel.setImage(subProductsObject.getString("main_image"));
                                        productModel.setIs_in_wishlist(subProductsObject.getString("is_in_wishlist"));
                                        productModel.setQty(subProductsObject.getString("p_qty"));
                                        productModel.setFlag(subProductsObject.getString("flag"));
                                        productModel.setType(subProductsObject.getString("type"));
                                        productModel.setIs_veg(subProductsObject.getString("is_veg"));
                                        JSONArray jsonArray1 = subProductsObject.getJSONArray("price_breakup");
                                        JSONObject jsonObject33 = jsonArray1.getJSONObject(0);
                                        productModel.setUnit(jsonObject33.getString("title"));
                                        productModel.setPrice(jsonObject33.getString("unit_price"));
                                        productModel.setDiscount(jsonObject33.getString("discount"));
                                        productModel.setSave_id(jsonObject33.getString("save_id"));
//                                        productModel.setGst(jsonObject33.getString("unit_gst"));

                                        subproduct_unitArraylist = new ArrayList<>();

//                                            JSONObject jsonObject4 = jsonObject2.getJSONObject("product_unit");
                                        JSONArray productArray = subProductsObject.getJSONArray("price_breakup");
                                        if (productArray.length() > 0) {
                                            for (int j = 0; j < productArray.length(); j++) {
                                                JSONObject imagesObject = productArray.getJSONObject(j);
                                                UnitModel unitModel = new UnitModel();
                                                unitModel.setId(imagesObject.getString("id"));
                                                unitModel.setUnit(imagesObject.getString("title"));
                                                unitModel.setUint_price(imagesObject.getString("unit_price"));
                                                unitModel.setUnit_discount(imagesObject.getString("discount"));
                                                unitModel.setSave_id(imagesObject.getString("save_id"));
                                                subproduct_unitArraylist.add(unitModel);
                                            }
                                            productModel.setProduct_unitArraylist(subproduct_unitArraylist);
                                        }


                                        subproductArrayList.add(productModel);
                                    }
                                    subCategoryProductModel.setProductModelArrayList(subproductArrayList);
                                    subCategoryProductModelArrayList.add(subCategoryProductModel);
                                }
                            }
                            dismissDialog();
                            rec_sub_list.setLayoutManager(new LinearLayoutManager(HomeActivity2.this));
                            SubCategoryAndProductListAdapter adapter = new SubCategoryAndProductListAdapter();
                            rec_sub_list.setAdapter(adapter);
                        } else {
                            lin_sub_categoryAndProduct_listings.setVisibility(GONE);
//                                MDToast.makeText(HomeActivity2.this, "no data found", MDToast.LENGTH_SHORT, MDToast.TYPE_INFO).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dismissDialog();
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
                    if (!cost_id.equals("")) {
                        params.put("customer_id", SharedPref.getVal(HomeActivity2.this, SharedPref.customer_id));
                    } else {
                        params.put("customer_id", "");
                    }
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            Toast.makeText(this, "no internet connection ", Toast.LENGTH_SHORT).show();
        }
    }


    public class SubCategoryAndProductListAdapter extends RecyclerView.Adapter<SubCategoryAndProductListAdapter.ItemViewholder> {
        Context context;
        int selectedPos = 0;

        @NonNull
        @Override
        public ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_category_details_home, parent, false);
            ItemViewholder itemViewholder = new ItemViewholder(view);
            return itemViewholder;
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewholder holder, int position) {
            SubCategoryProductModel model = subCategoryProductModelArrayList.get(position);
            ((ItemViewholder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return subCategoryProductModelArrayList.size();
        }

        public class ItemViewholder extends RecyclerView.ViewHolder {
            TextView txt_header_name;
            RecyclerView rec_sub_category, rec_product_listing;
            View view;
            ImageView img_banner;
            LinearLayout lnr_category;

            public ItemViewholder(@NonNull View itemView) {
                super(itemView);

                txt_header_name = itemView.findViewById(R.id.txt_header_name);
                img_banner = itemView.findViewById(R.id.img_banner);
                rec_sub_category = itemView.findViewById(R.id.rec_sub_category);
                rec_product_listing = itemView.findViewById(R.id.rec_product_listing);
                view = itemView.findViewById(R.id.view);
                lnr_category = itemView.findViewById(R.id.lnr_category);
            }

            public void bind(final SubCategoryProductModel model, final int position) {
                txt_header_name.setText(model.getCategory_name());
                Glide.with(HomeActivity2.this).load(model.getSub_cat_banner()).error(R.drawable.loader).into(img_banner);


                rec_sub_category.setLayoutManager(new GridLayoutManager(HomeActivity2.this, 2));
                CategorySubbAdapter adapter = new CategorySubbAdapter(model.getCategoryModelArrayList());
                rec_sub_category.setAdapter(adapter);

                if (model.getProductModelArrayList().size() > 0 || model.getProductModelArrayList() != null) {
                    rec_product_listing.setLayoutManager(new LinearLayoutManager(HomeActivity2.this, RecyclerView.HORIZONTAL, false));
                    SubcategoryProductListAdapter adapter1 = new SubcategoryProductListAdapter(model.getProductModelArrayList());
                    rec_product_listing.setAdapter(adapter1);
                }
//                if (selectedPos == position) {
//                    view.setVisibility(View.VISIBLE);
////                    sub_cat_id = model.getCategory_id();
////                    getProductList("sub_sub_cat");
//                } else {
//                    view.setVisibility(GONE);
//                }

//                img_banner.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(HomeActivity2.this, CategoryWiseProductListActivity.class);
//                        intent.putExtra("sub_cat_id", model.getCategory_id());
//                        intent.putExtra("sub_cat_name", model.getCategory_name());
//                        intent.putExtra("from", "cat");
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.slide_from_right, R.anim.nothing);
//                    }
//                });
            }
        }
    }

    /*Category in Sub Category Adapter*/
    public class CategorySubbAdapter extends RecyclerView.Adapter<CategorySubbAdapter.ViewHolder> {
        ArrayList<CategoryModel> categoryModelArrayList1;

        public CategorySubbAdapter(ArrayList<CategoryModel> categoryModelArrayList) {
            categoryModelArrayList1 = categoryModelArrayList;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(HomeActivity2.this).inflate(R.layout.item_sub_category_home, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            CategoryModel model = categoryModelArrayList1.get(position);
            ((ViewHolder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return categoryModelArrayList1.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout lnr_category;
            ImageView img_category;
            TextView txt_name;
            LinearLayout card_category;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                lnr_category = itemView.findViewById(R.id.lnr_category);
                img_category = itemView.findViewById(R.id.img_category);
                txt_name = itemView.findViewById(R.id.txt_name);
                card_category = itemView.findViewById(R.id.card_category);
            }

            public void bind(final CategoryModel model, int position) {
                Glide.with(HomeActivity2.this).load(model.getImage())
                        .error(R.drawable.loader).placeholder(R.drawable.loader).into(img_category);
                txt_name.setText(model.getCategory_name());

//                if (!model.getColor().equals("")) {
//                    img_category.setBackgroundColor(Color.parseColor(model.getColor()));
//                }

                lnr_category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (model.getSub_category().equals("1")) {
                            Intent intent = new Intent(HomeActivity2.this, SubCategoryListingActivity.class);
                            intent.putExtra("cat_id", model.getCategory_id());
                            intent.putExtra("cat_name", model.getCategory_name());
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_from_right, R.anim.nothing);
                        } else {
                            Intent intent = new Intent(HomeActivity2.this, CategoryWiseProductListActivity.class);
                            intent.putExtra("sub_cat_id", model.getCategory_id());
                            intent.putExtra("sub_cat_name", model.getCategory_name());
                            intent.putExtra("from", "cat");
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_from_right, R.anim.nothing);
                        }
                    }
                });
            }
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

            LinearLayout lnr_category, lin_comm;
            CardView card_bg;
            RelativeLayout lin_owner;
            TextView txt_name;

            public ItemViewholder(@NonNull View itemView) {
                super(itemView);

//                card_bg = itemView.findViewById(R.id.card_bg);
                lnr_category = itemView.findViewById(R.id.lnr_category);
                iv = itemView.findViewById(R.id.iv);
                txt_name = itemView.findViewById(R.id.txt_name);
//                txt_name = itemView.findViewById(R.id.txt_name);
            }

            public void bind(final CategoryModel model, final int position) {
                txt_name.setText("");

                txt_name.setText(model.getCategory_name());
                Glide.with(HomeActivity2.this).load(model.getImage()).into(iv);

                lnr_category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (model.getSubcategorey().equals("1")) {
                            Intent intent = new Intent(HomeActivity2.this, SubCategoryListingActivity.class);
                            intent.putExtra("cat_id", model.getCategory_id());
                            intent.putExtra("cat_name", model.getCategory_name());
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(HomeActivity2.this, CategoryWiseProductListActivity.class);
                            intent.putExtra("sub_cat_id", model.getCategory_id());
                            intent.putExtra("sub_cat_name", model.getCategory_name());
                            intent.putExtra("from", "cat");
                            startActivity(intent);
                        }
                    }
                });


            }

        }

    }

    public class SideCategoryAdapter extends RecyclerView.Adapter<SideCategoryAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(HomeActivity2.this).inflate(R.layout.side_category_list_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            CategoryModel model = sidecategoryModelArrayList.get(position);
            ((ViewHolder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return sidecategoryModelArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout lnr_side_category, lnr_subcategory_rec;
            RelativeLayout rel_images;
            ImageView img_categories, img_plus, img_minus;
            TextView txt_cat_name;
            RecyclerView rec_side_subcategory;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                lnr_side_category = itemView.findViewById(R.id.lnr_side_category);
                txt_cat_name = itemView.findViewById(R.id.txt_cat_name);
                lnr_subcategory_rec = itemView.findViewById(R.id.lnr_subcategory_rec);
                rec_side_subcategory = itemView.findViewById(R.id.rec_side_subcategory);
                rel_images = itemView.findViewById(R.id.rel_images);
                img_plus = itemView.findViewById(R.id.img_plus);
                img_minus = itemView.findViewById(R.id.img_minus);
            }

            public void bind(final CategoryModel model, int position) {
                txt_cat_name.setText(model.getCategory_name());
                if (model.getSubcategorey().equals("1")) {
                    rel_images.setVisibility(View.VISIBLE);
                } else {
                    rel_images.setVisibility(View.GONE);
                }

                lnr_side_category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (model.getSubcategorey().equals("1")) {
                            if (img_plus.getVisibility() == View.VISIBLE) {
                                img_plus.setVisibility(View.GONE);
                                img_minus.setVisibility(View.VISIBLE);
                                lnr_subcategory_rec.setVisibility(View.VISIBLE);
                                getSubCategory(model.getCategory_id());
                            } else {
                                img_plus.setVisibility(View.VISIBLE);
                                img_minus.setVisibility(View.GONE);
                                lnr_subcategory_rec.setVisibility(View.GONE);
                            }
                        } else {
                            Intent intent = new Intent(HomeActivity2.this, CategoryWiseProductListActivity.class);
                            intent.putExtra("sub_cat_id", model.getCategory_id());
                            intent.putExtra("sub_cat_name", model.getCategory_name());
                            intent.putExtra("from", "cat");
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_from_right, R.anim.nothing);
                            drawerlayout.closeDrawers();
                            lnr_main_side.setVisibility(View.GONE);
                            lnr_main_nav.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }

            public void getSubCategory(final String cat_id) {
                if (APIURLs.isNetwork(HomeActivity2.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.SUB_CATEGORY_URL, new Response.Listener<String>() {
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
                                        model.setColor(jsonObject1.getString("color"));
                                        subcategoryModelArrayList.add(model);
                                    }
                                    SubCategoryAdapter adapter = new SubCategoryAdapter(HomeActivity2.this);
                                    rec_side_subcategory.setAdapter(adapter);
                                    rec_side_subcategory.setVisibility(View.VISIBLE);
                                    lnr_subcategory_rec.setVisibility(View.VISIBLE);
                                } else {
                                    rec_side_subcategory.setVisibility(View.GONE);
                                    lnr_subcategory_rec.setVisibility(View.GONE);
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

//                            params.put("franchise_id", SharedPref.getVal(HomeActivity2.this, SharedPref.fran_code));

                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity2.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    dismissDialog();
                    /*final Dialog dialog = new Dialog(HomeActivity2.this);
                    dialog.setContentView(R.layout.no_internet_dialog);
                    Button button = dialog.findViewById(R.id.btn_process);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getSubCategory(cat_id);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();*/
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
                        Intent intent = new Intent(HomeActivity2.this, CategoryWiseProductListActivity.class);
                        intent.putExtra("sub_cat_id", model.getCategory_id());
                        intent.putExtra("sub_cat_name", model.getCategory_name());
                        intent.putExtra("from", "sub_cat");
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.nothing);
                        lnr_main_side.setVisibility(View.GONE);
                        lnr_main_nav.setVisibility(View.VISIBLE);
                        drawerlayout.closeDrawers();
                    }
                });
            }
        }
    }

    public class CategoryListingAdapter extends RecyclerView.Adapter<CategoryListingAdapter.ItemViewholder> {
        Context context;


        public CategoryListingAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public CategoryListingAdapter.ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_list_home_layout, parent, false);
            CategoryListingAdapter.ItemViewholder itemViewholder = new CategoryListingAdapter.ItemViewholder(view);
            return itemViewholder;
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryListingAdapter.ItemViewholder holder, int position) {
            ServicesOffersModel model = servicesOffersModels.get(position);
            ((CategoryListingAdapter.ItemViewholder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return servicesOffersModels.size();
        }

        public class ItemViewholder extends RecyclerView.ViewHolder {
            SliderView slider;
            ImageView logo1, logo2, logo_bg;

            LinearLayout lin_item, lin_comm;
            CardView card_bg;
            RelativeLayout rel_bg;
            TextView txt_name;
            SliderView sliderView1;

            public ItemViewholder(@NonNull View itemView) {
                super(itemView);

//                card_bg = itemView.findViewById(R.id.card_bg);
                lin_item = itemView.findViewById(R.id.lin_item);
                slider = itemView.findViewById(R.id.slider);
                logo1 = itemView.findViewById(R.id.logo1);
                logo2 = itemView.findViewById(R.id.logo2);
                logo_bg = itemView.findViewById(R.id.logo_bg);
                rel_bg = itemView.findViewById(R.id.rel_bg);
                txt_name = itemView.findViewById(R.id.txt_name);
                sliderView1 = itemView.findViewById(R.id.slider);

            }

            public void bind(final ServicesOffersModel model, final int position) {
//                txt_name.setText("");
//
                txt_name.setText(model.getSection_text());
//                Glide.with(HomeActivity2.this).load(model.getSection_icon()).into(logo1);
//                Glide.with(HomeActivity2.this).load(model.getSection_icon()).into(logo2);
//                Glide.with(HomeActivity2.this).load(model.getSection_background()).into(logo_bg);


                HomeServicesAdapter adapter = new HomeServicesAdapter(HomeActivity2.this, imagesArrayList, model);

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

        }

    }


    public class MostPopularProductsListingAdapter extends RecyclerView.Adapter<MostPopularProductsListingAdapter.ItemViewholder> {
        Context context;
        ArrayList<UnitModel> product_unitArraylist = new ArrayList<>();
        Double actualAmout;
        Double discount;
        Double payableAmt;

        public MostPopularProductsListingAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public MostPopularProductsListingAdapter.ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_most_popular_prdcts_list_home_layout, parent, false);
            MostPopularProductsListingAdapter.ItemViewholder itemViewholder = new MostPopularProductsListingAdapter.ItemViewholder(view);
            return itemViewholder;
        }

        @Override
        public void onBindViewHolder(@NonNull MostPopularProductsListingAdapter.ItemViewholder holder, int position) {
            ProductModel model = productModels.get(position);
            ((MostPopularProductsListingAdapter.ItemViewholder) holder).bind(model, position);
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

                        long total_price = Math.round(Double.valueOf(model.getPrice()));

                        txt_og_price.setText("MRP \u20B9" + total_price);
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
                            long price_amount = Math.round(Double.valueOf(actualamt));
                            txt_mrp.setText("MRP \u20b9" + price_amount);


                            double payableAmt = Double.parseDouble(model.getPrice()) - Double.parseDouble(model.getDiscount());
                            double finalpayamt = payableAmt * Double.parseDouble(model.getQty());

                            long finalpayamount = Math.round(Double.valueOf(finalpayamt));
                            txt_og_price.setText("Sterlex Super Mart \u20b9" + finalpayamount);

//                        txt_mrp.setText("MRP \u20B9" + model.getPrice());
//                        txt_discount.setText("Save\n\u20B9" + model.getDiscount());
                            double saveamt = Double.parseDouble(model.getDiscount()) * Double.parseDouble(model.getQty());
                            long save_amount = Math.round(Double.valueOf(saveamt));
                            txt_discount.setText("Save \u20B9" + save_amount);
                        } else {
                            actualAmout = Double.parseDouble(model.getPrice());
                            payableAmt = actualAmout - Double.parseDouble(model.getDiscount());

                            long finalpayamount = Math.round(Double.valueOf(payableAmt));
                            txt_og_price.setText("Sterlex Super Mart \u20b9" + finalpayamount);
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
                Glide.with(HomeActivity2.this).load(model.getImage()).error(R.drawable.loader).into(iv);

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
                            recy_qty.setLayoutManager(new LinearLayoutManager(HomeActivity2.this, RecyclerView.HORIZONTAL, false));
                            QuantityListingAdapter adapter1 = new QuantityListingAdapter(HomeActivity2.this, product_unitArraylist, txt_mrp, txt_og_price, txt_discount, txt_unit, rel_cart,
                                    btn_cart, rel_no, txt_cart_items, txt_count);
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
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity2.this);
                            // Setting Dialog Title
                            alertDialog.setTitle("Please Login");
                            // Setting Dialog Message
                            alertDialog.setMessage("Please login to add product in cart");
                            // On pressing Settings button
                            alertDialog.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(HomeActivity2.this, LoginActivity.class);
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
//                                Toast.makeText(HomeActivity2.this, str_price, Toast.LENGTH_SHORT).show();
                                String discount = txt_discount.getText().toString();
                                String str_discount = "0";
                                if (!discount.equals("0")) {
                                    str_discount = discount.substring(6, discount.length());
//                                    txt_mrp.setVisibility(View.VISIBLE);
//                                    txt_discount.setVisibility(View.VISIBLE);
//                                    Toast.makeText(HomeActivity2.this, str_discount, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(HomeActivity2.this, "out of stock", Toast.LENGTH_SHORT).show();
                            }
                        }
//                        addCart(model.getProduct_id(), model.getUnit(), model.getPrice(), model.getDiscount(), model.getQty(), model.getGst(), position);
                    }
                });


                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity2.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });
                rel_veg_nonveg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity2.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });
                txt_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity2.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });
                txt_mrp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity2.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });
                txt_og_price.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity2.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });
                txt_discount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity2.this, ProductDetailsActivity.class);
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
//                                        Toast.makeText(HomeActivity2.this, str_price, Toast.LENGTH_SHORT).show();
                                        String discount = txt_discount.getText().toString();
                                        String str_discount = "0";
                                        if (!discount.equals("0")) {
                                            str_discount = discount.substring(6, discount.length());
//                                            txt_mrp.setVisibility(View.VISIBLE);
//                                            txt_discount.setVisibility(View.VISIBLE);
//                                            Toast.makeText(HomeActivity2.this, str_discount, Toast.LENGTH_SHORT).show();
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
//                                    Toast.makeText(HomeActivity2.this, "removing", Toast.LENGTH_SHORT).show();
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

                if (APIURLs.isNetwork(HomeActivity2.this)) {
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
                                        long price_amount = Math.round(Double.valueOf(actualamt));
                                        txt_discountprice.setText("MRP \u20B9" + price_amount);

                                        txt_actualprice.setVisibility(View.GONE);
                                        txt_save_amt.setVisibility(View.GONE);
                                    } else {
                                        double actualamt = Double.parseDouble(price) * Double.parseDouble(qty1);
                                        long price_amount = Math.round(Double.valueOf(actualamt));

                                        txt_actualprice.setText("MRP \u20B9" + price_amount);

                                        double payableAmt = Double.parseDouble(price) - Double.parseDouble(discount);
                                        double finalpayamt = payableAmt * Double.parseDouble(qty1);

                                        long finalprice_amount = Math.round(Double.valueOf(finalpayamt));
                                        txt_discountprice.setText("\u20b9" + finalprice_amount);

                                        double saveamt = Double.parseDouble(discount) * Double.parseDouble(qty1);
                                        long save_amount = Math.round(Double.valueOf(saveamt));
                                        txt_save_amt.setText("Save \u20B9" + save_amount);

                                        txt_actualprice.setVisibility(View.VISIBLE);
                                        txt_save_amt.setVisibility(View.VISIBLE);
                                    }
                                    qty = 0;
                                    cartCount();
//                                    getMostPopularProductList();

                           /* productId = new ArrayList<>();
                            unitArrayList = new ArrayList<>();
                            unitArrayList = new ArrayList<>();
                            qtyArrayList = new ArrayList<>();
                            discountArrayList = new ArrayList<>();*/
//                            getProductList();
//                            getProductList();
                                } else {
                                    Toast.makeText(HomeActivity2.this, "Error", Toast.LENGTH_SHORT).show();
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
                            params.put("customer_id", SharedPref.getVal(HomeActivity2.this, SharedPref.customer_id));
                            params.put("product_id", prod_id);
                            params.put("unit", unit);
                            params.put("price", price);
                            params.put("gst", "");
                            params.put("discount", discount);
                            params.put("qty", qty1);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity2.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    dismissDialog();
                    FunctionConstant.noInternetDialog(HomeActivity2.this, "no internet connection");
                }
            }


            public void removeCart(final String product_id, final String unit, final int position) {
//                dialog.show();
                progressBar.setVisibility(View.VISIBLE);
                txt_count.setVisibility(GONE);
                if (APIURLs.isNetwork(HomeActivity2.this)) {
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
                                    Toast.makeText(HomeActivity2.this, "Item removed from cart successfully", Toast.LENGTH_SHORT).show();
                                    cartCount();
//                                    getMostPopularProductList();
                                   /* productId = new ArrayList<>();
                                    unitArrayList = new ArrayList<>();
                                    qtyArrayList = new ArrayList<>();
                                    discountArrayList = new ArrayList<>();*/
                                   /* adapter.notifyDataSetChanged();
                                    adapter.notifyItemRemoved(position);*/

                                    /*rel_cart.setVisibility(View.GONE);
                                    btn_cart.setVisibility(View.VISIBLE);*/
                                } else {
                                    Toast.makeText(HomeActivity2.this, "Error", Toast.LENGTH_SHORT).show();
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
                            params.put("customer_id", SharedPref.getVal(HomeActivity2.this, SharedPref.customer_id));
                            params.put("unit", unit);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity2.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    dismissDialog();
                    FunctionConstant.noInternetDialog(HomeActivity2.this, "no internet connection");
                }
            }
        }


        public class QuantityListingAdapter extends RecyclerView.Adapter<QuantityListingAdapter.ItemViewholder> {
            Context context;
            ArrayList<UnitModel> unitModelArrayList;

            TextView txt_mrp, txt_og_price, txt_discount, txt_unit, txt_cart_items, txt_count;
            RelativeLayout rel_cart, rel_no;
            Button btn_cart;

            int selected_index = 0;

            public QuantityListingAdapter(Context context, ArrayList<UnitModel> unitModelArrayList,
                                          TextView txt_mrp, TextView txt_og_price, TextView txt_discount, TextView txt_unit,
                                          RelativeLayout rel_cart1, Button btn_cart1, RelativeLayout rel_no1,
                                          TextView txt_cart_items1, TextView txt_count1) {
                this.context = context;
                this.unitModelArrayList = unitModelArrayList;
                this.txt_mrp = txt_mrp;
                this.txt_og_price = txt_og_price;
                this.txt_discount = txt_discount;
                this.txt_unit = txt_unit;
                this.txt_cart_items = txt_cart_items1;
                this.txt_count = txt_count1;
                this.rel_cart = rel_cart1;
                this.rel_no = rel_no1;
                this.btn_cart = btn_cart1;

            }

            @NonNull
            @Override
            public QuantityListingAdapter.ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_qty_layout, parent, false);
                QuantityListingAdapter.ItemViewholder itemViewholder = new QuantityListingAdapter.ItemViewholder(view);
                return itemViewholder;

            }

            @Override
            public void onBindViewHolder(@NonNull QuantityListingAdapter.ItemViewholder holder, int position) {
                UnitModel model = unitModelArrayList.get(position);
                ((QuantityListingAdapter.ItemViewholder) holder).bind(model, position);
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
                            itemQty(unitModel.getUnit(), unitModel.getUint_price(), unitModel.getUnit_discount(),
                                    unitModel, txt_cart_items,
                                    txt_count, rel_cart, rel_no,
                                    btn_cart);
                        }
                    });

                    if (selected_index == position) {
                        rel_item.setBackground(getResources().getDrawable(R.drawable.box_edittext_background_colored_layout));
                        txt_unit1.setTextColor(getResources().getColor(R.color.colorWhite));
                        itemQty(unitModel.getUnit(), unitModel.getUint_price(), unitModel.getUnit_discount(), unitModel, txt_cart_items,
                                txt_count, rel_cart, rel_no,
                                btn_cart);
                    } else {
                        rel_item.setBackground(getResources().getDrawable(R.drawable.box_edittext_background_layout));
                        txt_unit1.setTextColor(getResources().getColor(R.color.colorBlack));
                    }

                    rel_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selected_index = position;
                            notifyDataSetChanged();
                            itemQty(unitModel.getUnit(), unitModel.getUint_price(), unitModel.getUnit_discount(), unitModel, txt_cart_items,
                                    txt_count, rel_cart, rel_no,
                                    btn_cart);
                        }
                    });
                }

                public void itemQty(String qty, String price, String discount, UnitModel unitModel, TextView txt_cart_items,
                                    TextView txt_count, RelativeLayout rel_cart, RelativeLayout rel_no, Button btn_cart) {
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

//                    if (unitModel.getUnit_qty().equals("0")) {
//                        rel_cart.setVisibility(GONE);
//                        btn_cart.setVisibility(View.VISIBLE);
//                        rel_no.setVisibility(GONE);
//                    } else {
//                        rel_cart.setVisibility(View.VISIBLE);
//                        btn_cart.setVisibility(GONE);
//                        rel_no.setVisibility(View.VISIBLE);
//                        txt_cart_items.setText(unitModel.getUnit_qty());
//                        txt_count.setText(unitModel.getUnit_qty());
//
//                    }

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


    public class SubcategoryProductListAdapter extends RecyclerView.Adapter<SubcategoryProductListAdapter.ItemViewholder> {
        Context context;
        ArrayList<UnitModel> product_unitArraylist = new ArrayList<>();
        Double actualAmout;
        Double discount;
        Double payableAmt;
        ArrayList<ProductModel> categoryModelArrayList1;

        public SubcategoryProductListAdapter(ArrayList<ProductModel> productModelArrayList) {
            categoryModelArrayList1 = productModelArrayList;
        }

        @NonNull
        @Override
        public SubcategoryProductListAdapter.ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_listing_home, parent, false);
            SubcategoryProductListAdapter.ItemViewholder itemViewholder = new SubcategoryProductListAdapter.ItemViewholder(view);
            return itemViewholder;
        }

        @Override
        public void onBindViewHolder(@NonNull SubcategoryProductListAdapter.ItemViewholder holder, int position) {
            ProductModel model = categoryModelArrayList1.get(position);
            ((SubcategoryProductListAdapter.ItemViewholder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return categoryModelArrayList1.size();
        }

        public class ItemViewholder extends RecyclerView.ViewHolder {
            ImageView img_product, iv_veg, iv_nonveg;

            LinearLayout lin_item, lin_comm;
            CardView card_unit, card_add_toCart;
            RelativeLayout rel_veg_nonveg;
            TextView txt_name, txt_mrp, txt_discount_price, txt_discount, txt_count;
            TextView txt_unit;
            Button btn_cart;
            LikeButton btn_like;
            ImageView iv;
            int flag_qty = 0, qty_open_close_flag = 0;
            RelativeLayout rel_qty, rel_stock, rel_save, rel_discount, rel_cart, rel_minus, rel_plus, rel_subscribe, rel_subscribeminus, rel_subscribeplus, rel_cartmain;
            double ogPrice = 0.0, price = 0.0, discount = 0.0, totalPrice = 0.0;
            ProgressBar progressBar;

            public ItemViewholder(@NonNull View itemView) {
                super(itemView);

                img_product = itemView.findViewById(R.id.img_product);
                iv = itemView.findViewById(R.id.iv);
                iv_veg = itemView.findViewById(R.id.iv_veg);
                iv_nonveg = itemView.findViewById(R.id.iv_nonveg);
                rel_veg_nonveg = itemView.findViewById(R.id.rel_veg_nonveg);
                rel_cart = itemView.findViewById(R.id.rel_cart);
                rel_stock = itemView.findViewById(R.id.rel_stock);
                rel_cartmain = itemView.findViewById(R.id.rel_cartmain);
                rel_minus = itemView.findViewById(R.id.rel_minus);
                rel_plus = itemView.findViewById(R.id.rel_plus);
                rel_save = itemView.findViewById(R.id.rel_save);
                rel_qty = itemView.findViewById(R.id.rel_qty);
                card_unit = itemView.findViewById(R.id.card_unit);
                btn_cart = itemView.findViewById(R.id.btn_cart);
                txt_name = itemView.findViewById(R.id.txt_name);
                txt_mrp = itemView.findViewById(R.id.txt_mrp_price);
                txt_discount_price = itemView.findViewById(R.id.txt_discount_price);
                txt_discount = itemView.findViewById(R.id.txt_discount);
                txt_unit = itemView.findViewById(R.id.txt_unit);
                lin_item = itemView.findViewById(R.id.lin_item);
                txt_count = itemView.findViewById(R.id.txt_count);
                card_unit = itemView.findViewById(R.id.card_unit);
                btn_like = itemView.findViewById(R.id.btn_like);
                progressBar = itemView.findViewById(R.id.progressBar);
                txt_mrp.setPaintFlags(txt_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


            }

            public void bind(final ProductModel model, final int position) {

                if (!model.getSave_id().equals("")) {
                    btn_like.setLiked(true);
                } else {
                    btn_like.setLiked(false);
                }

                final Double pay_price = Double.valueOf(model.getPrice()) - Double.valueOf(model.getDiscount());

                txt_mrp.setText("MRP: \u20B9" + model.getPrice());
                txt_discount_price.setText("\u20B9" + pay_price.toString());
                Glide.with(HomeActivity2.this).load(model.getImage()).error(R.drawable.loader).into(img_product);
                if (!model.getDiscount().equals("0")) {
                    txt_discount.setText("SAVE \u20B9" + model.getDiscount());
                    txt_mrp.setVisibility(View.VISIBLE);
                } else {
                    rel_save.setVisibility(GONE);
                    txt_mrp.setVisibility(GONE);
                }
                txt_name.setText(model.getProduct_name());
                txt_unit.setText(model.getUnit());

                if (model.getProduct_unitArraylist().size() > 1) {
                    iv.setVisibility(View.VISIBLE);
                }

//                if (model.getFlag().equals("0")) {
//                    rel_stock.setVisibility(View.VISIBLE);
//                } else if (model.getFlag().equals("1")) {
//                    rel_stock.setVisibility(GONE);
//                }

                if (model.getQty().equals("0")) {
                    rel_cart.setVisibility(GONE);
                    btn_cart.setVisibility(View.VISIBLE);
                } else {
                    rel_cart.setVisibility(View.VISIBLE);
                    btn_cart.setVisibility(GONE);
                    txt_count.setText(model.getQty());

                }


                btn_like.setOnLikeListener(new OnLikeListener() {
                    @Override
                    public void liked(LikeButton likeButton) {
                        String mrp = txt_mrp.getText().toString();
                        mrp = mrp.substring(6, mrp.length());
                        String save = txt_discount.getText().toString();
                        save = save.substring(6, save.length());
                        addToWishList(model.getProduct_id(), txt_unit.getText().toString(), mrp, save, btn_like, model);
                    }

                    @Override
                    public void unLiked(LikeButton likeButton) {

                        removeWishList(model.getSave_id(), position, btn_like);
                    }
                });
                btn_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cost_id.equals("")) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity2.this);
                            // Setting Dialog Title
                            alertDialog.setTitle("Please Login");
                            // Setting Dialog Message
                            alertDialog.setMessage("Please login to add product in cart");
                            // On pressing Settings button
                            alertDialog.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(HomeActivity2.this, LoginActivity.class);
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
//                                Toast.makeText(HomeActivity2.this, str_price, Toast.LENGTH_SHORT).show();
                                String discount = txt_discount.getText().toString();
                                String str_discount = "0";
                                if (!discount.equals("0")) {
                                    str_discount = discount.substring(6, discount.length());
//                                    txt_mrp.setVisibility(View.VISIBLE);
//                                    txt_discount.setVisibility(View.VISIBLE);
//                                    Toast.makeText(HomeActivity2.this, str_discount, Toast.LENGTH_SHORT).show();
                                }
                                if (!cart_click) {
                                    cart_unit = model.getUnit();
                                    cart_unit_price = model.getPrice();
                                    cart_discount = model.getDiscount();
                                }
                                addCart(model.getProduct_id(), cart_unit, cart_unit_price,
                                        cart_discount, txt_count.getText().toString(), model.getGst(), position, txt_discount_price, txt_mrp, txt_discount
                                );
                                txt_count.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(HomeActivity2.this, "out of stock", Toast.LENGTH_SHORT).show();
                            }
                        }
//                        addCart(model.getProduct_id(), model.getUnit(), model.getPrice(), model.getDiscount(), model.getQty(), model.getGst(), position);
                    }
                });

                rel_qty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        subproduct_unitArraylist = new ArrayList<>();
                        subproduct_unitArraylist = model.getProduct_unitArraylist();
                        try {

                            if (subproduct_unitArraylist.size() > 1) {

                                final Dialog dialog1 = new Dialog(HomeActivity2.this);
                                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog1.setContentView(R.layout.product_unit_dialog_layout);
                                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                dialog1.getWindow().setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                dialog1.getWindow().setGravity(Gravity.CENTER);
                                dialog1.getWindow().setDimAmount((float) 0.8);
                                dialog1.setCancelable(true);
                                dialog1.setCanceledOnTouchOutside(true);
                                final TextView txt_title = dialog1.findViewById(R.id.txt_title);
                                final RecyclerView recy_list = dialog1.findViewById(R.id.recy_list);

                                txt_title.setText(model.getProduct_name());

                                recy_list.setLayoutManager(new LinearLayoutManager(HomeActivity2.this));
                                UnitListingAdapter adapter1 = new UnitListingAdapter(model.getProduct_id(), subproduct_unitArraylist, txt_discount_price, txt_discount, txt_mrp, dialog1, rel_save, txt_unit, txt_unit.getText().toString(), btn_like);
                                recy_list.setAdapter(adapter1);

//        Button btn_process = dialog1.findViewById(R.id.btn_process);
//
//        btn_process.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog1.dismiss();
//                if (edt_pincode.getText().toString().equals("")) {
//                    Toast.makeText(HomeActivity.this, "Please enter pincode", Toast.LENGTH_SHORT).show();
//                } else {
//                    checkPincode(edt_pincode.getText().toString());
//                }
//            }
//        });
                                dialog1.show();

//                            recy_qty.setVisibility(GONE);
//
//                            flag_qty = 1;

                            } else {
                                flag_qty = 0;


//                            recy_qty.setVisibility(GONE);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            flag_qty = 0;
                            card_unit.setVisibility(GONE);
//                    recy_qty.setVisibility(GONE);

                        }

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
//                                        String price = txt_mrp.getText().toString();
//                                        String str_price = price.substring(5, price.length());
//                                        Toast.makeText(HomeActivity2.this, str_price, Toast.LENGTH_SHORT).show();
                                        String discount = txt_discount.getText().toString();
                                        String str_discount = "0";
                                        if (!discount.equals("0")) {
                                            str_discount = discount.substring(6, discount.length());
//                                            txt_mrp.setVisibility(View.VISIBLE);
//                                            txt_discount.setVisibility(View.VISIBLE);
//                                            Toast.makeText(HomeActivity2.this, str_discount, Toast.LENGTH_SHORT).show();
                                        }

                                        if (!cart_click) {
                                            cart_unit = model.getUnit();
                                            cart_unit_price = model.getPrice();
                                            cart_discount = model.getDiscount();
                                        }
                                        addCart(model.getProduct_id(), cart_unit, cart_unit_price,
                                                cart_discount, txt_count.getText().toString(), model.getGst(), position, txt_discount_price, txt_mrp, txt_discount);
                                    }
                                    /*else if(qty==1){
                                        rel_cart.setVisibility(View.GONE);
                                        btn_cart.setVisibility(View.VISIBLE);
                                        removeCart(model.getProduct_id(),position);
                                    }*/
                                } else {
                                    rel_cart.setVisibility(GONE);
                                    btn_cart.setVisibility(View.VISIBLE);
//                                    Toast.makeText(HomeActivity2.this, "removing", Toast.LENGTH_SHORT).show();
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
                                    txt_count.getText().toString(), model.getGst(), position, txt_discount_price, txt_mrp, txt_discount);
                        } else {
                            Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

//
                img_product.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity2.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });

                txt_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity2.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });


            }


            /*Add to wishlist*/
            public void addToWishList(final String prod_id, final String unit, final String price, final String discount, final LikeButton btn, final ProductModel unitModel) {
//        rel_progress.setVisibility(View.VISIBLE);
                if (APIURLs.isNetwork(HomeActivity2.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.add_to_wishlist, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject1 = new JSONObject(response);
                                JSONArray jsonArray = jsonObject1.getJSONArray("result");
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String status = jsonObject.getString("status");
                                String msg = jsonObject.getString("msg");
                                if (status.equals("1")) {
                                    Snackbar.make(findViewById(android.R.id.content), "Product added to wish list", Snackbar.LENGTH_LONG).show();
                                    btn.setLiked(true);
                                    unitModel.setSave_id(jsonObject.getString("save_id"));
                                } else {
                                    Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show();
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
                            params.put("customer_id", SharedPref.getVal(HomeActivity2.this, SharedPref.customer_id));
                            params.put("product_id", prod_id);
                            params.put("unit", unit);
                            params.put("price", price);
                            params.put("discount", discount);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity2.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    Toast.makeText(HomeActivity2.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }

            /*Remove from wishlist*/
            public void removeWishList(final String product_id, int position, final LikeButton btn) {
                if (APIURLs.isNetwork(HomeActivity2.this)) {
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
                                    btn.setLiked(false);
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
//                            params.put("customer_id", SharedPref.getVal(HomeActivity2.this, SharedPref.customer_id));
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity2.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    Toast.makeText(HomeActivity2.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }


            public void addCart(final String prod_id, final String unit, final String price, final String discount, final String qty1, final String gst, final int position, final TextView txt_discountprice,
                                final TextView txt_actualprice,
                                final TextView txt_save_amt
            ) {
//                dialog.show();
                progressBar.setVisibility(View.VISIBLE);
                txt_count.setVisibility(GONE);

                if (APIURLs.isNetwork(HomeActivity2.this)) {
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
                                    txt_count.setVisibility(View.VISIBLE);
                                    txt_count.setText(qty1);

                                    if (discount.equals("0")) {
                                        double actualamt = Double.parseDouble(price) * Double.parseDouble(qty1);
                                        long price_amount = Math.round(Double.valueOf(actualamt));

                                        txt_discountprice.setText("MRP \u20B9" + price_amount);

                                        txt_actualprice.setVisibility(View.GONE);
                                        txt_save_amt.setVisibility(View.GONE);
                                    } else {
                                        double actualamt = Double.parseDouble(price) * Double.parseDouble(qty1);
                                        long price_amount = Math.round(Double.valueOf(actualamt));

                                        txt_actualprice.setText("MRP \u20B9" + price_amount);

                                        double payableAmt = Double.parseDouble(price) - Double.parseDouble(discount);
                                        double finalpayamt = payableAmt * Double.parseDouble(qty1);

                                        long final_amount = Math.round(Double.valueOf(finalpayamt));
                                        txt_discountprice.setText("\u20b9" + final_amount);

                                        double saveamt = Double.parseDouble(discount) * Double.parseDouble(qty1);
                                        long save_amount = Math.round(Double.valueOf(saveamt));

                                        txt_save_amt.setText("Save \u20B9" + save_amount);

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
                                    Toast.makeText(HomeActivity2.this, "Error", Toast.LENGTH_SHORT).show();
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
                            params.put("customer_id", SharedPref.getVal(HomeActivity2.this, SharedPref.customer_id));
                            params.put("product_id", prod_id);
                            params.put("unit", unit);
                            params.put("price", price);
                            params.put("gst", "");
                            params.put("discount", discount);
                            params.put("qty", qty1);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity2.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    dismissDialog();
                    FunctionConstant.noInternetDialog(HomeActivity2.this, "no internet connection");
                }
            }


            public void removeCart(final String product_id, final String unit, final int position) {
//                dialog.show();
                progressBar.setVisibility(View.VISIBLE);
                txt_count.setVisibility(GONE);
                if (APIURLs.isNetwork(HomeActivity2.this)) {
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
                                    Toast.makeText(HomeActivity2.this, "Item removed from cart successfully", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(HomeActivity2.this, "Error", Toast.LENGTH_SHORT).show();
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
                            params.put("customer_id", SharedPref.getVal(HomeActivity2.this, SharedPref.customer_id));
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity2.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    dismissDialog();
                    FunctionConstant.noInternetDialog(HomeActivity2.this, "no internet connection");
                }
            }


        }

        public class UnitListingAdapter extends RecyclerView.Adapter<UnitListingAdapter.ItemViewholder> {
            Context context;
            ArrayList<UnitModel> unitModelArrayList;
            String compare_unit;
            TextView txt_discount_price, txt_discount, txt_mrp, txt_unit;
            Dialog dialog;
            RelativeLayout rel_save;
            LikeButton btn_like;
            int selected_index = 0;
            String prod_id = "";

            public UnitListingAdapter(String prod_id1, ArrayList<UnitModel> unitModelArrayList, TextView txt_discount_price, TextView txt_discount, TextView txt_mrp, Dialog dialog1, RelativeLayout rel_save, TextView txt_unit, String compare_unit,
                                      LikeButton btn_like1) {
                this.unitModelArrayList = unitModelArrayList;
                this.txt_discount_price = txt_discount_price;
                this.txt_discount = txt_discount;
                this.txt_mrp = txt_mrp;
                this.dialog = dialog1;
                this.rel_save = rel_save;
                this.txt_unit = txt_unit;
                this.compare_unit = compare_unit;
                this.btn_like = btn_like1;
                this.prod_id = prod_id1;
            }

            @NonNull
            @Override
            public ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_unit_layout, parent, false);
                ItemViewholder itemViewholder = new ItemViewholder(view);
                return itemViewholder;

            }

            @Override
            public void onBindViewHolder(@NonNull ItemViewholder holder, int position) {
                UnitModel model = unitModelArrayList.get(position);
                ((ItemViewholder) holder).bind(model, position);
            }

            @Override
            public int getItemCount() {
                return unitModelArrayList.size();

            }

            public class ItemViewholder extends RecyclerView.ViewHolder {

                TextView name, pay_price, price;
                RelativeLayout rel_item;
                LinearLayout lin_item;

                public ItemViewholder(@NonNull View itemView) {
                    super(itemView);

                    lin_item = itemView.findViewById(R.id.lin_item);
                    name = itemView.findViewById(R.id.name);
                    pay_price = itemView.findViewById(R.id.pay_price);
                    price = itemView.findViewById(R.id.price);
                    rel_item = itemView.findViewById(R.id.rel_item);
                    price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                }

                public void bind(final UnitModel unitModel, final int position) {

                    if (!unitModel.getSave_id().equals("")) {
                        btn_like.setLiked(true);
                    } else {
                        btn_like.setLiked(false);

                    }


                    if (compare_unit.equals(unitModel.getUnit())) {
                        name.setTextColor(getResources().getColor(R.color.colorPrimary));
                        pay_price.setTextColor(getResources().getColor(R.color.colorPrimary));
                        price.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                    name.setText(unitModel.getUnit());

                    if (!unitModel.getUnit_discount().equals("0")) {
                        Double pay_price1 = Double.valueOf(unitModel.getUint_price()) - Double.valueOf(unitModel.getUnit_discount());

                        pay_price.setText("Pay \u20B9" + pay_price1.toString());
                        price.setText("MRP: \u20B9" + unitModel.getUint_price());

                    } else {
                    }

//                    for (int i = 0; i <= unitModelArrayList.size(); i++) {
//                        String str = unitModel.getUnit();
//                        if (prod_Qty.equals(str)) {
//                            rel_item.setBackground(getResources().getDrawable(R.drawable.box_edittext_background_colored_layout));
//                        } else {
//                            rel_item.setBackground(getResources().getDrawable(R.drawable.box_edittext_background_layout));
//                        }
//                    }
                    btn_like.setOnLikeListener(new OnLikeListener() {
                        @Override
                        public void liked(LikeButton likeButton) {
                            String mrp = txt_mrp.getText().toString();
                            mrp = mrp.substring(6, mrp.length());
                            String save = txt_discount.getText().toString();
                            save = save.substring(6, save.length());
                            addToWishList(prod_id, txt_unit.getText().toString(), mrp, save, btn_like, unitModel);
                        }

                        @Override
                        public void unLiked(LikeButton likeButton) {

                            removeWishList(unitModel.getSave_id(), position, btn_like);
                        }
                    });
                    lin_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Double pay_price1 = Double.valueOf(unitModel.getUint_price()) - Double.valueOf(unitModel.getUnit_discount());
                            txt_discount_price.setText("\u20B9" + pay_price1);
                            txt_discount.setText("SAVE \u20B9" + unitModel.getUnit_discount());
                            txt_mrp.setText("MRP: \u20B9" + unitModel.getUint_price());
                            txt_unit.setText(unitModel.getUnit());
                            if (unitModel.getUnit_discount().equals("0")) {
                                rel_save.setVisibility(GONE);
                            }
                            dialog.dismiss();
                        }
                    });

//                    if (selected_index == position) {
//                        rel_item.setBackground(getResources().getDrawable(R.drawable.box_edittext_background_colored_layout));
//                        txt_unit1.setTextColor(getResources().getColor(R.color.colorWhite));
//                        itemQty(unitModel.getUnit(), unitModel.getUint_price(), unitModel.getUnit_discount());
//                    } else {
//                        rel_item.setBackground(getResources().getDrawable(R.drawable.box_edittext_background_layout));
//                        txt_unit1.setTextColor(getResources().getColor(R.color.colorBlack));
//                    }

//                    rel_item.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            selected_index = position;
//                            notifyDataSetChanged();
//                            itemQty(unitModel.getUnit(), unitModel.getUint_price(), unitModel.getUnit_discount());
//                        }
//                    });
//                }

//                public void itemQty(String qty, String price, String discount) {
//                    cart_discount = discount;
//                    cart_unit_price = price;
//                    cart_unit = qty;
//                    cart_click = true;
//                    txt_unit.setText(qty);
//                    Double sprice = Double.parseDouble(price) - Double.parseDouble(discount);
//                    txt_og_price.setText("Sterlex Super Mart \u20B9" + sprice);
//                    if (discount.equals("0")) {
//                        txt_discount.setVisibility(GONE);
//                        txt_mrp.setVisibility(GONE);
//                    } else {
//                        txt_discount.setVisibility(View.VISIBLE);
//                        txt_mrp.setVisibility(View.VISIBLE);
//                        txt_discount.setText("Save \u20B9" + discount);
//                        txt_mrp.setText("MRP \u20B9" + price);
//                    }


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


            /*Add to wishlist*/
            public void addToWishList(final String prod_id, final String unit, final String price, final String discount, final LikeButton btn, final UnitModel unitModel) {
//        rel_progress.setVisibility(View.VISIBLE);
                if (APIURLs.isNetwork(HomeActivity2.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.add_to_wishlist, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject1 = new JSONObject(response);
                                JSONArray jsonArray = jsonObject1.getJSONArray("result");
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String status = jsonObject.getString("status");
                                String msg = jsonObject.getString("msg");
                                if (status.equals("1")) {
                                    Snackbar.make(findViewById(android.R.id.content), "Product added to wish list", Snackbar.LENGTH_LONG).show();
                                    btn.setLiked(true);
                                    unitModel.setSave_id(jsonObject.getString("save_id"));
                                } else {
                                    Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show();
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
                            params.put("customer_id", SharedPref.getVal(HomeActivity2.this, SharedPref.customer_id));
                            params.put("product_id", prod_id);
                            params.put("unit", unit);
                            params.put("price", price);
                            params.put("discount", discount);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity2.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    Toast.makeText(HomeActivity2.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }

            /*Remove from wishlist*/
            public void removeWishList(final String product_id, int position, final LikeButton btn) {
                if (APIURLs.isNetwork(HomeActivity2.this)) {
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
                                    btn.setLiked(false);
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
//                            params.put("customer_id", SharedPref.getVal(HomeActivity2.this, SharedPref.customer_id));
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity2.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    Toast.makeText(HomeActivity2.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }


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
            ((ShopByCategoryListingAdapter.ItemViewholder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {

            return categoryModels.size();

        }

        public class ItemViewholder extends RecyclerView.ViewHolder {
            ImageView img_category;

            CardView card_item;
            LinearLayout lin_viewall_category, lnr_category;
            CardView card_bg;
            RelativeLayout lin_owner;
            TextView txt_name;

            public ItemViewholder(@NonNull View itemView) {
                super(itemView);
                card_item = itemView.findViewById(R.id.card_item);
                lin_viewall_category = findViewById(R.id.lin_viewall_category);
                lnr_category = itemView.findViewById(R.id.lnr_category);
                img_category = itemView.findViewById(R.id.img_category);
                txt_name = itemView.findViewById(R.id.txt_name);

            }

            public void bind(final CategoryModel model, final int position) {
                if (!model.getColor().equals("")) {
                    card_item.setCardBackgroundColor(Color.parseColor(model.getColor()));
                }
                txt_name.setText(model.getCategory_name());
                Glide.with(HomeActivity2.this).load(model.getImage()).error(R.drawable.loader).
                        placeholder(R.drawable.loader).into(img_category);

                lnr_category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (model.getSubcategorey().equals("1")) {
                            Intent intent = new Intent(HomeActivity2.this, SubCategoryListingActivity.class);
                            intent.putExtra("cat_id", model.getCategory_id());
                            intent.putExtra("cat_name", model.getCategory_name());
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(HomeActivity2.this, CategoryWiseProductListActivity.class);
                            intent.putExtra("sub_cat_id", model.getCategory_id());
                            intent.putExtra("sub_cat_name", model.getCategory_name());
                            intent.putExtra("from", "cat");
                            startActivity(intent);
                        }
                    }
                });

                lin_viewall_category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(HomeActivity2.this, SubCategoryHomeActivity.class));
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
                startActivity(new Intent(HomeActivity2.this, LoginActivity.class));
            }
        });

        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity2.this, OtpActivity.class));
            }
        });
        dialog1.show();
    }


    /*Slider Adapter*/
    public class ViewPagerAdapter extends PagerAdapter {

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(HomeActivity2.this);
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.viewpager_item_layout, container, false);
            ImageView img_banner = layout.findViewById(R.id.img_banner);

            Glide.with(HomeActivity2.this).load(bannerModelArrayList.get(position).getBanner())
                    .error(R.drawable.loader).placeholder(R.drawable.loader).into(img_banner);
            container.addView(layout);
            return layout;
        }

        @Override
        public int getCount() {
            return bannerModelArrayList.size();
        }


        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }


        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }


    public void versionDialog() {
        final Dialog dialog1 = new Dialog(HomeActivity2.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.appupdatelayout);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog1.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog1.getWindow().setGravity(Gravity.CENTER);
        dialog1.getWindow().setDimAmount((float) 0.8);
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setCancelable(false);
        TextView txt_update = dialog1.findViewById(R.id.txt_update);
        TextView txt_cancel = dialog1.findViewById(R.id.txt_cancel);

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                APIURLs.cancleDialog = true;
                finishAffinity();
            }
        });

        txt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String playstore_link = "https://play.google.com/store/apps/details?id=" +
//                        getApplicationContext().getPackageName() +
//                        "&referrer=" + Html.fromHtml(SharedPref.getVal(HomeActivity2.this, SharedPref.customer_unique_id));
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(playstore_link)));
//                APIURLs.cancleDialog = false;
                dialog1.dismiss();
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
        lin_parent.setVisibility(View.VISIBLE);
        shimmer_view_container.setVisibility(View.GONE);
    }


    @Override
    protected void onPause() {
        shimmer_view_container.stopShimmerAnimation();
        lin_parent.setVisibility(View.VISIBLE);
        shimmer_view_container.setVisibility(View.GONE);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        shimmer_view_container.startShimmerAnimation();
        lin_parent.setVisibility(View.GONE);
        shimmer_view_container.setVisibility(View.VISIBLE);

        cost_id = SharedPref.getVal(HomeActivity2.this, SharedPref.customer_id);
        if (!cost_id.equals("")) {
            SharedPref.putBol(HomeActivity2.this, SharedPref.isLogin, true);
        }

        getProfile();
        edt_search.setText("");
        if (!cost_id.equals("")) {
//            img_userMenu.setVisibility(GONE);
            cartCount();
        } else {
//            img_userMenu.setVisibility(View.VISIBLE);
        }

        getCategoryList();

        getCategory();
        getSubCategoryAndProductsListing();

    }
}