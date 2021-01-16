package com.sterlex.in.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.github.pinball83.maskededittext.MaskedEditText;
import com.gmail.samehadar.iosdialog.IOSDialog;
import com.sterlex.in.Constant.APIURLs;
import com.sterlex.in.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText edt_mobile;
    Button btn_process;
    ImageView img_arrow;
    IOSDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        init();
        onClick();
    }

    private void init() {
        dialog = new IOSDialog.Builder(ForgotPasswordActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();

        edt_mobile = findViewById(R.id.edt_mobile);
        btn_process = findViewById(R.id.btn_process);
        img_arrow = findViewById(R.id.img_arrow);

    }

    public void onClick(){
        btn_process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_mobile.getText().toString().equals("")) {
                    edt_mobile.setError("Please enter mobile number");
                    edt_mobile.requestFocus();
                } else if (edt_mobile.getText().length() != 10) {
                    edt_mobile.setError("Please valid enter mobile number");
                    edt_mobile.requestFocus();
                } else{
                    SendOtp();
//                    startActivity(new Intent(ForgotPasswordActivity.this,UpdatePasswordActivity.class));
                }
            }
        });
        img_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    public void SendOtp() {
        dialog.show();
        if (APIURLs.isNetwork(ForgotPasswordActivity.this)) {
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
//                            SuccessToast(msg);
                            String otp = jsonObject1.getString("otp");
//                            Toast.makeText(ForgotPasswordActivity.this, msg, Toast.LENGTH_SHORT).show();//Show Success Toast

                            Intent intent = new Intent(ForgotPasswordActivity.this, UpdatePasswordActivity.class);
                            intent.putExtra("mobile", edt_mobile.getText().toString());
                            intent.putExtra("otp", otp);
                            intent.putExtra("user_id", jsonObject1.getString("user_id"));
//                            intent.putExtra("from", from);
                            startActivity(intent);
                            finish();
                        }  else {
//                            ErrorToast(msg);  //Show Error Toast
                            Toast.makeText(ForgotPasswordActivity.this, msg, Toast.LENGTH_SHORT).show();
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
            final Dialog dialog = new Dialog(ForgotPasswordActivity.this);
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
}
