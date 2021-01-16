package com.sterlex.in.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sterlex.in.Database.DatabaseHelper;
import com.sterlex.in.Model.HotProductModel;
import com.sterlex.in.Model.NotificationModel;
import com.sterlex.in.Model.OfferModel;
import com.gmail.samehadar.iosdialog.IOSDialog;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.sterlex.in.Adapter.HomeSliderAdapter;
import com.sterlex.in.Constant.APIURLs;
import com.sterlex.in.Constant.PrefManager;
import com.sterlex.in.Constant.SharedPref;
import com.sterlex.in.Model.BannerModel;
import com.sterlex.in.Model.CategoryModel;
import com.sterlex.in.Model.ProductModel;
import com.sterlex.in.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

//    LinearLayout lnr_home, lnr_home_selected, lnr_search, lnr_search_selected, lnr_main,
//            lnr_cart, lnr_cart_selected, lnr_subscription, lnr_subscription_selected, lnr_account, lnr_account_selected;
//    RelativeLayout rel_home, rel_search, rel_cart, rel_subscription, rel_account;
//    RecyclerView recyc_recommended, rec_category, rec_seller, rec_offer, rec_hot,
//            rec_todayoffer;
//    LinearLayout lnr_seller, lnr_offers, lnr_hot, lnr_todayoffers;
//    LinearLayout lnr_recommend;
//    TextView txt_cart_count, txt_address, txt_timer, txt_todaytitle;
//    RelativeLayout rel_cart_count;
    PrefManager prefManager;
    ArrayList<BannerModel> bannerModelArrayList = new ArrayList<>();
    ArrayList<CategoryModel> categoryModelArrayList = new ArrayList<>();
    ArrayList<ProductModel> productModelArrayList = new ArrayList<>();
    ArrayList<ProductModel> todayoffersModelArrayList = new ArrayList<>();
    ArrayList<OfferModel> offerModelArrayList = new ArrayList<>();
    ArrayList<HotProductModel> hotModelArrayList = new ArrayList<>();
    ArrayList<NotificationModel> notificationModelArrayList = new ArrayList<>();
    IOSDialog dialog;
    Double latitude, longitude;
    ViewPager viewpager;
//    ShimmerFrameLayout shimmer_view_container;
    String cost_id = "";
    String newVersion;
    DatabaseHelper databaseHelper;
//    RelativeLayout rel_noti_count;
//    TextView txt_noti_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        setContentView(R.layout.activity_home);
        prefManager = new PrefManager(HomeActivity.this);
        prefManager.setFirstTimeLaunch(false);
        databaseHelper = new DatabaseHelper(HomeActivity.this);

        cost_id = SharedPref.getVal(HomeActivity.this, SharedPref.customer_id);
        if (!cost_id.equals("")) {
            SharedPref.putBol(HomeActivity.this, SharedPref.isLogin, true);
        }
//        init();
//        onclick();
//        getBanners();
//        getCategoryList();
//        getBestSelling();
//        getOffers();
//        getHotProducts();
//        getTodayOffers();
//        getNotificationSize();
        boolean firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun", true);
        if (firstrun) {
//            showPincodeDialog();
            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("firstrun", false)
                    .commit();
        }

    }

    public void Slider() {
        SliderView sliderView = findViewById(R.id.imageSlider);

        HomeSliderAdapter adapter = new HomeSliderAdapter(this, bannerModelArrayList);
/*
        viewpager.setAdapter(adapter);
        viewpager.setClipToPadding(false);
        // set padding manually, the more you set the padding the more you see of prev & next page
        viewpager.setPadding(40, 0, 40, 0);
        // sets a margin b/w individual pages to ensure that there is a gap b/w them
        viewpager.setPageMargin(20);

        final int NUM_PAGES = bannerModelArrayList.size();
        final int[] currentPage = {0};

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage[0] == NUM_PAGES) {
                    currentPage[0] = 0;
                }
                viewpager.setCurrentItem(currentPage[0]++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 5000, 5000);*/

        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);

        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();
    }

//    public void getBanners() {
//        shimmer_view_container.startShimmerAnimation();
//        shimmer_view_container.setVisibility(View.VISIBLE);
//        lnr_main.setVisibility(View.GONE);
//        if (APIURLs.isNetwork(HomeActivity.this)) {
//
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.BANNER_URL, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        String status = jsonObject.getString("status");
//                        if (status.equals("1")) {
//                            bannerModelArrayList = new ArrayList<>();
//                            JSONArray jsonArray = jsonObject.getJSONArray("result");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                                BannerModel model = new BannerModel();
//                                model.setBanner_id(jsonObject1.getString("banner_id"));
//                                model.setBanner(jsonObject1.getString("banner"));
//                                model.setCategory_id(jsonObject1.getString("category_id"));
//                                bannerModelArrayList.add(model);
//                            }
//                            Slider();
//                        } else {
////                            ErrorToast("Error"); //Error Toast
//                            Toast.makeText(HomeActivity.this, "no data found", Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    dismissDialog();
//                    error.printStackTrace();
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    HashMap<String, String> params = new HashMap<>();
//                    if (!cost_id.equals("")) {
//                        params.put("franchise_id", SharedPref.getVal(HomeActivity.this, SharedPref.fran_code));
//                    } else {
//                        params.put("franchise_id", "1");
//                    }
//                    return params;
//                }
//            };
//
//
//            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            requestQueue.add(stringRequest);
//        } else {
//            dismissDialog();
////            FunctionConstant.noInternetDialog(HomeActivity.this,"on internet connection");
//            final Dialog dialog = new Dialog(HomeActivity.this);
//            dialog.setContentView(R.layout.no_internet_dialog);
//            Button button = dialog.findViewById(R.id.btn_process);
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    getVersionCode();
//                    getBestSelling();
//                    getCategoryList();
//                    getOffers();
//                    getBanners();
//                    getTodayOffers();
//                    getHotProducts();
//                    getNotificationSize();
//                    cartCount();
//                    dialog.dismiss();
//                }
//            });
//            dialog.show();
//        }
//    }

//    public void getCurrentLocation() {
//        try {
//            Geocoder geocoder;
//            List<Address> addresses;
//            latitude = new GPSTracker(this).getLatitude();
//            longitude = new GPSTracker(this).getLongitude();
//
//            if (latitude != 0.0) {
//                try {
//
//                    geocoder = new Geocoder(this, Locale.getDefault());
//                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//
//                    String add = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
////                txt_location.setText(address);
//                    String pin = addresses.get(0).getPostalCode();
//
////                    if (pin.equals("400614") || pin.equals("400705") || pin.equals("400706") || pin.equals("410208") || pin.equals("410210")) {
//                    txt_address.setText(add);
////                    } else {
////                        txt_address.setText(add);
////                        new AlertDialog.Builder(HomeActivity.this)
////                                .setTitle("Service unavailable")
////                                .setMessage("Currently we are not providing service in your area.")
////
////                                // Specifying a listener allows you to take an action before dismissing the dialog.
////                                // The dialog is automatically dismissed when a dialog button is clicked.
////                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
////                                    public void onClick(DialogInterface dialog, int which) {
////                                        // Continue with delete operation
////                                        dialog.dismiss();
////                                    }
////                                })
////                                .show();
////                    }
//
//                } catch (Exception es) {
//                    es.printStackTrace();
//                }
//
//            }
//            /*else {
//                showGpsDisabledDialog();
//            }*/
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
        // Setting Dialog Title
        alertDialog.setTitle("Permission Needed");
        // Setting Dialog Message
        alertDialog.setMessage("location permission needed to access this application.\n" +
                "Go to settings -> Permissions -> allow location Permission ");
        // On pressing Settings button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void showGpsDisabledDialog() {
        if (!((Activity) HomeActivity.this).isFinishing()) {
            new AlertDialog.Builder(HomeActivity.this)
                    .setMessage("GPS Disabled , to Continue, turn on your device location ")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                            startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
                            dialog.dismiss();
                        }
                    })

                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                            dialog.dismiss();
                        }
                    })

                    /*// A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finishAffinity();
                        }
                    })*/
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .show();
        }
    }

//    public void getCategoryList() {
////        dialog.show();
//        shimmer_view_container.startShimmerAnimation();
//        shimmer_view_container.setVisibility(View.VISIBLE);
//        lnr_main.setVisibility(View.GONE);
//        if (APIURLs.isNetwork(HomeActivity.this)) {
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.CATEGORY_URL, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        String status = jsonObject.getString("status");
//                        if (status.equals("1")) {
//                            categoryModelArrayList = new ArrayList<>();
//                            JSONArray jsonArray = jsonObject.getJSONArray("result");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                                CategoryModel model = new CategoryModel();
//                                model.setCategory_id(jsonObject1.getString("category_id"));
//                                model.setCategory_name(jsonObject1.getString("category_name"));
//                                model.setImage(jsonObject1.getString("image"));
//                                model.setSubcategorey(jsonObject1.getString("subcategorey"));
//                                model.setColor(jsonObject1.getString("color"));
//                                categoryModelArrayList.add(model);
//                            }
//                            CategoryAdapter adapter = new CategoryAdapter(HomeActivity.this);
//                            rec_category.setAdapter(adapter);
//                        } else {
////                            ErrorToast("Error"); //Error Toast
//                            Toast.makeText(HomeActivity.this, "Error", Toast.LENGTH_SHORT).show();
//                        }
//                        dismissDialog();
//                    } catch (JSONException e) {
//                        dismissDialog();
//                        e.printStackTrace();
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    dismissDialog();
//                    error.printStackTrace();
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    HashMap<String, String> params = new HashMap<>();
//                    if (!cost_id.equals("")) {
//                        params.put("franchise_id", SharedPref.getVal(HomeActivity.this, SharedPref.fran_code));
//                    } else {
//                        params.put("franchise_id", "1");
//                    }
//                    return params;
//                }
//            };
//
//            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            requestQueue.add(stringRequest);
//        } else {
//            dismissDialog();
////            FunctionConstant.noInternetDialog(HomeActivity.this, "no internet connection");
////            final Dialog dialog = new Dialog(HomeActivity.this);
////            dialog.setContentView(R.layout.no_internet_dialog);
////            Button button = dialog.findViewById(R.id.btn_process);
////            button.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    getCategoryList();
////                    dialog.dismiss();
////                }
////            });
////            dialog.show();
//
//        }
//    }

    public void getVersionCode() {
        if (APIURLs.isNetwork(HomeActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.get_cats, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            String android_app_version = jsonObject.getString("version_code");
                            if (!android_app_version.equals("")) {
                                try {
                                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                                    String version_code1 = pInfo.versionName;
//                                    double version_code = Double.valueOf(version_code1);
                                    boolean data_code = (Double.valueOf(android_app_version) > Double.valueOf(version_code1));
                                    // boolean data_code = (1.14 > 1.13);
                                    if (data_code) {
                                        versionDialog();
                                    }
                                } catch (PackageManager.NameNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
//                            ErrorToast("Error"); //Error Toast
                            Toast.makeText(HomeActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
//            final Dialog dialog = new Dialog(HomeActivity.this);
//            dialog.setContentView(R.layout.no_internet_dialog);
//            Button button = dialog.findViewById(R.id.btn_process);
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    getVersionCode();
//                    getBestSelling();
//                    getCategoryList();
//                    getOffers();
//                    getBanners();
//                    getTodayOffers();
//                    getHotProducts();
//                    getNotificationSize();
//                    cartCount();
//                    dialog.dismiss();
//                }
//            });
//            dialog.show();
        }
    }

   /* private void recommendedItems() {

//        dialog.setMessage("Loading...");
//        dialog.setCancelable(false);
//        dialog.show();

        if (APIURLs.isNetwork(HomeActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.RESTAURANT_NEARBY_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
//                            nearByRestaurantModel = new ArrayList<>();
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {

//                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
////                                NearByRestaurantModel model = new NearByRestaurantModel();
//                                model.setId(jsonObject1.getString("id"));
//                                model.setName(jsonObject1.getString("r_name"));
////                                model.setCuisines(jsonObject1.getString("Cuisines"));
//                                model.setRating(jsonObject1.getString("rating"));
//                                model.setDelivery_time(jsonObject1.getString("delivery_time"));
//                                model.setImage(jsonObject1.getString("r_image"));
//
//
//                                model.setCoupondCode(jsonObject1.getString("c_code"));
//                                model.setOffer_amount(jsonObject1.getString("offer_amount"));
//                                model.setDiscount_type(jsonObject1.getString("discount_type"));
//
//                                JSONArray jsonArray1 = jsonObject1.getJSONArray("cuisines1");
//                                if (jsonArray1.length() > 0) {
//                                    cuisinesModels = new ArrayList<>();
//                                    for (int j = 0; j < jsonArray1.length(); j++) {
//
//                                        JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
//                                        CuisinesModel model1 = new CuisinesModel();
//                                        model1.setId(jsonObject2.getString("id"));
//                                        model1.setName(jsonObject2.getString("name"));
//                                        cuisinesModels.add(model1);
////
//                                    }
//                                    model.setArrayList(cuisinesModels);
//                                }
//                                nearByRestaurantModel.add(model);
                            }
////                            rel_loader.setVisibility(View.GONE);
////                            lin_recyc_nearby.setVisibility(View.VISIBLE);



                            recyc_recommended.setAdapter(new RecommendedAdapter(HomeActivity.this));
                            recyc_recommended.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false));

//                            Toast.makeText(HomeActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                        } else if (status.equals("0")) {
//                            lin_recyc_nearby.setVisibility(View.GONE);

                            Toast.makeText(HomeActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
//                        dialog.dismiss();
                    } catch (JSONException e) {
//                        dialog.dismiss();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    dialog.dismiss();

                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
//                    params.put("mobile_no",edt_mobile.getText().toString());
//                    params.put("password",edt_password.getText().toString());
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
//            dialog.dismiss();
            Toast.makeText(this, "no internet connection", Toast.LENGTH_SHORT).show();
        }
    }*/

    public void loadMore(View view) {
        startActivity(new Intent(HomeActivity.this, BestSellingProductActivity.class));
    }

    public void loadMoreOffers(View view) {
        startActivity(new Intent(HomeActivity.this, TodayOffersActivity.class));
    }

    @Override
    protected void onPause() {
//        shimmer_view_container.stopShimmerAnimation();
////        lnr_main.setVisibility(View.VISIBLE);
//        shimmer_view_container.setVisibility(View.GONE);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        shimmer_view_container.startShimmerAnimation();
//        lnr_main.setVisibility(View.GONE);
//        shimmer_view_container.setVisibility(View.VISIBLE);

//        lnr_home_selected.setVisibility(View.VISIBLE);
//        lnr_home.setVisibility(View.GONE);
//
//        lnr_search.setVisibility(View.VISIBLE);
//        lnr_search_selected.setVisibility(View.GONE);
//
//        lnr_cart.setVisibility(View.VISIBLE);
//        lnr_cart_selected.setVisibility(View.GONE);
//
//        lnr_subscription.setVisibility(View.VISIBLE);
//        lnr_subscription_selected.setVisibility(View.GONE);
//
//        lnr_account.setVisibility(View.VISIBLE);
//        lnr_account_selected.setVisibility(View.GONE);
//
//        cartCount();
//        getVersionCode();
////        getrecommendList();
//        getNotificationSize();
//
//
//        if (FunctionConstant.GPSRuntime(HomeActivity.this)) {
//            getCurrentLocation();
//        } else {
//            showSettingsAlert();
//        }
    }

    public void offers(View view) {
        startActivity(new Intent(HomeActivity.this, CouponsActivity.class));
    }

    public void versionDialog() {
        final Dialog dialog1 = new Dialog(HomeActivity.this);
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
                String playstore_link = "https://play.google.com/store/apps/details?id=" +
                        getApplicationContext().getPackageName() +
                        "&referrer=" + Html.fromHtml(SharedPref.getVal(HomeActivity.this, SharedPref.customer_unique_id));
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(playstore_link)));
                APIURLs.cancleDialog = false;
                dialog1.dismiss();
            }
        });
        dialog1.show();
    }

    private void onclick() {
//        rel_home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                lnr_home_selected.setVisibility(View.VISIBLE);
//                lnr_home.setVisibility(View.GONE);
//
//                lnr_search.setVisibility(View.VISIBLE);
//                lnr_search_selected.setVisibility(View.GONE);
//
//                lnr_cart.setVisibility(View.VISIBLE);
//                lnr_cart_selected.setVisibility(View.GONE);
//
//                lnr_subscription.setVisibility(View.VISIBLE);
//                lnr_subscription_selected.setVisibility(View.GONE);
//
//                lnr_account.setVisibility(View.VISIBLE);
//                lnr_account_selected.setVisibility(View.GONE);
//            }
//        });

//        rel_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                lnr_home_selected.setVisibility(View.GONE);
//                lnr_home.setVisibility(View.VISIBLE);
//
//                lnr_search.setVisibility(View.GONE);
//                lnr_search_selected.setVisibility(View.VISIBLE);
//
//                lnr_cart.setVisibility(View.VISIBLE);
//                lnr_cart_selected.setVisibility(View.GONE);
//
//                lnr_subscription.setVisibility(View.VISIBLE);
//                lnr_subscription_selected.setVisibility(View.GONE);
//
//                lnr_account.setVisibility(View.VISIBLE);
//                lnr_account_selected.setVisibility(View.GONE);
//
//                startActivity(new Intent(HomeActivity.this, OffersActivity.class));
////                finish();
//            }
//        });
//
//        rel_cart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                lnr_home_selected.setVisibility(View.GONE);
//                lnr_home.setVisibility(View.VISIBLE);
//
//                lnr_search.setVisibility(View.VISIBLE);
//                lnr_search_selected.setVisibility(View.GONE);
//
//                lnr_cart.setVisibility(View.GONE);
//                lnr_cart_selected.setVisibility(View.VISIBLE);
//
//                lnr_subscription.setVisibility(View.VISIBLE);
//                lnr_subscription_selected.setVisibility(View.GONE);
//
//                lnr_account.setVisibility(View.VISIBLE);
//                lnr_account_selected.setVisibility(View.GONE);
//
//                if (cost_id.equals("")) {
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
//                    // Setting Dialog Title
//                    alertDialog.setTitle("Please Login");
//                    // Setting Dialog Message
//                    alertDialog.setMessage("Please login to view cart");
//                    // On pressing Settings button
//                    alertDialog.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
//                            intent.putExtra("from", "home");
//                            startActivity(intent);
//                        }
//                    });
//                    // on pressing cancel button
//                    alertDialog.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    });
//                    // Showing Alert Message
//                    alertDialog.show();
//                } else {
//                    startActivity(new Intent(HomeActivity.this, CartActivity.class));
//                }
//            }
//        });
//
//        rel_subscription.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                lnr_home_selected.setVisibility(View.GONE);
//                lnr_home.setVisibility(View.VISIBLE);
//
//                lnr_search.setVisibility(View.VISIBLE);
//                lnr_search_selected.setVisibility(View.GONE);
//
//                lnr_cart.setVisibility(View.VISIBLE);
//                lnr_cart_selected.setVisibility(View.GONE);
//
//
//                lnr_subscription.setVisibility(View.GONE);
//                lnr_subscription_selected.setVisibility(View.VISIBLE);
//
//
//                lnr_account.setVisibility(View.VISIBLE);
//                lnr_account_selected.setVisibility(View.GONE);
//
////                startActivity(new Intent(HomeActivity.this, SubscriptionCartActivity.class));
//                if (cost_id.equals("")) {
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
//                    // Setting Dialog Title
//                    alertDialog.setTitle("Please Login");
//                    // Setting Dialog Message
//                    alertDialog.setMessage("Please login to view subscription");
//                    // On pressing Settings button
//                    alertDialog.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
//                            intent.putExtra("from", "home");
//                            startActivity(intent);
//                        }
//                    });
//                    // on pressing cancel button
//                    alertDialog.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    });
//                    // Showing Alert Message
//                    alertDialog.show();
//                } else {
//                    startActivity(new Intent(HomeActivity.this, SubscriptionActivity.class));
//                }
//            }
//        });
//
//        rel_account.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                lnr_home_selected.setVisibility(View.GONE);
//                lnr_home.setVisibility(View.VISIBLE);
//
//                lnr_search.setVisibility(View.VISIBLE);
//                lnr_search_selected.setVisibility(View.GONE);
//
//                lnr_cart.setVisibility(View.VISIBLE);
//                lnr_cart_selected.setVisibility(View.GONE);
//
//                lnr_subscription.setVisibility(View.VISIBLE);
//                lnr_subscription_selected.setVisibility(View.GONE);
//
//                lnr_account.setVisibility(View.GONE);
//                lnr_account_selected.setVisibility(View.VISIBLE);
//
//                startActivity(new Intent(HomeActivity.this, CategoriesActivity.class));
//            }
//        });
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
       /* if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }*/

//        shimmer_view_container.stopShimmerAnimation();
//        lnr_main.setVisibility(View.VISIBLE);
//        shimmer_view_container.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        shimmer_view_container.stopShimmerAnimation();
////        lnr_main.setVisibility(View.VISIBLE);
//        shimmer_view_container.setVisibility(View.GONE);
    }

    public void vegetables(View view) {
        Intent intent = new Intent(HomeActivity.this, SubCategoryListingActivity.class);
        intent.putExtra("category", "Vegetables");
        startActivity(intent);
    }

    public void fruits(View view) {
        Intent intent = new Intent(HomeActivity.this, SubCategoryListingActivity.class);
        intent.putExtra("category", "Fruits");
        startActivity(intent);
    }

    public void beverages(View view) {
        Intent intent = new Intent(HomeActivity.this, SubCategoryListingActivity.class);
        intent.putExtra("category", "Beverages");
        startActivity(intent);
    }

    public void dairyProduct(View view) {
        Intent intent = new Intent(HomeActivity.this, SubCategoryListingActivity.class);
        intent.putExtra("category", "Dairy Product");
        startActivity(intent);
    }

    public void recommend(View view) {
        Intent intent = new Intent(HomeActivity.this, SubCategoryListingActivity.class);
        intent.putExtra("category", "Recommended");
        startActivity(intent);
    }

    private void init() {
        dialog = new IOSDialog.Builder(HomeActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();

//        viewpager = findViewById(R.id.viewpager);
//        lnr_main = findViewById(R.id.lnr_main);
//        shimmer_view_container = findViewById(R.id.shimmer_view_container);
//        lnr_recommend = findViewById(R.id.lnr_recommend);
//        lnr_seller = findViewById(R.id.lnr_seller);
//        lnr_offers = findViewById(R.id.lnr_offers);
//        lnr_hot = findViewById(R.id.lnr_hot);
//        rec_hot = findViewById(R.id.rec_hot);
//        txt_cart_count = findViewById(R.id.txt_cart_count);
//        txt_address = findViewById(R.id.txt_address);
//        txt_timer = findViewById(R.id.txt_timer);
//        txt_todaytitle = findViewById(R.id.txt_todaytitle);
//        txt_address.setSelected(true);
//        rel_cart_count = findViewById(R.id.rel_cart_count);
//        lnr_home = findViewById(R.id.lnr_home);
//        lnr_home_selected = findViewById(R.id.lnr_home_selected);
//        lnr_search = findViewById(R.id.lnr_search);
//        lnr_search_selected = findViewById(R.id.lnr_search_selected);
//        lnr_cart = findViewById(R.id.lnr_cart);
//        lnr_cart_selected = findViewById(R.id.lnr_cart_selected);
//        lnr_subscription = findViewById(R.id.lnr_subscription);
//        lnr_subscription_selected = findViewById(R.id.lnr_subscription_selected);
//        lnr_account = findViewById(R.id.lnr_account);
//        lnr_account_selected = findViewById(R.id.lnr_account_selected);
//        rel_home = findViewById(R.id.rel_home);
//        rel_search = findViewById(R.id.rel_search);
//        rel_cart = findViewById(R.id.rel_cart);
//        rel_subscription = findViewById(R.id.rel_subscription);
//        rel_account = findViewById(R.id.rel_account);
//        recyc_recommended = findViewById(R.id.recyc_recommended);
//        rec_category = findViewById(R.id.rec_category);
//        rec_seller = findViewById(R.id.rec_seller);
//        rec_offer = findViewById(R.id.rec_offer);
//        lnr_todayoffers = findViewById(R.id.lnr_todayoffers);
//        rec_todayoffer = findViewById(R.id.rec_todayoffer);
//        rel_noti_count = findViewById(R.id.rel_noti_count);
//        txt_noti_count = findViewById(R.id.txt_noti_count);
//        rec_category.setLayoutManager(new GridLayoutManager(HomeActivity.this, 2));
//        recyc_recommended.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false));
//        rec_seller.setLayoutManager(new LinearLayoutManager(HomeActivity.this, RecyclerView.HORIZONTAL, false));
//        rec_todayoffer.setLayoutManager(new LinearLayoutManager(HomeActivity.this, RecyclerView.HORIZONTAL, false));
//        rec_offer.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
//        rec_hot.setLayoutManager(new LinearLayoutManager(HomeActivity.this));

    }

//    public void notification(View view) {
//        startActivity(new Intent(HomeActivity.this, NotificationActivity.class));
//    }
//
//    public void search(View view) {
//        startActivity(new Intent(HomeActivity.this, SearchActivity.class));
//    }
//
//    public void account(View view) {
//        startActivity(new Intent(HomeActivity.this, AccountActivity.class));
//    }
//
//    public void showPincodeDialog() {
//        final Dialog dialog1 = new Dialog(this);
//        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog1.setContentView(R.layout.pincode_dialog_layout);
//        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        dialog1.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        dialog1.getWindow().setGravity(Gravity.CENTER);
//        dialog1.getWindow().setDimAmount((float) 0.8);
//        dialog1.setCancelable(false);
//        dialog1.setCanceledOnTouchOutside(false);
//        final EditText edt_pincode = dialog1.findViewById(R.id.edt_pincode);
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
//        dialog1.show();
//    }
//
//    public void cartCount() {
//        if (APIURLs.isNetwork(HomeActivity.this)) {
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.CARTCOUNT_URL, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        String status = jsonObject.getString("status");
//                        if (status.equals("1")) {
//                            JSONArray jsonArray = jsonObject.getJSONArray("result");
//                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
//                            String count = jsonObject1.getString("count");
//                            if (count.equals("0")) {
//                                rel_cart_count.setVisibility(View.GONE);
//                            } else {
//                                rel_cart_count.setVisibility(View.VISIBLE);
//                                txt_cart_count.setText(count);
//                            }
//                        } else {
//                            Toast.makeText(HomeActivity.this, "Error", Toast.LENGTH_SHORT).show();
//                        }
//                        dismissDialog();
//                    } catch (JSONException e) {
//                        dismissDialog();
//                        e.printStackTrace();
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    dismissDialog();
//                    error.printStackTrace();
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    HashMap<String, String> params = new HashMap<>();
//                    params.put("customer_id", SharedPref.getVal(HomeActivity.this, SharedPref.customer_id));
//                    return params;
//                }
//            };
//
//            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            requestQueue.add(stringRequest);
//        } else {
//            dismissDialog();
//
////            final Dialog dialog = new Dialog(HomeActivity.this);
////            dialog.setContentView(R.layout.no_internet_dialog);
////            Button button = dialog.findViewById(R.id.btn_process);
////            button.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    cartCount();
////                    dialog.dismiss();
////                }
////            });
////            dialog.show();
//        }
//    }
//
//    public void checkPincode(final String pincode) {
////        dialog.show();
//        if (APIURLs.isNetwork(HomeActivity.this)) {
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.check_pincode, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        JSONArray jsonArray = jsonObject.getJSONArray("result");
//                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
//                        String status = jsonObject1.getString("status");
//                        if (status.equals("0")) {
//                            new AlertDialog.Builder(HomeActivity.this)
//                                    .setTitle("Service unavailable")
//                                    .setMessage("Currently we are not available in your area.")
//
//                                    // Specifying a listener allows you to take an action before dismissing the dialog.
//                                    // The dialog is automatically dismissed when a dialog button is clicked.
//                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            // Continue with delete operation
//                                            dialog.dismiss();
//                                        }
//                                    })
//                                    .show();
//                        } else {
//                            new AlertDialog.Builder(HomeActivity.this)
//                                    .setTitle("Service available")
//                                    .setMessage("We are available in your area.")
//
//                                    // Specifying a listener allows you to take an action before dismissing the dialog.
//                                    // The dialog is automatically dismissed when a dialog button is clicked.
//                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            // Continue with delete operation
//                                            dialog.dismiss();
//                                        }
//                                    })
//                                    .show();
//                        }
//                        APIURLs.pincodeChecked = true;
//                        dismissDialog();
//                    } catch (JSONException e) {
//                        dismissDialog();
//                        e.printStackTrace();
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    dismissDialog();
//                    error.printStackTrace();
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    HashMap<String, String> params = new HashMap<>();
//                    params.put("pincode", pincode);
//                    if (!cost_id.equals("")) {
//                        params.put("franchise_id", SharedPref.getVal(HomeActivity.this, SharedPref.fran_code));
//                    } else {
//                        params.put("franchise_id", "1");
//                    }
//                    return params;
//                }
//            };
//
//            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            requestQueue.add(stringRequest);
//        } else {
//            dismissDialog();
//            final Dialog dialog = new Dialog(HomeActivity.this);
//            dialog.setContentView(R.layout.no_internet_dialog);
//            Button button = dialog.findViewById(R.id.btn_process);
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    checkPincode(pincode);
//                    dialog.dismiss();
//                }
//            });
//            dialog.show();
//        }
//    }
//
//    public void getBestSelling() {
////        dialog.show();
//        if (APIURLs.isNetwork(HomeActivity.this)) {
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.best_selling_product, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        String status = jsonObject.getString("status");
//                        if (status.equals("1")) {
//                            productModelArrayList = new ArrayList<>();
//                            JSONArray jsonArray = jsonObject.getJSONArray("result");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                                ProductModel model = new ProductModel();
//                                model.setProduct_id(jsonObject1.getString("product_id"));
//                                model.setProduct_name(jsonObject1.getString("product_name"));
//                                model.setImage(jsonObject1.getString("image"));
//                                model.setFlag(jsonObject1.getString("flag"));
//                                model.setQty(jsonObject1.getString("qty"));
//
//                                JSONArray jsonArray1 = jsonObject1.getJSONArray("price_details");
//                                if (jsonArray1.length() > 0) {
//                                    JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
//                                    model.setUnit(jsonObject2.getString("unit"));
//                                    model.setPrice(jsonObject2.getString("price"));
//                                    model.setDiscount(jsonObject2.getString("discount"));
//                                }
//
//                                productModelArrayList.add(model);
//                            }
//                            SellerAdapter adapter = new SellerAdapter();
//                            rec_seller.setAdapter(adapter);
//                            rec_seller.setVisibility(View.VISIBLE);
//                            lnr_seller.setVisibility(View.VISIBLE);
//                        } else {
//                            lnr_seller.setVisibility(View.GONE);
//                        }
//                        dismissDialog();
//                    } catch (JSONException e) {
//                        dismissDialog();
//                        e.printStackTrace();
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    dismissDialog();
//                    error.printStackTrace();
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    HashMap<String, String> params = new HashMap<>();
//                    if (!cost_id.equals("")) {
//                        params.put("franchise_id", SharedPref.getVal(HomeActivity.this, SharedPref.fran_code));
//                    } else {
//                        params.put("franchise_id", "1");
//                    }
//                    params.put("customer_id", SharedPref.getVal(HomeActivity.this, SharedPref.customer_id));
//                    return params;
//                }
//            };
//
//            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            requestQueue.add(stringRequest);
//        } else {
//            dismissDialog();
////            final Dialog dialog = new Dialog(HomeActivity.this);
////            dialog.setContentView(R.layout.no_internet_dialog);
////            Button button = dialog.findViewById(R.id.btn_process);
////            button.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    getBestSelling();
////                    dialog.dismiss();
////                }
////            });
////            dialog.show();
//        }
//    }
//
//    public void getOffers() {
////        dialog.show();
//        if (APIURLs.isNetwork(HomeActivity.this)) {
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.offer_list, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        String status = jsonObject.getString("status");
//                        if (status.equals("1")) {
//                            offerModelArrayList = new ArrayList<>();
//                            JSONArray jsonArray = jsonObject.getJSONArray("result");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                                OfferModel model = new OfferModel();
//                                model.setOffer_id(jsonObject1.getString("offer_id"));
//                                model.setImage(jsonObject1.getString("image"));
//                                offerModelArrayList.add(model);
//                            }
//                            OffersAdapter adapter = new OffersAdapter();
//                            rec_offer.setAdapter(adapter);
//                            rec_offer.setVisibility(View.VISIBLE);
//                            lnr_offers.setVisibility(View.VISIBLE);
//                        } else {
//                            lnr_offers.setVisibility(View.GONE);
//                        }
//                        dismissDialog();
//                    } catch (JSONException e) {
//                        dismissDialog();
//                        e.printStackTrace();
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    dismissDialog();
//                    error.printStackTrace();
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    HashMap<String, String> params = new HashMap<>();
//                    if (!cost_id.equals("")) {
//                        params.put("franchise_id", SharedPref.getVal(HomeActivity.this, SharedPref.fran_code));
//                    } else {
//                        params.put("franchise_id", "1");
//                    }
//                    return params;
//                }
//            };
//
//            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            requestQueue.add(stringRequest);
//        } else {
//            dismissDialog();
////            final Dialog dialog = new Dialog(HomeActivity.this);
////            dialog.setContentView(R.layout.no_internet_dialog);
////            Button button = dialog.findViewById(R.id.btn_process);
////            button.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    getOffers();
////                    dialog.dismiss();
////                }
////            });
////            dialog.show();
//        }
//    }
//
//    public void getHotProducts() {
////        dialog.show();
//        if (APIURLs.isNetwork(HomeActivity.this)) {
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.hot_product_list, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        String status = jsonObject.getString("status");
//                        if (status.equals("1")) {
//                            hotModelArrayList = new ArrayList<>();
//                            JSONArray jsonArray = jsonObject.getJSONArray("result");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                                HotProductModel model = new HotProductModel();
//                                model.setHp_id(jsonObject1.getString("hp_id"));
//                                model.setSub_category_id(jsonObject1.getString("sub_category_id"));
//                                model.setImage(jsonObject1.getString("image"));
//                                hotModelArrayList.add(model);
//                            }
//                            HotAdapter adapter = new HotAdapter();
//                            rec_hot.setAdapter(adapter);
//                            rec_hot.setVisibility(View.VISIBLE);
//                            lnr_hot.setVisibility(View.VISIBLE);
//                        } else {
//                            lnr_hot.setVisibility(View.GONE);
//                        }
//                        dismissDialog();
//                    } catch (JSONException e) {
//                        dismissDialog();
//                        e.printStackTrace();
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    dismissDialog();
//                    error.printStackTrace();
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    HashMap<String, String> params = new HashMap<>();
//                    if (!cost_id.equals("")) {
//                        params.put("franchise_id", SharedPref.getVal(HomeActivity.this, SharedPref.fran_code));
//                    } else {
//                        params.put("franchise_id", "1");
//                    }
//                    return params;
//                }
//            };
//
//            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            requestQueue.add(stringRequest);
//        } else {
//            dismissDialog();
////            final Dialog dialog = new Dialog(HomeActivity.this);
////            dialog.setContentView(R.layout.no_internet_dialog);
////            Button button = dialog.findViewById(R.id.btn_process);
////            button.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    getHotProducts();
////                    dialog.dismiss();
////                }
////            });
////            dialog.show();
//        }
//    }
//
//    public void getTodayOffers() {
////        dialog.show();
//        if (APIURLs.isNetwork(HomeActivity.this)) {
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.today_offer, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        String status = jsonObject.getString("status");
//                        if (status.equals("1")) {
//                            JSONArray jsonArray = jsonObject.getJSONArray("result");
//                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
//
//                            final String start_time = jsonObject1.getString("start_time");
//                            final String end_time = jsonObject1.getString("end_time");
//                            Thread myThread = null;
//                            Runnable myRunnableThread = new CountDownRunner(end_time);
//                            myThread = new Thread(myRunnableThread);
//                            myThread.start();
//
//                            productModelArrayList = new ArrayList<>();
//                            JSONArray jsonArray1 = jsonObject1.getJSONArray("product_list");
//                            for (int i = 0; i < jsonArray1.length(); i++) {
//                                JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
//                                ProductModel model = new ProductModel();
//                                model.setProduct_id(jsonObject2.getString("product_id"));
//                                model.setProduct_name(jsonObject2.getString("product_name"));
//                                model.setImage(jsonObject2.getString("image"));
//                                model.setFlag(jsonObject2.getString("flag"));
//                                model.setQty(jsonObject2.getString("qty"));
//
//                                JSONArray jsonArray2 = jsonObject2.getJSONArray("price_details");
//                                if (jsonArray2.length() > 0) {
//                                    JSONObject jsonObject3 = jsonArray2.getJSONObject(0);
//                                    model.setUnit(jsonObject3.getString("unit"));
//                                    model.setPrice(jsonObject3.getString("price"));
//                                    model.setDiscount(jsonObject3.getString("discount"));
//                                }
//
//                                todayoffersModelArrayList.add(model);
//                            }
//                            TodayOffersAdapter adapter = new TodayOffersAdapter();
//                            rec_todayoffer.setAdapter(adapter);
//                            rec_todayoffer.setVisibility(View.VISIBLE);
//                            lnr_todayoffers.setVisibility(View.VISIBLE);
//                        } else {
//                            lnr_todayoffers.setVisibility(View.GONE);
//                        }
//                        dismissDialog();
//                    } catch (JSONException e) {
//                        dismissDialog();
//                        e.printStackTrace();
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    dismissDialog();
//                    error.printStackTrace();
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    HashMap<String, String> params = new HashMap<>();
//                    params.put("customer_id", cost_id);
//                    return params;
//                }
//            };
//
//            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            requestQueue.add(stringRequest);
//        } else {
//            dismissDialog();
////            final Dialog dialog = new Dialog(HomeActivity.this);
////            dialog.setContentView(R.layout.no_internet_dialog);
////            Button button = dialog.findViewById(R.id.btn_process);
////            button.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    getTodayOffers();
////                    dialog.dismiss();
////                }
////            });
////            dialog.show();
//        }
//    }
//
//    public void getNotificationSize() {
//        if (APIURLs.isNetwork(HomeActivity.this)) {
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.notification_list, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        String status = jsonObject.getString("status");
//                        if (status.equals("1")) {
//                            notificationModelArrayList = new ArrayList<>();
//                            JSONArray jsonArray = jsonObject.getJSONArray("result");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                                NotificationModel model = new NotificationModel();
//                                model.setNotification_id(jsonObject1.getString("notification_id"));
//                                model.setTitle(jsonObject1.getString("title"));
//                                model.setImage(jsonObject1.getString("image"));
//                                model.setMessage(jsonObject1.getString("message"));
//                                notificationModelArrayList.add(model);
//                            }
//                        } else {
//
//                        }
//                        int size = notificationModelArrayList.size();
//                        String old_count = databaseHelper.getOldCount();
//                        if(old_count.equals("")){
//                            old_count = "0";
//                        }
//                        int displaySize = size - Integer.parseInt(old_count);
//                        if(displaySize>0){
//                            rel_noti_count.setVisibility(View.VISIBLE);
//                            txt_noti_count.setText(String.valueOf(displaySize));
//                        }else{
//                            rel_noti_count.setVisibility(View.GONE);
//                        }
//                        dismissDialog();
//                    } catch (JSONException e) {
//                        dismissDialog();
//                        e.printStackTrace();
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    dismissDialog();
//                    error.printStackTrace();
//                }
//            });
////            {
////                @Override
////                protected Map<String, String> getParams() throws AuthFailureError {
////                    HashMap<String, String> params = new HashMap<>();
////                    if (!cost_id.equals("")) {
////                        params.put("franchise_id", SharedPref.getVal(NotificationActivity.this, SharedPref.fran_code));
////                    } else {
////                        params.put("franchise_id", "1");
////                    }
////                    return params;
////                }
////            };
//
//            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            requestQueue.add(stringRequest);
//        } else {
//            dismissDialog();
////            final Dialog dialog = new Dialog(HomeActivity.this);
////            dialog.setContentView(R.layout.no_internet_dialog);
////            Button button = dialog.findViewById(R.id.btn_process);
////            button.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    getNotificationSize();
////                    dialog.dismiss();
////                }
////            });
////            dialog.show();
//        }
//    }
//
//    public void doWork(final String endtime) {
//        runOnUiThread(new Runnable() {
//            public void run() {
//                try {
//                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss aa");
//
//                    Date systemDate = Calendar.getInstance().getTime();
//                    String myDate = sdf.format(systemDate);
////                  txtCurrentTime.setText(myDate);
//
//                    Date Date1 = sdf.parse(myDate);
//                    Date Date2 = sdf.parse(endtime);
//
//                    long millse = Date1.getTime() - Date2.getTime();
//                    long mills = Math.abs(millse);
//
//                    int Hours = (int) (mills / (1000 * 60 * 60));
//                    int Mins = (int) (mills / (1000 * 60)) % 60;
//                    long Secs = (int) (mills / 1000) % 60;
//
//                    String diff = Hours + ":" + Mins + ":" + Secs; // updated value every1 second
//                    txt_timer.setText(diff + " Left");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    class CountDownRunner implements Runnable {
//        String endTime;
//
//        public CountDownRunner(String endTime) {
//            this.endTime = endTime;
//        }
//
//        // @Override
//        public void run() {
//            while (!Thread.currentThread().isInterrupted()) {
//                try {
//                    doWork(endTime);
//                    Thread.sleep(1000); // Pause of 1 Second
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                } catch (Exception e) {
//                }
//            }
//        }
//    }
//
//    public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ItemViewholder> {
//        Context context;
//
//        public CategoryAdapter(Context context) {
//            this.context = context;
//        }
//
//        @NonNull
//        @Override
//        public ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_layout, parent, false);
//            ItemViewholder itemViewholder = new ItemViewholder(view);
//            return itemViewholder;
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull ItemViewholder holder, int position) {
//            CategoryModel model = categoryModelArrayList.get(position);
//            ((ItemViewholder) holder).bind(model, position);
//
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return categoryModelArrayList.size();
//        }
//
//        public class ItemViewholder extends RecyclerView.ViewHolder {
//            ImageView img_category;
//            TextView txt_categoryName;
//            CardView card_item;
//            LinearLayout lnr_item;
//
//            public ItemViewholder(@NonNull View itemView) {
//                super(itemView);
//                img_category = itemView.findViewById(R.id.img_category);
//                txt_categoryName = itemView.findViewById(R.id.txt_categoryName);
//                card_item = itemView.findViewById(R.id.card_item);
//                lnr_item = itemView.findViewById(R.id.lnr_item);
//            }
//
//            public void bind(final CategoryModel model, int position) {
//                txt_categoryName.setText(model.getCategory_name());
//
//                Glide.with(HomeActivity.this)
//                        .load(model.getImage())
//                        .error(R.drawable.loader).
//                        placeholder(R.drawable.loader)
//                        .into(img_category);
//
//                if(!model.getColor().equals("")){
//                    card_item.setCardBackgroundColor(Color.parseColor(model.getColor()));
////                    card_item.setRadius(5);
//                }
//
//                card_item.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (model.getSubcategorey().equals("1")) {
//                            Intent intent = new Intent(HomeActivity.this, SubCategoryListingActivity.class);
//                            intent.putExtra("cat_id", model.getCategory_id());
//                            intent.putExtra("cat_name", model.getCategory_name());
//                            startActivity(intent);
//                        } else {
//                            Intent intent = new Intent(HomeActivity.this, CategoryWiseProductListActivity.class);
//                            intent.putExtra("sub_cat_id", model.getCategory_id());
//                            intent.putExtra("sub_cat_name", model.getCategory_name());
//                            intent.putExtra("from", "cat");
//                            startActivity(intent);
//                        }
//                    }
//                });
//            }
//        }
//    }
//
//    public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.ItemViewholder> {
//        Context context;
//
//        public RecommendedAdapter(Context context) {
//            this.context = context;
//        }
//
//        @NonNull
//        @Override
//        public ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_home_item_layout, parent, false);
//            ItemViewholder itemViewholder = new ItemViewholder(view);
//            return itemViewholder;
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull ItemViewholder holder, int position) {
//            ProductModel model = productModelArrayList.get(position);
//            ((ItemViewholder) holder).bind(model, position);
//
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return productModelArrayList.size();
//        }
//
//        public class ItemViewholder extends RecyclerView.ViewHolder {
//            TextView txt_productName, txt_weight, txt_price;
//            ImageView img_product;
//            RelativeLayout lnr_details;
//
//            public ItemViewholder(@NonNull View itemView) {
//                super(itemView);
//
//                txt_productName = itemView.findViewById(R.id.txt_productName);
//                txt_weight = itemView.findViewById(R.id.txt_weight);
//                txt_price = itemView.findViewById(R.id.txt_price);
//                img_product = itemView.findViewById(R.id.img_product);
//                lnr_details = itemView.findViewById(R.id.lnr_details);
//            }
//
//            public void bind(final ProductModel model, int position) {
//                Picasso.with(context).load(model.getImage()).into(img_product);
//                txt_weight.setText(model.getUnit());
//                txt_price.setText("Rs." + model.getPrice());
//                String productName = model.getProduct_name();
//                if (productName.length() > 10) {
//                    productName = productName.substring(0, 10);
//                    txt_productName.setText(productName + "...");
//                } else {
//                    txt_productName.setText(productName);
//                }
//
//                lnr_details.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
//                        intent.putExtra("prod_id", model.getProduct_id());
//                        startActivity(intent);
//                    }
//                });
//            }
//        }
//
//    }
//
//    public class SellerAdapter extends RecyclerView.Adapter<SellerAdapter.ViewHolder> {
//        Double actualAmout;
//        Double discount;
//        Double payableAmt;
//        int size;
//
//        @NonNull
//        @Override
//        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.best_seller_item_layout, parent, false);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//            final ProductModel model = productModelArrayList.get(position);
//            ((ViewHolder) holder).bind(model, position);
//            if (position == productModelArrayList.size()) {
//                /*holder.rel_viewMore.setVisibility(View.VISIBLE);
//                holder.lnr_details.setVisibility(View.GONE);*/
//                if (position == 5) {
//                    holder.rel_viewMore.setVisibility(View.VISIBLE);
//                    holder.lnr_details.setVisibility(View.GONE);
//                }
//            } else {
//                if (position == 5) {
//                    holder.rel_viewMore.setVisibility(View.VISIBLE);
//                    holder.lnr_details.setVisibility(View.GONE);
//                }
//            }
//
//            holder.rel_viewMore.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(HomeActivity.this, BestSellingProductActivity.class);
////                    intent.putExtra("prod_id", model.getProduct_id());
//                    startActivity(intent);
//                }
//            });
//          /*  if(size>6){
//                if(position == 6){
//                    holder.rel_viewMore.setVisibility(View.VISIBLE);
//                    holder.lnr_details.setVisibility(View.GONE);
//                }
//            }else{
//                if(position == productModelArrayList.size()) {
//                    holder.rel_viewMore.setVisibility(View.VISIBLE);
//                    holder.lnr_details.setVisibility(View.GONE);
//                }
//            }*/
//        }
//
//        @Override
//        public int getItemCount() {
//            size = productModelArrayList.size();
//            if (size > 6) {
//                return 6;
//            } else {
//                return productModelArrayList.size();
//            }
//        }
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//            TextView txt_productName, txt_weight, txt_discountprice, txt_actualprice, txt_discount;
//            ImageView img_product;
//            LinearLayout lnr_details;
//            RelativeLayout rel_discount, rel_viewMore;
//
//            public ViewHolder(@NonNull View itemView) {
//                super(itemView);
//
//                txt_productName = itemView.findViewById(R.id.txt_productName);
//                txt_weight = itemView.findViewById(R.id.txt_weight);
//                txt_discountprice = itemView.findViewById(R.id.txt_discountprice);
//                txt_actualprice = itemView.findViewById(R.id.txt_actualprice);
//                img_product = itemView.findViewById(R.id.img_product);
//                lnr_details = itemView.findViewById(R.id.lnr_details);
//                rel_discount = itemView.findViewById(R.id.rel_discount);
//                rel_viewMore = itemView.findViewById(R.id.rel_viewMore);
//                txt_discount = itemView.findViewById(R.id.txt_discount);
//                txt_actualprice.setPaintFlags(txt_actualprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//            }
//
//            public void bind(final ProductModel model, final int position) {
//                Picasso.with(HomeActivity.this).load(model.getImage())
//                        .error(R.drawable.loader)
//                        .placeholder(R.drawable.loader)
//                        .into(img_product);
//                txt_weight.setText(model.getUnit());
//                try {
//                    if (model.getDiscount().equals("0")) {
//                        txt_discountprice.setText("Rs." + model.getPrice());
//                        txt_actualprice.setVisibility(View.GONE);
//                        rel_discount.setVisibility(View.GONE);
//                    } else {
//                        txt_actualprice.setVisibility(View.VISIBLE);
//                        rel_discount.setVisibility(View.VISIBLE);
//                        actualAmout = Double.parseDouble(model.getPrice());
//                       /* discount = (actualAmout * Double.parseDouble(model.getDiscount())) / 100;
//                        payableAmt = actualAmout - discount;
//                        DecimalFormat twoDForm = new DecimalFormat("#");
//                        payableAmt = Double.valueOf(twoDForm.format(payableAmt));
//
//                        txt_discountprice.setText("Rs." + (int) Math.round(payableAmt));
//                        txt_actualprice.setText("Rs." + model.getPrice());
//                        txt_discount.setText(model.getDiscount() + "% off");*/
//
//                        payableAmt = actualAmout - Double.parseDouble(model.getDiscount());
//
//                        txt_discountprice.setText("Rs." + (int) Math.round(payableAmt));
//                        txt_actualprice.setText("Rs." + model.getPrice());
//                        txt_discount.setText("Save\n\u20B9" + model.getDiscount());
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                String productName = model.getProduct_name();
//                if (productName.length() > 13) {
//                    productName = productName.substring(0, 13);
//                    txt_productName.setText(productName + "...");
//                } else {
//                    txt_productName.setText(productName);
//                }
//
//                lnr_details.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                       /* if (position == productModelArrayList.size() - 1) {
//                            holder.rel_viewMore.setVisibility(View.VISIBLE);
//                            holder.lnr_details.setVisibility(View.GONE);
//                        } else {
//                            if (position == 5) {
//                                holder.rel_viewMore.setVisibility(View.VISIBLE);
//                                holder.lnr_details.setVisibility(View.GONE);
//                            }
//                        }*/
//                        Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
//                        intent.putExtra("prod_id", model.getProduct_id());
//                        startActivity(intent);
//                    }
//                });
//            }
//        }
//    }
//
//    public class TodayOffersAdapter extends RecyclerView.Adapter<TodayOffersAdapter.ViewHolder> {
//        Double actualAmout;
//        Double discount;
//        Double payableAmt;
//        int size;
//
//        @NonNull
//        @Override
//        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.best_seller_item_layout, parent, false);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//            final ProductModel model = todayoffersModelArrayList.get(position);
//            ((ViewHolder) holder).bind(model, position);
//            if (position == todayoffersModelArrayList.size()) {
//                /*holder.rel_viewMore.setVisibility(View.VISIBLE);
//                holder.lnr_details.setVisibility(View.GONE);*/
//                if (position == 5) {
//                    holder.rel_viewMore.setVisibility(View.VISIBLE);
//                    holder.lnr_details.setVisibility(View.GONE);
//                }
//            } else {
//                if (position == 5) {
//                    holder.rel_viewMore.setVisibility(View.VISIBLE);
//                    holder.lnr_details.setVisibility(View.GONE);
//                }
//            }
//
//            holder.rel_viewMore.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(HomeActivity.this, TodayOffersActivity.class);
////                    intent.putExtra("prod_id", model.getProduct_id());
//                    startActivity(intent);
//                }
//            });
//          /*  if(size>6){
//                if(position == 6){
//                    holder.rel_viewMore.setVisibility(View.VISIBLE);
//                    holder.lnr_details.setVisibility(View.GONE);
//                }
//            }else{
//                if(position == productModelArrayList.size()) {
//                    holder.rel_viewMore.setVisibility(View.VISIBLE);
//                    holder.lnr_details.setVisibility(View.GONE);
//                }
//            }*/
//        }
//
//        @Override
//        public int getItemCount() {
//            size = todayoffersModelArrayList.size();
//            if (size > 6) {
//                return 6;
//            } else {
//                return todayoffersModelArrayList.size();
//            }
//        }
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//            TextView txt_productName, txt_weight, txt_discountprice, txt_actualprice, txt_discount;
//            ImageView img_product;
//            LinearLayout lnr_details;
//            RelativeLayout rel_discount, rel_viewMore;
//
//            public ViewHolder(@NonNull View itemView) {
//                super(itemView);
//
//                txt_productName = itemView.findViewById(R.id.txt_productName);
//                txt_weight = itemView.findViewById(R.id.txt_weight);
//                txt_discountprice = itemView.findViewById(R.id.txt_discountprice);
//                txt_actualprice = itemView.findViewById(R.id.txt_actualprice);
//                img_product = itemView.findViewById(R.id.img_product);
//                lnr_details = itemView.findViewById(R.id.lnr_details);
//                rel_discount = itemView.findViewById(R.id.rel_discount);
//                rel_viewMore = itemView.findViewById(R.id.rel_viewMore);
//                txt_discount = itemView.findViewById(R.id.txt_discount);
//                txt_actualprice.setPaintFlags(txt_actualprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//            }
//
//            public void bind(final ProductModel model, final int position) {
//                Picasso.with(HomeActivity.this).load(model.getImage())
//                        .error(R.drawable.loader)
//                        .placeholder(R.drawable.loader)
//                        .into(img_product);
//                txt_weight.setText(model.getUnit());
//                try {
//                    if (model.getDiscount().equals("0")) {
//                        txt_discountprice.setText("Rs." + model.getPrice());
//                        txt_actualprice.setVisibility(View.GONE);
//                        rel_discount.setVisibility(View.GONE);
//                    } else {
//                        txt_actualprice.setVisibility(View.VISIBLE);
//                        rel_discount.setVisibility(View.VISIBLE);
//                        actualAmout = Double.parseDouble(model.getPrice());
//                       /* discount = (actualAmout * Double.parseDouble(model.getDiscount())) / 100;
//                        payableAmt = actualAmout - discount;
//                        DecimalFormat twoDForm = new DecimalFormat("#");
//                        payableAmt = Double.valueOf(twoDForm.format(payableAmt));
//
//                        txt_discountprice.setText("Rs." + (int) Math.round(payableAmt));
//                        txt_actualprice.setText("Rs." + model.getPrice());
//                        txt_discount.setText(model.getDiscount() + "% off");*/
//
//                        payableAmt = actualAmout - Double.parseDouble(model.getDiscount());
//
//                        txt_discountprice.setText("Rs." + (int) Math.round(payableAmt));
//                        txt_actualprice.setText("Rs." + model.getPrice());
//                        txt_discount.setText("Save\n\u20B9" + model.getDiscount());
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                String productName = model.getProduct_name();
//                if (productName.length() > 13) {
//                    productName = productName.substring(0, 13);
//                    txt_productName.setText(productName + "...");
//                } else {
//                    txt_productName.setText(productName);
//                }
//
//                lnr_details.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                       /* if (position == productModelArrayList.size() - 1) {
//                            holder.rel_viewMore.setVisibility(View.VISIBLE);
//                            holder.lnr_details.setVisibility(View.GONE);
//                        } else {
//                            if (position == 5) {
//                                holder.rel_viewMore.setVisibility(View.VISIBLE);
//                                holder.lnr_details.setVisibility(View.GONE);
//                            }
//                        }*/
//                        Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
//                        intent.putExtra("prod_id", model.getProduct_id());
//                        startActivity(intent);
//                    }
//                });
//            }
//        }
//    }
//
//    public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.ViewHolder> {
//
//        @NonNull
//        @Override
//        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.offer_list_item_layout, parent, false);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//            final OfferModel model = offerModelArrayList.get(position);
//            ((ViewHolder) holder).bind(model, position);
//        }
//
//        @Override
//        public int getItemCount() {
//            return offerModelArrayList.size();
//        }
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//            ImageView img_offer;
//            RelativeLayout rel_offers;
//
//            public ViewHolder(@NonNull View itemView) {
//                super(itemView);
//
//                img_offer = itemView.findViewById(R.id.img_offer);
//                rel_offers = itemView.findViewById(R.id.rel_offers);
//            }
//
//            public void bind(final OfferModel model, final int position) {
//                Picasso.with(HomeActivity.this).load(model.getImage())
//                        .error(R.drawable.loader)
//                        .placeholder(R.drawable.loader)
//                        .into(img_offer);
//
//                rel_offers.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(HomeActivity.this, OffersProductListActivity.class);
//                        intent.putExtra("offer_id", model.getOffer_id());
//                        startActivity(intent);
//                    }
//                });
//            }
//        }
//    }
//
//    public class HotAdapter extends RecyclerView.Adapter<HotAdapter.ViewHolder> {
//
//        @NonNull
//        @Override
//        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.offer_list_item_layout, parent, false);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//            final HotProductModel model = hotModelArrayList.get(position);
//            ((ViewHolder) holder).bind(model, position);
//        }
//
//        @Override
//        public int getItemCount() {
//            return hotModelArrayList.size();
//        }
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//            ImageView img_offer;
//            RelativeLayout rel_offers;
//
//            public ViewHolder(@NonNull View itemView) {
//                super(itemView);
//
//                img_offer = itemView.findViewById(R.id.img_offer);
//                rel_offers = itemView.findViewById(R.id.rel_offers);
//            }
//
//            public void bind(final HotProductModel model, final int position) {
//                Picasso.with(HomeActivity.this).load(model.getImage())
//                        .error(R.drawable.loader)
//                        .placeholder(R.drawable.loader)
//                        .into(img_offer);
//
//                rel_offers.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(HomeActivity.this, HotProductListActivity.class);
//                        intent.putExtra("sub_cat_id", model.getSub_category_id());
//                        startActivity(intent);
//                    }
//                });
//            }
//        }
//    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        SharedPref.putBol(HomeActivity.this, SharedPref.appExit, true);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
                SharedPref.putBol(HomeActivity.this, SharedPref.appExit, false);
                SharedPref.putBol(HomeActivity.this, SharedPref.pincodeChecked, false);
            }
        }, 2000);
    }
}
