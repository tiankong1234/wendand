package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PreviewFragment extends Fragment implements View.OnClickListener{
    private View view;
    private  Data data;
    TextView titleview;
    TextView dataview;
    TextView contentview;
    CardView cardView;
    LinearLayout linearview;
    View colorview;
    Button buttontitle;
    Button buttoncontent;
    Button buttonborder;
    int titlecolor;
    int contentcolor;
    int bordercolor;
    Button button1;
    Button button2;
    Button button3;
    private FloatingActionButton floatingActionButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public PreviewFragment(Data data){
        this.data=data;
        this.titlecolor=data.getTitlecolor();
        this.contentcolor=data.getContentcolor();
        this.bordercolor=data.getBordercolor();
    }
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        view=getLayoutInflater().inflate(R.layout.preview_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        button1=view.findViewById(R.id.textView1);
        button2=view.findViewById(R.id.textView2);
        button3=view.findViewById(R.id.textView3);
        titleview=view.findViewById(R.id.title);
        dataview=view.findViewById(R.id.date);
        contentview=view.findViewById(R.id.content);
        cardView=view.findViewById(R.id.cardviewcolor);
        linearview=view.findViewById(R.id.linearcolor);
        colorview=view.findViewById(R.id.titlecolor);
        buttontitle=view.findViewById(R.id.button1);
        buttoncontent=view.findViewById(R.id.button2);
        buttonborder=view.findViewById(R.id.button3);
        floatingActionButton=view.findViewById(R.id.floatingActionButton);
        titleview.setText(data.getTitle());
        dataview.setText(data.getTypename());
        contentview.setText(data.getContent());
        cardView.setCardBackgroundColor(data.getBordercolor());
        linearview.setBackgroundColor(data.getBordercolor());
        colorview.setBackgroundColor(data.getTitlecolor());
        contentview.setBackgroundColor(data.getContentcolor());
        buttontitle.setBackgroundColor(data.getTitlecolor());
        buttoncontent.setBackgroundColor(data.getContentcolor());
        buttonborder.setBackgroundColor(data.getBordercolor());
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                data.setTitlecolor(titlecolor);
                data.setContentcolor(contentcolor);
                data.setBordercolor(bordercolor);
                intent.putExtra("data",data);
                getActivity().setResult(Activity.RESULT_OK,intent);
                getActivity().finish();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textView1:
                Intent intent1=new Intent(getActivity(),ChooseColorActivity.class);
                startActivityForResult(intent1,1);
                break;
            case R.id.textView2:
                Intent intent2=new Intent(getActivity(),ChooseColorActivity.class);
                startActivityForResult(intent2,2);
                break;
            case R.id.textView3:
                Intent intent3=new Intent(getActivity(),ChooseColorActivity.class);
                startActivityForResult(intent3,3);
                break;
            default:break;
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK) {
            int position=data.getIntExtra("position",0);
            int color=Constants.intcolors[position];
            switch (requestCode) {
                case 1:
                    buttontitle.setBackgroundColor(color);
                    colorview.setBackgroundColor(color);
                    titlecolor=color;
                    break;
                case 2:
                    buttoncontent.setBackgroundColor(color);
                    contentview.setBackgroundColor(color);
                    contentcolor=color;
                    break;
                case 3:
                    buttonborder.setBackgroundColor(color);
                    cardView.setCardBackgroundColor(color);
                    linearview.setBackgroundColor(color);
                    bordercolor=color;
                    break;
                default:break;
            }
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent=new Intent();
                data.setTitlecolor(titlecolor);
                data.setContentcolor(contentcolor);
                data.setBordercolor(bordercolor);
                intent.putExtra("data",data);
                getActivity().setResult(Activity.RESULT_OK,intent);
                getActivity().finish();
                break;
            default: break;
        }
        return true;
    }
}
