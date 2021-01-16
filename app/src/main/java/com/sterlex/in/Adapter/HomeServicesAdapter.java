package com.sterlex.in.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.sterlex.in.Activity.HomeActivity2;
import com.sterlex.in.Activity.OffersProductListActivity;
import com.sterlex.in.Model.BannerModel;
import com.sterlex.in.Model.ServicesOffersModel;
import com.sterlex.in.R;

import java.util.ArrayList;

public class HomeServicesAdapter extends SliderViewAdapter<HomeServicesAdapter.HomeSliderAdapterVH> {

    private Context context;
    ArrayList<String> imagesArrayList;
    ServicesOffersModel models;

    public HomeServicesAdapter(Context context, ArrayList<String> imagesArrayList, ServicesOffersModel model) {
        this.context = context;
        this.imagesArrayList = imagesArrayList;
        this.models = model;
    }

   /* public void addItem(BannerModel sliderItem) {
        this.bannerModelArrayList.add(sliderItem);
        notifyDataSetChanged();
    }*/


    @Override
    public HomeSliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new HomeSliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(HomeSliderAdapterVH viewHolder, int position) {
//        final BannerModel bannerModel = bannerModelArrayList.get(position);

        String image = imagesArrayList.get(position);

        Glide.with(viewHolder.itemView)
                .load(image)
                .into(viewHolder.imageViewBackground);
//
        viewHolder.imageViewBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!models.getOffer_id().equals("")){
                    Intent intent = new Intent(context, OffersProductListActivity.class);
                    intent.putExtra("offer_id", models.getOffer_id());
                    intent.putExtra("offer_name", models.getSection_text());
                    context.startActivity(intent);
                }
            }
        });

//        switch (position) {
//            case 0:
//                Glide.with(viewHolder.itemView)
//                        .load(R.drawable.refer_banner)
//                        .into(viewHolder.imageViewBackground);
//                break;
//            case 1:
//                Glide.with(viewHolder.itemView)
//                        .load(R.drawable.refer_banner)
//                        .into(viewHolder.imageViewBackground);
//                break;
//            case 2:
//                Glide.with(viewHolder.itemView)
//                        .load(R.drawable.refer_banner)
//                        .into(viewHolder.imageViewBackground);
//                break;
//            case 3:
//                Glide.with(viewHolder.itemView)
//                        .load(R.drawable.refer_banner)
//                        .into(viewHolder.imageViewBackground);
//                break;
//            default:
//                Glide.with(viewHolder.itemView)
//                        .load(R.drawable.refer_banner)
//                        .into(viewHolder.imageViewBackground);
//                break;
//
//        }
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return imagesArrayList.size();
    }

    class HomeSliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
//        TextView textViewDescription;

        public HomeSliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
//            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }
}
