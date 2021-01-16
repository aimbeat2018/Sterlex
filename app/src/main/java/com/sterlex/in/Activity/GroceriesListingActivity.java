package com.sterlex.in.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sterlex.in.R;

public class GroceriesListingActivity extends AppCompatActivity {

    RecyclerView rec_listing;
    ImageView img_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groceries_listing);

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
        rec_listing = findViewById(R.id.rec_listing);
        img_arrow = findViewById(R.id.img_arrow);

        rec_listing.setAdapter(new ProductsListingAdapter(GroceriesListingActivity.this));
        rec_listing.setLayoutManager(new LinearLayoutManager(GroceriesListingActivity.this, RecyclerView.VERTICAL, false));
    }

    public void search(View view) {
        Intent intent = new Intent(GroceriesListingActivity.this, SearchActivity.class);
        intent.putExtra("key", "");
        intent.putExtra("from", "");
        startActivity(intent);

    }

    public void cart(View view) {
        startActivity(new Intent(GroceriesListingActivity.this, CartActivity2.class));
    }

    public class ProductsListingAdapter extends RecyclerView.Adapter<ProductsListingAdapter.ItemViewholder> {
        Context context;


        public ProductsListingAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_most_popular_prdcts_list_home_layout, parent, false);
            ProductsListingAdapter.ItemViewholder itemViewholder = new ProductsListingAdapter.ItemViewholder(view);
            return itemViewholder;
        }

        @Override
        public void onBindViewHolder(@NonNull ProductsListingAdapter.ItemViewholder holder, int position) {
//            HouseHelpCategoryPojo model = houseHelpCategoryPojos.get(position);
            ((ProductsListingAdapter.ItemViewholder) holder).bind(position);
        }

        @Override
        public int getItemCount() {
            return 5;
        }

        public class ItemViewholder extends RecyclerView.ViewHolder {
            ImageView iv;

            LinearLayout lin_item, lin_comm;
            CardView card_bg;
            RelativeLayout lin_owner;
            TextView txt_mrp;

            public ItemViewholder(@NonNull View itemView) {
                super(itemView);

//                card_bg = itemView.findViewById(R.id.card_bg);
                lin_item = itemView.findViewById(R.id.lin_item);
                txt_mrp = itemView.findViewById(R.id.txt_mrp);
                txt_mrp.setPaintFlags(txt_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            }

            public void bind(final int position) {
//                txt_name.setText("");
//
//                txt_name.setText(model.getCategoryname());
//
//
                lin_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GroceriesListingActivity.this, ProductDetailsActivity.class);
//                        intent.putExtra("id", "id");
                        startActivity(intent);
                    }
                });


            }

        }

    }

    public void showUserMenuDialog(View view) {
        final Dialog dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.usermenu_dialog_layout);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog1.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog1.getWindow().setGravity(Gravity.TOP);
        dialog1.getWindow().setDimAmount((float) 0.7);
        dialog1.setCancelable(true);
        dialog1.setCanceledOnTouchOutside(true);

        TextView txt_sign_in = dialog1.findViewById(R.id.txt_sign_in);
        TextView txt_register = dialog1.findViewById(R.id.txt_register);

        txt_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GroceriesListingActivity.this, LoginActivity.class));
            }
        });

        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GroceriesListingActivity.this, OtpActivity.class));
            }
        });
        dialog1.show();
    }
}