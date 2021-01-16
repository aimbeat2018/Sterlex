package com.sterlex.in.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sterlex.in.Activity.CategoryWiseProductListActivity;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.sterlex.in.Model.BannerModel;
import com.sterlex.in.R;

import java.util.ArrayList;

public class HomeSliderAdapter extends SliderViewAdapter<HomeSliderAdapter.HomeSliderAdapterVH> {

    private Context context;
    ArrayList<BannerModel> bannerModelArrayList;

    public HomeSliderAdapter(Context context,ArrayList<BannerModel> bannerModelArrayList) {
        this.context = context;
        this.bannerModelArrayList = bannerModelArrayList;
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
        final BannerModel bannerModel = bannerModelArrayList.get(position);


        Glide.with(viewHolder.itemView)
                .load(bannerModel.getBanner())
                .into(viewHolder.imageViewBackground);
//
        viewHolder.imageViewBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!bannerModel.getCategory_id().equals("")){
                    Intent intent = new Intent(context, CategoryWiseProductListActivity.class);
                    intent.putExtra("sub_cat_id", bannerModel.getCategory_id());
                    intent.putExtra("sub_cat_name", bannerModel.getSub_category());
                    intent.putExtra("from", "cat");
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
        return bannerModelArrayList.size();
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
