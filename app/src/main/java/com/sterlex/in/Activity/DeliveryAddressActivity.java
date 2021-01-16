package com.sterlex.in.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.sterlex.in.Constant.OrderSharedPrefence;
import com.sterlex.in.Constant.SharedPref;
import com.sterlex.in.Model.SlotModel;
import com.sterlex.in.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;


public class DeliveryAddressActivity extends AppCompatActivity {


    RelativeLayout rel_ordersummary, rel_discount, rel_del_charges;
    LinearLayout order_detail, order_hide_detail, lin_details, lin_pickup1, lin_pickup2, lin_home1, lin_home2, lin_address;
    ImageView img_arrow, img_del;
    String from = "", discount = "", address = "", total = "", actual_delivery_charge = "";
    TextView txt_home_del, txt_del_chrgs, txt_edit_add;
    TextView edt_pickup, edt_pickup_time, edt_addr, edt_del_tim;
    private static final int REQUEST_OPERATOR = 1;
    private static final int REQUEST_OPERATOR2 = 2;
    String pick_address = "", maddr = "", cost_id = "", percent = "", del_chrg = "", order_amount = "";
    IOSDialog dialog;
    boolean checkoutFlag = false;
    ArrayList<SlotModel> slotModels = new ArrayList<>();
    TextView txt_total, txt_discount, txt_del_charges, txt_grandTotal;
    ArrayList<SlotModel> slotArraylist = new ArrayList<>();
    public Dialog dialog1;
    Double calculatedDeliveryCharge = 0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);



        from = getIntent().getStringExtra("from");
//        del_charges = intent.getStringExtra("del_charges");
//        address = getIntent().getStringExtra("addr");
//        total = intent.getStringExtra("total");
        total = OrderSharedPrefence.getVal(DeliveryAddressActivity.this, OrderSharedPrefence.total_bag);
        discount = OrderSharedPrefence.getVal(DeliveryAddressActivity.this, OrderSharedPrefence.total_discount);
        cost_id = SharedPref.getVal(DeliveryAddressActivity.this, SharedPref.customer_id);


        init();
        if (!SharedPref.getVal(DeliveryAddressActivity.this, SharedPref.address).equals(""))
            edt_addr.setText(SharedPref.getVal(DeliveryAddressActivity.this, OrderSharedPrefence.deliver_address));

        onClick();
        deliveryCharges();


    }

    private void pickUpTiming() {
        dialog.show();
        if (APIURLs.isNetwork(DeliveryAddressActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.slot_time, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            slotModels = new ArrayList<>();
                            if (jsonObject.has("body")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("body");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    SlotModel model = new SlotModel();
                                    model.setDay(jsonObject1.getString("dayname"));

                                    String date = jsonObject1.getString("date");
//                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
//                                    try {
//                                        Date d = dateFormat.parse(date);
//                                        model.setSlot_timing(d.toString());
//
//                                    } catch (Exception e) {
//                                        //java.text.ParseException: Unparseable date: Geting error
//                                        e.printStackTrace();
//                                    }


                                    model.setSlot_timing(date);
                                    slotArraylist = new ArrayList<>();

                                    JSONArray jsonArray1 = jsonObject1.getJSONArray("slot_data");
                                    if (jsonArray1.length() > 0) {

                                        for (int j = 0; j < jsonArray1.length(); j++) {
                                            JSONObject jsonObject11 = jsonArray1.getJSONObject(j);
                                            SlotModel model1 = new SlotModel();

                                            model1.setSlot_timing(jsonObject11.getString("slot_time"));
                                            model1.setSlot_id(jsonObject11.getString("slot_id"));
                                            model1.setDisabled(jsonObject11.getString("disabled"));
                                            slotArraylist.add(model1);

                                        }
                                        model.setSlotArraylist(slotArraylist);
                                    }
                                    slotModels.add(model);
                                }
                                timingDialog();

                            }
                        } else {
//                            rec_list.setVisibility(View.GONE);
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
//                    params.put("customer_id", SharedPref.getVal(DeliveryAddressActivity.this, SharedPref.customer_id));

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            final Dialog dialog = new Dialog(DeliveryAddressActivity.this);
            dialog.setContentView(R.layout.no_internet_dialog);
            Button button = dialog.findViewById(R.id.btn_process);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickUpTiming();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    private void deliveryCharges() {
        dialog.show();
        if (APIURLs.isNetwork(DeliveryAddressActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.Delivery_Percentage_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("200")) {
                            slotModels = new ArrayList<>();
                            if (jsonObject.has("body")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("body");

                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                                percent = jsonObject1.getString("percentage");
                                del_chrg = jsonObject1.getString("delivery_charge_amount");
                                order_amount = jsonObject1.getString("order_amount");

                                OrderSharedPrefence.putVal(DeliveryAddressActivity.this, OrderSharedPrefence.percentage, percent);
                                OrderSharedPrefence.putVal(DeliveryAddressActivity.this, OrderSharedPrefence.delivery_charge_amount, del_chrg);
                                OrderSharedPrefence.putVal(DeliveryAddressActivity.this, OrderSharedPrefence.order_amount, order_amount);

                                if (!from.equals("self") || from.equals("pickUpaddr")) {
                                    Double price = Double.valueOf(total);
                                    Double amount = Double.valueOf(order_amount);
                                    if (price <= amount) {
                                        //amount_calculation
                                        OrderSharedPrefence.putVal(DeliveryAddressActivity.this, OrderSharedPrefence.delivery_charges, del_chrg);
                                        OrderSharedPrefence.putVal(DeliveryAddressActivity.this, OrderSharedPrefence.delivery_flag, "amount");
                                        actual_delivery_charge = del_chrg;
                                        long rounded_actual_delivery_charge = Math.round(Double.valueOf(actual_delivery_charge));
                                        txt_del_charges.setText("\u20B9" + rounded_actual_delivery_charge);
                                        txt_del_chrgs.setText("Delivery charges\n\u20B9" + rounded_actual_delivery_charge);

                                        Double grandTotal = Double.valueOf(total) - Double.valueOf(discount) + Double.valueOf(del_chrg);
                                        long rounded = Math.round(grandTotal);
                                        txt_grandTotal.setText("\u20B9" + rounded);


                                    } else {
                                        //percent calculation
                                        OrderSharedPrefence.putVal(DeliveryAddressActivity.this, OrderSharedPrefence.delivery_percent, percent);
                                        OrderSharedPrefence.putVal(DeliveryAddressActivity.this, OrderSharedPrefence.delivery_flag, "percent");
                                        calculatedDeliveryCharge = (Double.valueOf(total) * Double.valueOf(percent) / 100);
                                        actual_delivery_charge = calculatedDeliveryCharge.toString();
                                        long rounded_actual_delivery_charge = Math.round(Double.valueOf(actual_delivery_charge));
//                                        Toast.makeText(DeliveryAddressActivity.this, String.valueOf(rounded_actual_delivery_charge), Toast.LENGTH_SHORT).show();
                                        txt_del_charges.setText("\u20B9" + rounded_actual_delivery_charge);
                                        txt_del_chrgs.setText("Delivery charges\n\u20B9" + rounded_actual_delivery_charge);

                                        Double grandTotal = Double.valueOf(total) - Double.valueOf(discount) + Double.valueOf(actual_delivery_charge);

                                        long rounded = Math.round(grandTotal);
//                                        Toast.makeText(DeliveryAddressActivity.this, String.valueOf(rounded), Toast.LENGTH_SHORT).show();
                                        txt_grandTotal.setText("\u20B9" + rounded);

                                        OrderSharedPrefence.putVal(DeliveryAddressActivity.this, OrderSharedPrefence.delivery_charges, actual_delivery_charge);

                                    }
                                } else {
                                    Double grandTotal = Double.valueOf(total) - Double.valueOf(discount);
                                    long rounded = Math.round(grandTotal);
                                    txt_grandTotal.setText("\u20B9" + rounded);

                                    txt_del_charges.setText("FREE");
                                    rel_del_charges.setVisibility(View.VISIBLE);
                                    txt_del_charges.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    OrderSharedPrefence.putVal(DeliveryAddressActivity.this, OrderSharedPrefence.delivery_charges, "0");


                                }
                            }
                        } else {
                            Toast.makeText(DeliveryAddressActivity.this, "Error", Toast.LENGTH_SHORT).show();
//                            rec_list.setVisibility(View.GONE);
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
//                    params.put("customer_id", SharedPref.getVal(DeliveryAddressActivity.this, SharedPref.customer_id));

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            final Dialog dialog = new Dialog(DeliveryAddressActivity.this);
            dialog.setContentView(R.layout.no_internet_dialog);
            Button button = dialog.findViewById(R.id.btn_process);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickUpTiming();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    private void init() {
        dialog = new IOSDialog.Builder(DeliveryAddressActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();

        rel_ordersummary = findViewById(R.id.rel_ordersummary);
        rel_discount = findViewById(R.id.rel_discount);
        rel_del_charges = findViewById(R.id.rel_del_charges);
        lin_details = findViewById(R.id.lin_details);
        order_detail = findViewById(R.id.order_detail);
        order_hide_detail = findViewById(R.id.order_hide_detail);
        img_arrow = findViewById(R.id.img_arrow);
        img_del = findViewById(R.id.img_del);
        txt_home_del = findViewById(R.id.txt_home_del);
        txt_del_chrgs = findViewById(R.id.txt_del_chrgs);
        txt_edit_add = findViewById(R.id.txt_edit_add);
        edt_pickup = findViewById(R.id.edt_pickup);
        edt_pickup_time = findViewById(R.id.edt_pickup_time);
        edt_addr = findViewById(R.id.edt_addr);
        edt_addr.setSelected(true);
        edt_del_tim = findViewById(R.id.edt_del_tim);
        lin_home1 = findViewById(R.id.lin_home1);
        lin_home2 = findViewById(R.id.lin_home2);
        lin_pickup1 = findViewById(R.id.lin_pickup1);
        lin_pickup2 = findViewById(R.id.lin_pickup2);
        lin_address = findViewById(R.id.lin_address);
        txt_total = findViewById(R.id.txt_total);
        txt_discount = findViewById(R.id.txt_discount);
        txt_del_charges = findViewById(R.id.txt_del_charges);
        txt_grandTotal = findViewById(R.id.txt_grandTotal);

        long total_amount = Math.round(Double.valueOf(total));
        txt_total.setText("\u20B9" + total_amount);

        if (discount.equals("0")) {
            rel_discount.setVisibility(GONE);
        } else {
            long discount_amount = Math.round(Double.valueOf(discount));
            txt_discount.setText("\u20B9" + discount_amount);
        }

        if (from.equals("home")) {
            img_del.setImageDrawable(getResources().getDrawable(R.drawable.home_delivery));
            txt_home_del.setText("Home Delivery");
            txt_del_chrgs.setText("Delivery charges\n\u20B9" + del_chrg);
            lin_home1.setVisibility(View.VISIBLE);
            lin_home2.setVisibility(View.VISIBLE);
            lin_pickup1.setVisibility(View.GONE);
            lin_pickup2.setVisibility(View.GONE);
        } else if (from.equals("self")) {
            img_del.setImageDrawable(getResources().getDrawable(R.drawable.self_pickup));
            txt_home_del.setText("Self Pickup");
            txt_del_chrgs.setText("Delivery free");
            txt_edit_add.setVisibility(View.INVISIBLE);
            lin_address.setClickable(false);
            lin_address.setEnabled(false);
            rel_del_charges.setVisibility(View.GONE);
            lin_home1.setVisibility(View.GONE);
            lin_home2.setVisibility(View.GONE);
            lin_pickup1.setVisibility(View.VISIBLE);
            lin_pickup2.setVisibility(View.VISIBLE);

        } else if (from.equals("pickUpaddr")) {
            img_del.setImageDrawable(getResources().getDrawable(R.drawable.self_pickup));
            txt_home_del.setText("Self Pickup");
            txt_del_chrgs.setText("Delivery free");
            txt_edit_add.setVisibility(View.INVISIBLE);
            lin_address.setClickable(false);
            lin_address.setEnabled(false);
            lin_home1.setVisibility(View.GONE);
            rel_del_charges.setVisibility(View.GONE);
            lin_home2.setVisibility(View.GONE);
            lin_pickup1.setVisibility(View.VISIBLE);
            lin_pickup2.setVisibility(View.VISIBLE);
            maddr = getIntent().getStringExtra("addr");

            edt_addr.setText(maddr);


        } else if (from.equals("addr")) {
            img_del.setImageDrawable(getResources().getDrawable(R.drawable.home_delivery));
            txt_home_del.setText("Home Delivery");
            txt_del_chrgs.setText("Delivery charges\n\u20B9" + del_chrg);
            lin_home1.setVisibility(View.VISIBLE);
            lin_home2.setVisibility(View.VISIBLE);
            lin_pickup1.setVisibility(View.GONE);
            lin_pickup2.setVisibility(View.GONE);
            maddr = getIntent().getStringExtra("addr");
            edt_addr.setText(maddr);
        }

//        if(!maddr.equals("")){
//            edt_addr.setText(maddr);
//        }

//        edt_addr.setText(SharedPref.getVal(DeliveryAddressActivity.this, com.sterlex.in.Constant.SharedPref.address));


    }

    private void onClick() {
        rel_ordersummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkoutFlag) {

                    startActivity(new Intent(DeliveryAddressActivity.this, SelectPaymentModeActivity.class));
                    finish();

                }
            }
        });
        txt_edit_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryAddressActivity.this, ManageAddressActivity.class);
                intent.putExtra("from", from);
                intent.putExtra("del_charges", discount);
                startActivity(intent);
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
//        lin_address.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(DeliveryAddressActivity.this, ManageAddressActivity.class);
//                intent.putExtra("from", from);
//                intent.putExtra("del_charges", discount);
//                startActivity(intent);
//
//
//            }
//        });
        edt_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryAddressActivity.this, ManageAddressActivity.class);
                intent.putExtra("from", from);
                intent.putExtra("del_charges", discount);
                startActivity(intent);
                finish();


            }
        });

        edt_del_tim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(DeliveryAddressActivity.this, DeliveryAddressActivity.class));
                if (!edt_addr.getText().toString().equals("")) {

                    pickUpTiming();
                } else {
                    Toast.makeText(DeliveryAddressActivity.this, "Please select delivery address", Toast.LENGTH_SHORT).show();
                }
            }
        });

        edt_pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryAddressActivity.this, PickPointsListingActivity.class);
                startActivityForResult(intent, REQUEST_OPERATOR);


            }
        });

        edt_pickup_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edt_pickup.getText().toString().equals("")) {

                    pickUpTiming();
                } else {
                    Toast.makeText(DeliveryAddressActivity.this, "Please select pick up location", Toast.LENGTH_SHORT).show();
                }
            }
        });
        img_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onBackPressed();
                finish();
            }
        });


    }

    public void timingDialog() {
        dialog1 = new Dialog(DeliveryAddressActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog_delivery_time_slot);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog1.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog1.getWindow().setGravity(Gravity.CENTER);
        dialog1.getWindow().setDimAmount((float) 0.7);
        dialog1.setCancelable(true);
        dialog1.setCanceledOnTouchOutside(true);

        RecyclerView rec_timeSlot = dialog1.findViewById(R.id.rec_timeSlot);

        rec_timeSlot.setAdapter(new DeliveryTimeSlotListingAdapter(DeliveryAddressActivity.this));

        rec_timeSlot.setLayoutManager(new LinearLayoutManager(DeliveryAddressActivity.this, RecyclerView.VERTICAL, false));

        dialog1.show();
    }


    public class DeliveryTimeSlotListingAdapter extends RecyclerView.Adapter<DeliveryTimeSlotListingAdapter.ItemViewholder> {
        Context context;


        public DeliveryTimeSlotListingAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public DeliveryTimeSlotListingAdapter.ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_slot_days_item_layout, parent, false);
            DeliveryTimeSlotListingAdapter.ItemViewholder itemViewholder = new DeliveryTimeSlotListingAdapter.ItemViewholder(view);
            return itemViewholder;
        }

        @Override
        public void onBindViewHolder(@NonNull DeliveryTimeSlotListingAdapter.ItemViewholder holder, int position) {
            SlotModel model = slotModels.get(position);
            ((DeliveryTimeSlotListingAdapter.ItemViewholder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return slotModels.size();
        }

        public class ItemViewholder extends RecyclerView.ViewHolder {
            ImageView iv;

            LinearLayout lin_top, lin_comm;
            CardView card_bg;
            RecyclerView recyc_times;
            TextView txt_day, txt_date;

            public ItemViewholder(@NonNull View itemView) {
                super(itemView);

                txt_day = itemView.findViewById(R.id.txt_day);
                txt_date = itemView.findViewById(R.id.txt_date);
                recyc_times = itemView.findViewById(R.id.recyc_times);
                lin_top = itemView.findViewById(R.id.lin_top);
            }

            public void bind(final SlotModel model, final int position) {
//                txt_name.setText("");
//
                txt_day.setText(model.getDay());

                try {
                    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
                    String inputDateStr = model.getSlot_timing();
                    Date date = inputFormat.parse(inputDateStr);
                    String outputDateStr = outputFormat.format(date);
                    txt_date.setText(outputDateStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                slotArraylist = new ArrayList<>();
                slotArraylist = model.getSlotArraylist();
                try {
                    if (slotArraylist != null) {
                        if (slotArraylist.size() > 0) {
                            recyc_times.setLayoutManager(new LinearLayoutManager(DeliveryAddressActivity.this, RecyclerView.VERTICAL, false));
                            TimeSlotListingAdapter adapter1 = new TimeSlotListingAdapter(DeliveryAddressActivity.this, slotArraylist, txt_date.getText().toString());
                            recyc_times.setAdapter(adapter1);

                        } else {
                            lin_top.setVisibility(GONE);
                        }
                    } else {

                        lin_top.setVisibility(GONE);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    lin_top.setVisibility(GONE);

                }


            }


            public class TimeSlotListingAdapter extends RecyclerView.Adapter<TimeSlotListingAdapter.ItemViewholder1> {
                Context context;
                ArrayList<SlotModel> slotArraylist;
                String date;

                public TimeSlotListingAdapter(Context context, ArrayList<SlotModel> slotArraylist, String date) {
                    this.context = context;
                    this.slotArraylist = slotArraylist;
                    this.date = date;
                }

                @NonNull
                @Override
                public TimeSlotListingAdapter.ItemViewholder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_slot_item_layout, parent, false);
                    TimeSlotListingAdapter.ItemViewholder1 itemViewholder1 = new TimeSlotListingAdapter.ItemViewholder1(view);
                    return itemViewholder1;
                }

                @Override
                public void onBindViewHolder(@NonNull TimeSlotListingAdapter.ItemViewholder1 holder, int position) {
                    SlotModel model = slotArraylist.get(position);
                    ((TimeSlotListingAdapter.ItemViewholder1) holder).bind(model, position);
                }

                @Override
                public int getItemCount() {
                    return slotArraylist.size();
                }

                public class ItemViewholder1 extends RecyclerView.ViewHolder {
                    ImageView iv;

                    LinearLayout lin_top, lin_comm;
                    CardView card_bg;
                    RelativeLayout lin_owner;
                    TextView txt_time,
                            txt_availabilty;

                    public ItemViewholder1(@NonNull View itemView) {
                        super(itemView);

                        txt_time = itemView.findViewById(R.id.txt_time);
                        txt_availabilty = itemView.findViewById(R.id.txt_availabilty);
                        lin_top = itemView.findViewById(R.id.lin_top);
                    }

                    public void bind(final SlotModel slotModel, final int position) {
//                txt_name.setText("");
//
                        txt_time.setText(slotModel.getSlot_timing());
                        if (slotModel.getDisabled().equals("false")) {
                            lin_top.setBackgroundColor(getResources().getColor(R.color.light_gray));
                            txt_availabilty.setText("SLOTS FULL");
                        } else {
                            lin_top.setBackgroundColor(getResources().getColor(R.color.white));
                            txt_availabilty.setText("AVAILABLE");
                        }


                        lin_top.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!slotModel.getDisabled().equals("false")) {
                                    edt_pickup_time.setText(slotModel.getSlot_timing() + ", " + date);
                                    edt_del_tim.setText(slotModel.getSlot_timing() + ", " + date);

                                    OrderSharedPrefence.putVal(DeliveryAddressActivity.this, OrderSharedPrefence.slot_id, slotModel.getSlot_id());

                                    dialog1.dismiss();

                                    if (!edt_addr.getText().toString().equals("") && !edt_del_tim.getText().toString().equals("")) {
                                        checkoutFlag = true;
                                        rel_ordersummary.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                    }

                                    if (!edt_pickup.getText().toString().equals("") && !edt_pickup_time.getText().toString().equals("")) {
                                        checkoutFlag = true;
                                        rel_ordersummary.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                    }

                                }
                            }
                        });


                    }

                }

            }

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_OPERATOR) {
            if (resultCode == RESULT_OK) {
                try {
                    pick_address = data.getStringExtra("addr");

                    edt_pickup.setText(pick_address);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        from = getIntent().getStringExtra("from");

    }

}