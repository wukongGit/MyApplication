package com.suncheng.myapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.suncheng.myapplication.R;
import com.suncheng.myapplication.image.ImageHelper;

import java.util.List;

/**
 * Created by clevo on 2015/7/27.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MasonryView>{
    private List<String> products;

    public ImageAdapter() {
    }

    public void setData(List<String> list) {
        products = list;
    }

    @Override
    public MasonryView onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_item, viewGroup, false);
        return new MasonryView(view);
    }

    @Override
    public void onBindViewHolder(MasonryView masonryView, int position) {
        ImageHelper.getInstance().displayImage(products.get(position), masonryView.imageView, R.mipmap.ic_launcher);
    }


    @Override
    public int getItemCount() {
        if(products == null) {
            return 0;
        }
        return products.size();
    }

    public class MasonryView extends  RecyclerView.ViewHolder {

        private ImageView imageView;

       public MasonryView(View itemView){
           super(itemView);
           imageView= (ImageView) itemView.findViewById(R.id.image);
           itemView.findViewById(R.id.bottom).setVisibility(View.GONE);
       }
    }
}
