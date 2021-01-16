package com.sterlex.in.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.gmail.samehadar.iosdialog.IOSDialog;
import com.sterlex.in.Constant.APIURLs;
import com.sterlex.in.Constant.FunctionConstant;
import com.sterlex.in.Constant.OrderSharedPrefence;
import com.sterlex.in.Constant.SharedPref;
import com.sterlex.in.Model.AddressModel;
import com.sterlex.in.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ManageAddressActivity extends AppCompatActivity {

    CardView card_newAddress, card_address;
    ImageView img_back;
    RecyclerView rec_list;
    TextView txt_nodata;
    IOSDialog dialog;
    String from = "", del_charges = "", address = "";
    LinearLayout lin_parent;
    ShimmerFrameLayout shimmer_view_container;
    ArrayList<AddressModel> addressModelArrayList = new ArrayList<>();
    String product_id = "", grand_total = "", sub_total = "", coupon_id = "", slot_id = "", unit_price = "", discount_amt = "", coupon_amt = "", qty = "";
    SearchView searchView;
    Filter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        init();
        getIntentData();
        onClick();
        getAddressList();

        OrderSharedPrefence.putVal(ManageAddressActivity.this, OrderSharedPrefence.pickUp_id,"");
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        del_charges = intent.getStringExtra("del_charges");


    }

    private void getIntentData() {
        product_id = getIntent().getStringExtra("product_id");
        grand_total = getIntent().getStringExtra("grand_total");
        sub_total = getIntent().getStringExtra("sub_total");
        coupon_id = getIntent().getStringExtra("coupon_id");
        slot_id = getIntent().getStringExtra("slot_id");
        unit_price = getIntent().getStringExtra("unit_price");
        discount_amt = getIntent().getStringExtra("discount_amt");
        coupon_amt = getIntent().getStringExtra("coupon_amt");
        qty = getIntent().getStringExtra("qty");

    }

    private void onClick() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        card_newAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* AlertDialog.Builder alertDialog = new AlertDialog.Builder(ManageAddressActivity.this);
                // Setting Dialog Title
                alertDialog.setTitle("Address");
                // Setting Dialog Message
                alertDialog.setMessage("Do you want to add new address?");
                // On pressing Settings button
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                // on pressing cancel button
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                // Showing Alert Message
                alertDialog.show();*/

//                startActivity(new Intent(ManageAddressActivity.this, NewAddressAddingActivity.class));

                Intent intent = new Intent(ManageAddressActivity.this, NewAddressAddingActivity.class);
                intent.putExtra("from", from);
                intent.putExtra("del_charges", del_charges);
                startActivity(intent);

                finish();
            }
        });
    }
//
//    public void dismissDialog() {
//        if (dialog != null && dialog.isShowing()) {
//            dialog.dismiss();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (dialog != null && dialog.isShowing()) {
//            dialog.dismiss();
//        }
//    }

    private void init() {
        dialog = new IOSDialog.Builder(ManageAddressActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();
        shimmer_view_container = findViewById(R.id.shimmer_view_container);
        lin_parent = findViewById(R.id.lnr_main);
        card_newAddress = findViewById(R.id.card_newAddress);
        img_back = findViewById(R.id.img_back);
        rec_list = findViewById(R.id.rec_list);
        txt_nodata = findViewById(R.id.txt_nodata);
        searchView = findViewById(R.id.searchView);
        rec_list.setLayoutManager(new LinearLayoutManager(ManageAddressActivity.this));
//        rec_list.setAdapter(new AddressAdapter());



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                try {
                    filter.filter(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

    }

    public void getAddressList() {
//        dialog.show();

        shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        lin_parent.setVisibility(View.GONE);
        if (APIURLs.isNetwork(ManageAddressActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.customer_address_record, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            addressModelArrayList = new ArrayList<>();
                            if (jsonObject.has("result")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    AddressModel model = new AddressModel();
                                    model.setId(jsonObject1.getString("id"));
                                    model.setBuilding_name(jsonObject1.getString("building_name"));
                                    model.setLandmark(jsonObject1.getString("landmark"));
                                    model.setArea_id(jsonObject1.getString("area_id"));
                                    model.setArea_name(jsonObject1.getString("area_name"));
                                    model.setPincode(jsonObject1.getString("pincode"));
                                    model.setCity(jsonObject1.getString("city"));
                                    model.setState(jsonObject1.getString("state"));
                                    model.setFullname(jsonObject1.getString("fullname"));
                                    model.setMobile(jsonObject1.getString("mobile"));
                                    addressModelArrayList.add(model);
                                }
                                Collections.reverse(addressModelArrayList);
                                AddressAdapter adapter = new AddressAdapter();
                                rec_list.setAdapter(adapter);
                                rec_list.setVisibility(View.VISIBLE);
                                txt_nodata.setVisibility(View.GONE);
                                filter = adapter.getFilter();
                            }
                        } else {
                            rec_list.setVisibility(View.GONE);
                            txt_nodata.setVisibility(View.VISIBLE);
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
                    params.put("customer_id", SharedPref.getVal(ManageAddressActivity.this, SharedPref.customer_id));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            final Dialog dialog = new Dialog(ManageAddressActivity.this);
            dialog.setContentView(R.layout.no_internet_dialog);
            Button button = dialog.findViewById(R.id.btn_process);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAddressList();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> implements Filterable {

        ArrayList<AddressModel> arrayList1;

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ManageAddressActivity.this).inflate(R.layout.address_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            AddressModel model = addressModelArrayList.get(position);
            ((ViewHolder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return addressModelArrayList.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    FilterResults results = new FilterResults();
                    ArrayList<AddressModel> modelArrayList = new ArrayList<>();
                    if (arrayList1 == null)
                        arrayList1 = addressModelArrayList;
                    if (arrayList1 != null && arrayList1.size() > 0) {
                        for (final AddressModel model : arrayList1) {
                            if (model.getFullname().toLowerCase().contains(charSequence.toString())||
                                    model.getBuilding_name().toLowerCase().contains(charSequence.toString())||
                                    model.getLandmark().toLowerCase().contains(charSequence.toString())||
                                    model.getArea_name().toLowerCase().contains(charSequence.toString())||
                                    model.getCity().toLowerCase().contains(charSequence.toString())||
                                     model.getState().toLowerCase().contains(charSequence.toString())||
                                     model.getPincode().toLowerCase().contains(charSequence.toString())) {
                                modelArrayList.add(model);
                            }
                        }
                        results.values = modelArrayList;
                    }
                    results.values = modelArrayList;
                    results.count = modelArrayList.size();
                    return results;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    addressModelArrayList = (ArrayList<AddressModel>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textViewOptions, txt_map, txt_address, txt_address_full;
            CardView card_address;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                card_address = itemView.findViewById(R.id.card_address);
                textViewOptions = itemView.findViewById(R.id.textViewOptions);
                txt_map = itemView.findViewById(R.id.txt_map);
                txt_address = itemView.findViewById(R.id.txt_address);
                txt_address_full = itemView.findViewById(R.id.txt_address_full);
                card_address = itemView.findViewById(R.id.card_address);

                txt_address.setVisibility(View.GONE);
                txt_map.setVisibility(View.GONE);
            }

            public void bind(final AddressModel model, final int position) {
                final String str = "<b>" + model.getFullname() + "</b><br> " +
                        model.getBuilding_name() + ", " + model.getLandmark() + ", " +
                        model.getArea_name() + ", " + model.getCity() + ", " + model.getState() + " " + model.getPincode();

                final String addr_str = model.getBuilding_name() + ", " + model.getLandmark() + ", " +
                        model.getArea_name() + ", " + model.getCity() + ", " + model.getState() + " " + model.getPincode();
                txt_address_full.setText(Html.fromHtml(str));
                card_address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        String addr= Html.fromHtml(addr_str).toString();
                        OrderSharedPrefence.putVal(ManageAddressActivity.this,OrderSharedPrefence.delivery_status,"0");
                        OrderSharedPrefence.putVal(ManageAddressActivity.this,OrderSharedPrefence.deliver_address,addr);
                        OrderSharedPrefence.putVal(ManageAddressActivity.this,OrderSharedPrefence.area_id,model.getArea_id());
                        Intent intent = new Intent(ManageAddressActivity.this, DeliveryAddressActivity.class);
                        intent.putExtra("addr", addr);
                        intent.putExtra("from", "addr");
                        startActivity(intent);
                        finish();
                    }
                });
                textViewOptions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //creating a popup menu
                        PopupMenu popup = new PopupMenu(ManageAddressActivity.this, textViewOptions);
                        //inflating menu from xml resource
                        popup.inflate(R.menu.options_menu);
                        //adding click listener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.menu_delete:
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ManageAddressActivity.this);
                                        // Setting Dialog Title
                                        alertDialog.setTitle("Delete Address");
                                        // Setting Dialog Message
                                        alertDialog.setMessage("Do you want to delete this address?");
                                        // On pressing Settings button
                                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                deleteAddress(model.getId(), position);
                                                dialog.dismiss();
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
                                        return true;
                                    case R.id.menu_edit:
                                        AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(ManageAddressActivity.this);
                                        // Setting Dialog Title
                                        alertDialog1.setTitle("Update Address");
                                        // Setting Dialog Message
                                        alertDialog1.setMessage("Do you want to update this address?");
                                        // On pressing Settings button
                                        alertDialog1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(ManageAddressActivity.this, UpdateAddressAddingActivity.class);
                                                intent.putExtra("model", model);
                                                startActivity(intent);
                                                finish();
                                                dialog.dismiss();
                                            }
                                        });
                                        // on pressing cancel button
                                        alertDialog1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                        // Showing Alert Message
                                        alertDialog1.show();
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });
                        //displaying the popup
                        popup.show();
                    }
                });

               /* card_address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ManageAddressActivity.this);
                        // Setting Dialog Title
                        alertDialog.setTitle("Address");
                        // Setting Dialog Message
                        alertDialog.setMessage("Do you want to continue with this address?");
                        // On pressing Settings button
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ManageAddressActivity.this, SelectPaymentModeActivity.class);
                                intent.putExtra("product_id",product_id);
                                intent.putExtra("grand_total",grand_total);
                                intent.putExtra("sub_total",sub_total);
                                intent.putExtra("coupon_id",coupon_id);
                                intent.putExtra("slot_id",slot_id);
                                intent.putExtra("unit_price",unit_price);
                                intent.putExtra("discount_amt",discount_amt);
                                intent.putExtra("coupon_amt",coupon_amt);
                                intent.putExtra("qty",qty);
                                intent.putExtra("address",txt_address.getText().toString());
                                startActivity(intent);
                                finish();
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
                });*/
            }


            public void deleteAddress(final String id, final int position) {
                dialog.show();
                if (APIURLs.isNetwork(ManageAddressActivity.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.address_delete, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String status = jsonObject1.getString("status");
                                if (status.equals("1")) {
                                    Toast.makeText(ManageAddressActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                    notifyItemRemoved(position);
                                    addressModelArrayList.remove(position);
                                    getAddressList();
                                } else {
                                    Toast.makeText(ManageAddressActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                            params.put("id", id);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(ManageAddressActivity.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    dismissDialog();
                    FunctionConstant.noInternetDialog(ManageAddressActivity.this, "no internet connection");
                }
            }
        }
    }

    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        shimmer_view_container.stopShimmerAnimation();
        lin_parent.setVisibility(View.VISIBLE);
        shimmer_view_container.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        shimmer_view_container.stopShimmerAnimation();
        lin_parent.setVisibility(View.VISIBLE);
        shimmer_view_container.setVisibility(View.GONE);
    }


    @Override
    protected void onPause() {
        shimmer_view_container.stopShimmerAnimation();
        lin_parent.setVisibility(View.VISIBLE);
        shimmer_view_container.setVisibility(View.GONE);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        shimmer_view_container.startShimmerAnimation();
//        lin_parent.setVisibility(View.GONE);
//        shimmer_view_container.setVisibility(View.VISIBLE);
//        if (FunctionConstant.GPSRuntime(CartActivity.this)) {
//            getCurrentLocation();
//        } else {
//            showSettingsAlert();
//        }
    }

}
