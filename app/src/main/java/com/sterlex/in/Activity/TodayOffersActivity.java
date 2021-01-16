package com.sterlex.in.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.sterlex.in.Constant.APIURLs;
import com.sterlex.in.Constant.FunctionConstant;
import com.sterlex.in.Constant.SharedPref;
import com.sterlex.in.Model.CategoryModel;
import com.sterlex.in.Model.ProductModel;
import com.sterlex.in.Model.QuntityModel;
import com.sterlex.in.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.gmail.samehadar.iosdialog.IOSDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;

public class TodayOffersActivity extends AppCompatActivity {

    ImageView img_back, img_close;
    TextView txt_categoryName, txt_fromdate, txt_todate, txt_nodata, txt_cart_count, txt;
    RecyclerView rec_list, rec_subcategory;
    CardView card_bottom, card_mySubscription;
    LinearLayout lin_day, lin_one, lin_two, lin_three, lin_four, lin_s, lin_s_sel, lin_m,
            lin_m_sel, lin_t, lin_t_sel, lin_w, lin_w_sel, lin_th, lin_th_sel, lin_f, lin_f_sel, lin_sat, lin_sat_sel;
    LinearLayout lnr_home, lnr_home_selected, lnr_search, lnr_search_selected,
            lnr_cart, lnr_cart_selected, lnr_subscription, lnr_subscription_selected, lnr_account, lnr_account_selected;
    LinearLayout lnr_selectDays;
    RelativeLayout rel_home, rel_search, rel_cart, rel_subscription, rel_account;
    String category = "";
    String strFromDate = "", strToDate = "";
    private int mYear, mMonth, mDay;
    RelativeLayout rel_from, rel_to;
    RelativeLayout rel_cart_count;
    String sub_cat_id = "", sub_cat_name = "", from = "";
    IOSDialog dialog;
    int qty = 0, subsQty = 0;
    ArrayList<ProductModel> productModelArrayList = new ArrayList<>();
    ArrayList<CategoryModel> categoryModelArrayList = new ArrayList<>();
    String subscription_type = "", weeklyDays = "", customizeDays = "";
    ArrayList<String> productId = new ArrayList<>();
    ArrayList<String> daysArrayList = new ArrayList<>();
    ArrayList<QuntityModel> qtyArrayList = new ArrayList<>();
    ArrayList<String> unitPriceArrayList = new ArrayList<>();
    ArrayList<String> unitArrayList = new ArrayList<>();
    ArrayList<String> discountArrayList = new ArrayList<>();
    Button btn_submit;
    String customizeDay = "", strProductId = "", strQty = "", strUnitPrice = "", strUnit = "", strDiscount = "";
    QuntityModel qtymodel;
    ShimmerFrameLayout shimmer_view_container;
    LinearLayout lnr_main;
    ProductAdapter adapter;
    String cost_id = "";
    TextView txt_timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_offers);
        init();
        cost_id = SharedPref.getVal(TodayOffersActivity.this, SharedPref.customer_id);
        onClick();
    }

    private void init() {
        dialog = new IOSDialog.Builder(TodayOffersActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();

        img_back = findViewById(R.id.img_back);
        shimmer_view_container = findViewById(R.id.shimmer_view_container);
        img_close = findViewById(R.id.img_close);
        txt_categoryName = findViewById(R.id.txt_categoryName);
        txt_nodata = findViewById(R.id.txt_nodata);
        lin_day = findViewById(R.id.lin_day);
        lin_one = findViewById(R.id.lin_one);
        lin_two = findViewById(R.id.lin_two);
        lin_three = findViewById(R.id.lin_three);
        lin_four = findViewById(R.id.lin_four);
        btn_submit = findViewById(R.id.btn_submit);

        txt_cart_count = findViewById(R.id.txt_cart_count);
        rel_cart_count = findViewById(R.id.rel_cart_count);

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
        rec_subcategory = findViewById(R.id.rec_subcategory);

        txt_fromdate = findViewById(R.id.txt_fromdate);
        txt_todate = findViewById(R.id.txt_todate);
        rel_from = findViewById(R.id.rel_from);
        rel_to = findViewById(R.id.rel_to);
        txt_timer = findViewById(R.id.txt_timer);

        card_bottom = findViewById(R.id.card_bottom);
        card_mySubscription = findViewById(R.id.card_mySubscription);
        rec_list.setLayoutManager(new LinearLayoutManager(TodayOffersActivity.this));
        rec_subcategory.setLayoutManager(new LinearLayoutManager(TodayOffersActivity.this, RecyclerView.HORIZONTAL, false));
    }

    private void onClick() {

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_mySubscription.setVisibility(View.GONE);
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
                    Toast.makeText(TodayOffersActivity.this, "Please select from date", Toast.LENGTH_SHORT).show();
                } else if (strToDate.equals("")) {
                    Toast.makeText(TodayOffersActivity.this, "Please select to date", Toast.LENGTH_SHORT).show();
                } else {
                    if (subscription_type.equals("Weekly")) {
                        if (weeklyDays.equals("")) {
                            Toast.makeText(TodayOffersActivity.this, "Please select day", Toast.LENGTH_SHORT).show();
                        } else {
//                            subscribed();
                            Intent intent = new Intent(TodayOffersActivity.this, SubscriptionAddressActivity.class);
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
                            Toast.makeText(TodayOffersActivity.this, "Please select days", Toast.LENGTH_SHORT).show();
                        } else {
//                            subscribed();
                            Intent intent = new Intent(TodayOffersActivity.this, SubscriptionAddressActivity.class);
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
                        Intent intent = new Intent(TodayOffersActivity.this, SubscriptionAddressActivity.class);
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
                lin_day.setVisibility(View.GONE);
                subscription_type = "Weekly";
                lnr_selectDays.setVisibility(View.VISIBLE);

                lin_s.setVisibility(View.VISIBLE);
                lin_s_sel.setVisibility(View.GONE);

                lin_m.setVisibility(View.VISIBLE);
                lin_m_sel.setVisibility(View.GONE);

                lin_t.setVisibility(View.VISIBLE);
                lin_t_sel.setVisibility(View.GONE);

                lin_w.setVisibility(View.VISIBLE);
                lin_w_sel.setVisibility(View.GONE);

                lin_th.setVisibility(View.VISIBLE);
                lin_th_sel.setVisibility(View.GONE);

                lin_f.setVisibility(View.VISIBLE);
                lin_f_sel.setVisibility(View.GONE);

                lin_sat.setVisibility(View.VISIBLE);
                lin_sat_sel.setVisibility(View.GONE);

                weeklyDays = "";
                daysArrayList = new ArrayList<>();
            }
        });

        lin_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*card_bottom.setVisibility(View.GONE);*/
                card_mySubscription.setVisibility(View.VISIBLE);
                lin_day.setVisibility(View.GONE);
                subscription_type = "Daily";
                lnr_selectDays.setVisibility(View.GONE);


            }
        });
        lin_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*card_bottom.setVisibility(View.GONE);*/
                card_mySubscription.setVisibility(View.VISIBLE);

                lin_day.setVisibility(View.GONE);
                subscription_type = "Custom";
                lnr_selectDays.setVisibility(View.VISIBLE);


                lin_s.setVisibility(View.VISIBLE);
                lin_s_sel.setVisibility(View.GONE);

                lin_m.setVisibility(View.VISIBLE);
                lin_m_sel.setVisibility(View.GONE);

                lin_t.setVisibility(View.VISIBLE);
                lin_t_sel.setVisibility(View.GONE);

                lin_w.setVisibility(View.VISIBLE);
                lin_w_sel.setVisibility(View.GONE);

                lin_th.setVisibility(View.VISIBLE);
                lin_th_sel.setVisibility(View.GONE);

                lin_f.setVisibility(View.VISIBLE);
                lin_f_sel.setVisibility(View.GONE);

                lin_sat.setVisibility(View.VISIBLE);
                lin_sat_sel.setVisibility(View.GONE);

                weeklyDays = "";
                daysArrayList = new ArrayList<>();
            }
        });
        lin_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*card_bottom.setVisibility(View.GONE);*/
                card_mySubscription.setVisibility(View.GONE);
                lin_day.setVisibility(View.GONE);


                lin_s.setVisibility(View.VISIBLE);
                lin_s_sel.setVisibility(View.GONE);

                lin_m.setVisibility(View.VISIBLE);
                lin_m_sel.setVisibility(View.GONE);

                lin_t.setVisibility(View.VISIBLE);
                lin_t_sel.setVisibility(View.GONE);

                lin_w.setVisibility(View.VISIBLE);
                lin_w_sel.setVisibility(View.GONE);

                lin_th.setVisibility(View.VISIBLE);
                lin_th_sel.setVisibility(View.GONE);

                lin_f.setVisibility(View.VISIBLE);
                lin_f_sel.setVisibility(View.GONE);

                lin_sat.setVisibility(View.VISIBLE);
                lin_sat_sel.setVisibility(View.GONE);

                weeklyDays = "";
                customizeDay = "";
                daysArrayList = new ArrayList<>();
//                getProductList();
            }
        });

        rel_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(TodayOffersActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                final DatePickerDialog datePickerDialog = new DatePickerDialog(TodayOffersActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                    lin_s.setVisibility(View.GONE);
                    lin_s_sel.setVisibility(View.VISIBLE);

                    lin_m.setVisibility(View.VISIBLE);
                    lin_m_sel.setVisibility(View.GONE);

                    lin_t.setVisibility(View.VISIBLE);
                    lin_t_sel.setVisibility(View.GONE);

                    lin_w.setVisibility(View.VISIBLE);
                    lin_w_sel.setVisibility(View.GONE);

                    lin_th.setVisibility(View.VISIBLE);
                    lin_th_sel.setVisibility(View.GONE);

                    lin_f.setVisibility(View.VISIBLE);
                    lin_f_sel.setVisibility(View.GONE);

                    lin_sat.setVisibility(View.VISIBLE);
                    lin_sat_sel.setVisibility(View.GONE);

                    weeklyDays = "Sunday";

                } else {
                    lin_s.setVisibility(View.GONE);
                    lin_s_sel.setVisibility(View.VISIBLE);
                    daysArrayList.add("Sunday");
                }

            }
        });
        lin_s_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_s.setVisibility(View.VISIBLE);
                lin_s_sel.setVisibility(View.GONE);

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
                    lin_s_sel.setVisibility(View.GONE);

                    lin_m.setVisibility(View.GONE);
                    lin_m_sel.setVisibility(View.VISIBLE);

                    lin_t.setVisibility(View.VISIBLE);
                    lin_t_sel.setVisibility(View.GONE);

                    lin_w.setVisibility(View.VISIBLE);
                    lin_w_sel.setVisibility(View.GONE);

                    lin_th.setVisibility(View.VISIBLE);
                    lin_th_sel.setVisibility(View.GONE);

                    lin_f.setVisibility(View.VISIBLE);
                    lin_f_sel.setVisibility(View.GONE);

                    lin_sat.setVisibility(View.VISIBLE);
                    lin_sat_sel.setVisibility(View.GONE);

                    weeklyDays = "Monday";

                } else {
                    lin_m.setVisibility(View.GONE);
                    lin_m_sel.setVisibility(View.VISIBLE);
                    daysArrayList.add("Monday");
                }
            }
        });
        lin_m_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_m.setVisibility(View.VISIBLE);
                lin_m_sel.setVisibility(View.GONE);
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
                    lin_s_sel.setVisibility(View.GONE);

                    lin_m.setVisibility(View.VISIBLE);
                    lin_m_sel.setVisibility(View.GONE);

                    lin_t.setVisibility(View.GONE);
                    lin_t_sel.setVisibility(View.VISIBLE);

                    lin_w.setVisibility(View.VISIBLE);
                    lin_w_sel.setVisibility(View.GONE);

                    lin_th.setVisibility(View.VISIBLE);
                    lin_th_sel.setVisibility(View.GONE);

                    lin_f.setVisibility(View.VISIBLE);
                    lin_f_sel.setVisibility(View.GONE);

                    lin_sat.setVisibility(View.VISIBLE);
                    lin_sat_sel.setVisibility(View.GONE);

                    weeklyDays = "Tuesday";

                } else {
                    lin_t.setVisibility(View.GONE);
                    lin_t_sel.setVisibility(View.VISIBLE);

                    daysArrayList.add("Tuesday");
                }

            }
        });
        lin_t_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_t.setVisibility(View.VISIBLE);
                lin_t_sel.setVisibility(View.GONE);
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
                    lin_s_sel.setVisibility(View.GONE);

                    lin_m.setVisibility(View.VISIBLE);
                    lin_m_sel.setVisibility(View.GONE);

                    lin_t.setVisibility(View.VISIBLE);
                    lin_t_sel.setVisibility(View.GONE);

                    lin_w.setVisibility(View.GONE);
                    lin_w_sel.setVisibility(View.VISIBLE);

                    lin_th.setVisibility(View.VISIBLE);
                    lin_th_sel.setVisibility(View.GONE);

                    lin_f.setVisibility(View.VISIBLE);
                    lin_f_sel.setVisibility(View.GONE);

                    lin_sat.setVisibility(View.VISIBLE);
                    lin_sat_sel.setVisibility(View.GONE);

                    weeklyDays = "Wednesday";

                } else {
                    lin_w.setVisibility(View.GONE);
                    lin_w_sel.setVisibility(View.VISIBLE);

                    daysArrayList.add("Wednesday");
                }

            }
        });
        lin_w_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_w.setVisibility(View.VISIBLE);
                lin_w_sel.setVisibility(View.GONE);
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
                    lin_s_sel.setVisibility(View.GONE);

                    lin_m.setVisibility(View.VISIBLE);
                    lin_m_sel.setVisibility(View.GONE);

                    lin_t.setVisibility(View.VISIBLE);
                    lin_t_sel.setVisibility(View.GONE);

                    lin_w.setVisibility(View.VISIBLE);
                    lin_w_sel.setVisibility(View.GONE);

                    lin_th.setVisibility(View.GONE);
                    lin_th_sel.setVisibility(View.VISIBLE);

                    lin_f.setVisibility(View.VISIBLE);
                    lin_f_sel.setVisibility(View.GONE);

                    lin_sat.setVisibility(View.VISIBLE);
                    lin_sat_sel.setVisibility(View.GONE);

                    weeklyDays = "Thursday";

                } else {
                    lin_th.setVisibility(View.GONE);
                    lin_th_sel.setVisibility(View.VISIBLE);

                    daysArrayList.add("Thursday");
                }
            }
        });
        lin_th_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_th.setVisibility(View.VISIBLE);
                lin_th_sel.setVisibility(View.GONE);
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
                    lin_s_sel.setVisibility(View.GONE);

                    lin_m.setVisibility(View.VISIBLE);
                    lin_m_sel.setVisibility(View.GONE);

                    lin_t.setVisibility(View.VISIBLE);
                    lin_t_sel.setVisibility(View.GONE);

                    lin_w.setVisibility(View.VISIBLE);
                    lin_w_sel.setVisibility(View.GONE);

                    lin_th.setVisibility(View.VISIBLE);
                    lin_th_sel.setVisibility(View.GONE);

                    lin_f.setVisibility(View.GONE);
                    lin_f_sel.setVisibility(View.VISIBLE);

                    lin_sat.setVisibility(View.VISIBLE);
                    lin_sat_sel.setVisibility(View.GONE);

                    weeklyDays = "Friday";

                } else {
                    lin_f.setVisibility(View.GONE);
                    lin_f_sel.setVisibility(View.VISIBLE);

                    daysArrayList.add("Friday");
                }
            }
        });
        lin_f_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_f.setVisibility(View.VISIBLE);
                lin_f_sel.setVisibility(View.GONE);
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
                    lin_s_sel.setVisibility(View.GONE);

                    lin_m.setVisibility(View.VISIBLE);
                    lin_m_sel.setVisibility(View.GONE);

                    lin_t.setVisibility(View.VISIBLE);
                    lin_t_sel.setVisibility(View.GONE);

                    lin_w.setVisibility(View.VISIBLE);
                    lin_w_sel.setVisibility(View.GONE);

                    lin_th.setVisibility(View.VISIBLE);
                    lin_th_sel.setVisibility(View.GONE);

                    lin_f.setVisibility(View.VISIBLE);
                    lin_f_sel.setVisibility(View.GONE);

                    lin_sat.setVisibility(View.GONE);
                    lin_sat_sel.setVisibility(View.VISIBLE);

                    weeklyDays = "Saturday";

                } else {
                    lin_sat.setVisibility(View.GONE);
                    lin_sat_sel.setVisibility(View.VISIBLE);

                    daysArrayList.add("Saturday");
                }
            }
        });
        lin_sat_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_sat.setVisibility(View.VISIBLE);
                lin_sat_sel.setVisibility(View.GONE);
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
                lnr_home.setVisibility(View.GONE);

                lnr_search.setVisibility(View.VISIBLE);
                lnr_search_selected.setVisibility(View.GONE);

                lnr_cart.setVisibility(View.VISIBLE);
                lnr_cart_selected.setVisibility(View.GONE);

                lnr_subscription.setVisibility(View.VISIBLE);
                lnr_subscription_selected.setVisibility(View.GONE);

                lnr_account.setVisibility(View.VISIBLE);
                lnr_account_selected.setVisibility(View.GONE);
                startActivity(new Intent(TodayOffersActivity.this, HomeActivity.class));

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

                startActivity(new Intent(TodayOffersActivity.this, OffersActivity.class));
//                finish();
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
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(TodayOffersActivity.this);
                    // Setting Dialog Title
                    alertDialog.setTitle("Please Login");
                    // Setting Dialog Message
                    alertDialog.setMessage("Please login to view cart");
                    // On pressing Settings button
                    alertDialog.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(TodayOffersActivity.this, LoginActivity.class);
                            intent.putExtra("from", "selling_product");
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
                    startActivity(new Intent(TodayOffersActivity.this, CartActivity.class));
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
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(TodayOffersActivity.this);
                    // Setting Dialog Title
                    alertDialog.setTitle("Please Login");
                    // Setting Dialog Message
                    alertDialog.setMessage("Please login to view subscription");
                    // On pressing Settings button
                    alertDialog.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(TodayOffersActivity.this, LoginActivity.class);
                            intent.putExtra("from", "selling_product");
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
                    startActivity(new Intent(TodayOffersActivity.this, SubscriptionActivity.class));
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

                startActivity(new Intent(TodayOffersActivity.this, CategoriesActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
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

        shimmer_view_container.startShimmerAnimation();
        lnr_main.setVisibility(View.GONE);
        shimmer_view_container.setVisibility(View.VISIBLE);
        cartCount();
        getBestSelling();
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
        shimmer_view_container.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }*/
        shimmer_view_container.stopShimmerAnimation();
        lnr_main.setVisibility(View.VISIBLE);
        shimmer_view_container.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        shimmer_view_container.stopShimmerAnimation();
        lnr_main.setVisibility(View.VISIBLE);
        shimmer_view_container.setVisibility(View.GONE);
    }

    public void addSubscription(View view) {
        card_mySubscription.setVisibility(View.GONE);
        lin_day.setVisibility(View.GONE);
        startActivity(new Intent(TodayOffersActivity.this, SubscriptionActivity.class));
    }

    public void account(View view) {
        startActivity(new Intent(TodayOffersActivity.this, AccountActivity.class));
    }

    public void getBestSelling() {
//        dialog.show();
        shimmer_view_container.startShimmerAnimation();
        rec_list.setVisibility(View.GONE);
        shimmer_view_container.setVisibility(View.VISIBLE);

        if (APIURLs.isNetwork(TodayOffersActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.today_offer, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                            final String start_time = jsonObject1.getString("start_time");
                            final String end_time = jsonObject1.getString("end_time");
                            Thread myThread = null;
                            Runnable myRunnableThread = new CountDownRunner(end_time);
                            myThread= new Thread(myRunnableThread);
                            myThread.start();

                            productModelArrayList = new ArrayList<>();
                            JSONArray jsonArray1 = jsonObject1.getJSONArray("product_list");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                                ProductModel model = new ProductModel();
                                model.setProduct_id(jsonObject2.getString("product_id"));
                                model.setProduct_name(jsonObject2.getString("product_name"));
                                model.setImage(jsonObject2.getString("image"));
                                model.setFlag(jsonObject2.getString("flag"));
                                model.setQty(jsonObject2.getString("qty"));
                                model.setType(jsonObject2.getString("type"));

                                JSONArray jsonArray2 = jsonObject2.getJSONArray("price_details");
                                JSONObject jsonObject3 = jsonArray2.getJSONObject(0);
                                model.setUnit(jsonObject3.getString("unit"));
                                model.setPrice(jsonObject3.getString("price"));
                                model.setDiscount(jsonObject3.getString("discount"));

                                productModelArrayList.add(model);
                            }
                            ProductAdapter adapter = new ProductAdapter(TodayOffersActivity.this);
                            rec_list.setAdapter(adapter);
                            rec_list.setVisibility(View.VISIBLE);
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
                    params.put("customer_id",cost_id);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            final Dialog dialog = new Dialog(TodayOffersActivity.this);
            dialog.setContentView(R.layout.no_internet_dialog);
            Button button = dialog.findViewById(R.id.btn_process);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getBestSelling();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public void doWork(final String endtime) {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                try
                {
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss aa");

                    Date systemDate = Calendar.getInstance().getTime();
                    String myDate = sdf.format(systemDate);
//                  txtCurrentTime.setText(myDate);

                    Date Date1 = sdf.parse(myDate);
                    Date Date2 = sdf.parse(endtime);

                    long millse = Date1.getTime() - Date2.getTime();
                    long mills = Math.abs(millse);

                    int Hours = (int) (mills/(1000 * 60 * 60));
                    int Mins = (int) (mills/(1000*60)) % 60;
                    long Secs = (int) (mills / 1000) % 60;

                    String diff = Hours + ":" + Mins + ":" + Secs; // updated value every1 second
                    txt_timer.setText(diff +" Remaining");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    class CountDownRunner implements Runnable {
        String endTime;
        public CountDownRunner(String endTime) {
            this.endTime = endTime;
        }

        // @Override
        public void run()
        {
            while(!Thread.currentThread().isInterrupted())
            {
                try
                {
                    doWork(endTime);
                    Thread.sleep(1000); // Pause of 1 Second
                }
                catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                }
                catch(Exception e)
                {
                }
            }
        }
    }

    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
        Context context;
        Double actualAmout;
        Double discount;
        Double payableAmt;

        public ProductAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(TodayOffersActivity.this).inflate(R.layout.search_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            ProductModel model = productModelArrayList.get(position);
            ((ViewHolder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return productModelArrayList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_product;
            TextView txt_productName, txt_weight, txt_season, txt_actualprice, txt_stock, txt_discountprice, txt_discount, txt_subscribecount;
            Button btn_cart, btn_subscribe;
            LinearLayout lnr_product;
            RelativeLayout rel_subs, rel_stock;
            RelativeLayout rel_discount, rel_cart, rel_minus, rel_plus, rel_subscribe, rel_subscribeminus, rel_subscribeplus;
            RelativeLayout rel_cartmain;
            ProgressBar progressBar;
            TextView txt_count;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                img_product = itemView.findViewById(R.id.img_product);
                txt_productName = itemView.findViewById(R.id.txt_productName);
                txt_weight = itemView.findViewById(R.id.txt_weight);
                txt_season = itemView.findViewById(R.id.txt_season);
                txt_actualprice = itemView.findViewById(R.id.txt_actualprice);
                txt_discountprice = itemView.findViewById(R.id.txt_discountprice);
                txt_stock = itemView.findViewById(R.id.txt_stock);
                rel_stock = itemView.findViewById(R.id.rel_stock);
                btn_cart = itemView.findViewById(R.id.btn_cart);
                btn_subscribe = itemView.findViewById(R.id.btn_subscribe);
                lnr_product = itemView.findViewById(R.id.lnr_product);
                rel_discount = itemView.findViewById(R.id.rel_discount);
                rel_minus = itemView.findViewById(R.id.rel_minus);
                rel_plus = itemView.findViewById(R.id.rel_plus);
                rel_subs = itemView.findViewById(R.id.rel_subs);
                rel_subscribe = itemView.findViewById(R.id.rel_subscribe);
                rel_subscribeminus = itemView.findViewById(R.id.rel_subscribeminus);
                rel_subscribeplus = itemView.findViewById(R.id.rel_subscribeplus);
                rel_cart = itemView.findViewById(R.id.rel_cart);
                txt_discount = itemView.findViewById(R.id.txt_discount);
                txt_count = itemView.findViewById(R.id.txt_count);
                progressBar = itemView.findViewById(R.id.progressBar);
                rel_cartmain = itemView.findViewById(R.id.rel_cartmain);
                txt_subscribecount = itemView.findViewById(R.id.txt_subscribecount);
                txt_actualprice.setPaintFlags(txt_actualprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            public void bind(final ProductModel model, final int position) {
                Glide.with(TodayOffersActivity.this).
                        load(model.getImage()).error(R.drawable.loader).
                        placeholder(R.drawable.loader).into(img_product);
//                if (model.getProduct_name().length() <= 15) {
                txt_productName.setText(model.getProduct_name());
                /*} else {
                    String pname = model.getProduct_name();
                    pname = pname.substring(0, 15);
                    txt_productName.setText(pname + "...");
                }*/
                txt_weight.setText(model.getUnit());
                try {
                    if (model.getDiscount().equals("0")) {
                        txt_discountprice.setText("Rs." + model.getPrice());
                        txt_actualprice.setVisibility(View.GONE);
                        rel_discount.setVisibility(View.GONE);
                    } else {
                        txt_actualprice.setVisibility(View.VISIBLE);
                        rel_discount.setVisibility(View.VISIBLE);
                        actualAmout = Double.parseDouble(model.getPrice());
                       /* discount = (actualAmout * Double.parseDouble(model.getDiscount())) / 100;
                        payableAmt = actualAmout - discount;
                        DecimalFormat twoDForm = new DecimalFormat("#");
                        payableAmt = Double.valueOf(twoDForm.format(payableAmt));

                        txt_discountprice.setText("Rs." + (int) Math.round(payableAmt));
                        txt_actualprice.setText("Rs." + model.getPrice());
                        txt_discount.setText(model.getDiscount() + "% off");*/

                        payableAmt = actualAmout - Double.parseDouble(model.getDiscount());
                        long finalpayableAmtrounded = Math.round(payableAmt);

                        txt_discountprice.setText("Rs." + finalpayableAmtrounded);
                        txt_actualprice.setText("Rs." + model.getPrice());
                        txt_discount.setText("Save\n\u20B9" + model.getDiscount());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (model.getFlag().equals("0")) {
                    rel_stock.setVisibility(View.VISIBLE);
                } else if (model.getFlag().equals("1")) {
                    rel_stock.setVisibility(View.GONE);
                }

                if (model.getType().equals("0")) {
                    rel_subs.setVisibility(GONE);
                    rel_cartmain.setVisibility(View.VISIBLE);
                } else {
                    rel_subs.setVisibility(View.VISIBLE);
                    rel_cartmain.setVisibility(GONE);
                }

                if (model.getQty().equals("0")) {
                    rel_cart.setVisibility(View.GONE);
                    btn_cart.setVisibility(View.VISIBLE);
                } else {
                    rel_cart.setVisibility(View.VISIBLE);
                    btn_cart.setVisibility(View.GONE);
                    qty = Integer.valueOf(model.getQty());
                    txt_count.setText(String.valueOf(qty));
                }

                btn_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cost_id.equals("")) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(TodayOffersActivity.this);
                            // Setting Dialog Title
                            alertDialog.setTitle("Please Login");
                            // Setting Dialog Message
                            alertDialog.setMessage("Please login to add product in cart");
                            // On pressing Settings button
                            alertDialog.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(TodayOffersActivity.this, LoginActivity.class);
                                    intent.putExtra("from", "selling_product");
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
                                addCart(model.getProduct_id(), model.getUnit(), model.getPrice(),
                                        model.getDiscount(), txt_count.getText().toString(), position);
                            } else {
                                Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                btn_subscribe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cost_id.equals("")) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(TodayOffersActivity.this);
                            // Setting Dialog Title
                            alertDialog.setTitle("Please Login");
                            // Setting Dialog Message
                            alertDialog.setMessage("Please login for subscription");
                            // On pressing Settings button
                            alertDialog.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(TodayOffersActivity.this, LoginActivity.class);
                                    intent.putExtra("from", "offer_product");
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
                            qtymodel = new QuntityModel();
                            qtymodel.setId(model.getProduct_id());
                            qtymodel.setQty(txt_subscribecount.getText().toString());

                            qtyArrayList.add(qtymodel);
                            if (model.getFlag().equals("1")) {
                                rel_subscribe.setVisibility(View.VISIBLE);
                                btn_subscribe.setVisibility(View.GONE);
                                lin_day.setVisibility(View.VISIBLE);
                          /*  if (productId.size() > 0) {
                                for (int i = 0; i < productId.size(); i++) {
                                    if (productId.get(i).equals(model.getProduct_id())) {
                                        productId.remove(i);
                                    }
                                }
                            }
                            if (unitPriceArrayList.size() > 0) {
                                for (int i = 0; i < unitPriceArrayList.size(); i++) {
                                    if (unitPriceArrayList.get(i).equals(model.getPrice())) {
                                        unitPriceArrayList.remove(i);
                                    }
                                }
                            }
                            if (unitArrayList.size() > 0) {
                                for (int i = 0; i < unitArrayList.size(); i++) {
                                    if (unitArrayList.get(i).equals(model.getUnit())) {
                                        unitArrayList.remove(i);
                                    }
                                }
                            }
                            if (discountArrayList.size() > 0) {
                                for (int i = 0; i < discountArrayList.size(); i++) {
                                    if (discountArrayList.get(i).equals(model.getDiscount())) {
                                        discountArrayList.remove(i);
                                    }
                                }
                            }*/
                                productId.add(model.getProduct_id());
                                unitPriceArrayList.add(model.getPrice());
                                unitArrayList.add(model.getUnit());
                                discountArrayList.add(model.getDiscount());
                            } else {
                                Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
                            }
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
                                        txt_count.setVisibility(View.GONE);
                                        txt_count.setText(String.valueOf(qty));
                                        addCart(model.getProduct_id(), model.getUnit(), model.getPrice(),
                                                model.getDiscount(), txt_count.getText().toString(), position);
                                    }
                                    /*else if(qty==1){
                                        rel_cart.setVisibility(View.GONE);
                                        btn_cart.setVisibility(View.VISIBLE);
                                        removeCart(model.getProduct_id(),position);
                                    }*/
                                } else {
                                    rel_cart.setVisibility(View.GONE);
                                    btn_cart.setVisibility(View.VISIBLE);
                                    removeCart(model.getProduct_id(), position);
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
                            txt_count.setVisibility(View.GONE);
                            txt_count.setText(String.valueOf(qty));
                            addCart(model.getProduct_id(), model.getUnit(), model.getPrice(), model.getDiscount(),
                                    txt_count.getText().toString(), position);
                        } else {
                            Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                rel_subscribeminus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        subsQty = Integer.parseInt(txt_subscribecount.getText().toString());
                        if (model.getFlag().equals("1")) {
                            subsQty = Integer.parseInt(txt_subscribecount.getText().toString());
                            if (subsQty >= 1) {
                                subsQty--;
                                if (subsQty != 0) {
                                    if (subsQty > 1) {
                                        txt_subscribecount.setText(String.valueOf(subsQty));
                                    }
                                    txt_subscribecount.setText(String.valueOf(subsQty));
                                    for (int i = 0; i < qtyArrayList.size(); i++) {
                                        String id = qtyArrayList.get(i).getId();
                                        if (model.getProduct_id().equals(id)) {
                                            qtymodel = new QuntityModel();
                                            qtymodel.setId(id);
                                            qtymodel.setQty(String.valueOf(subsQty));
                                            qtyArrayList.remove(i);
                                            qtyArrayList.add(i, qtymodel);
                                        }
                                    }
                                } else {
                                    rel_subscribe.setVisibility(View.GONE);
                                    btn_subscribe.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < qtyArrayList.size(); i++) {
                                       /* if (position == i) {
                                            qtyArrayList.set(i, String.valueOf(subsQty));
                                        }*/
                                        String id = qtyArrayList.get(i).getId();
                                        if (model.getProduct_id().equals(id)) {
                                            qtymodel = new QuntityModel();
                                            qtymodel.setId(id);
                                            qtymodel.setQty(String.valueOf(subsQty));
                                            if (subsQty == 0) {
                                                qtyArrayList.remove(i);
                                                productId.remove(i);
                                                unitPriceArrayList.remove(i);
                                                unitArrayList.remove(i);
                                                discountArrayList.remove(i);
                                            } else {
                                                qtyArrayList.remove(i);
                                                qtyArrayList.add(i, qtymodel);
                                            }
                                        }
                                    }

                                    if (qtyArrayList.size() == 0) {
                                        lin_day.setVisibility(View.GONE);
                                        card_mySubscription.setVisibility(View.GONE);
                                        qtyArrayList = new ArrayList<>();
                                        productId = new ArrayList<>();
                                        unitPriceArrayList = new ArrayList<>();
                                        unitArrayList = new ArrayList<>();
                                        discountArrayList = new ArrayList<>();
                                    }

                                 /*   if (productId.size() > 0) {
                                        for (int i = 0; i < productId.size(); i++) {
                                            if (productId.get(i).equals(model.getProduct_id())) {
                                                productId.remove(i);
                                            }
                                        }
                                    }
                                    if (unitPriceArrayList.size() > 0) {
                                        for (int i = 0; i < unitPriceArrayList.size(); i++) {
                                            if (unitPriceArrayList.get(i).equals(model.getPrice())) {
                                                unitPriceArrayList.remove(i);
                                            }
                                        }
                                    }
                                    if (unitArrayList.size() > 0) {
                                        for (int i = 0; i < unitArrayList.size(); i++) {
                                            if (unitArrayList.get(i).equals(model.getUnit())) {
                                                unitArrayList.remove(i);
                                            }
                                        }
                                    }
                                    if (discountArrayList.size() > 0) {
                                        for (int i = 0; i < discountArrayList.size(); i++) {
                                            if (discountArrayList.get(i).equals(model.getDiscount())) {
                                                discountArrayList.remove(i);
                                            }
                                        }
                                    }
*/

                                }
                            }
                        } else {
                            Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                rel_subscribeplus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        subsQty = Integer.parseInt(txt_subscribecount.getText().toString());
                        if (model.getFlag().equals("1")) {
                            subsQty = Integer.parseInt(txt_subscribecount.getText().toString());
                            subsQty++;
                            txt_subscribecount.setText(String.valueOf(subsQty));
                            for (int i = 0; i < qtyArrayList.size(); i++) {
                               /* if (position == i) {
                                    qtyArrayList.set(i, String.valueOf(subsQty));
                                }*/
                                String id = qtyArrayList.get(i).getId();
                                if (model.getProduct_id().equals(id)) {
                                    qtymodel = new QuntityModel();
                                    qtymodel.setId(id);
                                    qtymodel.setQty(String.valueOf(subsQty));
                                    qtyArrayList.remove(i);
                                    qtyArrayList.add(i, qtymodel);
                                }
                            }
                        } else {
                            Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                lnr_product.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(new Intent(TodayOffersActivity.this, ProductDetailsActivity.class));
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                        clickEntry(model.getProduct_id());

                    }
                });

                /*btn_subscribe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (model.getFlag().equals("0")) {
                            Toast.makeText(context, "Out of stock", Toast.LENGTH_SHORT).show();
                        } else if (model.getFlag().equals("1")) {
                            btn_subscribe.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_button_layout));
                            btn_subscribe.setTextColor(getResources().getColor(R.color.colorWhite));

                        }
                    }
                });*/
            }

            public void removeCart(final String product_id, final int position) {
//                dialog.show();
                progressBar.setVisibility(View.VISIBLE);
                txt_count.setVisibility(View.GONE);
                if (APIURLs.isNetwork(TodayOffersActivity.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.remove_product_qty, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String status = jsonObject1.getString("status");
                                if (status.equals("1")) {
                                    qty = 0;
//                                    getProductList();
                                    Toast.makeText(TodayOffersActivity.this, "Item removed from cart successfully", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(TodayOffersActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
//                                dismissDialog();
                                progressBar.setVisibility(View.GONE);
                                txt_count.setVisibility(View.VISIBLE);
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
                            progressBar.setVisibility(View.GONE);
                            txt_count.setVisibility(View.VISIBLE);
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("product_id", product_id);
                            params.put("customer_id", SharedPref.getVal(TodayOffersActivity.this, SharedPref.customer_id));
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(TodayOffersActivity.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    dismissDialog();
                    FunctionConstant.noInternetDialog(TodayOffersActivity.this, "no internet connection");
                }
            }

            public void clickEntry(final String product_id) {
//                dialog.show();
                if (APIURLs.isNetwork(TodayOffersActivity.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.view_product_entry, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String status = jsonObject1.getString("status");
                                if (status.equals("1")) {
                                } else {
                                    Toast.makeText(TodayOffersActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                            params.put("product_id", product_id);
                            params.put("customer_id", SharedPref.getVal(TodayOffersActivity.this, SharedPref.customer_id));
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(TodayOffersActivity.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    dismissDialog();
                    FunctionConstant.noInternetDialog(TodayOffersActivity.this, "no internet connection");
                }
            }

            public void addCart(final String prod_id, final String unit, final String price, final String discount, final String qty1, final int position) {
//        dialog.show();
                progressBar.setVisibility(View.VISIBLE);
                txt_count.setVisibility(View.GONE);
                if (APIURLs.isNetwork(TodayOffersActivity.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.ADDCART_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String status = jsonObject1.getString("status");
                                if (status.equals("1")) {
                                    txt_count.setText(qty1);
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
                                    Toast.makeText(TodayOffersActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.GONE);
                                txt_count.setVisibility(View.VISIBLE);
//                        dismissDialog();
                            } catch (JSONException e) {
//                        dismissDialog();

                                progressBar.setVisibility(View.GONE);
                                txt_count.setVisibility(View.VISIBLE);
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                    dismissDialog();

                            progressBar.setVisibility(View.GONE);
                            txt_count.setVisibility(View.VISIBLE);
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("customer_id", SharedPref.getVal(TodayOffersActivity.this, SharedPref.customer_id));
                            params.put("product_id", prod_id);
                            params.put("unit", unit);
                            params.put("price", price);
                            params.put("discount", discount);
                            params.put("qty", qty1);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(TodayOffersActivity.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    dismissDialog();
                    FunctionConstant.noInternetDialog(TodayOffersActivity.this, "no internet connection");
                }
            }
        }
    }

    public void cartCount() {
        if (APIURLs.isNetwork(TodayOffersActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.CARTCOUNT_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            String count = jsonObject1.getString("count");
                            if (count.equals("0")) {
                                rel_cart_count.setVisibility(View.GONE);
                            } else {
                                rel_cart_count.setVisibility(View.VISIBLE);
                                txt_cart_count.setText(count);
                            }
                        } else {
                            Toast.makeText(TodayOffersActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                    params.put("customer_id", SharedPref.getVal(TodayOffersActivity.this, SharedPref.customer_id));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(TodayOffersActivity.this, "no internet connection");
        }
    }
}