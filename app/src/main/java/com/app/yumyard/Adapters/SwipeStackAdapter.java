package com.app.yumyard.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.app.yumyard.R;

import java.util.List;

public class SwipeStackAdapter extends BaseAdapter {

    private List<Integer> mData;
    private Context context;

    public SwipeStackAdapter(List<Integer> data, Context context) {
        this.mData = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Integer getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.suggestion_item, parent, false);

        ImageView img = (ImageView) convertView.findViewById(R.id.img);
        img.setImageResource(mData.get(position));


        return convertView;
    }
}
