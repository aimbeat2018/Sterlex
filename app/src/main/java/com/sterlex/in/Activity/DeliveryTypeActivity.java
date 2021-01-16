package com.sterlex.in.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sterlex.in.Constant.OrderSharedPrefence;
import com.sterlex.in.R;

public class DeliveryTypeActivity extends AppCompatActivity {

    Button btn_selfpickup, btn_home_delivery;
    LinearLayout order_detail, order_hide_detail, lin_details;
    ImageView img_arrow;
    String total = "", discount = "";
    TextView txt_total, txt_grandtotal, txt_savings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_type);

//        Intent intent = getIntent();
        total = OrderSharedPrefence.getVal(DeliveryTypeActivity.this, OrderSharedPrefence.total_bag);
        discount = OrderSharedPrefence.getVal(DeliveryTypeActivity.this, OrderSharedPrefence.total_discount);
        init();
        onClick();
    }

    private void onClick() {
        btn_selfpickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryTypeActivity.this, DeliveryAddressActivity.class);
                intent.putExtra("from", "self");
                OrderSharedPrefence.putVal(DeliveryTypeActivity.this, OrderSharedPrefence.delivery_type, "0");
//                intent.putExtra("total",total);
                startActivity(intent);
                finish();
            }
        });

        btn_home_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryTypeActivity.this, DeliveryAddressActivity.class);
                intent.putExtra("from", "home");
                OrderSharedPrefence.putVal(DeliveryTypeActivity.this, OrderSharedPrefence.delivery_type, "1");
                startActivity(intent);
                finish();
            }
        });

        order_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_detail.setVisibility(View.GONE);
                order_hide_detail.setVisibility(View.VISIBLE);
                lin_details.setVisibility(View.VISIBLE);
            }
        });
        order_hide_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_detail.setVisibility(View.VISIBLE);
                order_hide_detail.setVisibility(View.GONE);
                lin_details.setVisibility(View.GONE);
            }
        });
        img_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void init() {
        btn_selfpickup = findViewById(R.id.btn_selfpickup);
        btn_home_delivery = findViewById(R.id.btn_home_delivery);
        lin_details = findViewById(R.id.lin_details);
        lin_details.setVisibility(View.GONE);
        order_detail = findViewById(R.id.order_detail);
        order_hide_detail = findViewById(R.id.order_hide_detail);
        img_arrow = findViewById(R.id.img_arrow);
        txt_total = findViewById(R.id.txt_total);
        txt_grandtotal = findViewById(R.id.txt_grandtotal);
        txt_savings = findViewById(R.id.txt_savings);

        long discount_amount = Math.round(Double.valueOf(discount));
        txt_savings.setText("\u20B9" + discount_amount);

        long total_amount = Math.round(Double.valueOf(total));
        txt_total.setText("\u20B9" + total_amount);

        Double gtotal = Double.valueOf(total) - Double.valueOf(discount);

        long gtotal_amount = Math.round(gtotal);

        txt_grandtotal.setText("\u20B9" + gtotal_amount);

    }
}