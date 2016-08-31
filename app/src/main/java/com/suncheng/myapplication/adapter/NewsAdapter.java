package com.suncheng.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.suncheng.myapplication.R;
import com.suncheng.myapplication.model.Article;

import java.util.List;

/**
 * Created by suncheng on 2016/8/31.
 */
public class NewsAdapter extends BaseAdapter {
    LayoutInflater mInflater = null;
    List<Article> mList = null;

    public NewsAdapter(Context context){
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<Article> list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.news_item, null);
            holder.title = (TextView)convertView.findViewById(R.id.title);
            holder.content = (TextView)convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.title.setText(mList.get(position).getTitle());
        holder.content.setText(mList.get(position).getContent());
        return convertView;
    }

    class ViewHolder {
        TextView title;
        TextView content;
    }
}
