package com.sterlex.in.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gmail.samehadar.iosdialog.IOSDialog;
import com.sterlex.in.Constant.APIURLs;
import com.sterlex.in.Constant.FunctionConstant;
import com.sterlex.in.Constant.OrderSharedPrefence;
import com.sterlex.in.Constant.SharedPref;
import com.sterlex.in.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewAddressAddingActivity extends AppCompatActivity {

    ImageView img_back, img_right, img_wrong;
    EditText edt_name, edt_address, edt_landmark, edt_pincode, edt_area, edt_city, edt_state, edt_mobile;
    RelativeLayout rel_check;
    IOSDialog dialog;
    Button btn_submit;
    String from = "", del_charges = "", sect = "NA", address_type = "House", address = "";
    String from_date = "", to_date = "", subscribe_mode = "", product_id = "", qty = "", day = "", unit_price = "",
            unit = "", discount = "", finalUnit = "", finalUnitPrice = "", finalDiscount = "", price = "",
            total_bag = "", subscription_days = "", bag_discount = "", delivery_charges = "";
    String grand_total = "", sub_total = "", coupon_id = "", slot_id = "", discount_amt = "", coupon_amt = "", walletBalance = "", actualBalance = "";
    private static final int REQUEST_OPERATORR = 2;
    String ssityid = "NA", ssityname, scityid, scityname;
    TextView txt_home, txt_office, txt_other, txt_name_error, txt_addr_error, txt_landmark_error, txt_pincode_error, txt_area_error, txt_city_error, txt_state_error,
            txt_mobile_error;
    String cost_id = "", mobile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);
        init();
        cost_id = SharedPref.getVal(NewAddressAddingActivity.this, SharedPref.customer_id);
        mobile = SharedPref.getVal(NewAddressAddingActivity.this, SharedPref.mobile_no);
        edt_mobile.setText(mobile);
        onClick();
        from = getIntent().getStringExtra("from");
        del_charges = getIntent().getStringExtra("del_charges");

//        if (from.equals("subs")) {
//            getIntentDataSubs();
//        } else {
//            getIntentDataCart();
//        }

    }

    private void getIntentDataSubs() {
        from_date = getIntent().getStringExtra("from_date");
        to_date = getIntent().getStringExtra("to_date");
        subscribe_mode = getIntent().getStringExtra("subscribe_mode");
        product_id = getIntent().getStringExtra("product_id");
        qty = getIntent().getStringExtra("qty");
        day = getIntent().getStringExtra("day");
        unit_price = getIntent().getStringExtra("unit_price");
        unit = getIntent().getStringExtra("unit");
        discount = getIntent().getStringExtra("discount");
        coupon_id = getIntent().getStringExtra("coupon_id");
        total_bag = getIntent().getStringExtra("total_bag");
        subscription_days = getIntent().getStringExtra("subscription_days");
        bag_discount = getIntent().getStringExtra("bag_discount");
        coupon_amt = getIntent().getStringExtra("coupon_amt");
        price = getIntent().getStringExtra("price");

    }

    private void getIntentDataCart() {
        product_id = getIntent().getStringExtra("product_id");
        grand_total = getIntent().getStringExtra("grand_total");
        sub_total = getIntent().getStringExtra("sub_total");
        coupon_id = getIntent().getStringExtra("coupon_id");
        slot_id = getIntent().getStringExtra("slot_id");
        unit_price = getIntent().getStringExtra("unit_price");
        discount_amt = getIntent().getStringExtra("discount_amt");
        coupon_amt = getIntent().getStringExtra("coupon_amt");
        qty = getIntent().getStringExtra("qty");
        finalUnit = getIntent().getStringExtra("finalUnit");
        finalUnitPrice = getIntent().getStringExtra("finalUnitPrice");
        finalDiscount = getIntent().getStringExtra("finalDiscount");
        walletBalance = getIntent().getStringExtra("walletBalance");
        actualBalance = getIntent().getStringExtra("actualBalance");
        delivery_charges = getIntent().getStringExtra("delivery_charges");
    }

    private void onClick() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txt_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_home.setBackground(getResources().getDrawable(R.drawable.blue_main_layout));
                txt_home.setTextColor(getResources().getColor(R.color.colorWhite));

                txt_office.setBackground(getResources().getDrawable(R.drawable.shimmer_back));
                txt_office.setTextColor(getResources().getColor(R.color.colorBlack));
                txt_other.setBackground(getResources().getDrawable(R.drawable.shimmer_back));
                txt_other.setTextColor(getResources().getColor(R.color.colorBlack));
                address_type = "House";

            }
        });

        txt_office.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_office.setBackground(getResources().getDrawable(R.drawable.blue_main_layout));
                txt_office.setTextColor(getResources().getColor(R.color.colorWhite));

                txt_home.setBackground(getResources().getDrawable(R.drawable.shimmer_back));
                txt_home.setTextColor(getResources().getColor(R.color.colorBlack));
                txt_other.setBackground(getResources().getDrawable(R.drawable.shimmer_back));
                txt_other.setTextColor(getResources().getColor(R.color.colorBlack));
                address_type = "Office";
            }
        });

        txt_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_other.setBackground(getResources().getDrawable(R.drawable.blue_main_layout));
                txt_other.setTextColor(getResources().getColor(R.color.colorWhite));

                txt_office.setBackground(getResources().getDrawable(R.drawable.shimmer_back));
                txt_office.setTextColor(getResources().getColor(R.color.colorBlack));
                txt_home.setBackground(getResources().getDrawable(R.drawable.shimmer_back));
                txt_home.setTextColor(getResources().getColor(R.color.colorBlack));
                address_type = "Other";
            }
        });

        edt_pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edt_pincode.getText().length() == 6) {
                    /*if (edt_pincode.getText().toString().equals("400614") || edt_pincode.getText().toString().equals("400705") ||
                            edt_pincode.getText().toString().equals("400706") || edt_pincode.getText().toString().equals("410208") || edt_pincode.getText().toString().equals("410210")) {
                       *//* txt_address.setText(add);*//*
                    } else {
                        new AlertDialog.Builder(NewAddressAddingActivity.this)
                                .setTitle("Service unavailable")
                                .setMessage("Currently we are not provide service in your area.")

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Continue with delete operation
                                        dialog.dismiss();
                                        edt_pincode.setText("");
                                    }
                                })
                                .show();
                    }*/
                    checkPincode(edt_pincode.getText().toString());

                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_name.getText().toString().length() < 3) {
                    txt_name_error.setText("Enter at least 3 characters");
                    edt_name.requestFocus();
                } else if (edt_address.getText().toString().equals("")) {
                    txt_addr_error.setText("Address cannot be empty");
                    txt_name_error.setVisibility(View.GONE);
                    edt_address.requestFocus();
                } else if (edt_pincode.getText().toString().equals("")) {
                    txt_pincode_error.setText("Please enter 6 digit pincode");
                    txt_name_error.setVisibility(View.GONE);
                    txt_addr_error.setVisibility(View.GONE);
                    edt_pincode.requestFocus();
                } else if (edt_area.getText().toString().equals("")) {
                    txt_area_error.setText("Please enter area");
                    txt_name_error.setVisibility(View.GONE);
                    txt_addr_error.setVisibility(View.GONE);
                    txt_pincode_error.setVisibility(View.GONE);
                    edt_area.requestFocus();
                } else if (edt_city.getText().toString().equals("")) {
                    txt_city_error.setText("Please enter city");
                    txt_name_error.setVisibility(View.GONE);
                    txt_addr_error.setVisibility(View.GONE);
                    txt_pincode_error.setVisibility(View.GONE);
                    txt_area_error.setVisibility(View.GONE);
                    edt_city.requestFocus();
                } else if (edt_state.getText().toString().equals("")) {
                    txt_state_error.setText("Please enter state");
                    txt_city_error.setVisibility(View.GONE);
                    txt_name_error.setVisibility(View.GONE);
                    txt_addr_error.setVisibility(View.GONE);
                    txt_pincode_error.setVisibility(View.GONE);
                    txt_area_error.setVisibility(View.GONE);
                    edt_state.requestFocus();
                } else {

                    txt_state_error.setVisibility(View.GONE);
                    txt_city_error.setVisibility(View.GONE);
                    txt_name_error.setVisibility(View.GONE);
                    txt_addr_error.setVisibility(View.GONE);
                    txt_pincode_error.setVisibility(View.GONE);
                    txt_area_error.setVisibility(View.GONE);
//                    if (edt_address.getText().toString().isEmpty() || edt_address.getText().toString().equals(null) || edt_address.getText().toString().equals("null") || edt_address.getText().toString().equals(null)) {
//                        sect = "NA";
                    if (!edt_landmark.getText().toString().equals("")) {
                        address = edt_address.getText().toString() + ", " + edt_landmark.getText().toString() + ", " +
                                edt_area.getText().toString() + ", " + edt_city.getText().toString() + ", " + edt_state.getText().toString() + " - " + edt_pincode.getText().toString();
                    } else {
                        address = edt_address.getText().toString() + ", " +
                                edt_area.getText().toString() + ", " + edt_city.getText().toString() + ", " + edt_state.getText().toString() + " - " + edt_pincode.getText().toString();

                    }
                     /*}else {
//                        sect = edt_sector.getText().toString();

                        address = address_type + "\n" + edt_flatno.getText().toString() + " " +
                                edt_bldgName.getText().toString() + " " + edt_landmark.getText().toString()
                                + " " + edt_area.getText().toString() + "," + sect + "," + edt_pincode.getText().toString();
                    }*/
                    addNewAddress();
                }
            }
        });
    }

    private void init() {
        dialog = new IOSDialog.Builder(NewAddressAddingActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();
        img_back = findViewById(R.id.img_back);
        txt_home = findViewById(R.id.txt_home);
        txt_office = findViewById(R.id.txt_office);
        txt_other = findViewById(R.id.txt_other);
        edt_name = findViewById(R.id.edt_name);
        edt_address = findViewById(R.id.edt_address);
        edt_landmark = findViewById(R.id.edt_landmark);
        edt_pincode = findViewById(R.id.edt_pincode);
        edt_area = findViewById(R.id.edt_area);
        edt_city = findViewById(R.id.edt_city);
        edt_state = findViewById(R.id.edt_state);
        edt_mobile = findViewById(R.id.edt_mobile);
        btn_submit = findViewById(R.id.btn_submit);
        rel_check = findViewById(R.id.rel_check);
        img_right = findViewById(R.id.img_right);
        img_wrong = findViewById(R.id.img_wrong);

        txt_name_error = findViewById(R.id.txt_name_error);
        txt_addr_error = findViewById(R.id.txt_addr_error);
        txt_landmark_error = findViewById(R.id.txt_landmark_error);
        txt_pincode_error = findViewById(R.id.txt_pincode_error);
        txt_area_error = findViewById(R.id.txt_area_error);
        txt_city_error = findViewById(R.id.txt_city_error);
        txt_state_error = findViewById(R.id.txt_state_error);
        txt_mobile_error = findViewById(R.id.txt_mobile_error);
    }

    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void addNewAddress() {
        dialog.show();
        if (APIURLs.isNetwork(NewAddressAddingActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.customer_address, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
//                        JSONArray jsonArray = jsonObject.getJSONArray("result");
//                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
//                            Toast.makeText(NewAddressAddingActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
//                            if (from.equals("subs")) {
//                                Intent intent = new Intent(NewAddressAddingActivity.this, SubscriptionPaymentModeActivity.class);
//                                intent.putExtra("from_date", from_date);
//                                intent.putExtra("to_date", to_date);
//                                intent.putExtra("subscribe_mode", subscribe_mode);
//                                intent.putExtra("product_id", product_id);
//                                intent.putExtra("day", day);
//                                intent.putExtra("unit_price", unit_price);
//                                intent.putExtra("unit", unit);
//                                intent.putExtra("discount", discount);
//                                intent.putExtra("qty", qty);
//                                intent.putExtra("coupon_id", coupon_id);
//                                intent.putExtra("total_bag", total_bag);
//                                intent.putExtra("subscription_days", subscription_days);
//                                intent.putExtra("address", address);
//                                intent.putExtra("bag_discount", bag_discount);
//                                intent.putExtra("coupon_amt", coupon_amt);
//                                intent.putExtra("price", price);
//                                startActivity(intent);
//                                finish();
//                            } else {
//                                Intent intent = new Intent(NewAddressAddingActivity.this, SelectPaymentModeActivity.class);
//                                intent.putExtra("product_id", product_id);
//                                intent.putExtra("grand_total", grand_total);
//                                intent.putExtra("sub_total", sub_total);
//                                intent.putExtra("coupon_id", coupon_id);
//                                intent.putExtra("slot_id", slot_id);
//                                intent.putExtra("unit_price", unit_price);
//                                intent.putExtra("discount_amt", discount_amt);
//                                intent.putExtra("coupon_amt", coupon_amt);
//                                intent.putExtra("qty", qty);
//                                intent.putExtra("address", address);
//                                intent.putExtra("area_id", edt_pincode.getText().toString());
//                                intent.putExtra("finalUnit", finalUnit);
//                                intent.putExtra("finalUnitPrice", finalUnitPrice);
//                                intent.putExtra("finalDiscount", finalDiscount);
//                                intent.putExtra("delivery_charges", delivery_charges);
//                                intent.putExtra("actualBalance", actualBalance);
//                                intent.putExtra("walletBalance", walletBalance);
//                                intent.putExtra("delivery_status", "0");

//                                startActivity(intent);
//                                finish();
//                            }
//                            finish();
                            OrderSharedPrefence.putVal(NewAddressAddingActivity.this, address, OrderSharedPrefence.deliver_address);

                            Intent intent = new Intent(NewAddressAddingActivity.this, DeliveryAddressActivity.class);
                            intent.putExtra("addr", address);
                            intent.putExtra("from", from);
                            intent.putExtra("del_charges", del_charges);
                            startActivity(intent);
                            finish();


                        } else {
                            Toast.makeText(NewAddressAddingActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                    params.put("customer_id", SharedPref.getVal(NewAddressAddingActivity.this, SharedPref.customer_id));
                    params.put("address", edt_address.getText().toString());
                    params.put("landmark", edt_landmark.getText().toString());
                    params.put("pincode", edt_pincode.getText().toString());
                    params.put("area_name", edt_area.getText().toString());
                    params.put("city", edt_city.getText().toString());
                    params.put("state", edt_state.getText().toString());
                    params.put("add_type", "");
                    params.put("fullname", edt_name.getText().toString());
                    params.put("mobile", edt_mobile.getText().toString());
                    return params;

                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(NewAddressAddingActivity.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(NewAddressAddingActivity.this, "no internet connection");
        }
    }

    public void checkPincode(final String pincode) {
        dialog.show();
        if (APIURLs.isNetwork(NewAddressAddingActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.check_pincode, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        if (status.equals("0")) {
                            rel_check.setVisibility(View.VISIBLE);
                            img_right.setVisibility(View.GONE);
                            img_wrong.setVisibility(View.VISIBLE);
                           /* new AlertDialog.Builder(NewAddressAddingActivity.this)
                                    .setTitle("Service unavailable")
                                    .setMessage("Currently we are not available in your area.")

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Continue with delete operation
                                            dialog.dismiss();
                                            edt_pincode.setText("");
                                        }
                                    })
                                    .show();*/
                            Toast.makeText(NewAddressAddingActivity.this, "Currently we are not available in your area.", Toast.LENGTH_SHORT).show();
                        } else {
                            rel_check.setVisibility(View.VISIBLE);
                            img_right.setVisibility(View.VISIBLE);
                            img_wrong.setVisibility(View.GONE);
                           /* new AlertDialog.Builder(NewAddressAddingActivity.this)
                                    .setTitle("Service available")
                                    .setMessage("We are available in your area.")

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Continue with delete operation
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();*/
                            Toast.makeText(NewAddressAddingActivity.this, "We are available in your area.", Toast.LENGTH_SHORT).show();
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
            final Dialog dialog = new Dialog(NewAddressAddingActivity.this);
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

    @Override
    public void onBackPressed() {
        if (from.equals("cart")) {
            Intent intent = new Intent(NewAddressAddingActivity.this, AddressActivity.class);
            intent.putExtra("product_id", product_id);
            intent.putExtra("grand_total", grand_total);
            intent.putExtra("sub_total", sub_total);
            intent.putExtra("coupon_id", coupon_id);
            intent.putExtra("slot_id", slot_id);
            intent.putExtra("unit_price", unit_price);
            intent.putExtra("discount_amt", discount_amt);
            intent.putExtra("coupon_amt", coupon_amt);
            intent.putExtra("qty", qty);
            intent.putExtra("finalUnit", finalUnit);
            intent.putExtra("finalUnitPrice", finalUnitPrice);
            intent.putExtra("finalDiscount", finalDiscount);
            intent.putExtra("walletBalance", walletBalance);
            intent.putExtra("actualBalance", actualBalance);
            intent.putExtra("delivery_charges", delivery_charges);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(NewAddressAddingActivity.this, SubscriptionAddressActivity.class);
            intent.putExtra("from", "subs");
            intent.putExtra("from_date", from_date);
            intent.putExtra("to_date", to_date);
            intent.putExtra("subscribe_mode", subscribe_mode);
            intent.putExtra("product_id", product_id);
            intent.putExtra("day", day);
            intent.putExtra("unit_price", unit_price);
            intent.putExtra("unit", unit);
            intent.putExtra("discount", discount);
            intent.putExtra("qty", qty);
            startActivity(intent);
            finish();
        }
    }

    public void city(View v) {
        Intent intent = new Intent(NewAddressAddingActivity.this, CircleSelectionActivity2.class);
        startActivityForResult(intent, REQUEST_OPERATORR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_OPERATORR) {

            try {
                scityid = data.getStringExtra(CircleSelectionActivity2.RESULT_CIRCLCODE);
                scityname = data.getStringExtra(CircleSelectionActivity2.RESULT_CIRCLEID);

                edt_area.setText(scityid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
