package com.sterlex.in.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.sterlex.in.Model.CategoryModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.gmail.samehadar.iosdialog.IOSDialog;
import com.sterlex.in.Constant.APIURLs;
import com.sterlex.in.Constant.FunctionConstant;
import com.sterlex.in.Constant.SharedPref;
import com.sterlex.in.Model.ProductModel;
import com.sterlex.in.Model.QuntityModel;
import com.sterlex.in.Model.UnitModel;
import com.sterlex.in.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;

public class CategoryWiseProductListActivity extends AppCompatActivity {

    ImageView img_back, img_close, img_filter;
    TextView txt_categoryName, txt_fromdate, txt_todate, txt_nodata, txt_cart_count, txt;
    RecyclerView rec_list, rec_subcategory;
    CardView card_bottom, card_mySubscription;
    LinearLayout lin_nodata, lin_day, lin_one, lin_two, lin_three, lin_four, lin_s, lin_s_sel, lin_m,
            lin_m_sel, lin_t, lin_t_sel, lin_w, lin_w_sel, lin_th, lin_th_sel, lin_f, lin_f_sel, lin_sat, lin_sat_sel;
    LinearLayout lnr_home, lnr_home_selected, lnr_search, lnr_search_selected,
            lnr_cart, lnr_cart_selected, lnr_subscription, lnr_subscription_selected, lnr_account, lnr_account_selected;
    LinearLayout lnr_selectDays;
    RelativeLayout rel_home, rel_search, rel_cart, rel_cart_main, rel_subscription, rel_account;
    String category = "";
    String strFromDate = "", strToDate = "";
    private int mYear, mMonth, mDay;
    RelativeLayout rel_from, rel_to;
    RelativeLayout rel_cart_count,rel_cart_no_items;
    String sub_cat_id = "", sub_cat_name = "", from = "";
    IOSDialog dialog;
    int qty = 0, subsQty = 0;
    ArrayList<ProductModel> productModelArrayList = new ArrayList<>();
    ArrayList<UnitModel> product_unitArraylist = new ArrayList<>();
    ArrayList<CategoryModel> categoryModelArrayList = new ArrayList<>();
    String subscription_type = "", weeklyDays = "", customizeDays = "";
    ArrayList<String> productId = new ArrayList<>();
    ArrayList<String> daysArrayList = new ArrayList<>();
    ArrayList<QuntityModel> qtyArrayList = new ArrayList<>();
    ArrayList<String> unitPriceArrayList = new ArrayList<>();
    ArrayList<String> unitArrayList = new ArrayList<>();
    ArrayList<String> discountArrayList = new ArrayList<>();
    Button btn_submit;
    String customizeDay = "", prod_Qty = "null", strProductId = "", strQty = "", strUnitPrice = "", strUnit = "", strDiscount = "";
    QuntityModel qtymodel;
    ShimmerFrameLayout shimmer_view_container;
    LinearLayout lnr_main;
    ProductsListingAdapter adapter;
    SearchView searchView;
    Filter filter;
    String cost_id = "";
    RecyclerView rec_subsubcategory;
    Dialog dialog1;
    boolean cart_click = false;
    String cart_unit_price = "", cart_unit = "", cart_discount = "";
    TextView txt_cart_no_of_items, cart_txt_amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_wise_product_list);
        init();
        cost_id = SharedPref.getVal(CategoryWiseProductListActivity.this, SharedPref.customer_id);
        onClick();
        sub_cat_id = getIntent().getStringExtra("sub_cat_id");
        sub_cat_name = getIntent().getStringExtra("sub_cat_name");
        from = getIntent().getStringExtra("from");
        txt_categoryName.setText(sub_cat_name);
        if (from.equals("cat")) {
            getProductList();
        } else {
            getCategoryList();

        }
    }

    private void init() {
        dialog = new IOSDialog.Builder(CategoryWiseProductListActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();

        lin_nodata = findViewById(R.id.lin_nodata);
        rel_cart_main = findViewById(R.id.rel_cart_main);
        rel_cart_no_items = findViewById(R.id.rel_cart_no_items);
        txt_cart_no_of_items = findViewById(R.id.txt_cart_no_of_items);
        cart_txt_amount = findViewById(R.id.cart_txt_amount);

        img_back = findViewById(R.id.img_back);
        shimmer_view_container = findViewById(R.id.shimmer_view_container);
        img_close = findViewById(R.id.img_close);
        txt_categoryName = findViewById(R.id.txt);
        txt_nodata = findViewById(R.id.txt_nodata);
        lin_day = findViewById(R.id.lin_day);
        lin_one = findViewById(R.id.lin_one);
        lin_two = findViewById(R.id.lin_two);
        lin_three = findViewById(R.id.lin_three);
        lin_four = findViewById(R.id.lin_four);
        btn_submit = findViewById(R.id.btn_submit);

        txt_cart_count = findViewById(R.id.txt_cart_count);
        rel_cart_count = findViewById(R.id.rel_cart_count);
        searchView = findViewById(R.id.searchView);

        lnr_selectDays = findViewById(R.id.lnr_selectDays);
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

        lin_s = findViewById(R.id.lin_s);
        lin_s_sel = findViewById(R.id.lin_s_sel);
        lin_m = findViewById(R.id.lin_m);
        lin_m_sel = findViewById(R.id.lin_m_sel);
        lin_t = findViewById(R.id.lin_t);
        lin_t_sel = findViewById(R.id.lin_t_sel);
        lin_w = findViewById(R.id.lin_w);
        lin_w_sel = findViewById(R.id.lin_w_sel);
        lin_th = findViewById(R.id.lin_th);
        lin_th_sel = findViewById(R.id.lin_th_sel);
        lin_f = findViewById(R.id.lin_f);
        lin_f_sel = findViewById(R.id.lin_f_sel);
        lin_sat = findViewById(R.id.lin_sat);
        lin_sat_sel = findViewById(R.id.lin_sat_sel);

        rec_list = findViewById(R.id.rec_list);
        lnr_main = findViewById(R.id.lnr_main);
        img_filter = findViewById(R.id.img_filter);
        rec_subcategory = findViewById(R.id.rec_subcategory);

        txt_fromdate = findViewById(R.id.txt_fromdate);
        txt_todate = findViewById(R.id.txt_todate);
        rel_from = findViewById(R.id.rel_from);
        rel_to = findViewById(R.id.rel_to);

        card_bottom = findViewById(R.id.card_bottom);
        card_mySubscription = findViewById(R.id.card_mySubscription);
        rec_list.setLayoutManager(new LinearLayoutManager(CategoryWiseProductListActivity.this));
        rec_subcategory.setLayoutManager(new LinearLayoutManager(CategoryWiseProductListActivity.this, RecyclerView.HORIZONTAL, false));
    }

    public void showFilterDialog() {
        dialog1 = new Dialog(CategoryWiseProductListActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.sub_sub_category_dialog_layout);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog1.getWindow().setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog1.getWindow().setGravity(Gravity.CENTER);
        ImageView img_close = dialog1.findViewById(R.id.img_close);
        rec_subsubcategory = dialog1.findViewById(R.id.rec_subsubcategory);
        rec_subsubcategory.setLayoutManager(new LinearLayoutManager(CategoryWiseProductListActivity.this));
        SubCategoryAdapter subadapter = new SubCategoryAdapter(CategoryWiseProductListActivity.this);
        rec_subsubcategory.setAdapter(subadapter);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

        dialog1.show();
    }


    public void goToHome(View view) {

        startActivity(new Intent(CategoryWiseProductListActivity.this, HomeActivity2.class));
        finish();

    }
    private void onClick() {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    filter.filter(newText);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        rel_cart_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cost_id.equals("")) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(CategoryWiseProductListActivity.this);
                    // Setting Dialog Title
                    alertDialog.setTitle("Please Login");
                    // Setting Dialog Message
                    alertDialog.setMessage("Please login to view cart");
                    // On pressing Settings button
                    alertDialog.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(CategoryWiseProductListActivity.this, LoginActivity.class);
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
                    startActivity(new Intent(CategoryWiseProductListActivity.this, CartActivity2.class));

                }
            }
        });
        img_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSubCategoryList();
                Intent intent = new Intent(CategoryWiseProductListActivity.this, SubSubCategoryListActivity.class);
                intent.putExtra("cat_id", sub_cat_id);
                startActivity(intent);
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_mySubscription.setVisibility(GONE);
                lin_day.setVisibility(View.VISIBLE);
                customizeDay = "";
                weeklyDays = "";
                daysArrayList = new ArrayList<>();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (daysArrayList.size() > 0) {
                    /*Comma Separated Days*/
                    for (int i = 0; i < daysArrayList.size(); i++) {
                        customizeDay += daysArrayList.get(i) + ",";
                    }
                    customizeDay = customizeDay.substring(0, customizeDay.length() - 1);
                }
                if (productId.size() > 0) {
                    /*Comma Separated Product Id*/
                    for (int i = 0; i < productId.size(); i++) {
                        strProductId += productId.get(i) + ",";
                    }
                    strProductId = strProductId.substring(0, strProductId.length() - 1);
                }
                if (qtyArrayList.size() > 0) {
                    /*Comma Separated Qty*/
                    for (int i = 0; i < qtyArrayList.size(); i++) {
                        strQty += qtyArrayList.get(i).getQty() + ",";
                    }
                    strQty = strQty.substring(0, strQty.length() - 1);
                }
                if (unitPriceArrayList.size() > 0) {
                    /*Comma Separated Unit Price*/
                    for (int i = 0; i < unitPriceArrayList.size(); i++) {
                        strUnitPrice += unitPriceArrayList.get(i) + ",";
                    }
                    strUnitPrice = strUnitPrice.substring(0, strUnitPrice.length() - 1);
                }
                if (unitArrayList.size() > 0) {
                    /*Comma Separated Unit*/
                    for (int i = 0; i < unitArrayList.size(); i++) {
                        strUnit += unitArrayList.get(i) + ",";
                    }
                    strUnit = strUnit.substring(0, strUnit.length() - 1);
                }
                if (discountArrayList.size() > 0) {
                    /*Comma Separated Unit*/
                    for (int i = 0; i < discountArrayList.size(); i++) {
                        strDiscount += discountArrayList.get(i) + ",";
                    }
                    strDiscount = strDiscount.substring(0, strDiscount.length() - 1);
                }

              /*  if(subscription_type.equals("customize")){
                    subscription_type = "Custom";
                }else if(subscription_type.equals("weekly")){
                    subscription_type = "Weekly";
                }else if(subscription_type.equals("daily")){
                    subscription_type = "Daily";
                }*/

                if (strFromDate.equals("")) {
                    Toast.makeText(CategoryWiseProductListActivity.this, "Please select from date", Toast.LENGTH_SHORT).show();
                } else if (strToDate.equals("")) {
                    Toast.makeText(CategoryWiseProductListActivity.this, "Please select to date", Toast.LENGTH_SHORT).show();
                } else {
                    if (subscription_type.equals("Weekly")) {
                        if (weeklyDays.equals("")) {
                            Toast.makeText(CategoryWiseProductListActivity.this, "Please select day", Toast.LENGTH_SHORT).show();
                        } else {
//                            subscribed();
                            Intent intent = new Intent(CategoryWiseProductListActivity.this, SubscriptionCartActivity.class);
                            intent.putExtra("from_date", strFromDate);
                            intent.putExtra("to_date", strToDate);
                            intent.putExtra("subscribe_mode", subscription_type);
                            intent.putExtra("product_id", strProductId);
                            intent.putExtra("qty", strQty);
                            intent.putExtra("day", weeklyDays);
                            intent.putExtra("unit_price", strUnitPrice);
                            intent.putExtra("unit", strUnit);
                            intent.putExtra("discount", strDiscount);
                            startActivity(intent);
                            finish();
                        }
                    } else if (subscription_type.equals("Custom")) {
                        if (customizeDay.equals("")) {
                            Toast.makeText(CategoryWiseProductListActivity.this, "Please select days", Toast.LENGTH_SHORT).show();
                        } else {
//                            subscribed();
                            Intent intent = new Intent(CategoryWiseProductListActivity.this, SubscriptionCartActivity.class);
                            intent.putExtra("from_date", strFromDate);
                            intent.putExtra("to_date", strToDate);
                            intent.putExtra("subscribe_mode", subscription_type);
                            intent.putExtra("product_id", strProductId);
                            intent.putExtra("qty", strQty);
                            intent.putExtra("day", customizeDay);
                            intent.putExtra("unit_price", strUnitPrice);
                            intent.putExtra("unit", strUnit);
                            intent.putExtra("discount", strDiscount);
                            startActivity(intent);
                            finish();
                        }

                    } else {
//                        subscribed();
                        Intent intent = new Intent(CategoryWiseProductListActivity.this, SubscriptionCartActivity.class);
                        intent.putExtra("from_date", strFromDate);
                        intent.putExtra("to_date", strToDate);
                        intent.putExtra("subscribe_mode", subscription_type);
                        intent.putExtra("product_id", strProductId);
                        intent.putExtra("qty", strQty);
                        intent.putExtra("day", "");
                        intent.putExtra("unit_price", strUnitPrice);
                        intent.putExtra("unit", strUnit);
                        intent.putExtra("discount", strDiscount);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });


        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
//
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        lin_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*card_bottom.setVisibility(View.GONE);*/
                card_mySubscription.setVisibility(View.VISIBLE);
                lin_day.setVisibility(GONE);
                subscription_type = "Weekly";
                lnr_selectDays.setVisibility(View.VISIBLE);

                lin_s.setVisibility(View.VISIBLE);
                lin_s_sel.setVisibility(GONE);

                lin_m.setVisibility(View.VISIBLE);
                lin_m_sel.setVisibility(GONE);

                lin_t.setVisibility(View.VISIBLE);
                lin_t_sel.setVisibility(GONE);

                lin_w.setVisibility(View.VISIBLE);
                lin_w_sel.setVisibility(GONE);

                lin_th.setVisibility(View.VISIBLE);
                lin_th_sel.setVisibility(GONE);

                lin_f.setVisibility(View.VISIBLE);
                lin_f_sel.setVisibility(GONE);

                lin_sat.setVisibility(View.VISIBLE);
                lin_sat_sel.setVisibility(GONE);

                weeklyDays = "";
                daysArrayList = new ArrayList<>();
            }
        });

        lin_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*card_bottom.setVisibility(View.GONE);*/
                card_mySubscription.setVisibility(View.VISIBLE);
                lin_day.setVisibility(GONE);
                subscription_type = "Daily";
                lnr_selectDays.setVisibility(GONE);


            }
        });
        lin_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*card_bottom.setVisibility(View.GONE);*/
                card_mySubscription.setVisibility(View.VISIBLE);

                lin_day.setVisibility(GONE);
                subscription_type = "Custom";
                lnr_selectDays.setVisibility(View.VISIBLE);


                lin_s.setVisibility(View.VISIBLE);
                lin_s_sel.setVisibility(GONE);

                lin_m.setVisibility(View.VISIBLE);
                lin_m_sel.setVisibility(GONE);

                lin_t.setVisibility(View.VISIBLE);
                lin_t_sel.setVisibility(GONE);

                lin_w.setVisibility(View.VISIBLE);
                lin_w_sel.setVisibility(GONE);

                lin_th.setVisibility(View.VISIBLE);
                lin_th_sel.setVisibility(GONE);

                lin_f.setVisibility(View.VISIBLE);
                lin_f_sel.setVisibility(GONE);

                lin_sat.setVisibility(View.VISIBLE);
                lin_sat_sel.setVisibility(GONE);

                weeklyDays = "";
                daysArrayList = new ArrayList<>();
            }
        });
        lin_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*card_bottom.setVisibility(View.GONE);*/
                card_mySubscription.setVisibility(GONE);
                lin_day.setVisibility(GONE);


                lin_s.setVisibility(View.VISIBLE);
                lin_s_sel.setVisibility(GONE);

                lin_m.setVisibility(View.VISIBLE);
                lin_m_sel.setVisibility(GONE);

                lin_t.setVisibility(View.VISIBLE);
                lin_t_sel.setVisibility(GONE);

                lin_w.setVisibility(View.VISIBLE);
                lin_w_sel.setVisibility(GONE);

                lin_th.setVisibility(View.VISIBLE);
                lin_th_sel.setVisibility(GONE);

                lin_f.setVisibility(View.VISIBLE);
                lin_f_sel.setVisibility(GONE);

                lin_sat.setVisibility(View.VISIBLE);
                lin_sat_sel.setVisibility(GONE);

                weeklyDays = "";
                customizeDay = "";
                daysArrayList = new ArrayList<>();
//                getProductList();
            }
        });

        rel_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(CategoryWiseProductListActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int year1 = view.getYear();
                        int month = view.getMonth();
                        int day = view.getDayOfMonth();

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year1, month, day);

                        try {
                            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                            strFromDate = format.format(calendar.getTime());
                            txt_fromdate.setText(strFromDate);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            }
        });

        rel_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(CategoryWiseProductListActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int year1 = view.getYear();
                        int month = view.getMonth();
                        int day = view.getDayOfMonth();

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year1, month, day);

                        try {
                            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                            strToDate = format.format(calendar.getTime());
                            txt_todate.setText(strToDate);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            }
        });

        lin_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subscription_type.equals("Weekly")) {
                    lin_s.setVisibility(GONE);
                    lin_s_sel.setVisibility(View.VISIBLE);

                    lin_m.setVisibility(View.VISIBLE);
                    lin_m_sel.setVisibility(GONE);

                    lin_t.setVisibility(View.VISIBLE);
                    lin_t_sel.setVisibility(GONE);

                    lin_w.setVisibility(View.VISIBLE);
                    lin_w_sel.setVisibility(GONE);

                    lin_th.setVisibility(View.VISIBLE);
                    lin_th_sel.setVisibility(GONE);

                    lin_f.setVisibility(View.VISIBLE);
                    lin_f_sel.setVisibility(GONE);

                    lin_sat.setVisibility(View.VISIBLE);
                    lin_sat_sel.setVisibility(GONE);

                    weeklyDays = "Sunday";

                } else {
                    lin_s.setVisibility(GONE);
                    lin_s_sel.setVisibility(View.VISIBLE);
                    daysArrayList.add("Sunday");
                }

            }
        });
        lin_s_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_s.setVisibility(View.VISIBLE);
                lin_s_sel.setVisibility(GONE);

                for (int i = 0; i < daysArrayList.size(); i++) {
                    if (daysArrayList.contains("Sunday")) {
                        daysArrayList.remove(i);
                    }
                }
            }
        });

        lin_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (subscription_type.equals("Weekly")) {
                    lin_s.setVisibility(View.VISIBLE);
                    lin_s_sel.setVisibility(GONE);

                    lin_m.setVisibility(GONE);
                    lin_m_sel.setVisibility(View.VISIBLE);

                    lin_t.setVisibility(View.VISIBLE);
                    lin_t_sel.setVisibility(GONE);

                    lin_w.setVisibility(View.VISIBLE);
                    lin_w_sel.setVisibility(GONE);

                    lin_th.setVisibility(View.VISIBLE);
                    lin_th_sel.setVisibility(GONE);

                    lin_f.setVisibility(View.VISIBLE);
                    lin_f_sel.setVisibility(GONE);

                    lin_sat.setVisibility(View.VISIBLE);
                    lin_sat_sel.setVisibility(GONE);

                    weeklyDays = "Monday";

                } else {
                    lin_m.setVisibility(GONE);
                    lin_m_sel.setVisibility(View.VISIBLE);
                    daysArrayList.add("Monday");
                }
            }
        });
        lin_m_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_m.setVisibility(View.VISIBLE);
                lin_m_sel.setVisibility(GONE);
                for (int i = 0; i < daysArrayList.size(); i++) {
                    if (daysArrayList.contains("Monday")) {
                        daysArrayList.remove(i);
                    }
                }
            }
        });


        lin_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subscription_type.equals("Weekly")) {

                    lin_s.setVisibility(View.VISIBLE);
                    lin_s_sel.setVisibility(GONE);

                    lin_m.setVisibility(View.VISIBLE);
                    lin_m_sel.setVisibility(GONE);

                    lin_t.setVisibility(GONE);
                    lin_t_sel.setVisibility(View.VISIBLE);

                    lin_w.setVisibility(View.VISIBLE);
                    lin_w_sel.setVisibility(GONE);

                    lin_th.setVisibility(View.VISIBLE);
                    lin_th_sel.setVisibility(GONE);

                    lin_f.setVisibility(View.VISIBLE);
                    lin_f_sel.setVisibility(GONE);

                    lin_sat.setVisibility(View.VISIBLE);
                    lin_sat_sel.setVisibility(GONE);

                    weeklyDays = "Tuesday";

                } else {
                    lin_t.setVisibility(GONE);
                    lin_t_sel.setVisibility(View.VISIBLE);

                    daysArrayList.add("Tuesday");
                }

            }
        });
        lin_t_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_t.setVisibility(View.VISIBLE);
                lin_t_sel.setVisibility(GONE);
                for (int i = 0; i < daysArrayList.size(); i++) {
                    if (daysArrayList.contains("Tuesday")) {
                        daysArrayList.remove(i);
                    }
                }
            }
        });


        lin_w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subscription_type.equals("Weekly")) {

                    lin_s.setVisibility(View.VISIBLE);
                    lin_s_sel.setVisibility(GONE);

                    lin_m.setVisibility(View.VISIBLE);
                    lin_m_sel.setVisibility(GONE);

                    lin_t.setVisibility(View.VISIBLE);
                    lin_t_sel.setVisibility(GONE);

                    lin_w.setVisibility(GONE);
                    lin_w_sel.setVisibility(View.VISIBLE);

                    lin_th.setVisibility(View.VISIBLE);
                    lin_th_sel.setVisibility(GONE);

                    lin_f.setVisibility(View.VISIBLE);
                    lin_f_sel.setVisibility(GONE);

                    lin_sat.setVisibility(View.VISIBLE);
                    lin_sat_sel.setVisibility(GONE);

                    weeklyDays = "Wednesday";

                } else {
                    lin_w.setVisibility(GONE);
                    lin_w_sel.setVisibility(View.VISIBLE);

                    daysArrayList.add("Wednesday");
                }

            }
        });
        lin_w_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_w.setVisibility(View.VISIBLE);
                lin_w_sel.setVisibility(GONE);
                for (int i = 0; i < daysArrayList.size(); i++) {
                    if (daysArrayList.contains("Wednesday")) {
                        daysArrayList.remove(i);
                    }
                }
            }
        });


        lin_th.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (subscription_type.equals("Weekly")) {

                    lin_s.setVisibility(View.VISIBLE);
                    lin_s_sel.setVisibility(GONE);

                    lin_m.setVisibility(View.VISIBLE);
                    lin_m_sel.setVisibility(GONE);

                    lin_t.setVisibility(View.VISIBLE);
                    lin_t_sel.setVisibility(GONE);

                    lin_w.setVisibility(View.VISIBLE);
                    lin_w_sel.setVisibility(GONE);

                    lin_th.setVisibility(GONE);
                    lin_th_sel.setVisibility(View.VISIBLE);

                    lin_f.setVisibility(View.VISIBLE);
                    lin_f_sel.setVisibility(GONE);

                    lin_sat.setVisibility(View.VISIBLE);
                    lin_sat_sel.setVisibility(GONE);

                    weeklyDays = "Thursday";

                } else {
                    lin_th.setVisibility(GONE);
                    lin_th_sel.setVisibility(View.VISIBLE);

                    daysArrayList.add("Thursday");
                }
            }
        });
        lin_th_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_th.setVisibility(View.VISIBLE);
                lin_th_sel.setVisibility(GONE);
                for (int i = 0; i < daysArrayList.size(); i++) {
                    if (daysArrayList.contains("Thursday")) {
                        daysArrayList.remove(i);
                    }
                }
            }
        });


        lin_f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subscription_type.equals("Weekly")) {

                    lin_s.setVisibility(View.VISIBLE);
                    lin_s_sel.setVisibility(GONE);

                    lin_m.setVisibility(View.VISIBLE);
                    lin_m_sel.setVisibility(GONE);

                    lin_t.setVisibility(View.VISIBLE);
                    lin_t_sel.setVisibility(GONE);

                    lin_w.setVisibility(View.VISIBLE);
                    lin_w_sel.setVisibility(GONE);

                    lin_th.setVisibility(View.VISIBLE);
                    lin_th_sel.setVisibility(GONE);

                    lin_f.setVisibility(GONE);
                    lin_f_sel.setVisibility(View.VISIBLE);

                    lin_sat.setVisibility(View.VISIBLE);
                    lin_sat_sel.setVisibility(GONE);

                    weeklyDays = "Friday";

                } else {
                    lin_f.setVisibility(GONE);
                    lin_f_sel.setVisibility(View.VISIBLE);

                    daysArrayList.add("Friday");
                }
            }
        });
        lin_f_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_f.setVisibility(View.VISIBLE);
                lin_f_sel.setVisibility(GONE);
                for (int i = 0; i < daysArrayList.size(); i++) {
                    if (daysArrayList.contains("Friday")) {
                        daysArrayList.remove(i);
                    }
                }
            }
        });


        lin_sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subscription_type.equals("Weekly")) {

                    lin_s.setVisibility(View.VISIBLE);
                    lin_s_sel.setVisibility(GONE);

                    lin_m.setVisibility(View.VISIBLE);
                    lin_m_sel.setVisibility(GONE);

                    lin_t.setVisibility(View.VISIBLE);
                    lin_t_sel.setVisibility(GONE);

                    lin_w.setVisibility(View.VISIBLE);
                    lin_w_sel.setVisibility(GONE);

                    lin_th.setVisibility(View.VISIBLE);
                    lin_th_sel.setVisibility(GONE);

                    lin_f.setVisibility(View.VISIBLE);
                    lin_f_sel.setVisibility(GONE);

                    lin_sat.setVisibility(GONE);
                    lin_sat_sel.setVisibility(View.VISIBLE);

                    weeklyDays = "Saturday";

                } else {
                    lin_sat.setVisibility(GONE);
                    lin_sat_sel.setVisibility(View.VISIBLE);

                    daysArrayList.add("Saturday");
                }
            }
        });
        lin_sat_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_sat.setVisibility(View.VISIBLE);
                lin_sat_sel.setVisibility(GONE);
                for (int i = 0; i < daysArrayList.size(); i++) {
                    if (daysArrayList.contains("Saturday")) {
                        daysArrayList.remove(i);
                    }
                }
            }
        });

        if (card_bottom.getVisibility() == View.VISIBLE) {
            rec_list.setClickable(false);
        }
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rel_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_home_selected.setVisibility(View.VISIBLE);
                lnr_home.setVisibility(GONE);

                lnr_search.setVisibility(View.VISIBLE);
                lnr_search_selected.setVisibility(GONE);

                lnr_cart.setVisibility(View.VISIBLE);
                lnr_cart_selected.setVisibility(GONE);

                lnr_subscription.setVisibility(View.VISIBLE);
                lnr_subscription_selected.setVisibility(GONE);

                lnr_account.setVisibility(View.VISIBLE);
                lnr_account_selected.setVisibility(GONE);
                startActivity(new Intent(CategoryWiseProductListActivity.this, HomeActivity.class));

            }
        });

        rel_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_home_selected.setVisibility(GONE);
                lnr_home.setVisibility(View.VISIBLE);

                lnr_search.setVisibility(GONE);
                lnr_search_selected.setVisibility(View.VISIBLE);

                lnr_cart.setVisibility(View.VISIBLE);
                lnr_cart_selected.setVisibility(GONE);

                lnr_subscription.setVisibility(View.VISIBLE);
                lnr_subscription_selected.setVisibility(GONE);

                lnr_account.setVisibility(View.VISIBLE);
                lnr_account_selected.setVisibility(GONE);

                startActivity(new Intent(CategoryWiseProductListActivity.this, OffersActivity.class));
//                finish();
            }
        });

        rel_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_home_selected.setVisibility(GONE);
                lnr_home.setVisibility(View.VISIBLE);

                lnr_search.setVisibility(View.VISIBLE);
                lnr_search_selected.setVisibility(GONE);

                lnr_cart.setVisibility(GONE);
                lnr_cart_selected.setVisibility(View.VISIBLE);

                lnr_subscription.setVisibility(View.VISIBLE);
                lnr_subscription_selected.setVisibility(GONE);

                lnr_account.setVisibility(View.VISIBLE);
                lnr_account_selected.setVisibility(GONE);

                if (cost_id.equals("")) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(CategoryWiseProductListActivity.this);
                    // Setting Dialog Title
                    alertDialog.setTitle("Please Login");
                    // Setting Dialog Message
                    alertDialog.setMessage("Please login to view cart");
                    // On pressing Settings button
                    alertDialog.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(CategoryWiseProductListActivity.this, LoginActivity.class);
                            intent.putExtra("from", "cat_product");
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
                    startActivity(new Intent(CategoryWiseProductListActivity.this, CartActivity.class));
                }
            }
        });

        rel_subscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_home_selected.setVisibility(GONE);
                lnr_home.setVisibility(View.VISIBLE);

                lnr_search.setVisibility(View.VISIBLE);
                lnr_search_selected.setVisibility(GONE);

                lnr_cart.setVisibility(View.VISIBLE);
                lnr_cart_selected.setVisibility(GONE);


                lnr_subscription.setVisibility(GONE);
                lnr_subscription_selected.setVisibility(View.VISIBLE);


                lnr_account.setVisibility(View.VISIBLE);
                lnr_account_selected.setVisibility(GONE);

                if (cost_id.equals("")) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(CategoryWiseProductListActivity.this);
                    // Setting Dialog Title
                    alertDialog.setTitle("Please Login");
                    // Setting Dialog Message
                    alertDialog.setMessage("Please login to view subscription");
                    // On pressing Settings button
                    alertDialog.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(CategoryWiseProductListActivity.this, LoginActivity.class);
                            intent.putExtra("from", "cat_product");
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
                    startActivity(new Intent(CategoryWiseProductListActivity.this, SubscriptionActivity.class));
                }
            }
        });

        rel_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_home_selected.setVisibility(GONE);
                lnr_home.setVisibility(View.VISIBLE);

                lnr_search.setVisibility(View.VISIBLE);
                lnr_search_selected.setVisibility(GONE);

                lnr_cart.setVisibility(View.VISIBLE);
                lnr_cart_selected.setVisibility(GONE);

                lnr_subscription.setVisibility(View.VISIBLE);
                lnr_subscription_selected.setVisibility(GONE);

                lnr_account.setVisibility(GONE);
                lnr_account_selected.setVisibility(View.VISIBLE);

                startActivity(new Intent(CategoryWiseProductListActivity.this, CategoriesActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        lnr_home_selected.setVisibility(View.VISIBLE);
        lnr_home.setVisibility(GONE);

        lnr_search.setVisibility(View.VISIBLE);
        lnr_search_selected.setVisibility(GONE);

        lnr_cart.setVisibility(View.VISIBLE);
        lnr_cart_selected.setVisibility(GONE);

        lnr_subscription.setVisibility(View.VISIBLE);
        lnr_subscription_selected.setVisibility(GONE);

        lnr_account.setVisibility(View.VISIBLE);
        lnr_account_selected.setVisibility(GONE);

        shimmer_view_container.startShimmerAnimation();
        lnr_main.setVisibility(GONE);
        shimmer_view_container.setVisibility(View.VISIBLE);
        cartCount();
        /*if (from.equals("cat")) {
            getProductList();
        } else {
            getCategoryList();

        }*/
//        getProductList();
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
      /*  if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }*/
        shimmer_view_container.stopShimmerAnimation();
        lnr_main.setVisibility(View.VISIBLE);
        shimmer_view_container.setVisibility(GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }*/
        shimmer_view_container.stopShimmerAnimation();
        lnr_main.setVisibility(View.VISIBLE);
        shimmer_view_container.setVisibility(GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        shimmer_view_container.stopShimmerAnimation();
        lnr_main.setVisibility(View.VISIBLE);
        shimmer_view_container.setVisibility(GONE);
    }

    public void addSubscription(View view) {
        card_mySubscription.setVisibility(GONE);
        lin_day.setVisibility(GONE);
        startActivity(new Intent(CategoryWiseProductListActivity.this, SubscriptionActivity.class));
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
                startActivity(new Intent(CategoryWiseProductListActivity.this, LoginActivity.class));
                dialog1.dismiss();
            }
        });

        lnr_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryWiseProductListActivity.this, OtpActivity.class));
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
                startActivity(new Intent(CategoryWiseProductListActivity.this, AccountActivity.class));
                dialog1.dismiss();
            }
        });
        lnr_my_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryWiseProductListActivity.this, MyOrderActivity.class));
                dialog1.dismiss();
            }
        });
        lnr_my_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(CategoryWiseProductListActivity.this, SaveforLaterActivity.class));
                dialog1.dismiss();
            }
        });
        lnr_reorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(CategoryWiseProductListActivity.this, SubscriptionActivity.class));
                dialog1.dismiss();
            }
        });
        lnr_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new androidx.appcompat.app.AlertDialog.Builder(CategoryWiseProductListActivity.this)
                        .setTitle("Really Logout?")
                        .setMessage("Are you sure you want to logout?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                SharedPref.clearData(CategoryWiseProductListActivity.this);
                                startActivity(new Intent(CategoryWiseProductListActivity.this, LoginActivity.class));
                                finishAffinity();
                            }
                        }).create().show();
                dialog1.dismiss();
            }
        });


        dialog1.show();
    }


    public void getCategoryList() {
//        dialog.show();
        shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);

        if (APIURLs.isNetwork(CategoryWiseProductListActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.sub_sub_category_list, new Response.Listener<String>() {
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
                                model.setCategory_id(jsonObject1.getString("sub_subcategory_id"));
                                model.setCategory_name(jsonObject1.getString("sub_subcategory"));
                                model.setImage(jsonObject1.getString("image"));
                                model.setColor(jsonObject1.getString("color"));
                                categoryModelArrayList.add(model);
                            }
                            CategoryAdapter adapter = new CategoryAdapter(CategoryWiseProductListActivity.this);
                            rec_subcategory.setAdapter(adapter);
                            rec_subcategory.setVisibility(View.VISIBLE);
                            lin_nodata.setVisibility(GONE);
//                            img_filter.setVisibility(View.VISIBLE);
                        } else {
                            /*rec_subcategory.setVisibility(View.GONE);
                            lin_nodata.setVisibility(View.VISIBLE);*/
                            img_filter.setVisibility(GONE);
                            if (from.equals("cat")) {
                                getProductList();
                            } else if (from.equals("sub_cat")) {
                                 getsubProductList();
                            }
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
                    params.put("sub_category_id", sub_cat_id);

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            final Dialog dialog = new Dialog(CategoryWiseProductListActivity.this);
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

    public void getSubCategoryList() {
//        dialog.show();
        shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        rec_list.setVisibility(GONE);

        if (APIURLs.isNetwork(CategoryWiseProductListActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.sub_sub_category_list, new Response.Listener<String>() {
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
                                model.setCategory_id(jsonObject1.getString("sub_subcategory_id"));
                                model.setCategory_name(jsonObject1.getString("sub_subcategory"));
                                model.setImage(jsonObject1.getString("image"));
                                categoryModelArrayList.add(model);
                            }
                            showFilterDialog();

                        } else {

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
                    params.put("sub_category_id", sub_cat_id);

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            final Dialog dialog = new Dialog(CategoryWiseProductListActivity.this);
            dialog.setContentView(R.layout.no_internet_dialog);
            Button button = dialog.findViewById(R.id.btn_process);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSubCategoryList();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public void getProductList() {
//        dialog.show();
        shimmer_view_container.startShimmerAnimation();
        rec_list.setVisibility(GONE);
        shimmer_view_container.setVisibility(View.VISIBLE);
        lin_nodata.setVisibility(GONE);

        if (APIURLs.isNetwork(CategoryWiseProductListActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.CATEGORY_WISE_PRODUCT_LIST, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            productModelArrayList = new ArrayList<>();
                          /*  productId = new ArrayList<>();
                            unitArrayList = new ArrayList<>();
                            unitArrayList = new ArrayList<>();
                            qtyArrayList = new ArrayList<>();
                            discountArrayList = new ArrayList<>();*/
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                ProductModel model = new ProductModel();
                                model.setProduct_id(jsonObject1.getString("product_id"));
                                model.setProduct_name(jsonObject1.getString("product_name"));
                                model.setImage(jsonObject1.getString("image"));
                                model.setFlag(jsonObject1.getString("flag"));
                                model.setQty(jsonObject1.getString("product_qty"));
                                model.setType(jsonObject1.getString("type"));
                                model.setIs_veg(jsonObject1.getString("is_veg"));

                                JSONArray jsonArray1 = jsonObject1.getJSONArray("product_unit");
                                JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
                                model.setUnit(jsonObject2.getString("unit"));
                                model.setPrice(jsonObject2.getString("uint_price"));
                                model.setDiscount(jsonObject2.getString("unit_discount"));
                                model.setGst(jsonObject2.getString("unit_gst"));

                                product_unitArraylist = new ArrayList<>();

//                                            JSONObject jsonObject4 = jsonObject2.getJSONObject("product_unit");
                                JSONArray productArray = jsonObject1.getJSONArray("product_unit");
                                if (productArray.length() > 0) {
                                    for (int k = 0; k < productArray.length(); k++) {
                                        JSONObject imagesObject = productArray.getJSONObject(k);
                                        UnitModel unitModel = new UnitModel();
                                        unitModel.setUnit(imagesObject.getString("unit"));
                                        unitModel.setUint_price(imagesObject.getString("uint_price"));
                                        unitModel.setUnit_discount(imagesObject.getString("unit_discount"));
//                                                    unitModel.setUnit_purchase_price(imagesObject.getString("unit_purchase_price"));
                                        unitModel.setUnit_gst(imagesObject.getString("unit_gst"));
                                        product_unitArraylist.add(unitModel);
                                    }
                                    model.setProduct_unitArraylist(product_unitArraylist);
                                }
                                productModelArrayList.add(model);
                            }
                            adapter = new ProductsListingAdapter(CategoryWiseProductListActivity.this);
                            rec_list.setAdapter(adapter);
//                            filter = adapter.getFilter();
//                            adapter.setHasStableIds(true);
//                            rec_list.setItemViewCacheSize(productModelArrayList.size()-1);
//                            rec_list.setHasFixedSize(true);
                            rec_list.setVisibility(View.VISIBLE);
                            lin_nodata.setVisibility(GONE);
                        } else {
                            rec_list.setVisibility(GONE);
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
                    params.put("category_id", sub_cat_id);
                    params.put("customer_id", SharedPref.getVal(CategoryWiseProductListActivity.this, SharedPref.customer_id));

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            final Dialog dialog = new Dialog(CategoryWiseProductListActivity.this);
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

    public void getsubProductList() {
//        dialog.show();
        shimmer_view_container.startShimmerAnimation();
        rec_list.setVisibility(GONE);
        shimmer_view_container.setVisibility(View.VISIBLE);
        lin_nodata.setVisibility(GONE);

        if (APIURLs.isNetwork(CategoryWiseProductListActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.sub_sub_category_list, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            productModelArrayList = new ArrayList<>();
                          /*  productId = new ArrayList<>();
                            unitArrayList = new ArrayList<>();
                            unitArrayList = new ArrayList<>();
                            qtyArrayList = new ArrayList<>();
                            discountArrayList = new ArrayList<>();*/
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                ProductModel model = new ProductModel();
                                model.setProduct_id(jsonObject1.getString("product_id"));
                                model.setProduct_name(jsonObject1.getString("product_name"));
                                model.setImage(jsonObject1.getString("image"));
                                model.setFlag(jsonObject1.getString("flag"));
                                model.setQty(jsonObject1.getString("qty"));
                                model.setType(jsonObject1.getString("type"));
                                model.setIs_veg(jsonObject1.getString("is_veg"));

                                JSONArray jsonArray1 = jsonObject1.getJSONArray("price_details");
                                JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
                                model.setUnit(jsonObject2.getString("unit"));
                                model.setPrice(jsonObject2.getString("price"));
                                model.setDiscount(jsonObject2.getString("discount"));
                                product_unitArraylist = new ArrayList<>();

//                                            JSONObject jsonObject4 = jsonObject2.getJSONObject("product_unit");
                                JSONArray productArray = jsonObject1.getJSONArray("product_unit");
                                if (productArray.length() > 0) {
                                    for (int k = 0; k < productArray.length(); k++) {
                                        JSONObject imagesObject = productArray.getJSONObject(k);
                                        UnitModel unitModel = new UnitModel();
                                        unitModel.setUnit(imagesObject.getString("unit"));
                                        unitModel.setUint_price(imagesObject.getString("price"));
                                        unitModel.setUnit_discount(imagesObject.getString("discount"));
//                                                    unitModel.setUnit_purchase_price(imagesObject.getString("unit_purchase_price"));
//                                        unitModel.setUnit_gst(imagesObject.getString("unit_gst"));
                                        product_unitArraylist.add(unitModel);
                                    }
                                    model.setProduct_unitArraylist(product_unitArraylist);
                                }
                                productModelArrayList.add(model);
                            }
                            adapter = new ProductsListingAdapter(CategoryWiseProductListActivity.this);
                            rec_list.setAdapter(adapter);
//                            filter = adapter.getFilter();
//                            adapter.setHasStableIds(true);
//                            rec_list.setItemViewCacheSize(productModelArrayList.size()-1);
//                            rec_list.setHasFixedSize(true);
                            rec_list.setVisibility(View.VISIBLE);
                            lin_nodata.setVisibility(GONE);
                        } else {
                            rec_list.setVisibility(GONE);
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
                    params.put("sub_category_id", sub_cat_id);
                    params.put("customer_id", SharedPref.getVal(CategoryWiseProductListActivity.this, SharedPref.customer_id));

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            final Dialog dialog = new Dialog(CategoryWiseProductListActivity.this);
            dialog.setContentView(R.layout.no_internet_dialog);
            Button button = dialog.findViewById(R.id.btn_process);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getsubProductList();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public void getsubsubProductList(final String sub_cat_id) {
//        dialog.show();
        shimmer_view_container.startShimmerAnimation();
        rec_list.setVisibility(GONE);
        shimmer_view_container.setVisibility(View.VISIBLE);
        lin_nodata.setVisibility(GONE);

        if (APIURLs.isNetwork(CategoryWiseProductListActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.sub_subcategory_wise_product_list, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            productModelArrayList = new ArrayList<>();
                          /*  productId = new ArrayList<>();
                            unitArrayList = new ArrayList<>();
                            unitArrayList = new ArrayList<>();
                            qtyArrayList = new ArrayList<>();
                            discountArrayList = new ArrayList<>();*/
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                ProductModel model = new ProductModel();
                                model.setProduct_id(jsonObject1.getString("product_id"));
                                model.setProduct_name(jsonObject1.getString("product_name"));
                                model.setImage(jsonObject1.getString("image"));
                                model.setFlag(jsonObject1.getString("flag"));
                                model.setQty(jsonObject1.getString("product_qty"));
                                model.setType(jsonObject1.getString("type"));
                                model.setIs_veg(jsonObject1.getString("is_veg"));



                                JSONArray jsonArray1 = jsonObject1.getJSONArray("product_unit");
                                JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
                                model.setUnit(jsonObject2.getString("unit"));
                                model.setPrice(jsonObject2.getString("uint_price"));
                                model.setDiscount(jsonObject2.getString("unit_discount"));
                                model.setGst(jsonObject2.getString("unit_gst"));

                                product_unitArraylist = new ArrayList<>();

//                                            JSONObject jsonObject4 = jsonObject2.getJSONObject("product_unit");
                                JSONArray productArray = jsonObject1.getJSONArray("product_unit");
                                if (productArray.length() > 0) {
                                    for (int k = 0; k < productArray.length(); k++) {
                                        JSONObject imagesObject = productArray.getJSONObject(k);
                                        UnitModel unitModel = new UnitModel();
                                        unitModel.setUnit(imagesObject.getString("unit"));
                                        unitModel.setUint_price(imagesObject.getString("uint_price"));
                                        unitModel.setUnit_discount(imagesObject.getString("unit_discount"));
//                                                    unitModel.setUnit_purchase_price(imagesObject.getString("unit_purchase_price"));
                                        unitModel.setUnit_gst(imagesObject.getString("unit_gst"));
                                        product_unitArraylist.add(unitModel);
                                    }
                                    model.setProduct_unitArraylist(product_unitArraylist);
                                }
                                productModelArrayList.add(model);
                            }
                            adapter = new ProductsListingAdapter(CategoryWiseProductListActivity.this);
                            rec_list.setAdapter(adapter);
//                            filter = adapter.getFilter();
//                            adapter.setHasStableIds(true);
//                            rec_list.setItemViewCacheSize(productModelArrayList.size()-1);
//                            rec_list.setHasFixedSize(true);
                            rec_list.setVisibility(View.VISIBLE);
                            lin_nodata.setVisibility(GONE);
                        } else {
                            rec_list.setVisibility(GONE);
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
                    params.put("sub_subcategory_id", sub_cat_id);
                    params.put("customer_id", SharedPref.getVal(CategoryWiseProductListActivity.this, SharedPref.customer_id));

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            final Dialog dialog = new Dialog(CategoryWiseProductListActivity.this);
            dialog.setContentView(R.layout.no_internet_dialog);
            Button button = dialog.findViewById(R.id.btn_process);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getsubsubProductList(sub_cat_id);
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ItemViewholder> {
        Context context;
        int selectedPos = 0;

        public CategoryAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_sub_category_item_layout, parent, false);
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

            CardView card_item;
//            ImageView img_category;
            TextView txt_name;
            View view;
            LinearLayout lnr_category;

            public ItemViewholder(@NonNull View itemView) {
                super(itemView);

                card_item = itemView.findViewById(R.id.card_item);
//                img_category = itemView.findViewById(R.id.img_category);
                txt_name = itemView.findViewById(R.id.txt_categoryName);
                view = itemView.findViewById(R.id.view);
                lnr_category = itemView.findViewById(R.id.lnr_category);
            }

            public void bind(final CategoryModel model, final int position) {
                txt_name.setText(model.getCategory_name());
//                if(!model.getColor().equals("")){
//                    card_item.setCardBackgroundColor(Color.parseColor(model.getColor()));
//                }
                if (selectedPos == position) {
                    view.setVisibility(View.VISIBLE);
                    getsubsubProductList(model.getCategory_id());
                } else {
                    view.setVisibility(GONE);
                }

                lnr_category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedPos = position;
                        notifyDataSetChanged();
                    }
                });
            }
        }
    }

    public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ItemViewholder> {
        Context context;
        int selectedPos = 0;

        public SubCategoryAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_sub_category_dialog_item_layout, parent, false);
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
            TextView txt_categoryName;
            ImageView img_category;
            LinearLayout lnr_category;

            public ItemViewholder(@NonNull View itemView) {
                super(itemView);

                txt_categoryName = itemView.findViewById(R.id.txt_categoryName);
                img_category = itemView.findViewById(R.id.img_category);
                lnr_category = itemView.findViewById(R.id.lnr_category);
            }

            public void bind(final CategoryModel model, final int position) {
                Picasso.with(CategoryWiseProductListActivity.this).load(model.getImage())
                        .error(R.drawable.loader).placeholder(R.drawable.loader).into(img_category);
                txt_categoryName.setText(model.getCategory_name());

                lnr_category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                        getsubsubProductList(model.getCategory_id());
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
            ProductModel model = productModelArrayList.get(position);
            ((ProductsListingAdapter.ItemViewholder) holder).bind(model, position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return productModelArrayList.size();
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
            RelativeLayout rel_qty, rel_stock, rel_bottomcart, rel_discount, rel_cart, rel_minus, rel_plus, rel_subscribe, rel_subscribeminus, rel_subscribeplus;
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

                rel_bottomcart = itemView.findViewById(R.id.rel_cartmain);
                txt_mrp.setPaintFlags(txt_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


            }

            public void bind(final ProductModel model, final int position) {

//                if (model.getIs_veg().equals("1")) {
//                    iv_veg.setVisibility(View.VISIBLE);
//
//                } else if (model.getIs_veg().equals("0")) {
//                    iv_nonveg.setVisibility(View.VISIBLE);
//                } else {
//                    iv_nonveg.setVisibility(View.GONE);
//                    iv_veg.setVisibility(View.GONE);
//
//                }

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
                            long price_amount = Math.round(Double.valueOf(actualamt));

                            txt_mrp.setText("MRP \u20b9" + price_amount);


                            double payableAmt = Double.parseDouble(model.getPrice()) - Double.parseDouble(model.getDiscount());
                            double finalpayamt = payableAmt * Double.parseDouble(model.getQty());
                            long finalprice_amount = Math.round(Double.valueOf(finalpayamt));

                            txt_og_price.setText("Sterlex Super Mart \u20b9" + finalprice_amount);

//                        txt_mrp.setText("MRP \u20B9" + model.getPrice());
//                        txt_discount.setText("Save\n\u20B9" + model.getDiscount());
                            double saveamt = Double.parseDouble(model.getDiscount()) * Double.parseDouble(model.getQty());
                            long save_amount = Math.round(Double.valueOf(saveamt));

                            txt_discount.setText("Save \u20B9" + save_amount);
                        } else {
                            actualAmout = Double.parseDouble(model.getPrice());
                            payableAmt = actualAmout - Double.parseDouble(model.getDiscount());
                            long price_amount = Math.round(Double.valueOf(payableAmt));

                            txt_og_price.setText("Sterlex Super Mart \u20b9" + price_amount);
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
                Glide.with(CategoryWiseProductListActivity.this).load(model.getImage()).into(iv);

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
                            recy_qty.setLayoutManager(new LinearLayoutManager(CategoryWiseProductListActivity.this, RecyclerView.HORIZONTAL, false));
                            ProductsListingAdapter.QuantityListingAdapter adapter1 = new ProductsListingAdapter.QuantityListingAdapter(CategoryWiseProductListActivity.this, product_unitArraylist, txt_mrp, txt_og_price, txt_discount, txt_unit);
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
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CategoryWiseProductListActivity.this);
                            // Setting Dialog Title
                            alertDialog.setTitle("Please Login");
                            // Setting Dialog Message
                            alertDialog.setMessage("Please login to add product in cart");
                            // On pressing Settings button
                            alertDialog.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(CategoryWiseProductListActivity.this, LoginActivity.class);
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
//                                Toast.makeText(CategoryWiseProductListActivity.this, str_price, Toast.LENGTH_SHORT).show();
                                String discount = txt_discount.getText().toString();
                                String str_discount = "0";
                                if (!discount.equals("0")) {
                                    str_discount = discount.substring(6, discount.length());
//                                    txt_mrp.setVisibility(View.VISIBLE);
//                                    txt_discount.setVisibility(View.VISIBLE);
//                                    Toast.makeText(CategoryWiseProductListActivity.this, str_discount, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(CategoryWiseProductListActivity.this, "out of stock", Toast.LENGTH_SHORT).show();
                            }
                        }
//                        addCart(model.getProduct_id(), model.getUnit(), model.getPrice(), model.getDiscount(), model.getQty(), model.getGst(), position);
                    }
                });


                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CategoryWiseProductListActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });
                rel_veg_nonveg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CategoryWiseProductListActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });
                txt_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CategoryWiseProductListActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });
                txt_mrp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CategoryWiseProductListActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });
                txt_og_price.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CategoryWiseProductListActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });
                txt_discount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CategoryWiseProductListActivity.this, ProductDetailsActivity.class);
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
//                                        Toast.makeText(CategoryWiseProductListActivity.this, str_price, Toast.LENGTH_SHORT).show();
                                        String discount = txt_discount.getText().toString();
                                        String str_discount = "0";
                                        if (!discount.equals("0")) {
                                            str_discount = discount.substring(6, discount.length());
//                                            txt_mrp.setVisibility(View.VISIBLE);
//                                            txt_discount.setVisibility(View.VISIBLE);
//                                            Toast.makeText(CategoryWiseProductListActivity.this, str_discount, Toast.LENGTH_SHORT).show();
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
//                                    Toast.makeText(CategoryWiseProductListActivity.this, "removing", Toast.LENGTH_SHORT).show();
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

                if (APIURLs.isNetwork(CategoryWiseProductListActivity.this)) {
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
                                        txt_discountprice.setText("Sterlex Super Mart \u20b9" + finalprice_amount);

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
                                    Toast.makeText(CategoryWiseProductListActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                            params.put("customer_id", SharedPref.getVal(CategoryWiseProductListActivity.this, SharedPref.customer_id));
                            params.put("product_id", prod_id);
                            params.put("unit", unit);
                            params.put("price", price);
                            params.put("gst", "");
                            params.put("discount", discount);
                            params.put("qty", qty1);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(CategoryWiseProductListActivity.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    dismissDialog();
                    FunctionConstant.noInternetDialog(CategoryWiseProductListActivity.this, "no internet connection");
                }
            }


            public void removeCart(final String product_id, final String unit, final int position) {
//                dialog.show();
                progressBar.setVisibility(View.VISIBLE);
                txt_count.setVisibility(GONE);
                if (APIURLs.isNetwork(CategoryWiseProductListActivity.this)) {
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
                                    Toast.makeText(CategoryWiseProductListActivity.this, "Item removed from cart successfully", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(CategoryWiseProductListActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                            params.put("customer_id", SharedPref.getVal(CategoryWiseProductListActivity.this, SharedPref.customer_id));
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(CategoryWiseProductListActivity.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    dismissDialog();
                    FunctionConstant.noInternetDialog(CategoryWiseProductListActivity.this, "no internet connection");
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
            public long getItemId(int position) {
                return position;
            }

            @Override
            public int getItemViewType(int position) {
                return position;
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

    public void cartCount() {
        if (APIURLs.isNetwork(CategoryWiseProductListActivity.this)) {
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
                            Toast.makeText(CategoryWiseProductListActivity.this, "Error", Toast.LENGTH_SHORT).show();
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

                    params.put("customer_id", SharedPref.getVal(CategoryWiseProductListActivity.this, SharedPref.customer_id));
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

//            final Dialog dialog = new Dialog(CategoryWiseProductListActivity.this);
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



//    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> implements Filterable {
//        Context context;
//        Double actualAmout;
//        Double discount;
//        Double payableAmt;
//        ArrayList<ProductModel> arrayList;
//
//        public ProductAdapter(Context context) {
//            this.context = context;
//        }
//
//        @NonNull
//        @Override
//        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(CategoryWiseProductListActivity.this).inflate(R.layout.search_item_layout, parent, false);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
//            ProductModel model = productModelArrayList.get(position);
//            ((ViewHolder) holder).bind(model, position);
//        }
//
//        @Override
//        public int getItemCount() {
//            return productModelArrayList.size();
//        }
//
//        @Override
//        public Filter getFilter() {
//            return new Filter() {
//                @Override
//                protected FilterResults performFiltering(CharSequence charSequence) {
//                    FilterResults results = new FilterResults();
//                    ArrayList<ProductModel> data = new ArrayList<ProductModel>();
//                    if (arrayList == null)
//                        arrayList = productModelArrayList;
//                    if (arrayList != null && arrayList.size() > 0) {
//                        for (final ProductModel g : arrayList) {
//                            if ((g.getProduct_name().toLowerCase().contains(charSequence.toString())))
//                                data.add(g);
//                        }
//                        results.values = data;
//                    }
//                    results.values = data;
//                    results.count = data.size();
//                    return results;
//                }
//
//                @Override
//                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                    productModelArrayList = (ArrayList<ProductModel>) filterResults.values;
//                    notifyDataSetChanged();
//                }
//            };
//        }
//
//        @Override
//        public int getItemViewType(int position) {
//            return position;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//            ImageView img_product;
//            TextView txt_productName, txt_weight, txt_season, txt_actualprice, txt_stock, txt_discountprice, txt_discount, txt_subscribecount;
//            Button btn_cart, btn_subscribe;
//            LinearLayout lnr_product;
//            RelativeLayout rel_subs, rel_stock;
//            RelativeLayout rel_discount, rel_cart, rel_minus, rel_plus, rel_subscribe, rel_subscribeminus, rel_subscribeplus;
//            RelativeLayout rel_cartmain;
//            ProgressBar progressBar;
//            TextView txt_count;
//
//            public ViewHolder(@NonNull View itemView) {
//                super(itemView);
//
//                img_product = itemView.findViewById(R.id.img_product);
//                txt_productName = itemView.findViewById(R.id.txt_productName);
//                txt_weight = itemView.findViewById(R.id.txt_weight);
//                txt_season = itemView.findViewById(R.id.txt_season);
//                txt_actualprice = itemView.findViewById(R.id.txt_actualprice);
//                txt_discountprice = itemView.findViewById(R.id.txt_discountprice);
//                txt_stock = itemView.findViewById(R.id.txt_stock);
//                rel_stock = itemView.findViewById(R.id.rel_stock);
//                btn_cart = itemView.findViewById(R.id.btn_cart);
//                btn_subscribe = itemView.findViewById(R.id.btn_subscribe);
//                lnr_product = itemView.findViewById(R.id.lnr_product);
//                rel_discount = itemView.findViewById(R.id.rel_discount);
//                rel_minus = itemView.findViewById(R.id.rel_minus);
//                rel_plus = itemView.findViewById(R.id.rel_plus);
//                rel_subs = itemView.findViewById(R.id.rel_subs);
//                rel_subscribe = itemView.findViewById(R.id.rel_subscribe);
//                rel_subscribeminus = itemView.findViewById(R.id.rel_subscribeminus);
//                rel_subscribeplus = itemView.findViewById(R.id.rel_subscribeplus);
//                rel_cart = itemView.findViewById(R.id.rel_cart);
//                txt_discount = itemView.findViewById(R.id.txt_discount);
//                txt_count = itemView.findViewById(R.id.txt_count);
//                progressBar = itemView.findViewById(R.id.progressBar);
//                rel_cartmain = itemView.findViewById(R.id.rel_cartmain);
//                txt_subscribecount = itemView.findViewById(R.id.txt_subscribecount);
//                txt_actualprice.setPaintFlags(txt_actualprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//            }
//
//            public void bind(final ProductModel model, final int position) {
//                Glide.with(CategoryWiseProductListActivity.this).
//                        load(model.getImage()).error(R.drawable.loader).
//                        placeholder(R.drawable.loader).into(img_product);
////                if (model.getProduct_name().length() <= 20) {
//                txt_productName.setText(model.getProduct_name());
//                /*} else {
//                    String pname = model.getProduct_name();
//                    pname = pname.substring(0, 20);
//                    txt_productName.setText(pname + "...");
//                }*/
//                txt_weight.setText(model.getUnit());
//                try {
//                    if (model.getDiscount().equals("0")) {
//                        txt_discountprice.setText("Rs." + model.getPrice());
//                        txt_actualprice.setVisibility(GONE);
//                        rel_discount.setVisibility(GONE);
//                    } else {
//                        txt_actualprice.setVisibility(View.VISIBLE);
//                        rel_discount.setVisibility(View.VISIBLE);
//                        actualAmout = Double.parseDouble(model.getPrice());
//                       /* discount = (actualAmout * Double.parseDouble(model.getDiscount())) / 100;
//                        payableAmt = actualAmout - discount;
//                        DecimalFormat twoDForm = new DecimalFormat("#");
//                        payableAmt = Double.valueOf(twoDForm.format(payableAmt));*/
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
//                if (model.getFlag().equals("0")) {
//                    rel_stock.setVisibility(View.VISIBLE);
//                } else if (model.getFlag().equals("1")) {
//                    rel_stock.setVisibility(GONE);
//                }
//
//
//                if (model.getQty().equals("0")) {
//                    rel_cart.setVisibility(GONE);
//                    btn_cart.setVisibility(View.VISIBLE);
//                } else {
//                    rel_cart.setVisibility(View.VISIBLE);
//                    btn_cart.setVisibility(GONE);
//                    qty = Integer.valueOf(model.getQty());
//                    txt_count.setText(String.valueOf(qty));
//                }
//
//                btn_cart.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (cost_id.equals("")) {
//                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CategoryWiseProductListActivity.this);
//                            // Setting Dialog Title
//                            alertDialog.setTitle("Please Login");
//                            // Setting Dialog Message
//                            alertDialog.setMessage("Please login to add product in cart");
//                            // On pressing Settings button
//                            alertDialog.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Intent intent = new Intent(CategoryWiseProductListActivity.this, LoginActivity.class);
//                                    intent.putExtra("from", "cat_product");
//                                    startActivity(intent);
//                                }
//                            });
//                            // on pressing cancel button
//                            alertDialog.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                }
//                            });
//                            // Showing Alert Message
//                            alertDialog.show();
//                        } else {
//                            if (model.getFlag().equals("1")) {
//                                rel_cart.setVisibility(View.VISIBLE);
//                                btn_cart.setVisibility(GONE);
//                                addCart(model.getProduct_id(), model.getUnit(), model.getPrice(),
//                                        model.getDiscount(), txt_count.getText().toString(), position);
//                            } else {
//                                Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                });
//
//                btn_subscribe.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (cost_id.equals("")) {
//                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CategoryWiseProductListActivity.this);
//                            // Setting Dialog Title
//                            alertDialog.setTitle("Please Login");
//                            // Setting Dialog Message
//                            alertDialog.setMessage("Please login for subscription");
//                            // On pressing Settings button
//                            alertDialog.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Intent intent = new Intent(CategoryWiseProductListActivity.this, LoginActivity.class);
//                                    intent.putExtra("from", "cat_product");
//                                    startActivity(intent);
//                                }
//                            });
//                            // on pressing cancel button
//                            alertDialog.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                }
//                            });
//                            // Showing Alert Message
//                            alertDialog.show();
//                        } else {
//                            qtymodel = new QuntityModel();
//                            qtymodel.setId(model.getProduct_id());
//                            qtymodel.setQty(txt_subscribecount.getText().toString());
//
//                            qtyArrayList.add(qtymodel);
//                            if (model.getFlag().equals("1")) {
//                                rel_subscribe.setVisibility(View.VISIBLE);
//                                btn_subscribe.setVisibility(GONE);
//                                lin_day.setVisibility(View.VISIBLE);
//                          /*  if (productId.size() > 0) {
//                                for (int i = 0; i < productId.size(); i++) {
//                                    if (productId.get(i).equals(model.getProduct_id())) {
//                                        productId.remove(i);
//                                    }
//                                }
//                            }
//                            if (unitPriceArrayList.size() > 0) {
//                                for (int i = 0; i < unitPriceArrayList.size(); i++) {
//                                    if (unitPriceArrayList.get(i).equals(model.getPrice())) {
//                                        unitPriceArrayList.remove(i);
//                                    }
//                                }
//                            }
//                            if (unitArrayList.size() > 0) {
//                                for (int i = 0; i < unitArrayList.size(); i++) {
//                                    if (unitArrayList.get(i).equals(model.getUnit())) {
//                                        unitArrayList.remove(i);
//                                    }
//                                }
//                            }
//                            if (discountArrayList.size() > 0) {
//                                for (int i = 0; i < discountArrayList.size(); i++) {
//                                    if (discountArrayList.get(i).equals(model.getDiscount())) {
//                                        discountArrayList.remove(i);
//                                    }
//                                }
//                            }*/
//                                productId.add(model.getProduct_id());
//                                unitPriceArrayList.add(model.getPrice());
//                                unitArrayList.add(model.getUnit());
//                                discountArrayList.add(model.getDiscount());
//                            } else {
//                                Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                });
//
//                rel_minus.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        qty = Integer.parseInt(txt_count.getText().toString());
//                        if (model.getFlag().equals("1")) {
//                            qty = Integer.parseInt(txt_count.getText().toString());
//                            if (qty >= 1) {
//                                qty--;
//                                if (qty != 0) {
//                                    if (qty >= 1) {
////                                        progressBar.setVisibility(View.VISIBLE);
//                                        txt_count.setVisibility(GONE);
//                                        txt_count.setText(String.valueOf(qty));
//                                        addCart(model.getProduct_id(), model.getUnit(), model.getPrice(),
//                                                model.getDiscount(), txt_count.getText().toString(), position);
//                                    }
//                                    /*else if(qty==1){
//                                        rel_cart.setVisibility(View.GONE);
//                                        btn_cart.setVisibility(View.VISIBLE);
//                                        removeCart(model.getProduct_id(),position);
//                                    }*/
//                                } else {
//                                    rel_cart.setVisibility(GONE);
//                                    btn_cart.setVisibility(View.VISIBLE);
//                                    removeCart(model.getProduct_id(), position);
//                                }
//                            }
//                        } else {
//                            Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });
//
//                rel_plus.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        qty = Integer.parseInt(txt_count.getText().toString());
//                        if (model.getFlag().equals("1")) {
//                            qty = Integer.parseInt(txt_count.getText().toString());
//                            qty++;
//                            /* progressBar.setVisibility(View.VISIBLE);*/
//                            txt_count.setVisibility(GONE);
//                            txt_count.setText(String.valueOf(qty));
//                            addCart(model.getProduct_id(), model.getUnit(), model.getPrice(), model.getDiscount(),
//                                    txt_count.getText().toString(), position);
//                        } else {
//                            Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });
//
//                rel_subscribeminus.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        subsQty = Integer.parseInt(txt_subscribecount.getText().toString());
//                        if (model.getFlag().equals("1")) {
//                            subsQty = Integer.parseInt(txt_subscribecount.getText().toString());
//                            if (subsQty >= 1) {
//                                subsQty--;
//                                if (subsQty != 0) {
//                                    if (subsQty > 1) {
//                                        txt_subscribecount.setText(String.valueOf(subsQty));
//                                    }
//                                    txt_subscribecount.setText(String.valueOf(subsQty));
//                                    for (int i = 0; i < qtyArrayList.size(); i++) {
//                                        String id = qtyArrayList.get(i).getId();
//                                        if (model.getProduct_id().equals(id)) {
//                                            qtymodel = new QuntityModel();
//                                            qtymodel.setId(id);
//                                            qtymodel.setQty(String.valueOf(subsQty));
//                                            qtyArrayList.remove(i);
//                                            qtyArrayList.add(i, qtymodel);
//                                        }
//                                    }
//                                } else {
//                                    rel_subscribe.setVisibility(GONE);
//                                    btn_subscribe.setVisibility(View.VISIBLE);
//                                    for (int i = 0; i < qtyArrayList.size(); i++) {
//                                       /* if (position == i) {
//                                            qtyArrayList.set(i, String.valueOf(subsQty));
//                                        }*/
//                                        String id = qtyArrayList.get(i).getId();
//                                        if (model.getProduct_id().equals(id)) {
//                                            qtymodel = new QuntityModel();
//                                            qtymodel.setId(id);
//                                            qtymodel.setQty(String.valueOf(subsQty));
//                                            if (subsQty == 0) {
//                                                qtyArrayList.remove(i);
//                                                productId.remove(i);
//                                                unitPriceArrayList.remove(i);
//                                                unitArrayList.remove(i);
//                                                discountArrayList.remove(i);
//                                            } else {
//                                                qtyArrayList.remove(i);
//                                                qtyArrayList.add(i, qtymodel);
//                                            }
//                                        }
//                                    }
//
//                                    if (qtyArrayList.size() == 0) {
//                                        lin_day.setVisibility(GONE);
//                                        card_mySubscription.setVisibility(GONE);
//                                        qtyArrayList = new ArrayList<>();
//                                        productId = new ArrayList<>();
//                                        unitPriceArrayList = new ArrayList<>();
//                                        unitArrayList = new ArrayList<>();
//                                        discountArrayList = new ArrayList<>();
//                                    }
//
//                                 /*   if (productId.size() > 0) {
//                                        for (int i = 0; i < productId.size(); i++) {
//                                            if (productId.get(i).equals(model.getProduct_id())) {
//                                                productId.remove(i);
//                                            }
//                                        }
//                                    }
//                                    if (unitPriceArrayList.size() > 0) {
//                                        for (int i = 0; i < unitPriceArrayList.size(); i++) {
//                                            if (unitPriceArrayList.get(i).equals(model.getPrice())) {
//                                                unitPriceArrayList.remove(i);
//                                            }
//                                        }
//                                    }
//                                    if (unitArrayList.size() > 0) {
//                                        for (int i = 0; i < unitArrayList.size(); i++) {
//                                            if (unitArrayList.get(i).equals(model.getUnit())) {
//                                                unitArrayList.remove(i);
//                                            }
//                                        }
//                                    }
//                                    if (discountArrayList.size() > 0) {
//                                        for (int i = 0; i < discountArrayList.size(); i++) {
//                                            if (discountArrayList.get(i).equals(model.getDiscount())) {
//                                                discountArrayList.remove(i);
//                                            }
//                                        }
//                                    }
//*/
//
//                                }
//                            }
//                        } else {
//                            Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });
//
//                rel_subscribeplus.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        subsQty = Integer.parseInt(txt_subscribecount.getText().toString());
//                        if (model.getFlag().equals("1")) {
//                            subsQty = Integer.parseInt(txt_subscribecount.getText().toString());
//                            subsQty++;
//                            txt_subscribecount.setText(String.valueOf(subsQty));
//                            for (int i = 0; i < qtyArrayList.size(); i++) {
//                               /* if (position == i) {
//                                    qtyArrayList.set(i, String.valueOf(subsQty));
//                                }*/
//                                String id = qtyArrayList.get(i).getId();
//                                if (model.getProduct_id().equals(id)) {
//                                    qtymodel = new QuntityModel();
//                                    qtymodel.setId(id);
//                                    qtymodel.setQty(String.valueOf(subsQty));
//                                    qtyArrayList.remove(i);
//                                    qtyArrayList.add(i, qtymodel);
//                                }
//                            }
//                        } else {
//                            Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });
//
//                lnr_product.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(new Intent(CategoryWiseProductListActivity.this, ProductDetailsActivity.class));
//                        intent.putExtra("prod_id", model.getProduct_id());
//                        startActivity(intent);
//                        clickEntry(model.getProduct_id());
//
//                    }
//                });
//
//                /*btn_subscribe.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (model.getFlag().equals("0")) {
//                            Toast.makeText(context, "Out of stock", Toast.LENGTH_SHORT).show();
//                        } else if (model.getFlag().equals("1")) {
//                            btn_subscribe.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_button_layout));
//                            btn_subscribe.setTextColor(getResources().getColor(R.color.colorWhite));
//
//                        }
//                    }
//                });*/
//            }
//
//            public void removeCart(final String product_id, final int position) {
////                dialog.show();
//                progressBar.setVisibility(View.VISIBLE);
//                txt_count.setVisibility(GONE);
//                if (APIURLs.isNetwork(CategoryWiseProductListActivity.this)) {
//                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.remove_product_qty, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//                                JSONObject jsonObject = new JSONObject(response);
//                                JSONArray jsonArray = jsonObject.getJSONArray("result");
//                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
//                                String status = jsonObject1.getString("status");
//                                if (status.equals("1")) {
//                                    qty = 0;
////                                    getProductList();
//                                    Toast.makeText(CategoryWiseProductListActivity.this, "Item removed from cart successfully", Toast.LENGTH_SHORT).show();
//                                    cartCount();
//                                   /* productId = new ArrayList<>();
//                                    unitArrayList = new ArrayList<>();
//                                    qtyArrayList = new ArrayList<>();
//                                    discountArrayList = new ArrayList<>();*/
//                                   /* adapter.notifyDataSetChanged();
//                                    adapter.notifyItemRemoved(position);*/
//
//                                    /*rel_cart.setVisibility(View.GONE);
//                                    btn_cart.setVisibility(View.VISIBLE);*/
//                                } else {
//                                    Toast.makeText(CategoryWiseProductListActivity.this, "Error", Toast.LENGTH_SHORT).show();
//                                }
////                                dismissDialog();
//                                progressBar.setVisibility(GONE);
//                                txt_count.setVisibility(View.VISIBLE);
//                            } catch (JSONException e) {
////                                dismissDialog();
//                                progressBar.setVisibility(GONE);
//                                txt_count.setVisibility(View.VISIBLE);
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
////                            dismissDialog();
//                            progressBar.setVisibility(GONE);
//                            txt_count.setVisibility(View.VISIBLE);
//                            error.printStackTrace();
//                        }
//                    }) {
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//                            HashMap<String, String> params = new HashMap<>();
//                            params.put("product_id", product_id);
//                            params.put("customer_id", SharedPref.getVal(CategoryWiseProductListActivity.this, SharedPref.customer_id));
//                            return params;
//                        }
//                    };
//
//                    RequestQueue requestQueue = Volley.newRequestQueue(CategoryWiseProductListActivity.this);
//                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                    requestQueue.add(stringRequest);
//                } else {
//                    dismissDialog();
//                    FunctionConstant.noInternetDialog(CategoryWiseProductListActivity.this, "no internet connection");
//                }
//            }
//
//            public void clickEntry(final String product_id) {
////                dialog.show();
//                if (APIURLs.isNetwork(CategoryWiseProductListActivity.this)) {
//                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.view_product_entry, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//                                JSONObject jsonObject = new JSONObject(response);
//                                JSONArray jsonArray = jsonObject.getJSONArray("result");
//                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
//                                String status = jsonObject1.getString("status");
//                                if (status.equals("1")) {
//                                } else {
//                                    Toast.makeText(CategoryWiseProductListActivity.this, "Error", Toast.LENGTH_SHORT).show();
//                                }
//                                dismissDialog();
//                            } catch (JSONException e) {
//                                dismissDialog();
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            dismissDialog();
//                            error.printStackTrace();
//                        }
//                    }) {
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//                            HashMap<String, String> params = new HashMap<>();
//                            params.put("product_id", product_id);
//                            params.put("customer_id", SharedPref.getVal(CategoryWiseProductListActivity.this, SharedPref.customer_id));
//                            return params;
//                        }
//                    };
//
//                    RequestQueue requestQueue = Volley.newRequestQueue(CategoryWiseProductListActivity.this);
//                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                    requestQueue.add(stringRequest);
//                } else {
//                    dismissDialog();
//                    FunctionConstant.noInternetDialog(CategoryWiseProductListActivity.this, "no internet connection");
//                }
//            }
//
//            public void addCart(final String prod_id, final String unit, final String price, final String discount, final String qty1, final int position) {
////        dialog.show();
//                progressBar.setVisibility(View.VISIBLE);
//                txt_count.setVisibility(GONE);
//                if (APIURLs.isNetwork(CategoryWiseProductListActivity.this)) {
//                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.ADDCART_URL, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//                                JSONObject jsonObject = new JSONObject(response);
//                                JSONArray jsonArray = jsonObject.getJSONArray("result");
//                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
//                                String status = jsonObject1.getString("status");
//                                if (status.equals("1")) {
//                                    txt_count.setText(qty1);
//                                    qty = 0;
//                                    cartCount();
//
//                           /* productId = new ArrayList<>();
//                            unitArrayList = new ArrayList<>();
//                            unitArrayList = new ArrayList<>();
//                            qtyArrayList = new ArrayList<>();
//                            discountArrayList = new ArrayList<>();*/
////                            getProductList();
////                            getProductList();
//                                } else {
//                                    Toast.makeText(CategoryWiseProductListActivity.this, "Error", Toast.LENGTH_SHORT).show();
//                                }
//                                progressBar.setVisibility(GONE);
//                                txt_count.setVisibility(View.VISIBLE);
////                        dismissDialog();
//                            } catch (JSONException e) {
////                        dismissDialog();
//
//                                progressBar.setVisibility(GONE);
//                                txt_count.setVisibility(View.VISIBLE);
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
////                    dismissDialog();
//
//                            progressBar.setVisibility(GONE);
//                            txt_count.setVisibility(View.VISIBLE);
//                            error.printStackTrace();
//                        }
//                    }) {
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//                            HashMap<String, String> params = new HashMap<>();
//                            params.put("customer_id", SharedPref.getVal(CategoryWiseProductListActivity.this, SharedPref.customer_id));
//                            params.put("product_id", prod_id);
//                            params.put("unit", unit);
//                            params.put("price", price);
//                            params.put("discount", discount);
//                            params.put("qty", qty1);
//                            return params;
//                        }
//                    };
//
//                    RequestQueue requestQueue = Volley.newRequestQueue(CategoryWiseProductListActivity.this);
//                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                    requestQueue.add(stringRequest);
//                } else {
//                    dismissDialog();
//                    FunctionConstant.noInternetDialog(CategoryWiseProductListActivity.this, "no internet connection");
//                }
//            }
//        }
//    }
//
//    public void cartCount() {
//        if (APIURLs.isNetwork(CategoryWiseProductListActivity.this)) {
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
//                                rel_cart_count.setVisibility(GONE);
//                            } else {
//                                rel_cart_count.setVisibility(View.VISIBLE);
//                                txt_cart_count.setText(count);
//                            }
//                        } else {
//                            Toast.makeText(CategoryWiseProductListActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
//                    params.put("customer_id", SharedPref.getVal(CategoryWiseProductListActivity.this, SharedPref.customer_id));
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
//            FunctionConstant.noInternetDialog(CategoryWiseProductListActivity.this, "no internet connection");
//        }
//    }

}
