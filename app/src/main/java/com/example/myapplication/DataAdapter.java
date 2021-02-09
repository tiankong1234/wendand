package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private Activity context;
    private List<Data> datalist;
    public DataAdapter(Activity context,List<Data> list){
        this.context=context;
        this.datalist=list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.datalistitem,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, int position) {
        Data data= datalist.get(position);
        holder.titleview.setText(data.getTitle());
        holder.dataview.setText(data.getTypename());
        holder.contentview.setText(data.getContent());
        holder.cardView.setCardBackgroundColor(data.getBordercolor());
        holder.linearview.setBackgroundColor(data.getBordercolor());
        holder.colorview.setBackgroundColor(data.getTitlecolor());
        holder.contentview.setBackgroundColor(data.getContentcolor());
        holder.mainview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,EditDataActivity.class);
                intent.putExtra("data",data);
                intent.putExtra("position",position);
                context.startActivityForResult(intent,Constants.EDIT_DATA_REQUEST);
            }
        });

    }
    @Override
    public int getItemCount() {
        return datalist.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        View mainview;
        TextView titleview;
        TextView dataview;
        TextView contentview;
        CardView cardView;
        LinearLayout linearview;
        View colorview;
        public ViewHolder(View view) {
            super(view);
            mainview=view;
            titleview=view.findViewById(R.id.title);
            dataview=view.findViewById(R.id.date);
            contentview=view.findViewById(R.id.content);
            cardView=view.findViewById(R.id.cardviewcolor);
            linearview=view.findViewById(R.id.linearcolor);
            colorview=view.findViewById(R.id.titlecolor);
        }
    }
}
