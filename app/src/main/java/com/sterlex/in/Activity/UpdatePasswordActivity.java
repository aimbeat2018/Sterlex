package com.sterlex.in.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.gmail.samehadar.iosdialog.IOSDialog;
import com.sterlex.in.Constant.APIURLs;
import com.sterlex.in.Constant.SharedPref;
import com.sterlex.in.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class UpdatePasswordActivity extends AppCompatActivity {

    OtpTextView txt_otp;
    Button btn_process, btn_update_pwsd;
    String enter_otp = "";
    String mobile = "", city_id = "", state_id = "", otp = "", user_id = "", customer_fname = "", customer_lname = "", role_type = "", customer_email = "", customer_pincode = "", customer_phone = "", customer_password = "", referral_code = "", apply_rc = "", from = "";
    IOSDialog dialog;
    ImageView img_arrow;
    TextView txt_resend, txtv_otp, txtv_newpassword, txtv_confirmpassword;
    TextView txtMobileOtpTimer, txt_rsnd_otp;
    public int mobileCounter, mobileresentCounter;
    EditText edt_mobile,
            edt_otp, edt_new_password, edt_conf_password;
    RelativeLayout rel_resend;
    private static final String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        init();

        from = getIntent().getStringExtra("from");
        mobile = getIntent().getStringExtra("mobile");
        otp = getIntent().getStringExtra("otp");
        user_id = getIntent().getStringExtra("user_id");


        onClick();

        startMobileTimer(txtMobileOtpTimer);
    }


    private void init() {
        dialog = new IOSDialog.Builder(UpdatePasswordActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();
       /* dialog.create(UpdatePasswordActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait...")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.8f);*/

        rel_resend = findViewById(R.id.rel_resend);
        txt_rsnd_otp = findViewById(R.id.txt_rsnd_otp);
        btn_update_pwsd = findViewById(R.id.btn_update_pwsd);
        img_arrow = findViewById(R.id.img_arrow);
        txt_otp = findViewById(R.id.txt_otp);
        txtv_otp = findViewById(R.id.txtv_otp);
        txtv_newpassword = findViewById(R.id.txtv_newpassword);
        edt_new_password = findViewById(R.id.edt_new_password);
        txtv_confirmpassword = findViewById(R.id.txtv_confirmpassword);
        edt_conf_password = findViewById(R.id.edt_conf_password);
        btn_process = findViewById(R.id.btn_process);
        txt_resend = findViewById(R.id.txt_resend);
        txtMobileOtpTimer = findViewById(R.id.txtMobileOtpTimer);
        edt_mobile = findViewById(R.id.edt_mobile);
        edt_otp = findViewById(R.id.edt_otp);
    }


    private void onClick() {
        txt_rsnd_otp.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                SendOtp();
                    }
                });
        txt_otp.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(String otp) {
                enter_otp = otp;
            }
        });

        btn_process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_otp.getText().toString().equals("")) {
//                    ErrorToast("Please enter otp");
                    Toast.makeText(UpdatePasswordActivity.this, "Please enter otp", Toast.LENGTH_SHORT).show();
                } else if (!edt_otp.getText().toString().equals(otp)) {
//                    ErrorToast("Please valid enter otp");
                    Toast.makeText(UpdatePasswordActivity.this, "Please valid enter otp", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UpdatePasswordActivity.this, "Otp verified succesfully", Toast.LENGTH_SHORT).show();

                    txtv_otp.setVisibility(View.GONE);
                    edt_otp.setVisibility(View.GONE);
                    btn_process.setVisibility(View.GONE);
                    txtv_newpassword.setVisibility(View.VISIBLE);
                    edt_new_password.setVisibility(View.VISIBLE);
                    txtv_confirmpassword.setVisibility(View.VISIBLE);
                    edt_conf_password.setVisibility(View.VISIBLE);
                    btn_update_pwsd.setVisibility(View.VISIBLE);

                }
            }
        });
        btn_update_pwsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edt_new_password.getText().toString().equals("")) {
                    if (!edt_new_password.getText().toString().equals(edt_conf_password.getText().toString())) {
                        Toast.makeText(UpdatePasswordActivity.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                    } else {
                        updatePassword();
                    }
                } else {
                    Toast.makeText(UpdatePasswordActivity.this, "Enter New Password to Update", Toast.LENGTH_SHORT).show();
                }

            }
        });
//        txt_resend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SendOtp();
//            }
//        });

        img_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void startMobileTimer(final TextView text) {
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                rel_resend.setVisibility(View.GONE);
//                txtMobileOtpTimer.setVisibility(View.VISIBLE);
                text.setText("OTP can be resent after " + String.valueOf(millisUntilFinished / 1000) + " seconds");
//                mobileCounter++;
            }

            public void onFinish() {
//                mobileCounter = 0;
                rel_resend.setVisibility(View.VISIBLE);
//                txtMobileOtpTimer.setVisibility(View.GONE);
            }
        }.start();
    }


    public void SendOtp() {
        dialog.show();
        if (APIURLs.isNetwork(UpdatePasswordActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.reset_otp, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        String msg = jsonObject1.getString("msg");
                        if (status.equals("1")) {
                            otp = jsonObject1.getString("otp");
                        }  else {
//                            ErrorToast(msg);  //Show Error Toast
                            Toast.makeText(UpdatePasswordActivity.this, msg, Toast.LENGTH_SHORT).show();
//                            showDialog();
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
                    params.put("mobile_no", mobile);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            final Dialog dialog = new Dialog(UpdatePasswordActivity.this);
            dialog.setContentView(R.layout.no_internet_dialog);
            Button button = dialog.findViewById(R.id.btn_process);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SendOtp();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public void updatePassword() {
        dialog.show();
        if (APIURLs.isNetwork(UpdatePasswordActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.RESET_PASSWORD_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        if (status.equals("1")) {
//                            SuccessToast(jsonObject1.getString("msg")); //Success Toast
                            Toast.makeText(UpdatePasswordActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                            /*Save values in shared preference*/
                            SharedPref.putVal(UpdatePasswordActivity.this, SharedPref.customer_id, jsonObject1.getString("customer_id"));
                            SharedPref.putVal(UpdatePasswordActivity.this, SharedPref.customer_unique_id, jsonObject1.getString("customer_unique_id"));
                            SharedPref.putVal(UpdatePasswordActivity.this, SharedPref.mobile_no, jsonObject1.getString("mobile_no"));
                            SharedPref.putVal(UpdatePasswordActivity.this, SharedPref.email_id, jsonObject1.getString("email_id"));
                            SharedPref.putVal(UpdatePasswordActivity.this, SharedPref.customer_name, jsonObject1.getString("customer_name"));
                            SharedPref.putVal(UpdatePasswordActivity.this, SharedPref.token, jsonObject1.getString("token"));
                            SharedPref.putVal(UpdatePasswordActivity.this, SharedPref.referral_code, jsonObject1.getString("referral_code"));
//                            SharedPref.putVal(UpdatePasswordActivity.this, SharedPref.fran_code, jsonObject1.getString("franchise_id"));
                            SharedPref.putVal(UpdatePasswordActivity.this, SharedPref.version_code, jsonObject1.getString("version_code"));
                            SharedPref.putVal(UpdatePasswordActivity.this, SharedPref.customer_order_record, jsonObject1.getString("customer_order_record"));

                           /* if (from.equals("account")) {
                                startActivity(new Intent(UpdatePasswordActivity.this, AccountActivity.class));
                                finish();
                            } else if (from.equals("cart")) {
                                startActivity(new Intent(UpdatePasswordActivity.this, CartActivity.class));
                                finish();
                            } else if (from.equals("subs_cart")) {
                                startActivity(new Intent(UpdatePasswordActivity.this, SubscriptionCartActivity.class));
                                finish();
                            } else if (from.equals("home")) {
                                startActivity(new Intent(UpdatePasswordActivity.this, HomeActivity.class));
                                finish();
                            } else if (from.equals("sub_cat")) {
                                startActivity(new Intent(UpdatePasswordActivity.this, SubCategoryListingActivity.class));
                                finish();
                            } else if (from.equals("cat_product")) {
                                startActivity(new Intent(UpdatePasswordActivity.this, CategoryWiseProductListActivity.class));
                                finish();
                            } else if (from.equals("offer_product")) {
                                startActivity(new Intent(UpdatePasswordActivity.this, OffersProductListActivity.class));
                                finish();
                            } else if (from.equals("selling_product")) {
                                startActivity(new Intent(UpdatePasswordActivity.this, BestSellingProductActivity.class));
                                finish();
                            } else if (from.equals("product_details")) {
                                startActivity(new Intent(UpdatePasswordActivity.this, ProductDetailsActivity.class));
                                finish();
                            } else {*/
                            startActivity(new Intent(UpdatePasswordActivity.this, HomeActivity2.class));
                            finish();
//                            }
                        } else {
                            Toast.makeText(UpdatePasswordActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
//                            ErrorToast(jsonObject1.getString("msg")); //Error Toast
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
                    params.put("password", edt_new_password.getText().toString());
                    params.put("cust_id", user_id);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            final Dialog dialog = new Dialog(UpdatePasswordActivity.this);
            dialog.setContentView(R.layout.no_internet_dialog);
            Button button = dialog.findViewById(R.id.btn_process);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updatePassword();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
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
}