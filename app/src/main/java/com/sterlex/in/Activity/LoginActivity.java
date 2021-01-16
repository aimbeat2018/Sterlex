package com.sterlex.in.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    EditText edt_mobile,edt_pswd;
    Button btn_process;
    TextView txt_frogotpswd;
    ImageView img_arrow, img_passwordToggle;
    CheckBox check_tnc;
    IOSDialog dialog;
    String from="login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
//        from = getIntent().getStringExtra("from");
        onClick();
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("HASKEY", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("HASKEY", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("HASKEY", "printHashKey()", e);
        }
    }


    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
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

    private void init() {
       /* dialog = new KProgressHUD(LoginActivity.this);
        dialog.create(LoginActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait...")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.8f);*/
        img_arrow = findViewById(R.id.img_arrow);

        dialog = new IOSDialog.Builder(LoginActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();
        txt_frogotpswd = findViewById(R.id.txt_frogotpswd);
        edt_mobile = findViewById(R.id.edt_mobile);
        edt_pswd = findViewById(R.id.edt_pswd);
        check_tnc = findViewById(R.id.check_tnc);
        btn_process = findViewById(R.id.btn_process);
        img_passwordToggle = findViewById(R.id.img_passwordToggle);

        txt_frogotpswd.setPaintFlags(txt_frogotpswd.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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

    public void onClick() {
        btn_process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_mobile.getText().toString().equals("")) {
                    edt_mobile.setError("Please enter mobile number");
                    edt_mobile.requestFocus();
                } else if (edt_mobile.getText().length() != 10) {
                    edt_mobile.setError("Please valid enter mobile number");
                    edt_mobile.requestFocus();
                }  else if (edt_pswd.getText().toString().equals("")) {
                    edt_pswd.setError("Please enter password");
                    edt_pswd.requestFocus();
                }/*else if (!check_tnc.isChecked()) {
//                    edt_mobile.setError("Please valid enter mobile number");
                    Toast.makeText(LoginActivity.this, "Please accept the Terms & Conditions", Toast.LENGTH_SHORT).show();
                }*/ else {
                    LoginUser();
                }
            }
        });
        txt_frogotpswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
        img_passwordToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_pswd.getTransformationMethod().getClass().getSimpleName().equals("PasswordTransformationMethod")) {
                    img_passwordToggle.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_visibility_24));
                    edt_pswd.setTransformationMethod(new SingleLineTransformationMethod());
                } else {
                    img_passwordToggle.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_visibility_off_24));
                    edt_pswd.setTransformationMethod(new PasswordTransformationMethod());
                }

                edt_pswd.setSelection(edt_pswd.getText().length());
            }
        });

        img_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void register(View view) {
        Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
        intent.putExtra("from", from);
        startActivity(intent);
    }

    public void forgot(View view) {
        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
    }

    public void showDialog() {
        final Dialog dialog1 = new Dialog(LoginActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog_layout);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog1.getWindow().setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog1.getWindow().setGravity(Gravity.CENTER);
//        dialog1.getWindow().setDimAmount((float) 0.8);

        ImageView img_close = dialog1.findViewById(R.id.img_close);
        TextView txt_here = dialog1.findViewById(R.id.txt_here);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

        txt_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                dialog1.dismiss();
            }
        });

        dialog1.show();
    }


    public void LoginUser() {
        dialog.show();
        if (APIURLs.isNetwork(LoginActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.LOGIN_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        if (status.equals("1")) {

                            String customer_id = jsonObject1.getString("customer_id");
                            String customer_unique_id = jsonObject1.getString("customer_unique_id");
                            String mobile_no = jsonObject1.getString("mobile_no");
                            String email_id = jsonObject1.getString("email_id");
//                            String referral_code = jsonObject1.getString("referral_code");
                            String customer_name = jsonObject1.getString("customer_name");
                            String token = jsonObject1.getString("token");
//                            String franchise_id = jsonObject1.getString("franchise_id");
                            String version_code = jsonObject1.getString("version_code");
                            String customer_order_record = jsonObject1.getString("customer_order_record");
                            SharedPref.putVal(LoginActivity.this,SharedPref.customer_id,customer_id);
                            SharedPref.putVal(LoginActivity.this,SharedPref.customer_unique_id,customer_unique_id);
                            SharedPref.putVal(LoginActivity.this,SharedPref.mobile_no,mobile_no);
                            SharedPref.putVal(LoginActivity.this,SharedPref.email_id,email_id);
//                            SharedPref.putVal(LoginActivity.this,SharedPref.referral_code,referral_code);
                            SharedPref.putVal(LoginActivity.this,SharedPref.customer_name,customer_name);
                            SharedPref.putVal(LoginActivity.this,SharedPref.token,token);
//                            SharedPref.putVal(LoginActivity.this,SharedPref.fran_code,franchise_id);
                            SharedPref.putVal(LoginActivity.this,SharedPref.version_code,version_code);
                            SharedPref.putVal(LoginActivity.this,SharedPref.customer_order_record,customer_order_record);

//                            SendOtp();

                            Intent intent = new Intent(LoginActivity.this, HomeActivity2.class);
//                            intent.putExtra("from", from);
                            startActivity(intent);
                            finish();

                        }/* else if (status.equals("2")) {
                            Toast.makeText(LoginActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                        }*/ else {
//                            ErrorToast(jsonObject1.getString("msg")); // Error Toast
//                            showDialog(jsonObject1.getString("Mobile no already registered"));
                            Toast.makeText(LoginActivity.this, "Mobile no and Password does not match", Toast.LENGTH_SHORT).show();
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
                    params.put("mobile_no", edt_mobile.getText().toString().trim());
                    params.put("password", edt_pswd.getText().toString().trim());
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            final Dialog dialog = new Dialog(LoginActivity.this);
            dialog.setContentView(R.layout.no_internet_dialog);
            Button button = dialog.findViewById(R.id.btn_process);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginUser();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }


    public void SendOtp() {
        dialog.show();
        if (APIURLs.isNetwork(LoginActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.LOGIN_OTP_URL, new Response.Listener<String>() {
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
                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();//Show Success Toast

                            Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
                            intent.putExtra("mobile", edt_mobile.getText().toString());
                            intent.putExtra("otp", jsonObject1.getString("otp"));
                            intent.putExtra("from", from);
                            startActivity(intent);
                            finish();
                        } else if (status.equals("2")) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
                            // Setting Dialog Title
                            alertDialog.setTitle("Alert!!!");
                            // Setting Dialog Message
                            alertDialog.setMessage("You are blocked, \nPlease contact our team for further enquiry.");
                            // On pressing Settings button
                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(LoginActivity.this, HelpAndSupportActivity.class);
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
//                            ErrorToast(msg);  //Show Error Toast
//                            Toast.makeText(LoginActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                            showDialog();
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
            final Dialog dialog = new Dialog(LoginActivity.this);
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

    public void terms(View view) {
        startActivity(new Intent(LoginActivity.this, PrivacyPolicyActivity.class));
    }
}
