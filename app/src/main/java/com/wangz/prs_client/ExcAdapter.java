package com.wangz.prs_client;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExcAdapter extends RecyclerView.Adapter<ExcAdapter.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<HashMap<String,Object>> propertyList = new ArrayList<>();
    private DoExchangeClickListener mDoExchangeClickListener;
    public ExcAdapter(RecyclerView recyclerView) {
        this.mContext = recyclerView.getContext();
        this.mInflater = LayoutInflater.from(mContext);
    }
    public void setData(List<HashMap<String, Object>> propertyList){
        if(propertyList != null) {
            this.propertyList.clear();
            this.propertyList.addAll(propertyList);
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listview_manager, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.fucname.setText((String)propertyList.get(position).get("name"));
        holder.funicon.setImageResource((int)propertyList.get(position).get("icon"));
        holder.viewBtn.setText((String)propertyList.get(position).get("button"));
        final int pos= position;
        holder.viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDoExchangeClickListener != null) {
                    mDoExchangeClickListener.OnDoExchangeClickListener(pos);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return propertyList.size();
    }



    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(view, view.getTag());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView fucname;
        ImageView funicon;
        Button viewBtn;
        private ViewHolder(View view) {
            super(view);
            fucname = view.findViewById(R.id.functionName);
            funicon = view.findViewById(R.id.functionIcon);
            viewBtn = view.findViewById(R.id.functionButton);
        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, Object data);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface DoExchangeClickListener {
        void OnDoExchangeClickListener(int position);
    }

    public void setDoExchangeClickListener(DoExchangeClickListener listener) {
        this.mDoExchangeClickListener = listener;
    }


}
