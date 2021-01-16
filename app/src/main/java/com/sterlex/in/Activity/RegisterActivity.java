package com.sterlex.in.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.sterlex.in.Constant.FunctionConstant;
import com.sterlex.in.Constant.SharedPref;
import com.sterlex.in.R;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    String[] type = {"Select your role", "User", "Business"};

    RelativeLayout rel_check;
    EditText edt_fname, edt_lname, edt_email, edt_mobile, edt_pincode, edt_referralCode, statename, cityename, edt_pswd;
    Button btn_process;
    IOSDialog dialog;
    private static final String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    ImageView img_scanner, img_arrow, img_right, img_wrong;;
    final int QR_SCANNER = 1;
    private static final int REQUEST_OPERATOR = 1;
    private static final int REQUEST_OPERATORR = 2;
    String ssityid = "NA", ssityname = "", scityid = "", scityname = "";
    String mobile = "";
    Spinner spin, spinner_state, spinner_city;
    CheckBox check_tnc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        mobile = getIntent().getStringExtra("mobile");
        mobile = "9967578433";
        init();
        onClick();
        FunctionConstant.picRuntime(RegisterActivity.this);
        spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, type);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
//        spin.getSelectedItem();
//        Toast.makeText(getApplicationContext(),country[position] , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
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

    private void onClick() {

        img_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FunctionConstant.picRuntime(RegisterActivity.this)) {
                    startActivityForResult(new Intent(RegisterActivity.this, QrCodeScannerActivity.class), QR_SCANNER);
                } else {
                    Toast.makeText(RegisterActivity.this, "Camera Permission Required", Toast.LENGTH_SHORT).show();
                }
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

        img_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_pincode.getText().toString().trim().equals("")) {
                    edt_pincode.setError("Please enter pincode");
                    edt_pincode.requestFocus();
                } else if (edt_pincode.getText().toString().trim().length() != 6) {
                    edt_pincode.setError("Please enter valid pincode");
                    edt_pincode.requestFocus();
                } else if (edt_fname.getText().toString().equals("")) {
                    edt_fname.setError("Please enter name");
                    edt_fname.requestFocus();
                } else if (!isFullname(edt_fname.getText().toString())) {
                    edt_fname.setError("Please enter name");
                    edt_fname.requestFocus();
                } else if (edt_fname.getText().toString().length() < 3) {
                    edt_fname.setError("Name length should be greater than 3");
                    edt_fname.requestFocus();
                } else if (edt_lname.getText().toString().equals("")) {
                    edt_lname.setError("Please enter last name");
                    edt_lname.requestFocus();
                } /*else if (edt_mobile.getText().toString().equals("")) {
                    edt_mobile.setError("Please enter mobile number");
                    edt_mobile.requestFocus();
                } else if (edt_mobile.getText().length() != 10) {
                    edt_mobile.setError("Please valid enter mobile number");
                    edt_mobile.requestFocus();
                }*/ /*else if (edt_email.getText().toString().equals("")) {
                    edt_email.setError("Please enter email id");
                    edt_email.requestFocus();
                } else if (!FunctionConstant.isValidEmail(edt_email.getText().toString())) {
                    edt_email.setError("Please enter valid email id");
                    edt_email.requestFocus();
                } */ else if (spin.getSelectedItem().equals("Select your role")) {
                    Toast.makeText(RegisterActivity.this, "Please select your role", Toast.LENGTH_SHORT).show();
                } else if (statename.getText().toString().equals("")) {
                    statename.setError("Please select State name");
                    statename.requestFocus();
                } else if (cityename.getText().toString().equals("")) {
                    cityename.setError("Please select City name");
                    cityename.requestFocus();
                }else if (edt_pswd.getText().toString().equals("")) {
                    edt_pswd.setError("Please enter password");
                    edt_pswd.requestFocus();
                } else if (edt_pswd.getText().toString().length() < 6) {
                    edt_pswd.setError("Password length should be greater than 6");
                    edt_pswd.requestFocus();
                } else if (edt_pswd.getText().toString().equals("")) {
                    edt_pswd.setError("Please enter valid Password");
                    edt_pswd.requestFocus();
                } else if (!check_tnc.isChecked()) {
                    Toast.makeText(RegisterActivity.this, "Please do agree the Terms & Conditions", Toast.LENGTH_SHORT).show();

                }
                /*else if (edt_email.getText().toString().equals("")) {
                    edt_email.setError("Please enter email id");
                    edt_email.requestFocus();
                } else if (!FunctionConstant.isValidEmail(edt_email.getText().toString())) {
                    edt_email.setError("Please valid enter email id");
                    edt_email.requestFocus();
                }*/
                else {
//                    Toast.makeText(RegisterActivity.this, "Done", Toast.LENGTH_SHORT).show();
//                    sendOtp();
                    RegisterUser();
                }
            }
        });


    }

    public void checkPincode(final String pincode) {
        dialog.show();
        if (APIURLs.isNetwork(RegisterActivity.this)) {
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
                            Toast.makeText(RegisterActivity.this, "Currently we are not available in your area.", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(RegisterActivity.this, "We are available in your area.", Toast.LENGTH_SHORT).show();
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
            final Dialog dialog = new Dialog(RegisterActivity.this);
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

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public static boolean isFullname(String str) {
        String expression = "^[a-zA-Z\\s]+";
        return str.matches(expression);
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

    private void init() {
        dialog = new IOSDialog.Builder(RegisterActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();

        img_arrow = findViewById(R.id.img_arrow);
        edt_fname = findViewById(R.id.edt_fname);
        edt_lname = findViewById(R.id.edt_lname);
        edt_email = findViewById(R.id.edt_email);
        edt_mobile = findViewById(R.id.edt_mobile);
        edt_pincode = findViewById(R.id.edt_pincode);
        edt_referralCode = findViewById(R.id.edt_referralCode);
        btn_process = findViewById(R.id.btn_process);
        img_scanner = findViewById(R.id.img_scanner);
        statename = findViewById(R.id.statename);
        cityename = findViewById(R.id.cityename);
        edt_pswd = findViewById(R.id.edt_pswd);
        check_tnc = findViewById(R.id.check_tnc);
        rel_check = findViewById(R.id.rel_check);
        img_right = findViewById(R.id.img_right);
        img_wrong = findViewById(R.id.img_wrong);
        edt_mobile.setText(mobile);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QR_SCANNER) {
            if (resultCode == RESULT_OK) { // Activity.RESULT_OK
                String referral_code = data.getStringExtra("referral_code");
                edt_referralCode.setText(referral_code);
            }
        }
        if (requestCode == REQUEST_OPERATOR) {

            try {
                ssityid = data.getStringExtra(CircleSelectionActivity.RESULT_CIRCLCODE);
                ssityname = data.getStringExtra(CircleSelectionActivity.RESULT_CIRCLEID);

                statename.setText(ssityid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == REQUEST_OPERATORR) {

            try {
                scityid = data.getStringExtra(CircleSelectionActivity2.RESULT_CIRCLCODE);
                scityname = data.getStringExtra(CircleSelectionActivity2.RESULT_CIRCLEID);

                cityename.setText(scityid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void showDialog(String msg) {
        final Dialog dialog1 = new Dialog(RegisterActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.register_dialog_layout);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog1.getWindow().setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog1.getWindow().setGravity(Gravity.CENTER);
//        dialog1.getWindow().setDimAmount((float) 0.8);

        ImageView img_close = dialog1.findViewById(R.id.img_close);
        TextView txt_here = dialog1.findViewById(R.id.txt_here);
        TextView txt_register = dialog1.findViewById(R.id.txt_register);
        txt_register.setText(msg);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

        txt_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                dialog1.dismiss();
            }
        });

        dialog1.show();
    }

    public void RegisterUser() {
        dialog.show();
        if (APIURLs.isNetwork(RegisterActivity.this)) {
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
                            Toast.makeText(RegisterActivity.this, "your account has been successfully created", Toast.LENGTH_SHORT).show();
                            String cost_id = jsonObject1.getString("user_id");
                            String version_code = jsonObject1.getString("version_code");
                            String customer_order_record = jsonObject1.getString("customer_order_record");
                            SharedPref.putVal(RegisterActivity.this, SharedPref.customer_id, cost_id);

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

                            SharedPref.putVal(RegisterActivity.this, SharedPref.customer_id, cost_id);
                            SharedPref.putVal(RegisterActivity.this, SharedPref.customer_unique_id, customer_unique_id);
                            SharedPref.putVal(RegisterActivity.this, SharedPref.mobile_no, mobile_no);
                            SharedPref.putVal(RegisterActivity.this, SharedPref.email_id, email_id);
                            SharedPref.putVal(RegisterActivity.this, SharedPref.referral_code, referral_code);
                            SharedPref.putVal(RegisterActivity.this, SharedPref.customer_name, customer_name);
                            SharedPref.putVal(RegisterActivity.this, SharedPref.state_id, state_id);
                            SharedPref.putVal(RegisterActivity.this, SharedPref.city_id, city_id);
                            SharedPref.putVal(RegisterActivity.this, SharedPref.pincode, pincode);
                            SharedPref.putVal(RegisterActivity.this, SharedPref.token, token);
//                            SharedPref.putVal(RegisterActivity.this, SharedPref.fran_code, franchise_id);
                            SharedPref.putVal(RegisterActivity.this, SharedPref.version_code, version_code);
                            SharedPref.putVal(RegisterActivity.this, SharedPref.customer_order_record, customer_order_record);


//                            SendOtp();

                            Intent intent = new Intent(RegisterActivity.this, HomeActivity2.class);
//                            intent.putExtra("mobile", from);
                            startActivity(intent);
                            finish();

                        }/* else if (status.equals("2")) {
                            Toast.makeText(RegisterActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                        }*/ else {
//                            ErrorToast(jsonObject1.getString("msg")); // Error Toast
//                            showDialog(jsonObject1.getString("Mobile no already registered"));
                            Toast.makeText(RegisterActivity.this, "Mobile no already registered", Toast.LENGTH_SHORT).show();
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
                    params.put("customer_fname", edt_fname.getText().toString().trim());
                    params.put("customer_lname", edt_lname.getText().toString().trim());
                    params.put("city_id", scityid);
                    params.put("state_id", ssityid);
                    params.put("customer_email", edt_email.getText().toString().trim());
                    params.put("customer_pincode", edt_pincode.getText().toString().trim());
                    params.put("customer_phone", mobile);
                    params.put("customer_password", edt_pswd.getText().toString().trim());
                    params.put("my_refral_code", referralCode);
                    params.put("refral_code", edt_referralCode.getText().toString());
                    if(spin.getSelectedItem().equals("User")){

                        params.put("role_type", "1");
                    }else if(spin.getSelectedItem().equals("Business")){
                        params.put("role_type", "2");

                    }
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
            final Dialog dialog = new Dialog(RegisterActivity.this);
            dialog.setContentView(R.layout.no_internet_dialog);
            Button button = dialog.findViewById(R.id.btn_process);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RegisterUser();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public void sendOtp() {
        dialog.show();
        if (APIURLs.isNetwork(RegisterActivity.this)) {
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
                            Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();//Show Success Toast
                            Random random = new Random();
                            String referralCode = generateReferralCode(random);

                            Intent intent = new Intent(RegisterActivity.this, OtpActivity.class);
                            intent.putExtra("mobile", edt_mobile.getText().toString());
                            intent.putExtra("customer_fname", edt_fname.getText().toString().trim());
                            intent.putExtra("customer_lname", edt_lname.getText().toString().trim());
                            intent.putExtra("city_id", scityname);
                            intent.putExtra("state_id", ssityname);
                            intent.putExtra("customer_email", edt_email.getText().toString().trim());
                            intent.putExtra("customer_pincode", edt_pincode.getText().toString().trim());
                            intent.putExtra("customer_phone", edt_mobile.getText().toString().trim());
                            intent.putExtra("customer_password", edt_pswd.getText().toString().trim());
                            intent.putExtra("referral_code", referralCode);
                            intent.putExtra("apply_rc", edt_referralCode.getText().toString());
                            intent.putExtra("otp", jsonObject1.getString("otp"));
                            if (spin.getSelectedItem().equals("User")) {
                                intent.putExtra("role_type", "1");

                            } else if (spin.getSelectedItem().equals("Business")) {
                                intent.putExtra("role_type", "2");

                            }
                            intent.putExtra("from", "from");
                            startActivity(intent);
                            finish();

                        }/* else if (status.equals("2")) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);
                            // Setting Dialog Title
                            alertDialog.setTitle("Alert!!!");
                            // Setting Dialog Message
                            alertDialog.setMessage("You are blocked, \nPlease contact our team for further enquiry.");
                            // On pressing Settings button
                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(RegisterActivity.this, HelpAndSupportActivity.class);
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
                            Toast.makeText(RegisterActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
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
            final Dialog dialog = new Dialog(RegisterActivity.this);
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


    public void state(View v) {
        Intent i = new Intent(RegisterActivity.this, CircleSelectionActivity.class);
        startActivityForResult(i, REQUEST_OPERATOR);
    }


    public void city(View v) {

        if (ssityid.equals("NA")) {
            Toast.makeText(RegisterActivity.this, "Kindly Select State Name First", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(RegisterActivity.this, CircleSelectionActivity3.class);
            intent.putExtra("sid", ssityname);
            startActivityForResult(intent, REQUEST_OPERATORR);

        }

    }

}
