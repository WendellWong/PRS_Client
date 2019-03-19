package com.wangz.prs_client;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ExcAdapter extends ArrayAdapter<UserProperty> {
    private int resourceId;
    public ExcAdapter(@NonNull Context context, int textViewResourceId, List<UserProperty> objects) {
        super(context, textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        UserProperty userProperty=getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView propertyImage = (ImageView)view.findViewById(R.id.functionIcon) ;
        TextView propertyText = (TextView)view.findViewById(R.id.functionName);
        TextView buttonText = (TextView)view.findViewById(R.id.functionButton);
        propertyImage.setImageResource(R.drawable.avatar);
        propertyText.setText(userProperty.getName());
        buttonText.setText(R.string.doexchange);
        return view;
    }

}
