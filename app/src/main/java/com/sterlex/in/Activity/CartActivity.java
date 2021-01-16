package com.sterlex.in.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.gmail.samehadar.iosdialog.IOSDialog;
import com.sterlex.in.Constant.APIURLs;
import com.sterlex.in.Constant.FunctionConstant;
import com.sterlex.in.Constant.GPSTracker;
import com.sterlex.in.Constant.SharedPref;
import com.sterlex.in.Model.CartModel;
import com.sterlex.in.Model.SlotModel;
import com.sterlex.in.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    RecyclerView rec_cartList;
    ImageView img_back, img_remove;
    Button btn_pay;
    LinearLayout lnr_discount, lnr_cartempty, lnr_cartDetails, lnr_saverlater, lnr_coupon, lnr_walletBalance, lnr_totalsaving;
    TextView txt_TotalSaving;
    TextView edt_address, txt_nodata, txt_unitPrice, txt_discountCharges, txt_subTotal,
            txt_grandTotal, txt_saveLater1, txt_time, txt_couponCode, txt_couponCharges, txt_walletCharges, txt_address, txt_mincart, txt_delivery;
    IOSDialog dialog;
    ArrayList<CartModel> productModelArrayList = new ArrayList<>();
    ArrayList<CartModel> savelaterModelArrayList = new ArrayList<>();
    ArrayList<SlotModel> slotModelArrayList = new ArrayList<>();
    ScrollView scrollView;
    String selectedTime = "", selectedSlotId = "";
    int selectedIndex = -1;
    String total_bag = "", bag_discount = "", order_total = "", delivery_charges = "";
    private final int COUPON_USED = 1;
    String coupon_name = "", coupon_id = "", coupon_value = "", capping_value = "";
    String product_id = "";
    String finalQty = "", finalUnit = "", finalUnitPrice = "", finalDiscount = "";
    ArrayList<String> productIdArrayList = new ArrayList<>();
    ArrayList<String> qtyArrayList = new ArrayList<>();
    ArrayList<String> unitArrayList = new ArrayList<>();
    ArrayList<String> unitpriceArrayList = new ArrayList<>();
    ArrayList<String> discountArrayList = new ArrayList<>();
    String walletBalance = "", payBalance = "", miniCart = "";
    Double walletdouble;
    Double subTotaldouble;
    Double grandTotaldouble;
    Double latitude, longitude;
    double minCart = 0.0;
//    ShimmerFrameLayout shimmer_view_container;
    LinearLayout lnr_main;
    String cost_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        init();
        cost_id = SharedPref.getVal(CartActivity.this, SharedPref.customer_id);
        onClick();
//        minimuncart();
//        walletBalance();
//        getProductList();
//        getSaveLaterList();
    }

    private void onClick() {


//        img_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

    }

    public void getCurrentLocation() {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            latitude = new GPSTracker(this).getLatitude();
            longitude = new GPSTracker(this).getLongitude();

            if (latitude != 0.0) {
                try {

                    geocoder = new Geocoder(this, Locale.getDefault());
                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                    String add = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                txt_location.setText(address);
                    String pin = addresses.get(0).getPostalCode();

//                    if (pin.equals("400614") || pin.equals("400705") || pin.equals("400706") || pin.equals("410208") || pin.equals("410210")) {
                    txt_address.setText(add);
//                    } else {
//                        txt_address.setText(add);
//                        new AlertDialog.Builder(CartActivity.this)
//                                .setTitle("Service unavailable")
//                                .setMessage("Currently we are not provide service in your area.")
//
//                                // Specifying a listener allows you to take an action before dismissing the dialog.
//                                // The dialog is automatically dismissed when a dialog button is clicked.
//                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        // Continue with delete operation
//                                        dialog.dismiss();
//                                    }
//                                })
//                                .show();
//                    }

                } catch (Exception es) {
                    es.printStackTrace();
                }

            } else {
                showGpsDisabledDialog();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CartActivity.this);
        // Setting Dialog Title
        alertDialog.setTitle("Allow gps location");
        // Setting Dialog Message
        alertDialog.setMessage("Please Allow Location permission");
        // On pressing Settings button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    public void showGpsDisabledDialog() {
        if (!((Activity) CartActivity.this).isFinishing()) {
            new AlertDialog.Builder(CartActivity.this)
                    .setMessage("GPS Disabled , to Continue, turn on your device location ")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                            startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
                            dialog.dismiss();
                        }
                    })

                    /*// A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finishAffinity();
                        }
                    })*/
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .show();
        }
    }

    private void init() {
        dialog = new IOSDialog.Builder(CartActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();
        scrollView = findViewById(R.id.scrollView);
//        shimmer_view_container = findViewById(R.id.shimmer_view_container);
        lnr_main = findViewById(R.id.lnr_main);
        txt_nodata = findViewById(R.id.txt_nodata);
        rec_cartList = findViewById(R.id.rec_cartList);
       /* String add = SharedPref.getVal(CartActivity.this, SharedPref.Address);
        edt_address.setText(add);*/


        rec_cartList.setAdapter(new CartListingAdapter(CartActivity.this));
        rec_cartList.setLayoutManager(new LinearLayoutManager(CartActivity.this, RecyclerView.VERTICAL, false));
 }
    public void dismissDialog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               /* if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }*/
//                shimmer_view_container.stopShimmerAnimation();
//                shimmer_view_container.setVisibility(View.GONE);
                lnr_main.setVisibility(View.VISIBLE);
            }
        }, 2000);
    }


    public class CartListingAdapter extends RecyclerView.Adapter<CartListingAdapter.ItemViewholder> {
        Context context;


        public CartListingAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public CartListingAdapter.ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
            CartListingAdapter.ItemViewholder itemViewholder = new CartListingAdapter.ItemViewholder(view);
            return itemViewholder;
        }

        @Override
        public void onBindViewHolder(@NonNull CartListingAdapter.ItemViewholder holder, int position) {
//            HouseHelpCategoryPojo model = houseHelpCategoryPojos.get(position);
            ((CartListingAdapter.ItemViewholder) holder).bind(position);
        }

        @Override
        public int getItemCount() {
            return 5;
        }

        public class ItemViewholder extends RecyclerView.ViewHolder {
            ImageView iv;

            LinearLayout lin_openplot, lin_comm;
            CardView card_bg;
            RelativeLayout lin_owner;
            TextView txt_name;

            public ItemViewholder(@NonNull View itemView) {
                super(itemView);

//                card_bg = itemView.findViewById(R.id.card_bg);
//                iv = itemView.findViewById(R.id.iv);
//                txt_name = itemView.findViewById(R.id.txt_name);
            }

            public void bind(final int position) {
//                txt_name.setText("");
//
//                txt_name.setText(model.getCategoryname());
//
//
//                card_bg.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(this, class);
//                        intent.putExtra("id", "id");
//                        startActivity(intent);
//                    }
//                });


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
                startActivity(new Intent(CartActivity.this, LoginActivity.class));
            }
        });

        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, OtpActivity.class));
            }
        });
        dialog1.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      /*  if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }*/
//        shimmer_view_container.stopShimmerAnimation();
//        shimmer_view_container.setVisibility(View.GONE);
        lnr_main.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        shimmer_view_container.startShimmerAnimation();
//        shimmer_view_container.setVisibility(View.VISIBLE);
        lnr_main.setVisibility(View.GONE);

        if (FunctionConstant.GPSRuntime(CartActivity.this)) {
            getCurrentLocation();
        } else {
            showSettingsAlert();
        }
//        minimuncart();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        shimmer_view_container.stopShimmerAnimation();
//        shimmer_view_container.setVisibility(View.GONE);
        lnr_main.setVisibility(View.VISIBLE);
    }

    public void getSelectedTime() {
        if (selectedIndex != -1) {
            selectedTime = slotModelArrayList.get(selectedIndex).getSlot_timing();
            selectedSlotId = slotModelArrayList.get(selectedIndex).getSlot_id();
        } else {
            Toast.makeText(this, "Please select expected time", Toast.LENGTH_SHORT).show();
        }
    }



    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
        Context context;
        int qty = 0;

        public ProductAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(CartActivity.this).inflate(R.layout.cart_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            CartModel model = productModelArrayList.get(position);
            ((ViewHolder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return 5;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
//            ImageView img_product;
//            TextView txt_productName, txt_weight, txt_season, txt_actualprice, txt_stock, txt_discountprice, txt_discount, txt_count;
//            Button btn_cart, btn_subscribe;
//            LinearLayout lnr_product, lnr_remove;
//            RelativeLayout rel_discount, rel_cart, rel_minus, rel_plus, rel_saveLater, rel_removeCart;
//            CardView card_list;
//            ProgressBar progressBar;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
//
//                img_product = itemView.findViewById(R.id.img_product);
//                txt_productName = itemView.findViewById(R.id.txt_productName);
//                txt_weight = itemView.findViewById(R.id.txt_weight);
//                txt_season = itemView.findViewById(R.id.txt_season);
//                txt_actualprice = itemView.findViewById(R.id.txt_actualprice);
//                txt_discountprice = itemView.findViewById(R.id.txt_discountprice);
//                txt_stock = itemView.findViewById(R.id.txt_stock);
//                btn_cart = itemView.findViewById(R.id.btn_cart);
//                btn_subscribe = itemView.findViewById(R.id.btn_subscribe);
//                btn_subscribe.setVisibility(View.GONE);
//                lnr_product = itemView.findViewById(R.id.lnr_product);
//                lnr_remove = itemView.findViewById(R.id.lnr_remove);
//                rel_saveLater = itemView.findViewById(R.id.rel_saveLater);
//                rel_removeCart = itemView.findViewById(R.id.rel_removeCart);
//                rel_discount = itemView.findViewById(R.id.rel_discount);
//                rel_minus = itemView.findViewById(R.id.rel_minus);
//                rel_plus = itemView.findViewById(R.id.rel_plus);
//                rel_cart = itemView.findViewById(R.id.rel_cart);
//                card_list = itemView.findViewById(R.id.card_list);
//                txt_discount = itemView.findViewById(R.id.txt_discount);
//                progressBar = itemView.findViewById(R.id.progressBar);
//                txt_count = itemView.findViewById(R.id.txt_count);
//                txt_actualprice.setPaintFlags(txt_actualprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                lnr_remove.setVisibility(View.VISIBLE);
            }

            public void bind(final CartModel model, final int position) {

//                productIdArrayList.add(model.getProduct_id());
//                /*   if(unitArrayList.size()<0) {*/
//                unitArrayList.add(model.getUnit());
////                }
//
//                /* if(unitpriceArrayList.size()<0) {*/
//                unitpriceArrayList.add(model.getUnit_price());
////                }
//
//                /*if(discountArrayList.size()<0){*/
//                discountArrayList.add(model.getDiscount());
////                }
//
//                Glide.with(CartActivity.this).load(model.getImage()).into(img_product);
//                if (model.getProduct_name().length() <= 14) {
//                    txt_productName.setText(model.getProduct_name());
//                } else {
//                    String pname = model.getProduct_name();
//                    pname = pname.substring(0, 14);
//                    txt_productName.setText(pname + "...");
//                }
////                txt_productName.setText(model.getProduct_name());
//                txt_weight.setText(model.getUnit());
//                try {
//                    if (model.getDiscount().equals("0")) {
//                        txt_discountprice.setText("Rs." + model.getPrice());
//                        txt_actualprice.setVisibility(View.GONE);
//                        rel_discount.setVisibility(View.GONE);
//                    } else {
//                        txt_actualprice.setVisibility(View.VISIBLE);
//                        rel_discount.setVisibility(View.VISIBLE);
//                        txt_discountprice.setText("Rs." + model.getDiscountPrice());
//                        txt_actualprice.setText("Rs." + model.getPrice());
////                        txt_discount.setText(model.getDiscount() + "% off");
//
//                        txt_discount.setText("Save\n\u20B9" + model.getDiscount());
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                if (model.getQty().equals("0")) {
//                    rel_cart.setVisibility(View.GONE);
//                    btn_cart.setVisibility(View.VISIBLE);
//                } else {
//                    rel_cart.setVisibility(View.VISIBLE);
//                    btn_cart.setVisibility(View.GONE);
//                    qty = Integer.valueOf(model.getQty());
//                    txt_count.setText(String.valueOf(qty));
//                }
//
//                btn_cart.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        rel_cart.setVisibility(View.VISIBLE);
//                        btn_cart.setVisibility(View.GONE);
//                    }
//                });
//
//                rel_minus.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        qty = Integer.parseInt(txt_count.getText().toString());
//                        if (qty >= 1) {
//                            qty--;
//                            if (qty != 0) {
//                                txt_count.setText(String.valueOf(qty));
//                                txt_count.setVisibility(View.GONE);
//                                card_list.setVisibility(View.VISIBLE);
//                                addCart(model.getProduct_id(), model.getUnit(), model.getUnit_price(), model.getDiscount(), txt_count.getText().toString());
//                                for (int i = 0; i < qtyArrayList.size(); i++) {
//                                    if (position == i) {
//                                        qtyArrayList.set(i, String.valueOf(qty));
//                                    }
//                                }
//
//                              /*  if(unitArrayList.size()>0) {
//                                    for (int i = 0; i < unitArrayList.size(); i++) {
//                                        if (position == i) {
//                                            unitArrayList.set(i, String.valueOf(model.getUnit()));
//                                        }
//                                    }
//                                }
//
//                                if(unitpriceArrayList.size()>0) {
//                                    for (int i = 0; i < unitpriceArrayList.size(); i++) {
//                                        if (position == i) {
//                                            unitpriceArrayList.set(i, String.valueOf(model.getUnit_price()));
//                                        }
//                                    }
//                                }
//
//                                if(discountArrayList.size()>0) {
//                                    for (int i = 0; i < discountArrayList.size(); i++) {
//                                        if (position == i) {
//                                            discountArrayList.set(i, String.valueOf(model.getDiscount()));
//                                        }
//                                    }
//                                }*/
//                            } else {
//                                removeCart(model.getCart_id(), position);
//                                for (int i = 0; i < qtyArrayList.size(); i++) {
//                                    if (position == i) {
//                                        qtyArrayList.set(i, String.valueOf(qty));
//                                    }
//                                }
//
//                               /* if(unitArrayList.size()>0) {
//                                    for (int i = 0; i < unitArrayList.size(); i++) {
//                                        if (position == i) {
//                                            unitArrayList.set(i, String.valueOf(model.getUnit()));
//                                        }
//                                    }
//                                }
//
//                                if(unitpriceArrayList.size()>0) {
//                                    for (int i = 0; i < unitpriceArrayList.size(); i++) {
//                                        if (position == i) {
//                                            unitpriceArrayList.set(i, String.valueOf(model.getUnit_price()));
//                                        }
//                                    }
//                                }
//
//                                if(discountArrayList.size()>0) {
//                                    for (int i = 0; i < discountArrayList.size(); i++) {
//                                        if (position == i) {
//                                            discountArrayList.set(i, String.valueOf(model.getDiscount()));
//                                        }
//                                    }
//                                }*/
//                            }
//                        }
//                    }
//                });
//
//                rel_plus.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        qty = Integer.parseInt(txt_count.getText().toString());
//                        qty++;
//                        txt_count.setText(String.valueOf(qty));
//                        txt_count.setVisibility(View.GONE);
//                        addCart(model.getProduct_id(), model.getUnit(), model.getUnit_price(), model.getDiscount(), txt_count.getText().toString());
//
//                        for (int i = 0; i < qtyArrayList.size(); i++) {
//                            if (position == i) {
//                                qtyArrayList.set(i, String.valueOf(qty));
//                            }
//                        }
//
///*
//                        if(unitArrayList.size()>0) {
//                            for (int i = 0; i < unitArrayList.size(); i++) {
//                                if (position == i) {
//                                    unitArrayList.set(i, String.valueOf(model.getUnit()));
//                                }
//                            }
//                        }
//
//                        if(unitpriceArrayList.size()>0) {
//                            for (int i = 0; i < unitpriceArrayList.size(); i++) {
//                                if (position == i) {
//                                    unitpriceArrayList.set(i, String.valueOf(model.getUnit_price()));
//                                }
//                            }
//                        }
//
//                        if(discountArrayList.size()>0) {
//                            for (int i = 0; i < discountArrayList.size(); i++) {
//                                if (position == i) {
//                                    discountArrayList.set(i, String.valueOf(model.getDiscount()));
//                                }
//                            }
//                        }*/
//                    }
//                });
//
//                qtyArrayList.add(txt_count.getText().toString());
//
//                lnr_product.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(new Intent(CartActivity.this, ProductDetailsActivity.class));
//                        intent.putExtra("prod_id", model.getProduct_id());
//                        startActivity(intent);
//
//                    }
//                });
//                rel_removeCart.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        new AlertDialog.Builder(CartActivity.this)
//                                .setTitle("Alert!!!")
//                                .setMessage("Are u sure u want to remove item from cart?")
//
//                                // Specifying a listener allows you to take an action before dismissing the dialog.
//                                // The dialog is automatically dismissed when a dialog button is clicked.
//                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        // Continue with delete operation
//                                        dialog.dismiss();
//                                        removeCart(model.getCart_id(), position);
//                                    }
//                                })
//                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                })
//                                .show();
//                    }
//                });
//
//                rel_saveLater.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        new AlertDialog.Builder(CartActivity.this)
//                                .setTitle("Alert!!!")
//                                .setMessage("Are u sure?")
//
//                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        // Continue with delete operation
//                                        dialog.dismiss();
//                                        saveLater(model.getProduct_id(), model.getUnit(), model.getUnit_price(), model.getDiscount(), txt_count.getText().toString(), position);
//                                    }
//                                })
//                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                })
//                                .show();
//                    }
//                });
//            }
//
//
//            public void addCart(final String prod_id, final String unit, final String price, final String discount, final String qty1) {
////                dialog.show();
//                progressBar.setVisibility(View.VISIBLE);
//                txt_count.setVisibility(View.GONE);
//                if (APIURLs.isNetwork(CartActivity.this)) {
//                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.ADDCART_URL, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//                                JSONObject jsonObject = new JSONObject(response);
//                                JSONArray jsonArray = jsonObject.getJSONArray("result");
//                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
//                                String status = jsonObject1.getString("status");
//                                if (status.equals("1")) {
//                                    txt_count.setText(qty1);
//                                    qty = 0;
////                                    getProductList();
//                                    cartCalculation();
//                                } else {
//                                    Toast.makeText(CartActivity.this, "Error", Toast.LENGTH_SHORT).show();
//                                }
////                                dismissDialog();
//                                progressBar.setVisibility(View.GONE);
//                                txt_count.setVisibility(View.VISIBLE);
//                            } catch (JSONException e) {
////                                dismissDialog();
//                                progressBar.setVisibility(View.GONE);
//                                txt_count.setVisibility(View.VISIBLE);
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
////                            dismissDialog();
//                            progressBar.setVisibility(View.GONE);
//                            txt_count.setVisibility(View.VISIBLE);
//                            error.printStackTrace();
//                        }
//                    }) {
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//                            HashMap<String, String> params = new HashMap<>();
//                            params.put("customer_id", SharedPref.getVal(CartActivity.this, SharedPref.customer_id));
//                            params.put("product_id", prod_id);
//                            params.put("unit", unit);
//                            params.put("price", price);
//                            params.put("discount", discount);
//                            params.put("qty", qty1);
//                            return params;
//                        }
//                    };
//
//                    RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);
//                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                    requestQueue.add(stringRequest);
//                } else {
//                    dismissDialog();
//                    FunctionConstant.noInternetDialog(CartActivity.this, "no internet connection");
//                }
//            }
//
//            public void removeCart(final String cart_id, final int position) {
////                dialog.show();
//                if (APIURLs.isNetwork(CartActivity.this)) {
//                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.REMOVECART_URL, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//                                JSONObject jsonObject = new JSONObject(response);
//                                JSONArray jsonArray = jsonObject.getJSONArray("result");
//                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
//                                String status = jsonObject1.getString("status");
//                                if (status.equals("1")) {
//                                    qty = 0;
//                                    notifyDataSetChanged();
//                                    notifyItemRemoved(position);
//                                    productModelArrayList.remove(position);
//                                    if (productModelArrayList.size() > 0) {
//                                        lnr_cartempty.setVisibility(View.GONE);
//                                        lnr_cartDetails.setVisibility(View.VISIBLE);
//                                    } else {
//                                        lnr_cartempty.setVisibility(View.VISIBLE);
//                                        lnr_cartDetails.setVisibility(View.GONE);
//                                    }
//                                    cartCalculation();
//                                    notifyDataSetChanged();
//                                    txt_couponCode.setText("");
//                                } else {
//                                    Toast.makeText(CartActivity.this, "Error", Toast.LENGTH_SHORT).show();
//                                }
//                                dismissDialog();
//                            } catch (JSONException e) {
//                                dismissDialog();
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            dismissDialog();
//                            error.printStackTrace();
//                        }
//                    }) {
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//                            HashMap<String, String> params = new HashMap<>();
//                            params.put("cart_id", cart_id);
//                            return params;
//                        }
//                    };
//
//                    RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);
//                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                    requestQueue.add(stringRequest);
//                } else {
//                    dismissDialog();
//                    FunctionConstant.noInternetDialog(CartActivity.this, "no internet connection");
//                }
//            }
//
//            public void saveLater(final String prod_id, final String unit, final String price, final String discount, final String qty1, final int position) {
////                dialog.show();
//                if (APIURLs.isNetwork(CartActivity.this)) {
//                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.SAVEFORLATER_URL, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//                                JSONObject jsonObject = new JSONObject(response);
//                                JSONArray jsonArray = jsonObject.getJSONArray("result");
//                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
//                                String status = jsonObject1.getString("status");
//                                String msg = jsonObject1.getString("msg");
//                                if (status.equals("1")) {
//                                    notifyDataSetChanged();
//                                    notifyItemRemoved(position);
//                                    productModelArrayList.remove(position);
//                                    getSaveLaterList();
//                                    getProductList();
//                                    txt_couponCode.setText("");
//                                    coupon_name = "";
//                                    coupon_id = "";
//                                    coupon_value = "";
//                                    capping_value = "";
//                                    lnr_coupon.setVisibility(View.GONE);
//                                    txt_couponCharges.setText("");
//                                } else {
//                                    Toast.makeText(CartActivity.this, msg, Toast.LENGTH_SHORT).show();
//                                }
//                                dismissDialog();
//                            } catch (JSONException e) {
//                                dismissDialog();
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            dismissDialog();
//                            error.printStackTrace();
//                        }
//                    }) {
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//                            HashMap<String, String> params = new HashMap<>();
//                            params.put("customer_id", SharedPref.getVal(CartActivity.this, SharedPref.customer_id));
//                            params.put("product_id", prod_id);
//                            params.put("unit", unit);
//                            params.put("price", price);
//                            params.put("discount", discount);
//                            params.put("qty", qty1);
//                            return params;
//                        }
//                    };
//
//                    RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);
//                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                    requestQueue.add(stringRequest);
//                } else {
//                    dismissDialog();
//                    FunctionConstant.noInternetDialog(CartActivity.this, "no internet connection");
//                }
            }
        }
    }

    public class SaveLaterAdapter extends RecyclerView.Adapter<SaveLaterAdapter.ViewHolder> {
        Context context;
        Double actualAmout;
        Double discount;
        Double payableAmt;
        int qty = 0;

        public SaveLaterAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(CartActivity.this).inflate(R.layout.search_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            CartModel model = savelaterModelArrayList.get(position);
            ((ViewHolder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return savelaterModelArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_product, img_savelater;
            TextView txt_productName, txt_weight, txt_season, txt_actualprice, txt_stock, txt_discountprice, txt_discount, txt_count, txt_saveLater;
            Button btn_cart, btn_subscribe;
            LinearLayout lnr_product, lnr_remove;
            RelativeLayout rel_saveLater, rel_removeCart;
            RelativeLayout rel_discount, rel_cart, rel_minus, rel_plus;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                img_product = itemView.findViewById(R.id.img_product);
                txt_productName = itemView.findViewById(R.id.txt_productName);
                txt_weight = itemView.findViewById(R.id.txt_weight);
                txt_season = itemView.findViewById(R.id.txt_season);
                txt_actualprice = itemView.findViewById(R.id.txt_actualprice);
                txt_discountprice = itemView.findViewById(R.id.txt_discountprice);
                txt_stock = itemView.findViewById(R.id.txt_stock);
                btn_cart = itemView.findViewById(R.id.btn_cart);
                btn_cart.setVisibility(View.GONE);
                btn_subscribe = itemView.findViewById(R.id.btn_subscribe);
                lnr_product = itemView.findViewById(R.id.lnr_product);
                rel_discount = itemView.findViewById(R.id.rel_discount);
                rel_minus = itemView.findViewById(R.id.rel_minus);
                rel_plus = itemView.findViewById(R.id.rel_plus);
                rel_cart = itemView.findViewById(R.id.rel_cart);
                txt_discount = itemView.findViewById(R.id.txt_discount);
                txt_count = itemView.findViewById(R.id.txt_count);
                txt_saveLater = itemView.findViewById(R.id.txt_saveLater);
                img_savelater = itemView.findViewById(R.id.img_savelater);
                rel_saveLater = itemView.findViewById(R.id.rel_saveLater);
                rel_removeCart = itemView.findViewById(R.id.rel_removeCart);
                lnr_remove = itemView.findViewById(R.id.lnr_remove);
                txt_actualprice.setPaintFlags(txt_actualprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                btn_subscribe.setVisibility(View.GONE);
                lnr_remove.setVisibility(View.VISIBLE);
                rel_cart.setVisibility(View.VISIBLE);
                txt_saveLater.setText("MOVE TO CART");
                Glide.with(CartActivity.this).load(R.drawable.carts).into(img_savelater);
            }

            public void bind(final CartModel model, final int position) {
                Glide.with(CartActivity.this).load(model.getImage()).into(img_product);
                txt_productName.setText(model.getProduct_name());
                txt_weight.setText(model.getUnit());
                try {
                    if (model.getDiscount().equals("0")) {
                        txt_discountprice.setText("Rs." + model.getPrice());
                        txt_actualprice.setVisibility(View.GONE);
                        rel_discount.setVisibility(View.GONE);
                    } else {
                        txt_actualprice.setVisibility(View.VISIBLE);
                        rel_discount.setVisibility(View.VISIBLE);
                        actualAmout = Double.parseDouble(model.getPrice());
                        /*discount = (actualAmout * Double.parseDouble(model.getDiscount())) / 100;
                        payableAmt = actualAmout - discount;
                        DecimalFormat twoDForm = new DecimalFormat("#");
                        payableAmt = Double.valueOf(twoDForm.format(payableAmt));

                        txt_discountprice.setText("Rs." + (int) Math.round(payableAmt));
                        txt_actualprice.setText("Rs." + model.getPrice());
                        txt_discount.setText(model.getDiscount() + "% off");*/

                        payableAmt = actualAmout - Double.parseDouble(model.getDiscount());
                        long price_amount = Math.round(Double.valueOf(payableAmt));

                        txt_discountprice.setText("Rs." + price_amount);
                        txt_actualprice.setText("Rs." + model.getPrice());
                        txt_discount.setText("Save\n\u20B9" + model.getDiscount());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                /*if (model.getFlag().equals("0")) {
                    txt_stock.setVisibility(View.VISIBLE);
                } else if (model.getFlag().equals("1")) {
                    txt_stock.setVisibility(View.GONE);
                }*/

                if (model.getQty().equals("0")) {
                    rel_cart.setVisibility(View.GONE);
                    btn_cart.setVisibility(View.VISIBLE);
                } else {
                    rel_cart.setVisibility(View.VISIBLE);
                    btn_cart.setVisibility(View.GONE);
                    qty = Integer.valueOf(model.getQty());
                    txt_count.setText(String.valueOf(qty));
                }

                rel_saveLater.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addCart(model.getProduct_id(), model.getUnit(), model.getPrice(), model.getDiscount(), txt_count.getText().toString());
                    }
                });

                rel_removeCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeSaveLater(model.getCart_id(), position);
                    }
                });

                /*btn_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(model.getFlag().equals("1")) {
                            rel_cart.setVisibility(View.VISIBLE);
                            btn_cart.setVisibility(View.GONE);
                            addCart(model.getProduct_id(), model.getUnit(), model.getPrice(), model.getGst(), model.getDiscount(), txt_count.getText().toString());
                        }else{
                            Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/

                /*rel_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (qty >= 1) {
                            qty--;
                            if (qty != 0) {
                                txt_count.setText(String.valueOf(qty));
                                addCart(model.getProduct_id(),model.getUnit(),model.getPrice(),model.getGst(),model.getDiscount(),txt_count.getText().toString());
                            } else {
                                rel_cart.setVisibility(View.GONE);
                                btn_cart.setVisibility(View.VISIBLE);
                                addCart(model.getProduct_id(),model.getUnit(),model.getPrice(),model.getGst(),model.getDiscount(),txt_count.getText().toString());
                            }
                        }
                    }
                });*/

                /*rel_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        qty++;
                        txt_count.setText(String.valueOf(qty));
                        addCart(model.getProduct_id(),model.getUnit(),model.getPrice(),model.getGst(),model.getDiscount(),txt_count.getText().toString());

                    }
                });*/

                lnr_product.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(new Intent(CartActivity.this, ProductDetailsActivity.class));
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);

                    }
                });

                /*btn_subscribe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (model.getFlag().equals("0")) {
                            Toast.makeText(context, "Out of stock", Toast.LENGTH_SHORT).show();
                        } else if (model.getFlag().equals("1")) {
                            btn_subscribe.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_button_layout));
                            btn_subscribe.setTextColor(getResources().getColor(R.color.colorWhite));
                            lin_day.setVisibility(View.VISIBLE);
                            card_bottom.setVisibility(View.GONE);
                        }
                    }
                });*/
            }

            public void addCart(final String prod_id, final String unit, final String price, final String discount, final String qty1) {
//                dialog.show();
                if (APIURLs.isNetwork(CartActivity.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.ADDCART_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String status = jsonObject1.getString("status");
                                if (status.equals("1")) {
                                    /*qty = 0;*/
//                                    getProductList();
//                                    getSaveLaterList();
                                } else {
                                    Toast.makeText(CartActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                            params.put("customer_id", SharedPref.getVal(CartActivity.this, SharedPref.customer_id));
                            params.put("product_id", prod_id);
                            params.put("unit", unit);
                            params.put("price", price);
                            params.put("discount", discount);
                            params.put("qty", qty1);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    dismissDialog();
                    FunctionConstant.noInternetDialog(CartActivity.this, "no internet connection");
                }
            }

            public void removeSaveLater(final String cart_id, final int position) {
//                dialog.show();
                if (APIURLs.isNetwork(CartActivity.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.REMOVESAVELATER_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String status = jsonObject1.getString("status");
                                if (status.equals("1")) {
                                    notifyDataSetChanged();
                                    notifyItemRemoved(position);
                                    savelaterModelArrayList.remove(position);
                                    if (savelaterModelArrayList.size() < 0) {
                                        lnr_saverlater.setVisibility(View.GONE);
                                    } else {
                                        lnr_saverlater.setVisibility(View.VISIBLE);
                                    }
//                                    getSaveLaterList();
                                } else {
                                    Toast.makeText(CartActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                            params.put("save_id", cart_id);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    dismissDialog();
                    FunctionConstant.noInternetDialog(CartActivity.this, "no internet connection");
                }
            }
        }
    }

    public class SlotAdapter extends RecyclerView.Adapter<SlotAdapter.ViewHolder> {
        Context context;
        private RadioButton selectedRadioButton;

        public SlotAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(CartActivity.this).inflate(R.layout.slot_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            SlotModel model = slotModelArrayList.get(position);
            ((ViewHolder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return slotModelArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            RadioButton rb_time;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                rb_time = itemView.findViewById(R.id.rb_time);
            }

            public void bind(final SlotModel model, final int position) {
                txt_time.setText("Expected Delivery by " + model.getDay());

                rb_time.setText(model.getSlot_timing() + " ( by " + model.getDay() + ")");
                rb_time.setChecked(false);
                /*selectedRadioButton.setChecked(false);*/
                slotModelArrayList.get(position).setSelected(false);

                if (selectedIndex != -1) {
                    if (selectedIndex == position) {
                        rb_time.setChecked(true);
                        slotModelArrayList.get(position).setSelected(true);
                    } else {
                        rb_time.setChecked(false);
                    }
                }

                rb_time.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        // Set unchecked all other elements in the list, so to display only one selected radio button at a time
                        for (SlotModel model : slotModelArrayList)
                            model.setSelected(false);

                        // Set "checked" the model associated to the clicked radio button
                        slotModelArrayList.get(position).setSelected(true);
                        /*Toast.makeText(context, timingModelArrayList.get(position).getDay(), Toast.LENGTH_SHORT).show();*/

                        // If current view (RadioButton) differs from previous selected radio button, then uncheck selectedRadioButton
                        if (null != selectedRadioButton && !v.equals(selectedRadioButton))
                            selectedRadioButton.setChecked(false);

                        // Replace the previous selected radio button with the current (clicked) one, and "check" it
                        selectedIndex = position;
                        selectedRadioButton = (RadioButton) v;
                        selectedRadioButton.setChecked(true);
                        notifyDataSetChanged();
                    }
                });
            }

        }
    }
}
