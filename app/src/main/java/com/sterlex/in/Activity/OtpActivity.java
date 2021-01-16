package com.sterlex.in.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.sterlex.in.Constant.APIURLs;
import com.sterlex.in.Constant.SharedPref;
import com.sterlex.in.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class OtpActivity extends AppCompatActivity {

    OtpTextView txt_otp;
    Button btn_process, btn_update_pwsd;
    String enter_otp = "";
    String mobile = "", city_id = "", state_id = "", otp = "", user_id = "", customer_fname = "", customer_lname = "", role_type = "", customer_email = "", customer_pincode = "", customer_phone = "", customer_password = "", referral_code = "", apply_rc = "", from = "";
    IOSDialog dialog;
    ImageView img_arrow;
    TextView txt_resend, txtv_otp, txtv_newpassword;
    TextView txtMobileOtpTimer, txt_rsnd_otp;
    public int mobileCounter, mobileresentCounter;
    EditText edt_mobile,
            edt_otp, edt_new_password;
    RelativeLayout rel_resend;
    private static final String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        init();

//        from = getIntent().getStringExtra("from");
//        mobile = getIntent().getStringExtra("mobile");
//        otp = getIntent().getStringExtra("otp");

//        if (from.equals("register")) {
//            city_id = getIntent().getStringExtra("city_id");
//            state_id = getIntent().getStringExtra("state_id");
//            user_id = getIntent().getStringExtra("user_id");
//            mobile = getIntent().getStringExtra("mobile");
//            customer_fname = getIntent().getStringExtra("customer_fname");
//            customer_lname = getIntent().getStringExtra("customer_lname");
//            customer_email = getIntent().getStringExtra("customer_email");
//            customer_pincode = getIntent().getStringExtra("customer_pincode");
//            customer_phone = getIntent().getStringExtra("customer_phone");
//            customer_password = getIntent().getStringExtra("customer_password");
//            referral_code = getIntent().getStringExtra("referral_code");
//            role_type = getIntent().getStringExtra("role_type");
//            apply_rc = getIntent().getStringExtra("apply_rc");
//            edt_mobile.setText(mobile);
//        }
        onClick();

        startMobileTimer(txtMobileOtpTimer);
    }

    private void onClick() {
        txt_rsnd_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOtp();
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
                if (edt_otp.getVisibility() == View.VISIBLE) {

                } else {

                    if (edt_mobile.getText().toString().equals("")) {
//                    ErrorToast("Please enter otp");
                        Toast.makeText(OtpActivity.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
                    } else if (!(edt_mobile.getText().length() == 10)) {
//                    ErrorToast("Please valid enter otp");
                        Toast.makeText(OtpActivity.this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
                    } else {
                        SendOtp();
                    }
                }
//                    Toast.makeText(OtpActivity.this, "Otp verified succesfully", Toast.LENGTH_SHORT).show();
////                    if (from.equals("register")) {
////                        registerUser();
////                    } else {
////                        txtv_otp.setVisibility(View.GONE);
////                        edt_otp.setVisibility(View.GONE);
////                        btn_process.setVisibility(View.GONE);
////                        txtv_newpassword.setVisibility(View.VISIBLE);
////                        edt_new_password.setVisibility(View.VISIBLE);
////                        btn_update_pwsd.setVisibility(View.VISIBLE);
////                    }
//                    Intent intent = new Intent(OtpActivity.this, RegisterActivity.class);
//                    startActivity(intent);
//                }
            }
        });
        btn_update_pwsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_otp.getText().toString().equals("")) {
                    Toast.makeText(OtpActivity.this, "Enter otp to proceed", Toast.LENGTH_SHORT).show();
                }else if(!edt_otp.getText().toString().equals(otp)){
                    Toast.makeText(OtpActivity.this, "Otp doesn't match", Toast.LENGTH_SHORT).show();
//                    updatePassword();
                } else {
                    Intent intent = new Intent(OtpActivity.this,RegisterActivity.class);
                    intent.putExtra("mobile",edt_mobile.getText().toString());
                    startActivity(intent);
                    finish();
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

    private void init() {
        dialog = new IOSDialog.Builder(OtpActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();
       /* dialog.create(OtpActivity.this)
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
        btn_process = findViewById(R.id.btn_process);
        txt_resend = findViewById(R.id.txt_resend);
        txtMobileOtpTimer = findViewById(R.id.txtMobileOtpTimer);
        edt_mobile = findViewById(R.id.edt_mobile);
        edt_otp = findViewById(R.id.edt_otp);
    }

    public void sendOtp() {
        dialog.show();
        if (APIURLs.isNetwork(OtpActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.SEND_OTP, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        String msg = jsonObject1.getString("msg");
                        if (status.equals("1")) {
//                            SuccessToast(msg);
                            Toast.makeText(OtpActivity.this, msg, Toast.LENGTH_SHORT).show();//Show Success Toast


                            startActivity(new Intent(OtpActivity.this, HomeActivity2.class));
                            finish();

                        }/* else if (status.equals("2")) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(OtpActivity.this);
                            // Setting Dialog Title
                            alertDialog.setTitle("Alert!!!");
                            // Setting Dialog Message
                            alertDialog.setMessage("You are blocked, \nPlease contact our team for further enquiry.");
                            // On pressing Settings button
                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(OtpActivity.this, HelpAndSupportActivity.class);
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
                        }*/ else {
//                            ErrorToast(msg);  //Show Error Toast
                            Toast.makeText(OtpActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
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
                    params.put("mobile_no", edt_mobile.getText().toString().trim());
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            final Dialog dialog = new Dialog(OtpActivity.this);
            dialog.setContentView(R.layout.no_internet_dialog);
            Button button = dialog.findViewById(R.id.btn_process);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendOtp();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public void SuccessToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.sucess_toast_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        TextView txt_message = (TextView) layout.findViewById(R.id.txt_message);
        txt_message.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 150);
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
        toast.setGravity(Gravity.BOTTOM, 0, 150);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
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

    public void registerUser() {
        dialog.show();
        if (APIURLs.isNetwork(OtpActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.REGISTRATION_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("user_data");
                        if (status.equals("1")) {
                            Toast.makeText(OtpActivity.this, "your account has been successfully created", Toast.LENGTH_SHORT).show();
                            String cost_id = jsonObject1.getString("user_id");
                            String version_code = jsonObject1.getString("version_code");
                            String customer_order_record = jsonObject1.getString("customer_order_record");
                            SharedPref.putVal(OtpActivity.this, SharedPref.customer_id, cost_id);

//                            String customer_id = jsonObject2.getString("customer_id");
                            String customer_unique_id = jsonObject2.getString("customer_unique_id");
                            String mobile_no = jsonObject2.getString("mobile_no");
                            String email_id = jsonObject2.getString("email_id");
                            String referral_code = jsonObject2.getString("my_refral_code");
                            String customer_name = jsonObject2.getString("first_name") + " " + jsonObject2.getString("last_name");
                            String token = jsonObject2.getString("token");
                            String state_id = jsonObject2.getString("state_id");
                            String city_id = jsonObject2.getString("city_id");
                            String pincode = jsonObject2.getString("pincode");
//                            String franchise_id = jsonObject2.getString("franchise_id");

                            SharedPref.putVal(OtpActivity.this, SharedPref.customer_id, cost_id);
                            SharedPref.putVal(OtpActivity.this, SharedPref.customer_unique_id, customer_unique_id);
                            SharedPref.putVal(OtpActivity.this, SharedPref.mobile_no, mobile_no);
                            SharedPref.putVal(OtpActivity.this, SharedPref.email_id, email_id);
                            SharedPref.putVal(OtpActivity.this, SharedPref.referral_code, referral_code);
                            SharedPref.putVal(OtpActivity.this, SharedPref.customer_name, customer_name);
                            SharedPref.putVal(OtpActivity.this, SharedPref.state_id, state_id);
                            SharedPref.putVal(OtpActivity.this, SharedPref.city_id, city_id);
                            SharedPref.putVal(OtpActivity.this, SharedPref.pincode, pincode);
                            SharedPref.putVal(OtpActivity.this, SharedPref.token, token);
//                            SharedPref.putVal(OtpActivity.this, SharedPref.fran_code, franchise_id);
                            SharedPref.putVal(OtpActivity.this, SharedPref.version_code, version_code);
                            SharedPref.putVal(OtpActivity.this, SharedPref.customer_order_record, customer_order_record);


//                            SendOtp();

                            Intent intent = new Intent(OtpActivity.this, HomeActivity2.class);
//                            intent.putExtra("mobile", from);
                            startActivity(intent);
                            finish();

                        }/* else if (status.equals("2")) {
                            Toast.makeText(OtpActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                        }*/ else {
//                            ErrorToast(jsonObject1.getString("msg")); // Error Toast
//                            showDialog(jsonObject1.getString("Mobile no already registered"));
                            Toast.makeText(OtpActivity.this, "Mobile no already registered", Toast.LENGTH_SHORT).show();
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
                    Random random = new Random();
                    String referralCode = generateReferralCode(random);
                    params.put("customer_fname", customer_fname);
                    params.put("customer_lname", customer_lname);
                    params.put("city_id", city_id);
                    params.put("state_id", state_id);
                    params.put("customer_email", customer_email);
                    params.put("customer_pincode", customer_pincode);
                    params.put("customer_phone", customer_phone);
                    params.put("customer_password", customer_password);
                    params.put("my_refral_code", referralCode);
                    params.put("role_type", role_type);
                    params.put("refral_code", apply_rc);

                    params.put("token", FirebaseInstanceId.getInstance().getToken());
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            final Dialog dialog = new Dialog(OtpActivity.this);
            dialog.setContentView(R.layout.no_internet_dialog);
            Button button = dialog.findViewById(R.id.btn_process);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    registerUser();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public void updatePassword() {
        dialog.show();
        if (APIURLs.isNetwork(OtpActivity.this)) {
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
                            Toast.makeText(OtpActivity.this, "Password updated succesfully", Toast.LENGTH_SHORT).show();
                            /*Save values in shared preference*/
                            SharedPref.putVal(OtpActivity.this, SharedPref.customer_id, jsonObject1.getString("customer_id"));
                            SharedPref.putVal(OtpActivity.this, SharedPref.customer_unique_id, jsonObject1.getString("customer_unique_id"));
                            SharedPref.putVal(OtpActivity.this, SharedPref.mobile_no, jsonObject1.getString("mobile_no"));
                            SharedPref.putVal(OtpActivity.this, SharedPref.email_id, jsonObject1.getString("email_id"));
                            SharedPref.putVal(OtpActivity.this, SharedPref.customer_name, jsonObject1.getString("customer_name"));
                            SharedPref.putVal(OtpActivity.this, SharedPref.token, jsonObject1.getString("token"));
                            SharedPref.putVal(OtpActivity.this, SharedPref.referral_code, jsonObject1.getString("referral_code"));
//                            SharedPref.putVal(OtpActivity.this, SharedPref.fran_code, jsonObject1.getString("franchise_id"));
                            SharedPref.putVal(OtpActivity.this, SharedPref.version_code, jsonObject1.getString("version_code"));
                            SharedPref.putVal(OtpActivity.this, SharedPref.customer_order_record, jsonObject1.getString("customer_order_record"));

                           /* if (from.equals("account")) {
                                startActivity(new Intent(OtpActivity.this, AccountActivity.class));
                                finish();
                            } else if (from.equals("cart")) {
                                startActivity(new Intent(OtpActivity.this, CartActivity.class));
                                finish();
                            } else if (from.equals("subs_cart")) {
                                startActivity(new Intent(OtpActivity.this, SubscriptionCartActivity.class));
                                finish();
                            } else if (from.equals("home")) {
                                startActivity(new Intent(OtpActivity.this, HomeActivity.class));
                                finish();
                            } else if (from.equals("sub_cat")) {
                                startActivity(new Intent(OtpActivity.this, SubCategoryListingActivity.class));
                                finish();
                            } else if (from.equals("cat_product")) {
                                startActivity(new Intent(OtpActivity.this, CategoryWiseProductListActivity.class));
                                finish();
                            } else if (from.equals("offer_product")) {
                                startActivity(new Intent(OtpActivity.this, OffersProductListActivity.class));
                                finish();
                            } else if (from.equals("selling_product")) {
                                startActivity(new Intent(OtpActivity.this, BestSellingProductActivity.class));
                                finish();
                            } else if (from.equals("product_details")) {
                                startActivity(new Intent(OtpActivity.this, ProductDetailsActivity.class));
                                finish();
                            } else {*/
                            startActivity(new Intent(OtpActivity.this, HomeActivity2.class));
                            finish();
//                            }
                        } else {
                            Toast.makeText(OtpActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
            final Dialog dialog = new Dialog(OtpActivity.this);
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


    public void SendOtp() {
        dialog.show();
        if (APIURLs.isNetwork(OtpActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.SEND_OTP, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        String msg = jsonObject1.getString("msg");
                        if (status.equals("1")) {
//                            SuccessToast(msg);
                            otp = jsonObject1.getString("otp");
                            btn_process.setVisibility(View.GONE);
                            txtv_otp .setVisibility(View.VISIBLE);
                            edt_otp.setVisibility(View.VISIBLE);
                            btn_update_pwsd.setVisibility(View.VISIBLE);
//                            Toast.makeText(OtpActivity.this, msg, Toast.LENGTH_SHORT).show();//Show Success Toast
//
//                            Intent intent = new Intent(OtpActivity.this, OtpActivity.class);
//                            intent.putExtra("mobile", edt_mobile.getText().toString());
//                            intent.putExtra("otp", jsonObject1.getString("otp"));
////                            intent.putExtra("from", from);
//                            startActivity(intent);
//                            finish();
                        } else {
//                            ErrorToast(msg);  //Show Error Toast
                            Toast.makeText(OtpActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
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
                    params.put("mobile_no", edt_mobile.getText().toString().trim());
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            final Dialog dialog = new Dialog(OtpActivity.this);
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

    private void startResendMobileTimer(final TextView text) {
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                txt_resend.setVisibility(View.GONE);
                txtMobileOtpTimer.setVisibility(View.VISIBLE);
                text.setText("OTP can be resent after " + String.valueOf(millisUntilFinished / 1000) + " seconds");
//                mobileresentCounter++;
            }

            public void onFinish() {
//                mobileresentCounter = 0;
                txt_resend.setVisibility(View.VISIBLE);
                txtMobileOtpTimer.setVisibility(View.GONE);
            }
        }.start();
    }


    public static String generateReferralCode(Random r) {
        while (true) {
            char[] password = new char[r.nextBoolean() ? 8 : 8];
            boolean hasUpper = false, hasDigit = false;
            for (int i = 0; i < password.length; i++) {
                char ch = symbols.charAt(r.nextInt(symbols.length()));
                if (Character.isUpperCase(ch))
                    hasUpper = true;
                else if (Character.isDigit(ch))
                    hasDigit = true;
                password[i] = ch;
            }
            if (hasUpper && hasDigit) {
                return new String(password);
            }
        }
    }

}
