package com.suncheng.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.suncheng.myapplication.DetailActivity;
import com.suncheng.myapplication.R;
import com.suncheng.myapplication.image.ImageHelper;
import com.suncheng.myapplication.model.Article;

import java.util.List;

/**
 * Created by clevo on 2015/7/27.
 */

public class MasonryAdapter extends RecyclerView.Adapter<MasonryAdapter.MasonryView>{
    private List<Article> products;

    public MasonryAdapter() {
    }

    public void setData(List<Article> list) {
        products = list;
    }

    @Override
    public MasonryView onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_item, viewGroup, false);
        return new MasonryView(view);
    }

    @Override
    public void onBindViewHolder(MasonryView masonryView, int position) {
        ImageHelper.getInstance().displayImage(products.get(position).getImgUrl(), masonryView.imageView, R.mipmap.ic_launcher);
        masonryView.title.setText(products.get(position).getTitle());
        masonryView.author.setText(products.get(position).getAuthor());
        masonryView.praise.setText(products.get(position).getPraise());
        masonryView.scan.setText(products.get(position).getScan());
    }


    @Override
    public int getItemCount() {
        if(products == null) {
            return 0;
        }
        return products.size();
    }

    public class MasonryView extends  RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView title, author, praise, scan;


       public MasonryView(View itemView){
           super(itemView);
           imageView= (ImageView) itemView.findViewById(R.id.image );
           title= (TextView) itemView.findViewById(R.id.title);
           author = (TextView) itemView.findViewById(R.id.author);
           praise = (TextView) itemView.findViewById(R.id.praise);
           scan = (TextView) itemView.findViewById(R.id.scan);
           itemView.setOnClickListener(this);
       }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Context context = v.getContext();
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("article", products.get(position));
            context.startActivity(intent);
        }
    }
}
