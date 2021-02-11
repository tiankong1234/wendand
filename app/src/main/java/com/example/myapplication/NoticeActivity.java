package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;

public class NoticeActivity extends AppCompatActivity {
    private TextView titletext;
    private CardView addcard;
    private TextView content;
    private TextView typename;
    private TextView timedate;
    private Data data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        titletext=findViewById(R.id.titletext);
        addcard=findViewById(R.id.addcard);
        content=findViewById(R.id.contenttext);
        typename=findViewById(R.id.typename);
        timedate=findViewById(R.id.timedate);
        Bundle bundle=getIntent().getExtras();
        data=(Data)bundle.getSerializable("data");
        titletext.setText(data.getTitle());
        addcard.setCardBackgroundColor(data.getBordercolor());
        content.setText(data.getContent());
        typename.setText(data.getTypename());
        timedate.setText(data.getStartdatestr());
        startService(new Intent(this,MyService.class));
    }

}