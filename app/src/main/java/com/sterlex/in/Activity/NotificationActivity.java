package com.sterlex.in.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.sterlex.in.Constant.APIURLs;
import com.sterlex.in.Constant.SharedPref;
import com.sterlex.in.Constant.SwipeToDeleteCallback;
import com.sterlex.in.Database.DatabaseHelper;
import com.sterlex.in.Model.NotificationModel;
import com.sterlex.in.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class NotificationActivity extends AppCompatActivity {

    ImageView img_back;
    RecyclerView rec_notification;
    ShimmerFrameLayout shimmer_view_container;
    TextView txt_nodata;
    ArrayList<NotificationModel> notificationModelArrayList = new ArrayList<>();
    String cost_id="";
    DatabaseHelper databaseHelper;
    NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        init();
        databaseHelper = new DatabaseHelper(NotificationActivity.this);

        onClick();
        cost_id = SharedPref.getVal(NotificationActivity.this, SharedPref.customer_id);
        enableSwipeToDeleteAndUndo();
    }

    private void onClick() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init() {
        img_back = findViewById(R.id.img_back);
        shimmer_view_container = findViewById(R.id.shimmer_view_container);
        txt_nodata = findViewById(R.id.txt_nodata);
        rec_notification = findViewById(R.id.rec_notification);
        rec_notification.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
        rec_notification.setAdapter(new NotificationAdapter());
    }

    public void dismissDialog() {
       /* if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }*/

        shimmer_view_container.stopShimmerAnimation();
        rec_notification.setVisibility(View.VISIBLE);
        shimmer_view_container.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shimmer_view_container.stopShimmerAnimation();
        rec_notification.setVisibility(View.VISIBLE);
        shimmer_view_container.setVisibility(View.GONE);
    }


    @Override
    protected void onPause() {
        shimmer_view_container.stopShimmerAnimation();
        rec_notification.setVisibility(View.VISIBLE);
        shimmer_view_container.setVisibility(View.GONE);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        shimmer_view_container.startShimmerAnimation();
        rec_notification.setVisibility(View.GONE);
        shimmer_view_container.setVisibility(View.VISIBLE);
        getNotificationList();
    }

    public void getNotificationList() {
//        dialog.show();
        shimmer_view_container.startShimmerAnimation();
        shimmer_view_container.setVisibility(View.VISIBLE);
        rec_notification.setVisibility(View.GONE);
        if (APIURLs.isNetwork(NotificationActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.notification_list, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
//                            notificationModelArrayList = new ArrayList<>();
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                if(!databaseHelper.ifExists("notification_id",jsonObject1.getString("notification_id"))){
                                    if(!databaseHelper.ifDeleted("is_deleted","1")) {
                                        databaseHelper.addNotification(jsonObject1.getString("notification_id"),
                                                jsonObject1.getString("title"),
                                                jsonObject1.getString("message"),
                                                jsonObject1.getString("image"), "0");
                                    }
                                }
                                NotificationModel model = new NotificationModel();
                                model.setNotification_id(jsonObject1.getString("notification_id"));
                                model.setTitle(jsonObject1.getString("title"));
                                model.setImage(jsonObject1.getString("image"));
                                model.setMessage(jsonObject1.getString("message"));
                                notificationModelArrayList.add(model);
                            }
                           /* NotificationAdapter adapter = new NotificationAdapter();
                            rec_notification.setAdapter(adapter);
                            txt_nodata.setVisibility(View.GONE);
                            rec_notification.setVisibility(View.VISIBLE);*/
                            int size = notificationModelArrayList.size();
                            String old_count = databaseHelper.getOldCount();
                            if(old_count.equals("")){
                                old_count = "0";
                            }
                            if (old_count.equals("0") && size > Integer.parseInt(old_count)) {
                                databaseHelper.addcount(String.valueOf(size), old_count);
                            }else{
                                String count = String.valueOf(notificationModelArrayList.size());
                                databaseHelper.updateCount(String.valueOf(size),count);
                            }

                           getNotification();
                        } else {
                            txt_nodata.setVisibility(View.VISIBLE);
                            rec_notification.setVisibility(View.GONE);
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
            });
//            {
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    HashMap<String, String> params = new HashMap<>();
//                    if (!cost_id.equals("")) {
//                        params.put("franchise_id", SharedPref.getVal(NotificationActivity.this, SharedPref.fran_code));
//                    } else {
//                        params.put("franchise_id", "1");
//                    }
//                    return params;
//                }
//            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            final Dialog dialog = new Dialog(NotificationActivity.this);
            dialog.setContentView(R.layout.no_internet_dialog);
            Button button = dialog.findViewById(R.id.btn_process);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getNotificationList();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public void getNotification(){
        notificationModelArrayList = new ArrayList<>();
        notificationModelArrayList = databaseHelper.getNotificationList();
        Collections.reverse(notificationModelArrayList);

        if(notificationModelArrayList.size()>0){
            adapter = new NotificationAdapter();
            rec_notification.setAdapter(adapter);
            txt_nodata.setVisibility(View.GONE);
            rec_notification.setVisibility(View.VISIBLE);
        }else{
            txt_nodata.setVisibility(View.VISIBLE);
            rec_notification.setVisibility(View.GONE);
        }
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final String item = adapter.getData().get(position).getNotification_id();

                adapter.removeItem(position);


//                Snackbar snackbar = Snackbar
//                        .make(coordinatorLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
//                snackbar.setAction("UNDO", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        mAdapter.restoreItem(item, position);
//                        recyclerView.scrollToPosition(position);
//                    }
//                });

//                snackbar.setActionTextColor(Color.YELLOW);
//                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(rec_notification);
    }


    public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(NotificationActivity.this).inflate(R.layout.notification_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            NotificationModel model = notificationModelArrayList.get(position);
            ((ViewHolder) holder).bind(model,position);
        }

        @Override
        public int getItemCount() {
            return notificationModelArrayList.size();
        }


        public void removeItem(int position) {
            databaseHelper.removeNotification(notificationModelArrayList.get(position).getNotification_id(),"1");
            notificationModelArrayList.remove(position);
            notifyItemRemoved(position);
        }

        public ArrayList<NotificationModel> getData() {
            return notificationModelArrayList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txt_title, txt_desc;
            ImageView img_notification;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                txt_title = itemView.findViewById(R.id.txt_title);
                txt_desc = itemView.findViewById(R.id.txt_desc);
                img_notification = itemView.findViewById(R.id.img_notification);
            }

            public void bind(NotificationModel model , int position){
                txt_title.setText(model.getTitle());
                txt_desc.setText(model.getMessage());
                if(model.getImage().equals("")){
                    img_notification.setVisibility(View.GONE);
                }else{
                    img_notification.setVisibility(View.VISIBLE);
                    Glide.with(NotificationActivity.this)
                            .load(model.getImage())
                            .error(R.drawable.loader).
                            placeholder(R.drawable.loader)
                            .into(img_notification);
                }
            }
        }
    }
}
