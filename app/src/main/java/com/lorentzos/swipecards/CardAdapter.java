package com.lorentzos.swipecards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shall.tantandemo.R;

import java.util.List;

/**
 * Created by Shall on 2015-06-23.
 */
public class CardAdapter extends BaseAdapter {
    private Context mContext;
    private List<CardMode> mCardList;

    public CardAdapter(Context mContext, List<CardMode> mCardList) {
        this.mContext = mContext;
        this.mCardList = mCardList;
    }

    @Override
    public int getCount() {
        return mCardList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item, parent, false);
            holder = new ViewHolder();
            holder.mCardImageView = (ImageView) convertView.findViewById(R.id.helloText);
            holder.mCardName = (TextView) convertView.findViewById(R.id.card_name);
            holder.mCardImageNum = (TextView) convertView.findViewById(R.id.card_image_num);
            holder.mCardYear = (TextView) convertView.findViewById(R.id.card_year);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext)
                .load(mCardList.get(position).getImages().get(0))
                .into(holder.mCardImageView);
        holder.mCardName.setText(mCardList.get(position).getName());
        holder.mCardYear.setText(String.valueOf(mCardList.get(position).getYear()));
        return convertView;
    }

    class ViewHolder {
        TextView mCardName;
        TextView mCardYear;
        TextView mCardImageNum;
        ImageView mCardImageView;
    }
}
