package com.usf_mobile_dev.filmfriend.ui.recycler_adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.usf_mobile_dev.filmfriend.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StringRecyclerViewAdapter extends RecyclerView.Adapter
        <StringRecyclerViewAdapter.MyViewHolder>{

    Context mContext;
    ArrayList<String> stringList;

    public StringRecyclerViewAdapter(Context mContext, ArrayList<String> stringList) {
        this.mContext = mContext;
        this.stringList = stringList;

        Log.d("SET_STRING_LIST", stringList.toString());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_string_display, parent, false);
        MyViewHolder vHolder = new MyViewHolder(v);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (stringList != null && stringList.size() > 0) {
            holder.stringDisplay.setText(stringList.get(position));
        } else {
            // Covers the case of data not being ready yet.
            holder.stringDisplay.setText("");
        }
    }

    public void setStringList(ArrayList<String> stringList){
        this.stringList = stringList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (stringList != null)
            return stringList.size();
        else
            return 0;
    }

    public String getStringAtPosition(int position) {
        return stringList.get(position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView stringDisplay;

        public MyViewHolder(View itemView) {
            super(itemView);

            stringDisplay = (TextView)(itemView.findViewById(R.id.string_display_txtview));
            //stringDisplay = (TextView)itemView.findViewById(R.id.string_display_txtview);
        }
    }
}
