package com.sterlex.in.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sterlex.in.R;

public class OrderPlacedActivity extends AppCompatActivity {

    TextView txt_message;
    String OrderId = "";
    TextView txt_cong;
    ImageView image_basket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);
        init();

        OrderId = getIntent().getStringExtra("OrderId");
//        OrderId = "OrderId";

        if (OrderId.equals("")) {
            txt_cong.setText("Thank You !! your order has been placed successfully.");
        } else {
            txt_cong.setText("Thank You !! your order has been placed successfully.");
            txt_message.setText("Order id is #" + OrderId);
        }
        Glide.with(OrderPlacedActivity.this).asGif().load(R.drawable.payment_succesfull_gif).into(image_basket);

    }

    private void init() {
        txt_message = findViewById(R.id.txt_message);
        txt_cong = findViewById(R.id.txt_cong);
        image_basket = findViewById(R.id.image_basket);
    }

    public void goToHome(View view) {

        startActivity(new Intent(OrderPlacedActivity.this, HomeActivity2.class));
        finish();

    }
}
