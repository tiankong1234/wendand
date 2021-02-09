package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ColorAdapter extends ArrayAdapter {
    private int resourceid;
    private int[] colorlist;
    private String[] namecolorlist;
    public ColorAdapter(Context context, int resource,int[] colorlist,String[] namecolors) {
        super(context, resource);
        this.colorlist=colorlist;
        this.resourceid=resource;
        this.namecolorlist=namecolors;
    }

    @Override
    public int getCount() {
        return colorlist.length;
    }



    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        View view= LayoutInflater.from(getContext()).inflate(resourceid,parent,false);
        TextView colortitle=view.findViewById(R.id.textView1);
        TextView nametitle=view.findViewById(R.id.textView2);
        colortitle.setBackgroundColor(colorlist[position]);
        nametitle.setText(namecolorlist[position]);
        return view;
    }
}
