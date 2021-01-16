package com.sterlex.in.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sterlex.in.R;

public class PaymentActivity extends AppCompatActivity {

    ImageView img_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        init();
        onClick();
    }

    private void onClick() {
        img_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void init() {
        img_arrow = findViewById(R.id.img_arrow);
    }

    public void proceedToPaymentDialog(View view) {


        final Dialog dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.congratulations_dialog_layout);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog1.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog1.getWindow().setGravity(Gravity.CENTER);
        dialog1.getWindow().setDimAmount((float) 0.8);
        dialog1.setCancelable(true);
        dialog1.setCanceledOnTouchOutside(true);
        final EditText edt_pincode = dialog1.findViewById(R.id.edt_pincode);
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

    }


}