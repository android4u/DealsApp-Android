package com.example.androiddealsapp;

import java.util.ArrayList;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DealsAdapter extends ArrayAdapter<Deal> {
	
    private Context context;

    public DealsAdapter(Context context, int textViewResourceId, ArrayList<Deal> items) {
        super(context, textViewResourceId, items);
        this.context=context;
       
    }
    
    private class ViewHolder{
    	ImageView imageView;
    	TextView txtId;
    	TextView txtTitle;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Deal dealItem = getItem(position);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
              
        if (convertView == null) {
        	convertView = inflater.inflate(R.layout.custom_listview, null);
        	holder = new ViewHolder();
        	holder.txtTitle = (TextView) convertView.findViewById(R.id.shortAnnouncementtitle);
        	holder.txtId = (TextView) convertView.findViewById(R.id.nameId);
        	//holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
        	convertView.setTag(holder);
        }else{
        	holder=(ViewHolder)convertView.getTag();
        }
        holder.txtId.setText(dealItem.getId());
        holder.txtTitle.setText(dealItem.getShortAnnouncementTitle());
       // holder.imageView.setImage(dealItem.getSmallImageUrl());

       return convertView;
    }
}
